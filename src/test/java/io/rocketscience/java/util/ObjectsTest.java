package io.rocketscience.java.util;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * Conversion of null, boolean values, numbers and strings is straightward.
 *
 */
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

	@Test
	public void shouldConvertEmptyArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new byte[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertSingletonBooleanArrayToBoolean() {
		// TODO
	}

	// TODO: toBoolean(Array of objects)

	// TODO: toBoolean(Collection)

	// TODO: toBoolean(Optional)

	// TODO: toBoolean(Object)

	// -- unbox(Collection) tests

	@Test
	public void shouldUnboxNullCollection() {
		final Collection<?> c = null;
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxEmptyCollection() {
		final Collection<?> c = java.util.Arrays.asList();
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxSingletonCollection() {
		final Collection<?> c = java.util.Arrays.asList(1);
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldUnboxCollectionWithTwoValues() {
		final Collection<?> c = java.util.Arrays.asList(1, 2);
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	// -- unbox(List) tests

	@Test
	public void shouldUnboxNullList() {
		final List<?> c = null;
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxEmptyList() {
		final List<?> c = java.util.Arrays.asList();
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxSingletonList() {
		final List<?> c = java.util.Arrays.asList(1);
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldUnboxListWithTwoValues() {
		final List<?> c = java.util.Arrays.asList(1, 2);
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	// -- unbox(Optional) tests

	@Test
	public void shouldUnboxNullOptional() {
		final Optional<?> c = null;
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxEmptyOptional() {
		final Optional<?> c = Optional.empty();
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldUnboxDefinedOptional() {
		final Optional<?> c = Optional.of(1);
		final Object actual = Objects.unbox(c);
		assertThat(actual).isEqualTo(1);
	}

}
