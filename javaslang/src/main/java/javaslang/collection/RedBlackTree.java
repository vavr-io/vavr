/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.NodeModule.Empty;
import javaslang.collection.NodeModule.ValueNode;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Objects;

import static javaslang.collection.NodeModule.SubViewModule.AscendingSubTreeView.ascendingSubTree;
import static javaslang.collection.NodeModule.SubViewModule.DescendingSubTreeView.descendingSubTree;
import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;

/**
 * Purely functional Red/Black Tree, inspired by <a href="https://github.com/kazu-yamamoto/llrbtree/blob/master/Data/Set/RBTree.hs">Kazu Yamamoto's Haskell implementation</a>.
 * <p>
 * Based on
 * <ul>
 * <li><a href="http://www.eecs.usma.edu/webs/people/okasaki/pubs.html#jfp99">Chris Okasaki, "Red-Black Trees in a Functional Setting", Journal of Functional Programming, 9(4), pp 471-477, July 1999</a></li>
 * <li>Stefan Kahrs, "Red-black trees with types", Journal of functional programming, 11(04), pp 425-432, July 2001</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
interface RedBlackTree<T> extends Iterable<T> {

    static <T extends Comparable<? super T>> RedBlackTree<T> empty() {
        return new Empty<>((Comparator<? super T> & Serializable) T::compareTo);
    }

    static <T> RedBlackTree<T> empty(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new Empty<>(comparator);
    }

    static <T extends Comparable<? super T>> RedBlackTree<T> of(T value) {
        return of((Comparator<? super T> & Serializable) T::compareTo, value);
    }

    @SuppressWarnings("unchecked")
    static <T> java.util.TreeMap<T, T> java(T... values) {
        java.util.TreeMap<T, T> map = new java.util.TreeMap<>();
        for (T value : values) {
            map.put(value, value);
        }

        return map;
    }

    static <T> RedBlackTree<T> of(Comparator<? super T> comparator, T value) {
        Objects.requireNonNull(comparator, "comparator is null");
        final Empty<T> empty = new Empty<>(comparator);
        return new ValueNode<>(BLACK, 1, empty, value, empty, empty);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T extends Comparable<? super T>> RedBlackTree<T> of(T... values) {
        Objects.requireNonNull(values, "values is null");
        return of((Comparator<? super T> & Serializable) T::compareTo, values);
    }

    @SafeVarargs
    static <T> RedBlackTree<T> of(Comparator<? super T> comparator, T... values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        RedBlackTree<T> tree = empty(comparator);
        for (T value : values) {
            tree = tree.insert(value);
        }
        return tree;
    }

    static <T extends Comparable<? super T>> RedBlackTree<T> ofAll(Iterable<? extends T> values) {
        Objects.requireNonNull(values, "values is null");
        return ofAll((Comparator<? super T> & Serializable) T::compareTo, values);
    }

    @SuppressWarnings("unchecked")
    static <T> RedBlackTree<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        // function equality is not computable => same object check
        if (values instanceof RedBlackTree && ((RedBlackTree<T>) values).comparator() == comparator) {
            return (RedBlackTree<T>) values;
        } else {
            RedBlackTree<T> tree = empty(comparator);
            for (T value : values) {
                tree = tree.insert(value);
            }
            return tree;
        }
    }

    /**
     * Return the {@link Color} of this Red/Black Tree node.
     * <p>
     * An empty node is {@code BLACK} by definition.
     *
     * @return Either {@code RED} or {@code BLACK}.
     */
    Color color();

    /**
     * Returns the underlying {@link java.util.Comparator} of this RedBlackTree.
     *
     * @return The comparator.
     */
    Comparator<T> comparator();

    /**
     * Checks, if this {@code RedBlackTree} contains the given {@code value}.
     *
     * @param value A value.
     * @return true, if this tree contains the value, false otherwise.
     */
    boolean contains(T value);

    /**
     * Returns the empty instance of this RedBlackTree.
     *
     * @return An empty ReadBlackTree
     */
    RedBlackTree<T> emptyInstance();

    /**
     * Finds the value stored in this tree, if exists, by applying the underlying comparator to the tree elements and
     * the given element.
     * <p>
     * Especially the value returned may differ from the given value, even if the underlying comparator states that
     * both are equal.
     *
     * @param value A value
     * @return Some value, if this tree contains a value equal to the given value according to the underlying comparator. Otherwise None.
     */
    Option<T> find(T value);

    /**
     * Returns the greatest value less than or equal to the given value, or {@link Option#none()} if there is no such value.
     *
     * @param value the value
     * @return the greatest value less than or equal to the given value, or {@link Option#none()} if there is no such value
     */
    Option<T> floor(T value);

    /**
     * Returns the lowest value greater than or equal to the given value, or {@link Option#none()} if there is no such value.
     *
     * @param value the value
     * @return the lowest value greater than or equal to the given value, or {@link Option#none()} if there is no such value
     */
    Option<T> ceiling(T value);

    /**
     * Returns the lowest value strictly greater than the given value, or {@link Option#none()} if there is no such value.
     *
     * @param value the value
     * @return the lowest value strictly greater than the given value, or {@link Option#none()} if there is no such value
     */
    Option<T> higher(T value);

    /**
     * Returns the greatest value strictly less than the given value, or {@link Option#none()} if there is no such value.
     *
     * @param value the value
     * @return the greatest value strictly less than the given value, or {@link Option#none()} if there is no such value
     */
    Option<T> lower(T value);

    /**
     * Returns the highest element of this tree according to the underlying comparator.
     *
     * @return the highest element if tree is not empty, otherwise {@link Option#none}
     */
    Option<T> max();

    /**
     * Returns the lowest element of this tree according to the underlying comparator.
     *
     * @return the lowest element if this is not empty, otherwise {@link Option#none}
     */
    Option<T> min();

    /**
     * Checks if this {@link RedBlackTree} is empty.
     *
     * @return true, if it is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if the current node is a leaf.
     *
     * @return true if this node is a leaf, false otherwise
     */
    boolean isLeaf();

    /**
     * Checks if the current tree is a view of another tree
     *
     * @return
     */
    boolean isView();

    /**
     * Returns the size of this tree.
     *
     * @return the number of nodes of this tree, or 0 if this is an empty tree
     */
    int size();

    /**
     * Inserts a new value into this tree.
     *
     * @param value A value.
     * @return A new tree if this tree does not contain the given value, otherwise the same tree instance.
     */
    RedBlackTree<T> insert(T value);

    /**
     * Deletes a value from this RedBlackTree.
     *
     * @param value A value
     * @return A new RedBlackTree if the value is present, otherwise this.
     */
    RedBlackTree<T> delete(T value);

    RedBlackTree<T> difference(RedBlackTree<T> tree);

    RedBlackTree<T> intersection(RedBlackTree<T> tree);

    /**
     * Adds all of the elements of the given {@code tree} to this tree, if not already present.
     *
     * @param tree The RedBlackTree to form the union with.
     * @return A new RedBlackTree that contains all distinct elements of this and the given {@code tree}.
     */
    RedBlackTree<T> union(RedBlackTree<T> tree);

    /**
     * Returns an Iterator that iterates elements in the order induced by the underlying Comparator.
     * <p>
     * Internally an in-order traversal of the RedBlackTree is performed.
     * <p>
     * Example:
     *
     * <pre><code>
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * </code></pre>
     *
     * Iteration order: 1, 2, 3, 4, 5, 6, 7
     * <p>
     * See also <a href="http://n00tc0d3r.blogspot.de/2013/08/implement-iterator-for-binarytree-i-in.html">Implement Iterator for BinaryTree I (In-order)</a>.
     */
    @Override
    Iterator<T> iterator();

    /**
     * Creates a sub tree from the existing tree between values {@code fromStart} and {@code toEnd}.
     * <p>
     * The {@code fromInclude} and {@code toInclusive} flags control whether the {@code fromStart} and {@code toEnd}
     * values are included or not.
     * <p>
     * As far as clients are concerned, this method creates a new tree that can be operated as normal.
     * Creating a sub tree is a very quick operation, as under the hood we are just creating a view on the existing tree.
     *
     * @param fromStart     The lowest value in the sub tree
     * @param fromInclusive {@code true} if {@code fromStart} is to be included.
     *                      {@code false} if the low end point starts with the next value after {@code fromStart}
     * @param toEnd         The highest value in the sub tree
     * @param toInclusive   {@code true} if {@code toEnd} is to be included.
     *                      {@code false} if the high end point ends with the previous value before {@code toEnd}
     *
     * @return the sub tree created with values between {@code fromStart} and {@code toEnd}
     * @throws IllegalArgumentException If {@code fromStart} value is greater than {@code toEnd} value
     */
    RedBlackTree<T> subTree(Option<T> fromStart, boolean fromInclusive, Option<T> toEnd, boolean toInclusive);

    /**
     * Returns a reverse order tree with the contents of this tree.
     * <p>
     * The returned set has the ordering equivalent to {@link Comparator#reversed()} of the tree's comparator.
     *
     * @return a reverse order tree of this tree
     */
    RedBlackTree<T> descendingTree();

    /**
     * Returns a descending Iterator that iterates elements in the reversed order induced by the underlying Comparator.
     * <p>
     * Internally a reverse in-order traversal of the RedBlackTree is performed.
     * <p>
     * Example:
     *
     * <pre><code>
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * </code></pre>
     *
     * Iteration order: 7, 6, 5, 4, 3, 2, 1
     * <p>
     * See also <a href="http://n00tc0d3r.blogspot.de/2013/08/implement-iterator-for-binarytree-i-in.html">Implement Iterator for BinaryTree I (In-order)</a>.
     */
    default Iterator<T> descendingIterator() {
        return descendingTree().iterator();
    }

    /**
     * Compares color, value and sub-trees. The comparator is not compared because function equality is not computable.
     *
     * @return The hash code of this tree.
     */
    @Override
    boolean equals(Object o);

    /**
     * Computes the hash code of this tree based on color, value and sub-trees. The comparator is not taken into account.
     *
     * @return The hash code of this tree.
     */
    @Override
    int hashCode();

    /**
     * Returns a Lisp like representation of this tree.
     *
     * @return This Tree as Lisp like String.
     */
    String toLispString();

    enum Color {

        RED, BLACK;

        @Override
        public String toString() {
            return (this == RED) ? "R" : "B";
        }
    }
}

