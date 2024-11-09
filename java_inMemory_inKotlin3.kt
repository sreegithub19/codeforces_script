import java.io.File
import java.io.OutputStreamWriter
import java.io.BufferedWriter


fun runShellCommand(commandTemplate: String,script:String) {
    // Replace placeholders in the commandTemplate with the actual script and classpath
    val command = commandTemplate;

    // Split the command into arguments for ProcessBuilder
    val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

    // Start the process
    val process = processBuilder.start()

    // Send the script to jshell via standard input
    val writer = BufferedWriter(OutputStreamWriter(process.outputStream))
    writer.write(script)  // The script is passed to jshell through standard input
    //writer.flush()  // Ensure the input is sent to jshell
    writer.close()

    // Wait for the process to complete and get the exit code
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
        val script = """import org.apache.commons.lang3.StringUtils;
    
    System.out.println("the world!"));
    """
    
    // Prepare the full jshell command with echo
    val command = "echo '"+script+"' | jshell --class-path "+classpath+" --startup /dev/stdin"

    // Run the command and pass the script to jshell
    runShellCommand(command,script)
} 

}
