/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
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
                    generate(types);
                }
            }
        }
        return true;
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

    private String generate(Set<TypeElement> typeElements) {
        for (TypeElement typeElement : typeElements) {
            if (isTypeDeclarationValid(typeElement, processingEnv.getMessager())) {
                final List<ExecutableElement> executableElements = typeElement.getEnclosedElements().stream()
                        .filter(element -> element instanceof ExecutableElement && element.getAnnotationsByType(Unapply.class).length == 1)
                        .map(element -> (ExecutableElement) element)
                        .collect(Collectors.toList());
                for (ExecutableElement executableElement : executableElements) {
                    if (isMethodDeclarationValid(executableElement, processingEnv.getMessager())) {
                        final UnapplyReflector reflector = new UnapplyReflector(executableElement);
                        final int arity = reflector.getArity();

                        // TODO:DELME
                        System.out.printf("[%d] " + executableElement + "\n", arity);
                        if (arity == 0) {

                            // Tuple0 Nil(List.Nil<?> nil)

                        } else {

                            Lists.crossProduct(Arrays.asList(Param.values()), arity)
                                    .stream()
                                    .filter(params -> params.stream().map(Param::arity).reduce((a, b) -> a + b).get() <= 8)
                                    .collect(Collectors.toList())
                                    .forEach(System.out::println);

                            // <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons)

                        }
                    }
                }

                // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

                // @Unapply static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons)

                // @Unapply static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons)
                // @Unapply static     Tuple0    Nil(List.Nil<?> nil)

                // static Pattern0 Nil = new Pattern0() {
                //     @Override
                //     public Option<Void> apply(Object o) {
                //         return (o instanceof List.Nil) ? Option.nothing() : Option.none();
                //     }
                // };

                // static                                Pattern0                      List(Pattern0 p1, Pattern0 p2)
                // static                                Pattern1                      List(Pattern0 p1, Pattern1<? extends List<U>> p2)
                // static                                Pattern1                      List(Pattern0 p1, InversePattern<? extends List<U>> p2)
                // static <T extends List<U>, U, T1>     Pattern1<List<U>, T1>         List(Pattern1<? extends U, T1> p1, Pattern0 p2)
                // static <T extends List<U>, U, T1, T2> Pattern2<List<U>, T1, T2>     List(Pattern1<? extends U, T1> p1, Pattern1<? extends List<U>, T2> p2)
                // static <T extends List<U>, U>         Pattern1<List<U>, U>          List(InversePattern<? extends U> head, Pattern0 tail) {
                // static <T extends List<U>, U>         Pattern2<List<U>, U, List<U>> List(InversePattern<? extends U> head, Pattern1<? extends List<U>> tail)
                // static <T extends List<U>, U>         Pattern2<List<U>, U, List<U>> List(InversePattern<? extends U> head, InversePattern<List<U>> tail)
                // ...

                // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

                final Elements elementUtils = processingEnv.getElementUtils();
                final Filer filer = processingEnv.getFiler();
                final Types typeUtils = processingEnv.getTypeUtils();

                final String _package = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
                final String _class = typeElement.getSimpleName().toString() + "s";

                String result = generator.invoke("generate", _package, _class);

                try {
                    final JavaFileObject javaFileObject = filer.createSourceFile(typeElement.getQualifiedName() + "s", typeElement);
                    final Writer writer = javaFileObject.openWriter();
                    writer.write(result);
                    writer.flush();
                    writer.close();
                } catch (IOException x) {
                    throw new Error("Error creating file: " + _class);
                }

                // TODO: DELME
                System.out.println(result);
            }
        }

        // TODO
        return null;
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

    private static class UnapplyReflector {

        final ExecutableElement elem;

        UnapplyReflector(ExecutableElement elem) {
            this.elem = elem;
        }

        public int getArity() {
            final DeclaredType returnType = (DeclaredType) elem.getReturnType();
            final String simpleName = returnType.asElement().getSimpleName().toString();
            return Integer.parseInt(simpleName.substring("Tuple".length()));
        }

        // TODO
        //                System.out.println("METHOD: " + executableElement);
        //                final List<? extends TypeParameterElement> typeParameters = executableElement.getTypeParameters();
        //
        //                final TypeMirror typeMirror = executableElement.asType();
        //                System.out.println("  TYPE MIRROR: " + typeMirror);
        //
        //                final TypeMirror returnType = executableElement.getReturnType();
        //                System.out.println("  RETURN TYPE: " + returnType);
        //
        //                final TypeKind kind = returnType.getKind();
        //                System.out.println("    KIND: " + returnType.getKind());
        //                if (kind == TypeKind.DECLARED) {
        //                    final DeclaredType declaredType = (DeclaredType) returnType;
        //                    System.out.println("    DECLARED TYPE: " + declaredType);
        //                    System.out.println("      TYPE ARGS: " + declaredType.getTypeArguments());
        //                } else {
        //                    System.out.println("    KIND HANDLER: none");
        //                }
        //
        //                for (TypeParameterElement typeParameter : typeParameters) {
        //
        //                    System.out.println("  TYPE PARAMETER:");
        //                    final Element paramGenericElement = typeParameter.getGenericElement();
        //                    System.out.println("    GENERIC: " + paramGenericElement);
        //
        //                    final TypeMirror paramTypeMirror = typeParameter.asType();
        //                    System.out.println("    TYPE MIRROR: " + paramTypeMirror);
        //                }
    }
}
