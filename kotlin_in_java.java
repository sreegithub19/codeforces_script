package com.example;

import kotlin.script.experimental.api.*;
import kotlin.script.experimental.host.*;
import kotlin.script.experimental.jvm.*;
import kotlin.script.experimental.jvm.host.*;

public class kotlin_in_java {
    public static void main(String[] args) {
        String kotlinCode = """
        println("Hello from Kotlin!")
        """;

        ScriptCompilationConfiguration configuration = new ScriptCompilationConfiguration() {{
            jvm {
                dependencies.add("org.jetbrains.kotlin:kotlin-stdlib:" + KotlinVersion.CURRENT);
            }
        }};

        ScriptEvaluationConfiguration evaluationConfiguration = new ScriptEvaluationConfiguration();

        try {
            KotlinJsr223Engine engine = new KotlinJsr223Engine(configuration, evaluationConfiguration);
            engine.eval(kotlinCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
