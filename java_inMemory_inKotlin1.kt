import java.io.File

fun runShellCommand(command: String) {
    val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
    val process = processBuilder.start()
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
        val command = """jshell --class-path "$libsPath" <<EOF
            import org.apache.commons.lang3.StringUtils;
            
            class MyGreeter {
                public static String greet(String name) {
                    String message = "hello there!!" + name;
                    String capitalized = StringUtils.capitalize(message);
                    return capitalized;
                }
            }

            // Call the greet method and print the result
            System.out.println(MyGreeter.greet("world"));

            EOF
        """

        // Run the command
        runShellCommand(command)
    } else {
        println("Error: 'libs' directory not found or no JAR files present.")
    }
}
