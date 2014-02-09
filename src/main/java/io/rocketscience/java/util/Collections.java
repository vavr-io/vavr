package io.rocketscience.java.util;

import java.util.Collection;

public final class Collections {

	private Collections() {
        throw new AssertionError(Collections.class.getName() + " cannot be instantiated.");
    }
	
	/**
	 * Tests if given Collection is null or empty.
	 * 
	 * @param c A Collection
	 * @return true, if c is null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(Collection<?> c) {
		return c == null || c.size() == 0;
	}
	
}
