/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Represents a function with one argument.
 *
 * @param <T1> argument 1 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function1<T1, R> extends λ<R>, Function<T1, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> or a
     * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda
     * expression</a> to a {@code Function1}.
     * <p>
     * Examples (w.l.o.g. referring to Function1):
     * <pre><code>// lifting a lambda expression
     * Function1<Integer, Integer> add1 = Function1.lift(i -> i + 1);
     *
     * // lifting a method reference (, e.g. Integer method(Integer i) { return i + 1; })
     * Function1<Integer, Integer> add2 = Function1.lift(this::method);
     *
     * // lifting a lambda reference
     * Function1<Integer, Integer> add3 = Function1.lift(add1::apply);
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
     * @param <T1> 1st argument
     * @return a {@code Function1}
     */
    static <T1, R> Function1<T1, R> lift(Function1<T1, R> methodReference) {
        return methodReference;
    }

    /**
     * Returns the identity Function1, i.e. the function that returns its input.
     *
     * @param <T> argument type (and return type) of the identity function
     * @return the identity Function1
     */
    static <T> Function1<T, T> identity() {
        return t -> t;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param t1 argument 1
     * @return the result of function application
     * 
     */
    R apply(T1 t1);

    /**
     * Checks if this function is applicable to the given objects,
     * i.e. each of the given objects is either null or the object type is assignable to the parameter type.
     * <p>
     * Please note that it is not checked if this function is defined for the given objects.
     *
     * @param o1 object 1
     * @return true, if this function is applicable to the given objects, false otherwise.
     */
    default boolean isApplicableTo(Object o1) {
        final Class<?>[] paramTypes = getType().parameterArray();
        return
                (o1 == null || paramTypes[0].isAssignableFrom(o1.getClass()));
    }

    /**
     * Checks if this function is generally applicable to objects of the given types.
     *
     * @param type1 type 1
     * @return true, if this function is applicable to objects of the given types, false otherwise.
     */
    default boolean isApplicableToType(Class<?> type1) {
        Objects.requireNonNull(type1, "type1 is null");
        final Class<?>[] paramTypes = getType().parameterArray();
        return
                paramTypes[0].isAssignableFrom(type1);
    }

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default Function1<T1, R> curried() {
        return this;
    }

    @Override
    default Function1<Tuple1<T1>, R> tupled() {
        return t -> apply(t._1);
    }

    @Override
    default Function1<T1, R> reversed() {
        return this;
    }

    @Override
    default Function1<T1, R> memoized() {
        final Map<T1, R> cache = new ConcurrentHashMap<>();
        return t1 -> cache.computeIfAbsent(t1, this::apply);
    }

    /**
     * Returns a composed function that first applies this Function1 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function1<T1, V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1) -> after.apply(apply(t1));
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function1} {@code before} the
     * given argument and then applies this Function1 to the result.
     *
     * @param <V> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <V> Function1<V, R> compose(Function1<? super V, ? extends T1> before) {
        Objects.requireNonNull(before, "before is null");
        return v -> apply(before.apply(v));
    }
}