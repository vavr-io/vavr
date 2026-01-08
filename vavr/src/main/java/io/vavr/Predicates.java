/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.jspecify.annotations.NonNull;

/**
 * Defines general-purpose predicates which are particularly useful when working with {@link
 * API.Match}.
 *
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public final class Predicates {

  // hidden
  private Predicates() {}

  /**
   * Returns a predicate that is satisfied only when <strong>all</strong> of the provided {@code
   * predicates} return {@code true} for a given input.
   *
   * <p>If no predicates are supplied, the resulting predicate always evaluates to {@code true}.
   *
   * <pre>{@code
   * Predicate<Integer> isGreaterThanOne = i -> i > 1;
   * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
   *
   * allOf().test(0);                                   // true
   * allOf(isGreaterThanOne, isGreaterThanTwo).test(3); // true
   * allOf(isGreaterThanOne, isGreaterThanTwo).test(2); // false
   * }</pre>
   *
   * @param predicates the predicates to combine
   * @param <T> the input type
   * @return a predicate representing the logical conjunction of all given predicates
   * @throws NullPointerException if {@code predicates} is null
   */
  @SuppressWarnings({"varargs"})
  @SafeVarargs
  public static <T> Predicate<T> allOf(@NonNull Predicate<T> @NonNull ... predicates) {
    Objects.requireNonNull(predicates, "predicates is null");
    return t -> List.of(predicates).foldLeft(true, (bool, pred) -> bool && pred.test(t));
  }

  /**
   * Returns a predicate that is satisfied if <strong>at least one</strong> of the provided {@code
   * predicates} returns {@code true} for a given input.
   *
   * <p>If no predicates are supplied, the resulting predicate always evaluates to {@code false}.
   *
   * <pre>{@code
   * Predicate<Integer> isGreaterThanOne = i -> i > 1;
   * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
   *
   * anyOf().test(0);                                   // false
   * anyOf(isGreaterThanOne, isGreaterThanTwo).test(3); // true
   * anyOf(isGreaterThanOne, isGreaterThanTwo).test(2); // true
   * anyOf(isGreaterThanOne, isGreaterThanTwo).test(1); // false
   * }</pre>
   *
   * @param predicates the predicates to combine
   * @param <T> the input type
   * @return a predicate representing the logical disjunction of all given predicates
   * @throws NullPointerException if {@code predicates} is null
   */
  @SuppressWarnings({"varargs"})
  @SafeVarargs
  public static <T> Predicate<T> anyOf(@NonNull Predicate<T> @NonNull ... predicates) {
    Objects.requireNonNull(predicates, "predicates is null");
    return t -> List.of(predicates).find(pred -> pred.test(t)).isDefined();
  }

  /**
   * Returns a predicate that checks if <strong>at least one</strong> element in an {@code Iterable}
   * satisfies the given {@code predicate}.
   *
   * <p>If the {@code Iterable} is empty, the resulting predicate evaluates to {@code false}.
   *
   * <pre>{@code
   * Predicate<Integer> isGreaterThanOne = i -> i > 1;
   * Predicate<Iterable<Integer>> existsGreaterThanOne = exists(isGreaterThanOne);
   *
   * existsGreaterThanOne.test(List.of(0, 1, 2)); // true
   * existsGreaterThanOne.test(List.of(0, 1));    // false
   * existsGreaterThanOne.test(Collections.emptyList()); // false
   * }</pre>
   *
   * @param predicate the predicate to test elements against
   * @param <T> the type of elements in the {@code Iterable}
   * @return a predicate that evaluates to {@code true} if any element satisfies {@code predicate}
   * @throws NullPointerException if {@code predicate} is null
   */
  public static <T> Predicate<Iterable<T>> exists(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return iterable -> Iterator.ofAll(iterable).exists(predicate);
  }

  /**
   * Returns a predicate that checks if <strong>all</strong> elements in an {@code Iterable} satisfy
   * the given {@code predicate}.
   *
   * <p>If the {@code Iterable} is empty, the resulting predicate evaluates to {@code true}.
   *
   * <pre>{@code
   * Predicate<Integer> isGreaterThanOne = i -> i > 1;
   * Predicate<Iterable<Integer>> forAllGreaterThanOne = forAll(isGreaterThanOne);
   *
   * forAllGreaterThanOne.test(List.of(0, 1, 2)); // false
   * forAllGreaterThanOne.test(List.of(2, 3, 4)); // true
   * forAllGreaterThanOne.test(Collections.emptyList()); // true
   * }</pre>
   *
   * @param predicate the predicate to test elements against
   * @param <T> the type of elements in the {@code Iterable}
   * @return a predicate that evaluates to {@code true} only if all elements satisfy {@code
   *     predicate}
   * @throws NullPointerException if {@code predicate} is null
   */
  public static <T> Predicate<Iterable<T>> forAll(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return iterable -> Iterator.ofAll(iterable).forAll(predicate);
  }

  /**
   * Returns a {@code Predicate} that tests whether an object is an instance of the specified {@code
   * type}.
   *
   * <pre>{@code
   * Predicate<Object> instanceOfNumber = instanceOf(Number.class);
   *
   * instanceOfNumber.test(1);    // true
   * instanceOfNumber.test("1");  // false
   * }</pre>
   *
   * @param type the class to test instances against
   * @param <T> the type of objects being tested
   * @return a predicate that evaluates to {@code true} if the object is an instance of {@code type}
   * @throws NullPointerException if {@code type} is null
   */
  // DEV-NOTE: We need Class<? extends T> instead of Class<T>, see {@link
  // TryTest#shouldRecoverSuccessUsingCase()}
  public static <T> Predicate<T> instanceOf(@NonNull Class<? extends T> type) {
    Objects.requireNonNull(type, "type is null");
    return obj -> obj != null && type.isAssignableFrom(obj.getClass());
  }

  /**
   * Returns a {@code Predicate} that tests whether an object is equal to the specified {@code
   * value}, using {@link Objects#equals(Object, Object)} for comparison.
   *
   * <pre>{@code
   * Predicate<Integer> isOne = is(1);
   *
   * isOne.test(1); // true
   * isOne.test(2); // false
   * isOne.test(null); // false
   * }</pre>
   *
   * @param value the value to compare against; may be {@code null}
   * @param <T> the type of object being tested
   * @return a predicate that evaluates to {@code true} if the tested object equals {@code value}
   */
  public static <T> Predicate<T> is(T value) {
    return obj -> Objects.equals(obj, value);
  }

  /**
   * Returns a {@code Predicate} that tests whether an object is equal to at least one of the
   * specified {@code values}, using {@link Objects#equals(Object, Object)} for comparison.
   *
   * <pre>{@code
   * Predicate<Integer> isIn = isIn(1, 2, 3);
   *
   * isIn.test(1); // true
   * isIn.test(0); // false
   * isIn.test(null); // false
   * }</pre>
   *
   * @param values the values to compare against; may not be {@code null}
   * @param <T> the type of objects being tested
   * @return a predicate that evaluates to {@code true} if the tested object equals any of the
   *     specified {@code values}
   * @throws NullPointerException if {@code values} is null
   */
  @SuppressWarnings({"varargs"})
  @SafeVarargs
  public static <T> Predicate<T> isIn(T @NonNull ... values) {
    Objects.requireNonNull(values, "values is null");
    return obj -> List.of(values).find(value -> Objects.equals(value, obj)).isDefined();
  }

  /**
   * Returns a {@code Predicate} that tests whether an object is not {@code null}.
   *
   * <pre>{@code
   * Predicate<Integer> isNotNull = isNotNull();
   *
   * isNotNull.test(0);    // true
   * isNotNull.test(null); // false
   * }</pre>
   *
   * @param <T> the type of object being tested
   * @return a predicate that evaluates to {@code true} if the tested object is not {@code null}
   */
  public static <T> Predicate<T> isNotNull() {
    return Objects::nonNull;
  }

  /**
   * Returns a {@code Predicate} that tests whether an object is {@code null}.
   *
   * <pre>{@code
   * Predicate<Integer> isNull = isNull();
   *
   * isNull.test(null); // true
   * isNull.test(0);    // false
   * }</pre>
   *
   * @param <T> the type of object being tested
   * @return a predicate that evaluates to {@code true} if the tested object is {@code null}
   */
  public static <T> Predicate<T> isNull() {
    return Objects::isNull;
  }

  /**
   * Returns a predicate that is satisfied if <strong>none</strong> of the provided {@code
   * predicates} return {@code true} for a given input.
   *
   * <p>If no predicates are supplied, the resulting predicate always evaluates to {@code true}.
   *
   * <pre>{@code
   * Predicate<Integer> isGreaterThanOne = i -> i > 1;
   * Predicate<Integer> isGreaterThanTwo = i -> i > 2;
   *
   * noneOf().test(0);                                   // true
   * noneOf(isGreaterThanOne, isGreaterThanTwo).test(1); // true
   * noneOf(isGreaterThanOne, isGreaterThanTwo).test(2); // false
   * }</pre>
   *
   * @param predicates the predicates to combine
   * @param <T> the input type
   * @return a predicate that evaluates to {@code true} only if all predicates return {@code false}
   * @throws NullPointerException if {@code predicates} is null
   */
  @SuppressWarnings({"varargs"})
  @SafeVarargs
  public static <T> Predicate<T> noneOf(@NonNull Predicate<T> @NonNull ... predicates) {
    Objects.requireNonNull(predicates, "predicates is null");
    return anyOf(predicates).negate();
  }

  /**
   * Returns a predicate that negates the result of the given {@code predicate}.
   *
   * <pre>{@code
   * // Negates a method reference
   * Predicate<String> isNotNull1 = not(Objects::isNull);
   * isNotNull1.test("");   // true
   * isNotNull1.test(null); // false
   *
   * // Negates a predicate instance
   * Predicate<String> isNotNull2 = not(Predicates.isNull());
   * isNotNull2.test("");   // true
   * isNotNull2.test(null); // false
   * }</pre>
   *
   * @param predicate the predicate to negate
   * @param <T> the type of objects being tested
   * @return a predicate that evaluates to {@code true} if the original predicate evaluates to
   *     {@code false}, and vice versa
   * @throws NullPointerException if {@code predicate} is null
   */
  @SuppressWarnings("unchecked")
  public static <T> Predicate<T> not(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return (Predicate<T>) predicate.negate();
  }
}
