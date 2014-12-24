/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Require;
import javaslang.Tuple;

import java.io.*;

/**
 * A binary tree implementation where each node keeps a value. See <a href="http://en.wikipedia.org/wiki/Binary_tree">Wikipedia: Binary tree</a>.
 * <p/>
 * A binary tree consists of branches (nodes with children) and leafs (nodes without children). The empty tree is represented by Nil.
 * <pre>
 *     <code>BinaryTree = Nil | Leaf(value) | Branch(BinaryTree left, value, BinaryTree right)</code>
 * </pre>
 * A branch has a left and a right child, at least one child is not Nil.
 *
 * @param <T> the type of a tree node's value.
 */
public interface BinaryTree<T> extends Tree<T, BinaryTree<T>> {

    @Override
    default String getName() {
        return BinaryTree.class.getSimpleName();
    }

    /**
     * Gets the left branch of this BinaryTree.
     *
     * @return The left branch if this BinaryTree is a Node.
     * @throws java.lang.UnsupportedOperationException if this BinaryTree is Nil
     */
    BinaryTree<T> left();

    /**
     * Gets the right branch of this BinaryTree.
     *
     * @return The right branch if this BinaryTree is a Node.
     * @throws java.lang.UnsupportedOperationException if this BinaryTree is Nil
     */
    BinaryTree<T> right();

    // -- factory methods

    static <T> BinaryTree<T> of(BinaryTree<T> left, T value, BinaryTree<T> right) {
        if (left.isEmpty() && right.isEmpty()) {
            return new Leaf<>(value);
        } else {
            return new Branch<>(left, value, right);
        }
    }

    /**
     * Throws if left and right are Nil - if in doubt, use BinaryTree.of(left, value, right) instead.
     */
    static <T> Branch<T> Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
        return new Branch<>(left, value, right);
    }

    static <T> Leaf<T> Leaf(T value) {
        return new Leaf<>(value);
    }

    static <T> Nil<T> Nil() {
        return Nil.instance();
    }

    /**
     * Converts an Iterable to a balanced binary tree.
     * <p/>
     * Example: {@code BinaryTree.balance(List.of(1,2,3,4,5,6)) = (1 (2 3 4) (5 6))}
     *
     * @param iterable An Iterable
     * @param <T> Element type
     * @return A balanced tree containing all elements of the given iterable.
     */
    static <T> BinaryTree<T> balance(Iterable<T> iterable) {
        final List<T> list = List.of(iterable);
        if (list.isEmpty()) {
            return Nil();
        } else {
            final T value = list.head();
            // DEV-NOTE: intentionally calling list.size()/2 instead of list.tail().size()/2
            final Tuple.Tuple2<List<T>, List<T>> split = list.tail().splitAt(list.length() / 2);
            final BinaryTree<T> left = BinaryTree.balance(split._1);
            final BinaryTree<T> right = BinaryTree.balance(split._2);
            return BinaryTree.of(left, value, right);
        }
    }

    /**
     * Converts the given elements to a balanced binary tree.
     * <p/>
     * Example: {@code BinaryTree.balance(1,2,3,4,5,6) = (1 (2 3 4) (5 6))}
     *
     * @param elements Elements
     * @param <T> Element type
     * @return A balanced tree containing all given elements.
     */
    @SafeVarargs
    static <T> BinaryTree<T> balance(T... elements) {
        return BinaryTree.balance(List.of(elements));
    }

    // -- BinaryTree implementations

    static final class Leaf<T> extends AbstractTree<T, BinaryTree<T>> implements BinaryTree<T>, Serializable {

        private static final long serialVersionUID = -189719611914095083L;

        private final T value;

        public Leaf(T value) {
            this.value = value;
        }

        @Override
        public BinaryTree<T> left() {
            throw new UnsupportedOperationException("left of Leaf");
        }

        @Override
        public BinaryTree<T> right() {
            throw new UnsupportedOperationException("right of Leaf");
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
            return true;
        }

        @Override
        public List<BinaryTree<T>> children() {
            return List.nil();
        }
    }

    static final class Branch<T> extends AbstractTree<T, BinaryTree<T>> implements BinaryTree<T>, Serializable {

        private static final long serialVersionUID = -1368274890360703478L;

        private final BinaryTree<T> left;
        private final BinaryTree<T> right;
        private final T value;

        public Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
            Require.nonNull(left, "left is null");
            Require.nonNull(right, "right is null");
            Require.isFalse(left.isEmpty() && right.isEmpty(), "left and right are Nil");
            this.left = left;
            this.right = right;
            this.value = value;
        }

        @Override
        public BinaryTree<T> left() {
            return left;
        }

        @Override
        public BinaryTree<T> right() {
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
            return false;
        }

        @Override
        public List<BinaryTree<T>> children() {
            return List.of(left, right).filter(tree -> !tree.isEmpty());
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
            private transient Branch<T> branch;

            /**
             * Constructor for the case of serialization, called by {@link BinaryTree.Branch#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param branch a Branch
             */
            SerializationProxy(Branch<T> branch) {
                this.branch = branch;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws java.io.IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeObject(branch.value);
                s.writeObject(branch.left);
                s.writeObject(branch.right);
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
                final BinaryTree<T> left = (BinaryTree<T>) s.readObject();
                final BinaryTree<T> right = (BinaryTree<T>) s.readObject();
                branch = new Branch<>(left, value, right);
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
                return branch;
            }
        }
    }

    static final class Nil<T> extends AbstractTree<T, BinaryTree<T>> implements BinaryTree<T>, Serializable {

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
        public BinaryTree<T> left() {
            throw new UnsupportedOperationException("left of Nil");
        }

        @Override
        public BinaryTree<T> right() {
            throw new UnsupportedOperationException("right of Nil");
        }

        @Override
        public T get() {
            throw new UnsupportedOperationException("get of Nil");
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
        public List<BinaryTree<T>> children() {
            return List.nil();
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
