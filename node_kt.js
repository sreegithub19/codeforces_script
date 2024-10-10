const { execSync } = require('child_process');
const fs = require('fs');

// Kotlin code as a string
const kotlinCode = `
fun greet(name: String): String {
    return "Hello node, \\$name!"
}

println(greet("World"))
`;

// Write the Kotlin code to a temporary file
fs.writeFileSync('temp.kt', kotlinCode);

// Compile the Kotlin code to JavaScript
execSync('kotlinc temp.kt -include-runtime -d temp.jar');

// Run the compiled Kotlin code
execSync('java -jar temp.jar');

// Clean up
fs.unlinkSync('temp.kt');
fs.unlinkSync('temp.jar');
