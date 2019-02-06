package io.vavr.collection;

import org.openjdk.jmh.annotations.*;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IteratorBenchmark {

    private static final Object[] ARRAY_1000 = new Object[1000];

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Iterator<T> createArrayIteratorNaive(T... elements) {
        return new ArrayIteratorNaive<>(elements);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Iterator<T> createArrayIteratorWithLength(T... elements) {
        return new ArrayIteratorWithLength<>(elements);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Iterator<T> createArrayIteratorTryCatch(T... elements) {
        return new ArrayIteratorTryCatch<>(elements);
    }

    private void iterate(Iterable<Object> ts) {
        for (Object t : ts) {
            // do nothing
        }
    }

    @Benchmark
    public void fullyIteratingArrayIteratorNaiveForOneElement() {
        iterate(() -> createArrayIteratorNaive(1));
    }

    @Benchmark
    public void fullyIteratingArrayIteratorWithLengthForOneElement() {
        iterate(() -> createArrayIteratorWithLength(1));
    }

    @Benchmark
    public void fullyIteratingArrayIteratorTryCatchForOneElement() {
        iterate(() -> createArrayIteratorTryCatch(1));
    }

    @Benchmark
    public void fullyIteratingArrayIteratorNaiveFor1000Elements() {
        iterate(() -> createArrayIteratorNaive(ARRAY_1000));
    }

    @Benchmark
    public void fullyIteratingArrayIteratorWithLengthFor1000Elements() {
        iterate(() -> createArrayIteratorWithLength(ARRAY_1000));
    }

    @Benchmark
    public void fullyIteratingArrayIteratorTryCatchFor1000Elements() {
        iterate(() -> createArrayIteratorTryCatch(ARRAY_1000));
    }

}

final class ArrayIteratorNaive<T> implements Iterator<T> {

    private final T[] elements;
    private final int length;
    private int index = 0;

    ArrayIteratorNaive(T[] elements) {
        this.elements = elements;
        this.length = elements.length;
    }

    @Override
    public boolean hasNext() {
        return index < length;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return elements[index++];
    }

}

final class ArrayIteratorWithLength<T> implements Iterator<T> {

    private final T[] elements;
    private int index = 0;

    ArrayIteratorWithLength(T[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return elements[index++];
    }

}

final class ArrayIteratorTryCatch<T> implements Iterator<T> {

    private final T[] elements;
    private int index = 0;

    ArrayIteratorTryCatch(T[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T next() {
        try {
            final T next = elements[index];
            index++; // ensure index isn't increased before an IndexOutOfBoundsException
            return next;
        } catch(IndexOutOfBoundsException x) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        while (index < elements.length) {
            action.accept(elements[index++]);
        }
    }

}
