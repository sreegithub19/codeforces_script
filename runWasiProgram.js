const fs = require('fs');
const path = require('path');
const { WASI, Instance } = require('wasmtime');

// Path to your WASI-compatible WebAssembly binary
const wasmFilePath = path.resolve(__dirname, 'build/wasi_.wasm');

// Read the WASM binary
const wasmBuffer = fs.readFileSync(wasmFilePath);

// Create a WASI instance with the correct API
async function runWasiModule() {
  try {
    // Initialize WASI with arguments and environment variables
    const wasi = new WASI({
      args: ['node', 'wasi_.wasm'],  // Command-line args
      env: {},  // Optional: define environment variables if needed
      preopens: { '/sandbox': './sandbox' }  // Optional: define pre-opened directories
    });

    // Compile and instantiate the WebAssembly module with WASI imports
    const { module, instance } = await wasmtime.instantiate(wasmBuffer, wasi.getImports());

    // Start the WASI instance
    wasi.start(instance);
  } catch (err) {
    console.error('Error running WASI module:', err);
  }
}

runWasiModule();
