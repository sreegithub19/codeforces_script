import javax.tools.*
import java.lang.reflect.Method
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.io.ByteArrayOutputStream
import javax.tools.JavaFileObject

class InMemoryJavaFileManager(compiler: JavaCompiler) : ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
    private val classBytes = mutableMapOf<String, ByteArray>()

    override fun getJavaFileForOutput(
        location: JavaFileManager.Location,
        className: String,
        kind: JavaFileObject.Kind,
        sibling: FileObject?
    ): JavaFileObject {
        return object : SimpleJavaFileObject(URI.create("bytes://$className"), kind) {
            override fun openOutputStream(): java.io.OutputStream {
                return object : ByteArrayOutputStream() {
                    override fun close() {
                        classBytes[className] = this.toByteArray()
                        super.close()
                    }
                }
            }

            // Implement the missing abstract method
            override fun toUri(): URI {
                return URI.create("bytes://$className")
            }

            // You can implement other methods as needed.
            override fun getNestingKind(): javax.lang.model.element.NestingKind {
                return javax.lang.model.element.NestingKind.TOP_LEVEL
            }

            override fun getAccessLevel(): javax.lang.model.element.Modifier {
                return javax.lang.model.element.Modifier.PUBLIC
            }
        }
    }

    fun getClassBytes(className: String): ByteArray? {
        return classBytes[className]
    }
}

fun main() {
    // Java code to be compiled in-memory
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

    // Use JavaCompiler to compile the Java code dynamically
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()
    val fileManager = InMemoryJavaFileManager(compiler)

    val fileObject = object : SimpleJavaFileObject(URI.create("string:///Hello.java"), JavaFileObject.Kind.SOURCE) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return javaCode
        }
    }

    val compilationUnits = listOf(fileObject)

    val compilationResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits)?.call()
    if (compilationResult == true) {
        println("Compilation successful!")

        // Load the compiled class in-memory
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classBytes = fileManager.getClassBytes(name)
                    ?: throw ClassNotFoundException(name)
                return defineClass(name, classBytes, 0, classBytes.size)
            }
        }

        // Load and invoke the compiled class
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("main", Array<String>::class.java)
        method.invoke(null, arrayOf<String>())
    } else {
        println("Compilation failed!")
    }
}
