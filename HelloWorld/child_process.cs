using System;
using System.Diagnostics;

public class child_process
{
    public static void Main()
    {
        // Define the Python command as a multiline string
        string pythonCommand = @"
print('Hello from Python!')
x = 5
y = 10
print(f'The sum of {x} and {y} is {x + y}.')
";

        // Create a new process
        ProcessStartInfo processInfo = new ProcessStartInfo
        {
            FileName = "python", // Adjust this to "python3" if needed
            Arguments = $"-c \"{pythonCommand}\"", // The command to execute
            RedirectStandardOutput = true, // Redirect output to read it
            RedirectStandardError = true, // Redirect error output
            UseShellExecute = false, // Required for redirection
            CreateNoWindow = true // Do not create a window
        };

        using (Process process = new Process())
        {
            process.StartInfo = processInfo;

            // Start the process
            process.Start();

            // Read the output (or error)
            string output = process.StandardOutput.ReadToEnd();
            string error = process.StandardError.ReadToEnd();

            // Wait for the process to finish
            process.WaitForExit();

            // Print output and error (if any)
            Console.WriteLine("Output:");
            Console.WriteLine(output);
            if (!string.IsNullOrEmpty(error))
            {
                Console.WriteLine("Error:");
                Console.WriteLine(error);
            }
        }
    }
}
