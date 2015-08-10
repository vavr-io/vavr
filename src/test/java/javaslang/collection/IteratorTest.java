package javaslang.collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

public class IteratorTest extends AbstractTraversableOnceTest {

    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                Iterable<T> expected = (Iterable<T>) obj;
                Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
                return this;
            }
        };
    }

    // TODO remove
    @Test
    public void shouldFilterNonEmptyTraversable() {
        Iterator<Integer> it = List.of(1, 2, 3, 4).iterator();
        assertThat(List.ofAll(() -> it.filter(i -> i % 2 == 0))).isEqualTo(List.of(2, 4));
    }

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector() {
        return null;
    }

    @Override
    protected <T> Iterator<T> empty() {
        return Iterator.empty();
    }

    @Override
    protected <T> Iterator<T> of(T element) {
        return Iterator.of(element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Iterator<T> of(T... elements) {
        return Iterator.of(elements);
    }

    @Override
    protected <T> Iterator<T> ofAll(Iterable<? extends T> elements) {
        return Iterator.ofAll(elements);
    }

    @Override
    protected Iterator<Boolean> ofAll(boolean[] array) {
        return null;
    }

    @Override
    protected Iterator<Byte> ofAll(byte[] array) {
        return null;
    }

    @Override
    protected Iterator<Character> ofAll(char[] array) {
        return null;
    }

    @Override
    protected Iterator<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected Iterator<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected Iterator<Integer> ofAll(int[] array) {
        return null;
    }

    @Override
    protected Iterator<Long> ofAll(long[] array) {
        return null;
    }

    @Override
    protected Iterator<Short> ofAll(short[] array) {
        return null;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 3;
    }
}
