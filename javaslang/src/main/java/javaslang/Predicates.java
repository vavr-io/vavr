/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Defines a set of predicates which are particularly useful when working with {@link javaslang.API.Match}.
 *
 * <pre><code>import static javaslang.API.*;
 *
 * </code></pre>
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public final class Predicates {

    // hidden
    private Predicates() {
    }

    /**
     * Returns a {@code Predicate} that tests, if an object is instance of the specified {@code type}.
     *
     * @param type A type
     * @param <T>  Type of the given {@code type}
     * @return A new {@code Predicate}
     */
    // DEV-NOTE: The Class<? extends T> is needed, e.g. for PredicatesTest.shouldUsePredicateInCaseWithSuccess
    public static <T> Predicate<? super T> instanceOf(Class<? extends T> type) {
        Objects.requireNonNull(type, "type is null");
        return obj -> obj != null && type.isAssignableFrom(obj.getClass());
    }

    // -- Predicate combinators

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<? super T> allOf(Predicate<? super T>... predicates) {
        return t -> List.of(predicates).foldLeft(true, (bool, pred) -> bool && pred.test(t));
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<? super T> anyOf(Predicate<? super T>... predicates) {
        return t -> List.of(predicates).find(pred -> pred.test(t)).isDefined();
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<? super T> noneOf(Predicate<? super T>... predicates) {
        return allOf(predicates).negate();
    }
}
