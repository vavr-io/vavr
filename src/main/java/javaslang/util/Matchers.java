package javaslang.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Matchers {

	private Matchers() {
		throw new AssertionError(Matchers.class.getName()
				+ " cannot be instantiated.");
	}

	/** Shortcut for <code>new Matcher<>().caze(function)</code>. */
	public static <S, T> Matcher<T> caze(Function<S, T> function) {
		return new Matcher<T>().caze(function);
	}

	/** Shortcut for <code>new Matcher<>().caze(consumer)</code>. */
	public static <S> Matcher<Void> caze(Consumer<S> consumer) {
		return new Matcher<Void>().caze(consumer);
	}

	/** Shortcut for <code>new Matcher<>().caze(supplier)</code>. */
	public static <T> Matcher<T> caze(Supplier<T> supplier) {
		return new Matcher<T>().caze(supplier);
	}

	/** Shortcut for <code>new Matcher<>().caze(prototype, function)</code>. */
	public static <S, T> Matcher<T> caze(S prototype, Function<S, T> function) {
		return new Matcher<T>().caze(prototype, function);
	}

	/** Shortcut for <code>new Matcher<>().caze(prototype, consumer)</code>. */
	public static <S, T> Matcher<T> caze(S prototype, Consumer<S> consumer) {
		return new Matcher<T>().caze(prototype, consumer);
	}

	/** Shortcut for <code>new Matcher<>().caze(prototype, supplier)</code>. */
	public static <S, T> Matcher<T> caze(S prototype, Supplier<T> supplier) {
		return new Matcher<T>().caze(prototype, supplier);
	}

}
