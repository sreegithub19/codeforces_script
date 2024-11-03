#import </Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/System/Library/Frameworks/Foundation.framework/Headers/Foundation.h>

void findFoundationHeader() {
    NSArray *searchPaths = @[
        @"/usr/include/GNUstep/Foundation/Foundation.h",
        @"/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/System/Library/Frameworks/Foundation.framework/Headers/Foundation.h",
        @"/usr/local/include/Foundation/Foundation.h",
        @"/usr/include/Foundation/Foundation.h"
    ];
    
    for (NSString *path in searchPaths) {
        if ([[NSFileManager defaultManager] fileExistsAtPath:path]) {
            NSLog(@"Found Foundation.h at: %@", path);
            return;
        }
    }
    
    NSLog(@"Foundation.h not found in the specified paths.");
}

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        findFoundationHeader();
    }
    return 0;
}
