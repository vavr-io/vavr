/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.text.Multiplicity.Bounds.ZERO_TO_N;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;

import java.io.InputStream;
import java.nio.charset.Charset;

import javaslang.exception.Try;
import javaslang.io.IO;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void bootstrap() {

		final Grammar jsonGrammar = new JSONGrammar();
		System.out.println(jsonGrammar.stringify());

		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		System.out.println("Input:\n" + json.get());

		final Try<Tree<Token>> parseTree = json.flatMap(s -> jsonGrammar.parse(s));
		final String result = parseTree.map(tree -> tree.toString()).recover(x -> x.getMessage()).get();
		System.out.println("Parse tree:\n" + result);

	}

	static class JSONGrammar extends Grammar {

		JSONGrammar() {
			super(JSONGrammar::JSON);
		}

		static Parser JSON() {
			return new Branch("JSON", JSONGrammar::JSON_OBJECT, JSONGrammar::JSON_ARRAY, new Branch(new Literal("a"), new Literal("b")),
					JSONGrammar::JSON_NUMBER, JSONGrammar::JSON_BOOLEAN);
		}

		static Parser JSON_OBJECT() {
			return new Sequence("JSON_OBJECT", new Literal("{"), n(PAIR(), ","), new Literal("}"));
		}

		static Parser PAIR() {
			return new Sequence("PAIR", JSONGrammar::KEY, new Literal(":"), JSONGrammar::JSON);
		}

		static Parser KEY() {
			return new Sequence("KEY", new Branch(new Literal("\"a\""), new Literal("\"b\"")));
		}

		static Parser JSON_ARRAY() {
			return new Sequence("JSON_ARRAY", new Literal("["), n(JSON_NUMBER(), ","), new Literal(
					"]"));
		}

		static Parser JSON_NUMBER() {
			return new Sequence("JSON_NUMBER", new Multiplicity(new Sequence(new Literal("d"), new Literal("d")), ZERO_TO_N), new Branch(new Literal("1"), new Literal("2"), new Branch("Hello", new Literal("hello")), new Sequence(new Branch(new Literal("X"), new Literal("Y")), new Literal("A")), new Literal("3")));
		}

		static Parser JSON_BOOLEAN() {
			return new Sequence("JSON_BOOLEAN", new Branch(new Literal("true"), new Literal("false")));
		}
		
		/**
		 * n(P, ',') = [ P [ ',' P]* ]?
		 * 
		 * @param parser
		 * @param separator
		 * @return
		 */
		static Parser n(Parser parser, String separator) {

			// [ ',' P]*
			final Parser more = new Multiplicity(new Sequence(new Literal(separator), parser), ZERO_TO_N);

			// [ P <more> ]?
			return new Multiplicity(new Sequence(parser, more), ZERO_TO_ONE);
		}
	}

}
