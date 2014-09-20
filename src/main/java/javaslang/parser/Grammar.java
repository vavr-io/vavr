/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Requirements.requireNonNull;
import static javaslang.parser.Parser.Quantifier.UNBOUNDED;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Requirements;
import javaslang.Strings;
import javaslang.collection.Tree;
import javaslang.monad.Either;
import javaslang.monad.Failure;
import javaslang.monad.Success;
import javaslang.monad.Try;
import javaslang.parser.Parser.HasChildren;
import javaslang.parser.Parser.ParseResult;
import javaslang.parser.Parser.Rule;
import javaslang.parser.Parser.RulePart;

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
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/1888854/what-is-the-difference-between-an-abstract-syntax-tree-and-a-concrete-syntax-tre">Abstract
 *      vs. concrete syntax tree</a>
 */
// DEV-NOTE: Extra class needed because interface cannot override Object#toString()
public class Grammar {

	public static final Parser.Any ANY = Parser.Any.INSTANCE;
	public static final Parser.EOF EOF = Parser.EOF.INSTANCE;

	// TODO: should all parsers initially get the supplied referenced parsers?
	private final Parser.Rule startRule;

	/**
	 * Creates a grammar. Intended to be used with direct instantiation.
	 * 
	 * <pre>
	 * <code>
	 * final Parser.Rule startRule = Grammar.rule("root", Grammar.ANY);
	 * final Grammar grammar = new Grammar(startRule);
	 * final Try&lt;Tree&lt;Token&gt;&gt; cst = grammar.parse("text");
	 * </code>
	 * </pre>
	 * 
	 * @param startRule The start rule of the grammar.
	 */
	public Grammar(Parser.Rule startRule) {
		requireNonNull(startRule, "startRule is null");
		this.startRule = startRule;
	}

	/**
	 * Creates a grammar. Intended to be used with inheritance and method references.
	 * 
	 * <pre>
	 * <code>
	 * class MyGrammar extends Grammar {
	 * 
	 *     MyGrammar() {
	 *         super(MyGrammar::startRule);
	 *     }
	 *     
	 *     static Rule startRule() {
	 *         return rule("root", ANY);
	 *     }
	 *     
	 *     // ...
	 * 
	 * }
	 * 
	 * final Grammar grammar = new MyGrammar();
	 * final Try&lt;Tree&lt;Token&gt;&gt; cst = grammar.parse("text");
	 * </code>
	 * </pre>
	 * 
	 * @param startRuleSupplier Supplies the start rule of the grammar.
	 */
	public Grammar(Supplier<Parser.Rule> startRuleSupplier) {
		this(requireNonNull(startRuleSupplier, "startRuleSupplier is null").get());
	}

	/**
	 * TODO: javadoc
	 * 
	 * @param text A text input to be parsed.
	 * @return A concrete syntax tree of the text on parse success or a failure if a parse error occured.
	 */
	// TODO: return Either instead of Try, where Right is the CST/parse tree and Left is a detailed ParseFailure description
	public Try<Tree<Token>> parse(String text) {
		// TODO: simplify API: List<Node<Token>> vs ParseResult
		final Either<Integer, ParseResult> parseResult = startRule.parse(text, 0, false);
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
		final Set<Parser.Rule> rules = new LinkedHashSet<>();
		findRules(visited, rules, startRule);
		return rules.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
	}

