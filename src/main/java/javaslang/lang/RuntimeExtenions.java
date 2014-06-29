/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.lang;

/**
 * Additions to {@link java.lang.Runtime}.
 */
public final class RuntimeExtenions {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private RuntimeExtenions() {
		throw new AssertionError(RuntimeExtenions.class.getName() + " cannot be instantiated.");
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
			Timers.schedule(() -> runtime.halt(status), timeout);
			runtime.exit(status);
		} catch (Throwable x) {
			runtime.halt(status);
		} finally { // double-check
			runtime.halt(status);
		}
	}

}
