const { execSync } = require('child_process');

const cppCode = `
#include <iostream>

int main() {
    std::cout << R"(
    Hello from C++! in multiline!
    )" << std::endl;
    return 0;
}
`;

try {
    // Compile and run the C++ code using a single command
    const output = execSync(`echo '${cppCode.replace(/'/g, "'\\''")}' | g++ -x c++ -o hello - && ./hello`, { stdio: 'pipe' });

    // Print the output of the C++ program
    console.log(output.toString());
} catch (error) {
    console.error(`Error: ${error.message}`);
}
