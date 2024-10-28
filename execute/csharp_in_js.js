const { exec } = require('child_process');

// Function to execute Python code
function runPythonCode() {
    const command = `csi <<END
        using System;

        public class Program
        {
            public static void Main()
            {
                Console.WriteLine("Hello from C# Interactive in GitHub Actions multiline from nodejs!");
                Console.WriteLine("This is a multiline C# code example from nodejs.");
            }

            public static void Sub()
                {
                    Console.WriteLine("Hello from C# Interactive in GitHub Actions multiline from Sub!");
                    Console.WriteLine("This is a multiline C# code example from Sub.");
                }
        }

        Program.Main();
        END`;
    return exec(command);
}

// Execute the Python code
const pythonProcess = runPythonCode();

// Handle the output from the Python process
pythonProcess.stdout.on('data', (data) => {
    console.log(`${data}`);
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
