/////////////////////////////////////// 1. Look for changes here  /////////////////////////////////////////
const fs = require('fs');
const path = require('path');
const common = require('./fixed_code')

console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)


for(var i = 0 ; i < (fs.readdirSync(path.join('input', path.parse(path.basename(__filename)).name)).filter(file => fs.statSync(path.join('input', path.parse(path.basename(__filename)).name, file)).isFile()).length); i++){

    let outputFile = `output_${path.parse(path.basename(__filename))["name"]}_${i}.txt`;
    let inputFilePath = `input/${path.parse(path.basename(__filename))["name"]}/input_${path.parse(path.basename(__filename))["name"]}_${i}.txt`;
    let outputFilePath = `output/${path.parse(path.basename(__filename))["name"]}/output_${path.parse(path.basename(__filename))["name"]}_${i}.txt`;

    //////////////////////////////////////// 2. Problem-dependent and language-dependent code  /////////////////////////////////////////

    const pythonArgs = ['-c', `
with open('${inputFilePath}', 'r') as infile:
    input_content = infile.read().strip()
result = eval(input_content.replace(' ','*')) // 2
print(str(result))
    `];

    ///////////////////////////////////////// 3. Fixed code /////////////////////////////////////////////
    common(pythonArgs, outputFile, inputFilePath, outputFilePath);
}