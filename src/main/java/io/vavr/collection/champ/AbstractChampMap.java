package io.vavr.collection.champ;


import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for CHAMP maps.
 *
 * @param <K> the key type of the map
 * @param <V> the value typeof the map
 */
abstract class AbstractChampMap<K, V, X> extends AbstractMap<K, V>
        implements Serializable, Cloneable {
    private final static long serialVersionUID = 0L;

    /**
     * The current mutator id of this map.
     * <p>
     * All nodes that have the same non-null mutator id, are exclusively owned
     * by this map, and therefore can be mutated without affecting other map.
     * <p>
     * If this mutator id is null, then this map does not own any nodes.
     */
    protected IdentityObject mutator;

    /**
     * The root of this CHAMP trie.
     */
    protected BitmapIndexedNode<X> root;

    /**
     * The number of entries in this map.
     */
    protected int size;

    /**
     * The number of times this map has been structurally modified.
     */
    protected int modCount;

    protected IdentityObject getOrCreateMutator() {
        if (mutator == null) {
            mutator = new IdentityObject();
        }
        return mutator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractChampMap<K, V, X> clone() {
        try {
            mutator = null;
            return (AbstractChampMap<K, V, X>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AbstractChampMap<?, ?, ?>) {
            AbstractChampMap<?, ?, ?> that = (AbstractChampMap<?, ?, ?>) o;
            return size == that.size && root.equivalent(that.root);
        }
        return super.equals(o);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }


    public Iterator<Entry<K, V>> iterator() {
        return entrySet().iterator();
    }

    @SuppressWarnings("unchecked")
    boolean removeEntry(final Object o) {
        if (containsEntry(o)) {
            assert o != null;
            remove(((Entry<K, V>) o).getKey());
            return true;
        }
        return false;
    }

    /**
     * Returns true if this map contains the specified entry.
     *
     * @param o an entry
     * @return true if this map contains the entry
     */
    protected boolean containsEntry(Object o) {
        if (o instanceof java.util.Map.Entry) {
            @SuppressWarnings("unchecked") java.util.Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            return containsKey(entry.getKey())
                    && Objects.equals(entry.getValue(), get(entry.getKey()));
        }
        return false;
    }
}
