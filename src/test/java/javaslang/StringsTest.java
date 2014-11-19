/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import javaslang.Require.UnsatisfiedRequirementException;

import org.junit.Test;

public class StringsTest {

	@Test
	public void shouldNotBeInstantiable() {
		AssertionsExtensions.assertThat(Strings.class).isNotInstantiable();
	}

	// toString(Object)

	@Test
	public void shouldConvertNullToString() {
		final String actual = Strings.toString(null);
		assertThat(actual).isEqualTo("null");
	}

	@Test
	public void shouldConvertCharSequenceToString() {
		final String actual = Strings.toString(new CharSequenceImpl("test"));
		assertThat(actual).isEqualTo("\"test\"");
	}

	@Test
	public void shouldConvertNonArrayClassToString() {
		final String actual = Strings.toString(String.class);
		assertThat(actual).isEqualTo(String.class.getName());
	}

	@Test
	public void shouldConvertArrayClassToString() {
		final String actual = Strings.toString(byte[][][].class);
		assertThat(actual).isEqualTo("byte[][][]");
	}

	@Test
	public void shouldConvertArrayToString() {
		final String actual = Strings.toString(new byte[] { (byte) 1, (byte) 2, (byte) 3 });
		assertThat(actual).isEqualTo("Array(1, 2, 3)");
	}

	@Test
	public void shouldConvertArrayOfArraysToString() {
		final String actual = Strings.toString(new byte[][] { { (byte) 1 }, { (byte) 2 }, { (byte) 3 } });
		assertThat(actual).isEqualTo("Array(Array(1), Array(2), Array(3))");
	}

	@Test
	public void shouldConvertJavaslangListToString() {
		final String actual = Strings.toString(javaslang.collection.List.of(1, 2, 3));
		assertThat(actual).isEqualTo("List(1, 2, 3)");
	}

	@Test
	public void shouldConvertIterableToString() {
		final String actual = Strings.toString(Arrays.asList(1, 2, 3));
		assertThat(actual).isEqualTo("ArrayList(1, 2, 3)");
	}

	@Test
	public void shouldConvertOtherObjectToString() {
		final String actual = Strings.toString(new OtherObject());
		assertThat(actual).isEqualTo("OtherObject");
	}

	@Test
	public void shouldDetectDirectLoopOnToString() {
		final Object[] loop = new Object[1];
		loop[0] = loop;
		final String actual = Strings.toString(loop);
		assertThat(actual).isEqualTo("Array(...)");
	}

	@Test
	public void shouldVisitTwoSimilarPathsOnToString() {
		final Object[] path = { "path" };
		final Object[] array = { path, path };
		final String actual = Strings.toString(array);
		assertThat(actual).isEqualTo("Array(Array(\"path\"), Array(\"path\"))");
	}

	@Test
	public void shouldConvertPrimitiveArrayToString() {
		final boolean[] array = { true, false };
		final String actual = Strings.toString(array);
		assertThat(actual).isEqualTo("Array(true, false)");
	}

	// repeat(string, count)

	@Test
	public void shouldRepeatNullAsNull() {
		final String s = Strings.repeat(null, 2);
		assertThat(s).isNull();
	}

	@Test
	public void shouldRepeatStringZeroTimes() {
		final String s = Strings.repeat("x", 0);
		assertThat(s).isEqualTo("");
	}

	@Test
	public void shouldRepeatStringOneTime() {
		final String s = Strings.repeat("x", 1);
		assertThat(s).isEqualTo("x");
	}

	@Test
	public void shouldRepeatStringTwoTimes() {
		final String s = Strings.repeat("x", 2);
		assertThat(s).isEqualTo("xx");
	}

	@Test
	public void shouldRepeatStringNegativeTimes() {
		final String s = Strings.repeat("x", -2);
		assertThat(s).isEqualTo("");
	}

	// repeat(char, count)

	@Test
	public void shouldRepeatCharZeroTimes() {
		final String s = Strings.repeat('x', 0);
		assertThat(s).isEqualTo("");
	}

	@Test
	public void shouldRepeatCharOneTime() {
		final String s = Strings.repeat('x', 1);
		assertThat(s).isEqualTo("x");
	}

	@Test
	public void shouldRepeatCharTwoTimes() {
		final String s = Strings.repeat('x', 2);
		assertThat(s).isEqualTo("xx");
	}

	@Test
	public void shouldRepeatCharNegativeTimes() {
		final String s = Strings.repeat('x', -2);
		assertThat(s).isEqualTo("");
	}

	// escape(string)

	@Test
	public void shouldEscapeNull() {
		final String s = Strings.escape(null);
		assertThat(s).isNull();
	}

	@Test
	public void shouldEscapeEmptyString() {
		final String s = Strings.escape("");
		assertThat(s).isEqualTo("");
	}

