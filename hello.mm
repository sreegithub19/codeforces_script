#import <Foundation/Foundation.h>
#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // Python command as a std::string
        std::string pythonCommand = R"(
print('Hello from Python in Objective C++!!')
print('This is a multiline string.')
print('Using R for the task.')
)";

        // Node.js command as a std::string
        std::string nodeCommand = R"(
console.log('Hello from Node.js in Objective C++!!');
console.log('This is another multiline string.');
console.log('Using Node for the task.');
)";

        // Convert std::string to NSString for Python command
        NSString *pythonCommandNSString = [NSString stringWithUTF8String:pythonCommand.c_str()];

        // Convert std::string to NSString for Node.js command
        NSString *nodeCommandNSString = [NSString stringWithUTF8String:nodeCommand.c_str()];

        // Execute Python command
        NSTask *pythonTask = [[NSTask alloc] init];
        [pythonTask setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]];
        NSString *pythonFullCommand = [NSString stringWithFormat:@"python3 -c \"%@\"", pythonCommandNSString];
        [pythonTask setArguments:@[@"-c", pythonFullCommand]];
        
        NSPipe *pythonPipe = [NSPipe pipe];
        [pythonTask setStandardOutput:pythonPipe];

        NSError *pythonError = nil;
        [pythonTask launchAndReturnError:&pythonError];
        if (pythonError) {
            NSLog(@"Error launching Python task: %@", pythonError);
            return 1;
        }
        [pythonTask waitUntilExit];
        NSString *pythonOutput = [[NSString alloc] initWithData:[[pythonPipe fileHandleForReading] readDataToEndOfFile] encoding:NSUTF8StringEncoding];
        NSLog(@"\nPython Output:\n%@", pythonOutput);

        // Execute Node.js command
        NSTask *nodeTask = [[NSTask alloc] init];
        [nodeTask setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]];
        NSString *nodeFullCommand = [NSString stringWithFormat:@"node -e \"%@\"", nodeCommandNSString];
        [nodeTask setArguments:@[@"-c", nodeFullCommand]];
        
        NSPipe *nodePipe = [NSPipe pipe];
        [nodeTask setStandardOutput:nodePipe];

        NSError *nodeError = nil;
        [nodeTask launchAndReturnError:&nodeError];
        if (nodeError) {
            NSLog(@"Error launching Node.js task: %@", nodeError);
            return 1;
        }
        [nodeTask waitUntilExit];
        NSString *nodeOutput = [[NSString alloc] initWithData:[[nodePipe fileHandleForReading] readDataToEndOfFile] encoding:NSUTF8StringEncoding];
        NSLog(@"\nNode.js Output:\n%@", nodeOutput);
    }
    return 0;
}
