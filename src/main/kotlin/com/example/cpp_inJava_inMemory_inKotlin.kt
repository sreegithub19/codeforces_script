import javax.tools.*
import java.lang.reflect.Method
import java.net.URI
import java.io.*

class InMemoryJavaFileManager(compiler: JavaCompiler) : ForwardingJavaFileManager<JavaFileManager>(
    compiler.getStandardFileManager(null, null, null)
) {
    private val classBytes = mutableMapOf<String, ByteArray>()

    override fun getJavaFileForOutput(location: JavaFileManager.Location, className: String, kind: JavaFileObject.Kind, sibling: FileObject?): JavaFileObject {
        return object : SimpleJavaFileObject(URI.create("bytes://$className"), kind) {
            override fun openOutputStream(): OutputStream {
                return object : ByteArrayOutputStream() {
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
    val cppCode = """
    #include <iostream>
    #include <fstream>
    #include <string>

    int main() {
        std::cout << "Hello, Welcome to the C++ program." << std::endl;
        std::ifstream inputFile("%s"); // Use the absolute path for the file
        if (!inputFile) {
            std::cerr << "Error opening file." << std::endl;
            return 1; // Exit with an error code
        }

        std::string line;
        std::cout << "Contents of input.txt:" << std::endl;
        while (std::getline(inputFile, line)) {
            std::cout << line << std::endl; // Print each line to the console
        }

        inputFile.close(); // Close the file
        return 0;
    }
""".trimIndent()

val javaCode = """
    import java.io.*;
    import java.nio.file.*;
    import java.util.*;

    public class Hello {
        public static void greet() {
            try {
                String currentDir = System.getProperty("user.dir");
                String inputFilePath = Paths.get(currentDir, "input.txt").toString();

                String cppCode = "$cppCode".formatted(inputFilePath); // Use the absolute path

                // Prepare and run the C++ code
                ProcessBuilder builder = new ProcessBuilder();
                builder.command("bash", "-c", "echo \"" + cppCode.replace("\"", "\\\"") + "\" | g++ -x c++ -o hello - && ./hello");
                builder.directory(new File(currentDir));
                builder.redirectErrorStream(true);

                Process process = builder.start();

                // Read the output
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                // Wait for the process to complete
                int exitCode = process.waitFor();
                System.out.println("Process exited with code: " + exitCode);

            } catch (IOException | InterruptedException e) {
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
    } else {
        println("Compilation failed!")
    }
}
