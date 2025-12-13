import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestRefactorerV3 {
    
    private static final Pattern SECTION_HEADER_PATTERN = Pattern.compile("^(\\s*)//\\s+([-=]+)\\s*(.+?)\\s*[-=]*\\s*$");
    private static final Pattern TEST_ANNOTATION_PATTERN = Pattern.compile("^\\s*@Test\\b");
    private static final Pattern METHOD_START_PATTERN = Pattern.compile("^\\s*(public|private|protected)\\s+");
    private static final Pattern CLASS_PATTERN = Pattern.compile("^(\\s*)(public|protected)\\s+class\\s+(\\w+)");
    
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java TestRefactorerV3 <test-file-path>");
            System.exit(1);
        }
        
        String filePath = args[0];
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            System.err.println("File not found: " + filePath);
            System.exit(1);
        }
        
        String content = new String(Files.readAllBytes(path));
        String refactored = refactorTest(content);
        
        // Write back to the original file
        Files.write(path, refactored.getBytes());
        System.out.println("Refactored: " + filePath);
    }
    
    public static String refactorTest(String content) {
        List<String> lines = new ArrayList<>(Arrays.asList(content.split("\n", -1)));
        
        // Find the class declaration
        int classLineIndex = -1;
        String classIndent = "";
        for (int i = 0; i < lines.size(); i++) {
            Matcher m = CLASS_PATTERN.matcher(lines.get(i));
            if (m.find()) {
                classLineIndex = i;
                classIndent = m.group(1);
                break;
            }
        }
        
        if (classLineIndex == -1) {
            return content; // No class found
        }
        
        // Find the closing brace of the class by counting braces
        int braceCount = 0;
        int classEndIndex = -1;
        for (int i = classLineIndex; i < lines.size(); i++) {
            String line = lines.get(i);
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        classEndIndex = i;
                        break;
                    }
                }
            }
            if (classEndIndex != -1) break;
        }
        
        if (classEndIndex == -1 || classEndIndex <= classLineIndex) {
            return content;
        }
        
        // Parse sections and identify test methods
        Map<String, Section> sections = new LinkedHashMap<>();
        String currentSectionName = null;
        
        for (int i = classLineIndex + 1; i < classEndIndex; i++) {
            String line = lines.get(i);
            
            // Check for section header
            Matcher sectionMatcher = SECTION_HEADER_PATTERN.matcher(line);
            if (sectionMatcher.matches()) {
                String sectionName = sectionMatcher.group(3).trim();
                currentSectionName = sectionName;
                
                if (!sections.containsKey(sectionName)) {
                    sections.put(sectionName, new Section(sectionName));
                }
                sections.get(sectionName).headerLines.add(i);
            }
        }
        
        if (sections.isEmpty()) {
            return content; // No sections found
        }
        
        // Identify test methods and their bounds
        List<TestMethod> testMethods = new ArrayList<>();
        for (int i = classLineIndex + 1; i < classEndIndex; i++) {
            String line = lines.get(i);
            
            if (TEST_ANNOTATION_PATTERN.matcher(line).find()) {
                // Found a @Test annotation, find the method start (including all annotations/modifiers)
                int methodStart = findMethodStart(lines, i, classLineIndex + 1);
                int methodEnd = findMethodEnd(lines, i + 1, classEndIndex);
                
                // Determine which section this test belongs to
                String sectionName = findSectionForLine(sections, i);
                
                if (sectionName != null && methodEnd > methodStart) {
                    testMethods.add(new TestMethod(methodStart, methodEnd, sectionName));
                }
            }
        }
        
        // Group test methods by section
        Map<String, List<TestMethod>> methodsBySection = new LinkedHashMap<>();
        for (TestMethod tm : testMethods) {
            methodsBySection.computeIfAbsent(tm.sectionName, k -> new ArrayList<>()).add(tm);
        }
        
        // Remove sections with no test methods
        methodsBySection.keySet().retainAll(sections.keySet());
        if (methodsBySection.isEmpty()) {
            return content; // No test methods in sections
        }
        
        // Build the result
        Set<Integer> linesToRemove = new HashSet<>();
        for (TestMethod tm : testMethods) {
            for (int i = tm.startLine; i <= tm.endLine; i++) {
                linesToRemove.add(i);
            }
        }
        for (Section section : sections.values()) {
            if (methodsBySection.containsKey(section.name)) {
                linesToRemove.addAll(section.headerLines);
            }
        }
        
        // Build result: keep all lines except those being moved
        List<String> result = new ArrayList<>();
        for (int i = 0; i < classEndIndex; i++) {
            if (!linesToRemove.contains(i)) {
                result.add(lines.get(i));
            }
        }
        
        // Add nested classes before the closing brace
        String nestedIndent = classIndent + "    ";
        // Track used class names to avoid duplicates
        Map<String, Integer> classNameCounts = new LinkedHashMap<>();
        
        for (Map.Entry<String, List<TestMethod>> entry : methodsBySection.entrySet()) {
            String sectionName = entry.getKey();
            List<TestMethod> methods = entry.getValue();
            
            // Skip if no methods
            if (methods.isEmpty()) {
                continue;
            }
            
            // Generate unique class name
            String className = sanitizeClassName(sectionName) + "Tests";
            int count = classNameCounts.getOrDefault(className, 0);
            classNameCounts.put(className, count + 1);
            if (count > 0) {
                className = className + count;
            }
            
            result.add("");
            result.add(nestedIndent + "@org.junit.jupiter.api.Nested");
            result.add(nestedIndent + "class " + className + " {");
            
            // Add test methods
            for (TestMethod tm : methods) {
                result.add("");
                for (int i = tm.startLine; i <= tm.endLine; i++) {
                    result.add("        " + lines.get(i));
                }
            }
            
            result.add(nestedIndent + "}");
        }
        
        // Add closing brace
        result.add(lines.get(classEndIndex));
        
        // Add any remaining lines after class
        for (int i = classEndIndex + 1; i < lines.size(); i++) {
            result.add(lines.get(i));
        }
        
        return String.join("\n", result);
    }
    
    private static int findMethodStart(List<String> lines, int testAnnotationLine, int classBodyStart) {
        // Go backwards from @Test to find any preceding annotations or blank lines
        int start = testAnnotationLine;
        for (int i = testAnnotationLine - 1; i >= classBodyStart; i--) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("@") || line.startsWith("//")) {
                start = i;
            } else {
                break;
            }
        }
        return start;
    }
    
    private static int findMethodEnd(List<String> lines, int start, int limit) {
        int braceCount = 0;
        boolean inMethod = false;
        
        for (int i = start; i < limit; i++) {
            String line = lines.get(i);
            
            // Count braces
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    braceCount++;
                    inMethod = true;
                } else if (c == '}') {
                    braceCount--;
                    if (inMethod && braceCount == 0) {
                        return i;
                    }
                }
            }
        }
        
        return start; // Method not properly closed
    }
    
    private static String findSectionForLine(Map<String, Section> sections, int lineIndex) {
        String closestSection = null;
        int closestDistance = Integer.MAX_VALUE;
        
        for (Section section : sections.values()) {
            for (int headerLine : section.headerLines) {
                if (headerLine < lineIndex && lineIndex - headerLine < closestDistance) {
                    closestDistance = lineIndex - headerLine;
                    closestSection = section.name;
                }
            }
        }
        
        return closestSection;
    }
    
    private static String sanitizeClassName(String name) {
        // Remove common suffixes like "()" 
        name = name.replaceAll("\\(\\)$", "");
        name = name.replaceAll("\\(.*?\\)$", "");
        
        // Convert to PascalCase
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            } else if (c == '_' || c == '-' || c == ' ' || c == '.') {
                capitalizeNext = true;
            }
        }
        
        String className = result.toString();
        
        // Ensure it starts with a capital letter
        if (className.length() > 0 && !Character.isUpperCase(className.charAt(0))) {
            className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
        }
        
        // If empty or starts with digit, prepend "Section"
        if (className.isEmpty() || Character.isDigit(className.charAt(0))) {
            className = "Section" + className;
        }
        
        return className;
    }
    
    static class Section {
        String name;
        List<Integer> headerLines = new ArrayList<>();
        
        Section(String name) {
            this.name = name;
        }
    }
    
    static class TestMethod {
        int startLine;
        int endLine;
        String sectionName;
        
        TestMethod(int startLine, int endLine, String sectionName) {
            this.startLine = startLine;
            this.endLine = endLine;
            this.sectionName = sectionName;
        }
    }
}
