import javax.tools.*
import java.lang.reflect.Method
import java.net.URI

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

        // Load the compiled class
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classBytes = fileManager.getClassBytes(name)
                    ?: throw ClassNotFoundException(name)
                return defineClass(name, classBytes, 0, classBytes.size)
            }
        }
        
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("greet")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }
}
