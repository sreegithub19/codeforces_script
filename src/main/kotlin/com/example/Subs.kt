package com.example

import java.io.File
import javax.tools.JavaCompiler
import javax.tools.ToolProvider

fun main() {
    val javaCode = """
        public class Hello {
            public static void main() {
                System.out.println("Hello from Java!");
            }
        }
    """.trimIndent()

    // Save Java code to a temporary file
    val javaFile = File("Hello.java")
    javaFile.writeText(javaCode)

    // Get the Java compiler
    val compiler: JavaCompiler? = ToolProvider.getSystemJavaCompiler()
    
    // Compile the Java file
    val compilationResult = compiler?.run(null, null, null, javaFile.absolutePath)
    if (compilationResult == 0) {
        println("Compilation successful!")
        
        // Load and run the compiled Java class
        val clazz = Class.forName("Hello")
        val method = clazz.getMethod("main")
        method.invoke(null)
    } else {
        println("Compilation failed!")
    }

    // Clean up
    javaFile.delete()
}
