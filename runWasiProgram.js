const fs = require('fs');
const path = require('path');
const { WASI } = require('wasi');
const { readFileSync } = require('fs');
const { argv, env } = require('process');

const wasi = new WASI({
  args: argv,
  env,
  preopens: {
    '/sandbox': './'
  }
});

const wasmPath = path.join(__dirname, 'wasi_.wasm');
const wasmBytes = readFileSync(wasmPath);

(async () => {
  const { instance } = await WebAssembly.instantiate(wasmBytes, {
    wasi_snapshot_preview1: wasi.wasiImport
  });

  wasi.start(instance);
})();
