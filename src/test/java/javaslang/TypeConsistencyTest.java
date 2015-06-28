/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.control.Try;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TypeConsistencyTest {

    static final List<String> WHITELIST = List.of(

            // control.Match
            "javaslang.control.Match//public default java.util.function.Function java.util.function.Function.andThen(java.util.function.Function)",
            "javaslang.control.Match//public default java.util.function.Function java.util.function.Function.compose(java.util.function.Function)",

            // control.Failure
            "javaslang.control.Failure//public abstract javaslang.control.Try javaslang.control.Try.recover(java.util.function.Function)",
            "javaslang.control.Failure//public abstract javaslang.control.Try javaslang.control.Try.recoverWith(java.util.function.Function)",
            "javaslang.control.Failure//public abstract javaslang.control.Try javaslang.control.Try.failed()",
            "javaslang.control.Failure//public default javaslang.control.Try javaslang.control.Try.flatten(java.util.function.Function)",
            "javaslang.control.Failure//public abstract javaslang.control.Try javaslang.control.Try.map(java.util.function.Function)",
            "javaslang.control.Failure//public default javaslang.control.Try javaslang.control.Try.andThen(javaslang.control.Try$CheckedRunnable)",

            // control.Success
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.failed()",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.filter(java.util.function.Predicate)",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.flatMap(java.util.function.Function)",
            "javaslang.control.Success//public default javaslang.control.Try javaslang.control.Try.flatten(java.util.function.Function)",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.map(java.util.function.Function)",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.peek(java.util.function.Consumer)",
            "javaslang.control.Success//public default javaslang.control.Try javaslang.control.Try.andThen(javaslang.control.Try$CheckedRunnable)",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.andThen(javaslang.control.Try$CheckedConsumer)",
            "javaslang.control.Success//public abstract javaslang.control.Try javaslang.control.Try.mapTry(javaslang.CheckedFunction1)",

            // control.None
            "javaslang.control.None//public default javaslang.control.Option javaslang.control.Option.filter(java.util.function.Predicate)",
            "javaslang.control.None//public default javaslang.control.Option javaslang.control.Option.flatMap(java.util.function.Function)",
            "javaslang.control.None//public default javaslang.control.Option javaslang.control.Option.flatten(java.util.function.Function)",

            // control.Some
            "javaslang.control.Some//public default javaslang.control.Option javaslang.control.Option.filter(java.util.function.Predicate)",
            "javaslang.control.Some//public default javaslang.control.Option javaslang.control.Option.flatMap(java.util.function.Function)",
            "javaslang.control.Some//public default javaslang.control.Option javaslang.control.Option.flatten(java.util.function.Function)",

            // control.Left
            "javaslang.control.Left//public abstract javaslang.control.Either javaslang.control.Either.swap()",

            // control.Right
            "javaslang.control.Right//public abstract javaslang.control.Either javaslang.control.Either.swap()"
    );

    /**
     * CAUTION: Non-reifiable types (like {@code Tuple2<? extends Traversable<T>, ? extends Traversable<T>>})
     * are not recognized by this test because there is no runtime information available via reflection.
     */
    @Test
    public void shouldHaveAConsistentTypeSystem() {
        final Stream<Class<?>> classes = loadClasses("src-gen/main/java")
                .appendAll(loadClasses("src/main/java"))
                .filter(c -> {
                    final String name = c.getName();
                    return !name.startsWith("javaslang.Function") && !name.startsWith("javaslang.CheckedFunction") &&
                           !name.startsWith("javaslang.Consumer") && !name.startsWith("javaslang.CheckedConsumer");
                });
        final Stream<String> msgs = classes
                .map(clazz -> Tuple.of(clazz, getUnoverriddenMethods(clazz)))
                .filter(findings -> !findings._2.isEmpty())
                .map(findings -> Tuple.of(findings._1, findings._2.filter(method -> {
                    final String signature = findings._1.getName() + "//" + method;
                    return !WHITELIST.contains(signature);
                })))
                .filter(findings -> !findings._2.isEmpty())
                .sort((t1, t2) -> t1._1.getName().compareTo(t2._1.getName()))
                .map(findings -> String.format("%s has to override the following methods with return type %s:\n%s",
                        findings._1.getName(), findings._1.getSimpleName(), findings._2.map(m -> "* " + m).join("\n")));
        if (!msgs.isEmpty()) {
            throw new AssertionError(msgs.join("\n\n", "Unoverriden methods found.\n", ""));
        }
    }

    Stream<Method> getUnoverriddenMethods(Class<?> clazz) {
        final Stream<Class<?>> superClasses = Stream.of(clazz.getInterfaces())
                .append(clazz.getSuperclass())
                .filter(c -> c != null);
        if (superClasses.isEmpty()) {
            return Stream.nil();
        } else {
            final Stream<ComparableMethod> superMethods = getOverridableMethods(superClasses).filter(comparableMethod ->
                    // We're interested in methods that should be overridden with actual type as return type.
                    // Because we check this recursively, the class hierarchy is consistent here.
                    comparableMethod.m.getDeclaringClass().equals(comparableMethod.m.getReturnType()));
            final Stream<ComparableMethod> thisMethods = getOverridableMethods(Stream.of(clazz));
            return superMethods.filter(superMethod -> thisMethods
                    .findFirst(thisMethod -> thisMethod.equals(superMethod))
                            // TODO: special case if visibility is package private and classes are in different package
                    .map(thisMethod -> !clazz.equals(thisMethod.m.getReturnType()))
                    .orElse(true))
                    .sort()
                    .map(comparableMethod -> comparableMethod.m);
        }
    }

    Stream<ComparableMethod> getOverridableMethods(Stream<Class<?>> classes) {
        return classes
                .flatMap(clazz ->
                        Stream.of(clazz.getDeclaredMethods()).filter((Method m) ->
                                // https://javax0.wordpress.com/2014/02/26/syntethic-and-bridge-methods/
                                !m.isBridge() && !m.isSynthetic() &&
                                        // private, static and final methods cannot be overridden
                                        !Modifier.isPrivate(m.getModifiers()) && !Modifier.isFinal(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()) &&
                                        // we also don't want to cope with methods declared in Object
                                        !m.getDeclaringClass().equals(Object.class))
                                .map(ComparableMethod::new));
    }

    Stream<Class<?>> loadClasses(String srcDir) {
        final Path path = Paths.get(srcDir);
        final java.util.List<Class<?>> classes = new ArrayList<>();
        Try.of(() ->
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory()) {
                            final String name = file.subpath(path.getNameCount(), file.getNameCount()).toString();
                            if (name.endsWith(".java") && !name.endsWith(File.separator + "package-info.java")) {
                                final String className = name.substring(0, name.length() - ".java".length())
                                        .replaceAll("/", ".")
                                        .replaceAll("\\\\", ".");
                                try {
                                    final Class<?> clazz = getClass().getClassLoader().loadClass(className);
                                    classes.add(clazz);
                                } catch (ClassNotFoundException e) {
                                    throw new IOException(e);
                                }
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                }));
        return Stream.ofAll(classes);
    }

    static class ComparableMethod implements Comparable<ComparableMethod> {

        final Method m;

        ComparableMethod(Method m) {
            this.m = m;
        }

        @Override
        public int compareTo(ComparableMethod that) {
            return this.toString().compareTo(that.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ComparableMethod) {
                final ComparableMethod that = (ComparableMethod) o;
                return Objects.equals(this.m.getName(), that.m.getName()) &&
                        Arrays.equals(this.m.getParameterTypes(), that.m.getParameterTypes());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(m.getName(), Arrays.hashCode(m.getParameterTypes()));
        }

        @Override
        public String toString() {
            return m.getName() +
                    List.of(m.getParameterTypes()).map(Class::getName).join(", ", "(", ")") +
                    ": " +
                    m.getReturnType().getName();
        }
    }
}