interface NodeModule {

    /**
     * A RedBlackTree node
     *
     * There are two types of nodes: {@link ValueNode} and {@link Empty}
     */
    interface Node<T> extends RedBlackTree<T> {
        Node<T> right();
        Node<T> left();
        T value();
        Empty<T> emptyInstance();
        int blackHeight();
        Node<T> color(Color color);
        Node<T> insert(T value);
        Node<T> delete(T value);
        boolean isGreaterThan(T from, boolean fromInclusive);
        boolean isLessThan(T to, boolean toInclusive);
        Option<Node<T>> floor(T value, boolean inclusive);
        Option<Node<T>> ceiling(T value, boolean inclusive);
        Option<Node<T>> minNode();
        Option<Node<T>> maxNode();
        NodePath<T> findMinNodePath();
        NodePath<T> findMaxNodePath();
        NodePath<T> findFloorNodePath(T value, boolean inclusive);
        NodePath<T> findCeilingNodePath(T value, boolean inclusive);
    }

    /**
     * A non-empty tree node.
     *
     * @param <T> Component type
     */
    final class ValueNode<T> implements Node<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Color color;
        final int blackHeight;
        final Node<T> left;
        final T value;
        final Node<T> right;
        final Empty<T> empty;
        final int size;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        ValueNode(Color color, int blackHeight, Node<T> left, T value, Node<T> right, Empty<T> empty) {
            this.color = color;
            this.blackHeight = blackHeight;
            this.left = left;
            this.value = value;
            this.right = right;
            this.empty = empty;
            this.size = left.size() + right.size() + 1;
        }

        @Override
        public Color color() {
            return color;
        }

        @Override
        public Comparator<T> comparator() {
            return empty.comparator;
        }

        @Override
        public Node<T> right() {
            return right;
        }

