import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KotlinInJava {
    public static void main(String[] args) {
        String kotlinCode = """
            fun greet() {
                println("Hello from in-memory Kotlin code!")
            }

            fun main() {
                greet()
            }
        """;

        // Use the Kotlin scripting engine
        try {
            // Create a script engine manager and get the Kotlin engine
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("kotlin");

            // Compile and execute the Kotlin code
            engine.eval(kotlinCode);
            
            // Call the main function directly
            engine.eval("main()");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
