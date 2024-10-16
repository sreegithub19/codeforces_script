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

fun executeCppCode(cppCode: String) {
    // Prepare the command to compile and run the C++ code
    val processBuilder = ProcessBuilder("bash", "-c", """
        echo '$cppCode' | g++ -x c++ -o TempProgram - && ./TempProgram
    """.trimIndent())
        .redirectErrorStream(true)

    // Start the process and capture the output
    val process = processBuilder.start()
    val output = process.inputStream.bufferedReader().readText()
    process.waitFor()

    // Print the output
    println("C++ Output:\n$output")
}

fun main() {
    val javaCode = """
        public class Hello {
            public static void greet() {
                System.out.println("Hello from In-Memory Java in Kotlin!!");
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

        // Define and execute C++ code
        val cppCode = """
            #include <iostream>
            using namespace std;
            int main() {
                cout << "Hello from C++! in Kotlin!" << endl;
                return 0;
            }
        """.trimIndent()

        executeCppCode(cppCode)
    } else {
        println("Compilation failed!")
    }
}
