#import <Foundation/Foundation.h>
#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // Raw string literal should be std::string, not a pointer
        std::string pythonCommand = R"(
print('Hello from Python in Objective C++!!')
print('This is a multiline string.')
print('Using R for the task.')
)";
        NSTask *task = [[NSTask alloc] init];
        [task setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]]; // Use bash for better compatibility
        // Convert std::string to NSString
        NSString *command = [NSString stringWithFormat:@"python3 -c \"%@\"", pythonCommand];
        [task setArguments:@[@"-c", command]];
        NSPipe *pipe = [NSPipe pipe];
        [task setStandardOutput:pipe];
        NSError *error = nil;
        [task launchAndReturnError:&error];
        if (error) {
            NSLog(@"Error launching task: %@", error);
            return 1;
        }
        [task waitUntilExit];
        NSString *output = [[NSString alloc] initWithData:[[pipe fileHandleForReading] readDataToEndOfFile] encoding:NSUTF8StringEncoding];
        NSLog(@"\n%@", output);
    }
    return 0;
}
