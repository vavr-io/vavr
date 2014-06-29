/**    / \ ___  _    _  ___   _____ / \ ___   ____  _____
 *    /  //   \/ \  / \/   \ /   _//  //   \ /    \/  _  \   Javaslang
 *  _/  //  -  \  \/  /  -  \\_  \/  //  -  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_/ \_/\____/\_/ \_/____/\___\_/ \_/_/  \_/\___/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javaslang.lang.Strings;

/**
 * A {@link Match} throws a MatchError if no case matches the applied object.
 */
public class MatchError extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final Object obj;
	
	/**
	 * Internally called by {@link Match}.
	 * @param obj The object which could not be matched.
	 */
	MatchError(Object obj) {
		super((obj == null) ? "null" : "type: " + obj.getClass().getName() + ", value: " + Strings.toString(obj));
		this.obj = obj;
	}
	
	/**
	 * Returns the object which could not be matched.
	 * @return An Object.
	 */
	public Object getObject() {
		return obj;
	}

}
