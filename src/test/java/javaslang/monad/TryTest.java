/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javaslang.AssertionsExtensions;
import javaslang.Serializables;
import javaslang.monad.Try.Failure;
import javaslang.monad.Try.Success;

import org.junit.Test;

public class TryTest {

	private static final String OK = "ok";

	// -- Failure.Cause

	@Test
	public void shouldDetectFatalException() throws Exception {
		final Failure.Cause cause = Failure.Cause.of(new OutOfMemoryError());
		assertThat(cause.isFatal()).isTrue();
	}

	@Test
	public void shouldDetectNonFatalException() throws Exception {
		final Failure.Cause cause = Failure.Cause.of(new StackOverflowError());
		assertThat(cause.isFatal()).isFalse();
	}

	// -- Failure

	@Test
	public void shouldDetectFailureOfRunnable() {
		assertThat(Try.run(() -> { throw new RuntimeException(); }).isFailure()).isTrue();
	}

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
	public void shouldThrowWhenGetOnFailure() {
		AssertionsExtensions.assertThat(() -> failure().get()).isThrowing(Failure.NonFatal.class,
				"java.lang.RuntimeException");
	}

	@Test
	public void shouldReturnElseWhenOrElseOnFailure() {
		assertThat(failure().orElse(OK)).isEqualTo(OK);
	}

	@Test
	public void shouldReturnElseWhenOrElseGetOnFailure() {
		assertThat(failure().orElseGet(x -> OK)).isEqualTo(OK);
	}

	@Test
	public void shouldThrowOtherWhenOrElseThrowOnFailure() {
		AssertionsExtensions.assertThat(() -> failure().orElseThrow(x -> new IllegalStateException(OK))).isThrowing(
				IllegalStateException.class, OK);
	}

	@Test
	public void shouldRecoverOnFailure() {
		assertThat(failure().recover(x -> OK).get()).isEqualTo(OK);
	}

	@Test
	public void shouldRecoverWithOnFailure() {
		assertThat(failure().recoverWith(x -> success()).get()).isEqualTo(OK);
	}

	@Test
	public void shouldRecoverWithThrowingOnFailure() {
		final RuntimeException error = error();
		assertThat(failure().recoverWith(x -> {
			throw error;
		})).isEqualTo(new Failure<>(error));
	}

	@Test
	public void shouldConvertFailureToOption() {
		assertThat(failure().toOption().isPresent()).isFalse();
	}

