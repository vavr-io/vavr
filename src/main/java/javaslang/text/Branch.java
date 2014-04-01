/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Arrays.isNullOrEmpty;
import static javaslang.lang.Lang.require;
import static javaslang.lang.Strings.lineAndColumn;

import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.lang.Arrays;
import javaslang.lang.Strings;

public class Branch extends Parser {

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	Branch(Supplier<Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	Branch(String name, Supplier<Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		final Either<Integer, Tree<Token>> initial = new Left<>(index);
		return Arrays.parallelStream(parsers)
				.map(parser -> {
					return parser.get().parse(text, index);
				})
				.reduce(initial, (tree1, tree2) -> reduce(tree1, tree2, text, index),
						(t1, t2) -> reduce(t1, t2, text, index));
	}

	private Either<Integer, Tree<Token>> reduce(Either<Integer, Tree<Token>> tree1,
			Either<Integer, Tree<Token>> tree2, String text, int index) {
		require(tree1.isLeft() || tree2.isLeft(),
				() -> "ambiguity found at " + Strings.toString(lineAndColumn(text, index)) + ":\n"
						+ text);
		if (tree1.isRight()) {
			return tree1;
		} else if (tree2.isRight()) {
			return tree2;
		} else if (tree1.left().get() >= tree2.left().get()) {
			return tree1;
		} else {
			return tree2;
		}
	}
	
	@Override
	int getChildCount() {
		return parsers.length;
	}

	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		Parsers.stringify(name, parsers, "\n  | ", " | ", true, rule, definitions, visited);
	}

	@Override
	public String toString() {
		return Parsers.toString(name, parsers, " | ");
	}

}
