/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.collection.Foldable;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>A Monoid is a {@linkplain javaslang.algebra.Semigroup} (types with an associative binary operation) that has an
 * identity element {@code zero}.</p>
 * <p>Given a type {@code A}, instances of Monoid should satisfy the following laws:</p>
 * <ul>
 * <li>Associativity: {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any {@code x,y,z} of type
 * {@code A}.</li>
 * <li>Identity: {@code combine(zero(), x) == x == combine(x, zero())} for any {@code x} of type {@code A}.</li>
 * </ul>
 * <p>Example: {@linkplain java.lang.String} is a Monoid with zero {@code ""} (empty String) and String concatenation
 * {@code +} as combine operation.</p>
 * <p>Please note that some types can be viewed as a monoid in more than one way, e.g. both addition and multiplication
 * on numbers.</p>
 *
 * Folding:
 *
 * <ul>
 * <li>{@link #fold(Monoid, Foldable)}</li>
 * <li>{@link #foldLeft(Monoid, Foldable)}</li>
 * <li>{@link #foldMap(Monoid, Foldable, Function)}</li>
 * <li>{@link #foldRight(Monoid, Foldable)}</li>
 * </ul>
 *
 * @param <A> A type.
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public interface Monoid<A> extends Semigroup<A> {

    /**
     * Factory method for monoids, taking a zero and a Semigroup.
     *
     * @param <A>       Value type
     * @param zero      The zero of the Monoid.
     * @param semigroup The associative binary operation of the Monoid. Please note that
     *                  {@linkplain javaslang.algebra.Semigroup} is a {@linkplain java.lang.FunctionalInterface}.
     * @return a new Monoid on type A
     * @throws NullPointerException if {@code semigroup} is null
     */
    static <A> Monoid<A> of(A zero, Semigroup<A> semigroup) {
        Objects.requireNonNull(semigroup, "semigroup is null");
        return new Monoid<A>() {
            @Override
            public A combine(A a1, A a2) {
                return semigroup.combine(a1, a2);
            }

            @Override
            public A zero() {
                return zero;
            }
        };
    }

    /**
     * The monoid of endomorphisms under composition.
     *
     * @param <A> Value type
     * @return The monoid of endomorphisms of type A.
     */
    static <A> Monoid<Function<A, A>> endoMonoid() {
        return Monoid.of(Function.identity(), Function::compose);
    }

    /**
     * The unique neutral element regarding {@linkplain #combine(Object, Object)}.
     *
     * @return The zero element of this Monoid
     */
    A zero();

    // -- Fold operations

    /**
     * Folds the elements of {@code Foldable} from the left, starting with {@code monoid.zero()}
     * and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @param foldable A foldable
     * @param <T> type of the foldable elements
     * @return a folded value
     * @throws NullPointerException if {@code monoid} or {@code foldable} is null
     */
    static <T> T fold(Monoid<T> monoid, Foldable<T> foldable) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(foldable, "foldable is null");
        return foldable.foldLeft(monoid.zero(), monoid::combine);
    }

    /**
     * Folds the elements of {@code Foldable} from the left, starting with {@code monoid.zero()}
     * and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @param foldable A foldable
     * @param <T> type of the foldable elements
     * @return a folded value
     * @throws NullPointerException if {@code monoid} is null
     */
    static <T> T foldLeft(Monoid<T> monoid, Foldable<T> foldable) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(foldable, "foldable is null");
        return foldable.foldLeft(monoid.zero(), monoid::combine);
    }

    /**
     * Maps this elements to a {@code Monoid} and applies {@code foldLeft}, starting with {@code monoid.zero()}:
     * <pre><code>
     *  foldLeft(monoid.zero(), (ys, x) -&gt; monoid.combine(ys, mapper.apply(x)));
     * </code></pre>
     *
     * @param monoid A Monoid
     * @param foldable A foldable
     * @param mapper A mapper
     * @param <T> type of the foldable elements
     * @param <U>    Component type of the given monoid.
     * @return the folded monoid value.
     * @throws NullPointerException if {@code monoid} or {@code mapper} is null
     */
    static<T, U> U foldMap(Monoid<U> monoid, Foldable<T> foldable, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(foldable, "foldable is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return foldable.foldLeft(monoid.zero(), (ys, x) -> monoid.combine(ys, mapper.apply(x)));
    }

    /**
     * Folds this elements from the right, starting with {@code monoid.zero()} and successively calling {@code monoid::combine}.
     *
     * @param monoid A monoid, providing a {@code zero} and a {@code combine} function.
     * @param foldable A foldable
     * @param <T> type of the foldable elements
     * @return a folded value
     * @throws NullPointerException if {@code monoid} is null
     */
    static <T> T foldRight(Monoid<T> monoid, Foldable<T> foldable) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(foldable, "foldable is null");
        return foldable.foldRight(monoid.zero(), monoid::combine);
    }

}
