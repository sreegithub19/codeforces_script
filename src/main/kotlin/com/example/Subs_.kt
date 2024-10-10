import javax.tools.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.reflect.KFunction

fun main() {
    val javaCode = """
        public class Hello {
            public static void main() {
                System.out.println("Hello from Java!");
            }
        }
    """.trimIndent()

    // Prepare the compiler
    val compiler = ToolProvider.getSystemJavaCompiler()
    val diagnosticListener = DiagnosticCollector<JavaFileObject>()
    val outputStream = ByteArrayOutputStream()
    val printStream = PrintStream(outputStream)

    // Set up in-memory file manager
    val fileManager = object : ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(diagnosticListener, null, null)) {
        val classBytes = mutableMapOf<String, ByteArray>()

        override fun getJavaFileForOutput(location: StandardLocation, className: String, kind: JavaFileObject.Kind, sibling: FileObject?): JavaFileObject {
            return object : SimpleJavaFileObject(URI.create("string:///${className.replace('.', '/')}.class"), JavaFileObject.Kind.CLASS) {
                override fun openOutputStream(): OutputStream {
                    return ByteArrayOutputStream().also { baos ->
                        classBytes[className] = baos.toByteArray()
                    }
                }
            }
        }
    }

    // Compile the code
    val compilationUnits = listOf(JavaSourceFromString("Hello", javaCode))
    val result = compiler.getTask(printStream, fileManager, diagnosticListener, null, null, compilationUnits).call()

    if (result) {
        println("Compilation successful!")

        // Load the compiled class
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classData = fileManager.classBytes[name] ?: throw ClassNotFoundException(name)
                return defineClass(name, classData, 0, classData.size)
            }
        }

        val clazz = classLoader.loadClass("Hello")
        val method: KFunction<*> = clazz.getMethod("main") as KFunction<*>
        method.call()
    } else {
        println("Compilation failed! Errors:")
        diagnosticListener.diagnostics.forEach { println(it.getMessage(null)) }
    }
}

// Custom Java source file object for in-memory compilation
class JavaSourceFromString(private val name: String, private val code: String) : SimpleJavaFileObject(URI.create("string:///$name.java"), JavaFileObject.Kind.SOURCE) {
    override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence = code
}
