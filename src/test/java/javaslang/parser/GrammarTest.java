/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.InputStream;
import java.nio.charset.Charset;

import javaslang.collection.Tree;
import javaslang.io.IO;
import javaslang.monad.Try;

import org.junit.Ignore;
import org.junit.Test;

public class GrammarTest {

	@Test
	public void shouldStringifyGrammar() {
		final String expected = "json : object\n     | array\n     | STRING\n     | NUMBER\n     | 'true'\n     | 'false'\n     | 'null'\n     ;\n\nobject : '{' ( STRING ':' json ( ',' STRING ':' json )* )? '}' ;\n\narray : '[' ( json ( ',' json )* )? ']' ;\n\nSTRING : [a-zA-Z0-9_$]+ ;\n\nNUMBER : [0-9]+ ;";
		assertThat(new JSONGrammar().toString()).isEqualTo(expected);
	}

	@Test
	@Ignore
	// TODO: consider whitespace in parser rules
	public void shouldParseJSON() {

		final Grammar jsonGrammar = new JSONGrammar();
		/* TODO:DELME */System.out.println(jsonGrammar.toString() + "\n");

		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		/* TODO:DELME */System.out.println("Input:\n" + json.get());

		final Try<Tree<Token>> parseTree = json.flatMap(s -> jsonGrammar.parse(s));
		final String result = parseTree.map(tree -> tree.toString()).recover(x -> x.getMessage()).get();
		/* TODO:DELME */System.out.println("Parse tree:\n" + result);
	}

	static class JSONGrammar extends Grammar {

		// define start rule
		JSONGrammar() {
			super(JSONGrammar::json);
		}

		// json : object | array | STRING | NUMBER | 'true' | 'false' | 'null' ;
		static Parsers.Rule json() {
			return rule("json", JSONGrammar::object, JSONGrammar::array, JSONGrammar::STRING, JSONGrammar::NUMBER,
					str("true"), str("false"), str("null"));
		}

		// object : '{' ( property ( ',' property )* )? '}' ;
		static Parser object() {
			return rule("object", list(JSONGrammar::property, ",", "{", "}"));
		}

		// array : '[' ( json ( ',' json )* )? ']'
		static Parser array() {
			return rule("array", list(JSONGrammar::json, ",", "[", "]"));
		}

		// STRING : '"' (ESC | ~["\\])* '"' ;
		static Parser STRING() {
			// TODO
			return rule("STRING", _1_n(charSet("a-zA-Z0-9_$")));
		}

		// fragment ESC : '\\' ( ["\\/bfnrt] | UNICODE ) ;
		// fragment UNICODE : 'u' HEX HEX HEX HEX ;
		// fragment HEX : [0-9a-fA-F] ;
		static Parser ESC() {
			// TODO
			return null;
		}

		static Parser NUMBER() {
			return rule("NUMBER", _1_n(charSet("0-9")));
		}

		// pair : jsonString ':' json ;
		static Parser property() {
			return seq(JSONGrammar::STRING, str(":"), JSONGrammar::json);
		}
	}
}
