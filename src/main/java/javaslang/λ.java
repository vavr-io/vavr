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
    Type<R> getType();

    /**
     * Represents the type of a function which consists of <em>parameter types</em> and a <em>return type</em>.
     *
     * @param <R> the return type of the function
     * @since 2.0.0
     */
    interface Type<R> {

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
        Class<?>[] parameterTypes();
    }

    /**
     * This class is needed because the interface {@link Type} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * <p>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <R> Result type of the function
     * @since 2.0.0
     * @deprecated Internal API, not intended to be used. This class will disappear from public API as soon as possible.
     */
    @Deprecated
    abstract class AbstractType<R> implements Type<R>, Serializable {

        private static final long serialVersionUID = 1L;

        private final Class<R> returnType;
        private final Class<?>[] parameterTypes;

        private transient final Lazy<Integer> hashCode = Lazy.of(() -> List.of(parameterTypes())
                        .map(c -> c.getName().hashCode())
                        .fold(1, (acc, i) -> acc * 31 + i)
                        * 31 + returnType().getName().hashCode()
        );

        /**
         * Internal constructor.
         *
         * @param λ the outer function instance of this type
         * @deprecated There should be a constructor {@code AbstractType(Class<R> returnType, Class<?>[] parameterArray)} but because of implementation details this one is needed. It will disappear as soon as possible.
         */
        @SuppressWarnings("unchecked")
        @Deprecated
        protected AbstractType(λ<R> λ) {

            // hiding this functionality
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

            final MethodType methodType = new ReflectionUtil().getLambdaSignature(λ);

            this.returnType = (Class<R>) methodType.returnType();
            this.parameterTypes = methodType.parameterArray();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<R> returnType() {
            return returnType;
        }

        @Override
        public Class<?>[] parameterTypes() {
            return parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof AbstractType) {
                final AbstractType<?> that = (AbstractType<?>) o;
                return this.hashCode() == that.hashCode()
                        && this.returnType().equals(that.returnType)
                        && Arrays.equals(this.parameterTypes, that.parameterTypes);
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
            return List.of(parameterTypes).map(Class::getName).join(", ", "(", ")")
                    + " -> "
                    + returnType.getName();
        }
    }

    /**
     * Tagging ZAM interface for Memoized functions.
     */
    interface Memoized {
    }
}
