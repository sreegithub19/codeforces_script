import org.apache.maven.resolver.api.RepositorySystem
import org.apache.maven.resolver.internal.impl.DefaultRepositorySystem
import org.apache.maven.resolver.api.resolution.ArtifactResolutionRequest
import org.apache.maven.resolver.api.resolution.ArtifactResolutionResult
import org.apache.maven.resolver.api.resolution.ArtifactResolutionException
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.repository.RemoteRepository
import org.apache.maven.resolver.api.Artifact
import org.apache.maven.resolver.internal.DefaultRepositorySystemSession
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.ByteArrayInputStream
import java.io.File
import javax.tools.*
import java.net.URI
import java.lang.reflect.Method

// In-memory classloader and JavaFileManager to compile code
class InMemoryJavaFileManager(compiler: JavaCompiler) :
    ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
    private val classBytes = mutableMapOf<String, ByteArray>()

    override fun getJavaFileForOutput(
        location: JavaFileManager.Location,
        className: String,
        kind: JavaFileObject.Kind,
        sibling: FileObject?
    ): JavaFileObject {
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

// Function to resolve dependencies from in-memory pom.xml
fun resolveDependenciesFromPomXml(pomXml: String): List<File> {
    val repositorySystem: RepositorySystem = DefaultRepositorySystem()

    // Local repository for Aether to download dependencies to
    val localRepo = File(System.getProperty("user.home"), ".m2/repository")
    val remoteRepo = RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2").build()

    val session = DefaultRepositorySystemSession()
    session.localRepositoryManager = repositorySystem.newLocalRepositoryManager(session, localRepo)

    // Parse the POM XML from the in-memory string
    val modelReader = MavenXpp3Reader()
    val model = modelReader.read(ByteArrayInputStream(pomXml.toByteArray()))

    // Collect dependencies from the POM
    val dependencies = model.dependencies
    val resolvedFiles = mutableListOf<File>()
    for (dependency in dependencies) {
        val artifact = DefaultArtifact("${dependency.groupId}:${dependency.artifactId}:${dependency.version}")
        val request = ArtifactResolutionRequest().setArtifact(artifact).setRepositories(listOf(remoteRepo))
        try {
            val resolutionResult: ArtifactResolutionResult = repositorySystem.resolve(request)
            resolvedFiles.add(resolutionResult.artifacts.first().file)
        } catch (e: ArtifactResolutionException) {
            println("Failed to resolve: ${dependency.artifactId}:${dependency.version}")
        }
    }

    return resolvedFiles
}

// Function to compile and run Java code in-memory
fun compileAndRunJavaCode(javaCode: String, classpath: List<File>) {
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()

    // Convert the classpath files to string format
    val classpathString = classpath.joinToString(":") { it.absolutePath }

    // In-memory Java file manager for compilation
    val fileManager = InMemoryJavaFileManager(compiler)
    val javaFileObject = object : SimpleJavaFileObject(URI.create("string:///Hello.java"), JavaFileObject.Kind.SOURCE) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return javaCode
        }
    }

    val compilationUnits = listOf(javaFileObject)

    // Compile Java code in-memory
    val result = compiler.getTask(
        null, fileManager, null, listOf("-cp", classpathString), null, compilationUnits
    )?.call()

    if (result == true) {
        // Load and invoke the compiled class in memory
        val classLoader = object : ClassLoader() {
            override fun findClass(name: String): Class<*> {
                val classBytes = fileManager.getClassBytes(name) ?: throw ClassNotFoundException(name)
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

// Main function to simulate the process
fun main() {
    // Example in-memory pom.xml content with the Apache Commons Lang dependency
    val pomXml = """
        <project xmlns="http://maven.apache.org/POM/4.0.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
            <groupId>com.example</groupId>
            <artifactId>dynamic-java</artifactId>
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

    // Resolve dependencies from the in-memory pom.xml
    val dependencies = resolveDependenciesFromPomXml(pomXml)

    // Example Java code to compile and execute (using Commons Lang)
    val javaCode = """
        import org.apache.commons.lang3.StringUtils;

        public class Hello {
            public static void greet() {
                String message = "hello from dynamically compiled Java code with dependencies!";
                String capitalizedMessage = StringUtils.capitalize(message);
                System.out.println(capitalizedMessage);
            }

            public static void main(String[] args) {
                greet();
            }
        }
    """.trimIndent()

    // Compile and execute Java code
    compileAndRunJavaCode(javaCode, dependencies)
}
