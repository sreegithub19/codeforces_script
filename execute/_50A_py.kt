import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object _50A_py {
    @JvmStatic
    fun main(args: Array<String>) {
        val currentFileName = Exception().stackTrace[0].className
        val dirPath = "../input/$currentFileName"
        val dir = File(dirPath)

        println("/******************************************  $currentFileName  ******************************************/")

        if (dir.exists() && dir.isDirectory) {
            val files = dir.listFiles { file -> file.isFile }
            files?.forEachIndexed { index, _ ->
                val inputFilePath = "$dirPath/input$currentFileName_$index.txt"
                val outputFilePath = "../output/$currentFileName/output$currentFileName_$index.txt"

                try {
                    val inputLines = Files.readAllLines(Paths.get(inputFilePath))
                    if (inputLines.isNotEmpty()) {
                        // Split the first line by spaces and parse the numbers
                        val numbers = inputLines[0].trim().split("\\s+".toRegex())
                        if (numbers.size >= 2) {
                            val num1 = numbers[0].toInt()
                            val num2 = numbers[1].toInt()
                            val result = (num1 * num2) / 2

                            // Replace Utils.commonFunction with your output handling logic
                            Utils.commonFunction(outputFilePath, result, index)
                        } else {
                            println("Not enough numbers in input file: $inputFilePath")
                            System.exit(1)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    println("Invalid number format in file: $inputFilePath")
                    System.exit(1)
                }
            }
        }
    }
}
