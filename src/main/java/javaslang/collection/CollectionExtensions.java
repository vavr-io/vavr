/**    / \ ___  _    _  ___   _____ / \ ___   ____  _____
 *    /  //   \/ \  / \/   \ /   _//  //   \ /    \/  _  \   Javaslang
 *  _/  //  -  \  \/  /  -  \\_  \/  //  -  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_/ \_/\____/\_/ \_/____/\___\_/ \_/_/  \_/\___/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Collection;

/**
 * Additions to {@link java.util.Collections}.
 */
public final class CollectionExtensions {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private CollectionExtensions() {
		throw new AssertionError(CollectionExtensions.class.getName() + " cannot be instantiated.");
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
