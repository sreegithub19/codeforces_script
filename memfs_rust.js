const { exec } = require('child_process');
const { Volume } = require('memfs');

// Simulate the file system in memory using memfs
const vol = Volume.fromJSON({
  'src/main.rs': `
fn main() {
    println!("Hello from main!");
    my_library::hello();
}
`,
  'src/lib.rs': `
pub fn hello() {
    println!("Hello from lib!");
}
`
}, '/project');

// Step 1: Read the Rust code from the in-memory filesystem
const mainRustCode = vol.readFileSync('/project/src/main.rs', 'utf8');
const libRustCode = vol.readFileSync('/project/src/lib.rs', 'utf8');

// Step 2: Compile Rust code using rustc, passing the code directly (no physical files)
const compileRustCode = (code, filename) => {
  return new Promise((resolve, reject) => {
    const rustc = exec('rustc -x rust -o output_binary -', { input: code }, (err, stdout, stderr) => {
      if (err) {
        reject(stderr);
        return;
      }
      resolve(stdout);
    });
  });
};

// Step 3: Compile the library (lib.rs) and main.rs files using rustc
Promise.all([
  compileRustCode(libRustCode, 'lib.rs'),
  compileRustCode(mainRustCode, 'main.rs')
])
  .then(() => {
    // Step 4: Run the compiled Rust binary
    exec('./output_binary', (err, stdout, stderr) => {
      if (err) {
        console.error(`Error running Rust binary: ${stderr}`);
        return;
      }
      console.log('Rust program output:');
      console.log(stdout);
    });
  })
  .catch((err) => {
    console.error(`Error compiling Rust code: ${err}`);
  });
