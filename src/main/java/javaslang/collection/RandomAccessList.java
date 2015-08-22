/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.NoSuchElementException;

/**
 * A random access list that offers O(log n) random access and O(1) head, tail and prepend.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface RandomAccessList<T> {

    static <T> RandomAccessList<T> empty() {
        return Nil.instance();
    }

    T get(int index);

    T head();

    boolean isEmpty();

    RandomAccessList<T> prepend(T value);

    RandomAccessList<T> set(int index, T value);

    int size();

    RandomAccessList<T> tail();

    @Override
    String toString();

    final class Nil<T> implements RandomAccessList<T> {

        private static final Nil<?> INSTANCE = new Nil<>();

        private Nil() {
        }

        @SuppressWarnings("unchecked")
        private static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public T get(int index) {
            throw new NoSuchElementException("get on empty list");
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head on empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public RandomAccessList<T> prepend(T value) {
            return new Cons<>(1, new Leaf<>(value), Nil.instance());
        }

        @Override
        public RandomAccessList<T> set(int index, T value) {
            throw new UnsupportedOperationException("set on empty list");
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public RandomAccessList<T> tail() {
            throw new UnsupportedOperationException("tail on empty list");
        }

        @Override
        public String toString() {
            return "Nil";
        }
    }

    final class Cons<T> implements RandomAccessList<T> {

        private final int listSize;
        private final int treeSize;
        private final Tree<T> tree;
        private final RandomAccessList<T> tail;

        private Cons(int treeSize, Tree<T> tree, RandomAccessList<T> tail) {
            this.listSize = treeSize + tail.size();
            this.treeSize = treeSize;
            this.tree = tree;
            this.tail = tail;
        }

        @Override
        public T get(int index) {
            if (index < treeSize) {
                return Tree.lookup(treeSize, tree, index);
            } else {
                return tail.get(index - treeSize);
            }
        }

        @Override
        public T head() {
            return tree.getValue();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public RandomAccessList<T> prepend(T value) {
            if (tail.isEmpty() || treeSize != tail.size()) {
                return new Cons<>(1, new Leaf<>(value), this);
            } else {
                final Cons<T> rest = (Cons<T>) tail;
                return new Cons<>(1 + treeSize + rest.treeSize, new Node<>(tree, value, rest.tree), rest.tail);
            }
        }

        @Override
        public RandomAccessList<T> set(int index, T value) {
            if (index < treeSize) {
                return new Cons<>(treeSize, Tree.update(treeSize, tree, index, value), tail);
            } else {
                return new Cons<>(treeSize, tree, tail.set(index - treeSize, value));
            }
        }

        @Override
        public int size() {
            return listSize;
        }

        @Override
        public RandomAccessList<T> tail() {
            if (tree.isLeaf()) {
                return tail;
            } else {
                final Node<T> node = (Node<T>) tree;
                final int halfSize = treeSize / 2;
                return new Cons<>(halfSize, node.left, new Cons<>(halfSize, node.right, tail));
            }
        }

        @Override
        public String toString() {
            return String.format("Cons(%s, %s, %s)", treeSize, tree, tail);
        }
    }

    interface Tree<T> {

        static <T> T lookup(int size, Tree<T> tree, int i) {
            if (i == 0) {
                return tree.getValue();
            } else if (tree.isLeaf()) {
                throw new IndexOutOfBoundsException();
            } else {
                final Node<T> node = (Node<T>) tree;
                final int halfSize = size / 2;
                if (i <= halfSize) {
                    return lookup(halfSize, node.left, i - 1);
                } else {
                    return lookup(halfSize, node.right, i - 1 - halfSize);
                }
            }
        }

        static <T> Tree<T> update(int size, Tree<T> tree, int i, T value) {
            if (tree.isLeaf()) {
                if (i == 0) {
                    return new Leaf<>(value);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } else {
                final Node<T> node = (Node<T>) tree;
                if (i == 0) {
                    return new Node<>(node.left, value, node.right);
                } else {
                    final int halfSize = size / 2;
                    if (i <= halfSize) {
                        return new Node<>(update(halfSize, node.left, i - 1, value), node.value, node.right);
                    } else {
                        return new Node<>(node.left, node.value, update(halfSize, node.right, i - 1 - halfSize, value));
                    }
                }
            }
        }

        T getValue();

        boolean isLeaf();

        @Override
        String toString();
    }

    final class Node<T> implements Tree<T> {

        private final Tree<T> left;
        private final T value;
        private final Tree<T> right;

        private Node(Tree<T> left, T value, Tree<T> right) {
            this.left = left;
            this.value = value;
            this.right = right;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public String toString() {
            return String.format("Node(%s, %s, %s)", left, value, right);
        }
    }

    final class Leaf<T> implements Tree<T> {

        private final T value;

        private Leaf(T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public String toString() {
            return "Leaf(" + value + ")";
        }
    }
}
