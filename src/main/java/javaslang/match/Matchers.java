package javaslang.match;

import java.util.function.Function;

/**
 * This class contains static shortcuts for {@link Matcher} creation.
 */
public final class Matchers {

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
