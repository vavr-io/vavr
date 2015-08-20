/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;

/**
 * An immutable {@code RedBlackTree} implementation, based on the book "Purely Functional Data Structures" (Okasaki, 2003).
 * <p>
 * RedBlackTrees are typically used to implement sorted sets.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface RedBlackTree<T> /*TODO: extends Iterable<T>*/{

    static <T extends Comparable<T>> EmptyNode<T> empty() {
        return new EmptyNode<>(T::compareTo);
    }

    static <T> EmptyNode<T> empty(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new EmptyNode<>(comparator);
    }

    default TreeNode<T> add(T value) {

        class Util {

            TreeNode<T> insert(RedBlackTree<T> tree) {
                if (tree.isEmpty()) {
                    final EmptyNode<T> empty = (EmptyNode<T>) tree;
                    return new TreeNode<>(RED, empty, value, empty, empty.comparator);
                } else {
                    final TreeNode<T> node = (TreeNode<T>) tree;
                    final int comparison = node.comparator.compare(node.value, value);
                    if (comparison > 0) {
                        final RedBlackTree<T> currentLeft = node.left;
                        final RedBlackTree<T> newLeft = insert(currentLeft);
                        return (newLeft == currentLeft) ? node : TreeNode.lbalance(node.color, newLeft, node.value, node.right, node.comparator);
                    } else if (comparison < 0) {
                        final RedBlackTree<T> currentRight = node.right;
                        final RedBlackTree<T> newRight = insert(currentRight);
                        return (newRight == currentRight) ? node : TreeNode.rbalance(node.color, node.left, node.value, newRight, node.comparator);
                    } else {
                        return node;
                    }
                }
            }
        }

        final TreeNode<T> node = new Util().insert(this);

        return new TreeNode<>(BLACK, node.left, node.value, node.right, node.comparator);
    }

    /**
     * Clears this RedBlackTree.
     *
     * @return An EmptyNode
     */
    RedBlackTree.EmptyNode<T> clear();

    /**
     * Checks, if this {@code RedBlackTree} contains the given {@code value}.
     *
     * @param value A value.
     * @return true, if this tree contains the value, false otherwise.
     */
    boolean contains(T value);

    /**
     * Checks if this {@code RedBlackTree} is empty, i.e. an instance of {@code EmptyNode}.
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
    class TreeNode<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        public final Color color;
        public final RedBlackTree<T> left;
        public final T value;
        public final RedBlackTree<T> right;
        public final Comparator<? super T> comparator;

        private final transient Lazy<Integer> hashCode;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        private TreeNode(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<? super T> comparator) {
            this.color = color;
            this.left = left;
            this.value = value;
            this.right = right;
            this.comparator = comparator;
            this.hashCode =  Lazy.of(() -> Objects.hash(this.value, this.left, this.right));
        }

        @Override
        public EmptyNode<T> clear() {
            return RedBlackTree.empty(comparator);
        }

        @Override
        public boolean contains(T value) {
            final int result = comparator.compare(value, value);
            return (result == 0) || (result < 0 && left.contains(value)) || (result > 0 && right.contains(value));
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof TreeNode) {
                final TreeNode<?> that = (TreeNode<?>) o;
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
                final TreeNode<?> node = (TreeNode<?>) tree;
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

        private static <T> TreeNode<T> lbalance(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<? super T> comparator) {
            if (color == BLACK) {
                if (!left.isEmpty()) {
                    final TreeNode<T> ln = (TreeNode<T>) left;
                    if (ln.color == RED) {
                        if (!ln.left.isEmpty()) {
                            final TreeNode<T> lln = (TreeNode<T>) ln.left;
                            if (lln.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, lln.left, lln.value, lln.right, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, ln.right, value, right, comparator);
                                return new TreeNode<>(RED, newLeft, ln.value, newRight, comparator);
                            }
                        }
                        if (!ln.right.isEmpty()) {
                            final TreeNode<T> lrn = (TreeNode<T>) ln.right;
                            if (lrn.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, ln.left, ln.value, lrn.left, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, lrn.right, value, right, comparator);
                                return new TreeNode<>(RED, newLeft, lrn.value, newRight, comparator);
                            }
                        }
                    }
                }
            }
            return new TreeNode<>(color, left, value, right, comparator);
        }

        private static <T> TreeNode<T> rbalance(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<? super T> comparator) {
            if (color == BLACK) {
                if (!right.isEmpty()) {
                    final TreeNode<T> rn = (TreeNode<T>) right;
                    if (rn.color == RED) {
                        if (!rn.left.isEmpty()) {
                            final TreeNode<T> rln = (TreeNode<T>) rn.left;
                            if (rln.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, left, value, rln.left, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, rln.right, rn.value, rn.right, comparator);
                                return new TreeNode<>(RED, newLeft, rln.value, newRight, comparator);
                            }
                        }
                        if (!rn.right.isEmpty()) {
                            final TreeNode<T> rrn = (TreeNode<T>) rn.right;
                            if (rrn.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, left, value, rn.left, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, rrn.left, rrn.value, rrn.right, comparator);
                                return new TreeNode<>(RED, newLeft, rn.value, newRight, comparator);
                            }
                        }
                    }
                }
            }
            return new TreeNode<>(color, left, value, right, comparator);
        }
    }

    /**
     * The empty tree node. It can't be a singleton because it depends on a {@link Comparator}.
     *
     * @param <T> Component type
     */
    class EmptyNode<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        public final Comparator<? super T> comparator;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        private EmptyNode(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public EmptyNode<T> clear() {
            return this;
        }

        @Override
        public boolean contains(T value) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof EmptyNode);
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
