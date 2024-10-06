import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {
    public static void main(String[] args) {
    }
    
    public static void commonFunction(String outputFilePath ,int result , int i) throws IOException {
         // Read contents from the output file
        List<String> outputLines = Files.readAllLines(Paths.get(outputFilePath));
        boolean found = false;

        // Compare the computed result with each line in the output file
        for (String line : outputLines) {
            if (line.trim().equals(String.valueOf(result))) {
                found = true;
                break;
            }
        }

        // String multilineString = """
        //         This is a multiline string.
        //         It can span multiple lines.
        //         Each line is preserved as written.
        //         """;

        // System.out.println(multilineString);

        // Output result of the comparison
        if (found) System.out.println(i + ":true");
        else {
            System.out.println(i + ":false");
            System.exit(1);
        }
    }
}