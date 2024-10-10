import java.io.*
import java.nio.file.*
import java.util.*

object _50A_py {
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
                            // Split the first line by spaces and parse the numbers

                            val numbers: Array<String> = inputLines[0].trim().split("\\s+")
                            if (numbers.size >= 2) {
                                val num1: Int = Integer.parseInt(numbers[0])
                                val num2: Int = Integer.parseInt(numbers[1])
                                val result = (num1 * num2) / 2

                                /******************************************** End of Answer code  */
                                Utils.commonFunction(outputFilePath, result, i)
                            } else {
                                System.err.println("Not enough numbers in input file: $inputFilePath")
                                System.exit(1)
                            }
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