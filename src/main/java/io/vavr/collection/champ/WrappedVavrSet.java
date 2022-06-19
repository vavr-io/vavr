package io.vavr.collection.champ;

import io.vavr.collection.Iterator;
import io.vavr.collection.LinkedChampSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.SetMixin;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Wraps {@code Set}  functions into the {@link Set} interface.
 *
 * @param <E> the element type of the set
 */
public class WrappedVavrSet<E> implements SetMixin<E> {
    private static final long serialVersionUID = 1L;
    protected final Function<E, Set<E>> addFunction;
    protected final IntFunction<Set<E>> dropRightFunction;
    protected final IntFunction<Set<E>> takeRightFunction;
    protected final Predicate<E> containsFunction;
    protected final Function<E, Set<E>> removeFunction;
    protected final Function<Iterable<? extends E>, Set<E>> addAllFunction;
    protected final Supplier<Set<E>> clearFunction;
    protected final Supplier<Set<E>> initFunction;
    protected final Supplier<Iterator<E>> iteratorFunction;
    protected final IntSupplier lengthFunction;
    protected final BiFunction<Object, BiFunction<? super E, ? super Object, Object>, Object> foldRightFunction;

    /**
     * Wraps the keys of the specified {@link Map} into a {@link Set} interface.
     *
     * @param map the map
     */
    public WrappedVavrSet(Map<E, ?> map) {
        this.addFunction = e -> new WrappedVavrSet<>(map.put(e, null));
        this.foldRightFunction = (u, f) -> map.foldRight(u, (tuple, uu) -> f.apply(tuple._1(), uu));
        this.dropRightFunction = n -> new WrappedVavrSet<>(map.dropRight(n));
        this.takeRightFunction = n -> new WrappedVavrSet<>(map.takeRight(n));
        this.containsFunction = map::containsKey;
        this.clearFunction = () -> new WrappedVavrSet<>(map.dropRight(map.length()));
        this.initFunction = () -> new WrappedVavrSet<>(map.init());
        this.iteratorFunction = map::keysIterator;
        this.lengthFunction = map::length;
        this.removeFunction = e -> new WrappedVavrSet<>(map.remove(e));
        this.addAllFunction = i -> {
            Map<E, ?> m = map;
            for (E e : i) {
                m = m.put(e, null);
            }
            return new WrappedVavrSet<>(m);
        };
    }

    public WrappedVavrSet(Function<E, Set<E>> addFunction,
                          IntFunction<Set<E>> dropRightFunction,
                          IntFunction<Set<E>> takeRightFunction,
                          Predicate<E> containsFunction,
                          Function<E, Set<E>> removeFunction,
                          Function<Iterable<? extends E>, Set<E>> addAllFunction,
                          Supplier<Set<E>> clearFunction,
                          Supplier<Set<E>> initFunction,
                          Supplier<Iterator<E>> iteratorFunction, IntSupplier lengthFunction,
                          BiFunction<Object, BiFunction<? super E, ? super Object, Object>, Object> foldRightFunction) {
        this.addFunction = addFunction;
        this.dropRightFunction = dropRightFunction;
        this.takeRightFunction = takeRightFunction;
        this.containsFunction = containsFunction;
        this.removeFunction = removeFunction;
        this.addAllFunction = addAllFunction;
        this.clearFunction = clearFunction;
        this.initFunction = initFunction;
        this.iteratorFunction = iteratorFunction;
        this.lengthFunction = lengthFunction;
        this.foldRightFunction = foldRightFunction;
    }

    @Override
    public Set<E> add(E element) {
        return addFunction.apply(element);
    }

    @Override
    public Set<E> addAll(Iterable<? extends E> elements) {
        return addAllFunction.apply(elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Set<R> create() {
        return (Set<R>) clearFunction.get();
    }

    @Override
    public <R> Set<R> createFromElements(Iterable<? extends R> elements) {
        return this.<R>create().addAll(elements);
    }

    @Override
    public Set<E> remove(E element) {
        return removeFunction.apply(element);
    }

    @Override
    public boolean contains(E element) {
        return containsFunction.test(element);
    }

    @Override
    public Set<E> dropRight(int n) {
        return dropRightFunction.apply(n);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> U foldRight(U zero, BiFunction<? super E, ? super U, ? extends U> combine) {
        return (U) foldRightFunction.apply(zero, (BiFunction<? super E, ? super Object, Object>) combine);
    }

    @Override
    public Set<E> init() {
        return initFunction.get();
    }

    @Override
    public Iterator<E> iterator() {
        return iteratorFunction.get();
    }

    @Override
    public int length() {
        return lengthFunction.getAsInt();
    }

    @Override
    public Set<E> takeRight(int n) {
        return takeRightFunction.apply(n);
    }

    private Object writeReplace() {
        // FIXME WrappedVavrSet is not serializable. We convert
        //            it into a LinkedChampSet.
        return new LinkedChampSet.SerializationProxy<E>(this.toJavaSet());
    }
}
