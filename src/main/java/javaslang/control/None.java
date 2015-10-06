/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * None is a singleton representation of the undefined {@link javaslang.control.Option}. The instance is obtained by
 * calling {@link #instance()}.
 *
 * @param <T> The type of the optional value.
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public final class None<T> implements Option<T>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of None.
     */
    private static final None<?> INSTANCE = new None<>();

    /**
     * Hidden constructor.
     */
    private None() {
    }

    /**
     * Returns the singleton instance of None as {@code None<T>} in the context of a type {@code <T>}, e.g.
     * <pre>
     * <code>final Option&lt;Integer&gt; o = None.instance(); // o is of type None&lt;Integer&gt;</code>
     * </pre>
     *
     * @param <T> The type of the optional value.
     * @return None
     */
    public static <T> None<T> instance() {
        @SuppressWarnings("unchecked")
        final None<T> none = (None<T>) INSTANCE;
        return none;
    }

    @Override
    public T get() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public boolean isEmpty() {
        return true;
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
        return "None";
    }

    // -- Serializable implementation

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of None.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
