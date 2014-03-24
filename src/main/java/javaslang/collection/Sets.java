/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.collection;

import static javaslang.lang.Lang.require;

import java.util.HashSet;
import java.util.Set;

/**
 * Extension methods for {@link java.util.Set}.
 */
public class Sets {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private Sets() {
		throw new AssertionError(Sets.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Creates a Set containing the given elements.
	 * <p>
	 * Shortcut for
	 * 
	 * <pre>
	 * <code>
	 * final Set&lt;T&gt; result = new HashSet&lt;&gt;();
	 * result.add(element_1);
	 * result.add(element_2);
	 * ...
	 * result.add(element_n);
	 * </code>
	 * </pre>
	 * 
	 * where T is a common type of the given elements.
	 * 
	 * @param <T> Type of elements.
	 * @param elements A varargs of elements.
	 * @return A Set containing the given elements.
	 */
	@SafeVarargs
	public static <T> Set<T> newSet(T... elements) {
		final Set<T> result = new HashSet<>(elements.length);
		for (T element : elements) {
			result.add(element);
		}
		return result;
	}

	/**
	 * Relative complement of sets, subtracting elements <code>set1 - set2</code>. Returns a new
	 * set, does not modify set1 and set2.
	 * 
	 * @param <T> type of Set elements.
	 * @param set1 Starting set.
	 * @param set2 Elements to be removed from set1.
	 * @return All elements of set1 which are not in set2.
	 * @throws IllegalStateException if set1 or set2 is null.
	 */
	public static <T> Set<T> complement(Set<T> set1, Set<T> set2) {
		require(set1 != null, "set1 is null");
		require(set2 != null, "set2 is null");
		final Set<T> result = new HashSet<>(set1.size() + set2.size());
		result.addAll(set1);
		result.removeAll(set2);
		return result;
	}

	/**
	 * Intersection of sets <code>set1</code> and <code>set2</code>. Returns a new set, does not
	 * modify set1 and set2.
	 * 
	 * @param <T> type of Set elements.
	 * @param set1 A set.
	 * @param set2 Another set.
	 * @return All elements which are in both of the given sets, set1 and set2.
	 * @throws IllegalStateException if set1 or set2 is null.
	 */
	public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
		require(set1 != null, "set1 is null");
		require(set2 != null, "set2 is null");
		final Set<T> result = new HashSet<>(set1.size() + set2.size());
		result.addAll(set1);
		result.retainAll(set2);
		return result;
	}

	/**
	 * Union of sets, adding <code>set1 + set2</code>. Returns a new set, does not modify set1 and
	 * set2.
	 * 
	 * @param <T> type of Set elements
	 * @param set1 A set.
	 * @param set2 Another set.
	 * @return The union of the given sets.
	 * @throws IllegalStateException if set1 or set2 is null.
	 */
	public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
		require(set1 != null, "set1 is null");
		require(set2 != null, "set2 is null");
		final Set<T> result = new HashSet<>(set1.size() + set2.size());
		result.addAll(set1);
		result.addAll(set2);
		return result;
	}

}
