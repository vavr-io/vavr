package javaslang.collection;

import static javaslang.lang.Lang.require;

import java.util.HashSet;
import java.util.Set;

public class Sets {

	private Sets() {
		throw new AssertionError(Sets.class.getName() + " cannot be instantiated.");
	}
	
	@SafeVarargs
	public static <T> Set<T> newSet(T... elements) {
		final Set<T> result = new HashSet<>(elements.length);
		for(T element : elements) {
			result.add(element);
		}
		return result;
	}
	
	/**
	 * Relative complement of sets, subtracting elements <code>set1 - set2</code>. Returns a new set, does not modify
	 * set1 and set2.
	 * 
	 * @param set1 Starting set.
	 * @param set2 Elements to be removed from set1.
	 * @return All element of set1 which are not in set2.
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
	 * Intersection of sets <code>set1</code> and <code>set2</code>. Returns a new set, does not modify set1 and set2.
	 * 
	 * @param set1 A set.
	 * @param set2 Another set.
	 * @return All elements which are in both of the given sets, set1 and set2.
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
	 * Union of sets, adding <code>set1 + set2</code>. Returns a new set, does not modify set1 and set2.
	 * 
	 * @param set1 A set.
	 * @param set2 Another set.
	 * @return The union of the given sets.
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
