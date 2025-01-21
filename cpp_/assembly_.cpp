#include <iostream>

int main() {
    int a = 14, b = 10;
    int sum, difference, product, quotient;

    // Inline assembly to perform addition using AT&T syntax
    __asm__ (
        "addl %%ebx, %%eax;" // Perform addition: a + b
        : "=a" (sum)         // Output the result to 'sum'
        : "a" (a), "b" (b)   // Input operands: 'a' in eax, 'b' in ebx
    );

    // Inline assembly to perform subtraction using AT&T syntax
    __asm__ (
        "subl %%ebx, %%eax;" // Perform subtraction: a - b
        : "=a" (difference)  // Output the result to 'difference'
        : "a" (a), "b" (b)   // Input operands: 'a' in eax, 'b' in ebx
    );

    // Inline assembly to perform multiplication using AT&T syntax
    __asm__ (
        "imull %%ebx, %%eax;" // Perform multiplication: a * b
        : "=a" (product)      // Output the result to 'product'
        : "a" (a), "b" (b)    // Input operands: 'a' in eax, 'b' in ebx
    );

    // Ensure we don't divide by zero
    if (b != 0) {
        // Inline assembly to perform division using AT&T syntax
        __asm__ (
            "movl %%eax, %%edx;" // Move 'a' to edx
            "xorl %%eax, %%eax;" // Clear eax
            "divl %%ebx;"        // Perform division: a / b
            : "=d" (quotient)    // Output the quotient to 'quotient'
            : "d" (a), "b" (b)   // Input operands: 'a' in edx, 'b' in ebx
        );
    } else {
        std::cerr << "Error: Division by zero" << std::endl;
        return 1;
    }

    std::cout << "Sum: " << sum << std::endl;
    std::cout << "Difference: " << difference << std::endl;
    std::cout << "Product: " << product << std::endl;
    std::cout << "Quotient: " << quotient << std::endl;

    return 0;
}