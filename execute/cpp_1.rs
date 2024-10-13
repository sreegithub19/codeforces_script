use std::process::Command;
use std::env;

fn main() {
    // Get the current directory of the Rust file
    let current_dir = env::current_dir().expect("Failed to get current directory");

    let cpp_code = r#"
#include <iostream>
#include <fstream>
#include <string>

int main() {
    // Take user input
    std::string name;
    std::cout << "Enter your name: ";
    std::getline(std::cin, name); // Read a line of text from stdin
    std::cout << "Hello, " << name << "! Welcome to the C++ program." << std::endl;

    // Read from a file
    std::ifstream inputFile("input.txt"); // Open the file
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
"#;

    // Compile and run the C++ code using a single command
    let output = Command::new("bash")
        .arg("-c")
        .arg(format!(
            "echo '{}' | g++ -x c++ -o hello - && ./hello",
            cpp_code.replace("'", "'\"'\"'")
        ))
        .current_dir(current_dir) // Ensure the command runs in the current directory
        .output()
        .expect("Failed to execute command");

    // Print the output of the C++ program
    println!("{}", String::from_utf8_lossy(&output.stdout));
    println!("{}", String::from_utf8_lossy(&output.stderr));
}
