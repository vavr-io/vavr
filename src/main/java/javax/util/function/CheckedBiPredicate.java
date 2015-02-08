/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction2;

import java.util.Objects;

/**
 * Checked version of java.util.function.BiPredicate.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U> extends CheckedFunction2<T, U, Boolean> {

    static final long serialVersionUID = 1L;

    boolean test(T t, U u) throws Throwable;

    @Override
    default Boolean apply(T t, U u) throws Throwable {
        return test(t, u);
    }

    default CheckedBiPredicate<T, U> and(CheckedBiPredicate<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> test(t, u) && other.test(t, u);
    }

    default CheckedBiPredicate<T, U> negate() {
        return (T t, U u) -> !test(t, u);
    }

    default CheckedBiPredicate<T, U> or(CheckedBiPredicate<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> test(t, u) || other.test(t, u);
    }
}
