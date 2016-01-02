/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Try;
import javaslang.λModule.ReflectionUtil;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * This is a general definition of a (checked/unchecked) function of unknown parameters and a return type R.
 * <p>
 * A checked function may throw an exception. The exception type cannot be expressed as a generic type parameter
 * because Java cannot calculate type bounds on function composition.
 *
 * @param <R> Return type of the function.
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface λ<R> extends Serializable {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * @return the number of function arguments.
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    int arity();

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    // generic argument count varies
    @SuppressWarnings("rawtypes")
    λ curried();

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    λ<R> tupled();

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    λ<R> reversed();

    /**
     * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
     * On subsequent calls given the same arguments the memoized value is returned.
     * <p>
     * Please note that memoizing functions do not permit {@code null} as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    λ<R> memoized();

    /**
     * Checks if this function is memoizing (= caching) computed values.
     *
     * @return true, if this function is memoizing, false otherwise
     */
    boolean isMemoized();

    /**
     * Get reflective type information about lambda parameters and return type.
     *
     * @return A new instance containing the type information
     */
    Type<R> getType();

    /**
     * Checks if this function is applicable to the given objects,
     * i.e. each of the given objects is either null or the object type is assignable to the parameter type.
     * <p>
     * Please note that it is not checked if this function is defined for the given objects.
     * <p>
     * A function is applicable to no objects by definition.
     *
     * @param objects Objects, may be null
     * @return true, if {@code 0 < objects.length <= arity()} and this function is applicable to the given objects, false otherwise.
     * @throws NullPointerException if {@code objects} is null.
     */
    default boolean isApplicableTo(Object... objects) {
        Objects.requireNonNull(objects, "objects is null");
        if (objects.length == 0 || objects.length > arity()) {
            return false;
        }
        final Class<?>[] paramTypes = getType().parameterTypes();
        for (int i = 0; i < objects.length; i++) {
            final Object o = objects[i];
            if (o != null && !paramTypes[i].isAssignableFrom(o.getClass())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this function is generally applicable to objects of the given types.
     * <p>
     * A function is applicable to no types by definition.
     *
     * @param types Argument types
     * @return true, if {@code 0 <= types.length <= arity()} and this function is applicable to objects of the given types, false otherwise.
     * @throws NullPointerException if {@code types} or one of the elements of {@code types} is null.
     */
    default boolean isApplicableToTypes(Class<?>... types) {
        Objects.requireNonNull(types, "types is null");
        if (types.length == 0 || types.length > arity()) {
            return false;
        }
        final Class<?>[] paramTypes = getType().parameterTypes();
        for (int i = 0; i < types.length; i++) {
            final Class<?> type = Objects.requireNonNull(types[i], "types[" + i + "] is null");
            if (!paramTypes[i].isAssignableFrom(type)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Represents the type of a function which consists of <em>parameter types</em> and a <em>return type</em>.
     *
     * @param <R> the return type of the function
     * @since 2.0.0
     */
    // DEV-NOTE: implicitly static and therefore not leaking implicit this reference of enclosing instance
    abstract class Type<R> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Class<R> returnType;
        private final Class<?>[] parameterTypes;

        /**
         * Internal constructor.
         *
         * @param λ the outer function instance of this type
         */
        @SuppressWarnings("unchecked")
        protected Type(λ<R> λ) {
            final MethodType methodType = ReflectionUtil.getLambdaSignature(λ);
            this.returnType = (Class<R>) methodType.returnType();
            this.parameterTypes = methodType.parameterArray();
        }

        public Class<R> returnType() {
            return returnType;
        }

        public Class<?>[] parameterTypes() {
            return parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Type) {
                final Type<?> that = (Type<?>) o;
                return this.hashCode() == that.hashCode()
                        && this.returnType().equals(that.returnType)
                        && Arrays.equals(this.parameterTypes, that.parameterTypes);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return List.of(parameterTypes())
                    .map(c -> c.getName().hashCode())
                    .fold(1, (acc, i) -> acc * 31 + i)
                    * 31 + returnType().getName().hashCode();
        }

        @Override
        public String toString() {
            return List.of(parameterTypes).map(Class::getName).mkString("(", ", ", ")") + " -> " + returnType.getName();
        }
    }
}

interface λModule {

    // hiding this functionality
    final class ReflectionUtil {

        static MethodType getLambdaSignature(Serializable lambda) {
            final String signature = getSerializedLambda(lambda).getInstantiatedMethodType();
            return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
        }

        private static SerializedLambda getSerializedLambda(Serializable lambda) {
            return Try.of(() -> {
                final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                return (SerializedLambda) method.invoke(lambda);
            }).get();
        }
    }
}
