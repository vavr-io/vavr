/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static java.util.stream.Collectors.joining;
import static javaslang.Lang.requireNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javaslang.Strings;

/**
 * TODO: javadoc
 * 
 * @param <E> Component type of the List.
 */
public interface List<E> extends Iterable<E> {

	/**
	 * Returns the first element of this List in O(1).
	 * 
	 * @return The head of this List.
	 * @throws UnsupportedOperationException if this is EmptyList.
	 */
	E head();

	/**
	 * Returns all elements except the first element of this List in O(1).
	 * 
	 * @return The tail of this List.
	 * @throws UnsupportedOperationException if this is EmptyList.
	 */
	List<E> tail();

	/**
	 * Tests whether this List is empty in O(1).
	 * 
	 * @return true, if this List is empty, false otherwise.
	 */
	boolean isEmpty();

	/**
	 * Reverses this List and returns a new List in O(n).
	 * <p>
	 * The result is equivalent to {@code isEmpty() ? 0 : 1 + tail().size()} but implemented without
	 * recursion.
	 * 
	 * @return A new List containing the elements of this List in reverse order.
	 */
	default List<E> reverse() {
		List<E> result = EmptyList.instance();
		for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
			result = result.prepend(list.head());
		}
		return result;
	}

	/**
	 * Calculates the size of a List in O(n).
	 * <p>
	 * The result is equivalent to {@code isEmpty() ? 0 : 1 + tail().size()} but implemented without
	 * recursion.
	 * 
	 * @return The size of this List.
	 */
	default int size() {
		int result = 0;
		for (List<E> list = this; !list.isEmpty(); list = list.tail(), result++)
			;
		return result;
	}

	/**
	 * Appends an element to this List in O(n).
	 * <p>
	 * The result is equivalent to {@code reverse().prepend(element).reverse()}.
	 * 
	 * @param element An element.
	 * @return A new List containing the elements of this list, appended the given element.
	 */
	default List<E> append(E element) {
		if (isEmpty()) {
			return new LinearList<>(element, this);
		} else {
			return reverse().prepend(element).reverse();
		}
	}

	/**
	 * Appends all elements of a given List to this List in O(n).
	 * <p>
	 * Example: {@code List.of(1,2,3).appendAll(List.of(4,5,6))} equals {@code List.of(1,2,3,4,5,6)}
	 * .
	 * <p>
	 * The result is equivalent to {@code elements.prependAll(this)}.
	 * 
	 * @param elements Elements to be appended.
	 * @return A new List containing the given elements appended to this List.
	 * @throws javaslang.Lang.UnsatisfiedRequirementException if elements is null
	 */
	@SuppressWarnings("unchecked")
	default List<E> appendAll(List<? extends E> elements) {
		requireNonNull(elements, "elements is null");
		if (isEmpty()) {
			return (List<E>) elements;
		} else if (elements.isEmpty()) {
			return this;
		} else {
			return ((List<E>) elements).prependAll(this);
		}
	}

	/**
	 * Prepends an element to this List in O(1).
	 * <p>
	 * The result is equivalent to {@code new LinearList<>(element, this)}.
	 * 
	 * @param element An element.
	 * @return A new List containing the elements of this list, prepended the given element.
	 */
	default List<E> prepend(E element) {
		return new LinearList<>(element, this);
	}

	/**
	 * Prepends all elements of a given List to this List in O(n).
	 * <p>
	 * Example: {@code List.of(4,5,6).prependAll(List.of(1,2,3))} equals
	 * {@code List.of(1,2,3,4,5,6)}.
	 * <p>
	 * The result is equivalent to
	 * {@code elements.isEmpty() ? this : prependAll(elements.tail()).prepend(elements.head())} but
	 * implemented without recursion.
	 * 
	 * @param elements Elements to be prepended.
	 * @return A new List containing the given elements prepended to this List.
	 * @throws javaslang.Lang.UnsatisfiedRequirementException if elements is null
	 */
	@SuppressWarnings("unchecked")
	default List<E> prependAll(List<? extends E> elements) {
		requireNonNull(elements, "elements is null");
		if (isEmpty()) {
			return (List<E>) elements;
		} else if (elements.isEmpty()) {
			return this;
		} else {
			List<E> result = this;
			for (List<? extends E> list = elements.reverse(); !list.isEmpty(); list = list.tail()) {
				result = result.prepend(list.head());
			}
			return result;
		}
	}

	/**
	 * TODO
	 * 
	 * ().insert(0, 1) = (1)
	 * (4).insert(0, 1) = (1,4)
	 * (4).insert(1, 1) = (4,1)
	 * (1,2,3).insert(2, 4) = (1,2,4,3)
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	default List<E> insert(int index, E element) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("insert(" + index + ", e)");
		}
		List<E> result = EmptyList.instance();
		List<E> list = this;
		for (int i = index; i > 0; i--, list = list.tail()) {
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException("insert("
						+ index
						+ ", e) on list of size "
						+ size());
			}
			result = result.prepend(list.head());
		}
		return list.prepend(element).prependAll(result.reverse());
	}

	/**
	 * TODO
	 * 
	 * ().insertAll(0, (1,2,3)) = (1,2,3)
	 * (4).insertAll(0, (1,2,3)) = (1,2,3,4)
	 * (4).insertAll(1, (1,2,3)) = (4,1,2,3)
	 * (1,2,3).insertAll(2, (4,5)) = (1,2,4,5,3)
	 * 
	 * @param index
	 * @param elements
	 * @return
	 */
	default List<E> insertAll(int index, List<? extends E> elements) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
		}
		List<E> result = EmptyList.instance();
		List<E> list = this;
		for (int i = index; i > 0; i--, list = list.tail()) {
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException("insertAll("
						+ index
						+ ", elements) on list of size "
						+ size());
			}
			result = result.prepend(list.head());
		}
		return list.prependAll(elements).prependAll(result.reverse());
	}

	// TODO: remove(element)

	// TODO: removeAll(List)

	// TODO: retainAll(List)

	// TODO: replaceAll(List)

	// TODO: T[] toArray(T[] a)

	// TODO: clear

	// TODO: iterator(int)

	/**
	 * Tests if this List contains a given value as an element in O(n).
	 * <p>
	 * The result is equivalent to {@code indexOf(element) != -1}.
	 * 
	 * @param element An Object of type E, may be null.
	 * @return true, if element is in this List, false otherwise.
	 */
	default boolean contains(E element) {
		return indexOf(element) != -1;
	}

	/**
	 * Tests if this List contains all given values as elements in O(n^2).
	 * <p>
	 * The result is equivalent to
	 * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())}
	 * but implemented without recursion.
	 * 
	 * @param elements A List of values of type E.
	 * @return true, if this List contains all given elements, false otherwise.
	 * @throws javaslang.Lang.UnsatisfiedRequirementException if elements is null
	 */
	default boolean containsAll(List<? extends E> elements) {
		requireNonNull(elements, "elements is null");
		for (List<? extends E> list = elements; !list.isEmpty(); list = list.tail()) {
			if (!this.contains(list.head())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the index of the given element in O(n). The result is -1, if the element is not
	 * contained.
	 * <p>
	 * The result is equivalent to {@code head().equals(element) ? 0 : 1 + tail().indexOf(element)}
	 * but implemented without recursion.
	 * 
	 * @param element An Object of type E, may be null.
	 * @return The index of element or -1.
	 */
	default int indexOf(E element) {
		int index = 0;
		for (List<E> list = this; !list.isEmpty(); list = list.tail(), index++) {
			if (Objects.equals(list.head(), element)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the last index of the given element in O(n). The result is -1, if the element is not
	 * contained.
	 * <p>
	 * The result is equivalent to
	 * {@code (reverse().indexOf(element) == -1) ? -1 : size() - reverse().indexOf(element)} but
	 * implemented without recursion.
	 * 
	 * @param element An Object of type E, may be null.
	 * @return The index of element or -1.
	 */
	default int lastIndexOf(E element) {
		int result = -1, index = 0;
		for (List<E> list = this; !list.isEmpty(); list = list.tail(), index++) {
			if (Objects.equals(list.head(), element)) {
				result = index;
			}
		}
		return result;
	}

	/**
	 * Returns the element of this List at the specified index in O(n).
	 * <p>
	 * The result is roughly equivalent to {@code (index == 0) ? head() : tail().get(index - 1)} but
	 * implemented without recursion.
	 * 
	 * @param index An index, where 0 &lt;= index &lt; this.size()
	 * @return The element at the specified index.
	 * @throws IndexOutOfBoundsException if this List is empty, index &lt; 0 or index &gt;= size of
	 *             this List.
	 */
	default E get(int index) {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("get(" + index + ") on empty list");
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException("get(" + index + ")");
		}
		List<E> list = this;
		for (int i = index - 1; i >= 0; i--) {
			list = list.tail();
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format("get(%s) on list of size %s",
						index, index - i));
			}
		}
		return list.head();
	}

	/**
	 * Replaces the element at the specified index in O(n).
	 * <p>
	 * The result is roughly equivalent to
	 * {@code (index == 0) ? tail().prepend(element) : new LinearList(head(), tail().set(index - 1, element))}
	 * but implemented without recursion.
	 * 
	 * @param index An index, where 0 &lt;= index &lt; this.size()
	 * @param element A new element.
	 * @return A list containing all of the elements of this List but the given element at the given
	 *         index.
	 * @throws IndexOutOfBoundsException if this List is empty, index &lt; 0 or index &gt;= size of
	 *             this List.
	 */
	default List<E> set(int index, E element) {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("set(" + index + ", e) on empty list");
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException("set(" + index + ", e)");
		}
		List<E> result = EmptyList.instance();
		List<E> list = this;
		for (int i = index; i > 0; i--, list = list.tail()) {
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException("set("
						+ index
						+ ", e) on list of size "
						+ size());
			}
			result = result.prepend(list.head());
		}
		if (list.isEmpty()) {
			throw new IndexOutOfBoundsException("set(" + index + ", e) on list of size " + size());
		}
		// skip the current head element because it is replaced
		return list.tail().prepend(element).prependAll(result.reverse());
	}

	/**
	 * Returns a new List which contains all elements starting at beginIndex (inclusive).
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>{@code List.empty().sublist(0)} returns {@code List.empty()}</li>
	 * <li>{@code List.of(1).sublist(0)} returns {@code List.of(1)}</li>
	 * <li>{@code List.of(1).sublist(1)} returns {@code List.empty()}</li>
	 * <li>{@code List.of(1,2,3).sublist(1)} returns {@code List.of(2,3)}</li>
	 * <li>{@code List.of(1,2,3).sublist(3)} returns {@code List.empty()}</li>
	 * </ul>
	 * <p>
	 * The following calls are illegal:
	 * <ul>
	 * <li>{@code List.empty().sublist(1)} throws</li>
	 * <li>{@code List.of(1,2,3).sublist(-1)} throws}</li>
	 * <li>{@code List.of(1,2,3).sublist(4)} throws}</li>
	 * </ul>
	 * <p>
	 * The result is equivalent to {@code (index == 0) ? this : tail().sublist(index - 1)} but
	 * implemented without recursion.
	 * <p>
	 * If you do not want the bounds to be checked, use the fail-safe variant
	 * {@code drop(beginIndex)} instead.
	 * 
	 * @param beginIndex Start index of the sublist, where 0 &lt;= beginIndex &lt;= size()
	 * @return The sublist of the List, starting at beginIndex (inclusive).
	 * @see #drop(int)
	 * @see #take(int)
	 */
	default List<E> sublist(int beginIndex) {
		if (beginIndex < 0) {
			throw new IndexOutOfBoundsException("sublist(" + beginIndex + ")");
		}
		List<E> result = this;
		for (int i = 0; i < beginIndex; i++, result = result.tail()) {
			if (result.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format("sublist(%s) on list of size %s",
						beginIndex, i));
			}
		}
		return result;
	}

	/**
	 * Returns a new List which contains the elements from beginIndex (inclusive) to endIndex
	 * (exclusive) of this List.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>{@code List.empty().sublist(0,0)} returns {@code List.empty()}</li>
	 * <li>{@code List.of(1).sublist(0,0)} returns {@code List.empty()}</li>
	 * <li>{@code List.of(1).sublist(0,1)} returns {@code List.of(1)}</li>
	 * <li>{@code List.of(1).sublist(1,1)} returns {@code List.empty()}</li>
	 * <li>{@code List.of(1,2,3).sublist(1,3)} returns {@code List.of(2,3)}</li>
	 * <li>{@code List.of(1,2,3).sublist(3,3)} returns {@code List.empty()}</li>
	 * </ul>
	 * <p>
	 * The following calls are illegal:
	 * <ul>
	 * <li>{@code List.of(1,2,3).sublist(1,0)} throws}</li>
	 * <li>{@code List.of(1,2,3).sublist(-1,2)} throws}</li>
	 * <li>{@code List.of(1,2,3).sublist(1,4)} throws}</li>
	 * </ul>
	 * <p>
	 * The result is equivalent to
	 * {@code (beginIndex == 0) ? reverse().sublist(size() - endIndex).reverse() : tail().sublist(beginIndex - 1, endIndex)}
	 * but implemented without recursion.
	 * <p>
	 * If you do not want the bounds to be checked, use the fail-safe variant
	 * {@code drop(beginIndex).take(endIndex - beginIndex)} instead.
	 * 
	 * @param beginIndex Start index of the sublist, where 0 &lt;= beginIndex &lt;= size()
	 * @param endIndex End index of the sublist, where beginIndex &lt;= endIndex &lt;= size()
	 * @return The sublist of the List, starting at beginIndex (inclusive) and ending at endIndex
	 *         (exclusive).
	 * @see #drop(int)
	 * @see #take(int)
	 */
	default List<E> sublist(int beginIndex, int endIndex) {
		if (beginIndex < 0 || endIndex - beginIndex < 0) {
			throw new IndexOutOfBoundsException(String.format("sublist(%s, %s) on list of size %s",
					beginIndex, endIndex, size()));
		}
		List<E> result = EmptyList.instance();
		List<E> list = this;
		for (int i = 0; i < endIndex; i++, list = list.tail()) {
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format(
						"sublist(%s, %s) on list of size %s", beginIndex, endIndex, i));
			}
			if (i >= beginIndex) {
				result = result.prepend(list.head());
			}
		}
		return result.reverse();
	}

	/**
	 * Drops the first n elements of this list or the whole list, if this size &lt; n or n &lt; 0;
	 * <p>
	 * The result is equivalent to {@code sublist(n)} but does not throw if n &lt; 0 or n &gt;
	 * this.size().
	 * 
	 * @param n The number of elements to drop.
	 * @return A list consisting of all elements of this list except the first n ones, or else the
	 *         empty list, if this list has less than n elements.
	 */
	default List<E> drop(int n) {
		List<E> result = this;
		for (int i = 0; i < n && !result.isEmpty(); i++, result = result.tail())
			;
		return result;
	}

	/**
	 * Takes the first n elements of this list or the whole list, if this size &lt; n.
	 * <p>
	 * The result is equivalent to {@code sublist(0, n)} but does not throw if n &lt; 0 or n &gt;
	 * this.size().
	 * 
	 * @param n The number of elements to take.
	 * @return A list consisting of the first n elements of this list or the whole list, if it has
	 *         less than n elements.
	 */
	default List<E> take(int n) {
		List<E> result = EmptyList.instance();
		List<E> list = this;
		for (int i = 0; i < n && !list.isEmpty(); i++, list = list.tail()) {
			result.prepend(list.head());
		}
		return result.reverse();
	}

	default E[] toArray() {
		@SuppressWarnings("unchecked")
		final E[] result = (E[]) new Object[size()];
		// TODO: return toArray(result);
		int i = 0;
		for (List<E> list = this; !list.isEmpty(); list = list.tail(), i++) {
			result[i] = list.head();
		}
		return result;
	}
	
	default E[] toArray(E[] array) {
		// TODO
		return null;
	}

	default java.util.ArrayList<E> toArrayList() {
		final java.util.ArrayList<E> result = new java.util.ArrayList<>();
		for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
			result.add(list.head());
		}
		return result;
	}

	default List<E> sort() {
		return stream().sorted().collect(List.collector());
	}

	default List<E> sort(Comparator<? super E> c) {
		return stream().sorted(c).collect(List.collector());
	}

	default Stream<E> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	default Stream<E> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#spliterator()
	 */
	@Override
	default Spliterator<E> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED
				| Spliterator.IMMUTABLE);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	default Iterator<E> iterator() {

		final class ListIterator implements Iterator<E> {

			List<E> list;

			ListIterator(List<E> list) {
				requireNonNull(list, "list is null");
				this.list = list;
			}

			@Override
			public boolean hasNext() {
				return !list.isEmpty();
			}

			@Override
			public E next() {
				if (list.isEmpty()) {
					throw new NoSuchElementException();
				} else {
					final E result = list.head();
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
	 * Returns the single instance of EmptyList. Convenience method for {@code EmptyList.instance()}
	 * .
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
	 * <code>  List.of(1, 2, 3, 4)
	 * = EmptyList.instance().prepend(4).prepend(3).prepend(2).prepend(1)
	 * = new LinearList(1, new LinearList(2, new LinearList(3, new LinearList(4, EmptyList.instance()))))</code>
	 * </pre>
	 *
	 * @param <T> Component type of the List.
	 * @param elements List elements.
	 * @return A list containing the given elements in the same order.
	 * @throws javaslang.Lang.UnsatisfiedRequirementException if elements is null
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

	static <T> Collector<T, List<T>, List<T>> collector() {
		return new CollectorImpl<T, List<T>, List<T>>(//
				List::empty, // supplier
				List::prepend, // accumulator
				(left, right) -> left.prependAll(right), // combiner
				List::reverse, // finisher
				Characteristics.IDENTITY_FINISH);
	}

	/**
	 * Simple implementation class for {@code Collector}.
	 *
	 * @param <T> the type of elements to be collected
	 * @param <A> the type of the accumulator
	 * @param <R> the type of the result
	 */
	static class CollectorImpl<T, A, R> implements Collector<T, A, R> {

		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator,
				BinaryOperator<A> combiner, Function<A, R> finisher,
				Characteristics characteristics1, Characteristics... characteristics2) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = Collections.unmodifiableSet(EnumSet.of(characteristics1,
					characteristics2));
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}
	}

	/**
	 * This class is needed because the interface {@link List} cannot use default methods to
	 * override Object's non-final methods equals, hashCode and toString.
	 * <p>
	 * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow
	 * default methods to override Object's methods</a>.
	 *
	 * @param <E> Component type of the List.
	 */
	abstract class AbstractList<E> implements List<E> {

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof List)) {
				return false;
			} else {
				List<?> list1 = this;
				List<?> list2 = (List<?>) o;
				while (!list1.isEmpty() && !list2.isEmpty()) {
					final Object head1 = list1.head();
					final Object head2 = list2.head();
					final boolean isEqual = Objects.equals(head1, head2);
					if (!isEqual) {
						return false;
					}
					list1 = list1.tail();
					list2 = list2.tail();
				}
				final boolean isSameSize = list1.isEmpty() && list2.isEmpty();
				return isSameSize;
			}
		}

		@Override
		public int hashCode() {
			int hashCode = 1;
			for (List<E> list = this; !list.isEmpty(); list = list.tail()) {
				final E element = list.head();
				hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
			}
			return hashCode;
		}

		@Override
		public String toString() {
			return stream().map(Strings::toString).collect(joining(", ", "(", ")"));
		}

	}

}
