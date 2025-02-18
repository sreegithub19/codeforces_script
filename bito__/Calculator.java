import java.util.Scanner;

public class Calculator {

    // Arithmetic operation methods
    public static double add(double a, double b) {
        return a + b;
    }
    
    public static double subtract(double a, double b) {
        return a - b;
    }
    
    public static double multiply(double a, double b) {
        return a * b;
    }
    
    // For division, if b is 0 we throw an IllegalArgumentException.
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }
    
    // Interactive main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the first number
        System.out.print("Enter the first number: ");
        double num1 = scanner.nextDouble();

        // Prompt the user to enter the second number
        System.out.print("Enter the second number: ");
        double num2 = scanner.nextDouble();

        // Perform arithmetic operations using the static methods
        double sum = add(num1, num2);
        double difference = subtract(num1, num2);
        double product = multiply(num1, num2);

        System.out.println("\nResults:");
        System.out.println(num1 + " + " + num2 + " = " + sum);
        System.out.println(num1 + " - " + num2 + " = " + difference);
        System.out.println(num1 + " * " + num2 + " = " + product);

        // Check division separately to avoid an exception in the main experience
        if (num2 != 0) {
            double quotient = divide(num1, num2);
            System.out.println(num1 + " / " + num2 + " = " + quotient);
        } else {
            System.out.println("Division by zero is not allowed.");
        }
        
        scanner.close();
    }
}