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
        val tripleQuotes = "\"\"\""
        val script = """
    import org.apache.commons.lang3.StringUtils;
    
    class MyGreeter {
        public static String greet(String name) {
             String message = $tripleQuotes
             hello there!!
             $tripleQuotes;
            String combinedMessage = message + name;
            String capitalized = StringUtils.capitalize(combinedMessage);

                String command = "print('2 from Python!')";

                try {
                    // Execute the command directly using Runtime.exec()
                    Process process = Runtime.getRuntime().exec(new String[] { "python", "-c", command });

                    // Capture and print the output of the shell command
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Wait for the process to finish and get the exit code
                    int exitCode = process.waitFor();
                    System.out.println("Command executed with exit code: " + exitCode);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            return capitalized;
        }
    }

    // Call the greet method and print the result
    System.out.println(MyGreeter.greet("world"));
    """
    
    

        // Prepare the full jshell command and input
        val command = listOf("jshell","--class-path", classpath)

        // Run the command and pass the script to jshell
        runShellCommand(command.joinToString(" "), script)
        val script_ = """
            This is a raw string containing triple quotes:
            $tripleQuotes
            End of script
        """
        println(script_)
    } else {
        println("Error: 'libs' directory not found or no JAR files present.")
    }
}
