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
public class LinkedChampChampSet<E> extends BitmapIndexedNode<SequencedElement<E>> implements VavrSetMixin<E, LinkedChampChampSet<E>>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final LinkedChampChampSet<?> EMPTY = new LinkedChampChampSet<>(
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

    LinkedChampChampSet(
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
            seqRoot = seqRoot.update(mutator, elem, LinkedChampChampSet.seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, Object::equals, LinkedChampChampSet::seqHashCode);
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
    public static <E> LinkedChampChampSet<E> empty() {
        return ((LinkedChampChampSet<E>) LinkedChampChampSet.EMPTY);
    }

    /**
     * Returns a LinkedChampSet set that contains the provided elements.
     *
     * @param iterable an iterable
     * @param <E>      the element type
     * @return a LinkedChampSet set of the provided elements
     */
    @SuppressWarnings("unchecked")
    public static <E> LinkedChampChampSet<E> ofAll(Iterable<? extends E> iterable) {
        return ((LinkedChampChampSet<E>) LinkedChampChampSet.EMPTY).addAll(iterable);
    }

    /**
     * Returns true if the sequenced elements must be renumbered because
     * {@code first} or {@code last} are at risk of overflowing.
     * <p>
     * {@code first} and {@code last} are estimates of the first and last
     * sequence numbers in the trie. The estimated extent may be larger
     * than the actual extent, but not smaller.
     *
     * @param size  the size of the trie
     * @param first the estimated first sequence number
     * @param last  the estimated last sequence number
     * @return
     */
    static boolean mustRenumber(int size, int first, int last) {
        return size == 0 && (first != -1 || last != 0)
                || last > Integer.MAX_VALUE - 2
                || first < Integer.MIN_VALUE + 2;
    }

    /**
     * Renumbers the sequenced elements in the trie if necessary.
     *
     * @param root    the root of the trie
     * @param seqRoot
     * @param size    the size of the trie
     * @param first   the estimated first sequence number
     * @param last    the estimated last sequence number
     * @return a new {@link LinkedChampChampSet} instance
     */
    @NonNull
    private LinkedChampChampSet<E> renumber(
            BitmapIndexedNode<SequencedElement<E>> root,
            BitmapIndexedNode<SequencedElement<E>> seqRoot,
            int size, int first, int last) {
        if (mustRenumber(size, first, last)) {
            IdentityObject mutator = new IdentityObject();
            BitmapIndexedNode<SequencedElement<E>> renumberedRoot = SequencedElement.renumber(size, root, mutator, Objects::hashCode, Objects::equals);
            BitmapIndexedNode<SequencedElement<E>> renumberedSeqRoot = buildSequenceRoot(renumberedRoot, mutator);
            return new LinkedChampChampSet<>(
                    renumberedRoot, renumberedSeqRoot,
                    size, -1, size);
        }
        return new LinkedChampChampSet<>(root, seqRoot, size, first, last);
    }

    @Override
    public <R> Set<R> create() {
        return empty();
    }

    @Override
    public <R> LinkedChampChampSet<R> createFromElements(Iterable<? extends R> elements) {
        return ofAll(elements);
    }

    @Override
    public LinkedChampChampSet<E> add(E key) {
        return copyAddLast(key, false);
    }

    private @NonNull LinkedChampChampSet<E> copyAddLast(@Nullable E e,
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
            boolean isUpdated = details.isReplaced();
            newSeqRoot = newSeqRoot.update(mutator,
                    newElem, seqHash(last), 0, details,
                    getUpdateFunction(),
                    Objects::equals, LinkedChampChampSet::seqHashCode);
            if (isUpdated) {
                newSeqRoot = newSeqRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        Objects::equals);

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
    public LinkedChampChampSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof LinkedChampChampSet<?>)) {
            return (LinkedChampChampSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableLinkedChampChampSet)) {
            return ((MutableLinkedChampChampSet<E>) set).toImmutable();
        }
        final MutableLinkedChampChampSet<E> t = this.toMutable();
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
            i = new ReversedKeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE, size());
        } else {
            i = new KeyEnumeratorSpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE, size());
        }
        return new VavrIteratorFacade<>(i, null);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public LinkedChampChampSet<E> remove(final E key) {
        return copyRemove(key, first, last);
    }

    private @NonNull LinkedChampChampSet<E> copyRemove(@Nullable E key, int newFirst, int newLast) {
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
                    seqHash(seq), 0, details, Objects::equals);
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

    MutableLinkedChampChampSet<E> toMutable() {
        return new MutableLinkedChampChampSet<>(this);
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link LinkedChampChampSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.LinkedChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, LinkedChampChampSet<T>> collector() {
        return Collections.toListAndThen(LinkedChampChampSet::ofAll);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    public static <T> LinkedChampChampSet<T> of(T element) {
        return LinkedChampChampSet.<T>empty().add(element);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof LinkedChampChampSet) {
            LinkedChampChampSet<?> that = (LinkedChampChampSet<?>) other;
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
    public static <T> LinkedChampChampSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return LinkedChampChampSet.<T>empty().addAll(Arrays.asList(elements));
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
    public static <T> LinkedChampChampSet<T> narrow(LinkedChampChampSet<? extends T> hashSet) {
        return (LinkedChampChampSet<T>) hashSet;
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
            return LinkedChampChampSet.ofAll(deserialized);
        }
    }

    private Object writeReplace() {
        return new LinkedChampChampSet.SerializationProxy<E>(this.toMutable());
    }

    @Override
    public LinkedChampChampSet<E> replace(E currentElement, E newElement) {
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

        // currentElement was in the 'root' trie => also remove it from the 'sequenceRoot' trie
        var newSeqRoot = sequenceRoot;
        SequencedElement<E> currentData = detailsCurrent.getData();
        int seq = currentData.getSequenceNumber();
        newSeqRoot = newSeqRoot.remove(mutator, currentData, seqHash(seq), 0, detailsCurrent, LinkedChampChampSet::seqEquals);

        // try to update the newElement
        ChangeEvent<SequencedElement<E>> detailsNew = new ChangeEvent<>();
        SequencedElement<E> newData = new SequencedElement<>(newElement, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement), 0, detailsNew,
                getForceUpdateFunction(),
                Objects::equals, Objects::hashCode);
        boolean isReplaced = detailsNew.isReplaced();
        SequencedElement<E> replacedData = detailsNew.getData();

        // the newElement was replaced => remove the replaced data from the 'sequenceRoot' trie
        if (isReplaced) {
            newSeqRoot = newSeqRoot.remove(mutator, replacedData, seqHash(replacedData.getSequenceNumber()), 0, detailsNew, LinkedChampChampSet::seqEquals);
        }

        // the newElement was inserted => insert it also in the 'sequenceRoot' trie
        newSeqRoot = newSeqRoot.update(mutator,
                newData, seqHash(seq), 0, detailsNew,
                getForceUpdateFunction(),
                LinkedChampChampSet::seqEquals, LinkedChampChampSet::seqHashCode);

        if (isReplaced) {
            // the newElement was already in the trie => renumbering may be necessary
            return renumber(newRoot, newSeqRoot, size - 1, first, last);
        } else {
            // the newElement was not in the trie => no renumbering is needed
            return new LinkedChampChampSet<>(newRoot, newSeqRoot, size, first, last);
        }
    }

    private static <E> boolean seqEquals(SequencedElement<E> a, SequencedElement<E> b) {
        return a.getSequenceNumber() == b.getSequenceNumber();
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
        MutableLinkedChampChampSet<E> set = new MutableLinkedChampChampSet<>();
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
        MutableLinkedChampChampSet<E> set = toMutable();
        for (Iterator<E> i = iterator(true); i.hasNext() && n > 0; n--) {
            set.remove(i.next());
        }
        return set.toImmutable();
    }

    @Override
    public LinkedChampChampSet<E> tail() {
        // XXX LinkedChampSetTest wants us to throw UnsupportedOperationException
        //     instead of NoSuchElementException when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        SequencedElement<E> k = BucketSequencedIterator.getFirst(this, first, last);
        return copyRemove(k.getElement(), k.getSequenceNumber() + 1, last);
    }

    @Override
    public E head() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return BucketSequencedIterator.getFirst(this, first, last).getElement();
    }

    @Override
    public LinkedChampChampSet<E> init() {
        // XXX Traversable.init() specifies that we must throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return copyRemoveLast();
    }

    private LinkedChampChampSet<E> copyRemoveLast() {
        SequencedElement<E> k = BucketSequencedIterator.getLast(this, first, last);
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


    /**
     * Computes a hash code from the sequence number, so that we can
     * use it for iteration in a CHAMP trie.
     * <p>
     * Convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE.
     * Then reorders its bits from 66666555554444433333222221111100 to
     * 00111112222233333444445555566666.
     *
     * @param sequenceNumber a sequence number
     * @return a hash code
     */
    static int seqHash(int sequenceNumber) {
        int u = sequenceNumber + Integer.MIN_VALUE;
        return (u >>> 27)
                | ((u & 0b00000_11111_00000_00000_00000_00000_00) >>> 17)
                | ((u & 0b00000_00000_11111_00000_00000_00000_00) >>> 7)
                | ((u & 0b00000_00000_00000_11111_00000_00000_00) << 3)
                | ((u & 0b00000_00000_00000_00000_11111_00000_00) << 13)
                | ((u & 0b00000_00000_00000_00000_00000_11111_00) << 23)
                | ((u & 0b00000_00000_00000_00000_00000_00000_11) << 30);
    }

    static <E> int seqHashCode(SequencedElement<E> e) {
        return seqHash(e.getSequenceNumber());
    }
}
