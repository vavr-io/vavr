/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class TryTest {

	@Test(expected = Fatal.class)
	public void shouldPassThroughFatalException() {
		Try.of(() -> {
			throw new UnknownError();
		});
	}

	@Test
	public void shouldCreateFailureOnNonFatalException() {
		final Try<?> actual = Try.of(() -> {
			throw new RuntimeException();
		});
		assertThat(actual.isFailure()).isTrue();
		assertThat(actual.failed().get().getClass().getName()).isEqualTo(
				RuntimeException.class.getName());
	}

	@Test
	public void shouldCreateSuccess() {
		final Try<String> actual = Try.of(() -> "ok");
		assertThat(actual.isSuccess()).isTrue();
		assertThat(actual.get()).isEqualTo("ok");
	}

}
