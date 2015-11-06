/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Folding is an application of {@code Monoid}s.
 * <p>
 * <strong>Example:</strong>
 *
 * <pre><code>
 * Monoid&lt;String&gt; concat = Monoid.of("", (a1, a2) -&gt; a1 + a2);
 * Stream.of("1", "2", "3").fold(concat);
 * </code></pre>
 *
 * is the same as
 *
 * <pre><code>
 * Stream.of("1", "2", "3").fold("", (a1, a2) -&gt; a1 + a2);
 * </code></pre>
 *
 * @param <T> Component type of this foldable
 */
public interface Foldable<T> {

    /**
     * Folds this elements from the left, starting with {@code monoid.zero()} and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @return a folded value
     * @throws NullPointerException if {@code monoid} is null
     */
    @SuppressWarnings("unchecked")
    default T fold(Monoid<? extends T> monoid) {
        Objects.requireNonNull(monoid, "fold monoid is null");
        final Monoid<T> m = (Monoid<T>) monoid;
        return foldLeft(m.zero(), m::combine);
    }

    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> combine) {
        Objects.requireNonNull(combine, "fold combine is null");
        return foldLeft(zero, combine);
    }

    /**
     * Folds this elements from the left, starting with {@code monoid.zero()} and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @return a folded value
     * @throws NullPointerException if {@code monoid} is null
     */
    @SuppressWarnings("unchecked")
    default T foldLeft(Monoid<? extends T> monoid) {
        Objects.requireNonNull(monoid, "foldLeft monoid is null");
        final Monoid<T> m = (Monoid<T>) monoid;
        return foldLeft(m.zero(), m::combine);
    }

    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param <U> the type of the folded value
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine);

    /**
     * Maps this elements to a {@code Monoid} and applies {@code foldLeft}, starting with {@code monoid.zero()}:
     * <pre><code>
     *  foldLeft(monoid.zero(), (ys, x) -&gt; monoid.combine(ys, mapper.apply(x)));
     * </code></pre>
     *
     * @param monoid A Monoid
     * @param mapper A mapper
     * @param <U>    Component type of the given monoid.
     * @return the folded monoid value.
     * @throws NullPointerException if {@code monoid} or {@code mapper} is null
     */
    default <U> U foldMap(Monoid<U> monoid, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(monoid.zero(), (ys, x) -> monoid.combine(ys, mapper.apply(x)));
    }

    /**
     * Folds this elements from the right, starting with {@code monoid.zero()} and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @return a folded value
     * @throws NullPointerException if {@code monoid} is null
     */
    @SuppressWarnings("unchecked")
    default T foldRight(Monoid<? extends T> monoid) {
        Objects.requireNonNull(monoid, "foldRight monoid is null");
        final Monoid<T> m = (Monoid<T>) monoid;
        return foldRight(m.zero(), m::combine);
    }

    /**
     * Folds this elements from the right, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param <U> the type of the folded value
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine);

}
