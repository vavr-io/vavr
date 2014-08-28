/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import java.util.function.Supplier;

import javaslang.Strings;
import javaslang.collection.Node;
import javaslang.collection.Tree;
import javaslang.either.Either;
import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;

interface GrammarInterface {

	static final Parsers.Any ANY = Parsers.Any.INSTANCE;
	static final Parsers.EOF EOF = Parsers.EOF.INSTANCE;

	/**
	 * Start rule of this grammar.
	 * 
	 * @return The start rule.
	 */
	Parsers.Rule getStartRule();

	/**
	 * TODO: javadoc
	 * 
	 * @param text A text input to be parsed.
	 * @return A concrete syntax tree of the text on parse success or a failure if a parse error occured.
	 */
	default Try<Tree<Token>> parse(String text) {
		final Either<Integer, Node<Token>> parseResult = getStartRule().parse(text, 0, false);
		if (parseResult.isRight()) {
			final Tree<Token> concreteSyntaxTree = parseResult.right().get().asTree();
			return new Success<>(concreteSyntaxTree);
		} else {
			final int index = parseResult.left().get();
			return new Failure<>(new IllegalArgumentException("cannot parse input at "
					+ Strings.lineAndColumn(text, index)));
		}
	}

	@Override
	String toString();

	// -- atomic shortcuts used in grammar definitions

	/**
	 * Shortcut for {@code new Parsers.Rule(name, alternatives)}.
	 * 
	 * @param name Rule name.
	 * @param alternatives Rule alternatives.
	 * @return A new {@link Parsers.Rule}.
	 */
	@SafeVarargs
	static Parsers.Rule rule(String name, Supplier<Parser>... alternatives) {
		return new Parsers.Rule(name, alternatives);
	}

	/**
	 * Shortcut for {@code new Parsers.SubRule(alternatives)}.
	 * 
	 * @param alternatives SubRule alternatives.
	 * @return A new {@link Parsers.SubRule}.
	 */
	@SafeVarargs
	static Parsers.SubRule subRule(Supplier<Parser>... alternatives) {
		return new Parsers.SubRule(alternatives);
	}

	/**
	 * Shortcut for {@code new Parsers.Sequence(parsers))}.
	 * 
	 * @param parsers Sequenced parsers.
	 * @return A new {@link Parsers.Sequence}.
	 */
	@SafeVarargs
	static Parsers.Sequence seq(Supplier<Parser>... parsers) {
		return new Parsers.Sequence(parsers);
	}

	/**
	 * Shortcut for {@code new Parsers.CharSet(charset)}.
	 * 
	 * @param charset A charset String.
	 * @return A new {@link Parsers.CharSet}.
	 */
	static Parsers.CharSet charSet(String charset) {
		return new Parsers.CharSet(charset);
	}

	/**
	 * Shortcut for {@code new Parsers.CharRange(from, to)}.
	 * 
	 * @param from Left bound of the range, inclusive.
	 * @param to Right bound of the range, inclusive.
	 * @return A new {@link Parsers.CharRange}.
	 */
	static Parsers.CharRange range(char from, char to) {
		return new Parsers.CharRange(from, to);
	}

	/**
	 * Shortcut for {@code new Parsers.Literal(s)}.
	 * 
	 * @param s A string.
	 * @return A new {@link Parsers.Literal}.
	 */
	static Parsers.Literal str(String s) {
		return new Parsers.Literal(s);
	}

	/**
	 * Shortcut for {@code new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ZERO_TO_ONE)}
	 * .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parsers.Quantifier}.
	 */
	@SafeVarargs
	static Parsers.Quantifier _0_1(Supplier<Parser>... parsers) {
		return new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ZERO_TO_ONE);
	}

	/**
	 * Shortcut for {@code new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ZERO_TO_N)}.
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parsers.Quantifier}.
	 */
	@SafeVarargs
	static Parsers.Quantifier _0_n(Supplier<Parser>... parsers) {
		return new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ZERO_TO_N);
	}

	/**
	 * Shortcut for {@code new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ONE_TO_N)}.
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parsers.Quantifier}.
	 */
	@SafeVarargs
	static Parsers.Quantifier _1_n(Supplier<Parser>... parsers) {
		return new Parsers.Quantifier(new Parsers.Sequence(parsers), Parsers.Quantifier.Bounds.ONE_TO_N);
	}

	// -- composite shortcuts used in grammar definitions

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
	static Parser list(Supplier<Parser> parser, String separator) {
		return _0_1(parser, _0_n(str(separator), parser));
	}
}
