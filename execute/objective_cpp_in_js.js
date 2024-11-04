const { exec } = require('child_process');

const command = `
echo '#import </Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/System/Library/Frameworks/Foundation.framework/Headers/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSLog(@"Hello from Objective-C++ on GitHub Actions from here inline in nodejs!");
    }
    return 0;
}' | clang++ -std=c++11 -framework Foundation -x objective-c++ -o hello0 - && ./hello0
`;

// Execute the command with stdio: 'pipe'
exec(command, { stdio: 'pipe' }, (error, stdout, stderr) => {
    if (error) {
        console.error(`Error: ${error.message}`);
        return;
    }
    if (stderr) {
        console.error(`Stderr: ${stderr}`);
        return;
    }
    console.log(`Output: ${stdout}`);
});
