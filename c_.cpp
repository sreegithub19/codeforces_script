#include <iostream>

extern "C" {
    #include <stdio.h> // Include the C standard I/O header

    // Pure C code
    void cFunction() {
        printf("Hello from C code!\n");
    }
}

int main() {
    cFunction(); // Call the C function
    return 0;
}
