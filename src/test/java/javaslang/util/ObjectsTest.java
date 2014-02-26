package javaslang.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;

import javaslang.util.Objects;

import org.junit.Test;

/**
 * Conversion of null, boolean values, numbers and strings is straightward.
 *
 */
public class ObjectsTest {

	// -- toBoolean tests

	@Test
	public void shouldConvertNullToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(null);
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertTrueToTrue() {
		final Option<Boolean> actual = Objects.toBoolean(true);
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertFalseToFalse() {
		final Option<Boolean> actual = Objects.toBoolean(false);
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertOneToTrue() {
		final Option<Boolean> actual = Objects.toBoolean(1);
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertZeroToFalse() {
		final Option<Boolean> actual = Objects.toBoolean(0);
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertDoubleOneToTrue() {
		final Option<Boolean> actual = Objects.toBoolean(1.0d);
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertDoubleZeroToFalse() {
		final Option<Boolean> actual = Objects.toBoolean(0.0d);
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertBigDecimalZeroToFalse() {
		final Option<Boolean> actual = Objects.toBoolean(new BigDecimal(0.0d));
		assertThat(actual).isEqualTo(Option.of(false));
	}
	
	@Test
	public void shouldConvertBigDecimalOneToTrue() {
		final Option<Boolean> actual = Objects.toBoolean(new BigDecimal("1.0"));
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertCharZeroToTrue() {
		final Option<Boolean> actual = Objects.toBoolean('0');
		assertThat(actual).isEqualTo(Option.of(true));
	}
	
	@Test
	public void shouldConvertCharOneToTrue() {
		final Option<Boolean> actual = Objects.toBoolean('1');
		assertThat(actual).isEqualTo(Option.of(true));
	}
	
	@Test
	public void shouldConvertLowercaseTrueStringToTrue() {
		final Option<Boolean> actual = Objects.toBoolean("true");
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertUppercaseTrueStringToTrue() {
		final Option<Boolean> actual = Objects.toBoolean("TRUE");
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertLowercaseFalseStringToFalse() {
		final Option<Boolean> actual = Objects.toBoolean("false");
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertUppercaseFalseStringToFalse() {
		final Option<Boolean> actual = Objects.toBoolean("FALSE");
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertOneStringToTrue() {
		final Option<Boolean> actual = Objects.toBoolean("1");
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertZeroStringToFalse() {
		final Option<Boolean> actual = Objects.toBoolean("0");
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertDoubleOneStringToTrue() {
		final Option<Boolean> actual = Objects.toBoolean("1.0");
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertDoubleZeroStringToFalse() {
		final Option<Boolean> actual = Objects.toBoolean("0.0");
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertDoubleStringToTrue() {
		final Option<Boolean> actual = Objects.toBoolean("0.1");
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertLettersToNull() {
		final Option<Boolean> actual = Objects.toBoolean("ABC");
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertEmptyBooleanArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new boolean[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertBooleanArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new boolean[] { false });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertBooleanArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new boolean[] { true });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertBooleanArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new boolean[] { false, true });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyByteArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new byte[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertByteArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new byte[] { 0 });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertByteArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new byte[] { 1 });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertByteArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new byte[] { 0, 1 });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyCharArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertCharArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] { 0 });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertCharArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] { 1 });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertCharArrayContainingDecimalZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] { 0 });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertCharArrayContainingDecimalOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] { 1 });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertCharArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new char[] { '0', '1' });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyDoubleArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new double[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertDoubleArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new double[] { 0.0d });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertDoubleArrayContainingAFractionToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new double[] { 0.1d });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertDoubleArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new double[] { 1d });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertDoubleArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new double[] { 0.0d, 0.1d });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyFloatArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new float[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertFloatArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new float[] { 0.0f });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertFloatArrayContainingAFractionToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new float[] { 0.1f });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertFloatArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new float[] { 1f });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertFloatArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new float[] { 0.0f, 0.1f });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyIntArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new int[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertIntArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new int[] { 0 });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertIntArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new int[] { 1 });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertIntArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new int[] { 0, 1 });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyLongArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new long[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertLongArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new long[] { 0l });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertLongArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new long[] { 1l });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertLongArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new long[] { 0l, 1l });
		assertThat(actual).isEqualTo(None.instance());
	}

	@Test
	public void shouldConvertEmptyShortArrayToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new short[] {});
		assertThat(actual).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldConvertShortArrayContainingZeroToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new short[] { 0 });
		assertThat(actual).isEqualTo(Option.of(false));
	}

	@Test
	public void shouldConvertShortArrayContainingOneToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new short[] { 1 });
		assertThat(actual).isEqualTo(Option.of(true));
	}

	@Test
	public void shouldConvertShortArrayContainingTwoElementsToBoolean() {
		final Option<Boolean> actual = Objects.toBoolean(new short[] { 0, 1 });
		assertThat(actual).isEqualTo(None.instance());
	}

}
