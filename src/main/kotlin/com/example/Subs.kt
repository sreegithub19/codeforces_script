import java.io.File
import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader

fun main() {
    val javaCode = """
        public class Hello {
            public static void greet() {
                System.out.println("Hello from Java!");
            }
        }
    """.trimIndent()

    // Save Java code to a temporary file
    val javaFile = File("Hello.java")
    javaFile.writeText(javaCode)

    // Get the Java compiler
    val compiler: JavaCompiler? = ToolProvider.getSystemJavaCompiler()
    
    // Compile the Java file
    val compilationResult = compiler?.run(null, null, null, javaFile.absolutePath)
    if (compilationResult == 0) {
        println("Compilation successful!")

        // Load the compiled class
        val classLoader = URLClassLoader(arrayOf(File(".").toURI().toURL()))
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("greet")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }

    // Clean up
    javaFile.delete()
}
