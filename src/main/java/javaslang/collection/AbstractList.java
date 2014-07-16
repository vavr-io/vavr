/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static java.util.stream.Collectors.joining;

import java.util.Objects;

import javaslang.Strings;

/**
 * This class is needed because interface List cannot override Object's methods equals, hashCode and
 * toString.
 * <p>
 * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to override Object's methods</a>.
 *
 * @param <T> Component type of the List.
 */
abstract class AbstractList<T> implements List<T> {

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
		for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
			final T element = list.head();
			hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return stream().map(Strings::toString).collect(joining(", ", "(", ")"));
	}

}
