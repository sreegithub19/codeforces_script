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

// Step 2: Combine the code for compilation
const combinedRustCode = `
${libRustCode}

${mainRustCode}
`;

// Step 3: Use echo and rustc to compile the Rust code from memory (using pipes)
const compileRustCode = (code) => {
  return new Promise((resolve, reject) => {
    const command = `echo "${code}" | rustc -o output_binary -`;
    
    exec(command, (err, stdout, stderr) => {
      if (err) {
        reject(stderr);
        return;
      }
      resolve(stdout);
    });
  });
};

// Step 4: Compile and run the Rust code
compileRustCode(combinedRustCode)
  .then(() => {
    // Step 5: Execute the compiled Rust binary
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
