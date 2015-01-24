/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/**
 * A Semigroup is an algebraic structure consisting of
 *
 * <ul>
 *     <li>Some type A</li>
 *     <li>An associative binary operation {@code combine}, such that {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any x,y,z of type A.</li>
 * </ul>
 *
 * Technically a Semigroup is the same as a {@code java.util.function.BiFunction<A,A,A>}. Introducing this new type
 * clarifies that the operation {@code combine} is associative.
 *
 * @param <A> A type.
 */
@FunctionalInterface
public interface Semigroup<A> {

    A combine(A a1, A a2);
}
