/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple2;
import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;
import javaslang.collection.Stream;
import javaslang.collection.Traversable;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * Generators are the building blocks for providing arbitrary objects.
 * </p>
 * <p>
 * Gen&lt;T&gt; implements Iterable&lt;T&gt; which allows us to stream arbitrary generated objects.
 * </p>
 * Example:
 * <pre>
 * <code>
 * // random trees
 * final Gen&lt;BinaryTree&lt;Integer&gt;&gt; treeGen = ...;
 *
 * // stream sum of tree node values to console for 100 trees
 * Stream.of(treeGen).map(Tree::sum).take(100).stdout();
 * </code>
 * </pre>
 *
 * @param <T> type of generated objects
 * @see javaslang.test.Arbitrary
 */
@FunctionalInterface
public interface Gen<T> extends Function<Random, T>, Monad1<T, Gen<?>> {

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

    /**
     * <p>Chooses an int between min and max, bounds inclusive and numbers equally distributed.</p>
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
     * <p>Chooses a long between min and max, bounds inclusive and numbers equally distributed.</p>
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
     * <p>Chooses a double between min and max, bounds inclusive and numbers equally distributed.</p>
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
    static <T> Gen<T> frequency(Tuple2<Integer, Gen<T>>... generators) {
        Objects.requireNonNull(generators, "generators is null");
        if (generators.length == 0) {
            throw new IllegalArgumentException("generators is empty");
        }
        @SuppressWarnings({"unchecked", "varargs"})
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
        final Traversable<Tuple2<Integer, Gen<T>>> traversable = Traversable.of(generators);
        if (traversable.isEmpty()) {
            throw new IllegalArgumentException("generators is empty");
        }
        final class Frequency {
            Gen<T> gen(int n, Traversable<Tuple2<Integer, Gen<T>>> list) {
                if (list.isEmpty()) {
                    return fail("frequency of nil");
                } else {
                    final int k = list.head()._1;
                    return (n <= k) ? list.head()._2 : gen(n - k, list.tail());
                }
            }
        }
        final int size = traversable.map(t -> t._1).sum();
        return choose(1, size).flatMap(n -> new Frequency().gen(n, traversable));
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
        final Traversable<Gen<T>> traversable = Traversable.of(generators);
        if (traversable.isEmpty()) {
            throw new IllegalArgumentException("generators is empty");
        }
        @SuppressWarnings({"unchecked", "varargs"})
        final Gen<T>[] array = traversable.toJavaArray((Class<Gen<T>>) (Class) Gen.class);
        return oneOf(array);
    }

    /**
     * Functional interface of this generator.
     *
     * @return A generated value of type T.
     */
    @Override
    T apply(Random random);

    /**
     * Converts this Gen to an Arbitrary
     * @return An arbitrary which returns this generator regardless of the provided size hint n
     */
    default Arbitrary<T> arbitrary() {
        return n -> this;
    }

    /**
     * Maps generated Ts to Us.
     *
     * @param mapper A function that maps a generated T to an object of type U.
     * @param <U>    Type of the mapped object
     * @return A new generator
     */
    @Override
    default <U> Gen<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return random -> mapper.apply(apply(random));
    }

    /**
     * Maps generated Ts to Us.
     *
     * @param mapper A function that maps a generated T to a new generator which generates objects of type U.
     * @param <U>    Type of generated objects of the new generator
     * @return A new generator
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U, GEN extends HigherKinded1<U, Gen<?>>> Gen<U> flatMap(Function<? super T, GEN> mapper) {
        //noinspection Convert2MethodRef
        return random -> ((Gen<U>) mapper.apply(apply(random))).apply(random);
    }

    /**
     * Returns a generator based on this generator which produces values that fulfill the given predicate.
     *
     * @param predicate A predicate
     * @return A new generator
     */
    default Gen<T> filter(Predicate<T> predicate) {
        return random -> {
            int count = 0;
            T t;
            while (!predicate.test(t = apply(random))) {
                if (++count == Integer.MAX_VALUE) {
                    throw new IllegalStateException("empty filter");
                }
            }
            return t;
        };
    }
}
