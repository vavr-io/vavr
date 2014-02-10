package io.rocketscience.java.lang;

import java.util.function.Supplier;

/**
 * General Java languange extensions. See also {@link java.util.Objects}.
 */
public final class Lang {
	
	private Lang() {
        throw new AssertionError(Lang.class.getName() + " cannot be instantiated.");
    }
	
	/**
	 * Runtime check which will throw an IllegalStateException containing the given message if the condition is false.
	 * 
	 * @param condition A boolean
	 * @param message A message An error message.
	 * @throws IllegalStateException If condition is false, contains the message.
	 */
	public static void require(boolean condition, String message) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * Runtime check which will throw an IllegalStateException containing the given message if the condition is false.
	 * The message is computed only if the condition is false.
	 * 
	 * @param condition A boolean
	 * @param lazyMessage An error message, computed lazily.
	 * @throws IllegalStateException If condition is false, contains the message.
	 */
	public static void require(boolean condition, Supplier<String> lazyMessage) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(lazyMessage.get());
		}
	}
	
}
