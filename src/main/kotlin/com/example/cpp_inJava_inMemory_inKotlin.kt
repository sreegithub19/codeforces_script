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

            import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.util.stream.Collectors;

        public class Hello {
            public static void greet() {
                System.out.println("Hello from In-Memory Java in Kotlin in 2nd file!!");
            }

            public static void executeCppCode(String cppCode) {
                try {
                    // Prepare the command to compile and run the C++ code
                    ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                        "echo '" + cppCode.replace("'", "\\'") + "' | g++ -x c++ -o TempProgram - && ./TempProgram");
                    processBuilder.redirectErrorStream(true);
                    
                    // Start the process and capture the output
                    Process process = processBuilder.start();
                    String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
                    process.waitFor();

                    // Print the output
                    System.out.println("C++ Output:\n" + output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        // Define C++ code and execute it
        val cppCode = """
    #include <iostream>
    #include <fstream>
    #include <string>

    using namespace std;

    int main() {
        ifstream inputFile("src/main/kotlin/com/example/input.txt"); // Open the input file
        if (!inputFile) { // Check if the file opened successfully
            cerr << "Error opening input.txt" << endl;
            return 1; // Exit with error
        }

        string line;
        while (getline(inputFile, line)) { // Read the file line by line
            cout << line << endl; // Print each line
        }

        inputFile.close(); // Close the file

        cout << "Hello from C++! in Kotlin in 2nd file!" << endl;
        return 0; // Exit successfully
    }
""".trimIndent()

        val cppMethod: Method = clazz.getMethod("executeCppCode", String::class.java)
        cppMethod.invoke(null, cppCode)
    } else {
        println("Compilation failed!")
    }
}
