package javaslang.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

public abstract class AbstractMapTest extends AbstractTraversableTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> empty() {
        return (Traversable<T>) emptyMap();
    }

    abstract protected <T> AbstractIntMap<T> emptyMap();

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> of(T element) {
        return (Traversable<T>) mapOf(element);
    }

    abstract protected <T> AbstractIntMap<T> mapOf(T element);

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> of(T... elements) {
        return (Traversable<T>) mapOf(elements);
    }

    @SuppressWarnings("unchecked")
    abstract protected <T> AbstractIntMap<T> mapOf(T... elements);

    @Override
    boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 0;
    }

    @Override
    protected <T> Traversable<T> ofAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    protected Traversable<Boolean> ofAll(boolean[] array) {
        return null;
    }

    @Override
    protected Traversable<Byte> ofAll(byte[] array) {
        return null;
    }

    @Override
    protected Traversable<Character> ofAll(char[] array) {
        return null;
    }

    @Override
    protected Traversable<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected Traversable<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected Traversable<Integer> ofAll(int[] array) {
        return null;
    }

    @Override
    protected Traversable<Long> ofAll(long[] array) {
        return null;
    }

    @Override
    protected Traversable<Short> ofAll(short[] array) {
        return null;
    }
}
