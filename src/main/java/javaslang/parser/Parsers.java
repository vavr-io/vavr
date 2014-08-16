/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.stream.Collectors.joining;
import static javaslang.Requirements.require;
import static javaslang.Requirements.requireNonNull;
import static javaslang.Requirements.requireNotInstantiable;
import static javaslang.Requirements.requireNotNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javaslang.Arrayz;
import javaslang.Requirements.UnsatisfiedRequirementException;
import javaslang.Strings;
import javaslang.Tuples;
import javaslang.Tuples.Tuple2;
import javaslang.collection.Sets;
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
		requireNotInstantiable();
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
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index < text.length()) {
				return new Right<>(new Tree<>("Any", Tuples.of(index, 1)));
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
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index < text.length() && isInRange.test(text.charAt(index))) {
				return new Right<>(new Tree<>("CharRange", Tuples.of(index, 1)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return from + ".." + to;
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
			this.charSetString = charSetString;
			this.isInSet = parse(charSetString);
		}

		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index < text.length() && isInSet.test(text.charAt(index))) {
				return new Right<>(new Tree<>("CharSet", Tuples.of(index, 1)));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return charSetString.chars().boxed().map(i -> {
				final char c = (char) i.intValue();
				// TODO: add more special characters?
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
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (index == text.length()) {
				return new Right<>(new Tree<>("EOF", Tuples.of(index, 0)));
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
			// TODO: escape literal? e.g. '\n' -> '\\n'
			this.literal = literal;
		}

		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			if (text.startsWith(literal, index)) {
				return new Right<>(new Tree<>("Literal", Tuples.of(index, literal.length())));
			} else {
				return new Left<>(index);
			}
		}

		@Override
		public String toString() {
			return "'" + literal + "'";
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
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			final Tree<Tuple2<Integer, Integer>> result = new Tree<>(bounds.name(), Tuples.of(index, 0));
			parseChildren(result, text, index);
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
			if (parser instanceof Rule && !((Rule) parser).isSubRule()) {
				return ((Rule) parser).name + bounds.symbol;
			} else if (parser instanceof Sequence) {
				return "(" + parser.toString() + ")" + bounds.symbol;
			} else {
				return parser.toString() + bounds.symbol;
			}
		}

		// TODO: rewrite this method (immutable & recursive)
		private void parseChildren(Tree<Tuple2<Integer, Integer>> tree, String text, int index) {
			final boolean unbound = !Bounds.ZERO_TO_ONE.equals(bounds);
			boolean found = true;
			do {
				final Either<Integer, Tree<Tuple2<Integer, Integer>>> child = parser.get().parse(text,
						tree.value._1 + tree.value._2);
				if (child.isRight()) {
					final Tree<Tuple2<Integer, Integer>> node = child.right().get();
					tree.attach(node);
					tree.value = Tuples.of(tree.value._1, node.value._2);
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

		static final Set<String> RESERVED_WORDS = Sets.of("EOF");

		final Optional<String> name;
		final Supplier<Parser>[] alternatives;

		/**
		 * Creates a subrule with multiple alternatives which is embedded in a primary rule. In particular a subrule has
		 * no name and therefore may not be referenced by other (primary) rules.
		 * 
		 * @param alternative1 First alternative rule.
		 * @param alternative2 Second alternative rule.
		 * @param alternatives Zero or more alternative rules.
		 * @throws UnsatisfiedRequirementException if one of the alternatives is null.
		 */
		@SafeVarargs
		Rule(Supplier<Parser> alternative1, Supplier<Parser> alternative2, Supplier<Parser>... alternatives) {
			requireNonNull(alternative1, "alternative1 is null");
			requireNonNull(alternative2, "alternative2 is null");
			requireNonNull(alternatives, "alternatives is null");
			this.name = Optional.empty();
			this.alternatives = Arrayz.combine(Arrayz.of(alternative1, alternative2), alternatives);
		}

		/**
		 * Creates a primary rule, i.e. a rule with a name which may be referenced by other rules.
		 * 
		 * @param alternative1 First alternative rule.
		 * @param alternatives Zero or more alternative rules.
		 * @throws UnsatisfiedRequirementException if name is invalid, i.e. null, a reserved word or not a valid
		 *             identifier) or one of the alternatives is null.
		 */
		@SafeVarargs
		Rule(String name, Supplier<Parser> alternative1, Supplier<Parser>... alternatives) {
			requireNonNull(name, "name is null");
			// TODO: require(RULE_IDENTIFIER.test(name), "name " + name +
			// " is not a valid rule identifier (<pattern>");
			require(!RESERVED_WORDS.contains(name), "name " + name + " is reserved");
			requireNonNull(alternative1, "alternative1 is null");
			requireNonNull(alternatives, "alternatives is null");
			this.name = Optional.of(name);
			this.alternatives = Arrayz.prepend(alternatives, alternative1);
		}

		public boolean isSubRule() {
			return !name.isPresent();
		}

		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			final Either<Integer, Tree<Tuple2<Integer, Integer>>> initial = new Left<>(index);
			return Stream
					.of(alternatives)
					.parallel()
					.map(parser -> parser.get().parse(text, index))
					.reduce(initial, (t1, t2) -> reduce(t1, t2, text, index), (t1, t2) -> reduce(t1, t2, text, index));
		}

		@Override
		public String toString() {
			// TODO
			throw new UnsupportedOperationException();
		}

		/**
		 * Make decision on following one of two parse trees. Each tree may may be a valid match on the input text or
		 * not.
		 * 
		 * @param tree1 First parse tree.
		 * @param tree2 Second parse tree.
		 * @param text The input text.
		 * @param index The current index.
		 * @return One of the given parse trees, which is either no match (Left) or a match (Right).
		 */
		// TODO: implement operator precedence & resolve ambiguities here
		private Either<Integer, Tree<Tuple2<Integer, Integer>>> reduce(
				Either<Integer, Tree<Tuple2<Integer, Integer>>> tree1,
				Either<Integer, Tree<Tuple2<Integer, Integer>>> tree2, String text, int index) {
			// if both trees are valid parse results, i.e. Right, then we found an ambiguity
			require(tree1.isLeft() || tree2.isLeft(), () -> "Ambiguity found at "
					+ Strings.lineAndColumn(text, index)
					+ ":\n"
					+ text);
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

	// TODO: javadoc
	static class Sequence implements Parser {

		// TODO: make whitespace regex configurable
		private static final Pattern WHITESPACE = Pattern.compile("\\s*");

		final Supplier<Parser>[] parsers;

		@SafeVarargs
		public Sequence(Supplier<Parser>... parsers) {
			requireNotNullOrEmpty(parsers, "No parsers");
			this.parsers = parsers;
		}

		// TODO: rewrite this method (immutable & recursive)
		@Override
		public Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index) {
			// Starts with an emty root tree and successively attaches parsed children.
			final Either<Integer, Tree<Tuple2<Integer, Integer>>> initial = new Right<>(new Tree<>("Sequence",
					Tuples.of(index, 0)));
			return Stream.of(parsers).reduce(initial, (tree, parser) -> {
				if (tree.isLeft()) {
					// first failure returned
					return tree;
				} else {
					// next parser parses at current index
					final Tree<Tuple2<Integer, Integer>> node = tree.right().get();
					final int lastIndex = node.value._1 + node.value._2;
					final Matcher matcher = WHITESPACE.matcher(text);
					final int currentIndex = matcher.find(lastIndex) ? matcher.end() : lastIndex;
					final Either<Integer, Tree<Tuple2<Integer, Integer>>> parsed = parser.get().parse(text,
							currentIndex);
					// on success, attach token to tree
					if (parsed.isRight()) {
						final Tree<Tuple2<Integer, Integer>> child = parsed.right().get();
						node.attach(child);
						node.value = Tuples.of(node.value._1, child.value._2);
						return tree;
					} else {
						// parser failed => the whole sequence could not be parsed
						return parsed;
					}
				}
			}, (t1, t2) -> null); // combiner not used because stream is not parallel
		}

		@Override
		public String toString() {
			// TODO
			throw new UnsupportedOperationException();
		}
	}
}
