import Foundation

// Create a Process instance
let process = Process()

// Set the executable path (path to swift binary)
process.executableURL = URL(fileURLWithPath: "/opt/hostedtoolcache/swift-Ubuntu/5.7.3/x64/usr/bin/swift")

// Set the arguments for the command to run (multiline Swift code as a single string)
let swiftCode = """
print("Hello, Swift shell World from -e!")
let x = 42
print("The answer is \(x)")
"""

// Escape the newline characters to make the entire code a single string
let swiftArguments = swiftCode.replacingOccurrences(of: "\n", with: "\\n")

// Pass the arguments (swift -e and the escaped Swift code)
process.arguments = ["-e", swiftArguments]

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
