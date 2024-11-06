import java.io.File
import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import java.lang.reflect.Method
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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
        val cls = loadClassFromMemory("Hello")

        // Step 9: Invoke the 'main' method
        val method: Method = cls.getDeclaredMethod("main", Array<String>::class.java)
        method.invoke(null, arrayOf<String>()) // No arguments for main method
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

// Custom ClassLoader to load the class from byte array in memory
class InMemoryClassLoader(private val classData: Map<String, ByteArray>) : ClassLoader() {
    override fun findClass(name: String): Class<*> {
        val classBytes = classData[name] ?: throw ClassNotFoundException(name)
        return defineClass(name, classBytes, 0, classBytes.size)
    }
}

// Load the compiled class from memory (using custom ClassLoader)
fun loadClassFromMemory(className: String): Class<*> {
    // Retrieve compiled bytecode for the Hello class
    val classData = compileJavaCodeToByteArray()

    // Create a class loader that can load the compiled class from byte array
    val classLoader = InMemoryClassLoader(classData)

    return classLoader.loadClass(className)
}

// Compile the Java code to a byte array and return the class data
fun compileJavaCodeToByteArray(): Map<String, ByteArray> {
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()

    // Output stream to capture the compiled bytecode
    val byteArrayOutputStream = ByteArrayOutputStream()

    // Step 1: Prepare in-memory file manager for the compiled bytecode
    val fileManager = object : javax.tools.ForwardingJavaFileManager<javax.tools.JavaFileManager>(
        compiler.getStandardFileManager(null, null, null)
    ) {
        override fun getJavaFileForOutput(
            location: javax.tools.StandardLocation,
            className: String,
            kind: javax.tools.JavaFileObject.Kind,
            sibling: javax.tools.FileObject?
        ): javax.tools.JavaFileObject {
            return object : javax.tools.SimpleJavaFileObject(
                java.net.URI.create("byte:///$className"),
                kind
            ) {
                override fun openOutputStream(): java.io.OutputStream {
                    return byteArrayOutputStream
                }
            }
        }
    }

    // Step 2: Create the JavaSourceFromString file object
    val javaFileObject = JavaSourceFromString("Hello", javaCode.trimIndent())

    // Step 3: Compile and write to byte array output stream
    val task = compiler.getTask(null, fileManager, null, listOf("-cp", getClasspathFromMaven()), null, listOf(javaFileObject))
    val success = task.call()

    if (success) {
        return mapOf("Hello" to byteArrayOutputStream.toByteArray())
    } else {
        throw RuntimeException("Java compilation failed.")
    }
}
