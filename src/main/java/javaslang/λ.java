/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Try;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This is a general definition of a (checked/unchecked) function of unknown parameters and a return type R.
 * <p>
 * A checked function may throw an exception. The exception type cannot be expressed as a generic type parameter
 * because Java cannot calculate type bounds on function composition.
 *
 * @param <R> Return type of the function.
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
     * Please note that memoizing functions do not permit `null` as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    λ<R> memoized();

    /**
     * Checks if this function is memoizing computed values.
     *
     * @return true, if this instance implements {@link Memoized}, false otherwise
     */
    default boolean isMemoized() {
        return this instanceof Memoized;
    }

    /**
     * Get reflective type information about lambda parameters and return type.
     *
     * @return A new instance containing the type information
     */
    default Type<R> getType() {

        final class ReflectionUtil {

            MethodType getLambdaSignature(Serializable lambda) {
                final String signature = getSerializedLambda(lambda).getInstantiatedMethodType();
                return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
            }

            private SerializedLambda getSerializedLambda(Serializable lambda) {
                return Try.of(() -> {
                    final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
                    method.setAccessible(true);
                    return (SerializedLambda) method.invoke(lambda);
                }).get();
            }
        }

        final MethodType methodType = new ReflectionUtil().getLambdaSignature(this);

        return new Type<R>() {

            private static final long serialVersionUID = 1L;

            private transient final Lazy<Integer> hashCode = Lazy.of(() -> List.of(parameterArray())
                    .map(c -> c.getName().hashCode())
                    .fold(1, (acc, i) -> acc * 31 + i)
                    * 31 + returnType().getName().hashCode()
            );

            @SuppressWarnings("unchecked")
            @Override
            public Class<R> returnType() {
                return (Class<R>) methodType.returnType();
            }

            @Override
            public Class<?>[] parameterArray() {
                return methodType.parameterArray();
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                } else if (o instanceof Type) {
                    final Type<?> that = (Type<?>) o;
                    return this.hashCode() == that.hashCode()
                            && this.returnType().equals(that.returnType())
                            && Arrays.equals(this.parameterArray(), that.parameterArray());
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return hashCode.get();
            }

            @Override
            public String toString() {
                return List.of(parameterArray()).map(Class::getName).join(", ", "(", ")")
                        + " -> "
                        + returnType().getName();
            }
        };
    }

    /**
     * Represents the type of a function which consists of <em>parameter types</em> and a <em>return type</em>.
     *
     * @param <R> the return type of the function
     */
    interface Type<R> extends Serializable {

        long serialVersionUID = 1L;

        /**
         * Returns the return type of the {@code λ}.
         *
         * @return the return type
         */
        Class<R> returnType();

        /**
         * Presents the parameter types of the {@code λ} as an array.
         *
         * @return the parameter types
         */
        Class<?>[] parameterArray();
    }

    /**
     * Tagging ZAM interface for Memoized functions.
     */
    interface Memoized {
    }
}
