/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static javaslang.Lang.requireNotInstantiable;

import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import javaslang.match.Match.BooleanFunction;
import javaslang.match.Match.ByteFunction;
import javaslang.match.Match.CharFunction;
import javaslang.match.Match.FloatFunction;
import javaslang.match.Match.SerializableFunction;
import javaslang.match.Match.ShortFunction;

/**
 * Extension methods for {@link Match}.
 */
public final class Matchs {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Matchs() {
		requireNotInstantiable();
	}

	/**
	 * Shortcut for {@code new Match.Builder<T>().caze(function)}.
	 * 
	 * @param <S> type of the object to be matched
	 * @param <T> return type of the matcher function
	 * @param function A function which is applied to a matched object.
	 * @return A Match of type T
	 */
	public static <S, T> Match.Builder<T> caze(SerializableFunction<S, T> function) {
		return new Match.Builder<T>().caze(function);
	}

	/**
	 * Shortcut for {@code new Match.Builder<T>().caze(prototype, function)}.
	 * 
	 * @param <S> type of the prototype object
	 * @param <T> return type of the matcher function
	 * @param prototype An object which matches by equality.
	 * @param function A function which is applied to a matched object.
	 * @return A Match of type T
	 */
	public static <S, T> Match.Builder<T> caze(S prototype, SerializableFunction<S, T> function) {
		return new Match.Builder<T>().caze(prototype, function);
	}
	
	public static <T> Match.Builder<T> caze(BooleanFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(ByteFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(CharFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(DoubleFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(FloatFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(IntFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(LongFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

	public static <T> Match.Builder<T> caze(ShortFunction<T> function) {
		return new Match.Builder<T>().caze(function);
	}

}
