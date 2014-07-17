/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.List.AbstractList;

/**
 * Non-empty List.
 * 
 * TODO: javadoc
 *
 * @param <E> Component type of the List.
 */
public class LinearList<E> extends AbstractList<E> {

	private final E head;
	private final List<E> tail;

	public LinearList(E head, List<E> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public E head() {
		return head;
	}

	@Override
	public List<E> tail() {
		return tail;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
