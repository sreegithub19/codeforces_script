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
            let mut name = String::new();
            println!("Enter your name: ");
            io::stdin().read_line(&mut name).expect("Failed to read line");
            let name = name.trim();
            println!("Hello, {}! Welcome to the Rust program.", name);

            let file = File::open("input_1.txt").expect("Could not open file from in-memory file");
            let reader = BufReader::new(file);
            println!("Contents of input_1.txt:");
            for line in reader.lines() {
                println!("{}", line.expect("Failed to read line"));
            }
        }
    )";

    // Compile and run Rust code in one command
    std::string command = "echo '";
    command += rust_code;
    command += "' | rustc -o /dev/stdout - && /dev/stdout";

    // Execute the command
    int result = system(command.c_str());

    return result;
}
