import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader

fun main() {
    // Define the Java code for JShell input
    val jshellCode = """
        import org.apache.commons.lang3.StringUtils;

        class MyGreeter {
            public static String greet(String name) {
                String message = \"""
                hello there again from Kotlin, 
                \""" + name;
                String capitalized = StringUtils.capitalize(message);
                return capitalized;
            }
        }

        // Call the greet method and print the result
        System.out.println(MyGreeter.greet("world"));
    """.trimIndent()

    // Run the JShell process with the defined classpath and input code
    runJShell(jshellCode)
}

fun runJShell(jshellCode: String) {
    // Build the command to run JShell with the appropriate classpath
    val command = listOf(
        "jshell",
        "--class-path", "libs/*"  // Point to Maven dependencies (libs folder)
    )

    // Initialize the ProcessBuilder with the command and input the JShell code
    val processBuilder = ProcessBuilder(command)
    processBuilder.redirectErrorStream(true) // Combine stdout and stderr

    // Start the process and provide the JShell input via stdin
    val process = processBuilder.start()

    // Write the JShell code into the process's stdin
    val writer = process.outputStream.bufferedWriter()
    writer.write(jshellCode)
    writer.flush()
    writer.close()

    // Capture and print the output from the process
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String? = reader.readLine()
    while (line != null) {
        println(line)  // Print each line of the output
        line = reader.readLine()
    }

    // Wait for the process to complete and check if it was successful
    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("JShell execution completed successfully.")
    } else {
        println("JShell execution failed with exit code: $exitCode")
    }
}