	@Test
	public void shouldFilterMatchingPredicateOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.filter(s -> true)).isEqualTo(actual);
	}

	@Test
	public void shouldFilterNonMatchingPredicateOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.filter(s -> false)).isEqualTo(actual);
	}

	@Test
	public void shouldFilterWithExceptionOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.filter(this::filter)).isEqualTo(actual);
	}

	@Test
	public void shouldFlatMapOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.flatMap(s -> Try.of(() -> s + "!"))).isEqualTo(actual);
	}

	@Test
	public void shouldFlatMapWithExceptionOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.flatMap(this::flatMap)).isEqualTo(actual);
	}

	@Test
	public void shouldForEachOnFailure() {
		final List<String> actual = new ArrayList<>();
		failure().forEach(actual::add);
		assertThat(actual.isEmpty()).isTrue();
	}

	@Test
	public void shouldMapOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.map(s -> s + "!")).isEqualTo(actual);
	}

	@Test
	public void shouldMapWithExceptionOnFailure() {
		final Try<String> actual = failure();
		assertThat(actual.map(this::map)).isEqualTo(actual);
	}

	@Test
	public void shouldCreateFailureOnNonFatalException() {
		assertThat(failure().failed().get().getClass().getName()).isEqualTo(RuntimeException.class.getName());
	}

	@Test
	public void shouldReturnSameFailureWhenUnitIsCalledOnFailure() {
		final Failure<?> failure = new Failure<>(error());
		assertThat(failure.unit("1")).isEqualTo(failure);
	}

	// equals

	@Test
	public void shouldEqualFailureIfObjectIsSame() {
		final Failure<?> success = new Failure<>(error());
		assertThat(success).isEqualTo(success);
	}

	@Test
	public void shouldNotEqualFailureIfObjectIsNull() {
		assertThat(new Failure<>(error())).isNotNull();
	}

	@Test
	public void shouldNotEqualFailureIfObjectIsOfDifferentType() {
		assertThat(new Failure<>(error()).equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualFailure() {
		assertThat(new Failure<>(error())).isEqualTo(new Failure<>(error()));
	}

	// hashCode

	@Test
	public void shouldHashFailure() {
		final Throwable error = error();
		assertThat(new Failure<>(error).hashCode()).isEqualTo(Objects.hashCode(error));
	}

	// toString

	@Test
	public void shouldConvertFailureToString() {
		assertThat(new Failure<>(error()).toString()).isEqualTo("Failure(java.lang.RuntimeException: error)");
	}

	// serialization

	@Test
	public void shouldSerializeDeserializeFailure() {
		final Object actual = Serializables.deserialize(Serializables.serialize(new Failure<>(error())));
		assertThat(actual.toString()).isEqualTo(new Failure<>(error()).toString());
	}

	// -- Success

	@Test
	public void shouldDetectSuccessOfRunnable() {
		assertThat(Try.run(() -> System.out.println("side-effect")).isSuccess()).isTrue();
	}

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
		AssertionsExtensions.assertThat(() -> success().filter(s -> false).get()).isThrowing(Failure.NonFatal.class,
				"java.util.NoSuchElementException: Predicate does not hold for " + OK);
	}

	@Test
	public void shouldFilterWithExceptionOnSuccess() {
		AssertionsExtensions.assertThat(() -> success().filter(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldFlatMapOnSuccess() {
		assertThat(success().flatMap(s -> Try.of(() -> s + "!")).get()).isEqualTo(OK + "!");
	}

	@Test
	public void shouldFlatMapWithExceptionOnSuccess() {
		AssertionsExtensions.assertThat(() -> success().flatMap(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldForEachOnSuccess() {
		final List<String> actual = new ArrayList<>();
		success().forEach(actual::add);
		assertThat(actual).isEqualTo(Arrays.asList(OK));
	}

	@Test
	public void shouldMapOnSuccess() {
		assertThat(success().map(s -> s + "!").get()).isEqualTo(OK + "!");
	}

	@Test
	public void shouldMapWithExceptionOnSuccess() {
		AssertionsExtensions.assertThat(() -> success().map(s -> {
			throw new RuntimeException("xxx");
		}).get()).isThrowing(Failure.NonFatal.class, "java.lang.RuntimeException: xxx");
	}

	@Test
	public void shouldThrowWhenCallingFailedOnSuccess() {
		AssertionsExtensions.assertThat(() -> success().failed().get()).isThrowing(Failure.NonFatal.class,
				"java.lang.UnsupportedOperationException: Success.failed()");
	}

	@Test
	public void shouldCreateNewSuccessWhenUnitIsCalledOnSuccess() {
		assertThat(new Success<>(1).unit("a")).isEqualTo(new Success<>("a"));
	}

	// equals

	@Test
	public void shouldEqualSuccessIfObjectIsSame() {
		final Success<?> success = new Success<>(1);
		assertThat(success).isEqualTo(success);
	}

	@Test
	public void shouldNotEqualSuccessIfObjectIsNull() {
		assertThat(new Success<>(1)).isNotNull();
	}

	@Test
	public void shouldNotEqualSuccessIfObjectIsOfDifferentType() {
		assertThat(new Success<>(1).equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualSuccess() {
		assertThat(new Success<>(1)).isEqualTo(new Success<>(1));
	}

	// hashCode

	@Test
	public void shouldHashSuccess() {
		assertThat(new Success<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
	}

	// toString

	@Test
	public void shouldConvertSuccessToString() {
		assertThat(new Success<>(1).toString()).isEqualTo("Success(1)");
	}

	// serialization

	@Test
	public void shouldSerializeDeserializeSuccess() {
		final Object actual = Serializables.deserialize(Serializables.serialize(new Success<>(1)));
		assertThat(actual).isEqualTo(new Success<>(1));
	}

	// -- helpers

	private RuntimeException error() {
		return new RuntimeException("error");
	}

	private Try<String> failure() {
		return Try.of(() -> {
			throw new RuntimeException();
		});
	}

	private <T> boolean filter(T t) {
		throw new RuntimeException("xxx");
	}

	private <T> Try<T> flatMap(T t) {
		throw new RuntimeException("xxx");
	}

	private <T> T map(T t) {
		throw new RuntimeException("xxx");
	}

	private Try<String> success() {
		return Try.of(() -> "ok");
	}
}
