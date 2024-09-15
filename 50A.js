/////////////////////////////////////// 1. Look for changes here  /////////////////////////////////////////

const readline = require('readline');
const path = require('path');

console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)

let fs = require('fs');
const fileStream = require('fs').createReadStream(`input/input_${path.parse(path.basename(__filename))["name"]}_.txt`);
const outputFilePath = `output/output_${path.parse(path.basename(__filename))["name"]}_.txt`;

// Create an interface for reading input from stdin
const rl = readline.createInterface({
  input: fileStream
  //,output: process.stdout
});

//////////////////////////////////////// 2. Problem-dependent code  /////////////////////////////////////////


let inputLines = [];
let currentLine = 0;

rl.on('line', (line) => {
  inputLines.push(line.trim());
  if (inputLines.length === 2 + Number(inputLines[0])) {
    rl.close();
    processInput();
  }
});

function processInput() {
  // Parse inputs
  const N = Number(inputLines[0]);
  const ranges = inputLines.slice(1, N + 1).map(line => line.split(' ').map(Number));
  const K = Number(inputLines[N + 1]);

  let T = Array(N + 1).fill(0);
  T[0] = 1;

  for (const [L, R] of ranges) {
    let c = 1;
    let n = 0;

    while (c <= 10**18) {
      n += Math.max(0, Math.min(R, 2*c - 1) - Math.max(L, c) + 1);
      c *= 10;
    }

    const p = n / (R - L + 1);

    const B = [...T];
    T = Array(N + 1).fill(0);
    T[0] = B[0] * (1 - p);

    for (let j = 1; j <= N; j++) {
      T[j] = B[j] * (1 - p) + B[j - 1] * p;
    }
  }

  const thresholdIndex = Math.floor(N * K + 99) / 100;
  let result = T.slice(thresholdIndex).reduce((sum, value) => sum + value, 0);

  console.log(result);



///////////////////////////////////////// 3. Fixed code /////////////////////////////////////////////

  let result1 = Array(result.toString().split('\n').length).fill(result).join('\n');
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
}
