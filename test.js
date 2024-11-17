const fs = require('fs');
const assert = require('assert');

// Load WebAssembly module
async function loadWasm() {
  const wasmBuffer = fs.readFileSync('./build/index.wasm');
  const wasmModule = await WebAssembly.compile(wasmBuffer);
  const wasmInstance = await WebAssembly.instantiate(wasmModule);

  return wasmInstance.exports;
}

(async () => {
  const wasm = await loadWasm();

  // Test the 'add' function from WebAssembly
  assert.strictEqual(wasm.add(5, 7), 12, 'WASM function should return');
  assert.strictEqual(wasm.subtract(5, 7), -2, 'WASM function should return');
  assert.strictEqual(wasm.multiply(5, 7), 35, 'WASM function should return');
  assert.strictEqual(Math.round(wasm.divide(5, 7)), 1, 'WASM function should return');

  console.log('WASM tests passed!');
})();
