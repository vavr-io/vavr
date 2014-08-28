/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Requirements;

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
public class Grammar implements GrammarInterface {

	private final Parsers.Rule startRule;

	protected Grammar(Supplier<Parsers.Rule> startRule) {
		Requirements.requireNonNull(startRule, "startRule is null");
		this.startRule = startRule.get();
	}

	@Override
	public Parsers.Rule getStartRule() {
		return startRule;
	}

	@Override
	public String toString() {
		// preserving insertion order
		final LinkedHashSet<Parsers.Rule> rules = new LinkedHashSet<>();
		findRules(rules, startRule);
		return rules.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
	}

	private void findRules(Set<Parsers.Rule> rules, Parsers.Rule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			Stream.of(rule.alternatives)
					.filter(parser -> parser.get() instanceof Parsers.Rule)
					.forEach(ruleRef -> findRules(rules, (Parsers.Rule) ruleRef));
		}
	}
}
