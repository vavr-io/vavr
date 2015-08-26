/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Match;
import javaslang.control.None;
import javaslang.control.Option;

import java.io.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

/**
 * <p>A general Tree interface.</p>
 *
 * @param <T> component type of this Tree
 * @since 1.1.0
 */
public interface Tree<T> extends Traversable<T> {

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

    <U> Tree<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

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
    List<? extends Tree<T>> getChildren();

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

    <U> Tree<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Traverses the Tree in pre-order.
     *
     * @return A List containing all elements of this tree, which is List if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default List<T> traverse() {
        return traverse(Order.PRE_ORDER);
    }

    /**
     * Traverses the Tree in a specific order.
     *
     * @param order the tree traversal order
     * @return A List containing all elements of this tree, which is List if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default List<T> traverse(Order order) {
        Objects.requireNonNull(order, "order is null");
        class Traversal {
            List<T> preOrder(Tree<T> tree) {
                return tree.getChildren()
                        .foldLeft(List.of(tree.getValue()), (acc, child) -> acc.appendAll(preOrder(child)));
            }

            List<T> inOrder(Tree<T> tree) {
                if (tree.isLeaf()) {
                    return List.of(tree.getValue());
                } else {
                    final List<? extends Tree<T>> children = tree.getChildren();
                    return children.tail().foldLeft(List.<T> empty(), (acc, child) -> acc.appendAll(inOrder(child)))
                            .prepend(tree.getValue())
                            .prependAll(inOrder(children.head()));
                }
            }

            List<T> postOrder(Tree<T> tree) {
                return tree.getChildren()
                        .foldLeft(List.<T> empty(), (acc, child) -> acc.appendAll(postOrder(child)))
                        .append(tree.getValue());
            }

            List<T> levelOrder(Tree<T> tree) {
                List<T> result = List.empty();
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
        if (isEmpty()) {
            return List.empty();
        } else {
            final Traversal traversal = new Traversal();
            return Match.of(order)
                    .whenIs(Order.PRE_ORDER).then(() -> traversal.preOrder(this))
                    .whenIs(Order.IN_ORDER).then(() -> traversal.inOrder(this))
                    .whenIs(Order.POST_ORDER).then(() -> traversal.postOrder(this))
                    .whenIs(Order.LEVEL_ORDER).then(() -> traversal.levelOrder(this))
                    .get();
        }
    }

    // -- Methods inherited from Traversable

    @Override
    default Empty<T> clear() {
        return Tree.empty();
    }

    /**
     * Checks whether the given element occurs in this tree.
     *
     * @param element An element.
     * @return true, if this tree contains
     */
    @Override
    default boolean contains(T element) {
        if (isEmpty()) {
            return false;
        } else if (Objects.equals(getValue(), element)) {
            return true;
        } else {
            for (Tree<T> child : getChildren()) {
                if (child.contains(element)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    List<T> distinct();

    @Override
    List<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> List<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    List<T> drop(int n);

    @Override
    List<T> dropRight(int n);

    @Override
    List<T> dropWhile(Predicate<? super T> predicate);

    @Override
    List<T> filter(Predicate<? super T> predicate);

    @Override
    default <U> Tree<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    Tree<Object> flatten();


    @Override
    <C> Map<C, List<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    T head();

    @Override
    Option<T> headOption();

    @Override
    List<T> init();

    @Override
    Option<List<T>> initOption();

    /**
     * Checks if this tree is the empty tree.
     *
     * @return true, if this tree is empty, false otherwise.
     */
    @Override
    boolean isEmpty();

    /**
     * Iterates over the elements of this tree in pre-order.
     *
     * @return An iterator of this tree's node values.
     */
    @Override
    default Iterator<T> iterator() {
        return traverse().iterator();
    }

    @Override
    Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate);

    @Override
    Tree<T> peek(Consumer<? super T> action);

    @Override
    Tree<T> replace(T currentElement, T newElement);

    @Override
    Tree<T> replaceAll(T currentElement, T newElement);

    @Override
    Tree<T> replaceAll(UnaryOperator<T> operator);

    @Override
    List<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate);

    @Override
    List<T> tail();

    @Override
    Option<List<T>> tailOption();

    @Override
    List<T> take(int n);

    @Override
    List<T> takeRight(int n);

    @Override
    List<T> takeWhile(Predicate<? super T> predicate);

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
        public List<T> distinct() {
            return traverse().distinct();
        }

        @Override
        public List<T> distinctBy(Comparator<? super T> comparator) {
            return traverse().distinctBy(comparator);
        }

        @Override
        public <U> List<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
            return traverse().distinctBy(keyExtractor);
        }

        @Override
        public List<T> drop(int n) {
            return traverse().drop(n);
        }

        @Override
        public List<T> dropRight(int n) {
            return traverse().dropRight(n);
        }

        @Override
        public List<T> dropWhile(Predicate<? super T> predicate) {
            return traverse().dropWhile(predicate);
        }

        @Override
        public List<T> filter(Predicate<? super T> predicate) {
            return traverse().filter(predicate);
        }

        @Override
        public Option<T> findLast(Predicate<? super T> predicate) {
            return traverse().findLast(predicate);
        }

        @Override
        public <U> Tree<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
            return null; // TODO
        }

        @Override
        public Tree<Object> flatten() {
            return null; // TODO
        }

        @Override
        public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
            return traverse().foldRight(zero, f);
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean hasDefiniteSize() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public boolean isLeaf() {
            return children.isEmpty();
        }

        @Override
        public boolean isTraversableAgain() {
            return true;
        }

        @Override
        public <C> Map<C, List<T>> groupBy(Function<? super T, ? extends C> classifier) {
            return traverse().groupBy(classifier);
        }

