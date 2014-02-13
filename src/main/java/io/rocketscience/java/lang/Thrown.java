package io.rocketscience.java.lang;

import static io.rocketscience.java.lang.Lang.require;

public abstract class Thrown extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public abstract Throwable get();

	public abstract boolean isFatal();

	/**
	 * Wraps t in a Thrown which is either a {@link Fatal} or a {@link NonFatal}. The given Throwable t is wrapped in a
	 * Fatal, i.e. considered as a non-recoverable, if t is an instance of one of the following classes:
	 * 
	 * <ul>
	 * <li>InterruptedException</li>
	 * <li>LinkageError</li>
	 * <li>ThreadDeath</li>
	 * <li>VirtualMachineError (i.e. OutOfMemoryError)</li>
	 * </ul>
	 * 
	 * However, StackOverflowError is considered as a non-fatal.
	 * 
	 * @param t A Throwable
	 * @return A {@link Fatal}, if t is fatal, a {@link NonFatal} otherwise.
	 */
	public static Thrown of(Throwable t) {
		require(t != null, "throwable is null");
		final boolean isFatal = t instanceof VirtualMachineError//
				|| t instanceof ThreadDeath//
				|| t instanceof InterruptedException//
				|| t instanceof LinkageError;
		return isFatal ? new Fatal(t) : new NonFatal(t);
	}

}
