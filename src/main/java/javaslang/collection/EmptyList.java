/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * The empty List.
 *
 * TODO: javadoc
 * 
 * @param <T> Component type of the List.
 */
public final class EmptyList<T> implements List<T> {
	
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
	public T head() {
		throw new UnsupportedOperationException("head of empty list");
	}
	
	@Override
	public List<T> tail() {
		throw new UnsupportedOperationException("tail of empty list");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
	
}
