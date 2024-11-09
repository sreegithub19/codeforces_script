import subprocess

# Shell script as a string
shell_script = """
jshell --startup /dev/stdin <<EOF
System.out.println("Hello, World");
System.out.println("This is a second line."+"2");
System.out.println("|  Welcome to JShell -- Version 22.0.2");
EOF
"""

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