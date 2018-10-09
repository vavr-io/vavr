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

import io.vavr.Tuple2;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Generators are the building blocks for providing arbitrary objects.
 * <p>
 * To ease the creation of Arbitraries, Gen is a FunctionalInterface which extends {@code Function<Random, T>}.
 * <p>
 * Gen objects are obtained via one of the methods {@code choose}, {@code fail}, {@code frequency}, {@code of} and
 * {@code oneOf}.
 * <p>
 * Given Gen objects may be transformed using one of the methods {@code filter}, {@code map} and {@code flatMap}.
 * <p>
 * A simple way to obtain an Arbitrary of a Gen is to call {@linkplain Gen#arbitrary()}.
 * This will ignore the size hint of Arbitrary.
 *
 * @param <T> type of generated objects
 * @author Daniel Dietrich
 * @see Arbitrary
 */
@FunctionalInterface
public interface Gen<T> {

    /**
     * Functional interface of this generator.
     *
     * @param random a random number generator
     * @return A generated value of type T.
     */
    T apply(Random random);

    /**
     * A generator which constantly returns t.
     *
     * @param t   A value.
     * @param <T> Type of t.
     * @return A new T generator
     */
    static <T> Gen<T> of(T t) {
        return ignored -> t;
    }

    static <T> Gen<T> of(T seed, Function<? super T, ? extends T> next) {
        Objects.requireNonNull(next, "next is null");
        final Iterator<T> iterator = Iterator.iterate(seed, next);
        return ignored -> iterator.next();
    }

    /**
     * Chooses an int between min and max, bounds inclusive and numbers distributed according to the distribution of
     * the underlying random number generator.
     * <p>
     * Note: min and max are internally swapped if min &gt; max.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new int generator
     */
    static Gen<Integer> choose(int min, int max) {
        if (min == max) {
            return ignored -> min;
        } else {
            final int _min = Math.min(min, max);
            final int _max = Math.max(min, max);
            return rng -> rng.nextInt(Math.abs(_max - _min) + 1) + _min;
        }
    }

    /**
     * Chooses a long between min and max, bounds inclusive and numbers distributed according to the distribution of
     * the underlying random number generator.
     * <p>
     * Note: min and max are internally swapped if min &gt; max.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new long generator
     */
    static Gen<Long> choose(long min, long max) {
        if (min == max) {
            return ignored -> min;
        } else {
            return random -> {
                final double d = random.nextDouble();
                final long _min = Math.min(min, max);
                final long _max = Math.max(min, max);
                return (long) ((d * _max) + ((1.0 - d) * _min) + d);
            };
        }
    }

    /**
     * Chooses a double between min and max, bounds inclusive and numbers distributed according to the distribution
     * of the underlying random number generator.
     * <p>
     * Note: min and max are internally swapped if min &gt; max.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new double generator
     * @throws IllegalArgumentException if min or max is infinite, min or max is not a number (NaN)
     */
    static Gen<Double> choose(double min, double max) {
        if (Double.isInfinite(min)) {
            throw new IllegalArgumentException("min is infinite");
        }
        if (Double.isInfinite(max)) {
            throw new IllegalArgumentException("max is infinite");
        }
        if (Double.isNaN(min)) {
            throw new IllegalArgumentException("min is not a number (NaN)");
        }
        if (Double.isNaN(max)) {
            throw new IllegalArgumentException("max is not a number (NaN)");
        }
        if (min == max) {
            return ignored -> min;
        } else {
            return random -> {
                final double d = random.nextDouble();
                final double _min = Math.min(min, max);
                final double _max = Math.max(min, max);
                return d * _max + (1.0 - d) * _min;
            };
        }
    }

    /**
     * Chooses a char between min and max, bounds inclusive and chars distributed according to the underlying random
     * number generator.
     * <p>
     * Note: min and max are internally swapped if min &gt; max.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new char generator
     */
    static Gen<Character> choose(char min, char max) {
        if (min == max) {
            return ignored -> min;
        } else {
            return Gen.choose((int) min, (int) max).map(i -> (char) (int) i);
        }
    }

    /**
     * Chooses a char from all chars in the array
     *
     * @param characters array with the characters to choose from
     * @return A new array generator
     */
    static Gen<Character> choose(char... characters) {
        Objects.requireNonNull(characters, "characters is null");
        final Character[] validCharacters = List.ofAll(characters).toJavaArray(Character[]::new);
        return choose(validCharacters);
    }

    /**
     * Chooses an enum value from all the enum constants defined in the enumerated type.
     *
     * @param clazz Enum class
     * @param <T>   type of enum constants
     * @return A new enum generator
     */
    static <T extends Enum<T>> Gen<T> choose(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return Gen.choose(clazz.getEnumConstants());
    }

    /**
     * Chooses a value from all values in the array.
     *
     * @param values array with the values to choose from
     * @param <T>    value type
     * @return A new array generator
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    static <T> Gen<T> choose(T... values) {
        Objects.requireNonNull(values, "values is null");
        if (values.length == 0) {
            return Gen.fail("Empty array");
        } else {
            return Gen.choose(0, values.length - 1).map(i -> values[i]);
        }
    }

    /**
     * Chooses a value from all values in the iterable
     *
     * @param values iterable with the values to choose from.
     * @param <T>    value type
     * @return A new iterable generator
     */
    static <T> Gen<T> choose(Iterable<T> values) {
        Objects.requireNonNull(values, "values is null");
        final Iterator<T> iterator = Iterator.ofAll(values);
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Empty iterable");
        }
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) iterator.toJavaArray();
        return choose(array);
    }

    /**
     * A failing generator which throws a RuntimeException("failed").
     *
     * @param <T> Type of values theoretically generated.
     * @return A new generator which always fails with the message "failed"
     */
    static <T> Gen<T> fail() {
        return fail("failed");
    }

    /**
     * A failing generator which throws a RuntimeException.
     *
     * @param message Message thrown.
     * @param <T>     Type of values theoretically generated.
     * @return A new generator which always fails with the given message
     */
    static <T> Gen<T> fail(String message) {
        return ignored -> {
            throw new RuntimeException(message);
        };
    }

    /**
     * Chooses one of the given generators according to their frequency.
     * Only generators with positive frequencies are used in returned
     * generator.
     *
     * @param generators A non-empty array of Tuples (frequency, generator)
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators doesn't contain any generator with positive frequency
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> Gen<T> frequency(Tuple2<Integer, Gen<T>>... generators) {
        Objects.requireNonNull(generators, "generators is null");
        if (generators.length == 0) {
            throw new IllegalArgumentException("generators is empty");
        }
        final Iterable<Tuple2<Integer, Gen<T>>> iterable = Stream.of(generators);
        return frequency(iterable);
    }

    /**
     * Chooses one of the given generators according to their frequency.
     * Only generators with positive frequencies ares used in returned
     * generator.
     *
     * @param generators A non-empty traversable of Tuples (frequency, generator)
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators doesn't contain any generator with positive frequency
     */
    static <T> Gen<T> frequency(Iterable<Tuple2<Integer, Gen<T>>> generators) {
        Objects.requireNonNull(generators, "generators is null");
        final Vector<Tuple2<Integer, Gen<T>>> filtered = Iterator.ofAll(generators)
                .filter(t -> t._1() > 0).toVector();
        if (filtered.isEmpty()) {
            throw new IllegalArgumentException("no generator with positive weight");
        }
        final int size = filtered.map(t -> t._1).sum().intValue();
        return choose(1, size).flatMap(n -> GenModule.frequency(n, filtered.iterator()));
    }

    /**
     * Intersperse values from this generator instance with those of another.
     *
     * @param other another T generator to accept values from.
     * @return A new T generator
     */
    default Gen<T> intersperse(Gen<T> other) {
        final Iterator<Gen<T>> iter = Iterator.continually(this).intersperse(other);
        return random -> iter.get().apply(random);
    }

    /**
     * Randomly chooses one of the given generators.
     *
     * @param generators A non-empty array of generators
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators is empty
     */
    @SafeVarargs
    static <T> Gen<T> oneOf(Gen<T>... generators) {
        Objects.requireNonNull(generators, "generators is null");
        if (generators.length == 0) {
            throw new IllegalArgumentException("generators is empty");
        }
        return choose(0, generators.length - 1).flatMap(i -> generators[i]);
    }

    /**
     * Randomly chooses one of the given generators.
     *
     * @param generators A non-empty Iterable of generators
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators is empty
     */
    static <T> Gen<T> oneOf(Iterable<Gen<T>> generators) {
        Objects.requireNonNull(generators, "generators is null");
        final Stream<Gen<T>> stream = Stream.ofAll(generators);
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("generators is empty");
        }
        @SuppressWarnings("unchecked")
        final Gen<T>[] array = stream.toJavaArray(Gen[]::new);
        return oneOf(array);
    }

    /**
     * Converts this Gen to an Arbitrary
     *
     * @return An arbitrary which returns this generator regardless of the provided size hint n
     */
    default Arbitrary<T> arbitrary() {
        return n -> this;
    }

    /**
     * Returns a generator based on this generator which produces values that fulfill the given predicate.
     *
     * @param predicate A predicate
     * @return A new generator
     */
    default Gen<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return random -> {
            int count = 0;
            final int filterThreshold = Integer.MAX_VALUE;
            T t;
            while (!predicate.test(t = apply(random))) {
                // it may take a looooooong time to hit this condition!
                if (++count == filterThreshold) {
                    throw new IllegalStateException("empty filter");
                }
            }
            return t;
        };
    }

    /**
     * Maps generated Ts to Us.
     *
     * @param mapper A function that maps a generated T to a new generator which generates objects of type U.
     * @param <U>    Type of generated objects of the new generator
     * @return A new generator
     */
    default <U> Gen<U> flatMap(Function<? super T, ? extends Gen<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return random -> mapper.apply(apply(random)).apply(random);
    }

    /**
     * Maps generated Ts to Us.
     *
     * @param mapper A function that maps a generated T to an object of type U.
     * @param <U>    Type of the mapped object
     * @return A new generator
     */
    default <U> Gen<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return random -> mapper.apply(apply(random));
    }

    default Gen<T> peek(Consumer<? super T> action) {
        return random -> {
            final T t = apply(random);
            action.accept(t);
            return t;
        };
    }

    /**
     * Transforms this {@code Gen}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Gen<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }
}

interface GenModule {

    /**
     * Chooses a Gen according to the given frequencies.
     *
     * @param n    a random value between 0 and sum(frequencies) - 1
     * @param iter a non-empty Iterator of (frequency, Gen) pairs
     * @param <T>  type of generated values
     * @return A value generator, choosen according to the given frequencies and the underlying n
     */
    static <T> Gen<T> frequency(int n, java.util.Iterator<Tuple2<Integer, Gen<T>>> iter) {
        do {
            final Tuple2<Integer, Gen<T>> freqGen = iter.next();
            final int k = freqGen._1;
            if (n <= k) {
                return freqGen._2;
            } else {
                n = n - k;
            }
        } while (true);
    }

}
