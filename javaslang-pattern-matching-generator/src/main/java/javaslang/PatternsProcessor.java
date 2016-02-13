/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.PatternsProcessor.PatternsModel.UnapplyModel;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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
public class PatternsProcessor extends AbstractProcessor {

    // corresponds to the number of Javaslang Tuples.
    private static final int ARITY = 8;

    private final JS generator;

    public PatternsProcessor() {
        this.generator = JS.create("-scripting").load(
                "javaslang/code-gen.js",
                "javaslang/pattern-generator.js"
        );
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
            final Set<TypeElement> types = roundEnv.getElementsAnnotatedWith(Patterns.class).stream()
                    .filter(element -> element instanceof TypeElement)
                    .map(element -> (TypeElement) element)
                    .collect(Collectors.toSet());
            if (types.size() > 0) {
                if (roundEnv.processingOver()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Processing over.");
                } else {
                    final List<PatternsModel> patternsModels = createModel(types);
                    if (!patternsModels.isEmpty()) {
                        generate(patternsModels);
                    }
                }
            }
        }
        return true;
    }

    private void generate(List<PatternsModel> patternsModels) {
        final Filer filer = processingEnv.getFiler();
        for (PatternsModel model : patternsModels) {
            try (final Writer writer = filer.createSourceFile(model.getFullQualifiedName(), model.typeElement).openWriter()) {
                final String result = model.toString(); // TODO: generator.invoke("generate", model);
                /*DEBUG*/
                System.out.println(result);
                writer.write(result);
            } catch (IOException x) {
                throw new Error("Error creating patterns " + model.getFullQualifiedName(), x);
            }
        }
    }

    private List<PatternsModel> createModel(Set<TypeElement> typeElements) {
        final List<PatternsModel> patternsModels = new ArrayList<>();
        for (TypeElement typeElement : typeElements) {
            if (Patterns.Checker.isValid(typeElement, processingEnv.getMessager())) {
                final List<ExecutableElement> executableElements = typeElement.getEnclosedElements().stream()
                        .filter(element -> element instanceof ExecutableElement && element.getAnnotationsByType(Unapply.class).length == 1)
                        .map(element -> (ExecutableElement) element)
                        .collect(Collectors.toList());
                final List<UnapplyModel> unapplyModels = new ArrayList<>();
                for (ExecutableElement executableElement : executableElements) {
                    if (Unapply.Checker.isValid(executableElement, processingEnv.getMessager())) {
                        unapplyModels.add(new UnapplyModel(executableElement));
                    }
                }
                if (unapplyModels.isEmpty()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No @Unapply methods found.", typeElement);
                } else {
                    patternsModels.add(new PatternsModel(typeElement, unapplyModels));
                }
            }
        }
        return patternsModels;
    }

    /**
     * The file name is derived from the annotated class.
     * If the class name is just '$' it will be replaced with 'Patterns',
     * otherwise 'Patterns' will be concatenated.
     */
    static class PatternsModel {

        final TypeElement typeElement;
        final String pkg;
        final String name;
        final List<UnapplyModel> unapplys;

        PatternsModel(TypeElement typeElement, List<UnapplyModel> unapplys) {
            this.typeElement = typeElement;
            final String simpleName = typeElement.getSimpleName().toString();
            final String qualifiedName = typeElement.getQualifiedName().toString();
            this.pkg = qualifiedName.isEmpty() ? "" : qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
            this.name = ("$".equals(simpleName) ? "" : simpleName) + Patterns.class.getSimpleName();
            this.unapplys = unapplys;
        }

        public String getFullQualifiedName() {
            return pkg.isEmpty() ? name : pkg + "." + name;
        }

        @Override
        public String toString() {
            return "Patterns(pkg=" +
                    pkg +
                    ", name=" +
                    name +
                    unapplys.stream().map(Object::toString).collect(joining(",\n    ", ",\n    ", "\n")) +
                    ")";
        }

        static class UnapplyModel {

            final int arity;
            final String name;
            final List<GenericModel> generics;
            final TypeModel paramType;
            final TypeModel returnType;

            UnapplyModel(ExecutableElement elem) {
                this.arity = getArity(elem);
                this.name = getName(elem);
                this.generics = getTypeParameters(elem);
                this.paramType = getParamType(elem);
                this.returnType = getReturnType(elem);

                // DEBUG
                System.out.println("@Unapply " + name);
                if (arity == 0) {
                    System.out.println("trivial...");
                } else {
                    final List<List<Param>> variations = Lists.crossProduct(Arrays.asList(Param.values()), arity)
                            .stream()
                            .filter(params -> params.stream().map(Param::arity).reduce((a, b) -> a + b).get() <= ARITY)
                            .collect(Collectors.toList());
                    for (List<Param> variation : variations) {
                        final int returnPatternArity = variation.stream().mapToInt(Param::arity).sum();
                        if (returnPatternArity == 0) {
                            // TODO
                            System.out.println("returnArity 0");
                        } else {
                            final String method = Stream.of(getGenerics(variation), getReturnType(variation, returnPatternArity), name, getParams(variation), "{", "...", "}")
                                    .collect(joining(" "));
                        /*DEBUG*/
                            System.out.println(method);
                        }
                    }
                }
            }

            String getGenerics(List<Param> variation) {
                List<String> result = new ArrayList<>();
                result.add("__ extends " + paramType.name);
                result.addAll(generics.stream().map(generic -> generic.name).collect(Collectors.toList()));
                int i = 1;
                for (Param param : variation) {
                    if (param == Param.T) {
                        // TODO
                    } else if (param == Param.InversePattern) {
                        // uses pre-defined result tuple type parameter
                    } else {
                        for (int j = 1; j <= param.arity; j++) {
                            result.add("T" + (i++));
                        }
                    }
                }
                return result.stream().collect(joining(", ", "<", ">"));
            }

            String getReturnType(List<Param> variation, int returnTypeArtiy) {
                if (arity == 0) {
                    return "Pattern0";
                } else {
                    final String[] tupleArgTypes;
                    {
                        int start = returnType.name.indexOf('<') + 1;
                        int end = returnType.name.lastIndexOf('>');
                        tupleArgTypes = returnType.name.substring(start, end).split(",");
                        assert tupleArgTypes.length == variation.size();
                    }
                    final List<String> resultTypes = new ArrayList<>();
                    resultTypes.add(paramType.name);
                    for (int i = 0; i < variation.size(); i++) {
                        Param param = variation.get(i);
                        if (param == Param.T) {
                            // TODO
                        } else if (param == Param.InversePattern) {
                            resultTypes.add(tupleArgTypes[i]);
                        } else {
                            resultTypes.add(tupleArgTypes[i]);
                        }
                    }
                    return "Pattern" + returnTypeArtiy + "<" + resultTypes.stream().collect(joining(", ")) + ">";
                }
            }

            String getParams(List<Param> variation) {
                StringBuilder builder = new StringBuilder("(");
                for (int i = 0; i < variation.size(); i++) {
                    Param param = variation.get(i);
                    builder.append(param.name());
                    // TODO: generics
                    builder.append(" ").append("p").append(i + 1);
                    if (i < variation.size() - 1) {
                        builder.append(", ");
                    }
                }
                builder.append(")");
                return builder.toString();
            }

            @Override
            public String toString() {
                return String.format("UnapplyModel(arity=%s, name=%s, generics=%s, paramType=%s, returnType=%s)", arity, name, generics, paramType, returnType);
            }

            private static int getArity(ExecutableElement elem) {
                final DeclaredType returnType = (DeclaredType) elem.getReturnType();
                final String simpleName = returnType.asElement().getSimpleName().toString();
                return Integer.parseInt(simpleName.substring("Tuple".length()));
            }

            private static String getName(ExecutableElement elem) {
                return elem.getSimpleName().toString();
            }

            private static List<GenericModel> getTypeParameters(ExecutableElement elem) {
                return elem.getTypeParameters().stream().map(typeArg -> new GenericModel(typeArg.asType())).collect(Collectors.toList());
            }

            private static TypeModel getParamType(ExecutableElement elem) {
                // unapply method has exactly one parameter (= object to be deconstructed)
                return new TypeModel((DeclaredType) elem.getParameters().get(0).asType());
            }

            private static TypeModel getReturnType(ExecutableElement elem) {
                return new TypeModel((DeclaredType) elem.getReturnType());
            }

            static class TypeModel {

                final String name;
                final List<GenericModel> generics;

                TypeModel(DeclaredType type) {
                    this.name = type.toString();
                    this.generics = type.getTypeArguments().stream().map(GenericModel::new).collect(Collectors.toList());
                }

                @Override
                public String toString() {
                    return String.format("TypeModel(name=%s, generics=%s)", name, generics);
                }

            }

            static class GenericModel {

                final String name;
                final TypeKind kind;

                GenericModel(TypeMirror typeMirror) {
                    this.name = typeMirror.toString();
                    this.kind = typeMirror.getKind();
                }

                @Override
                public String toString() {
                    return "Generic(name=" + name + ", kind=" + kind + ")";
                }
            }
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
    }
}
