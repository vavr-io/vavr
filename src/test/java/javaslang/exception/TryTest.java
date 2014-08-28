/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TryTest {

	private static final String OK = "ok";

	// -- failure

	@Test(expected = Failure.Fatal.class)
	public void shouldPassThroughFatalException() {
		Try.of(() -> {
			throw new UnknownError();
		});
	}

	@Test
	public void shouldDetectFailureOnNonFatalException() {
		assertThat(failure().isFailure()).isTrue();
	}

	@Test
	public void shouldDetectNonSuccessOnFailure() {
		assertThat(failure().isSuccess()).isFalse();
	}

	@Test
	public void shouldCreateFailureOnNonFatalException() {
		assertThat(failure().failed().get().getClass().getName()).isEqualTo(RuntimeException.class.getName());
	}

	private Try<String> failure() {
		return Try.of(() -> {
			throw new RuntimeException();
		});
	}

	// -- success

	@Test
	public void shouldDetectSuccess() {
		assertThat(success().isSuccess()).isTrue();
	}

	@Test
	public void shouldDetectNonFailureOnSuccess() {
		assertThat(success().isFailure()).isFalse();
	}

	@Test
	public void shouldGetOnSuccess() {
		assertThat(success().get()).isEqualTo(OK);
	}

	@Test
	public void shouldOrElseOnSuccess() {
		assertThat(success().orElse(null)).isEqualTo(OK);
	}

	@Test
	public void shouldOrElseGetOnSuccess() {
		assertThat(success().orElseGet(x -> null)).isEqualTo(OK);
	}

	@Test
	public void shouldOrElseThrowOnSuccess() {
		assertThat(success().orElseThrow(x -> null)).isEqualTo(OK);
	}

	@Test
	public void shouldRecoverOnSuccess() {
		assertThat(success().recover(x -> null).get()).isEqualTo(OK);
	}

	@Test
	public void shouldRecoverWithOnSuccess() {
		assertThat(success().recoverWith(x -> null).get()).isEqualTo(OK);
	}

	@Test
	public void shouldConvertSuccessToOption() {
		assertThat(success().toOption().get()).isEqualTo(OK);
	}

	@Test
	public void shouldFilterMatchingPredicateOnSuccess() {
		assertThat(success().filter(s -> true).get()).isEqualTo(OK);
	}

	@Test
	public void shouldFilterNonMatchingPredicateOnSuccess() {
		assertThat(() -> success().filter(s -> false).get()).isThrowing(Failure.NonFatal.class,
				"java.util.NoSuchElementException: Predicate does not hold for " + OK);
	}

	@Test
	public void shouldFilterWithExceptionOnSuccess() {
		assertThat(() -> success().filter(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldFlatMapOnSuccess() {
		assertThat(success().flatMap(s -> Try.of(() -> s + "!")).get()).isEqualTo(OK + "!");
	}

	@Test
	public void shouldFlatMapWithExceptionOnSuccess() {
		assertThat(() -> success().flatMap(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldForEachOnSuccess() {
		final List<String> actual = new ArrayList<>();
		success().forEach(s -> actual.add(s));
		assertThat(actual).isEqualTo(Arrays.asList(OK));
	}

	@Test
	public void shouldMapOnSuccess() {
		assertThat(success().map(s -> s + "!").get()).isEqualTo(OK + "!");
	}

	@Test
	public void shouldMapWithExceptionOnSuccess() {
		assertThat(() -> success().map(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldThrowWhenCallingFailedOnSuccess() {
		assertThat(() -> success().failed().get()).isThrowing(Failure.NonFatal.class,
				"java.lang.UnsupportedOperationException: Success.failed()");
	}

	private Try<String> success() {
		return Try.of(() -> "ok");
	}
}
