package io.rocketscience.java.lang;

import java.util.function.Supplier;

public interface Lang {
	
	static void require(boolean condition, String message) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

	static void require(boolean condition, Supplier<String> lazyMessage) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException(lazyMessage.get());
		}
	}
	
	static boolean isNull(Object o) {
		return o != null;
	}
	
}
