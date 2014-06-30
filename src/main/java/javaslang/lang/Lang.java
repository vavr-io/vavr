/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lang;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * General Java languange extensions.
 */
public final class Lang {

	private Lang() {
		throw new AssertionError(Lang.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Shortcut for {@code System.out.print(javaslang.text.Strings.toString(o))} .
	 * 
	 * @param o an Object
	 */
	public static void print(Object o) {
		final String s = Strings.toString(o);
		System.out.print(s);
	}

	/**
	 * Shortcut for {@code System.out.println(javaslang.text.Strings.toString(o))}.
	 * 
	 * @param o an Object
	 */
	public static void println(Object o) {
		final String s = Strings.toString(o);
		System.out.println(s);
	}

	/**
	 * Shortcut for
	 * {@code System.out.print(String.format(format, Arrays.map(objects, Objects::toString)))} .
	 * 
	 * @param format A String format
	 * @param objects Zero or more String format arguments
	 * 
	 * @see String#format(String, Object...)
	 */
	public static void print(String format, Object... objects) {
		final String s = format(format, objects);
		System.out.print(s);
	}

	/**
	 * Shortcut for {@code System.out.println(String.format(format, Arrays.map(objects,
	 * Objects::toString)))}.
	 * 
	 * @param format A String format
	 * @param objects Zero or more String format arguments
	 * 
	 * @see String#format(String, Object...)
	 */
	public static void println(String format, Object... objects) {
		final String s = format(format, objects);
		System.out.println(s);
	}

	/**
	 * Converts given objects to strings and passes them as args to
	 * {@code String.format(format, args)}.
	 * 
	 * @param format A String format, see {@link String#format(String, Object...)}
	 * @param objects String format arguments
	 * @return A formatted string
	 */
	private static String format(String format, Object[] objects) {
		final Object[] args = Stream.of(objects).map(Strings::toString).toArray();
		return String.format(format, args);
	}

	/**
	 * Runtime check which will throw an IllegalStateException containing the given message if the
	 * condition is false.
	 * 
	 * @param condition A boolean
	 * @param message A message An error message.
	 * @throws IllegalStateException if condition is false, contains the message.
	 */
	public static void require(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * Runtime check which will throw an IllegalStateException containing the given message if the
	 * condition is false. The message is computed only if the condition is false.
	 * 
	 * @param condition A boolean
	 * @param messageSupplier An error message, computed lazily.
	 * @throws IllegalStateException if condition is false, contains the message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static void require(boolean condition, Supplier<String> messageSupplier) {
		if (!condition) {
			throw new IllegalStateException(Objects.requireNonNull(messageSupplier,
					"messageSupplier is null").get());
		}
	}

	/**
	 * Ensures that there are no cycles in a direct/indirect recursion. A method may use this, if
	 * the return value recursively calls the method under some circumstances and the recursion does
	 * not end.
	 * <p>
	 * The ThreadLocal instance should be unique for each recursive method. In a static context, the
	 * ThreadLocal instance is also static:
	 * 
	 * <pre>
	 * <code>
	 * private static final ThreadLocal<Boolean> isToStringLocked = new ThreadLocal<>();
	 *     
	 * public static String toString(Object o) {
	 *     return Lang.decycle(isToStringLocked, () -> o.toString(), () -> "...");
	 * }
	 * </code>
	 * </pre>
	 * 
	 * In a non-static context, the ThreadLocal instance is also non-static:
	 * 
	 * <pre>
	 * <code>
	 * private final ThreadLocal<Boolean> isHashCodeLocked = new ThreadLocal<>();
	 * 
	 * &#64;Override
	 * public int hashCode() {
	 *     return Lang.decycle(isHashCodeLocked, () -> super.hashCode(), () -> 0);
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param isLocked A semaphore, set to true and false, should be used exclusively within one
	 *            method.
	 * @param value A return value used if no cycle is present.
	 * @param defaultValue A return value used if a cycle has been detected.
	 * @return value.get() if no cycle detected, otherwise defaultValue.get().
	 */
	public static <T> T decycle(ThreadLocal<Boolean> isLocked, Supplier<T> value,
			Supplier<T> defaultValue) {
		if (Boolean.TRUE.equals(isLocked.get())) {
			return defaultValue.get();
		} else {
			try {
				isLocked.set(true);
				return value.get();
			} finally {
				isLocked.set(false);
			}
		}
	}

}
