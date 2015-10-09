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

import java.util.function.Function;

public class IteratorTest extends AbstractTraversableOnceTest {

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

            @SuppressWarnings("unchecked")
            private Option<?> wrapIterator(Option<?> option) {
                return option.map(o -> (o instanceof Iterator) ? List.ofAll((Iterator) o) : o);
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
                    Assertions.assertThat(t1).isEqualTo(t2);
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
    protected <T> Iterator<T> empty() {
        return Iterator.empty();
    }

    @Override
    protected <T> Iterator<T> of(T element) {
        return Iterator.of(element);
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    @Override
    protected final <T> Iterator<T> of(T... elements) {
        return Iterator.of(elements);
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
    boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 3;
    }

    // -- static from(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(Iterator.from(-1).take(3)).isEqualTo(Iterator.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateTerminatingIntStream() {
        //noinspection NumericOverflow
        assertThat(Iterator.from(Integer.MAX_VALUE).take(2)).isEqualTo(Iterator.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
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
    @SuppressWarnings("unchecked")
    public void shouldNonNilGroupByIdentity() {
        // we can't compare iterators, should map it to sequences
        Seq<?> actual = of('a', 'b', 'c').groupBy(Function.identity())
                .map(e -> Map.Entry.of(e.key, List.ofAll(e.value)));
        Seq<?> expected = HashMap.empty().put('a', of('a')).put('b', of('b')).put('c', of('c'))
                .map(e -> Map.Entry.of(e.key, List.ofAll((java.lang.Iterable<Character>) e.value)));
        assertThat(actual).isEqualTo(expected);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void shouldNonNilGroupByEqual() {
        // we can't compare iterators, should map it to sequences
        Seq<?> actual = of('a', 'b', 'c').groupBy(c -> 1)
                .map(e -> Map.Entry.of(e.key, List.ofAll(e.value)));
        Seq<?> expected = HashMap.empty().put(1, of('a', 'b', 'c'))
                .map(e -> Map.Entry.of(e.key, List.ofAll((java.lang.Iterable<Character>) e.value)));
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
