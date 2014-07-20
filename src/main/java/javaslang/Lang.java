/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * General Java languange extensions.
 */
public final class Lang {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Lang() {
		requireNotInstantiable();
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
	 * Method used in conjunction with the class-not-instantiable idiom. The exception stack strace
	 * tells the prohibited constructor call.
	 * 
	 * <pre>
	 * <code>
	 * import static javaslang.Lang.*;
	 * 
	 * public class FunkyType {
	 * 
	 *    &#47;**
	 *     * This class is not intended to be instantiated.
	 *     *&#47;
	 *     private FunkyType() {
	 *         requireNotInstantiable();
	 *     }
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @throws UnsatisfiedRequirementException yes, that's true.
	 */
	public static void requireNotInstantiable() {
		throw new UnsatisfiedRequirementException("Class cannot be instantiated.");
	}

	/**
	 * Exits the JVM using {@code Runtime.getRuntime().exit(status)} (which is equivalent to
	 * {@code System.exit(0)}). If something goes wrong while running the finalizers and shutdown
	 * hooks, or the timeout is reached, the JVM is forced to be terminated by calling
	 * {@code Runtime.getRuntime().halt(status)}.
	 * 
	 * @param status the exit status, zero for OK, non-zero for error
	 * @param timeout The maximum delay in milliseconds before calling
	 *            {@code Runtime.getRuntime().halt(status)}.
	 * 
	 * @see <a href="http://blog.joda.org/2014/02/exiting-jvm.html">exiting jvm</a>
	 */
	public static void exit(int status, long timeout) {
		final Runtime runtime = Runtime.getRuntime();
		try {
			schedule(() -> runtime.halt(status), timeout);
			runtime.exit(status);
		} catch (Throwable x) {
			runtime.halt(status);
		} finally { // double-check
			runtime.halt(status);
		}
	}

	/**
	 * Syntactic sugar, allows to call
	 * 
	 * <pre>
	 * <code>
	 * final Timer timer = Timers.schedule(() -&gt; println("hi"), 1000)
	 * </code>
	 * </pre>
	 * 
	 * instead of
	 * 
	 * <pre>
	 * <code>
	 * final Timer timer = new Timer();
	 * timer.schedule(new TimerTask() {
	 *     &#64;Override
	 *     public void run() {
	 *         println("hi");
	 *     }
	 * }, 1000);
	 * </code>
	 * </pre>
	 * 
	 * @param task A Runnable
	 * @param delay A delay in milliseconds
	 * @return A Timer
	 */
	public static Timer schedule(Runnable task, long delay) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				task.run();
			}
		}, delay);
		return timer;
	}

	/**
	 * Stream regex match results of {@link java.util.regex.Matcher}.
	 * 
	 * @param matcher A Matcher.
	 * @return A Stream of matches by successively calling {@link Matcher#group()}.
	 */
	public static Stream<String> stream(Matcher matcher) {
		final List<String> matches = new ArrayList<>();
		for (; matcher.find(); matches.add(matcher.group()))
			;
		return matches.stream();
	}

	/**
	 * Thrown by the {@linkplain Lang#require}* methods.
	 */
	public static class UnsatisfiedRequirementException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		UnsatisfiedRequirementException(String message) {
			super(message);
		}
	}

}
