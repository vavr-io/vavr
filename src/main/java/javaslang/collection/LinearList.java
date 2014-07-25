/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

import javaslang.collection.List.AbstractList;

/**
 * Non-empty List.
 * 
 * @param <E> Component type of the List.
 */
// DEV NOTE: A future implementation of Cloneable clone() could be new LinearList(head, tail).
public class LinearList<E> extends AbstractList<E> implements Serializable {

	private static final long serialVersionUID = 2734262077812389412L;

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
