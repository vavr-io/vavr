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

import java.util.function.Predicate;
import org.jspecify.annotations.NonNull;

import static io.vavr.CheckedPredicateModule.sneakyThrow;

/**
 * A {@linkplain java.util.function.Predicate} that is allowed to throw checked exceptions.
 *
 * @param <T> the type of the input to the predicate
 */
@FunctionalInterface
public interface CheckedPredicate<T> {

  /**
   * Creates a {@code CheckedPredicate} from the given method reference or lambda.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * final CheckedPredicate<Boolean> checkedPredicate = CheckedPredicate.of(Boolean::booleanValue);
   * final Predicate<Boolean> predicate = checkedPredicate.unchecked();
   *
   * // returns true
   * predicate.test(Boolean.TRUE);
   *
   * // may throw an exception
   * predicate.test(null);
   * }</pre>
   *
   * @param methodReference typically a method reference, e.g. {@code Type::method}
   * @param <T> the type of values tested by the predicate
   * @return a new {@code CheckedPredicate} wrapping the given method reference
   * @see CheckedFunction1#of(CheckedFunction1)
   */
  static <T> CheckedPredicate<T> of(@NonNull CheckedPredicate<T> methodReference) {
    return methodReference;
  }

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t the input argument
   * @return {@code true} if the argument satisfies the predicate, {@code false} otherwise
   * @throws Throwable if an error occurs during evaluation
   */
  boolean test(T t) throws Throwable;

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a new {@code CheckedPredicate} representing the negated condition
   */
  default CheckedPredicate<T> negate() {
    return t -> !test(t);
  }

  /**
   * Returns an unchecked {@link Predicate} that <em>sneakily throws</em> any exception encountered
   * when testing a value.
   *
   * @return a {@link Predicate} that may throw any {@link Throwable} without declaring it
   */
  default Predicate<T> unchecked() {
    return t -> {
      try {
        return test(t);
      } catch (Throwable x) {
        return sneakyThrow(x);
      }
    };
  }
}

interface CheckedPredicateModule {

  // DEV-NOTE: we do not plan to expose this as public API
  @SuppressWarnings("unchecked")
  static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
    throw (T) t;
  }
}
