#include <iostream>
#include <cstdlib>

int main() {
    // Inline Rust code to print "Hello, Rust world!"
    const char* rust_code = R"(
        fn main() {
            println!("Hello, Rust world from cpp!");
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
