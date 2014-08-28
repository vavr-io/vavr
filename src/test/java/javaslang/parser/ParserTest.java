/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

	@Test
	public void shouldNotInstantiable() {
		assertThat(Parsers.class).isNotInstantiable();
	}

	// -- Any parser

	@Test
	public void shouldConvertAnyToString() {
		assertThat(Parsers.Any.INSTANCE.toString()).isEqualTo(".");
	}

	@Test
	public void shouldParseCharUsingAny() {
		final String actual = parse(Parsers.Any.INSTANCE, "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	// -- CharRange parser

	@Test
	public void shouldConvertCharRangeToString() {
		assertThat(new Parsers.CharRange('a', 'z').toString()).isEqualTo("'a'..'z'");
	}

	@Test
	public void shouldParseCharWithinCharRange() {
		final String actual = parse(new Parsers.CharRange('a', 'z'), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinCharRange() {
		final Runnable actual = () -> parse(new Parsers.CharRange('a', 'z'), "@@@", false);
		assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- CharSet parser

	@Test
	public void shouldConvertCharSetToString() {
		assertThat(new Parsers.CharSet("a-z$_A-Z").toString()).isEqualTo("[a-z$_A-Z]");
	}

	@Test
	public void shouldParseCharWithinCharSetWithRange() {
		final String actual = parse(new Parsers.CharSet("a-z"), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinCharSetWithRange() {
		final Runnable actual = () -> parse(new Parsers.CharSet("a-z"), "@@@", false);
		assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharSetTestingSingleChar() {
		final String actual = parse(new Parsers.CharSet("a-z$_A-Z"), "$", false);
		assertThat(actual).isEqualTo("$");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharSetTesting2ndRange() {
		final String actual = parse(new Parsers.CharSet("a-z$_A-Z"), "D", false);
		assertThat(actual).isEqualTo("D");
	}

	@Test
	public void shouldNotParseChartNotWithinFullFledgedCharSet() {
		final Runnable actual = () -> parse(new Parsers.CharSet("a-z$_A-Z"), "@@@", false);
		assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- EOF parser

	@Test
	public void shouldConvertEOFToString() {
		assertThat(Parsers.EOF.INSTANCE.toString()).isEqualTo("EOF");
	}

	@Test
	public void shouldRecognizeEOF() {
		final String actual = parse(Parsers.EOF.INSTANCE, "", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldRecognizeNotEOF() {
		final Runnable actual = () -> parse(Parsers.EOF.INSTANCE, "abc", false);
		assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Literal parser

	@Test
	public void shouldConvertLiteralToString() {
		assertThat(new Parsers.Literal("v'ger\\").toString()).isEqualTo("'v\\'ger\\\\'");
	}

	@Test
	public void shouldParseLiteral() {
		final String actual = parse(new Parsers.Literal("literal"), "literal!", false);
		assertThat(actual).isEqualTo("literal");
	}

	@Test
	public void shouldNotParseLiteralIfNotMatching() {
		final Runnable actual = () -> parse(new Parsers.Literal("no match"), "literal!", false);
		assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Rule parser

	@Test
	public void shouldConvertRuleToString() {
		assertThat(expr().toString()).isEqualTo("expr : expr '+' expr\n     | expr '*' expr\n     | INT\n     ;");
	}

	// DEV-NOTE: recursive self-reference of the rule
	static Parsers.Rule expr() {
		return new Parsers.Rule("expr", new Parsers.Sequence(ParserTest::expr, new Parsers.Literal("+"),
				ParserTest::expr), new Parsers.Sequence(ParserTest::expr, new Parsers.Literal("*"), ParserTest::expr),
				new Parsers.Rule("INT", () -> null));
	}

	@Test
	@Ignore
	public void shouldParseTextUsingRule() {
		fail("not implemented");
	}

	// TODO: shouldParseTextUsingFirstMatchingRule

	// -- Sequence parser

	@Test
	public void shouldParseTextUsingSequence() {
		final Parser[] parsers = new Parser[] {
				new Parsers.Literal("one"),
				Parsers.Any.INSTANCE,
				new Parsers.Literal("two"),
				Parsers.Any.INSTANCE,
				new Parsers.Literal("three") };
		final String actual = parse(new Parsers.Sequence(parsers), "one two three...", false);
		assertThat(actual).isEqualTo("one two three");
	}

	// -- parse helpers

	private String parse(Parser parser, String text, boolean lexer) {
		return parser
				.parse(text, 0, lexer)
				.right()
				.orElseThrow(i -> new AssertionError("no match at index " + i))
				.getValue()
				.asSubstringOf(text);
	}

	// -- resolving ambiguities

	/**
	 * Given an input sequence {@code 'aaa'}, the following {@code LexerRule2} should match.
	 * 
	 * <pre>
	 * <code>
	 * grammar
	 *   : LexerRule1
	 *   | LexerRule2
	 *   ; // EOF not required
	 * 
	 * LexerRule1
	 *   : 'a'
	 *   ;
	 *   
	 * LexerRule2
	 *   : 'aa'
	 *   ;
	 * </code>
	 * </pre>
	 */
	// TODO: antlr uses the first rule which matches
	@Test
	@Ignore
	public void shouldMatchTheLexerRuleThatRecognizesTheMostInputCharacters() {
		fail("not implemented");
	}

}
