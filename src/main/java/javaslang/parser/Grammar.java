/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Requirements.requireNonNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javaslang.Requirements;
import javaslang.Strings;
import javaslang.collection.Node;
import javaslang.collection.Tree;
import javaslang.monad.Either;
import javaslang.monad.Failure;
import javaslang.monad.Success;
import javaslang.monad.Try;
import javaslang.parser.Parser.HasChildren;

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
	public Try<Tree<Token>> parse(String text) {
		final Either<Integer, List<Node<Token>>> parseResult = startRule.parse(text, 0, false);
		if (parseResult.isRight()) {
			// DEV-NODE: a Rule returns a CST with one node => head() is result
			final Tree<Token> concreteSyntaxTree = parseResult.get().get(0).asTree();
			return new Success<>(concreteSyntaxTree);
		} else {
			final int index = parseResult.left().get();
			return new Failure<>(new IllegalArgumentException("cannot parse input at "
					+ Strings.lineAndColumn(text, index)));
		}
	}

	@Override
	public String toString() {
		// preserving insertion order
		final Set<Parser.Rule> rules = new LinkedHashSet<>();
		final Set<Parser> visited = new HashSet<>();
		findRules(visited, rules, startRule);
		return rules.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
	}

	private void findRules(Set<Parser> visited, Set<Parser.Rule> rules, Parser parser) {
		if (!visited.contains(parser)) {
			visited.add(parser);
			if (parser instanceof Parser.Rule) {
				rules.add((Parser.Rule) parser);
			}
			if (parser instanceof HasChildren) {
				((HasChildren) parser).getChildren().stream().forEach(child -> findRules(visited, rules, child.get()));
			}
		}
	}

	// -- atomic shortcuts used in grammar definitions

	/**
	 * Shortcut for {@code new Parser.Rule(name, alternatives)}.
	 * 
	 * @param name Rule name.
	 * @param alternatives Rule alternatives.
	 * @return A new {@link Parser.Rule}.
	 */
	@SafeVarargs
	public static Parser.Rule rule(String name, Supplier<Parser>... alternatives) {
		return new Parser.Rule(name, alternatives);
	}

	/**
	 * Shortcut for {@code new Parser.SubRule(alternatives)}.
	 * 
	 * @param alternatives SubRule alternatives.
	 * @return A new {@link Parser.SubRule}.
	 */
	@SafeVarargs
	public static Parser.SubRule subRule(Supplier<Parser>... alternatives) {
		return new Parser.SubRule(alternatives);
	}

	/**
	 * Shortcut for {@code new Parser.Sequence(parsers))}.
	 * 
	 * @param parsers Sequenced parsers.
	 * @return A new {@link Parser.Sequence}.
	 */
	@SafeVarargs
	public static Parser.Sequence seq(Supplier<Parser>... parsers) {
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
	 * Shortcut for {@code new Parser.Quantifier(new Parser.Sequence(parsers), Parser.Quantifier.Bounds.ZERO_TO_ONE)} .
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	@SafeVarargs
	public static Parser.Quantifier _0_1(Supplier<Parser>... parsers) {
		return quantifier(parsers, Parser.Quantifier.Bounds.ZERO_TO_ONE);
	}

	/**
	 * Shortcut for {@code new Parser.Quantifier(new Parser.Sequence(parsers), Parser.Quantifier.Bounds.ZERO_TO_N)}.
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	@SafeVarargs
	public static Parser.Quantifier _0_n(Supplier<Parser>... parsers) {
		return quantifier(parsers, Parser.Quantifier.Bounds.ZERO_TO_N);
	}

	/**
	 * Shortcut for {@code new Parser.Quantifier(new Parser.Sequence(parsers), Parser.Quantifier.Bounds.ONE_TO_N)}.
	 * 
	 * @param parsers Quantified parsers.
	 * @return A new {@link Parser.Quantifier}.
	 */
	@SafeVarargs
	public static Parser.Quantifier _1_n(Supplier<Parser>... parsers) {
		return quantifier(parsers, Parser.Quantifier.Bounds.ONE_TO_N);
	}

	private static Parser.Quantifier quantifier(Supplier<Parser>[] parsers, Parser.Quantifier.Bounds bounds) {
		Requirements.requireNotNullOrEmpty(parsers, "parsers is null or empty");
		final Supplier<Parser> parser = (parsers.length == 1) ? parsers[0] : new Parser.Sequence(parsers);
		return new Parser.Quantifier(parser, bounds);
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
	public static Parser list(Supplier<Parser> parser, String delimiter) {
		return _0_1(parser, _0_n(str(delimiter), parser));
	}

	public static Parser list(Supplier<Parser> parser, String delimiter, String prefix, String suffix) {
		return seq(str(prefix), _0_1(parser, _0_n(str(delimiter), parser)), str(suffix));
	}
}
