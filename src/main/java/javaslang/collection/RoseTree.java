/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple0;
import javaslang.Tuple1;
import javaslang.Tuple2;

import java.io.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * A rose tree implementation, i.e. a tree with an arbitrary number of children, where each node keeps a value.
 * See <a href="http://en.wikipedia.org/wiki/Rose_tree">Wikipedia: Rose tree</a>.
 *
 * @param <T> the type of a Node's value.
 */
public interface RoseTree<T> extends Tree<T> {

    static final long serialVersionUID = 1L;

    @SafeVarargs
    static <T> NonNil<T> of(T value, NonNil<T>... children) {
        Objects.requireNonNull(children, "children is null");
        if (children.length == 0) {
            return new Leaf<>(value);
        } else {
            @SuppressWarnings({"unchecked", "varargs"})
            final List<NonNil<T>> list = List.of(children);
            return new Branch<>(value, list);
        }
    }

    @SafeVarargs
    static <T> Branch<T> branch(T value, NonNil<T> child1, NonNil<T>... children) {
        Objects.requireNonNull(children, "child1 is null");
        Objects.requireNonNull(children, "children is null");
        @SuppressWarnings({"unchecked", "varargs"})
        final List<NonNil<T>> list = List.of(children).prepend(child1);
        return new Branch<>(value, list);
    }

    static <T> Leaf<T> leaf(T value) {
        return new Leaf<>(value);
    }

    static <T> Nil<T> nil() {
        return Nil.instance();
    }

    @Override
    List<NonNil<T>> getChildren();

    @SuppressWarnings("unchecked")
    @Override
    default <U> RoseTree<U> map(Function<? super T, ? extends U> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else if (isLeaf()) {
            return new Leaf<>(mapper.apply(getValue()));
        } else {
            final U value = mapper.apply(getValue());
            /*
             * DEV-NOTE: The constructor of Branch and the factory method RoseTree.of
             * expect NonNil children. With the implementation of map this implies that
             * the result of the method getChildren().map is of type List<NonNil>.
             */
            @SuppressWarnings({"unchecked", "rawtypes"})
            final List children = (List<NonNil<U>>) (List) getChildren().map(tree -> tree.map(mapper));
            return new Branch<>(value, children);
        }
    }

    /**
     * Implementors of this tagging interface indicate that they are not Nil.
     *
     * @param <T> Component type of the rose tree.
     */
    static interface NonNil<T> extends RoseTree<T> {
    }

    static final class Leaf<T> extends AbstractRoseTree<T> implements NonNil<T> {

        private static final long serialVersionUID = 1L;

        private final T value;

        public Leaf(T value) {
            this.value = value;
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
        public List<NonNil<T>> getChildren() {
            return List.nil();
        }

        @Override
        public Tuple1<T> unapply() {
            return Tuple.of(value);
        }
    }

    static final class Branch<T> extends AbstractRoseTree<T> implements NonNil<T> {

        private static final long serialVersionUID = 1L;

        private final List<NonNil<T>> children;
        private final T value;

        public Branch(T value, List<NonNil<T>> children) {
            Objects.requireNonNull(children, "children is null");
            if (children.isEmpty()) {
                throw new IllegalArgumentException("no children");
            }
            this.children = children;
            this.value = value;
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
        public List<NonNil<T>> getChildren() {
            return children;
        }

        @Override
        public Tuple2<T, List<NonNil<T>>> unapply() {
            return Tuple.of(value, children);
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
             * Constructor for the case of serialization, called by {@link Branch#writeReplace()}.
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
                s.writeObject(branch.children);
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
                final List<NonNil<T>> children = (List<NonNil<T>>) s.readObject();
                branch = new Branch<>(value, children);
            }

            /**
             * <p>
             * {@code readResolve} method for the serialization proxy pattern.
             * </p>
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

    static final class Nil<T> extends AbstractRoseTree<T> {

        private static final long serialVersionUID = 1L;

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
        public List<NonNil<T>> getChildren() {
            return List.nil();
        }

        @Override
        public Tuple0 unapply() {
            return Tuple0.instance();
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

    static abstract class AbstractRoseTree<T> implements RoseTree<T> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof RoseTree) {
                final RoseTree<?> that = (RoseTree<?>) o;
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
                // need cast because of jdk 1.8.0_25 compiler error
                //noinspection RedundantCast
                return (int) getChildren().map(Objects::hashCode).foldLeft(31 + Objects.hashCode(getValue()), (i, j) -> i * 31 + j);
            }
        }

        @Override
        public String toString() {
            return RoseTree.class.getSimpleName() + toLispString();
        }
    }
}
