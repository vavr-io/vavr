/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Require;

import java.io.*;

// TODO: consider updating tree structures with lenses (http://stackoverflow.com/questions/3900307/cleaner-way-to-update-nested-structures)

/**
 * A rose tree implementation, i.e. a tree with an arbitrary number of children.
 *
 * @param <T> the type of a Node's value.
 */
public interface RTree<T> extends Tree<T, RTree<T>> {

    @Override
    default String getName() {
        return "RTree";
    }

    // -- factory methods

    @SafeVarargs
    static <T> Node<T> of(T value, RTree<T>... children) {
        return new Node<>(value, List.of(children).filter(e -> !e.isEmpty()));
    }

    static <T> Nil<T> nil() {
        return Nil.instance();
    }

    // -- RTree implementations

    static final class Node<T> extends AbstractTree<T, RTree<T>> implements RTree<T>, Serializable {

        private static final long serialVersionUID = -1368274890360703478L;

        private final List<RTree<T>> children;
        private final T value;

        public Node(T value, List<RTree<T>> children) {
            Require.nonNull(children, "children is null");
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
            return children.isEmpty();
        }

        @Override
        public List<RTree<T>> children() {
            return children;
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
                s.writeObject(node.children);
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
                final List<RTree<T>> children = (List<RTree<T>>) s.readObject();
                node = new Node<>(value, children);
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

    static final class Nil<T> extends AbstractTree<T, RTree<T>> implements RTree<T>, Serializable {

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
            throw new UnsupportedOperationException("get value of empty rose tree");
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
        public List<RTree<T>> children() {
            throw new UnsupportedOperationException("children of empty rose tree");
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
