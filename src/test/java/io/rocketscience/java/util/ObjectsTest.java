package io.rocketscience.java.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class ObjectsTest {
	
	// -- toBoolean tests

	@Test
	public void shouldConvertNullToBoolean() {
		final Boolean actual = Objects.toBoolean(null);
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertTrueToTrue() {
		final Boolean actual = Objects.toBoolean(true);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertFalseToFalse() {
		final Boolean actual = Objects.toBoolean(false);
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertOneToTrue() {
		final Boolean actual = Objects.toBoolean(1);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertZeroToFalse() {
		final Boolean actual = Objects.toBoolean(0);
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertDoubleOneToTrue() {
		final Boolean actual = Objects.toBoolean(1.0d);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertDoubleZeroToFalse() {
		final Boolean actual = Objects.toBoolean(0.0d);
		assertThat(actual).isEqualTo(false);
	}
	
	@Test
	public void shouldConvertLowercaseTrueStringToTrue() {
		final Boolean actual = Objects.toBoolean("true");
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertUppercaseTrueStringToTrue() {
		final Boolean actual = Objects.toBoolean("TRUE");
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shouldConvertLowercaseFalseStringToFalse() {
		final Boolean actual = Objects.toBoolean("false");
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertUppercaseFalseStringToFalse() {
		final Boolean actual = Objects.toBoolean("FALSE");
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertOneStringToTrue() {
		final Boolean actual = Objects.toBoolean("1");
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shouldConvertZeroStringToFalse() {
		final Boolean actual = Objects.toBoolean("0");
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertDoubleOneStringToTrue() {
		final Boolean actual = Objects.toBoolean("1.0");
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertDoubleZeroStringToFalse() {
		final Boolean actual = Objects.toBoolean("0.0");
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertDoubleStringToTrue() {
		final Boolean actual = Objects.toBoolean("0.1");
		assertThat(actual).isEqualTo(true);
	}
	
	// TODO: toBoolean(Array of primitives) // boolean, byte, char, double, float, int, long, short
	
	// TODO: toBoolean(Array of objects)
	
	// TODO: toBoolean(Collection)
	
	// TODO: toBoolean(Optional)
	
	// TODO: toBoolean(Object)
	
	

}
