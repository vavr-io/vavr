import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestRefactorerDebug {
    
    private static final Pattern SECTION_HEADER_PATTERN = Pattern.compile("^(\\s*)//\\s+([-=]+)\\s*(.+?)\\s*[-=]*\\s*$");
    private static final Pattern TEST_METHOD_PATTERN = Pattern.compile("^\\s*@Test\\b");
    
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        Path path = Paths.get(filePath);
        String content = new String(Files.readAllBytes(path));
        List<String> lines = Arrays.asList(content.split("\n"));
        
        System.out.println("Looking for section headers...");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher m = SECTION_HEADER_PATTERN.matcher(line);
            if (m.matches()) {
                String sectionName = m.group(3).trim();
                System.out.println("Line " + i + ": Found section '" + sectionName + "'");
                
                // Check following lines for test methods
                boolean hasTests = false;
                for (int j = i + 1; j < Math.min(i + 50, lines.size()); j++) {
                    if (TEST_METHOD_PATTERN.matcher(lines.get(j)).find()) {
                        hasTests = true;
                        System.out.println("  -> Has test method at line " + j);
                        break;
                    }
                    // Stop at next section
                    if (SECTION_HEADER_PATTERN.matcher(lines.get(j)).matches()) {
                        break;
                    }
                }
                if (!hasTests) {
                    System.out.println("  -> No test methods found");
                }
            }
        }
    }
}
