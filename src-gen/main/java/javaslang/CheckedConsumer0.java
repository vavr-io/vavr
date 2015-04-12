/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;

/**
 * Represents a consumer with no arguments.
 * <p>
 * A consumer is a special function that returns nothing, i.e. {@code Void}. This implies that the consumer
 * performs side-effects based on its arguments.
 * <p>
 * Java requires a function of return type {@code Void} to explicitely return null (the only instance
 * of Void). To circumvent this and in favor of a more consise notation of consumers, Javaslang provides the
 * funtional interfaces {@linkplain Consumer0} to {@linkplain Consumer26}.
 *
 *
 * @since 1.3.0
 */
@FunctionalInterface
public interface CheckedConsumer0 extends λ<Void> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Accepts no arguments and returns nothing.
     *
     * @throws Throwable if something goes wrong accepting the given arguments
     */
    void accept() throws Throwable;

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default CheckedConsumer0 curried() {
        return this;
    }

    @Override
    default CheckedConsumer1<Tuple0> tupled() {
        return t -> accept();
    }

    @Override
    default CheckedConsumer0 reversed() {
        return this;
    }

    /**
     * Returns a composed function that first applies this CheckedConsumer0 to the given arguments and then applies
     * {@code after} to the same arguments.
     *
     * @param after the consumer which accepts the given arguments after this
     * @return a consumer composed of this and after
     * @throws NullPointerException if after is null
     */
    default CheckedConsumer0 andThen(CheckedConsumer0 after) {
        Objects.requireNonNull(after, "after is null");
        return () -> { accept(); after.accept(); };
    }

    /**
     * Returns a composed function that first applies {@code before} to the given arguments and then applies
     * this CheckedConsumer0 to the same arguments.
     *
     * @param before the consumer which accepts the given arguments before this
     * @return a consumer composed of before and this
     * @throws NullPointerException if before is null
     */
    default CheckedConsumer0 compose(CheckedConsumer0 before) {
        Objects.requireNonNull(before, "before is null");
        return () -> { before.accept(); accept(); };
    }
}