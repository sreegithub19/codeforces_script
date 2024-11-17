const fs = require('fs');
const assert = require('assert');

// Define the imports required by the WebAssembly module
const imports = {
  env: {
    // Example: If your module needs memory, you need to provide it.
    // This is just an example, adjust based on the actual imports required by your WASM module.
    memory: new WebAssembly.Memory({ initial: 256, maximum: 256 }),
    // Example: If your module calls `console.log`, you can define it like this:
    log: (msg) => console.log(msg),
  },
};

// Load and instantiate the WebAssembly module
async function loadWasmModule() {
  const wasmBuffer = fs.readFileSync('./build/example.wasm');
  const wasmModule = await WebAssembly.compile(wasmBuffer);
  const wasmInstance = await WebAssembly.instantiate(wasmModule, imports); // Pass imports

  return wasmInstance.exports;
}

// Test matrix creation and operations
async function testMatrixOperations(wasm) {
  const matrixA = new wasm.Matrix(1, 2, 3, 4);
  const matrixB = new wasm.Matrix(5, 6, 7, 8);

  const addedMatrix = matrixA.add(matrixB);
  assert.deepStrictEqual([addedMatrix.a, addedMatrix.b, addedMatrix.c, addedMatrix.d], [6, 8, 10, 12], 'Matrix addition failed');

  const multipliedMatrix = matrixA.multiply(matrixB);
  assert.deepStrictEqual([multipliedMatrix.a, multipliedMatrix.b, multipliedMatrix.c, multipliedMatrix.d], [19, 22, 43, 50], 'Matrix multiplication failed');

  console.log('Matrix operations passed!');
}

// Test dot product of vectors
async function testDotProduct(wasm) {
  const vec1 = new Int32Array([1, 2, 3]);
  const vec2 = new Int32Array([4, 5, 6]);
  const dotProd = wasm.dotProduct(vec1, vec2);
  assert.strictEqual(dotProd, 32, 'Dot product failed');
  console.log('Dot product test passed!');
}

// Test array multiplication and sum
async function testArrayOperations(wasm) {
  const arr1 = new Int32Array([1, 2, 3]);
  const arr2 = new Int32Array([4, 5, 6]);
  
  const multipliedArray = wasm.arrayMultiply(arr1, arr2);
  assert.deepStrictEqual(Array.from(multipliedArray), [4, 10, 18], 'Array multiplication failed');

  const sum = wasm.arraySum(arr1);
  assert.strictEqual(sum, 6, 'Array sum failed');
  
  console.log('Array manipulation tests passed!');
}

// Main function to execute all tests
async function runTests() {
  try {
    const wasm = await loadWasmModule();

    await testMatrixOperations(wasm);
    await testDotProduct(wasm);
    await testArrayOperations(wasm);

    console.log('All tests passed successfully!');
  } catch (error) {
    console.error('Test failed:', error);
  }
}

// Run all tests
runTests();
