/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.exception.Cause;

import org.junit.Test;

public class CauseTest {

	@Test
	public void shouldDetectFatalException() throws Exception {
		final Cause cause = Cause.of(new OutOfMemoryError());
		assertThat(cause.isFatal()).isTrue();
	}

	@Test
	public void shouldDetectNonFatalException() throws Exception {
		final Cause cause = Cause.of(new StackOverflowError());
		assertThat(cause.isFatal()).isFalse();
	}

}
