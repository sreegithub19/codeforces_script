/////////////////////////////////////// 1. Look for changes here  /////////////////////////////////////////

const readline = require('readline');
const path = require('path');

console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)

let fs = require('fs');
const inputFilePath = `input/input_${path.parse(path.basename(__filename))["name"]}_.txt`;
const outputFilePath = `output/output_${path.parse(path.basename(__filename))["name"]}_.txt`;


//////////////////////////////////////// 2. Problem-dependent and language-dependent code  /////////////////////////////////////////



const { spawn, exec } = require('child_process');

// Define the Python command and arguments
const pythonCommand = 'python3'; // Use 'python' if needed
const pythonArgs = ['-c', `
with open('${inputFilePath}', 'r') as infile:
    input_content = infile.read().strip()
result = eval(input_content.replace(' ','*')) // 2
print(str(result))
`];


  ///////////////////////////////////////// 3. Fixed code /////////////////////////////////////////////

// Spawn the Python process
const pythonProcess = spawn(pythonCommand, pythonArgs);

// Initialize a variable to store the output
let output = '';

// Handle standard output
pythonProcess.stdout.on('data', (data) => {
  output += data.toString(); // Append data to the output variable
});

// Handle standard error
pythonProcess.stderr.on('data', (data) => {
  console.error(`Python Error: ${data.toString().trim()}`);
});

// Handle errors related to spawning the process
pythonProcess.on('error', (error) => {
  console.error(`Error executing Python command: ${error}`);
  process.exit(1);
});

// Handle process exit
pythonProcess.on('close', (code) => {
  if (code !== 0) {
    console.error(`Python process exited with code ${code}`);
    process.exit(code);
  }

  // Trim any extra whitespace or newlines from the output
  const trimmedOutput = output.trim();
  console.log(`Output of python code is ${trimmedOutput}`);



let result1 = trimmedOutput;
// Read output.txt and compare directly in one line
fs.readFile(outputFilePath, 'utf8', (err, data) => {
    if (err) {
        console.error('Error reading output file:', err);
        return;
    }
    const isMatch = result1.trim() === data.trim();
    if(isMatch)console.log(true);

    else{
        console.error("Wrong!!");
        process.exit(1);
    }
    });


});


//////////////////////////////////////////////////////////////////////////////////