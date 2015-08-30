/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.lang.Iterable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code Queue} stores elements allowing a first-in-first-out (FIFO) retrieval.
 * <p>
 * Queue API:
 *
 * <ul>
 * <li>{@link #dequeue()}</li>
 * <li>{@link #dequeueOption()}</li>
 * <li>{@link #enqueue(Object)}</li>
 * <li>{@link #enqueue(Object[])}</li>
 * <li>{@link #enqueueAll(java.lang.Iterable)}</li>
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
public class Queue<T> implements LinearSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Queue<?> EMPTY = new Queue<>(List.empty(), List.empty());

    private final List<T> front;
    private final List<T> rear;

    private final /*TODO(FIXME): transient*/ Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

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
        final boolean frontIsEmpty = front.isEmpty();
        this.front = frontIsEmpty ? rear.reverse() : front;
        this.rear = frontIsEmpty ? front : rear;
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
        return Queue.ofAll(List.of(element));
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
    @SuppressWarnings({ "unchecked", "varargs" })
    public static <T> Queue<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return Queue.ofAll(List.of(elements));
    }

    /**
     * Creates a Queue of the given elements.
     *
     * @param <T>      Component type of the Queue.
     * @param elements An java.lang.Iterable of elements.
     * @return A queue containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Queue<T> ofAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Queue) {
            return (Queue<T>) elements;
        } else if (elements instanceof List) {
            return new Queue<>((List<T>) elements, List.empty());
        } else {
            return new Queue<>(List.ofAll(elements), List.empty());
        }
    }

    /**
     * Creates a Seq based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Seq of Boolean values
     * @throws NullPointerException if array is null
     */
    public static Queue<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Queue of Byte values
     * @throws NullPointerException if array is null
     */
    public static Queue<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Queue of Character values
     * @throws NullPointerException if array is null
     */
    public static Queue<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Queue of Double values
     * @throws NullPointerException if array is null
     */
    public static Queue<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Queue of Float values
     * @throws NullPointerException if array is null
     */
    public static Queue<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Queue of Integer values
     * @throws NullPointerException if array is null
     */
    public static Queue<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Queue of Long values
     * @throws NullPointerException if array is null
     */
    public static Queue<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Queue of Short values
     * @throws NullPointerException if array is null
     */
    public static Queue<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Queue.ofAll(List.ofAll(array));
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.range(0, 0)  // = Queue()
     * Queue.range(2, 0)  // = Queue()
     * Queue.range(-2, 2) // = Queue(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    public static Queue<Integer> range(int from, int toExclusive) {
        return Queue.ofAll(List.range(from, toExclusive));
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeBy(1, 3, 1)  // = Queue(1, 2)
     * Queue.rangeBy(1, 4, 2)  // = Queue(1, 3)
     * Queue.rangeBy(4, 1, -2) // = Queue(4, 2)
     * Queue.rangeBy(4, 1, 2)  // = Queue()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or {@code Nil} if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static Queue<Integer> rangeBy(int from, int toExclusive, int step) {
        return Queue.ofAll(List.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Queue of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.range(0L, 0L)  // = Queue()
     * Queue.range(2L, 0L)  // = Queue()
     * Queue.range(-2L, 2L) // = Queue(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    public static Queue<Long> range(long from, long toExclusive) {
        return Queue.ofAll(List.range(from, toExclusive));
    }

    /**
     * Creates a Queue of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeBy(1L, 3L, 1L)  // = Queue(1L, 2L)
     * Queue.rangeBy(1L, 4L, 2L)  // = Queue(1L, 3L)
     * Queue.rangeBy(4L, 1L, -2L) // = Queue(4L, 2L)
     * Queue.rangeBy(4L, 1L, 2L)  // = Queue()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or {@code Nil} if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static Queue<Long> rangeBy(long from, long toExclusive, long step) {
        return Queue.ofAll(List.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeClosed(0, 0)  // = Queue(0)
     * Queue.rangeClosed(2, 0)  // = Queue()
     * Queue.rangeClosed(-2, 2) // = Queue(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    public static Queue<Integer> rangeClosed(int from, int toInclusive) {
        return Queue.ofAll(List.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeClosedBy(1, 3, 1)  // = Queue(1, 2, 3)
     * Queue.rangeClosedBy(1, 4, 2)  // = Queue(1, 3)
     * Queue.rangeClosedBy(4, 1, -2) // = Queue(4, 2)
     * Queue.rangeClosedBy(4, 1, 2)  // = Queue()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or {@code Nil} if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static Queue<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Queue.ofAll(List.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Queue of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeClosed(0L, 0L)  // = Queue(0L)
     * Queue.rangeClosed(2L, 0L)  // = Queue()
     * Queue.rangeClosed(-2L, 2L) // = Queue(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or {@code Nil} if {@code from > toInclusive}
     */
    public static Queue<Long> rangeClosed(long from, long toInclusive) {
        return Queue.ofAll(List.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Queue of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Queue.rangeClosedBy(1L, 3L, 1L)  // = Queue(1L, 2L, 3L)
     * Queue.rangeClosedBy(1L, 4L, 2L)  // = Queue(1L, 3L)
     * Queue.rangeClosedBy(4L, 1L, -2L) // = Queue(4L, 2L)
     * Queue.rangeClosedBy(4L, 1L, 2L)  // = Queue()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or {@code Nil} if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static Queue<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Queue.ofAll(List.rangeClosedBy(from, toInclusive, step));
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
     * @param elements An java.lang.Iterable of elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    public Queue<T> enqueueAll(java.lang.Iterable<? extends T> elements) {
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
    public Queue<T> appendAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return enqueueAll(elements);
    }

    @Override
    public Queue<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public <U> Queue<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        return toList().crossProduct(that).toQueue();
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
    public Queue<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return toList().distinctBy(comparator).toQueue();
    }

    @Override
    public <U> Queue<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return toList().distinctBy(keyExtractor).toQueue();
    }

    @Override
    public Queue<T> drop(int n) {
        if(n <= 0) {
            return this;
        }
        return new Queue<>(front.drop(n), rear.dropRight(n - front.length()));
    }

    @Override
    public Queue<T> dropRight(int n) {
        if(n <= 0) {
            return this;
        }
        return new Queue<>(front.dropRight(n), rear.drop(n - front.length()));
    }

    @Override
    public Queue<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> dropped = toList().dropWhile(predicate);
        return dropped.length() == length() ? this : dropped.toQueue();
    }

    @Override
    public Queue<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> filtered = toList().filter(predicate);
        return filtered.length() == length() ? this : filtered.toQueue();
    }

    @Override
    public <U> Queue<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new Queue<>(front.flatMap(mapper), rear.flatMap(mapper));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Queue<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    public Queue<Object> flatten() {
        return toList().flatten().toQueue();
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
    public <C> Map<C, Queue<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final Queue<T> queue = map.get(key).orElse(Queue.empty()).enqueue(t);
            return map.put(key, queue);
        });
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
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
    public int indexOf(T element, int from) {
        final int frontIndex = front.indexOf(element, from);
        if (frontIndex != -1) {
            return frontIndex;
        } else {
            // we need to reverse because we search the first occurrence
            final int rearIndex = rear.reverse().indexOf(element, from - front.length());
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
    public Queue<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
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
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public int lastIndexOf(T element, int end) {
        return toList().lastIndexOf(element, end);
    }

    @Override
    public int length() {
        return front.length() + rear.length();
    }

    @Override
    public <U> Queue<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new Queue<>(front.map(mapper), rear.map(mapper));
    }

    @Override
    public Queue<T> padTo(int length, T element) {
        if(length <= length()) {
            return this;
        }
        return toList().padTo(length, element).toQueue();
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
    public Queue<T> prependAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Queue<>(front.prependAll(elements), rear);
    }

    @Override
    public Queue<T> remove(T element) {
        final List<T> removed = toList().remove(element);
        return removed.length() == length() ? this : removed.toQueue();
    }

    @Override
    public Queue<T> removeFirst(Predicate<T> predicate) {
        final List<T> removed = toList().removeFirst(predicate);
        return removed.length() == length() ? this : removed.toQueue();
    }

    @Override
    public Queue<T> removeLast(Predicate<T> predicate) {
        final List<T> removed = toList().removeLast(predicate);
        return removed.length() == length() ? this : removed.toQueue();
    }

    @Override
    public Queue<T> removeAt(int indx) {
        return toList().removeAt(indx).toQueue();
    }

    @Override
    public Queue<T> removeAll(T element) {
        final List<T> newFront = front.removeAll(element);
        final List<T> newRear = rear.removeAll(element);
        return newFront.length() + newRear.length() == length() ? this : new Queue<>(newFront, newRear);
    }

    @Override
    public Queue<T> removeAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final List<T> newFront = front.removeAll(elements);
        final List<T> newRear = rear.removeAll(elements);
        return newFront.length() + newRear.length() == length() ? this : new Queue<>(newFront, newRear);
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
    public Queue<T> retainAll(java.lang.Iterable<? extends T> elements) {
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
    public Queue<T> slice(int beginIndex) {
        return toList().slice(beginIndex).toQueue();
    }

    @Override
    public Queue<T> slice(int beginIndex, int endIndex) {
        return toList().slice(beginIndex, endIndex).toQueue();
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
    public Tuple2<Queue<T>, Queue<T>> splitAt(Predicate<? super T> predicate) {
        return toList().splitAt(predicate).map(List::toQueue, List::toQueue);
    }

    @Override
    public Tuple2<Queue<T>, Queue<T>> splitAtInclusive(Predicate<? super T> predicate) {
        return toList().splitAtInclusive(predicate).map(List::toQueue, List::toQueue);
    }

    @Override
    public boolean startsWith(Iterable<? extends T> that, int offset) {
        return toList().startsWith(that, offset);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
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
        if(n >= length()) {
            return this;
        }
        final int frontLength = front.length();
        if (n < frontLength) {
            return new Queue<>(front.take(n), List.empty());
        } else if (n == frontLength) {
            return new Queue<>(front, List.empty());
        } else {
            return new Queue<>(front, rear.takeRight(n - frontLength));
        }
    }

    @Override
    public Queue<T> takeRight(int n) {
        if(n >= length()) {
            return this;
        }
        final int rearLength = rear.length();
        if (n < rearLength) {
            return new Queue<>(rear.take(n).reverse(), List.empty());
        } else if (n == rearLength) {
            return new Queue<>(rear.reverse(), List.empty());
        } else {
            return new Queue<>(front.takeRight(n - rearLength), rear);
        }
    }

    @Override
    public Queue<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> taken = toList().takeWhile(predicate);
        return taken.length() == length() ? this : taken.toQueue();
    }

    @Override
    public <U> Queue<U> unit(java.lang.Iterable<? extends U> iterable) {
        return Queue.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Queue<T1>, Queue<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return toList().unzip(unzipper).map(List::toQueue, List::toQueue);
    }

    @Override
    public <U> Queue<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return toList().zip(that).toQueue();
    }

    @Override
    public <U> Queue<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
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
            final Queue<?> that = (Queue<?>) o;
            return this.hashCode() == that.hashCode() && this.toList().equals(that.toList());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode.get();
    }

    @Override
    public String toString() {
        return mkString(", ", "Queue(", ")");
    }
}
