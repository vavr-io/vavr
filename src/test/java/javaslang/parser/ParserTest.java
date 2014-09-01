/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import javaslang.AssertionsExtensions;
import javaslang.AssertionsExtensions.CheckedRunnable;

import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

	@Test
	public void shouldNotInstantiable() {
		AssertionsExtensions.assertThat(Parsers.class).isNotInstantiable();
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

	// -- Range parser

	@Test
	public void shouldConvertRangeToString() {
		assertThat(new Parsers.Range('a', 'z').toString()).isEqualTo("'a'..'z'");
	}

	@Test
	public void shouldParseCharWithinRange() {
		final String actual = parse(new Parsers.Range('a', 'z'), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinRange() {
		final CheckedRunnable actual = () -> parse(new Parsers.Range('a', 'z'), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Charset parser

	@Test
	public void shouldConvertCharsetToString() {
		assertThat(new Parsers.Charset("a-z$_A-Z").toString()).isEqualTo("[a-z$_A-Z]");
	}

	@Test
	public void shouldParseCharWithinCharsetWithRange() {
		final String actual = parse(new Parsers.Charset("a-z"), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinCharsetWithRange() {
		final CheckedRunnable actual = () -> parse(new Parsers.Charset("a-z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTestingSingleChar() {
		final String actual = parse(new Parsers.Charset("a-z$_A-Z"), "$", false);
		assertThat(actual).isEqualTo("$");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTesting2ndRange() {
		final String actual = parse(new Parsers.Charset("a-z$_A-Z"), "D", false);
		assertThat(actual).isEqualTo("D");
	}

	@Test
	public void shouldNotParseChartNotWithinFullFledgedCharset() {
		final CheckedRunnable actual = () -> parse(new Parsers.Charset("a-z$_A-Z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
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
		final CheckedRunnable actual = () -> parse(Parsers.EOF.INSTANCE, "abc", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
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
		final CheckedRunnable actual = () -> parse(new Parsers.Literal("no match"), "literal!", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
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

	// -- Token

	@Test
	public void shouldConvertTokenToString() {
		assertThat(new Token("id", 0, 0).toString()).isEqualTo("id[0,0]");
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
