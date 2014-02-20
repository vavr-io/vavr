package javaslang.util;

import java.util.Collection;
import java.util.List;

public final class Collections {

	private Collections() {
		throw new AssertionError(Collections.class.getName() + " cannot be instantiated.");
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
	 * @param list A List, may be null or empty.
	 * @return The last element of the list or null, if the list is null, empty or the last
	 *         element is null.
	 */
	public static <T> Option<T> lastElement(List<T> list) {
		return isNullOrEmpty(list) ? None.<T>instance() : new Some<T>(list.get(list.size() - 1));
	}
	
}
