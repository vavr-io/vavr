import java.util.regex.*;

public class test_pattern {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("^(\\s*)//\\s+([-=]+)\\s*(.+?)\\s*[-=]*\\s*$");
        String[] tests = {
            "    // - toString",
            "    // -- helpers",
            "    // -- transform()",
            "    //-- test"
        };
        
        for (String test : tests) {
            Matcher m = p.matcher(test);
            System.out.println("Testing: '" + test + "'");
            if (m.matches()) {
                System.out.println("  Matches! Groups: '" + m.group(1) + "', '" + m.group(2) + "', '" + m.group(3) + "'");
            } else {
                System.out.println("  No match");
            }
        }
    }
}
