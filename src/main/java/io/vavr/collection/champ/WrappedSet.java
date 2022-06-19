package io.vavr.collection.champ;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Wraps {@code Set} functions into the {@link Set} interface.
 *
 * @param <E> the element type of the set
 * @author Werner Randelshofer
 */
public class WrappedSet<E> extends AbstractSet<E> {
    protected final Supplier<Iterator<E>> iteratorFunction;
    protected final IntSupplier sizeFunction;
    protected final Predicate<Object> containsFunction;
    protected final Predicate<E> addFunction;
    protected final Runnable clearFunction;
    protected final Predicate<Object> removeFunction;


    public WrappedSet(Set<E> backingSet) {
        this(backingSet::iterator, backingSet::size,
                backingSet::contains, backingSet::clear, backingSet::add, backingSet::remove);
    }

    public WrappedSet(Supplier<Iterator<E>> iteratorFunction,
                      IntSupplier sizeFunction,
                      Predicate<Object> containsFunction) {
        this(iteratorFunction, sizeFunction, containsFunction, null, null, null);
    }

    public WrappedSet(Supplier<Iterator<E>> iteratorFunction,
                      IntSupplier sizeFunction,
                      Predicate<Object> containsFunction,
                      Runnable clearFunction,
                      Predicate<E> addFunction,
                      Predicate<Object> removeFunction) {
        this.iteratorFunction = iteratorFunction;
        this.sizeFunction = sizeFunction;
        this.containsFunction = containsFunction;
        this.clearFunction = clearFunction == null ? () -> {
            throw new UnsupportedOperationException();
        } : clearFunction;
        this.removeFunction = removeFunction == null ? o -> {
            throw new UnsupportedOperationException();
        } : removeFunction;
        this.addFunction = addFunction == null ? o -> {
            throw new UnsupportedOperationException();
        } : addFunction;
    }

    @Override
    public boolean remove(Object o) {
        return removeFunction.test(o);
    }

    @Override
    public void clear() {
        clearFunction.run();
    }

    @Override
    public Spliterator<E> spliterator() {
        return super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return super.stream();
    }

    @Override
    public Iterator<E> iterator() {
        return iteratorFunction.get();
    }

    /*
    //@Override  since 11
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }*/

    @Override
    public int size() {
        return sizeFunction.getAsInt();
    }

    @Override
    public boolean contains(Object o) {
        return containsFunction.test(o);
    }

    @Override
    public boolean add(E e) {
        return addFunction.test(e);
    }

    public <U> U transform(Function<? super io.vavr.collection.Set<Long>, ? extends U> f) {
        // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
        //     wants us to have a transform() method although this class
        //     is a standard Collection class.
        throw new UnsupportedOperationException();
    }
}
