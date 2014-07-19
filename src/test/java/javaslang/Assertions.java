/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Lang.requireNotInstantiable;

public final class Assertions {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Assertions() {
		requireNotInstantiable();
	}

	public static RunnableAssert assertThat(Runnable test) {
		return new RunnableAssert(test);
	}

	public static class RunnableAssert {

		final Runnable test;

		RunnableAssert(Runnable test) {
			this.test = test;
		}

		public void isThrowing(Class<? extends Throwable> expectedException, String expectedMessage) {
			Lang.requireNonNull(expectedException, "expectedException is null");
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
				final boolean isOk = (actualMessage == null) ? (expectedMessage == null)
						: actualMessage.equals(expectedMessage);
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
