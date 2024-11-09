import subprocess
import os
import glob

# Define the path to the 'libs' directory
libs_dir = "libs"

classpath = ""
# Check if the 'libs' directory exists and is a directory
if os.path.exists(libs_dir) and os.path.isdir(libs_dir):
    # Get a list of all .jar files in the 'libs' directory
    jar_files = glob.glob(os.path.join(libs_dir, "*.jar"))
    
    if jar_files:
        # Join the .jar file paths with a colon to create the classpath
        classpath = ":".join(jar_files)
        print("Classpath:", classpath)
    else:
        print("No .jar files found in the libs directory.")
else:
    print(f"The directory '{libs_dir}' does not exist or is not a directory.")

# Shell script as a string
shell_script = '''
jshell --class-path '''+classpath+''' --startup /dev/stdin <<EOF
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;


class MyGreeter {
    public static String greet(String name) {
            String message = """
            hello there!!
            """;
        String combinedMessage = message + name;
        String capitalized = StringUtils.capitalize(combinedMessage);


            String command = "print('2 from Python!')";
            System.out.println("Hello, test.py World");
            System.out.println("This is a second line."+"2");
            //System.out.println("|  Welcome to JShell -- Version 22.0.2");

            try {
                // Execute the command directly using Runtime.exec()

                // Capture and print the output of the shell command
                Process process = Runtime.getRuntime().exec(new String[] { "python", "-c", command });
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

/exit

EOF
'''

# Execute the shell script
process = subprocess.run(shell_script, shell=True, text=True, capture_output=True)

# Get the stdout (the output of the command)
output = process.stdout
print(process.stderr)

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