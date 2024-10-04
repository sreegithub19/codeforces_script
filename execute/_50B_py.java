import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class _50B_py {
    public static void main(String[] args) {
        String currentFileName = new Exception().getStackTrace()[0].getClassName();
        String dirPath = "../input/" + currentFileName;

        System.out.println("dsdwddd:" + dirPath);

        System.out.println("/******************************************  "  + currentFileName + "  ******************************************/");

        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(File::isFile);

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
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
                                double result = 0;
                                for (int count : freq) {
                                    if (count > 0) {
                                        result += Math.pow(count, 2);
                                    }
                                }


 /******************************************** End of Answer code ********************************************/

                                // Read contents from the output file
                                List<String> outputLines = Files.readAllLines(Paths.get(outputFilePath));
                                boolean found = false;

                                // Compare the computed result with each line in the output file
                                for (String line : outputLines) {
                                    if (line.trim().equals(String.valueOf((int) result))) {
                                        found = true;
                                        break;
                                    }
                                }

                                // Output result of the comparison
                                if (found) {
                                    System.out.println(i + ":true");
                                } else {
                                    System.out.println(i + ":false");
                                    System.exit(1);
                                }
                            } else {
                                System.err.println("Not enough numbers in input file: " + inputFilePath);
                                System.exit(1);
                            }
                        }
                    catch (IOException e) {
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
