package javaslang.collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.junit.Test;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.stream.Collector;

public class IteratorTest extends AbstractTraversableTest {

    protected <T> IterableAssert<T> assertThat(java.lang.Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                java.lang.Iterable<T> expected = (java.lang.Iterable<T>) obj;
                Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
                return this;
            }
        };
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
    boolean isThisLazyJavaslangObject() {
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

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        /* ignore */
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        /* ignore */
    }

    @Test
    public void shouldSerializeDeserializeNil() {
        // TODO ?
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // TODO ?
    }

    @Test
    public void shouldSerializeDeserializeNonNil() {
        // TODO ?
    }

    @Test
    public void shouldConcatenateListOfEmptyIterators() {
        assertThat(Iterator.ofIterators().isEmpty()).isTrue();
        assertThat(Iterator.ofIterators(Iterator.empty()).isEmpty()).isTrue();
        assertThat(Iterator.ofIterators(Iterator.empty(), Iterator.empty()).isEmpty()).isTrue();
    }

    @Test
    public void shouldConcatenateListOfNonEmptyIterators() {
        assertThat(Iterator.ofIterators(of(1, 2), of(), of(3))).isEqualTo(of(1, 2, 3));
    }

}
