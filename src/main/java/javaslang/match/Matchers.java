/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.match;

import java.util.function.Function;

/**
 * Extension methods for {@link Matcher}.
 */
public final class Matchers {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private Matchers() {
		throw new AssertionError(Matchers.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Shortcut for {@code new Matcher<>().caze(function)}.
	 * 
	 * @param <S> type of the object to be matched
	 * @param <T> return type of the matcher function
	 * @param function A function which is applied to a matched object.
	 * @return A Matcher of type T
	 */
	public static <S, T> Matcher<T> caze(Function<S, T> function) {
		return new Matcher<T>().caze(function);
	}

	/**
	 * Shortcut for {@code new Matcher<>().caze(prototype, function)}.
	 * 
	 * @param <S> type of the prototype object
	 * @param <T> return type of the matcher function
	 * @param prototype An object which matches by equality.
	 * @param function A function which is applied to a matched object.
	 * @return A Matcher of type T
	 */
	public static <S, T> Matcher<T> caze(S prototype, Function<S, T> function) {
		return new Matcher<T>().caze(prototype, function);
	}

}
