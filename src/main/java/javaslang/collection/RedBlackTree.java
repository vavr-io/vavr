/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;

// Scala's RedBlackTree: https://github.com/scala/scala/blob/v2.11.7/src/library/scala/collection/immutable/RedBlackTree.scala
// Scala's TreeSet API: http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.TreeSet
// A mutable implementation: http://cs.lmu.edu/~ray/notes/redblacktrees/
public interface RedBlackTree<T> {

    @SuppressWarnings("unchecked")
    static <T> EmptyNode<T> empty() {
        return new EmptyNode<>((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));
    }

    default TreeNode<T> add(T that) {
        class Util {
            TreeNode<T> insert(RedBlackTree<T> tree, T that) {
                if (tree.isEmpty()) {
                    final EmptyNode<T> empty = (EmptyNode<T>) tree;
                    return new TreeNode<>(RED, empty, that, empty, empty.comparator);
                } else {
                    final TreeNode<T> node = (TreeNode<T>) tree;
                    final int result = node.comparator.compare(node.value, that);
                    if (result == 0) {
                        return node;
                    } else if (result < 0) {
                        return balance(node.color, node.left, node.value, insert(node.right, that), node.comparator);
                    } else {
                        return balance(node.color, insert(node.left, that), node.value, node.right, node.comparator);
                    }
                }
            }
        }
        final TreeNode<T> result = new Util().insert(this, that);
        return new TreeNode<>(BLACK, result.left, result.value, result.right, result.comparator);
    }

    default TreeNode<T> balance(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<T> comparator) {
        if (color == BLACK) {
            if (!left.isEmpty() && !left.isLeaf()) {
                final TreeNode<T> ln = (TreeNode<T>) left;
                if (ln.color == RED) {
                    if (!ln.left.isEmpty()/* && !ln.left.isLeaf()*/) {
                        final TreeNode<T> lln = (TreeNode<T>) ln.left;
                        if (lln.color == RED) {
                            final TreeNode<T> newLeft = new TreeNode<>(BLACK, lln.left, lln.value, lln.right, comparator);
                            final TreeNode<T> newRight = new TreeNode<>(BLACK, ln.right, value, right, comparator);
                            return new TreeNode<>(RED, newLeft, ln.value, newRight, comparator);
                        }
                    }
                    if (!ln.right.isEmpty()/* && !ln.right.isLeaf()*/) {
                        final TreeNode<T> lrn = (TreeNode<T>) ln.right;
                        if (lrn.color == RED) {
                            final TreeNode<T> newLeft = new TreeNode<>(BLACK, ln.left, ln.value, lrn.left, comparator);
                            final TreeNode<T> newRight = new TreeNode<>(BLACK, lrn.right, value, right, comparator);
                            return new TreeNode<>(RED, newLeft, lrn.value, newRight, comparator);
                        }
                    }
                }
            }
            if (!right.isEmpty() && !right.isLeaf()) {
                final TreeNode<T> rn = (TreeNode<T>) right;
                if (rn.color == RED) {
                    if (!rn.left.isEmpty()/* && !rn.left.isLeaf()*/) {
                        final TreeNode<T> rln = (TreeNode<T>) rn.left;
                        if (rln.color == RED) {
                            final TreeNode<T> newLeft = new TreeNode<>(BLACK, left, value, rln.left, comparator);
                            final TreeNode<T> newRight = new TreeNode<>(BLACK, rln.right, rn.value, rn.right, comparator);
                            return new TreeNode<>(RED, newLeft, rln.value, newRight, comparator);
                        }
                    }
                    if (!rn.right.isEmpty()/* && !rn.right.isLeaf()*/) {
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

    boolean contains(T that);

    boolean isEmpty();

    boolean isLeaf();

    // does not consider Comparator
    @Override
    boolean equals(Object o);

    // does not consider Comparator
    @Override
    int hashCode();

    @Override
    String toString();

    enum Color {

        RED, BLACK;

        @Override
        public String toString() {
            return (this == RED) ? "R" : "B";
        }
    }

    class TreeNode<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Color color;
        final RedBlackTree<T> left;
        final T value;
        final RedBlackTree<T> right;
        final Comparator<T> comparator;

        TreeNode(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<T> comparator) {
            this.color = color;
            this.left = left;
            this.value = value;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        public boolean contains(T that) {
            final int result = comparator.compare(value, that);
            return (result == 0) || (result < 0 && left.contains(that)) || (result > 0 && right.contains(that));
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return left.isEmpty() && right.isEmpty();
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
            return Objects.hash(value, left, right);
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
    }

    class EmptyNode<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Comparator<T> comparator;

        EmptyNode(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public boolean contains(T that) {
            return false;
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
