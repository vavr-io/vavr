package io.rocketscience.java.lang;

import java.util.function.Supplier;

public final class RuntimeExtensions {
	
	private RuntimeExtensions() {
	}
	
	public static void require(boolean condition, String message) throws IllegalStateException {
		require(condition, () -> message);
	}

	public static void require(boolean condition, Supplier<String> message) throws IllegalStateException {
		if (!condition) {
			throw new IllegalStateException("Requirement violation. " + message);
		}
	}
	
}
