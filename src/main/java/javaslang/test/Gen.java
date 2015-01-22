/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Algebra.*;
import javaslang.Tuple.Tuple2;
import javaslang.collection.List;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Generators are the basis for providing arbitrary objects.
 *
 * @param <T> type of generated objects
 */
@FunctionalInterface
public interface Gen<T> extends Supplier<T>, Monad<T, Gen<?>> {

    // @see http://javarevisited.blogspot.co.uk/2013/05/how-to-generate-random-numbers-in-java-between-range.html
    static Supplier<Random> RND = ThreadLocalRandom::current;

    static <T> Gen<T> constant(T t) {
        return () -> t;
    }

    static <T> Gen<Integer> choose(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min > max: %s > %s", min, max));
        }
        return () -> RND.get().nextInt(Math.abs(max - min) + 1) + min;
    }

    static <T> Gen<T> fail(String message) {
        return () -> { throw new RuntimeException(message); };
    }

    @SafeVarargs
    static <T> Gen<T> frequency(Tuple2<Integer, Gen<T>>... ts) {
        Objects.requireNonNull(ts, "ts is null");
        return frequency(List.of(ts));
    }

    static <T> Gen<T> frequency(List<Tuple2<Integer, Gen<T>>> ts) {
        Objects.requireNonNull(ts, "ts is null");
        final class Frequency {
            Gen<T> gen(int n, List<Tuple2<Integer, Gen<T>>> list) {
                if (list.isEmpty()) {
                    return fail("frequency of nil");
                } else {
                    final int k = list.head()._1;
                    return (n <= k) ? list.head()._2 : Frequency.this.gen(n - k, list.tail());
                }
            }
        }
        final int size = ts.map(t -> t._1).sum();
        return choose(1, size).flatMap(n -> new Frequency().gen(n, ts));
    }

    @SafeVarargs
    static <T> Gen<T> oneOf(Gen<T>... ts) {
        return choose(0, ts.length - 1).flatMap(i -> ts[i]);
    }

    static <T> Gen<T> oneOf(Iterable<Gen<T>> ts) {
        //noinspection unchecked
        return oneOf(List.of(ts).toJavaArray((Class<Gen<T>>) (Class) Gen.class));
    }

    @Override
    T get();

    @Override
    default <U> Gen<U> map(Function<? super T, ? extends U> f) {
        return () -> f.apply(get());
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U, GEN extends HigherKinded<U, Gen<?>>> Gen<U> flatMap(Function<? super T, GEN> f) {
        return (Gen<U>) f.apply(get());
    }

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
}
