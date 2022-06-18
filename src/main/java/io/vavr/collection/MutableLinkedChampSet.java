package io.vavr.collection;

import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.BucketSequencedIterator;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.FailFastIterator;
import io.vavr.collection.champ.HeapSequencedIterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.Sequenced;
import io.vavr.collection.champ.SequencedElement;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Implements a mutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
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
 *     <li>toImmutable: O(1) + a cost distributed across subsequent updates in
 *     this set</li>
 *     <li>clone: O(1) + a cost distributed across subsequent updates in this
 *     set and in the clone</li>
 *     <li>iterator.next: O(log N)</li>
 *     <li>getFirst, getLast: O(N)</li>
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
 * This set can create an immutable copy of itself in O(1) time and O(0) space
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
public class MutableLinkedChampSet<E> extends AbstractChampSet<E, SequencedElement<E>> {
    private final static long serialVersionUID = 0L;

    /**
     * Counter for the sequence number of the last element. The counter is
     * incremented after a new entry is added to the end of the sequence.
     */
    private int last = 0;
    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement before a new entry is added to the start of the sequence.
     */
    private int first = 0;

    /**
     * Constructs an empty set.
     */
    public MutableLinkedChampSet() {
        root = BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a set containing the elements in the specified
     * {@link Iterable}.
     *
     * @param c an iterable
     */
    @SuppressWarnings("unchecked")
    public MutableLinkedChampSet(Iterable<? extends E> c) {
        if (c instanceof MutableLinkedChampSet<?>) {
            c = ((MutableLinkedChampSet<? extends E>) c).toImmutable();
        }
        if (c instanceof LinkedChampSet<?>) {
            LinkedChampSet<E> that = (LinkedChampSet<E>) c;
            this.root = that;
            this.size = that.size;
            this.first = that.first;
            this.last = that.last;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            addAll(c);
        }
    }

    @Override
    public boolean add(final E e) {
        return addLast(e, false);
    }

    //@Override
    public void addFirst(E e) {
        addFirst(e, true);
    }

    private boolean addFirst(E e, boolean moveToFirst) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedElement<E>> newRoot = root.update(getOrCreateMutator(), new SequencedElement<>(e, first - 1),
                Objects.hashCode(e), 0, details,
                moveToFirst ? getUpdateAndMoveToFirstFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.modified) {
            root = newRoot;
            if (details.updated) {
                first = details.getOldValue().getSequenceNumber() == first ? first : first - 1;
                last = details.getOldValue().getSequenceNumber() == last ? last - 1 : last;
            } else {
                modCount++;
                first--;
                size++;
            }
            renumber();
        }
        return details.modified;
    }

    //@Override
    public void addLast(E e) {
        addLast(e, true);
    }

    private boolean addLast(E e, boolean moveToLast) {
        final ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedElement<E>> newRoot = root.update(
                getOrCreateMutator(), new SequencedElement<>(e, last), Objects.hashCode(e), 0,
                details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.modified) {
            root = newRoot;
            if (details.updated) {
                first = details.getOldValue().getSequenceNumber() == first - 1 ? first - 1 : first;
                last = details.getOldValue().getSequenceNumber() == last ? last : last + 1;
            } else {
                modCount++;
                size++;
                last++;
            }
            renumber();
        }
        return details.modified;
    }

    @Override
    public void clear() {
        root = BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
        first = 0;
        last = 0;
    }

    /**
     * Returns a shallow copy of this set.
     */
    @Override
    public MutableLinkedChampSet<E> clone() {
        return (MutableLinkedChampSet<E>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return Node.NO_VALUE != root.findByKey(new SequencedElement<>((E) o),
                Objects.hashCode((E) o), 0, Objects::equals);
    }

    //@Override
    public E getFirst() {
        return HeapSequencedIterator.getFirst(root, first, last).getElement();
    }

    // @Override
    public E getLast() {
        return HeapSequencedIterator.getLast(root, first, last).getElement();
    }

    @Override
    public Iterator<E> iterator() {
        return iterator(false);
    }

    /**
     * Returns an iterator over the elements of this set, that optionally
     * iterates in reversed direction.
     *
     * @param reversed whether to iterate in reverse direction
     * @return an iterator
     */
    public Iterator<E> iterator(boolean reversed) {
        Iterator<E> i = BucketSequencedIterator.isSupported(size, first, last)
                ? new BucketSequencedIterator<>(size, first, last, root, reversed,
                this::iteratorRemove, SequencedElement::getElement)
                : new HeapSequencedIterator<>(size, root, reversed,
                this::iteratorRemove, SequencedElement::getElement);
        return new FailFastIterator<>(i,
                () -> MutableLinkedChampSet.this.modCount);
    }

    private void iteratorRemove(SequencedElement<E> element) {
        remove(element.getElement());
    }


    @Override
    public boolean remove(final Object o) {
        final ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        @SuppressWarnings("unchecked")//
        final BitmapIndexedNode<SequencedElement<E>> newRoot = root.remove(
                getOrCreateMutator(), new SequencedElement<>((E) o),
                Objects.hashCode(o), 0, details, Objects::equals);
        if (details.modified) {
            root = newRoot;
            size--;
            modCount++;
            int seq = details.getOldValue().getSequenceNumber();
            if (seq == last - 1) {
                last--;
            }
            if (seq == first) {
                first++;
            }
            renumber();
        }
        return details.modified;
    }


    //@Override
    public E removeFirst() {
        SequencedElement<E> k = HeapSequencedIterator.getFirst(root, first, last);
        remove(k.getElement());
        first = k.getSequenceNumber();
        renumber();
        return k.getElement();
    }

    //@Override
    public E removeLast() {
        SequencedElement<E> k = HeapSequencedIterator.getLast(root, first, last);
        remove(k.getElement());
        last = k.getSequenceNumber();
        renumber();
        return k.getElement();
    }

    /**
     * Renumbers the sequence numbers if they have overflown,
     * or if the extent of the sequence numbers is more than
     * 4 times the size of the set.
     */
    private void renumber() {
        if (size == 0) {
            first = -1;
            last = 0;
            return;
        }
        if (Sequenced.mustRenumber(size, first, last)) {
            root = SequencedElement.renumber(size, root, getOrCreateMutator(),
                    Objects::hashCode, Objects::equals);
            last = size;
            first = -1;
        }
    }

    /*
    @Override
    public SequencedSet<E> reversed() {
        return new WrappedSequencedSet<>(
                () -> iterator(true),
                () -> iterator(false),
                this::size,
                this::contains,
                this::clear,
                this::remove,
                this::getLast, this::getFirst,
                e -> addFirst(e, false), this::add,
                this::addLast, this::addFirst
        );
    }*/

    /**
     * Returns an immutable copy of this set.
     *
     * @return an immutable copy
     */
    public LinkedChampSet<E> toImmutable() {
        mutator = null;
        return size == 0 ? LinkedChampSet.of() : new LinkedChampSet<>(root, size, first, last);
    }

    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return new MutableLinkedChampSet<>(deserialized);
        }
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateFunction() {
        return (oldK, newK) -> oldK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToLastFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToFirstFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    public <U> U transform(Function<? super io.vavr.collection.Set<Long>, ? extends U> f) {
        // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
        //     wants us to have a transform() method although this class
        //     is a standard Collection class.
        throw new UnsupportedOperationException();
    }

}