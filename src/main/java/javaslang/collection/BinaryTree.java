/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Manifest;
import javaslang.Require;
import javaslang.Tuple;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

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
public interface BinaryTree<T> extends Tree<T, BinaryTree<?>, BinaryTree<T>> {

    @Override
    default String getName() {
        return BinaryTree.class.getSimpleName();
    }

    @Override
    List<BinaryTree<T>> getChildren();

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

    // -- Foldable implementation

    @Override
    default Iterator<T> iterator() {
        // TODO: create an iterator based on an Ordering which is part of the Tree instance
        return flatten().iterator();
    }

    @Override
    default <U> BinaryTree<U> unit(U element) {
        return new Leaf<>(element);
    }

    @Override
    default BinaryTree<T> zero() {
        return Nil.instance();
    }

    @Override
    default BinaryTree<T> combine(BinaryTree<T> tree1, BinaryTree<T> tree2) {
        if (tree1.isEmpty()) {
            return tree2;
        } else if (tree2.isEmpty()) {
            return tree1;
        } else if (tree1.isLeaf()) {
            return new Branch<>(tree2, tree1.getValue(), Nil.instance());
        } else {
            return new Branch<>(combine(tree1.left(), tree2), tree1.getValue(), tree1.right());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U, TREE extends Manifest<U, BinaryTree<?>>> BinaryTree<U> flatMap(Function<? super T, TREE> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else if (isLeaf()) {
            return (BinaryTree<U>) mapper.apply(getValue());
        } else {
            final BinaryTree<U> tree = (BinaryTree<U>) mapper.apply(getValue());
            final BinaryTree<U> left = left().flatMap(mapper).concat(tree.left());
            final BinaryTree<U> right = right().flatMap(mapper).concat(tree.right());
            return BinaryTree.of(left, tree.getValue(), right);
        }
    }

    @Override
    default <U> BinaryTree<U> map(Function<? super T, ? extends U> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else if (isLeaf()) {
            return new Leaf<>(mapper.apply(getValue()));
        } else {
            return BinaryTree.of(left().map(mapper), mapper.apply(getValue()), right().map(mapper));
        }
    }

// TODO
//    zip(Iterable)
//    zipAll(Iterable, Object, Object)
//    zipWithIndex()
//    unzip(java.util.function.Function)

    // -- factory methods

    static <T> BinaryTree<T> of(BinaryTree<T> left, T value, BinaryTree<T> right) {
        Require.nonNull(left, "left is null");
        Require.nonNull(right, "right is null");
        if (left.isEmpty() && right.isEmpty()) {
            return new Leaf<>(value);
        } else {
            return new Branch<>(left, value, right);
        }
    }

    /**
     * Throws if left and right are Nil - if in doubt, use BinaryTree.of(left, value, right) instead.
     */
    static <T> Branch<T> branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
        Require.nonNull(left, "left is null");
        return new Branch<>(left, value, right);
    }

    static <T> Leaf<T> leaf(T value) {
        return new Leaf<>(value);
    }

    static <T> Nil<T> nil() {
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
            return Nil.instance();
        } else {
            final T value = list.head();
            // DEV-NOTE: intentionally calling list.size()/2 instead of list.tail().size()/2
            final Tuple.Tuple2<List<T>, List<T>> split = list.tail().splitAt(list.length() / 2);
            final BinaryTree<T> left = BinaryTree.balance(split._1);
            final BinaryTree<T> right = BinaryTree.balance(split._2);
            // DEV-NOTE: result may be a Leaf or a Branch
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

    static final class Leaf<T> extends AbstractBinaryTree<T> implements Serializable {

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
        public T getValue() {
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
        public List<BinaryTree<T>> getChildren() {
            return List.nil();
        }
    }

    static final class Branch<T> extends AbstractBinaryTree<T> implements Serializable {

        private static final long serialVersionUID = -1368274890360703478L;

        private final BinaryTree<T> left;
        private final BinaryTree<T> right;
        private final T value;

        public Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
            Require.nonNull(left, "left is null");
            Require.nonNull(right, "right is null");
            Require.isFalse(left.isEmpty() && right.isEmpty(), "left and right are Nil - use Leaf instead of Branch");
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
        public T getValue() {
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
        public List<BinaryTree<T>> getChildren() {
            // IntelliJ error: List.of(left, right).filter(tree -> !tree.isEmpty());
            return List.<BinaryTree<T>> nil().prepend(right).prepend(left).filter(tree -> !tree.isEmpty());
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

    static final class Nil<T> extends AbstractBinaryTree<T> implements Serializable {

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
        public T getValue() {
            throw new UnsupportedOperationException("getValue of Nil");
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
        public List<BinaryTree<T>> getChildren() {
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

    // -- Tree API shared by implementations

    static abstract class AbstractBinaryTree<T> implements BinaryTree<T> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof BinaryTree)) {
                return false;
            } else {
                final BinaryTree that = (BinaryTree) o;
                return (this.isEmpty() && that.isEmpty()) || (!this.isEmpty() && !that.isEmpty()
                        && Objects.equals(this.getValue(), that.getValue())
                        && this.getChildren().equals(that.getChildren()));
            }
        }

        @Override
        public int hashCode() {
            if (isEmpty()) {
                return 1;
            } else {
                // need cast because of jdk 1.8.0_25 compiler error
                //noinspection RedundantCast
                return (int) getChildren().map(Objects::hashCode).foldLeft(31 + Objects.hashCode(getValue()), (i,j) -> i * 31 + j);
            }
        }

        @Override
        public String toString() {
            return toLispString();
        }
    }
}
