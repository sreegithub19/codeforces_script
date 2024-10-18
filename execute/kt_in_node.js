const { spawn } = require('child_process');

// Function to run Kotlin code from a string
function runKotlinCode(code) {
    const kotlinREPL = spawn('kotlin', ['-e', code]);

    kotlinREPL.stdout.on('data', (data) => {
        console.log(`${data}`);
    });

    kotlinREPL.stderr.on('data', (data) => {
        console.error(`Error: ${data}`);
    });

    kotlinREPL.on('close', () => {});
}

// Kotlin code as a string
const kotlinCode = `
fun main() {
    println("Hello from in-memory Kotlin!")
}
main()
`;

// Run the Kotlin code
runKotlinCode(kotlinCode);