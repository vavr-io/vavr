package io.vavr.collection.champ;


import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Wraps an {@link Enumerator} into an {@link io.vavr.collection.Iterator} interface.
 *
 * @param <E> the element type
 */
public class VavrIteratorFacade<E> implements io.vavr.collection.Iterator<E> {
    private final @NonNull Enumerator<E> e;
    private final @Nullable Consumer<E> removeFunction;
    private boolean valueReady;
    private boolean canRemove;
    private E current;

    public VavrIteratorFacade(@NonNull Enumerator<E> e, @Nullable Consumer<E> removeFunction) {
        this.e = e;
        this.removeFunction = removeFunction;
    }

    @Override
    public boolean hasNext() {
        if (!valueReady) {
            // e.moveNext() changes e.current().
            // But the contract of hasNext() does not allow, that we change
            // the current value of the iterator.
            // This is why, we need a 'current' field in this facade.
            valueReady = e.moveNext();
        }
        return valueReady;
    }

    @Override
    public E next() {
        if (!valueReady && !hasNext()) {
            throw new NoSuchElementException();
        } else {
            valueReady = false;
            canRemove = true;
            return current = e.current();
        }
    }

    @Override
    public void remove() {
        if (!canRemove) throw new IllegalStateException();
        if (removeFunction != null) {
            removeFunction.accept(current);
            canRemove = false;
        } else {
            io.vavr.collection.Iterator.super.remove();
        }
    }
}