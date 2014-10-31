/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Requirements.requireNonNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Strings;
import javaslang.collection.Tree;
import javaslang.monad.Either;
import javaslang.monad.Failure;
import javaslang.monad.Success;
import javaslang.monad.Try;
import javaslang.parser.Parser.Any;
import javaslang.parser.Parser.Charset;
import javaslang.parser.Parser.EOF;
import javaslang.parser.Parser.Empty;
import javaslang.parser.Parser.HasChildren;
import javaslang.parser.Parser.Literal;
import javaslang.parser.Parser.NegatableRulePart;
import javaslang.parser.Parser.Negation;
import javaslang.parser.Parser.ParseResult;
import javaslang.parser.Parser.Quantifier;
import javaslang.parser.Parser.Range;
import javaslang.parser.Parser.Reference;
import javaslang.parser.Parser.Rule;
import javaslang.parser.Parser.RulePart;
import javaslang.parser.Parser.Sequence;
import javaslang.parser.Parser.Subrule;

/**
 * <pre>
 * <code>class JSONGrammar extends Grammar {
 * 
 *     // define start rule
 *     JSONGrammar() {
 *         super("JSON");
 *     }
 *     
 *     &#64;Override
 *     protected Rule getStartRule() {
 *         return json();
 *     }
 *     
 *     // json : jsonObject | jsonArray | jsonString | jsonNumber | 'true' | 'false' | 'null' ;
 *     Rule json() {
 *         return rule("json",
 *                 this::jsonObject,
 *                 this::jsonArray,
 *                 this::jsonString,
 *                 this::jsonNumber,
 *                 str("true"),
 *                 str("false"),
 *                 str("null"));
 *     }
 *     
 *     // jsonObject : '{' ( pair ( ',' pair )* )? '}' ;
 *     Parser jsonObject() {
 *         return rule("jsonObject", seq(str("{"), list(this::pair, ","), str("}"));
 *     }
 *     
 *     // pair : jsonString ':' json ;
 *     Parser pair() {
 *         return seq(this::jsonString, str(":"), this::json);
 *     }
 *     
 *     // etc.
 *     
 * }</code>
 * </pre>
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/1888854/what-is-the-difference-between-an-abstract-syntax-tree-and-a-concrete-syntax-tre">Abstract
 *      vs. concrete syntax tree</a>
 */
// DEV-NOTE: Extra class needed because interface cannot override Object#toString()
public abstract class Grammar {

	public static final Any ANY = Any.INSTANCE;
	public static final EOF EOF = Parser.EOF.INSTANCE;
	public static final Empty ε = Empty.INSTANCE;

	private final String name;

	// DEV-NOTE: the startRule is not passed to the constructor in order to have non-static references to methods,
	//           i.e. `this::rule` instead of `Grammar::rule`. 
	protected Grammar(String name) {
		this.name = name;
	}

	/**
	 * Creates a grammar using a specific start rule.
	 * 
	 * <pre>
	 * <code>
	 * final Rule startRule = Grammar.rule("root", Grammar.ANY);
	 * final Grammar grammar = Grammar.of("MyGrammar", startRule);
	 * final Try&lt;Tree&lt;Token&gt;&gt; cst = grammar.parse("text");
	 * </code>
	 * </pre>
	 * 
	 * @param name Name of the Grammar.
	 * @param startRule The start rule of the grammar.
	 * @return A new Grammar instance.
	 */
	public static Grammar of(String name, final Rule startRule) {
		requireNonNull(name, "name is null");
		requireNonNull(startRule, "startRule is null");
		return new Grammar(name) {
			@Override
			protected Rule getStartRule() {
				return startRule;
			}
		};
	}

	protected abstract Rule getStartRule();

	/**
	 * TODO: javadoc
	 * 
	 * @param text A text input to be parsed.
	 * @return A concrete syntax tree of the text on parse success or a failure if a parse error occured.
	 */
	// TODO: return Either instead of Try, where Right is the CST/parse tree and Left is a detailed ParseFailure description
	public Try<Tree<Token>> parse(String text) {
		requireNonNull(text, "text is null");
		// TODO: simplify API: List<Node<Token>> vs ParseResult
		final Either<Integer, ParseResult> parseResult = getStartRule().parse(text, 0, false);
		if (parseResult.isRight()) {
			// DEV-NODE: a Rule returns a CST with one node => head() is result
			final Tree<Token> concreteSyntaxTree = parseResult.get().tokens.get(0).asTree();
			return new Success<>(concreteSyntaxTree);
		} else {
			final int index = parseResult.left().get();
			return new Failure<>(new IllegalArgumentException("cannot parse input at "
					+ Strings.lineAndColumn(text, index)));
		}
	}

	@Override
	public String toString() {
		final Set<Parser> visited = new HashSet<>();
		final Set<Rule> rules = new LinkedHashSet<>();
		findRules(visited, rules, getStartRule());
		return "grammar " + name + " ;\n\n" + rules.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
	}

	private void findRules(Set<Parser> visited, Set<Rule> rules, Parser parser) {
		if (!visited.contains(parser)) {
			visited.add(parser);
			if (parser instanceof Rule) {
				rules.add((Rule) parser);
			}
			if (parser instanceof HasChildren) {
				final HasChildren hasChildren = (HasChildren) parser;
				Stream.of(hasChildren.getChildren()).forEach(child -> findRules(visited, rules, child));
			}
		}
	}

