/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/**
 * <p>A Semigroup is a type with an associative binary operation {@linkplain #combine(Object, Object)}.</p>
 * <p>Given a type {@code A}, instances of Semigroup should satisfy the following law:</p>
 * <ul>
 * <li>Associativity: {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any {@code x,y,z} of type
 * {@code A}.</li>
 * </ul>
 * <p>Note: Technically a Semigroup is the same as a {@code java.util.function.BiFunction<A,A,A>}. Introducing this new type
 * clarifies that the operation {@code combine} is associative.</p>
 *
 * @param <A> A type.
 * @author Daniel Dietrich
 * @since 1.1.0
 */
@FunctionalInterface
public interface Semigroup<A> {

    /**
     * Combines two elements of the same type, which is also returned.
     *
     * @param a1 An element
     * @param a2 Another element
     * @return The combination of a1 and a2
     */
    A combine(A a1, A a2);
}
