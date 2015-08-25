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
import javaslang.control.Try;

/**
 * Represents a function with 7 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <T5> argument 5 of the function
 * @param <T6> argument 6 of the function
 * @param <T7> argument 7 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> or a
     * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda
     * expression</a> to a {@code CheckedFunction7}.
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
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @param <T7> 7th argument
     * @return a {@code CheckedFunction7}
     */
    static <T1, T2, T3, T4, T5, T6, T7, R> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> lift(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to 7 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) throws Throwable;

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
     * @param o5 object 5
     * @param o6 object 6
     * @param o7 object 7
     * @return true, if this function is applicable to the given objects, false otherwise.
     */
    default boolean isApplicableTo(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        final Class<?>[] paramTypes = getType().parameterTypes();
        return
                (o1 == null || paramTypes[0].isAssignableFrom(o1.getClass())) &&
                (o2 == null || paramTypes[1].isAssignableFrom(o2.getClass())) &&
                (o3 == null || paramTypes[2].isAssignableFrom(o3.getClass())) &&
                (o4 == null || paramTypes[3].isAssignableFrom(o4.getClass())) &&
                (o5 == null || paramTypes[4].isAssignableFrom(o5.getClass())) &&
                (o6 == null || paramTypes[5].isAssignableFrom(o6.getClass())) &&
                (o7 == null || paramTypes[6].isAssignableFrom(o7.getClass()));
    }

    /**
     * Checks if this function is generally applicable to objects of the given types.
     *
     * @param type1 type 1
     * @param type2 type 2
     * @param type3 type 3
     * @param type4 type 4
     * @param type5 type 5
     * @param type6 type 6
     * @param type7 type 7
     * @return true, if this function is applicable to objects of the given types, false otherwise.
     */
    default boolean isApplicableToTypes(Class<?> type1, Class<?> type2, Class<?> type3, Class<?> type4, Class<?> type5, Class<?> type6, Class<?> type7) {
        Objects.requireNonNull(type1, "type1 is null");
        Objects.requireNonNull(type2, "type2 is null");
        Objects.requireNonNull(type3, "type3 is null");
        Objects.requireNonNull(type4, "type4 is null");
        Objects.requireNonNull(type5, "type5 is null");
        Objects.requireNonNull(type6, "type6 is null");
        Objects.requireNonNull(type7, "type7 is null");
        final Class<?>[] paramTypes = getType().parameterTypes();
        return
                paramTypes[0].isAssignableFrom(type1) &&
                paramTypes[1].isAssignableFrom(type2) &&
                paramTypes[2].isAssignableFrom(type3) &&
                paramTypes[3].isAssignableFrom(type4) &&
                paramTypes[4].isAssignableFrom(type5) &&
                paramTypes[5].isAssignableFrom(type6) &&
                paramTypes[6].isAssignableFrom(type7);
    }

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction6<T2, T3, T4, T5, T6, T7, R> apply(T1 t1) throws Throwable {
        return (T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction5<T3, T4, T5, T6, T7, R> apply(T1 t1, T2 t2) throws Throwable {
        return (T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction4<T4, T5, T6, T7, R> apply(T1 t1, T2 t2, T3 t3) throws Throwable {
        return (T4 t4, T5 t5, T6 t6, T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Applies this function partially to 4 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction3<T5, T6, T7, R> apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable {
        return (T5 t5, T6 t6, T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Applies this function partially to 5 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction2<T6, T7, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws Throwable {
        return (T6 t6, T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Applies this function partially to 6 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction1<T7, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws Throwable {
        return (T7 t7) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    default int arity() {
        return 7;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, CheckedFunction1<T5, CheckedFunction1<T6, CheckedFunction1<T7, R>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    default CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
    }

    @Override
    default CheckedFunction7<T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    default CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> memoized() {
        if (this instanceof Memoized) {
            return this;
        } else {
            final Object lock = new Object();
            final Map<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> cache = new HashMap<>();
            final CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled = tupled();
            return (CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> & Memoized) (t1, t2, t3, t4, t5, t6, t7) -> {
                synchronized (lock) {
                    return cache.computeIfAbsent(Tuple.of(t1, t2, t3, t4, t5, t6, t7), t -> Try.of(() -> tupled.apply(t)).get());
                }
            };
        }
    }

    /**
     * Returns a composed function that first applies this CheckedFunction7 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    default Type<T1, T2, T3, T4, T5, T6, T7, R> getType() {
        return new Type<>(this);
    }

    /**
     * Represents the type of a {@code CheckedFunction} which consists of 7 <em>parameter 7 types</em>
     * and a <em>return type</em>.
     *
     *
     * @param <T1> the 1st parameter type of the function
     * @param <T2> the 2nd parameter type of the function
     * @param <T3> the 3rd parameter type of the function
     * @param <T4> the 4th parameter type of the function
     * @param <T5> the 5th parameter type of the function
     * @param <T6> the 6th parameter type of the function
     * @param <T7> the 7th parameter type of the function
     * @param <R> the return type of the function
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    final class Type<T1, T2, T3, T4, T5, T6, T7, R> extends λ.Type<R> {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("deprecation")
        private Type(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> λ) {
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

        @SuppressWarnings("unchecked")
        public Class<T5> parameterType5() {
            return (Class<T5>) parameterTypes()[4];
        }

        @SuppressWarnings("unchecked")
        public Class<T6> parameterType6() {
            return (Class<T6>) parameterTypes()[5];
        }

        @SuppressWarnings("unchecked")
        public Class<T7> parameterType7() {
            return (Class<T7>) parameterTypes()[6];
        }
    }
}