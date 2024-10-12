use std::process::Command;

fn main() {
    let cpp_code = r#"
#include <iostream>

int main() {
    std::cout << R"(
    Hello from C++! in multiline!
)" << std::endl;
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
        .output()
        .expect("Failed to execute command");

    // Print the output of the C++ program
    println!("{}", String::from_utf8_lossy(&output.stdout));
    println!("{}", String::from_utf8_lossy(&output.stderr));
}
