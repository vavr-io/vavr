/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.List.Nil;
import javaslang.collection.Tree.Empty;
import javaslang.collection.Tree.Node;
import javaslang.collection.TreeModule.*;
import javaslang.control.Match;
import javaslang.control.Option;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static javaslang.collection.Tree.Order.PRE_ORDER;

/**
 * A general Tree interface.
 *
 * @param <T> component type of this Tree
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public interface Tree<T> extends Traversable<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Tree}.
     *
     * @param <T> Component type of the Tree.
     * @return A javaslang.collection.Tree Collector.
     */
    static <T> Collector<T, ArrayList<T>, Tree<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Tree<T>> finisher = Tree::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the singleton empty tree.
     *
     * @param <T> Type of tree values.
     * @return The empty tree.
     */
    static <T> Empty<T> empty() {
        return Empty.instance();
    }

    /**
     * Returns a new Node containing the given value and having no children.
     *
     * @param value A value
     * @param <T>   Value type
     * @return A new Node instance.
     */
    static <T> Node<T> of(T value) {
        return new Node<>(value, List.empty());
    }

    /**
     * Returns a new Node containing the given value and having the given children.
     *
     * @param value    A value
     * @param children The child nodes, possibly empty
     * @param <T>      Value type
     * @return A new Node instance.
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> Node<T> of(T value, Node<T>... children) {
        Objects.requireNonNull(children, "children is null");
        return new Node<>(value, List.of(children));
    }

    /**
     * Returns a new Node containing the given value and having the given children.
     *
     * @param value    A value
     * @param children The child nodes, possibly empty
     * @param <T>      Value type
     * @return A new Node instance.
     */
    static <T> Node<T> of(T value, Iterable<Node<T>> children) {
        Objects.requireNonNull(children, "children is null");
        return new Node<>(value, List.ofAll(children));
    }

    /**
     * Creates a Tree of the given elements.
     *
     * @param <T> Component type of the List.
     * @param values Zero or more values.
     * @return A Tree containing the given values.
     * @throws NullPointerException if {@code values} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> Tree<T> of(T... values) {
        Objects.requireNonNull(values, "values is null");
        List<T> list = List.of(values);
        return list.isEmpty() ? Empty.instance() : new Node<>(list.head(), list.tail().map(Tree::of));
    }

    /**
     * Creates a Tree of the given elements.
     * <p>
     * If the given iterable is a tree, it is returned as result.
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the List.
     * @param iterable An Iterable of elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    static <T> Tree<T> ofAll(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Tree) {
            return (Tree<T>) iterable;
        } else {
            final List<T> list = List.ofAll(iterable);
            return list.isEmpty() ? Empty.instance() : new Node<>(list.head(), list.tail().map(Tree::of));
        }
    }

    /**
     * Returns a Tree containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the Tree
     * @param n The number of elements in the Tree
     * @param f The Function computing element values
     * @return A Tree consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    static <T> Tree<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, Tree.empty(), Tree::of);
    }

    /**
     * Returns a Tree containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the Tree
     * @param n The number of elements in the Tree
     * @param s The Supplier computing element values
     * @return A Tree of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    static <T> Tree<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, Tree.empty(), Tree::of);
    }

    /**
     * Gets the value of this tree.
     *
     * @return The value of this tree.
     * @throws java.lang.UnsupportedOperationException if this tree is empty
     */
    T getValue();

    /**
     * Returns the children of this tree.
     *
     * @return the tree's children
     */
    List<Node<T>> getChildren();

    /**
     * Checks if this Tree is a leaf. A tree is a leaf if it is a Node with no children.
     * Because the empty tree is no Node, it is not a leaf by definition.
     *
     * @return true if this tree is a leaf, false otherwise.
     */
    boolean isLeaf();

    /**
     * Checks if this Tree is a branch. A Tree is a branch if it is a Node which has children.
     * Because the empty tree is not a Node, it is not a branch by definition.
     *
     * @return true if this tree is a branch, false otherwise.
     */
    default boolean isBranch() {
        return !(isEmpty() || isLeaf());
    }

    /**
     * Traverses this tree in a specific {@link javaslang.collection.Tree.Order}.
     *
     * @param order A traversal order
     * @return A new Iterator
     */
    default Iterator<T> iterator(Order order) {
        return traverse(order).iterator();
    }

    /**
     * Returns the number of nodes (including root and leafs).
     *
     * @return The size of the tree.
     */
    int size();

    /**
     * Transforms this {@code Tree}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Tree<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Traverses this tree in {@link Order#PRE_ORDER}.
     *
     * @return A sequence of the tree values in pre-order.
     */
    default Seq<T> traverse() {
        return traverse(PRE_ORDER);
    }

    /**
     * Traverses the Tree in a specific order.
     *
     * @param order the tree traversal order
     * @return A List containing all elements of this tree, which is List if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default Seq<T> traverse(Order order) {
        Objects.requireNonNull(order, "order is null");
        if (isEmpty()) {
            return Stream.empty();
        } else {
            switch (order) {
                case PRE_ORDER:
                    return Traversal.preOrder(this);
                case IN_ORDER:
                    return Traversal.inOrder(this);
                case POST_ORDER:
                    return Traversal.postOrder(this);
                case LEVEL_ORDER:
                    return Traversal.levelOrder(this);
                default:
                    throw new IllegalStateException("Unknown order: " + order.name());
            }
        }
    }

    /**
     * Counts the number of branches of this tree. The empty tree and a leaf have no branches.
     *
     * @return The number of branches of this tree.
     */
    default int branchCount() {
        if (isEmpty() || isLeaf()) {
            return 0;
        } else {
            return getChildren().foldLeft(1, (count, child) -> count + child.branchCount());
        }
    }

    /**
     * Counts the number of leaves of this tree. The empty tree has no leaves.
     *
     * @return The number of leaves of this tree.
     */
    default int leafCount() {
        if (isEmpty()) {
            return 0;
        } else if (isLeaf()) {
            return 1;
        } else {
            return getChildren().foldLeft(0, (count, child) -> count + child.leafCount());
        }
    }

    /**
     * Counts the number of nodes (i.e. branches and leaves) of this tree. The empty tree has no nodes.
     *
     * @return The number of nodes of this tree.
     */
    default int nodeCount() {
        if (isEmpty()) {
            return 0;
        } else {
            return 1 + getChildren().foldLeft(0, (count, child) -> count + child.nodeCount());
        }
    }

    // -- Methods inherited from Traversable

    @Override
    default Tree<T> clear() {
        return Tree.empty();
    }

    @Override
    default Seq<T> distinct() {
        return traverse().distinct();
    }

    @Override
    default Seq<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().distinctBy(comparator);
        }
    }

    @Override
    default <U> Seq<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().distinctBy(keyExtractor);
        }
    }

    @Override
    default Seq<T> drop(int n) {
        if (n >= length()) {
            return Stream.empty();
        } else {
            return traverse().drop(n);
        }
    }

    @Override
    default Seq<T> dropRight(int n) {
        if (n >= length()) {
            return Stream.empty();
        } else {
            return traverse().dropRight(n);
        }
    }

    @Override
    default Seq<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    default Seq<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().dropWhile(predicate);
        }
    }

    @Override
    default Seq<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().filter(predicate);
        }
    }

    @Override
    default Seq<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    default <U> Tree<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? Empty.instance() : FlatMap.apply((Node<T>) this, mapper);
    }

    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return zero;
        } else {
            return iterator().foldRight(zero, f);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default <C> Map<C, Seq<T>> groupBy(Function<? super T, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        if (isEmpty()) {
            return HashMap.empty();
        } else {
            return (Map<C, Seq<T>>) traverse().groupBy(classifier);
        }
    }

    @Override
    default Iterator<Seq<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    default T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty tree");
        } else {
            return iterator().next();
        }
    }

    @Override
    default Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    @Override
    default Seq<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty tree");
        } else {
            return traverse().init();
        }
    }

    @Override
    default Option<Seq<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    default Iterator<T> iterator() {
        return traverse().iterator();
    }

    @Override
    default int length() {
        return size();
    }

    @Override
    default <U> Tree<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? Empty.instance() : TreeModule.Map.apply((Node<T>) this, mapper);
    }

    @Override
    default Match.MatchMonad.Of<Tree<T>> match() {
        return Match.of(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Tuple2<Seq<T>, Seq<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(Stream.empty(), Stream.empty());
        } else {
            return (Tuple2<Seq<T>, Seq<T>>) traverse().partition(predicate);
        }
    }

    @Override
    default Tree<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    default Tree<T> replace(T currentElement, T newElement) {
        if (isEmpty()) {
            return Empty.instance();
        } else {
            return Replace.apply((Node<T>) this, currentElement, newElement);
        }
    }

    @Override
    default Tree<T> replaceAll(T currentElement, T newElement) {
        return map(t -> Objects.equals(t, currentElement) ? newElement : t);
    }

    @Override
    default Seq<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return traverse().retainAll(elements);
    }

    @Override
    default Seq<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    default <U> Seq<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, List.empty(), List::prepend, List::reverse);
    }

    @Override
    default <U> Seq<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, List.empty(), List::prepend, Function.identity());
    }

    @Override
    default Iterator<Seq<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Iterator<Seq<T>> sliding(int size, int step) {
        return iterator().sliding(size, step);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Tuple2<Seq<T>, Seq<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(Stream.empty(), Stream.empty());
        } else {
            return (Tuple2<Seq<T>, Seq<T>>) traverse().span(predicate);
        }
    }

    @Override
    default Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default String stringPrefix() {
        return "Tree";
    }

    @Override
    default Seq<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty tree");
        } else {
            return traverse().tail();
        }
    }

    @Override
    default Option<Seq<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    default Seq<T> take(int n) {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().take(n);
        }
    }

    @Override
    default Seq<T> takeRight(int n) {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return traverse().takeRight(n);
        }
    }

    @Override
    default Seq<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return traverse().takeUntil(predicate);
    }

    @Override
    default Seq<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return traverse().takeWhile(predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <T1, T2> Tuple2<Tree<T1>, Tree<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (isEmpty()) {
            return Tuple.of(Empty.instance(), Empty.instance());
        } else {
            return (Tuple2<Tree<T1>, Tree<T2>>) (Object) Unzip.apply((Node<T>) this, unzipper);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default <T1, T2, T3> Tuple3<Tree<T1>, Tree<T2>, Tree<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (isEmpty()) {
            return Tuple.of(Empty.instance(), Empty.instance(), Empty.instance());
        } else {
            return (Tuple3<Tree<T1>, Tree<T2>, Tree<T3>>) (Object) Unzip.apply3((Node<T>) this, unzipper);
        }
    }

    @Override
    default <U> Tree<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return Empty.instance();
        } else {
            return Zip.apply((Node<T>) this, that.iterator());
        }
    }

    @Override
    default <U> Tree<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return Iterator.ofAll(that).map(elem -> Tuple.of(thisElem, elem)).toTree();
        } else {
            final java.util.Iterator<U> thatIter = that.iterator();
            final Tree<Tuple2<T, U>> tree = ZipAll.apply((Node<T>) this, thatIter, thatElem);
            if (thatIter.hasNext()) {
                final Iterable<Node<Tuple2<T, U>>> remainder = Iterator
                        .ofAll(thatIter)
                        .map(elem -> Tree.of(Tuple.of(thisElem, elem)));
                return new Node<>(tree.getValue(), tree.getChildren().appendAll(remainder));
            } else {
                return tree;
            }
        }
    }

    @Override
    default Tree<Tuple2<T, Integer>> zipWithIndex() {
        return zip(Iterator.from(0));
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Creates a neat 2-dimensional drawing of a tree. Unicode characters are used to draw node junctions.
     *
     * @return A nice string representation of the tree.
     */
    String draw();

    /**
     * Represents a tree node.
     *
     * @param <T> value type
     */
    final class Node<T> implements Tree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;
        private final List<Node<T>> children;
        private final Lazy<Integer> size;
        private final int hashCode;

        /**
         * Constructs a rose tree branch.
         *
         * @param value    A value.
         * @param children A non-empty list of children.
         * @throws NullPointerException     if children is null
         * @throws IllegalArgumentException if children is empty
         */
        public Node(T value, List<Node<T>> children) {
            Objects.requireNonNull(children, "children is null");
            this.value = value;
            this.children = children;
            this.size = Lazy.of(() -> 1 + children.foldLeft(0, (acc, child) -> acc + child.length()));
            this.hashCode = 31 * 31 + 31 * Objects.hashCode(value) + Objects.hashCode(children);
        }

        @Override
        public List<Node<T>> getChildren() {
            return children;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return children.isEmpty();
        }

        @Override
        public int size() {
            return size.get();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Node) {
                final Node<?> that = (Node<?>) o;
                return Objects.equals(this.getValue(), that.getValue())
                        && Objects.equals(this.getChildren(), that.getChildren());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public String toString() {
            return stringPrefix() + (isLeaf() ? "(" + value + ")" : toLispString(this));
        }

        @Override
        public String draw() {
            StringBuilder builder = new StringBuilder();
            drawAux("", builder);
            return builder.toString();
        }

        private void drawAux(String indent, StringBuilder builder) {
            builder.append(value);
            for (List<Node<T>> it = children; !it.isEmpty(); it = it.tail()) {
                final boolean isLast = it.tail().isEmpty();
                builder.append('\n')
                        .append(indent)
                        .append(isLast ? "└──" : "├──");
                it.head().drawAux(indent + (isLast ? "   " : "│  "), builder);
            }
        }

        private static String toLispString(Tree<?> tree) {
            final String value = String.valueOf(tree.getValue());
            if (tree.isLeaf()) {
                return value;
            } else {
                return String.format("(%s %s)", value, tree.getChildren().map(Node::toLispString).mkString(" "));
            }
        }

        // -- Serializable implementation

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p>
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
         * <p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws java.io.InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable nodes with final
         * instance fields.
         *
         * @param <T> The component type of the underlying tree.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            // the instance to be serialized/deserialized
            private transient Node<T> node;

            /**
             * Constructor for the case of serialization, called by {@link Node#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param node a Branch
             */
            SerializationProxy(Node<T> node) {
                this.node = node;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws java.io.IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeObject(node.value);
                s.writeObject(node.children);
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws IOException            If an error occurs reading from the stream.
             */
            @SuppressWarnings("unchecked")
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                final T value = (T) s.readObject();
                final List<Node<T>> children = (List<Node<T>>) s.readObject();
                node = new Node<>(value, children);
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return node;
            }
        }
    }

    /**
     * The empty tree. Use Tree.empty() to create an instance.
     *
     * @param <T> type of the tree's values
     */
    final class Empty<T> implements Tree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Empty<?> INSTANCE = new Empty<>();

        // hidden
        private Empty() {
        }

        @SuppressWarnings("unchecked")
        public static <T> Empty<T> instance() {
            return (Empty<T>) INSTANCE;
        }

        @Override
        public List<Node<T>> getChildren() {
            return Nil.instance();
        }

        @Override
        public T getValue() {
            throw new UnsupportedOperationException("getValue of empty Tree");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return stringPrefix() + "()";
        }

        @Override
        public String draw() { return "▣"; }

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

    /**
     * Tree traversal order.
     * <p>
     * Example tree:
     * <pre>
     * <code>
     *         1
     *        / \
     *       /   \
     *      /     \
     *     2       3
     *    / \     /
     *   4   5   6
     *  /       / \
     * 7       8   9
     * </code>
     * </pre>
     *
     * See also
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Tree_traversal">Tree traversal</a> (wikipedia)</li>
     * <li>See <a href="http://rosettacode.org/wiki/Tree_traversal">Tree traversal</a> (rosetta code)</li>
     * </ul>
     */
    // see http://programmers.stackexchange.com/questions/138766/in-order-traversal-of-m-way-trees
    enum Order {

        /**
         * 1 2 4 7 5 3 6 8 9 (= depth-first)
         */
        PRE_ORDER,

        /**
         * 7 4 2 5 1 8 6 9 3
         */
        IN_ORDER,

        /**
         * 7 4 5 2 8 9 6 3 1
         */
        POST_ORDER,

        /**
         * 1 2 3 4 5 6 7 8 9 (= breadth-first)
         */
        LEVEL_ORDER
    }
}

