// factorial_script.sci
// A simple Scilab script to compute the factorial of a number

// Clear the console and all variables
clc;
clear;

// Define the number for which to compute the factorial
n = 5;

// Function to compute factorial
function f = factorial(n)
    if n == 0 then
        f = 1;
    else
        f = n * factorial(n - 1);
    end
endfunction

// Compute the factorial of n
result = factorial(n);

// Display the result
disp("The factorial of " + string(n) + " is " + string(result));
