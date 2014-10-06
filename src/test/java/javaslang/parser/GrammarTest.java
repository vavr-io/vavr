/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.IO.UTF8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import javaslang.IO;
import javaslang.collection.Tree;
import javaslang.monad.Either;
import javaslang.monad.Try;
import javaslang.parser.Parser.Charset;
import javaslang.parser.Parser.ParseResult;
import javaslang.parser.Parser.Quantifier;
import javaslang.parser.Parser.Rule;
import javaslang.parser.Parser.RulePart;

import org.junit.Ignore;
import org.junit.Test;

public class GrammarTest {

	@Test
	public void shouldStringifyGrammar() {
		final String expected = "grammar JSON ;\n\njson : object\n     | array\n     | STRING\n     | NUMBER\n     | 'true'\n     | 'false'\n     | 'null'\n     ;\n\nobject : '{' ( NAME ':' json ( ',' NAME ':' json )* )? '}' ;\n\nNAME : '\"' STRING '\"' ;\n\nSTRING : [a-zA-Z0-9_$]+ ;\n\narray : '[' ( json ( ',' json )* )? ']' ;\n\nNUMBER : [0-9]+ ;";
		assertThat(new JSONGrammar().toString()).isEqualTo(expected);
	}

	// -- Grammar.parse()

	@Test
	public void shouldParseTextWhenMatching() {
		assertThat(Grammar.of("test", Grammar.rule("root", Grammar.EOF)).parse("").toString()).isEqualTo(
				"Success(Tree(root <EOF>))");
	}

	@Test
	public void shouldParseTextWhenNotMatching() {
		assertThat(Grammar.of("test", Grammar.rule("root", Grammar.ANY)).parse("").toString()).isEqualTo(
				"Failure(java.lang.IllegalArgumentException: cannot parse input at (1, 1))");
	}

