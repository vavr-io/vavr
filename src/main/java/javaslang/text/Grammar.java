/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;

// TODO: CST to AST transformation (as external DSL within the grammar)
// TODO: add Regex Parser: "regex" (literal has single quotes 'lll')
// TODO: unescape literals
// TODO: remove Branch, Sequence and Multiplicity nodes if they have no name/id
public class Grammar {

	final Supplier<Parser> parser;

	public Grammar(Supplier<Parser> parser) {
		require(parser != null, "parser is null");
		this.parser = new Sequence(parser, EOF.INSTANCE);
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
