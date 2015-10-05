/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List.Nil;
import javaslang.collection.Tree.Node;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

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
     * Returns the singleton empty tree.
     *
     * @param <T> Type of tree values.
     * @return The empty tree.
     */
    static <T> Empty<T> empty() {
        return Empty.instance();
    }

    static <T> Node<T> of(T value) {
        return new Node<>(value, List.empty());
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    static <T> Node<T> of(T value, Node<T>... children) {
        Objects.requireNonNull(children, "children is null");
        return new Node<>(value, List.of(children));
    }

    static <T> Node<T> of(T value, java.lang.Iterable<? extends Node<T>> children) {
        Objects.requireNonNull(children, "children is null");
        return new Node<>(value, List.ofAll(children));
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

    default Iterator<T> iterator(Order order) {
        return traverse(order).iterator();
    }

    default Seq<T> traverse() {
        return isEmpty() ? Stream.empty() : traverse(PRE_ORDER);
    }

    /**
     * Traverses the Tree in a specific order.
     *
     * @param order the tree traversal order
     * @return A List containing all elements of this tree, which is List if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default Seq<T> traverse(Order order) {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            Objects.requireNonNull(order, "order is null");
            switch (order) {
                case PRE_ORDER:
                    return TreeModule.Traversal.preOrder(this);
                case IN_ORDER:
                    return TreeModule.Traversal.inOrder(this);
                case POST_ORDER:
                    return TreeModule.Traversal.postOrder(this);
                case LEVEL_ORDER:
                    return TreeModule.Traversal.levelOrder(this);
                default:
                    throw new IllegalStateException("Unknown order: " + order.name());
            }
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
    default Option<T> findLast(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            return iterator().findLast(predicate);
        }
    }

    @Override
    default <U> Tree<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return null; // TODO
    }

    @Override
    default Tree<?> flatten() {
        return null; // TODO
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
        return isEmpty() ? None.instance() : new Some<>(head());
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
        return isEmpty() ? None.instance() : new Some<>(init());
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
    default <U> Tree<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return null; // TODO
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
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceRight of empty Tree");
        } else {
            return iterator().reduceRight(op);
        }
    }

    @Override
    default Tree<T> replace(T currentElement, T newElement) {
        return null; // TODO
    }

    @Override
    default Tree<T> replaceAll(T currentElement, T newElement) {
        return null; // TODO
    }

    @Override
    default Tree<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        return null; // TODO
    }

    @Override
    default Seq<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return traverse().retainAll(elements);
    }

    @Override
    default Tuple2<Seq<T>, Seq<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(Stream.empty(), Stream.empty());
        } else {
            return null; // TODO
        }
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
        return isEmpty() ? None.instance() : new Some<>(tail());
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

    @Override
    default <T1, T2> Tuple2<Tree<T1>, Tree<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return null; // TODO
    }

    @Override
    default <U> Tree<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return null; // TODO
    }

    @Override
    default <U> Tree<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return null; // TODO
    }

    @Override
    default Tree<Tuple2<T, Integer>> zipWithIndex() {
        return null; // TODO
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

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

        private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

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
        public int length() {
            return size.get();
        }

        @Override
        public boolean isLeaf() {
            return children.isEmpty();
        }

        @Override
        public List<Node<T>> getChildren() {
            return children;
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
            return hashCode.get();
        }

        @Override
        public String toString() {
            return isLeaf() ? "(" + value + ")" : toLispString(this);
        }

        private static String toLispString(Node<?> node) {
            final String value = String.valueOf(node.value);
            if (node.isLeaf()) {
                return value;
            } else {
                return String.format("(%s %s)", value, node.children.map(Node::toLispString).mkString(" "));
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
        public int length() {
            return 0;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return Traversable.hash(this);
        }

        @Override
        public String toString() {
            return "()";
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

    // -- static extension methods

    /**
     * Counts the number of branches of this tree. The empty tree and a leaf have no branches.
     *
     * @param tree the Tree
     * @return The number of branches of this tree.
     */
    static int branchCount(Tree<?> tree) {
        if (tree.isEmpty() || tree.isLeaf()) {
            return 0;
        } else {
            return tree.getChildren().foldLeft(1, (count, child) -> count + Tree.branchCount(child));
        }
    }

    /**
     * Counts the number of leaves of this tree. The empty tree has no leaves.
     *
     * @param tree the Tree
     * @return The number of leaves of this tree.
     */
    static int leafCount(Tree<?> tree) {
        if (tree.isEmpty()) {
            return 0;
        } else if (tree.isLeaf()) {
            return 1;
        } else {
            return tree.getChildren().foldLeft(0, (count, child) -> count + Tree.leafCount(child));
        }
    }

    /**
     * Counts the number of nodes (i.e. branches and leaves) of this tree. The empty tree has no nodes.
     *
     * @param tree the Tree
     * @return The number of nodes of this tree.
     */
    static int nodeCount(Tree<?> tree) {
        if (tree.isEmpty()) {
            return 0;
        } else {
            return 1 + tree.getChildren().foldLeft(0, (count, child) -> count + Tree.nodeCount(child));
        }
    }
}

interface TreeModule {

    final class Traversal {

        private Traversal() {
        }

        static <T> Stream<T> preOrder(Tree<T> tree) {
            return tree.getChildren()
                    .foldLeft(Stream.of(tree.getValue()), (acc, child) -> acc.appendAll(preOrder(child)));
        }

        static <T> Stream<T> inOrder(Tree<T> tree) {
            if (tree.isLeaf()) {
                return Stream.of(tree.getValue());
            } else {
                final List<Node<T>> children = tree.getChildren();
                return children.tail().foldLeft(Stream.<T> empty(), (acc, child) -> acc.appendAll(inOrder(child)))
                        .prepend(tree.getValue())
                        .prependAll(inOrder(children.head()));
            }
        }

        static <T> Stream<T> postOrder(Tree<T> tree) {
            return tree.getChildren()
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
}