/**
 * Because the empty tree {@code Empty} cannot be a child of an existing tree, method implementations distinguish between the
 * empty and non-empty case. Because the structure of trees is recursive, often we have commands in the form of module
 * classes with one static method.
 */
interface TreeModule {

    final class FlatMap {

        @SuppressWarnings("unchecked")
        static <T, U> Tree<U> apply(Node<T> node, Function<? super T, ? extends Iterable<? extends U>> mapper) {
            final Tree<U> mapped = Tree.ofAll(mapper.apply(node.getValue()));
            if (mapped.isEmpty()) {
                return Tree.empty();
            } else {
                final List<Node<U>> children = (List<Node<U>>) (Object) node
                        .getChildren()
                        .map(child -> FlatMap.apply(child, mapper))
                        .filter(Tree::isDefined);
                return Tree.of(mapped.getValue(), children.prependAll(mapped.getChildren()));
            }
        }
    }

    final class Map {

        static <T, U> Node<U> apply(Node<T> node, Function<? super T, ? extends U> mapper) {
            final U value = mapper.apply(node.getValue());
            final List<Node<U>> children = node.getChildren().map(child -> Map.apply(child, mapper));
            return new Node<>(value, children);
        }
    }

    final class Replace {

        // Idea:
        // Traverse (depth-first) until a match is found, then stop and rebuild relevant parts of the tree.
        // If not found, return the same tree instance.
        static <T> Node<T> apply(Node<T> node, T currentElement, T newElement) {
            if (Objects.equals(node.getValue(), currentElement)) {
                return new Node<>(newElement, node.getChildren());
            } else {
                for (Node<T> child : node.getChildren()) {
                    final Node<T> newChild = Replace.apply(child, currentElement, newElement);
                    final boolean found = newChild != child;
                    if (found) {
                        final List<Node<T>> newChildren = node.getChildren().replace(child, newChild);
                        return new Node<>(node.getValue(), newChildren);
                    }
                }
                return node;
            }
        }
    }