        @Override
        public T head() {
            return traverse().head(); // TODO: use iterator (in traverse order)
        }

        @Override
        public Option<T> headOption() {
            return traverse().headOption(); // TODO: use iterator (in traverse order)
        }

        @Override
        public List<T> init() {
            return traverse().init();
        }

        @Override
        public Option<List<T>> initOption() {
            return traverse().initOption();
        }

        @Override
        public List<Node<T>> getChildren() {
            return children;
        }

        @Override
        public Node<T> peek(Consumer<? super T> action) {
            traverse().peek(action); // TODO: use iterator (in traverse order)
            return this;
        }

        @Override
        public T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
            return traverse().reduceRight(op);
        }

        @Override
        public Tree<T> replace(T currentElement, T newElement) {
            return null; // TODO
        }

        @Override
        public Tree<T> replaceAll(T currentElement, T newElement) {
            return null; // TODO
        }

        @Override
        public Tree<T> replaceAll(UnaryOperator<T> operator) {
            return null; // TODO
        }

        @Override
        public List<T> retainAll(java.lang.Iterable<? extends T> elements) {
            return traverse().retainAll(elements);
        }

        @Override
        public Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
            return traverse().span(predicate);
        }

        @Override
        public List<T> tail() {
            return traverse().tail();
        }

        @Override
        public Option<List<T>> tailOption() {
            return traverse().tailOption();
        }

        @Override
        public List<T> take(int n) {
            return traverse().take(n);
        }

        @Override
        public List<T> takeRight(int n) {
            return traverse().takeRight(n);
        }

        @Override
        public List<T> takeWhile(Predicate<? super T> predicate) {
            return traverse().takeWhile(predicate);
        }

        @Override
        public <U> Node<U> map(Function<? super T, ? extends U> mapper) {
            return new Node<>(mapper.apply(value), children.map(child -> child.map(mapper)));
        }

        @Override
        public Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate) {
            return traverse().partition(predicate);
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
         * <p>
         * {@code writeReplace} method for the serialization proxy pattern.
         * </p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
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
                return node;
            }
        }
    }

    /**
     * The singleton instance of the empty tree.
     *
     * @param <T> type of the tree's values
     */
    final class Empty<T> implements Tree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Empty<?> INSTANCE = new Empty<>();

        // hidden
        private Empty() {
        }

        /**
         * Returns the singleton instance of the empty tree.
         *
         * @param <T> type of the tree's values
         * @return the single tree instance
         */
        @SuppressWarnings("unchecked")
        public static <T> Empty<T> instance() {
            return (Empty<T>) INSTANCE;
        }

        @Override
        public List.Nil<T> distinct() {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> distinctBy(Comparator<? super T> comparator) {
            return List.Nil.instance();
        }

        @Override
        public <U> List.Nil<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> drop(int n) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> dropRight(int n) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> dropWhile(Predicate<? super T> predicate) {
            return List.Nil.instance();
        }

        @Override
        public List<Node<T>> getChildren() {
            return List.Nil.instance();
        }

        @Override
        public T getValue() {
            throw new UnsupportedOperationException("getValue of empty Tree");
        }

        @Override
        public boolean hasDefiniteSize() {
            return true;
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
        public boolean isTraversableAgain() {
            return true;
        }

        @Override
        public Empty<T> peek(Consumer<? super T> action) {
            return this;
        }

        @Override
        public T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
            throw new UnsupportedOperationException("reduceRight of empty Tree");
        }

        @Override
        public Tree<T> replace(T currentElement, T newElement) {
            return Tree.empty();
        }

        @Override
        public Tree<T> replaceAll(T currentElement, T newElement) {
            return Tree.empty();
        }

        @Override
        public Tree<T> replaceAll(UnaryOperator<T> operator) {
            return Tree.empty();
        }

        @Override
        public List.Nil<T> retainAll(java.lang.Iterable<? extends T> elements) {
            return List.Nil.instance();
        }

        @Override
        public Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
            return Tuple.of(List.empty(), List.empty());
        }

        @Override
        public List<T> tail() {
            throw new UnsupportedOperationException("tail of empty tree");
        }

        @Override
        public None<List<T>> tailOption() {
            return None.instance();
        }

        @Override
        public List.Nil<T> take(int n) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> takeRight(int n) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> takeWhile(Predicate<? super T> predicate) {
            return List.Nil.instance();
        }

        @Override
        public List.Nil<T> filter(Predicate<? super T> predicate) {
            return List.Nil.instance();
        }

        @Override
        public None<T> findLast(Predicate<? super T> predicate) {
            return None.instance();
        }

        @Override
        public <U> Empty<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
            return Empty.instance();
        }

        @Override
        public <U> Empty<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
            return Empty.instance();
        }

        @Override
        public Empty<Object> flatten() {
            return Empty.instance();
        }

        @Override
        public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
            throw new UnsupportedOperationException("foldRight of empty tree");
        }

        @Override
        public <C> Map<C, List<T>> groupBy(Function<? super T, ? extends C> classifier) {
            return HashMap.empty();
        }

        @Override
        public T head() {
            throw new UnsupportedOperationException("head of empty tree");
        }

        @Override
        public None<T> headOption() {
            return None.instance();
        }

        @Override
        public List<T> init() {
            throw new UnsupportedOperationException("init of empty tree");
        }

        @Override
        public None<List<T>> initOption() {
            return None.instance();
        }

        @Override
        public <U> Empty<U> map(Function<? super T, ? extends U> mapper) {
            return Empty.instance();
        }

        @Override
        public Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate) {
            return Tuple.of(List.empty(), List.empty());
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
     * <p>Tree traversal order.</p>
     *
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
