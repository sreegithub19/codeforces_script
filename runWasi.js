const fs = require('fs');
const path = require('path');
const { WASI } = require('@wasmer/wasi');
const { argv, env } = require('process');

async function runWasi() {
  const wasi = new WASI({
    args: argv,
    env,
    preopens: {
      '/sandbox': './'
    }
  });

  // Load and instantiate the WASM module
  const wasmPath = path.join(__dirname, 'build/greet.wasm');
  const wasmBytes = fs.readFileSync(wasmPath);

  const { instance } = await wasi.instantiate(wasmBytes);

  // Call the 'greet' function with "World" as argument
  const result = instance.exports.greet("World");
  console.log(result);  // Should output: "Hello, World!"
}

runWasi().catch(console.error);
