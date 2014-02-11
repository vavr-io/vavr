package io.rocketscience.java.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;

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
	public void shouldConvertBigDecimalZeroToFalse() {
		final Boolean actual = Objects.toBoolean(new BigDecimal(0.0d));
		assertThat(actual).isEqualTo(false);
	}
	
	@Test
	public void shouldConvertBigDecimalOneToTrue() {
		final Boolean actual = Objects.toBoolean(new BigDecimal("1.0"));
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertCharZeroToTrue() {
		final Boolean actual = Objects.toBoolean('0');
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shouldConvertCharOneToTrue() {
		final Boolean actual = Objects.toBoolean('1');
		assertThat(actual).isEqualTo(true);
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
	public void shouldConvertLettersToNull() {
		final Boolean actual = Objects.toBoolean("ABC");
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertEmptyBooleanArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new boolean[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertBooleanArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new boolean[] { false });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertBooleanArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new boolean[] { true });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertBooleanArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new boolean[] { false, true });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyByteArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new byte[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertByteArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new byte[] { 0 });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertByteArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new byte[] { 1 });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertByteArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new byte[] { 0, 1 });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyCharArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertCharArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] { 0 });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertCharArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] { 1 });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertCharArrayContainingDecimalZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] { 0 });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertCharArrayContainingDecimalOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] { 1 });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertCharArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new char[] { '0', '1' });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyDoubleArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new double[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertDoubleArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new double[] { 0.0d });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertDoubleArrayContainingAFractionToBoolean() {
		final Boolean actual = Objects.toBoolean(new double[] { 0.1d });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertDoubleArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new double[] { 1d });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertDoubleArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new double[] { 0.0d, 0.1d });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyFloatArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new float[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertFloatArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new float[] { 0.0f });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertFloatArrayContainingAFractionToBoolean() {
		final Boolean actual = Objects.toBoolean(new float[] { 0.1f });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertFloatArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new float[] { 1f });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertFloatArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new float[] { 0.0f, 0.1f });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyIntArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new int[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertIntArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new int[] { 0 });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertIntArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new int[] { 1 });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertIntArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new int[] { 0, 1 });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyLongArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new long[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertLongArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new long[] { 0l });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertLongArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new long[] { 1l });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertLongArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new long[] { 0l, 1l });
		assertThat(actual).isEqualTo(null);
	}

	@Test
	public void shouldConvertEmptyShortArrayToBoolean() {
		final Boolean actual = Objects.toBoolean(new short[] {});
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	public void shouldConvertShortArrayContainingZeroToBoolean() {
		final Boolean actual = Objects.toBoolean(new short[] { 0 });
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldConvertShortArrayContainingOneToBoolean() {
		final Boolean actual = Objects.toBoolean(new short[] { 1 });
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shouldConvertShortArrayContainingTwoElementsToBoolean() {
		final Boolean actual = Objects.toBoolean(new short[] { 0, 1 });
		assertThat(actual).isEqualTo(null);
	}

}
