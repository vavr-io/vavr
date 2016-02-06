/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * A JavaScript / Nashorn interface for Java.
 * <p>
 * <strong>Example:</strong>
 *
 * <pre><code>
 * // enables scripting mode / syntax extensions
 * JS js = JS.create("-scripting").load("myscript1.js", "some/dir/myscript2.js");
 * String msg = js.invoke("sayHello", "Javaslang");
 * </code></pre>
 *
 * <strong>Nashorn &amp; JavaScript resources:</strong>
 * <ul>
 * <li><a href="http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial">Java 8 Nashorn Tutorial</a></li>
 * <li><a href="https://github.com/shekhargulati/java8-the-missing-tutorial/blob/master/10-nashorn.md">Java 8: The Missing Tutorial / Nashorn: Run JavaScript on the JVM</a></li>
 * <li><a href="https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions"></a>OpenJDK Wiki: Nashorn Syntax Extensions</li>
 * <li><a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference">MDN: JavaScript reference</a></li>
 * </ul>
 */
interface JS {

    /**
     * Creates a new JS instance.
     *
     * @param args arguments passed to the Nashorn engine factory.
     * @return a new JS instance
     */
    static JS create(String... args) {
        return new JSImpl(args);
    }

    /**
     * Loads javascript resources using the current {@code ClassLoader}.
     *
     * @param scripts resource names to be loaded from the classpath
     * @return this JS instance
     */
    default JS load(String... scripts) {
        return load(getClass().getClassLoader(), scripts);
    }

    /**
     * Loads javascript resources using a specific {@code ClassLoader}.
     *
     * @param classLoader the {@code ClassLoader} used to load the {@code scripts}
     * @param scripts     resource names to be loaded from the classpath
     * @return this JS instance
     */
    JS load(ClassLoader classLoader, String... scripts);

    /**
     * Invokes a javascript function.
     *
     * @param name function name
     * @param args function args, may be empty
     * @param <R>  result type (unsafe cast)
     * @return The result of the function call
     * @throws java.lang.Error if an error occurs executing the function or the result is not of type R
     */
    <R> R invoke(String name, Object... args);

}

final class JSImpl implements JS {

    private final ScriptEngine engine;

    JSImpl(String... args) {
        this.engine = new NashornScriptEngineFactory().getScriptEngine(args);
    }

    @Override
    public JS load(ClassLoader classLoader, String... scripts) {
        for (String script : scripts) {
            load(classLoader, script);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R invoke(String name, Object... args) {

        final Object result;
        try {
            final Invocable invocable = (Invocable) engine;
            result = invocable.invokeFunction(name, args);
        } catch (ScriptException x) {
            throw new Error("Error invoking function " + name + Arrays.toString(args), x);
        } catch (NoSuchMethodException x) {
            throw new Error("Function not found: " + name + Arrays.toString(args), x);
        }

        try {
            return (R) result;
        } catch (ClassCastException x) {
            throw new Error("Unexpected result type of function " + name + Arrays.toString(args), x);
        }
    }

    private void load(ClassLoader classLoader, String name) {

        final InputStream script = classLoader.getResourceAsStream(name);
        if (script == null) {
            throw new Error("Script not found: " + name);
        }

        try {
            engine.eval(new InputStreamReader(script));
        } catch (ScriptException x) {
            throw new Error("Error loading script " + name, x);
        }
    }
}