    final class Traversal {

        static <T> Stream<T> preOrder(Tree<T> tree) {
            return tree.getChildren().foldLeft(Stream.of(tree.getValue()),
                    (acc, child) -> acc.appendAll(preOrder(child)));
        }

        static <T> Stream<T> inOrder(Tree<T> tree) {
            if (tree.isLeaf()) {
                return Stream.of(tree.getValue());
            } else {
                final List<Node<T>> children = tree.getChildren();
                return children
                        .tail()
                        .foldLeft(Stream.<T> empty(), (acc, child) -> acc.appendAll(inOrder(child)))
                        .prepend(tree.getValue())
                        .prependAll(inOrder(children.head()));
            }
        }

        static <T> Stream<T> postOrder(Tree<T> tree) {
            return tree
                    .getChildren()
                    .foldLeft(Stream.<T> empty(), (acc, child) -> acc.appendAll(postOrder(child)))
                    .append(tree.getValue());
        }

        static <T> Stream<T> levelOrder(Tree<T> tree) {
            Stream<T> result = Stream.empty();
            final java.util.Queue<Tree<T>> queue = new java.util.LinkedList<>();
            queue.add(tree);
            while (!queue.isEmpty()) {
                final Tree<T> next = queue.remove();
                result = result.prepend(next.getValue());
                queue.addAll(next.getChildren().toJavaList());
            }
            return result.reverse();
        }
    }

