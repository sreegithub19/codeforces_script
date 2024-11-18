const { exec } = require('child_process');
const readline = require('readline');

// Inline Rust code as a string for mathematical operations
const rustCode = `
use std::f64::consts::PI;

fn main() {
    // Define num1 and num2 as f64 (default floating-point type)
    let num1: f64 = 12.0;
    let num2: f64 = 8.0;

    // Perform some mathematical operations
    let sum = num1 + num2;
    let difference = num1 - num2;
    let product = num1 * num2;
    let quotient = num1 / num2;
    let square_root = num1.sqrt();  // works with f64
    let area_of_circle = PI * num1.powi(2);  // Area of a circle (πr²)

    // Print the results to the console
    println!("Sum in nodejs: {}", sum);
    println!("Difference in nodejs: {}", difference);
    println!("Product in nodejs: {}", product);
    println!("Quotient in nodejs: {}", quotient);
    println!("Square root of {} in nodejs: {}", num1, square_root);
    println!("Area of circle with radius {} in nodejs: {}", num1, area_of_circle);
}
`;

// Create a readline interface to interact with the user
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});


  // Log the greeting from the Node.js side
  console.log(`Hello! Welcome to the Node.js program.`);

  // Use `echo` to pipe the Rust code directly to `rustc` (compiling it)
  const compileCommand = `echo "${rustCode}" | rustc -o inline_rust_program - && ./inline_rust_program`;

  // Run the command to compile and execute the Rust code
  exec(compileCommand, (err, stdout, stderr) => {
    if (err) {
      console.error(`Error executing Rust code: ${stderr}`);
      return;
    }

    // Print the output from the Rust program
    console.log("Rust Program Output:");
    console.log(stdout);

    // Clean up the compiled binary after execution
    exec("rm -f inline_rust_program", (err, stdout, stderr) => {
      if (err) {
        console.error(`Error cleaning up binary: ${stderr}`);
      } else {
        console.log("Cleaned up the compiled binary.");
      }
    });

  });
