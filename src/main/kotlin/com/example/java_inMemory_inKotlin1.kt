import java.io.File
import java.io.ByteArrayOutputStream
import java.lang.reflect.Method
import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject
import javax.tools.StandardLocation
import javax.tools.ForwardingJavaFileManager
import java.net.URI

fun main() {
    // Java code to be compiled (as a string)
    val javaCode = """
        import org.apache.commons.lang3.StringUtils;
        
        public class Hello {
            public static void main(String[] args) {
                String message = "Hello from dynamically compiled Java!";
                String capitalizedMessage = StringUtils.capitalize(message);
                System.out.println(capitalizedMessage);
            }
        }
    """.trimIndent()

    // Compile and run the Java code with Maven dependencies in classpath
    compileAndRunWithClasspath(javaCode)
}

fun compileAndRunWithClasspath(javaCode: String) {
    // Step 1: Get the classpath string of Maven dependencies (in 'libs' directory)
    val classpath = getClasspathFromMaven()

    // Step 2: Get the Java compiler
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()

    // Step 3: Prepare in-memory file manager
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
                override fun toUri(): URI = URI.create("byte:///$className")
                override fun getNestingKind(): javax.tools.JavaFileObject.NestingKind = javax.tools.JavaFileObject.NestingKind.TOP_LEVEL
                override fun getAccessLevel(): javax.lang.model.element.Modifier = javax.lang.model.element.Modifier.PUBLIC
            }
        }

        // Return the compiled bytecode as a byte array
        fun getByteArray(): ByteArray = byteArrayOutputStream.toByteArray()
    }

    // Step 4: Create virtual file for the source code
    val file = JavaSourceFromString("Hello", javaCode)

    // Step 5: Compile the source code with classpath
    val compilationUnits = listOf(file)
    val task = compiler.getTask(null, fileManager, null, listOf("-cp", classpath), null, compilationUnits)

    val success = task.call()

    if (success) {
        // Step 6: Dynamically load the compiled class (from memory)
        val classData = fileManager.getByteArray()
        val cls = loadClassFromByteArray("Hello", classData)

        // Step 7: Invoke the 'main' method
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
class JavaSourceFromString(name: String, private val code: String) : SimpleJavaFileObject(
    URI.create("string:///$name.java"), JavaFileObject.Kind.SOURCE
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
