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
	public void shouldParseCharUsingAny() {
		final String actual = parse(Parsers.Any.INSTANCE, "abc");
		assertThat(actual).isEqualTo("a");
	}

	// -- CharRange parser

	@Test
	public void shouldParseCharWithinCharRange() {
		final String actual = parse(new Parsers.CharRange('a', 'z'), "abc");
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharOutofCharRange() {
		final Runnable actual = () -> parse(new Parsers.CharRange('a', 'z'), "@@@");
		assertThat(actual).isThrowing(AssertionError.class, "no character");
	}

	// -- CharSet parser

	@Test
	public void shouldParseCharWithinCharSetWithRange() {
		final String actual = parse(new Parsers.CharSet("a-z"), "abc");
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharOutOfCharSetWithRange() {
		final Runnable actual = () -> parse(new Parsers.CharSet("a-z"), "@@@");
		assertThat(actual).isThrowing(AssertionError.class, "no character");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharSetTestingSingleChar() {
		final String actual = parse(new Parsers.CharSet("a-z$_A-Z"), "$");
		assertThat(actual).isEqualTo("$");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharSetTesting2ndRange() {
		final String actual = parse(new Parsers.CharSet("a-z$_A-Z"), "D");
		assertThat(actual).isEqualTo("D");
	}

	@Test
	public void shouldNotParseCharOutOfFullFledgedCharSet() {
		final Runnable actual = () -> parse(new Parsers.CharSet("a-z$_A-Z"), "@@@");
		assertThat(actual).isThrowing(AssertionError.class, "no character");
	}

	// -- EOF parser

	@Test
	public void shouldRecognizeEOF() {
		final String actual = parse(Parsers.EOF.INSTANCE, "");
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldRecognizeNotEOF() {
		final Runnable actual = () -> parse(Parsers.EOF.INSTANCE, "abc");
		assertThat(actual).isThrowing(AssertionError.class, "no character");
	}

	// -- Literal parser

	@Test
	public void shouldParseLiteral() {
		final String actual = parse(new Parsers.Literal("literal"), "literal!");
		assertThat(actual).isEqualTo("literal");
	}

	@Test
	public void shouldNotParseLiteralIfNotMatching() {
		final Runnable actual = () -> parse(new Parsers.Literal("no match"), "literal!");
		assertThat(actual).isThrowing(AssertionError.class, "no character");
	}

	// -- Rule parser

	@Test
	@Ignore
	public void shouldParseCharUsingRule() {
		fail("not implemented");
	}

	// -- Sequence parser

	@Test
	@Ignore
	public void shouldParseCharUsingSequence() {
		fail("not implemented");
	}

	// -- parse helpers

	private String parse(Parser parser, String text) {
		return parser
				.parse(text, 0)
				.right()
				.orElseThrow(() -> new AssertionError("no character"))
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
