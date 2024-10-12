const { exec } = require('child_process');

// Python code as a string
const pythonCode = `
import sys

# Function to read from a file
def read_file(filename):
    with open(filename, 'r') as file:
        return file.read()

user_input = ""        
# Get user input
nums = int(input("Enter number:"))
for i in range(nums):
    user_input += input("Enter something: ")

# Read from the specified file (change 'input.txt' to your filename)
file_content = read_file('input.txt')
print(f"File Content: {file_content}")
print(f"User Input: {user_input}")
`;

// Function to execute Python code
function runPythonCode(code) {
    const command = `python -c "${code.replace(/"/g, '\\"')}"`;
    return exec(command);
}

// Execute the Python code
const pythonProcess = runPythonCode(pythonCode);

// Handle the output from the Python process
pythonProcess.stdout.on('data', (data) => {
    console.log(`Output: ${data}`);
});

// Handle errors
pythonProcess.stderr.on('data', (data) => {
    console.error(`Error: ${data}`);
});

// Handle when the process exits
pythonProcess.on('close', (code) => {
    console.log(`Python process exited with code ${code}`);
    if (code === 0) {
        console.log('Python script executed successfully.');
        process.exit(0); // Exit Node.js process successfully
    } else {
        console.error('Python script failed.');
        process.exit(code); // Exit with the error code
    }
});

// To handle user input, we need to listen to stdin
process.stdin.on('data', (input) => {
    pythonProcess.stdin.write(input);
    //pythonProcess.stdin.end(); // Close stdin to indicate no more input
});
