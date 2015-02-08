/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.Predicate.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedPredicate<T> extends CheckedFunction1<T, Boolean> {

    static final long serialVersionUID = 1L;

    static <T> CheckedPredicate<T> isEqual(Object targetRef) {
        return (targetRef == null)
                ? Objects::isNull
                : targetRef::equals;
    }

    boolean test(T t) throws Throwable;

    @Override
    default Boolean apply(T t) throws Throwable {
        return test(t);
    }

    default CheckedPredicate<T> and(CheckedPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default CheckedPredicate<T> negate() {
        return (t) -> !test(t);
    }

    default CheckedPredicate<T> or(CheckedPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }
}
