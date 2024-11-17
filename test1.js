const fs = require('fs');
const assert = require('assert');

// Define the imports required by the WebAssembly module
const imports = {
  env: {
    memory: new WebAssembly.Memory({ initial: 256, maximum: 256 }),
    abort: () => {
      console.error("Abort called in WebAssembly module!");
    },
    log: (msg) => console.log(msg),
  },
};

// Load and instantiate the WebAssembly module
async function loadWasmModule() {
  const wasmBuffer = fs.readFileSync('./build/example.wasm');
  const wasmModule = await WebAssembly.compile(wasmBuffer);
  const wasmInstance = await WebAssembly.instantiate(wasmModule, imports);

  // Log exports to inspect what is available
  console.log('WASM Exports:', wasmInstance.exports);

  return wasmInstance.exports;
}

// Test matrix creation and operations
async function testMatrixOperations(wasm) {
  // Log matrix-related exports to check what we are working with
  if (wasm.Matrix) {
    console.log('Matrix found:', wasm.Matrix);
  } else {
    console.log('Matrix not found in exports');
  }

  try {
    // Check if Matrix is a constructor function or a factory function
    if (typeof wasm.Matrix === 'function') {
      const matrixA = wasm.Matrix(1, 2, 3, 4);  // Factory function case
      const matrixB = wasm.Matrix(5, 6, 7, 8);

      const addedMatrix = matrixA.add(matrixB);
      assert.deepStrictEqual([addedMatrix.a, addedMatrix.b, addedMatrix.c, addedMatrix.d], [6, 8, 10, 12], 'Matrix addition failed');

      const multipliedMatrix = matrixA.multiply(matrixB);
      assert.deepStrictEqual([multipliedMatrix.a, multipliedMatrix.b, multipliedMatrix.c, multipliedMatrix.d], [19, 22, 43, 50], 'Matrix multiplication failed');

      console.log('Matrix operations passed!');
    } else {
      console.log('Matrix is not a constructor or factory function');
    }
  } catch (error) {
    console.error('Matrix operation test failed:', error);
  }
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
