/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.exception;

/**
 * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class NonFatal extends Cause {

	private static final long serialVersionUID = 1L;

	NonFatal(Throwable cause) {
		super(cause);
	}

	@Override
	public boolean isFatal() {
		return false;
	}

}
