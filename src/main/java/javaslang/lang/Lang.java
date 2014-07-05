/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lang;

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
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is
	 * false.
	 * 
	 * @param condition A boolean.
	 * @param message An error message.
	 * @throws UnsatisfiedRequirementException If condition is false, contains the given message.
	 */
	public static void require(boolean condition, String message) {
		if (!condition) {
			throw new UnsatisfiedRequirementException(message);
		}
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is
	 * false.
	 * 
	 * @param condition A boolean.
	 * @param messageSupplier An error message, computed lazily.
	 * @throws UnsatisfiedRequirementException If the given condition is false, contains the
	 *             supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static void require(boolean condition, Supplier<String> messageSupplier) {
		if (!condition) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given object is null.
	 * 
	 * @param <T> Type of object.
	 * @param obj An object.
	 * @return obj
	 * @throws UnsatisfiedRequirementException If the given object is null.
	 */
	public static <T> T requireNonNull(T obj) {
		if (obj == null) {
			throw new UnsatisfiedRequirementException("Object is null");
		}
		return obj;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given object is null.
	 * 
	 * @param <T> Type of object.
	 * @param obj An object
	 * @param message An error message.
	 * @return obj
	 * @throws UnsatisfiedRequirementException If the given object is null, contains the given
	 *             message.
	 */
	public static <T> T requireNonNull(T obj, String message) {
		if (obj == null) {
			throw new UnsatisfiedRequirementException(message);
		}
		return obj;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given object is null.
	 * 
	 * @param <T> Type of object.
	 * @param obj An object.
	 * @param messageSupplier An error message, computed lazily.
	 * @return obj
	 * @throws UnsatisfiedRequirementException If the given object is null, contains the supplied
	 *             message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
		if (obj == null) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return obj;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or
	 * empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty.
	 */
	public static <T> T[] requireNotNullOrEmpty(T[] array) {
		if (requireNonNull(array).length == 0) {
			throw new UnsatisfiedRequirementException("Array is empty");
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or
	 * empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @param message An error message.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty, contains the given
	 *             message.
	 */
	public static <T> T[] requireNotNullOrEmpty(T[] array, String message) {
		if (requireNonNull(array).length == 0) {
			throw new UnsatisfiedRequirementException(message);
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or
	 * empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @param messageSupplier An error message, computed lazily.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty, contains the supplied
	 *             message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static <T> T[] requireNotNullOrEmpty(T[] array, Supplier<String> messageSupplier) {
		if (requireNonNull(array).length == 0) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is
	 * null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty.
	 */
	public static CharSequence requireNotNullOrEmpty(CharSequence chars) {
		if (requireNonNull(chars).length() == 0) {
			throw new UnsatisfiedRequirementException("CharSequence is empty");
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is
	 * null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @param message An error message.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the
	 *             given message.
	 */
	public static CharSequence requireNotNullOrEmpty(CharSequence chars, String message) {
		if (requireNonNull(chars).length() == 0) {
			throw new UnsatisfiedRequirementException(message);
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is
	 * null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @param messageSupplier An error message, computed lazily.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the
	 *             supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static CharSequence requireNotNullOrEmpty(CharSequence chars,
			Supplier<String> messageSupplier) {
		if (requireNonNull(chars).length() == 0) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return chars;
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
	 * private static final ThreadLocal&lt;Boolean&gt; isToStringLocked = new ThreadLocal&lt;&gt;();
	 *     
	 * public static String toString(Object o) {
	 *     return Lang.decycle(isToStringLocked, () -&gt; o.toString(), () -&gt; "...");
	 * }
	 * </code>
	 * </pre>
	 * 
	 * In a non-static context, the ThreadLocal instance is also non-static:
	 * 
	 * <pre>
	 * <code>
	 * private final ThreadLocal&lt;Boolean&gt; isHashCodeLocked = new ThreadLocal&lt;&gt;();
	 * 
	 * &#64;Override
	 * public int hashCode() {
	 *     return Lang.decycle(isHashCodeLocked, () -&gt; super.hashCode(), () -&gt; 0);
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param <T> Element type of recursion values.
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

	public static class UnsatisfiedRequirementException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		UnsatisfiedRequirementException(String message) {
			super(message);
		}
	}

}
