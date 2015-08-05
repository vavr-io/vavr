/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;

public interface RedBlackTree<T> extends Tree<T> {

    @SuppressWarnings("unchecked")
    static <T> EmptyNode<T> empty() {
        return new EmptyNode<>((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));
    }

    TreeNode<T> add(T that);

    @Override
    boolean contains(T that);

    @Override
    boolean isEmpty();

    @Override
    <U> RedBlackTree<U> map(Function<? super T, ? extends U> mapper);

    enum Color { RED, BLACK }

    class TreeNode<T> extends AbstractTreeNode<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Color color;
        private final RedBlackTree<T> left;
        private final T value;
        private final RedBlackTree<T> right;
        private final Comparator<T> comparator;

        private TreeNode(Color color, RedBlackTree<T> left, T value, RedBlackTree<T> right, Comparator<T> comparator) {
            this.color = color;
            this.left = left;
            this.value = value;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        public TreeNode<T> add(T that) {
            final int result = comparator.compare(value, that);
            if (result == 0) {
                return this;
            } else if (result < 0) {
                return balance(left, right.add(that));
            } else {
                return balance(left.add(that), right);
            }
        }

        @Override
        public boolean contains(T that) {
            final int result = comparator.compare(value, that);
            return (result == 0) || (result < 0 && left.contains(that)) || (result > 0 && right.contains(that));
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
            return left.isEmpty() && right.isEmpty();
        }

        @Override
        public List<? extends Tree<T>> getChildren() {
            if (left.isEmpty()) {
                if (right.isEmpty()) {
                    return List.empty();
                } else {
                    return List.of(right);
                }
            } else {
                if (right.isEmpty()) {
                    return List.of(left);
                } else {
                    return List.of(left, right);
                }
            }
        }

        // TODO: new comparator for objects of type U needed. using natural comparator of objects - may throw ClassCastExeption
        @SuppressWarnings("unchecked")
        @Override
        public <U> RedBlackTree<U> map(Function<? super T, ? extends U> mapper) {
            return new TreeNode<U>(color, left.map(mapper), mapper.apply(value), right.map(mapper), (o1, o2) -> ((Comparable<U>) o1).compareTo(o2));
        }

        private TreeNode<T> balance(RedBlackTree<T> left, RedBlackTree<T> right) {
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
                        } else if (!ln.right.isEmpty()) {
                            final TreeNode<T> lrn = (TreeNode<T>) ln.right;
                            if (lrn.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, ln.left, ln.value, lrn.left, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, lrn.right, value, right, comparator);
                                return new TreeNode<>(RED, newLeft, lrn.value, newRight, comparator);
                            }
                        }
                    }
                } else if (!right.isEmpty()) {
                    final TreeNode<T> rn = (TreeNode<T>) right;
                    if (rn.color == RED) {
                        if (!rn.left.isEmpty()) {
                            final TreeNode<T> rln = (TreeNode<T>) rn.left;
                            if (rln.color == RED) {
                                final TreeNode<T> newLeft = new TreeNode<>(BLACK, left, value, rln.left, comparator);
                                final TreeNode<T> newRight = new TreeNode<>(BLACK, rln.right, rn.value, rn.right, comparator);
                                return new TreeNode<>(RED, newLeft, rln.value, newRight, comparator);
                            }
                        } else if (!rn.right.isEmpty()) {
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

    class EmptyNode<T>  extends AbstractTreeNode<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Comparator<T> comparator;

        private EmptyNode(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public TreeNode<T> add(T that) {
            return new TreeNode<>(Color.RED, this, that, this, comparator);
        }

        @Override
        public boolean contains(T that) {
            return false;
        }

        @Override
        public T getValue() {
            throw new NoSuchElementException("EmptyNode.getValue()");
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
        public List<? extends Tree<T>> getChildren() {
            return List.empty();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> EmptyNode<U> map(Function<? super T, ? extends U> mapper) {
            return (EmptyNode<U>) this;
        }
    }

    abstract class AbstractTreeNode<T> implements RedBlackTree<T> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof RedBlackTree) {
                final RedBlackTree<?> that = (RedBlackTree<?>) o;
                return (this.isEmpty() && that.isEmpty()) || (!this.isEmpty() && !that.isEmpty()
                        && Objects.equals(this.getValue(), that.getValue())
                        && this.getChildren().equals(that.getChildren()));
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            if (isEmpty()) {
                return 1;
            } else {
                return getChildren().map(Objects::hashCode).foldLeft(31 + Objects.hashCode(getValue()), (i, j) -> i * 31 + j);
            }
        }

        @Override
        public String toString() {
            return RedBlackTree.class.getSimpleName() + toLispString();
        }
    }
}
