import javax.tools.*
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class InMemoryJavaFileManager(compiler: JavaCompiler) : ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
    private val classBytes = mutableMapOf<String, ByteArray>()
    private val pomFiles = mutableMapOf<String, ByteArray>()

    // Handle in-memory pom.xml file as if it's part of the project
    fun addPomFile(pomContent: String) {
        pomFiles["pom.xml"] = pomContent.toByteArray()
    }

    // Handle retrieving in-memory pom.xml file
    fun getPomFile(): ByteArray? {
        return pomFiles["pom.xml"]
    }

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
    // Java code for the example class
    val javaCode = """
        public class Hello {
            public static void greet() {
                System.out.println("Hello from In-Memory Java with pom.xml!");
            }
        }
    """.trimIndent()

    // Simulated pom.xml content in memory
    val pomXmlContent = """
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
            <groupId>com.example</groupId>
            <artifactId>hello-world</artifactId>
            <version>1.0-SNAPSHOT</version>
            <dependencies>
                <dependency>
                    <groupId>org.apache.commons</groupId>
                    <artifactId>commons-lang3</artifactId>
                    <version>3.12.0</version>
                </dependency>
            </dependencies>
        </project>
    """.trimIndent()

    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()
    val fileManager = InMemoryJavaFileManager(compiler)

    // Add the pom.xml content to the in-memory file manager
    fileManager.addPomFile(pomXmlContent)

    // Access and print the pom.xml content
    val pomFileBytes = fileManager.getPomFile()
    if (pomFileBytes != null) {
        println("In-Memory POM file content:")
        println(String(pomFileBytes))
    } else {
        println("No in-memory POM file found.")
    }

    // Compile Java code
    val fileObject = object : SimpleJavaFileObject(URI.create("string:///Hello.java"), JavaFileObject.Kind.SOURCE) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return javaCode
        }
    }

    val compilationUnits = listOf(fileObject)
    val compilationResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits)?.call()

    if (compilationResult == true) {
        println("Compilation successful!")

        // Load the compiled class from memory
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classBytes = fileManager.getClassBytes(name)
                    ?: throw ClassNotFoundException(name)
                return defineClass(name, classBytes, 0, classBytes.size)
            }
        }

        // Invoke the greet method from the compiled Hello class
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("greet")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }
}
