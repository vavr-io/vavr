/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A code generator for Javaslang <em>structural pattern matching</em> patterns.
 * <p>
 * <strong>Note:</strong>
 * <p>
 * If javac complains {@code [WARNING] No processor claimed any of these annotations: ...}
 * we need to provide the compiler arg {@code -Xlint:-processing}.
 * <p>
 * See <a href="https://bugs.openjdk.java.net/browse/JDK-6999068">JDK-6999068 bug</a>.
 */
//
// DEV-NOTE:
// In order to keep the Javaslang maven module dependency graph simple, the javaslang-matchable-annotation
// is stringly-typed and does not have any dependencies to other Javaslang modules containing core classes
// like Functions and Tuples.
//
public class MatchableProcessor extends AbstractProcessor {

    private static final String CODE_GEN_JS = "javaslang/code_gen.js";
    private static final String PATTERN_GENERATOR_JS = "javaslang/pattern_generator.js";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        log("Processing annotations: " + annotations);

        if (!annotations.isEmpty()) {
            final Set<TypeElement> types = roundEnv.getElementsAnnotatedWith(Matchable.class).stream()
                    .filter(element -> element instanceof TypeElement)
                    .map(element -> (TypeElement) element)
                    .collect(Collectors.toSet());
            if (types.size() > 0) {
                if (roundEnv.processingOver()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Processing over.");
                } else {
                    generate(types);
                }
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // we do not use @SupportedAnnotationTypes in order to be type-safe
        return Collections.singleton(Matchable.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // intended to be used with Java 8+
        return SourceVersion.latestSupported();
    }

    private String generate(Set<TypeElement> types) {

        log("Processing types: " + types);

        final JS generator = JS.create("-scripting").load(CODE_GEN_JS, PATTERN_GENERATOR_JS);

        final Elements elementUtils = processingEnv.getElementUtils();
        final Filer filer = processingEnv.getFiler();
        final Types typeUtils = processingEnv.getTypeUtils();

        for (TypeElement type : types) {

            final String _package = elementUtils.getPackageOf(type).getQualifiedName().toString();
            final String _class = type.getSimpleName().toString() + "s";

            String result = generator.invoke("generate", _package, _class);

            try {
                final JavaFileObject javaFileObject = filer.createSourceFile(type.getQualifiedName() + "s", type);
                final Writer writer = javaFileObject.openWriter();
                writer.write(result);
                writer.flush();
                writer.close();
            } catch(IOException x) {
                throw new Error("Error creating file: " + _class);
            }

            log(result);
        }

        // TODO
        return null;
    }

    private void log(String message) {
        System.out.println("[ANNOTATION-PROCESSOR] " + message);
    }
}