    final class Unzip {

        static <T, T1, T2> Tuple2<Node<T1>, Node<T2>> apply(Node<T> node,
                                                            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
            final Tuple2<? extends T1, ? extends T2> value = unzipper.apply(node.getValue());
            final List<Tuple2<Node<T1>, Node<T2>>> children = node
                    .getChildren()
                    .map(child -> Unzip.apply(child, unzipper));
            final Node<T1> node1 = new Node<>(value._1, children.map(t -> t._1));
            final Node<T2> node2 = new Node<>(value._2, children.map(t -> t._2));
            return Tuple.of(node1, node2);
        }

        static <T, T1, T2, T3> Tuple3<Node<T1>, Node<T2>, Node<T3>> apply3(Node<T> node,
                                                                           Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
            final Tuple3<? extends T1, ? extends T2, ? extends T3> value = unzipper.apply(node.getValue());
            final List<Tuple3<Node<T1>, Node<T2>, Node<T3>>> children = node.getChildren()
                    .map(child -> Unzip.apply3(child, unzipper));
            final Node<T1> node1 = new Node<>(value._1, children.map(t -> t._1));
            final Node<T2> node2 = new Node<>(value._2, children.map(t -> t._2));
            final Node<T3> node3 = new Node<>(value._3, children.map(t -> t._3));
            return Tuple.of(node1, node2, node3);
        }

    }

