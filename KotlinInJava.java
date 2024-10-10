//import org.jetbrains.kotlin.cli.common.messages.MessageCollector;
//import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;
//import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment;
//import org.jetbrains.kotlin.config.CompilerConfiguration;
//import org.jetbrains.kotlin.config.JVMConfigurationKeys;
//
//import javax.tools.JavaCompiler;
//import javax.tools.ToolProvider;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class KotlinInJava {
//    public static void main(String[] args) {
//        String kotlinCode = "fun greet() { println(\"Hello from embedded Kotlin!\") }\ngreet()";
//
//        try {
//            // Create a temporary Kotlin file
//            Path tempKotlinFile = Files.createTempFile("TempKotlin", ".kt");
//            Files.write(tempKotlinFile, kotlinCode.getBytes());
//
//            // Compile the Kotlin file
//            CompilerConfiguration configuration = new CompilerConfiguration();
//            configuration.put(JVMConfigurationKeys.MODULE_NAME, "TempModule");
//            KotlinCoreEnvironment environment = KotlinCoreEnvironment.createForProduction(
//                    new MessageCollector() {}, configuration, null);
//
//            K2JVMCompiler compiler = new K2JVMCompiler();
//            compiler.exec(environment.getEnvironment().getCompilerConfiguration());
//
//            // Load and run the compiled class (you may need to adjust the classpath)
//            String className = "TempKotlinKt"; // Kotlin compiles to <filename>Kt
//            Class<?> clazz = Class.forName(className);
//            clazz.getMethod("greet").invoke(null);
//
//            // Clean up the temporary file
//            Files.delete(tempKotlinFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
