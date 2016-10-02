/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Stream;

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
 * @since 1.2.0
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
     * @param <U> Type of generator value
     * @return A new generator
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    static <U> Arbitrary<U> of(U... values) {
        return ofAll(Gen.choose(values));
    }

    /**
     * Generates an arbitrary value from a given generator
     * @param generator A generator to produce arbitrary values
     * @param <U> Type of generator value
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
        return size -> random -> Gen.choose(-size, size).apply(random);
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
            return random -> Gen.choose(0, size).map(i -> {
                List<T> list = List.empty();
                for (int j = 0; j < i; j++) {
                    final T element = genT.apply(random);
                    list = list.prepend(element);
                }
                return list;
            }).apply(random);
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
            return random -> Gen.choose(0, size).map(i -> Stream.ofAll(new Iterator<T>() {

                int count = i;

                @Override
                public boolean hasNext() {
                    return count-- > 0;
                }

                @Override
                public T next() {
                    return genT.apply(random);
                }
            })).apply(random);
        };
    }
}
