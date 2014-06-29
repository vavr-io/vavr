/**    / \ ___  _    _  ___   _____ / \ ___   ____  _____
 *    /  //   \/ \  / \/   \ /   _//  //   \ /    \/  _  \   Javaslang
 *  _/  //  -  \  \/  /  -  \\_  \/  //  -  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_/ \_/\____/\_/ \_/____/\___\_/ \_/_/  \_/\___/    Licensed under the Apache License, Version 2.0
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
