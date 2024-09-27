const readline = require('readline');
const path = require('path');
const { spawn, exec } = require('child_process');
let fs = require('fs');

function common(pythonArgs,outputFile, inputFilePath,outputFilePath) {

    // Spawn the Python process
    const pythonProcess = spawn('python3', pythonArgs);

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
    console.log(`${outputFile} :${trimmedOutput}`);



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

}

module.exports = common;
