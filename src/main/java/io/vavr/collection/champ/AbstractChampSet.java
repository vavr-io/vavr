package io.vavr.collection.champ;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;

abstract class AbstractChampSet<E, X> extends AbstractSet<E> implements Serializable, Cloneable {
    private final static long serialVersionUID = 0L;
    /**
     * The current mutator id of this set.
     * <p>
     * All nodes that have the same non-null mutator id, are exclusively owned
     * by this set, and therefore can be mutated without affecting other sets.
     * <p>
     * If this mutator id is null, then this set does not own any nodes.
     */
    protected IdentityObject mutator;

    /**
     * The root of this CHAMP trie.
     */
    protected BitmapIndexedNode<X> root;

    /**
     * The number of elements in this set.
     */
    protected int size;

    /**
     * The number of times this set has been structurally modified.
     */
    protected transient int modCount;

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll((Iterable<? extends E>) c);
    }

    /**
     * Adds all specified elements that are not already in this set.
     *
     * @param c an iterable of elements
     * @return {@code true} if this set changed
     */
    public boolean addAll(Iterable<? extends E> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        for (E e : c) {
            modified |= add(e);
        }
        return modified;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AbstractChampSet<?, ?>) {
            AbstractChampSet<?, ?> that = (AbstractChampSet<?, ?>) o;
            return size == that.size && root.equivalent(that.root);
        }
        return super.equals(o);
    }

    @Override
    public int size() {
        return size;
    }

    protected IdentityObject getOrCreateMutator() {
        if (mutator == null) {
            mutator = new IdentityObject();
        }
        return mutator;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeAll((Iterable<?>) c);
    }

    /**
     * Removes all specified elements that are in this set.
     *
     * @param c an iterable of elements
     * @return {@code true} if this set changed
     */
    public boolean removeAll(Iterable<?> c) {
        if (isEmpty()) {
            return false;
        }
        if (c == this) {
            clear();
            return true;
        }
        boolean modified = false;
        for (Object o : c) {
            modified |= remove(o);
        }
        return modified;
    }


    @Override
    @SuppressWarnings("unchecked")
    public AbstractChampSet<E, X> clone() {
        try {
            mutator = null;
            return (AbstractChampSet<E, X>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}