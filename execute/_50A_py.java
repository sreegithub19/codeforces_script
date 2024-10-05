import java.io.*;
import java.nio.file.*;
import java.util.*;

public class _50A_py {
    public static void main(String[] args) {
        String currentFileName = new Exception().getStackTrace()[0].getClassName(), dirPath = "../input/" + currentFileName;
        File dir = new File(dirPath);

        System.out.println("/******************************************  "  + currentFileName + "  ******************************************/");

        if (dir.exists() && dir.isDirectory()) {
            if (dir.listFiles(File::isFile) != null) {
                for (int i = 0; i < dir.listFiles(File::isFile).length; i++) {
                    String inputFilePath = dirPath + "/input" + currentFileName + "_" + i + ".txt";
                    String outputFilePath = "../output/" + currentFileName + "/output" + currentFileName + "_" + i + ".txt";

                    try {
                        List<String> inputLines = Files.readAllLines(Paths.get(inputFilePath));
                        if (!inputLines.isEmpty()) {

  /******************************************** Answer code ************************************************/
                            // Split the first line by spaces and parse the numbers
                            String[] numbers = inputLines.get(0).trim().split("\\s+");
                            if (numbers.length >= 2) {
                                int num1 = Integer.parseInt(numbers[0]);
                                int num2 = Integer.parseInt(numbers[1]);
                                int result = (num1 * num2) / 2;

 /******************************************** End of Answer code ********************************************/
                                Utils.commonFunction(outputFilePath , result , i);
                            } else {
                                System.err.println("Not enough numbers in input file: " + inputFilePath);
                                System.exit(1);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in file: " + inputFilePath);
                        System.exit(1);
                    }
                }
            }
        }
    }
}
