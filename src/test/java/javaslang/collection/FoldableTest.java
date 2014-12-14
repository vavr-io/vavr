/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra;
import javaslang.Manifest;
import javaslang.Tuple;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class FoldableTest {

    // -- NOTE: creational operations are not tested because they are implemented by the Testee 'Seq'

    // -- reducing operations

    // foldLeft

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

    // foldRight

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

    // foldMap

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

    // reduceLeft

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceLeftEmptyFoldable() {
        Seq.<Integer>of().reduceLeft((i1, i2) -> i1 + i2);
    }

    @Test
    public void shouldReduceLeftNonEmptyFoldable() {
        final Integer actual = Seq.<Integer>of(1, 2, 3).reduceLeft((i1, i2) -> i1 + i2);
        assertThat(actual).isEqualTo(6);
    }

    // reduceRight

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

    // isEmpty

    @Test
    public void shouldBeEmptyGivenEmptyFoldable() {
        assertThat(Seq.of().isEmpty()).isTrue();
    }

    @Test
    public void shouldNotBeEmptyGivenNonEmptyFoldable() {
        assertThat(Seq.of(1).isEmpty()).isFalse();
    }

    // length

    @Test
    public void shouldComputeLengthOfEmptyFoldable() {
        assertThat(Seq.of().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).length()).isEqualTo(3);
    }

    // size

    @Test
    public void shouldComputeSizeOfEmptyFoldable() {
        assertThat(Seq.of().size()).isEqualTo(0);
    }

    @Test
    public void shouldComputeSizeOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).size()).isEqualTo(3);
    }

    // head

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenHeadOfEmptyFoldable() {
        Seq.of().head();
    }

    @Test
    public void shouldGetHeadOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).head()).isEqualTo(1);
    }

    // init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfEmptyFoldable() {
        Seq.of().init();
    }

    @Test
    public void shouldGetInitOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).init()).isEqualTo(Seq.of(1, 2));
    }

    // last

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLastOfEmptyFoldable() {
        Seq.of().last();
    }

    @Test
    public void shouldGetLastOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).last()).isEqualTo(3);
    }

    // tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOfEmptyFoldable() {
        Seq.of().tail();
    }

    @Test
    public void shouldGetTailOfNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).tail()).isEqualTo(Seq.of(2, 3));
    }

    // -- filtering & transformations

    // filter

    @Test
    public void shouldFilterEmptyFoldable() {
        assertThat(Seq.<Integer>of().filter(i -> i % 2 == 0)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldFilterNonEmptyFoldable() {
        assertThat(Seq.<Integer>of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(Seq.of(2, 4));
    }

    // flatMap

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

    // map

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

    // intersperse

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

    // reverse

    @Test
    public void shouldReverseEmptyFoldable() {
        assertThat(Seq.of().reverse()).isEqualTo(Seq.of());
    }

    @Test
    public void shouldReverseNonEmptyFoldable() {
        assertThat(Seq.of(1,2,3).reverse()).isEqualTo(Seq.of(3,2,1));
    }

    // span

    @Test
    public void shouldSpanEmptyFoldable() {
        assertThat(Seq.<Integer> of().span(i -> i < 3)).isEqualTo(Tuple.of(Seq.of(), Seq.of()));
    }

    @Test
    public void shouldSpanNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3, 4).span(i -> i < 3)).isEqualTo(Tuple.of(Seq.of(1, 2), Seq.of(3, 4)));
    }

    // split

    @Test
    public void shouldSplitAtEmptyFoldable() {
        assertThat(Seq.of().splitAt(2)).isEqualTo(Tuple.of(Seq.of(), Seq.of()));
    }

    @Test
    public void shouldSplitAtNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3, 4).splitAt(2)).isEqualTo(Tuple.of(Seq.of(1, 2), Seq.of(3, 4)));
    }

    // zip

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

    // zipAll

    @Test
    public void shouldZipAllEmptyFoldable() {
        final Seq<Tuple.Tuple2<Object, Object>> actual = Seq.of().zipAll(Seq.of(), null, null);
        assertThat(actual).isEqualTo(Seq.of());
    }

    @Test
    public void shouldZipAllIfFirstIsBigger() {
        final Seq<Tuple.Tuple2<Integer,String>> actual = Seq.of(1, 2).zipAll(Seq.of("a"), 9, "z");
        final Seq<Tuple.Tuple2<Integer, String>> expected = Seq.of(Tuple.of(1,"a"), Tuple.of(2, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllIfSecondIsBigger() {
        final Seq<Tuple.Tuple2<Integer,String>> actual = Seq.of(1).zipAll(Seq.of("a", "b"), 9, "z");
        final Seq<Tuple.Tuple2<Integer, String>> expected = Seq.of(Tuple.of(1,"a"), Tuple.of(9, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllBothSameSize() {
        final Seq<Tuple.Tuple2<Integer,String>> actual = Seq.of(1,2).zipAll(Seq.of("a", "b"), 9, "z");
        final Seq<Tuple.Tuple2<Integer, String>> expected = Seq.of(Tuple.of(1,"a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    // zipWithIndex

    @Test
    public void shouldZipWithIndexEmptyFoldable() {
        assertThat(Seq.of().zipWithIndex()).isEqualTo(Seq.of());
    }

    @Test
    public void shouldZipWithIndexNonEmptyFoldable() {
        assertThat(Seq.of("a", "b").zipWithIndex()).isEqualTo(Seq.of(Tuple.of("a", 0), Tuple.of("b", 1)));
    }

    // unzip

    @Test
    public void shouldUnzipEmptyFoldable() {
        final Tuple.Tuple2<Seq<Integer>, Seq<Integer>> actual = Seq.of().unzip(t -> null);
        final Tuple.Tuple2<Seq<Integer>, Seq<Integer>> expected = Tuple.of(Seq.of(), Seq.of());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnzipNonEmptyFoldable() {
        final Tuple.Tuple2<Seq<Integer>, Seq<Integer>> actual = Seq.of(1, 2).unzip(i -> Tuple.of(i / 2, i % 2));
        final Tuple.Tuple2<Seq<Integer>, Seq<Integer>> expected = Tuple.of(Seq.of(0,1), Seq.of(1,0));
        assertThat(actual).isEqualTo(expected);
    }

    // -- selection operations

    // drop

    @Test
    public void shouldDropEmptyFoldable() {
        final Seq<?> seq = Seq.of();
        assertThat(seq.drop(1)).isEqualTo(seq);
    }

    @Test
    public void shouldDropOneOfNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.drop(1)).isEqualTo(Seq.of(2));
    }

    @Test
    public void shouldDropMoreThanContained() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.drop(3)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldDropNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.drop(1)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldDropZeroElements() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.drop(0)).isEqualTo(seq);
    }

    @Test
    public void shouldDropNegativeCount() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.drop(-1)).isEqualTo(seq);
    }

    // dropRight

    @Test
    public void shouldDropRightEmptyFoldable() {
        final Seq<?> seq = Seq.of();
        assertThat(seq.dropRight(1)).isEqualTo(seq);
    }

    @Test
    public void shouldDropRightOneOfNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.dropRight(1)).isEqualTo(Seq.of(1));
    }

    @Test
    public void shouldDropRightMoreThanContained() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.dropRight(3)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldDropRightNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.dropRight(1)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldDropRightZeroElements() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.dropRight(0)).isEqualTo(seq);
    }

    @Test
    public void shouldDropRightNegativeCount() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.dropRight(-1)).isEqualTo(seq);
    }

    // dropWhile

    @Test
    public void shouldDropWhileEmptyFoldable() {
        assertThat(Seq.<Integer> of().dropWhile(i -> i <= 2)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldDropWhileNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).dropWhile(i -> i <= 2)).isEqualTo(Seq.of(3));
    }

    @Test
    public void shouldDropWhileWhenAlwaysTrue() {
        assertThat(Seq.of(1, 2, 3).dropWhile(i -> true)).isEqualTo(Seq.of());
    }

    // take

    @Test
    public void shouldTakeEmptyFoldable() {
        final Seq<?> seq = Seq.of();
        assertThat(seq.take(1)).isEqualTo(seq);
    }

    @Test
    public void shouldTakeOneOfNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.take(1)).isEqualTo(Seq.of(1));
    }

    @Test
    public void shouldTakeMoreThanContained() {
        final Seq<?> seq = Seq.of(1, 2);
        assertThat(seq.take(3)).isEqualTo(Seq.of(1, 2));
    }

    @Test
    public void shouldTakeNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.take(1)).isEqualTo(Seq.of(1));
    }

    @Test
    public void shouldTakeZeroElements() {
        assertThat(Seq.of(1).take(0)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldTakeNegativeCount() {
        assertThat(Seq.of(1).take(-1)).isEqualTo(Seq.of());
    }

    // takeRight

    @Test
    public void shouldTakeRightEmptyFoldable() {
        final Seq<?> seq = Seq.of();
        assertThat(seq.takeRight(1)).isEqualTo(seq);
    }

    @Test
    public void shouldTakeRightOneOfNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1,2);
        assertThat(seq.takeRight(1)).isEqualTo(Seq.of(2));
    }

    @Test
    public void shouldTakeRightMoreThanContained() {
        final Seq<?> seq = Seq.of(1,2);
        assertThat(seq.takeRight(3)).isEqualTo(Seq.of(1, 2));
    }

    @Test
    public void shouldTakeRightNonEmptyFoldable() {
        final Seq<?> seq = Seq.of(1);
        assertThat(seq.takeRight(1)).isEqualTo(Seq.of(1));
    }

    @Test
    public void shouldTakeRightZeroElements() {
        assertThat(Seq.of(1).takeRight(0)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldTakeRightNegativeCount() {
        assertThat(Seq.of(1).takeRight(-1)).isEqualTo(Seq.of());
    }

    // takeWhile

    @Test
    public void shouldTakeWhileEmptyFoldable() {
        assertThat(Seq.<Integer> of().takeWhile(i -> i <= 2)).isEqualTo(Seq.of());
    }

    @Test
    public void shouldTakeWhileNonEmptyFoldable() {
        assertThat(Seq.of(1, 2, 3).takeWhile(i -> i <= 2)).isEqualTo(Seq.of(1, 2));
    }

    @Test
    public void shouldTakeWhileWhenAlwaysTrue() {
        assertThat(Seq.of(1, 2, 3).takeWhile(i -> true)).isEqualTo(Seq.of(1, 2, 3));
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

        @SuppressWarnings("unchecked")
        @Override
        public <U, SEQ extends Manifest<U, Seq<?>>> Seq<U> flatMap(Function<? super T, SEQ> mapper) {
            return (Seq) Foldable.super.flatMap(mapper::apply);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Seq<U> map(Function<? super T, ? extends U> mapper) {
            return (Seq) Foldable.super.map(mapper::apply);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Seq<Tuple.Tuple2<T, U>> zip(Iterable<U> that) {
            return (Seq) Foldable.super.zip(that);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Seq<Tuple.Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
            return (Seq) Foldable.super.zipAll(that, thisElem, thatElem);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Seq<Tuple.Tuple2<T, Integer>> zipWithIndex() {
            return (Seq) Foldable.super.zipWithIndex();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T1, T2> Tuple.Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super T, Tuple.Tuple2<T1, T2>> unzipper) {
            return (Tuple.Tuple2<Seq<T1>, Seq<T2>>) Foldable.super.unzip(unzipper);
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
