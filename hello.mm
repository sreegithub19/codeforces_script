#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSTask *task = [[NSTask alloc] init];
        [task setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]];
        [task setArguments:@[@"-c", @"python3 -c \"print('Hello from Python in Objective C++!!')\""]];
        [task setStandardOutput:[NSPipe pipe]];

        NSError *error = nil;
        if (![task launchAndReturnError:&error]) {
            NSLog(@"Error launching task: %@", error);
            return 1;
        }

        [task waitUntilExit];
        NSLog(@"\n%@", [[[[task.standardOutput fileHandleForReading] readDataToEndOfFile] initWithData:encoding:NSUTF8StringEncoding]]);
    }
    return 0;
}
