const { execSync } = require('child_process');

const objcCode = `
#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSLog(@"Hello from Objective-C++! in multiline from Nodejs!");
    }
    return 0;
}
`;

try {
    // Compile and run the Objective-C++ code using a single command
    const output = execSync(`echo '${objcCode.replace(/'/g, "'\\''")}' | clang++ -x objective-c++ -framework Foundation -o hello - -isysroot $(xcrun --sdk macosx --show-sdk-path) && ./hello`, { stdio: 'pipe' });

    // Print the output of the Objective-C++ program
    console.log(output.toString());
} catch (error) {
    console.error(`Error: ${error.message}`);
}
