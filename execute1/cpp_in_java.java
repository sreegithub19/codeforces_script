import java.io.*;
import java.nio.file.*;
import java.util.*;

public class cpp_in_java {
    public static void main(String[] args) {
        try {
            // Get the current directory
            String currentDir = System.getProperty("user.dir");

            // Construct the absolute path for input.txt
            String inputFilePath = Paths.get(currentDir, "execute1", "input.txt").toString();

            // Prepare the C++ code using a text block
            String cppCode = """
                #include <iostream>
                #include <fstream>
                #include <string>

                int main() {
                    // Take user input
                    //std::string name;
                    //std::cout << "Enter your name: ";
                    //std::getline(std::cin, name); // Read a line of text from stdin
                    std::cout << "Hello, Welcome to the C++ program." << std::endl;

                    // Read from a file
                    std::ifstream inputFile("%s"); // Use the absolute path for the file
                    if (!inputFile) {
                        std::cerr << "Error opening file." << std::endl;
                        return 1; // Exit with an error code
                    }

                    std::string line;
                    std::cout << "Contents of input.txt:" << std::endl;
                    while (std::getline(inputFile, line)) {
                        std::cout << line << std::endl; // Print each line to the console
                    }

                    inputFile.close(); // Close the file
                    return 0;
                }
                """.formatted(inputFilePath); // Use the absolute path

            // Compile and run the C++ code using a single command
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", "echo \"" + cppCode.replace("\"", "\\\"") + "\" | g++ -x c++ -o hello - && ./hello");
            builder.directory(new File(currentDir));
            builder.redirectErrorStream(true);

            Process process = builder.start();

            // Read the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
