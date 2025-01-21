#include <iostream>

int main() {
    int a = 14, b = 10;
    int sum, difference, product, quotient;

    // Inline assembly to perform basic arithmetic operations
    asm ("addl %%ebx, %%eax;" : "=a"(sum) : "a"(a), "b"(b));
    asm ("subl %%ebx, %%eax;" : "=a"(difference) : "a"(a), "b"(b));
    asm ("imull %%ebx, %%eax;" : "=a"(product) : "a"(a), "b"(b));
    asm ("movl %%eax, %%edx; xorl %%eax, %%eax; divl %%ebx;" : "=d"(quotient) : "d"(a), "b"(b));

    std::cout << "Sum: " << sum << std::endl;
    std::cout << "Difference: " << difference << std::endl;
    std::cout << "Product: " << product << std::endl;
    std::cout << "Quotient: " << quotient << std::endl;

    return 0;
}
