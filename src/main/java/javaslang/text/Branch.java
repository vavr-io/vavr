/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.Lang.require;
import static javaslang.Lang.requireNotNullOrEmpty;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Strings;
import javaslang.either.Either;
import javaslang.either.Left;

public class Branch extends Parser {

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	public Branch(Supplier<Parser>... parsers) {
		requireNotNullOrEmpty(parsers, "No parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	public Branch(String name, Supplier<Parser>... parsers) {
		requireNotNullOrEmpty(parsers, "No parsers");
		this.name = name;
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		final Either<Integer, Tree<Token>> initial = new Left<>(index);
		return Stream.of(parsers)
				.parallel()
				.map(parser -> parser.get().parse(text, index))
				.reduce(initial, (t1, t2) -> reduce(t1, t2, text, index),
						(t1, t2) -> reduce(t1, t2, text, index));
	}

	private Either<Integer, Tree<Token>> reduce(Either<Integer, Tree<Token>> tree1,
			Either<Integer, Tree<Token>> tree2, String text, int index) {
		require(tree1.isLeft() || tree2.isLeft(),
				() -> "Ambiguity found at " + Strings.lineAndColumn(text, index) + ":\n" + text);
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
	void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		final Predicate<Parser> checkBraces = p -> parsers.length > 1 && p instanceof Branch
				&& ((Branch) p).getChildCount() > 1;
		Parser.stringify(name, parsers, "\n  | ", " | ", checkBraces, rule, definitions, visited);
	}

	@Override
	public String toString() {
		return (name != null) ? name : Stream.of(parsers)
				.map(p -> p.get().toString())
				.collect(Collectors.joining(" | "));
	}

}
