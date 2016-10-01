/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Comparators.SerializableComparator;
import javaslang.collection.PriorityQueue.PriorityQueueBase.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;
import static javaslang.collection.PriorityQueue.PriorityQueueBase.*;

/**
 * @author Pap Lőrinc
 * @since 2.1.0
 */
public final class PriorityQueue<T> extends AbstractsQueue<T, PriorityQueue<T>> implements Serializable, Kind1<PriorityQueue<T>, T> {
    private static final long serialVersionUID = 1L;

    private final SerializableComparator<? super T> comparator;
    private final Seq<Node<T>> forest;
    private final int size;

    private PriorityQueue(SerializableComparator<? super T> comparator, Seq<Node<T>> forest, int size) {
        this.comparator = comparator;
        this.forest = forest;
        this.size = size;
    }

    private PriorityQueue<T> with(Seq<Node<T>> forest, int size) {
        return new PriorityQueue<>(this.comparator, forest, size);
    }

    /**
     * Enqueues a new element.
     *
     * @param element The new element
     * @return a new {@code PriorityQueue} instance, containing the new element
     */
    @Override
    public PriorityQueue<T> enqueue(T element) {
        final Seq<Node<T>> result = insert(comparator, element, forest);
        return with(result, size + 1);
    }

    /**
     * Enqueues the given elements.
     *
     * @param elements An {@link PriorityQueue} of elements, may be empty
     * @return a new {@link PriorityQueue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    @Override
    public PriorityQueue<T> enqueueAll(Iterable<? extends T> elements) {
        return merge(ofAll(comparator, elements));
    }

    /**
     * Returns the first element of a non-empty {@link PriorityQueue}.
     *
     * @return The first element of this {@link PriorityQueue}.
     * @throws NoSuchElementException if this is empty
     */
    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty " + stringPrefix());
        } else {
            return findMin(comparator, forest).root;
        }
    }

    /**
     * Drops the first element of a non-empty {@link PriorityQueue}.
     *
     * @return A new instance of PriorityQueue containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    @Override
    public PriorityQueue<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty " + stringPrefix());
        } else {
            return dequeue()._2;
        }
    }

    @Override
    public Tuple2<T, PriorityQueue<T>> dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue of empty " + stringPrefix());
        } else {
            final Tuple2<T, Seq<Node<T>>> dequeue = deleteMin(comparator, this.forest);
            return Tuple.of(dequeue._1, with(dequeue._2, this.size - 1));
        }
    }

    public PriorityQueue<T> merge(PriorityQueue<T> target) {
        final Seq<Node<T>> meld = meld(comparator, this.forest, target.forest);
        return with(meld, this.size + target.size);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the empty PriorityQueue.
     *
     * @param <T> Component type
     * @return The empty PriorityQueue.
     */
    public static <T extends Comparable<T>> PriorityQueue<T> empty() {
        return empty(naturalComparator());
    }

    public static <T> PriorityQueue<T> empty(Comparator<? super T> comparator) {
        return new PriorityQueue<>(SerializableComparator.of(comparator), List.empty(), 0);
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@code PriorityQueue<T>}.
     *
     * @param <T> Component type of the {@code PriorityQueue}.
     * @return A {@code PriorityQueue<T>} Collector.
     */
    public static <T> Collector<T, ArrayList<T>, PriorityQueue<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, PriorityQueue<T>> finisher = values -> ofAll(naturalComparator(), values);
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Narrows a widened {@code PriorityQueue<? extends T>} to {@code PriorityQueue<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param queue An {@code PriorityQueue}.
     * @param <T>   Component type of the {@code PriorityQueue}.
     * @return the given {@code PriorityQueue} instance as narrowed type {@code PriorityQueue<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> PriorityQueue<T> narrow(PriorityQueue<? extends T> queue) {
        return (PriorityQueue<T>) queue;
    }

    public static <T extends Comparable<T>> PriorityQueue<T> of(T element) {
        return of(naturalComparator(), element);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> PriorityQueue<T> of(T... elements) {
        return ofAll(naturalComparator(), List.of(elements));
    }

    public static <T> PriorityQueue<T> of(Comparator<? super T> comparator, T element) {
        return ofAll(comparator, List.of(element));
    }

    @SuppressWarnings("unchecked")
    public static <T> PriorityQueue<T> of(Comparator<? super T> comparator, T... elements) {
        return ofAll(comparator, List.of(elements));
    }

    public static <T extends Comparable<T>> PriorityQueue<T> ofAll(Iterable<? extends T> elements) {
        return ofAll(naturalComparator(), elements);
    }

    public static <T> PriorityQueue<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");

        final SerializableComparator<? super T> serializableComparator = SerializableComparator.of(comparator);

        int size = 0;
        Seq<Node<T>> forest = List.empty();
        for (T value : elements) {
            forest = insert(serializableComparator, value, forest);
            size++;
        }
        return new PriorityQueue<>(serializableComparator, forest, size);
    }

    public static <T extends Comparable<T>> PriorityQueue<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        return ofAll(naturalComparator(), Iterator.ofAll(javaStream.iterator()));
    }

    public static <T> PriorityQueue<T> ofAll(Comparator<? super T> comparator, java.util.stream.Stream<? extends T> javaStream) {
        return ofAll(comparator, Iterator.ofAll(javaStream.iterator()));
    }

    /**
     * Returns a {@link PriorityQueue} containing {@code size} values of a given Function {@code function}
     * over a range of integer values from {@code 0} to {@code size - 1}.
     *
     * @param <T>      Component type of the {@link PriorityQueue}
     * @param size     The number of elements in the {@link PriorityQueue}
     * @param function The Function computing element values
     * @return A {@link PriorityQueue} consisting of elements {@code function(0),function(1), ..., function(size - 1)}
     * @throws NullPointerException if {@code function} is null
     */
    @GwtIncompatible
    public static <T> PriorityQueue<T> tabulate(int size, Function<? super Integer, ? extends T> function) {
        Objects.requireNonNull(function, "function is null");
        final Comparator<? super T> comparator = naturalComparator();
        return Collections.tabulate(size, function, empty(comparator), values -> ofAll(comparator, List.of(values)));
    }

    /**
     * Returns a {@link PriorityQueue} containing {@code size} values supplied by a given Supplier {@code supplier}.
     *
     * @param <T>      Component type of the {@link PriorityQueue}
     * @param size     The number of elements in the {@link PriorityQueue}
     * @param supplier The Supplier computing element values
     * @return A {@link PriorityQueue} of size {@code size}, where each element contains the result supplied by {@code supplier}.
     * @throws NullPointerException if {@code supplier} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    public static <T> PriorityQueue<T> fill(int size, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        final Comparator<? super T> comparator = naturalComparator();
        return Collections.fill(size, supplier, empty(comparator), values -> ofAll(comparator, List.of(values)));
    }

    @Override
    public List<T> toList() {
        List<T> results = List.empty();
        for (PriorityQueue<T> queue = this; !queue.isEmpty(); ) {
            final Tuple2<T, PriorityQueue<T>> dequeue = queue.dequeue();
            results = results.prepend(dequeue._1);
            queue = dequeue._2;
        }
        return results.reverse();
    }

    @Override
    public PriorityQueue<T> distinct() {
        return distinctBy(comparator);
    }

    @Override
    public PriorityQueue<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return ofAll(comparator, iterator().distinctBy(comparator));
    }

    @Override
    public <U> PriorityQueue<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return ofAll(comparator, iterator().distinctBy(keyExtractor));
    }

    @Override
    public PriorityQueue<T> drop(int n) {
        PriorityQueue<T> result = this;
        for (long i = n; i > 0 && !result.isEmpty(); i--) {
            result = result.tail();
        }
        return result;
    }

    @Override
    public PriorityQueue<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty(comparator);
        } else {
            return ofAll(comparator, iterator().dropRight(n));
        }
    }

    @Override
    public PriorityQueue<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public PriorityQueue<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        PriorityQueue<T> result = this;
        while (!result.isEmpty() && predicate.test(result.head())) {
            result = result.tail();
        }
        return result;
    }

    @Override
    public PriorityQueue<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return ofAll(comparator, iterator().filter(predicate));
    }

    @Override
    public <U> PriorityQueue<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMap(naturalComparator(), mapper);
    }

    public <U> PriorityQueue<U> flatMap(Comparator<U> comparator, Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(comparator, iterator().flatMap(mapper));
    }

    /**
     * Accumulates the elements of this {@link PriorityQueue} by successively calling the given function {@code f} from the right,
     * starting with a value {@code zero} of type B.
     * <p>
     * Example: {@code PriorityQueue.of("a", "b", "c").foldRight("", (x, xs) -> x + xs) = "abc"}
     *
     * @param zero        Value to start the accumulation with.
     * @param accumulator The accumulator function.
     * @return an accumulated version of this.
     * @throws NullPointerException if {@code f} is null
     */
    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> accumulator) {
        Objects.requireNonNull(zero, "zero is null");
        Objects.requireNonNull(accumulator, "accumulator is null");
        return toList().foldRight(zero, accumulator);
    }

    @Override
    public <C> Map<C, ? extends PriorityQueue<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, (elements) -> ofAll(comparator, elements));
    }

    @Override
    public Iterator<? extends PriorityQueue<T>> grouped(int size) {
        return sliding(size, size);
    }

    /**
     * Checks if this {@link PriorityQueue} is known to have a finite size.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this {@link PriorityQueue} is known to have a finite size, false otherwise.
     */
    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    /**
     * Dual of {@linkplain #tail()}, returning all elements except the last.
     *
     * @return a new instance containing all elements except the last.
     * @throws UnsupportedOperationException if this is empty
     */
    @Override
    public PriorityQueue<T> init() {
        return ofAll(comparator, iterator().init());
    }

    /**
     * Checks if this {@link PriorityQueue} can be repeatedly traversed.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this {@link PriorityQueue} is known to be traversable repeatedly, false otherwise.
     */
    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    /**
     * Computes the number of elements of this {@link PriorityQueue}.
     * <p>
     * Same as {@link #size()}.
     *
     * @return the number of elements
     */
    @Override
    public int length() {
        return size;
    }

    @Override
    public <U> PriorityQueue<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return map(naturalComparator(), mapper);
    }

    public <U> PriorityQueue<U> map(Comparator<U> comparator, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(comparator, iterator().map(mapper));
    }

    @Override
    public Tuple2<? extends PriorityQueue<T>, ? extends PriorityQueue<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        PriorityQueue<T> left = empty(comparator), right = left;
        for (T t : this) {
            if (predicate.test(t)) {
                left = left.enqueue(t);
            } else {
                right = right.enqueue(t);
            }
        }
        return Tuple.of(left, right);

    }

    @Override
    public PriorityQueue<T> replace(T currentElement, T newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return ofAll(comparator, iterator().replace(currentElement, newElement));
    }

    @Override
    public PriorityQueue<T> replaceAll(T currentElement, T newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return ofAll(comparator, iterator().replaceAll(currentElement, newElement));
    }

    @Override
    public PriorityQueue<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return Collections.scanLeft(this, zero, operation, empty(comparator), PriorityQueue::enqueue, Function.identity());
    }

    @Override
    public <U> PriorityQueue<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, empty(naturalComparator()), PriorityQueue::enqueue, Function.identity());
    }

    @Override
    public <U> PriorityQueue<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, empty(naturalComparator()), PriorityQueue::enqueue, Function.identity());
    }

    @Override
    public Iterator<? extends PriorityQueue<T>> sliding(int size) {
        return iterator().sliding(size).map(v -> ofAll(comparator, v));
    }

    @Override
    public Iterator<? extends PriorityQueue<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(v -> ofAll(comparator, v));
    }

    @Override
    public Tuple2<? extends PriorityQueue<T>, ? extends PriorityQueue<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    public PriorityQueue<T> take(int n) {
        return ofAll(comparator, iterator().take(n));
    }

    @Override
    public PriorityQueue<T> takeRight(int n) {
        return ofAll(comparator, toList().takeRight(n));
    }

    @Override
    public PriorityQueue<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return ofAll(comparator, iterator().takeUntil(predicate));
    }

    @Override
    public <T1, T2> Tuple2<? extends PriorityQueue<T1>, ? extends PriorityQueue<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Tuple2<Iterator<T1>, Iterator<T2>> unzip = iterator().unzip(unzipper);
        return Tuple.of(ofAll(naturalComparator(), unzip._1), ofAll(naturalComparator(), unzip._2));
    }

    @Override
    public <T1, T2, T3> Tuple3<? extends PriorityQueue<T1>, ? extends PriorityQueue<T2>, ? extends PriorityQueue<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Tuple3<Iterator<T1>, Iterator<T2>, Iterator<T3>> unzip3 = iterator().unzip3(unzipper);
        return Tuple.of(ofAll(naturalComparator(), unzip3._1), ofAll(naturalComparator(), unzip3._2), ofAll(naturalComparator(), unzip3._3));
    }

    @Override
    public <U> PriorityQueue<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }


    @Override
    public <U, R> PriorityQueue<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(naturalComparator(), iterator().zipWith(that, mapper));
    }

    @Override
    public <U> PriorityQueue<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public PriorityQueue<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    public <U> PriorityQueue<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(naturalComparator(), iterator().zipWithIndex(mapper));
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public String stringPrefix() {
        return "PriorityQueue";
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof PriorityQueue && Collections.areEqual(this, (Iterable) o);
    }

    protected static class PriorityQueueBase {
        /* Based on http://www.brics.dk/RS/96/37/BRICS-RS-96-37.pdf */
        protected static class Node<T> implements Serializable {
            private static final long serialVersionUID = 1L;

            protected final T root;
            protected final int rank;
            protected final Seq<Node<T>> children;

            private Node(T root, int rank, Seq<Node<T>> children) {
                this.root = root;
                this.rank = rank;
                this.children = children;

                assert children.forAll(c -> c.rank < this.rank);
            }

            protected static <T> Node<T> of(T value, int rank, Seq<Node<T>> children) {
                return new Node<>(value, rank, children);
            }

            /*
             * fun link (t1 as Node (x1,r1,c1), t2 as Node (x2,r2,c2)) = (∗ r1 = r2 ∗)
             * *  if Elem.leq (x1,x2) then Node (x1,r1+1,t2 :: c1)
             * *  else                     Node (x2,r2+1,t1 :: c2
             */
            protected Node<T> link(SerializableComparator<? super T> comparator, Node<T> tree) {
                assert rank == tree.rank;

                return comparator.isLessOrEqual(this.root, tree.root)
                        ? of(this.root, this.rank + 1, tree.appendTo(this.children))
                        : of(tree.root, tree.rank + 1, this.appendTo(tree.children));
            }

            /*
             * fun skewLink (t0 as Node (x0,r0, _), t1 as Node (x1,r1,c1), t2 as Node (x2,r2,c2)) =
             * *  if Elem.leq (x1,x0) andalso Elem.leq (x1,x2) then      Node (x1,r1+1,t0 :: t2 :: c1)
             * *  else if Elem.leq (x2,x0) andalso Elem.leq (x2,x1) then Node (x2,r2+1,t0 :: t1 :: c2)
             * *  else                                                   Node (x0,r1+1,[t1, t2])
             */
            protected Node<T> skewLink(SerializableComparator<? super T> comparator, Node<T> left, Node<T> right) {
                assert rank == 0 && left.rank == right.rank;

                if (comparator.isLessOrEqual(left.root, root) && comparator.isLessOrEqual(left.root, right.root)) {
                    return of(left.root, left.rank + 1, appendTo(right.appendTo(left.children)));
                } else if (comparator.isLessOrEqual(right.root, root)) {
                    assert comparator.isLessOrEqual(right.root, left.root);
                    return of(right.root, right.rank + 1, appendTo(left.appendTo(right.children)));
                } else {
                    assert children.isEmpty();
                    return of(root, left.rank + 1, List.of(left, right));
                }
            }

            protected Seq<Node<T>> appendTo(Seq<Node<T>> forest) {
                return forest.prepend(this);
            }

            @Override
            public String toString() {
                return "Node(" + root + ", " + rank + ", " + children + ')';
            }
        }

        /**
         * fun deleteMin [] = raise EMPTY
         * * | deleteMin ts =
         * *     val (Node (x,r,c), ts) = getMin ts
         * *     val (ts',xs') = split ([],[],c)
         * *     in fold insert xs' (meld (ts, ts')) end
         **/
        static <T> Tuple2<T, Seq<Node<T>>> deleteMin(SerializableComparator<? super T> comparator, Seq<Node<T>> forest) {
            if (forest.isEmpty()) {
                throw new NoSuchElementException();
            } else {
                /* get the minimum tree and the rest of the forest */
                final Node<T> minTree = findMin(comparator, forest);
                final Seq<Node<T>> forestTail = (minTree == forest.head()) ? forest.tail() : forest.remove(minTree);

                final Seq<Node<T>> newForest = rebuild(comparator, minTree.children);
                return Tuple.of(minTree.root, meld(comparator, newForest, forestTail));
            }
        }

        /**
         * Separate the rank 0 trees from the rest, rebuild the 0 rank ones and merge them back
         * <p>
         * fun split (ts,xs,[]) = (ts, xs)
         * * | split (ts,xs,t :: c) =
         * *     if rank t = 0 then split (ts,root t :: xs,c)
         * *     else               split (t :: ts,xs,c)
         */
        static <T> Seq<Node<T>> rebuild(SerializableComparator<? super T> comparator, Seq<Node<T>> forest) {
            Seq<Node<T>> nonZeroRank = List.empty(), zeroRank = List.empty();
            for (; !forest.isEmpty(); forest = forest.tail()) {
                final Node<T> initialForestHead = forest.head();
                if (initialForestHead.rank == 0) {
                    zeroRank = insert(comparator, initialForestHead.root, zeroRank);
                } else {
                    nonZeroRank = initialForestHead.appendTo(nonZeroRank);
                }
            }
            return meld(comparator, nonZeroRank, zeroRank);
        }

        /**
         * fun insert (x, ts as t1 :: t2 :: rest) =
         * *     if rank t1 = rank t2 then skewLink(Node(x,0,[]),t1,t2) :: rest
         * *     else                      Node (x,0,[]) :: ts
         * * | insert (x, ts) =            Node (x,0,[]) :: ts
         **/
        static <T> Seq<Node<T>> insert(SerializableComparator<? super T> comparator, T element, Seq<Node<T>> forest) {
            final Node<T> tree = Node.of(element, 0, List.empty());
            if (forest.size() >= 2) {
                final Seq<Node<T>> tail = forest.tail();
                final Node<T> t1 = forest.head(), t2 = tail.head();
                if (t1.rank == t2.rank) {
                    return tree.skewLink(comparator, t1, t2).appendTo(tail.tail());
                }
            }
            return tree.appendTo(forest);
        }

        /** fun meld (ts, ts') = meldUniq (uniqify ts, uniqify ts') */
        static <T> Seq<Node<T>> meld(SerializableComparator<? super T> comparator, Seq<Node<T>> source, Seq<Node<T>> target) {
            return meldUnique(comparator, uniqify(comparator, source), uniqify(comparator, target));
        }

        /**
         * fun uniqify [] = []
         * *  | uniqify (t :: ts) = ins (t, ts) (∗ eliminate initial duplicate ∗)
         **/
        static <T> Seq<Node<T>> uniqify(SerializableComparator<? super T> comparator, Seq<Node<T>> forest) {
            return forest.isEmpty()
                   ? forest
                   : ins(comparator, forest.head(), forest.tail());
        }

        /**
         * fun ins (t, []) = [t]
         * * | ins (t, t' :: ts) = (∗ rank t ≤ rank t' ∗)
         * *     if rank t < rank t' then t :: t' :: ts
         * *     else                     ins (link (t, t'), ts)
         */
        static <T> Seq<Node<T>> ins(SerializableComparator<? super T> comparator, Node<T> tree, Seq<Node<T>> forest) {
            while (!forest.isEmpty() && tree.rank == forest.head().rank) {
                tree = tree.link(comparator, forest.head());
                forest = forest.tail();
            }
            return tree.appendTo(forest);
        }

        /**
         * fun meldUniq ([], ts) = ts
         * *  | meldUniq (ts, []) = ts
         * *  | meldUniq (t1 :: ts1, t2 :: ts2) =
         * *      if rank t1 < rank t2 then      t1 :: meldUniq (ts1, t2 :: ts2)
         * *      else if rank t2 < rank t1 then t2 :: meldUniq (t1 :: ts1, ts2)
         * *      else                           ins (link (t1, t2), meldUniq (ts1, ts2))
         **/
        static <T> Seq<Node<T>> meldUnique(SerializableComparator<? super T> comparator, Seq<Node<T>> forest1, Seq<Node<T>> forest2) {
            if (forest1.isEmpty()) {
                return forest2;
            } else if (forest2.isEmpty()) {
                return forest1;
            } else {
                final Node<T> tree1 = forest1.head(), tree2 = forest2.head();

                if (tree1.rank == tree2.rank) {
                    final Node<T> tree = tree1.link(comparator, tree2);
                    final Seq<Node<T>> forest = meldUnique(comparator, forest1.tail(), forest2.tail());
                    return ins(comparator, tree, forest);
                } else {
                    if (tree1.rank < tree2.rank) {
                        final Seq<Node<T>> forest = meldUnique(comparator, forest1.tail(), forest2);
                        assert forest.isEmpty() || tree1.rank < forest.head().rank;
                        return tree1.appendTo(forest);
                    } else {
                        final Seq<Node<T>> forest = meldUnique(comparator, forest1, forest2.tail());
                        assert forest.isEmpty() || tree2.rank < forest.head().rank;
                        return tree2.appendTo(forest);
                    }
                }
            }
        }

        /**
         * Find the minimum root in the forest
         * <p>
         * fun findMin [] = raise EMPTY
         * * | findMin [t] = root t
         * * | findMin (t :: ts) =
         * *     let val x = findMin ts
         * *     in if Elem.leq (root t, x) then root t else x end
         */
        static <T> Node<T> findMin(SerializableComparator<? super T> comparator, Seq<Node<T>> forest) {
            final Iterator<Node<T>> iterator = forest.iterator();
            Node<T> min = iterator.next();
            for (Node<T> node : iterator) {
                if (comparator.isLess(node.root, min.root)) {
                    min = node;
                }
            }
            return min;
        }
    }
}