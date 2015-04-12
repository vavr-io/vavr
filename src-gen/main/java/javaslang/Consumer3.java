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
 * Represents a consumer with three arguments.
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
 * @param <T3> argument 3 of the consumer
 *
 * @since 1.3.0
 */
@FunctionalInterface
public interface Consumer3<T1, T2, T3> extends λ<Void> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Accepts three arguments and returns nothing.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * 
     */
    void accept(T1 t1, T2 t2, T3 t3);

    @Override
    default int arity() {
        return 3;
    }

    @Override
    default Function1<T1, Function1<T2, Consumer1<T3>>> curried() {
        return t1 -> t2 -> t3 -> accept(t1, t2, t3);
    }

    @Override
    default Consumer1<Tuple3<T1, T2, T3>> tupled() {
        return t -> accept(t._1, t._2, t._3);
    }

    @Override
    default Consumer3<T3, T2, T1> reversed() {
        return (t3, t2, t1) -> accept(t1, t2, t3);
    }

    /**
     * Returns a composed function that first applies this Consumer3 to the given arguments and then applies
     * {@code after} to the same arguments.
     *
     * @param after the consumer which accepts the given arguments after this
     * @return a consumer composed of this and after
     * @throws NullPointerException if after is null
     */
    default Consumer3<T1, T2, T3> andThen(Consumer3<T1, T2, T3> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3) -> { accept(t1, t2, t3); after.accept(t1, t2, t3); };
    }

    /**
     * Returns a composed function that first applies {@code before} to the given arguments and then applies
     * this Consumer3 to the same arguments.
     *
     * @param before the consumer which accepts the given arguments before this
     * @return a consumer composed of before and this
     * @throws NullPointerException if before is null
     */
    default Consumer3<T1, T2, T3> compose(Consumer3<T1, T2, T3> before) {
        Objects.requireNonNull(before, "before is null");
        return (t1, t2, t3) -> { before.accept(t1, t2, t3); accept(t1, t2, t3); };
    }
}