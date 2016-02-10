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
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class PatternsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        log("Processing annotations: " + annotations);

        if (!annotations.isEmpty()) {
            final Set<TypeElement> types = roundEnv.getElementsAnnotatedWith(Patterns.class).stream()
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
        return Collections.singleton(Patterns.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // intended to be used with Java 8+
        return SourceVersion.latestSupported();
    }

    private String generate(Set<TypeElement> types) {

        log("Processing types: " + types);

        final JS generator = JS.create("-scripting").load(
                "javaslang/code-gen.js",
                "javaslang/pattern-generator.js"
        );

        final Elements elementUtils = processingEnv.getElementUtils();
        final Filer filer = processingEnv.getFiler();
        final Types typeUtils = processingEnv.getTypeUtils();

        for (TypeElement type : types) {

            // TOOD: ensure that the @Pattern-annotated type is
            // - package-private
            // - no inner class(?)
            // - ...

            // TODO: which order is returned? - if none, use Set instead
            // TODO: executable elements should not be constructors or default methods
            // TODO: modifiers should be only static! (no public, private, protected, final, ...). allow synchronized?
            final List<ExecutableElement> executableElements = type.getEnclosedElements().stream()
                    .filter(element -> element instanceof ExecutableElement)
                    .map(element -> (ExecutableElement) element)
                    .filter(element -> element.getAnnotationsByType(Unapply.class).length > 0)
                    .collect(Collectors.toList());

            for (ExecutableElement executableElement : executableElements) {

                System.out.println("METHOD: " + executableElement);
                final List<? extends TypeParameterElement> typeParameters = executableElement.getTypeParameters();

                final TypeMirror typeMirror = executableElement.asType();
                System.out.println("  TYPE MIRROR: " + typeMirror);

                final TypeMirror returnType = executableElement.getReturnType();
                System.out.println("  RETURN TYPE: " + returnType);

                final TypeKind kind = returnType.getKind();
                System.out.println("    KIND: " + returnType.getKind());
                if (kind == TypeKind.DECLARED) {
                    final DeclaredType declaredType = (DeclaredType) returnType;
                    System.out.println("    DECLARED TYPE: " + declaredType);
                    System.out.println("      TYPE ARGS: " + declaredType.getTypeArguments());
                } else {
                    System.out.println("    KIND HANDLER: none");
                }

                for (TypeParameterElement typeParameter : typeParameters) {

                    System.out.println("  TYPE PARAMETER:");
                    final Element paramGenericElement = typeParameter.getGenericElement();
                    System.out.println("    GENERIC: " + paramGenericElement);

                    final TypeMirror paramTypeMirror = typeParameter.asType();
                    System.out.println("    TYPE MIRROR: " + paramTypeMirror);
                }
            }

            final String _package = elementUtils.getPackageOf(type).getQualifiedName().toString();
            final String _class = type.getSimpleName().toString() + "s";

            String result = generator.invoke("generate", _package, _class);

            try {
                final JavaFileObject javaFileObject = filer.createSourceFile(type.getQualifiedName() + "s", type);
                final Writer writer = javaFileObject.openWriter();
                writer.write(result);
                writer.flush();
                writer.close();
            } catch (IOException x) {
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
