/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Assertions.assertThat;

import org.junit.Test;

public class ParsersTest {

	@Test
	public void shouldNotInstantiable() {
		assertThat(Parsers.class).isNotInstantiable();
	}

	// -- parser implementations

	@Test
	public void shouldParseCharUsingAny() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingCharRange() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingCharSet() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingEOF() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingLiteral() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingRule() {
		// TODO: fail("not implemented");
	}

	@Test
	public void shouldParseCharUsingSequence() {
		// TODO: fail("not implemented");
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
	public void shouldMatchTheLexerRuleThatRecognizesTheMostInputCharacters() {
		// TODO
	}

}
