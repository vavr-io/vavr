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
