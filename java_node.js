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

////////////////////////////////////

// Define the inline Java code
const mavenCode = `
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>dummy-project</artifactId>
    <version>1.0-SNAPSHOT</version>
</project>

`;

// Write the Java code to a file
fs.writeFileSync('pom.xml', mavenCode.trim());