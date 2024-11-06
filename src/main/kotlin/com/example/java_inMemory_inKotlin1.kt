import java.io.File
import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import java.lang.reflect.Method

fun main() {
    // Step 1: Java code to be compiled (as a string)
    val javaCode = """
    import org.apache.commons.lang3.StringUtils;
    
    public class Hello {
        public static void main(String[] args) {
            String message = "hello from dynamically compiled java again here!" + " : dsgsdgsg";
            String capitalizedMessage = StringUtils.capitalize(message);
            System.out.println(capitalizedMessage);
        }
    }
""".trimIndent()

    // Step 2: Compile and run the Java code with Maven dependencies in classpath
    compileAndRunWithClasspath(javaCode)
}

fun compileAndRunWithClasspath(javaCode: String) {
    // Step 3: Get Maven dependencies classpath
    val classpath = getClasspathFromMaven()

    // Step 4: Get the Java compiler
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()

    // Step 5: Prepare in-memory file manager
    val fileManager = compiler.getStandardFileManager(null, null, null)

    // Step 6: Create virtual file for the source code
    val file = JavaSourceFromString("Hello", javaCode)

    // Step 7: Compile the source code with classpath
    val compilationUnits = listOf(file)
    val task = compiler.getTask(null, fileManager, null, listOf("-cp", classpath), null, compilationUnits)

    val success = task.call()

    if (success) {
        // Step 8: Dynamically load the compiled class (from memory)
        val cls = Class.forName("Hello")

        // Step 9: Invoke the 'greet' method
        val method: Method = cls.getDeclaredMethod("greet")
        method.invoke(null) // Static method, so we pass null
    } else {
        println("Compilation failed.")
    }
}

// Get the classpath string of Maven dependencies (in 'libs' directory)
fun getClasspathFromMaven(): String {
    val classpath = StringBuilder()
    val libsDir = File("libs")
    val libs = libsDir.listFiles { _, name -> name.endsWith(".jar") }

    libs?.forEach { lib ->
        classpath.append(lib.absolutePath).append(File.pathSeparator)
    }

    return classpath.toString()
}

// Custom JavaFileObject to represent source code in memory
class JavaSourceFromString(name: String, private val code: String) : javax.tools.SimpleJavaFileObject(
    java.net.URI.create("string:///$name.java"), javax.tools.JavaFileObject.Kind.SOURCE
) {
    override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence = code
}
