const { execSync } = require('child_process');
const { Volume } = require('memfs');

// Simulate the file system with multiple Python files in memory
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

// Write the Python files into the in-memory virtual file system
const mainPythonCode = vol.readFileSync('/project/main.py', 'utf8');
const modulePythonCode = vol.readFileSync('/project/module.py', 'utf8');

// Function to execute Python file synchronously via execSync
const executePythonFile = (filePath) => {
  try {
    // Check which Python command to use (could be python3 on some systems)
    const pythonCommand = `python3 ${filePath}`;  // Use python3 if python is not working.

    // Ensure we use the correct shell (`/bin/sh` should exist on macOS)
    const result = execSync(pythonCommand, {
      cwd: '/project',
      encoding: 'utf8',
      shell: '/bin/sh',  // macOS should have /bin/sh, so ensure it's used explicitly.
    });

    console.log(result);
  } catch (err) {
    console.error('Error executing Python code:', err.stderr || err.message);
  }
};

// Execute the Python files in memory
const executeFilesInMemory = () => {
  // Write the Python code to in-memory files
  vol.writeFileSync('/project/module.py', modulePythonCode);
  vol.writeFileSync('/project/main.py', mainPythonCode);

  // Execute module.py first
  console.log('Executing module.py:');
  executePythonFile('/project/module.py');

  // Execute main.py, which imports and calls module.py
  console.log('\nExecuting main.py:');
  executePythonFile('/project/main.py');
};

// Run the entire process
executeFilesInMemory();
