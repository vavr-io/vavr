/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javaslang.collection.List.AbstractList;

/**
 * Non-empty List.
 * 
 * @param <E> Component type of the List.
 */
// DEV NOTE: class declared final because of serialization proxy pattern.
// (see Effective Java, 2nd ed., p. 315)
public final class LinearList<E> extends AbstractList<E> implements Serializable {

	private static final long serialVersionUID = 53595355464228669L;

	private final E head;
	private final List<E> tail;

	public LinearList(E head, List<E> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public E head() {
		return head;
	}

	@Override
	public List<E> tail() {
		return tail;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	// -- Serializable implementation

	/**
	 * {@code writeReplace} method for the serialization proxy pattern.
	 * <p>
	 * The presence of this method causes the serialization system to emit a SerializationProxy
	 * instance instead of an instance of the enclosing class.
	 * 
	 * @return A SerialiationProxy for this enclosing class.
	 */
	private Object writeReplace() {
		return new SerializationProxy<>(this);
	}

	/**
	 * {@code readObject} method for the serialization proxy pattern.
	 * <p>
	 * Guarantees that the serialization system will never generate a serialized instance of the
	 * enclosing class.
	 * 
	 * @param stream An object serialization stream.
	 * @throws InvalidObjectException This method will throw with the message "Proxy required".
	 */
	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

	/**
	 * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists
	 * with final instance fields.
	 *
	 * @param <E> The component type of the underlying list.
	 */
	// DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
	// classes. Also, it may not be compatible with circular object graphs.
	private static class SerializationProxy<E> implements Serializable {

		private static final long serialVersionUID = 3851894487210781138L;

		// the instance to be serialized/deserialized
		private transient LinearList<E> list;

		/**
		 * Constructor for the case of serialization, called by {@link LinearList#writeReplace()}.
		 * <p>
		 * The constructor of a SerializationProxy takes an argument that concisely represents the
		 * logical state of an instance of the enclosing class.
		 * 
		 * @param linearList
		 */
		SerializationProxy(LinearList<E> list) {
			this.list = list;
		}

		/**
		 * Write an object to a serialization stream.
		 * 
		 * @param s An object serialization stream.
		 * @throws IOException If an error occurs writing to the stream.
		 */
		private void writeObject(ObjectOutputStream s) throws IOException {
			s.defaultWriteObject();
			s.writeInt(list.size());
			for (List<E> l = list; !l.isEmpty(); l = l.tail()) {
				s.writeObject(l.head());
			}
		}

		/**
		 * Read an object from a deserialization stream.
		 * 
		 * @param s An object deserialization stream.
		 * @throws ClassNotFoundException If the object's class read from the stream cannot be
		 *             found.
		 * @throws InvalidObjectException If the stream contains no list elements.
		 * @throws IOException If an error occurs reading from the stream.
		 */
		private void readObject(ObjectInputStream s) throws ClassNotFoundException,
				InvalidObjectException, IOException {
			s.defaultReadObject();
			final int size = s.readInt();
			if (size <= 0) {
				throw new InvalidObjectException("No elements");
			}
			List<E> temp = EmptyList.instance();
			for (int i = 0; i < size; i++) {
				@SuppressWarnings("unchecked")
				final E element = (E) s.readObject();
				temp = temp.prepend(element);
			}
			list = (LinearList<E>) temp.reverse();
		}

		/**
		 * {@code readResolve} method for the serialization proxy pattern.
		 * <p>
		 * Returns a logically equivalent instance of the enclosing class. The presence of this
		 * method causes the serialization system to translate the serialization proxy back into an
		 * instance of the enclosing class upon deserialization.
		 * 
		 * @return A deserialized instance of the enclosing class.
		 */
		private Object readResolve() {
			return list;
		}

	}

}