        @Override
        public Node<T> left() {
            return left;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public int blackHeight() {
            return blackHeight;
        }

        @Override
        public boolean contains(T value) {
            final int result = empty.comparator.compare(value, this.value);
            if (result < 0) {
                return left.contains(value);
            } else if (result > 0) {
                return right.contains(value);
            } else {
                return true;
            }
        }

        @Override
        public Empty<T> emptyInstance() {
            return empty;
        }

        @Override
        public Option<T> find(T value) {
            Node<T> current = this;
            while (!current.isEmpty()) {
                final int result = empty.comparator.compare(value, current.value());
                if (result < 0) {
                    current = current.left();
                }
                else if (result > 0) {
                    current = current.right();
                }
                else {
                    return Option.some(current.value());
                }
            }
            return Option.none();
        }

        @Override
        public Option<T> floor(T value) {
            return floor(value, true).map(Node::value);
        }

        @Override
        public Option<T> ceiling(T value) {
            return ceiling(value, true).map(Node::value);
        }

        @Override
        public Option<T> lower(T value) {
            return floor(value, false).map(Node::value);
        }

        @Override
        public Option<T> higher(T value) {
            return ceiling(value, false).map(Node::value);
        }

        @Override
        public Option<Node<T>> floor(T value, boolean inclusive) {
            Node<T> candidate = null;
            Node<T> current = this;
            while (!current.isEmpty()) {
                final int result = empty.comparator.compare(value, current.value());
                if (result < 0 || (!inclusive && result == 0)) {
                    current = current.left();
                }
                else if (result > 0) {
                    candidate = current;
                    current = current.right();
                }
                else {
                    return Option.some(current);
                }
            }
            return candidate == null ? Option.none() : Option.some(candidate);
        }

        @Override
        public Option<Node<T>> ceiling(T value, boolean inclusive) {
            Node<T> candidate = null;
            Node<T> current = this;
            while (!current.isEmpty()) {
                final int result = empty.comparator.compare(value, current.value());
                if (result > 0 || (!inclusive && result == 0)) {
                    current = current.right();
                }
                else if (result < 0) {
                    candidate = current;
                    current = current.left();
                }
                else {
                    return Option.some(current);
                }
            }
            return candidate == null ? Option.none() : Option.some(candidate);
        }

        public NodePath<T> findCeilingNodePath(T value, boolean inclusive) {
            java.util.List<Node<T>> workPath = new ArrayList<>();
            java.util.List<Node<T>> path = new ArrayList<>();
            Node<T> current = this;
            while (!current.isEmpty()) {
                workPath.add(current);
                final int result = empty.comparator.compare(value, current.value());
                if (result > 0 || (!inclusive && result == 0)) {
                    current = current.right();
                }
                else if (result < 0) {
                    current = current.left();
                    path.addAll(workPath);
                    workPath.clear();
                }
                else {
                    path.addAll(workPath);
                    return NodePath.of(path);
                }
            }
            return NodePath.of(path);
        }

        public NodePath<T> findFloorNodePath(T value, boolean inclusive) {
            java.util.List<Node<T>> workPath = new ArrayList<>();
            java.util.List<Node<T>> path = new ArrayList<>();
            Node<T> current = this;
            while (!current.isEmpty()) {
                workPath.add(current);
                final int result = empty.comparator.compare(value, current.value());
                if (result < 0 || (!inclusive && result == 0)) {
                    current = current.left();
                }
                else if (result > 0) {
                    current = current.right();
                    path.addAll(workPath);
                    workPath.clear();
                }
                else {
                    path.addAll(workPath);
                    return NodePath.of(path);
                }
            }
            return NodePath.of(path);
        }

        public NodePath<T> findMinNodePath() {
            java.util.List<Node<T>> path = new ArrayList<>();
            Node<T> current = this;
            path.add(current);
            while (!current.left().isEmpty()) {
                current = current.left();
                path.add(current);
            }
            return NodePath.of(path);
        }

        public NodePath<T> findMaxNodePath() {
            java.util.List<Node<T>> path = new ArrayList<>();
            Node<T> current = this;
            path.add(current);
            while (!current.right().isEmpty()) {
                current = current.right();
                path.add(current);
            }
            return NodePath.of(path);
        }

        @Override
        public Option<T> max() {
            Node<T> curr = this;
            while (!curr.right().isEmpty()) {
                curr = curr.right();
            }
            return Option.some(curr.value());
        }

        public Option<Node<T>> maxNode() {
            Node<T> curr = this;
            while (!curr.right().isEmpty()) {
                curr = curr.right();
            }
            return Option.some(curr);
        }

        @Override
        public Option<T> min() {
            Node<T> curr = this;
            while (!curr.left().isEmpty()) {
                curr = curr.left();
            }
            return Option.some(curr.value());
        }

        @Override
        public Option<Node<T>> minNode() {
            Node<T> curr = this;
            while (!curr.left().isEmpty()) {
                curr = curr.left();
            }
            return Option.some(curr);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Node<T> insert(T value) {
            return ValueNode.insert(this, value).color(BLACK);
        }

        @Override
        public Node<T> delete(T value) {
            final Node<T> tree = ValueNode.delete(this, value)._1;
            return ValueNode.color(tree, BLACK);
        }

        @Override
        public RedBlackTree<T> difference(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            if (tree.isEmpty()) {
                return this;
            } else {
                final Node<T> that = (Node<T>) tree;
                final Tuple2<Node<T>, Node<T>> split = ValueNode.split(this, that.value());
                return ValueNode.merge(split._1.difference(that.left()), split._2.difference(that.right()));
            }
        }

        @Override
        public RedBlackTree<T> intersection(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            if (tree.isEmpty()) {
                return tree;
            } else {
                final Node<T> that = (Node<T>) tree;
                final Tuple2<Node<T>, Node<T>> split = ValueNode.split(this, that.value());
                if (contains(that.value())) {
                    return ValueNode.join(split._1.intersection(that.left()), that.value(), split._2.intersection(that.right()));
                } else {
                    return ValueNode.merge(split._1.intersection(that.left()), split._2.intersection(that.right()));
                }
            }
        }

        @Override
        public RedBlackTree<T> union(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            if (tree.isEmpty()) {
                return this;
            } else {
                final Node<T> that = (Node<T>) tree;
                final Tuple2<Node<T>, Node<T>> split = ValueNode.split(this, that.value());
                return ValueNode.join(split._1.union(that.left()), that.value(), split._2.union(that.right()));
            }
        }

        @Override
        public RedBlackTree<T> subTree(Option<T> fromStart, boolean fromInclusive, Option<T> toEnd, boolean toInclusive) {
            return ascendingSubTree(this, fromStart, fromInclusive, toEnd, toInclusive);
        }

        @Override
        public RedBlackTree<T> descendingTree() {
            return descendingSubTree(this, true, null, true, true, null, true);
        }

        @Override
        public Iterator<T> iterator() {
            return new StandardIterator<>(this);
        }

        static class StandardIterator<T> extends AbstractIterator<T> {
            Deque<Node<T>> stack = new ArrayDeque<>();

            StandardIterator(Node<T> node) {
                pushLeftChildren(node);
            }

            @Override
             public boolean hasNext() {
                 return !stack.isEmpty();
             }

             @Override
             public T getNext() {
                 final Node<T> node = stack.pop();
                 if (!node.right().isEmpty()) {
                     pushLeftChildren(node.right());
                 }
                 return node.value();
             }

             private void pushLeftChildren(Node<T> that) {
                 Node<T> tree = that;
                 while (!tree.isEmpty()) {
                     final Node<T> node = tree;
                     stack.push(node);
                     tree = node.left();
                 }
             }
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ValueNode) {
                final ValueNode<?> that = (ValueNode<?>) o;
                return Collections.equals(this, that);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            // DEV-NOTE: Using `Objects.hash(this.value, this.left, this.right)` would leak the tree structure to the outside.
            //           We just want to hash the values in the right order.
            return Collections.hash(this);
        }

        @Override
        public String toString() {
            return color + ":" + value;
        }

        @Override
        public String toLispString() {
            return isLeaf() ? "(" + color + ":" + value + ")" : toLispString(this);
        }

        private static String toLispString(RedBlackTree<?> tree) {
            if (tree.isEmpty()) {
                return "";
            } else {
                final ValueNode<?> node = (ValueNode<?>) tree;
                final String value = node.color + ":" + node.value;
                if (node.isLeaf()) {
                    return value;
                } else {
                    final String left = node.left.isEmpty() ? "" : " " + toLispString(node.left);
                    final String right = node.right.isEmpty() ? "" : " " + toLispString(node.right);
                    return "(" + value + left + right + ")";
                }
            }
        }

        @Override
        public boolean isLeaf() {
            return left.isEmpty() && right.isEmpty();
        }

        @Override
        public boolean isView() {
            return false;
        }

        @Override
        public Node<T> color(Color color) {
            return (this.color == color) ? this : new ValueNode<>(color, blackHeight, left, value, right, empty);
        }

        static <T> Node<T> color(Node<T> tree, Color color) {
            return tree.isEmpty() ? tree : tree.color(color);
        }

        public boolean isGreaterThan(T from, boolean fromInclusive) {
            int fromResult = comparator().compare(this.value, from);
            return fromInclusive ? fromResult >= 0 : fromResult > 0;
        }

        public boolean isLessThan(T to, boolean toInclusive) {
            int toResult = comparator().compare(this.value, to);
            return toInclusive ? toResult <= 0 : toResult < 0;
        }

        private static <T> ValueNode<T> balanceLeft(Color color, int blackHeight, Node<T> left, T value,
                                                    Node<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!left.isEmpty()) {
                    if (left.color() == RED) {
                        if (!left.left().isEmpty()) {
                            final Node<T> lln = left.left();
                            if (lln.color() == RED) {
                                final Node<T> newLeft = new ValueNode<>(BLACK, blackHeight, lln.left(), lln.value(), lln.right(),
                                        empty);
                                final Node<T> newRight = new ValueNode<>(BLACK, blackHeight, left.right(), value, right, empty);
                                return new ValueNode<>(RED, blackHeight + 1, newLeft, left.value(), newRight, empty);
                            }
                        }
                        if (!left.right().isEmpty()) {
                            final Node<T> lrn = left.right();
                            if (lrn.color() == RED) {
                                final Node<T> newLeft = new ValueNode<>(BLACK, blackHeight, left.left(), left.value(), lrn.left(),
                                        empty);
                                final Node<T> newRight = new ValueNode<>(BLACK, blackHeight, lrn.right(), value, right, empty);
                                return new ValueNode<>(RED, blackHeight + 1, newLeft, lrn.value(), newRight, empty);
                            }
                        }
                    }
                }
            }
            return new ValueNode<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Node<T> balanceRight(Color color, int blackHeight, Node<T> left, T value,
                                                     Node<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!right.isEmpty()) {
                    if (right.color() == RED) {
                        if (!right.right().isEmpty()) {
                            final Node<T> rrn = right.right();
                            if (rrn.color() == RED) {
                                final ValueNode<T> newLeft = new ValueNode<>(BLACK, blackHeight, left, value, right.left(), empty);
                                final ValueNode<T> newRight = new ValueNode<>(BLACK, blackHeight, rrn.left(), rrn.value(), rrn.right(),
                                        empty);
                                return new ValueNode<>(RED, blackHeight + 1, newLeft, right.value(), newRight, empty);
                            }
                        }
                        if (!right.left().isEmpty()) {
                            final Node<T> rln = right.left();
                            if (rln.color() == RED) {
                                final Node<T> newLeft = new ValueNode<>(BLACK, blackHeight, left, value, rln.left(), empty);
                                final Node<T> newRight = new ValueNode<>(BLACK, blackHeight, rln.right(), right.value(), right.right(),
                                        empty);
                                return new ValueNode<>(RED, blackHeight + 1, newLeft, rln.value(), newRight, empty);
                            }
                        }
                    }
                }
            }
            return new ValueNode<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Tuple2<? extends Node<T>, Boolean> blackify(Node<T> tree) {
            if (tree instanceof ValueNode) {
                final ValueNode<T> node = (ValueNode<T>) tree;
                if (node.color == RED) {
                    return Tuple.of(node.color(BLACK), false);
                }
            }
            return Tuple.of(tree, true);
        }

