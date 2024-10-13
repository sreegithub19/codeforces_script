#include <iostream>
#include <cstdlib>
#include <string>

int main() {
    // Inline Rust code to print and read input
    const char* rust_code = R"(
        use std::io;
        use std::fs::File;
        use std::io::{BufReader, BufRead};

        fn main() {
            // Take user input
            let mut name = String::new();
            println!("Enter your name: ");
            io::stdin().read_line(&mut name).expect("Failed to read line");
            let name = name.trim(); // Remove trailing newline

            println!("Hello, {}! Welcome to the Rust program.", name);

            // Read from a file
            let file = File::open("input.txt").expect("Could not open file");
            let reader = BufReader::new(file);

            println!("Contents of input.txt:");
            for line in reader.lines() {
                println!("{}", line.expect("Failed to read line"));
            }
        }
    )";

    // Compile and run Rust code using a pipe
    std::string command = "echo '";
    command += rust_code;
    command += "' | rustc -o hello_rust - && ./hello_rust";

    // Execute the command
    int result = system(command.c_str());

    // Clean up the generated Rust binary
    system("rm -f hello_rust");

    return result;
}
