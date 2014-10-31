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
import static javaslang.parser.Parser.Quantifier.UNBOUNDED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
 * <p>
 * The parser interface. Parsers reflect the building parts of a grammar. The core grammar syntax describes a formal
 * language targeted to parsers of finite character sequences.
 * </p>
 * 
 * <h2>Character Groups</h2>
 * 
 * <p>
 * These are the atomic building parts to match groups of characters:
 * </p>
 * 
 * <ul>
 * <li>{@code 'text'} -- a Literal containing one or more characters</li>
 * <li>{@code .} -- Any, a single character</li>
 * <li>{@code 'a'..'z'} -- a Range of characters</li>
 * <li>{@code [a-zA-Z$]} -- a Charset, equals {@code ( 'a'..'z' | 'A'..'Z' | '$' )}</li>
 * <li>{@code EOF} -- end of file, matches no character</li>
 * </ul>
 * 
 * <p>
 * Single character matchers (Any, Range and Charset) and EOF may be negated:
 * </p>
 * 
 * <ul>
 * <li>{@code !T} -- negation, character not in {@code T}</li>
 * </ul>
 * 
 * <p>
 * The following rules hold:
 * </p>
 * 
 * <ul>
 * <li>{@code !!T} = {@code T}</li>
 * <li>{@code !.} = {@code EOF}, {@code !EOF} = {@code .}</li>
 * </ul>
 * 
 * <p>
 * The operator precedence of {@code !} is the highest of all, e.g. {@code !T*} is the same as {@code (!T)*}.
 * </p>
 * 
 * <h2>Rules</h2>
 * 
 * <p>
 * We distinguish between parser rules and lexer rules, which have the same syntax:
 * </p>
 * 
 * <pre>
 * <code>
 * rule : alternative_1 | ... | alternative_n
 * </code>
 * </pre>
 * 
 * <p>
 * where {@code n > 0} and rule is the name of the rule. The name is unique in the scope of a grammar. The first
 * matching alternative wins. The alternatives are composed of the building parts of character groups as described above
 * and:
 * </p>
 * 
 * <ul>
 * <li>{@code ruleRef} -- reference to another rule by name</li>
 * <li>{@code T?} -- zero or one occurrence of {@code T}</li>
 * <li>{@code T*} -- zero or more occurrences of {@code T}</li>
 * <li>{@code T+} -- one or more occurrences of {@code T}</li>
 * <li><code>T{m,n}</code> -- m to n occurrences of {@code T}</li>
 * <li><code>T{m}</code> -- same as <code>T{m,m}</code></li>
 * <li>{@code T1 ... Tn} -- a sequence which matches if all parts match</li>
 * <li>{@code ( T1 | ... | Tn )} -- a subrule of alternatives, first match wins</li>
 * </ul>
 * 
 * <p>
 * Additionally there are empty (sub)rule alternatives:
 * </p>
 *
 * <pre>
 * <code>
 * rule : ( | alternative1) | | ( alternative2 | )
 * </code>
 * </pre>
 * 
 * <p>
 * In the following, the difference between lexer and parser rules is described.
 * </p>
 * 
 * <h3>Lexer rules</h3>
 * 
 * <ul>
 * <li>Lexer rules (short: token) produce the leafs of a parse tree.</li>
 * <li>They consist of a (non-empty) sequence of characters.</li>
 * <li>There is one empty token, {@code EOF}.</li>
 * <li>The name of a tokens is not part of the parse tree.</li>
 * <li>A token name starts with an upper case character.</li>
 * <li>Whitespace is not parsed automatically within a token rule.</li>
 * <li>Tokens may contain references to other tokens.</li>
 * <li>Tokens may not contain references to rules other than tokens.</li>
 * </ul>
 * 
 * <h3>Parser rules</h3>
 * 
 * <ul>
 * <li>Parser rules (short: rule) produce the inner nodes of a parse tree.</li>
 * <li>They have a name and a non-empty list of children.</li>
 * <li>The name of a rule and it's children are part of the parse tree.</li>
 * <li>A rule name starts with a lower case character.</li>
 * <li>Whitespace is parsed automatically within a parser rule.</li>
 * <li>Rules may contain references to other rules and tokens.</li>
 * </ul>
 * 
 * <h3>Lexical Token Definitions within Parser Rules</h3>
 * 
 * <p>
 * A parser rule part T is purely lexical if it is a combination of the following rule parts:
 * </p>
 * 
 * <ul>
 * <li>A single character matcher (Any {@code .}, Range {@code 'a'..'z'} or Charset {@code [a-zA-Z$]})</li>
 * <li>End of file matcher ({@code EOF})</li>
 * <li>Negation ({@code !T})</li>
 * <li>Quantification ({@code T?}, {@code T*}, {@code T+}, <code>T{m,n}</code>, <code>T{m}</code>)</li>
 * </ul>
 * 
 * <p>
 * Purely lexical parse results are combined to a token.
 * </p>
 *
 * <h2>Whitespace Handling</h2>
 * 
 * <ul>
 * <li>Rule alternatives are wrapped in sequences in order to handle whitespace in each rule.</li>
 * <li>Therefor quantifiers only have to handle whitespace *between* occurrences.</li>
 * <li>Tokens and pure lexical rule parts within parser rules do not skip whitespace.</li>
 * </ul>
 * 
 */