        private static <T> Tuple2<? extends Node<T>, Boolean> delete(Node<T> node, T value) {
            if (node.isEmpty()) {
                return Tuple.of(node, false);
            } else {
                final int comparison = node.comparator().compare(value, node.value());
                if (comparison < 0) {
                    final Tuple2<? extends Node<T>, Boolean> deleted = delete(node.left(), value);
                    final Node<T> deletedNode = deleted._1;
                    final boolean isDeleted = deleted._2;
                    if (isDeleted) {
                        return ValueNode.unbalancedRight(node.color(), node.blackHeight() - 1, deletedNode, node.value(), node.right(), node.emptyInstance());
                    } else {
                        final ValueNode<T> newNode = new ValueNode<>(node.color(), node.blackHeight(), deletedNode, node.value(), node.right(), node.emptyInstance());
                        return Tuple.of(newNode, false);
                    }
                } else if (comparison > 0) {
                    final Tuple2<? extends Node<T>, Boolean> deleted = delete(node.right(), value);
                    final Node<T> deletedNode = deleted._1;
                    final boolean isDeleted = deleted._2;
                    if (isDeleted) {
                        return ValueNode.unbalancedLeft(node.color(), node.blackHeight() - 1, node.left(), node.value(), deletedNode, node.emptyInstance());
                    } else {
                        final ValueNode<T> newNode = new ValueNode<>(node.color(), node.blackHeight(), node.left(), node.value(), deletedNode, node.emptyInstance());
                        return Tuple.of(newNode, false);
                    }
                } else {
                    if (node.right().isEmpty()) {
                        if (node.color() == BLACK) {
                            return blackify(node.left());
                        } else {
                            return Tuple.of(node.left(), false);
                        }
                    } else {
                        final Node<T> nodeRight = node.right();
                        final Tuple3<? extends Node<T>, Boolean, T> newRight = deleteMin(nodeRight);
                        final Node<T> deletedNode = newRight._1;
                        final boolean isDeleted = newRight._2;
                        final T deletedValue = newRight._3;
                        if (isDeleted) {
                            return ValueNode.unbalancedLeft(node.color(), node.blackHeight() - 1, node.left(), deletedValue, deletedNode, node.emptyInstance());
                        } else {
                            final Node<T> newNode = new ValueNode<>(node.color(), node.blackHeight(), node.left(), deletedValue, deletedNode, node.emptyInstance());
                            return Tuple.of(newNode, false);
                        }
                    }
                }
            }
        }

        private static <T> Tuple3<? extends Node<T>, Boolean, T> deleteMin(Node<T> node) {
            if (node.left().isEmpty()) {
                if (node.color() == BLACK) {
                    if (node.right().isEmpty()) {
                        return Tuple.of(node.emptyInstance(), true, node.value());
                    } else {
                        final Node<T> rightNode = node.right();
                        return Tuple.of(rightNode.color(BLACK), false, node.value());
                    }
                } else {
                    return Tuple.of(node.right(), false, node.value());
                }
            } else {
                final Node<T> nodeLeft = node.left();
                final Tuple3<? extends Node<T>, Boolean, T> newNode = deleteMin(nodeLeft);
                final Node<T> deletedNode = newNode._1;
                final boolean isDeleted = newNode._2;
                final T deletedValue = newNode._3;
                if (isDeleted) {
                    final Tuple2<Node<T>, Boolean> tD = ValueNode.unbalancedRight(node.color(), node.blackHeight() - 1, deletedNode,
                            node.value(), node.right(), node.emptyInstance());
                    return Tuple.of(tD._1, tD._2, deletedValue);
                } else {
                    final Node<T> tD = new ValueNode<>(node.color(), node.blackHeight(), deletedNode, node.value(), node.right(), node.emptyInstance());
                    return Tuple.of(tD, false, deletedValue);
                }
            }
        }

        private static <T> Node<T> insert(Node<T> node, T value) {
            if (node.isEmpty()) {
                final Empty<T> empty = (Empty<T>) node;
                return new ValueNode<>(RED, 1, empty, value, empty, empty);
            } else {
                final int comparison = node.comparator().compare(value, node.value());
                if (comparison < 0) {
                    final Node<T> newLeft = insert(node.left(), value);
                    return (newLeft == node.left())
                            ? node
                            : ValueNode.balanceLeft(node.color(), node.blackHeight(), newLeft, node.value(), node.right(), node.emptyInstance());
                } else if (comparison > 0) {
                    final Node<T> newRight = insert(node.right(), value);
                    return (newRight == node.right())
                            ? node
                            : ValueNode.balanceRight(node.color(), node.blackHeight(), node.left(), node.value(), newRight, node.emptyInstance());
                } else {
                    // DEV-NOTE: Even if there is no _comparison_ difference, the object may not be _equal_.
                    //           To save an equals() call, which may be expensive, we return a new instance.
                    return new ValueNode<>(node.color(), node.blackHeight(), node.left(), value, node.right(), node.emptyInstance());
                }
            }
        }

        private static boolean isRed(RedBlackTree<?> tree) {
            return !tree.isEmpty() && ((ValueNode<?>) tree).color == RED;
        }

        static <T> RedBlackTree<T> join(RedBlackTree<T> t1, T value, RedBlackTree<T> t2) {
            if (t1.isEmpty()) {
                return t2.insert(value);
            } else if (t2.isEmpty()) {
                return t1.insert(value);
            } else {
                final ValueNode<T> n1 = (ValueNode<T>) t1;
                final ValueNode<T> n2 = (ValueNode<T>) t2;
                final int comparison = n1.blackHeight - n2.blackHeight;
                if (comparison < 0) {
                    return ValueNode.joinLT(n1, value, n2, n1.blackHeight).color(BLACK);
                } else if (comparison > 0) {
                    return ValueNode.joinGT(n1, value, n2, n2.blackHeight).color(BLACK);
                } else {
                    return new ValueNode<>(BLACK, n1.blackHeight + 1, n1, value, n2, n1.empty);
                }
            }
        }

