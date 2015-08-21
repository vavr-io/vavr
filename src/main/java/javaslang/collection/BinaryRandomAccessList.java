/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;

import java.io.Serializable;

/**
 * A Binaray Random-Access List as described in Okasaki 2003, p. 119 ff.
 */
public final class BinaryRandomAccessList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final BinaryRandomAccessList<?> EMPTY = new BinaryRandomAccessList<>(List.empty());

    private final List<Digit<T>> digits;

    private BinaryRandomAccessList(List<Digit<T>> digits) {
        this.digits = digits;
    }

    @SuppressWarnings("unchecked")
    public static <T> BinaryRandomAccessList<T> empty() {
        return (BinaryRandomAccessList<T>) EMPTY;
    }

    public boolean isEmpty() {
        return digits.isEmpty();
    }

    public BinaryRandomAccessList<T> prepend(T x) {
        return new BinaryRandomAccessList<>(consTree(new Leaf<>(x), digits));
    }

    public T head() {
        // assumption: tree is a Leaf
        return ((Leaf<T>) unconsTree(digits)._1).value;
    }

    public BinaryRandomAccessList<T> tail() {
        return new BinaryRandomAccessList<>(unconsTree(digits)._2);
    }

    public T get(int index) {
        return look(index, digits);
    }

    public BinaryRandomAccessList<T> set(int index, T value) {
        if (index < 0 || isEmpty()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return new BinaryRandomAccessList<>(update(index, value, digits));
    }

    public int size() {
        // TODO: or add a field size to BinaryRandomAccessList
        return digits.foldLeft(0, (acc, digit) -> acc + (digit instanceof Zero ? 0 : ((One<?>) digit).tree.size()));
    }

    private static <T> List<Digit<T>> update(int i, T y, List<Digit<T>> digits) {
        final Digit<T> digit = digits.head();
        final List<Digit<T>> ts = digits.tail();
        if (digit instanceof Zero) {
            return update(i, y, ts).prepend(Zero.instance());
        } else {
            final Tree<T> t = ((One<T>) digit).tree;
            final int size = t.size();
            if (i < size) {
                return ts.prepend(new One<>(updateTree(i, y, t)));
            } else {
                return update(i - size, y, ts).prepend(new One<>(t));
            }
        }
    }

    private static <T> Tree<T> updateTree(int i, T y, Tree<T> tree) {
        if (i == 0 && tree instanceof Leaf) {
            return new Leaf<>(y);
        } else {
            final Node<T> node = (Node<T>) tree;
            final int w = tree.size();
            if (i < w / 2) {
                return new Node<>(w, updateTree(i, y, node.t1), node.t2);
            } else {
                return new Node<>(w, node.t1, updateTree(i - w / 2, y, node.t2));
            }
        }
    }

    private static <T> List<Digit<T>> consTree(Leaf<T> t, List<Digit<T>> digits) {
        if (digits.isEmpty()) {
            return List.of(new One<>(t));
        } else {
            final Digit<T> digit = digits.head();
            final List<Digit<T>> ts = digits.tail();
            if (digit instanceof Zero) {
                return ts.prepend(new One<>(t));
            } else {
                final Tree<T> t2 = ((One<T>) digit).tree;
                return consTree(t.link(t2), ts).prepend(Zero.instance());
            }
        }
    }

    private static <T> T look(int i, List<Digit<T>> digits) {
        if (digits.isEmpty()) {
            throw new IllegalStateException("bad subscript");
        }
        final Digit<T> digit = digits.head();
        final List<Digit<T>> ts = digits.tail();
        if (digit instanceof Zero) {
            return look(i, ts);
        } else {
            final Tree<T> t = ((One<T>) digit).tree;
            final int size = t.size();
            if (i < size) {
                return lookTree(i, t);
            } else {
                return look(i - size, ts);
            }
        }
    }

    private static <T> T lookTree(int i, Tree<T> tree) {
        if (tree instanceof Leaf) {
            if (i == 0) {
                return ((Leaf<T>) tree).value;
            } else {
                throw new IllegalStateException("bad subscript");
            }
        } else {
            final Node<T> node = (Node<T>) tree;
            final int halfSize = node.size / 2;
            if (i < halfSize) {
                return lookTree(i, node.t1);
            } else {
                return lookTree(i - halfSize, node.t2);
            }
        }
    }

    private static <T> List<Digit<T>> consTree(Tree<T> t, List<Digit<T>> digits) {
        if (digits.isEmpty()) {
            return List.of(new One<T>(t));
        } else {
            final Digit<T> digit = digits.head();
            final List<Digit<T>> ts = digits.tail();
            if (digit instanceof Zero) {
                return ts.prepend(new One<>(t));
            } else {
                final Tree<T> t2 = ((One<T>) digit).tree;
                return consTree(t.link(t2), ts).prepend(Zero.instance());
            }
        }
    }

    private static <T> Tuple2<Tree<T>, List<Digit<T>>> unconsTree(List<Digit<T>> digits) {
        if (digits.isEmpty()) {
            throw new UnsupportedOperationException("empty list");
        }
        final Digit<T> digit = digits.head();
        final List<Digit<T>> ts = digits.tail();
        if (digit instanceof One) {
            final One<T> one = (One<T>) digit;
            if (ts.isEmpty()) {
                return Tuple.of(one.tree, List.empty());
            } else {
                return Tuple.of(one.tree, ts.prepend(Zero.instance()));
            }
        } else {
            // assumption: tail is not empty
            // assumption: tree is a node
            final Tuple2<Tree<T>, List<Digit<T>>> uncons = unconsTree(ts);
            final Node<T> node = (Node<T>) uncons._1;
            final List<Digit<T>> ts_ = uncons._2;
            return Tuple.of(node.t1, ts_.prepend(new One<>(node.t2)));
        }
    }

    private interface Digit<T> {
    }

    private static final class Zero<T> implements Digit<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Zero<?> INSTANCE = new Zero<>();

        private Zero() {
        }

        @SuppressWarnings("unchecked")
        static <T> Zero<T> instance() {
            return (Zero<T>) INSTANCE;
        }

        public String toString() {
            return String.format("Zero");
        }
    }

    private static final class One<T> implements Digit<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final Tree<T> tree;

        private One(Tree<T> tree) {
            this.tree = tree;
        }

        public String toString() {
            return String.format("One(%s)", tree);
        }
    }

    private interface Tree<T> {

        default Node<T> link(Tree<T> that) {
            return new Node<>(this.size() + that.size(), this, that);
        }

        int size();
    }

    private static final class Leaf<T> implements Tree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private T value;

        private Leaf(T value) {
            this.value = value;
        }

        @Override
        public int size() {
            return 1;
        }

        public String toString() {
            return String.format("Leaf(%s)", value);
        }
    }

    private static final class Node<T> implements Tree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final int size;
        private final Tree<T> t1;
        private final Tree<T> t2;

        private Node(int size, Tree<T> t1, Tree<T> t2) {
            this.size = size;
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public int size() {
            return size;
        }

        public String toString() {
            return String.format("Node(%s, %s, %s)", size, t1, t2);
        }
    }
}
