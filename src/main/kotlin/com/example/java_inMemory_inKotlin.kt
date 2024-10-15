import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import javax.tools.SimpleJavaFileObject
import javax.tools.JavaFileObject
import java.lang.reflect.Method
import java.net.URI

fun main() {
    val javaCode = """
        public class Hello {
            public static void greet() {
                System.out.println("Hello from In-Memory Java!");
            }
        }
    """.trimIndent()

    // Create a Java file object from the code string
    val fileObject = object : SimpleJavaFileObject(
        URI.create("string:///Hello.java"),
        JavaFileObject.Kind.SOURCE
    ) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return javaCode
        }
    }

    // Get the Java compiler
    val compiler: JavaCompiler? = ToolProvider.getSystemJavaCompiler()
    val compilationUnits = listOf(fileObject)

    // Compile the Java code
    val compilationResult = compiler?.getTask(null, null, null, null, null, compilationUnits)?.call()
    if (compilationResult == true) {
        println("Compilation successful!")

        // Load the compiled class
        val classLoader = Thread.currentThread().contextClassLoader
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("greet")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }
}
