package javaslang.util;

import java.util.function.Function;

public final class Matchers {

	private Matchers() {
		throw new AssertionError(Matchers.class.getName()
				+ " cannot be instantiated.");
	}

	/** Shortcut for <code>new Matcher<>().caze(function)</code>. */
	public static <S, T> Matcher<T> caze(Function<S, T> function) {
		return new Matcher<T>().caze(function);
	}

	/** Shortcut for <code>new Matcher<>().caze(prototype, function)</code>. */
	public static <S, T> Matcher<T> caze(S prototype, Function<S, T> function) {
		return new Matcher<T>().caze(prototype, function);
	}

}
