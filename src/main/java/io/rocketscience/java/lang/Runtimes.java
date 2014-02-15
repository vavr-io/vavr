package io.rocketscience.java.lang;

import io.rocketscience.java.util.Timers;

public final class Runtimes {

	private Runtimes() {
		throw new AssertionError(Runtimes.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Exits the JVM using <code>Runtime.getRuntime().exit(status)</code> (which is equivalent to
	 * <code>System.exit(0)</code>). If something goes wrong while running the finalizers and shutdown hooks, or the
	 * timeout is reached, the JVM is forced to be terminated by calling <code>Runtime.getRuntime().halt(status)</code>.
	 * 
	 * @param status the exit status, zero for OK, non-zero for error
	 * @param timeout The maximum delay in milliseconds before calling <code>Runtime.getRuntime().halt(status)</code>.
	 * 
	 * @see http://blog.joda.org/2014/02/exiting-jvm.html
	 */
	public static void exit(int status, long timeout) {
		final Runtime runtime = Runtime.getRuntime();
		try {
			Timers.schedule(() -> runtime.halt(status), timeout);
			runtime.exit(status);
		} catch (Throwable x) {
			runtime.halt(status);
		}
	}

}
