/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;
import javaslang.lang.Arrays;

public class Sequence extends Parser {

	// TODO: make whitespace regex configurable
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	Sequence(Supplier<Parser>... parsers) {
		require(!Arrays.isNullOrEmpty(parsers), "no parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	Sequence(String name, Supplier<Parser>... parsers) {
		require(!Arrays.isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		// Starts with an emty root tree and successively attaches parsed children.
		// The whole result is null, if one of the children couldn't been parsed.
		// TODO: Does parallelStream() instead of stream() sense (using StreamSupport and Ordered
		// Splitterator)!?
		final String id = (name == null) ? "<Anonymous>" : name;
		final Either<Integer, Tree<Token>> initial = new Right<>(new Tree<>(id, new Token(text,
				index, index)));
		return Arrays.stream(parsers).reduce(initial, (tree, parser) -> {
			if (tree.isLeft()) {
				// if one parser returned null the sequence does not match and the whole
				// result is null.
				return tree;
			} else {
				try {
					// next parser parses at current index
					final Tree<Token> node = tree.right().get();
					final int lastIndex = node.getValue().end;
					final Matcher matcher = WHITESPACE.matcher(text);
					final int currentIndex = matcher.find(lastIndex) ? matcher.end() : lastIndex;
					final Either<Integer, Tree<Token>> parsed = parser.get().parse(text,
							currentIndex);
					// on success, attach token to tree, else the sequence does not match and the
					// whole
					// result is null
					if (parsed.isRight()) {
						final Tree<Token> child = parsed.right().get();
						// attach child
						node.attach(child);
						// update current index
						node.getValue().end = child.getValue().end;
						return tree;
					} else {
						// parser failed => the whole sequence could not be parsed
						return new Left<>(node.getValue().end);
					}
				} catch (Exception x) {
					// TODO
					x.printStackTrace();
					throw x;
				}
			}
		}, (t1, t2) -> null); // the combiner is not used here because this is no parallel stream
	}

	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		if (name != null) {
			rule.append(name);
			if (!visited.contains(name)) {
				visited.add(name);
				definitions.append(name + "\n  : ");
				final StringBuilder definitionsBuffer = new StringBuilder();
				internalStringify(definitions, definitionsBuffer, visited);
				definitions.append("\n  ;\n\n").append(definitionsBuffer);
			}
		} else {
			internalStringify(rule, definitions, visited);
		}
	}
	
	private void internalStringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		boolean separator = false;
		for (Supplier<? extends Parser> parserSupplier : parsers) {
			final Parser parser = parserSupplier.get();
			if (separator) {
				rule.append(" ");
			} else {
				separator = true;
			}
			parser.stringify(rule, definitions, visited);
		}
	}
}
