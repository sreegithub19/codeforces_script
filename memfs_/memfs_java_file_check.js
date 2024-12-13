const { vol } = require('memfs');  // In-memory file system module
const { exec } = require('child_process');
const path = require('path');

// Create an in-memory file system with a simple Java program
vol.fromJSON({
  '/HelloWorld.java': `
    public class HelloWorld {
      public static void main(String[] args) {
        System.out.println("Hello, world from in-memory Java!");
      }
    }
  `
});

// Check if the file exists in the virtual file system
if (vol.existsSync('/HelloWorld.java')) {
  console.log('Java file exists in memory!');
} else {
  console.log('Java file does not exist!');
}
