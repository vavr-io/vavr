package io.vavr.collection.champ;

import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.stream.Collector;

import static io.vavr.collection.champ.SequencedData.mustRenumber;
import static io.vavr.collection.champ.SequencedData.seqHash;

/**
 * Implements a mutable set using two Compressed Hash-Array Mapped Prefix-trees
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which elements were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>copyAdd: O(1) amortized due to
 *  *     renumbering</li>
 *     <li>copyRemove: O(1) amortized due to
 *  *     renumbering</li>
 *     <li>contains: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(1)</li>
 *     <li>getFirst(), getLast(): O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other sets.
 * <p>
 * If a write operation is performed on a node, then this set creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1).
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
 * The renumbering is why the {@code add} and {@code remove} methods are O(1)
 * only in an amortized sense.
 * <p>
 * To support iteration, a second CHAMP trie is maintained. The second CHAMP
 * trie has the same contents as the first. However, we use the sequence number
 * for computing the hash code of an element.
 * <p>
 * In this implementation, a hash code has a length of
 * 32 bits, and is split up in little-endian order into 7 parts of
 * 5 bits (the last part contains the remaining bits).
 * <p>
 * We convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE
 * to it. And then we reorder its bits from
 * 66666555554444433333222221111100 to 00111112222233333444445555566666.
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
public class SequencedChampSet<E> extends BitmapIndexedNode<SequencedElement<E>> implements VavrSetMixin<E, SequencedChampSet<E>>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final SequencedChampSet<?> EMPTY = new SequencedChampSet<>(
            BitmapIndexedNode.emptyNode(), BitmapIndexedNode.emptyNode(), 0, -1, 0);

    final @NonNull BitmapIndexedNode<SequencedElement<E>> sequenceRoot;
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

    SequencedChampSet(
            @NonNull BitmapIndexedNode<SequencedElement<E>> root,
            @NonNull BitmapIndexedNode<SequencedElement<E>> sequenceRoot,
            int size, int first, int last) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        assert (long) last - first >= size : "size=" + size + " first=" + first + " last=" + last;
        this.size = size;
        this.first = first;
        this.last = last;
        this.sequenceRoot = Objects.requireNonNull(sequenceRoot);
    }

    static <E> BitmapIndexedNode<SequencedElement<E>> buildSequenceRoot(@NonNull BitmapIndexedNode<SequencedElement<E>> root, @NonNull IdentityObject mutator) {
        BitmapIndexedNode<SequencedElement<E>> seqRoot = emptyNode();
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        for (KeyIterator<SequencedElement<E>> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            SequencedElement<E> elem = i.next();
            seqRoot = seqRoot.update(mutator, elem, SequencedData.seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, SequencedData::seqEquals, SequencedData::seqHash);
        }
        return seqRoot;
    }

    /**
     * Returns an empty immutable set.
     *
     * @param <E> the element type
     * @return an empty immutable set
     */
    @SuppressWarnings("unchecked")
    public static <E> SequencedChampSet<E> empty() {
        return ((SequencedChampSet<E>) SequencedChampSet.EMPTY);
    }

    /**
     * Returns a LinkedChampSet set that contains the provided elements.
     *
     * @param iterable an iterable
     * @param <E>      the element type
     * @return a LinkedChampSet set of the provided elements
     */
    @SuppressWarnings("unchecked")
    public static <E> SequencedChampSet<E> ofAll(Iterable<? extends E> iterable) {
        return ((SequencedChampSet<E>) SequencedChampSet.EMPTY).addAll(iterable);
    }


    /**
     * Renumbers the sequenced elements in the trie if necessary.
     *
     * @param root    the root of the trie
     * @param seqRoot
     * @param size    the size of the trie
     * @param first   the estimated first sequence number
     * @param last    the estimated last sequence number
     * @return a new {@link SequencedChampSet} instance
     */
    @NonNull
    private SequencedChampSet<E> renumber(
            BitmapIndexedNode<SequencedElement<E>> root,
            BitmapIndexedNode<SequencedElement<E>> seqRoot,
            int size, int first, int last) {
        if (mustRenumber(size, first, last)) {
            IdentityObject mutator = new IdentityObject();
            BitmapIndexedNode<SequencedElement<E>> renumberedRoot = SequencedData.renumber(
                    size, root, seqRoot, mutator, Objects::hashCode, Objects::equals,
                    (e, seq) -> new SequencedElement<>(e.getElement(), seq));
            BitmapIndexedNode<SequencedElement<E>> renumberedSeqRoot = buildSequenceRoot(renumberedRoot, mutator);
            return new SequencedChampSet<>(
                    renumberedRoot, renumberedSeqRoot,
                    size, -1, size);
        }
        return new SequencedChampSet<>(root, seqRoot, size, first, last);
    }

    /**
     * Creates an empty set of the specified element type.
     *
     * @param <R> the element type
     * @return a new empty set.
     */
    @Override
    public <R> Set<R> create() {
        return empty();
    }

    /**
     * Creates an empty set of the specified element type, and adds all
     * the specified elements.
     *
     * @param elements the elements
     * @param <R>      the element type
     * @return a new set that contains the specified elements.
     */
    @Override
    public <R> SequencedChampSet<R> createFromElements(Iterable<? extends R> elements) {
        return ofAll(elements);
    }

    @Override
    public SequencedChampSet<E> add(E key) {
        return addLast(key, false);
    }

    private @NonNull SequencedChampSet<E> addLast(@Nullable E e,
                                                  boolean moveToLast) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        SequencedElement<E> newElem = new SequencedElement<>(e, last);
        var newRoot = update(
                null, newElem, Objects.hashCode(e), 0,
                details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        var newSeqRoot = sequenceRoot;
        int newFirst = first;
        int newLast = last;
        int newSize = size;
        if (details.isModified()) {
            IdentityObject mutator = new IdentityObject();
            SequencedElement<E> oldElem = details.getData();
            boolean isReplaced = details.isReplaced();
            newSeqRoot = newSeqRoot.update(mutator,
                    newElem, seqHash(last), 0, details,
                    getUpdateFunction(),
                    SequencedData::seqEquals, SequencedData::seqHash);
            if (isReplaced) {
                newSeqRoot = newSeqRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        SequencedData::seqEquals);

                newFirst = details.getData().getSequenceNumber() == newFirst - 1 ? newFirst - 1 : newFirst;
                newLast = details.getData().getSequenceNumber() == newLast ? newLast : newLast + 1;
            } else {
                newSize++;
                newLast++;
            }
            return renumber(newRoot, newSeqRoot, newSize, newFirst, newLast);
        }
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public SequencedChampSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof SequencedChampSet<?>)) {
            return (SequencedChampSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableSequencedChampSet)) {
            return ((MutableSequencedChampSet<E>) set).toImmutable();
        }
        final MutableSequencedChampSet<E> t = this.toMutable();
        boolean modified = false;
        for (final E key : set) {
            modified |= t.add(key);
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public boolean contains(E o) {
        return find(new SequencedElement<>(o), Objects.hashCode(o), 0, Objects::equals) != Node.NO_DATA;
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

    private @NonNull Iterator<E> iterator(boolean reversed) {
        Enumerator<E> i;
        if (reversed) {
            i = new ReversedKeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE, size());
        } else {
            i = new KeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE, size());
        }
        return new VavrIteratorFacade<>(i, null);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public SequencedChampSet<E> remove(final E key) {
        return remove(key, first, last);
    }

    private @NonNull SequencedChampSet<E> remove(@Nullable E key, int newFirst, int newLast) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedElement<E>> newRoot = remove(null,
                new SequencedElement<>(key),
                keyHash, 0, details, Objects::equals);
        BitmapIndexedNode<SequencedElement<E>> newSeqRoot = sequenceRoot;
        if (details.isModified()) {
            var oldElem = details.getData();
            int seq = oldElem.getSequenceNumber();
            newSeqRoot = newSeqRoot.remove(null,
                    oldElem,
                    seqHash(seq), 0, details, SequencedData::seqEquals);
            if (seq == newFirst) {
                newFirst++;
            }
            if (seq == newLast - 1) {
                newLast--;
            }
            return renumber(newRoot, newSeqRoot, size - 1, newFirst, newLast);
        }
        return this;
    }

    /**
     * Creates a mutable copy of this set.
     *
     * @return a mutable sequenced CHAMP set
     */
    MutableSequencedChampSet<E> toMutable() {
        return new MutableSequencedChampSet<>(this);
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link SequencedChampSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.LinkedChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, SequencedChampSet<T>> collector() {
        return Collections.toListAndThen(SequencedChampSet::ofAll);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    public static <T> SequencedChampSet<T> of(T element) {
        return SequencedChampSet.<T>empty().add(element);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof SequencedChampSet) {
            SequencedChampSet<?> that = (SequencedChampSet<?>) other;
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
    public static <T> SequencedChampSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return SequencedChampSet.<T>empty().addAll(Arrays.asList(elements));
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
    public static <T> SequencedChampSet<T> narrow(SequencedChampSet<? extends T> hashSet) {
        return (SequencedChampSet<T>) hashSet;
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        public SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return SequencedChampSet.ofAll(deserialized);
        }
    }

    private Object writeReplace() {
        return new SequencedChampSet.SerializationProxy<E>(this.toMutable());
    }

    @Override
    public SequencedChampSet<E> replace(E currentElement, E newElement) {
        // currentElement and newElem are the same => do nothing
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }

        // try to remove currentElem from the 'root' trie
        final ChangeEvent<SequencedElement<E>> detailsCurrent = new ChangeEvent<>();
        IdentityObject mutator = new IdentityObject();
        BitmapIndexedNode<SequencedElement<E>> newRoot = remove(mutator,
                new SequencedElement<>(currentElement),
                Objects.hashCode(currentElement), 0, detailsCurrent, Objects::equals);
        // currentElement was not in the 'root' trie => do nothing
        if (!detailsCurrent.isModified()) {
            return this;
        }

        // currentElement was in the 'root' trie, and we have just removed it
        // => also remove its entry from the 'sequenceRoot' trie
        var newSeqRoot = sequenceRoot;
        SequencedElement<E> currentData = detailsCurrent.getData();
        int seq = currentData.getSequenceNumber();
        newSeqRoot = newSeqRoot.remove(mutator, currentData, seqHash(seq), 0, detailsCurrent, SequencedData::seqEquals);

        // try to update the trie with the newElement
        ChangeEvent<SequencedElement<E>> detailsNew = new ChangeEvent<>();
        SequencedElement<E> newData = new SequencedElement<>(newElement, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement), 0, detailsNew,
                getForceUpdateFunction(),
                Objects::equals, Objects::hashCode);
        boolean isReplaced = detailsNew.isReplaced();

        // there already was an element with key newElement._1 in the trie, and we have just replaced it
        // => remove the replaced entry from the 'sequenceRoot' trie
        if (isReplaced) {
            SequencedElement<E> replacedEntry = detailsNew.getData();
            newSeqRoot = newSeqRoot.remove(mutator, replacedEntry, seqHash(replacedEntry.getSequenceNumber()), 0, detailsNew, SequencedData::seqEquals);
        }

        // we have just successfully added or replaced the newElement
        // => insert the new entry in the 'sequenceRoot' trie
        newSeqRoot = newSeqRoot.update(mutator,
                newData, seqHash(seq), 0, detailsNew,
                getForceUpdateFunction(),
                SequencedData::seqEquals, SequencedData::seqHash);

        if (isReplaced) {
            // we reduced the size of the map by one => renumbering may be necessary
            return renumber(newRoot, newSeqRoot, size - 1, first, last);
        } else {
            // we did not change the size of the map => no renumbering is needed
            return new SequencedChampSet<>(newRoot, newSeqRoot, size, first, last);
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
        MutableSequencedChampSet<E> set = new MutableSequencedChampSet<>();
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
        MutableSequencedChampSet<E> set = toMutable();
        for (Iterator<E> i = iterator(true); i.hasNext() && n > 0; n--) {
            set.remove(i.next());
        }
        return set.toImmutable();
    }

    @Override
    public SequencedChampSet<E> tail() {
        // XXX LinkedChampSetTest wants us to throw UnsupportedOperationException
        //     instead of NoSuchElementException when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        SequencedElement<E> k = Node.getFirst(this);
        return remove(k.getElement(), k.getSequenceNumber() + 1, last);
    }

    @Override
    public E head() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return Node.getFirst(this).getElement();
    }

    @Override
    public SequencedChampSet<E> init() {
        // XXX Traversable.init() specifies that we must throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return removeLast();
    }

    private SequencedChampSet<E> removeLast() {
        SequencedElement<E> k = Node.getLast(this);
        return remove(k.getElement(), first, k.getSequenceNumber());
    }


    @Override
    public Option<? extends Set<E>> initOption() {
        return isEmpty() ? Option.none() : Option.some(removeLast());
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
