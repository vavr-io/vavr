/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static javaslang.collection.Iterator.*;

public class IteratorTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @SuppressWarnings("unchecked")
            @Override
            public IterableAssert<T> isEqualTo(Object expected) {
                if (actual instanceof Option) {
                    final Option<?> opt1 = ((Option<?>) actual);
                    final Option<?> opt2 = (Option<?>) expected;
                    Assertions.assertThat(wrapIterator(opt1)).isEqualTo(wrapIterator(opt2));
                    return this;
                } else {
                    final Iterable<T> iterable = (Iterable<T>) expected;
                    Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(iterable));
                    return this;
                }
            }

            private Option<?> wrapIterator(Option<?> option) {
                return option.map(o -> (o instanceof Iterator) ? List.ofAll((Iterator<?>) o) : o);
            }
        };
    }

    @Override
    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {
            @Override
            public ObjectAssert<T> isEqualTo(Object expected) {
                if (actual instanceof Tuple2) {
                    final Tuple2<?, ?> t1 = ((Tuple2<?, ?>) actual).map(this::toList);
                    final Tuple2<?, ?> t2 = ((Tuple2<?, ?>) expected).map(this::toList);
                    Assertions.assertThat((Object) t1).isEqualTo(t2);
                    return this;
                } else if (actual instanceof Tuple3) {
                    final Tuple3<?, ?, ?> t1 = ((Tuple3<?, ?, ?>) actual).map(this::toList);
                    final Tuple3<?, ?, ?> t2 = ((Tuple3<?, ?, ?>) expected).map(this::toList);
                    Assertions.assertThat((Object) t1).isEqualTo(t2);
                    return this;
                } else {
                    return super.isEqualTo(expected);
                }
            }

            private Tuple2<Object, Object> toList(Object o1, Object o2) {
                return Tuple.of(wrapIterator(o1), wrapIterator(o2));
            }

            private Tuple3<Object, Object, Object> toList(Object o1, Object o2, Object o3) {
                return Tuple.of(wrapIterator(o1), wrapIterator(o2), wrapIterator(o3));
            }

            private Object wrapIterator(Object o) {
                return (o instanceof Iterator) ? List.ofAll((Iterator<?>) o) : o;
            }
        };
    }

    @Override
    protected boolean isSerializable() {
        return false;
    }

    @Override
    protected boolean isTraversableAgain() {
        return false;
    }

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Iterator<T>> collector() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected <T> Iterator<T> empty() {
        return Iterator.empty();
    }

    @Override
    protected <T> Iterator<T> of(T element) {
        return Iterator.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Iterator<T> of(T... elements) {
        return Iterator.of(elements);
    }

    @Override
    protected <T> Iterator<T> ofAll(Iterable<? extends T> elements) {
        return Iterator.ofAll(elements);
    }

    @Override
    protected <T> Iterator<T> ofJavaStream(Stream<? extends T> javaStream) {
        return Iterator.ofAll(javaStream.iterator());
    }

    @Override
    protected Iterator<Boolean> ofAll(boolean[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Byte> ofAll(byte[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Character> ofAll(char[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Double> ofAll(double[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Float> ofAll(float[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Integer> ofAll(int[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Long> ofAll(long[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected Iterator<Short> ofAll(short[] array) {
        return Iterator.ofAll(array);
    }

    @Override
    protected <T> Iterator<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Iterator.tabulate(n, f);
    }

    @Override
    protected <T> Iterator<T> fill(int n, Supplier<? extends T> s) {
        return Iterator.fill(n, s);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 3;
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyArgList() {
        of().next();
    }

    // -- static narrow()

    @Test
    public void shouldNarrowIterator() {
        final Iterator<Double> doubles = of(1.0d);
        final Iterator<Number> numbers = narrow(doubles);
        final int actual = numbers.concat(Iterator.of(new BigDecimal("2.0"))).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- static ofAll()

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyIterable() {
        ofAll(List.empty()).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyBoolean() {
        ofAll(new boolean[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyByte() {
        ofAll(new byte[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyChar() {
        ofAll(new char[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyDouble() {
        ofAll(new double[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyFloat() {
        ofAll(new float[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyInt() {
        ofAll(new int[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyLong() {
        ofAll(new long[0]).next();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyShort() {
        ofAll(new short[0]).next();
    }

    // -- static concat()

    @Test
    public void shouldConcatEmptyIterableIterable() {
        final Iterable<Iterable<Integer>> empty = List.empty();
        assertThat(concat(empty)).isSameAs(Iterator.empty());

    }

    @Test
    public void shouldConcatNonEmptyIterableIterable() {
        final Iterable<Iterable<Integer>> itIt = List.of(List.of(1, 2), List.of(3));
        assertThat(concat(itIt)).isEqualTo(Iterator.of(1, 2, 3));

    }

    @Test
    public void shouldConcatEmptyArrayIterable() {
        assertThat(concat()).isSameAs(Iterator.empty());

    }

    @Test
    public void shouldConcatNonEmptyArrayIterable() {
        assertThat(concat(List.of(1, 2), List.of(3))).isEqualTo(Iterator.of(1, 2, 3));

    }

    // -- concat

    @Test
    public void shouldConcatThisNonEmptyWithEmpty() {
        final Iterator<Integer> it = Iterator.of(1);
        assertThat(it.concat(Iterator.<Integer> empty())).isSameAs(it);
    }

    @Test
    public void shouldConcatThisEmptyWithNonEmpty() {
        final Iterator<Integer> it = Iterator.of(1);
        assertThat(Iterator.<Integer> empty().concat(it)).isSameAs(it);
    }

    @Test
    public void shouldConcatThisNonEmptyWithNonEmpty() {
        assertThat(Iterator.of(1).concat(Iterator.of(2))).isEqualTo(Iterator.of(1, 2));
    }

    // -- transform

    @Test
    public void shouldTransform() {
        final Iterator<?> it = Iterator.of(1, 2).transform(ii -> ii.drop(1));
        assertThat(it).isEqualTo(Iterator.of(2));
    }

    // -- static from(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(from(-1).take(3)).isEqualTo(Iterator.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateOverflowingIntStream() {
        //noinspection NumericOverflow
        assertThat(from(Integer.MAX_VALUE).take(2))
                .isEqualTo(Iterator.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
    }

    // -- static from(int, int)

    @Test
    public void shouldGenerateIntStreamWithStep() {
        assertThat(from(-1, 6).take(3)).isEqualTo(Iterator.of(-1, 5, 11));
    }

    @Test
    public void shouldGenerateOverflowingIntStreamWithStep() {
        //noinspection NumericOverflow
        assertThat(from(Integer.MAX_VALUE, 2).take(2))
                .isEqualTo(Iterator.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 2));
    }

    // -- static from(long)

    @Test
    public void shouldGenerateLongStream() {
        assertThat(from(-1L).take(3)).isEqualTo(Iterator.of(-1L, 0L, 1L));
    }

    @Test
    public void shouldGenerateOverflowingLongStream() {
        //noinspection NumericOverflow
        assertThat(from(Long.MAX_VALUE).take(2)).isEqualTo(Iterator.of(Long.MAX_VALUE, Long.MAX_VALUE + 1));
    }

    // -- static from(long, long)

    @Test
    public void shouldGenerateLongStreamWithStep() {
        assertThat(from(-1L, 5L).take(3)).isEqualTo(Iterator.of(-1L, 4L, 9L));
    }

    @Test
    public void shouldGenerateOverflowingLongStreamWithStep() {
        //noinspection NumericOverflow
        assertThat(from(Long.MAX_VALUE, 2).take(2)).isEqualTo(Iterator.of(Long.MAX_VALUE, Long.MAX_VALUE + 2));
    }

    // -- static continually(Supplier)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(continually(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static iterate(T, Function)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplierWithAccessToPreviousValue() {
        assertThat(iterate(2, (i) -> i + 2).take(3).reduce((i, j) -> i + j)).isEqualTo(12);
    }

    @Test
    public void shouldNotCallSupplierUntilNecessary() {
        assertThat(iterate(2, (i) -> {
            throw new RuntimeException();
        }).head()).isEqualTo(2);
    }

    // ++++++ OBJECT ++++++

    // -- equals

    @Override
    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        // a equals impl would enforce evaluation which is not wanted
    }

    // TODO: equals of same object and different objects of same shape

    // -- hashCode

    @Override
    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        // a hashCode impl would enforce evaluation which is not wanted
    }

    @Override
    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        // a hashCode impl would enforce evaluation which is not wanted
    }

    // -- groupBy

    @Override
    public void shouldNonNilGroupByIdentity() {
        // we can't compare iterators, should map it to sequences
        final Seq<?> actual = of('a', 'b', 'c').groupBy(Function.identity()).map(e -> Tuple.of(e._1, List.ofAll(e._2)));
        final Seq<?> expected = HashMap.of(
                'a', List.ofAll(of('a')),
                'b', List.ofAll(of('b')),
                'c', List.ofAll(of('c'))).toList();
        assertThat(actual).isEqualTo(expected);
    }

    @Override
    public void shouldNonNilGroupByEqual() {
        // we can't compare iterators, should map it to sequences
        final Seq<?> actual = of('a', 'b', 'c').groupBy(c -> 1).map(e -> Tuple.of(e._1, List.ofAll(e._2)));
        final Seq<?> expected = HashMap.of(1, List.ofAll(of('a', 'b', 'c'))).toList();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateDoubleRangeByFromInfinity() {
        assertThat(rangeBy(Double.NEGATIVE_INFINITY, 0.0, 1.0)).startsWith(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE);
        assertThat(rangeBy(Double.POSITIVE_INFINITY, 0.0, -1.0)).startsWith(Double.POSITIVE_INFINITY, Double.MAX_VALUE);
    }

    @Test
    public void shouldCreateDoubleRangeClosedByFromInfinity() {
        assertThat(rangeClosedBy(Double.NEGATIVE_INFINITY, 0.0, 1.0)).startsWith(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE);
        assertThat(rangeClosedBy(Double.POSITIVE_INFINITY, 0.0, -1.0)).startsWith(Double.POSITIVE_INFINITY, Double.MAX_VALUE);
    }

    @Test
    public void shouldCreateDoubleRangeByFromMaxToInfinity() {
        assertThat(rangeBy(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 3E307)).isEqualTo(of(Double.MAX_VALUE));
        assertThat(rangeBy(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY, -3E307)).isEqualTo(of(-Double.MAX_VALUE));
    }

    @Test
    public void shouldCreateDoubleRangeClosedByFromMaxToInfinity() {
        assertThat(rangeClosedBy(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 3E307)).isEqualTo(of(Double.MAX_VALUE));
        assertThat(rangeClosedBy(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY, -3E307)).isEqualTo(of(-Double.MAX_VALUE));
    }

    // -- unfoldRight

    @Test
    public void shouldUnfoldRightToEmpty() {
        assertThat(Iterator.unfoldRight(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldRightSimpleList() {
        assertThat(
                Iterator.unfoldRight(10, x -> x == 0
                        ? Option.none()
                        : Option.of(new Tuple2<>(x, x - 1))))
                .isEqualTo(of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
    }

    @Test
    public void shouldUnfoldLeftToEmpty() {
        assertThat(Iterator.unfoldLeft(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldLeftSimpleList() {
        assertThat(
                Iterator.unfoldLeft(10, x -> x == 0
                        ? Option.none()
                        : Option.of(new Tuple2<>(x - 1, x))))
                .isEqualTo(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void shouldUnfoldToEmpty() {
        assertThat(Iterator.unfold(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldSimpleList() {
        assertThat(
                Iterator.unfold(10, x -> x == 0
                        ? Option.none()
                        : Option.of(new Tuple2<>(x - 1, x))))
                .isEqualTo(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Override
    @Test
    public void shouldHaveAReasonableToString() {
        // iterators are intermediate objects and should not have an equals, hashCode or toString
    }
}
