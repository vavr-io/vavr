/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.stream.Collectors.joining;
import static javaslang.Lang.require;
import static javaslang.Lang.requireNonNull;
import static javaslang.Lang.requireNotInstantiable;
import static javaslang.Lang.requireNotNullOrEmpty;

import java.util.function.Supplier;
import java.util.stream.Stream;

import javaslang.Stringz;
import javaslang.Tuplez;
import javaslang.Tuplez.Tuple2;
import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;
import javaslang.match.Match;
import javaslang.match.Matchz;
import javaslang.text.Tree;

class Parserz {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Parserz() {
		requireNotInstantiable();
	}

	/**
	 * Dot '.' parser. Matches a single, arbitrary character.
	 */
	static class Dot extends AbstractParser {

		static final Dot INSTANCE = new Dot();

		// hidden
		private Dot() {
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.parser.Parser#parse(java.lang.String, int)
		 */
		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index < text.length()) {
				return new Right<>(new Tree<>("Dot", Tuplez.of(index, 1)));
			} else {
				return new Left<>(index);
			}
		}

	}

	/**
	 * End-of-file (EOF) parser. Recognized the end of the input.
	 */
	static class EOF extends AbstractParser {

		static final EOF INSTANCE = new EOF();

		// hidden
		private EOF() {
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.parser.Parser#parse(java.lang.String, int)
		 */
		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index == text.length()) {
				return new Right<>(new Tree<>("EOF", Tuplez.of(index, 0)));
			} else {
				return new Left<>(index);
			}
		}

	}

	/**
	 * String literal parser:
	 * 
	 * <pre>
	 * <code>
	 * 'funky string'
	 * </code>
	 * </pre>
	 */
	static class Literal extends AbstractParser {

		final String literal;

		Literal(String literal) {
			this.literal = literal;
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.parser.Parser#parse(java.lang.String, int)
		 */
		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (text.startsWith(literal, index)) {
				return new Right<>(new Tree<>("Literal", Tuplez.of(index, literal.length())));
			} else {
				return new Left<>(index);
			}
		}

	}

	/**
	 * Grammar rule parser:
	 * 
	 * <pre>
	 * <code>
	 * ruleName
	 *   : subrule1
	 *   | subrule2
	 *   ;
	 * </code>
	 * </pre>
	 */
	static class Rule extends AbstractParser {

		final String name;
		final Supplier<Parser>[] parsers;

		@SafeVarargs
		Rule(String name, Supplier<Parser>... parsers) {
			requireNonNull(name, "name is null");
			requireNotNullOrEmpty(parsers, "No parsers");
			this.name = name;
			this.parsers = parsers;
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.parser.Parser#parse(java.lang.String, int)
		 */
		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			final Either<Integer, Tree<Tuple2<Integer, Integer>>> initial = new Left<>(index);
			return Stream
					.of(parsers)
					.parallel()
					.map(parser -> parser.get().parse(text, index))
					.reduce(initial, (t1, t2) -> reduce(t1, t2, text, index),
							(t1, t2) -> reduce(t1, t2, text, index));
		}

		/**
		 * Make decision on following one of two parse trees. Each tree may may be a valid match on
		 * the input text or not.
		 * 
		 * @param tree1 First parse tree.
		 * @param tree2 Second parse tree.
		 * @param text The input text.
		 * @param index The current index.
		 * @return One of the given parse trees, which is either no match (Left) or a match (Right).
		 */
		private Either<Integer, Tree<Tuple2<Integer, Integer>>> reduce(
				Either<Integer, Tree<Tuple2<Integer, Integer>>> tree1,
				Either<Integer, Tree<Tuple2<Integer, Integer>>> tree2, String text, int index) {
			// if both trees are valid parse results, i.e. Right, then we found an ambiguity
			require(tree1.isLeft() || tree2.isLeft(),
					() -> "Ambiguity found at " + Stringz.lineAndColumn(text, index) + ":\n" + text);
			if (tree1.isRight()) {
				return tree1; // first tree is a valid parse result
			} else if (tree2.isRight()) {
				return tree2; // second tree is a valid parse result
			} else if (tree1.left().get() >= tree2.left().get()) {
				return tree1; // both trees did not match, first tree consumed more characters
			} else {
				return tree2; // both trees did not match, second tree consumed more characters
			}
		}

	}

	/**
	 * Unity of common parser properties.
	 */
	private static abstract class AbstractParser implements Parser, Supplier<Parser> {

		static final Match<String> TO_STRING = Matchz//
				.caze((Dot dot) -> ".")
				.caze((EOF eof) -> "EOF")
				.caze((Literal l) -> "'" + l.literal + "'")
				.caze((Rule r) -> Stream
						.of(r.parsers)
						.map(p -> p.get().toString())
						.collect(joining("\n  | ", r.name + "\n  : ", "\n  ;")));

		/*
		 * (non-Javadoc)
		 * @see java.util.function.Supplier#get()
		 */
		@Override
		public final Parser get() {
			return this;
		}

		/**
		 * Calls {@link Stringifiable#stringify()}
		 */
		@Override
		public final String toString() {
			return TO_STRING.apply(this);
		}

	}

}
