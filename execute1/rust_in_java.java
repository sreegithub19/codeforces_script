import java.io.*;
import java.nio.file.*;
import java.util.*;

public class rust_in_java {
    public static void main(String[] args) {
        try {
            // Get the current directory
            String currentDir = System.getProperty("user.dir");

            // Construct the absolute path for input.txt
            String inputFilePath = Paths.get(currentDir, "input.txt").toString();

            // Prepare the Rust code using a text block
            String rustCode = """
                fn main() {
                    use std::fs::File;
                    use std::io::{self, BufRead};

                    let path = "%s"; // Use the absolute path for the file

                    let input = File::open(path).unwrap_or_else(|_| {
                        eprintln!("Error opening file.");
                        std::process::exit(1);
                    });

                    let reader = io::BufReader::new(input);
                    println!("Contents of input.txt:");
                    for line in reader.lines() {
                        match line {
                            Ok(content) => println!("{}", content),
                            Err(_) => eprintln!("Error reading line."),
                        }
                    }
                }
                """.formatted(inputFilePath); // Use the absolute path

            // Compile and run the Rust code using a single command
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", "echo \"" + rustCode.replace("\"", "\\\"") + "\" | rustc -o hello - && ./hello");
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