	@Test
	public void shouldEscapeQuotes() {
		final String s = Strings.escape("\"");
		assertThat(s).isEqualTo("\\\"");
	}

	@Test
	public void shouldEscapeEscapeChar() {
		final String s = Strings.escape("\\");
		assertThat(s).isEqualTo("\\\\");
	}

	@Test
	public void shouldNotEscapeNonEscapeChars() {
		final String name = getClass().getName();
		final String s = Strings.escape(name);
		assertThat(s).isEqualTo(name);
	}

	// ecape(string, character, escape)

	@Test
	public void shouldEscapeCharacterNull() {
		final String s = Strings.escape(null, '\\', '\\');
		assertThat(s).isNull();
	}

	@Test
	public void shouldEscapeCharacterEmptyString() {
		final String s = Strings.escape("", '\\', '\\');
		assertThat(s).isEqualTo("");
	}

	@Test
	public void shouldEscapeCharacterEscapeChar() {
		final String s = Strings.escape("\\", '\\', '\\');
		assertThat(s).isEqualTo("\\\\");
	}

	@Test
	public void shouldNotEscapeCharacterNonEscapeChars() {
		final String name = getClass().getName();
		final String s = Strings.escape(name, '\\', '\\');
		assertThat(s).isEqualTo(name);
	}

	// lineAndColumn

	@Test
	public void shouldThrowWhenLineAndColumnOfNullString() {
		AssertionsExtensions.assertThat(() -> Strings.lineAndColumn(null, 0)).isThrowing(
				UnsatisfiedRequirementException.class, "s is null");
	}

	@Test
	public void shouldThrowWhenLineAndColumnOfEmptyString() {
		final String s = "";
		assertThat(Strings.lineAndColumn(s, 0)).isEqualTo(Tuple.of(1, 1));
	}

	@Test
	public void shouldComputeLineAndColumnForStartOfString() {
		final String s = "text";
		assertThat(Strings.lineAndColumn(s, 0)).isEqualTo(Tuple.of(1, 1));
	}

	@Test
	public void shouldComputeLineAndColumnForEndOfStringWithoutNewlines() {
		final String s = "text";
		assertThat(Strings.lineAndColumn(s, s.length())).isEqualTo(Tuple.of(1, s.length() + 1));
	}

	@Test
	public void shouldComputeLineAndColumnForEndOfStringWithNewlines() {
		final String s = "lorem\nipsum";
		assertThat(Strings.lineAndColumn(s, s.length())).isEqualTo(Tuple.of(2, 6));
	}

	@Test
	public void shouldComputeLineAndColumnWithinStringAtEndOfLine() {
		final String s = "lorem\nipsum";
		assertThat(Strings.lineAndColumn(s, s.indexOf('\n'))).isEqualTo(Tuple.of(1, 6));
	}

	@Test
	public void shouldComputeLineAndColumnWithinStringWithNewlines() {
		final String s = "lorem\nipsum";
		assertThat(Strings.lineAndColumn(s, s.indexOf('\n') + 1)).isEqualTo(Tuple.of(2, 1));
	}

	// isNullOrEmpty

	@Test
	public void shouldRecognizeNullString() {
		assertThat(Strings.isNullOrEmpty(null)).isTrue();
	}

	@Test
	public void shouldRecognizeEmptyString() {
		assertThat(Strings.isNullOrEmpty("")).isTrue();
	}

	@Test
	public void shouldRecognizeNonEmptyWhitespaceString() {
		assertThat(Strings.isNullOrEmpty(" ")).isFalse();
	}

	@Test
	public void shouldRecognizeNonEmptyNonWhitespaceString() {
		assertThat(Strings.isNullOrEmpty("abc")).isFalse();
	}

	// join(String[], separator, escape)

	@Test
	public void shouldJoinArrayOfEmptyStringWithEscape() {
		final String[] array = new String[] { "" };
		assertThat(Strings.join(array, ';', '\\')).isEqualTo("");
	}

	@Test
	public void shouldJoinArrayOfTwoEmptyStringsWithEscape() {
		final String[] array = new String[] { "", "" };
		assertThat(Strings.join(array, ';', '\\')).isEqualTo(";");
	}

	@Test
	public void shouldJoinArrayOfSeparatorWithEscape() {
		final String[] array = new String[] { ";" };
		assertThat(Strings.join(array, ';', '\\')).isEqualTo("\\;");
	}

	@Test
	public void shouldJoinArrayOfTwoSeparatorsWithEscape() {
		final String[] array = new String[] { ";", ";" };
		assertThat(Strings.join(array, ';', '\\')).isEqualTo("\\;;\\;");
	}

	@Test
	public void shouldJoinArrayOfTwoNonSeparators() {
		final String[] array = new String[] { "A", "B" };
		assertThat(Strings.join(array, ';', '\\')).isEqualTo("A;B");
	}

