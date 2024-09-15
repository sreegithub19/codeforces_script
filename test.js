const { exec } = require('child_process');

// Define the Python command to execute
const pythonCommand = `python -c '''
print("Hello, World!")'''
`;

// Execute the Python command
exec(pythonCommand, (error, stdout, stderr) => {
  if (error) {
    console.error(`Error executing Python command: ${error}`);
    process.exit(1);
  }

  if(stdout.trim()=="Hello, World!"){
    console.log("stdout.trim():",stdout.trim())
    console.log("ok")
  }
  else process.exit(1);

  // Print any errors from the Python command
  if (stderr) {
    console.error(`Python Error: ${stderr}`);
    process.exit(1);
  }
});
