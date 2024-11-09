import subprocess

# Shell script as a string
shell_script = '''
jshell --startup /dev/stdin <<EOF
System.out.println("Hello, World");
System.out.println("This is a second line."+"2");
//System.out.println("|  Welcome to JShell -- Version 22.0.2");

import org.apache.commons.lang3.StringUtils;

class MyGreeter {
    public static String greet(String name) {
            String message = """
            hello there!!
            """;
        String combinedMessage = message + name;
        String capitalized = StringUtils.capitalize(combinedMessage);

            String command = "print('2 from Python!')";

            try {
                // Execute the command directly using Runtime.exec()
                Process process = Runtime.getRuntime().exec(new String[] { "python", "-c", command });

                // Capture and print the output of the shell command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Wait for the process to finish and get the exit code
                int exitCode = process.waitFor();
                System.out.println("Command executed with exit code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        return capitalized;
    }
}

// Call the greet method and print the result
System.out.println(MyGreeter.greet("world"));

EOF
'''

# Execute the shell script
process = subprocess.run(shell_script, shell=True, text=True, capture_output=True)

# Get the stdout (the output of the command)
output = process.stdout

# Split the output into lines
lines = output.splitlines()

# Find the last index of the line that contains "|  Welcome to JShell -- Version 22.0.2"
end_index = None
for i in range(len(lines) - 1, -1, -1):  # Loop in reverse order
    if "|  Welcome to JShell -- Version 22.0.2" in lines[i]:
        end_index = i
        break

# If the line is found, remove everything after it
if end_index is not None:
    lines = lines[:end_index]

# Rejoin the lines into a string and print the modified output
modified_output = "\n".join(lines)
print(modified_output)