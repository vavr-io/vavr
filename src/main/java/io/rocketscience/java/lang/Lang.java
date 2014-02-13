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

	/**
	 * Checks whether t is a fatal, i.e. non-recoverable, error or not. t is considered fatal, if it is an instance of
	 * the following classes:
	 * 
	 * <ul>
	 * <li>InterruptedException</li>
	 * <li>LinkageError</li>
	 * <li>ThreadDeath</li>
	 * <li>VirtualMachineError (i.e. OutOfMemoryError)</li>
	 * </ul>
	 * 
	 * However, StackOverflowError is considered non-fatal.
	 * 
	 * @param t A Throwable
	 * @return true, if t is fatal, false otherwise.
	 */
	public static boolean isFatal(Throwable t) {
		return t instanceof VirtualMachineError || t instanceof ThreadDeath || t instanceof InterruptedException
				|| t instanceof LinkageError;
	}

}
