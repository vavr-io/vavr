/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.Require.UnsatisfiedRequirementException;
import javaslang.Tuple.Tuple2;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An immutable List implementation, suitable for concurrent programming.
 * <p/>
 * A List is composed of a {@code head()} element and a {@code tail()} List.
 * <p/>
 * There are two implementations of the interface List:
 * <ul>
 * <li>{@link Nil}, which represents a List containing no elements.</li>
 * <li>{@link Cons}, which represents a List containing elements.</li>
 * </ul>
 * <p/>
 * Use {@code List.of(1, 2, 3)} instead of {@code new Cons(1, new Cons(2, new Cons(3, Nil.instance())))}.
 * <p/>
 * Use {@code List.nil()} instead of {@code Nil.instance()}.
 * <p/>
 * In contrast to the mutable List variant {@link java.util.ArrayList}, it does not make sense for immutable Lists to
 * implement the interface {@link java.lang.Cloneable} because of the following conclusion: <blockquote>
 * "[...] , it doesn’t make sense for immutable classes to support object copying, because copies would be virtually indistinguishable from the original."
 * </blockquote> <em>(see Effective Java, 2nd ed., p. 61)</em>.
 *
 * @param <E> Component type of the List.
 */
public interface List<E> extends Seq<E>, Algebra.Monad<E, List<?>>, Algebra.Monoid<List<E>> {

    /**
     * Convenience method, well known from java.util collections. It has no effect on the original List, it just returns
     * Nil.instance().
     *
     * @return Nil.instance()
     */
    @Override
    default List<E> clear() {
        return Nil.instance();
    }

    @Override
    default boolean contains(E element) {
        return indexOf(element) != -1;
    }

