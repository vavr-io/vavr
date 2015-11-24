/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;

public abstract class AbstractTraversableTest extends AbstractValueTest {

    protected boolean isTraversableAgain() {
        return true;
    }

    abstract protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector();

    @Override
    abstract protected <T> Traversable<T> empty();

    @Override
    abstract protected <T> Traversable<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Traversable<T> ofAll(T... elements);

    abstract protected <T> Traversable<T> ofAll(java.lang.Iterable<? extends T> elements);

    abstract protected Traversable<Boolean> ofAll(boolean[] array);

    abstract protected Traversable<Byte> ofAll(byte[] array);

    abstract protected Traversable<Character> ofAll(char[] array);

    abstract protected Traversable<Double> ofAll(double[] array);

    abstract protected Traversable<Float> ofAll(float[] array);

    abstract protected Traversable<Integer> ofAll(int[] array);

    abstract protected Traversable<Long> ofAll(long[] array);

    abstract protected Traversable<Short> ofAll(short[] array);

    // -- static empty()

    @Test
    public void shouldCreateNil() {
        final Traversable<?> actual = empty();
        assertThat(actual.length()).isEqualTo(0);
    }

    // -- static ofAll()

    @Test
    public void shouldCreateSeqOfSeqUsingCons() {
        final List<List<Object>> actual = of(List.empty()).toList();
        assertThat(actual).isEqualTo(List.of(List.empty()));
    }

    // -- static ofAll(T...)

