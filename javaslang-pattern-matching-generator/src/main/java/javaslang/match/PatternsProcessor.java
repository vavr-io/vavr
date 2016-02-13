/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

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
// DEV-NOTES:
//
// In order to keep the Javaslang maven module dependency graph simple, the javaslang-matchable-annotation
// is stringly-typed and does not have any dependencies to other Javaslang modules containing core classes
// like Functions and Tuples.
//
// See Difference between Element, Type and Mirror: http://stackoverflow.com/a/2127320/1110815
//
// BUILD-NOTES:
//
// Tests work best with `mvn clean test-compile -DskipGen`
//
public class PatternsProcessor extends AbstractProcessor {

    // corresponds to the number of Javaslang Tuples.
    private static final int ARITY = 8;

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

    /**
     * Gathers annotated elements, transforms elements to a generator model and generates the model to code.
     *
     * @param annotations the annotation types requested to be processed
     * @param roundEnv    environment for information about the current and prior round
     * @return whether or not the set of annotation types are claimed by this processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            final Set<TypeElement> typeElements = roundEnv.getElementsAnnotatedWith(Patterns.class).stream()
                    .filter(element -> element instanceof TypeElement)
                    .map(element -> (TypeElement) element)
                    .collect(Collectors.toSet());
            if (!typeElements.isEmpty()) {
                if (roundEnv.processingOver()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Processing over.");
                } else {
                    generate(typeElements);
                }
            }
        }
        return true;
    }

    private void generate(Set<TypeElement> typeElements) {
        final Filer filer = processingEnv.getFiler();
        for (TypeElement typeElement : typeElements) {
            final String name = Elements.getFullQualifiedName(typeElement);
            generate(typeElement).ifPresent(code -> {
                try (final Writer writer = filer.createSourceFile(name, typeElement).openWriter()) {
                    writer.write(code);
                } catch (IOException x) {
                    throw new Error("Error creating generating " + name, x);
                }
            });
        }
    }

    private Optional<String> generate(TypeElement typeElement) {
        List<ExecutableElement> executableElements = getMethods(typeElement);
        if (executableElements.isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No @Unapply methods found.", typeElement);
            return Optional.empty();
        } else {
            final String _package = Elements.getPackage(typeElement);
            final String _class = Elements.getSimpleName(typeElement);
            final String result = (_package.isEmpty() ? "" : "package " + _package + ";\n\n") +
                    "import static javaslang.Match.*;\n\n" +
                    "public final class " + _class + "{\n\n" +
                    "    private " + _class + "() {\n" +
                    "    }\n\n" +
                    generate(typeElement, executableElements) +
                    "}\n";
            return Optional.of(result);
        }
    }

    private String generate(TypeElement typeElement, List<ExecutableElement> executableElements) {
        final StringBuilder builder = new StringBuilder();
        for (ExecutableElement executableElement : executableElements) {
            generate(typeElement, executableElement, builder);
            builder.append("\n");
        }
        return builder.toString();
    }

    private void generate(TypeElement type, ExecutableElement elem, StringBuilder builder) {
        final String typeName = Elements.getFullQualifiedName(type);
        final String name = elem.getSimpleName().toString();
        int arity = getArity(elem);
        if (arity == 0) {
            builder.append("    public static Pattern0 ")
                    .append(name).append(" = Pattern0.create(").append(typeName).append(".class);\n");
        } else {
            final List<List<Param>> variations = Lists.crossProduct(Arrays.asList(Param.values()), arity)
                    .stream()
                    .filter(params -> params.stream().map(Param::arity).reduce((a, b) -> a + b).get() <= ARITY)
                    .collect(Collectors.toList());
            for (List<Param> variation : variations) {
                final String method = Stream.of(
                        "public",
                        "static",
                        getGenerics(elem, variation),
                        getReturnType(elem, variation),
                        name,
                        getParams(elem, variation),
                        "{ return Pattern" + Param.getArity(variation) + ".create(" + typeName + ".class, " + type.getSimpleName() + "::" + name + ", p1, p2); }"
                ).collect(joining(" "));
                builder.append("    ").append(method).append("\n");
            }
        }
    }

    private String getGenerics(ExecutableElement elem, List<Param> variation) {
        List<String> result = new ArrayList<>();
        result.add("__ extends " + Elements.getParameterType(elem, 0));
        result.addAll(Arrays.asList(Elements.getTypeParameters(elem)));
        int j = 1;
        for (Param param : variation) {
            // Pattern0 has no result types, InversePattern takes pre-defined result tuple type parameter
            if (param != Param.Pattern0 && param != Param.InversePattern) {
                for (int i = 1; i <= param.arity; i++) {
                    result.add("T" + (j++));
                }
            }
        }
        return result.stream().collect(joining(", ", "<", ">"));
    }

    private String getReturnType(ExecutableElement elem, List<Param> variation) {
        final int resultArity = Param.getArity(variation);
        if (resultArity == 0) {
            return "Pattern0";
        } else {
            final String[] tupleArgTypes = Elements.getReturnTypeArgs(elem);
            final List<String> resultTypes = new ArrayList<>();
            resultTypes.add(Elements.getParameterType(elem, 0));
            int j = 1;
            for (int i = 0; i < variation.size(); i++) {
                Param param = variation.get(i);
                if (param == Param.InversePattern) {
                    resultTypes.add(tupleArgTypes[i]);
                } else if (param != Param.T && param != Param.Pattern0) {
                    for (int k = 1; k <= param.arity; k++) {
                        resultTypes.add("T" + (j++));
                    }
                }
            }
            return "Pattern" + resultArity + "<" + resultTypes.stream().collect(joining(", ")) + ">";
        }
    }

    private String getParams(ExecutableElement elem, List<Param> variation) {
        StringBuilder builder = new StringBuilder("(");
        final String[] tupleArgTypes = Elements.getReturnTypeArgs(elem);
        int j = 1;
        for (int i = 0; i < variation.size(); i++) {
            Param param = variation.get(i);
            if (param == Param.T) {
                builder.append(tupleArgTypes[i]);
            } else if (param == Param.InversePattern) {
                builder.append(param.name()).append("<? extends ").append(tupleArgTypes[i]).append(">");
            } else if (param == Param.Pattern0) {
                builder.append("Pattern0");
            } else {
                builder.append(param.name()).append("<? extends ").append(tupleArgTypes[i]).append(", ");
                for (int k = 1; k <= param.arity; k++) {
                    builder.append("T").append(j++);
                    if (k < param.arity) {
                        builder.append(", ");
                    }
                }
                builder.append(">");
            }
            builder.append(" ").append("p").append(i + 1);
            if (i < variation.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private List<ExecutableElement> getMethods(TypeElement typeElement) {
        if (Patterns.Checker.isValid(typeElement, processingEnv.getMessager())) {
            return typeElement.getEnclosedElements().stream()
                    .filter(element -> element.getAnnotationsByType(Unapply.class).length == 1 &&
                            element instanceof ExecutableElement &&
                            Unapply.Checker.isValid((ExecutableElement) element, processingEnv.getMessager()))
                    .map(element -> (ExecutableElement) element)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // Not part of Elements helper because specific for this use-case

    private static int getArity(ExecutableElement elem) {
        final DeclaredType returnType = (DeclaredType) elem.getReturnType();
        final String simpleName = returnType.asElement().getSimpleName().toString();
        return Integer.parseInt(simpleName.substring("Tuple".length()));
    }
}

enum Param {

    T(0),               // Eq(t) = o -> Objects.equals(o, t)
    InversePattern(1),  // $()
    Pattern0(0),        // $_
    Pattern1(1),        // $("test")
    Pattern2(2),        // combinations of the above...
    Pattern3(3),
    Pattern4(4),
    Pattern5(5),
    Pattern6(6),
    Pattern7(7),
    Pattern8(8);

    final int arity;

    Param(int arity) {
        this.arity = arity;
    }

    int arity() {
        return arity;
    }

    static int getArity(List<Param> params) {
        return params.stream().mapToInt(Param::arity).sum();
    }
}
