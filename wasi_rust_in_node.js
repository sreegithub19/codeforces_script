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
    println!("Sum in nodejs in wasi: {}", sum);
    println!("Difference in nodejs in wasi: {}", difference);
    println!("Product in nodejs in wasi: {}", product);
    println!("Quotient in nodejs in wasi: {}", quotient);
    println!("Square root of {} in nodejs in wasi: {}", num1, square_root);
    println!("Area of circle with radius {} in nodejs in wasi: {}", num1, area_of_circle);
}
`;


  // Use `echo` to pipe the Rust code directly to `rustc` (compiling it)
  const compileCommand = `echo '${Buffer.from(rustCode).toString('base64')}' | base64 --decode | rustc --target wasm32-wasip1 -o inline_rust_program.wasm - &&
    wasmtime inline_rust_program.wasm`;

  // Run the command to compile and execute the Rust code
  exec(compileCommand, (err, stdout, stderr) => {
    if (err) {
      console.error(`Error executing Rust code: ${stderr}`);
      return;
    }

    // Print the output from the Rust program
    console.log(stdout);

  });
