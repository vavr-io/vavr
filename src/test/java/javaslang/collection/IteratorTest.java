/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collector;

public class IteratorTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(java.lang.Iterable<T> actual) {
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
                    java.lang.Iterable<T> iterable = (java.lang.Iterable<T>) expected;
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
                    final Tuple2<?, ?> t1 = ((Tuple2<?, ?>) actual).flatMap(this::toList);
                    final Tuple2<?, ?> t2 = ((Tuple2<?, ?>) expected).flatMap(this::toList);
                    Assertions.assertThat((Object) t1).isEqualTo(t2);
                    return this;
                } else {
                    return super.isEqualTo(expected);
                }
            }

            private Tuple2<Object, Object> toList(Object o1, Object o2) {
                return Tuple.of(wrapIterator(o1), wrapIterator(o2));
            }

            private Object wrapIterator(Object o) {
                return (o instanceof Iterator) ? List.ofAll((Iterator<?>) o) : o;
            }
        };
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

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOfEmptyArgList() {
        of().next();
    }

    @Override
    protected <T> Iterator<T> ofAll(java.lang.Iterable<? extends T> elements) {
        return Iterator.ofAll(elements);
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
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 3;
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
        Iterable<Iterable<Integer>> empty = List.empty();
        assertThat(Iterator.concat(empty)).isSameAs(Iterator.empty());

    }

    @Test
    public void shouldConcatNonEmptyIterableIterable() {
        Iterable<Iterable<Integer>> itIt = List.of(List.of(1, 2), List.of(3));
        assertThat(Iterator.concat(itIt)).isEqualTo(Iterator.of(1, 2, 3));

    }

    @Test
    public void shouldConcatEmptyArrayIterable() {
        assertThat(Iterator.concat()).isSameAs(Iterator.empty());

    }

    @Test
    public void shouldConcatNonEmptyArrayIterable() {
        assertThat(Iterator.concat(List.of(1, 2), List.of(3))).isEqualTo(Iterator.of(1, 2, 3));

    }

    // -- static from(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(Iterator.from(-1).take(3)).isEqualTo(Iterator.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateTerminatingIntStream() {
        //noinspection NumericOverflow
        assertThat(Iterator.from(Integer.MAX_VALUE).take(2))
                .isEqualTo(Iterator.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
    }

    // -- static from(long)

    @Test
    public void shouldGenerateLongStream() {
        assertThat(Iterator.from(-1L).take(3)).isEqualTo(Iterator.of(-1L, 0L, 1L));
    }

    @Test
    public void shouldGenerateTerminatingLongStream() {
        //noinspection NumericOverflow
        assertThat(Iterator.from(Long.MAX_VALUE).take(2)).isEqualTo(Iterator.of(Long.MAX_VALUE, Long.MAX_VALUE + 1));
    }

    // -- static gen(Supplier)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(Iterator.gen(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static gen(T, Function)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplierWithAccessToPreviousValue() {
        assertThat(Iterator.gen(2, (i) -> i + 2).take(3).reduce((i, j) -> i + j)).isEqualTo(12);
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
        final Seq<?> expected = HashMap.empty()
                .put('a', List.ofAll(of('a')))
                .put('b', List.ofAll(of('b')))
                .put('c', List.ofAll(of('c')))
                .toList();
        assertThat(actual).isEqualTo(expected);
    }

    @Override
    public void shouldNonNilGroupByEqual() {
        // we can't compare iterators, should map it to sequences
        final Seq<?> actual = of('a', 'b', 'c').groupBy(c -> 1).map(e -> Tuple.of(e._1, List.ofAll(e._2)));
        final Seq<?> expected = HashMap.empty().put(1, List.ofAll(of('a', 'b', 'c'))).toList();
        assertThat(actual).isEqualTo(expected);
    }

    // -- serialization/deserialization

    @Override
    @Test
    public void shouldSerializeDeserializeNil() {
        // iterators are intermediate objects and not serializable/deserializable
    }

    @Override
    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // iterators are intermediate objects and not serializable/deserializable
    }

    @Override
    @Test
    public void shouldSerializeDeserializeNonNil() {
        // iterators are intermediate objects and not serializable/deserializable
    }

}
