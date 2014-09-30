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
		final String expected = "json : object\n     | array\n     | STRING\n     | NUMBER\n     | 'true'\n     | 'false'\n     | 'null'\n     ;\n\nobject : '{' ( NAME ':' json ( ',' NAME ':' json )* )? '}' ;\n\nNAME : '\"' STRING '\"' ;\n\nSTRING : [a-zA-Z0-9_$]+ ;\n\narray : '[' ( json ( ',' json )* )? ']' ;\n\nNUMBER : [0-9]+ ;";
		assertThat(new JSONGrammar().toString()).isEqualTo(expected);
	}

	// -- Grammar.parse()

	@Test
	public void shouldParseTextWhenMatching() {
		assertThat(new Grammar(Grammar.rule("root", Grammar.EOF)).parse("").toString()).isEqualTo(
				"Success(Tree(root <EOF>))");
	}

	@Test
	public void shouldParseTextWhenNotMatching() {
		assertThat(new Grammar(Grammar.rule("root", Grammar.ANY)).parse("").toString()).isEqualTo(
				"Failure(java.lang.IllegalArgumentException: cannot parse input at (1, 1))");
	}

	@Test
	public void shouldParseWhitespace() {
		final Parser WS = new Rule("WS", new Quantifier(new Charset(" \t\r\n"), 0, Quantifier.UNBOUNDED));
		final Either<Integer, ParseResult> actual = WS.parse("  ", 0, true);
		final Either<Integer, ParseResult> expected = Parser.token("  ", 0, 2, true);
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
		final String expected = "Tree(richString '\"\"\"' ' test ' '\"\"\"')";
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
			super(AntlrIssue118Grammar::start);
		}

		static Rule start() {
			return rule("start",//
					ref(AntlrIssue118Grammar::ID),//
					seq(ref(AntlrIssue118Grammar::ID), ref(AntlrIssue118Grammar::INT), ref(AntlrIssue118Grammar::ID)));
		}

		static Rule ID() {
			return rule("ID", _1_n(charset("a-z")));
		}

		static Rule INT() {
			return rule("INT", _1_n(charset("0-9")));
		}
	}

	// -- Example grammar: Simple sequence of tokens

	static class SimpleSequenceGrammar extends Grammar {

		SimpleSequenceGrammar() {
			super(SimpleSequenceGrammar::startRule);
		}

		static Rule startRule() {
			return rule("startRule", seq(str("a"), str("b"), str("c")));
		}
	}

	// -- Example grammar: JSON

	static class JSONGrammar extends Grammar {

		// define start rule
		JSONGrammar() {
			super(JSONGrammar::json);
		}

		// json : object | array | STRING | NUMBER | 'true' | 'false' | 'null' ;
		static Rule json() {
			return rule("json", ref(JSONGrammar::object), ref(JSONGrammar::array), ref(JSONGrammar::STRING),
					ref(JSONGrammar::NUMBER), str("true"), str("false"), str("null"));
		}

		// object : '{' ( property ( ',' property )* )? '}' ;
		static Rule object() {
			return rule("object", list(property(), ",", "{", "}"));
		}

		// array : '[' ( json ( ',' json )* )? ']'
		static Rule array() {
			return rule("array", list(ref(JSONGrammar::json), ",", "[", "]"));
		}

		// STRING : '"' (ESC | ~["\\])* '"' ;
		static Rule STRING() {
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

		static Rule NUMBER() {
			return rule("NUMBER", _1_n(charset("0-9")));
		}

		// property : NAME ':' json ;
		static RulePart property() {
			return seq(ref(JSONGrammar::NAME), str(":"), ref(JSONGrammar::json));
		}

		// NAME : '"' STRING '"'
		static Rule NAME() {
			return rule("NAME", seq(str("\""), ref(JSONGrammar::STRING), str("\"")));
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
		// define start rule
		GroupGrammar() {
			super(GroupGrammar::groups);
		}

		static Rule groups() {
			return rule("groups", seq(_0_n(ref(GroupGrammar::group)), EOF));
		}

		static Rule group() {
			return rule("group", seq(str("("), _1_n(ref(GroupGrammar::WORD)), str(")")));
		}

		static Rule WORD() {
			return rule("WORD", _1_n(range('a', 'z')));
		}
	}

	/**
	 * {@code richString : '"""' .*? '"""'}
	 */
	static class RichStringGrammar extends Grammar {

		RichStringGrammar() {
			super(RichStringGrammar::richString);
		}

		static Rule richString() {
			// TODO: issue #30: .*? instead of [ a-z]*
			return rule("richString", seq(str("\"\"\""), _0_n(charset(" a-z")), str("\"\"\"")));
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
		 * 
		 * INT : '0'..'9'+
		 * </code>
		 * </pre>
		 */
		static Rule expr() {
			return rule("expr",//
					seq(ref(ExpressionGrammar::expr), str("*"), ref(ExpressionGrammar::expr)),//
					seq(ref(ExpressionGrammar::expr), str("+"), ref(ExpressionGrammar::expr)),//
					ref(ExpressionGrammar::INT));
		}

		/**
		 * A parser for positive natural numbers including zero.
		 * 
		 * @return A natural number parser.
		 */
		static Rule INT() {
			return rule("INT", _1_n(range('0', '9')));
		}
	}

	static class IndirectExpressionGrammar extends Grammar {

		// define start rule
		IndirectExpressionGrammar() {
			super(IndirectExpressionGrammar::expr);
		}

		/**
		 * <pre>
		 * <code>
		 * expr : mul | add | INT
		 * 
		 * mul : expr '*' expr
		 * 
		 * add : expr '+' expr
		 * 
		 * INT : '0'..'9'+
		 * </code>
		 * </pre>
		 */
		static Rule expr() {
			return rule("expr", seq(//
					ref(IndirectExpressionGrammar::mul),//
					ref(IndirectExpressionGrammar::add),//
					ref(IndirectExpressionGrammar::INT)));
		}

		static Rule mul() {
			return rule("mul",
					seq(ref(IndirectExpressionGrammar::expr), str("*"), ref(IndirectExpressionGrammar::expr)));
		}

		static Rule add() {
			return rule("mul",
					seq(ref(IndirectExpressionGrammar::expr), str("+"), ref(IndirectExpressionGrammar::expr)));
		}

		/**
		 * A parser for positive natural numbers including zero.
		 * 
		 * @return A natural number parser.
		 */
		static Rule INT() {
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
			super(AheadOfTimeSnakeGrammar::eat);
		}

		static Rule eat() {
			return rule("eat", seq(ref(AheadOfTimeSnakeGrammar::eat), str("@")), EOF);
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
			super(HungrySnakeGrammar::eat);
		}

		static Rule eat() {
			return rule("eat", seq(str("@"), ref(HungrySnakeGrammar::eat)), EOF);
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
			super(PrudentSnakeGrammar::eat);
		}

		static Rule eat() {
			return rule("eat", EOF, seq(str("@"), ref(HungrySnakeGrammar::eat)));
		}
	}
}