        private static <T> Node<T> joinGT(Node<T> n1, T value, Node<T> n2, int h2) {
            if (n1.blackHeight() == h2) {
                return new ValueNode<>(RED, h2 + 1, n1, value, n2, n1.emptyInstance());
            } else {
                final Node<T> node = joinGT(n1.right(), value, n2, h2);
                return ValueNode.balanceRight(n1.color(), n1.blackHeight(), n1.left(), n1.value(), node, n2.emptyInstance());
            }
        }

        private static <T> ValueNode<T> joinLT(ValueNode<T> n1, T value, ValueNode<T> n2, int h1) {
            if (n2.blackHeight == h1) {
                return new ValueNode<>(RED, h1 + 1, n1, value, n2, n1.empty);
            } else {
                final ValueNode<T> node = joinLT(n1, value, (ValueNode<T>) n2.left, h1);
                return ValueNode.balanceLeft(n2.color, n2.blackHeight, node, n2.value, n2.right, n2.empty);
            }
        }

        private static <T> RedBlackTree<T> merge(RedBlackTree<T> t1, RedBlackTree<T> t2) {
            if (t1.isEmpty()) {
                return t2;
            } else {
                if (t2.isEmpty()) {
                    return t1;
                }
                else {
                    ValueNode<T> firstNode = (ValueNode<T>)t1;
                    ValueNode<T> secondNode = (ValueNode<T>)t2;
                    final int comparison = firstNode.blackHeight() - secondNode.blackHeight();
                    if (comparison < 0) {
                        final Node<T> node = ValueNode.mergeLT(firstNode, secondNode, firstNode.blackHeight());
                        return ValueNode.color(node, BLACK);
                    }
                    else if (comparison > 0) {
                        final Node<T> node = ValueNode.mergeGT(firstNode, secondNode, secondNode.blackHeight());
                        return ValueNode.color(node, BLACK);
                    }
                    else {
                        final Node<T> node = ValueNode.mergeEQ(firstNode, secondNode);
                        return ValueNode.color(node, BLACK);
                    }
                }
            }
        }

        private static <T> Node<T> mergeEQ(Node<T> n1, Node<T> n2) {
            final T m = n2.min().get();
            final Node<T> t2 = ValueNode.deleteMin(n2)._1;
            final int h2 = t2.isEmpty() ? 0 : ((ValueNode<T>) t2).blackHeight;
            if (n1.blackHeight() == h2) {
                return new ValueNode<>(RED, n1.blackHeight() + 1, n1, m, t2, n1.emptyInstance());
            } else if (isRed(n1.left())) {
                final ValueNode<T> node = new ValueNode<>(BLACK, n1.blackHeight(), n1.right(), m, t2, n1.emptyInstance());
                return new ValueNode<>(RED, n1.blackHeight(), ValueNode.color(n1.left(), BLACK), n1.value(), node, n1.emptyInstance());
            } else if (isRed(n1.right())) {
                final Node<T> rl = ((ValueNode<T>) n1.right()).left;
                final T rx = ((ValueNode<T>) n1.right()).value;
                final Node<T> rr = ((ValueNode<T>) n1.right()).right;
                final ValueNode<T> left = new ValueNode<>(RED, n1.blackHeight(), n1.left(), n1.value(), rl, n1.emptyInstance());
                final ValueNode<T> right = new ValueNode<>(RED, n1.blackHeight(), rr, m, t2, n1.emptyInstance());
                return new ValueNode<>(BLACK, n1.blackHeight(), left, rx, right, n1.emptyInstance());
            } else {
                return new ValueNode<>(BLACK, n1.blackHeight(), n1.color(RED), m, t2, n1.emptyInstance());
            }
        }

        private static <T> Node<T> mergeGT(Node<T> n1, Node<T> n2, int h2) {
            if (n1.blackHeight() == h2) {
                return ValueNode.mergeEQ(n1, n2);
            } else {
                final Node<T> node = ValueNode.mergeGT(n1.right(), n2, h2);
                return ValueNode.balanceRight(n1.color(), n1.blackHeight(), n1.left(), n1.value(), node, n1.emptyInstance());
            }
        }

        private static <T> Node<T> mergeLT(Node<T> n1, Node<T> n2, int h1) {
            if (n2.blackHeight() == h1) {
                return ValueNode.mergeEQ(n1, n2);
            } else {
                final Node<T> node = ValueNode.mergeLT(n1, n2.left(), h1);
                return ValueNode.balanceLeft(n2.color(), n2.blackHeight(), node, n2.value(), n2.right(), n2.emptyInstance());
            }
        }

