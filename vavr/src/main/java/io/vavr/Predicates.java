/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr;

import io.vavr.collection.Iterator;
import io.vavr.collection.List;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Defines general-purpose predicates which are particularly useful when working with
 * {@link API.Match}.
 *
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public final class Predicates {

    // hidden
    private Predicates() {
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is instance of the specified {@code type}.
     *
     * @param type A type
     * @param <T>  Type of the given {@code type}
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code type} is null
     */
    // DEV-NOTE: We need Class<? extends T> instead of Class<T>, see {@link TryTest#shouldRecoverSuccessUsingCase()}
    @GwtIncompatible
    public static <T> Predicate<T> instanceOf(Class<? extends T> type) {
        Objects.requireNonNull(type, "type is null");
        return obj -> obj != null && type.isAssignableFrom(obj.getClass());
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is equal to the specified {@code value} using
     * {@link Objects#equals(Object, Object)} for comparison.
     * <p>
     * Hint: Use {@code is(null)} instead of introducing a new predicate {@code isNull()}
     *
     * @param value A value, may be null
     * @param <T>   value type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> is(T value) {
        return obj -> Objects.equals(obj, value);
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is equal to at least one of the specified {@code values}
     * using {@link Objects#equals(Object, Object)} for comparison.
     *
     * @param values an array of values of type T
     * @param <T>    value type
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code values} is null
     */
    // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> isIn(T... values) {
        Objects.requireNonNull(values, "values is null");
        return obj -> List.of(values).find(value -> Objects.equals(value, obj)).isDefined();
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is null
     *
     * @param <T> value type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> isNull() {
        return Objects::isNull;
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is not null
     *
     * @param <T> value type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> isNotNull() {
        return Objects::nonNull;
    }

    // -- Predicate combinators

    /**
     * A combinator that checks if <strong>all</strong> of the given {@code predicates} are satisfied.
     * <p>
     * By definition {@code allOf} is satisfied if the given {@code predicates} are empty.
     *
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     */
    // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> allOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return t -> List.of(predicates).foldLeft(true, (bool, pred) -> bool && pred.test(t));
    }

    /**
     * A combinator that checks if <strong>at least one</strong> of the given {@code predicates} is satisfies.
     *
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     */
    // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> anyOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return t -> List.of(predicates).find(pred -> pred.test(t)).isDefined();
    }

    /**
     * A combinator that checks if <strong>none</strong> of the given {@code predicates} is satisfied.
     * <p>
     * Naturally {@code noneOf} is satisfied if the given {@code predicates} are empty.
     *
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     */
    // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> noneOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return anyOf(predicates).negate();
    }

    /**
     * A combinator that checks if <strong>one or more</strong> elements of an {@code Iterable} satisfy the {@code predicate}.
     *
     * @param predicate A {@code Predicate} that tests elements of type {@code T}
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<Iterable<T>> exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterable -> Iterator.ofAll(iterable).exists(predicate);
    }

    /**
     * A combinator that checks if <strong>all</strong> elements of an {@code Iterable} satisfy the {@code predicate}.
     *
     * @param predicate A {@code Predicate} that tests elements of type {@code T}
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<Iterable<T>> forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterable -> Iterator.ofAll(iterable).forAll(predicate);
    }
}
