/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.collection.Stream;
import javaslang.collection.Traversable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class TypeChecker {

    public static void main(String[] args) {
        new TypeChecker().shouldHaveAConsistentTypeSystem();
    }

    //@Test
    public void shouldHaveAConsistentTypeSystem() {
        // TODO: check(BinaryTree.class, RoseTree.class, List.class, Stream.class, Success.class, Failure.class, Left.class, Right.class, None.class, Some.class, Monoid.class, Monad1.class, ...)
        final Class<?> clazz = Seq.class;
        final Traversable<Method> unoverriddenMethods = getUnoverriddenMethods(clazz);
        if (!unoverriddenMethods.isEmpty()) {
            final String msg = String.format("%s has to override the following methods with return type %s:\n%s",
                    clazz.getName(), clazz.getSimpleName(), unoverriddenMethods.map(Object::toString).join("\n"));
            // TODO: throw new AssertionError(msg);
            System.out.println(msg);
        }
    }

    Traversable<Method> getUnoverriddenMethods(Class<?> clazz) {
        final Traversable<Class<?>> superClasses = Stream.of(clazz.getInterfaces())
                .append(clazz.getSuperclass())
                .filter(c -> c != null);
        if (superClasses.isEmpty()) {
            return Stream.nil();
        } else {
            final Traversable<ComparableMethod> superMethods = getRelevantMethods(superClasses).filter(comparableMethod ->
                    // We're interested in methods that should be overridden with actual type as return type.
                    // Because we check this recursively, the class hierarchy is consistent here.
                    comparableMethod.m.getDeclaringClass().equals(comparableMethod.m.getReturnType()));
            final Traversable<ComparableMethod> thisMethods = getRelevantMethods(Stream.of(clazz));
            return superMethods.filter(superMethod -> thisMethods
                        .findFirst(thisMethod -> thisMethod.equals(superMethod))
                        // TODO: special case if visibility is package private and classes are in different package
                        .map(thisMethod -> !thisMethod.m.getDeclaringClass().equals(thisMethod.m.getReturnType()))
                        .orElse(true))
                    .map(comparableMethod -> comparableMethod.m);
        }
    }

    // TODO: change Traversable to Seq after running TypeChecker and fixing findings
    Traversable<ComparableMethod> getRelevantMethods(Traversable<Class<?>> classes) {
        return classes
                .flatMap(clazz ->
                        Stream.of(clazz.getDeclaredMethods()).filter((Method m) ->
                                // https://javax0.wordpress.com/2014/02/26/syntethic-and-bridge-methods/
                                !m.isBridge() && !m.isSynthetic() &&
                                // private and static methods cannot be overridden
                                !Modifier.isPrivate(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()))
                            .map(ComparableMethod::new));
    }

    static class ComparableMethod {

        final Method m;

        ComparableMethod(Method m) {
            this.m = m;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ComparableMethod) {
                final ComparableMethod that = (ComparableMethod) o;
                return Objects.equals(this.m.getName(), that.m.getName()) &&
                        Objects.equals(this.m.getParameterTypes(), that.m.getParameterTypes());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(m.getName(), m.getParameterTypes());
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
