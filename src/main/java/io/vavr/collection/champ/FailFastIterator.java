package io.vavr.collection.champ;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.IntSupplier;

public class FailFastIterator<E> implements Iterator<E>, io.vavr.collection.Iterator<E> {
    private final Iterator<? extends E> i;
    private int expectedModCount;
    private final IntSupplier modCountSupplier;

    public FailFastIterator(Iterator<? extends E> i, IntSupplier modCountSupplier) {
        this.i = i;
        this.modCountSupplier = modCountSupplier;
        this.expectedModCount = modCountSupplier.getAsInt();
    }

    @Override
    public boolean hasNext() {
        ensureUnmodified();
        return i.hasNext();
    }

    @Override
    public E next() {
        ensureUnmodified();
        return i.next();
    }

    protected void ensureUnmodified() {
        if (expectedModCount != modCountSupplier.getAsInt()) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    public void remove() {
        ensureUnmodified();
        i.remove();
        expectedModCount = modCountSupplier.getAsInt();
    }
}
