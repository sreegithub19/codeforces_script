/********************************************* Problem-dependent and language-dependent code    /*********************************************/
let code = `
print(str(eval(input_content.replace(' ','*')) // 2))
 `;
/********************************************* Fixed code /*********************************************/

let [fs, path, common] = [require('fs'), require('path'), require('../fixed_code')], dirPath = path.join('./input', path.parse(path.basename(__filename)).name); 
fs.readdirSync(dirPath).filter(file => fs.statSync(path.join(dirPath, file)).isFile()).forEach((_, i) => common(['-c', `with open('./input/${path.parse(path.basename(__filename)).name}/input_${path.parse(path.basename(__filename)).name}_${i}.txt', 'r') as infile: input_content = infile.read().strip()` + code], `./output/output_${path.parse(path.basename(__filename)).name}_${i}.txt`, `./input/${path.parse(path.basename(__filename)).name}/input_${path.parse(path.basename(__filename)).name}_${i}.txt`,  `./output/${path.parse(path.basename(__filename)).name}/output_${path.parse(path.basename(__filename)).name}_${i}.txt`));