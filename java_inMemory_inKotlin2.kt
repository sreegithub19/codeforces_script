import javax.tools.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.net.URL
import java.net.URLClassLoader

// In-memory file manager
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

fun downloadDependency(groupId: String, artifactId: String, version: String, repoUrl: String, targetDir: Path) {
    // Convert groupId from 'org.apache.commons' to 'org/apache/commons'
    val groupIdPath = groupId.replace('.', '/')
    
    val url = "$repoUrl/$groupIdPath/$artifactId/$version/${artifactId}-$version.jar"
    val jarUrl = URL(url)
    val targetFile = targetDir.resolve("${artifactId}-$version.jar")

    // Download the JAR file
    jarUrl.openStream().use { inputStream ->
        Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING)
    }
    println("Downloaded $artifactId version $version to $targetFile")
}

fun main() {
    val javaCode = """
        import org.apache.commons.lang3.StringUtils;
        
        public class Hello {
            public static void greet() {
                String message = "Hello from In-Memory Java!";
                System.out.println(StringUtils.upperCase(message));
            }
        }
    """.trimIndent()

    // Simulated pom.xml content in memory with dependency on commons-lang3
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

    // Access and print the pom.xml content (just for validation)
    val pomFileBytes = fileManager.getPomFile()
    if (pomFileBytes != null) {
        println("In-Memory POM file content:")
        println(String(pomFileBytes))
    } else {
        println("No in-memory POM file found.")
    }

    // Step 1: Download the commons-lang3 JAR
    val targetDir = Path.of("libs")
    Files.createDirectories(targetDir)

    // Maven repository URL
    val mavenRepoUrl = "https://repo.maven.apache.org/maven2"

    // Download commons-lang3 version 3.12.0
    downloadDependency("org.apache.commons", "commons-lang3", "3.12.0", mavenRepoUrl, targetDir)

    // Step 2: Compile Java code with the external dependency
    val fileObject = object : SimpleJavaFileObject(URI.create("string:///Hello.java"), JavaFileObject.Kind.SOURCE) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return javaCode
        }
    }

    val compilationUnits = listOf(fileObject)
    val compilationResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits)?.call()

    if (compilationResult == true) {
        println("Compilation successful!")

        // Step 3: Load the compiled class and add the dependency to the classpath
        val classLoader = URLClassLoader(
            arrayOf(targetDir.resolve("commons-lang3-3.12.0.jar").toUri().toURL()),
            InMemoryJavaFileManager::class.java.classLoader // Fix this part
        )

        // Load the compiled class from memory
        val clazz = classLoader.loadClass("Hello")
        val method: Method = clazz.getMethod("greet")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }
}
