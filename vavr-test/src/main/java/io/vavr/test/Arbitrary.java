/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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
package io.vavr.test;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents an arbitrary object of type T.
 *
 * @param <T> The type of the arbitrary object.
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface Arbitrary<T> {

    /**
     * Returns a generator for objects of type T.
     * Use {@link Gen#map(Function)} and {@link Gen#flatMap(Function)} to
     * combine object generators.
     * <p>
     * Example:
     * <pre>
     * <code>
     * // represents arbitrary binary trees of a certain depth n
     * final class ArbitraryTree implements Arbitrary&lt;BinaryTree&lt;Integer&gt;&gt; {
     *     &#64;Override
     *     public Gen&lt;BinaryTree&lt;Integer&gt;&gt; apply(int n) {
     *         return Gen.choose(-1000, 1000).flatMap(value -&gt; {
     *                  if (n == 0) {
     *                      return Gen.of(BinaryTree.leaf(value));
     *                  } else {
     *                      return Gen.frequency(
     *                              Tuple.of(1, Gen.of(BinaryTree.leaf(value))),
     *                              Tuple.of(4, Gen.of(BinaryTree.branch(apply(n / 2).get(), value, apply(n / 2).get())))
     *                      );
     *                  }
     *         });
     *     }
     * }
     *
     * // tree generator with a size hint of 10
     * final Gen&lt;BinaryTree&lt;Integer&gt;&gt; treeGen = new ArbitraryTree().apply(10);
     *
     * // stream sum of tree node values to console for 100 arbitrary trees
     * Stream.of(() -&gt; treeGen.apply(RNG.get())).map(Tree::sum).take(100).stdout();
     * </code>
     * </pre>
     *
     * @param size A (not necessarily positive) size parameter which may be interpreted individually and is constant for all arbitrary objects regarding one property check.
     * @return A generator for objects of type T.
     */
    Gen<T> apply(int size);

    /**
     * Returns an Arbitrary based on this Arbitrary which produces unique values.
     *
     * @return A new generator
     */
    default Arbitrary<T> distinct() {
        return distinctBy(Function.identity());
    }

    /**
     * Returns an Arbitrary based on this Arbitrary which produces unique values based on the given comparator.
     *
     * @param comparator A comparator
     * @return A new generator
     */
    default Arbitrary<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    /**
     * Returns an Arbitrary based on this Arbitrary which produces unique values based on the given function.
     *
     * @param <U>          key type
     * @param keyExtractor A function
     * @return A new generator
     */
    default <U> Arbitrary<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    /**
     * Returns an Arbitrary based on this Arbitrary which produces values that fulfill the given predicate.
     *
     * @param predicate A predicate
     * @return A new generator
     */
    default Arbitrary<T> filter(Predicate<? super T> predicate) {
        return size -> apply(size).filter(predicate);
    }

    /**
     * Maps arbitrary objects T to arbitrary object U.
     *
     * @param mapper A function that maps arbitrary Ts to arbitrary Us given a mapper.
     * @param <U>    New type of arbitrary objects
     * @return A new Arbitrary
     */
    default <U> Arbitrary<U> flatMap(Function<? super T, ? extends Arbitrary<? extends U>> mapper) {
        return size -> {
            final Gen<T> gen = apply(size);
            return random -> mapper.apply(gen.apply(random)).apply(size).apply(random);
        };
    }

    /**
     * Intersperses values from this arbitrary instance with those of another.
     *
     * @param other another T arbitrary to accept values from.
     * @return A new T arbitrary
     */
    default Arbitrary<T> intersperse(Arbitrary<T> other) {
        Objects.requireNonNull(other, "other is null");
        return size -> this.apply(size).intersperse(other.apply(size));
    }

    /**
     * Maps arbitrary objects T to arbitrary object U.
     *
     * @param mapper A function that maps an arbitrary T to an object of type U.
     * @param <U>    Type of the mapped object
     * @return A new generator
     */
    default <U> Arbitrary<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return n -> {
            final Gen<T> generator = apply(n);
            return random -> mapper.apply(generator.apply(random));
        };
    }

    default Arbitrary<T> peek(Consumer<? super T> action) {
        return size -> apply(size).peek(action);
    }

    /**
     * Transforms this {@code Arbitrary}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Arbitrary<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Generates an arbitrary value from a fixed set of values
     *
     * @param values A fixed set of values
     * @param <U>    Type of generator value
     * @return A new generator
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    static <U> Arbitrary<U> of(U... values) {
        return ofAll(Gen.choose(values));
    }

    /**
     * Generates an arbitrary value from a given generator
     *
     * @param generator A generator to produce arbitrary values
     * @param <U>       Type of generator value
     * @return A new generator
     */
    static <U> Arbitrary<U> ofAll(Gen<U> generator) {
        return size -> generator;
    }

    /**
     * Generates arbitrary integer values.
     *
     * @return A new Arbitrary of Integer
     */
    static Arbitrary<Integer> integer() {
        return size -> Gen.choose(-size, size);
    }

    /**
     * Generates arbitrary {@link LocalDateTime}s with {@link LocalDateTime#now()} as {@code median} and
     * {@link ChronoUnit#DAYS} as chronological unit.
     *
     * @return A new Arbitrary of LocalDateTime
     * @see #localDateTime(LocalDateTime, ChronoUnit)
     */
    static Arbitrary<LocalDateTime> localDateTime() {
        return localDateTime(ChronoUnit.DAYS);
    }

    /**
     * Generates arbitrary {@link LocalDateTime}s with {@link LocalDateTime#now()} as {@code median}.
     *
     * @param unit Chronological unit of {@code size}
     * @return A new Arbitrary of LocalDateTime
     * @see #localDateTime(LocalDateTime, ChronoUnit)
     */
    static Arbitrary<LocalDateTime> localDateTime(ChronoUnit unit) {
        return localDateTime(LocalDateTime.now(), unit);
    }

    /**
     * Generates arbitrary {@link LocalDateTime}s. All generated values are drawn from a range with {@code median}
     * as center and {@code median +/- size} as included boundaries. {@code unit} defines the chronological unit
     * of {@code size}.
     *
     * <p>
     * Example:
     * <pre>
     * <code>
     * Arbitrary.localDateTime(LocalDateTime.now(), ChronoUnit.YEARS);
     * </code>
     * </pre>
     *
     * @param median Center of the LocalDateTime range
     * @param unit   Chronological unit of {@code size}
     * @return  A new Arbitrary of LocalDateTime
     */
    static Arbitrary<LocalDateTime> localDateTime(LocalDateTime median, ChronoUnit unit) {
        Objects.requireNonNull(median, "median is null");
        Objects.requireNonNull(unit, "unit is null");
        return size -> {
            if(size == 0) {
                return Gen.of(median);
            }
            final LocalDateTime start = median.minus(size, unit);
            final LocalDateTime end = median.plus(size, unit);
            final long duration = Duration.between(start, end).toMillis();
            final Gen<Long> from = Gen.choose(0, duration);
            return random -> start.plus(from.apply(random), ChronoUnit.MILLIS);
        };
    }

    /**
     * Generates arbitrary strings based on a given alphabet represented by <em>gen</em>.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Arbitrary.string(
     *     Gen.frequency(
     *         Tuple.of(1, Gen.choose('A', 'Z')),
     *         Tuple.of(1, Gen.choose('a', 'z')),
     *         Tuple.of(1, Gen.choose('0', '9'))));
     * </code>
     * </pre>
     *
     * @param gen A character generator
     * @return a new Arbitrary of String
     */
    static Arbitrary<String> string(Gen<Character> gen) {
        return size -> random -> Gen.choose(0, size).map(i -> {
            final char[] chars = new char[i];
            for (int j = 0; j < i; j++) {
                chars[j] = gen.apply(random);
            }
            return new String(chars);
        }).apply(random);
    }

    /**
     * Generates arbitrary lists based on a given element generator arbitraryT.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Arbitrary.list(Arbitrary.integer());
     * </code>
     * </pre>
     *
     * @param arbitraryT Arbitrary elements of type T
     * @param <T>        Component type of the List
     * @return a new Arbitrary of List&lt;T&gt;
     */
    static <T> Arbitrary<List<T>> list(Arbitrary<T> arbitraryT) {
        return size -> {
            final Gen<T> genT = arbitraryT.apply(size);
            return random -> List.fill(Gen.choose(0, size).apply(random), () -> genT.apply(random));
        };
    }

    /**
     * Generates arbitrary streams based on a given element generator arbitraryT.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Arbitrary.stream(Arbitrary.integer());
     * </code>
     * </pre>
     *
     * @param arbitraryT Arbitrary elements of type T
     * @param <T>        Component type of the Stream
     * @return a new Arbitrary of Stream&lt;T&gt;
     */
    static <T> Arbitrary<Stream<T>> stream(Arbitrary<T> arbitraryT) {
        return size -> {
            final Gen<T> genT = arbitraryT.apply(size);
            return random -> Stream.continually(() -> genT.apply(random))
                    .take(Gen.choose(0, size).apply(random));
        };
    }
}
