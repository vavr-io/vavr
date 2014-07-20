/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javaslang.Strings;
import javaslang.Tuples;
import javaslang.Tuples.Tuple1;

import org.junit.Test;

public class StringsTest {
	
	@Test
	public void shouldConvertNullToString() {
		final String actual = Strings.toString(null);
		assertThat(actual).isEqualTo("null");
	}

	@Test
	public void shouldDetectDirectLoopOnToString() {
		final Object[] loop = new Object[1];
		loop[0] = loop;
		final String actual = Strings.toString(loop);
		assertThat(actual).isEqualTo("[...]");
	}

	@Test
	public void shouldDetectIndirectLoopOnToString() {
		final List<Tuple1<List<?>>> list = new ArrayList<>();
		final Tuple1<List<?>> tuple = Tuples.of(list);
		list.add(tuple);
		final String actual = Strings.toString(list);
		assertThat(actual).isEqualTo("((...))");
	}
	
	@Test
	public void shouldCallToStringRecursively() {
		final String actual = Tuples.of(Tuples.of(1)).toString();
		final String expected = "((1))";
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void shouldVisitTwoSimilarPathsOnToString() {
		final Object[] path = { "path" };
		final Object[] array = { path, path };
		final String actual = Strings.toString(array);
		assertThat(actual).isEqualTo("[[\"path\"], [\"path\"]]");
	}
	
	@Test
	public void shouldConvertPrimitiveArrayToString() {
		final boolean[] array = { true, false };
		final String actual = Strings.toString(array);
		assertThat(actual).isEqualTo("[true, false]");
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
	public void shouldJoinTwoNonSeparators() {
		final String actual = Strings.join(new String[] { "A", "B" }, ';', '\\');
		assertThat(actual).isEqualTo("A;B");
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
	public void shouldSplitTwoNonSeparators() {
		final String[] actual = Strings.split("A;B", ';', '\\');
		assertThat(actual).isEqualTo(new String[] { "A", "B" });
	}

	@Test
	public void shouldSplitTwoEscapedSeparatorsWithEscape() {
		final String[] actual = Strings.split("\\\\\\^^\\\\\\^\\\\", '^', '\\');
		assertThat(actual).isEqualTo(new String[] { "\\^", "\\^\\" });
	}
	
}
