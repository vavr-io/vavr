package io.rocketscience.java.lang;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class CauseTest {

	@Test
	public void testName() throws Exception {
		final Cause cause = Cause.of(new OutOfMemoryError());
		assertThat(cause.isFatal()).isTrue();
		
	}
	
}
