/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * A {@code Queue} stores elements allowing a first-in-first-out (FIFO) retrieval.
 * <p>
 * Queue API:
 *
 * <ul>
 * <li>{@link #dequeue()}</li>
 * <li>{@link #dequeueOption()}</li>
 * <li>{@link #enqueue(Object)}</li>
 * <li>{@link #enqueue(Object[])}</li>
 * <li>{@link #enqueueAll(Iterable)}</li>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * </ul>
 *
 * A Queue internally consists of a front List containing the front elements of the Queue in the correct order and a
 * rear List containing the rear elements of the Queue in reverse order.
 * <p>
 * When the front list is empty, front and rear are swapped and rear is reversed. This implies the following queue
 * invariant: {@code front.isEmpty() => rear.isEmpty()}.
 * <p>
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 42 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the Queue
 * @since 2.0.0
 */
public class Queue<T> implements Seq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Queue<?> EMPTY = new Queue<>(List.nil(), List.nil());

    private final List<T> front;
    private final List<T> rear;

    /**
     * Creates a Queue consisting of a front List and a rear List.
     * <p>
     * For a {@code Queue(front, rear)} the following invariant holds: {@code Queue is empty <=> front is empty}.
     * In other words: If the Queue is not empty, the front List contains at least one element.
     *
     * @param front A List of front elements, in correct order.
     * @param rear  A List of rear elements, in reverse order.
     */
    private Queue(List<T> front, List<T> rear) {
        final boolean isFrontEmpty = front.isEmpty();
        this.front = isFrontEmpty ? rear.reverse() : front;
        this.rear = isFrontEmpty ? front : rear;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Queue}
     * .
     *
     * @param <T> Component type of the Queue.
     * @return A javaslang.collection.Queue Collector.
     */
    public static <T> Collector<T, ArrayList<T>, Queue<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Queue<T>> finisher = Queue::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the empty Queue.
     *
     * @param <T> Component type
     * @return The empty Queue.
     */
    @SuppressWarnings("unchecked")
    public static <T> Queue<T> empty() {
        return (Queue<T>) EMPTY;
    }

    /**
     * Returns a singleton {@code Queue}, i.e. a {@code Queue} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Queue instance containing the given element
     */
    public static <T> Queue<T> of(T element) {
        return new Queue<>(List.of(element), List.nil());
    }

    /**
     * Creates a Queue of the given elements.
     *
     * @param <T>      Component type of the Queue.
     * @param elements Zero or more elements.
     * @return A queue containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
    public static <T> Queue<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Queue<>(List.of(elements), List.nil());
    }

    /**
     * Creates a Queue of the given elements.
     *
     * @param <T>      Component type of the Queue.
     * @param elements An Iterable of elements.
     * @return A queue containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Queue<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Queue) {
            return (Queue<T>) elements;
        } else if (elements instanceof List) {
            return new Queue<>((List<T>) elements, List.nil());
        } else {
            return new Queue<>(List.ofAll(elements), List.nil());
        }
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    public static Queue<Integer> range(int from, int toExclusive) {
        return new Queue<>(List.range(from, toExclusive), List.nil());
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    public static Queue<Integer> rangeClosed(int from, int toInclusive) {
        return new Queue<>(List.rangeClosed(from, toInclusive), List.nil());
    }

    /**
     * Removes an element from this Queue.
     *
     * @return a tuple containing the first element and the remaining elements of this Queue
     * @throws java.util.NoSuchElementException if this Queue is empty
     */
    public Tuple2<T, Queue<T>> dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue of empty Queue");
        } else {
            return Tuple.of(head(), tail());
        }
    }

    /**
     * Removes an element from this Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise {@code Some} {@code Tuple} containing the first element and the remaining elements of this Queue
     */
    public Option<Tuple2<T, Queue<T>>> dequeueOption() {
        return isEmpty() ? None.instance() : new Some<>(dequeue());
    }

    /**
     * Enqueues a new element.
     *
     * @param element The new element
     * @return a new {@code Queue} instance, containing the new element
     */
    public Queue<T> enqueue(T element) {
        return new Queue<>(front, rear.prepend(element));
    }

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    @SuppressWarnings("unchecked")
    public Queue<T> enqueue(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> temp = rear;
        for (T element : elements) {
            temp = temp.prepend(element);
        }
        return new Queue<>(front, temp);
    }

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    public Queue<T> enqueueAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> temp = rear;
        for (T element : elements) {
            temp = temp.prepend(element);
        }
        return new Queue<>(front, temp);
    }

    /**
     * Returns the first element without modifying the Queue.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this Queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("peek of empty Queue");
        } else {
            return front.head();
        }
    }

    /**
     * Returns the first element without modifying the Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise a {@code Some} containing the first element
     */
    public Option<T> peekOption() {
        return isEmpty() ? None.instance() : new Some<>(front.head());
    }

    // -- Adjusted return types of Seq methods

    @Override
    public Queue<T> append(T element) {
        return enqueue(element);
    }

    @Override
    public Queue<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return enqueueAll(elements);
    }

    @Override
    public Queue<T> clear() {
        return Queue.empty();
    }

    @Override
    public Queue<Queue<T>> combinations() {
        return toList().combinations().map(Queue::ofAll).toQueue();
    }

    @Override
    public Queue<Queue<T>> combinations(int k) {
        return toList().combinations(k).map(Queue::ofAll).toQueue();
    }

    @Override
    public Queue<T> distinct() {
        return toList().distinct().toQueue();
    }

    @Override
    public <U> Queue<T> distinct(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return toList().distinct(keyExtractor).toQueue();
    }

    @Override
    public Queue<T> drop(int n) {
        return new Queue<>(front.drop(n), rear.dropRight(n - front.length()));
    }

    @Override
    public Queue<T> dropRight(int n) {
        return new Queue<>(front.dropRight(n), rear.drop(n - front.length()));
    }

    @Override
    public Queue<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().dropWhile(predicate).toQueue();
    }

    @Override
    public Queue<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().filter(predicate).toQueue();
    }

    @Override
    public Queue<T> findAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().findAll(predicate).toQueue();
    }

    @Override
    public <U> Queue<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new Queue<>(front.flatMap(mapper), rear.flatMap(mapper));
    }

    @Override
    public <U> Queue<U> flatten(Function<? super T, ? extends Iterable<U>> f) {
        Objects.requireNonNull(f, "f is null");
        return new Queue<>(front.flatten(f), rear.flatten(f));
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on empty Queue");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        final int length = front.length();
        if (index < length) {
            return front.get(index);
        } else {
            final int rearIndex = index - length;
            final int rearLength = rear.length();
            if (rearIndex < rearLength) {
                final int reverseRearIndex = rearLength - rearIndex - 1;
                return rear.get(reverseRearIndex);
            } else {
                throw new IndexOutOfBoundsException(String.format("get(%s) on Queue of length %s", index, length()));
            }
        }
    }

    @Override
    public Queue<Queue<T>> grouped(int size) {
        return toList().grouped(size).map(Queue::ofAll).toQueue();
    }

    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty queue");
        } else {
            return front.head();
        }
    }

    @Override
    public Option<T> headOption() {
        return isEmpty() ? None.instance() : new Some<>(front.head());
    }

    @Override
    public int indexOf(T element) {
        final int frontIndex = front.indexOf(element);
        if (frontIndex != -1) {
            return frontIndex;
        } else {
            // we need to reverse because we search the first occurrence
            final int rearIndex = rear.reverse().indexOf(element);
            return (rearIndex == -1) ? -1 : rearIndex + front.length();
        }
    }

    @Override
    public Queue<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty Queue");
        } else if (rear.isEmpty()) {
            return new Queue<>(front.init(), rear);
        } else {
            return new Queue<>(front, rear.tail());
        }
    }

    @Override
    public Option<Queue<T>> initOption() {
        return isEmpty() ? None.instance() : new Some<>(init());
    }

    @Override
    public Queue<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        final int length = front.length();
        if (index <= length) {
            return new Queue<>(front.insert(index, element), rear);
        } else {
            final int rearIndex = index - length;
            final int rearLength = rear.length();
            if (rearIndex <= rearLength) {
                final int reverseRearIndex = rearLength - rearIndex - 1;
                return new Queue<>(front, rear.insert(reverseRearIndex, element));
            } else {
                throw new IndexOutOfBoundsException(String.format("insert(%s, e) on Queue of length %s", index, length()));
            }
        }
    }

    @Override
    public Queue<T> insertAll(int index, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", e)");
        }
        final int length = front.length();
        if (index <= length) {
            return new Queue<>(front.insertAll(index, elements), rear);
        } else {
            final int rearIndex = index - length;
            final int rearLength = rear.length();
            if (rearIndex <= rearLength) {
                final int reverseRearIndex = rearLength - rearIndex - 1;
                return new Queue<>(front, rear.insertAll(reverseRearIndex, elements));
            } else {
                throw new IndexOutOfBoundsException(String.format("insertAll(%s, e) on Queue of length %s", index, length()));
            }
        }
    }

    @Override
    public Queue<T> intersperse(T element) {
        if (isEmpty()) {
            return this;
        } else if (rear.isEmpty()) {
            return new Queue<>(front.intersperse(element), rear);
        } else {
            return new Queue<>(front.intersperse(element), rear.intersperse(element).append(element));
        }
    }

    @Override
    public boolean isEmpty() {
        return front.isEmpty();
    }

    @Override
    public int lastIndexOf(T element) {
        return toList().lastIndexOf(element);
    }

    @Override
    public <U> Queue<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new Queue<>(front.map(mapper), rear.map(mapper));
    }

    @Override
    public Tuple2<Queue<T>, Queue<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().partition(predicate).map(List::toQueue, List::toQueue);
    }

    @Override
    public Queue<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    public Queue<Queue<T>> permutations() {
        return toList().permutations().map(List::toQueue).toQueue();
    }

    @Override
    public Queue<T> prepend(T element) {
        return new Queue<>(front.prepend(element), rear);
    }

    @Override
    public Queue<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Queue<>(front.prependAll(elements), rear);
    }

    @Override
    public Queue<T> remove(T element) {
        return toList().remove(element).toQueue();
    }

    @Override
    public Queue<T> removeAll(T element) {
        return new Queue<>(front.removeAll(element), rear.removeAll(element));
    }

    @Override
    public Queue<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Queue<>(front.removeAll(elements), rear.removeAll(elements));
    }

    @Override
    public Queue<T> replace(T currentElement, T newElement) {
        return toList().replace(currentElement, newElement).toQueue();
    }

    @Override
    public Queue<T> replaceAll(T currentElement, T newElement) {
        return new Queue<>(front.replaceAll(currentElement, newElement), rear.replaceAll(currentElement, newElement));
    }

    @Override
    public Queue<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        return new Queue<>(front.replaceAll(operator), rear.replaceAll(operator));
    }

    @Override
    public Queue<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Queue<>(front.retainAll(elements), rear.retainAll(elements));
    }

    @Override
    public Queue<T> reverse() {
        return isEmpty() ? this : toList().reverse().toQueue();
    }

    @Override
    public Queue<T> set(int index, T element) {
        return toList().set(index, element).toQueue();
    }

    @Override
    public Queue<Queue<T>> sliding(int size) {
        return toList().sliding(size).map(List::toQueue).toQueue();
    }

    @Override
    public Queue<Queue<T>> sliding(int size, int step) {
        return toList().sliding(size, step).map(List::toQueue).toQueue();
    }

    @Override
    public Queue<T> sort() {
        return toList().sort().toQueue();
    }

    @Override
    public Queue<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return toList().sort(comparator).toQueue();
    }

    @Override
    public Tuple2<Queue<T>, Queue<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().span(predicate).map(List::toQueue, List::toQueue);
    }

    @Override
    public Tuple2<Queue<T>, Queue<T>> splitAt(int n) {
        return toList().splitAt(n).map(List::toQueue, List::toQueue);
    }

    @Override
    public Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public Queue<T> subsequence(int beginIndex) {
        return toList().subsequence(beginIndex).toQueue();
    }

    @Override
    public Queue<T> subsequence(int beginIndex, int endIndex) {
        return toList().subsequence(beginIndex, endIndex).toQueue();
    }

    @Override
    public Queue<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty Queue");
        } else {
            return new Queue<>(front.tail(), rear);
        }
    }

    @Override
    public Option<Queue<T>> tailOption() {
        return isEmpty() ? None.instance() : new Some<>(tail());
    }

    @Override
    public Queue<T> take(int n) {
        final int frontLength = front.length();
        if (n < frontLength) {
            return new Queue<>(front.take(n), List.nil());
        } else if (n == frontLength) {
            return new Queue<>(front, List.nil());
        } else {
            return new Queue<>(front, rear.takeRight(n - frontLength));
        }
    }

    @Override
    public Queue<T> takeRight(int n) {
        final int rearLength = rear.length();
        if (n < rearLength) {
            return new Queue<>(rear.take(n).reverse(), List.nil());
        } else if (n == rearLength) {
            return new Queue<>(rear.reverse(), List.nil());
        } else {
            return new Queue<>(front.takeRight(n - rearLength), rear);
        }
    }

    @Override
    public Queue<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return toList().takeWhile(predicate).toQueue();
    }

    @Override
    public <T1, T2> Tuple2<Queue<T1>, Queue<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return toList().unzip(unzipper).map(List::toQueue, List::toQueue);
    }

    @Override
    public <U> Queue<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return toList().zip(that).toQueue();
    }

    @Override
    public <U> Queue<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return toList().zipAll(that, thisElem, thatElem).toQueue();
    }

    @Override
    public Queue<Tuple2<T, Integer>> zipWithIndex() {
        return toList().zipWithIndex().toQueue();
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Queue) {
            List<?> list1 = this.toList();
            List<?> list2 = ((Queue<?>) o).toList();
            while (!list1.isEmpty() && !list2.isEmpty()) {
                final boolean isEqual = Objects.equals(list1.head(), list2.head());
                if (!isEqual) {
                    return false;
                }
                list1 = list1.tail();
                list2 = list2.tail();
            }
            return list1.isEmpty() && list2.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (T element : this) {
            hashCode = 31 * hashCode + Objects.hashCode(element);
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return map(String::valueOf).join(", ", "Queue(", ")");
    }
}
