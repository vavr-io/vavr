import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestRefactorer {
    
    private static final Pattern SECTION_HEADER_PATTERN = Pattern.compile("^(\\s*)//\\s+([-=]+)\\s*(.+?)\\s*[-=]*\\s*$");
    private static final Pattern TEST_METHOD_PATTERN = Pattern.compile("^\\s*@Test\\b");
    private static final Pattern METHOD_START_PATTERN = Pattern.compile("^\\s*(public|private|protected)\\s+");
    
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java TestRefactorer <test-file-path>");
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
        
        // Write to a new file for review
        String outputPath = filePath.replace(".java", "_refactored.java");
        Files.write(Paths.get(outputPath), refactored.getBytes());
        System.out.println("Refactored file written to: " + outputPath);
    }
    
    public static String refactorTest(String content) {
        List<String> lines = Arrays.asList(content.split("\n"));
        
        // Find the class declaration
        int classLineIndex = -1;
        String classIndent = "";
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.matches(".*\\b(public|protected)\\s+class\\s+\\w+.*")) {
                classLineIndex = i;
                classIndent = getIndent(line);
                break;
            }
        }
        
        if (classLineIndex == -1) {
            return content; // No class found
        }
        
        // Parse sections and their test methods
        List<Section> sections = new ArrayList<>();
        Section currentSection = null;
        List<String> beforeFirstSection = new ArrayList<>();
        boolean inClassBody = false;
        int braceDepth = 0;
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            
            // Track when we enter the class body
            if (i == classLineIndex) {
                inClassBody = true;
                beforeFirstSection.add(line);
                braceDepth += countChar(line, '{') - countChar(line, '}');
                continue;
            }
            
            if (!inClassBody) {
                beforeFirstSection.add(line);
                continue;
            }
            
            braceDepth += countChar(line, '{') - countChar(line, '}');
            
            // Check if we've exited the class
            if (braceDepth <= 0) {
                // We've reached the end of the class
                if (currentSection != null) {
                    sections.add(currentSection);
                }
                break;
            }
            
            // Check for section header
            Matcher sectionMatcher = SECTION_HEADER_PATTERN.matcher(line);
            if (sectionMatcher.matches() && braceDepth == 1) {
                String separators = sectionMatcher.group(2);
                String sectionName = sectionMatcher.group(3).trim();
                
                // Only process if it has dashes/equals and is not too long (likely a real section)
                if (separators.length() >= 2 && sectionName.length() < 50) {
                    // Save previous section
                    if (currentSection != null) {
                        sections.add(currentSection);
                    }
                    
                    // Start new section
                    currentSection = new Section(sectionName, i);
                    continue;
                }
            }
            
            // Add line to appropriate section or beforeFirstSection
            if (currentSection == null) {
                beforeFirstSection.add(line);
            } else {
                currentSection.lines.add(line);
            }
        }
        
        // Add last section if any
        if (currentSection != null) {
            sections.add(currentSection);
        }
        
        // If no sections found, return original
        if (sections.isEmpty()) {
            return content;
        }
        
        // Filter sections that actually contain test methods
        sections = sections.stream()
            .filter(s -> s.hasTestMethods())
            .collect(Collectors.toList());
        
        if (sections.isEmpty()) {
            return content;
        }
        
        // Build refactored content
        StringBuilder result = new StringBuilder();
        
        // Add everything before first section
        for (String line : beforeFirstSection) {
            result.append(line).append("\n");
        }
        
        // Add nested classes for each section
        String nestedIndent = classIndent + "    ";
        for (Section section : sections) {
            result.append("\n");
            result.append(nestedIndent).append("@org.junit.jupiter.api.Nested\n");
            result.append(nestedIndent).append("class ").append(sanitizeClassName(section.name)).append(" {\n");
            
            // Add section content with extra indentation
            for (String line : section.lines) {
                if (!line.trim().isEmpty() || section.lines.indexOf(line) > 0) {
                    result.append("    ").append(line).append("\n");
                }
            }
            
            result.append(nestedIndent).append("}\n");
        }
        
        result.append(classIndent).append("}\n");
        
        return result.toString();
    }
    
    private static String getIndent(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ' || c == '\t') {
                count++;
            } else {
                break;
            }
        }
        return line.substring(0, count);
    }
    
    private static int countChar(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
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
        int startLine;
        List<String> lines = new ArrayList<>();
        
        Section(String name, int startLine) {
            this.name = name;
            this.startLine = startLine;
        }
        
        boolean hasTestMethods() {
            boolean foundTest = false;
            for (String line : lines) {
                if (TEST_METHOD_PATTERN.matcher(line).find()) {
                    foundTest = true;
                    break;
                }
            }
            return foundTest;
        }
    }
}
