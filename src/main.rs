use std::f64::consts::PI;

fn main() {
    // Define some numbers for the operations
    let num1 = 12.0;
    let num2 = 8.0;

    // Perform some mathematical operations
    let sum = num1 + num2;
    let difference = num1 - num2;
    let product = num1 * num2;
    let quotient = num1 / num2;
    let square_root = num1.sqrt();
    let area_of_circle = PI * num1.powi(2);  // Area of a circle (πr²)

    // Print the results to the console
    println!("Sum: {}", sum);
    println!("Difference: {}", difference);
    println!("Product: {}", product);
    println!("Quotient: {}", quotient);
    println!("Square root of {}: {}", num1, square_root);
    println!("Area of circle with radius {}: {}", num1, area_of_circle);
}