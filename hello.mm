#import <Foundation/Foundation.h>
#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // Use std::string for the Python command
        std::string pythonCommand = R"(
print('Hello from Python in Objective C++!!')
print('This is a multiline string.')
print('Using R for the task.')
)";

        // Convert std::string to NSString
        NSString *pythonCommandNSString = [NSString stringWithUTF8String:pythonCommand.c_str()];

        NSTask *task = [[NSTask alloc] init];
        [task setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]];
        
        // Use NSString for the command
        NSString *command = [NSString stringWithFormat:@"python3 -c \"%@\"", pythonCommandNSString];
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
