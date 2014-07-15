/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static java.util.stream.Collectors.joining;
import javaslang.Stringz;

/**
 * Non-empty List.
 * 
 * TODO: javadoc
 *
 * @param <T> Component type of the List.
 */
public class LinearList<T> extends AbstractList<T> {

	private final T head;
	private final List<T> tail;

	LinearList(T head, List<T> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public T head() {
		return head;
	}

	@Override
	public List<T> tail() {
		return tail;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
