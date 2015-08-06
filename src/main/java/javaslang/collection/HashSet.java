/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code HashSet} implementation.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
final class HashSet<T> implements Set<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashSet<?> INSTANCE = new HashSet<>(HashArrayMappedTrie.empty());

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> empty() {
        return (HashSet<T>) INSTANCE;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Set}s.
     *
     * @param <T> Component type of the List.
     * @return A javaslang.collection.List Collector.
     */
    public static <T> Collector<T, ArrayList<T>, Set<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Set<T>> finisher = HashSet::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    static <T> HashSet<T> of(T element) {
        return HashSet.<T> empty().add(element);
    }

    /**
     * <p>
     * Creates a HashSet of the given elements.
     * </p>
     *
     * <pre>
     * <code>  HashSet.of(1, 2, 3, 4)
     * </pre>
     *
     * @param <T>      Component type of the HashSet.
     * @param elements Zero or more elements.
     * @return A set containing the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    static <T> HashSet<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashSet<T> result = HashSet.empty();
        for (int i = 0; i < elements.length; i++) {
            result = result.add(elements[i]);
        }
        return result;
    }

    /**
     * Creates a HashSet of the given entries.
     *
     * @param entries Set entries
     * @param <T>     The value type
     * @return A new HashSet containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> ofAll(Iterable<? extends T> entries) {
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof HashSet) {
            return (HashSet<T>) entries;
        } else {
            HashArrayMappedTrie<T, Object> tree = HashArrayMappedTrie.empty();
            for (T entry : entries) {
                tree = tree.put(entry, entry);
            }
            return tree.isEmpty() ? empty() : new HashSet<>(tree);
        }
    }

    private final HashArrayMappedTrie<T, Object> tree;
    private final transient Lazy<List<T>> list;
    private final transient Lazy<Integer> hash;

    private HashSet(HashArrayMappedTrie<T, Object> tree) {
        this.tree = tree;
        this.list = Lazy.of(() -> List.ofAll(tree::iterator).map(t -> t._1));
        this.hash = Lazy.of(() -> list.get().hashCode());
    }

    @Override
    public HashSet<T> add(T element) {
        return new HashSet<>(tree.put(element, element));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Iterator<Tuple2<T, Object>> it = tree.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return it.next()._1;
            }
        };
    }

    @Override
    public int hashCode() {
        return hash.get();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof HashSet) {
            return list.equals(((HashSet) o).list);
        } else {
            return false;
        }
    }


    @Override
    public HashSet<T> clear() {
        return empty();
    }

    @Override
    public HashSet<Tuple2<T, T>> cartesianProduct() {
        return cartesianProduct(this);
    }

    @Override
    public <U> HashSet<Tuple2<T, U>> cartesianProduct(Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        return HashSet.ofAll(list.get().cartesianProduct(that));
    }

    @Override
    public boolean contains(T element) {
        return tree.get(element).isDefined();
    }

    @Override
    public HashSet<T> distinct() {
        return HashSet.ofAll(list.get().distinct());
    }

    @Override
    public HashSet<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return HashSet.ofAll(list.get().distinctBy(comparator));
    }

    @Override
    public <U> HashSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return HashSet.ofAll(list.get().distinctBy(keyExtractor));
    }

    @Override
    public HashSet<T> drop(int n) {
        return HashSet.ofAll(list.get().drop(n));
    }

    @Override
    public HashSet<T> dropRight(int n) {
        return HashSet.ofAll(list.get().dropRight(n));
    }

    @Override
    public HashSet<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashSet.ofAll(list.get().dropWhile(predicate));
    }

    @Override
    public HashSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashSet.ofAll(list.get().filter(predicate));
    }

    @Override
    public HashSet<Some<T>> filterOption(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashSet.ofAll(list.get().filterOption(predicate));
    }

    @Override
    public HashSet<T> findAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashSet.ofAll(list.get().findAll(predicate));
    }

    @Override
    public <U> HashSet<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return HashSet.ofAll(list.get().flatMap(mapper));
    }

    @Override
    public <U> HashSet<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return HashSet.ofAll(list.get().flatMapM(mapper));
    }

    @Override
    public HashSet<Object> flatten() {
        return HashSet.ofAll(list.get().flatten());
    }

    @Override
    public HashSet<HashSet<T>> grouped(int size) {
        return HashSet.ofAll(list.get().grouped(size).map(HashSet::ofAll));
    }

    @Override
    public T head() {
        return list.get().head();
    }

    @Override
    public Option<T> headOption() {
        return list.get().headOption();
    }

    @Override
    public HashSet<T> init() {
        return HashSet.ofAll(list.get().init());
    }

    @Override
    public Option<? extends Set<T>> initOption() {
        Option<List<T>> opt = list.get().initOption();
        return opt.isDefined() ? new Some<>(HashSet.ofAll(opt.get())) : None.instance();
    }

    @Override
    public HashSet<T> intersperse(T element) {
        return HashSet.ofAll(list.get().intersperse(element));
    }

    @Override
    public int length() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public <U> HashSet<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return HashSet.ofAll(list.get().map(mapper));
    }

    @Override
    public Tuple2<HashSet<T>, HashSet<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        Tuple2<List<T>, List<T>> t = list.get().partition(predicate);
        return Tuple.of(HashSet.ofAll(t._1), HashSet.ofAll(t._2));
    }

    @Override
    public HashSet<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        return HashSet.ofAll(list.get().peek(action));
    }

    @Override
    public HashSet<T> remove(T element) {
        return new HashSet<>(tree.remove(element));
    }

    @Override
    public HashSet<T> removeAll(T element) {
        return remove(element);
    }

    @Override
    public HashSet<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<T, Object> trie = tree;
        for (T element : elements) {
            trie = trie.remove(element);
        }
        return new HashSet<>(trie);
    }

    @Override
    public HashSet<T> replace(T currentElement, T newElement) {
        if (tree.containsKey(currentElement)) {
            return remove(currentElement).add(newElement);
        } else {
            return this;
        }
    }

    @Override
    public HashSet<T> replaceAll(T currentElement, T newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public HashSet<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        HashSet<T> result = HashSet.empty();
        for (T element : this) {
            result = result.add(operator.apply(element));
        }
        return result;
    }

    @Override
    public HashSet<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final HashSet<T> keeped = HashSet.ofAll(elements);
        HashSet<T> result = HashSet.empty();
        for (T element : this) {
            if (keeped.contains(element)) {
                result = result.add(element);
            }
        }
        return result.reverse();
    }

    @Override
    public HashSet<T> reverse() {
        return this;
    }

    @Override
    public HashSet<? extends Set<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public HashSet<HashSet<T>> sliding(int size, int step) {
        List<HashSet<T>> l = list.get().sliding(size, step).map(HashSet::ofAll);
        return HashSet.ofAll(l);
    }

    @Override
    public Tuple2<HashSet<T>, HashSet<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        Tuple2<List<T>, List<T>> t = list.get().span(predicate);
        return Tuple.of(HashSet.ofAll(t._1), HashSet.ofAll(t._2));
    }

    @Override
    public HashSet<T> tail() {
        return HashSet.ofAll(list.get().tail());
    }

    @Override
    public Option<HashSet<T>> tailOption() {
        Option<List<T>> opt = list.get().tailOption();
        return opt.isDefined() ? new Some<>(HashSet.ofAll(opt.get())) : None.instance();
    }

    @Override
    public HashSet<T> take(int n) {
        HashSet<T> result = HashSet.empty();
        Iterator<T> it = iterator();
        while (n > 0 && it.hasNext()) {
            result = result.add(it.next());
            n--;
        }
        return result;
    }

    @Override
    public HashSet<T> takeRight(int n) {
        return HashSet.ofAll(list.get().takeRight(n));
    }

    @Override
    public HashSet<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashSet.ofAll(list.get().takeWhile(predicate));
    }

    @Override
    public <U> HashSet<U> unit(Iterable<? extends U> iterable) {
        return HashSet.ofAll(list.get().unit(iterable));
    }

    @Override
    public <T1, T2> Tuple2<HashSet<T1>, HashSet<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Tuple2<List<T1>, List<T2>> t = list.get().unzip(unzipper);
        return Tuple.of(HashSet.ofAll(t._1), HashSet.ofAll(t._2));
    }

    @Override
    public <U> HashSet<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return HashSet.ofAll(list.get().zip(that));
    }

    @Override
    public <U> HashSet<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return HashSet.ofAll(list.get().zipAll(that, thisElem, thatElem));
    }

    @Override
    public HashSet<Tuple2<T, Integer>> zipWithIndex() {
        return HashSet.ofAll(list.get().zipWithIndex());
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
        return new SerializationProxy<>(this.list.get());
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

    /**
     * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
     * instance fields.
     *
     * @param <T> The component type of the underlying list.
     */
    // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
    // classes. Also, it may not be compatible with circular object graphs.
    private static final class SerializationProxy<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        // the instance to be serialized/deserialized
        private transient List<T> list;

        /**
         * Constructor for the case of serialization, called by {@link HashSet#writeReplace()}.
         * <p/>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param list a Cons
         */
        SerializationProxy(List<T> list) {
            this.list = list;
        }

        /**
         * Write an object to a serialization stream.
         *
         * @param s An object serialization stream.
         * @throws java.io.IOException If an error occurs writing to the stream.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(list.length());
            for (List<T> l = list; !l.isEmpty(); l = l.tail()) {
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
        private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
            s.defaultReadObject();
            final int size = s.readInt();
            if (size < 0) {
                throw new InvalidObjectException("No elements");
            }
            List<T> temp = List.empty();
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                final T element = (T) s.readObject();
                temp = temp.prepend(element);
            }
            list = temp.reverse();
        }

        /**
         * <p>
         * {@code readResolve} method for the serialization proxy pattern.
         * </p>
         * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
         * serialization system to translate the serialization proxy back into an instance of the enclosing class
         * upon deserialization.
         *
         * @return A deserialized instance of the enclosing class.
         */
        private Object readResolve() {
            return HashSet.ofAll(list);
        }
    }
}
