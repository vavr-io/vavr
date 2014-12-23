/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.function.Supplier;

/**
 * Runtime checks of requirements.
 */
public final class Require {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Require() {
		throw new AssertionError(Require.class.getName() + " is not intended to be instantiated.");
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is false.
	 * 
	 * @param condition A boolean.
	 * @param message An error message.
	 * @throws UnsatisfiedRequirementException If condition is false, contains the given message.
	 */
	public static void isTrue(boolean condition, String message) {
		if (!condition) {
			throw new UnsatisfiedRequirementException(message);
		}
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is true.
	 * 
	 * @param condition A boolean.
	 * @param messageSupplier An error message, computed lazily.
	 * @throws UnsatisfiedRequirementException If the given condition is true, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static void isFalse(boolean condition, Supplier<String> messageSupplier) {
		Require.isTrue(!condition, messageSupplier);
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is true.
	 *
	 * @param condition A boolean.
	 * @param message An error message.
	 * @throws UnsatisfiedRequirementException If condition is true, contains the given message.
	 */
	public static void isFalse(boolean condition, String message) {
		Require.isTrue(!condition, message);
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given condition is false.
	 *
	 * @param condition A boolean.
	 * @param messageSupplier An error message, computed lazily.
	 * @throws UnsatisfiedRequirementException If the given condition is false, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static void isTrue(boolean condition, Supplier<String> messageSupplier) {
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
	public static <T> T nonNull(T obj) {
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
	 * @throws UnsatisfiedRequirementException If the given object is null, contains the given message.
	 */
	public static <T> T nonNull(T obj, String message) {
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
	 * @throws UnsatisfiedRequirementException If the given object is null, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static <T> T nonNull(T obj, Supplier<String> messageSupplier) {
		if (obj == null) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return obj;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty.
	 */
	public static <T> T[] notNullOrEmpty(T[] array) {
		if (nonNull(array).length == 0) {
			throw new UnsatisfiedRequirementException("Array is empty");
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @param message An error message.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty, contains the given message.
	 */
	public static <T> T[] notNullOrEmpty(T[] array, String message) {
		if (nonNull(array, message).length == 0) {
			throw new UnsatisfiedRequirementException(message);
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given array is null or empty.
	 * 
	 * @param <T> Component type of array.
	 * @param array An array.
	 * @param messageSupplier An error message, computed lazily.
	 * @return array
	 * @throws UnsatisfiedRequirementException If the given array is empty, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static <T> T[] notNullOrEmpty(T[] array, Supplier<String> messageSupplier) {
		if (nonNull(array, messageSupplier).length == 0) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return array;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty.
	 */
	public static CharSequence notNullOrEmpty(CharSequence chars) {
		if (nonNull(chars).length() == 0) {
			throw new UnsatisfiedRequirementException("CharSequence is empty");
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @param message An error message.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the given message.
	 */
	public static CharSequence notNullOrEmpty(CharSequence chars, String message) {
		if (nonNull(chars, message).length() == 0) {
			throw new UnsatisfiedRequirementException(message);
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty.
	 * 
	 * @param chars A CharSequence.
	 * @param messageSupplier An error message, computed lazily.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static CharSequence notNullOrEmpty(CharSequence chars, Supplier<String> messageSupplier) {
		if (nonNull(chars, messageSupplier).length() == 0) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty. The
	 * CharSequence is considered empty, if it contains only whitespaces.
	 * 
	 * @param chars A CharSequence.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty.
	 */
	public static CharSequence notNullOrEmptyTrimmed(CharSequence chars) {
		if (nonNull(chars).toString().trim().length() == 0) {
			throw new UnsatisfiedRequirementException("CharSequence is empty");
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty. The
	 * CharSequence is considered empty, if it contains only whitespaces.
	 * 
	 * @param chars A CharSequence.
	 * @param message An error message.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the given message.
	 */
	public static CharSequence notNullOrEmptyTrimmed(CharSequence chars, String message) {
		if (nonNull(chars, message).toString().trim().length() == 0) {
			throw new UnsatisfiedRequirementException(message);
		}
		return chars;
	}

	/**
	 * Runtime check which throws an UnsatisfiedRequirementException if the given CharSequence is null or empty. The
	 * CharSequence is considered empty, if it contains only whitespaces.
	 * 
	 * @param chars A CharSequence.
	 * @param messageSupplier An error message, computed lazily.
	 * @return chars
	 * @throws UnsatisfiedRequirementException If the given CharSequence is empty, contains the supplied message.
	 * @throws NullPointerException If messageSupplier is null.
	 */
	public static CharSequence notNullOrEmptyTrimmed(CharSequence chars, Supplier<String> messageSupplier) {
		if (nonNull(chars, messageSupplier).toString().trim().length() == 0) {
			throw new UnsatisfiedRequirementException(messageSupplier.get());
		}
		return chars;
	}

	/**
	 * Thrown by the Require.* methods.
	 */
	public static class UnsatisfiedRequirementException extends RuntimeException {

		private static final long serialVersionUID = -6601105224101209604L;

		UnsatisfiedRequirementException(String message) {
			super(message);
		}
	}
}
