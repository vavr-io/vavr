package io.vavr.collection;

import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.BucketSequencedIterator;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.HeapSequencedIterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.Sequenced;
import io.vavr.collection.champ.SequencedElement;
import io.vavr.collection.champ.UniqueId;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collector;

/**
 * Implements an immutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>allows null elements</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which elements were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>copyAdd: O(1) amortized</li>
 *     <li>copyRemove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toMutable: O(1) + a cost distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(N)</li>
 *     <li>iterator.next: O(1) with bucket sort or O(log N) with a heap</li>
 *     <li>getFirst(), getLast(): O(N)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP tree contains nodes that may be shared with other sets.
 * <p>
 * If a write operation is performed on a node, then this set creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1).
 * <p>
 * This set can create a mutable copy of itself in O(1) time and O(1) space
 * using method {@link #toMutable()}}. The mutable copy shares its nodes
 * with this set, until it has gradually replaced the nodes with exclusively
 * owned nodes.
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
public class LinkedChampSet<E> extends BitmapIndexedNode<SequencedElement<E>> implements SetMixin<E, LinkedChampSet<E>>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final LinkedChampSet<?> EMPTY = new LinkedChampSet<>(BitmapIndexedNode.emptyNode(), 0, -1, 0);

    final int size;

    /**
     * Counter for the sequence number of the last element. The counter is
     * incremented after a new entry has been added to the end of the sequence.
     */
    final int last;


    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement after a new entry has been added to the start of the sequence.
     */
    final int first;

    LinkedChampSet(BitmapIndexedNode<SequencedElement<E>> root, int size, int first, int last) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        assert (long) last - first >= size : "size=" + size + " first=" + first + " last=" + last;
        this.size = size;
        this.first = first;
        this.last = last;
    }


    /**
     * Returns an empty immutable set.
     *
     * @param <E> the element type
     * @return an empty immutable set
     */
    @SuppressWarnings("unchecked")
    public static <E> LinkedChampSet<E> empty() {
        return ((LinkedChampSet<E>) LinkedChampSet.EMPTY);
    }

    /**
     * Returns a LinkedChampSet set that contains the provided elements.
     *
     * @param iterable an iterable
     * @param <E>      the element type
     * @return a LinkedChampSet set of the provided elements
     */
    @SuppressWarnings("unchecked")
    public static <E> LinkedChampSet<E> ofAll(Iterable<? extends E> iterable) {
        return ((LinkedChampSet<E>) LinkedChampSet.EMPTY).addAll(iterable);
    }

    /**
     * Renumbers the sequenced elements in the trie if necessary.
     *
     * @param root  the root of the trie
     * @param size  the size of the trie
     * @param first the estimated first sequence number
     * @param last  the estimated last sequence number
     * @return a new {@link LinkedChampSet} instance
     */

    private LinkedChampSet<E> renumber(BitmapIndexedNode<SequencedElement<E>> root, int size, int first, int last) {
        if (size == 0) {
            return of();
        }
        if (Sequenced.mustRenumber(size, first, last)) {
            return new LinkedChampSet<>(
                    SequencedElement.renumber(size, root, new UniqueId(), Objects::hashCode, Objects::equals),
                    size, -1, size);
        }
        return new LinkedChampSet<>(root, size, first, last);
    }

    @Override
    public <R> Set<R> create() {
        return empty();
    }

    @Override
    public <R> LinkedChampSet<R> createFromElements(Iterable<? extends R> elements) {
        return ofAll(elements);
    }

    @Override
    public LinkedChampSet<E> add(E key) {
        return addLast(key, false);
    }

    private LinkedChampSet<E> addLast(final E key, boolean moveToLast) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedElement<E>> root = update(null,
                new SequencedElement<>(key, last), Objects.hashCode(key), 0, details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.updated) {
            return moveToLast
                    ? renumber(root, size,
                    details.getOldValue().getSequenceNumber() == first ? first + 1 : first,
                    details.getOldValue().getSequenceNumber() == last ? last : last + 1)
                    : new LinkedChampSet<>(root, size, first, last);
        }
        return details.modified ? renumber(root, size + 1, first, last + 1) : this;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public LinkedChampSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof LinkedChampSet<?>)) {
            return (LinkedChampSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableLinkedChampSet)) {
            return ((MutableLinkedChampSet<E>) set).toImmutable();
        }
        final MutableLinkedChampSet<E> t = this.toMutable();
        boolean modified = false;
        for (final E key : set) {
            modified |= t.add(key);
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public boolean contains(E o) {
        return findByKey(new SequencedElement<>(o), Objects.hashCode(o), 0, Objects::equals) != Node.NO_VALUE;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateFunction() {
        return (oldK, newK) -> oldK;
    }

    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getForceUpdateFunction() {
        return (oldK, newK) -> newK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToLastFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToFirstFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
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
        return BucketSequencedIterator.isSupported(size, first, last)
                ? new BucketSequencedIterator<>(size, first, last, this, reversed,
                null, SequencedElement::getElement)
                : new HeapSequencedIterator<>(size, this, reversed,
                null, SequencedElement::getElement);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Set<E> remove(final E key) {
        return copyRemove(key, first, last);
    }

    private LinkedChampSet<E> copyRemove(final E key, int newFirst, int newLast) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedElement<E>> newRootNode = remove(null,
                new SequencedElement<>(key),
                keyHash, 0, details, Objects::equals);
        if (details.modified) {
            int seq = details.getOldValue().getSequenceNumber();
            if (seq == newFirst) {
                newFirst++;
            }
            if (seq == newLast - 1) {
                newLast--;
            }
            return renumber(newRootNode, size - 1, newFirst, newLast);
        }
        return this;
    }

    MutableLinkedChampSet<E> toMutable() {
        return new MutableLinkedChampSet<>(this);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link LinkedChampSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.LinkedChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, LinkedChampSet<T>> collector() {
        return Collections.toListAndThen(LinkedChampSet::ofAll);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    public static <T> LinkedChampSet<T> of(T element) {
        return LinkedChampSet.<T>empty().add(element);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof LinkedChampSet) {
            LinkedChampSet<?> that = (LinkedChampSet<?>) other;
            return size == that.size && equivalent(that);
        }
        return Collections.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(iterator());
    }

    /**
     * Creates a LinkedChampSet of the given elements.
     *
     * <pre><code>LinkedChampSet.of(1, 2, 3, 4)</code></pre>
     *
     * @param <T>      Component type of the LinkedChampSet.
     * @param elements Zero or more elements.
     * @return A set containing the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> LinkedChampSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return LinkedChampSet.<T>empty().addAll(Arrays.asList(elements));
    }

    /**
     * Narrows a widened {@code LinkedChampSet<? extends T>} to {@code LinkedChampSet<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param hashSet A {@code LinkedChampSet}.
     * @param <T>     Component type of the {@code LinkedChampSet}.
     * @return the given {@code LinkedChampSet} instance as narrowed type {@code HashSet<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedChampSet<T> narrow(LinkedChampSet<? extends T> hashSet) {
        return (LinkedChampSet<T>) hashSet;
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    public static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        public SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return LinkedChampSet.ofAll(deserialized);
        }
    }

    private Object writeReplace() {
        return new LinkedChampSet.SerializationProxy<E>(this.toMutable());
    }

    @Override
    public LinkedChampSet<E> replace(E currentElement, E newElement) {
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }
        final int keyHash = Objects.hashCode(currentElement);
        final ChangeEvent<SequencedElement<E>> detailsCurrent = new ChangeEvent<>();
        BitmapIndexedNode<SequencedElement<E>> newRootNode = remove(null,
                new SequencedElement<>(currentElement),
                keyHash, 0, detailsCurrent, Objects::equals);
        if (!detailsCurrent.modified) {
            return this;
        }
        int seq = detailsCurrent.getOldValue().getSequenceNumber();
        ChangeEvent<SequencedElement<E>> detailsNew = new ChangeEvent<>();
        newRootNode = newRootNode.update(null,
                new SequencedElement<>(newElement, seq), Objects.hashCode(newElement), 0, detailsNew,
                getForceUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (detailsNew.updated) {
            return renumber(newRootNode, size - 1, first, last);
        } else {
            return new LinkedChampSet<>(newRootNode, size, first, last);
        }
    }

    @Override
    public boolean isSequential() {
        return true;
    }

    @Override
    public Set<E> toLinkedSet() {
        return this;
    }

    @Override
    public Set<E> takeRight(int n) {
        if (n >= size) {
            return this;
        }
        MutableLinkedChampSet<E> set = new MutableLinkedChampSet<>();
        for (Iterator<E> i = iterator(true); i.hasNext() && n > 0; n--) {
            set.addFirst(i.next());
        }
        return set.toImmutable();
    }

    @Override
    public Set<E> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        MutableLinkedChampSet<E> set = toMutable();
        for (Iterator<E> i = iterator(true); i.hasNext() && n > 0; n--) {
            set.remove(i.next());
        }
        return set.toImmutable();
    }

    @Override
    public LinkedChampSet<E> tail() {
        // XXX LinkedChampSetTest wants us to throw UnsupportedOperationException
        //     instead of NoSuchElementException when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        SequencedElement<E> k = HeapSequencedIterator.getFirst(this, first, last);
        return copyRemove(k.getElement(), k.getSequenceNumber() + 1, last);
    }

    @Override
    public E head() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return HeapSequencedIterator.getFirst(this, first, last).getElement();
    }

    @Override
    public LinkedChampSet<E> init() {
        // XXX Traversable.init() specifies that we must throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return copyRemoveLast();
    }

    private LinkedChampSet<E> copyRemoveLast() {
        SequencedElement<E> k = HeapSequencedIterator.getLast(this, first, last);
        return copyRemove(k.getElement(), first, k.getSequenceNumber());
    }


    @Override
    public Option<? extends Set<E>> initOption() {
        return isEmpty() ? Option.none() : Option.some(copyRemoveLast());
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super E, ? super U, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        U xs = zero;
        for (Iterator<E> i = iterator(true); i.hasNext(); ) {
            xs = combine.apply(i.next(), xs);
        }
        return xs;
    }
}
