/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.AbstractValueTest;
import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.junit.jupiter.api.TestTemplate;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.List;
import static io.vavr.API.Map;
import static io.vavr.OutputTester.captureErrOut;
import static io.vavr.OutputTester.captureStdOut;
import static io.vavr.OutputTester.failingPrintStream;
import static io.vavr.OutputTester.failingPrintWriter;
import static io.vavr.OutputTester.withFailingErrOut;
import static io.vavr.OutputTester.withFailingStdOut;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public abstract class AbstractTraversableTest extends AbstractValueTest {

    protected final boolean isTraversableAgain() {
        return empty().isTraversableAgain();
    }

    protected final boolean isOrdered() {
        return empty().isOrdered();
    }

    protected abstract <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector();

    @Override
    protected abstract <T> Traversable<T> empty();

    protected boolean emptyShouldBeSingleton() {
        return true;
    }

    @Override
    protected abstract <T> Traversable<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    protected abstract <T> Traversable<T> of(T... elements);

    protected abstract <T> Traversable<T> ofAll(Iterable<? extends T> elements);

    protected abstract <T extends Comparable<? super T>> Traversable<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream);

    protected abstract Traversable<Boolean> ofAll(boolean... elements);

    protected abstract Traversable<Byte> ofAll(byte... elements);

    protected abstract Traversable<Character> ofAll(char... elements);

    protected abstract Traversable<Double> ofAll(double... elements);

    protected abstract Traversable<Float> ofAll(float... elements);

    protected abstract Traversable<Integer> ofAll(int... elements);

    protected abstract Traversable<Long> ofAll(long... elements);

    protected abstract Traversable<Short> ofAll(short... elements);

    protected abstract <T> Traversable<T> tabulate(int n, Function<? super Integer, ? extends T> f);

    protected abstract <T> Traversable<T> fill(int n, Supplier<? extends T> s);

    // -- static empty()

    @TestTemplate
    public void shouldCreateNil() {
        final Traversable<?> actual = empty();
        assertThat(actual.length()).isEqualTo(0);
    }

    // -- static narrow()

    @TestTemplate
    public void shouldNarrowTraversable() {
        final Traversable<Double> doubles = of(1.0d);
        final Traversable<Number> numbers = Traversable.narrow(doubles);
        final boolean actual = numbers.contains(new BigDecimal("2.0"));
        assertThat(actual).isFalse();
    }

    // -- static of()

    @TestTemplate
    public void shouldCreateSeqOfSeqUsingCons() {
        final List<List<Object>> actual = of(List.empty()).toList();
        assertThat(actual).isEqualTo(List.of(List.empty()));
    }

    // -- static of(T...)

    @TestTemplate
    public void shouldCreateInstanceOfElements() {
        final List<Integer> actual = of(1, 2).toList();
        assertThat(actual).isEqualTo(List.of(1, 2));
    }

    // -- static of(Iterable)

    @TestTemplate
    public void shouldCreateListOfIterable() {
        final java.util.List<Integer> arrayList = asList(1, 2);
        final List<Integer> actual = ofAll(arrayList).toList();
        assertThat(actual).isEqualTo(List.of(1, 2));
    }

    // -- static ofAll(java.util.stream.Stream)

    @TestTemplate
    public void shouldCreateStreamFromEmptyJavaUtilStream() {
        final java.util.stream.Stream<Integer> javaStream = java.util.stream.Stream.empty();
        assertThat(ofJavaStream(javaStream)).isEqualTo(empty());
    }

    @TestTemplate
    public void shouldCreateStreamFromNonEmptyJavaUtilStream() {
        final java.util.stream.Stream<Integer> javaStream = java.util.stream.Stream.of(1, 2, 3);
        assertThat(ofJavaStream(javaStream)).isEqualTo(of(1, 2, 3));
    }

    // -- static of(<primitive array>)

    @TestTemplate
    public void shouldCreateListOfPrimitiveBooleanArray() {
        final Traversable<Boolean> actual = ofAll(true, false);
        final Traversable<Boolean> expected = of(true, false);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveByteArray() {
        final Traversable<Byte> actual = ofAll((byte) 1, (byte) 2, (byte) 3);
        final Traversable<Byte> expected = of((byte) 1, (byte) 2, (byte) 3);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveCharArray() {
        final Traversable<Character> actual = ofAll('a', 'b', 'c');
        final Traversable<Character> expected = of('a', 'b', 'c');
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveDoubleArray() {
        final Traversable<Double> actual = ofAll(1d, 2d, 3d);
        final Traversable<Double> expected = of(1d, 2d, 3d);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveFloatArray() {
        final Traversable<Float> actual = ofAll(1f, 2f, 3f);
        final Traversable<Float> expected = of(1f, 2f, 3f);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveIntArray() {
        final Traversable<Integer> actual = ofAll(1, 2, 3);
        final Traversable<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveLongArray() {
        final Traversable<Long> actual = ofAll(1L, 2L, 3L);
        final Traversable<Long> expected = of(1L, 2L, 3L);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldCreateListOfPrimitiveShortArray() {
        final Traversable<Short> actual = ofAll((short) 1, (short) 2, (short) 3);
        final Traversable<Short> expected = of((short) 1, (short) 2, (short) 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- average

    @TestTemplate
    public void shouldReturnNoneWhenComputingAverageOfNil() {
        assertThat(empty().average()).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldThrowWhenComputingAverageOfStrings() {
        assertThrows(UnsupportedOperationException.class, () -> of("1", "2", "3").average());
    }

    @TestTemplate
    public void shouldComputeAverageOfByte() {
        assertThat(of((byte) 1, (byte) 2).average().get()).isEqualTo(1.5);
    }

    @TestTemplate
    public void shouldComputeAverageOfDouble() {
        assertThat(of(.1, .2, .3).average().get()).isEqualTo(.2, within(10e-17));
    }

    @TestTemplate
    public void shouldComputeAverageOfFloat() {
        assertThat(of(.1f, .2f, .3f).average().get()).isEqualTo(.2, within(10e-9));
    }

    @TestTemplate
    public void shouldComputeAverageOfInt() {
        assertThat(of(1, 2, 3).average().get()).isEqualTo(2);
    }

    @TestTemplate
    public void shouldComputeAverageOfLong() {
        assertThat(of(1L, 2L, 3L).average().get()).isEqualTo(2);
    }

    @TestTemplate
    public void shouldComputeAverageOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).average().get()).isEqualTo(2);
    }

    @TestTemplate
    public void shouldComputeAverageOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).average().get()).isEqualTo(.5);
    }

    @TestTemplate
    public void shouldComputeAverageOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).average().get()).isEqualTo(.5);
    }

    @TestTemplate
    public void shouldComputeAverageAndCompensateErrors() {
        // Kahan's summation algorithm (used by DoubleStream.average()) returns 0.0 (false)
        // Neumaier's modification of Kahan's algorithm returns 0.75 (correct)
        assertThat(of(1.0, +10e100, 2.0, -10e100).average().get()).isEqualTo(0.75);
    }

    @TestTemplate
    public void shouldCalculateAverageOfDoublesContainingNaN() {
        assertThat(of(1.0, Double.NaN, 2.0).average().get()).isNaN();
    }

    @TestTemplate
    public void shouldCalculateAverageOfFloatsContainingNaN() {
        assertThat(of(1.0f, Float.NaN, 2.0f).average().get()).isNaN();
    }

    @TestTemplate
    public void shouldCalculateAverageOfDoubleAndFloat() {
        assertThat(this.<Number>of(1.0, 1.0f).average().get()).isEqualTo(1.0);
    }

    @TestTemplate
    public void shouldCalculateAverageOfDoublePositiveAndNegativeInfinity() {
        assertThat(of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).average().get()).isNaN();
    }

    @TestTemplate
    public void shouldCalculateAverageOfFloatPositiveAndNegativeInfinity() {
        assertThat(of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY).average().get()).isNaN();
    }

    // -- collect

    @TestTemplate
    public void shouldThrowOnCollectWhenPartialFunctionIsNull() {
        assertThatThrownBy(() -> empty().collect((PartialFunction<Object, ?>) null))
          .isExactlyInstanceOf(NullPointerException.class)
          .hasMessage("partialFunction is null");
    }

    @TestTemplate
    public void shouldCollectUsingPartialFunction() {
        final PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(Integer i) {
                return String.valueOf(i);
            }

            @Override
            public boolean isDefinedAt(Integer i) {
                return i % 2 == 1;
            }
        };
        final Traversable<String> actual = of(1, 2, 3).collect(pf);
        assertThat(actual).isEqualTo(of("1", "3"));
    }

    @TestTemplate
    public void shouldCollectUsingCase() {
        final Traversable<String> actual = of(1, 2, 3).collect(
          Case($(i -> i % 2 == 1), String::valueOf)
        );
        assertThat(actual).isEqualTo(of("1", "3"));
    }

    @TestTemplate
    public void shouldCollectUsingMap() {
        final Map<Integer, String> map = Map(1, "one", 3, "three");
        final Traversable<String> actual = of(1, 2, 3, 4).collect(map.asPartialFunction());
        assertThat(actual).isEqualTo(of("one", "three"));
    }

    @SuppressWarnings("unchecked")
    @TestTemplate
    public void shouldCollectUsingMultimap() {
        if (!isOrdered()) {
            final Multimap<Integer, String> map = HashMultimap.withSeq().of(1, "one", 1, "un", 3, "three", 3, "trois");
            final Traversable<Traversable<String>> actual = of(1, 2, 3, 4).collect(map.asPartialFunction());
            assertThat(actual).isEqualTo(of(List("one", "un"), List("three", "trois")));
        }
    }

    @TestTemplate
    public void shouldCollectUsingSeq() {
        final Seq<String> map = List("one", "two", "three", "four");
        final Traversable<String> actual = of(0, 2).collect(map.asPartialFunction());
        assertThat(actual).isEqualTo(of("one", "three"));
    }

    // -- contains

    @TestTemplate
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = empty().contains(null);
        assertThat(actual).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = of(1, 2, 3).contains(0);
        assertThat(actual).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = of(1, 2, 3).contains(2);
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @TestTemplate
    public void shouldHandleDuplicates() {
        final boolean actual = of(1, 2, 3, 2, 3, 1).containsAll(of(1, 2, 2));
        assertThat(actual).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = empty().containsAll(of(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = of(1, 2, 3).containsAll(of(2, 3, 4));
        assertThat(actual).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = of(1, 2, 3).containsAll(of(1, 2, 3));
        assertThat(actual).isTrue();
    }

    // -- count

    @TestTemplate
    public void shouldCountWhenIsEmpty() {
        assertThat(empty().count(ignored -> true)).isEqualTo(0);
    }

    @TestTemplate
    public void shouldCountWhenNoneSatisfiesThePredicate() {
        assertThat(of(1, 2, 3).count(ignored -> false)).isEqualTo(0);
    }

    @TestTemplate
    public void shouldCountWhenAllSatisfyThePredicate() {
        assertThat(of(1, 2, 3).count(ignored -> true)).isEqualTo(3);
    }

    @TestTemplate
    public void shouldCountWhenSomeSatisfyThePredicate() {
        assertThat(of(1, 2, 3).count(i -> i % 2 == 0)).isEqualTo(1);
    }

    // -- distinct

    @TestTemplate
    public void shouldComputeDistinctOfEmptyTraversable() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().distinct()).isEqualTo(empty());
        } else {
            assertThat(empty().distinct()).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        final Traversable<Integer> testee = of(1, 1, 2, 2, 3, 3);
        final Traversable<Integer> actual = testee.distinct();
        final Traversable<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
        if (testee.isDistinct()) {
            assertThat(actual).isSameAs(testee);
        }
    }

    // -- distinctBy(Comparator)

    @TestTemplate
    public void shouldComputeDistinctByOfEmptyTraversableUsingComparator() {
        final Comparator<Integer> comparator = comparingInt(i -> i);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(this.<Integer>empty().distinctBy(comparator)).isEqualTo(empty());
        } else {
            assertThat(this.<Integer>empty().distinctBy(comparator)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        final Comparator<String> comparator = comparingInt(s -> (s.charAt(1)));
        final Traversable<String> distinct = of("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(comparator)
          .map(s -> s.substring(1));
        assertThat(distinct).isEqualTo(of("a", "b", "c"));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDistinctByComparatorEmptyTraversable() {
        final Traversable<?> empty = empty();
        assertThat(empty.distinctBy(Comparators.naturalComparator())).isSameAs(empty);
    }

    // -- distinctBy(Function)

    @TestTemplate
    public void shouldComputeDistinctByOfEmptyTraversableUsingKeyExtractor() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().distinctBy(Function.identity())).isEqualTo(empty());
        } else {
            assertThat(empty().distinctBy(Function.identity())).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        final Function<String, Character> function = c -> c.charAt(1);
        final Traversable<String> distinct = of("1a", "2a", "3a", "3b", "4b", "5c").distinctBy(function)
          .map(s -> s.substring(1));
        assertThat(distinct).isEqualTo(of("a", "b", "c"));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDistinctByFunctionEmptyTraversable() {
        final Traversable<?> empty = empty();
        assertThat(empty.distinctBy(Function.identity())).isSameAs(empty);
    }

    // -- drop

    @TestTemplate
    public void shouldDropNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().drop(1)).isEqualTo(empty());
        } else {
            assertThat(empty().drop(1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldDropNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).drop(-1)).isEqualTo(of(1, 2, 3));
    }

    @TestTemplate
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).drop(2)).isEqualTo(of(3));
    }

    @TestTemplate
    public void shouldDropAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).drop(4)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).drop(4)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDropZeroCount() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.drop(0)).isSameAs(t);
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDropNegativeCount() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.drop(-1)).isSameAs(t);
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyDropOne() {
        final Traversable<?> empty = empty();
        assertThat(empty.drop(1)).isSameAs(empty);
    }

    // -- dropRight

    @TestTemplate
    public void shouldDropRightNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().dropRight(1)).isEqualTo(empty());
        } else {
            assertThat(empty().dropRight(1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldDropRightNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).dropRight(-1)).isEqualTo(of(1, 2, 3));
    }

    @TestTemplate
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).dropRight(2)).isEqualTo(of(1));
    }

    @TestTemplate
    public void shouldDropRightAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).dropRight(4)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).dropRight(4)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDropRightZeroCount() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.dropRight(0)).isSameAs(t);
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenDropRightNegativeCount() {
        final Traversable<Integer> t = of(1, 2, 3);
        assertThat(t.dropRight(-1)).isSameAs(t);
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyDropRightOne() {
        final Traversable<?> empty = empty();
        assertThat(empty.dropRight(1)).isSameAs(empty);
    }

    // -- dropUntil

    @TestTemplate
    public void shouldDropUntilNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().dropUntil(ignored -> true)).isEqualTo(empty());
        } else {
            assertThat(empty().dropUntil(ignored -> true)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldDropUntilNoneIfPredicateIsTrue() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).dropUntil(ignored -> true)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.dropUntil(ignored -> true)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldDropUntilAllIfPredicateIsFalse() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).dropUntil(ignored -> false)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).dropUntil(ignored -> false)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldDropUntilCorrect() {
        assertThat(of(1, 2, 3).dropUntil(i -> i >= 2)).isEqualTo(of(2, 3));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyDropUntil() {
        final Traversable<?> empty = empty();
        assertThat(empty.dropUntil(ignored -> true)).isSameAs(empty);
    }

    // -- dropWhile

    @TestTemplate
    public void shouldDropWhileNoneOnNil() {
        final Traversable<?> empty = empty();
        final Traversable<?> actual = empty.dropWhile(ignored -> true);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(actual).isEqualTo(empty);
        } else {
            assertThat(actual).isSameAs(empty);
        }
    }

    @TestTemplate
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).dropWhile(ignored -> false)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.dropWhile(ignored -> false)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldDropWhileAllIfPredicateIsTrue() {
        final Traversable<Integer> actual = of(1, 2, 3).dropWhile(ignored -> true);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(actual).isEqualTo(empty());
        } else {
            assertThat(actual).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldDropWhileAccordingToPredicate() {
        assertThat(of(1, 2, 3).dropWhile(i -> i < 2)).isEqualTo(of(2, 3));
    }

    @TestTemplate
    public void shouldDropWhileAndNotTruncate() {
        assertThat(of(1, 2, 3).dropWhile(i -> i % 2 == 1)).isEqualTo(of(2, 3));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyDropWhile() {
        final Traversable<?> empty = empty();
        assertThat(empty.dropWhile(ignored -> true)).isSameAs(empty);
    }

    // -- existsUnique

    @TestTemplate
    public void shouldBeAwareOfExistingUniqueElement() {
        assertThat(of(1, 2).existsUnique(i -> i == 1)).isTrue();
    }

    @TestTemplate
    public void shouldBeAwareOfNonExistingUniqueElement() {
        assertThat(this.<Integer>empty().existsUnique(i -> i == 1)).isFalse();
    }

    @TestTemplate
    public void shouldBeAwareOfExistingNonUniqueElement() {
        assertThat(of(1, 1, 2).existsUnique(i -> i == 1)).isFalse();
    }

    // -- filter

    @TestTemplate
    public void shouldFilterExistingElements() {
        assertThat(of(1, 2, 3).filter(i -> i == 1)).isEqualTo(of(1));
        assertThat(of(1, 2, 3).filter(i -> i == 2)).isEqualTo(of(2));
        assertThat(of(1, 2, 3).filter(i -> i == 3)).isEqualTo(of(3));
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).filter(ignore -> true)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.filter(ignore -> true)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldFilterNonExistingElements() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(this.<Integer>empty().filter(i -> i == 0)).isEqualTo(empty());
            assertThat(of(1, 2, 3).filter(i -> i == 0)).isEqualTo(empty());
        } else {
            assertThat(this.<Integer>empty().filter(i -> i == 0)).isSameAs(empty());
            assertThat(of(1, 2, 3).filter(i -> i == 0)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenFilteringEmptyTraversable() {
        final Traversable<?> empty = empty();
        assertThat(empty.filter(v -> true)).isSameAs(empty);
    }

    // -- reject

    @TestTemplate
    public void shouldRejectExistingElements() {
        assertThat(of(1, 2, 3).reject(i -> i == 1)).isEqualTo(of(2, 3));
        assertThat(of(1, 2, 3).reject(i -> i == 2)).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).reject(i -> i == 3)).isEqualTo(of(1, 2));
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).reject(ignore -> false)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.reject(ignore -> false)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldRejectNonExistingElements() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(this.<Integer>empty().reject(i -> i == 0)).isEqualTo(empty());
            assertThat(of(1, 2, 3).reject(i -> i > 0)).isEqualTo(empty());
        } else {
            assertThat(this.<Integer>empty().reject(i -> i == 0)).isSameAs(empty());
            assertThat(of(1, 2, 3).reject(i -> i > 0)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenRejectingEmptyTraversable() {
        final Traversable<?> empty = empty();
        assertThat(empty.reject(v -> true)).isSameAs(empty);
    }

    // -- find

    @TestTemplate
    public void shouldFindFirstOfNil() {
        assertThat(empty().find(ignored -> true)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldFindFirstOfNonNil() {
        assertThat(of(1, 2, 3, 4).find(i -> i % 2 == 0)).isEqualTo(Option.of(2));
    }

    // -- findLast

    @TestTemplate
    public void shouldFindLastOfNil() {
        assertThat(empty().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldFindLastOfNonNil() {
        assertThat(of(1, 2, 3, 4).findLast(i -> i % 2 == 0)).isEqualTo(Option.of(4));
    }

    // -- flatMap

    @TestTemplate
    public void shouldFlatMapEmpty() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().flatMap(v -> of(v, 0))).isEqualTo(empty());
        } else {
            assertThat(empty().flatMap(v -> of(v, 0))).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldFlatMapNonEmpty() {
        assertThat(of(1, 2, 3).flatMap(v -> of(v, 0))).isEqualTo(of(1, 0, 2, 0, 3, 0));
    }

    // -- fold

    @TestTemplate
    public void shouldFoldNil() {
        assertThat(this.<String>empty().fold("", (a, b) -> a + b)).isEqualTo("");
    }

    @TestTemplate
    public void shouldThrowWhenFoldNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().fold(null, null));
    }

    @TestTemplate
    public void shouldFoldSingleElement() {
        assertThat(of(1).fold(0, (a, b) -> a + b)).isEqualTo(1);
    }

    @TestTemplate
    public void shouldFoldMultipleElements() {
        assertThat(of(1, 2, 3).fold(0, (a, b) -> a + b)).isEqualTo(6);
    }

    // -- foldLeft

    @TestTemplate
    public void shouldFoldLeftNil() {
        assertThat(this.<String>empty().foldLeft("", (xs, x) -> xs + x)).isEqualTo("");
    }

    @TestTemplate
    public void shouldThrowWhenFoldLeftNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().foldLeft(null, null));
    }

    @TestTemplate
    public void shouldFoldLeftNonNil() {
        assertThat(of("a", "b", "c").foldLeft("!", (xs, x) -> xs + x)).isEqualTo("!abc");
    }

    // -- foldRight

    @TestTemplate
    public void shouldFoldRightNil() {
        assertThat(this.<String>empty().foldRight("", (x, xs) -> x + xs)).isEqualTo("");
    }

    @TestTemplate
    public void shouldThrowWhenFoldRightNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().foldRight(null, null));
    }

    @TestTemplate
    public void shouldFoldRightNonNil() {
        assertThat(of("a", "b", "c").foldRight("!", (x, xs) -> x + xs)).isEqualTo("abc!");
    }

    // -- forEachWithIndex

    @TestTemplate
    public void shouldConsumeNoElementWithIndexWhenEmpty() {
        final boolean[] actual = {false};
        final boolean[] expected = {false};
        empty().forEachWithIndex((chr, index) -> actual[0] = true);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldConsumeEachElementWithIndexWhenNonEmpty() {
        final java.util.List<Tuple2<Character, Integer>> actual = new java.util.ArrayList<>();
        final java.util.List<Tuple2<Character, Integer>> expected = Arrays.asList(Tuple.of('a', 0), Tuple.of('b', 1), Tuple.of('c', 2));
        ofAll('a', 'b', 'c').forEachWithIndex((chr, index) -> actual.add(Tuple.of(chr, index)));
        assertThat(actual).isEqualTo(expected);
    }

    // -- groupBy

    @TestTemplate
    public void shouldNilGroupBy() {
        assertThat(empty().groupBy(Function.identity())).isEqualTo(LinkedHashMap.empty());
    }

    @TestTemplate
    public void shouldNonNilGroupByIdentity() {
        final Map<?, ?> actual = of('a', 'b', 'c').groupBy(Function.identity());
        final Map<?, ?> expected = LinkedHashMap.empty().put('a', of('a')).put('b', of('b')).put('c', of('c'));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldNonNilGroupByEqual() {
        final Map<?, ?> actual = of('a', 'b', 'c').groupBy(c -> 1);
        final Map<?, ?> expected = LinkedHashMap.empty().put(1, of('a', 'b', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- arrangeBy

    @TestTemplate
    public void shouldNilArrangeBy() {
        assertThat(empty().arrangeBy(Function.identity())).isEqualTo(Option.of(LinkedHashMap.empty()));
    }

    @TestTemplate
    public void shouldNonNilArrangeByIdentity() {
        final Option<Map<Character, Character>> actual = of('a', 'b', 'c').arrangeBy(Function.identity());
        final Option<Map<?, ?>> expected = Option.of(LinkedHashMap.empty().put('a', 'a').put('b', 'b').put('c', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldNonNilArrangeByEqual() {
        final Option<Map<Integer, Character>> actual = of('a', 'b', 'c').arrangeBy(c -> 1);
        final Option<Map<?, ?>> expected = Option.none();
        assertThat(actual).isEqualTo(expected);
    }

    // -- grouped

    @TestTemplate
    public void shouldGroupedNil() {
        assertThat(empty().grouped(1).isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldThrowWhenGroupedWithSizeZero() {
        assertThrows(IllegalArgumentException.class, () -> empty().grouped(0));
    }

    @TestTemplate
    public void shouldThrowWhenGroupedWithNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().grouped(-1));
    }

    @TestTemplate
    public void shouldGroupedTraversableWithEqualSizedBlocks() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4).grouped(2).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(3, 4));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldGroupedTraversableWithRemainder() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5).grouped(2).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(3, 4), io.vavr.collection.Vector.of(5));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldGroupedWhenTraversableLengthIsSmallerThanBlockSize() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4).grouped(5).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2, 3, 4));
        assertThat(actual).isEqualTo(expected);
    }

    // -- hasDefiniteSize

    @TestTemplate
    public void shouldReturnSomethingOnHasDefiniteSize() {
        empty().hasDefiniteSize();
    }

    // -- head

    @TestTemplate
    public void shouldThrowWhenHeadOnNil() {
        assertThrows(NoSuchElementException.class, () -> empty().head());
    }

    @TestTemplate
    public void shouldReturnHeadOfNonNil() {
        assertThat(of(1, 2, 3).head()).isEqualTo(1);
    }

    // -- headOption

    @TestTemplate
    public void shouldReturnNoneWhenCallingHeadOptionOnNil() {
        assertThat(empty().headOption().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldReturnSomeHeadWhenCallingHeadOptionOnNonNil() {
        assertThat(of(1, 2, 3).headOption()).isEqualTo(Option.some(1));
    }

    // -- init

    @TestTemplate
    public void shouldThrowWhenInitOfNil() {
        assertThrows(UnsupportedOperationException.class, () -> empty().init());
    }

    @TestTemplate
    public void shouldGetInitOfNonNil() {
        assertThat(of(1, 2, 3).init()).isEqualTo(of(1, 2));
    }

    // -- initOption

    @TestTemplate
    public void shouldReturnNoneWhenCallingInitOptionOnNil() {
        assertThat(empty().initOption().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        assertThat(of(1, 2, 3).initOption()).isEqualTo(Option.some(of(1, 2)));
    }

    // -- isEmpty

    @TestTemplate
    public void shouldRecognizeNil() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonNil() {
        assertThat(of(1).isEmpty()).isFalse();
    }

    // -- iterator

    @TestTemplate
    public void shouldNotHasNextWhenNilIterator() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @TestTemplate
    public void shouldThrowOnNextWhenNilIterator() {
        assertThrows(NoSuchElementException.class, () -> empty().iterator().next());
    }

    @TestTemplate
    public void shouldIterateFirstElementOfNonNil() {
        assertThat(of(1, 2, 3).iterator().next()).isEqualTo(1);
    }

    @TestTemplate
    public void shouldFullyIterateNonNil() {
        final io.vavr.collection.Iterator<Integer> iterator = of(1, 2, 3).iterator();
        int actual;
        for (int i = 1; i <= 3; i++) {
            actual = iterator.next();
            assertThat(actual).isEqualTo(i);
        }
        assertThat(iterator.hasNext()).isFalse();
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextOnEmptyIterator() {
        assertThatThrownBy(() -> empty().iterator().next()).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextTooOftenOnNonEmptyIterator() {
        final io.vavr.collection.Iterator<Integer> iterator = of(1).iterator();
        assertThatThrownBy(() -> {
            iterator.next();
            iterator.next();
        }).isInstanceOf(NoSuchElementException.class);
    }

    // -- mkString()

    @TestTemplate
    public void shouldMkStringNil() {
        assertThat(empty().mkString()).isEqualTo("");
    }

    @TestTemplate
    public void shouldMkStringNonNil() {
        assertThat(of('a', 'b', 'c').mkString()).isEqualTo("abc");
    }

    // -- mkString(delimiter)

    @TestTemplate
    public void shouldMkStringWithDelimiterNil() {
        assertThat(empty().mkString(",")).isEqualTo("");
    }

    @TestTemplate
    public void shouldMkStringWithDelimiterNonNil() {
        assertThat(of('a', 'b', 'c').mkString(",")).isEqualTo("a,b,c");
    }

    // -- mkString(delimiter, prefix, suffix)

    @TestTemplate
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkString("[", ",", "]")).isEqualTo("[]");
    }

    @TestTemplate
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(of('a', 'b', 'c').mkString("[", ",", "]")).isEqualTo("[a,b,c]");
    }

    // -- mkCharSeq()

    @TestTemplate
    public void shouldMkCharSeqNil() {
        assertThat(empty().mkCharSeq()).isEqualTo(CharSeq.empty());
    }

    @TestTemplate
    public void shouldMkCharSeqNonNil() {
        assertThat(of('a', 'b', 'c').mkCharSeq()).isEqualTo(CharSeq.of("abc"));
    }

    // -- mkCharSeq(delimiter)

    @TestTemplate
    public void shouldMkCharSeqWithDelimiterNil() {
        assertThat(empty().mkCharSeq(",")).isEqualTo(CharSeq.empty());
    }

    @TestTemplate
    public void shouldMkCharSeqWithDelimiterNonNil() {
        assertThat(of('a', 'b', 'c').mkCharSeq(",")).isEqualTo(CharSeq.of("a,b,c"));
    }

    // -- mkCharSeq(delimiter, prefix, suffix)

    @TestTemplate
    public void shouldMkCharSeqWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkCharSeq("[", ",", "]")).isEqualTo(CharSeq.of("[]"));
    }

    @TestTemplate
    public void shouldMkCharSeqWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(of('a', 'b', 'c').mkCharSeq("[", ",", "]")).isEqualTo(CharSeq.of("[a,b,c]"));
    }

    // -- last

    @TestTemplate
    public void shouldThrowWhenLastOnNil() {
        assertThrows(NoSuchElementException.class, () -> empty().last());
    }

    @TestTemplate
    public void shouldReturnLastOfNonNil() {
        assertThat(of(1, 2, 3).last()).isEqualTo(3);
    }

    // -- lastOption

    @TestTemplate
    public void shouldReturnNoneWhenCallingLastOptionOnNil() {
        assertThat(empty().lastOption().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldReturnSomeLastWhenCallingLastOptionOnNonNil() {
        assertThat(of(1, 2, 3).lastOption()).isEqualTo(Option.some(3));
    }

    // -- length

    @TestTemplate
    public void shouldComputeLengthOfNil() {
        assertThat(empty().length()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldComputeLengthOfNonNil() {
        assertThat(of(1, 2, 3).length()).isEqualTo(3);
    }

    // -- max

    @TestTemplate
    public void shouldReturnNoneWhenComputingMaxOfNil() {
        assertThat(empty().max()).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldComputeMaxOfOneValue() {
        assertThat(of(5).max()).isEqualTo(Option.some(5));
    }

    @TestTemplate
    public void shouldComputeMaxOfStrings() {
        assertThat(of("1", "2", "3").max()).isEqualTo(Option.some("3"));
    }

    @TestTemplate
    public void shouldComputeMaxOfBoolean() {
        assertThat(of(true, false).max()).isEqualTo(Option.some(true));
    }

    @TestTemplate
    public void shouldComputeMaxOfByte() {
        assertThat(of((byte) 1, (byte) 2).max()).isEqualTo(Option.some((byte) 2));
    }

    @TestTemplate
    public void shouldComputeMaxOfChar() {
        assertThat(of('a', 'b', 'c').max()).isEqualTo(Option.some('c'));
    }

    @TestTemplate
    public void shouldComputeMaxOfDouble() {
        assertThat(of(.1, .2, .3).max()).isEqualTo(Option.some(.3));
    }

    @TestTemplate
    public void shouldComputeMaxOfFloat() {
        assertThat(of(.1f, .2f, .3f).max()).isEqualTo(Option.some(.3f));
    }

    @TestTemplate
    public void shouldComputeMaxOfInt() {
        assertThat(of(1, 2, 3).max()).isEqualTo(Option.some(3));
    }

    @TestTemplate
    public void shouldComputeMaxOfLong() {
        assertThat(of(1L, 2L, 3L).max()).isEqualTo(Option.some(3L));
    }

    @TestTemplate
    public void shouldComputeMaxOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).max()).isEqualTo(Option.some((short) 3));
    }

    @TestTemplate
    public void shouldComputeMaxOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).max()).isEqualTo(Option.some(BigInteger.ONE));
    }

    @TestTemplate
    public void shouldComputeMaxOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).max()).isEqualTo(Option.some(BigDecimal.ONE));
    }

    @TestTemplate
    public void shouldThrowNPEWhenMaxOfNullAndInt() {
        assertThrows(NullPointerException.class, () -> of(null, 1).max());
    }

    @TestTemplate
    public void shouldThrowNPEWhenMaxOfIntAndNull() {
        assertThrows(NullPointerException.class, () -> of(1, null).max());
    }

    @TestTemplate
    public void shouldCalculateMaxOfDoublesContainingNaN() {
        assertThat(of(1.0, Double.NaN, 2.0).max().get()).isNaN();
    }

    @TestTemplate
    public void shouldCalculateMaxOfFloatsContainingNaN() {
        assertThat(of(1.0f, Float.NaN, 2.0f).max().get()).isEqualTo(Float.NaN);
    }

    @TestTemplate
    public void shouldThrowClassCastExceptionWhenTryingToCalculateMaxOfDoubleAndFloat() {
        assertThatThrownBy(() -> this.<Number>of(1.0, 1.0f).max()).isInstanceOf(ClassCastException.class);
    }

    @TestTemplate
    public void shouldCalculateMaxOfDoublePositiveAndNegativeInfinity() {
        assertThat(of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).max()
          .get()).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @TestTemplate
    public void shouldCalculateMaxOfFloatPositiveAndNegativeInfinity() {
        assertThat(of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY).max().get()).isEqualTo(Float.POSITIVE_INFINITY);
    }

    // -- maxBy(Comparator)

    @TestTemplate
    public void shouldThrowWhenMaxByWithNullComparator() {
        assertThrows(NullPointerException.class, () -> of(1).maxBy((Comparator<Integer>) null));
    }

    @TestTemplate
    public void shouldThrowWhenMaxByOfNil() {
        assertThat(empty().maxBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldCalculateMaxByOfInts() {
        assertThat(of(1, 2, 3).maxBy(comparingInt(i -> i))).isEqualTo(Option.some(3));
    }

    @TestTemplate
    public void shouldCalculateInverseMaxByOfInts() {
        assertThat(of(1, 2, 3).maxBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some(1));
    }

    // -- maxBy(Function)

    @TestTemplate
    public void shouldThrowWhenMaxByWithNullFunction() {
        assertThrows(NullPointerException.class, () -> of(1).maxBy((Function<Integer, Integer>) null));
    }

    @TestTemplate
    public void shouldThrowWhenMaxByFunctionOfNil() {
        assertThat(this.<Integer>empty().maxBy(i -> i)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldCalculateMaxByFunctionOfInts() {
        assertThat(of(1, 2, 3).maxBy(i -> i)).isEqualTo(Option.some(3));
    }

    @TestTemplate
    public void shouldCalculateInverseMaxByFunctionOfInts() {
        assertThat(of(1, 2, 3).maxBy(i -> -i)).isEqualTo(Option.some(1));
    }

    @TestTemplate
    public void shouldCallMaxFunctionOncePerElement() {
        final int[] cnt = {0};
        assertThat(of(1, 2, 3).maxBy(i -> {
            cnt[0]++;
            return i;
        })).isEqualTo(Option.some(3));
        assertThat(cnt[0]).isEqualTo(3);
    }

    // -- min

    @TestTemplate
    public void shouldReturnNoneWhenComputingMinOfNil() {
        assertThat(empty().min()).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldComputeMinOfOneValue() {
        assertThat(of(5).min()).isEqualTo(Option.some(5));
    }

    @TestTemplate
    public void shouldComputeMinOfStrings() {
        assertThat(of("1", "2", "3").min()).isEqualTo(Option.some("1"));
    }

    @TestTemplate
    public void shouldComputeMinOfBoolean() {
        assertThat(of(true, false).min()).isEqualTo(Option.some(false));
    }

    @TestTemplate
    public void shouldComputeMinOfByte() {
        assertThat(of((byte) 1, (byte) 2).min()).isEqualTo(Option.some((byte) 1));
    }

    @TestTemplate
    public void shouldComputeMinOfChar() {
        assertThat(of('a', 'b', 'c').min()).isEqualTo(Option.some('a'));
    }

    @TestTemplate
    public void shouldComputeMinOfDouble() {
        assertThat(of(.1, .2, .3).min()).isEqualTo(Option.some(.1));
    }

    @TestTemplate
    public void shouldComputeMinOfFloat() {
        assertThat(of(.1f, .2f, .3f).min()).isEqualTo(Option.some(.1f));
    }

    @TestTemplate
    public void shouldComputeMinOfInt() {
        assertThat(of(1, 2, 3).min()).isEqualTo(Option.some(1));
    }

    @TestTemplate
    public void shouldComputeMinOfLong() {
        assertThat(of(1L, 2L, 3L).min()).isEqualTo(Option.some(1L));
    }

    @TestTemplate
    public void shouldComputeMinOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).min()).isEqualTo(Option.some((short) 1));
    }

    @TestTemplate
    public void shouldComputeMinOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).min()).isEqualTo(Option.some(BigInteger.ZERO));
    }

    @TestTemplate
    public void shouldComputeMinOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).min()).isEqualTo(Option.some(BigDecimal.ZERO));
    }

    @TestTemplate
    public void shouldThrowNPEWhenMinOfNullAndInt() {
        assertThrows(NullPointerException.class, () -> of(null, 1).min());
    }

    @TestTemplate
    public void shouldThrowNPEWhenMinOfIntAndNull() {
        assertThrows(NullPointerException.class, () -> of(1, null).min());
    }

    @TestTemplate
    public void shouldCalculateMinOfDoublesContainingNaN() {
        assertThat(of(1.0, Double.NaN, 2.0).min().get()).isNaN();
    }

    @TestTemplate
    public void shouldCalculateMinOfFloatsContainingNaN() {
        assertThat(of(1.0f, Float.NaN, 2.0f).min().get()).isEqualTo(Float.NaN);
    }

    @TestTemplate
    public void shouldThrowClassCastExceptionWhenTryingToCalculateMinOfDoubleAndFloat() {
        assertThatThrownBy(() -> this.<Number>of(1.0, 1.0f).min()).isInstanceOf(ClassCastException.class);
    }

    @TestTemplate
    public void shouldCalculateMinOfDoublePositiveAndNegativeInfinity() {
        assertThat(of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).min()
          .get()).isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @TestTemplate
    public void shouldCalculateMinOfFloatPositiveAndNegativeInfinity() {
        assertThat(of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).min()
          .get()).isEqualTo(Double.NEGATIVE_INFINITY);
    }

    // -- minBy(Comparator)

    @TestTemplate
    public void shouldThrowWhenMinByWithNullComparator() {
        assertThrows(NullPointerException.class, () -> of(1).minBy((Comparator<Integer>) null));
    }

    @TestTemplate
    public void shouldThrowWhenMinByOfNil() {
        assertThat(empty().minBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldCalculateMinByOfInts() {
        assertThat(of(1, 2, 3).minBy(comparingInt(i -> i))).isEqualTo(Option.some(1));
    }

    @TestTemplate
    public void shouldCalculateInverseMinByOfInts() {
        assertThat(of(1, 2, 3).minBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some(3));
    }

    // -- minBy(Function)

    @TestTemplate
    public void shouldThrowWhenMinByWithNullFunction() {
        assertThrows(NullPointerException.class, () -> of(1).minBy((Function<Integer, Integer>) null));
    }

    @TestTemplate
    public void shouldThrowWhenMinByFunctionOfNil() {
        assertThat(this.<Integer>empty().minBy(i -> i)).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldCalculateMinByFunctionOfInts() {
        assertThat(of(1, 2, 3).minBy(i -> i)).isEqualTo(Option.some(1));
    }

    @TestTemplate
    public void shouldCalculateInverseMinByFunctionOfInts() {
        assertThat(of(1, 2, 3).minBy(i -> -i)).isEqualTo(Option.some(3));
    }

    @TestTemplate
    public void shouldCallMinFunctionOncePerElement() {
        final int[] cnt = {0};
        assertThat(of(1, 2, 3).minBy(i -> {
            cnt[0]++;
            return i;
        })).isEqualTo(Option.some(1));
        assertThat(cnt[0]).isEqualTo(3);
    }

    // -- nonEmpty

    @TestTemplate
    public void shouldCalculateNonEmpty() {
        assertThat(empty().nonEmpty()).isFalse();
        assertThat(of(1).nonEmpty()).isTrue();
    }

    // -- orElse

    @TestTemplate
    public void shouldCaclEmptyOrElseSameOther() {
        final Iterable<Integer> other = of(42);
        assertThat(empty().orElse(other)).isSameAs(other);
    }

    @TestTemplate
    public void shouldCaclEmptyOrElseEqualOther() {
        assertThat(empty().orElse(Arrays.asList(1, 2))).isEqualTo(of(1, 2));
    }

    @TestTemplate
    public void shouldCaclNonemptyOrElseOther() {
        final Traversable<Integer> src = of(42);
        assertThat(src.orElse(List.of(1))).isSameAs(src);
    }

    @TestTemplate
    public void shouldCaclEmptyOrElseSameSupplier() {
        final Iterable<Integer> other = of(42);
        final Supplier<Iterable<Integer>> supplier = () -> other;
        assertThat(empty().orElse(supplier)).isSameAs(other);
    }

    @TestTemplate
    public void shouldCaclEmptyOrElseEqualSupplier() {
        assertThat(empty().orElse(() -> Arrays.asList(1, 2))).isEqualTo(of(1, 2));
    }

    @TestTemplate
    public void shouldCaclNonemptyOrElseSupplier() {
        final Traversable<Integer> src = of(42);
        assertThat(src.orElse(() -> List.of(1))).isSameAs(src);
    }

    // -- partition

    @TestTemplate
    public void shouldThrowWhenPartitionNilAndPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> empty().partition(null));
    }

    @TestTemplate
    public void shouldPartitionNil() {
        assertThat(empty().partition(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @TestTemplate
    public void shouldPartitionIntsInOddAndEvenHavingOddAndEvenNumbers() {
        assertThat(of(1, 2, 3, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(of(1, 3), of(2, 4)));
    }

    @TestTemplate
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
        assertThat(of(1, 3).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(of(1, 3), empty()));
    }

    @TestTemplate
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        assertThat(of(2, 4).partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(empty(), of(2, 4)));
    }

    // -- product

    @TestTemplate
    public void shouldComputeProductOfNil() {
        assertThat(empty().product()).isEqualTo(1);
    }

    @TestTemplate
    public void shouldThrowWhenComputingProductOfStrings() {
        assertThrows(UnsupportedOperationException.class, () -> of("1", "2", "3").product());
    }

    @TestTemplate
    public void shouldComputeProductOfByte() {
        assertThat(of((byte) 1, (byte) 2).product()).isEqualTo(2L);
    }

    @TestTemplate
    public void shouldComputeProductOfDouble() {
        assertThat(of(.1, .2, .3).product().doubleValue()).isEqualTo(.006, within(10e-18));
    }

    @TestTemplate
    public void shouldComputeProductOfFloat() {
        assertThat(of(.1f, .2f, .3f).product().doubleValue()).isEqualTo(.006, within(10e-10));
    }

    @TestTemplate
    public void shouldComputeProductOfInt() {
        assertThat(of(1, 2, 3).product()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeProductOfLong() {
        assertThat(of(1L, 2L, 3L).product()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeProductOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).product()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeProductOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).product()).isEqualTo(BigInteger.ZERO);
    }

    @TestTemplate
    public void shouldComputeProductOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).product()).isEqualTo(BigDecimal.ZERO);
    }

    // -- reduceOption

    @TestTemplate
    public void shouldThrowWhenReduceOptionNil() {
        assertThat(this.<String>empty().reduceOption((a, b) -> a + b)).isSameAs(Option.none());
    }

    @TestTemplate
    public void shouldThrowWhenReduceOptionNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduceOption(null));
    }

    @TestTemplate
    public void shouldReduceOptionNonNil() {
        assertThat(of(1, 2, 3).reduceOption((a, b) -> a + b)).isEqualTo(Option.of(6));
    }

    // -- reduce

    @TestTemplate
    public void shouldThrowWhenReduceNil() {
        assertThrows(NoSuchElementException.class, () -> this.<String>empty().reduce((a, b) -> a + b));
    }

    @TestTemplate
    public void shouldThrowWhenReduceNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduce(null));
    }

    @TestTemplate
    public void shouldReduceNonNil() {
        assertThat(of(1, 2, 3).reduce((a, b) -> a + b)).isEqualTo(6);
    }

    // -- reduceLeftOption

    @TestTemplate
    public void shouldThrowWhenReduceLeftOptionNil() {
        assertThat(this.<String>empty().reduceLeftOption((a, b) -> a + b)).isSameAs(Option.none());
    }

    @TestTemplate
    public void shouldThrowWhenReduceLeftOptionNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduceLeftOption(null));
    }

    @TestTemplate
    public void shouldReduceLeftOptionNonNil() {
        assertThat(of("a", "b", "c").reduceLeftOption((xs, x) -> xs + x)).isEqualTo(Option.of("abc"));
    }

    // -- reduceLeft

    @TestTemplate
    public void shouldThrowWhenReduceLeftNil() {
        assertThrows(NoSuchElementException.class, () -> this.<String>empty().reduceLeft((a, b) -> a + b));
    }

    @TestTemplate
    public void shouldThrowWhenReduceLeftNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduceLeft(null));
    }

    @TestTemplate
    public void shouldReduceLeftNonNil() {
        assertThat(of("a", "b", "c").reduceLeft((xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- reduceRightOption

    @TestTemplate
    public void shouldThrowWhenReduceRightOptionNil() {
        assertThat(this.<String>empty().reduceRightOption((a, b) -> a + b)).isSameAs(Option.none());
    }

    @TestTemplate
    public void shouldThrowWhenReduceRightOptionNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduceRightOption(null));
    }

    @TestTemplate
    public void shouldReduceRightOptionNonNil() {
        assertThat(of("a", "b", "c").reduceRightOption((x, xs) -> x + xs)).isEqualTo(Option.of("abc"));
    }

    // -- reduceRight

    @TestTemplate
    public void shouldThrowWhenReduceRightNil() {
        assertThrows(NoSuchElementException.class, () -> this.<String>empty().reduceRight((a, b) -> a + b));
    }

    @TestTemplate
    public void shouldThrowWhenReduceRightNullOperator() {
        assertThrows(NullPointerException.class, () -> this.<String>empty().reduceRight(null));
    }

    @TestTemplate
    public void shouldReduceRightNonNil() {
        assertThat(of("a", "b", "c").reduceRight((x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- replace(curr, new)

    @TestTemplate
    public void shouldReplaceElementOfNilUsingCurrNew() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(this.<Integer>empty().replace(1, 2)).isEqualTo(empty());
        } else {
            assertThat(this.<Integer>empty().replace(1, 2)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReplaceFirstOccurrenceOfNonNilUsingCurrNewWhenMultipleOccurrencesExist() {
        final Traversable<Integer> testee = of(0, 1, 2, 1);
        final Traversable<Integer> actual = testee.replace(1, 3);
        final Traversable<Integer> expected = of(0, 3, 2, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldReplaceElementOfNonNilUsingCurrNewWhenOneOccurrenceExists() {
        assertThat(of(0, 1, 2).replace(1, 3)).isEqualTo(of(0, 3, 2));
    }

    @TestTemplate
    public void shouldReplaceElementOfNonNilUsingCurrNewWhenNoOccurrenceExists() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(0, 1, 2).replace(33, 3)).isEqualTo(of(0, 1, 2));
        } else {
            final Traversable<Integer> src = of(0, 1, 2);
            assertThat(src.replace(33, 3)).isSameAs(src);
        }
    }

    // -- replaceAll(curr, new)

    @TestTemplate
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(this.<Integer>empty().replaceAll(1, 2)).isEqualTo(empty());
        } else {
            assertThat(this.<Integer>empty().replaceAll(1, 2)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldReplaceAllElementsOfNonNilUsingCurrNonExistingNew() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(0, 1, 2, 1).replaceAll(33, 3)).isEqualTo(of(0, 1, 2, 1));
        } else {
            final Traversable<Integer> src = of(0, 1, 2, 1);
            assertThat(src.replaceAll(33, 3)).isSameAs(src);
        }
    }

    @TestTemplate
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(of(0, 1, 2, 1).replaceAll(1, 3)).isEqualTo(of(0, 3, 2, 3));
    }

    // -- retainAll

    @TestTemplate
    public void shouldRetainAllElementsFromNil() {
        final Traversable<Object> empty = empty();
        final Traversable<Object> actual = empty.retainAll(of(1, 2, 3));
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(actual).isEqualTo(empty);
        } else {
            assertThat(actual).isSameAs(empty);
        }
    }

    @TestTemplate
    public void shouldRetainAllExistingElementsFromNonNil() {
        final Traversable<Integer> src = of(1, 2, 3, 2, 1, 3);
        final Traversable<Integer> expected = of(1, 2, 2, 1);
        final Traversable<Integer> actual = src.retainAll(of(1, 2));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldRetainAllElementsFromNonNil() {
        final Traversable<Integer> src = of(1, 2, 1, 2, 2);
        final Traversable<Integer> expected = of(1, 2, 1, 2, 2);
        final Traversable<Integer> actual = src.retainAll(of(1, 2));
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(actual).isEqualTo(expected);
        } else {
            assertThat(actual).isSameAs(src);
        }
    }

    @TestTemplate
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        final Traversable<Integer> src = of(1, 2, 3);
        final Traversable<Object> expected = empty();
        final Traversable<Integer> actual = src.retainAll(of(4, 5));
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(actual).isEqualTo(expected);
        } else {
            assertThat(actual).isSameAs(expected);
        }
    }

    // -- scan, scanLeft, scanRight

    @TestTemplate
    public void shouldScanEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scan(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(this.of(0));
    }

    @TestTemplate
    public void shouldScanLeftEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scanLeft(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(of(0));
    }

    @TestTemplate
    public void shouldScanRightEmpty() {
        final Traversable<Integer> testee = empty();
        final Traversable<Integer> actual = testee.scanRight(0, (s1, s2) -> s1 + s2);
        assertThat(actual).isEqualTo(of(0));
    }

    @TestTemplate
    public void shouldScanNonEmpty() {
        final Traversable<Integer> testee = of(1, 2, 3);
        final Traversable<Integer> actual = testee.scan(0, (acc, s) -> acc + s);
        assertThat(actual).isEqualTo(of(0, 1, 3, 6));
    }

    @TestTemplate
    public void shouldScanLeftNonEmpty() {
        final Traversable<Integer> testee = of(1, 2, 3);
        final Traversable<String> actual = testee.scanLeft("x", (acc, i) -> acc + i);
        assertThat(actual).isEqualTo(of("x", "x1", "x12", "x123"));
    }

    @TestTemplate
    public void shouldScanRightNonEmpty() {
        final Traversable<Integer> testee = of(1, 2, 3);
        final Traversable<String> actual = testee.scanRight("x", (i, acc) -> acc + i);
        assertThat(actual).isEqualTo(of("x321", "x32", "x3", "x"));
    }

    @TestTemplate
    public void shouldScanWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scan(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.of("x", "xa").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(expected).containsAll(actual);
        assertThat(actual.length()).isEqualTo(expected.length());
    }

    @TestTemplate
    public void shouldScanLeftWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scanLeft(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.of("x", "xa").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(expected).containsAll(actual);
        assertThat(actual.length()).isEqualTo(expected.length());
    }

    @TestTemplate
    public void shouldScanRightWithNonComparable() {
        final Traversable<NonComparable> testee = of(new NonComparable("a"));
        final List<NonComparable> actual = List.ofAll(testee.scanRight(new NonComparable("x"), (u1, u2) -> new NonComparable(u1.value + u2.value)));
        final List<NonComparable> expected = List.of("ax", "x").map(NonComparable::new);
        assertThat(actual).containsAll(expected);
        assertThat(expected).containsAll(actual);
        assertThat(actual.length()).isEqualTo(expected.length());
    }

    // -- slideBy(classifier)

    @TestTemplate
    public void shouldSlideNilByClassifier() {
        assertThat(empty().slideBy(Function.identity())).isEmpty();
    }

    @TestTemplate
    public void shouldTerminateSlideByClassifier() {
        assertTimeout(Duration.ofSeconds(1),() -> {
            AtomicInteger ai = new AtomicInteger(0);
            List<List<String>> expected = List.of(List.of("a", "-"), List.of("-"), List.of("d"));
            List<List<String>> actual = List.of("a", "-", "-", "d")
              .slideBy(x -> x.equals("-") ? ai.getAndIncrement() : ai.get())
              .toList();
            assertThat(actual).containsAll(expected);
            assertThat(expected).containsAll(actual);
        });
    }

    @TestTemplate
    public void shouldSlideSingularByClassifier() {
        final List<Traversable<Integer>> actual = of(1).slideBy(Function.identity()).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlideNonNilByIdentityClassifier() {
        final List<Traversable<Integer>> actual = of(1, 2, 3).slideBy(Function.identity()).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1), io.vavr.collection.Vector.of(2), io.vavr.collection.Vector.of(3));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlideNonNilByConstantClassifier() {
        final List<Traversable<Integer>> actual = of(1, 2, 3).slideBy(e -> "same").toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2, 3));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlideNonNilBySomeClassifier() {
        final List<Traversable<Integer>> actual = of(10, 20, 30, 42, 52, 60, 72).slideBy(e -> e % 10).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(10, 20, 30), io.vavr.collection.Vector.of(42, 52), io.vavr.collection.Vector.of(60), io.vavr.collection.Vector.of(72));
        assertThat(actual).isEqualTo(expected);
    }

    // -- sliding(size)

    @TestTemplate
    public void shouldThrowWhenSlidingNilByZeroSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().sliding(0));
    }

    @TestTemplate
    public void shouldThrowWhenSlidingNilByNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().sliding(-1));
    }

    @TestTemplate
    public void shouldThrowWhenSlidingNonNilByZeroSize() {
        assertThrows(IllegalArgumentException.class, () -> of(1).sliding(0));
    }

    @TestTemplate
    public void shouldThrowWhenSlidingNonNilByNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> of(1).sliding(-1));
    }

    @TestTemplate
    public void shouldSlideNilBySize() {
        assertThat(empty().sliding(1)).isEmpty();
    }

    @TestTemplate
    public void shouldSlideNonNilBySize1() {
        final List<Traversable<Integer>> actual = of(1, 2, 3).sliding(1).toList().map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1), io.vavr.collection.Vector.of(2), io.vavr.collection.Vector.of(3));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlideNonNilBySize2() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5).sliding(2).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(2, 3), io.vavr.collection.Vector.of(3, 4), io.vavr.collection.Vector.of(4, 5));
        assertThat(actual).isEqualTo(expected);
    }

    // -- sliding(size, step)

    @TestTemplate
    public void shouldThrowWhenSlidingNilByPositiveStepAndNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().sliding(-1, 1));
    }

    @TestTemplate
    public void shouldThrowWhenSlidingNilByNegativeStepAndNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().sliding(-1, -1));
    }

    @TestTemplate
    public void shouldThrowWhenSlidingNilByNegativeStepAndPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> empty().sliding(1, -1));
    }

    @TestTemplate
    public void shouldSlideNilBySizeAndStep() {
        assertThat(empty().sliding(1, 1).isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldSlide5ElementsBySize2AndStep3() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5).sliding(2, 3).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(4, 5));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlide5ElementsBySize2AndStep4() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5).sliding(2, 4).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(5));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlide5ElementsBySize2AndStep5() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5).sliding(2, 5).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlide4ElementsBySize5AndStep3() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4).sliding(5, 3).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2, 3, 4));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlide7ElementsBySize1AndStep3() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5, 6, 7).sliding(1, 3).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1), io.vavr.collection.Vector.of(4), io.vavr.collection.Vector.of(7));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldSlide7ElementsBySize2AndStep3() {
        final List<Traversable<Integer>> actual = of(1, 2, 3, 4, 5, 6, 7).sliding(2, 3).toList()
          .map(io.vavr.collection.Vector::ofAll);
        final List<Traversable<Integer>> expected = List.of(io.vavr.collection.Vector.of(1, 2), io.vavr.collection.Vector.of(4, 5), io.vavr.collection.Vector.of(7));
        assertThat(actual).isEqualTo(expected);
    }

    // -- span

    @TestTemplate
    public void shouldSpanNil() {
        assertThat(this.<Integer>empty().span(i -> i < 2)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @TestTemplate
    public void shouldSpanNonNil() {
        assertThat(of(0, 1, 2, 3).span(i -> i < 2)).isEqualTo(Tuple.of(of(0, 1), of(2, 3)));
    }

    @TestTemplate
    public void shouldSpanAndNotTruncate() {
        assertThat(of(1, 1, 2, 2, 3, 3).span(x -> x % 2 == 1)).isEqualTo(Tuple.of(of(1, 1), of(2, 2, 3, 3)));
        assertThat(of(1, 1, 2, 2, 4, 4).span(x -> x == 1)).isEqualTo(Tuple.of(of(1, 1), of(2, 2, 4, 4)));
    }

    // -- spliterator

    @TestTemplate
    public void shouldSplitNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        this.<Integer>empty().spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEmpty();
    }

    @TestTemplate
    public void shouldSplitNonNil() {
        final java.util.List<Integer> actual = new java.util.ArrayList<>();
        of(1, 2, 3).spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(asList(1, 2, 3));
    }

    @TestTemplate
    public void shouldHaveImmutableSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
    }

    // -- stderr

    @TestTemplate
    public void shouldHandleStderrIOException() {
        assertThrows(IllegalStateException.class, () -> withFailingErrOut(() -> of(0).stderr()));
    }

    // -- stdout

    @TestTemplate
    public void shouldHandleStdoutIOException() {
        assertThrows(IllegalStateException.class, () -> withFailingStdOut(() -> of(0).stdout()));
    }

    // -- PrintStream

    @TestTemplate
    public void shouldWriteToPrintStream() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        final PrintStream out = new PrintStream(baos);
        of(1, 2, 3).out(out);
        assertThat(baos.toString()).isEqualTo(of(1, 2, 3).mkString("", lineSeparator(), lineSeparator()));
    }

    @TestTemplate
    public void shouldHandlePrintStreamIOException() {
        assertThrows(IllegalStateException.class, () -> {
            try (PrintStream failingPrintStream = failingPrintStream()) {
                of(0).out(failingPrintStream);
            }
        });
    }

    // -- PrintWriter

    @TestTemplate
    public void shouldWriteToPrintWriter() {
        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);
        of(1, 2, 3).out(out);
        assertThat(sw.toString()).isEqualTo(of(1, 2, 3).mkString("", lineSeparator(), lineSeparator()));
    }

    @TestTemplate
    public void shouldHandlePrintWriterIOException() {
        assertThrows(IllegalStateException.class, () -> {
            try (PrintWriter failingPrintWriter = failingPrintWriter()) {
                of(0).out(failingPrintWriter);
            }
        });
    }

    // -- sum

    @TestTemplate
    public void shouldComputeSumOfNil() {
        assertThat(empty().sum()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldThrowWhenComputingSumOfStrings() {
        assertThrows(UnsupportedOperationException.class, () -> of("1", "2", "3").sum());
    }

    @TestTemplate
    public void shouldComputeSumOfByte() {
        assertThat(of((byte) 1, (byte) 2).sum()).isEqualTo(3L);
    }

    @TestTemplate
    public void shouldComputeSumOfDouble() {
        assertThat(of(.1, .2, .3).sum().doubleValue()).isEqualTo(.6, within(10e-16));
    }

    @TestTemplate
    public void shouldComputeSumOfFloat() {
        assertThat(of(.1f, .2f, .3f).sum().doubleValue()).isEqualTo(.6, within(10e-8));
    }

    @TestTemplate
    public void shouldComputeSumOfInt() {
        assertThat(of(1, 2, 3).sum()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeSumOfLong() {
        assertThat(of(1L, 2L, 3L).sum()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeSumOfShort() {
        assertThat(of((short) 1, (short) 2, (short) 3).sum()).isEqualTo(6L);
    }

    @TestTemplate
    public void shouldComputeSumOfBigInteger() {
        assertThat(of(BigInteger.ZERO, BigInteger.ONE).sum()).isEqualTo(BigInteger.ONE);
    }

    @TestTemplate
    public void shouldComputeSumOfBigDecimal() {
        assertThat(of(BigDecimal.ZERO, BigDecimal.ONE).sum()).isEqualTo(BigDecimal.ONE);
    }

    // -- take

    @TestTemplate
    public void shouldTakeNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().take(1)).isEqualTo(empty());
        } else {
            assertThat(empty().take(1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeNoneIfCountIsNegative() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).take(-1)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).take(-1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).take(2)).isEqualTo(of(1, 2));
    }

    @TestTemplate
    public void shouldTakeAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).take(4)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.take(4)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceIfTakeAll() {
        final Traversable<?> t = of(1, 2, 3);
        assertThat(t.take(3)).isSameAs(t);
        assertThat(t.take(4)).isSameAs(t);
    }

    // -- takeRight

    @TestTemplate
    public void shouldTakeRightNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().takeRight(1)).isEqualTo(empty());
        } else {
            assertThat(empty().takeRight(1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeRightNoneIfCountIsNegative() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeRight(-1)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).takeRight(-1)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).takeRight(2)).isEqualTo(of(2, 3));
    }

    @TestTemplate
    public void shouldTakeRightAllIfCountExceedsSize() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeRight(4)).isEqualTo(of(1, 2, 3));
        } else {
            final Traversable<Integer> t = of(1, 2, 3);
            assertThat(t.takeRight(4)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldReturnSameInstanceIfTakeRightAll() {
        final Traversable<?> t = of(1, 2, 3);
        assertThat(t.takeRight(3)).isSameAs(t);
        assertThat(t.takeRight(4)).isSameAs(t);
    }

    // -- takeUntil

    @TestTemplate
    public void shouldTakeUntilNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().takeUntil(x -> true)).isEqualTo(empty());
        } else {
            assertThat(empty().takeUntil(x -> true)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeUntilAllOnFalseCondition() {
        final Traversable<Integer> t = of(1, 2, 3);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeUntil(x -> false)).isEqualTo(of(1, 2, 3));
        } else {
            assertThat(t.takeUntil(x -> false)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldTakeUntilAllOnTrueCondition() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeUntil(x -> true)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).takeUntil(x -> true)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeUntilAsExpected() {
        assertThat(of(2, 4, 5, 6).takeUntil(x -> x % 2 != 0)).isEqualTo(of(2, 4));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyTakeUntil() {
        final Traversable<?> empty = empty();
        assertThat(empty.takeUntil(ignored -> false)).isSameAs(empty);
    }

    // -- takeWhile

    @TestTemplate
    public void shouldTakeWhileNoneOnNil() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(empty().takeWhile(x -> true)).isEqualTo(empty());
        } else {
            assertThat(empty().takeWhile(x -> true)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeWhileAllOnFalseCondition() {
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeWhile(x -> false)).isEqualTo(empty());
        } else {
            assertThat(of(1, 2, 3).takeWhile(x -> false)).isSameAs(empty());
        }
    }

    @TestTemplate
    public void shouldTakeWhileAllOnTrueCondition() {
        final Traversable<Integer> t = of(1, 2, 3);
        if (useIsEqualToInsteadOfIsSameAs()) {
            assertThat(of(1, 2, 3).takeWhile(x -> true)).isEqualTo(of(1, 2, 3));
        } else {
            assertThat(t.takeWhile(x -> true)).isSameAs(t);
        }
    }

    @TestTemplate
    public void shouldTakeWhileAsExpected() {
        assertThat(of(2, 4, 5, 6).takeWhile(x -> x % 2 == 0)).isEqualTo(of(2, 4));
    }

    @TestTemplate
    public void shouldReturnSameInstanceWhenEmptyTakeWhile() {
        final Traversable<?> empty = empty();
        assertThat(empty.takeWhile(ignored -> false)).isSameAs(empty);
    }

    // -- tail

    @TestTemplate
    public void shouldThrowWhenTailOnNil() {
        assertThrows(UnsupportedOperationException.class, () -> empty().tail());
    }

    @TestTemplate
    public void shouldReturnTailOfNonNil() {
        assertThat(of(1, 2, 3).tail()).isEqualTo(of(2, 3));
    }

    // -- tailOption

    @TestTemplate
    public void shouldReturnNoneWhenCallingTailOptionOnNil() {
        assertThat(empty().tailOption().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(of(1, 2, 3).tailOption()).isEqualTo(Option.some(of(2, 3)));
    }

    // -- unzip

    @TestTemplate
    public void shouldUnzipNil() {
        assertThat(empty().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(empty(), empty()));
    }

    @TestTemplate
    public void shouldUnzipNonNil() {
        final Tuple actual = of(0, 1).unzip(i -> Tuple.of(i, (char) ((short) 'a' + i)));
        final Tuple expected = Tuple.of(of(0, 1), of('a', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldUnzip3Nil() {
        assertThat(empty().unzip3(x -> Tuple.of(x, x, x))).isEqualTo(Tuple.of(empty(), empty(), empty()));
    }

    @TestTemplate
    public void shouldUnzip3NonNil() {
        final Tuple actual = of(0, 1).unzip3(i -> Tuple.of(i, (char) ((short) 'a' + i), (char) ((short) 'a' + i + 1)));
        final Tuple expected = Tuple.of(of(0, 1), of('a', 'b'), of('b', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @TestTemplate
    public void shouldZipNils() {
        final Traversable<?> actual = empty().zip(empty());
        assertThat(actual).isEmpty();
    }

    @TestTemplate
    public void shouldZipEmptyAndNonNil() {
        final Traversable<?> actual = empty().zip(of(1));
        assertThat(actual).isEmpty();
    }

    @TestTemplate
    public void shouldZipNonEmptyAndNil() {
        final Traversable<?> actual = of(1).zip(empty());
        assertThat(actual).isEmpty();
    }

    @TestTemplate
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b"));
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipNonNilsOfSameSize() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    @SuppressWarnings("unchecked")
    public void shouldZipWithNonNilsOfSameSize() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2, 3).zipWith(of("a", "b", "c"), Tuple::of);
        final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldThrowIfZipWithThatIsNull() {
        assertThrows(NullPointerException.class, () -> empty().zip(null));
    }

    // -- zipAll

    @TestTemplate
    public void shouldZipAllNils() {
        final Traversable<?> actual = empty().zipAll(empty(), null, null);
        assertThat(actual).isEmpty();
    }

    @TestTemplate
    public void shouldZipAllEmptyAndNonNil() {
        final Traversable<?> actual = empty().zipAll(of(1), null, null);
        final Traversable<Tuple2<Object, Integer>> expected = of(Tuple.of(null, 1));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipAllNonEmptyAndNil() {
        final Traversable<?> actual = of(1).zipAll(empty(), null, null);
        final Traversable<Tuple2<Integer, Object>> expected = of(Tuple.of(1, null));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b"), 9, "z");
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldZipAllNonNilsOfSameSize() {
        final Traversable<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked") final Traversable<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldThrowIfZipAllWithThatIsNull() {
        assertThrows(NullPointerException.class, () -> empty().zipAll(null, null, null));
    }

    // -- zipWithIndex

    @TestTemplate
    public void shouldZipNilWithIndex() {
        assertThat(this.<String>empty().zipWithIndex()).isEqualTo(this.<Tuple2<String, Integer>>empty());
    }

    @TestTemplate
    public void shouldZipNonNilWithIndex() {
        final Traversable<Tuple2<String, Integer>> actual = of("a", "b", "c").zipWithIndex();
        @SuppressWarnings("unchecked") final Traversable<Tuple2<String, Integer>> expected = of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    @SuppressWarnings("unchecked")
    public void shouldZipNonNilWithIndexWithMapper() {
        final Traversable<Tuple2<String, Integer>> actual = of("a", "b", "c").zipWithIndex(Tuple::of);
        final Traversable<Tuple2<String, Integer>> expected = of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
    }

    // -- toJavaArray(IntFunction)

    @TestTemplate
    public void shouldConvertNilToJavaArray() {
        final Integer[] actual = List.<Integer>empty().toJavaArray(Integer[]::new);
        final Integer[] expected = new Integer[]{};
        assertThat(actual).isEqualTo(expected);
    }

    @TestTemplate
    public void shouldConvertNonNilToJavaArray() {
        final Integer[] array = of(1, 2).toJavaArray(Integer[]::new);
        final Integer[] expected = new Integer[]{1, 2};
        assertThat(array).isEqualTo(expected);
    }

    // -- toJavaList

    @TestTemplate
    public void shouldConvertNilToArrayList() {
        assertThat(this.<Integer>empty().toJavaList()).isEqualTo(new ArrayList<Integer>());
    }

    @TestTemplate
    public void shouldConvertNonNilToArrayList() {
        assertThat(of(1, 2, 3).toJavaList()).isEqualTo(asList(1, 2, 3));
    }

    // -- toJavaMap(Function)

    @TestTemplate
    public void shouldConvertNilToHashMap() {
        assertThat(this.<Integer>empty().toJavaMap(x -> Tuple.of(x, x))).isEqualTo(new java.util.HashMap<>());
    }

    @TestTemplate
    public void shouldConvertNonNilToHashMap() {
        final java.util.Map<Integer, Integer> expected = new java.util.HashMap<>();
        expected.put(1, 1);
        expected.put(2, 2);
        assertThat(of(1, 2).toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
    }

    // -- toJavaSet

    @TestTemplate
    public void shouldConvertNilToHashSet() {
        assertThat(this.<Integer>empty().toJavaSet()).isEqualTo(new java.util.HashSet<>());
    }

    @TestTemplate
    public void shouldConvertNonNilToHashSet() {
        final java.util.Set<Integer> expected = new java.util.HashSet<>();
        expected.add(2);
        expected.add(1);
        expected.add(3);
        assertThat(of(1, 2, 2, 3).toJavaSet()).isEqualTo(expected);
    }

    // toTree

    @TestTemplate
    public void shouldConvertToTree() {
        //Value["id:parent")]
        final Traversable<String> value = of(
          "1:",
          "2:1", "3:1",
          "4:2", "5:2", "6:3",
          "7:4", "8:6", "9:6"
        );
        final Seq<Tree<String>> roots = value
          .toTree(s -> s.split(":")[0], s -> s.split(":").length == 1 ? null : s.split(":")[1])
          .map(l -> l.map(s -> s.split(":")[0]));
        assertThat(roots).hasSize(1);
        final Tree<String> root = roots.head();
        if (value.hasDefiniteSize()) {
            assertThat(root).hasSameSizeAs(value);
        }
        assertThat(root.toLispString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8 9)))");
    }

    // ++++++ OBJECT ++++++

    // -- equals

    @SuppressWarnings("EqualsWithItself")
    @TestTemplate
    public void shouldEqualSameTraversableInstance() {
        final Traversable<?> nonEmpty = of(1);
        assertThat(nonEmpty.equals(nonEmpty)).isTrue();
        assertThat(empty().equals(empty())).isTrue();
    }

    @TestTemplate
    public void shouldNilNotEqualsNull() {
        assertThat(empty()).isNotNull();
    }

    @TestTemplate
    public void shouldNonNilNotEqualsNull() {
        assertThat(of(1)).isNotNull();
    }

    @TestTemplate
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(empty()).isNotEqualTo("");
    }

    @TestTemplate
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(of(1)).isNotEqualTo("");
    }

    @TestTemplate
    public void shouldRecognizeEqualityOfNils() {
        assertThat(empty()).isEqualTo(empty());
    }

    @TestTemplate
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 3))).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 4))).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2))).isFalse();
        assertThat(of(1, 2).equals(of(1, 2, 3))).isFalse();
    }

    // -- hashCode

    @TestTemplate
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode() == empty().hashCode()).isTrue();
    }

    @TestTemplate
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(of(1, 2).hashCode() == of(1, 2).hashCode()).isTrue();
    }

    @TestTemplate
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(of(1, 2).hashCode() != of(2, 3).hashCode()).isTrue();
    }

    @TestTemplate
    public void shouldComputeHashCodeOfEmpty() {
        assertThat(empty().hashCode()).isEqualTo(1);
    }

    @TestTemplate
    public void shouldNotThrowStackOverflowErrorWhenCalculatingHashCodeOf1000000Integers() {
        assertThat(ofAll(io.vavr.collection.Iterator.range(0, 1000000)).hashCode()).isNotNull();
    }

    // -- toString

    @TestTemplate
    public void shouldConformEmptyStringRepresentation() {
        final Traversable<Object> testee = empty();
        if (!testee.hasDefiniteSize()) {
            assertThat(testee.toString()).isEqualTo(testee.stringPrefix() + "()");
            testee.size(); // evaluates all elements of lazy collections
        }
        assertThat(testee.toString()).isEqualTo(toString(testee));
    }

    @TestTemplate
    public void shouldConformNonEmptyStringRepresentation() {
        final Traversable<Object> testee = of("a", "b", "c");
        if (isTraversableAgain()) {
            if (!testee.hasDefiniteSize()) {
                assertThat(testee.toString()).isEqualTo(testee.stringPrefix() + "(a, ?)");
                testee.size(); // evaluates all elements of lazy collections
            }
            assertThat(testee.toString()).isEqualTo(toString(testee));
        } else {
            assertThat(testee.toString()).isEqualTo(testee.stringPrefix() + "(?)");
        }
    }

    private static String toString(Traversable<?> traversable) {
        return traversable.mkString(traversable.stringPrefix() + "(", ", ", ")");
    }

    // -- static collector()

    @TestTemplate
    public void shouldStreamAndCollectNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.empty().collect(collector());
            assertThat(actual).isEmpty();
        });
    }

    @TestTemplate
    public void shouldStreamAndCollectNonNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).collect(this.<Object>collector());
            assertThat(actual).isEqualTo(of(1, 2, 3));
        });
    }

    @TestTemplate
    public void shouldParallelStreamAndCollectNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.empty().parallel().collect(collector());
            assertThat(actual).isEmpty();
        });
    }

    @TestTemplate
    public void shouldParallelStreamAndCollectNonNil() {
        testCollector(() -> {
            final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).parallel()
              .collect(this.<Object>collector());
            assertThat(actual).isEqualTo(of(1, 2, 3));
        });
    }

    // -- single

    @TestTemplate
    public void shouldSingleFailEmpty() {
        assertThrows(NoSuchElementException.class, () -> empty().single());
    }

    @TestTemplate
    public void shouldSingleFailTwo() {
        assertThrows(NoSuchElementException.class, () -> of(1, 2).single());
    }

    @TestTemplate
    public void shouldSingleWork() {
        assertThat(of(1).single()).isEqualTo(1);
    }

    // -- singleOption

    @TestTemplate
    public void shouldSingleOptionFailEmpty() {
        assertThat(empty().singleOption()).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldSingleOptionFailTwo() {
        assertThat(of(1, 2).singleOption()).isEqualTo(Option.none());
    }

    @TestTemplate
    public void shouldSingleOptionWork() {
        assertThat(of(1).singleOption()).isEqualTo(Option.of(1));
    }

    @TestTemplate
    public void shouldTabulateTheSeq() {
        final Function<Number, Integer> f = i -> i.intValue() * i.intValue();
        final Traversable<Number> actual = tabulate(3, f);
        assertThat(actual).isEqualTo(of(0, 1, 4));
    }

    @TestTemplate
    public void shouldTabulateTheSeqCallingTheFunctionInTheRightOrder() {
        final java.util.LinkedList<Integer> ints = new java.util.LinkedList<>(asList(0, 1, 2));
        final Function<Integer, Integer> f = i -> ints.remove();
        final Traversable<Integer> actual = tabulate(3, f);
        assertThat(actual).isEqualTo(of(0, 1, 2));
    }

    @TestTemplate
    public void shouldTabulateTheSeqWith0Elements() {
        assertThat(tabulate(0, i -> i)).isEqualTo(empty());
    }

    @TestTemplate
    public void shouldTabulateTheSeqWith0ElementsWhenNIsNegative() {
        assertThat(tabulate(-1, i -> i)).isEqualTo(empty());
    }

    // -- fill(int, Supplier)

    @TestTemplate
    public void shouldFillTheSeqCallingTheSupplierInTheRightOrder() {
        final java.util.LinkedList<Integer> ints = new java.util.LinkedList<>(asList(0, 1));
        final Traversable<Number> actual = fill(2, ints::remove);
        assertThat(actual).isEqualTo(of(0, 1));
    }

    @TestTemplate
    public void shouldFillTheSeqWith0Elements() {
        assertThat(fill(0, () -> 1)).isEqualTo(empty());
    }

    @TestTemplate
    public void shouldFillTheSeqWith0ElementsWhenNIsNegative() {
        assertThat(fill(-1, () -> 1)).isEqualTo(empty());
    }

    @TestTemplate
    public void ofShouldReturnTheSingletonEmpty() {
        if (!emptyShouldBeSingleton()) {
            return;
        }
        assertThat(of()).isSameAs(empty());
    }

    @TestTemplate
    public void ofAllShouldReturnTheSingletonEmpty() {
        if (!emptyShouldBeSingleton()) {
            return;
        }
        assertThat(ofAll(io.vavr.collection.Iterator.empty())).isSameAs(empty());
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
