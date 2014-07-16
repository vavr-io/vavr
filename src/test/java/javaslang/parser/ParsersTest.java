/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.parser.Parsers.Literal;
import javaslang.parser.Parsers.Rule;

import org.junit.Test;

public class ParsersTest {

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
	@Test
	public void shouldMatchTheLexerRuleThatRecognizesTheMostInputCharacters() {
		// TODO
	}

	/**
	 * Given an input sequence {@code 'aaa'}, the following {@code all} rule should match.
	 * 
	 * <pre>
	 * <code>
	 * grammar
	 *   : LexerRule1
	 *   | LexerRule2
	 *   | .*         # all
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
	@Test
	public void shouldMatchTheAllRule() {
		// TODO
	}
	
	@Test
	public void shouldConvertRuleToString() {
		final String actual = new Rule("testRule", new Literal("123")).toString();
		assertThat(actual).isEqualTo("testRule\n  : '123'\n  ;");
	}

}
