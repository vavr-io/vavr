/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.collection.Comparators.SerializableComparator;
import javaslang.collection.Tree.Node;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;
import static javaslang.collection.PriorityQueue.PriorityQueueHelper.*;

/**
 * @author Pap Lőrinc
 * @since 2.1.0
 */
public final class PriorityQueue<T> extends AbstractsQueue<T, PriorityQueue<T>> implements Serializable, Kind1<PriorityQueue<T>, T> {
    private static final long serialVersionUID = 1L;

    private final SerializableComparator<? super T> comparator;
    private final List<Node<Ranked<T>>> forest;
    private final int size;

    private PriorityQueue(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest, int size) {
        this.comparator = comparator;
        this.forest = forest;
        this.size = size;
    }

    private PriorityQueue<T> with(List<Node<Ranked<T>>> forest, int size) {
        return new PriorityQueue<>(this.comparator, forest, size);
    }

    private PriorityQueue<T> with(SerializableComparator<? super T> comparator) {
        return new PriorityQueue<>(comparator, this.forest, this.size);
    }

    /**
     * Enqueues a new element.
     *
     * @param element The new element
     * @return a new {@code PriorityQueue} instance, containing the new element
     */
    @Override
    public PriorityQueue<T> enqueue(T element) {
        final List<Node<Ranked<T>>> insert = insert(comparator, forest, element);
        return with(insert, size + 1);
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
            final T currentMin = root(forest.head());
            return findMin(comparator, forest, currentMin);
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
            final List<Node<Ranked<T>>> forest = deleteMin(comparator, this.forest);
            return with(forest, this.size - 1);
        }
    }

    public PriorityQueue<T> merge(PriorityQueue<T> target) {
        final List<Node<Ranked<T>>> meld = meld(comparator, this.forest, target.forest);
        return with(meld, this.size + target.size);
    }

    @Override
    public boolean isEmpty() {
        return forest.isEmpty();
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
    static <T> Collector<T, ArrayList<T>, PriorityQueue<T>> collector() {
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
     * by performing a type safe-cast. This is eligible because immutable/read-only
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

        final List<T> values = List.ofAll(elements);
        final SerializableComparator<? super T> serializableComparator = SerializableComparator.of(comparator);

        return new PriorityQueue<>(serializableComparator, asForest(serializableComparator, values), values.size());
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
    static <T> PriorityQueue<T> tabulate(int size, Function<? super Integer, ? extends T> function) {
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
    static <T> PriorityQueue<T> fill(int size, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        final Comparator<? super T> comparator = naturalComparator();
        return Collections.fill(size, supplier, empty(comparator), values -> ofAll(comparator, List.of(values)));
    }

    @Override
    public List<T> toList() {
        List<T> results = List.empty();
        for (PriorityQueue<T> queue = this; !queue.isEmpty(); queue = queue.tail()) {
            results = results.prepend(queue.head());
        }
        return results.reverse();
    }

    @Override
    public PriorityQueue<T> distinct() {
        return ofAll(comparator, iterator().distinctBy(comparator));
    }

    @Override
    public <U> PriorityQueue<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final Comparator<? super T> comparator = (o1, o2) -> (keyExtractor.apply(o1) == keyExtractor.apply(o2)) ? 0 : -1;
        return distinctBy(comparator);
    }

    @Override
    @SuppressWarnings("TrivialMethodReference")
    public PriorityQueue<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return with(comparator::compare).distinct();
    }

    @Override
    public PriorityQueue<T> drop(long n) {
        PriorityQueue<T> result = this;
        for (long i = n; i > 0 && !result.isEmpty(); i--) {
            result = result.tail();
        }
        return result;
    }

    @Override
    public PriorityQueue<T> dropRight(long n) {
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
        Objects.requireNonNull(classifier, "classifier is null");
        return iterator().groupBy(classifier).map((c, q) -> Tuple.of(c, ofAll(comparator, q)));
    }

    @Override
    public Iterator<? extends PriorityQueue<T>> grouped(long size) {
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
    public Iterator<? extends PriorityQueue<T>> sliding(long size) {
        return iterator().sliding(size).map(v -> ofAll(comparator, v));
    }

    @Override
    public Iterator<? extends PriorityQueue<T>> sliding(long size, long step) {
        return iterator().sliding(size, step).map(v -> ofAll(comparator, v));
    }

    @Override
    public Tuple2<? extends PriorityQueue<T>, ? extends PriorityQueue<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    public PriorityQueue<T> take(long n) {
        return ofAll(comparator, iterator().take(n));
    }

    @Override
    public PriorityQueue<T> takeRight(long n) {
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
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zip(that));
    }

    @Override
    public <U> PriorityQueue<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public PriorityQueue<Tuple2<T, Long>> zipWithIndex() {
        return ofAll(iterator().zipWithIndex());
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
        return o == this || o instanceof PriorityQueue && Collections.equals(this, (Iterable) o);
    }

    protected static class PriorityQueueHelper {
        /* Based on http://www.brics.dk/RS/96/37/BRICS-RS-96-37.pdf */

        /*
       * fun deleteMin [] = raise EMPTY
       * * | deleteMin ts =
       * *     let fun getMin [t]=(t, [])
       * *           | getMin (t :: ts) =
       * *               let val (t', ts') = getMin ts
       * *               in if Elem.leq(root t, root t') then (t, ts)
       * *                  else                              (t', t :: ts') end
       * *         fun split (ts,xs,[]) = (ts, xs)
       * *           | split (ts,xs,t :: c) =
       * *               if rank t = 0 then split (ts,root t :: xs,c)
       * *               else               split (t :: ts,xs,c)
       * *         val (Node (x,r,c), ts) = getMin ts
       * *         val (ts',xs') = split ([],[],c)
       * * in fold insert xs' (meld (ts, ts')) end
       * */
        protected static <T> List<Node<Ranked<T>>> deleteMin(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest) {
            if (forest.isEmpty()) {
                throw new NoSuchElementException();
            } else {
                /* get the minimum tree and the rest of the forest */
                final Node<Ranked<T>> minTree = forest.reduce((t1, t2) -> comparator.isLessOrEqual(root(t1), root(t2)) ? t1 : t2); // TODO could we use PriorityQueue here instead ... no, really
                final List<Node<Ranked<T>>> forestTail = (minTree == forest.head()) ? forest.tail() : forest.remove(minTree);

                final List<Node<Ranked<T>>> newForest = rebuild(comparator, minTree.getChildren());
                return meld(comparator, newForest, forestTail);
            }
        }

        /* Separate the rank 0 trees from the rest, rebuild the 0 rank ones and merge them back */
        private static <T> List<Node<Ranked<T>>> rebuild(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest) {
            List<Node<Ranked<T>>> nonZeroRank = List.empty(), zeroRank = List.empty();
            for (; !forest.isEmpty(); forest = forest.tail()) {
                final Node<Ranked<T>> initialForestHead = forest.head();
                if (rank(initialForestHead) == 0) {
                    zeroRank = insert(comparator, zeroRank, root(initialForestHead));
                } else {
                    nonZeroRank = concat(initialForestHead, nonZeroRank);
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
        protected static <T> List<Node<Ranked<T>>> insert(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest, T element) {
            Node<Ranked<T>> tree = Tree.of(Ranked.of(element, 0));
            if (forest.size() >= 3) {
                final Node<Ranked<T>> t1 = forest.head(), t2 = forest.get(1);
                if (rank(t1) == rank(t2)) {
                    forest = forest.drop(2);
                    tree = skewLink(comparator, tree, t1, t2);
                }
            }
            return concat(tree, forest);
        }

        protected static <T> List<Node<Ranked<T>>> asForest(SerializableComparator<? super T> comparator, Traversable<? extends T> targetValues) {
            return targetValues.foldLeft(List.empty(), (f, e) -> insert(comparator, f, e));
        }

        /**
         * fun meld (ts, ts') = meldUniq (uniqify ts, uniqify ts')
         **/
        protected static <T> List<Node<Ranked<T>>> meld(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> source, List<Node<Ranked<T>>> target) {
            return meldUnique(comparator, uniqify(comparator, source), uniqify(comparator, target));
        }

        /**
         * fun uniqify [] = []
         * *  | uniqify (t :: ts) = ins (t, ts) (∗ eliminate initial duplicate ∗)
         **/
        private static <T> List<Node<Ranked<T>>> uniqify(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest) {
            return forest.isEmpty()
                    ? List.empty()
                    : ins(comparator, forest.head(), forest.tail());
        }

        /**
         * fun ins (t, []) = [t]
         * * | ins (t, t' :: ts) = (∗ rank t ≤ rank t' ∗)
         * *     if rank t < rank t' then t :: t' :: ts
         * *     else                     ins (link (t, t'), ts)
         */
        private static <T> List<Node<Ranked<T>>> ins(SerializableComparator<? super T> comparator, Node<Ranked<T>> tree, List<Node<Ranked<T>>> forest) {
            while (!forest.isEmpty() && rank(tree) >= rank(forest.head())) {
                tree = link(comparator, tree, forest.head());
                forest = forest.tail();
            }
            return concat(tree, forest);
        }

        /**
         * fun meldUniq ([], ts) = ts
         * *  | meldUniq (ts, []) = ts
         * *  | meldUniq (t1 :: ts1, t2 :: ts2) =
         * *      if rank t1 < rank t2 then      t1 :: meldUniq (ts1, t2 :: ts2)
         * *      else if rank t2 < rank t1 then t2 :: meldUniq (t1 :: ts1, ts2)
         * *      else                           ins (link (t1, t2), meldUniq (ts1, ts2))
         **/
        private static <T> List<Node<Ranked<T>>> meldUnique(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest1, List<Node<Ranked<T>>> forest2) {
            if (forest1.isEmpty()) {
                return forest2;
            } else if (forest2.isEmpty()) {
                return forest1;
            } else {
                final Node<Ranked<T>> tree1 = forest1.head(), tree2 = forest2.head();

                if (rank(tree1) < rank(tree2)) {
                    final List<Node<Ranked<T>>> tail = meldUnique(comparator, forest1.tail(), forest2);
                    return concat(tree1, tail);
                } else if (rank(tree1) > rank(tree2)) {
                    final List<Node<Ranked<T>>> tail = meldUnique(comparator, forest1, forest2.tail());
                    return concat(tree2, tail);
                } else {
                    final Node<Ranked<T>> link = link(comparator, tree1, tree2);
                    final List<Node<Ranked<T>>> tail = meldUnique(comparator, forest1.tail(), forest2.tail());
                    return ins(comparator, link, tail);
                }
            }
        }

        /**
         * fun link (t1 as Node (x1,r1,c1), t2 as Node (x2,r2,c2)) = (∗ r1 = r2 ∗)
         * *  if Elem.leq (x1,x2) then Node (x1,r1+1,t2 :: c1)
         * *  else                     Node (x2,r2+1,t1 :: c2
         */
        private static <T> Node<Ranked<T>> link(SerializableComparator<? super T> comparator, Node<Ranked<T>> tree1, Node<Ranked<T>> tree2) {
            final T root1 = root(tree1), root2 = root(tree2);

            final Ranked<T> ranked;
            final List<Node<Ranked<T>>> children;
            if (comparator.isLessOrEqual(root1, root2)) {
                ranked = Ranked.of(root1, rank(tree1) + 1);
                children = concat(tree2, tree1.getChildren());
            } else {
                ranked = Ranked.of(root2, rank(tree2) + 1);
                children = concat(tree1, tree2.getChildren());
            }
            return Tree.of(ranked, children);
        }

        /**
         * fun skewLink (t0 as Node (x0,r0, _), t1 as Node (x1,r1,c1), t2 as Node (x2,r2,c2)) =
         * *  if Elem.leq (x1,x0) andalso Elem.leq (x1,x2) then      Node (x1,r1+1,t0 :: t2 :: c1)
         * *  else if Elem.leq (x2,x0) andalso Elem.leq (x2,x1) then Node (x2,r2+1,t0 :: t1 :: c2)
         * *  else                                                   Node (x0,r1+1,[t1, t2])
         **/
        private static <T> Node<Ranked<T>> skewLink(SerializableComparator<? super T> comparator, Node<Ranked<T>> tree0, Node<Ranked<T>> tree1, Node<Ranked<T>> tree2) {
            final T root0 = root(tree0), root1 = root(tree1), root2 = root(tree2);

            final Ranked<T> ranked;
            final List<Node<Ranked<T>>> children;
            if (comparator.isLessOrEqual(root1, root0) && comparator.isLessOrEqual(root1, root2)) {
                ranked = Ranked.of(root1, rank(tree1) + 1);
                children = concat(tree0, concat(tree2, tree1.getChildren()));
            } else if (comparator.isLessOrEqual(root2, root0) && comparator.isLessOrEqual(root2, root1)) {
                ranked = Ranked.of(root2, rank(tree2) + 1);
                children = concat(tree0, concat(tree1, tree2.getChildren()));
            } else {
                ranked = Ranked.of(root0, rank(tree1) + 1);
                children = List.of(tree1, tree2);
            }
            return Tree.of(ranked, children);
        }

        /**
         * fun findMin [] = raise EMPTY
         * * | findMin [t] = root t
         * * | findMin (t :: ts) =
         * *     let val x = findMin ts
         * *     in if Elem.leq (root t, x) then root t else x end
         */
        protected static <T> T findMin(SerializableComparator<? super T> comparator, List<Node<Ranked<T>>> forest, T currentMin) {
            while (forest.size() > 1) {
                forest = forest.tail();
                currentMin = min(comparator, currentMin, root(forest.head()));
            }
            return currentMin;
        }

        private static <T> T min(SerializableComparator<? super T> comparator, T value1, T value2) {
            return comparator.isLessOrEqual(value1, value2) ? value1 : value2;
        }

        protected static <T> T root(Node<Ranked<T>> tree) {
            return tree.getValue().value;
        }

        private static <T> int rank(Node<Ranked<T>> tree) {
            return tree.getValue().rank;
        }

        private static <T> List<T> concat(T head, List<T> rest) {
            return rest.prepend(head);
        }

        protected static class Ranked<T> implements Serializable {
            private static final long serialVersionUID = 1L;

            final T value;
            final int rank;

            Ranked(T value, int rank) {
                this.value = value;
                this.rank = rank;
            }

            static <T> Ranked<T> of(T value, int rank) {
                return new Ranked<>(value, rank);
            }

            @Override
            public String toString() {
                return "Ranked(" + value + ", " + rank + ')';
            }
        }
    }
}