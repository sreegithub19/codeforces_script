import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KotlinInJava {
    public static void main(String[] args) {
        String kotlinCode = "println(\"Hello from Kotlin!\")";

        // Create a Kotlin script engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("kotlin");

        try {
            // Evaluate the Kotlin code
            engine.eval(kotlinCode);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