interface Parser extends Serializable {

	// TODO: issue #48: make whitespace configurable
	static final Rule DEFAULT_WS = new Rule("WS", new Quantifier(new Charset(" \t\r\n"), 0, UNBOUNDED));

	/**
	 * A parser rule part T is purely lexical (short: pure) if it is a combination of the following rule parts:
	 * 
	 * <ul>
	 * <li>A single character matcher Any {@code .}, Range {@code 'a'..'z'} or Charset {@code [a-zA-Z$]}</li>
	 * <li>End of file matcher {@code EOF}</li>
	 * <li>Negation {@code !T}</li>
	 * <li>Quantification {@code T?}, {@code T*}, {@code T+}, <code>T{m,n}</code>, <code>T{m}</code></li>
	 * </ul>
	 * 
	 * Purely lexical parse results are combined to a token.
	 * 
	 * @return true, if this parser is pure, false otherwise.
	 */
	boolean isPure();

	/**
	 * Trying to parse a text using the current parser, starting at the given index.
	 * 
	 * @param text The whole text to parse.
	 * @param index The current index of the parser.
	 * @param lexicalScope The lexical scope is defined by a rule. The scope is lexing (true) or parsing (false). When
	 *            in lexing mode, cascaded rules are combined to one and whitespace is not ignored. When in parsing
	 *            mode, parse-results of cascaded rules are added as children the the actual tree node and whitespace
	 *            may be ignored.
	 * @return Either a Left, containing the index of failure or a Right, containing the range (index, length) parsed.
	 */
	Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope);

	/**
	 * Returns a String representation in grammar notation of this parser.
	 * 
	 * @return A String in grammar notation.
	 */
	@Override
	String toString();

	// -- parsers

	/**
	 * Wildcard parser: {@code .}
	 * <p>
	 * Matches a single, arbitrary character.
	 */
	static class Any implements NegatableRulePart {

		private static final long serialVersionUID = -4429256043627772276L;

		static final Any INSTANCE = new Any();

		// hidden
		private Any() {
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated) {
			if (negated) {
				return EOF.INSTANCE.parse(text, index, lexicalScope, false);
			} else {
				final boolean match = index < text.length();
				return match ? token(text, index, 1, false) : stoppedAt(index);
			}
		}

		@Override
		public String toString() {
			return ".";
		}

		// -- Serializable implementation

		/**
		 * Instance control for object serialization.
		 * 
		 * @return The singleton instance of ANY.
		 * @see java.io.Serializable
		 */
		private Object readResolve() {
			return INSTANCE;
		}
	}

	/**
	 * Character set parser: {@code [a-zA-Z$_]}
	 */
	static class Charset implements NegatableRulePart {

		private static final long serialVersionUID = -8608573218872232679L;

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
		public boolean isPure() {
			return true;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated) {
			final boolean match = index < text.length() && (inSet.test(text.charAt(index)) ^ negated);
			return match ? token(text, index, 1, false) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return String
					.format("[%s]", stringify(charsetString).replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]"));
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
	 * Empty alternative parser: {@code ( T | )}
	 * <p>
	 * Recognizes literally nothing, i.e. returns an empty token.
	 */
	static class Empty implements RulePart {

		private static final long serialVersionUID = 5479444554723545030L;

		static final Empty INSTANCE = new Empty();

		// hidden
		private Empty() {
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			return token(text, index, 0, false);
		}

		@Override
		public String toString() {
			return "";
		}

		// -- Serializable implementation

		/**
		 * Instance control for object serialization.
		 * 
		 * @return The singleton instance of Empty.
		 * @see java.io.Serializable
		 */
		private Object readResolve() {
			return INSTANCE;
		}
	}

	/**
	 * End-of-file parser: {@code EOF}
	 * <p>
	 * Recognizes the end of the input.
	 */
	static class EOF implements NegatableRulePart {

		private static final long serialVersionUID = 727834629856776708L;

		static final EOF INSTANCE = new EOF();

		// hidden
		private EOF() {
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated) {
			if (negated) {
				return Any.INSTANCE.parse(text, index, lexicalScope, false);
			} else {
				final boolean match = (index == text.length());
				return match ? token(text, index, 0, false) : stoppedAt(index);
			}
		}

		@Override
		public String toString() {
			return "EOF";
		}

		// -- Serializable implementation

		/**
		 * Instance control for object serialization.
		 * 
		 * @return The singleton instance of EOF.
		 * @see java.io.Serializable
		 */
		private Object readResolve() {
			return INSTANCE;
		}
	}

	/**
	 * String literal parser: {@code 'funky'}
	 */
	static class Literal implements RulePart {

		private static final long serialVersionUID = 4722212439353996246L;

		final String literal;

		Literal(String literal) {
			requireNotNullOrEmpty(literal, "literal is null or empty");
			this.literal = literal;
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			final boolean match = text.startsWith(literal, index);
			return match ? token(text, index, literal.length(), false) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return String.format("'%s'", stringify(literal).replaceAll("'", "\\\\'"));
		}
	}

	/**
	 * Negation parser: {@code !T}
	 */
	static class Negation implements NegatableRulePart, HasChildren {

		private static final long serialVersionUID = -626637972108117183L;

		final NegatableRulePart parser;

		Negation(NegatableRulePart parser) {
			requireNonNull(parser, "parser is null");
			this.parser = parser;
		}

		@Override
		public Parser[] getChildren() {
			return new Parser[] { parser };
		}

		@Override
		public boolean isPure() {
			return parser.isPure();
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated) {
			return parser.parse(text, index, lexicalScope, !negated);
		}

		@Override
		public String toString() {
			return "!" + parser.toString();
		}
	}

	/**
	 * Quantifier parser:
	 * 
	 * <ul>
	 * <li>parse X 0..1 times: {@code X?}</li>
	 * <li>parse X 0..n times: {@code X*}</li>
	 * <li>parse X 1..n times: {@code X+}</li>
	 * <li>parse X a..b times: <code>X{a,b}</code></li>
	 * <li>parse X a times: <code>X{a}</code></li>
	 * </ul>
	 * 
	 * Whitespace was already skipped if we are within a parser rule (lexicalScope == true), assuming that all rules
	 * technically have a sequence of parsers as child and the sequence parser does skip whitespace.
	 */
	static class Quantifier implements RulePart, HasChildren {

		private static final long serialVersionUID = -5039116522552503733L;

		static final int UNBOUNDED = Integer.MAX_VALUE;

		final RulePart parser;
		final int lowerBound;
		final int upperBound;

		Quantifier(RulePart parser, int lowerBound, int upperBound) {
			requireNonNull(parser, "parser is null");
			require(0 <= lowerBound, "lowerBound < 0");
			require(lowerBound <= upperBound, "lowerBound > upperBound");
			require(upperBound > 0 || upperBound == UNBOUNDED, "upperBound <= 0"); // this 
			this.parser = parser;
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		@Override
		public Parser[] getChildren() {
			return new Parser[] { parser };
		}

		@Override
		public boolean isPure() {
			return parser.isPure();
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			final List<Node<Token>> tokens = new ArrayList<>();
			final boolean lexical = lexicalScope || isPure();
			int currentIndex = index;
			for (int i = 0; i < upperBound; i++) {
				final Either<Integer, ParseResult> parsed = parser.parse(text, currentIndex, lexicalScope);
				if (parsed.isRight()) {
					final ParseResult parseResult = parsed.right().get();
					tokens.addAll(parseResult.tokens);
					currentIndex = skipWhitespace(text, parseResult.endIndex, lexical);
				} else {
					if (i < lowerBound) {
						return parsed;
					} else {
						return right(tokens, index, currentIndex, lexical);
					}
				}
			}
			return right(tokens, index, currentIndex, lexical);
		}

		private Either<Integer, ParseResult> right(List<Node<Token>> tokens, int index, int currentIndex,
				boolean lexicalScope) {
			return new Right<>(new ParseResult(tokens, index, currentIndex, lexicalScope));
		}

		@Override
		public String toString() {
			return toString(parser) + toString(lowerBound, upperBound);
		}

		private String toString(RulePart parser) {
			if (parser instanceof Sequence) {
				return "( " + parser.toString() + " )";
			} else {
				return parser.toString();
			}
		}

		private String toString(int lowerBound, int upperBound) {
			if (lowerBound == 0 && upperBound == 1) {
				return "?";
			} else if (lowerBound == 0 && upperBound == UNBOUNDED) {
				return "*";
			} else if (lowerBound == 1 && upperBound == UNBOUNDED) {
				return "+";
			} else if (lowerBound == upperBound) {
				return "{" + lowerBound + "}";
			} else {
				return "{" + lowerBound + "," + upperBound + "}";
			}
		}
	}

	/**
	 * Character range parser: {@code 'a'..'z'}
	 */
	static class Range implements NegatableRulePart {

		private static final long serialVersionUID = 1254044797785225220L;

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
		public boolean isPure() {
			return true;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated) {
			final boolean match = index < text.length() && (isInRange.test(text.charAt(index)) ^ negated);
			return match ? token(text, index, 1, false) : stoppedAt(index);
		}

		@Override
		public String toString() {
			return String.format("'%s'..'%s'", toString(from), toString(to));
		}

		private String toString(char c) {
			return (c == '\'') ? "\\'" : charToString(c);
		}
	}

	/**
	 * Rule reference parser: {@code rule : ruleRef}
	 */
	static class Reference implements RulePart, HasChildren {

		private static final long serialVersionUID = 1520214209747690474L;

		final Supplier<Rule> ruleSupplier;
		Rule rule;

		Reference(Supplier<Rule> ruleSupplier) {
			this.ruleSupplier = ruleSupplier;
		}

		@Override
		public Parser[] getChildren() {
			return new Parser[] { getRule() };
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			return getRule().parse(text, index, lexicalScope);
		}

		@Override
		public String toString() {
			return getRule().name;
		}

		private Rule getRule() {
			// no need to make this thread-safe
			if (rule == null) {
				rule = ruleSupplier.get();
			}
			return rule;
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
	static class Rule implements Parser, HasChildren {

		private static final long serialVersionUID = -5475018808758906093L;

		final String name;
		final RulePart[] alternatives;
		final boolean lexical;

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
		Rule(String name, RulePart... alternatives) {
			requireNotNullOrEmpty(name, "name is null or empty");
			requireNotNullOrEmpty(alternatives, "alternatives is null or empty");
			this.name = name;
			this.alternatives = alternatives;
			this.lexical = Character.isUpperCase(name.charAt(0));
		}

		@Override
		public Parser[] getChildren() {
			return alternatives;
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			require(!lexicalScope || lexical, "parser rule '" + name + "' is referenced by a lexical rule");
			final int currentIndex = skipWhitespace(text, index, lexicalScope);
			int failedIndex = currentIndex;
			for (RulePart alternative : alternatives) {
				final Either<Integer, ParseResult> result = alternative.parse(text, currentIndex, lexical);
				if (result.isRight()) {
					final ParseResult p = result.get();
					return lexical ? result : symbol(name, text, currentIndex, p.endIndex - p.startIndex, p.tokens);
				} else {
					failedIndex = Math.max(failedIndex, result.left().get());
				}
			}
			return stoppedAt(failedIndex);
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Rule)) {
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
			return Stream.of(alternatives).map(Object::toString).collect(joining(delimiter, prefix, suffix));
		}
	}

	/**
	 * Sequence parser: {@code X1 ... Xn}, where {@code n >= 2}.
	 */
	static class Sequence implements RulePart, HasChildren {

		private static final long serialVersionUID = -1904918065761909674L;

		final RulePart[] parsers;

		@SafeVarargs
		Sequence(RulePart... parsers) {
			requireNonNull(parsers, "parsers is null");
			require(parsers.length > 0, "no parsers");
			this.parsers = parsers;
		}

		@Override
		public Parser[] getChildren() {
			return parsers;
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			final List<Node<Token>> tokens = new ArrayList<>();
			int currentIndex = index;
			for (RulePart parser : parsers) {
				final Either<Integer, ParseResult> parsed = parser.parse(text,
						skipWhitespace(text, currentIndex, lexicalScope), lexicalScope);
				if (parsed.isRight()) {
					final ParseResult parseResult = parsed.get();
					tokens.addAll(parseResult.tokens);
					currentIndex = parseResult.endIndex;
				} else {
					return parsed;
				}
			}
			/* TODO:DELME */System.out.println(getClass().getSimpleName()
					+ " : "
					+ text.substring(index, Math.min(index + 20, text.length())));
			return new Right<>(new ParseResult(tokens, index, skipWhitespace(text, currentIndex, lexicalScope),
					lexicalScope));
		}

		@Override
		public String toString() {
			return Stream.of(parsers).map(Object::toString).collect(Collectors.joining(" "));
		}
	}

	/**
	 * Subrule parser: {@code ( X1 | ... | Xn )}, where {@code n >= 2}.
	 */
	static class Subrule implements RulePart, HasChildren {

		private static final long serialVersionUID = -2043785863211379238L;

		final RulePart[] alternatives;

		@SafeVarargs
		Subrule(RulePart... alternatives) {
			requireNonNull(alternatives, "alternatives is null");
			require(alternatives.length >= 2, "number of alternatives < 2");
			this.alternatives = alternatives;
		}

		@Override
		public Parser[] getChildren() {
			return alternatives;
		}

		@Override
		public boolean isPure() {
			return false;
		}

		@Override
		public Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			int failedIndex = index;
			for (RulePart alternative : alternatives) {
				final Either<Integer, ParseResult> result = alternative.parse(text, index, lexicalScope);
				if (result.isRight()) {
					return result;
				} else {
					failedIndex = Math.max(failedIndex, result.left().get());
				}
			}
			return stoppedAt(failedIndex);
		}

		@Override
		public String toString() {
			return Stream.of(alternatives).map(Object::toString).collect(Collectors.joining(" | ", "( ", " )"));
		}
	}

	// -- parse-result factory methods

	// terminal token / leaf of the parse tree
	static Either<Integer, ParseResult> token(String text, int index, int length, boolean lexicalScope) {
		/* TODO:DEBUG */System.out.println(String.format("token(%s, %s): %s", index, index + length,
				text.substring(index, index + length)));
		final List<Node<Token>> tokens = Arrays.asList(new Node<>(new Token(null, text, index, length)));
		final ParseResult parseResult = new ParseResult(tokens, index, index + length, lexicalScope);
		return new Right<>(parseResult);
	}

	// non-terminal symbol / inner rule of the parse tree / rule with children
	static Either<Integer, ParseResult> symbol(String id, String text, int index, int length, List<Node<Token>> children) {
		/* TODO: DEBUG */System.out.println(String.format("symbol(%s, %s, %s): %s", id, index, index + length,
				text.substring(index, index + length)));
		final List<Node<Token>> tokens = Arrays.asList(new Node<>(new Token(id, text, index, length), children));
		final ParseResult parseResult = new ParseResult(tokens, index, index + length, false);
		return new Right<>(parseResult);
	}

	// no match found
	static Either<Integer, ParseResult> stoppedAt(int index) {
		/* TODO:DEBUG */System.out.println("stoppedAt " + index);
		return new Left<>(index);
	}

	// -- character conversion / stringification

	static String stringify(String s) {
		return s.chars().boxed().map(i -> charToString((char) i.intValue())).collect(joining());
	}

	static String charToString(char c) {
		switch (c) {
			case 0x08:
				return "\\b"; // backspace
			case 0x09:
				return "\\t"; // tab
			case 0x0A:
				return "\\n"; // newline
			case 0x0C:
				return "\\f"; // formfeed
			case 0x0D:
				return "\\r"; // carriage return
			case 0x5C:
				return "\\\\"; // backslash
			default: {
				final boolean printable = 0x20 <= c && c <= 0x7F;
				return printable ? String.valueOf(c) : String.format("\\u%04x", (int) c);
			}
		}
	}

	// -- whitespace handling

	static int skipWhitespace(String text, int index, boolean lexicalScope) {
		return lexicalScope ? index : DEFAULT_WS
				.parse(text, index, true)
				.map(parseResult -> parseResult.endIndex)
				.orElse(index);
	}

	// -- additional types

	/**
	 * A rule consists of rule parts:
	 * 
	 * <pre>
	 * <code>
	 * rule : rulePart_1 | ... | rulePart_n
	 * </code>
	 * </pre>
	 * 
	 * Classes implementing RulePart are:
	 * <p>
	 * <ul>
	 * <li>{@linkplain Any}</li>
	 * <li>{@linkplain Charset}</li>
	 * <li>{@linkplain EOF}</li>
	 * <li>{@linkplain Literal}</li>
	 * <li>{@linkplain Negation}</li>
	 * <li>{@linkplain Quantifier}</li>
	 * <li>{@linkplain Range}</li>
	 * <li>{@linkplain Reference}</li>
	 * <li>{@linkplain Sequence}</li>
	 * <li>{@linkplain Subrule}</li>
	 * </ul>
	 */
	static interface RulePart extends Parser {
	}

	/**
	 * A {@linkplain Negation} may only negate parsers that implement {@linkplain NegatableRulePart}. This extra
	 * interface is necessary to transport the negation context which makes double-negation possible.
	 */
	static interface NegatableRulePart extends RulePart {

		Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope, boolean negated);

		@Override
		default Either<Integer, ParseResult> parse(String text, int index, boolean lexicalScope) {
			return parse(text, index, lexicalScope, false);
		}
	}

	/**
	 * Implemented by all parsers that depend on other parsers.
	 */
	static interface HasChildren {
		Parser[] getChildren();
	}

	/**
	 * Represents the positive result a {@link Parser#parse(String, int, boolean)} call.
	 */
	static class ParseResult {
		final List<Node<Token>> tokens;
		final int startIndex;
		final int endIndex;

		ParseResult(List<Node<Token>> tokens, int startIndex, int endIndex, boolean combine) {
			this.tokens = (combine && tokens.size() > 1) ? combine(tokens, startIndex, endIndex) : tokens;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		List<Node<Token>> combine(List<Node<Token>> tokens, int index, int endIndex) {
			final String text = tokens.get(0).getValue().getText();
			final int length = endIndex - index;
			return Arrays.asList(new Node<>(new Token(null, text, index, length)));
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof ParseResult)) {
				return false;
			} else {
				final ParseResult that = (ParseResult) o;
				return this.startIndex == that.startIndex
						&& this.endIndex == that.endIndex
						&& Objects.equals(this.tokens, that.tokens);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(startIndex, endIndex, tokens);
		}

		@Override
		public String toString() {
			return tokens.toString();
		}
	}
}
