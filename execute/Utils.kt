import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List

object Utils {
    fun main(args: Array<String?>?) {
    }

    @Throws(IOException::class)
    fun commonFunction(outputFilePath: String?, result: Int, i: Int) {
        // Read contents from the output file
        val outputLines: List<String> = Files.readAllLines(Paths.get(outputFilePath))
        var found = false

        // Compare the computed result with each line in the output file
        for (line in outputLines) {
            if (line.trim().equals(String.valueOf(result))) {
                found = true
                break
            }
        }

        // String multilineString = """
        //         This is a multiline string.
        //         It can span multiple lines.
        //         Each line is preserved as written.
        //         """;

        // System.out.println(multilineString);

        // Output result of the comparison
        if (found) System.out.println("$i:true")
        else {
            System.out.println("$i:false")
            System.exit(1)
        }
    }
}