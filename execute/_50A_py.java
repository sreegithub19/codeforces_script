import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class _50A_py {
    public static void main(String[] args) {
        String currentFileName = new File(_50A_py.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        String dirPath = "./input/" + currentFileName.substring(0, currentFileName.lastIndexOf('.'));

		System.out.println("currentFileName:" + currentFileName);
		System.out.println("dirPath:" + dirPath);

        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(File::isFile);

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String inputFilePath = dirPath + "/input_" + currentFileName.substring(0, currentFileName.lastIndexOf('.')) + "_" + i + ".txt";
                    String outputFilePath = "./output/output_" + currentFileName.substring(0, currentFileName.lastIndexOf('.')) + "_" + i + ".txt";

                    try {
                        List<String> inputLines = Files.readAllLines(Paths.get(inputFilePath));
                        if (inputLines.size() >= 2) {
                            int num1 = Integer.parseInt(inputLines.get(0).trim());
                            int num2 = Integer.parseInt(inputLines.get(1).trim());
                            int result = (num1 * num2) / 2;

                            // Read contents from the output file
                            List<String> outputLines = Files.readAllLines(Paths.get(outputFilePath));
                            boolean found = false;

                            // Compare the computed result with each line in the output file
                            for (String line : outputLines) {
                                if (line.trim().equals(String.valueOf(result))) {
                                    found = true;
                                    break;
                                }
                            }

                            // Output result of the comparison
                            if (found) {
                                System.out.println("Result " + result + " found in output file.");
                            } else {
                                System.out.println("Result " + result + " not found in output file.");
                            }
                        }
                    } 
					catch (IOException e) {
                        e.printStackTrace();
                    } 
					catch (NumberFormatException e) {
                        System.err.println("Invalid number format in file: " + inputFilePath);
                    }
                }
            }
        }
    }
}
