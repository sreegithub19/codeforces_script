import Foundation

// Create a Process instance
let process = Process()

// Set the executable path (in this case, /bin/bash)
process.executableURL = URL(fileURLWithPath: "/usr/bin/swift")

// Set the arguments for the command to run
process.arguments = ["-e", """
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
