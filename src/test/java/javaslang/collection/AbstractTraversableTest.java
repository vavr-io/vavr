/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.algebra.Monoid;
import javaslang.control.Option;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests all methods defined in {@link javaslang.collection.Traversable}.
 */
public abstract class AbstractTraversableTest {

    abstract protected <T> Traversable<T> nil();

    @SuppressWarnings("unchecked")
    abstract protected <T> Traversable<T> of(T... elements);

    // -- clear

    @Test
    public void shouldClearNil() {
        assertThat(this.nil().clear()).isEqualTo(this.nil());
    }

    @Test
    public void shouldClearNonNil() {
        assertThat(this.of(1, 2, 3).clear()).isEqualTo(this.nil());
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = this.nil().contains(null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = this.of(1, 2, 3).contains(0);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = this.of(1, 2, 3).contains(2);
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = this.nil().containsAll(this.of(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = this.of(1, 2, 3).containsAll(this.of(2, 3, 4));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = this.of(1, 2, 3).containsAll(this.of(1, 2, 3));
        assertThat(actual).isTrue();
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptyTraversable() {
        assertThat(this.nil().distinct()).isEqualTo(this.nil());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        assertThat(this.of(1, 1, 2, 2, 3, 3).distinct()).isEqualTo(this.of(1, 2, 3));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(this.nil().drop(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).drop(-1)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).drop(2)).isEqualTo(this.of(3));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).drop(4)).isEqualTo(this.nil());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(this.nil().dropRight(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).dropRight(-1)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).dropRight(2)).isEqualTo(this.of(1));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).dropRight(4)).isEqualTo(this.nil());
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(this.nil().dropWhile(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        assertThat(this.of(1, 2, 3).dropWhile(ignored -> false)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(this.of(1, 2, 3).dropWhile(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(this.of(1, 2, 3).dropWhile(i -> i < 2)).isEqualTo(this.of(2, 3));
    }

    // -- filter

    @Test
    public void shouldFilterEmptyTraversable() {
        assertThat(this.nil().filter(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldFilterNonEmptyTraversable() {
        assertThat(this.of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(this.of(2, 4));
    }

    // -- findAll

    @Test
    public void shouldFindAllOfNil() {
        assertThat(this.nil().findAll(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldFindAllOfNonNil() {
        assertThat(this.of(1, 2, 3, 4).findAll(i -> i % 2 == 0)).isEqualTo(this.of(2, 4));
    }

    // -- findFirst

    @Test
    public void shouldFindFirstOfNil() {
        assertThat(this.nil().findFirst(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindFirstOfNonNil() {
        assertThat(this.of(1, 2, 3, 4).findFirst(i -> i % 2 == 0)).isEqualTo(Option.of(2));
    }

    // -- findLast

    @Test
    public void shouldFindLastOfNil() {
        assertThat(this.nil().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastOfNonNil() {
        assertThat(this.of(1, 2, 3, 4).findLast(i -> i % 2 == 0)).isEqualTo(Option.of(4));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptyTraversable() {
        assertThat(this.nil().flatMap(this::of)).isEqualTo(this.nil());
    }

    @Test
    public void shouldFlatMapNonEmptyTraversable() {
        assertThat(this.of(1, 2, 3).flatMap(this::of)).isEqualTo(this.of(1, 2, 3));
    }

    // -- fold

    @Test
    public void shouldFoldNil() {
        assertThat(this.<String> nil().fold("", (a, b) -> a + b)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldNullOperator() {
        this.<String> nil().fold(null, null);
    }

    @Test
    public void shouldFoldNonNil() {
        assertThat(this.of(1, 2, 3).fold(0, (a, b) -> a + b)).isEqualTo(6);
    }

    // -- foldLeft

    @Test
    public void shouldFoldLeftNil() {
        assertThat(this.<String> nil().foldLeft("", (xs, x) -> xs + x)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldLeftNullOperator() {
        this.<String> nil().foldLeft(null, null);
    }

    @Test
    public void shouldFoldLeftNonNil() {
        assertThat(this.of("a", "b", "c").foldLeft("", (xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- foldMap

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldMapAndMonoidIsNull() {
        this.nil().foldMap(null, String::valueOf);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldMapAndMapperIsNull() {
        this.nil().foldMap(Monoid.endoMonoid(), null);
    }

    @Test
    public void shouldFoldMapNil() {
        this.nil().foldMap(Monoid.endoMonoid(), o -> Function.identity());
    }

    @Test
    public void shouldFoldMapNonNil() {
        class StringConcat implements Monoid<String> {

            @Override
            public String zero() {
                return "";
            }

            @Override
            public String combine(String a1, String a2) {
                return a1 + a2;
            }
        }
        final Monoid<String> monoid = new StringConcat();
        final String actual = this.of('a', 'b', 'c').foldMap(monoid, String::valueOf);
        assertThat(actual).isEqualTo("abc");
    }

    // -- foldRight

    @Test
    public void shouldFoldRightNil() {
        assertThat(this.<String> nil().foldRight("", (x, xs) -> x + xs)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldRightNullOperator() {
        this.<String> nil().foldRight(null, null);
    }

    @Test
    public void shouldFoldRightNonNil() {
        assertThat(this.of("a", "b", "c").foldRight("", (x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- head

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenHeadOnNil() {
        this.nil().head();
    }

    @Test
    public void shouldReturnHeadOfNonNil() {
        assertThat(this.of(1, 2, 3).head()).isEqualTo(1);
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        this.nil().init();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(this.of(1, 2, 3).init()).isEqualTo(this.of(1, 2));
    }

    // -- intersperse

    @Test
    public void shouldIntersperseNil() {
        assertThat(this.<Character> nil().intersperse(',')).isEqualTo(this.nil());
    }

    @Test
    public void shouldIntersperseSingleton() {
        assertThat(this.of('a').intersperse(',')).isEqualTo(this.of('a'));
    }

    @Test
    public void shouldIntersperseMultipleElements() {
        assertThat(this.of('a', 'b').intersperse(',')).isEqualTo(this.of('a', ',', 'b'));
    }

    // -- isEmpty

    @Test
    public void shouldRecognizeNil() {
        assertThat(this.nil().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonNil() {
        assertThat(this.of(1).isEmpty()).isFalse();
    }

    // -- iterator

    @Test
    public void shouldNotHasNextWhenNilIterator() {
        assertThat(this.nil().iterator().hasNext()).isFalse();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnNextWhenNilIterator() {
        this.nil().iterator().next();
    }

    @Test
    public void shouldIterateFirstElementOfNonNil() {
        assertThat(this.of(1, 2, 3).iterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final Iterator<Integer> iterator = this.of(1, 2, 3).iterator();
        int actual;
        for (int i = 1; i <= 3; i++) {
            actual = iterator.next();
            assertThat(actual).isEqualTo(i);
        }
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- join()

    @Test
    public void shouldJoinNil() {
        assertThat(this.nil().join()).isEqualTo("");
    }

    @Test
    public void shouldJoinNonNil() {
        assertThat(this.of('a', 'b', 'c').join()).isEqualTo("abc");
    }

    // -- join(delimiter)

    @Test
    public void shouldJoinWithDelimiterNil() {
        assertThat(this.nil().join(",")).isEqualTo("");
    }

    @Test
    public void shouldJoinWithDelimiterNonNil() {
        assertThat(this.of('a', 'b', 'c').join(",")).isEqualTo("a,b,c");
    }

    // -- join(delimiter, prefix, suffix)

    @Test
    public void shouldJoinWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(this.nil().join(",", "[", "]")).isEqualTo("[]");
    }

    @Test
    public void shouldJoinWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(this.of('a', 'b', 'c').join(",", "[", "]")).isEqualTo("[a,b,c]");
    }

    // -- last

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLastOnNil() {
        this.nil().last();
    }

    @Test
    public void shouldReturnLastOfNonNil() {
        assertThat(this.of(1, 2, 3).last()).isEqualTo(3);
    }

    // -- length

    @Test
    public void shouldComputeLengthOfNil() {
        assertThat(this.nil().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonNil() {
        assertThat(this.of(1, 2, 3).length()).isEqualTo(3);
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(this.<Integer> nil().map(i -> i + 1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldMapNonNil() {
        assertThat(this.of(1, 2, 3).map(i -> i + 1)).isEqualTo(this.of(2, 3, 4));
    }

    // -- product

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotGetProductOfNil() {
        nil().product();
    }

    @Test
    public void shouldGetProductOfNonNil() {
        assertThat(of(2, 3).product()).isEqualTo(6);
    }

    // -- reduce

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceNil() {
        this.<String> nil().reduce((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceNullOperator() {
        this.<String> nil().reduce(null);
    }

    @Test
    public void shouldReduceNonNil() {
        assertThat(this.of(1, 2, 3).reduce((a, b) -> a + b)).isEqualTo(6);
    }

    // -- reduceLeft

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceLeftNil() {
        this.<String> nil().reduceLeft((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceLeftNullOperator() {
        this.<String> nil().reduceLeft(null);
    }

    @Test
    public void shouldReduceLeftNonNil() {
        assertThat(this.of("a", "b", "c").reduceLeft((xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- reduceRight

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenReduceRightNil() {
        this.<String> nil().reduceRight((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceRightNullOperator() {
        this.<String> nil().reduceRight(null);
    }

    @Test
    public void shouldReduceRightNonNil() {
        assertThat(this.of("a", "b", "c").reduceRight((x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- remove

    @Test
    public void shouldRemoveElementFromNil() {
        assertThat(this.nil().remove(null)).isEqualTo(this.nil());
    }

    @Test
    public void shouldRemoveFirstElement() {
        assertThat(this.of(1, 2, 3).remove(1)).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldRemoveLastElement() {
        assertThat(this.of(1, 2, 3).remove(3)).isEqualTo(this.of(1, 2));
    }

    @Test
    public void shouldRemoveInnerElement() {
        assertThat(this.of(1, 2, 3).remove(2)).isEqualTo(this.of(1, 3));
    }

    @Test
    public void shouldRemoveNonExistingElement() {
        assertThat(this.of(1, 2, 3).remove(4)).isEqualTo(this.of(1, 2, 3));
    }

    // -- removeAll(Iterable)

    @Test
    public void shouldRemoveAllElementsFromNil() {
        assertThat(this.nil().removeAll(this.of(1, 2, 3))).isEqualTo(this.nil());
    }

    @Test
    public void shouldRemoveAllExistingElementsFromNonNil() {
        assertThat(this.of(1, 2, 3, 1, 2, 3).removeAll(this.of(1, 2))).isEqualTo(this.of(3, 3));
    }

    @Test
    public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
        assertThat(this.of(1, 2, 3).removeAll(this.of(4, 5))).isEqualTo(this.of(1, 2, 3));
    }

    // -- removeAll(Object)

    @Test
    public void shouldRemoveAllObjectsFromNil() {
        assertThat(this.nil().removeAll(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldRemoveAllExistingObjectsFromNonNil() {
        assertThat(this.of(1, 2, 3, 1, 2, 3).removeAll(1)).isEqualTo(this.of(2, 3, 2, 3));
    }

    @Test
    public void shouldNotRemoveAllNonObjectsElementsFromNonNil() {
        assertThat(this.of(1, 2, 3).removeAll(4)).isEqualTo(this.of(1, 2, 3));
    }

    // -- replace(curr, new)

    @Test
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(this.<Integer> nil().replace(1, 2)).isEqualTo(this.nil());
    }

    @Test
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        assertThat(this.of(0, 1, 2, 1).replace(1, 3)).isEqualTo(this.of(0, 3, 2, 1));
    }

    // -- replaceAll(curr, new)

    @Test
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(this.<Integer> nil().replaceAll(1, 2)).isEqualTo(this.nil());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(this.of(0, 1, 2, 1).replaceAll(1, 3)).isEqualTo(this.of(0, 3, 2, 3));
    }

    // -- replaceAll(UnaryOp)

    @Test
    public void shouldReplaceAllElementsOfNilUsingUnaryOp() {
        assertThat(this.<Integer> nil().replaceAll(i -> i + 1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingUnaryOp() {
        assertThat(this.of(1, 2, 3).replaceAll(i -> i + 1)).isEqualTo(this.of(2, 3, 4));
    }

    // -- retainAll

    @Test
    public void shouldRetainAllElementsFromNil() {
        assertThat(this.nil().retainAll(this.of(1, 2, 3))).isEqualTo(this.nil());
    }

    @Test
    public void shouldRetainAllExistingElementsFromNonNil() {
        assertThat(this.of(1, 2, 3, 1, 2, 3).retainAll(this.of(1, 2))).isEqualTo(this.of(1, 2, 1, 2));
    }

    @Test
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        assertThat(this.of(1, 2, 3).retainAll(this.of(4, 5))).isEqualTo(this.nil());
    }

    // -- reverse

    @Test
    public void shouldReverseNil() {
        assertThat(this.nil().reverse()).isEqualTo(this.nil());
    }

    @Test
    public void shouldReverseNonNil() {
        assertThat(this.of(1, 2, 3).reverse()).isEqualTo(this.of(3, 2, 1));
    }

    // -- span

    @Test
    public void shouldSpanNil() {
        assertThat(this.<Integer> nil().span(i -> i < 2)).isEqualTo(Tuple.of(this.nil(), this.nil()));
    }

    @Test
    public void shouldSpanNonNil() {
        assertThat(this.of(0, 1, 2, 3).span(i -> i < 2)).isEqualTo(Tuple.of(this.of(0, 1), this.of(2, 3)));
    }

    // -- spliterator

    @Test
    public void shouldSplitNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        this.<Integer> nil().spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Arrays.asList());
    }

    @Test
    public void shouldSplitNonNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        this.of(1, 2, 3).spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Arrays.asList(1, 2, 3));
    }

    @Test
    public void shouldHaveImmutableSpliterator() {
        assertThat(this.of(1, 2, 3).spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(this.of(1, 2, 3).spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
    }

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(this.of(1, 2, 3).spliterator().characteristics() & Spliterator.SIZED).isNotZero();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(this.of(1, 2, 3).spliterator().getExactSizeIfKnown()).isEqualTo(3);
    }

    // -- stderr

    @Test
    public void shouldWriteToStderr() {
        of(1, 2, 3).stderr();
    }

    // -- stdout

    @Test
    public void shouldWriteToStdout() {
        of(1, 2, 3).stdout();
    }

    // -- sum

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSumNil() {
        nil().sum();
    }

    @Test
    public void shouldSumNonNil() {
        assertThat(of(2, 3).sum()).isEqualTo(5);
    }

    // -- take

    @Test
    public void shouldTakeNoneOnNil() {
        assertThat(this.nil().take(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).take(-1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).take(2)).isEqualTo(this.of(1, 2));
    }

    @Test
    public void shouldTakeAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).take(4)).isEqualTo(this.of(1, 2, 3));
    }

    // -- takeRight

    @Test
    public void shouldTakeRightNoneOnNil() {
        assertThat(this.nil().takeRight(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeRightNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).takeRight(-1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).takeRight(2)).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldTakeRightAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).takeRight(4)).isEqualTo(this.of(1, 2, 3));
    }

    // -- takeWhile

    @Test
    public void shouldTakeWhileNoneOnNil() {
        assertThat(this.nil().takeWhile(x -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(this.of(1, 2, 3).takeWhile(x -> false)).isEqualTo(this.nil());
    }

    @Test
    public void shouldTakeWhileAllOnTrueCondition() {
        assertThat(this.of(1, 2, 3).takeWhile(x -> true)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldTakeWhileAsExpected() {
        assertThat(this.of(2, 4, 5, 6).takeWhile(x -> x % 2 == 0)).isEqualTo(this.of(2, 4));
    }

    // -- tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOnNil() {
        this.nil().tail();
    }

    @Test
    public void shouldReturnTailOfNonNil() {
        final Traversable<Integer> actual = this.of(1, 2, 3).tail();
        final Traversable<Integer> expected = this.of(2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- toJavaArray(Class)

    @Test
    public void shouldConvertNilToJavaArray() {
        final Integer[] actual = List.<Integer> nil().toJavaArray(Integer.class);
        final Integer[] expected = new Integer[] {};
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonNilToJavaArray() {
        final Integer[] array = List.of(1, 2).toJavaArray(Integer.class);
        final Integer[] expected = new Integer[] { 1, 2 };
        assertThat(array).isEqualTo(expected);
    }

    // -- toJavaList

    @Test
    public void shouldConvertNilToArrayList() {
        assertThat(this.<Integer> nil().toJavaList()).isEqualTo(new ArrayList<Integer>());
    }

    @Test
    public void shouldConvertNonNilToArrayList() {
        assertThat(this.of(1, 2, 3).toJavaList()).isEqualTo(Arrays.asList(1, 2, 3));
    }

    // -- toJavaMap(Function)

    @Test
    public void shouldConvertNilToHashMap() {
        assertThat(this.<Integer> nil().toJavaMap(x -> Tuple.of(x, x))).isEqualTo(new HashMap<>());
    }

    @Test
    public void shouldConvertNonNilToHashMap() {
        final java.util.Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 1);
        expected.put(2, 2);
        assertThat(this.of(1, 2).toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
    }

    // -- toJavaSet

    @Test
    public void shouldConvertNilToHashSet() {
        assertThat(this.<Integer> nil().toJavaMap(x -> Tuple.of(x, x))).isEqualTo(new HashMap<>());
    }

    @Test
    public void shouldConvertNonNilToHashSet() {
        final java.util.Set<Integer> expected = new HashSet<>();
        expected.add(2);
        expected.add(1);
        expected.add(3);
        assertThat(this.of(1, 2, 2, 3).toJavaSet()).isEqualTo(expected);
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(this.nil().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(this.nil(), this.nil()));
    }

    @Test
    public void shouldUnzipNonNil() {
        final Tuple actual = this.of(0, 1).unzip(i -> Tuple.of(i, (char) ((short) 'a' + i)));
        final Tuple expected = Tuple.of(this.of(0, 1), this.<Character> of('a', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @Test
    public void shouldZipNils() {
        final Traversable<?> actual = this.nil().zip(this.nil());
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Traversable<?> actual = this.nil().zip(this.of(1));
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Traversable<?> actual = this.of(1).zip(this.nil());
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2).zip(this.of("a", "b", "c"));
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2, 3).zip(this.of("a", "b"));
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2, 3).zip(this.of("a", "b", "c"));
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipWithThatIsNull() {
        this.nil().zip(null);
    }

    // -- zipAll

    @Test
    public void shouldZipAllNils() {
        final Traversable<?> actual = this.nil().zipAll(this.nil(), null, null);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        final Traversable<?> actual = this.nil().zipAll(this.of(1), null, null);
        assertThat(actual).isEqualTo(this.of(Tuple.of(null, 1)));
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Traversable<?> actual = this.of(1).zipAll(this.nil(), null, null);
        assertThat(actual).isEqualTo(this.of(Tuple.of(1, null)));
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2).zipAll(this.of("a", "b", "c"), 9, "z");
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2, 3).zipAll(this.of("a", "b"), 9, "z");
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final Traversable<Tuple2<Integer, String>> actual = this.of(1, 2, 3).zipAll(this.of("a", "b", "c"), 9, "z");
        final Traversable<Tuple2<Integer, String>> expected = this.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        this.nil().zipAll(null, null, null);
    }

    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(this.<String> nil().zipWithIndex()).isEqualTo(this.<Tuple2<String, Integer>> nil());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final Traversable<Tuple2<String, Integer>> actual = this.of("a", "b", "c").zipWithIndex();
        final Traversable<Tuple2<String, Integer>> expected = this.of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
    }

    // ++++++ OBJECT ++++++

    // -- equals

    @Test
    public void shouldEqualSameTraversableInstance() {
        final Traversable<?> traversable = this.nil();
        assertThat(traversable).isEqualTo(traversable);
    }

    @Test
    public void shouldNilNotEqualsNull() {
        assertThat(this.nil()).isNotNull();
    }

    @Test
    public void shouldNonNilNotEqualsNull() {
        assertThat(this.of(1)).isNotNull();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(this.nil()).isNotEqualTo("");
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(this.of(1)).isNotEqualTo("");
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(this.nil()).isEqualTo(this.nil());
    }

    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(this.of(1, 2, 3).equals(this.of(1, 2, 3))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(this.of(1, 2, 3).equals(this.of(1, 2, 4))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(this.of(1, 2, 3).equals(this.of(1, 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(this.nil().hashCode() == this.nil().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(this.of(1, 2).hashCode() == this.of(1, 2).hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(this.of(1, 2).hashCode() != this.of(2, 3).hashCode()).isTrue();
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeNil() {
        final Object actual = deserialize(serialize(this.nil()));
        final Object expected = this.nil();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(this.nil())) == this.nil();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(this.of(1, 2, 3)));
        final Object expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }
}