    @Override
    default E get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on empty list");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        List<E> list = this;
        for (int i = index - 1; i >= 0; i--) {
            list = list.tail();
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on list of size %s", index, index - i));
            }
        }
        return list.head();
    }

    @Override
    default int indexOf(E element) {
        int index = 0;
        for (List<E> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    default List<E> insert(int index, E element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        List<E> preceding = Nil.instance();
        List<E> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e) on list of size " + size());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<E> result = tail.prepend(element);
        for (E next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<E> insertAll(int index, Iterable<? extends E> elements) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        List<E> preceding = Nil.instance();
        List<E> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on list of size " + size());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<E> result = tail.prependAll(elements);
        for (E next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default Iterator<E> iterator(int index) {
        return sublist(index).iterator();
    }

    @Override
    default int lastIndexOf(E element) {
        int result = -1, index = 0;
        for (List<E> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default List<E> prepend(E element) {
        return new Cons<>(element, this);
    }

    @Override
    default List<E> prependAll(Iterable<? extends E> elements) {
        Require.nonNull(elements, "elements is null");
        return List.of(elements).foldRight(this, (x, xs) -> xs.prepend(x));
    }

    @Override
    default List<E> remove(E element) {
        List<E> preceding = List.nil();
        List<E> tail = this;
        boolean found = false;
        while (!found && !tail.isEmpty()) {
            final E head = tail.head();
            if (head.equals(element)) {
                found = true;
            } else {
                preceding = preceding.prepend(head);
            }
            tail = tail.tail();
        }
        List<E> result = tail;
        for (E next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<E> removeAll(E removed) {
        List<E> result = List.nil();
        for (E element : this) {
            if (!element.equals(removed)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
    }

    @Override
    default List<E> removeAll(Iterable<? extends E> elements) {
        List<E> removed = List.of(elements);
        List<E> result = List.nil();
        for (E element : this) {
            if (!removed.contains(element)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
    }

    @Override
    default List<E> replace(E currentElement, E newElement) {
        List<E> preceding = Nil.instance();
        List<E> tail = this;
        while (!tail.isEmpty() && !Objects.equals(tail.head(), currentElement)) {
            preceding = preceding.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        }
        // skip the current head element because it is replaced
        List<E> result = tail.tail().prepend(newElement);
        for (E next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<E> replaceAll(E currentElement, E newElement) {
        List<E> result = Nil.instance();
        for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
            final E head = list.head();
            final E elem = Objects.equals(head, currentElement) ? newElement : head;
            result = result.prepend(elem);
        }
        return result.reverse();
    }

    @Override
    default List<E> replaceAll(UnaryOperator<E> operator) {
        List<E> result = Nil.instance();
        for (E element : this) {
            result = result.prepend(operator.apply(element));
        }
        return result.reverse();
    }

    @Override
    default List<E> retainAll(Iterable<? extends E> elements) {
        final List<E> keeped = List.of(elements).distinct();
        List<E> result = List.nil();
        for (E element : this) {
            if (keeped.contains(element)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
    }

    @Override
    default List<E> set(int index, E element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on empty list");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        List<E> preceding = Nil.instance();
        List<E> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("set(" + index + ", e) on list of size " + size());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on list of size " + size());
        }
        // skip the current head element because it is replaced
        List<E> result = tail.tail().prepend(element);
        for (E next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<E> subsequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("sublist(" + beginIndex + ")");
        }
        List<E> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("sublist(%s) on list of size %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default List<E> sublist(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex - beginIndex < 0) {
            throw new IndexOutOfBoundsException(String.format("sublist(%s, %s) on list of size %s", beginIndex,
                    endIndex, size()));
        }
        List<E> result = Nil.instance();
        List<E> list = this;
        for (int i = 0; i < endIndex; i++, list = list.tail()) {
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("sublist(%s, %s) on list of size %s", beginIndex,
                        endIndex, i));
            }
            if (i >= beginIndex) {
                result = result.prepend(list.head());
            }
        }
        return result.reverse();
    }

    @Override
    default List<E> sort() {
        return stream().sorted().collect(List.collector());
    }

    @Override
    default List<E> sort(Comparator<? super E> c) {
        return stream().sorted(c).collect(List.collector());
    }

    // -- List conversion

    /**
     * Returns an array containing all elements of this List in the same order. The array is created in O(2n).
     *
     * @return The elements of this List as array.
     */
    default Object[] toArray() {
        final Object[] result = new Object[size()];
        int i = 0;
        for (List<E> list = this; !list.isEmpty(); list = list.tail(), i++) {
            result[i] = list.head();
        }
        return result;
    }

    /**
     * Returns the given array filled with this elements in the same order or a new Array containing this elements, if
     * array.length &lt; size(). This takes O(2n).
     * <p/>
     * According to {@link java.util.ArrayList#toArray(Object[])}, the element in the array immediately following the
     * end of the List is set to null.
     *
     * @param array An Array to be filled with this elements.
     * @return The given array containing this elements or a new one if array.length &lt; size().
     */
    default E[] toArray(E[] array) {
        return toArrayList().toArray(array);
    }

    /**
     * Converts this List into an {@link java.util.ArrayList} which is mutable.
     *
     * @return An ArrayList of the same size, containing this elements.
     */
    default java.util.ArrayList<E> toArrayList() {
        final java.util.ArrayList<E> result = new java.util.ArrayList<>();
        for (E element : this) {
            result.add(element);
        }
        return result;
    }

    /**
     * Returns a sequential {@link java.util.stream.Stream} representation of this List.
     * <p/>
     * This call is equivalent to {@code StreamSupport.stream(spliterator(), false)}.
     *
     * @return A sequential Stream of elements of this List.
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a parallel {@link java.util.stream.Stream} representation of this List.
     * <p/>
     * This call is equivalent to {@code StreamSupport.stream(spliterator(), true)}.
     *
     * @return A parallel Stream of elements of this List.
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    // -- Implementation of interface Foldable

    @Override
    default <T> List<T> unit(T element) {
        return List.of(element);
    }

    @Override
    default List<E> zero() {
        return List.nil();
    }

    @Override
    default List<E> combine(List<E> l1, List<E> l2) {
        return l2.prependAll(l1);
    }

    // -- List specific optimization of default Foldable interface methods

    @Override
    default List<E> distinct() {
        // TODO: optimize (-> set/red-black-tree)
        return foldRight(nil(), (x, xs) -> xs.contains(x) ? xs : xs.prepend(x));
    }

    @Override
    default List<E> filter(Predicate<? super E> predicate) {
        return foldRight(nil(), (x, xs) -> predicate.test(x) ? xs.prepend(x) : xs);
    }

    // @see Algebra.Monad.flatMap()
    @Override
    default <T, LIST extends Manifest<T, List<?>>> List<T> flatMap(Function<? super E, LIST> mapper) {
        //noinspection unchecked
        return foldRight(nil(), (x, xs) -> xs.prependAll((List<T>) mapper.apply(x)));
    }

    // @see Algebra.Monad.map()
    @Override
    default <T> List<T> map(Function<? super E, ? extends T> mapper) {
        return foldRight(nil(), (x, xs) -> xs.prepend(mapper.apply(x)));
    }

    @Override
    default List<E> intersperse(E element) {
        return foldRight(nil(), (x, xs) -> xs.isEmpty() ? xs.prepend(x) : xs.prepend(element).prepend(x));
    }

    /**
     * Reverses this List and returns a new List in O(n).
     * <p/>
     * The result is equivalent to
     * <p/>
     * <pre>
     * <code>List&lt;E&gt; reverse(List&lt;E&gt; reversed, List&lt;E&gt; remaining) {
     *     if (remaining.isEmpty()) {
     *        return reversed;
     *     } else {
     *        return reverse(reversed.prepend(remaining.head()), remaining.tail());
     *     }
     * }
     * reverse(Nil.instance(), this);</code>
     * </pre>
     * <p/>
     * but implemented without recursion.
     *
     * @return A new List containing the elements of this List in reverse order.
     */
    @Override
    default List<E> reverse() {
        return foldLeft(nil(), List::prepend);
    }

    /**
     * Returns a List formed from this List and another Iterable collection by combining corresponding elements in
     * pairs. If one of the two collections is longer than the other, its remaining elements are ignored.
     *
     * @param <T>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new List containing pairs consisting of corresponding elements of this list and that. The length of the
     * returned collection is the minimum of the lengths of this List and that.
     * @throws UnsatisfiedRequirementException if that is null.
     */
    @Override
    default <T> List<Tuple2<E, T>> zip(Iterable<T> that) {
        Require.nonNull(that, "that is null");
        List<Tuple2<E, T>> result = Nil.instance();
        List<E> list1 = this;
        Iterator<T> list2 = that.iterator();
        while (!list1.isEmpty() && list2.hasNext()) {
            result = result.prepend(Tuple.of(list1.head(), list2.next()));
            list1 = list1.tail();
        }
        return result.reverse();
    }

    /**
     * Returns a List formed from this List and another Iterable collection by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     *
     * @param <T>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this List is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this List.
     * @return A new List containing pairs consisting of corresponding elements of this List and that. The length of the
     * returned collection is the maximum of the lengths of this List and that. If this List is shorter than
     * that, thisElem values are used to pad the result. If that is shorter than this List, thatElem values are
     * used to pad the result.
     * @throws UnsatisfiedRequirementException if that is null.
     */
    @Override
    default <T> List<Tuple2<E, T>> zipAll(Iterable<T> that, E thisElem, T thatElem) {
        Require.nonNull(that, "that is null");
        List<Tuple2<E, T>> result = Nil.instance();
        Iterator<E> list1 = this.iterator();
        Iterator<T> list2 = that.iterator();
        while (list1.hasNext() || list2.hasNext()) {
            final E elem1 = list1.hasNext() ? list1.next() : thisElem;
            final T elem2 = list2.hasNext() ? list2.next() : thatElem;
            result = result.prepend(Tuple.of(elem1, elem2));
        }
        return result.reverse();
    }

    /**
     * Zips this List with its indices.
     *
     * @return A new List containing all elements of this List paired with their index, starting with 0.
     */
    @Override
    default List<Tuple2<E, Integer>> zipWithIndex() {
        List<Tuple2<E, Integer>> result = Nil.instance();
        int index = 0;
        for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
            result = result.prepend(Tuple.of(list.head(), index++));
        }
        return result.reverse();
    }

    @Override
    default <E1, E2> Tuple2<List<E1>, List<E2>> unzip(Function<? super E, Tuple2<E1, E2>> unzipper) {
        Require.nonNull(unzipper, "unzipper is null");
        List<E1> xs = nil();
        List<E2> ys = nil();
        for (E element : this) {
            final Tuple2<E1, E2> t = unzipper.apply(element);
            xs = xs.prepend(t._1);
            ys = ys.prepend(t._2);
        }
        return Tuple.of(xs.reverse(), ys.reverse());
    }

    /**
     * Takes the first n elements of this list or the whole list, if this size &lt; n. The elements are taken in O(n).
     * <p/>
     * The result is equivalent to {@code sublist(0, n)} but does not throw if n &lt; 0 or n &gt; size(). In the case of
     * n &lt; 0 the Nil is returned, in the case of n &gt; size() this List is returned.
     *
     * @param n The number of elements to take.
     * @return A list consisting of the first n elements of this list or the whole list, if it has less than n elements.
     */
    @Override
    default List<E> take(int n) {
        List<E> result = Nil.instance();
        List<E> list = this;
        for (int i = 0; i < n && !list.isEmpty(); i++, list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
    }

    @Override
    default List<E> takeWhile(Predicate<? super E> predicate) {
        List<E> result = Nil.instance();
        for (List<E> list = this; !list.isEmpty() && predicate.test(list.head()); list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
    }

    // -- Implementation of interface Iterable

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    default Iterator<E> iterator() {

        final class ListIterator implements Iterator<E> {

            List<E> list = List.this;

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public E next() {
                if (list.isEmpty()) {
                    throw new NoSuchElementException();
                } else {
                    final E result = list.head();
                    list = list.tail();
                    return result;
                }
            }
        }

        return new ListIterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#spliterator()
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    // -- Object equals, hashCode, toString

    /**
     * Equivalent to {@link java.util.List#equals(Object)}.
     */
    @Override
    boolean equals(Object o);

    /**
     * Equivalent to {@link java.util.List#hashCode()}.
     */
    @Override
    int hashCode();

    /**
     * Returns a String representation of this List.
     * <p/>
     * If this is Nil, {@code "()"} is returned.
     * <p/>
     * If this is an Cons containing the elements e1, ..., en, then {@code "(" + Strings.toString(e1)
     * + ", " + ... + ", " + Strings.toString(en) + ")"} is returned.
     *
     * @return This List as String.
     */
    @Override
    String toString();


    // -- factory methods

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()} .
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> List<T> nil() {
        return Nil.instance();
    }

    /**
     * Creates a List of the given elements.
     * <p/>
     * <pre>
     * <code>  List.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))</code>
     * </pre>
     *
     * @param <T>      Component type of the List.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     */
    @SafeVarargs
    static <T> List<T> of(T... elements) {
        Require.nonNull(elements, "elements is null");
        List<T> result = Nil.instance();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = result.prepend(elements[i]);
        }
        return result;
    }

    /**
     * Creates a List of the given elements.
     *
     * @param <T>      Component type of the List.
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     */
    static <T> List<T> of(Iterable<? extends T> elements) {
        Require.nonNull(elements, "elements is null");
        if (elements instanceof List) {
            @SuppressWarnings("unchecked")
            final List<T> list = (List<T>) elements;
            return list;
        } else {
            List<T> result = Nil.instance();
            for (T element : elements) {
                result = result.prepend(element);
            }
            return result.reverse();
        }
    }

    // -- List providers

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link Stream#collect(Collector)} to obtain a {@link javaslang.collection.List}.
     *
     * @param <T> Component type of the List.
     * @return A List Collector.
     */
    static <T> Collector<T, ArrayList<T>, List<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, List<T>> finisher = elements -> {
            List<T> result = Nil.instance();
            for (T element : elements) {
                result = result.prepend(element);
            }
            return result.reverse();
        };
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    // -- List implementations

    /**
     * Non-empty List.
     *
     * @param <E> Component type of the List.
     */
    // DEV NOTE: class declared final because of serialization proxy pattern.
    // (see Effective Java, 2nd ed., p. 315)
    static final class Cons<E> extends AbstractList<E> implements ValueObject {

        private static final long serialVersionUID = 53595355464228669L;

        private final E head;
        private final List<E> tail;

        public Cons(E head, List<E> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public E head() {
            return head;
        }

        @Override
        public List<E> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        // -- Serializable implementation

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p/>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * {@code readObject} method for the serialization proxy pattern.
         * <p/>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
         * instance fields.
         *
         * @param <E> The component type of the underlying list.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        private static final class SerializationProxy<E> implements Serializable {

            private static final long serialVersionUID = 3851894487210781138L;

            // the instance to be serialized/deserialized
            private transient Cons<E> list;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param list a Cons
             */
            SerializationProxy(Cons<E> list) {
                this.list = list;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(list.size());
                for (List<E> l = list; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws InvalidObjectException If the stream contains no list elements.
             * @throws IOException            If an error occurs reading from the stream.
             */
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, InvalidObjectException,
                    IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                List<E> temp = Nil.instance();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final E element = (E) s.readObject();
                    temp = temp.prepend(element);
                }
                list = (Cons<E>) temp.reverse();
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p/>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return list;
            }
        }
    }

    /**
     * The empty List.
     * <p/>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <E> Component type of the List.
     */
    static final class Nil<E> extends AbstractList<E> implements ValueObject {

        private static final long serialVersionUID = 809473773619488283L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        public static <T> Nil<T> instance() {
            @SuppressWarnings("unchecked")
            final Nil<T> instance = (Nil<T>) INSTANCE;
            return instance;
        }

        @Override
        public E head() {
            throw new UnsupportedOperationException("head of empty list");
        }

        @Override
        public List<E> tail() {
            throw new UnsupportedOperationException("tail of empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        // -- Serializable implementation

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

    // -- List API shared by implementations Cons and Nil

    /**
     * This class is needed because the interface {@link List} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * <p/>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <E> Component type of the List.
     */
    static abstract class AbstractList<E> implements List<E> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof List)) {
                return false;
            } else {
                List<?> list1 = this;
                List<?> list2 = (List<?>) o;
                while (!list1.isEmpty() && !list2.isEmpty()) {
                    final boolean isEqual = Objects.equals(list1.head(), list2.head());
                    if (!isEqual) {
                        return false;
                    }
                    list1 = list1.tail();
                    list2 = list2.tail();
                }
                return list1.isEmpty() && list2.isEmpty();
            }
        }

        @Override
        public int hashCode() {
            int hashCode = 1;
            for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
                final E element = list.head();
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            return map(Strings::toString).join(", ", "List(", ")");
        }
    }
}
