/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/**
 * Represents an arbitrary object of type T.
 *
 * @param <T> The type of the arbitrary object.
 */
@FunctionalInterface
public interface Arbitrary<T> {

    /**
     * Use {@link Gen} to generate arbitrary objects.
     *
     * @param size A size parameter which may be interpreted idividually and is constant for all arbitrary objects regarding one property check.
     * @return A generator for objects of type T.
     */
    Gen<T> arbitrary(int size);

}
