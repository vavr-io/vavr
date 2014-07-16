/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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

	/**
	 * Example: {@code List.of(1,2,3).appendAll(List.of(4,5,6))} equals {@code List.of(1,2,3,4,5,6)}
	 * .
	 * 
	 * @param elements Elements to be appended.
	 * @return A list containing the given elements appended to this list.
	 */
	default List<T> appendAll(List<? extends T> elements) {
		requireNonNull(elements, "elements is null");
		return this.reverse().prependAll(elements.reverse()).reverse();
	}

	default List<T> prepend(T element) {
		return new LinearList<>(element, this);
	}

	/**
	 * Example: {@code List.of(4,5,6).prependAll(List.of(1,2,3))} equals
	 * {@code List.of(1,2,3,4,5,6)}.
	 * 
	 * @param elements Elements to be prepended.
	 * @return A list containing the given elements prepended to this list.
	 */
	default List<T> prependAll(List<? extends T> elements) {
		requireNonNull(elements, "elements is null");
		List<T> result = this;
		for (List<? extends T> list = elements.reverse(); !list.isEmpty(); list = list.tail()) {
			result = result.prepend(list.head());
		}
		return result;
	}

	// TODO: insert(index, element)

	// TODO: insertAll

	// TODO: remove(element)

	// TODO: removeAll

	// TODO: retainAll

	default List<T> sort() {
		return stream().sorted().collect(List.collector());
	}

	default List<T> sort(Comparator<? super T> c) {
		return stream().sorted(c).collect(List.collector());
	}

	static <E> Collector<E, List<E>, List<E>> collector() {
		return new CollectorImpl<E, List<E>, List<E>>(//
				List::empty, // supplier
				List::prepend, // accumulator
				(left, right) -> left.prependAll(right), // combiner
				List::reverse, // finisher
				Characteristics.IDENTITY_FINISH);
	}

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

	default List<T> set(int index, T element) {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("set(" + index + ") on empty list");
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException("set(" + index + ")");
		}
		// TODO: IndexOutOfBounds("set(index, ...)
		return this.sublist(index + 1).prepend(element).prependAll(sublist(0, index));
	}

	default List<T> sublist(int beginIndex) {
		if (beginIndex < 0) {
			throw new IndexOutOfBoundsException("sublist(" + beginIndex + ")");
		}
		List<T> result = this;
		for (int i = 0; i < beginIndex; i++) {
			result = result.tail();
			if (result.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format("sublist(%s) on list of size %s",
						beginIndex, beginIndex - i));
			}
		}
		return result;
	}

	default List<T> sublist(int beginIndex, int endIndex) {
		final int subLen = endIndex - beginIndex;
		if (beginIndex < 0 || subLen < 0) {
			throw new IndexOutOfBoundsException(String.format("sublist(%s, %s) on list of size %2",
					beginIndex, endIndex, size()));
		}
		List<T> result = EmptyList.instance();
		List<T> list = this.sublist(beginIndex);
		for (int i = 0; i < subLen; i++, list = list.tail()) {
			if (list.isEmpty()) {
				throw new IndexOutOfBoundsException(String.format(
						"sublist(%s, %s) on list of size %2", beginIndex, endIndex, beginIndex
								+ (subLen - i)));
			}
			result = result.prepend(list.head());
		}
		return result.reverse();
	}

	/**
	 * Drops the first n elements of this list or the whole list, if this size &lt; n.
	 * 
	 * @param n The number of elements to drop.
	 * @return A list consisting of all elements of this list except the first n ones, or else the
	 *         empty list, if this list has less than n elements.
	 */
	default List<T> drop(int n) {
		List<T> result = this;
		for (int i = 0; i < n && !result.isEmpty(); i++, result = result.tail())
			;
		return result;
	}

	/**
	 * Takes the first n elements of this list or the whole list, if this size &lt; n.
	 * 
	 * @param n The number of elements to take.
	 * @return A list consisting of the first n elements of this list or the whole list, if it has
	 *         less than n elements.
	 */
	default List<T> take(int n) {
		List<T> result = EmptyList.instance();
		List<T> list = this;
		for (int i = 0; i < n && !list.isEmpty(); i++, list = list.tail()) {
			result.prepend(list.head());
		}
		return result.reverse();
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

}
