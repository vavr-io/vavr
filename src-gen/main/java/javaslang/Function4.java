/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a function with 4 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> or a
     * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda
     * expression</a> to a {@code Function4}.
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
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a {@code Function4}
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> lift(Function4<T1, T2, T3, T4, R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to 4 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @return the result of function application
     * 
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Checks if this function is applicable to the given objects,
     * i.e. each of the given objects is either null or the object type is assignable to the parameter type.
     * <p>
     * Please note that it is not checked if this function is defined for the given objects.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @param o4 object 4
     * @return true, if this function is applicable to the given objects, false otherwise.
     */
    default boolean isApplicableTo(Object o1, Object o2, Object o3, Object o4) {
        final Class<?>[] paramTypes = getType().parameterTypes();
        return
                (o1 == null || paramTypes[0].isAssignableFrom(o1.getClass())) &&
                (o2 == null || paramTypes[1].isAssignableFrom(o2.getClass())) &&
                (o3 == null || paramTypes[2].isAssignableFrom(o3.getClass())) &&
                (o4 == null || paramTypes[3].isAssignableFrom(o4.getClass()));
    }

    /**
     * Checks if this function is generally applicable to objects of the given types.
     *
     * @param type1 type 1
     * @param type2 type 2
     * @param type3 type 3
     * @param type4 type 4
     * @return true, if this function is applicable to objects of the given types, false otherwise.
     */
    default boolean isApplicableToTypes(Class<?> type1, Class<?> type2, Class<?> type3, Class<?> type4) {
        Objects.requireNonNull(type1, "type1 is null");
        Objects.requireNonNull(type2, "type2 is null");
        Objects.requireNonNull(type3, "type3 is null");
        Objects.requireNonNull(type4, "type4 is null");
        final Class<?>[] paramTypes = getType().parameterTypes();
        return
                paramTypes[0].isAssignableFrom(type1) &&
                paramTypes[1].isAssignableFrom(type2) &&
                paramTypes[2].isAssignableFrom(type3) &&
                paramTypes[3].isAssignableFrom(type4);
    }

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     * 
     */
    default Function3<T2, T3, T4, R> apply(T1 t1) {
        return (T2 t2, T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     * 
     */
    default Function2<T3, T4, R> apply(T1 t1, T2 t2) {
        return (T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     * 
     */
    default Function1<T4, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4) -> apply(t1, t2, t3, t4);
    }

    @Override
    default int arity() {
        return 4;
    }

    @Override
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
    }

    @Override
    default Function1<Tuple4<T1, T2, T3, T4>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4);
    }

    @Override
    default Function4<T4, T3, T2, T1, R> reversed() {
        return (t4, t3, t2, t1) -> apply(t1, t2, t3, t4);
    }

    @Override
    default Function4<T1, T2, T3, T4, R> memoized() {
        if (this instanceof Memoized) {
            return this;
        } else {
            final Object lock = new Object();
            final Map<Tuple4<T1, T2, T3, T4>, R> cache = new HashMap<>();
            final Function1<Tuple4<T1, T2, T3, T4>, R> tupled = tupled();
            return (Function4<T1, T2, T3, T4, R> & Memoized) (t1, t2, t3, t4) -> {
                synchronized (lock) {
                    return cache.computeIfAbsent(Tuple.of(t1, t2, t3, t4), tupled::apply);
                }
            };
        }
    }

    /**
     * Returns a composed function that first applies this Function4 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function4<T1, T2, T3, T4, V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }

    @Override
    default Type<T1, T2, T3, T4, R> getType() {
        return new Type<>(this);
    }

    /**
     * Represents the type of a {@code Function} which consists of 4 <em>parameter 4 types</em>
     * and a <em>return type</em>.
     *
     *
     * @param <T1> the 1st parameter type of the function
     * @param <T2> the 2nd parameter type of the function
     * @param <T3> the 3rd parameter type of the function
     * @param <T4> the 4th parameter type of the function
     * @param <R> the return type of the function
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    final class Type<T1, T2, T3, T4, R> extends λ.Type<R> {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("deprecation")
        private Type(Function4<T1, T2, T3, T4, R> λ) {
            super(λ);
        }

        @SuppressWarnings("unchecked")
        public Class<T1> parameterType1() {
            return (Class<T1>) parameterTypes()[0];
        }

        @SuppressWarnings("unchecked")
        public Class<T2> parameterType2() {
            return (Class<T2>) parameterTypes()[1];
        }

        @SuppressWarnings("unchecked")
        public Class<T3> parameterType3() {
            return (Class<T3>) parameterTypes()[2];
        }

        @SuppressWarnings("unchecked")
        public Class<T4> parameterType4() {
            return (Class<T4>) parameterTypes()[3];
        }
    }
}