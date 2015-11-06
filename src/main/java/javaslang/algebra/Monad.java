/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines a Monad by generalizing the flatMap function.
 * <p>
 * All instances of the Monad interface should obey the three control laws:
 * <ul>
 * <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 * <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 * <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
 * </ul>
 * given
 * <ul>
 * <li>an object {@code m} of type {@code Monad<A>}</li>
 * <li>an object {@code a} of type {@code A}</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code Monad<A>}</li>
 * <li>a function {@code f: A → M}
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
 *
 * @param <T> component type of this monad
 * @since 1.1.0
 */
public interface Monad<T> {

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
     * Flattens this {@code Monad} by one level.
     * <p>
     * Example:
     *
     * <pre><code>List(Some(1), Some(2), None).flatten() = List(1, 2)</code></pre>
     *
     * <strong>Caution:</strong> Effectively {@code flatMap(Function.identity())} is called. That requires this
     * elements to be of type {@code java.lang.Iterable<U>}. More specifically this type {@code FilterMonadic<T>}
     * has to be of type {@code FilterMonadic<? extends java.lang.Iterable<U>>} for some given {@code U}.
     * We (currently) can't express this constraint with Java's type system. If this type does not fulfill the
     * requirement at runtime, a {@code ClassCastException} is thrown.
     * <p>
     * <strong>It is unsafe to use {@code flatten()}.</strong> Especially this compiles but throws at runtime:
     *
     * <pre><code>// Compiles. Throws at runtime because elements are not Iterable!
     * List&lt;String&gt; list = List(1, 2, 3).flatten();</code></pre>
     *
     * <strong>Also beware of the following exceptional cases:</strong>
     * {@link javaslang.collection.CharSeq#flatten()} and {@link javaslang.collection.Map#flatten()}.
     *
     * @param <U> the nested component type
     * @return A flattened version of this {@code Monad}.
     * @throws UnsupportedOperationException if this elements are not of type {@code ? extends java.lang.Iterable<? extends T>}.
     */
    <U> Monad<U> flatten();

    /**
     * FlatMaps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Monad}
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Monad<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    /**
     * Maps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Monad}
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Monad<U> map(Function<? super T, ? extends U> mapper);

}
