import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestRefactorerV2 {
    
    private static final Pattern SECTION_HEADER_PATTERN = Pattern.compile("^(\\s*)//\\s+([-=]+)\\s*(.+?)\\s*[-=]*\\s*$");
    private static final Pattern TEST_METHOD_PATTERN = Pattern.compile("^\\s*@Test\\b");
    private static final Pattern CLASS_PATTERN = Pattern.compile("^(\\s*)(public|protected)\\s+class\\s+(\\w+)");
    
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java TestRefactorerV2 <test-file-path>");
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
        String className = "";
        for (int i = 0; i < lines.size(); i++) {
            Matcher m = CLASS_PATTERN.matcher(lines.get(i));
            if (m.find()) {
                classLineIndex = i;
                classIndent = m.group(1);
                className = m.group(3);
                break;
            }
        }
        
        if (classLineIndex == -1) {
            return content; // No class found
        }
        
        // Find the closing brace of the class (simple approach: last non-empty line)
        int classEndIndex = lines.size() - 1;
        while (classEndIndex > classLineIndex && lines.get(classEndIndex).trim().isEmpty()) {
            classEndIndex--;
        }
        if (classEndIndex > classLineIndex && lines.get(classEndIndex).trim().equals("}")) {
            // This is likely the class closing brace
        } else {
            return content; // Can't find class end
        }
        
        // Parse sections within the class body
        List<Section> sections = new ArrayList<>();
        Section currentSection = null;
        
        for (int i = classLineIndex + 1; i < classEndIndex; i++) {
            String line = lines.get(i);
            
            // Check for section header
            Matcher sectionMatcher = SECTION_HEADER_PATTERN.matcher(line);
            if (sectionMatcher.matches()) {
                String sectionName = sectionMatcher.group(3).trim();
                
                // Save previous section if it exists
                if (currentSection != null && currentSection.hasTestMethods()) {
                    sections.add(currentSection);
                }
                
                // Start new section
                currentSection = new Section(sectionName, line, i);
                continue;
            }
            
            // Add line to current section
            if (currentSection != null) {
                currentSection.addLine(line, i);
            }
        }
        
        // Add last section if it has tests
        if (currentSection != null && currentSection.hasTestMethods()) {
            sections.add(currentSection);
        }
        
        // If no sections with tests found, return original
        if (sections.isEmpty()) {
            return content;
        }
        
        // Remove section lines and their test methods from original
        Set<Integer> linesToRemove = new HashSet<>();
        for (Section section : sections) {
            linesToRemove.add(section.headerLineIndex);
            linesToRemove.addAll(section.contentLineIndices);
        }
        
        // Build the result: keep all lines except those being moved
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= classEndIndex; i++) {
            if (i < classEndIndex && !linesToRemove.contains(i)) {
                result.add(lines.get(i));
            }
        }
        
        // Add nested classes before the closing brace
        String nestedIndent = classIndent + "    ";
        for (Section section : sections) {
            // Add blank line before nested class
            result.add("");
            result.add(nestedIndent + "@org.junit.jupiter.api.Nested");
            result.add(nestedIndent + "class " + sanitizeClassName(section.name) + " {");
            
            // Add section content with extra indentation
            for (String line : section.contentLines) {
                result.add("    " + line);
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
        
        // If empty or starts with digit, prepend "Test"
        if (className.isEmpty() || Character.isDigit(className.charAt(0))) {
            className = "Test" + className;
        }
        
        return className;
    }
    
    static class Section {
        String name;
        String headerLine;
        int headerLineIndex;
        List<String> contentLines = new ArrayList<>();
        List<Integer> contentLineIndices = new ArrayList<>();
        
        Section(String name, String headerLine, int headerLineIndex) {
            this.name = name;
            this.headerLine = headerLine;
            this.headerLineIndex = headerLineIndex;
        }
        
        void addLine(String line, int index) {
            contentLines.add(line);
            contentLineIndices.add(index);
        }
        
        boolean hasTestMethods() {
            for (String line : contentLines) {
                if (TEST_METHOD_PATTERN.matcher(line).find()) {
                    return true;
                }
            }
            return false;
        }
    }
}
