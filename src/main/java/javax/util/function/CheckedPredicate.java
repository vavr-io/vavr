/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.Predicate.
 * Essentially the same as {@code CheckedFunction1<T, Boolean>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedPredicate<T> extends Serializable {

    static final long serialVersionUID = 1L;

    static <T> CheckedPredicate<T> isEqual(Object targetRef) {
        return (targetRef == null)
                ? Objects::isNull
                : targetRef::equals;
    }

    boolean test(T t) throws Throwable;

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
