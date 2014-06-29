/**    / \ ___  _    _  ___   _____ / \ ___   ____  _____
 *    /  //   \/ \  / \/   \ /   _//  //   \ /    \/  _  \   Javaslang
 *  _/  //  -  \  \/  /  -  \\_  \/  //  -  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_/ \_/\____/\_/ \_/____/\___\_/ \_/_/  \_/\___/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javaslang.either.Either;
import javaslang.either.Right;
import javaslang.lang.ArrayExtensions;

public class Sequence extends Parser {

	// TODO: make whitespace regex configurable
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	Sequence(Supplier<Parser>... parsers) {
		require(!ArrayExtensions.isNullOrEmpty(parsers), "no parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	Sequence(String name, Supplier<Parser>... parsers) {
		require(!ArrayExtensions.isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		// Starts with an emty root tree and successively attaches parsed children.
		final String id = (name == null) ? "<Sequence>".intern() : name;
		final Either<Integer, Tree<Token>> initial = new Right<>(new Tree<>(id, new Token(text,
				index, index)));
		return Stream.of(parsers).reduce(initial, (tree, parser) -> {
			if (tree.isLeft()) {
				// first failure returned
				return tree;
			} else {
				// next parser parses at current index
				final Tree<Token> node = tree.right().get();
				final int lastIndex = node.getValue().end;
				final Matcher matcher = WHITESPACE.matcher(text);
				final int currentIndex = matcher.find(lastIndex) ? matcher.end() : lastIndex;
				final Either<Integer, Tree<Token>> parsed = parser.get().parse(text, currentIndex);
				// on success, attach token to tree
				if (parsed.isRight()) {
					final Tree<Token> child = parsed.right().get();
					node.attach(child);
					node.getValue().end = child.getValue().end;
					return tree;
				} else {
					// parser failed => the whole sequence could not be parsed
					return parsed;
				}
			}
		}, (t1, t2) -> null); // the combiner is not used here because this is no parallel stream
	}
	
	@Override
	int getChildCount() {
		return parsers.length;
	}

	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		Parsers.stringify(name, parsers, " ", " ", false, rule, definitions, visited);
	}

	@Override
	public String toString() {
		return Parsers.toString(name, parsers, " ");
	}

}
