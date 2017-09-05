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
 * Defines general-purpose predicates which are particularly useful when working with {@link API.Match}.
 *
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public final class Predicates {

    // hidden
    private Predicates() {
    }

    /**
     * A combinator that checks if <strong>all</strong> of the given {@code predicates} are satisfied.
     * <p>
     * By definition {@code allOf} is satisfied if the given {@code predicates} are empty.
     *
     * <pre>{@code
     * Predicate<Integer> isGreaterThanOne = i -> i > 1;
     * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
     * allOf().test(0);                                   // true
     * allOf(isGreaterThanOne, isGreaterThanTwo).test(3); // true
     * allOf(isGreaterThanOne, isGreaterThanTwo).test(2); // false
     * }</pre>
     *
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicates} is null
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> allOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return t -> List.of(predicates).foldLeft(true, (bool, pred) -> bool && pred.test(t));
    }

    /**
     * A combinator that checks if <strong>at least one</strong> of the given {@code predicates} is satisfies.
     *
     * <pre>{@code
     * Predicate<Integer> isGreaterThanOne = i -> i > 1;
     * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
     * anyOf().test(0);                                   // false
     * anyOf(isGreaterThanOne, isGreaterThanTwo).test(3); // true
     * anyOf(isGreaterThanOne, isGreaterThanTwo).test(2); // true
     * anyOf(isGreaterThanOne, isGreaterThanTwo).test(1); // false
     * }</pre>
     * 
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicates} is null
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> anyOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return t -> List.of(predicates).find(pred -> pred.test(t)).isDefined();
    }

    /**
     * A combinator that checks if <strong>one or more</strong> elements of an {@code Iterable} satisfy the {@code predicate}.
     *
     * <pre>{@code
     * Predicate<Integer> isGreaterThanOne = i -> i > 1;
     * Predicate<Iterable<Integer>> existsGreaterThanOne = exists(isGreaterThanOne);
     * existsGreaterThanOne.test(List.of(0, 1, 2)); // true
     * existsGreaterThanOne.test(List.of(0, 1));    // false
     * }</pre>
     * 
     * @param predicate A {@code Predicate} that tests elements of type {@code T}
     * @param <T>        tested object type
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicate} is null
     */
    public static <T> Predicate<Iterable<T>> exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterable -> Iterator.ofAll(iterable).exists(predicate);
    }

    /**
     * A combinator that checks if <strong>all</strong> elements of an {@code Iterable} satisfy the {@code predicate}.
     *
     * <pre>{@code
     * Predicate<Integer> isGreaterThanOne = i -> i > 1;
     * Predicate<Iterable<Integer>> forAllGreaterThanOne = forAll(isGreaterThanOne);
     * forAllGreaterThanOne.test(List.of(0, 1, 2)); // false
     * forAllGreaterThanOne.test(List.of(2, 3, 4)); // true
     * }</pre>
     *
     * @param predicate A {@code Predicate} that tests elements of type {@code T}
     * @param <T>        tested object type
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicate} is null
     */
    public static <T> Predicate<Iterable<T>> forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterable -> Iterator.ofAll(iterable).forAll(predicate);
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is instance of the specified {@code type}.
     *
     * <pre>{@code
     * Predicate<Object> instanceOfNumber = instanceOf(Number.class);
     * instanceOfNumber.test(1);    // true
     * instanceOfNumber.test("1");  // false
     * }</pre>
     *
     * @param type A type
     * @param <T>  tested object type
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
     *
     * <pre>{@code
     * Predicate<Integer> isOne = is(1);
     * isOne.test(1); // true
     * isOne.test(2); // false
     * }</pre>
     *
     * @param value A value, may be null
     * @param <T>   tested object type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> is(T value) {
        return obj -> Objects.equals(obj, value);
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is equal to at least one of the specified {@code values}
     * using {@link Objects#equals(Object, Object)} for comparison.
     *
     * <pre>{@code
     * Predicate<Integer> isIn = isIn(1, 2, 3);
     * isIn.test(1); // true
     * isIn.test(0); // false
     * }</pre>
     *
     * @param values an array of values of type T
     * @param <T>    closure over tested object types
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code values} is null
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> isIn(T... values) {
        Objects.requireNonNull(values, "values is null");
        return obj -> List.of(values).find(value -> Objects.equals(value, obj)).isDefined();
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is not null
     *
     * <pre>{@code
     * Predicate<Integer> isNotNull = isNotNull();
     * isNotNull.test(0);    // true
     * isNotNull.test(null); // false
     * }</pre>
     *
     * @param <T> tested object type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> isNotNull() {
        return Objects::nonNull;
    }

    /**
     * Creates a {@code Predicate} that tests, if an object is null
     *
     * <pre>{@code
     * Predicate<Integer> isNull = isNull();
     * isNull.test(null); // true
     * isNull.test(0);    // false
     * }</pre>
     *
     * @param <T> tested object type
     * @return A new {@code Predicate}
     */
    public static <T> Predicate<T> isNull() {
        return Objects::isNull;
    }

    /**
     * A combinator that checks if <strong>none</strong> of the given {@code predicates} is satisfied.
     * <p>
     * Naturally {@code noneOf} is satisfied if the given {@code predicates} are empty.
     *
     * <pre>{@code
     * Predicate<Integer> isGreaterThanOne = i -> i > 1;
     * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
     * noneOf().test(0);                                   // true
     * noneOf(isGreaterThanOne, isGreaterThanTwo).test(1); // true
     * noneOf(isGreaterThanOne, isGreaterThanTwo).test(2); // false
     * }</pre>
     *
     * @param predicates An array of predicates
     * @param <T>        closure over tested object types
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicates} is null
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <T> Predicate<T> noneOf(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates, "predicates is null");
        return anyOf(predicates).negate();
    }

    /**
     * Negates a given {@code Predicate}.
     *
     * <pre>{@code
     * // negates a method reference
     * Predicate<String> isNotNull1 = not(Objects::isNull);
     * isNotNull1.test("");   // true
     * isNotNull1.test(null); // false
     *
     * // negates a predicate instance
     * Predicate<String> isNotNull2 = not(Predicates.isNull());
     * isNotNull2.test("");   // true
     * isNotNull2.test(null); // false
     * }</pre>
     *
     * @param predicate A {@code Predicate} that tests elements of type {@code T}
     * @param <T>       tested object type
     * @return A new {@code Predicate}
     * @throws NullPointerException if {@code predicate} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> not(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (Predicate<T>) predicate.negate();
    }

}
