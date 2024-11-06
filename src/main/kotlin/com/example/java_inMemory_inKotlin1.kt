import javax.tools.*
import java.io.File
import java.lang.reflect.Method
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

class InMemoryJavaFileManager(compiler: JavaCompiler) : ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
    private val classBytes = mutableMapOf<String, ByteArray>()

    override fun getJavaFileForOutput(location: JavaFileManager.Location, className: String, kind: JavaFileObject.Kind, sibling: FileObject?): JavaFileObject {
        return object : SimpleJavaFileObject(URI.create("bytes://$className"), kind) {
            override fun openOutputStream(): java.io.OutputStream {
                return object : java.io.ByteArrayOutputStream() {
                    override fun close() {
                        classBytes[className] = this.toByteArray()
                        super.close()
                    }
                }
            }
        }
    }

    fun getClassBytes(className: String): ByteArray? {
        return classBytes[className]
    }
}

fun main() {
    // Step 1: Download Maven dependencies (e.g., commons-lang3)
    val mavenDependenciesDir = "libs"
    val mavenCmd = "mvn dependency:copy-dependencies -DoutputDirectory=$mavenDependenciesDir -DincludeScope=runtime"

    val process = ProcessBuilder(mavenCmd.split(" "))
        .directory(File("."))
        .inheritIO()
        .start()

    val exitCode = process.waitFor()
    if (exitCode != 0) {
        println("Maven dependency download failed!")
        return
    }

    // Step 2: Prepare Java code
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

    // Step 3: Compile the Java code dynamically
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

        // Step 4: Load the compiled class
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classBytes = fileManager.getClassBytes(name)
                    ?: throw ClassNotFoundException(name)
                return defineClass(name, classBytes, 0, classBytes.size)
            }
        }

        // Step 5: Load the Hello class and invoke the main method
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("main", Array<String>::class.java)
        method.invoke(null, arrayOf<String>())
    } else {
        println("Compilation failed!")
    }
}
