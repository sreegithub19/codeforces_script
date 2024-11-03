#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSString *pythonCommand = R"(
print('Hello from Python in Objective C++!!')
print('This is a multiline string.')
print('Using R for the task.')
        )";
        NSTask *task = [[NSTask alloc] init];
        [task setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]]; // Use bash for better compatibility
        [task setArguments:@[@"-c", [NSString stringWithFormat:@"python3 -c \"%@\"", pythonCommand]]];
        NSPipe *pipe = [NSPipe pipe];
        [task setStandardOutput:pipe];
        NSError *error = nil;
        [task launchAndReturnError:&error];
        if (error) {
            NSLog(@"Error launching task: %@", error);
            return 1;
        }
        [task waitUntilExit];
        NSLog(@"\n%@", [[NSString alloc] initWithData:[[pipe fileHandleForReading] readDataToEndOfFile] encoding:NSUTF8StringEncoding]);
    }
    return 0;
}