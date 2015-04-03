/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.algebra.Monad1;
import javaslang.algebra.Monoid;
import javaslang.collection.*;
import javaslang.control.*;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class TypeChecker {

    @Test
    @Ignore
    public void shouldHaveAConsistentTypeSystem() throws Exception {
        // TODO: recursively check the type hierarchy (e.g. Seq in the case of List and Stream - but remember visited classes)
        // TODO: perform this check for *all* Javaslang classes (src and src-gen)
        final Stream<String> msgs = Stream.of(BinaryTree.class, RoseTree.class, List.class, Stream.class, Success.class, Failure.class, Left.class, Right.class, None.class, Some.class, Monoid.class, Monad1.class)
                .map(clazz -> Tuple.of(clazz, getUnoverriddenMethods(clazz)))
                .filter(findings -> !findings._2.isEmpty())
                .map(findings -> String.format("%s has to override the following methods with return type %s:\n%s",
                        findings._1.getName(), findings._1.getSimpleName(), findings._2.map(Object::toString).join("\n")));
        if (!msgs.isEmpty()) {
            throw new AssertionError(msgs.join("\n\n", "Unoverriden methods found.\n", ""));
        }
    }

    Traversable<Method> getUnoverriddenMethods(Class<?> clazz) {
        final Traversable<Class<?>> superClasses = Stream.of(clazz.getInterfaces())
                .append(clazz.getSuperclass())
                .filter(c -> c != null);
        if (superClasses.isEmpty()) {
            return Stream.nil();
        } else {
            final Traversable<ComparableMethod> superMethods = getOverridableMethods(superClasses).filter(comparableMethod ->
                    // We're interested in methods that should be overridden with actual type as return type.
                    // Because we check this recursively, the class hierarchy is consistent here.
                    comparableMethod.m.getDeclaringClass().equals(comparableMethod.m.getReturnType()));
            final Traversable<ComparableMethod> thisMethods = getOverridableMethods(Stream.of(clazz));
            return superMethods.filter(superMethod -> thisMethods
                    .findFirst(thisMethod -> thisMethod.equals(superMethod))
                            // TODO: special case if visibility is package private and classes are in different package
                    .map(thisMethod -> !clazz.equals(thisMethod.m.getReturnType()))
                    .orElse(true))
                    .map(comparableMethod -> comparableMethod.m)
                    // TODO: .sort()
                    ;
        }
    }

    // TODO: change Traversable to Seq after running TypeChecker and fixing findings
    Traversable<ComparableMethod> getOverridableMethods(Traversable<Class<?>> classes) {
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
