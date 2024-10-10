import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223Engine;

public class KotlinInJava {
    public static void main(String[] args) {
        String kotlinCode = "println(\"Hello from Kotlin!\")";

        // Create a Kotlin script engine
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = new KotlinJsr223Engine();

        try {
            // Evaluate the Kotlin code
            engine.eval(kotlinCode);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
