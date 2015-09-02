/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

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
 */
public interface RedBlackTree<T> {

    static <T extends Comparable<T>> Empty<T> empty() {
        return new Empty<>(T::compareTo);
    }

    static <T> Empty<T> empty(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new Empty<>(comparator);
    }

    static <T extends Comparable<T>> Node<T> of(T value) {
        final Empty<T> empty = empty();
        return new Node<>(BLACK, 1, empty, value, empty, empty);
    }

    static <T> Node<T> of(T value, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final Empty<T> empty = empty(comparator);
        return new Node<>(BLACK, 1, empty, value, empty, empty);
    }

    default Node<T> add(T value) {

        class Util {

            Node<T> insert(RedBlackTree<T> tree) {
                if (tree.isEmpty()) {
                    final Empty<T> empty = (Empty<T>) tree;
                    return new Node<>(RED, 1, empty, value, empty, empty);
                } else {
                    final Node<T> node = (Node<T>) tree;
                    final int comparison = node.empty.comparator.compare(value, node.value);
                    if (comparison < 0) {
                        final RedBlackTree<T> newLeft = insert(node.left);
                        return (newLeft == node.left) ? node : Node.balanceLeft(node.color, node.blackHeight, newLeft, node.value, node.right, node.empty);
                    } else if (comparison > 0) {
                        final RedBlackTree<T> newRight = insert(node.right);
                        return (newRight == node.right) ? node : Node.balanceRight(node.color, node.blackHeight, node.left, node.value, newRight, node.empty);
                    } else {
                        return node;
                    }
                }
            }
        }

        return new Util().insert(this).color(BLACK);
    }

    /**
     * Clears this RedBlackTree.
     *
     * @return An empty ReadBlackTree
     */
    Empty<T> clear();

    /**
     * Checks, if this {@code RedBlackTree} contains the given {@code value}.
     *
     * @param value A value.
     * @return true, if this tree contains the value, false otherwise.
     */
    boolean contains(T value);

    /**
     * The black height of the tree.
     *
     * @return The black hight.
     */
    int blackHeight();

    /**
     * Checks if this {@code RedBlackTree} is empty, i.e. an instance of {@code Leaf}.
     *
     * @return true, if it is empty, false otherwise.
     */
    boolean isEmpty();

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
    @Override
    String toString();

    enum Color {

        RED, BLACK;

        @Override
        public String toString() {
            return (this == RED) ? "R" : "B";
        }
    }

    /**
     * A non-empty tree node.
     *
     * @param <T> Component type
     */
    class Node<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        public final Color color;
        public final int blackHeight;
        public final RedBlackTree<T> left;
        public final T value;
        public final RedBlackTree<T> right;
        public final Empty<T> empty;

        private final transient Lazy<Integer> hashCode;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        private Node(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            this.color = color;
            this.blackHeight = blackHeight;
            this.left = left;
            this.value = value;
            this.right = right;
            this.empty = empty;
            this.hashCode = Lazy.of(() -> Objects.hash(this.value, this.left, this.right));
        }

        @Override
        public Empty<T> clear() {
            return empty;
        }

        @Override
        public boolean contains(T value) {
            final int result = empty.comparator.compare(value, value);
            if (result < 0) {
                return left.contains(value);
            } else if (result > 0) {
                return right.contains(value);
            } else {
                return true;
            }
        }

        @Override
        public int blackHeight() {
            return blackHeight;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Node) {
                final Node<?> that = (Node<?>) o;
                return Objects.equals(this.value, that.value)
                        && this.left.equals(that.left)
                        && this.right.equals(that.right);
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
            return isLeaf() ? "(" + color + ":" + value + ")" : toLispString(this);
        }

        private static String toLispString(RedBlackTree<?> tree) {
            if (tree.isEmpty()) {
                return "";
            } else {
                final Node<?> node = (Node<?>) tree;
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

        private boolean isLeaf() {
            return left.isEmpty() && right.isEmpty();
        }

        private Node<T> color(Color color) {
            return (this.color == color) ? this : new Node<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Node<T> balanceLeft(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!left.isEmpty()) {
                    final Node<T> ln = (Node<T>) left;
                    if (ln.color == RED) {
                        if (!ln.left.isEmpty()) {
                            final Node<T> lln = (Node<T>) ln.left;
                            if (lln.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, lln.left, lln.value, lln.right, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, ln.right, value, right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, ln.value, newRight, empty);
                            }
                        }
                        if (!ln.right.isEmpty()) {
                            final Node<T> lrn = (Node<T>) ln.right;
                            if (lrn.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, ln.left, ln.value, lrn.left, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, lrn.right, value, right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, lrn.value, newRight, empty);
                            }
                        }
                    }
                }
            }
            return new Node<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Node<T> balanceRight(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!right.isEmpty()) {
                    final Node<T> rn = (Node<T>) right;
                    if (rn.color == RED) {
                        if (!rn.right.isEmpty()) {
                            final Node<T> rrn = (Node<T>) rn.right;
                            if (rrn.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, left, value, rn.left, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, rrn.left, rrn.value, rrn.right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, rn.value, newRight, empty);
                            }
                        }
                        if (!rn.left.isEmpty()) {
                            final Node<T> rln = (Node<T>) rn.left;
                            if (rln.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, left, value, rln.left, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, rln.right, rn.value, rn.right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, rln.value, newRight, empty);
                            }
                        }
                    }
                }
            }
            return new Node<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedLeft(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            if (!left.isEmpty()) {
                final Node<T> ln = (Node<T>) left;
                if (ln.color == BLACK) {
                    final Node<T> newNode = Node.balanceLeft(BLACK, blackHeight, ln.color(RED), value, right, empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !ln.right.isEmpty()) {
                    final Node<T> lrn = (Node<T>) ln.right;
                    if (lrn.color == BLACK) {
                        final Node<T> newRightNode = Node.balanceLeft(BLACK, blackHeight, lrn.color(RED), value, right, empty);
                        final Node<T> newNode = new Node<>(BLACK, ln.blackHeight, ln.left, ln.value, newRightNode, empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException(String.format("unbalancedLeft(%s, %s, %s, %s, %s)", color, blackHeight, left, value, right));
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedRight(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            if (!right.isEmpty()) {
                final Node<T> rn = (Node<T>) right;
                if (rn.color == BLACK) {
                    final Node<T> newNode = Node.balanceRight(BLACK, blackHeight, left, value, rn.color(RED), empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !rn.left.isEmpty()) {
                    final Node<T> rln = (Node<T>) rn.left;
                    if (rln.color == BLACK) {
                        final Node<T> newLeftNode = Node.balanceRight(BLACK, blackHeight, left, value, rln.color(RED), empty);
                        final Node<T> newNode = new Node<>(BLACK, rn.blackHeight, newLeftNode, rn.value, rn.right, empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException(String.format("unbalancedRight(%s, %s, %s, %s, %s)", color, blackHeight, left, value, right));
        }
    }

    /**
     * The empty tree node. It can't be a singleton because it depends on a {@link Comparator}.
     *
     * @param <T> Component type
     */
    class Empty<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        public final Comparator<? super T> comparator;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        private Empty(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public Empty<T> clear() {
            return this;
        }

        @Override
        public boolean contains(T value) {
            return false;
        }

        @Override
        public int blackHeight() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof Empty);
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return "()";
        }
    }
}
