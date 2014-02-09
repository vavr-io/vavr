package io.rocketscience.java.util;


public final class Arrays {

	private Arrays() {
        throw new AssertionError(Arrays.class.getName() + " cannot be instantiated.");
    }
	
	/**
	 * Tests if given Array is null or empty.
	 * 
	 * @param arr An Array
	 * @return true, if arr is null or empty, false otherwise
	 */
	public static <T> boolean isNullOrEmpty(T[] arr) {
		return arr == null || arr.length == 0;
	}
	
}
