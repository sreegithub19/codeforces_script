import java.io.File
import java.io.OutputStreamWriter
import java.io.BufferedWriter

fun runShellCommand(command: String, input: String) {
    val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

    val process = processBuilder.start()

    // Send the script to jshell via standard input
    val writer = BufferedWriter(OutputStreamWriter(process.outputStream))
    writer.write(input)
    writer.flush()  // Ensure the input is sent to jshell
    writer.close()

    val exitCode = process.waitFor()
    println("Process exited with code: $exitCode")
}

fun main() {
    // Get the absolute path for the 'libs/*' pattern
    val libsDir = File("libs")
    val libsPath = if (libsDir.exists() && libsDir.isDirectory) {
        libsDir.listFiles { _, name -> name.endsWith(".jar") }?.joinToString(":") { it.absolutePath }
    } else {
        null
    }

    // If libs/*.jar exists, create the classpath string for jshell
    if (libsPath != null) {
        // Remove extra quotes in the classpath (if any)
        val classpath = libsPath.replace("\"", "")

        // JShell script content to be passed as input
        val script = """import org.apache.commons.lang3.StringUtils;
            
            class MyGreeter {
                public static String greet(String name) {
                    String message = \"\"\"hello there!!\"\"\" + name;
                    String capitalized = StringUtils.capitalize(message);
                    return capitalized;
                }
            }

            // Call the greet method and print the result
            System.out.println(MyGreeter.greet("world"));
        """

        // Prepare the full jshell command and input
        val command = listOf("jshell", "--class-path", classpath)

        // Run the command and pass the script to jshell
        runShellCommand(command.joinToString(" "), script)
    } else {
        println("Error: 'libs' directory not found or no JAR files present.")
    }
}
