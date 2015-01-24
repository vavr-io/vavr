/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.algebra.*;
import javaslang.Tuple2;
import javaslang.collection.Stream;
import javaslang.collection.Traversable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
public interface Gen<T> extends Supplier<T>, Monad<T, Gen<?>>, Iterable<T> {

    /**
     * A thread-safe, equally distributed random number generator.
     */
    static Supplier<Random> RNG = ThreadLocalRandom::current;

    /**
     * A generator which constantly returns t.
     *
     * @param t   A value.
     * @param <T> Type of t.
     * @return A new T generator
     */
    static <T> Gen<T> of(T t) {
        return () -> t;
    }

    /**
     * Chooses an int between min and max, bounds inclusive and numbers equally distributed.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new int generator
     * @throws IllegalArgumentException if min &gt; max
     */
    static Gen<Integer> choose(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min > max: %s > %s", min, max));
        }
        if (min == max) {
            return () -> min;
        } else {
            return () -> RNG.get().nextInt(Math.abs(max - min) + 1) + min;
        }
    }

    /**
     * Chooses a long between min and max, bounds inclusive and numbers equally distributed.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new long generator
     * @throws IllegalArgumentException if min &gt; max
     */
    static Gen<Long> choose(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min > max: %s > %s", min, max));
        }
        if (min == max) {
            return () -> min;
        } else {
            return () -> {
                final double d = RNG.get().nextDouble();
                return (long) ((d * max) + ((1.0 - d) * min) + d);
            };
        }
    }

    /**
     * Chooses a double between min and max, bounds inclusive and numbers equally distributed.
     *
     * @param min lower bound
     * @param max upper bound
     * @return A new double generator
     * @throws IllegalArgumentException if min &gt; max, min or max is infinite, min or max is not a number (NaN)
     */
    static Gen<Double> choose(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min > max: %s > %s", min, max));
        }
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
            return () -> min;
        } else {
            return () -> {
                final double d = RNG.get().nextDouble();
                return d * max + (1.0 - d) * min;
            };
        }
    }

    /**
     * A failing generator which throws a RuntimeException on {@link #get()}.
     *
     * @param message Message thrown.
     * @param <T>     Type of values theoretically generated.
     * @return A new generator which always fails with the given message
     */
    static <T> Gen<T> fail(String message) {
        return () -> {
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
        return frequency(Stream.of(generators));
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
        //noinspection unchecked
        return oneOf(traversable.toJavaArray((Class<Gen<T>>) (Class) Gen.class));
    }

    /**
     * Functional interface of this generator, inherited by {@link java.util.function.Supplier}.
     *
     * @return A generated value of type T.
     */
    @Override
    T get();

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
        return () -> mapper.apply(get());
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
    default <U, GEN extends HigherKinded<U, Gen<?>>> Gen<U> flatMap(Function<? super T, GEN> mapper) {
        //noinspection Convert2MethodRef
        return () -> ((Gen<U>) mapper.apply(get())).get();
    }

    /**
     * Returns a generator based on this generator which produces values that fulfill the given predicate.
     *
     * @param predicate A predicate
     * @return A new generator
     */
    default Gen<T> filter(Predicate<T> predicate) {
        return () -> {
            int count = 0;
            T t;
            while (!predicate.test(t = get())) {
                if (++count == Integer.MAX_VALUE) {
                    throw new IllegalStateException("empty filter");
                }
            }
            return t;
        };
    }

    /**
     * An infinite number of random objects of type T generated by this.
     *
     * @return A new iterator.
     */
    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return Gen.this.get();
            }
        };
    }
}
