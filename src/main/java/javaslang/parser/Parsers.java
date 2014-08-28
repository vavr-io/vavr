/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.stream.Collectors.joining;
import static javaslang.Requirements.require;
import static javaslang.Requirements.requireNonNull;
import static javaslang.Requirements.requireNotNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Requirements.UnsatisfiedRequirementException;
import javaslang.Strings;
import javaslang.collection.Node;
import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;

//
// TODO:
// - Greedy & non-greedy quantors (?,+,*,??,+?,*?) // Naming: Quantor vs Multiplicity
//
// - Empty subrules
//   rule : ( "a" | | "c" ) # empty sub-rule alternative
//        |                 # empty alternative
//        ;
//
// - Charset [a-zA-Z]+ is a shorthand for Range ('a'..'z'|'A'..'Z')+ 
//   a-z = 'a'..'z' 
//   [a-zA-Z]+      Multiplicity(1..n)
//                         | 
//                      Choice 
//                       /   \ 
//                   Range   Range 
//                    / \     / \ 
//                   a   z   A   Z 
//
// - Whitespace handling
//   * Automatic whitespace handling within parser rules (use reserved word WHITESPACE instead of WS)
//   * Switch to non-automatic whitespace handling within lexer rules
//   * Leads to no distinction between lexer and parser phase.
//     Just one phase with context switch.
//     No switch for Literals in the context of parsers.
//     Context switch only within referenced lexer rules.
//
//   Lexer rules 
//   ----------- 
//   Charset : '[' (Char | Char '-' Char)+ ']'; // no auto-whitespace between lexer parts! 
//   Char : // Unicode character 
//
//   Parser rules 
//   ------------ 
//   example : Charset*; // whitespace between parser tokens allowed!
//
public final class Parsers {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Parsers() {
		throw new AssertionError(Parsers.class.getName() + " is not intended to be instantiated.");
	}

	/**
	 * Wildcard '.' parser. Matches a single, arbitrary character.
	 */
	static class Any implements Parser {

		static final Any INSTANCE = new Any();

