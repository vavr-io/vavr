/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.function.Function;

/**
 * A Monoid is a Semigroup with an identity element {@code zero}, i.e. it consits of
 *
 * <ul>
 *     <li>Some type A</li>
 *     <li>An associative binary operation {@code combine}, such that {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any x,y,z of type A.</li>
 *     <li>An identity element {@code zero}, such that {@code combine(zero(), x) == x == combine(x, zero())} for any x of type A.</li>
 * </ul>
 *
 * @param <A> A type.
 */
public interface Monoid<A> extends Semigroup<A> {

    A zero();

    /**
     * Function composition of one type is an Endo monoid.
     *
     * @param <A> Value type
     * @return The Endo monoid of type A.
     */
    static <A> Monoid<Function<A, A>> endoMonoid() {
        return Monoid.of(Function.identity(), Function::compose);
    }

    /**
     * Factory method for monoidsm taking a zero and a Semigroup.
     *
     * @param <A> Component type
     * @param zero The zero of the Monoid.
     * @param semigroup Has the associative operation of the Monoid.
     * @return a new Monoid
     */
    static <A> Monoid<A> of(A zero, Semigroup<A> semigroup) {
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
}