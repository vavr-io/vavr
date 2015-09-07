package javaslang.collection;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.ArrayList;
import java.util.stream.Collector;

public abstract class AbstractMapTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(java.lang.Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                Stream<Object> expectedStream = Stream.ofAll((java.lang.Iterable<Object>) obj);
                Stream<Object> actualStream = Stream.ofAll(actual);
                if(!actualStream.isEmpty() && actualStream.head() instanceof Map.Entry) {
                    assertThat(actualStream.map(e -> ((Map.Entry) e).value)).isEqualTo(obj);
                    return this;
                }
                if(!expectedStream.isEmpty() && expectedStream.head() instanceof Map.Entry) {
                    assertThat(actualStream).isEqualTo(expectedStream.map(e -> ((Map.Entry) e).value));
                    return this;
                }
                assertThat(actualStream.equals(expectedStream)).isTrue();
                return this;
            }
        };
    }

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> empty() {
        return (Traversable<T>) emptyMap();
    }

    abstract protected <T> Map<Integer, T> emptyMap();

    @Override
    boolean useIsEqualToInsteadOfIsSameAs() {
        // TODO
        return true;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> of(T element) {
        Map<Integer, T> map = emptyMap();
        map = map.put(0, element);
        return (Traversable<T>) AbstractIntMap.of(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> of(T... elements) {
        Map<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return (Traversable<T>) AbstractIntMap.of(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> ofAll(Iterable<? extends T> elements) {
        Map<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return (Traversable<T>) AbstractIntMap.of(map);
    }

    @Override
    protected Traversable<Boolean> ofAll(boolean[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Byte> ofAll(byte[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Character> ofAll(char[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Double> ofAll(double[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Float> ofAll(float[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Integer> ofAll(int[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Long> ofAll(long[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected Traversable<Short> ofAll(short[] array) {
        return ofAll(Iterator.ofAll(array));
    }
}
