package main.java.com.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KotlinInJava {
    public static void main(String[] args) {
        String kotlinScript = """
        println("Hello, Kotlin embedded from Java!")
        """;

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("kts");

        try {
            engine.eval(kotlinScript);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}