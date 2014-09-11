/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import javaslang.IO;
import javaslang.collection.Node;
import javaslang.collection.Tree;
import javaslang.monad.Either;
import javaslang.monad.Try;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void shouldStringifyGrammar() {
		final String expected = "json : object\n     | array\n     | STRING\n     | NUMBER\n     | 'true'\n     | 'false'\n     | 'null'\n     ;\n\nobject : '{' ( NAME ':' json ( ',' NAME ':' json )* )? '}' ;\n\nNAME : '\"' STRING '\"' ;\n\nSTRING : [a-zA-Z0-9_$]+ ;\n\narray : '[' ( json ( ',' json )* )? ']' ;\n\nNUMBER : [0-9]+ ;";
		assertThat(new JSONGrammar().toString()).isEqualTo(expected);
	}

	// -- Grammar.parse()

	@Test
	public void shouldParseTextWhenMatching() {
		assertThat(new Grammar(Grammar.rule("root", Grammar.EOF)).parse("").toString()).isEqualTo(
				"Success(Tree(root EOF))");
	}

	@Test
	public void shouldParseTextWhenNotMatching() {
		assertThat(new Grammar(Grammar.rule("root", Grammar.ANY)).parse("").toString()).isEqualTo(
				"Failure(java.lang.IllegalArgumentException: cannot parse input at (1, 1))");
	}

	@Test
	public void shouldParseWhitespace() {
		final Parser WS = new Parser.Rule("WS", new Parser.Quantifier(new Parser.Charset(" \t\r\n"),
				Parser.Quantifier.Bounds.ZERO_TO_N));
		final Either<Integer, List<Node<Token>>> actual = WS.parse("  ", 0, true);
		final Either<Integer, List<Node<Token>>> expected = Parser.token("  ", 0, 2);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	// TODO: consider whitespace in parser rules
	public void shouldParseJSON() {

		final Grammar jsonGrammar = new JSONGrammar();
		/* TODO:DELME */System.out.println(jsonGrammar.toString() + "\n");

		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		/* TODO:DELME */System.out.println("Input:\n" + json.get());

		final Try<Tree<Token>> parseTree = json.flatMap(s -> jsonGrammar.parse(s));
		final String coffeeScriptString = parseTree
				.map(tree -> tree.toCoffeeScriptString())
				.recover(x -> x.getMessage())
				.get();
		final String lispString = parseTree.map(tree -> tree.toLispString()).recover(x -> x.getMessage()).get();
		/* TODO:DELME */System.out.println("\nParse tree (CoffeeScript-like representation):\n" + coffeeScriptString);
		/* TODO:DELME */System.out.println("\nParse tree (LISP-like representation):\n" + lispString);
	}

	// -- factory methods

	@Test
	public void shouldCreateRule() {
		assertThat(Grammar.rule("rule", Grammar.EOF).toString()).isEqualTo("rule : EOF ;");
	}

	@Test
	public void shouldCreateSubrule() {
		assertThat(Grammar.subRule(Grammar.ANY, Grammar.EOF).toString()).isEqualTo("( . | EOF )");
	}

	@Test
	public void shouldCreateSequence() {
		assertThat(Grammar.seq(Grammar.ANY, Grammar.EOF).toString()).isEqualTo(". EOF");
	}

	@Test
	public void shouldCreateCharset() {
		assertThat(Grammar.charset("a-zA-Z$_").toString()).isEqualTo("[a-zA-Z$_]");
	}

	@Test
	public void shouldCreateRange() {
		assertThat(Grammar.range('a', 'z').toString()).isEqualTo("'a'..'z'");
	}

	@Test
	public void shouldCreateStringLiteral() {
		assertThat(Grammar.str("text").toString()).isEqualTo("'text'");
	}

	@Test
	public void shouldCreateQuantifier0to1() {
		assertThat(Grammar._0_1(Grammar.ANY).toString()).isEqualTo(".?");
	}

	@Test
	public void shouldCreateQuantifier0toN() {
		assertThat(Grammar._0_n(Grammar.ANY).toString()).isEqualTo(".*");
	}

	@Test
	public void shouldCreateQuantifier_1toN() {
		assertThat(Grammar._1_n(Grammar.ANY).toString()).isEqualTo(".+");
	}

	@Test
	public void shouldCreateDelimitedList() {
		assertThat(Grammar.list(Grammar.ANY, ",").toString()).isEqualTo("( . ( ',' . )* )?");
	}

	@Test
	public void shouldCreateDelimitedListWithPrefixAndSuffix() {
		assertThat(Grammar.list(Grammar.ANY, ",", "{", "}").toString()).isEqualTo("'{' ( . ( ',' . )* )? '}'");
	}

	// -- Example grammar: JSON

	static class JSONGrammar extends Grammar {

		// define start rule
		JSONGrammar() {
			super(JSONGrammar::json);
		}

		// json : object | array | STRING | NUMBER | 'true' | 'false' | 'null' ;
		static Parser.Rule json() {
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
			return rule("STRING", _1_n(charset("a-zA-Z0-9_$")));
		}

		// fragment ESC : '\\' ( ["\\/bfnrt] | UNICODE ) ;
		// fragment UNICODE : 'u' HEX HEX HEX HEX ;
		// fragment HEX : [0-9a-fA-F] ;
		static Parser ESC() {
			// TODO
			return null;
		}

		static Parser NUMBER() {
			return rule("NUMBER", _1_n(charset("0-9")));
		}

		// property : NAME ':' json ;
		static Parser property() {
			return seq(JSONGrammar::NAME, str(":"), JSONGrammar::json);
		}

		// NAME : '"' STRING '"'
		static Parser NAME() {
			return rule("NAME", seq(str("\""), JSONGrammar::STRING, str("\"")));
		}
	}

	// -- Example grammar: Resursive expressions

	static class ExpressionGrammar extends Grammar {

		// define start rule
		ExpressionGrammar() {
			super(ExpressionGrammar::expr);
		}

		/**
		 * <pre>
		 * <code>
		 * expr : expr '*' expr
		 *      | expr '+' expr
		 *      | INT
		 *      ;
		 * 
		 * INT : '0'..'9'+ ;
		 * </code>
		 * </pre>
		 */
		static Parser.Rule expr() {
			return rule("expr",//
					seq(ExpressionGrammar::expr, str("*"), ExpressionGrammar::expr),//
					seq(ExpressionGrammar::expr, str("+"), ExpressionGrammar::expr),//
					ExpressionGrammar::INT);
		}

		/**
		 * A parser for positive natural numbers including zero.
		 * 
		 * @return A natural number parser.
		 */
		static Parser INT() {
			return _1_n(range('0', '9'));
		}
	}
}
