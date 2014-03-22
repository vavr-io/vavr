package javaslang.collection;

import static javaslang.lang.Lang.require;

import java.util.Collection;
import java.util.List;

import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

/**
 * Additions to {@link java.util.Collections}.
 */
public final class Collections {

	private Collections() {
		throw new AssertionError(Collections.class.getName() + " cannot be instantiated.");
	}
	
	/**
	 * Adds an element to a given collection and returns the element.
	 * The element is returned, even if {@code c.add(elem)} returns false.
	 * 
	 * @param <T> the component type of the collection
	 * @param <E> the element type, extends T
	 * @param c A Collection of component type T
	 * @param elem An element of type T
	 * @return The given element
	 */
	public static <T,E extends T> E add(Collection<T> c, E elem) {
		require(c != null, "collection is null");
		c.add(elem);
		return elem;
	}

	/**
	 * Tests if given Collection is null or empty.
	 * 
	 * @param c A Collection
	 * @return true, if c is null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(Collection<?> c) {
		return c == null || c.size() == 0;
	}

	/**
	 * Returns the last element of the given list.
	 * 
	 * @param <T> type of list elements
	 * @param list A List, may be null or empty.
	 * @return The last element of the list or null, if the list is null, empty or the last
	 *         element is null.
	 */
	public static <T> Option<T> lastElement(List<T> list) {
		return isNullOrEmpty(list) ? None.<T>instance() : new Some<T>(list.get(list.size() - 1));
	}
	
}
