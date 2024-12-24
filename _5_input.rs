use std::io::{stdin, stdout, Write};

fn main() {
    let mut s = String::new();
    
    // First prompt and input
    print!("Please enter some text: ");
    let _ = stdout().flush();
    stdin().read_line(&mut s).expect("Did not enter a correct string");

    // Trim newline characters from the first input
    if let Some('\n') = s.chars().next_back() {
        s.pop();
    }
    if let Some('\r') = s.chars().next_back() {
        s.pop();
    }
    println!("You typed: {}", s);

    // Second prompt and input
    print!("Please enter some text again: ");
    let _ = stdout().flush();
    stdin().read_line(&mut s).expect("Did not enter a correct string");

    // Trim newline characters from the second input
    if let Some('\n') = s.chars().next_back() {
        s.pop();
    }
    if let Some('\r') = s.chars().next_back() {
        s.pop();
    }
    println!("You againtyped: {}", s);

    // Clear the buffer for the second input
    s.clear(); // Clear previous input to avoid appending to the first input

    // Second prompt and input
    print!("Please enter some text again: ");
    let _ = stdout().flush();
    stdin().read_line(&mut s).expect("Did not enter a correct string");

    // Trim newline characters from the second input
    if let Some('\n') = s.chars().next_back() {
        s.pop();
    }
    if let Some('\r') = s.chars().next_back() {
        s.pop();
    }
    println!("You againtyped: {}", s);
}
