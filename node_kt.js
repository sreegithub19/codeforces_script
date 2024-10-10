const { execSync } = require('child_process');
const fs = require('fs');

// Kotlin code as a string
const kotlinCode = `
fun greet(name: String): String {
    return "Hello node, \$name!"
}

fun main() {
    println(greet("World"))
}
`;

// Write the Kotlin code to a temporary file
fs.writeFileSync('temp.kt', kotlinCode);

try {
    // Compile the Kotlin code to a JAR file
    execSync('kotlinc temp.kt -include-runtime -d temp.jar');

    // Run the compiled Kotlin code and capture output
    const output = execSync('java -jar temp.jar', { encoding: 'utf8' });
    
    // Print the output to the console
    console.log(output);
} catch (error) {
    console.error('Error:', error.message);
} finally {
    // Clean up
    fs.unlinkSync('temp.kt');
    fs.unlinkSync('temp.jar');
}
