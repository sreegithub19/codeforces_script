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
    // Example shell command (you can replace this with any shell command or script)
    runShellCommand("""echo 'Hello from Kotlin Shell Script!'
    """)
}
