const { exec } = require('child_process');
const fs = require('fs');

// Define the inline Java code
const javaCode = `
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from inline Java code via Node.js!");
    }
}`;

// Write the Java code to a file
fs.writeFileSync('Main.java', javaCode.trim());

// Compile and run the Java file
exec('javac Main.java && java Main', (error, stdout, stderr) => {
    if (error) {
        console.error(`Error: ${error.message}`);
        return;
    }
    if (stderr) {
        console.error(`stderr: ${stderr}`);
        return;
    }
    console.log(`stdout: ${stdout}`);
});
