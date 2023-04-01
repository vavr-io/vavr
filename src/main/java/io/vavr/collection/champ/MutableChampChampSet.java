package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vavr.collection.champ.ChampChampSet.seqHash;


/**
 * Implements a mutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is mutable</li>
 *     <li>is not thread-safe</li>
 *     <li>iterates in the order, in which elements were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>add: O(1) amortized</li>
 *     <li>remove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toImmutable: O(1) + O(log N) distributed across subsequent updates in
 *     this set</li>
 *     <li>clone: O(1) + O(log N) distributed across subsequent updates in this
 *     set and in the clone</li>
 *     <li>iterator creation: O(N)</li>
 *     <li>iterator.next: O(1) with bucket sort, O(log N) with heap sort</li>
 *     <li>getFirst, getLast: O(N)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other sets, and nodes
 * that are exclusively owned by this set.
 * <p>
 * If a write operation is performed on an exclusively owned node, then this
 * set is allowed to mutate the node (mutate-on-write).
 * If a write operation is performed on a potentially shared node, then this
 * set is forced to create an exclusive copy of the node and of all not (yet)
 * exclusively owned parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1) in either
 * case.
 * <p>
 * This set can create an immutable copy of itself in O(1) time and O(1) space
 * using method {@link #toImmutable()}. This set loses exclusive ownership of
 * all its tree nodes.
 * Thus, creating an immutable copy increases the constant cost of
 * subsequent writes, until all shared nodes have been gradually replaced by
 * exclusively owned nodes again.
 * <p>
 * Insertion Order:
 * <p>
 * This set uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code add} is O(1) only in an amortized sense.
 * <p>
 * The iterator of the set is a priority queue, that orders the entries by
 * their stored insertion counter value. This is why {@code iterator.next()}
 * is O(log n).
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
public class MutableChampChampSet<E> extends AbstractChampSet<E, SequencedElement<E>> {
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
     * The root of the CHAMP trie for the sequence numbers.
     */
    private @NonNull BitmapIndexedNode<SequencedElement<E>> sequenceRoot;

    /**
     * Constructs an empty set.
     */
    public MutableChampChampSet() {
        root = BitmapIndexedNode.emptyNode();
        sequenceRoot = BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a set containing the elements in the specified
     * {@link Iterable}.
     *
     * @param c an iterable
     */
    @SuppressWarnings("unchecked")
    public MutableChampChampSet(Iterable<? extends E> c) {
        if (c instanceof MutableChampChampSet<?>) {
            c = ((MutableChampChampSet<? extends E>) c).toImmutable();
        }
        if (c instanceof ChampChampSet<?>) {
            ChampChampSet<E> that = (ChampChampSet<E>) c;
            this.root = that;
            this.size = that.size;
            this.first = that.first;
            this.last = that.last;
            this.sequenceRoot = that.sequenceRoot;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            this.sequenceRoot = BitmapIndexedNode.emptyNode();
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

    /**
     * Gets the mutator id of this set. Creates a new id, if this
     * set has no mutator id.
     *
     * @return a new unique id or the existing unique id.
     */
    protected @NonNull IdentityObject getOrCreateIdentity() {
        if (mutator == null) {
            mutator = new IdentityObject();
        }
        return mutator;
    }

    private boolean addFirst(E e, boolean moveToFirst) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        SequencedElement<E> newElem = new SequencedElement<>(e, first - 1);
        IdentityObject mutator = getOrCreateIdentity();
        root = root.update(mutator, newElem,
                Objects.hashCode(e), 0, details,
                moveToFirst ? getUpdateAndMoveToFirstFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            SequencedElement<E> oldElem = details.getData();
            boolean isUpdated = details.isUpdated();
            sequenceRoot = sequenceRoot.update(mutator,
                    newElem, seqHash(first - 1), 0, details,
                    getUpdateFunction(),
                    Objects::equals, ChampChampSet::seqHashCode);
            if (isUpdated) {
                sequenceRoot = sequenceRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        Objects::equals);

                first = details.getData().getSequenceNumber() == first ? first : first - 1;
                last = details.getData().getSequenceNumber() == last ? last - 1 : last;
            } else {
                modCount++;
                first--;
                size++;
            }
            renumber();
        }
        return details.isModified();
    }

    //@Override
    public void addLast(E e) {
        addLast(e, true);
    }

    private boolean addLast(E e, boolean moveToLast) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        SequencedElement<E> newElem = new SequencedElement<>(e, last);
        IdentityObject mutator = getOrCreateIdentity();
        root = root.update(
                mutator, newElem, Objects.hashCode(e), 0,
                details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            SequencedElement<E> oldElem = details.getData();
            boolean isUpdated = details.isUpdated();
            sequenceRoot = sequenceRoot.update(mutator,
                    newElem, seqHash(last), 0, details,
                    getUpdateFunction(),
                    Objects::equals, ChampChampSet::seqHashCode);
            if (isUpdated) {
                sequenceRoot = sequenceRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        Objects::equals);

                first = details.getData().getSequenceNumber() == first - 1 ? first - 1 : first;
                last = details.getData().getSequenceNumber() == last ? last : last + 1;
            } else {
                modCount++;
                size++;
                last++;
            }
            renumber();
        }
        return details.isModified();
    }

    @Override
    public void clear() {
        root = BitmapIndexedNode.emptyNode();
        sequenceRoot = BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
        first = -1;
        last = 0;
    }

    /**
     * Returns a shallow copy of this set.
     */
    @Override
    public MutableChampChampSet<E> clone() {
        return (MutableChampChampSet<E>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return Node.NO_DATA != root.find(new SequencedElement<>((E) o),
                Objects.hashCode((E) o), 0, Objects::equals);
    }

    //@Override
    public E getFirst() {
        return Node.getFirst(sequenceRoot).getElement();
    }

    // @Override
    public E getLast() {
        return Node.getLast(sequenceRoot).getElement();
    }

    @Override
    public Iterator<E> iterator() {
        return iterator(false);
    }

    private @NonNull Iterator<E> iterator(boolean reversed) {
        Enumerator<E> i;
        if (reversed) {
            i = new ReversedKeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        } else {
            i = new KeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        }
        return new FailFastIterator<>(new IteratorFacade<>(i, this::iteratorRemove), () -> MutableChampChampSet.this.modCount);
    }

    private @NonNull Spliterator<E> spliterator(boolean reversed) {
        Spliterator<E> i;
        if (reversed) {
            i = new ReversedKeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        } else {
            i = new KeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        }
        return i;
    }

    @Override
    public @NonNull Spliterator<E> spliterator() {
        return spliterator(false);
    }

    private void iteratorRemove(E element) {
        mutator = null;
        remove(element);
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        IdentityObject mutator = getOrCreateIdentity();
        root = root.remove(
                mutator, new SequencedElement<>((E) o),
                Objects.hashCode(o), 0, details, Objects::equals);
        if (details.isModified()) {
            size--;
            modCount++;
            var elem = details.getData();
            int seq = elem.getSequenceNumber();
            sequenceRoot = sequenceRoot.remove(mutator,
                    elem,
                    seqHash(seq), 0, details, Objects::equals);
            if (seq == last - 1) {
                last--;
            }
            if (seq == first) {
                first++;
            }
            renumber();
        }
        return details.isModified();
    }


    //@Override
    public E removeFirst() {
        SequencedElement<E> k = Node.getFirst(sequenceRoot);
        remove(k.getElement());
        return k.getElement();
    }

    //@Override
    public E removeLast() {
        SequencedElement<E> k = Node.getLast(sequenceRoot);
        remove(k.getElement());
        return k.getElement();
    }

    /**
     * Renumbers the sequence numbers if they have overflown,
     * or if the extent of the sequence numbers is more than
     * 4 times the size of the set.
     */
    private void renumber() {
        if (ChampChampSet.mustRenumber(size, first, last)) {
            IdentityObject mutator = getOrCreateIdentity();
            root = SequencedElement.renumber(size, root, mutator,
                    Objects::hashCode, Objects::equals);
            sequenceRoot = ChampChampSet.buildSequenceRoot(root, mutator);
            last = size;
            first = -1;
        }
    }


    /**
     * Returns an immutable copy of this set.
     *
     * @return an immutable copy
     */
    public ChampChampSet<E> toImmutable() {
        mutator = null;
        return size == 0 ? ChampChampSet.of() : new ChampChampSet<>(root, sequenceRoot, size, first, last);
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
            return new MutableChampChampSet<>(deserialized);
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