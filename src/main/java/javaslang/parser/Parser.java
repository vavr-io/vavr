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
import java.util.Arrays;
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
import javaslang.monad.Either;
import javaslang.monad.Left;
import javaslang.monad.Right;

/**
 * The parser interface.
 */
//
//TODO:
//- Greedy & non-greedy quantors (?,+,*,??,+?,*?) // Naming: Quantor vs Multiplicity
//
//- Empty subrules
//rule : ( "a" | | "c" ) # empty sub-rule alternative
//     |                 # empty alternative
//     ;
//
//- Charset [a-zA-Z]+ is a shorthand for Range ('a'..'z'|'A'..'Z')+ 
//a-z = 'a'..'z' 
//[a-zA-Z]+      Multiplicity(1..n)
//                      | 
//                   Choice 
//                    /   \ 
//                Range   Range 
//                 / \     / \ 
//                a   z   A   Z 
//
//- Whitespace handling
//* Automatic whitespace handling within parser rules (use reserved word WHITESPACE instead of WS)
//* Switch to non-automatic whitespace handling within lexer rules
//* Leads to no distinction between lexer and parser phase.
//  Just one phase with context switch.
//  No switch for Literals in the context of parsers.
//  Context switch only within referenced lexer rules.
//
//Lexer rules 
//----------- 
//Charset : '[' (Char | Char '-' Char)+ ']'; // no auto-whitespace between lexer parts! 
//Char : // Unicode character 
//
//Parser rules 
//------------ 
//example : Charset*; // whitespace between parser tokens allowed!
//
interface Parser extends Supplier<Parser> {