	@Test
	public void shouldJoinArrayOfTwoEscapedSeparatorsWithEscape() {
		final String[] array = new String[] { "\\^", "\\^\\" };
		assertThat(Strings.join(array, '^', '\\')).isEqualTo("\\\\\\^^\\\\\\^\\\\");
	}

	// join(Iterable<String>, separator, escape)

	@Test
	public void shouldJoinIterableOfEmptyStringWithEscape() {
		final Iterable<String> iterable = Arrays.asList(new String[] { "" });
		assertThat(Strings.join(iterable, ';', '\\')).isEqualTo("");
	}

	@Test
	public void shouldJoinIterableOfTwoEmptyStringsWithEscape() {
		final Iterable<String> iterable = Arrays.asList(new String[] { "", "" });
		assertThat(Strings.join(iterable, ';', '\\')).isEqualTo(";");
	}

	@Test
	public void shouldJoinIterableOfSeparatorWithEscape() {
		final Iterable<String> iterable = Arrays.asList(new String[] { ";" });
		assertThat(Strings.join(iterable, ';', '\\')).isEqualTo("\\;");
	}

	@Test
	public void shouldJoinIterableOfTwoSeparatorsWithEscape() {
		final Iterable<String> iterable = Arrays.asList(new String[] { ";", ";" });
		assertThat(Strings.join(iterable, ';', '\\')).isEqualTo("\\;;\\;");
	}

	@Test
	public void shouldJoinIterableOfTwoNonSeparators() {
		final Iterable<String> iterable = Arrays.asList(new String[] { "A", "B" });
		assertThat(Strings.join(iterable, ';', '\\')).isEqualTo("A;B");
	}

	@Test
	public void shouldJoinIterableOfTwoEscapedSeparatorsWithEscape() {
		final Iterable<String> iterable = Arrays.asList(new String[] { "\\^", "\\^\\" });
		assertThat(Strings.join(iterable, '^', '\\')).isEqualTo("\\\\\\^^\\\\\\^\\\\");
	}

	// split(string, separator)

	@Test
	public void shouldSplitEmptyString() {
		final String[] actual = Strings.split("", "#");
		assertThat(actual).isEqualTo(new String[] { "" });
	}

	@Test
	public void shouldSplitWithOneSeparator() {
		final String[] actual = Strings.split("#", "#");
		assertThat(actual).isEqualTo(new String[] { "", "" });
	}

	@Test
	public void shouldSplitWithTwoSeparators() {
		final String[] actual = Strings.split("##", "#");
		assertThat(actual).isEqualTo(new String[] { "", "", "" });
	}

	@Test
	public void shouldSplitWithEmptyInnerToken() {
		final String[] actual = Strings.split("123##456", "#");
		assertThat(actual).isEqualTo(new String[] { "123", "", "456" });
	}

	@Test
	public void shouldSplitWithLongSeparators() {
		final String[] actual = Strings.split("123##456", "##");
		assertThat(actual).isEqualTo(new String[] { "123", "456" });
	}

	@Test
	public void shouldSplitEmptyStartAndEndToken() {
		final String[] actual = Strings.split("#123#", "#");
		assertThat(actual).isEqualTo(new String[] { "", "123", "" });
	}

	// split(string, separator, escape)

	@Test
	public void shouldSplitEmptyStringWithEscape() {
		final String[] actual = Strings.split("", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { "" });
	}

	@Test
	public void shouldSplitTwoEmptyStringsWithEscape() {
		final String[] actual = Strings.split(";", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { "", "" });
	}

	@Test
	public void shouldSplitSeparatorWithEscape() {
		final String[] actual = Strings.split("\\;", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { ";" });
	}

	@Test
	public void shouldSplitTwoSeparatorsWithEscape() {
		final String[] actual = Strings.split("\\;;\\;", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { ";", ";" });
	}

	@Test
	public void shouldSplitTwoNonSeparators() {
		final String[] actual = Strings.split("A;B", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { "A", "B" });
	}

	@Test
	public void shouldSplitTwoEscapedSeparatorsWithEscape() {
		final String[] actual = Strings.split("\\\\\\^^\\\\\\^\\\\", '^', '\\');
		assertThat(actual).isEqualTo(new String[] { "\\^", "\\^\\" });
	}

	static class CharSequenceImpl implements CharSequence {

		final CharSequence delegate;

		CharSequenceImpl(CharSequence delegate) {
			this.delegate = delegate;
		}

		@Override
		public int length() {
			return delegate.length();
		}

		@Override
		public char charAt(int index) {
			return delegate.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return delegate.subSequence(start, end);
		}

		@Override
		public String toString() {
			return delegate.toString();
		}
	}

	static class OtherObject {
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
}
