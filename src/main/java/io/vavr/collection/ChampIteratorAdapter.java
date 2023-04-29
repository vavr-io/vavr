package io.vavr.collection;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Adapts a {@link Spliterator} to the {@link Iterator} interface.
 * @param <E> the element type
 */
class ChampIteratorAdapter<E> implements Iterator<E>, Consumer<E> {
    private final Spliterator<E> spliterator;

    public ChampIteratorAdapter(Spliterator<E> spliterator) {
        this.spliterator = spliterator;
    }

    boolean hasCurrent = false;
    E current;

    public void accept(E t) {
        hasCurrent = true;
        current = t;
    }

    @Override
    public boolean hasNext() {
        if (!hasCurrent) {
            spliterator.tryAdvance(this);
        }
        return hasCurrent;
    }

    @Override
    public E next() {
        if (!hasCurrent && !hasNext())
            throw new NoSuchElementException();
        else {
            hasCurrent = false;
            E t = current;
            current = null;
            return t;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        if (hasCurrent) {
            hasCurrent = false;
            E t = current;
            current = null;
            action.accept(t);
        }
        spliterator.forEachRemaining(action);
    }
}