    final class Zip {

        @SuppressWarnings("unchecked")
        static <T, U> Tree<Tuple2<T, U>> apply(Node<T> node, java.util.Iterator<U> that) {
            if (!that.hasNext()) {
                return Empty.instance();
            } else {
                final Tuple2<T, U> value = Tuple.of(node.getValue(), that.next());
                final List<Node<Tuple2<T, U>>> children = (List<Node<Tuple2<T, U>>>) (Object) node
                        .getChildren()
                        .map(child -> Zip.apply(child, that))
                        .filter(Tree::isDefined);
                return new Node<>(value, children);
            }
        }
    }

    final class ZipAll {

        @SuppressWarnings("unchecked")
        static <T, U> Tree<Tuple2<T, U>> apply(Node<T> node, java.util.Iterator<U> that, U thatElem) {
            if (!that.hasNext()) {
                return node.map(value -> Tuple.of(value, thatElem));
            } else {
                final Tuple2<T, U> value = Tuple.of(node.getValue(), that.next());
                final List<Node<Tuple2<T, U>>> children = (List<Node<Tuple2<T, U>>>) (Object) node
                        .getChildren()
                        .map(child -> ZipAll.apply(child, that, thatElem))
                        .filter(Tree::isDefined);
                return new Node<>(value, children);
            }
        }
    }
}
