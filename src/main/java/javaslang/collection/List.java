/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Lang.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * TODO: need a Collector for terminal stream operations for this List type
 * 
 * TODO: javadoc
 * 
 * @param <T> Component type of this list
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

	/**
	 * Calculates the size of a List in O(n).
	 * 
	 * @return The size of this List.
	 */
	default int size() {
		return isEmpty() ? 0 : 1 + tail().size();
	}

	default boolean contains(T o) {
		List<T> list = this;
		while (!list.isEmpty()) {
			final T head = list.head();
			if (head == o || (head != null && head.equals(o))) {
				return true;
			}
			list = list.tail();
		}
		return false;
	}

	default T get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("No element at specified index: " + index);
		}
		List<T> list = this;
		int currentIndex = index;
		while (currentIndex > 0) {
			currentIndex--;
			list = list.tail();
		}
		return list.head();
	}

	/**
	 * Reverses this List and returns a new List in O(n).
	 * 
	 * @return A new List instance containing the elements of this List in reverse order.
	 */
	default List<T> reverse() {
		List<T> result = EmptyList.instance();
		List<T> list = this;
		while (!list.isEmpty()) {
			result = result.prepend(list.head());
			list = list.tail();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	default T[] toArray() {
		return (T[]) stream().toArray();
	}

	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	default Stream<T> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}

	@Override
	default Spliterator<T> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED);
	}

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
	
	static <T> List<T> empty() {
		return EmptyList.instance();
	}
	
	// Listz.of(1, 2, 3, 4) = List(1, List(2, List(3, List(4, EmptyList()))))
	@SafeVarargs
	static <T> List<T> of(T... elements) {
		requireNonNull(elements, "elements is null");
		List<T> result = EmptyList.instance();
		for (int i = elements.length - 1; i >= 0; i--) {
			result.prepend(elements[i]);
		}
		return result;
	}

}
