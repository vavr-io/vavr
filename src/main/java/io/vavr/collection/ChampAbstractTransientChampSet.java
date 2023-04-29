/*
 * @(#)AbstractMutableChampSet.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;


import java.io.Serial;
import java.util.Collection;

/**
 * Abstract base class for CHAMP sets.
 *
 * @param <E> the element type of the set
 * @param <X> the key type of the CHAMP trie
 */
public abstract class ChampAbstractTransientChampSet<E, X> {
    @Serial
    private static final long serialVersionUID = 0L;

    /**
     * The current mutator id of this set.
     * <p>
     * All nodes that have the same non-null mutator id, are exclusively owned
     * by this set, and therefore can be mutated without affecting other sets.
     * <p>
     * If this mutator id is null, then this set does not own any nodes.
     */
    protected ChampIdentityObject mutator;

    /**
     * The root of this CHAMP trie.
     */
    protected ChampBitmapIndexedNode<X> root;

    /**
     * The number of elements in this set.
     */
    protected int size;

    /**
     * The number of times this set has been structurally modified.
     */
    protected transient int modCount;


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


    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ChampAbstractTransientChampSet<?, ?>) {
            ChampAbstractTransientChampSet<?, ?> that = (ChampAbstractTransientChampSet<?, ?>) o;
            return size == that.size && root.equivalent(that.root);
        }
        return super.equals(o);
    }


    public int size() {
        return size;
    }

    /**
     * Gets the mutator id of this set. Creates a new id, if this
     * set has no mutator id.
     *
     * @return a new unique id or the existing unique id.
     */

    protected ChampIdentityObject getOrCreateIdentity() {
        if (mutator == null) {
            mutator = new ChampIdentityObject();
        }
        return mutator;
    }


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

    abstract boolean add(E e);

    abstract boolean remove(Object e);

    abstract void clear();

    boolean isEmpty() {
        return size() == 0;
    }
}