	/**
	 * Trying to parse a text using the current parser, starting at the given index.
	 * 
	 * @param text The whole text to parse.
	 * @param index The current index of the parser.
	 * @param lex Indicates, if the scope is lexing (true) or parsing (false). When in lexing mode, cascaded rules are
	 *            combined to one and whitespace is not ignored. When in parsing mode, parse-results of cascaded rules
	 *            are added as children the the actual tree node and whitespace may be ignored.
	 * @return Either a Left, containing the index of failure or a Right, containing the range (index, length) parsed.
	 */
	// TODO: lex vs. lexer/parser rules vs. fragments
	Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex);

	/**
	 * Being a self-supplier is the key for constructing grammars programatically using methods, which are evaluated
	 * lazily. This allows to build grammars containing cyclic references.
	 * <p>
	 * Parser implementations which have child parsers, should provide a constructor having Supplier&lt;Parser&gt; as
	 * argument.
	 * 
	 * @return This Parser instance.
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	default Parser get() {
		return this;
	}

	/**
	 * Returns a String representation in grammar notation of this parser.
	 * 
	 * @return A String in grammar notation.
	 */
	@Override
	String toString();

	// -- parsers

	/**
	 * Wildcard '.' parser. Matches a single, arbitrary character.
	 */
	static class Any implements Parser {

		static final Any INSTANCE = new Any();

		// hidden
		private Any() {
		}

		@Override
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			final boolean match = index < text.length();
			return match ? token(index, 1) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return ".";
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
	static class Charset implements Parser {

		/** Matches ranges within char sets. */
		static final Pattern CHAR_SET_RANGE_PATTERN = Pattern.compile(".-.");

		final String charsetString;
		final Predicate<Character> inSet;

		/**
		 * Constructs a character range.
		 * 
		 * @param set A set of characters to include.
		 */
		Charset(String charsetString) {
			requireNotNullOrEmpty(charsetString, "charsetString is null or empty");
			this.charsetString = charsetString;
			this.inSet = parse(charsetString);
		}

		@Override
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			final boolean match = index < text.length() && inSet.test(text.charAt(index));
			return match ? token(index, 1) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return charsetString.chars().boxed().map(i -> {
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
		 * @param charsetString A String defining a char set.
		 * @return A Predicate that tests, if a given char is in the char set.
		 */
		private Predicate<Character> parse(String charsetString) {

			final java.util.List<Predicate<Character>> predicates = new ArrayList<>();
			final Matcher matcher = CHAR_SET_RANGE_PATTERN.matcher(charsetString);
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

		static final String EOF = "EOF";
		static final EOF INSTANCE = new EOF();

		// hidden
		private EOF() {
		}

		@Override
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			final boolean match = (index == text.length());
			return match ? token(EOF, index, 0) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return EOF;
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
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			final boolean match = text.startsWith(literal, index);
			return match ? token(index, literal.length()) : stoppedAt(index);
		}

		@Override
		public String toString() {
			// TODO: is this escaping sufficient?
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
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {

			if (lex) {
				// combine results
				return combine(parser.get(), text, index).left().flatMap(endIndex -> {
					return (index == endIndex && bounds.required) ? stoppedAt(index) : token(index, endIndex - index);
				});
			} else {
				// aggregate results
				return aggregate(parser.get(), text, index);
			}
		}

		private Either<Integer, List<Node<Token>>> combine(Parser parser, String text, int index) {
			final Either<Integer, List<Node<Token>>> result = parser.parse(text, index, true);
			return result.flatMap(list -> {
				final Node<Token> lastNode = list.get(list.size() - 1);
				final Token token = lastNode.getValue();
				if (token.length > 0) { // DEV-NODE: prevent loops, e.g. empty literal or EOF parsers
						return combine(parser, text, token.endIndex());
					} else {
						return stoppedAt(token.endIndex());
					}
				});
		}

		private Either<Integer, List<Node<Token>>> aggregate(Parser parser, String text, int index) {
			return null; // TODO
		}

		@Override
		public String toString() {
			final String parserString;
			if (parser instanceof Rule) {
				parserString = ((Rule) parser).name;
			} else if (parser instanceof Sequence) {
				parserString = "( " + parser.toString() + " )";
			} else {
				parserString = parser.toString();
			}
			return parserString + bounds.symbol;
		}

		static enum Bounds {

			// greedy
			ZERO_TO_ONE("?", false, false), ZERO_TO_N("*", false, true), ONE_TO_N("+", true, true);

			final String symbol;
			final boolean required;
			final boolean unbound;

			Bounds(String symbol, boolean required, boolean unbound) {
				this.symbol = symbol;
				this.required = required;
				this.unbound = unbound;
			}
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
	static class Range implements Parser {

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
		Range(char from, char to) {
			require(from <= to, "from > to");
			this.from = from;
			this.to = to;
			this.isInRange = c -> from <= c && c <= to;
		}

		@Override
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			final boolean match = index < text.length() && isInRange.test(text.charAt(index));
			return match ? token(index, 1) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return String.format("'%s'..'%s'", from, to);
		}
	}

	/**
	 * Grammar rule parser:
	 * 
	 * <pre>
	 * <code>
	 * ruleName : subrule_1
	 *          | ...
	 *          | subrule_n
	 *          ;
	 * </code>
	 * </pre>
	 */
	static class Rule implements Parser {

		final String name;
		final Supplier<Parser>[] alternatives;
		final boolean lexerRule;

		/**
		 * Creates a primary rule, i.e. a rule with a unique name which may be referenced by other rules.
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
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			// TODO: does this check has to be made at parse-time?
			require(!lex || lexerRule, "parser rule '" + name + "' is referenced by a lexer rule");
			for (Supplier<Parser> alternative : alternatives) {
				final Either<Integer, List<Node<Token>>> result = alternative.get().parse(text, index, lexerRule);
				if (result.isRight()) {
					return lex ? result : token(name, index, length(result), result.get());
				}
			}
			return stoppedAt(index);
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (o == null || !(o instanceof Rule)) {
				return false;
			} else {
				final String thatName = ((Rule) o).name;
				return name.equals(thatName);
			}
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		@Override
		public String toString() {
			final String indent = Strings.repeat(' ', name.length());
			final String delimiter = "\n" + indent + " | ";
			final String prefix = name + " : ";
			final String suffix = (alternatives.length < 2) ? " ;" : "\n" + indent + " ;";
			return Stream.of(alternatives).map(supplier -> {
				final Parser parser = supplier.get();
				return (parser instanceof Rule) ? ((Rule) parser).name : parser.get().toString();
			}).collect(joining(delimiter, prefix, suffix));
		}
	}

	// TODO: javadoc
	static class Sequence implements Parser {

		// TODO: issue #25: custom whitespace handling
		static final Parser DEFAULT_WS = new Rule("WS", new Quantifier(new Charset(" \t\r\n"),
				Quantifier.Bounds.ZERO_TO_N));

		final Supplier<Parser>[] parsers;

		@SafeVarargs
		public Sequence(Supplier<Parser>... parsers) {
			requireNonNull(parsers, "no parsers");
			require(parsers.length >= 2, "number of parsers < 2");
			this.parsers = parsers;
		}

		@Override
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			return lex ? combine(text, index) : aggregate(text, index);
		}

		private Either<Integer, List<Node<Token>>> combine(String text, int index) {
			return Stream.of(parsers).reduce(token(index, 0), (current, parser) -> current.flatMap(t -> {
				final int currentIndex = t.get(0).getValue().endIndex();
				return parser.get().parse(text, currentIndex, true);
			}), (t1, t2) -> null);
		}

		private Either<Integer, List<Node<Token>>> aggregate(String text, int index) {
			final List<Node<Token>> result = new ArrayList<>();
			int currentIndex = index;
			for (Supplier<Parser> parser : parsers) {
				final Either<Integer, List<Node<Token>>> parsed = parser.get().parse(text,
						skipWhitespace(text, currentIndex), false);
				if (parsed.isRight()) {
					result.addAll(parsed.get());
					currentIndex = result.get(result.size() - 1).getValue().endIndex();
				} else {
					return parsed; // Left / stoppedAt
				}
			}
			return new Right<>(result);

		}

		private int skipWhitespace(String text, int index) {
			return DEFAULT_WS.parse(text, index, true).map(t -> t.get(0).getValue().endIndex()).orElse(index);
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
		public Either<Integer, List<Node<Token>>> parse(String text, int index, boolean lex) {
			for (Supplier<Parser> alternative : alternatives) {
				final Either<Integer, List<Node<Token>>> result = alternative.get().parse(text, index, lex);
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

	// -- parse-result factory methods

	// terminal token
	static Either<Integer, List<Node<Token>>> token(int index, int length) {
		return token(null, index, length);
	}

	// rule
	static Either<Integer, List<Node<Token>>> token(String id, int index, int length) {
		return new Right<>(Arrays.asList(new Node<>(new Token(id, index, length))));
	}

	// rule with children
	static Either<Integer, List<Node<Token>>> token(String id, int index, int length, List<Node<Token>> children) {
		return new Right<>(Arrays.asList(new Node<>(new Token(id, index, length), children)));
	}

	// no match found
	static Either<Integer, List<Node<Token>>> stoppedAt(int index) {
		return new Left<>(index);
	}

	// -- parse-result helpers

	static int endIndex(Either<Integer, List<Node<Token>>> parsed) {
		return parsed.map(list -> list.isEmpty() ? -1 : list.get(list.size() - 1).getValue().endIndex()).orElse(-1);
	}

	static int length(Either<Integer, List<Node<Token>>> parsed) {
		return parsed.map(list -> {
			if (list.isEmpty()) {
				return 0;
			} else {
				final int startIndex = list.get(0).getValue().index;
				final int endIndex = list.get(list.size() - 1).getValue().endIndex();
				return endIndex - startIndex;
			}
		}).orElse(0);
	}
}