		// hidden
		private Any() {
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			if (index < text.length()) {
				return new Right<>(new Node<>(new Token("Any", index, 1)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return ".";
		}
	}

	/**
	 * Character range parser:
	 *
	 * <pre>
	 * <code>
	 * 'a'..'z'
	 * </code>
	 * </pre>
	 */
	static class CharRange implements Parser {

		final char from;
		final char to;
		final Predicate<Character> isInRange;

		/**
		 * Constructs a character range.
		 * 
		 * @param from First character this range includes.
		 * @param to Last character this range includes.
		 * @throws UnsatisfiedRequirementException in case of a negative range, i.e. from &gt; to.
		 */
		CharRange(char from, char to) {
			require(from <= to, "from > to");
			this.from = from;
			this.to = to;
			this.isInRange = c -> from <= c && c <= to;
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			if (index < text.length() && isInRange.test(text.charAt(index))) {
				return new Right<>(new Node<>(new Token("CharRange", index, 1)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return String.format("'%s'..'%s'", from, to);
		}
	}

	/**
	 * Character set parser:
	 *
	 * <pre>
	 * <code>
	 * [a-zA-Z$_]
	 * </code>
	 * </pre>
	 */
	static class CharSet implements Parser {

		/** Matches ranges within char sets. */
		static final Pattern CHAR_SET_RANGE_PATTERN = Pattern.compile(".-.");

		final String charSetString;
		final Predicate<Character> isInSet;

		/**
		 * Constructs a character range.
		 * 
		 * @param set A set of characters to include.
		 */
		CharSet(String charSetString) {
			requireNotNullOrEmpty(charSetString, "charSetString is null or empty");
			this.charSetString = charSetString;
			this.isInSet = parse(charSetString);
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			if (index < text.length() && isInSet.test(text.charAt(index))) {
				return new Right<>(new Node<>(new Token("CharSet", index, 1)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return charSetString.chars().boxed().map(i -> {
				final char c = (char) i.intValue();
				// TODO: add more special characters. See http://stackoverflow.com/questions/504402/how-to-handle-escape-sequences-in-string-literals-in-antlr-3a
					switch (c) {
						case 0x09:
							return "\\t";
						case 0x0A:
							return "\\n";
						case 0x0D:
							return "\\r";
						case 0x0C:
							return "\\f";
						case 0x5C:
							return "\\\\";
						default:
							// TODO: unicode chars
							return String.valueOf(c);
					}
				}).collect(joining("", "[", "]"));
		}

		/**
		 * Parses a char set String which contains sequences of characters and character ranges denoted as {@code a-z}.
		 * 
		 * @param charSetString A String defining a char set.
		 * @return A Predicate that tests, if a given char is in the char set.
		 */
		private Predicate<Character> parse(String charSetString) {

			final List<Predicate<Character>> predicates = new ArrayList<>();
			final Matcher matcher = CHAR_SET_RANGE_PATTERN.matcher(charSetString);
			final StringBuffer charsBuf = new StringBuffer();

			while (matcher.find()) {

				// save single characters to buffer
				matcher.appendReplacement(charsBuf, "");

				// create Predicate from range
				final String range = matcher.group(0);
				final char from = range.charAt(0);
				final char to = range.charAt(2);
				predicates.add(c -> from <= c && c <= to);

			}

			// collect remaining characters to buffer and create Predicate
			matcher.appendTail(charsBuf);
			final String chars = charsBuf.toString();
			predicates.add(c -> chars.indexOf(c) != -1);

			// compose predicates
			return predicates.stream().reduce(Predicate::or).orElse(c -> false);
		}
	}

	/**
	 * End-of-file (EOF) parser. Recognized the end of the input.
	 */
	static class EOF implements Parser {

		static final EOF INSTANCE = new EOF();

		// hidden
		private EOF() {
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			if (index == text.length()) {
				return new Right<>(new Node<>(new Token("EOF", index, 0)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return "EOF";
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
	static class Literal implements Parser {

		final String literal;

		Literal(String literal) {
			requireNotNullOrEmpty(literal, "literal is null or empty");
			// TODO: escape literal? e.g. '\n' -> '\\n'
			this.literal = literal;
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			if (text.startsWith(literal, index)) {
				return new Right<>(new Node<>(new Token("Literal", index, literal.length())));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return "'" + Strings.escape(literal, '\'', '\\') + "'";
		}
	}

	// TODO: javadoc
	static class Quantifier implements Parser {

		final Supplier<Parser> parser;
		final Bounds bounds;

		Quantifier(Supplier<Parser> parser, Bounds bounds) {
			requireNonNull(parser, "parser is null");
			requireNonNull(bounds, "bounds is null");
			this.parser = parser;
			this.bounds = bounds;
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			final Node<Token> result = new Node<>(new Token(bounds.name(), index, 0));
			parseChildren(result, text, index, lexer);
			final boolean notMatched = result.getChildren().isEmpty();
			final boolean shouldHaveMatched = Bounds.ONE_TO_N.equals(bounds);
			if (notMatched && shouldHaveMatched) {
				return new Left<>(index);
			} else {
				return new Right<>(result);
			}
		}

		@Override
		public String toString() {
			final String parserString;
			if (parser instanceof Rule) {
				parserString = ((Rule) parser).name;
			} else if (parser instanceof Sequence) {
				parserString = "(" + parser.toString() + ")";
			} else {
				parserString = parser.toString();
			}
			return parserString + bounds.symbol;
		}

		// TODO: rewrite this method (immutable & recursive)
		private void parseChildren(Node<Token> tree, String text, int index, boolean lexer) {
			final boolean unbound = !Bounds.ZERO_TO_ONE.equals(bounds);
			boolean found = true;
			final Token token = tree.getValue();
			do {
				final Either<Integer, Node<Token>> child = parser.get().parse(text, token.index + token.length, lexer);
				if (child.isRight()) {
					final Node<Token> node = child.right().get();
					tree.attach(node);
					token.length = node.getValue().length;
				} else {
					found = false;
				}
			} while (unbound && found);
		}

		static enum Bounds {

			// greedy
			ZERO_TO_ONE("?", true), ZERO_TO_N("*", true), ONE_TO_N("+", true),

			// non-greedy
			ZERO_TO_ONE_NG("??", false), ZERO_TO_N_NG("*?", false), ONE_TO_N_NG("+?", false);

			final String symbol;
			final boolean greedy;

			Bounds(String symbol, boolean greedy) {
				this.symbol = symbol;
				this.greedy = greedy;
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
	static class Rule implements Parser {

		final String name;
		final Supplier<Parser>[] alternatives;
		final boolean lexerRule;

		/**
		 * Creates a primary rule, i.e. a rule with a name which may be referenced by other rules.
		 * <p>
		 * Rules starting with an upper case are lexer rules, rules starting with a lower case are parser rules. Lexer
		 * rules are not allowed to reference parser rules.
		 * 
		 * @param alternatives One or more alternative rules.
		 * @throws UnsatisfiedRequirementException if name is invalid, i.e. null, a reserved word or not a valid
		 *             identifier) or one of the alternatives is null.
		 */
		@SafeVarargs
		Rule(String name, Supplier<Parser>... alternatives) {
			requireNotNullOrEmpty(name, "name is null or empty");
			requireNotNullOrEmpty(alternatives, "alternatives is null or empty");
			this.name = name;
			this.alternatives = alternatives;
			this.lexerRule = Character.isUpperCase(name.charAt(0));
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			require(!lexer || lexerRule, "parser rule '" + name + "' is referenced by a lexer rule");
			for (Supplier<Parser> alternative : alternatives) {
				final Either<Integer, Node<Token>> result = alternative.get().parse(text, index, lexerRule);
				if (result.isRight()) {
					final Node<Token> child = result.right().get();
					final Token token = child.getValue();
					return new Right<>(new Node<>(new Token("Rule", index, token.length)).attach(child));
				}
			}
			return new Left<>(index);
		}

		@Override
		public String toString() {
			final String indent = Strings.repeat(' ', name.length());
			return Stream.of(alternatives).map(supplier -> {
				final Parser parser = supplier.get();
				if (parser instanceof Rule) {
					return ((Rule) parser).name;
				} else {
					return parser.get().toString();
				}
			}).collect(Collectors.joining("\n" + indent + " | ", name + " : ", "\n" + indent + " ;"));
		}
	}

	// TODO: javadoc
	static class SubRule implements Parser {

		final Supplier<Parser>[] alternatives;

		@SafeVarargs
		SubRule(Supplier<Parser>... alternatives) {
			requireNonNull(alternatives, "alternatives is null");
			require(alternatives.length >= 2, "number of alternatives < 2");
			this.alternatives = alternatives;
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			for (Supplier<Parser> alternative : alternatives) {
				final Either<Integer, Node<Token>> result = alternative.get().parse(text, index, lexer);
				if (result.isRight()) {
					return result;
				}
			}
			return new Left<>(index);
		}

		@Override
		public String toString() {
			return Stream.of(alternatives).map(supplier -> {
				final Parser parser = supplier.get();
				if (parser instanceof Rule) {
					return ((Rule) parser).name;
				} else {
					return parser.toString();
				}
			}).collect(Collectors.joining(" | ", "( ", " )"));
		}
	}

	// TODO: javadoc
	static class Sequence implements Parser {

		final Supplier<Parser>[] parsers;

		@SafeVarargs
		public Sequence(Supplier<Parser>... parsers) {
			requireNonNull(parsers, "no parsers");
			require(parsers.length >= 2, "number of parsers < 2");
			this.parsers = parsers;
		}

		@Override
		public Either<Integer, Node<Token>> parse(String text, int index, boolean lexer) {
			// Starts with an emty root tree and successively attaches parsed children.
			final List<Node<Token>> children = new ArrayList<>();
			final Either<Integer, Integer> initialIndex = new Right<>(index);
			final Either<Integer, Integer> result = Stream.of(parsers).reduce(initialIndex, (currentIndex, parser) -> {
				return currentIndex.right().flatMap(parseAtIndex -> {
					final Either<Integer, Node<Token>> parseResult = parser.get().parse(text, parseAtIndex, lexer);
					return parseResult.right().map(node -> {
						final Token token = node.getValue();
						return token.index + token.length;
					});
				});
			}, (index1, index2) -> null);
			return result.right().map(
					lastIndex -> new Node<>(new Token("Sequence", index, lastIndex - index)).attach(children));
		}

		@Override
		public String toString() {
			return Stream.of(parsers).map(supplier -> {
				final Parser parser = supplier.get();
				if (parser instanceof Rule) {
					return ((Rule) parser).name;
				} else {
					return parser.toString();
				}
			}).collect(Collectors.joining(" "));
		}
	}
}
