package io.rocketscience.java.lang;

import java.util.function.Supplier;

/**
 * General Java languange extensions. See also {@link java.util.Objects}.
 */
public final class Lang {
	
	private Lang() {
        throw new AssertionError(Lang.class.getName() + " cannot be instantiated.");
    }
	
	public static void require(boolean condition, String message) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

	public static void require(boolean condition, Supplier<String> lazyMessage) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(lazyMessage.get());
		}
	}
	
}