	// -- atomic shortcuts used in grammar definitions

	/**
	 * Shortcut for {@code new Reference(ruleSupplier)}.
	 * 
	 * @param ruleSupplier A rule supplier.
	 * @return A new {@link Reference}.
	 */
	public static Reference ref(Supplier<Rule> ruleSupplier) {
		return new Reference(ruleSupplier);
	}

	/**
	 * Shortcut for {@code new Rule(name, alternatives)}.
	 * 
	 * @param name Rule name.
	 * @param alternatives Rule alternatives.
	 * @return A new {@link Rule}.
	 */
	@SafeVarargs
	public static Rule rule(String name, RulePart... alternatives) {
		return new Rule(name, alternatives);
	}

	/**
	 * Shortcut for {@code new Subrule(alternatives)}.
	 * 
	 * @param alternatives Subrule alternatives.
	 * @return A new {@link Subrule}.
	 */
	@SafeVarargs
	public static Subrule subrule(RulePart... alternatives) {
		return new Subrule(alternatives);
	}

	/**
	 * Shortcut for {@code new Sequence(parsers))}.
	 * 
	 * @param parsers Sequenced parsers.
	 * @return A new {@link Sequence}.
	 */
	@SafeVarargs
	public static Sequence seq(RulePart... parsers) {
		return new Sequence(parsers);
	}

	/**
	 * Shortcut for {@code new Charset(charset)}.
	 * 
	 * @param charset A charset String.
	 * @return A new {@link Charset}.
	 */
	public static Charset charset(String charset) {
		return new Charset(charset);
	}

	/**
	 * Shortcut for {@code new Range(from, to)}.
	 * 
	 * @param from Left bound of the range, inclusive.
	 * @param to Right bound of the range, inclusive.
	 * @return A new {@link Range}.
	 */
	public static Range range(char from, char to) {
		return new Range(from, to);
	}

	/**
	 * Shortcut for {@code new Literal(s)}.
	 * 
	 * @param s A string.
	 * @return A new {@link Literal}.
	 */
	public static Literal str(String s) {
		return new Literal(s);
	}

	/**
	 * Shortcut for {@code new Quantifier((parsers.length == 1) ? parsers[0] : new Sequence(parsers), 0, 1)} .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Quantifier}.
	 */
	public static Quantifier _0_1(RulePart... parsers) {
		return mul(0, 1, parsers);
	}

	/**
	 * Shortcut for
	 * {@code new Quantifier((parsers.length == 1) ? parsers[0] : new Sequence(parsers), 0, Quantifier.UNBOUNDED)} .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Quantifier}.
	 */
	public static Quantifier _0_n(RulePart... parsers) {
		return mul(0, Quantifier.UNBOUNDED, parsers);
	}

	/**
	 * Shortcut for
	 * {@code new Quantifier((parsers.length == 1) ? parsers[0] : new Sequence(parsers), 1, Quantifier.UNBOUNDED)} .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Quantifier}.
	 */
	public static Quantifier _1_n(RulePart... parsers) {
		return mul(1, Quantifier.UNBOUNDED, parsers);
	}

	/**
	 * Shortcut for {@code new Quantifier((parsers.length == 1) ? parsers[0] : new Sequence(parsers), times, times)} .
	 * 
	 * @param times Number of required matches.
	 * @param parsers Quantified / multiplied parsers.
	 * @return A new {@link Quantifier}.
	 */
	public static Quantifier mul(int times, RulePart... parsers) {
		final RulePart parser = (parsers.length == 1) ? parsers[0] : new Sequence(parsers);
		return new Quantifier(parser, times, times);
	}

	/**
	 * Shortcut for
	 * {@code new Quantifier((parsers.length == 1) ? parsers[0] : new Sequence(parsers), 1, Quantifier.UNBOUNDED)} .
	 * 
	 * @param lowerBound Number of required matches.
	 * @param upperBound Number of allowed matches. Infinite, if {@code Quantifier.UNBOUNDED} is given.
	 * @param parsers Quantified / multiplied parsers.
	 * @return A new {@link Quantifier}.
	 */
	public static Quantifier mul(int lowerBound, int upperBound, RulePart... parsers) {
		final RulePart parser = (parsers.length == 1) ? parsers[0] : new Sequence(parsers);
		return new Quantifier(parser, lowerBound, upperBound);
	}

	/**
	 * Shortcut for {@code new Negation(parser)}.
	 * 
	 * @param parser Negated parser.
	 * @return A new {@link Negation}.
	 */
	public static Negation not(NegatableRulePart parser) {
		return new Negation(parser);
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
	 * @param parser A
	 * @param delimiter A delimiter.
	 * @return A Parser which recognizes {@code ( P ( ',' P )* )?}.
	 */
	public static RulePart list(RulePart parser, String delimiter) {
		return _0_1(parser, _0_n(str(delimiter), parser));
	}

	public static RulePart list(RulePart parser, String delimiter, String prefix, String suffix) {
		return seq(str(prefix), _0_1(parser, _0_n(str(delimiter), parser)), str(suffix));
	}
}
