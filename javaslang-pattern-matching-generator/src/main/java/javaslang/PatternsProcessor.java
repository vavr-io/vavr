/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.PatternsProcessor.PatternsModel.UnapplyModel;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static javax.lang.model.element.Modifier.*;

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

    private int ARITY = 8;

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
            if (isTypeDeclarationValid(typeElement, processingEnv.getMessager())) {
                final List<ExecutableElement> executableElements = typeElement.getEnclosedElements().stream()
                        .filter(element -> element instanceof ExecutableElement && element.getAnnotationsByType(Unapply.class).length == 1)
                        .map(element -> (ExecutableElement) element)
                        .collect(Collectors.toList());
                final List<UnapplyModel> unapplyModels = new ArrayList<>();
                for (ExecutableElement executableElement : executableElements) {
                    if (isMethodDeclarationValid(executableElement, processingEnv.getMessager())) {
                        // TODO: compute variations and store them in model
//                        Lists.crossProduct(Arrays.asList(Param.values()), arity)
//                                .stream()
//                                .filter(params -> params.stream().map(Param::arity).reduce((a, b) -> a + b).get() <= ARITY)
//                                .collect(Collectors.toList());
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

    private static boolean isTypeDeclarationValid(TypeElement typeElement, Messager messager) {
        class TypeElemChecker extends ElemChecker<TypeElemChecker, TypeElement> {
            TypeElemChecker(TypeElement elem, Messager messager) {
                super(elem, messager);
            }

            boolean isNested() {
                return elem.getNestingKind().isNested();
            }
        }
        final TypeElemChecker typeChecker = new TypeElemChecker(typeElement, messager);
        return typeChecker.ensure(e -> !e.isNested(), () -> "Patterns need to be defined in top-level classes.");
    }

    private static boolean isMethodDeclarationValid(ExecutableElement executableElement, Messager messager) {
        class ExecElemChecker extends ElemChecker<ExecElemChecker, ExecutableElement> {
            ExecElemChecker(ExecutableElement elem, Messager messager) {
                super(elem, messager);
            }

            boolean isMethod() {
                final String name = elem.getSimpleName().toString();
                return !name.isEmpty() && !"<init>".equals(name) && !"<clinit>".equals(name);
            }

            boolean doesNotThrow() {
                return elem.getThrownTypes().isEmpty();
            }
        }
        final ExecElemChecker methodChecker = new ExecElemChecker(executableElement, messager);
        return methodChecker.ensure(ExecElemChecker::isMethod, () -> "Annotation " + Unapply.class.getName() + " only allowed for methods.") &&
                methodChecker.ensure(ExecElemChecker::doesNotThrow, () -> "@" + "Unapply method should not throw (checked) exceptions.") &&
                methodChecker.ensure(e -> !e.elem.isDefault(), () -> "@" + "Unapply method needs to be declared in a class, not an interface.") &&
                methodChecker.ensure(e -> !e.elem.isVarArgs(), () -> "@" + "Unapply method has varargs.") &&
                methodChecker.ensure(e -> e.elem.getParameters().size() == 1, () -> "Unapply method must have exactly one parameter of the object to be deconstructed.") &&
                methodChecker.ensure(e -> e.elem.getReturnType().toString().startsWith("javaslang.Tuple"), () -> "Return type of unapply method must be a Tuple.") &&
                methodChecker.ensure(e -> e.hasAll(STATIC), () -> "Unapply method needs to be static.") &&
                methodChecker.ensure(e -> e.hasNone(PRIVATE, PROTECTED, ABSTRACT), () -> "Unapply method may not be private or protected.");
    }

    private static abstract class ElemChecker<SELF extends ElemChecker<SELF, E>, E extends Element> {

        final E elem;
        final Messager messager;

        ElemChecker(E elem, Messager messager) {
            this.elem = elem;
            this.messager = messager;
        }

        boolean ensure(Predicate<? super SELF> condition, Supplier<String> msg) {
            @SuppressWarnings("unchecked")
            final boolean result = condition.test((SELF) this);
            if (!result) {
                messager.printMessage(Diagnostic.Kind.ERROR, msg.get(), elem);
            }
            return result;
        }

        boolean hasAll(Modifier... modifiers) {
            return elem.getModifiers().containsAll(Arrays.asList(modifiers));
        }

        boolean hasNone(Modifier... modifiers) {
            final Set<Modifier> set = elem.getModifiers();
            for (Modifier modifier : modifiers) {
                if (set.contains(modifier)) {
                    return false;
                }
            }
            return true;
        }
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
                    unapplys.stream().map(Object::toString).collect(Collectors.joining(",\n    ", ",\n    ", "\n")) +
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

        T(0),               // value
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
