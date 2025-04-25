import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow

object _50B_py {
    @JvmStatic
    fun main(args: Array<String>) {
        val currentFileName = Exception().stackTrace[0].className
        val dirPath = "../input/$currentFileName"
        val dir = File(dirPath)

        println("/******************************************  $currentFileName  ******************************************/")

        if (dir.exists() && dir.isDirectory) {
            dir.listFiles(File::isFile)?.let { files ->
                for (i in files.indices) {
                    val inputFilePath = "$dirPath/input${currentFileName}_$i.txt"
                    val outputFilePath = "../output/$currentFileName/output${currentFileName}_$i.txt"

                    try {
                        val inputLines = Files.readAllLines(Paths.get(inputFilePath))
                        if (inputLines.isNotEmpty()) {

                            /******************************************** Answer code ************************************************/
                            val freq = IntArray(128)

                            // Iterate through each string in the inputLines
                            for (line in inputLines) {
                                for (c in line) {
                                    freq[c.code]++
                                }
                            }
                            var result = 0
                            for (count in freq) {
                                if (count > 0) {
                                    result += count.toDouble().pow(2.0).toInt()
                                }
                            }
                            result = result.toInt()

                            /******************************************** End of Answer code ********************************************/
                            Utils.commonFunction(outputFilePath, result, i)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: NumberFormatException) {
                        System.err.println("Invalid number format in file: $inputFilePath")
                        System.exit(1)
                    }
                }
            }
        }
    }
}