        static <T> Tuple2<Node<T>, Node<T>> split(Node<T> tree, T value) {
            if (tree.isEmpty()) {
                return Tuple.of(tree, tree);
            } else {
                final int comparison = tree.comparator().compare(value, tree.value());
                if (comparison < 0) {
                    final Tuple2<Node<T>, Node<T>> split = ValueNode.split(tree.left(), value);
                    return Tuple.of(split._1, (Node<T>)ValueNode.join(split._2, tree.value(), ValueNode.color(tree.right(), BLACK)));
                } else if (comparison > 0) {
                    final Tuple2<Node<T>, Node<T>> split = ValueNode.split(tree.right(), value);
                    return Tuple.of((Node<T>)ValueNode.join(ValueNode.color(tree.left(), BLACK), tree.value(), split._1), split._2);
                } else {
                    return Tuple.of(ValueNode.color(tree.left(), BLACK), ValueNode.color(tree.right(), BLACK));
                }
            }
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedLeft(Color color, int blackHeight, Node<T> left,
                                                                        T value, Node<T> right, Empty<T> empty) {
            if (!left.isEmpty()) {
                final ValueNode<T> ln = (ValueNode<T>) left;
                if (ln.color == BLACK) {
                    final Node<T> newNode = ValueNode.balanceLeft(BLACK, blackHeight, ln.color(RED), value, right, empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !ln.right.isEmpty()) {
                    final ValueNode<T> lrn = (ValueNode<T>) ln.right;
                    if (lrn.color == BLACK) {
                        final Node<T> newRightNode = ValueNode.balanceLeft(BLACK, blackHeight, lrn.color(RED), value, right,
                                empty);
                        final Node<T> newNode = new ValueNode<>(BLACK, ln.blackHeight, ln.left, ln.value, newRightNode,
                                empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException("unbalancedLeft(" + color + ", " + blackHeight + ", " + left + ", " + value + ", " + right + ")");
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedRight(Color color, int blackHeight, Node<T> left,
                                                                         T value, Node<T> right, Empty<T> empty) {
            if (!right.isEmpty()) {
                if (right.color() == BLACK) {
                    final Node<T> newNode = ValueNode.balanceRight(BLACK, blackHeight, left, value, right.color(RED), empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !right.left().isEmpty()) {
                    final Node<T> rln = right.left();
                    if (rln.color() == BLACK) {
                        final Node<T> newLeftNode = ValueNode.balanceRight(BLACK, blackHeight, left, value, rln.color(RED), empty);
                        final Node<T> newNode = new ValueNode<>(BLACK, right.blackHeight(), newLeftNode, right.value(), right.right(), empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException("unbalancedRight(" + color + ", " + blackHeight + ", " + left + ", " + value + ", " + right + ")");
        }
    }

    /**
     * The empty tree node. It can't be a singleton because it depends on a {@link Comparator}.
     *
     * @param <T> Component type
     */
    final class Empty<T> implements Node<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Comparator<T> comparator;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        @SuppressWarnings("unchecked")
        Empty(Comparator<? super T> comparator) {
            this.comparator = (Comparator<T>) comparator;
        }

        @Override
        public Color color() {
            return BLACK;
        }

        @Override
        public Comparator<T> comparator() {
            return comparator;
        }

        @Override
        public boolean contains(T value) {
            return false;
        }

        @Override
        public Empty<T> emptyInstance() {
            return this;
        }

        @Override
        public Option<T> find(T value) {
            return Option.none();
        }

        @Override
        public Option<T> floor(T value) {
            return Option.none();
        }

        @Override
        public Option<T> ceiling(T value) {
            return Option.none();
        }

        @Override
        public Option<T> higher(T value) {
            return Option.none();
        }

        @Override
        public Option<T> lower(T value) {
            return Option.none();
        }

        @Override
        public Option<T> max() {
            return Option.none();
        }

        @Override
        public Option<T> min() {
            return Option.none();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public boolean isView() {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Node<T> right() {
            return emptyInstance();
        }

        @Override
        public Node<T> left() {
            return emptyInstance();
        }

        @Override
        public T value() {
            throw new UnsupportedOperationException("Empty node does not have any value");
        }

        @Override
        public int blackHeight() {
            return 0;
        }

        @Override
        public Node<T> color(Color color) {
            return this;
        }

        @Override
        public boolean isGreaterThan(T from, boolean fromInclusive) {
            return false;
        }

        @Override
        public boolean isLessThan(T to, boolean toInclusive) {
            return false;
        }

        @Override
        public NodePath<T> findMinNodePath() {
            return NodePath.empty();
        }

        @Override
        public NodePath<T> findMaxNodePath() {
            return NodePath.empty();
        }

        @Override
        public NodePath<T> findFloorNodePath(T value, boolean inclusive) {
            return NodePath.empty();
        }

        @Override
        public NodePath<T> findCeilingNodePath(T value, boolean inclusive) {
            return NodePath.empty();
        }

        @Override
        public Option<Node<T>> floor(T value, boolean inclusive) {
            return Option.none();
        }

        @Override
        public Option<Node<T>> ceiling(T value, boolean inclusive) {
            return Option.none();
        }

        @Override
        public Option<Node<T>> minNode() {
            return Option.none();
        }

        @Override
        public Option<Node<T>> maxNode() {
            return Option.none();
        }

        @Override
        public Node<T> insert(T value) {
            return new ValueNode<>(BLACK, 1, this, value, this, this);
        }

        @Override
        public Node<T> delete(T value) {
            return this;
        }

        @Override
        public RedBlackTree<T> difference(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            return this;
        }

        @Override
        public RedBlackTree<T> intersection(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            return this;
        }

        @Override
        public RedBlackTree<T> union(RedBlackTree<T> tree) {
            Objects.requireNonNull(tree, "tree is null");
            if (tree.isEmpty()) {
                return this;
            }
            else {
                final ValueNode<T> that = (ValueNode<T>) tree;
                return that.color(BLACK);
            }
        }

        @Override
        public RedBlackTree<T> subTree(Option<T> fromStart, boolean fromInclusive, Option<T> toEnd, boolean toInclusive) {
            return this;
        }

        @Override
        public RedBlackTree<T> descendingTree() {
            return this;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterator.empty();
        }

        @Override
        public boolean equals(Object o) {
            // note: it is not possible to compare the comparators because function equality is not computable
            return (o == this) || (o instanceof Empty);
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public String toLispString() {
            return "()";
        }
    }

    final class NodePath<T> {
        // List of nodes representing this path. The head is the leaf, and the last item is the root
        private java.util.List<Node<T>> nodesFromRoot;

        static <T> NodePath<T> empty() {
            return new NodePath<>(List.empty());
        }

        static <T> NodePath<T> of(java.util.List<Node<T>> nodesFromRoot) {
            return new NodePath<>(nodesFromRoot);
        }

        private NodePath(List<Node<T>> nodesStack) {
            nodesFromRoot = nodesStack.reverse().toJavaList();
        }

        private NodePath(java.util.List<Node<T>> nodesFromRoot) {
            this.nodesFromRoot = nodesFromRoot;
        }

        public boolean isEmpty() {
            return nodesFromRoot.isEmpty();
        }

        Deque<Node<T>> getFilteredStack(boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                        boolean toUnbounded, T toEnd, boolean toInclusive) {
            Deque<Node<T>> stack = new ArrayDeque<>();
            nodesFromRoot.stream()
                    .filter((n -> (fromUnbounded || n.isGreaterThan(fromStart, fromInclusive))
                                 && (toUnbounded || n.isLessThan(toEnd, toInclusive))))
                    .forEach(stack::push);
            return stack;
        }
    }

    interface SubViewModule {
        /**
         * This is the base class for Tree views.
         * <p>
         * An {@code AbstractSubTreeView} consists of a node and a range that delimits the nodes visible to the view.
         * <br>
         * The range comprises two endpoints, namely {@code fromStart} and {@code toEnd}. Each endpoint is defined by three properties.
         *
         * These properties define the start endpoint:
         * <ul>
         *     <li>{@code fromStart} -     The lowest element that should be included in the list, or {@code null} if
         *                                 the view is unbounded on the low end.</li>
         *     <li>{@code fromInclusive} - If {@code true} then the {@code fromStart} element is also included.
         *                                 If {@code false} then the {@code fromStart} element is excluded from the view.</li>
         *     <li>{@code fromUnbounded} - If {@code true} then the start endpoint is unbounded, and the view will include
         *                                 all elements lower than {@code toEnd}. When the start endpoint is unbounded,
         *                                 {@code fromStart} and {@code fromInclusive} are ignored.</li>
         * </ul>
         *
         * Similarly, these properties define the end endpoint:
         * <ul>
         *     <li>{@code toEnd} -       The highest element that should be included in the list, or {@code null} if
         *                               the view is unbounded on the high end.</li>
         *     <li>{@code toInclusive} - If {@code true} then the {@code toEnd} element is also included.
         *                               If {@code false} then the {@code toEnd} element is excluded from the view.</li>
         *     <li>{@code toUnbounded} - If {@code true} then the end endpoint is unbounded, and the view will include
         *                                 all elements higher than {@code fromStart}. When the end endpoint is unbounded,
         *                                 {@code toEnd} and {@code toInclusive} are ignored.</li>
         * </ul>
         */
        abstract class AbstractSubTreeView<T> implements RedBlackTree<T>, Serializable {
            private static final long serialVersionUID = 1L;
            final Node<T> node;
            final boolean fromUnbounded;
            final boolean fromInclusive;
            final T fromStart;
            final boolean toUnbounded;
            final boolean toInclusive;
            final T toEnd;

            private AbstractSubTreeView(Node<T> node,
                                        boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                        boolean toUnbounded, T toEnd, boolean toInclusive) {
                this.node = node;
                this.fromUnbounded = fromUnbounded;
                this.fromStart = fromStart;
                this.fromInclusive = fromInclusive;
                this.toUnbounded = toUnbounded;
                this.toEnd = toEnd;
                this.toInclusive = toInclusive;
            }

            protected abstract RedBlackTree<T> cloneWithNewRoot(Node<T> newRoot);

            final boolean inRange(T value) {
                return !isTooLow(value) && !isTooHigh(value);
            }

            final boolean isTooHigh(T value) {
                if (toUnbounded) {
                    return false;
                }
                int res = node.comparator().compare(value, toEnd);
                return res > 0 || (res == 0 && !toInclusive);
            }

            final boolean isTooLow(T value) {
                if (fromUnbounded) {
                    return false;
                }
                int res = node.comparator().compare(value, fromStart);
                return res < 0 || (res == 0 && !fromInclusive);
            }

            @Override
            public Color color() {
                return node.color();
            }

            @Override
            public boolean contains(T value) {
                if (!inRange(value)) {
                    return false;
                }
                return node.contains(value);
            }

            @Override
            public RedBlackTree<T> emptyInstance() {
                return node.emptyInstance();
            }

            @Override
            public Option<T> find(T value) {
                if (!inRange(value)) {
                    return Option.none();
                }
                return node.find(value);
            }

            @Override
            public boolean isEmpty() {
                return iterator().isEmpty();
            }

            @Override
            public boolean isLeaf() {
                return node.isLeaf();
            }

            @Override
            public boolean isView() {
                return true;
            }

            @Override
            public int size() {
                return iterator().size();
            }

            final Option<T> absFloor(T value) {
                if (isTooHigh(value)) {
                    return absMax();
                }
                Option<T> floor = node.floor(value);
                if (floor.isDefined() && isTooLow(floor.get())) {
                    floor = Option.none();
                }
                return floor;
            }

            final Option<T> absCeiling(T value) {
                if (isTooLow(value)) {
                    return absMin();
                }
                Option<T> ceiling = node.ceiling(value);
                if (ceiling.isDefined() && isTooHigh(ceiling.get())) {
                    ceiling = Option.none();
                }
                return ceiling;
            }

            final Option<T> absLower(T value) {
                if (isTooHigh(value)) {
                    return absMax();
                }
                Option<T> lower = node.lower(value);
                if (lower.isDefined() && isTooLow(lower.get())) {
                    return Option.none();
                }
                return lower;
            }

            final Option<T> absHigher(T value) {
                if (isTooLow(value)) {
                    return absMin();
                }
                Option<T> higher = node.higher(value);
                if (higher.isDefined() && isTooHigh(higher.get())) {
                    return Option.none();
                }
                return higher;
            }

            final Option<T> absMax() {
                if (toUnbounded) {
                    return node.max();
                }
                Option<T> max = toInclusive ? node.floor(toEnd) : node.lower(toEnd);
                if (max.isDefined() && isTooLow(max.get())) {
                    return Option.none();
                }
                return max;
            }

            final Option<T> absMin() {
                if (fromUnbounded) {
                    return node.min();
                }
                Option<T> min = fromInclusive ? node.ceiling(fromStart) : node.higher(fromStart);
                if (min.isDefined() && isTooHigh(min.get())) {
                    return Option.none();
                }
                return min;
            }


            @Override
            public RedBlackTree<T> insert(T value) {
                if (!inRange(value)) {
                    return RedBlackTree.ofAll(comparator(), iterator()).insert(value);
                }
                return cloneWithNewRoot(node.insert(value));
            }

            @Override
            public RedBlackTree<T> delete(T value) {
                if (!inRange(value)) {
                    return this;
                }
                return cloneWithNewRoot(node.delete(value));
            }

            @Override
            public RedBlackTree<T> difference(RedBlackTree<T> tree) {
                throw new UnsupportedOperationException("Unable to calculate differences in a sub tree");
            }

            @Override
            public RedBlackTree<T> intersection(RedBlackTree<T> tree) {
                throw new UnsupportedOperationException("Unable to calculate intersection in a sub tree");
            }

            @Override
            public RedBlackTree<T> union(RedBlackTree<T> tree) {
                throw new UnsupportedOperationException("Unable to calculate union in a sub tree");
            }

            static <T> void checkFromValueIsLowerOrEqualThanToValue(Comparator<T> comparator, Option<T> from, Option<T> to) {
                if (from.isDefined() && to.isDefined() && comparator.compare(from.get(), to.get()) > 0) {
                    throw new IllegalArgumentException("Unable create view as From value is greater than To value");
                }
            }

            static <T> void checkFromValueIsLowerOrEqualThanToValue(Comparator<T> comparator, boolean fromUnbounded, T fromStart, boolean toUnbounded, T toEnd) {
                if (!fromUnbounded && !toUnbounded && comparator.compare(fromStart, toEnd) > 0) {
                    throw new IllegalArgumentException("Unable create view as From value is greater than To value");
                }
            }

            Tuple2<Option<T>, Boolean> calculateNewViewFromStart(Option<T> newViewFromStart, boolean newViewFromIncluded) {
                if (newViewFromStart.isEmpty()) {
                    return Tuple.of(Option.of(fromStart), fromInclusive);
                }
                if (fromUnbounded) {
                    return Tuple.of(newViewFromStart, newViewFromIncluded);
                }

                int res = comparator().compare(fromStart, newViewFromStart.get());
                if (res < 0 || (res == 0 && !newViewFromIncluded)) {
                    return Tuple.of(newViewFromStart, newViewFromIncluded);
                }
                return Tuple.of(Option.of(fromStart), fromInclusive);
            }

            Tuple2<Option<T>, Boolean> calculateNewViewToEnd(Option<T> newViewToEnd, boolean newViewToIncluded) {
                if (newViewToEnd.isEmpty()) {
                    return Tuple.of(Option.of(toEnd), toInclusive);
                }
                if (toUnbounded) {
                    return Tuple.of(newViewToEnd, newViewToIncluded);
                }

                int res = comparator().compare(newViewToEnd.get(), toEnd);
                if (res < 0 || (res == 0 && !newViewToIncluded)) {
                    return Tuple.of(newViewToEnd, newViewToIncluded);
                }
                return Tuple.of(Option.of(toEnd), toInclusive);
            }

            @Override
            public String toString() {
                return String.format("%s%s, %s%s", fromInclusive ? "[" : "(", min(), max(), toInclusive ? "]" : ")");
            }

            @Override
            public String toLispString() {
                return node.toLispString();
            }
        }

        final class AscendingSubTreeView<T> extends AbstractSubTreeView<T> {
            private static final long serialVersionUID = 1L;

            static <T> RedBlackTree<T> ascendingSubTree(Node<T> node, Option<T> fromStart, boolean fromInclusive, Option<T> toEnd, boolean toInclusive) {
                return ascendingSubTree(node, fromStart.isEmpty(), fromStart.getOrElse((T)null), fromInclusive, toEnd.isEmpty(), toEnd.getOrElse((T)null), toInclusive);
            }

            static <T> RedBlackTree<T> ascendingSubTree(Node<T> node,
                                                        boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                                        boolean toUnbounded, T toEnd, boolean toInclusive) {
                checkFromValueIsLowerOrEqualThanToValue(node.comparator(), fromUnbounded, fromStart, toUnbounded, toEnd);
                return new AscendingSubTreeView<>(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            private AscendingSubTreeView(Node<T> node,
                                         boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                         boolean toUnbounded, T toEnd, boolean toInclusive) {
                super(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            @Override
            protected AscendingSubTreeView<T> cloneWithNewRoot(Node<T> newRoot) {
                return new AscendingSubTreeView<>(newRoot, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            @Override
            public Comparator<T> comparator() {
                return node.comparator();
            }

            @Override
            public Option<T> floor(T value) {
                return absFloor(value);
            }

            @Override
            public Option<T> ceiling(T value) {
                return absCeiling(value);
            }

            @Override
            public Option<T> lower(T value) {
                return absLower(value);
            }

            @Override
            public Option<T> higher(T value) {
                return absHigher(value);
            }

            @Override
            public Option<T> max() {
                return absMax();
            }

            @Override
            public Option<T> min() {
                return absMin();
            }

            @Override
            public RedBlackTree<T> subTree(Option<T> newViewFromStart, boolean newViewFromInclusive, Option<T> newViewToEnd, boolean newViewToInclusive) {
                checkFromValueIsLowerOrEqualThanToValue(comparator(), newViewFromStart, newViewToEnd);

                Tuple2<Option<T>, Boolean> newFrom = calculateNewViewFromStart(newViewFromStart, newViewFromInclusive);
                Tuple2<Option<T>, Boolean> newTo = calculateNewViewToEnd(newViewToEnd, newViewToInclusive);
                if (newFrom._1.isDefined() && newTo._1.isDefined()) {
                    if (comparator().compare(newFrom._1.get(), newTo._1.get()) > 0) {
                        return emptyInstance();
                    }
                }
                return ascendingSubTree(node, newFrom._1, newFrom._2, newTo._1, newTo._2);
            }

            @Override
            public RedBlackTree<T> descendingTree() {
                return descendingSubTree(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            @Override
            public Iterator<T> iterator() {
                return new AscendingSubTreeIterator<>(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            private static class AscendingSubTreeIterator<T> extends  AbstractIterator<T>{
                private final RedBlackTree<T> endNode;
                private Deque<Node<T>> stack;

                AscendingSubTreeIterator(Node<T> node,
                                         boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                         boolean toUnbounded, T toEnd, boolean toInclusive) {
                    Tuple2<Deque<Node<T>>, Node<T>> res = calculateWorkStackAndToNode(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
                    stack = res._1;
                    endNode = res._2;
                }

                private Tuple2<Deque<Node<T>>, Node<T>> calculateWorkStackAndToNode(Node<T> node,
                                                                                    boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                                                                    boolean toUnbounded, T toEnd, boolean toInclusive) {

                    final NodePath<T> startPath = fromUnbounded ? node.findMinNodePath() : node.findCeilingNodePath(fromStart, fromInclusive);
                    final Option<Node<T>> endPath = toUnbounded ? node.maxNode() : node.floor(toEnd, toInclusive);

                    if (startPath.isEmpty() || endPath.isEmpty()) {
                        return Tuple.of(new ArrayDeque<>(), node);
                    }

                    Deque<Node<T>> workStack = startPath.getFilteredStack(fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
                    return Tuple.of(workStack, endPath.get());
                }

                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T getNext() {
                    final Node<T> node = stack.pop();
                    if (node == endNode) {
                        stack.clear();
                    }
                    else if (!node.right().isEmpty()) {
                        pushLeftChildren(node.right());
                    }
                    return node.value();
                }

                private void pushLeftChildren(Node<T> that) {
                    Node<T> node = that;
                    do {
                        stack.push(node);
                        node = node.left();
                    } while (!node.isEmpty());
                }
            }
        }

        final class DescendingSubTreeView<T> extends AbstractSubTreeView<T> {
            private static final long serialVersionUID = 1L;
            private final Comparator<T> reversedComparator;

            static <T> RedBlackTree<T> descendingSubTree(Node<T> node, Option<T> from, boolean fromInclusive, Option<T> to, boolean toInclusive) {
                return descendingSubTree(node, from.isEmpty(), from.getOrElse((T)null), fromInclusive, to.isEmpty(), to.getOrElse((T)null), toInclusive);
            }

            static <T> RedBlackTree<T> descendingSubTree(Node<T> node,
                                                         boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                                         boolean toUnbounded, T toEnd, boolean toInclusive) {
                checkFromValueIsLowerOrEqualThanToValue(node.comparator(), fromUnbounded, fromStart, toUnbounded, toEnd);
                Comparator<T> reversedComparator = node.comparator().reversed();
                return new DescendingSubTreeView<>(reversedComparator, node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            private DescendingSubTreeView(Comparator<T> reversedComparator, Node<T> node,
                                          boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                          boolean toUnbounded, T toEnd, boolean toInclusive) {
                super(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
                this.reversedComparator = reversedComparator;
            }

            @Override
            protected DescendingSubTreeView<T> cloneWithNewRoot(Node<T> newRoot) {
                return new DescendingSubTreeView<>(comparator(), newRoot, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            @Override
            public Comparator<T> comparator() {
                return reversedComparator;
            }

            @Override
            public Option<T> floor(T value) {
                return absCeiling(value);
            }

            @Override
            public Option<T> ceiling(T value) {
                return absFloor(value);
            }

            @Override
            public Option<T> lower(T value) {
                return absHigher(value);
            }

            @Override
            public Option<T> higher(T value) {
                return absLower(value);
            }

            @Override
            public Option<T> max() {
                return absMin();
            }

            @Override
            public Option<T> min() {
                return absMax();
            }

            @Override
            public RedBlackTree<T> subTree(Option<T> reversedFromStart, boolean fromInclusive, Option<T> reversedToEnd, boolean toInclusive) {
                checkFromValueIsLowerOrEqualThanToValue(reversedComparator, reversedFromStart, reversedToEnd);

                Tuple2<Option<T>, Boolean> absFromStart = calculateNewViewFromStart(reversedToEnd, toInclusive);
                Tuple2<Option<T>, Boolean> absToEnd = calculateNewViewToEnd(reversedFromStart, fromInclusive);
                if (absFromStart._1.isDefined() && absToEnd._1.isDefined()) {
                    if (comparator().compare(absFromStart._1.get(), absToEnd._1.get()) < 0) {
                        return emptyInstance();
                    }
                }
                return descendingSubTree(node, absFromStart._1, absFromStart._2, absToEnd._1, absToEnd._2);
            }

            @Override
            public RedBlackTree<T> descendingTree() {
                return ascendingSubTree(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            @Override
            public Iterator<T> iterator() {
                return new DescendingSubTreeIterator<>(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
            }

            private static class DescendingSubTreeIterator<T> extends  AbstractIterator<T>{
                private final Node<T> endNode;
                private Deque<Node<T>> stack;

                DescendingSubTreeIterator(Node<T> node,
                                            boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                            boolean toUnbounded, T toEnd, boolean toInclusive) {
                    Tuple2<Deque<Node<T>>, Node<T>> res = calculateWorkStackAndToNode(node, fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
                    stack = res._1;
                    endNode = res._2;
                }

                private Tuple2<Deque<Node<T>>, Node<T>> calculateWorkStackAndToNode(Node<T> node,
                                                                                    boolean fromUnbounded, T fromStart, boolean fromInclusive,
                                                                                    boolean toUnbounded, T toEnd, boolean toInclusive) {

                    final NodePath<T> startPath = toUnbounded ? node.findMaxNodePath() : node.findFloorNodePath(toEnd, toInclusive);
                    final Option<Node<T>> endNode = fromUnbounded ? node.minNode() : node.ceiling(fromStart, fromInclusive);

                    if (endNode.isEmpty() || startPath.isEmpty()) {
                        return Tuple.of(new ArrayDeque<>(), node);
                    }

                    Deque<Node<T>> workStack = startPath.getFilteredStack(fromUnbounded, fromStart, fromInclusive, toUnbounded, toEnd, toInclusive);
                    return Tuple.of(workStack, endNode.get());
                }

                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T getNext() {
                    final Node<T> node = stack.pop();
                    if (node == endNode) {
                        stack.clear();
                    }
                    else if (!node.left().isEmpty()) {
                        pushRightChildren(node.left());
                    }
                    return node.value();
                }

                private void pushRightChildren(Node<T> that) {
                    Node<T> node = that;
                    do {
                        stack.push(node);
                        node = node.right();
                    } while (!node.isEmpty());
                }
            }
        }
    }
}
