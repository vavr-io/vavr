package javaslang.exception;

import static javaslang.match.Matchers.caze;
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

	@Test
	public void shouldMatchSuccess() {
		final int actual = Try.of(() -> "ok").match(
				caze((Success<String> success) -> 1).
				caze((Failure<String> failure) -> 0)
		);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchFailure() {
		final int actual = Try.of(() -> {
			throw new RuntimeException();
		}).match(
				caze((Success<String> success) -> 1).
				caze((Failure<String> failure) -> 0)
		);
		assertThat(actual).isEqualTo(0);
	}
	
}
