/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Requirements.requireNonNull;
import static javaslang.parser.Parsers.Quantifier.Bounds.ONE_TO_N;
import static javaslang.parser.Parsers.Quantifier.Bounds.ZERO_TO_N;
import static javaslang.parser.Parsers.Quantifier.Bounds.ZERO_TO_ONE;

import java.util.function.Supplier;

import javaslang.Strings;
import javaslang.Tuples.Tuple2;
import javaslang.either.Either;
import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;
import javaslang.parser.Parsers.Literal;
import javaslang.parser.Parsers.Quantifier;
import javaslang.parser.Parsers.Rule;
import javaslang.parser.Parsers.Sequence;

// TODO: Distinguish between tokenizing (Lexer) and parsing (Parser)
//       - https://github.com/antlr/grammars-v4/blob/master/antlr4/ANTLRv4Lexer.g4
//       - https://github.com/antlr/grammars-v4/blob/master/antlr4/ANTLRv4Parser.g4
// TODO: Make regular expressions first class members of grammar definitions, e.g. fragment DIGITS: '1'..'9' '0'..'9'*;
//		- '|' <space> * ? + .. \n \r \t etc. (escape reserved symbols, e.g. quote ')
// TODO: Make grammars compatible to Antlr4 grammars (i.e. parse (and stringify) them - https://github.com/antlr/grammars-v4/blob/master/java8/Java8.g4
// TODO: Add fragments - http://stackoverflow.com/questions/6487593/what-does-fragment-means-in-antlr
// TODO: CST to AST transformation (as external DSL within the grammar)
// TODO: add Regex Parser: "regex" (literal has single quotes 'lll')
// TODO: unescape literals
// TODO: remove Branch, Sequence and Multiplicity nodes if they have no name/id
/**
 * <pre>
 * <code>class JSONGrammar extends Grammar {
 * 
 *     // define start rule
 *     JSONGrammar() {
 *         super(JSONGrammar::json);
 *     }
 *     
 *     // json : jsonObject | jsonArray | jsonString | jsonNumber | 'true' | 'false' | 'null' ;
 *     static Rule json() {
 *         return rule("json",
 *                 JSONGrammar::jsonObject,
 *                 JSONGrammar::jsonArray,
 *                 JSONGrammar::jsonString,
 *                 JSONGrammar::jsonNumber,
 *                 str("true"),
 *                 str("false"),
 *                 str("null"));
 *     }
 *     
 *     // jsonObject : '{' ( pair ( ',' pair )* )? '}' ;
 *     static Parser jsonObject() {
 *         return rule("jsonObject", seq(str("{"), list(JSONGrammar::pair, ","), str("}"));
 *     }
 *     
 *     // pair : jsonString ':' json ;
 *     static Parser pair() {
 *         return seq(JSONGrammar::jsonString, str(":"), JSONGrammar::json);
 *     }
 *     
 *     // etc.
 *     
 * }</code>
 * </pre>
 */
// DEV NOTE: Grammar is meant to be extended, i.e. it is not final.
public class Grammar {

	private final Supplier<Rule> startRule;

	public Grammar(Supplier<Rule> startRule) {
		requireNonNull(startRule, "startRule is null");
		this.startRule = startRule;
	}

	public Try<Tree<Tuple2<Integer, Integer>>> parse(String text) {
		final Either<Integer, Tree<Tuple2<Integer, Integer>>> parseResult = startRule.get().parse(
				text, 0);
		if (parseResult.isRight()) {
			final Tree<Tuple2<Integer, Integer>> concreteSyntaxTree = parseResult.right().get();
			return new Success<>(concreteSyntaxTree);
		} else {
			final int index = parseResult.left().get();
			final Exception x = new IllegalArgumentException("cannot parse input at "
					+ Strings.lineAndColumn(text, index));
			return new Failure<>(x);
		}
	}

	// -- shortcuts used in grammar definitions

	/**
	 * A separated list, equivalent to {@code ( P ( ',' P )* )?}.
	 * <p>
	 * {@code list(parser, separator)}
	 * <p>
	 * is a shortcut for
	 * <p>
	 * {@code _0_1(parser, _0_N(str(separator), parser))}.
	 * <p>
	 * which expands to
	 * <p>
	 * {@code new Quantifier(new Sequence(parser, new Quantifier(new Sequence(new Literal(separator), parser), ZERO_TO_N)), ZERO_TO_ONE)}.
	 * 
	 * @param parser A Parser.
	 * @param separator A separator.
	 * @return A Parser which recognizes {@code ( P ( ',' P )* )?}.
	 */
	public static Parser list(Supplier<Parser> parser, String separator) {
		return _0_1(parser, _0_n(str(separator), parser));
	}

	/**
	 * A string literal, {@code '<string>'}.
	 * 
	 * @param s A string.
	 * @return {@code new Literal(s)}.
	 */
	public static Parser str(String s) {
		return new Literal(s);
	}

	@SafeVarargs
	public static Parser _0_1(Supplier<Parser>... parsers) {
		return new Quantifier(new Sequence(parsers), ZERO_TO_ONE);
	}

	@SafeVarargs
	public static Parser _0_n(Supplier<Parser>... parsers) {
		return new Quantifier(new Sequence(parsers), ZERO_TO_N);
	}

	@SafeVarargs
	public static Parser _1_n(Supplier<Parser>... parsers) {
		return new Quantifier(new Sequence(parsers), ONE_TO_N);
	}

}
