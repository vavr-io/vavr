/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.io.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * A binary tree implementation where each node keeps a value.
 * <p>
 * A binary tree consists of branches (nodes with children) and leaves (nodes without children).
 * A branch has a left and a right child, at least one child is not Nil. The empty tree is represented by Nil.
 * <pre>
 *     <code>BinaryTree = Nil | Leaf(value) | Branch(BinaryTree left, value, BinaryTree right)</code>
 * </pre>
 *
 * See also <a href="http://en.wikipedia.org/wiki/Binary_tree">Binary tree</a> (wikipedia).
 *
 * @param <T> the type of a tree node's value.
 * @since 1.1.0
 */
public interface BinaryTree<T> extends Tree<T> {

    /**
     * Creates a either a binary tree branch or a leaf, depending on the children left and right.
     * By definition, a binary tree node with two Nil children is a leaf.
     *
     * @param left  Left subtree
     * @param value A value
     * @param right Right subtree
     * @param <T>   The value type
     * @return A new, non-nil binary tree
     */
    static <T> BinaryTree<T> of(BinaryTree<T> left, T value, BinaryTree<T> right) {
        Objects.requireNonNull(left, "left is null");
        Objects.requireNonNull(right, "right is null");
        if (left.isEmpty() && right.isEmpty()) {
            return new Leaf<>(value);
        } else {
            return new Branch<>(left, value, right);
        }
    }

    /**
     * Throws if left and right are Nil - if in doubt, use BinaryTree.of(left, value, right) instead.
     *
     * @param left  Left subtree
     * @param value A value
     * @param right Right subtree
     * @param <T>   Component type
     * @return A new binary tree branch
     * @throws NullPointerException     if left or right is null
     * @throws IllegalArgumentException if left and right are empty (Nil)
     */
    static <T> Branch<T> branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
        Objects.requireNonNull(left, "left is null");
        Objects.requireNonNull(right, "right is null");
        if (left.isEmpty() && right.isEmpty()) {
            throw new IllegalArgumentException("left and right are Nil - use BinaryTree.of(left, value, right) if in doubt.");
        }
        return new Branch<>(left, value, right);
    }

    /**
     * Creates a binary tree leaf of a given value.
     *
     * @param value the leaf's value
     * @param <T>   value type
     * @return a new tree leaf
     */
    static <T> Leaf<T> leaf(T value) {
        return new Leaf<>(value);
    }

    /**
     * Returns the empty binary tree
     *
     * @param <T> the tree's value type
     * @return the empty binary tree
     */
    static <T> Nil<T> nil() {
        return Nil.instance();
    }

    /**
     * <p>
     * Converts an Iterable to a balanced binary tree.
     * </p>
     * <p>
     * Example: {@code BinaryTree.balance(List.of(1, 2, 3, 4, 5, 6)) = (1 (2 3 4) (5 6))}
     * </p>
     *
     * @param iterable An Iterable
     * @param <T>      Element type
     * @return A balanced tree containing all elements of the given iterable.
     */
    static <T> BinaryTree<T> balance(Iterable<T> iterable) {
        final List<T> list = List.ofAll(iterable);
        if (list.isEmpty()) {
            return Nil.instance();
        } else {
            final T value = list.head();
            // DEV-NOTE: intentionally calling list.length()/2 instead of list.tail().length()/2
            final Tuple2<List<T>, List<T>> split = list.tail().splitAt(list.length() / 2);
            final BinaryTree<T> left = BinaryTree.balance(split._1);
            final BinaryTree<T> right = BinaryTree.balance(split._2);
            // DEV-NOTE: result may be a Leaf or a Branch
            return BinaryTree.of(left, value, right);
        }
    }

    /**
     * <p>
     * Converts the given elements to a balanced binary tree.
     * </p>
     * <p>
     * Example: {@code BinaryTree.balance(1, 2, 3, 4, 5, 6) = (1 (2 3 4) (5 6))}
     * </p>
     *
     * @param elements Elements
     * @param <T>      Element type
     * @return A balanced tree containing all given elements.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    static <T> BinaryTree<T> balance(T... elements) {
        final List<T> list = List.of(elements);
        return BinaryTree.balance(list);
    }

    /**
     * Returns the balanced version of this tree, i.e. the tree where all subtrees have minimal depth.
     *
     * @return The balanced version of this binary tree.
     */
    default BinaryTree<T> balance() {
        return BinaryTree.balance(flatten());
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

    @Override
    default <U> BinaryTree<U> map(Function<? super T, ? extends U> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return BinaryTree.of(left().map(mapper), mapper.apply(getValue()), right().map(mapper));
        }
    }

    /**
     * Representation of a binary tree leaf.
     *
     * @param <T> value type
     * @since 1.1.0
     */
    final class Leaf<T> extends AbstractBinaryTree<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        /**
         * Constructs a binary tree leaf.
         *
         * @param value a value
         */
        public Leaf(T value) {
            this.value = value;
        }

        @Override
        public BinaryTree<T> left() {
            return BinaryTree.nil();
        }

        @Override
        public BinaryTree<T> right() {
            return BinaryTree.nil();
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

    /**
     * Representation of a binary tree branch.
     *
     * @param <T> value type
     * @since 1.1.0
     */
    final class Branch<T> extends AbstractBinaryTree<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final BinaryTree<T> left;
        private final BinaryTree<T> right;
        private final T value;

        /**
         * Constructs a binary tree branch consisting of a value, a left and a right subtree.
         *
         * @param left  the left tree of this branch
         * @param value a value
         * @param right the right tree of this branch
         * @throws NullPointerException     if left or right is null
         * @throws IllegalArgumentException if left and right are empty (Nil)
         */
        public Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
            Objects.requireNonNull(left, "left is null");
            Objects.requireNonNull(right, "right is null");
            if (left.isEmpty() && right.isEmpty()) {
                throw new IllegalArgumentException("left and right are Nil - use Leaf instead of Branch");
            }
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
            return List.<BinaryTree<T>>nil().prepend(right).prepend(left).filter(tree -> !tree.isEmpty());
        }

        // -- Serializable implementation

        /**
         * <p>
         * {@code writeReplace} method for the serialization proxy pattern.
         * </p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * <p>
         * {@code readObject} method for the serialization proxy pattern.
         * </p>
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

            private static final long serialVersionUID = 1L;

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

    /**
     * The singleton instance of the empty binary tree.
     *
     * @param <T> type of the tree's values
     * @since 1.1.0
     */
    final class Nil<T> extends AbstractBinaryTree<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton instance of the empty binary tree.
         *
         * @param <T> type of the tree's values
         * @return the single binary tree instance
         */
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

    /**
     * An abstract binary tree implementation which just overrides equals, hashCode and toString.
     *
     * @param <T> value type of the binary tree
     * @since 1.1.0
     */
    abstract class AbstractBinaryTree<T> implements BinaryTree<T> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof BinaryTree) {
                final BinaryTree<?> that = (BinaryTree<?>) o;
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
            return BinaryTree.class.getSimpleName() + toLispString();
        }
    }
}
