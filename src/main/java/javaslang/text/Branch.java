/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.lang.ArrayExtensions;
import javaslang.lang.Strings;

public class Branch extends Parser {

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	Branch(Supplier<Parser>... parsers) {
		require(!ArrayExtensions.isNullOrEmpty(parsers), "no parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	Branch(String name, Supplier<Parser>... parsers) {
		require(!ArrayExtensions.isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		final Either<Integer, Tree<Token>> initial = new Left<>(index);
		return Stream.of(parsers).parallel()
				.map(parser -> parser.get().parse(text, index))
				.reduce(initial,
						(t1, t2) -> reduce(t1, t2, text, index),
						(t1, t2) -> reduce(t1, t2, text, index));
	}

	private Either<Integer, Tree<Token>> reduce(Either<Integer, Tree<Token>> tree1,
			Either<Integer, Tree<Token>> tree2, String text, int index) {
		require(tree1.isLeft() || tree2.isLeft(),
				() -> "ambiguity found at " + Strings.lineAndColumn(text, index) + ":\n"
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
