package javaslang.lang;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class StringsTest {
	
	@Test
	public void shouldConvertNullToString() {
		final String actual = Strings.toString(null);
		assertThat(actual).isEqualTo("null");
	}

	@Test
	public void shouldDetectInfiniteLoopsOnToString() {
		final Object[] loop = new Object[1];
		loop[0] = loop;
		final String actual = Strings.toString(loop);
		assertThat(actual).isEqualTo("[...]");
	}

	@Test
	public void shouldVisitTwoSimilarPathsOnToString() {
		final Object[] path = new Object[] { "path" };
		final Object[] array = new Object[] { path, path };
		final String actual = Strings.toString(array);
		assertThat(actual).isEqualTo("[[\"path\"], [\"path\"]]");
	}

	@Test
	public void shouldRepeatNullAsNull() {
		final String s = Strings.repeat(null, 2);
		assertThat(s).isNull();
	}

	@Test
	public void shouldRepeatZeroTimes() {
		final String s = Strings.repeat("x", 0);
		assertThat(s).isEqualTo("");
	}

	@Test
	public void shouldRepeatOneTime() {
		final String s = Strings.repeat("x", 1);
		assertThat(s).isEqualTo("x");
	}

	@Test
	public void shouldRepeatTwoTimes() {
		final String s = Strings.repeat("x", 2);
		assertThat(s).isEqualTo("xx");
	}

	@Test
	public void shouldRepeatNegativeTimes() {
		final String s = Strings.repeat("x", -2);
		assertThat(s).isEqualTo("");
	}

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
	
	@Test
	public void shouldJoinEmptyStringWithEscape() {
		final String actual = Strings.join(new String[] { "" }, ';', '\\');
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldJoinTwoEmptyStringsWithEscape() {
		final String actual = Strings.join(new String[] { "", "" }, ';', '\\');
		assertThat(actual).isEqualTo(";");
	}

	@Test
	public void shouldJoinSeparatorWithEscape() {
		final String actual = Strings.join(new String[] { ";" }, ';', '\\');
		assertThat(actual).isEqualTo("\\;");
	}

	@Test
	public void shouldJoinTwoSeparatorsWithEscape() {
		final String actual = Strings.join(new String[] { ";", ";" }, ';', '\\');
		assertThat(actual).isEqualTo("\\;;\\;");
	}

	@Test
	public void shouldJoinTwoEscapedSeparatorsWithEscape() {
		final String actual = Strings.join(new String[] { "\\^", "\\^\\" }, '^', '\\');
		assertThat(actual).isEqualTo("\\\\\\^^\\\\\\^\\\\");
	}
	
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
	public void shouldSplitTwoEscapedSeparatorsWithEscape() {
		final String[] actual = Strings.split("\\\\\\^^\\\\\\^\\\\", '^', '\\');
		assertThat(actual).isEqualTo(new String[] { "\\^", "\\^\\" });
	}
	
}
