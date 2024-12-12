const { Volume } = require('memfs');

// Step 1: Simulate multiple virtual files using memfs
const vol = Volume.fromJSON({
  'file1.txt': 'This is the content of file 1.',
  'file2.txt': 'This is the content of file 2.',
  'subdir/file3.txt': 'This is the content of file 3 inside a subdirectory.',
  'file4.txt': 'This is the content of file 4.',
}, '/project');

// Step 2: Interact with the virtual files
// Read content from virtual files
const file1Content = vol.readFileSync('/project/file1.txt', 'utf8');
console.log('file1.txt content:', file1Content);

// Write to a new virtual file
vol.writeFileSync('/project/file5.txt', 'This is a new file 5.');
const file5Content = vol.readFileSync('/project/file5.txt', 'utf8');
console.log('file5.txt content:', file5Content);

// Check if a virtual file exists
const fileExists = vol.existsSync('/project/file2.txt');
console.log('file2.txt exists:', fileExists);

// Step 3: Perform file operations
// Create a new subdirectory and write a file inside it
vol.mkdirSync('/project/newdir');
vol.writeFileSync('/project/newdir/file6.txt', 'This is file 6 inside newdir.');
const file6Content = vol.readFileSync('/project/newdir/file6.txt', 'utf8');
console.log('file6.txt content:', file6Content);

// Step 4: Delete a virtual file
vol.unlinkSync('/project/file4.txt');
const file4Exists = vol.existsSync('/project/file4.txt');
console.log('file4.txt exists after deletion:', file4Exists);

// Step 5: List all files in the virtual file system
const fileList = vol.readdirSync('/project');
console.log('Files in /project:', fileList);

// Step 6: Handle errors (file does not exist)
try {
  vol.readFileSync('/project/nonexistent.txt', 'utf8');
} catch (err) {
  console.error('Error reading nonexistent.txt:', err.message);
}
