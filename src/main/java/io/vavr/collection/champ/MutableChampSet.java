package io.vavr.collection.champ;

import io.vavr.collection.Set;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Implements a mutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is mutable</li>
 *     <li>is not thread-safe</li>
 *     <li>does not guarantee a specific iteration order</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>add: O(1)</li>
 *     <li>remove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toImmutable: O(1) + O(log N) distributed across subsequent updates in
 *     this set</li>
 *     <li>clone: O(1) + O(log N) distributed across subsequent updates in this
 *     set and in the clone</li>
 *     <li>iterator.next: O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP tree contains nodes that may be shared with other sets, and nodes
 * that are exclusively owned by this set.
 * <p>
 * If a write operation is performed on an exclusively owned node, then this
 * set is allowed to mutate the node (mutate-on-write).
 * If a write operation is performed on a potentially shared node, then this
 * set is forced to create an exclusive copy of the node and of all not (yet)
 * exclusively owned parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1) in either
 * case.
 * <p>
 * This set can create an immutable copy of itself in O(1) time and O(1) space
 * using method {@link #toImmutable()}. This set loses exclusive ownership of
 * all its tree nodes.
 * Thus, creating an immutable copy increases the constant cost of
 * subsequent writes, until all shared nodes have been gradually replaced by
 * exclusively owned nodes again.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access this set concurrently, and at least
 * one of the threads modifies the set, it <em>must</em> be synchronized
 * externally.  This is typically accomplished by synchronizing on some
 * object that naturally encapsulates the set.
 * <p>
 * References:
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. BSD-2-Clause License</dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 * @param <E> the element type
 */
public class MutableChampSet<E> extends AbstractChampSet<E, E> {
    private final static long serialVersionUID = 0L;

    /**
     * Constructs an empty set.
     */
    public MutableChampSet() {
        root = BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a set containing the elements in the specified iterable.
     *
     * @param c an iterable
     */
    @SuppressWarnings("unchecked")
    public MutableChampSet(Iterable<? extends E> c) {
        if (c instanceof MutableChampSet<?>) {
            c = ((MutableChampSet<? extends E>) c).toImmutable();
        }
        if (c instanceof ChampSet<?>) {
            ChampSet<E> that = (ChampSet<E>) c;
            this.root = that;
            this.size = that.size;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            addAll(c);
        }
    }

    @Override
    public boolean add(final E e) {
        ChangeEvent<E> details = new ChangeEvent<>();
        root = root.update(getOrCreateMutator(),
                e, Objects.hashCode(e), 0, details,
                (oldk, newk) -> oldk,
                Objects::equals, Objects::hashCode);
        if (details.modified) {
            size++;
            modCount++;
        }
        return details.modified;
    }

    @Override
    public void clear() {
        root = BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    /**
     * Returns a shallow copy of this set.
     */
    @Override
    public MutableChampSet<E> clone() {
        return (MutableChampSet<E>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return Node.NO_VALUE != root.findByKey((E) o, Objects.hashCode(o), 0,
                Objects::equals);
    }

    @Override
    public Iterator<E> iterator() {
        return new FailFastIterator<>(
                new KeyIterator<>(root, this::iteratorRemove),
                () -> this.modCount);
    }

    private void iteratorRemove(E e) {
        mutator = null;
        remove(e);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        ChangeEvent<E> details = new ChangeEvent<>();
        root = root.remove(
                getOrCreateMutator(), (E) o, Objects.hashCode(o), 0, details,
                Objects::equals);
        if (details.modified) {
            size--;
            modCount++;
        }
        return details.modified;
    }

    /**
     * Returns an immutable copy of this set.
     *
     * @return an immutable copy
     */
    public ChampSet<E> toImmutable() {
        mutator = null;
        return size == 0 ? ChampSet.empty() : new ChampSet<>(root, size);
    }

    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return new MutableChampSet<>(deserialized);
        }
    }

    public <U> U transform(Function<? super Set<Long>, ? extends U> f) {
        // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
        //     wants us to have a transform() method although this class
        //     is a standard Collection class.
        throw new UnsupportedOperationException();
    }
}
