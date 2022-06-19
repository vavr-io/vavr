package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.function.Function;

/**
 * Maps an {@link Iterator} in an {@link Iterator} of a different element type.
 * <p>
 * The underlying iterator is referenced - not copied.
 *
 * @param <E> the mapped element type
 * @param <F> the original element type
 * @author Werner Randelshofer
 */
public class MappedIterator<E, F> implements Iterator<E>, io.vavr.collection.Iterator<E> {
    private final Iterator<F> i;

    private final Function<F, E> mappingFunction;

    public MappedIterator(Iterator<F> i, Function<F, E> mappingFunction) {
        this.i = i;
        this.mappingFunction = mappingFunction;
    }

    @Override
    public boolean hasNext() {
        return i.hasNext();
    }

    @Override
    public E next() {
        return mappingFunction.apply(i.next());
    }

    @Override
    public void remove() {
        i.remove();
    }
}
