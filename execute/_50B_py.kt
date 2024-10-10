import java.io.*
import java.nio.file.*
import java.util.*

object _50B_py {
    fun main(args: Array<String?>?) {
        val currentFileName: String = Exception().getStackTrace().get(0).getClassName()
        val dirPath = "../input/$currentFileName"
        val dir: File = File(dirPath)

        System.out.println("/******************************************  $currentFileName  ******************************************/")

        if (dir.exists() && dir.isDirectory()) {
            if (dir.listFiles(File::isFile) != null) {
                for (i in 0 until dir.listFiles(File::isFile).length) {
                    val inputFilePath = dirPath.toString() + "/input" + currentFileName + "_" + i + ".txt"
                    val outputFilePath = "../output/" + currentFileName + "/output" + currentFileName + "_" + i + ".txt"

                    try {
                        val inputLines: List<String> = Files.readAllLines(Paths.get(inputFilePath))
                        if (!inputLines.isEmpty()) {
                            /******************************************** Answer code  */

                            val freq = IntArray(128)


                            // Iterate through each string in the inputLines
                            for (line in inputLines) {
                                for (c in line.toCharArray()) {
                                    freq[c.code]++
                                }
                            }
                            var result = 0
                            for (count in freq) {
                                if (count > 0) {
                                    result += Math.pow(count, 2)
                                }
                            }
                            result = result

                            /******************************************** End of Answer code  */
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
