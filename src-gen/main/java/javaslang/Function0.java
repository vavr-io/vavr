/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import javaslang.collection.List;
import javaslang.control.Try;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function0<R> extends λ<R>, Supplier<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> or a
     * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda
     * expression</a> to a {@code Function0}.
     * <p>
     * Examples (w.l.o.g. referring to Function1):
     * <pre><code>// lifting a lambda expression
     * Function1&lt;Integer, Integer&gt; add1 = Function1.lift(i -&gt; i + 1);
     *
     * // lifting a method reference (, e.g. Integer method(Integer i) { return i + 1; })
     * Function1&lt;Integer, Integer&gt; add2 = Function1.lift(this::method);
     *
     * // lifting a lambda reference
     * Function1&lt;Integer, Integer&gt; add3 = Function1.lift(add1::apply);
     * </code></pre>
     * <p>
     * <strong>Caution:</strong> Reflection loses type information of lifted lambda reference.
     * <pre><code>// type of lifted a lambda expression
     * MethodType type1 = add1.getType(); // (Integer)Integer
     *
     * // type of lifted method reference
     * MethodType type2 = add2.getType(); // (Integer)Integer
     *
     * // type of lifted lambda reference
     * MethodType type2 = add3.getType(); // (Object)Object
     * </code></pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @return a {@code Function0}
     */
    static <R> Function0<R> lift(Function0<R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * 
     */
    R apply();

    /**
     * Implementation of {@linkplain java.util.function.Supplier#get()}, just calls {@linkplain #apply()}.
     *
     * @return the result of {@code apply()}
     */
    @Override
    default R get() {
        return apply();
    }

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default Function0<R> curried() {
        return this;
    }

    @Override
    default Function1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default Function0<R> reversed() {
        return this;
    }

    @Override
    default Function0<R> memoized() {
        if (this instanceof Memoized) {
            return this;
        } else {
            return (Function0<R> & Memoized) Lazy.of(this::apply)::get;
        }
    }

    /**
     * Returns a composed function that first applies this Function0 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function0<V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return () -> after.apply(apply());
    }

    @Override
    default Type<R> getType() {
        return Type.of(this);
    }

    /**
     * Represents the type of a {@code Function} which consists of 0 <em>parameter no types</em>
     * and a <em>return type</em>.
     *
     *
     * @param <R> the return type of the function
     */
    final class Type<R> implements λ.Type<R>, Serializable {

        private static final long serialVersionUID = 1L;

        private final Class<R> returnType;
        private final Class<?>[] parameterArray;

        private transient final Lazy<Integer> hashCode = Lazy.of(() -> List.of(parameterArray())
                .map(c -> c.getName().hashCode())
                .fold(1, (acc, i) -> acc * 31 + i)
                * 31 + returnType().getName().hashCode()
        );

        private Type(Class<R> returnType, Class<?>[] parameterArray) {
            this.returnType = returnType;
            this.parameterArray = parameterArray;
        }

        @SuppressWarnings("unchecked")
        private static <R> Type<R> of(Function0<R> f) {
            final MethodType methodType = getLambdaSignature(f);
            return new Type<>((Class<R>) methodType.returnType(), methodType.parameterArray());
        }

        // TODO: get rid of this repitition in every Function*.Type (with Java 9?)
        private static MethodType getLambdaSignature(Serializable lambda) {
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

        @SuppressWarnings("unchecked")
        @Override
        public Class<R> returnType() {
            return returnType;
        }

        @Override
        public Class<?>[] parameterArray() {
            return parameterArray;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Type) {
                final Type<?> that = (Type<?>) o;
                return this.hashCode() == that.hashCode()
                        && this.returnType.equals(that.returnType)
                        && Arrays.equals(this.parameterArray, that.parameterArray);
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
            return List.of(parameterArray).map(Class::getName).join(", ", "(", ")")
                    + " -> "
                    + returnType.getName();
        }
    }
}