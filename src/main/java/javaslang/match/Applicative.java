/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

/**
 * Interface for a transformation that is applicable to specific objects.
 *
 * @param <R> return type of the transformation.
 */
interface Applicative<R> {

	/**
	 * Tests if a given object is applicable to the transformation {@link #apply(Object)}.
	 * 
	 * @param obj An object.
	 * @return true, if obj is applicable to {@link #apply(Object)}, false otherwise.
	 */
	boolean isApplicable(Object obj);

	/**
	 * 
	 * @param obj An object.
	 * @return
	 */
	R apply(Object obj);
}
