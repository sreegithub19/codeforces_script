#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // Define the Python code as a string
        NSString *pythonCode = @"print('Hello, Python from Objective-C World!')\n";

        // Create the NSTask instance
        NSTask *task = [[NSTask alloc] init];

        // Set the executable to the Python interpreter
        [task setExecutableURL:[NSURL fileURLWithPath:@"python"]];
        
        // Set the arguments: -c for command and the Python code
        [task setArguments:@[@"-c", pythonCode]];
        
        // Create a pipe to capture standard output
        NSPipe *pipe = [NSPipe pipe];
        [task setStandardOutput:pipe];

        // Launch the task
        NSError *error = nil;
        [task launchAndReturnError:&error];

        if (error) {
            NSLog(@"Error launching task: %@", error);
            return 1;
        }

        // Wait for the task to finish
        [task waitUntilExit];

        // Read the output from the pipe
        NSData *data = [[pipe fileHandleForReading] readDataToEndOfFile];
        NSString *output = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];

        // Print the output
        NSLog(@"Output:\n%@", output);
    }
    return 0;
}
