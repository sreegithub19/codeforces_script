const { exec } = require('child_process');
const { Volume } = require('memfs');

// Step 1: Simulate the file system with multiple Python files in memory
const vol = Volume.fromJSON({
  'main.py': `
import module

def main():
    print("This is the main file.")
    module.greet()

if __name__ == "__main__":
    main()
`,
  'module.py': `
def greet():
    print("Hello from the module!")
`
}, '/project');

// Step 2: Write the Python files into the in-memory virtual file system
const mainPythonCode = vol.readFileSync('/project/main.py', 'utf8');
const modulePythonCode = vol.readFileSync('/project/module.py', 'utf8');

// Step 3: Write the Python code into files for execution (using child_process)
const executePythonCode = (code, filePath) => {
  return new Promise((resolve, reject) => {
    const pythonCommand = `python -c "${code}"`;

    exec(pythonCommand, (err, stdout, stderr) => {
      if (err) {
        reject(stderr);
        return;
      }
      resolve(stdout);
    });
  });
};

// Step 4: Execute the Python code
const executeFilesInMemory = async () => {
  try {
    // Step 4.1: Execute the module first (this could also be combined in the main.py)
    console.log('Executing module.py:');
    await executePythonCode(modulePythonCode, '/project/module.py');

    // Step 4.2: Execute the main.py, which imports and calls the module
    console.log('\nExecuting main.py:');
    const output = await executePythonCode(mainPythonCode, '/project/main.py');
    console.log(output);
  } catch (err) {
    console.error('Error executing Python code:', err);
  }
};

// Step 5: Run the entire process
executeFilesInMemory();
