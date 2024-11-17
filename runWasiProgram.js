const fs = require('fs');
const path = require('path');
const { WASI } = require('@wasmer/wasi');

// Path to your WASI-compatible WebAssembly binary
const wasmFilePath = path.resolve(__dirname, 'build/wasi_.wasm');

// Create a new WASI instance
const wasi = new WASI({
  args: ['node', 'wasi_.wasm'],  // Command-line args for your WASI program
  env: {},  // Optional: Define environment variables for your WASI program
  preopens: { '/sandbox': './sandbox' }  // Optional: Pre-open directories
});

// Function to load and run the WASI WebAssembly module
async function runWasiModule() {
  try {
    const wasmBuffer = fs.readFileSync(wasmFilePath);

    // WebAssembly instantiation using WASI imports
    const { instance, module } = await WebAssembly.instantiate(wasmBuffer, wasi.getImports());

    console.log('WASM Module:', module);
console.log('WASI Imports:', wasi.getImports());

    // Start the WASI instance (this will run your program)
    wasi.start(instance);
  } catch (err) {
    console.error("Error running WASI module:", err);
  }
}

runWasiModule();
