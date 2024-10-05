import java.io.*;
import java.nio.file.*;
import java.util.*;

public class _50B_py {
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
                            
                                int freq[] = new int[128]; 

                                // Iterate through each string in the inputLines
                                for (String line : inputLines) {
                                    for (char c : line.toCharArray()) {
                                        freq[c]++;
                                    }
                                }
                                int result = 0;
                                for (int count : freq) {
                                    if (count > 0) {
                                        result += Math.pow(count, 2);
                                    }
                                }
                                result = (int) result;

 /******************************************** End of Answer code ********************************************/
                                Utils.commonFunction(outputFilePath , result , i);
                            } else {
                                System.err.println("Not enough numbers in input file: " + inputFilePath);
                                System.exit(1);
                            }
                        }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (NumberFormatException e) {
                        System.err.println("Invalid number format in file: " + inputFilePath);
                        System.exit(1);
                    }
                }
            }
        }
    }
}
