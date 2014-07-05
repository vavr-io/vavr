/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.Lang.requireNotNullOrEmpty;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.either.Either;
import javaslang.either.Right;

public class Sequence extends Parser {

	// TODO: make whitespace regex configurable
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	public Sequence(Supplier<Parser>... parsers) {
		requireNotNullOrEmpty(parsers, "No parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	public Sequence(String name, Supplier<Parser>... parsers) {
		requireNotNullOrEmpty(parsers, "No parsers");
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
	void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		final Predicate<Parser> writeBraces = p -> parsers.length > 1 && p instanceof Branch && ((Branch) p).name == null;
		Parser.stringify(name, parsers, " ", " ", writeBraces, rule, definitions, visited);
	}

	@Override
	public String toString() {		
		return (name != null) ? name : Stream.of(parsers)
				.map(p -> p.get().toString())
				.collect(Collectors.joining(" "));
	}

}
