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
 * Implements an immutable set using a Compressed Hash-Array Mapped Prefix-tree
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
 *     <li>copyAdd: O(1) amortized</li>
 *     <li>copyRemove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(N)</li>
 *     <li>iterator.next: O(1) with bucket sort, O(log N) with heap sort</li>
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
public class ChampChampSet<E> extends BitmapIndexedNode<SequencedElement<E>> implements VavrSetMixin<E, ChampChampSet<E>>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ChampChampSet<?> EMPTY = new ChampChampSet<>(
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

    ChampChampSet(
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
            seqRoot = seqRoot.update(mutator, elem, ChampChampSet.seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, Object::equals, ChampChampSet::seqHashCode);
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
    public static <E> ChampChampSet<E> empty() {
        return ((ChampChampSet<E>) ChampChampSet.EMPTY);
    }

    /**
     * Returns a LinkedChampSet set that contains the provided elements.
     *
     * @param iterable an iterable
     * @param <E>      the element type
     * @return a LinkedChampSet set of the provided elements
     */
    @SuppressWarnings("unchecked")
    public static <E> ChampChampSet<E> ofAll(Iterable<? extends E> iterable) {
        return ((ChampChampSet<E>) ChampChampSet.EMPTY).addAll(iterable);
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
     * @return a new {@link ChampChampSet} instance
     */
    @NonNull
    private ChampChampSet<E> renumber(
            BitmapIndexedNode<SequencedElement<E>> root,
            BitmapIndexedNode<SequencedElement<E>> seqRoot,
            int size, int first, int last) {
        if (mustRenumber(size, first, last)) {
            IdentityObject mutator = new IdentityObject();
            BitmapIndexedNode<SequencedElement<E>> renumberedRoot = SequencedElement.renumber(size, root, mutator, Objects::hashCode, Objects::equals);
            BitmapIndexedNode<SequencedElement<E>> renumberedSeqRoot = buildSequenceRoot(renumberedRoot, mutator);
            return new ChampChampSet<>(
                    renumberedRoot, renumberedSeqRoot,
                    size, -1, size);
        }
        return new ChampChampSet<>(root, seqRoot, size, first, last);
    }

    @Override
    public <R> Set<R> create() {
        return empty();
    }

    @Override
    public <R> ChampChampSet<R> createFromElements(Iterable<? extends R> elements) {
        return ofAll(elements);
    }

    @Override
    public ChampChampSet<E> add(E key) {
        return copyAddLast(key, false);
    }

    private @NonNull ChampChampSet<E> copyAddLast(@Nullable E e,
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
            boolean isUpdated = details.isUpdated();
            newSeqRoot = newSeqRoot.update(mutator,
                    newElem, seqHash(last), 0, details,
                    getUpdateFunction(),
                    Objects::equals, ChampChampSet::seqHashCode);
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
    public ChampChampSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof ChampChampSet<?>)) {
            return (ChampChampSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableChampChampSet)) {
            return ((MutableChampChampSet<E>) set).toImmutable();
        }
        final MutableChampChampSet<E> t = this.toMutable();
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
    public ChampChampSet<E> remove(final E key) {
        return copyRemove(key, first, last);
    }

    private @NonNull ChampChampSet<E> copyRemove(@Nullable E key, int newFirst, int newLast) {
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

    MutableChampChampSet<E> toMutable() {
        return new MutableChampChampSet<>(this);
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link ChampChampSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.LinkedChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, ChampChampSet<T>> collector() {
        return Collections.toListAndThen(ChampChampSet::ofAll);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    public static <T> ChampChampSet<T> of(T element) {
        return ChampChampSet.<T>empty().add(element);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof ChampChampSet) {
            ChampChampSet<?> that = (ChampChampSet<?>) other;
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
    public static <T> ChampChampSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return ChampChampSet.<T>empty().addAll(Arrays.asList(elements));
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
    public static <T> ChampChampSet<T> narrow(ChampChampSet<? extends T> hashSet) {
        return (ChampChampSet<T>) hashSet;
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
            return ChampChampSet.ofAll(deserialized);
        }
    }

    private Object writeReplace() {
        return new ChampChampSet.SerializationProxy<E>(this.toMutable());
    }

    @Override
    public ChampChampSet<E> replace(E currentElement, E newElement) {
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }

        final ChangeEvent<SequencedElement<E>> detailsCurrent = new ChangeEvent<>();
        IdentityObject mutator = new IdentityObject();
        BitmapIndexedNode<SequencedElement<E>> newRoot = remove(mutator,
                new SequencedElement<>(currentElement),
                Objects.hashCode(currentElement), 0, detailsCurrent, Objects::equals);
        if (!detailsCurrent.isModified()) {
            return this;
        }

        SequencedElement<E> currentData = detailsCurrent.getData();
        int seq = currentData.getSequenceNumber();
        ChangeEvent<SequencedElement<E>> detailsNew = new ChangeEvent<>();
        SequencedElement<E> newData = new SequencedElement<>(newElement, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement), 0, detailsNew,
                getForceUpdateFunction(),
                Objects::equals, Objects::hashCode);
        boolean isUpdated = detailsNew.isUpdated();
        SequencedElement<E> newDataThatWasReplaced = detailsNew.getData();
        var newSeqRoot = sequenceRoot;
        if (!Objects.equals(newElement, currentElement)) {
            newSeqRoot = newSeqRoot.remove(mutator, currentData, seqHash(seq), 0, detailsNew, ChampChampSet::seqEquals);
            if (newDataThatWasReplaced != null) {
                newSeqRoot = newSeqRoot.remove(mutator, newDataThatWasReplaced, seqHash(newDataThatWasReplaced.getSequenceNumber()), 0, detailsNew, ChampChampSet::seqEquals);
            }
        }
        newSeqRoot = newSeqRoot.update(mutator,
                newData, seqHash(seq), 0, detailsNew,
                getForceUpdateFunction(),
                ChampChampSet::seqEquals, ChampChampSet::seqHashCode);
        if (isUpdated) {
            return renumber(newRoot, newSeqRoot, size - 1, first, last);
        } else {
            return new ChampChampSet<>(newRoot, newSeqRoot, size, first, last);
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
        MutableChampChampSet<E> set = new MutableChampChampSet<>();
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
        MutableChampChampSet<E> set = toMutable();
        for (Iterator<E> i = iterator(true); i.hasNext() && n > 0; n--) {
            set.remove(i.next());
        }
        return set.toImmutable();
    }

    @Override
    public ChampChampSet<E> tail() {
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
    public ChampChampSet<E> init() {
        // XXX Traversable.init() specifies that we must throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return copyRemoveLast();
    }

    private ChampChampSet<E> copyRemoveLast() {
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
