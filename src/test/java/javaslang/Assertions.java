/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Assertions {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Assertions() {
		throw new AssertionError(Assertions.class.getName() + " is not intended to be instantiated.");
	}

	public static ClassAssert assertThat(Class<?> clazz) {
		return new ClassAssert(clazz);
	}

	public static RunnableAssert assertThat(Runnable test) {
		return new RunnableAssert(test);
	}

	public static class ClassAssert {

		final Class<?> clazz;

		ClassAssert(Class<?> clazz) {
			this.clazz = clazz;
		}

		public void isNotInstantiable() {
			try {
				final Constructor<?> cons = clazz.getDeclaredConstructor();
				cons.setAccessible(true);
				cons.newInstance();
			} catch (InvocationTargetException x) {
				final String exception = ((x.getCause() == null) ? x : x.getCause()).getClass().getSimpleName();
				final String actualMessage = (x.getCause() == null) ? x.getMessage() : x.getCause().getMessage();
				final String expectedMessage = clazz.getName() + " is not intended to be instantiated.";
				if (!expectedMessage.equals(actualMessage)) {
					throw new AssertionError(String.format("Expected AssertionError(\"%s\") but was %s(\"%s\")",
							expectedMessage, exception, actualMessage));
				}
			} catch (Exception x) {
				throw new RuntimeException("Error instantiating " + clazz.getName(), x);
			}
		}
	}

	public static class RunnableAssert {

		final Runnable test;

		RunnableAssert(Runnable test) {
			this.test = test;
		}

		public void isThrowing(Class<? extends Throwable> expectedException, String expectedMessage) {
			Requirements.requireNonNull(expectedException, "expectedException is null");
			try {
				test.run();
				throw new AssertionError(expectedException.getName() + " not thrown");
			} catch (AssertionError x) {
				throw x;
			} catch (Throwable x) {
				if (!expectedException.isAssignableFrom(x.getClass())) {
					throw new AssertionError("Expected exception assignable to type "
							+ expectedException.getName()
							+ " but was "
							+ x.getClass().getName()
							+ " with message "
							+ Strings.toString(x.getMessage())
							+ ".");
				}
				final String actualMessage = x.getMessage();
				final boolean isOk = (actualMessage == null) ? (expectedMessage == null) : actualMessage
						.equals(expectedMessage);
				if (!isOk) {
					throw new AssertionError("Expected exception message "
							+ Strings.toString(expectedMessage)
							+ " but was "
							+ Strings.toString(actualMessage)
							+ ".");
				}
			}
		}
	}
}
