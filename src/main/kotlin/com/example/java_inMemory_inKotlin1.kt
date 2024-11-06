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
    runShellCommand("""echo 'Hello from \"\"\" abd \"\"\" Kotlin Shell Script!'
    """)

    val command = """jshell --class-path "libs/*" <<EOF
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

    runShellCommand(command)
}
