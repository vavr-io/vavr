/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Require;
import javaslang.Tuple;

import java.io.*;

// TODO: read on updating nested data structures with lenses (http://stackoverflow.com/questions/3900307/cleaner-way-to-update-nested-structures)

/**
 * A binary tree implementation, i.e. a tree with a left and a right branch.
 * <p/>
 * In general, there are many different possabilities how to implement a binary tree, e.g.
 * <ul>
 * <li><code>Tree = Nil | Node(left: Tree, value, right: Tree)</code></li>
 * <li><code>Tree = Leaf(value) | Branch(left: Tree, value, right: Tree)</code></li>
 * <li><code>Tree = Leaf(value) | Branch(left: Tree, right: Tree)</code></li>
 * </ul>
 * A natural, algebraic tree representation needs to consider the empty tree which is the neutral element of tree operations.
 * Additionally there is no reason to restrict trees to have values at leaf nodes only. Algorithms which rely on this property
 * have to maintain this state by their own.
 * <p/>
 * Therefore the first tree representation fits best:
 * <pre>
 * <code>Tree = Nil | Node(left: Tree, value, right: Tree)</code>
 * </pre>
 *
 * @param <T> the type of a Node's value.
 */
public interface BTree<T> extends Tree<T, BTree<T>> {

    @Override
    default String getName() {
        return "BTree";
    }

    /**
     * Gets the left branch of this BTree.
     *
     * @return The left branch if this BTree is a Node.
     * @throws java.lang.UnsupportedOperationException if this BTree is Nil
     */
    BTree<T> left();

    /**
     * Gets the right branch of this BTree.
     *
     * @return The right branch if this BTree is a Node.
     * @throws java.lang.UnsupportedOperationException if this BTree is Nil
     */
    BTree<T> right();

    // -- factory methods

    static <T> Nil<T> nil() {
        return Nil.instance();
    }

    static <T> Node<T> of(BTree<T> left, T value, BTree<T> right) {
        return new Node<>(left, value, right);
    }

    static <T> Node<T> of(T value) {
        return BTree.of(Nil.instance(), value, Nil.instance());
    }

    /**
     * Converts an Iterable to a balanced binary tree.
     * <p/>
     * Example: {@code }BTree.balance(List.of(1,2,3,4,5,6)) = (1 (2 3 4) (5 6))}
     *
     * @param iterable An Iterable
     * @param <T> Element type
     * @return A balanced tree containing all elements of the given iterable.
     */
    static <T> BTree<T> balance(Iterable<T> iterable) {
        final List<T> list = List.of(iterable);
        if (list.isEmpty()) {
            return BTree.nil();
        } else {
            final T value = list.head();
            // DEV-NOTE: intentionally calling list.size()/2 instead of list.tail().size()/2
            final Tuple.Tuple2<List<T>, List<T>> split = list.tail().splitAt(list.size() / 2);
            final BTree<T> left = BTree.balance(split._1);
            final BTree<T> right = BTree.balance(split._2);
            return BTree.of(left, value, right);
        }
    }

    // -- BTree implementations

    static final class Node<T> extends AbstractTree<T, BTree<T>> implements BTree<T>, Serializable {

        private static final long serialVersionUID = -1368274890360703478L;

        private final BTree<T> left;
        private final BTree<T> right;
        private final T value;

        public Node(BTree<T> left, T value, BTree<T> right) {
            Require.nonNull(left, "left is null");
            Require.nonNull(right, "right is null");
            this.left = left;
            this.right = right;
            this.value = value;
        }

        @Override
        public BTree<T> left() {
            return left;
        }

        @Override
        public BTree<T> right() {
            return right;
        }

        @Override
        public T get() {
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
        public List<BTree<T>> children() {
            return List.of(left, right).filter(e -> !e.isEmpty());
        }

        // -- Serializable implementation

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p/>
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
         * <p/>
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

            private static final long serialVersionUID = -8789880233113465837L;

            // the instance to be serialized/deserialized
            private transient Node<T> node;

            /**
             * Constructor for the case of serialization, called by {@link Node#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param node a Node
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
                s.writeObject(node.left);
                s.writeObject(node.right);
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
                final BTree<T> left = (BTree<T>) s.readObject();
                final BTree<T> right = (BTree<T>) s.readObject();
                node = new Node<>(left, value, right);
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p/>
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

    static final class Nil<T> extends AbstractTree<T, BTree<T>> implements BTree<T>, Serializable {

        private static final long serialVersionUID = 4966576338736993154L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        public static <T> Nil<T> instance() {
            @SuppressWarnings("unchecked")
            final Nil<T> instance = (Nil<T>) INSTANCE;
            return instance;
        }

        @Override
        public BTree<T> left() {
            throw new UnsupportedOperationException("left of empty binary tree");
        }

        @Override
        public BTree<T> right() {
            throw new UnsupportedOperationException("right of empty binary tree");
        }

        @Override
        public T get() {
            throw new UnsupportedOperationException("get value of empty binary tree");
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
        public List<BTree<T>> children() {
            throw new UnsupportedOperationException("children of empty binary tree");
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
}
