/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
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
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 42 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the Queue
 * @since 1.3.0
 */
public interface Queue<T> extends Seq<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Queue}
     * .
     *
     * @param <T> Component type of the Queue.
     * @return A javaslang.collection.Queue Collector.
     */
    static <T> Collector<T, ArrayList<T>, Queue<T>> collector() {
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
    static <T> Queue<T> nil() {
        return new Cons<>(List.nil(), List.nil());
    }

    /**
     * Returns a singleton {@code Queue}, i.e. a {@code Queue} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Queue instance containing the given element
     */
    static <T> Queue<T> of(T element) {
        return new Cons<>(List.of(element), List.nil());
    }

    /**
     * <p>
     * Creates a Queue of the given elements.
     * </p>
     *
     * @param <T>      Component type of the Queue.
     * @param elements Zero or more elements.
     * @return A queue containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
    static <T> Queue<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new Cons<>(List.of(elements), List.nil());
    }

    /**
     * Creates a Queue of the given elements.
     *
     * @param <T>      Component type of the Queue.
     * @param elements An Iterable of elements.
     * @return A queue containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    static <T> Queue<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Queue) {
            @SuppressWarnings("unchecked")
            final Queue<T> queue = (Queue<T>) elements;
            return queue;
        } else {
            return new Cons<>(List.ofAll(elements), List.nil());
        }
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static Queue<Integer> range(int from, int toExclusive) {
        return new Cons<>(List.range(from, toExclusive), List.nil());
    }

    /**
     * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static Queue<Integer> rangeClosed(int from, int toInclusive) {
        return new Cons<>(List.rangeClosed(from, toInclusive), List.nil());
    }

    /**
     * Removes an element from this Queue.
     *
     * @return a tuple containing the first element and the remaining elements of this Queue
     * @throws java.util.NoSuchElementException if this Queue is empty
     */
    Tuple2<T, Queue<T>> dequeue();

    /**
     * Removes an element from this Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise {@code Some} {@code Tuple} containing the first element and the remaining elements of this Queue
     */
    Option<Tuple2<T, Queue<T>>> dequeueOption();

    /**
     * Enqueues a new element.
     *
     * @param element The new element
     * @return a new {@code Queue} instance, containing the new element
     */
    Queue<T> enqueue(T element);

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    Queue<T> enqueue(T... elements);

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    Queue<T> enqueueAll(Iterable<T> elements);

    /**
     * Returns the first element without modifying the Queue.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this Queue is empty
     */
    T peek();

    /**
     * Returns the first element without modifying the Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise a {@code Some} containing the first element
     */
    Option<T> peekOption();

    // -- Adjusted return types of Seq methods

    @Override
    Queue<T> append(T element);

    @Override
    Queue<T> appendAll(Iterable<? extends T> elements);

    @Override
    Queue<T> clear();

    @Override
    Queue<Queue<T>> combinations();

    @Override
    Queue<Queue<T>> combinations(int k);

    @Override
    Queue<T> distinct();

    @Override
    <U> Queue<T> distinct(Function<? super T, ? extends U> keyExtractor);

    @Override
    Queue<T> drop(int n);

    @Override
    Queue<T> dropRight(int n);

    @Override
    Queue<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Queue<T> filter(Predicate<? super T> predicate);

    @Override
    Queue<T> findAll(Predicate<? super T> predicate);

    @Override
    <U> Queue<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper);

    @Override
    <U> Queue<U> flatten(Function<? super T, ? extends Iterable<U>> f);

    @Override
    T get(int index);

    @Override
    Queue<Queue<T>> grouped(int size);

    @Override
    int indexOf(T element);

    @Override
    Queue<T> init();

    @Override
    Option<Queue<T>> initOption();

    @Override
    Queue<T> insert(int index, T element);

    @Override
    Queue<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    Queue<T> intersperse(T element);

    @Override
    int lastIndexOf(T element);

    @Override
    <U> Queue<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<Queue<T>, Queue<T>> partition(Predicate<? super T> predicate);

    @Override
    Queue<T> peek(Consumer<? super T> action);

    @Override
    Queue<Queue<T>> permutations();

    @Override
    Queue<T> prepend(T element);

    @Override
    Queue<T> prependAll(Iterable<? extends T> elements);

    @Override
    Queue<T> remove(T element);

    @Override
    Queue<T> removeAll(T element);

    @Override
    Queue<T> removeAll(Iterable<? extends T> elements);

    @Override
    Queue<T> replace(T currentElement, T newElement);

    @Override
    Queue<T> replaceAll(T currentElement, T newElement);

    @Override
    Queue<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Queue<T> retainAll(Iterable<? extends T> elements);

    @Override
    Queue<T> reverse();

    @Override
    Queue<T> set(int index, T element);

    @Override
    Queue<Queue<T>> sliding(int size);

    @Override
    Queue<Queue<T>> sliding(int size, int step);

    @Override
    Queue<T> sort();

    @Override
    Queue<T> sort(Comparator<? super T> comparator);

    @Override
    Tuple2<Queue<T>, Queue<T>> span(Predicate<? super T> predicate);

    @Override
    Tuple2<Queue<T>, Queue<T>> splitAt(int n);

    @Override
    Queue<T> subsequence(int beginIndex);

    @Override
    Queue<T> subsequence(int beginIndex, int endIndex);

    @Override
    Queue<T> tail();

    @Override
    Option<Queue<T>> tailOption();

    @Override
    Queue<T> take(int n);

    @Override
    Queue<T> takeRight(int n);

    @Override
    Queue<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<Queue<T1>, Queue<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Queue<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Queue<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Queue<Tuple2<T, Integer>> zipWithIndex();

    /**
     * Non-empty {@code List}, consisting of a {@code head} and a {@code tail}.
     *
     * @param <T> Component type of the List.
     * @since 1.1.0
     */
    // DEV NOTE: class declared final because of serialization proxy pattern (see Effective Java, 2nd ed., p. 315)
    final class Cons<T> extends AbstractQueue<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final List<T> front;
        private final List<T> rear;

        /**
         * Creates a List consisting of a head value and a trailing List.
         *
         * @param front The front elements of the Queue
         * @param rear The rear elements of the Queue
         */
        Cons(List<T> front, List<T> rear) {
            this.front = front;
            this.rear = rear;
        }

        @Override
        public T head() {
            return front.head();
        }

        @Override
        public Some<T> headOption() {
            return new Some<>(front.head());
        }

        @Override
        public Queue<T> init() {
            if (rear.isEmpty()) {
                return cons(front.init(), rear);
            } else {
                return cons(front, rear.tail());
            }
        }

        @Override
        public Some<Queue<T>> initOption() {
            return new Some<>(init());
        }

        @Override
        public Queue<T> tail() {
            return cons(front.tail(), rear);
        }

        @Override
        public Some<Queue<T>> tailOption() {
            return new Some<>(tail());
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        /**
         * <p>
         * {@code writeReplace} method for the serialization proxy pattern.
         * </p>
         * <p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         * </p>
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * <p>
         * {@code readObject} method for the serialization proxy pattern.
         * </p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws java.io.InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            private transient Cons<T> queue;

            SerializationProxy(Cons<T> queue) {
                this.queue = queue;
            }

            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                writeList(s, queue.front);
                writeList(s, queue.rear);
            }

            private void writeList(ObjectOutputStream s, List<T> list) throws IOException {
                s.writeInt(list.length());
                for (List<T> l = list; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                List<T> front = readList(s);
                List<T> rear = readList(s);
                queue = new Cons<>(front, rear);
            }

            private List<T> readList(ObjectInputStream s) throws ClassNotFoundException, IOException {
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                List<T> list = List.nil();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final T element = (T) s.readObject();
                    list = list.prepend(element);
                }
                return list.reverse();
            }

            private Object readResolve() {
                return queue;
            }
        }
    }

    /**
     * Representation of the singleton empty {@code List}.
     *
     * @param <T> Component type of the List.
     * @since 1.1.0
     */
    final class Nil<T> extends AbstractQueue<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton instance of the liked list.
         *
         * @param <T> Component type of the List
         * @return the singleton instance of the linked list.
         */
        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head of empty queue");
        }

        @Override
        public None<T> headOption() {
            return None.instance();
        }

        @Override
        public Queue<T> init() {
            throw new NoSuchElementException("init of empty queue");
        }

        @Override
        public None<Queue<T>> initOption() {
            return None.instance();
        }

        @Override
        public Queue<T> tail() {
            throw new UnsupportedOperationException("tail of empty queue");
        }

        @Override
        public None<Queue<T>> tailOption() {
            return None.instance();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }

    /**
     * <p>
     * This class is needed because the interface {@link List} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * </p>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <T> Component type of the List.
     * @since 1.3.0
     */
    abstract class AbstractQueue<T> implements Queue<T> {

        private AbstractQueue() {
        }

        protected static <T> Queue<T> cons(List<T> front, List<T> rear) {
            if (front.isEmpty()) {
                if (rear.isEmpty()) {
                    // this is the reason why we don't use `new Cons<>(front, rear)` directly
                    return Nil.instance();
                } else {
                    return new Cons<>(rear.reverse(), List.nil());
                }
            } else {
                return new Cons<>(front, rear);
            }
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
}
