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
 * Represents a consumer with two arguments.
 * <p>
 * A consumer is a special function that returns nothing, i.e. {@code Void}. This implies that the consumer
 * performs side-effects based on its arguments.
 * <p>
 * Java requires a function of return type {@code Void} to explicitely return null (the only instance
 * of Void). To circumvent this and in favor of a more consise notation of consumers, Javaslang provides the
 * funtional interfaces {@linkplain Consumer0} to {@linkplain Consumer26}.
 *
 * @param <T1> argument 1 of the consumer
 * @param <T2> argument 2 of the consumer
 *
 * @since 1.3.0
 */
@FunctionalInterface
public interface CheckedConsumer2<T1, T2> extends λ<Void> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Accepts two arguments and returns nothing.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @throws Throwable if something goes wrong accepting the given arguments
     */
    void accept(T1 t1, T2 t2) throws Throwable;

    @Override
    default int arity() {
        return 2;
    }

    @Override
    default CheckedFunction1<T1, CheckedConsumer1<T2>> curried() {
        return t1 -> t2 -> accept(t1, t2);
    }

    @Override
    default CheckedConsumer1<Tuple2<T1, T2>> tupled() {
        return t -> accept(t._1, t._2);
    }

    @Override
    default CheckedConsumer2<T2, T1> reversed() {
        return (t2, t1) -> accept(t1, t2);
    }

    /**
     * Returns a composed function that first applies this CheckedConsumer2 to the given arguments and then applies
     * {@code after} to the same arguments.
     *
     * @param after the consumer which accepts the given arguments after this
     * @return a consumer composed of this and after
     * @throws NullPointerException if after is null
     */
    default CheckedConsumer2<T1, T2> andThen(CheckedConsumer2<T1, T2> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2) -> { accept(t1, t2); after.accept(t1, t2); };
    }

    /**
     * Returns a composed function that first applies {@code before} to the given arguments and then applies
     * this CheckedConsumer2 to the same arguments.
     *
     * @param before the consumer which accepts the given arguments before this
     * @return a consumer composed of before and this
     * @throws NullPointerException if before is null
     */
    default CheckedConsumer2<T1, T2> compose(CheckedConsumer2<T1, T2> before) {
        Objects.requireNonNull(before, "before is null");
        return (t1, t2) -> { before.accept(t1, t2); accept(t1, t2); };
    }
}