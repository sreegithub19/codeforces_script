#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        [[[[NSTask alloc] init] setExecutableURL:[NSURL fileURLWithPath:@"/bin/bash"]],
          [[NSTask alloc] setArguments:@[@"-c", @"python3 -c \"print('Hello from Python in Objective C++!!')\""]],
          [[NSTask alloc] setStandardOutput:[NSPipe pipe]],
          [NSTask launchAndReturnError:nil],
          [NSTask waitUntilExit]];
        
        NSLog(@"\n%@", [[NSString alloc] initWithData:[[[[NSTask standardOutput] fileHandleForReading] readDataToEndOfFile] encoding:NSUTF8StringEncoding]]);
    }
    return 0;
}
