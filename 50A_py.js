    /********************************************* Fixed code /*********************************************/

let [fs , path,common] = [require('fs') , require('path'),require('./fixed_code')];

console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)

for(var i = 0 ; i < (fs.readdirSync(path.join('input', path.parse(path.basename(__filename)).name)).filter(file => fs.statSync(path.join('input', path.parse(path.basename(__filename)).name, file)).isFile()).length); i++){
    let [outputFile , inputFilePath , outputFilePath] = [`output_${path.parse(path.basename(__filename))["name"]}_${i}.txt`,`input/${path.parse(path.basename(__filename))["name"]}/input_${path.parse(path.basename(__filename))["name"]}_${i}.txt` , `output/${path.parse(path.basename(__filename))["name"]}/output_${path.parse(path.basename(__filename))["name"]}_${i}.txt`];

    /********************************************* Problem-dependent and language-dependent code    /*********************************************/

let code = `
print(str(eval(input_content.replace(' ','*')) // 2))
    `;


     /********************************************* Fixed code  /*********************************************/
let pythonArgs = ['-c', `
with open('${inputFilePath}', 'r') as infile:
    input_content = infile.read().strip()
` + code];
    common(pythonArgs, outputFile, inputFilePath, outputFilePath);
}