/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;

/**
 * <p>Implementation of an empty tuple, a tuple containing no elements.</p>
 * <p>
 * Because the empty tuple is a singleton, there is no accessible constructor.
 * Please use {@linkplain Tuple0#instance()} or {@linkplain Tuple#empty()} to obtain the single instance.
 * </p>
 *
 * @since 1.1.0
 */
public final class Tuple0 implements Tuple, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of Tuple0.
     */
    private static final Tuple0 INSTANCE = new Tuple0();

    // hidden constructor, internally called
    private Tuple0() {
    }

    /**
     * Returns the singleton instance of Tuple0.
     *
     * @return The singleton instance of Tuple0.
     */
    public static Tuple0 instance() {
        return INSTANCE;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "()";
    }

    // -- Serializable implementation

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Tuple0.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }
}