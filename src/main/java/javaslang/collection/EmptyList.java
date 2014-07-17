/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.List.AbstractList;

/**
 * The empty List.
 *
 * TODO: javadoc
 * 
 * @param <E> Component type of the List.
 */
public final class EmptyList<E> extends AbstractList<E> {
	
	private static final EmptyList<?> INSTANCE = new EmptyList<>();
	
	// hidden
	private EmptyList() {
	}
	
	public static <T> EmptyList<T> instance() {
		@SuppressWarnings("unchecked")
		final EmptyList<T> instance = (EmptyList<T>) INSTANCE;
		return instance;
	}
	
	@Override
	public E head() {
		throw new UnsupportedOperationException("head of empty list");
	}
	
	@Override
	public List<E> tail() {
		throw new UnsupportedOperationException("tail of empty list");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
	
}