    @Test
    public void shouldCreateInstanceOfElements() {
        final List<Integer> actual = ofAll(1, 2).toList();
        assertThat(actual).isEqualTo(List.ofAll(1, 2));
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2);
        final List<Integer> actual = ofAll(arrayList).toList();
        assertThat(actual).isEqualTo(List.ofAll(1, 2));
    }

    // -- static ofAll(<primitive array>)

    @Test
    public void shouldCreateListOfPrimitiveBooleanArray() {
        final Traversable<Boolean> actual = ofAll(new boolean[] { true, false });
        final Traversable<Boolean> expected = ofAll(true, false);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveByteArray() {
        final Traversable<Byte> actual = ofAll(new byte[] { 1, 2, 3 });
        final Traversable<Byte> expected = ofAll((byte) 1, (byte) 2, (byte) 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveCharArray() {
        final Traversable<Character> actual = ofAll(new char[] { 'a', 'b', 'c' });
        final Traversable<Character> expected = ofAll('a', 'b', 'c');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveDoubleArray() {
        final Traversable<Double> actual = ofAll(new double[] { 1d, 2d, 3d });
        final Traversable<Double> expected = ofAll(1d, 2d, 3d);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveFloatArray() {
        final Traversable<Float> actual = ofAll(new float[] { 1f, 2f, 3f });
        final Traversable<Float> expected = ofAll(1f, 2f, 3f);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveIntArray() {
        final Traversable<Integer> actual = ofAll(new int[] { 1, 2, 3 });
        final Traversable<Integer> expected = ofAll(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveLongArray() {
        final Traversable<Long> actual = ofAll(new long[] { 1L, 2L, 3L });
        final Traversable<Long> expected = ofAll(1L, 2L, 3L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveShortArray() {
        final Traversable<Short> actual = ofAll(new short[] { (short) 1, (short) 2, (short) 3 });
        final Traversable<Short> expected = ofAll((short) 1, (short) 2, (short) 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- average

    @Test
    public void shouldReturnNoneWhenComputingAverageOfNil() {
        assertThat(empty().average()).isEqualTo(None.instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingAverageOfStrings() {
        ofAll("1", "2", "3").average();
    }

    @Test
    public void shouldComputeAverageOfByte() {
        assertThat(ofAll((byte) 1, (byte) 2).average().get()).isEqualTo(1.5);
    }

    @Test
    public void shouldComputeAverageOfDouble() {
        assertThat(ofAll(.1, .2, .3).average().get()).isEqualTo(.2, within(10e-17));
    }

    @Test
    public void shouldComputeAverageOfFloat() {
        assertThat(ofAll(.1f, .2f, .3f).average().get()).isEqualTo(.2, within(10e-9));
    }

    @Test
    public void shouldComputeAverageOfInt() {
        assertThat(ofAll(1, 2, 3).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfLong() {
        assertThat(ofAll(1L, 2L, 3L).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfShort() {
        assertThat(ofAll((short) 1, (short) 2, (short) 3).average().get()).isEqualTo(2);
    }

    @Test
    public void shouldComputeAverageOfBigInteger() {
        assertThat(ofAll(BigInteger.ZERO, BigInteger.ONE).average().get()).isEqualTo(.5);
    }

    @Test
    public void shouldComputeAverageOfBigDecimal() {
        assertThat(ofAll(BigDecimal.ZERO, BigDecimal.ONE).average().get()).isEqualTo(.5);
    }

    // -- clear

    @Test

    public void shouldClearNil() {
        assertThat(empty().clear()).isEqualTo(empty());
    }

    @Test
    public void shouldClearNonNil() {
        assertThat(ofAll(1, 2, 3).clear()).isEmpty();
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = empty().contains(null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = ofAll(1, 2, 3).contains(0);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = ofAll(1, 2, 3).contains(2);
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = empty().containsAll(ofAll(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = ofAll(1, 2, 3).containsAll(ofAll(2, 3, 4));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = ofAll(1, 2, 3).containsAll(ofAll(1, 2, 3));
        assertThat(actual).isTrue();
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptyTraversable() {
        assertThat(empty().distinct()).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        assertThat(ofAll(1, 1, 2, 2, 3, 3).distinct()).isEqualTo(ofAll(1, 2, 3));
    }

    // -- distinct(Comparator)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingComparator() {
        final Comparator<Integer> comparator = (i1, i2) -> i1 - i2;
        assertThat(this.<Integer> empty().distinctBy(comparator)).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        final Comparator<String> comparator = (s1, s2) -> (s1.charAt(1)) - (s2.charAt(1));
        assertThat(ofAll("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(comparator)).isEqualTo(ofAll("1a", "3b", "5c"));
    }

    // -- distinct(Function)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingKeyExtractor() {
        assertThat(empty().distinctBy(Function.identity())).isEqualTo(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        assertThat(ofAll("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(c -> c.charAt(1)))
                .isEqualTo(ofAll("1a", "3b", "5c"));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(empty().drop(1)).isEqualTo(empty());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        final Traversable<Integer> t = ofAll(1, 2, 3);
        assertThat(t.drop(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(ofAll(1, 2, 3).drop(2)).isEqualTo(of(3));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(ofAll(1, 2, 3).drop(4)).isEqualTo(empty());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(empty().dropRight(1)).isEqualTo(empty());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        final Traversable<Integer> t = ofAll(1, 2, 3);
        assertThat(t.dropRight(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(ofAll(1, 2, 3).dropRight(2)).isEqualTo(of(1));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(ofAll(1, 2, 3).dropRight(4)).isEqualTo(empty());
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(empty().dropWhile(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(ofAll(1, 2, 3).dropWhile(ignored -> false)).isEqualTo(ofAll(1, 2, 3));
        } else {
            Traversable<Integer> t = ofAll(1, 2, 3);
            assertThat(t.dropWhile(ignored -> false)).isSameAs(t);
        }
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(ofAll(1, 2, 3).dropWhile(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(ofAll(1, 2, 3).dropWhile(i -> i < 2)).isEqualTo(ofAll(2, 3));
    }

    // -- existsUnique

    @Test
    public void shouldBeAwareOfExistingUniqueElement() {
        assertThat(ofAll(1, 2).existsUnique(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingUniqueElement() {
        assertThat(this.<Integer> empty().existsUnique(i -> i == 1)).isFalse();
    }

    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        assertThat(ofAll(1, 1, 2).existsUnique(i -> i == 1)).isFalse();
    }

    // -- findFirst

    @Test
    public void shouldFindFirstOfNil() {
        assertThat(empty().findFirst(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindFirstOfNonNil() {
        assertThat(ofAll(1, 2, 3, 4).findFirst(i -> i % 2 == 0)).isEqualTo(Option.of(2));
    }

    // -- findLast

    @Test
    public void shouldFindLastOfNil() {
        assertThat(empty().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastOfNonNil() {
        assertThat(ofAll(1, 2, 3, 4).findLast(i -> i % 2 == 0)).isEqualTo(Option.of(4));
    }

    // -- fold

    @Test
    public void shouldFoldMultipleElements() {
        assertThat(ofAll(1, 2, 3).fold(0, (a, b) -> a + b)).isEqualTo(6);
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
        assertThat(ofAll("a", "b", "c").foldLeft("", (xs, x) -> xs + x)).isEqualTo("abc");
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
        assertThat(ofAll("a", "b", "c").foldRight("", (x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- hasDefiniteSize

    @Test
    public void shouldReturnSomethingOnHasDefiniteSize() {
        empty().hasDefiniteSize();
    }

    // -- head

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenHeadOnNil() {
        empty().head();
    }

    @Test
    public void shouldReturnHeadOfNonNil() {
        assertThat(ofAll(1, 2, 3).head()).isEqualTo(1);
    }

    // -- headOption

    @Test
    public void shouldReturnNoneWhenCallingHeadOptionOnNil() {
        assertThat(empty().headOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeHeadWhenCallingHeadOptionOnNonNil() {
        assertThat(ofAll(1, 2, 3).headOption()).isEqualTo(new Some<>(1));
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        empty().init().get();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(ofAll(1, 2, 3).init()).isEqualTo(ofAll(1, 2));
    }

    // -- groupBy

    @Test
    public void shouldNilGroupBy() {
        assertThat(empty().groupBy(Function.identity())).isEqualTo(HashMap.empty());
    }

    @Test
    public void shouldNonNilGroupByIdentity() {
        Map<?, ?> actual = ofAll('a', 'b', 'c').groupBy(Function.identity());
        Map<?, ?> expected = HashMap.empty().put('a', of('a')).put('b', of('b')).put('c', of('c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNonNilGroupByEqual() {
        Map<?, ?> actual = ofAll('a', 'b', 'c').groupBy(c -> 1);
        Map<?, ?> expected = HashMap.empty().put(1, ofAll('a', 'b', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- initOption

    @Test
    public void shouldReturnNoneWhenCallingInitOptionOnNil() {
        assertThat(empty().initOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        assertThat(ofAll(1, 2, 3).initOption()).isEqualTo(new Some<>(ofAll(1, 2)));
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

    // -- isTraversableAgain

    @Test
    public void shouldReturnSomethingOnIsTraversableAgain() {
        empty().isTraversableAgain();
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
        assertThat(ofAll(1, 2, 3).iterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final Iterator<Integer> iterator = ofAll(1, 2, 3).iterator();
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
        assertThat(ofAll('a', 'b', 'c').mkString()).isEqualTo("abc");
    }

    // -- mkString(delimiter)

    @Test
    public void shouldMkStringWithDelimiterNil() {
        assertThat(empty().mkString(",")).isEqualTo("");
    }

    @Test
    public void shouldMkStringWithDelimiterNonNil() {
        assertThat(ofAll('a', 'b', 'c').mkString(",")).isEqualTo("a,b,c");
    }

    // -- mkString(delimiter, prefix, suffix)

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkString("[", ",", "]")).isEqualTo("[]");
    }

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(ofAll('a', 'b', 'c').mkString("[", ",", "]")).isEqualTo("[a,b,c]");
    }

    // -- last

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenLastOnNil() {
        empty().last();
    }

    @Test
    public void shouldReturnLastOfNonNil() {
        assertThat(ofAll(1, 2, 3).last()).isEqualTo(3);
    }

    // -- lastOption

    @Test
    public void shouldReturnNoneWhenCallingLastOptionOnNil() {
        assertThat(empty().lastOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeLastWhenCallingLastOptionOnNonNil() {
        assertThat(ofAll(1, 2, 3).lastOption()).isEqualTo(new Some<>(3));
    }

    // -- length

    @Test
    public void shouldComputeLengthOfNil() {
        assertThat(empty().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonNil() {
        assertThat(ofAll(1, 2, 3).length()).isEqualTo(3);
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
    public void shouldPartitionIntsInOddAndEvenHavingOddAndEvenNumbers() {
        assertThat(ofAll(1, 2, 3, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(ofAll(1, 3), ofAll(2, 4)));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
        assertThat(ofAll(1, 3).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(ofAll(1, 3), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        assertThat(ofAll(2, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(empty(), ofAll(2, 4)));
    }

    // -- max

    @Test
    public void shouldReturnNoneWhenComputingMaxOfNil() {
        assertThat(empty().max()).isEqualTo(None.instance());
    }

    @Test
    public void shouldComputeMaxOfStrings() {
        assertThat(ofAll("1", "2", "3").max()).isEqualTo(new Some<>("3"));
    }

    @Test
    public void shouldComputeMaxOfBoolean() {
        assertThat(ofAll(true, false).max()).isEqualTo(new Some<>(true));
    }

    @Test
    public void shouldComputeMaxOfByte() {
        assertThat(ofAll((byte) 1, (byte) 2).max()).isEqualTo(new Some<>((byte) 2));
    }

    @Test
    public void shouldComputeMaxOfChar() {
        assertThat(ofAll('a', 'b', 'c').max()).isEqualTo(new Some<>('c'));
    }

    @Test
    public void shouldComputeMaxOfDouble() {
        assertThat(ofAll(.1, .2, .3).max()).isEqualTo(new Some<>(.3));
    }

    @Test
    public void shouldComputeMaxOfFloat() {
        assertThat(ofAll(.1f, .2f, .3f).max()).isEqualTo(new Some<>(.3f));
    }

    @Test
    public void shouldComputeMaxOfInt() {
        assertThat(ofAll(1, 2, 3).max()).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldComputeMaxOfLong() {
        assertThat(ofAll(1L, 2L, 3L).max()).isEqualTo(new Some<>(3L));
    }

    @Test
    public void shouldComputeMaxOfShort() {
        assertThat(ofAll((short) 1, (short) 2, (short) 3).max()).isEqualTo(new Some<>((short) 3));
    }

    @Test
    public void shouldComputeMaxOfBigInteger() {
        assertThat(ofAll(BigInteger.ZERO, BigInteger.ONE).max()).isEqualTo(new Some<>(BigInteger.ONE));
    }

    @Test
    public void shouldComputeMaxOfBigDecimal() {
        assertThat(ofAll(BigDecimal.ZERO, BigDecimal.ONE).max()).isEqualTo(new Some<>(BigDecimal.ONE));
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
        assertThat(ofAll(1, 2, 3).maxBy((i1, i2) -> i1 - i2)).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldCalculateInverseMaxByOfInts() {
        assertThat(ofAll(1, 2, 3).maxBy((i1, i2) -> i2 - i1)).isEqualTo(new Some<>(1));
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
        assertThat(ofAll(1, 2, 3).maxBy(i -> i)).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldCalculateInverseMaxByFunctionOfInts() {
        assertThat(ofAll(1, 2, 3).maxBy(i -> -i)).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldCallMaxFunctionOncePerElement() {
        final int[] cnt = { 0 };
        assertThat(ofAll(1, 2, 3).maxBy(i -> {
            cnt[0]++;
            return i;
        })).isEqualTo(new Some<>(3));
        assertThat(cnt[0]).isEqualTo(3);
    }

    // -- min

    @Test
    public void shouldReturnNoneWhenComputingMinOfNil() {
        assertThat(empty().min()).isEqualTo(None.instance());
    }

    @Test
    public void shouldComputeMinOfStrings() {
        assertThat(ofAll("1", "2", "3").min()).isEqualTo(new Some<>("1"));
    }

    @Test
    public void shouldComputeMinOfBoolean() {
        assertThat(ofAll(true, false).min()).isEqualTo(new Some<>(false));
    }

    @Test
    public void shouldComputeMinOfByte() {
        assertThat(ofAll((byte) 1, (byte) 2).min()).isEqualTo(new Some<>((byte) 1));
    }

    @Test
    public void shouldComputeMinOfChar() {
        assertThat(ofAll('a', 'b', 'c').min()).isEqualTo(new Some<>('a'));
    }

    @Test
    public void shouldComputeMinOfDouble() {
        assertThat(ofAll(.1, .2, .3).min()).isEqualTo(new Some<>(.1));
    }

    @Test
    public void shouldComputeMinOfFloat() {
        assertThat(ofAll(.1f, .2f, .3f).min()).isEqualTo(new Some<>(.1f));
    }

    @Test
    public void shouldComputeMinOfInt() {
        assertThat(ofAll(1, 2, 3).min()).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldComputeMinOfLong() {
        assertThat(ofAll(1L, 2L, 3L).min()).isEqualTo(new Some<>(1L));
    }

    @Test
    public void shouldComputeMinOfShort() {
        assertThat(ofAll((short) 1, (short) 2, (short) 3).min()).isEqualTo(new Some<>((short) 1));
    }

    @Test
    public void shouldComputeMinOfBigInteger() {
        assertThat(ofAll(BigInteger.ZERO, BigInteger.ONE).min()).isEqualTo(new Some<>(BigInteger.ZERO));
    }

    @Test
    public void shouldComputeMinOfBigDecimal() {
        assertThat(ofAll(BigDecimal.ZERO, BigDecimal.ONE).min()).isEqualTo(new Some<>(BigDecimal.ZERO));
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
        assertThat(ofAll(1, 2, 3).minBy((i1, i2) -> i1 - i2)).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldCalculateInverseMinByOfInts() {
        assertThat(ofAll(1, 2, 3).minBy((i1, i2) -> i2 - i1)).isEqualTo(new Some<>(3));
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
        assertThat(ofAll(1, 2, 3).minBy(i -> i)).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldCalculateInverseMinByFunctionOfInts() {
        assertThat(ofAll(1, 2, 3).minBy(i -> -i)).isEqualTo(new Some<>(3));
    }

    @Test
    public void shouldCallMinFunctionOncePerElement() {
        final int[] cnt = { 0 };
        assertThat(ofAll(1, 2, 3).minBy(i -> {
            cnt[0]++;
            return i;
        })).isEqualTo(new Some<>(1));
        assertThat(cnt[0]).isEqualTo(3);
    }

    // -- product

    @Test
    public void shouldComputeProductOfNil() {
        assertThat(empty().product()).isEqualTo(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingProductOfStrings() {
        ofAll("1", "2", "3").product();
    }

    @Test
    public void shouldComputeProductOfByte() {
        assertThat(ofAll((byte) 1, (byte) 2).product()).isEqualTo(2);
    }

    @Test
    public void shouldComputeProductOfDouble() {
        assertThat(ofAll(.1, .2, .3).product().doubleValue()).isEqualTo(.006, within(10e-18));
    }

    @Test
    public void shouldComputeProductOfFloat() {
        assertThat(ofAll(.1f, .2f, .3f).product().doubleValue()).isEqualTo(.006, within(10e-10));
    }

    @Test
    public void shouldComputeProductOfInt() {
        assertThat(ofAll(1, 2, 3).product()).isEqualTo(6);
    }

    @Test
    public void shouldComputeProductOfLong() {
        assertThat(ofAll(1L, 2L, 3L).product()).isEqualTo(6L);
    }

    @Test
    public void shouldComputeProductOfShort() {
        assertThat(ofAll((short) 1, (short) 2, (short) 3).product()).isEqualTo(6);
    }

    @Test
    public void shouldComputeProductOfBigInteger() {
        assertThat(ofAll(BigInteger.ZERO, BigInteger.ONE).product()).isEqualTo(0L);
    }

    @Test
    public void shouldComputeProductOfBigDecimal() {
        assertThat(ofAll(BigDecimal.ZERO, BigDecimal.ONE).product()).isEqualTo(0.0);
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
        assertThat(ofAll(1, 2, 3).reduce((a, b) -> a + b)).isEqualTo(6);
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
        assertThat(ofAll("a", "b", "c").reduceLeft((xs, x) -> xs + x)).isEqualTo("abc");
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
        assertThat(ofAll("a", "b", "c").reduceRight((x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- replace(curr, new)

    @Test
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replace(1, 2)).isEqualTo(empty());
    }

    @Test
    public void shouldReplaceFirstOccurrenceOfNonNilUsingCurrNewWhenMultipleOccurrencesExist() {
        final Traversable<Integer> testee = ofAll(0, 1, 2, 1);
        final Traversable<Integer> actual = testee.replace(1, 3);
        final Traversable<Integer> expected = ofAll(0, 3, 2, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReplaceElementOfNonNilUsingCurrNewWhenOneOccurrenceExists() {
        assertThat(ofAll(0, 1, 2).replace(1, 3)).isEqualTo(ofAll(0, 3, 2));
    }

    // -- replaceAll(curr, new)

    @Test
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replaceAll(1, 2)).isEqualTo(empty());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(ofAll(0, 1, 2, 1).replaceAll(1, 3)).isEqualTo(ofAll(0, 3, 2, 3));
    }

    // -- retainAll

    @Test
    public void shouldRetainAllElementsFromNil() {
        assertThat(empty().retainAll(ofAll(1, 2, 3))).isEqualTo(empty());
    }

    @Test
    public void shouldRetainAllExistingElementsFromNonNil() {
        assertThat(ofAll(1, 2, 3, 1, 2, 3).retainAll(ofAll(1, 2))).isEqualTo(ofAll(1, 2, 1, 2));
    }

    @Test
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        assertThat(ofAll(1, 2, 3).retainAll(ofAll(4, 5))).isEqualTo(empty());
    }

    // -- scan, scanLeft, scanRight

    @Test
    public void shouldScanEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scan(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(ofAll(0));
    }

    @Test
    public void shouldScanLeftEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scanLeft(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(ofAll(0));
    }

    @Test
    public void shouldScanRightEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scanRight(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(ofAll(0));
    }

    @Test
    public void shouldScanNonEmpty() {
        final Traversable<Integer> testee = ofAll(1, 2, 3);
        final Traversable<Integer> actual = testee.scan(0, (acc, s) -> acc + s);
        assertThat(actual).isEqualTo(ofAll(0, 1, 3, 6));
    }

    @Test
    public void shouldScanLeftNonEmpty() {
        final Traversable<Integer> testee = ofAll(1, 2, 3);
        final Traversable<String> actual = testee.scanLeft("x", (acc, i) -> acc + i);
        assertThat(actual).isEqualTo(ofAll("x", "x1", "x12", "x123"));
    }

    @Test
    public void shouldScanRightNonEmpty() {
        final Traversable<Integer> testee = ofAll(1, 2, 3);
        final Traversable<String> actual = testee.scanRight("x", (i, acc) -> acc + i);
        assertThat(actual).isEqualTo(ofAll("x321", "x32", "x3", "x"));
    }

    @Test
    public void shouldScanWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scan(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.ofAll("x", "xa").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(actual.length()).isEqualTo(expected.length());
    }

    @Test
    public void shouldScanLeftWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scanLeft(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.ofAll("x", "xa").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(actual.length()).isEqualTo(expected.length());
    }

    @Test
    public void shouldScanRightWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scanRight(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.ofAll("ax", "x").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(actual.length()).isEqualTo(expected.length());
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

    @Test
    public void shouldSlideNonNilBySize1() {
        assertThat(ofAll(1, 2, 3).sliding(1).toList()).isEqualTo(List.ofAll(Vector.of(1), Vector.of(2), Vector.of(3)));
    }

    @Test // #201
    public void shouldSlideNonNilBySize2() {
        assertThat(ofAll(1, 2, 3, 4, 5).sliding(2).toList())
                .isEqualTo(List.ofAll(Vector.ofAll(1, 2), Vector.ofAll(2, 3), Vector.ofAll(3, 4), Vector.ofAll(4, 5)));
    }

    // -- sliding(size, step)

    @Test
    public void shouldSlideNilBySizeAndStep() {
        assertThat(empty().sliding(1, 1).isEmpty()).isTrue();
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep3() {
        assertThat(ofAll(1, 2, 3, 4, 5).sliding(2, 3).toList()).isEqualTo(List.ofAll(Vector.ofAll(1, 2), Vector.ofAll(4, 5)));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep4() {
        assertThat(ofAll(1, 2, 3, 4, 5).sliding(2, 4).toList()).isEqualTo(List.ofAll(Vector.ofAll(1, 2), Vector.of(5)));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep5() {
        assertThat(ofAll(1, 2, 3, 4, 5).sliding(2, 5).toList()).isEqualTo(List.of(Vector.ofAll(1, 2)));
    }

    @Test
    public void shouldSlide4ElementsBySize5AndStep3() {
        assertThat(ofAll(1, 2, 3, 4).sliding(5, 3).toList()).isEqualTo(List.of(Vector.ofAll(1, 2, 3, 4)));
    }

    // -- span

    @Test
    public void shouldSpanNil() {
        assertThat(this.<Integer> empty().span(i -> i < 2)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSpanNonNil() {
        assertThat(ofAll(0, 1, 2, 3).span(i -> i < 2)).isEqualTo(Tuple.of(ofAll(0, 1), ofAll(2, 3)));
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
        ofAll(1, 2, 3).spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Arrays.asList(1, 2, 3));
    }

    @Test
    public void shouldHaveImmutableSpliterator() {
        assertThat(ofAll(1, 2, 3).spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(ofAll(1, 2, 3).spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
    }

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(ofAll(1, 2, 3).spliterator().characteristics() & Spliterator.SIZED).isNotZero();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(ofAll(1, 2, 3).spliterator().getExactSizeIfKnown()).isEqualTo(3);
    }

    // -- stderr

    @Test
    public void shouldWriteToStderr() {
        ofAll(1, 2, 3).stderr();
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
        ofAll(1, 2, 3).stdout();
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

    // -- PrintStream

    @Test
    public void shouldWriteToPrintStream() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        final PrintStream out = new PrintStream(baos);
        ofAll(1, 2, 3).out(out);
        assertThat(baos.toString()).isEqualTo(ofAll(1, 2, 3).mkString("", "\n", "\n"));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandlePrintStreamIOException() {
        try (PrintStream failingPrintStream = failingPrintStream()) {
            of(0).out(failingPrintStream);
        }
    }

    // -- PrintWriter

    @Test
    public void shouldWriteToPrintWriter() {
        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);
        ofAll(1, 2, 3).out(out);
        assertThat(sw.toString()).isEqualTo(ofAll(1, 2, 3).mkString("", "\n", "\n"));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandlePrintWriterIOException() {
        try (PrintWriter failingPrintWriter = failingPrintWriter()) {
            of(0).out(failingPrintWriter);
        }
    }

    // -- sum

    @Test
    public void shouldComputeSumOfNil() {
        assertThat(empty().sum()).isEqualTo(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingSumOfStrings() {
        ofAll("1", "2", "3").sum();
    }

    @Test
    public void shouldComputeSumOfByte() {
        assertThat(ofAll((byte) 1, (byte) 2).sum()).isEqualTo(3);
    }

    @Test
    public void shouldComputeSumOfDouble() {
        assertThat(ofAll(.1, .2, .3).sum().doubleValue()).isEqualTo(.6, within(10e-16));
    }

    @Test
    public void shouldComputeSumOfFloat() {
        assertThat(ofAll(.1f, .2f, .3f).sum().doubleValue()).isEqualTo(.6, within(10e-8));
    }

    @Test
    public void shouldComputeSumOfInt() {
        assertThat(ofAll(1, 2, 3).sum()).isEqualTo(6);
    }

    @Test
    public void shouldComputeSumOfLong() {
        assertThat(ofAll(1L, 2L, 3L).sum()).isEqualTo(6L);
    }

    @Test
    public void shouldComputeSumOfShort() {
        assertThat(ofAll((short) 1, (short) 2, (short) 3).sum()).isEqualTo(6);
    }

    @Test
    public void shouldComputeSumOfBigInteger() {
        assertThat(ofAll(BigInteger.ZERO, BigInteger.ONE).sum()).isEqualTo(1L);
    }

    @Test
    public void shouldComputeSumOfBigDecimal() {
        assertThat(ofAll(BigDecimal.ZERO, BigDecimal.ONE).sum()).isEqualTo(1.0);
    }

    // -- take

    @Test
    public void shouldTakeNoneOnNil() {
        assertThat(empty().take(1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(ofAll(1, 2, 3).take(-1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(ofAll(1, 2, 3).take(2)).isEqualTo(ofAll(1, 2));
    }

    @Test
    public void shouldTakeAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(ofAll(1, 2, 3).take(4)).isEqualTo(ofAll(1, 2, 3));
        } else {
            final Traversable<Integer> t = ofAll(1, 2, 3);
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
        assertThat(ofAll(1, 2, 3).takeRight(-1)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(ofAll(1, 2, 3).takeRight(2)).isEqualTo(ofAll(2, 3));
    }

    @Test
    public void shouldTakeRightAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(ofAll(1, 2, 3).takeRight(4)).isEqualTo(ofAll(1, 2, 3));
        } else {
            final Traversable<Integer> t = ofAll(1, 2, 3);
            assertThat(t.takeRight(4)).isSameAs(t);
        }
    }

    // -- takeUntil

    @Test
    public void shouldTakeUntilNoneOnNil() {
        assertThat(empty().takeUntil(x -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeUntilAllOnFalseCondition() {
        final Traversable<Integer> t = ofAll(1, 2, 3);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(ofAll(1, 2, 3).takeUntil(x -> false)).isEqualTo(ofAll(1, 2, 3));
        } else {
            assertThat(t.takeUntil(x -> false)).isSameAs(t);
        }
    }

    @Test
    public void shouldTakeUntilAllOnTrueCondition() {
        assertThat(ofAll(1, 2, 3).takeUntil(x -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeUntilAsExpected() {
        assertThat(ofAll(2, 4, 5, 6).takeUntil(x -> x % 2 != 0)).isEqualTo(ofAll(2, 4));
    }

    // -- takeWhile

    @Test
    public void shouldTakeWhileNoneOnNil() {
        assertThat(empty().takeWhile(x -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(ofAll(1, 2, 3).takeWhile(x -> false)).isEqualTo(empty());
    }

    @Test
    public void shouldTakeWhileAllOnTrueCondition() {
        final Traversable<Integer> t = ofAll(1, 2, 3);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(ofAll(1, 2, 3).takeWhile(x -> true)).isEqualTo(ofAll(1, 2, 3));
        } else {
            assertThat(t.takeWhile(x -> true)).isSameAs(t);
        }
    }

    @Test
    public void shouldTakeWhileAsExpected() {
        assertThat(ofAll(2, 4, 5, 6).takeWhile(x -> x % 2 == 0)).isEqualTo(ofAll(2, 4));
    }

    // -- tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOnNil() {
        empty().tail();
    }

    @Test
    public void shouldReturnTailOfNonNil() {
        assertThat(ofAll(1, 2, 3).tail()).isEqualTo(ofAll(2, 3));
    }

    // -- tailOption

    @Test
    public void shouldReturnNoneWhenCallingTailOptionOnNil() {
        assertThat(empty().tailOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(ofAll(1, 2, 3).tailOption()).isEqualTo(new Some<>(ofAll(2, 3)));
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
        final Integer[] array = ofAll(1, 2).toJavaArray(Integer.class);
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
        assertThat(ofAll(1, 2, 3).toJavaList()).isEqualTo(Arrays.asList(1, 2, 3));
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
        assertThat(ofAll(1, 2).toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
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
        assertThat(ofAll(1, 2, 2, 3).toJavaSet()).isEqualTo(expected);
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
        assertThat(ofAll(1, 2, 3).equals(ofAll(1, 2, 3))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(ofAll(1, 2, 3).equals(ofAll(1, 2, 4))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(ofAll(1, 2, 3).equals(ofAll(1, 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode() == empty().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(ofAll(1, 2).hashCode() == ofAll(1, 2).hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(ofAll(1, 2).hashCode() != ofAll(2, 3).hashCode()).isTrue();
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
        final Object actual = deserialize(serialize(ofAll(1, 2, 3)));
        final Object expected = ofAll(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.empty().collect(this.<Object> collector());
            assertThat(actual).isEmpty();
        });
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).collect(this.<Object> collector());
            assertThat(actual).isEqualTo(ofAll(1, 2, 3));
        });
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.empty().parallel().collect(this.<Object> collector());
            assertThat(actual).isEmpty();
        });
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).parallel().collect(this.<Object> collector());
            assertThat(actual).isEqualTo(ofAll(1, 2, 3));
        });
    }

    private void testCollector(Runnable test) {
        if (isTraversableAgain()) {
            test.run();
        } else {
            try {
                collector();
                fail("Collections which are not traversable again should not define a Collector.");
            } catch (UnsupportedOperationException x) {
                // ok
            } catch (Throwable x) {
                fail("Unexpected exception", x);
            }
        }
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

    static PrintWriter failingPrintWriter() {
        return new PrintWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }

    /**
     * Wraps a String in order to ensure that it is not Comparable.
     */
    static final class NonComparable {

        final String value;

        NonComparable(String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof NonComparable) {
                final NonComparable that = (NonComparable) obj;
                return Objects.equals(this.value, that.value);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
