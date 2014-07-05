/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.Lang.requireNonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;

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
public class Grammar {
	
	private final Supplier<Parser> parser;

	public Grammar(Supplier<Parser> parser) {
		requireNonNull(parser, "parser is null");
		this.parser = new Sequence(parser, EOF.instance());
	}

	public Try<Tree<Token>> parse(String text) {
		final Either<Integer, Tree<Token>> cst = parser.get().parse(text, 0);
		return cst.isRight() ? new Success<>(cst.right().get()) : new Failure<>(
				new IllegalArgumentException("cannot parse input at index " + cst.left().get()));
	}

	public String stringify() {
		final StringBuilder rule = new StringBuilder();
		final StringBuilder definitions = new StringBuilder();
		final Set<String> visited = new HashSet<>();
		rule.append("Grammar\n  : ");
		parser.get().stringify(rule, definitions, visited);
		rule.append("\n  ;\n\n");
		return rule.append(definitions).toString();
	}
	
}
