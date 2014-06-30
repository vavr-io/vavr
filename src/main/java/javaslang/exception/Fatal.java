/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

/**
 * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class Fatal extends Cause {

	private static final long serialVersionUID = 1L;

	Fatal(Throwable cause) {
		super(cause);
	}

	@Override
	public boolean isFatal() {
		return true;
	}

}
