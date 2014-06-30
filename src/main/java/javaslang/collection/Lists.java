/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.List;

import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

/**
 * Extension methods for {@link java.util.List}.
 * <p>
 * For list creation, see {@link java.util.Arrays#asList(Object...)} and
 * {@link java.util.Collections#emptyList()}.<br>
 * <strong>Note:</strong> If the list is an array of primitive component type, e.g. int[], the
 * {@link java.util.Arrays#asList(Object...)} is not sufficient, because the array is treated as
 * object. In this case please use one of the asList methods provided by
 * {@link javaslang.lang.ArrayExtensions}, e.g. {@link javaslang.lang.ArrayExtensions#asList(int[])}.
 * 
 * @see java.util.Collections
 */
public final class Lists {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private Lists() {
		throw new AssertionError(Lists.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Returns an {@link javaslang.option.Option} of the first element of the given list. If the
	 * given list is null or empty, {@link javaslang.option.None} is returned, otherwise
	 * {@link javaslang.option.Some} containing the first element.
	 * 
	 * @param <T> type of list elements
	 * @param list A List, may be null or empty.
	 * @return None, if the list is null or empty, otherwise Some containing the first element.
	 */
	public static <T> Option<T> firstElement(List<T> list) {
		return CollectionExtensions.isNullOrEmpty(list) ? None.<T> instance() : new Some<T>(list.get(0));
	}

	/**
	 * Returns an {@link javaslang.option.Option} of the last element of the given list. If the
	 * given list is null or empty, {@link javaslang.option.None} is returned, otherwise
	 * {@link javaslang.option.Some} containing the last element.
	 * 
	 * @param <T> type of list elements
	 * @param list A List, may be null or empty.
	 * @return None, if the list is null or empty, otherwise Some containing the last element.
	 */
	public static <T> Option<T> lastElement(List<T> list) {
		return CollectionExtensions.isNullOrEmpty(list) ? None.<T> instance() : new Some<T>(list.get(list
				.size() - 1));
	}
	
	/**
	 * Shortcut for {@code list.toArray(new T[list.size()])}.
	 * 
	 * @param <T> Element type.
	 * @param list A List containing elements of type T.
	 * @return An array containing the elements of the given list in the same order.
	 */
	public static <T> T[] toArray(List<T> list) {
		@SuppressWarnings("unchecked")
		final T[] array = (T[]) new Object[list.size()];
		return list.toArray(array);
	}

}
