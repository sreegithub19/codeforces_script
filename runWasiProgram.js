const fs = require('fs');
const path = require('path');
const { WASI } = require('@wasmer/wasi');
const { instantiate } = require('@wasmer/wasi');

async function runWasi() {
  // Initialize the WASI environment without preopens or sandbox
  const wasi = new WASI({
    args: ["./wasi_.wasm"]  // Optional arguments to pass to the WASI module
  });

  // Read the WASI-compatible WASM file
  const wasmPath = path.resolve(__dirname, 'build/my_wasi_program.wasm');
  const wasmBytes = fs.readFileSync(wasmPath);

  // Instantiate the WASM module with the WASI environment
  const { instance } = await instantiate(wasmBytes, {
    wasi_snapshot_preview1: wasi.wasiImport,
  });

  // Start the WASI module
  wasi.start(instance);
}

runWasi().catch(console.error);