	private void findRules(Set<Parser> visited, Set<Parser.Rule> rules, Parser parser) {
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
	 * Shortcut for {@code new Parser.RuleRef(ruleSupplier)}.
	 * 
	 * @param ruleSupplier A rule supplier.
	 * @return A new {@link Parser.RuleRef}.
	 */
	public static Parser.RuleRef ref(Supplier<Parser.Rule> ruleSupplier) {
		return new Parser.RuleRef(ruleSupplier);
	}

	/**
	 * Shortcut for {@code new Parser.Rule(name, alternatives)}.
	 * 
	 * @param name Rule name.
	 * @param alternatives Rule alternatives.
	 * @return A new {@link Parser.Rule}.
	 */
	@SafeVarargs
	public static Parser.Rule rule(String name, RulePart... alternatives) {
		return new Parser.Rule(name, alternatives);
	}

	/**
	 * Shortcut for {@code new Parser.Subrule(alternatives)}.
	 * 
	 * @param alternatives Subrule alternatives.
	 * @return A new {@link Parser.Subrule}.
	 */
	@SafeVarargs
	public static Parser.Subrule subRule(RulePart... alternatives) {
		return new Parser.Subrule(alternatives);
	}

	/**
	 * Shortcut for {@code new Parser.Sequence(parsers))}.
	 * 
	 * @param parsers Sequenced parsers.
	 * @return A new {@link Parser.Sequence}.
	 */
	@SafeVarargs
	public static Parser.Sequence seq(RulePart... parsers) {
		return new Parser.Sequence(parsers);
	}

	/**
	 * Shortcut for {@code new Parser.Charset(charset)}.
	 * 
	 * @param charset A charset String.
	 * @return A new {@link Parser.Charset}.
	 */
	public static Parser.Charset charset(String charset) {
		return new Parser.Charset(charset);
	}

	/**
	 * Shortcut for {@code new Parser.Range(from, to)}.
	 * 
	 * @param from Left bound of the range, inclusive.
	 * @param to Right bound of the range, inclusive.
	 * @return A new {@link Parser.Range}.
	 */
	public static Parser.Range range(char from, char to) {
		return new Parser.Range(from, to);
	}

	/**
	 * Shortcut for {@code new Parser.Literal(s)}.
	 * 
	 * @param s A string.
	 * @return A new {@link Parser.Literal}.
	 */
	public static Parser.Literal str(String s) {
		return new Parser.Literal(s);
	}

	/**
	 * Shortcut for
	 * {@code new Parser.Quantifier((parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers), 0, 1)} .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	public static Parser.Quantifier _0_1(RulePart... parsers) {
		return mul(0, 1, parsers);
	}

	/**
	 * Shortcut for
	 * {@code new Parser.Quantifier((parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers), 0, Parser.Quantifier.UNBOUNDED)}
	 * .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	public static Parser.Quantifier _0_n(RulePart... parsers) {
		return mul(0, UNBOUNDED, parsers);
	}

	/**
	 * Shortcut for
	 * {@code new Parser.Quantifier((parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers), 1, Parser.Quantifier.UNBOUNDED)}
	 * .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	public static Parser.Quantifier _1_n(RulePart... parsers) {
		return mul(1, UNBOUNDED, parsers);
	}

	/**
	 * Shortcut for
	 * {@code new Parser.Quantifier((parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers), 1, Parser.Quantifier.UNBOUNDED)}
	 * .
	 * 
	 * @param lowerBound Number of required matches.
	 * @param upperBound Number of allowed matches. Infinite, if {@code Quantifier.UNBOUNDED} is given.
	 * @param parsers Quantified / multiplied parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	public static Parser.Quantifier mul(int lowerBound, int upperBound, RulePart... parsers) {
		Requirements.requireNotNullOrEmpty(parsers, "parsers is null or empty");
		final RulePart parser = (parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers);
		return new Parser.Quantifier(parser, lowerBound, upperBound);
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
	 * @param delimiter A delimiter.
	 * @return A Parser which recognizes {@code ( P ( ',' P )* )?}.
	 */
	public static Parser.RulePart list(RulePart parser, String delimiter) {
		return _0_1(parser, _0_n(str(delimiter), parser));
	}

	public static Parser.RulePart list(RulePart parser, String delimiter, String prefix, String suffix) {
		return seq(str(prefix), _0_1(parser, _0_n(str(delimiter), parser)), str(suffix));
	}
}
