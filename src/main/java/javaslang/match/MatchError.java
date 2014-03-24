/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.match;

import javaslang.lang.Strings;

/**
 * A {@link Matcher} throws a MatchError if no case matches the applied object.
 */
public class MatchError extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final Object obj;
	
	/**
	 * Internally called by {@link Matcher}.
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
