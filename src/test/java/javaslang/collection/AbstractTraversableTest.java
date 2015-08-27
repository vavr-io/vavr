/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests all methods defined in {@link javaslang.collection.Traversable}.
 */
public abstract class AbstractTraversableTest extends AbstractValueTest {

    abstract protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector();

    abstract protected <T> Traversable<T> empty();

    abstract protected <T> Traversable<T> of(T element);

    @SuppressWarnings("unchecked")
    abstract protected <T> Traversable<T> of(T... elements);

    abstract protected <T> Traversable<T> ofAll(java.lang.Iterable<? extends T> elements);

    abstract protected Traversable<Boolean> ofAll(boolean[] array);

    abstract protected Traversable<Byte> ofAll(byte[] array);

    abstract protected Traversable<Character> ofAll(char[] array);

    abstract protected Traversable<Double> ofAll(double[] array);

    abstract protected Traversable<Float> ofAll(float[] array);

    abstract protected Traversable<Integer> ofAll(int[] array);

    abstract protected Traversable<Long> ofAll(long[] array);

    abstract protected Traversable<Short> ofAll(short[] array);

    // -- average

    @Test
    public void shouldReturnNoneWhenComputingAverageOfNil() {
        assertThat(empty().average()).isEqualTo(None.instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingAverageOfStrings() {
        of("1", "2", "3").average();
    }

    @Test
    public void shouldComputeAverageOfByte() {
        assertThat(of((byte) 1, (byte) 2).average().get()).isEqualTo(1.5);
    }

    @Test
    public void shouldComputeAverageOfDouble() {
        assertThat(of(.1, .2, .3).average().get()).isEqualTo(.2, within(10e-17));
    }

    @Test
    public void shouldComputeAverageOfFloat() {
        assertThat(of(.1f, .2f, .3f).average().get()).isEqualTo(.2, within(10e-9));
    }

    @Test
    public void shouldComputeAverageOfInt() {
        assertThat(of(1, 2, 3).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfLong() {
        assertThat(of(1L, 2L, 3L).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).average().get()).isEqualTo(.5);
    }

    @Test
    public void shouldComputeAverageOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).average().get()).isEqualTo(.5);
    }

    // -- clear

    @Test

    public void shouldClearNil() {
        assertThat(empty().clear()).isEqualTo(empty());
    }

