const { execSync } = require('child_process');
const path = require('path');

const cppCode = `
#include <iostream>
#include <string>
#include <fstream>

int main() {
    // input
    std::string name;
    std::cout << "Enter your name: ";
    std::getline(std::cin, name); // Read a line of text from stdin
    std::cout << "Hello, " << name << "! Welcome to the C++ program." << std::endl;

    // file reading
    std::ifstream inputFile("input.txt"); // Open the file
    if (!inputFile) {
        std::cerr << "Error opening file." << std::endl;
        return 1; // Exit with an error code
    }

    std::string line;
    while (std::getline(inputFile, line)) {
        std::cout << line << std::endl; // Print the line to the console
    }

    inputFile.close(); // Close the file
    return 0;
}
`;

try {
    const output = execSync(`echo '${cppCode.replace(/'/g, "'\\''")}' | g++ -x c++ -o hello - && ./hello`, { 
        stdio: 'inherit',
        cwd: __dirname // Set the current working directory to where the script is
    });
} catch (error) {
    console.error(`Error: ${error.message}`);
}
