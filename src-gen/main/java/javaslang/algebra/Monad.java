/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javaslang.*;
import javaslang.collection.*;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Defines a Monad by generalizing the flatMap function.
 * <p>
 * A {@code Monad} is a {@link Functor} with a {@code flatMap} method that satisfies the Monad laws, also known
 * as the three control laws:
 * <p>
 * Let
 * <ul>
 * <li>{@code A}, {@code B}, {@code C} be types</li>
 * <li>{@code unit: A -> Monad<A>} a constructor</li>
 * <li>{@code f: A -> Monad<B>}, {@code g: B -> Monad<C>} functions</li>
 * <li>{@code a} be an object of type {@code A}</li>
 * <li>{@code m} be an object of type {@code Monad<A>}</li>
 * </ul>
 * Then all instances of the {@code Monad} interface should obey the three control laws:
 * <ul>
 * <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 * <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 * <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))}</li>
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
 *
 * @param <T> component type of this monad
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public interface Monad<T> extends Functor<T>, Iterable<T>, Convertible<T> {

    /**
     * Lifts a {@code Function} to a higher {@code Function1} that operates on Monads.
     *
     * @param <T> 1st argument type of f
     * @param <R> result type of f
     * @param f a Function
     * @return a new Function1 that lifts the given function f in a layer that operates on monads.
     */
    static <T, R> Function1<? super Monad<T>, Monad<R>> lift(Function<? super T, ? extends R> f) {
        return mT -> mT.map(f::apply);
    }

    /**
     * Lifts a {@code BiFunction} to a higher {@code Function2} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <R> result type of f
     * @param f a BiFunction
     * @return a new Function2 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, R> Function2<Monad<T1>, Monad<T2>, Monad<R>> lift(BiFunction<? super T1, ? super T2, ? extends R> f) {
        return (mT1, mT2) ->
                       mT1.flatMap(t1 ->
                       mT2.map(t2 -> f.apply(t1, t2)));
    }

    /**
     * Lifts a {@code Function3} to a higher {@code Function3} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <R> result type of f
     * @param f a Function3
     * @return a new Function3 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, R> Function3<Monad<T1>, Monad<T2>, Monad<T3>, Monad<R>> lift(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        return (mT1, mT2, mT3) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.map(t3 -> f.apply(t1, t2, t3))));
    }

    /**
     * Lifts a {@code Function4} to a higher {@code Function4} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <R> result type of f
     * @param f a Function4
     * @return a new Function4 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, R> Function4<Monad<T1>, Monad<T2>, Monad<T3>, Monad<T4>, Monad<R>> lift(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        return (mT1, mT2, mT3, mT4) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.flatMap(t3 ->
                       mT4.map(t4 -> f.apply(t1, t2, t3, t4)))));
    }

    /**
     * Lifts a {@code Function5} to a higher {@code Function5} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <R> result type of f
     * @param f a Function5
     * @return a new Function5 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, R> Function5<Monad<T1>, Monad<T2>, Monad<T3>, Monad<T4>, Monad<T5>, Monad<R>> lift(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        return (mT1, mT2, mT3, mT4, mT5) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.flatMap(t3 ->
                       mT4.flatMap(t4 ->
                       mT5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
    }

    /**
     * Lifts a {@code Function6} to a higher {@code Function6} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <R> result type of f
     * @param f a Function6
     * @return a new Function6 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, R> Function6<Monad<T1>, Monad<T2>, Monad<T3>, Monad<T4>, Monad<T5>, Monad<T6>, Monad<R>> lift(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.flatMap(t3 ->
                       mT4.flatMap(t4 ->
                       mT5.flatMap(t5 ->
                       mT6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
    }

    /**
     * Lifts a {@code Function7} to a higher {@code Function7} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <T7> 7th argument type of f
     * @param <R> result type of f
     * @param f a Function7
     * @return a new Function7 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, T7, R> Function7<Monad<T1>, Monad<T2>, Monad<T3>, Monad<T4>, Monad<T5>, Monad<T6>, Monad<T7>, Monad<R>> lift(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.flatMap(t3 ->
                       mT4.flatMap(t4 ->
                       mT5.flatMap(t5 ->
                       mT6.flatMap(t6 ->
                       mT7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
    }

    /**
     * Lifts a {@code Function8} to a higher {@code Function8} that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <T7> 7th argument type of f
     * @param <T8> 8th argument type of f
     * @param <R> result type of f
     * @param f a Function8
     * @return a new Function8 that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<Monad<T1>, Monad<T2>, Monad<T3>, Monad<T4>, Monad<T5>, Monad<T6>, Monad<T7>, Monad<T8>, Monad<R>> lift(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7, mT8) ->
                       mT1.flatMap(t1 ->
                       mT2.flatMap(t2 ->
                       mT3.flatMap(t3 ->
                       mT4.flatMap(t4 ->
                       mT5.flatMap(t5 ->
                       mT6.flatMap(t6 ->
                       mT7.flatMap(t7 ->
                       mT8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
    }

    /**
     * Filters this {@code Monad} by testing a predicate.
     * <p>
     * The semantics may vary from class to class, e.g. for single-valued type (like Option) and multi-values types
     * (like Traversable). The commonality is, that filtered.isEmpty() will return true, if no element satisfied
     * the given predicate.
     * <p>
     * Also, an implementation may throw {@code NoSuchElementException}, if no element makes it through the filter
     * and this state cannot be reflected. E.g. this is the case for {@link javaslang.control.Either.LeftProjection} and
     * {@link javaslang.control.Either.RightProjection}.
     *
     * @param predicate A predicate
     * @return a new Monad instance
     * @throws NullPointerException if {@code predicate} is null
     */
    Monad<T> filter(Predicate<? super T> predicate);

    /**
     * FlatMaps this value to a new value with different component type.
     * <p>
     * FlatMap is the sequence operation for functions and behaves like the imperative {@code ;}.
     * <p>
     * If the previous results are needed, flatMap cascades:
     * <pre>
     * <code>
     * m1().flatMap(result1 -&gt;
     *      m2(result1).flatMap(result2 -&gt;
     *          m3(result1, result2).flatMap(result3 -&gt;
     *              ...
     *          )
     *      )
     * );
     * </code>
     * </pre>
     * If only the last result is needed, flatMap may be used sequentially:
     * <pre>
     * <code>
     * m1().flatMap(this::m2)
     *     .flatMap(this::m3)
     *     .flatMap(...);
     * </code>
     * </pre>
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Monad}
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Monad<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    // -- adjusting return types of super interface methods

    /**
     * Maps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Monad}
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    <U> Monad<U> map(Function<? super T, ? extends U> mapper);

}

/**
 * Conversion methods.
 *
 * @param <T> Component type.
 */
interface Convertible<T> {

    /**
     * Converts this value to a {@link Array}.
     *
     * @return A new {@link Array}.
     */
    Array<T> toArray();

    /**
     * Converts this value to a {@link CharSeq}.
     *
     * @return A new {@link CharSeq}.
     */
    CharSeq toCharSeq();

    /**
     * Converts this value to an untyped Java array.
     *
     * @return A new Java array.
     */
    default Object[] toJavaArray() {
        return toJavaList().toArray();
    }

    /**
     * Converts this value to a typed Java array.
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    T[] toJavaArray(Class<T> componentType);

    /**
     * Converts this value to an {@link java.util.List}.
     *
     * @return A new {@link java.util.ArrayList}.
     */
    java.util.List<T> toJavaList();

    /**
     * Converts this value to a {@link java.util.Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link java.util.HashMap}.
     */
    <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f);

    /**
     * Converts this value to an {@link java.util.Optional}.
     *
     * @return A new {@link java.util.Optional}.
     */
    java.util.Optional<T> toJavaOptional();

    /**
     * Converts this value to a {@link java.util.Set}.
     *
     * @return A new {@link java.util.HashSet}.
     */
    java.util.Set<T> toJavaSet();

    /**
     * Converts this value to a {@link java.util.stream.Stream}.
     *
     * @return A new {@link java.util.stream.Stream}.
     */
    java.util.stream.Stream<T> toJavaStream();

    /**
     * Converts this value to a {@link Lazy}.
     *
     * @return A new {@link Lazy}.
     */
    Lazy<T> toLazy();

    /**
     * Converts this value to a {@link List}.
     *
     * @return A new {@link List}.
     */
    List<T> toList();

    /**
     * Converts this value to a {@link Map}.
     *
     * @param mapper A function that maps an element to a key/value pair represented by Tuple2
     * @param <K>    The key type
     * @param <V>    The value type
     * @return A new {@link HashMap}.
     */
    <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> mapper);

    /**
     * Converts this value to an {@link Option}.
     *
     * @return A new {@link Option}.
     */
    Option<T> toOption();

    /**
     * Converts this value to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    Queue<T> toQueue();

    /**
     * Converts this value to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    Set<T> toSet();

    /**
     * Converts this value to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    Stack<T> toStack();

    /**
     * Converts this value to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    Stream<T> toStream();

    /**
     * Converts this value to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(NoSuchElementException)} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @return A new {@link Try}.
     */
    Try<T> toTry();

    /**
     * Converts this value to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(ifEmpty.get())} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @param ifEmpty an exception supplier
     * @return A new {@link Try}.
     */
    Try<T> toTry(Supplier<? extends Throwable> ifEmpty);

    /**
     * Converts this value to a {@link Tree}.
     *
     * @return A new {@link Tree}.
     */
    Tree<T> toTree();

    /**
     * Converts this value to a {@link Vector}.
     *
     * @return A new {@link Vector}.
     */
    Vector<T> toVector();

}