	@Test
	public void shouldParseWhitespace() {
		final Parser WS = new Rule("WS", new Quantifier(new Charset(" \t\r\n"), 0, Quantifier.UNBOUNDED));
		final Either<Integer, ParseResult> actual = WS.parse("  ", 0, true);
		final Either<Integer, ParseResult> expected = Parser.token("  ", 0, 2, false);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldParseSimpleSequence() {
		final Try<Tree<Token>> parseTree = new SimpleSequenceGrammar().parse("abc");
		assertThat(parseTree.get().toLispString()).isEqualTo("Tree(startRule 'a' 'b' 'c')");
	}

	@Test
	public void shouldParseJSON() {

		final Grammar jsonGrammar = new JSONGrammar();
		/* TODO:DELME */System.out.println(jsonGrammar.toString() + "\n");

		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, UTF8);
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
		assertThat(Grammar.subrule(Grammar.ANY, Grammar.EOF).toString()).isEqualTo("( . | EOF )");
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
	public void shouldCreateQuantifierFromLowerBoundToUpperBound() {
		assertThat(Grammar.mul(1, 2, Grammar.ANY).toString()).isEqualTo(".{1,2}");
	}

	@Test
	public void shouldCreateQuantifierTimes() {
		assertThat(Grammar.mul(3, Grammar.ANY).toString()).isEqualTo(".{3}");
	}

	@Test
	public void shouldCreateNegation() {
		assertThat(Grammar.not(Grammar.ANY).toString()).isEqualTo("!.");
	}

	@Test
	public void shouldCreateDelimitedList() {
		assertThat(Grammar.list(Grammar.ANY, ",").toString()).isEqualTo("( . ( ',' . )* )?");
	}

	@Test
	public void shouldCreateDelimitedListWithPrefixAndSuffix() {
		assertThat(Grammar.list(Grammar.ANY, ",", "{", "}").toString()).isEqualTo("'{' ( . ( ',' . )* )? '}'");
	}

	// -- whitespace

	@Test
	public void shouldParseGroupsWithoutWhitespace() {
		final String actual = new GroupGrammar().parse("(abc)(def ghi)").get().toString();
		final String expected = "Tree(groups (group '(' 'abc' ')') (group '(' 'def' 'ghi' ')') <EOF>)";
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldParseGroupsWithWhitespace() {
		final String actual = new GroupGrammar().parse("( abc ) ( def ghi )").get().toString();
		final String expected = "Tree(groups (group '(' 'abc' ')') (group '(' 'def' 'ghi' ')') <EOF>)";
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldParseRichStringWithoutEatingUpWhitespace() {
		final String actual = new RichStringGrammar().parse("\"\"\" test \"\"\"").get().toString();
		final String expected = "Tree('\"\"\" test \"\"\"')";
		assertThat(actual).isEqualTo(expected);
	}

	// -- direct recursion

	@Test
	@Ignore
	public void shouldSupportDirectRecursionOnMatchingInput() {
		final Try<Tree<Token>> actual = new ExpressionGrammar().parse("1 + 2 * 3");
		/* TODO(#32):DEBUG */System.out.println(actual.get().toString());
	}

	@Test
	@Ignore
	public void shouldSupportDirectRecursionOnNonMatchingInput() {
		final Try<Tree<Token>> actual = new ExpressionGrammar().parse("1 + 2 *");
		/* TODO(#32):DEBUG */System.out.println(actual.get().toString());
	}

	@Test
	public void shouldMatchRecursivelyWithPrudentSnakeGrammar() {
		final Try<Tree<Token>> actual = new PrudentSnakeGrammar().parse("@@@");
		/* TODO(#32):DEBUG */System.out.println(actual.get().toString());
	}

	// -- indirect recursion

	@Test
	@Ignore
	public void shouldSupportIndirectRecursionOnMatchingInput() {
		final Try<Tree<Token>> actual = new IndirectExpressionGrammar().parse("1 + 2 * 3");
		/* TODO(#32):DEBUG */System.out.println(actual.get().toString());
	}

	@Test
	@Ignore
	public void shouldSupportIndirectRecursionOnNonMatchingInput() {
		final Try<Tree<Token>> actual = new IndirectExpressionGrammar().parse("1 + 2 *");
		/* TODO(#32):DEBUG */System.out.println(actual.get().toString());
	}

	/**
	 * If the start rule does not contain an explicit EOF transition, ParserATNSimulator.adaptivePredict may fail to
	 * return a viable alternative.
	 * <p>
	 * Grammar:
	 * 
	 * <pre>
	 * <code>
	 * start : ID | ID INT ID;
	 * ID : [a-z]+;
	 * INT : [0-9]+;
	 * WS : [ \t]+ -> skip;
	 * </code>
	 * </pre>
	 * 
	 * Input: {@code x 1}
	 */
	@Test
	public void shouldAntlrIssue118() {
		final String actual = new AntlrIssue118Grammar().parse("x 1").get().toString();
		assertThat(actual).isEqualTo("Tree(start 'x')");
	}

	static class AntlrIssue118Grammar extends Grammar {

		AntlrIssue118Grammar() {
			super("AntlrIssue118");
		}

		@Override
		protected Rule getStartRule() {
			return start();
		}

		// start : ID | ID INT ID
		Rule start() {
			return rule("start", ref(this::ID), seq(ref(this::ID), ref(this::INT), ref(this::ID)));
		}

		// ID : [a-z]+
		Rule ID() {
			return rule("ID", _1_n(charset("a-z")));
		}

		// INT : [0-9]+
		Rule INT() {
			return rule("INT", _1_n(charset("0-9")));
		}
	}

	// -- Example grammar: Simple sequence of tokens

	static class SimpleSequenceGrammar extends Grammar {

		SimpleSequenceGrammar() {
			super("SimpleSequence");
		}

		@Override
		protected Rule getStartRule() {
			return startRule();
		}

		// startRule : 'a' 'b' 'c'
		Rule startRule() {
			return rule("startRule", seq(str("a"), str("b"), str("c")));
		}
	}

	// -- Example grammar: JSON

	static class JSONGrammar extends Grammar {

		JSONGrammar() {
			super("JSON");
		}

		@Override
		protected Rule getStartRule() {
			return json();
		}

		// json : object | array | STRING | NUMBER | 'true' | 'false' | 'null' ;
		Rule json() {
			return rule("json", ref(this::object), ref(this::array), ref(this::STRING), ref(this::NUMBER), str("true"),
					str("false"), str("null"));
		}

		// object : '{' ( property ( ',' property )* )? '}' ;
		Rule object() {
			return rule("object", list(property(), ",", "{", "}"));
		}

		// array : '[' ( json ( ',' json )* )? ']'
		Rule array() {
			return rule("array", list(ref(this::json), ",", "[", "]"));
		}

		// STRING : '"' (ESC | ~["\\])* '"' ;
		Rule STRING() {
			// TODO
			return rule("STRING", _1_n(charset("a-zA-Z0-9_$")));
		}

		// fragment ESC : '\\' ( ["\\/bfnrt] | UNICODE ) ;
		// fragment UNICODE : 'u' HEX HEX HEX HEX ;
		// fragment HEX : [0-9a-fA-F] ;
		Parser ESC() {
			// TODO
			return null;
		}

		// NUMBER : [0-9]+
		Rule NUMBER() {
			return rule("NUMBER", _1_n(charset("0-9")));
		}

		// property : NAME ':' json ;
		RulePart property() {
			return seq(ref(this::NAME), str(":"), ref(this::json));
		}

		// NAME : '"' STRING '"'
		Rule NAME() {
			return rule("NAME", seq(str("\""), ref(this::STRING), str("\"")));
		}
	}

	/**
	 * Grammar for groups of words.
	 * 
	 * <pre>
	 * <code>
	 * groups : group* EOF
	 * 
	 * group : '(' WORD+ ')'
	 * 
	 * WORD : 'a'..'z'+
	 * </code>
	 * </pre>
	 */
	static class GroupGrammar extends Grammar {

		GroupGrammar() {
			super("Group");
		}

		@Override
		protected Rule getStartRule() {
			return groups();
		}

		// groups : group* EOF
		Rule groups() {
			return rule("groups", seq(_0_n(ref(this::group)), EOF));
		}

		// group : '(' WORD+ ')'
		Rule group() {
			return rule("group", seq(str("("), _1_n(ref(this::WORD)), str(")")));
		}

		// WORD : 'a'..'z'+
		Rule WORD() {
			return rule("WORD", _1_n(range('a', 'z')));
		}
	}

	/**
	 * {@code richString : '"""' .*? '"""'}
	 */
	static class RichStringGrammar extends Grammar {

		RichStringGrammar() {
			super("RichString");
		}

		@Override
		protected Rule getStartRule() {
			return RichString();
		}

		// RichString : '"""' [ a-z]* '"""'
		Rule RichString() {
			// TODO: issue #30: .*? instead of [ a-z]*
			return rule("RichString", seq(str("\"\"\""), _0_n(charset(" a-z")), str("\"\"\"")));
		}
	}

	// -- Example grammar: Resursive expressions

	static class ExpressionGrammar extends Grammar {

		ExpressionGrammar() {
			super("Expression");
		}

		@Override
		protected Rule getStartRule() {
			return expr();
		}

		// expr : expr '*' expr | expr '+' expr | INT
		Rule expr() {
			return rule("expr",//
					seq(ref(this::expr), str("*"), ref(this::expr)),//
					seq(ref(this::expr), str("+"), ref(this::expr)),//
					ref(this::INT));
		}

		// INT : '0'..'9'+
		Rule INT() {
			return rule("INT", _1_n(range('0', '9')));
		}
	}

	static class IndirectExpressionGrammar extends Grammar {

		IndirectExpressionGrammar() {
			super("IndirectExpression");
		}

		@Override
		protected Rule getStartRule() {
			return expr();
		}

		// expr : mul | add | INT
		Rule expr() {
			return rule("expr", ref(this::mul), ref(this::add), ref(this::INT));
		}

		// mul : expr '*' expr
		Rule mul() {
			return rule("mul", seq(ref(this::expr), str("*"), ref(this::expr)));
		}

		// add : expr '+' expr
		Rule add() {
			return rule("mul", seq(ref(this::expr), str("+"), ref(this::expr)));
		}

		// INT : '0'..'9'+
		Rule INT() {
			return rule("INT", _1_n(range('0', '9')));
		}
	}

	/**
	 * <pre>
	 * <code>
	 * eat : eat '@' | EOF
	 * </code>
	 * </pre>
	 */
	static class AheadOfTimeSnakeGrammar extends Grammar {

		AheadOfTimeSnakeGrammar() {
			super("AheadOfTimeSnake");
		}

		@Override
		protected Rule getStartRule() {
			return eat();
		}

		// eat : eat '@' | EOF
		Rule eat() {
			return rule("eat", seq(ref(this::eat), str("@")), EOF);
		}
	}

	/**
	 * <pre>
	 * <code>
	 * eat : '@' eat | EOF
	 * </code>
	 * </pre>
	 */
	static class HungrySnakeGrammar extends Grammar {

		HungrySnakeGrammar() {
			super("HungrySnake");
		}

		@Override
		protected Rule getStartRule() {
			return eat();
		}

		// eat : '@' eat | EOF
		Rule eat() {
			return rule("eat", seq(str("@"), ref(this::eat)), EOF);
		}
	}

	/**
	 * <pre>
	 * <code>
	 * eat : EOF | '@' eat
	 * </code>
	 * </pre>
	 */
	static class PrudentSnakeGrammar extends Grammar {

		PrudentSnakeGrammar() {
			super("PrudentSnake");
		}

		@Override
		protected Rule getStartRule() {
			return eat();
		}

		// eat : EOF | '@' eat
		Rule eat() {
			return rule("eat", EOF, seq(str("@"), ref(this::eat)));
		}
	}
}
