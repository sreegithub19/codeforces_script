import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object Utils {
    @JvmStatic
    fun main(args: Array<String>) {}

    @Throws(IOException::class)
    fun commonFunction(outputFilePath: String, result: Int, i: Int) {
        // Read contents from the output file
        val outputLines = Files.readAllLines(Paths.get(outputFilePath))
        var found = false

        // Compare the computed result with each line in the output file
        for (line in outputLines) {
            if (line.trim() == result.toString()) {
                found = true
                break
            }
        }

        // Output result of the comparison
        if (found) {
            println("$i:true")
        } else {
            println("$i:false")
            System.exit(1)
        }
    }
}