/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple2;
import javaslang.Value;
import javaslang.collection.Iterator;
import javaslang.collection.Stream;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>Generators are the building blocks for providing arbitrary objects.</p>
 * <p>To ease the creation of Arbitraries, Gen is a FunctionalInterface which extends {@code Function1<Random, T>}.</p>
 * <p>Gen objects are obtained via one of the methods {@code choose}, {@code fail}, {@code frequency}, {@code of} and
 * {@code oneOf}.</p>
 * <p>Given Gen objects may be transformed using one of the methods {@code filter}, {@code map} and {@code flatMap}.</p>
 * <p>A simple way to obtain an Arbitrary of a Gen is to call {@linkplain javaslang.test.Gen#arbitrary()}.
 * This will ignore the size hint of Arbitrary.</p>
 *
 * @param <T> type of generated objects
 * @see javaslang.test.Arbitrary
 * @since 1.2.0
 */
@FunctionalInterface
public interface Gen<T> extends Value<T> {

    int FILTER_THRESHOLD = Integer.MAX_VALUE;

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
        final Iterator<T> iterator = Stream.gen(seed, next).iterator();
        return ignored -> iterator.next();
    }

    /**
     * <p>Chooses an int between min and max, bounds inclusive and numbers distributed according to the distribution of
     * the underlying random number generator.</p>
     * <p>Note: min and max are internally swapped if min &gt; max.</p>
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
     * <p>Chooses a long between min and max, bounds inclusive and numbers distributed according to the distribution of
     * the underlying random number generator.</p>
     * <p>Note: min and max are internally swapped if min &gt; max.</p>
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
     * <p>Chooses a double between min and max, bounds inclusive and numbers distributed according to the distribution
     * of the underlying random number generator.</p>
     * <p>Note: min and max are internally swapped if min &gt; max.</p>
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
     * <p>Chooses a char between min and max, bounds inclusive and chars distributed according to the underlying random
     * number generator.</p>
     * <p>Note: min and max are internally swapped if min &gt; max.</p>
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new char generator
     */
    static Gen<Character> choose(char min, char max) {
        if (min == max) {
            return ignored -> min;
        } else {
            return random -> (char) (int) Gen.choose((int) min, (int) max).apply(random);
        }
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
     *
     * @param generators A non-empty array of Tuples (frequency, generator)
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators is empty
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
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
     *
     * @param generators A non-empty traversable of Tuples (frequency, generator)
     * @param <T>        Type to be generated
     * @return A new T generator
     * @throws java.lang.NullPointerException     if generators is null
     * @throws java.lang.IllegalArgumentException if generators is empty
     */
    static <T> Gen<T> frequency(Iterable<Tuple2<Integer, Gen<T>>> generators) {
        Objects.requireNonNull(generators, "generators is null");
        final Stream<Tuple2<Integer, Gen<T>>> stream = Stream.ofAll(generators);
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("generators is empty");
        }
        final class Frequency {
            Gen<T> gen(int n, Stream<Tuple2<Integer, Gen<T>>> stream) {
                final int k = stream.head()._1;
                if (k < 0) {
                    throw new IllegalArgumentException("negative frequency: " + k);
                }
                return (n <= k) ? stream.head()._2 : gen(n - k, stream.tail());
            }
        }
        final int size = stream.map(t -> t._1).sum().intValue();
        return choose(1, size).flatMap(n -> new Frequency().gen(n, stream));
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
        final Gen<T>[] array = stream.toJavaArray((Class<Gen<T>>) (Class) Gen.class);
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
    @Override
    default Gen<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return random -> {
            int count = 0;
            T t;
            while (!predicate.test(t = apply(random))) {
                // it may take a looooooong time to hit this condition!
                if (++count == FILTER_THRESHOLD) {
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

    @SuppressWarnings("unchecked")
    @Override
    default <U> Gen<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return random -> mapper.apply(apply(random)).get();
    }

    @Override
    default Gen<Object> flatten() {
        return random -> {
            final T value = apply(random);
            return (value instanceof Gen) ? ((Gen<?>) value).flatten().apply(random) : value;
        };
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

    @Override
    default Gen<T> peek(Consumer<? super T> action) {
        return random -> {
            final T t = apply(random);
            action.accept(t);
            return t;
        };
    }

    @Override
    default T get() {
        return apply(Checkable.RNG.get());
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default Iterator<T> iterator() {
        final Random random = Checkable.RNG.get();
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return apply(random);
            }
        };
    }
}
