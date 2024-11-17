const fs = require('fs');
const path = require('path');
const { WASI } = require('wasmtime');

// Path to your WASI-compatible WebAssembly binary
const wasmFilePath = path.resolve(__dirname, 'build/wasi_.wasm');

// Create a new WASI instance using Wasmtime
const wasi = new WASI({
  args: ['node', 'wasi_.wasm'], // Command-line args for your WASI program
  env: {}, // Optional: Define environment variables for your WASI program
  preopens: { '/sandbox': './sandbox' } // Optional: Pre-open directories
});

// Function to load and run the WASI WebAssembly module
async function runWasiModule() {
  try {
    const wasmBuffer = fs.readFileSync(wasmFilePath);

    // Instantiate and run the WebAssembly module using wasmtime
    const { instance } = await WASI.instantiate(wasmBuffer, wasi.getImports());

    // Start the WASI instance (this will run your program)
    wasi.start(instance);
  } catch (err) {
    console.error('Error running WASI module:', err);
  }
}

runWasiModule();
