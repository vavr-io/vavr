/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Lang.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * TODO: need a Collector for terminal stream operations for this List type
 * 
 * TODO: javadoc
 * 
 * @param <T> Component type of the List.
 */
public interface List<T> extends Iterable<T> {

	T head();

	List<T> tail();

	boolean isEmpty();

	default List<T> append(T element) {
		return reverse().prepend(element).reverse();
	}

	default List<T> prepend(T element) {
		return new LinearList<>(element, this);
	}

	// TODO: insert(index, element)

	// TODO: set(index, element)

	// TODO: remove(element)

	// TODO: appendAll

	// TODO: prependAll

	// TODO: insertAll

	// TODO: removeAll

	// TODO: retainAll

	// TODO: sort : List<T>

	// TODO: clear -> List.empty()

	// TODO: subList(fromIndex, toIndex)

	/**
	 * Calculates the size of a List in O(n).
	 * 
	 * {@code isEmpty() ? 0 : 1 + tail().size()}
	 * 
	 * @return The size of this List.
	 */
	default int size() {
		int result = 0;
		for (List<T> list = this; !list.isEmpty(); list = list.tail(), result++)
			;
		return result;
	}

	default boolean contains(T o) {
		return indexOf(o) != -1;
	}

	default T get(int index) {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("get(" + index + ") on empty list");
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException("get(" + index + ")");
		}
		List<T> list = this;
		for (int i = index; i > 0; i--) {
			list = list.tail();
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format("get(%s) on list of size %s",
						index, index - i));
			}
		}
		return list.head();
	}

	default int indexOf(T o) {
		int index = 0;
		for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
			if (Objects.equals(head(), o)) {
				return index;
			}
		}
		return -1;
	}

	default int lastIndexOf(T o) {
		int result = -1, index = 0;
		for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
			if (Objects.equals(head(), o)) {
				result = index;
			}
		}
		return result;
	}

	/**
	 * Reverses this List and returns a new List in O(n).
	 * 
	 * @return A new List instance containing the elements of this List in reverse order.
	 */
	default List<T> reverse() {
		List<T> result = EmptyList.instance();
		for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
			result = result.prepend(list.head());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	default T[] toArray() {
		// TODO: better impl?
		return (T[]) stream().toArray();
	}

	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	default Stream<T> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#spliterator()
	 */
	@Override
	default Spliterator<T> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED
				| Spliterator.IMMUTABLE);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	default Iterator<T> iterator() {

		final class ListIterator implements Iterator<T> {

			List<T> list;

			ListIterator(List<T> list) {
				requireNonNull(list, "list is null");
				this.list = list;
			}

			@Override
			public boolean hasNext() {
				return !list.isEmpty();
			}

			@Override
			public T next() {
				if (list.isEmpty()) {
					throw new NoSuchElementException();
				} else {
					final T result = list.head();
					list = list.tail();
					return result;
				}
			}
		}

		return new ListIterator(this);
	}

	/**
	 * Equivalent to {@link java.util.List#equals(Object)}.
	 */
	boolean equals(Object o);

	/**
	 * Equivalent to {@link java.util.List#hashCode()}.
	 */
	int hashCode();

	/**
	 * TODO: javadoc
	 */
	String toString();

	/**
	 * Returns the single instance of EmptyList. Same as {@code EmptyList.instance()}.
	 * 
	 * @param <T> Component type of EmptyList, determined by type inference in the particular
	 *            context.
	 * @return The empty list.
	 */
	static <T> List<T> empty() {
		return EmptyList.instance();
	}

	/**
	 * Creates a List of given elements.
	 * 
	 * <pre>
	 * <code>
	 *   List.of(1, 2, 3, 4)
	 * = EmptyList.instance().prepend(4).prepend(3).prepend(2).prepend(1)
	 * = new LinearList(1, new LinearList(2, new LinearList(3, new LinearList(4, EmptyList.instance()))))
	 * </code>
	 * </pre>
	 *
	 * @param <T> Component type of the List.
	 * @param elements List elements.
	 * @return A list containing the given elements in the same order.
	 */
	@SafeVarargs
	static <T> List<T> of(T... elements) {
		requireNonNull(elements, "elements is null");
		List<T> result = EmptyList.instance();
		for (int i = elements.length - 1; i >= 0; i--) {
			result = result.prepend(elements[i]);
		}
		return result;
	}

}
