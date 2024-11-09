import Foundation

// Create a Process instance
let process = Process()

// Set the arguments for the command to run
process.arguments = ["/opt/hostedtoolcache/swift-Ubuntu/5.7.3/x64/usr/bin/swift","-e", """
print("Hello, Swift shell World from -e!")
"""]

// Create a Pipe to capture the output
let pipe = Pipe()
process.standardOutput = pipe

do {
    // Launch the process
    try process.run()
    
    // Read the output
    let data = pipe.fileHandleForReading.readDataToEndOfFile()
    if let output = String(data: data, encoding: .utf8) {
        print("Command Output: \(output)")
    }
} catch {
    print("Error executing command: \(error)")
}
