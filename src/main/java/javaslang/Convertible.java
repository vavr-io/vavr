/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.Either;
import javaslang.control.Match;
import javaslang.control.Option;
import javaslang.control.Try;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Conversion methods, shared by {@link javaslang.algebra.Monad} and {@link javaslang.Value}.
 *
 * @param <T> Component type.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Convertible<T> {

    /**
     * Provides syntactic sugar for {@link javaslang.control.Match.MatchMonad.Of}.
     * <p>
     * We write
     *
     * <pre><code>
     * value.match()
     *      .when(...).then(...)
     *      .get();
     * </code></pre>
     *
     * instead of
     *
     * <pre><code>
     * Match.of(value)
     *      .when(...).then(...)
     *      .get();
     * </code></pre>
     *
     * @return a new type-safe match builder.
     */
    Match.MatchMonad.Of<? extends Convertible<T>> match();

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
     * Converts this value to a {@link Either}.
     *
     * @param <R>   right type
     * @param right A supplier of a right value
     * @return A new {@link Either.Right} containing the result of {@code right} if this is empty, otherwise
     * a new {@link Either.Left} containing this value.
     * @throws NullPointerException if {@code right} is null
     */
    <R> Either<T, R> toLeft(Supplier<? extends R> right);

    /**
     * Converts this value to a {@link Either}.
     *
     * @param <R>   right type
     * @param right An instance of a right value
     * @return A new {@link Either.Right} containing the value of {@code right} if this is empty, otherwise
     * a new {@link Either.Left} containing this value.
     * @throws NullPointerException if {@code right} is null
     */
    <R> Either<T, R> toLeft(R right);

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
     * Converts this value to a {@link Either}.
     *
     * @param <L>  left type
     * @param left A supplier of a left value
     * @return A new {@link Either.Left} containing the result of {@code left} if this is empty, otherwise
     * a new {@link Either.Right} containing this value.
     * @throws NullPointerException if {@code left} is null
     */
    <L> Either<L, T> toRight(Supplier<? extends L> left);

    /**
     * Converts this value to a {@link Either}.
     *
     * @param <L>  left type
     * @param left An instance of a left value
     * @return A new {@link Either.Left} containing the value of {@code left} if this is empty, otherwise
     * a new {@link Either.Right} containing this value.
     * @throws NullPointerException if {@code left} is null
     */
    <L> Either<L, T> toRight(L left);

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
