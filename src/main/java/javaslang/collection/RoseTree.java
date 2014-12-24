/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Require;

import java.io.*;

/**
 * A rose tree implementation, i.e. a tree with an arbitrary number of children, where each node keeps a value.
 * See <a href="http://en.wikipedia.org/wiki/Rose_tree">Wikipedia: Rose tree</a>.
 *
 * @param <T> the type of a Node's value.
 */
public interface RoseTree<T> extends Tree<T, RoseTree<T>> {

    @Override
    default String getName() {
        return RoseTree.class.getSimpleName();
    }

    // -- factory methods

    @SafeVarargs
    static <T> Branch<T> Branch(T value, NonNil<T> child1, NonNil<T>... children) {
        Require.nonNull(children, "children is null");
        return new Branch<>(value, List.of(children).prepend(child1));
    }

    static <T> Leaf<T> Leaf(T value) {
        return new Leaf<>(value);
    }

    static <T> Nil<T> Nil() {
        return Nil.instance();
    }

    // -- RoseTree implementations

    /**
     * Implementors of this tagging interface indicate that they are not Nil.
     * @param <T> Component type of the rose tree.
     */
    static interface NonNil<T> extends RoseTree<T> {
    }

    static final class Leaf<T> extends AbstractTree<T, RoseTree<T>> implements NonNil<T>, Serializable {

        private static final long serialVersionUID = -6301673452872179894L;

        private final T value;

        public Leaf(T value) {
            this.value = value;
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
        public List<RoseTree<T>> children() {
            throw new UnsupportedOperationException("children of Leaf");
        }
    }

    static final class Branch<T> extends AbstractTree<T, RoseTree<T>> implements NonNil<T>, Serializable {

        private static final long serialVersionUID = -1368274890360703478L;

        private final List<? extends RoseTree<T>> children;
        private final T value;

        public Branch(T value, List<NonNil<T>> children) {
            Require.nonNull(children, "children is null");
            Require.isFalse(children.isEmpty(), "no children");
            this.children = children;
            this.value = value;
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
        public List<RoseTree<T>> children() {
            @SuppressWarnings("unchecked")
            final List<RoseTree<T>> cast = (List<RoseTree<T>>) children;
            return cast;
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

            private static final long serialVersionUID = -1169723642575166947L;

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

    static final class Nil<T> extends AbstractTree<T, RoseTree<T>> implements RoseTree<T>, Serializable {

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
        public List<RoseTree<T>> children() {
            throw new UnsupportedOperationException("children of Nil");
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
