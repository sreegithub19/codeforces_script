/////////////////////////////////////// 1. Look for changes here  /////////////////////////////////////////

const readline = require('readline');
const path = require('path');
const { spawn, exec } = require('child_process');
let fs = require('fs');
const common = require('./fixed_code')


console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)

const inputFilePath = `input/input_${path.parse(path.basename(__filename))["name"]}_.txt`;
const outputFilePath = `output/output_${path.parse(path.basename(__filename))["name"]}_.txt`;

//////////////////////////////////////// 2. Problem-dependent and language-dependent code  /////////////////////////////////////////


// Define the Python command and arguments
const pythonCommand = 'python3'; // Use 'python' if needed
const pythonArgs = ['-c', `
with open('${inputFilePath}', 'r') as infile:
    input_content = infile.read().strip()
result = eval(input_content.replace(' ','*')) // 2
print(str(result))
`];

///////////////////////////////////////// 3. Fixed code /////////////////////////////////////////////
common(pythonArgs,inputFilePath,outputFilePath);