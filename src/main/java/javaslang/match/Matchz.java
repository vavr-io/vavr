/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static javaslang.Lang.requireNotInstantiable;
import javaslang.match.Match.SerializableFunction;

/**
 * Extension methods for {@link Match}.
 */
public final class Matchz {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Matchz() {
		requireNotInstantiable();
	}

	/**
	 * Shortcut for {@code new Match<>().caze(function)}.
	 * 
	 * @param <S> type of the object to be matched
	 * @param <T> return type of the matcher function
	 * @param function A function which is applied to a matched object.
	 * @return A Match of type T
	 */
	public static <S, T> Match<T> caze(SerializableFunction<S, T> function) {
		return new Match<T>().caze(function);
	}

	/**
	 * Shortcut for {@code new Match<>().caze(prototype, function)}.
	 * 
	 * @param <S> type of the prototype object
	 * @param <T> return type of the matcher function
	 * @param prototype An object which matches by equality.
	 * @param function A function which is applied to a matched object.
	 * @return A Match of type T
	 */
	public static <S, T> Match<T> caze(S prototype, SerializableFunction<S, T> function) {
		return new Match<T>().caze(prototype, function);
	}

}