    @Test
    public void shouldClearNonNil() {
        assertThat(of(1, 2, 3).clear()).isEqualTo(empty());
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = empty().contains(null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = of(1, 2, 3).contains(0);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = of(1, 2, 3).contains(2);
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = empty().containsAll(of(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = of(1, 2, 3).containsAll(of(2, 3, 4));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = of(1, 2, 3).containsAll(of(1, 2, 3));
        assertThat(actual).isTrue();
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptyTraversable() {
        assertThat(empty().distinct()).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        assertThat(of(1, 1, 2, 2, 3, 3).distinct()).isEqualTo(of(1, 2, 3));
    }

    // -- distinct(Comparator)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingComparator() {
        final Comparator<Integer> comparator = (i1, i2) -> i1 - i2;
        assertThat(this.<Integer> empty().distinctBy(comparator)).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        final Comparator<String> comparator = (s1, s2) -> ((int) s1.charAt(1)) - ((int) s2.charAt(1));
        assertThat(of("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(comparator)).isEqualTo(of("1a", "3b", "5c"));
    }

    // -- distinct(Function)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingKeyExtractor() {
        assertThat(empty().distinctBy(Function.identity())).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        assertThat(of("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(c -> c.charAt(1))).isEqualTo(of("1a", "3b", "5c"));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(empty().drop(1)).isEqualTo(empty());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.drop(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).drop(2)).isEqualTo(of(3));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(of(1, 2, 3).drop(4)).isEqualTo(empty());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(empty().dropRight(1)).isEqualTo(empty());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.dropRight(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).dropRight(2)).isEqualTo(of(1));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(of(1, 2, 3).dropRight(4)).isEqualTo(empty());
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(empty().dropWhile(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.dropWhile(ignored -> false)).isSameAs(t);
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(of(1, 2, 3).dropWhile(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(of(1, 2, 3).dropWhile(i -> i < 2)).isEqualTo(of(2, 3));
    }


    // -- existsUnique

    @Test
    public void shouldBeAwareOfExistingUniqueElement() {
        assertThat(of(1, 2).existsUnique(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingUniqueElement() {
        assertThat(this.<Integer> empty().existsUnique(i -> i == 1)).isFalse();
    }

    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        assertThat(of(1, 1, 2).existsUnique(i -> i == 1)).isFalse();
    }

    // -- filter

    @Test
    public void shouldFilterEmptyTraversable() {
        assertThat(empty().filter(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldFilterNonEmptyTraversable() {
        assertThat(of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(of(2, 4));
    }

    @Test
    public void shouldFilterNonEmptyTraversableAllMatch() {
        final Traversable<Integer> t = of(1, 2, 3, 4);
        if(isThisLazyCollection()) {
            assertThat(t.filter(i -> true)).isEqualTo(t);
        } else {
            assertThat(t.filter(i -> true)).isSameAs(t);
        }
    }

    // -- findFirst

    @Test
    public void shouldFindFirstOfNil() {
        assertThat(empty().findFirst(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindFirstOfNonNil() {
        assertThat(of(1, 2, 3, 4).findFirst(i -> i % 2 == 0)).isEqualTo(Option.of(2));
    }

    // -- findLast

    @Test
    public void shouldFindLastOfNil() {
        assertThat(empty().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastOfNonNil() {
        assertThat(of(1, 2, 3, 4).findLast(i -> i % 2 == 0)).isEqualTo(Option.of(4));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptyTraversable() {
        assertThat(empty().flatMap(this::of)).isEqualTo(empty());
    }

    @Test
    public void shouldFlatMapNonEmptyTraversable() {
        assertThat(of(1, 2, 3).flatMap(this::of)).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldFlatMapTraversableByExpandingElements() {
        assertThat(of(1, 2, 3).flatMap(i -> {
            if (i == 1) {
                return of(1, 2, 3);
            } else if (i == 2) {
                return of(4, 5);
            } else {
                return of(6);
            }
        })).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldFlatMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Traversable<Integer> actualInts = of(0, 1, 2).flatMap(ignored -> of(seq.getAndIncrement(), seq.getAndIncrement()));
        final Traversable<Integer> expectedInts = of(0, 1, 2, 3, 4, 5);
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- flatten()

    @Test
    public void shouldFlattenEmptyTraversable() {
        assertThat(empty().flatten()).isEqualTo(empty());
    }

    @Test
    public void shouldFlattenTraversableOfPlainElements() {
        assertThat(of(1, 2, 3).flatten()).isEqualTo(of(1, 2, 3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlattenTraversableOfTraversables() {
        assertThat(of(of(1), of(2, 3)).flatten()).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldFlattenTraversableOfTraversablesAndPlainElements() {
        assertThat(of(1, of(of(2, 3), 4), 5).flatten()).isEqualTo(of(1, 2, 3, 4, 5));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlattenDifferentElementTypes() {
        assertThat(of(1, "2", of(3.1415, 1L)).flatten()).isEqualTo(of(1, "2", 3.1415, 1L));
    }

    // -- fold

    @Test
    public void shouldFoldNil() {
        assertThat(this.<String> empty().fold("", (a, b) -> a + b)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldNullOperator() {
        this.<String> empty().fold(null, null);
    }

    @Test
    public void shouldFoldNonNil() {
        assertThat(of(1, 2, 3).fold(0, (a, b) -> a + b)).isEqualTo(6);
    }

    // -- foldLeft

    @Test
    public void shouldFoldLeftNil() {
        assertThat(this.<String> empty().foldLeft("", (xs, x) -> xs + x)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldLeftNullOperator() {
        this.<String> empty().foldLeft(null, null);
    }

    @Test
    public void shouldFoldLeftNonNil() {
        assertThat(of("a", "b", "c").foldLeft("", (xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- foldRight

    @Test
    public void shouldFoldRightNil() {
        assertThat(this.<String> empty().foldRight("", (x, xs) -> x + xs)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldRightNullOperator() {
        this.<String> empty().foldRight(null, null);
    }

    @Test
    public void shouldFoldRightNonNil() {
        assertThat(of("a", "b", "c").foldRight("", (x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- head

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenHeadOnNil() {
        empty().head();
    }

    @Test
    public void shouldReturnHeadOfNonNil() {
        assertThat(of(1, 2, 3).head()).isEqualTo(1);
    }

    // -- headOption

    @Test
    public void shouldReturnNoneWhenCallingHeadOptionOnNil() {
        assertThat(empty().headOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeHeadWhenCallingHeadOptionOnNonNil() {
        assertThat(of(1, 2, 3).headOption()).isEqualTo(new Some<>(1));
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        empty().init();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(of(1, 2, 3).init()).isEqualTo(of(1, 2));
    }

    // -- initOption

    @Test
    public void shouldReturnNoneWhenCallingInitOptionOnNil() {
        assertThat(empty().initOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        assertThat(of(1, 2, 3).initOption()).isEqualTo(new Some<>(of(1, 2)));
    }

    // -- isEmpty

    @Test
    public void shouldRecognizeNil() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonNil() {
        assertThat(of(1).isEmpty()).isFalse();
    }

    // -- iterator

    @Test
    public void shouldNotHasNextWhenNilIterator() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnNextWhenNilIterator() {
        empty().iterator().next();
    }

    @Test
    public void shouldIterateFirstElementOfNonNil() {
        assertThat(of(1, 2, 3).iterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final Iterator<Integer> iterator = of(1, 2, 3).iterator();
        int actual;
        for (int i = 1; i <= 3; i++) {
            actual = iterator.next();
            assertThat(actual).isEqualTo(i);
        }
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- mkString()

    @Test
    public void shouldMkStringNil() {
        assertThat(empty().mkString()).isEqualTo("");
    }

    @Test
    public void shouldMkStringNonNil() {
        assertThat(of('a', 'b', 'c').mkString()).isEqualTo("abc");
    }

    // -- mkString(delimiter)

    @Test
    public void shouldMkStringWithDelimiterNil() {
        assertThat(empty().mkString(",")).isEqualTo("");
    }

    @Test
    public void shouldMkStringWithDelimiterNonNil() {
        assertThat(of('a', 'b', 'c').mkString(",")).isEqualTo("a,b,c");
    }

    // -- mkString(delimiter, prefix, suffix)

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkString(",", "[", "]")).isEqualTo("[]");
    }

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(of('a', 'b', 'c').mkString(",", "[", "]")).isEqualTo("[a,b,c]");
    }

    // -- last

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenLastOnNil() {
        empty().last();
    }

    @Test
    public void shouldReturnLastOfNonNil() {
        assertThat(of(1, 2, 3).last()).isEqualTo(3);
    }

    // -- lastOption

    @Test
    public void shouldReturnNoneWhenCallingLastOptionOnNil() {
        assertThat(empty().lastOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeLastWhenCallingLastOptionOnNonNil() {
        assertThat(of(1, 2, 3).lastOption()).isEqualTo(new Some<>(3));
    }

    // -- length

    @Test
    public void shouldComputeLengthOfNil() {
        assertThat(empty().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonNil() {
        assertThat(of(1, 2, 3).length()).isEqualTo(3);
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(this.<Integer> empty().map(i -> i + 1)).isEqualTo(empty());
    }

    @Test
    public void shouldMapNonNil() {
        assertThat(of(1, 2, 3).map(i -> i + 1)).isEqualTo(of(2, 3, 4));
    }

    @Test
    public void shouldMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Traversable<Integer> expectedInts = of(0, 1, 2, 3, 4);
        final Traversable<Integer> actualInts = expectedInts.map(ignored -> seq.getAndIncrement());
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- partition

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenPartitionNilAndPredicateIsNull() {
        empty().partition(null);
    }

    @Test
    public void shouldPartitionNil() {
        assertThat(empty().partition(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOddAndEventNumbers() {
        assertThat(of(1, 2, 3, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(of(1, 3), of(2, 4)));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
        assertThat(of(1, 3).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(of(1, 3), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        assertThat(of(2, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(empty(), of(2, 4)));
    }

    // -- max

    @Test
    public void shouldReturnNoneWhenComputingMaxOfNil() {
        assertThat(empty().max()).isEqualTo(None.instance());
    }

    @Test
    public void shouldComputeMaxOfStrings() {
        assertThat(of("1", "2", "3").max()).isEqualTo(new Some<>("3"));
    }

    @Test
    public void shouldComputeMaxOfBoolean() {
        assertThat(of(true, false).max()).isEqualTo(new Some<>(true));
    }

    @Test
    public void shouldComputeMaxOfByte() {
        assertThat(of((byte) 1, (byte) 2).max()).isEqualTo(new Some<>((byte) 2));
    }

    @Test
    public void shouldComputeMaxOfChar() {
        assertThat(of('a', 'b', 'c').max()).isEqualTo(new Some<>('c'));
    }

    @Test
    public void shouldComputeMaxOfDouble() {
        assertThat(of(.1, .2, .3).max()).isEqualTo(new Some<>(.3));
    }

    @Test
    public void shouldComputeMaxOfFloat() {
        assertThat(of(.1f, .2f, .3f).max()).isEqualTo(new Some<>(.3f));
    }

    @Test
    public void shouldComputeMaxOfInt() {
        assertThat(of(1, 2, 3).max()).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldComputeMaxOfLong() {
        assertThat(of(1L, 2L, 3L).max()).isEqualTo(new Some<>(3L));
    }

    @Test
    public void shouldComputeMaxOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).max()).isEqualTo(new Some<>((short) 3));
    }

    @Test
    public void shouldComputeMaxOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).max()).isEqualTo(new Some<>(BigInteger.ONE));
    }

    @Test
    public void shouldComputeMaxOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).max()).isEqualTo(new Some<>(BigDecimal.ONE));
    }

    // -- maxBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullComparator() {
        of(1).maxBy((Comparator<Integer>) null);
    }

    @Test
    public void shouldThrowWhenMaxByOfNil() {
        assertThat(empty().maxBy((o1, o2) -> 0)).isEqualTo(None.instance());
    }

    @Test
    public void shouldCalculateMaxByOfInts() {
        assertThat(of(1, 2, 3).maxBy((i1, i2) -> i1 - i2)).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldCalculateInverseMaxByOfInts() {
        assertThat(of(1, 2, 3).maxBy((i1, i2) -> i2 - i1)).isEqualTo(new Some<>(1));
    }

    // -- maxBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullFunction() {
        of(1).maxBy((Function<Integer, Integer>) null);
    }

    @Test
    public void shouldThrowWhenMaxByFunctionOfNil() {
        assertThat(this.<Integer> empty().maxBy(i -> i)).isEqualTo(None.instance());
    }

    @Test
    public void shouldCalculateMaxByFunctionOfInts() {
        assertThat(of(1, 2, 3).maxBy(i -> i)).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldCalculateInverseMaxByFunctionOfInts() {
        assertThat(of(1, 2, 3).maxBy(i -> -i)).isEqualTo(new Some<>(1));
    }

    // -- min

    @Test
    public void shouldReturnNoneWhenComputingMinOfNil() {
        assertThat(empty().min()).isEqualTo(None.instance());
    }

    @Test
    public void shouldComputeMinOfStrings() {
        assertThat(of("1", "2", "3").min()).isEqualTo(new Some<>("1"));
    }

    @Test
    public void shouldComputeMinOfBoolean() {
        assertThat(of(true, false).min()).isEqualTo(new Some<>(false));
    }

    @Test
    public void shouldComputeMinOfByte() {
        assertThat(of((byte) 1, (byte) 2).min()).isEqualTo(new Some<>((byte) 1));
    }

    @Test
    public void shouldComputeMinOfChar() {
        assertThat(of('a', 'b', 'c').min()).isEqualTo(new Some<>('a'));
    }

    @Test
    public void shouldComputeMinOfDouble() {
        assertThat(of(.1, .2, .3).min()).isEqualTo(new Some<>(.1));
    }

    @Test
    public void shouldComputeMinOfFloat() {
        assertThat(of(.1f, .2f, .3f).min()).isEqualTo(new Some<>(.1f));
    }

    @Test
    public void shouldComputeMinOfInt() {
        assertThat(of(1, 2, 3).min()).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldComputeMinOfLong() {
        assertThat(of(1L, 2L, 3L).min()).isEqualTo(new Some<>(1L));
    }

    @Test
    public void shouldComputeMinOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).min()).isEqualTo(new Some<>((short) 1));
    }

    @Test
    public void shouldComputeMinOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).min()).isEqualTo(new Some<>(BigInteger.ZERO));
    }

    @Test
    public void shouldComputeMinOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).min()).isEqualTo(new Some<>(BigDecimal.ZERO));
    }

    // -- minBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullComparator() {
        of(1).minBy((Comparator<Integer>) null);
    }

    @Test
    public void shouldThrowWhenMinByOfNil() {
        assertThat(empty().minBy((o1, o2) -> 0)).isEqualTo(None.instance());
    }

    @Test
    public void shouldCalculateMinByOfInts() {
        assertThat(of(1, 2, 3).minBy((i1, i2) -> i1 - i2)).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldCalculateInverseMinByOfInts() {
        assertThat(of(1, 2, 3).minBy((i1, i2) -> i2 - i1)).isEqualTo(new Some<>(3));
    }

    // -- minBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullFunction() {
        of(1).minBy((Function<Integer, Integer>) null);
    }

    @Test
    public void shouldThrowWhenMinByFunctionOfNil() {
        assertThat(this.<Integer> empty().minBy(i -> i)).isEqualTo(None.instance());
    }

    @Test
    public void shouldCalculateMinByFunctionOfInts() {
        assertThat(of(1, 2, 3).minBy(i -> i)).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldCalculateInverseMinByFunctionOfInts() {
        assertThat(of(1, 2, 3).minBy(i -> -i)).isEqualTo(new Some<>(3));
    }

    // -- peek

    @Test
    public void shouldPeekNonNilPerformingAnAction() {
        final int[] effect = { 0 };
        final Value<Integer> actual = of(1, 2, 3).peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of(1, 2, 3)); // traverses all elements in the lazy case
        assertThat(effect[0]).isEqualTo(getPeekNonNilPerformingAnAction());
    }

    // returns the peek result of the specific Traversable implementation
    abstract int getPeekNonNilPerformingAnAction();

    // -- product

    @Test
    public void shouldComputeProductOfNil() {
        assertThat(empty().product()).isEqualTo(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingProductOfStrings() {
        of("1", "2", "3").product();
    }

    @Test
    public void shouldComputeProductOfByte() {
        assertThat(of((byte) 1, (byte) 2).product()).isEqualTo(2);
    }

    @Test
    public void shouldComputeProductOfDouble() {
        assertThat(of(.1, .2, .3).product().doubleValue()).isEqualTo(.006, within(10e-18));
    }

    @Test
    public void shouldComputeProductOfFloat() {
        assertThat(of(.1f, .2f, .3f).product().doubleValue()).isEqualTo(.006, within(10e-10));
    }

    @Test
    public void shouldComputeProductOfInt() {
        assertThat(of(1, 2, 3).product()).isEqualTo(6);
    }

    @Test
    public void shouldComputeProductOfLong() {
        assertThat(of(1L, 2L, 3L).product()).isEqualTo(6L);
    }

    @Test
    public void shouldComputeProductOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).product()).isEqualTo(6);
    }

    @Test
    public void shouldComputeProductOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).product()).isEqualTo(0L);
    }

    @Test
    public void shouldComputeProductOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).product()).isEqualTo(0.0);
    }

    // -- reduce

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceNil() {
        this.<String> empty().reduce((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceNullOperator() {
        this.<String> empty().reduce(null);
    }

    @Test
    public void shouldReduceNonNil() {
        assertThat(of(1, 2, 3).reduce((a, b) -> a + b)).isEqualTo(6);
    }

    // -- reduceLeft

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceLeftNil() {
        this.<String> empty().reduceLeft((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceLeftNullOperator() {
        this.<String> empty().reduceLeft(null);
    }

    @Test
    public void shouldReduceLeftNonNil() {
        assertThat(of("a", "b", "c").reduceLeft((xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- reduceRight

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceRightNil() {
        this.<String> empty().reduceRight((a, b) -> a + b);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceRightNullOperator() {
        this.<String> empty().reduceRight(null);
    }

    @Test
    public void shouldReduceRightNonNil() {
        assertThat(of("a", "b", "c").reduceRight((x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- replace(curr, new)

    @Test
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replace(1, 2)).isEqualTo(empty());
    }

    @Test
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        assertThat(of(0, 1, 2, 1).replace(1, 3)).isEqualTo(of(0, 3, 2, 1));
    }

    // -- replaceAll(curr, new)

    @Test
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replaceAll(1, 2)).isEqualTo(empty());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(of(0, 1, 2, 1).replaceAll(1, 3)).isEqualTo(of(0, 3, 2, 3));
    }

    // -- replaceAll(UnaryOp)

    @Test
    public void shouldReplaceAllElementsOfNilUsingUnaryOp() {
        assertThat(this.<Integer> empty().replaceAll(i -> i + 1)).isEqualTo(empty());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingUnaryOp() {
        assertThat(of(1, 2, 3).replaceAll(i -> i + 1)).isEqualTo(of(2, 3, 4));
    }

    // -- retainAll

    @Test
    public void shouldRetainAllElementsFromNil() {
        assertThat(empty().retainAll(of(1, 2, 3))).isEqualTo(empty());
    }

    @Test
    public void shouldRetainAllExistingElementsFromNonNil() {
        assertThat(of(1, 2, 3, 1, 2, 3).retainAll(of(1, 2))).isEqualTo(of(1, 2, 1, 2));
    }

    @Test
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        assertThat(of(1, 2, 3).retainAll(of(4, 5))).isEqualTo(empty());
    }

    // -- sliding(size)

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNilByZeroSize() {
        empty().sliding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNilByNegativeSize() {
        empty().sliding(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNonNilByZeroSize() {
        of(1).sliding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNonNilByNegativeSize() {
        of(1).sliding(-1);
    }

    @Test
    public void shouldSlideNilBySize() {
        assertThat(empty().sliding(1).isEmpty()).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSlideNonNilBySize1() {
        assertThat(of(1, 2, 3).sliding(1).toList()).isEqualTo(List.of(Vector.of(1), Vector.of(2), Vector.of(3)));
    }

    @SuppressWarnings("unchecked")
    @Test // #201
    public void shouldSlideNonNilBySize2() {
        assertThat(of(1, 2, 3, 4, 5).sliding(2).toList()).isEqualTo(List.of(Vector.of(1, 2), Vector.of(2, 3), Vector.of(3, 4), Vector.of(4, 5)));
    }

    // -- sliding(size, step)

    @Test
    public void shouldSlideNilBySizeAndStep() {
        assertThat(empty().sliding(1, 1).isEmpty()).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSlide5ElementsBySize2AndStep3() {
        assertThat(of(1, 2, 3, 4, 5).sliding(2, 3).toList()).isEqualTo(List.of(Vector.of(1, 2), Vector.of(4, 5)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSlide5ElementsBySize2AndStep4() {
        assertThat(of(1, 2, 3, 4, 5).sliding(2, 4).toList()).isEqualTo(List.of(Vector.of(1, 2), Vector.of(5)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSlide5ElementsBySize2AndStep5() {
        assertThat(of(1, 2, 3, 4, 5).sliding(2, 5).toList()).isEqualTo(List.of(Vector.of(1, 2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSlide4ElementsBySize5AndStep3() {
        assertThat(of(1, 2, 3, 4).sliding(5, 3).toList()).isEqualTo(List.of(Vector.of(1, 2, 3, 4)));
    }

    // -- span

    @Test
    public void shouldSpanNil() {
        assertThat(this.<Integer> empty().span(i -> i < 2)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSpanNonNil() {
        assertThat(of(0, 1, 2, 3).span(i -> i < 2)).isEqualTo(Tuple.of(of(0, 1), of(2, 3)));
    }

    // -- spliterator

    @Test
    public void shouldSplitNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        this.<Integer> empty().spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Collections.emptyList());
    }

    @Test
    public void shouldSplitNonNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        of(1, 2, 3).spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Arrays.asList(1, 2, 3));
    }

    @Test
    public void shouldHaveImmutableSpliterator() {
        assertThat(of(1, 2, 3).spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
    }

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().characteristics() & Spliterator.SIZED).isNotZero();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(of(1, 2, 3).spliterator().getExactSizeIfKnown()).isEqualTo(3);
    }

    // -- stderr

    @Test
    public void shouldWriteToStderr() {
        of(1, 2, 3).stderr();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStderrIOException() {
        final PrintStream originalErr = System.err;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setErr(failingPrintStream);
            of(0).stderr();
        } finally {
            System.setErr(originalErr);
        }
    }

    // -- stdout

    @Test
    public void shouldWriteToStdout() {
        of(1, 2, 3).stdout();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStdoutIOException() {
        final PrintStream originalOut = System.out;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setOut(failingPrintStream);
            of(0).stdout();
        } finally {
            System.setOut(originalOut);
        }
    }

    // -- sum

    @Test
    public void shouldComputeSumOfNil() {
        assertThat(empty().sum()).isEqualTo(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingSumOfStrings() {
        of("1", "2", "3").sum();
    }

    @Test
    public void shouldComputeSumOfByte() {
        assertThat(of((byte) 1, (byte) 2).sum()).isEqualTo(3);
    }

    @Test
    public void shouldComputeSumOfDouble() {
        assertThat(of(.1, .2, .3).sum().doubleValue()).isEqualTo(.6, within(10e-16));
    }

    @Test
    public void shouldComputeSumOfFloat() {
        assertThat(of(.1f, .2f, .3f).sum().doubleValue()).isEqualTo(.6, within(10e-8));
    }

    @Test
    public void shouldComputeSumOfInt() {
        assertThat(of(1, 2, 3).sum()).isEqualTo(6);
    }

    @Test
    public void shouldComputeSumOfLong() {
        assertThat(of(1L, 2L, 3L).sum()).isEqualTo(6L);
    }

    @Test
    public void shouldComputeSumOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).sum()).isEqualTo(6);
    }

    @Test
    public void shouldComputeSumOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).sum()).isEqualTo(1L);
    }

    @Test
    public void shouldComputeSumOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).sum()).isEqualTo(1.0);
    }

    // -- take

    @Test
    public void shouldTakeNoneOnNil() {
        assertThat(empty().take(1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).take(-1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).take(2)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldTakeAllIfCountExceedsSize() {
        final Traversable<Integer> t = of(1, 2, 3);
        if(isThisLazyCollection()) {
            assertThat(t.take(4)).isEqualTo(t);
        } else {
            assertThat(t.take(4)).isSameAs(t);
        }
    }

    // -- takeRight

    @Test
    public void shouldTakeRightNoneOnNil() {
        assertThat(empty().takeRight(1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeRightNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).takeRight(-1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).takeRight(2)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldTakeRightAllIfCountExceedsSize() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.takeRight(4)).isSameAs(t);
    }

    // -- takeWhile

    @Test
    public void shouldTakeWhileNoneOnNil() {
        assertThat(empty().takeWhile(x -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(of(1, 2, 3).takeWhile(x -> false)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeWhileAllOnTrueCondition() {
        final Traversable<Integer> t = of(1, 2, 3);
        if(isThisLazyCollection()) {
            assertThat(t.takeWhile(x -> true)).isEqualTo(t);
        } else {
            assertThat(t.takeWhile(x -> true)).isSameAs(t);
        }
    }

    @Test
    public void shouldTakeWhileAsExpected() {
        assertThat(of(2, 4, 5, 6).takeWhile(x -> x % 2 == 0)).isEqualTo(of(2, 4));
    }

    // -- tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOnNil() {
        empty().tail();
    }

    @Test
    public void shouldReturnTailOfNonNil() {
        assertThat(of(1, 2, 3).tail()).isEqualTo(of(2, 3));
    }

    // -- tailOption

    @Test
    public void shouldReturnNoneWhenCallingTailOptionOnNil() {
        assertThat(empty().tailOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(of(1, 2, 3).tailOption()).isEqualTo(new Some<>(of(2, 3)));
    }

    // -- toJavaArray(Class)

    @Test
    public void shouldConvertNilToJavaArray() {
        final Integer[] actual = List.<Integer> empty().toJavaArray(Integer.class);
        final Integer[] expected = new Integer[] {};
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonNilToJavaArray() {
        final Integer[] array = of(1, 2).toJavaArray(Integer.class);
        final Integer[] expected = new Integer[] { 1, 2 };
        assertThat(array).isEqualTo(expected);
    }

    // -- toJavaList

    @Test
    public void shouldConvertNilToArrayList() {
        assertThat(this.<Integer> empty().toJavaList()).isEqualTo(new ArrayList<Integer>());
    }

    @Test
    public void shouldConvertNonNilToArrayList() {
        assertThat(of(1, 2, 3).toJavaList()).isEqualTo(Arrays.asList(1, 2, 3));
    }

    // -- toJavaMap(Function)

    @Test
    public void shouldConvertNilToHashMap() {
        assertThat(this.<Integer> empty().toJavaMap(x -> Tuple.of(x, x))).isEqualTo(new java.util.HashMap<>());
    }

    @Test
    public void shouldConvertNonNilToHashMap() {
        final java.util.Map<Integer, Integer> expected = new java.util.HashMap<>();
        expected.put(1, 1);
        expected.put(2, 2);
        assertThat(of(1, 2).toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
    }

    // -- toJavaSet

    @Test
    public void shouldConvertNilToHashSet() {
        assertThat(this.<Integer> empty().toJavaSet()).isEqualTo(new java.util.HashSet<>());
    }

    @Test
    public void shouldConvertNonNilToHashSet() {
        final java.util.Set<Integer> expected = new java.util.HashSet<>();
        expected.add(2);
        expected.add(1);
        expected.add(3);
        assertThat(of(1, 2, 2, 3).toJavaSet()).isEqualTo(expected);
    }

    // ++++++ OBJECT ++++++

    // -- equals

    @Test
    public void shouldEqualSameTraversableInstance() {
        final Traversable<?> traversable = empty();
        assertThat(traversable).isEqualTo(traversable);
    }

    @Test
    public void shouldNilNotEqualsNull() {
        assertThat(empty()).isNotNull();
    }

    @Test
    public void shouldNonNilNotEqualsNull() {
        assertThat(of(1)).isNotNull();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(empty()).isNotEqualTo("");
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(of(1)).isNotEqualTo("");
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(empty()).isEqualTo(empty());
    }

    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 3))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 4))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode() == empty().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(of(1, 2).hashCode() == of(1, 2).hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(of(1, 2).hashCode() != of(2, 3).hashCode()).isTrue();
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeNil() {
        final Object actual = deserialize(serialize(empty()));
        final Object expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(empty())) == empty();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(of(1, 2, 3)));
        final Object expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // helpers

    static PrintStream failingPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }

    abstract boolean isThisLazyCollection();
}
