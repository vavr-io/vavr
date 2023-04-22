package io.vavr.collection.champ;

import io.vavr.collection.Set;

import java.io.Serial;
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
public class MutableHashSet<E> extends ChampPackage.AbstractChampSet<E, E> {
    @Serial
    private final static long serialVersionUID = 0L;

    /**
     * Constructs an empty set.
     */
    public MutableHashSet() {
        root = ChampPackage.BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a set containing the elements in the specified iterable.
     *
     * @param c an iterable
     */
    @SuppressWarnings("unchecked")
    public MutableHashSet(Iterable<? extends E> c) {
        if (c instanceof MutableHashSet<?>) {
            c = ((MutableHashSet<? extends E>) c).toImmutable();
        }
        if (c instanceof HashSet<?>) {
            HashSet<E> that = (HashSet<E>) c;
            this.root = that;
            this.size = that.size;
        } else {
            this.root = ChampPackage.BitmapIndexedNode.emptyNode();
            addAll(c);
        }
    }

    @Override
    public boolean add(final E e) {
        ChampPackage.ChangeEvent<E> details = new ChampPackage.ChangeEvent<>();
        root = root.update(getOrCreateIdentity(),
                e, Objects.hashCode(e), 0, details,
                (oldk, newk) -> oldk,
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            size++;
            modCount++;
        }
        return details.isModified();
    }

    @Override
    public void clear() {
        root = ChampPackage.BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    /**
     * Returns a shallow copy of this set.
     */
    @Override
    public MutableHashSet<E> clone() {
        return (MutableHashSet<E>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return ChampPackage.Node.NO_DATA != root.find((E) o, Objects.hashCode(o), 0,
                Objects::equals);
    }

    @Override
    public Iterator<E> iterator() {
        return new ChampPackage.FailFastIterator<>(
                new ChampPackage.KeyIterator<>(root, this::iteratorRemove),
                () -> this.modCount);
    }

    private void iteratorRemove(E e) {
        mutator = null;
        remove(e);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        ChampPackage.ChangeEvent<E> details = new ChampPackage.ChangeEvent<>();
        root = root.remove(
                getOrCreateIdentity(), (E) o, Objects.hashCode(o), 0, details,
                Objects::equals);
        if (details.isModified()) {
            size--;
            modCount++;
        }
        return details.isModified();
    }

    /**
     * Returns an immutable copy of this set.
     *
     * @return an immutable copy
     */
    public HashSet<E> toImmutable() {
        mutator = null;
        return size == 0 ? HashSet.empty() : new HashSet<>(root, size);
    }

    @Serial
    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<E> extends ChampPackage.SetSerializationProxy<E> {
        @Serial
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Serial
        @Override
        protected Object readResolve() {
            return new MutableHashSet<>(deserialized);
        }
    }

    public <U> U transform(Function<? super Set<Long>, ? extends U> f) {
        // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
        //     wants us to have a transform() method although this class
        //     is a standard Collection class.
        throw new UnsupportedOperationException();
    }
}
