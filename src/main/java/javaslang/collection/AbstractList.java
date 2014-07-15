/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static java.util.stream.Collectors.joining;
import javaslang.Stringz;

/**
 * 
 *
 * @param <T>
 */
public abstract class AbstractList<T> implements List<T> {

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
				final boolean isEqual = (head1 == null) ? head2 == null : head1.equals(head2);
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
		for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
			final T element = list.head();
			hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return stream().map(Stringz::toString).collect(joining(", ", "(", ")"));
	}

}
