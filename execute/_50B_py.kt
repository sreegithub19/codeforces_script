import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object _50B_py {
    fun main() {
        val currentFileName = Exception().stackTrace[0].className
        val dirPath = "../input/$currentFileName"
        val dir = File(dirPath)

        println("/******************************************  $currentFileName  ******************************************/")

        if (dir.exists() && dir.isDirectory) {
            val inputFiles = dir.listFiles { file -> file.isFile }
            if (inputFiles != null) {
                for (i in inputFiles.indices) {
                    // Use currentFileName directly without any potential naming issues
                    val inputFilePath = "$dirPath/input_${currentFileName}_$i.txt"
                    val outputFilePath = "../output/$currentFileName/output_${currentFileName}_$i.txt"

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
                                    result += count * count
                                }
                            }

                            /******************************************** End of Answer code ********************************************/
                            Utils.commonFunction(outputFilePath, result, i)
                        } else {
                            System.err.println("Not enough numbers in input file: $inputFilePath")
                            System.exit(1)
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