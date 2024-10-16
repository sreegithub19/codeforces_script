import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class InMemoryKotlinFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, byte[]> classBytes = new HashMap<>();

    protected InMemoryKotlinFileManager(JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        return new SimpleJavaFileObject(URI.create("bytes:///" + className), kind) {
            @Override
            public OutputStream openOutputStream() {
                return new ByteArrayOutputStream() {
                    @Override
                    public void close() {
                        classBytes.put(className, this.toByteArray());
                        super.close();
                    }
                };
            }
        };
    }

    public byte[] getClassBytes(String className) {
        return classBytes.get(className);
    }
}

public class kotlin_in_java {
    public static void main(String[] args) {
        String kotlinCode = "fun greet() { println(\"Hello from in-memory Kotlin code!\") }";

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        InMemoryKotlinFileManager fileManager = new InMemoryKotlinFileManager(compiler);

        JavaFileObject fileObject = new SimpleJavaFileObject(URI.create("string:///Hello.kt"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return kotlinCode;
            }
        };

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        boolean success = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
        if (success) {
            System.out.println("Compilation successful!");

            // Load the compiled class
            ClassLoader classLoader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    byte[] classBytes = fileManager.getClassBytes(name);
                    if (classBytes == null) {
                        throw new ClassNotFoundException(name);
                    }
                    return defineClass(name, classBytes, 0, classBytes.length);
                }
            };

            try {
                Class<?> clazz = classLoader.loadClass("HelloKt"); // Kotlin uses "Kt" suffix for top-level functions
                Method method = clazz.getMethod("greet");
                method.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Compilation failed!");
        }
    }
}
