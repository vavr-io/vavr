/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra;
import javaslang.Tuple;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class FoldableTest {

    // -- NOTE: creational operations are not tested because they are implemented by the Testee 'Seq'

    // -- reducing operations

    @Test
    public void shouldFoldLeftEmptyFoldable() {
        final String actual = Seq.<Integer>of().foldLeft("''", (xs, x) -> "(" + xs + " + '" + x + "')");
        assertThat(actual).isEqualTo("''");
    }

    @Test
    public void shouldFoldLeftNonEmptyFoldable() {
        final String actual = Seq.of(1, 2, 3).foldLeft("''", (xs, x) -> "(" + xs + " + '" + x + "')");
        assertThat(actual).isEqualTo("((('' + '1') + '2') + '3')");
    }

    @Test
    public void shouldFoldRightEmptyFoldable() {
        final String actual = Seq.<Integer>of().foldRight("''", (x, xs) -> "(" + xs + " + '" + x + "')");
        assertThat(actual).isEqualTo("''");
    }

    @Test
    public void shouldFoldRightNonEmptyFoldable() {
        final String actual = Seq.of(1, 2, 3).foldRight("''", (x, xs) -> "(" + xs + " + '" + x + "')");
        assertThat(actual).isEqualTo("((('' + '3') + '2') + '1')");
    }

    @Test
    public void shouldFoldMapEmptyFoldable() {
        final String actual = Seq.<Integer>of().foldMap(Algebra.Monoid.of("", (s1, s2) -> s1 + s2), String::valueOf);
        assertThat(actual).isEqualTo("");
    }

    @Test
    public void shouldFoldMapNonEmptyFoldable() {
        final String actual = Seq.<Integer>of(1, 2, 3).foldMap(Algebra.Monoid.of("", (s1, s2) -> s1 + s2), String::valueOf);
        assertThat(actual).isEqualTo("123");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceLeftEmptyFoldable() {
        Seq.<Integer>of().reduceLeft((i1, i2) -> i1 + i2);
    }

    @Test
    public void shouldReduceLeftNonEmptyFoldable() {
        final Integer actual = Seq.<Integer>of(1, 2, 3).reduceLeft((i1, i2) -> i1 + i2);
        assertThat(actual).isEqualTo(6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceRightEmptyFoldable() {
        Seq.<Integer>of().reduceRight((i1, i2) -> i1 + i2);
    }

    @Test
    public void shouldReduceRightNonEmptyFoldable() {
        final Integer actual = Seq.<Integer>of(1, 2, 3).reduceRight((i1, i2) -> i1 + i2);
        assertThat(actual).isEqualTo(6);
    }

    // -- basic operations

    @Test
    public void shouldBeEmptyGivenEmptyFoldable() {
        assertThat(Seq.of().isEmpty()).isTrue();
    }

    @Test
    public void shouldNotBeEmptyGivenNonEmptyFoldable() {
        assertThat(Seq.of(1).isEmpty()).isFalse();
    }

    @Test
    public void shouldComputeLengthOfEmptyFoldable() {
        assertThat(Seq.of().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).length()).isEqualTo(3);
    }

    @Test
    public void shouldComputeSizeOfEmptyFoldable() {
        assertThat(Seq.of().size()).isEqualTo(0);
    }

    @Test
    public void shouldComputeSizeOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).size()).isEqualTo(3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenHeadOfEmptyFoldable() {
        Seq.of().head();
    }

    @Test
    public void shouldGetHeadOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).head()).isEqualTo(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfEmptyFoldable() {
        Seq.of().init();
    }

    @Test
    public void shouldGetInitOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).init()).isEqualTo(Seq.of(1, 2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLastOfEmptyFoldable() {
        Seq.of().last();
    }

    @Test
    public void shouldGetLastOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).last()).isEqualTo(3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOfEmptyFoldable() {
        Seq.of().tail();
    }

    @Test
    public void shouldGetTailOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).tail()).isEqualTo(Seq.of(2, 3));
    }

    // -- filtering & transformations

    @Test
    public void shouldFilterEmptyFoldable() {
        assertThat(Seq.<Integer>of().filter(i -> i % 2 == 0)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldFilterNonEmptyFoldable() {
        assertThat(Seq.<Integer>of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(Seq.of(2, 4));
    }

    @Test
    public void shouldFlatMapEmptyFoldable() {
        final Seq<String> actual = Seq.of().flatMap(i -> Seq.of(String.valueOf(i)));
        assertThat(actual).isEqualTo(Seq.of());
    }

    @Test
    public void shouldFlatMapNonEmptyFoldable() {
        final Seq<String> actual = Seq.of(1,2,3).flatMap(i -> Seq.of(String.valueOf(i)));
        final Seq<String> expected = Seq.of("1", "2", "3");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMapEmptyFoldable() {
        final Seq<String> actual = Seq.of().map(String::valueOf);
        assertThat(actual).isEqualTo(Seq.of());
    }

    @Test
    public void shouldMapNonEmptyFoldable() {
        final Seq<String> actual = Seq.of(1,2,3).map(String::valueOf);
        final Seq<String> expected = Seq.of("1", "2", "3");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersperseEmptyFoldable() {
        final Seq<String> actual = Seq.<String> of().intersperse("#");
        assertThat(actual).isEqualTo(Seq.of());
    }

    @Test
    public void shouldIntersperseNonEmptyFoldable() {
        final Seq<String> actual = Seq.of("1","2","3").intersperse("#");
        final Seq<String> expected = Seq.of("1", "#", "2", "#", "3");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReverseEmptyFoldable() {
        assertThat(Seq.of().reverse()).isEqualTo(Seq.of());
    }

    @Test
    public void shouldReverseNonEmptyFoldable() {
        assertThat(Seq.of(1,2,3).reverse()).isEqualTo(Seq.of(3,2,1));
    }

    @Test
    public void shouldSpanEmptyFoldable() {
        assertThat(Seq.<Integer> of().span(i -> i < 3)).isEqualTo(Tuple.of(Seq.of(), Seq.of()));
    }

    @Test
    public void shouldSpanNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3, 4).span(i -> i < 3)).isEqualTo(Tuple.of(Seq.of(1, 2), Seq.of(3, 4)));
    }

    @Test
    public void shouldSplitAtEmptyFoldable() {
        assertThat(Seq.of().splitAt(2)).isEqualTo(Tuple.of(Seq.of(), Seq.of()));
    }

    @Test
    public void shouldSplitAtNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3, 4).splitAt(2)).isEqualTo(Tuple.of(Seq.of(1, 2), Seq.of(3, 4)));
    }

    @Test
    public void shouldZipEmptyFoldable() {
        final Seq<Tuple.Tuple2<Object, Object>> actual = Seq.of().zip(Seq.of());
        final Seq<Tuple.Tuple2<Object, Object>> expected = Seq.of();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonEmptyFoldable() {
        final Seq<Tuple.Tuple2<Integer, String>> actual = Seq.of(1, 2).zip(Seq.of("a", "b"));
        final Seq<Tuple.Tuple2<Integer, String>> expected = Seq.of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldZipAllNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldZipWithIndexEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldZipWithIndexNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldUnzipEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldUnzipNonEmptyFoldable() {
        // TODO
    }

    // -- selection operations

    @Test
    public void shouldDropEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldDropNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldDropRightEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldDropRightNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldDropWhileEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldDropWhileNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeRightEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeRightNonEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeWhileEmptyFoldable() {
        // TODO
    }

    @Test
    public void shouldTakeWhileNonEmptyFoldable() {
        // TODO
    }

    // -- test helpers

    /**
     * A mutable List based Foldable implementation for testing purposes only.
     *
     * @param <T> Component type of the sequence.
     */
    static class Seq<T> implements Foldable<T, Seq<?>, Seq<T>> {

        private final java.util.List<T> data = new java.util.ArrayList<>();

        private Seq() {
        }

        // -- Foldable

        @Override
        public Iterator<T> iterator() {
            return data.iterator();
        }

        @Override
        public <B> Seq<B> unit(B b) {
            final Seq<B> seq = new Seq<>();
            seq.data.add(b);
            return seq;
        }

        @Override
        public Seq<T> zero() {
            return new Seq<>();
        }

        @Override
        public Seq<T> combine(Seq<T> a1, Seq<T> a2) {
            final Seq<T> seq = new Seq<>();
            seq.data.addAll(a1.data);
            seq.data.addAll(a2.data);
            return seq;
        }

        // -- need to correct return types of Foldable operations

        @Override
        public <U, SEQ extends Algebra.Monad<U, Seq<?>>> Seq<U> flatMap(Function<? super T, SEQ> mapper) {
            //noinspection unchecked
            return (Seq) Foldable.super.flatMap(mapper::apply);
        }

        @Override
        public <U> Seq<U> map(Function<? super T, ? extends U> mapper) {
            //noinspection unchecked
            return (Seq) Foldable.super.map(mapper::apply);
        }

        @Override
        public <U> Seq<Tuple.Tuple2<T, U>> zip(Iterable<U> that) {
            //noinspection unchecked
            return (Seq) Foldable.super.zip(that);
        }

        // -- equals, hashCode & toString

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof Seq && ((Seq) o).data.equals(data));
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

        @Override
        public String toString() {
            return data.toString();
        }

        // -- factory methods

        @SafeVarargs
        public static <T> Seq<T> of(T... elements) {
            final Seq<T> seq = new Seq<>();
            Collections.addAll(seq.data, elements);
            return seq;
        }
    }
}
