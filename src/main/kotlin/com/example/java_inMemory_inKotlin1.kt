import java.io.File
import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import java.lang.reflect.Method
import java.io.ByteArrayOutputStream
import javax.tools.JavaFileObject
import javax.tools.ForwardingJavaFileManager
import javax.tools.StandardLocation
import java.util.*

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
    val fileManager = object : ForwardingJavaFileManager<javax.tools.JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
        private val byteArrayOutputStream = ByteArrayOutputStream()
        
        override fun getJavaFileForOutput(
            location: StandardLocation?,
            className: String?,
            kind: JavaFileObject.Kind?,
            sibling: JavaFileObject?
        ): JavaFileObject {
            // Return an in-memory file object to capture the compiled bytecode
            return object : JavaFileObject {
                override fun getName(): String = className ?: ""
                override fun openOutputStream(): ByteArrayOutputStream = byteArrayOutputStream
                override fun openReader(ignoreEncodingErrors: Boolean): java.io.Reader = throw UnsupportedOperationException()
                override fun openInputStream(): java.io.InputStream = throw UnsupportedOperationException()
                override fun delete(): Boolean = false
                override fun isNameCompatible(name: String?, kind: JavaFileObject.Kind?): Boolean = true
                override fun getKind(): JavaFileObject.Kind = JavaFileObject.Kind.CLASS
                override fun toUri(): java.net.URI = URI.create("byte:///$className")
            }
        }

        // Return the compiled bytecode as a map
        fun getByteArray(): ByteArray = byteArrayOutputStream.toByteArray()
    }

    // Step 6: Create virtual file for the source code
    val file = JavaSourceFromString("Hello", javaCode)

    // Step 7: Compile the source code with classpath
    val compilationUnits = listOf(file)
    val task = compiler.getTask(null, fileManager, null, listOf("-cp", classpath), null, compilationUnits)

    val success = task.call()

    if (success) {
        // Step 8: Dynamically load the compiled class (from memory)
        val classData = fileManager.getByteArray()
        val cls = loadClassFromByteArray("Hello", classData)

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

// Load class from the compiled bytecode (using a custom class loader)
fun loadClassFromByteArray(className: String, classData: ByteArray): Class<*> {
    val classLoader = object : ClassLoader() {
        override fun findClass(name: String): Class<*> {
            if (name == className) {
                return defineClass(name, classData, 0, classData.size)
            }
            return super.findClass(name)
        }
    }
    return classLoader.loadClass(className)
}
