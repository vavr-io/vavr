package io.vavr.collection;

import org.openjdk.jmh.annotations.*;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ArrayIteratorBenchmark {

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

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Iterator<T> createArrayIteratorTryCatchOptimized(T... elements) {
        return new ArrayIteratorTryCatchOptimized<>(elements);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Iterator<T> createArrayIteratorTryCatchOptimizedExtendsAbstractIterator(T... elements) {
        return new ArrayIteratorTryCatchOptimizedExtendsAbstractIterator<>(elements);
    }

    // see https://hg.openjdk.java.net/code-tools/jmh/file/5984e353dca7/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_11_Loops.java
    private int x = 1;
    private int y = 2;

    private int iterate(Iterable<Object> ts) {
        int sum = 0;
        for (Object t : ts) {
            sum += (x + y);
        }
        return sum;
    }

    @Benchmark
    public int fullyIteratingArrayIteratorNaiveForOneElement() {
        return iterate(() -> createArrayIteratorNaive(1));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorWithLengthForOneElement() {
        return iterate(() -> createArrayIteratorWithLength(1));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchForOneElement() {
        return iterate(() -> createArrayIteratorTryCatch(1));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchOptimizedForOneElement() {
        return iterate(() -> createArrayIteratorTryCatchOptimized(1));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchOptimizedExtendsAbstractIteratorForOneElement() {
        return iterate(() -> createArrayIteratorTryCatchOptimizedExtendsAbstractIterator(1));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorNaiveFor1000Elements() {
        return iterate(() -> createArrayIteratorNaive(ARRAY_1000));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorWithLengthFor1000Elements() {
        return iterate(() -> createArrayIteratorWithLength(ARRAY_1000));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchFor1000Elements() {
        return iterate(() -> createArrayIteratorTryCatch(ARRAY_1000));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchOptimizedFor1000Elements() {
        return iterate(() -> createArrayIteratorTryCatchOptimized(ARRAY_1000));
    }

    @Benchmark
    public int fullyIteratingArrayIteratorTryCatchOptimizeExtendsAbstractIteratordFor1000Elements() {
        return iterate(() -> createArrayIteratorTryCatchOptimizedExtendsAbstractIterator(ARRAY_1000));
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

}

final class ArrayIteratorTryCatchOptimized<T> implements Iterator<T> {

    private final T[] elements;
    private int index = 0;

    ArrayIteratorTryCatchOptimized(T[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T next() {
        try {
            return elements[index++];
        } catch(IndexOutOfBoundsException x) {
            index--;
            throw new NoSuchElementException();
        }
    }

}

final class ArrayIteratorTryCatchOptimizedExtendsAbstractIterator<T> extends AbstractIterator<T> {

    private final T[] elements;
    private int index = 0;

    ArrayIteratorTryCatchOptimizedExtendsAbstractIterator(T[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T next() {
        try {
            return elements[index++];
        } catch(IndexOutOfBoundsException x) {
            index--;
            throw new NoSuchElementException();
        }
    }

}
