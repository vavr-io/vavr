/**                       ___.__          .                   ___
 *  ______._____  _______/  /  / ______  / \_   ______.______/__/_____  ______  _______._____
 * /   __/   _  \/   ___/      \/   ___\/  _/__/_____/   ___/  /   ___\/   _  \/   ___/   ___\
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.IO
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javaslang.lang.Arrays;

class Sequence implements Parser {
	
	// TODO: make whitespace regex configurable
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");

	final String name;
	final List<Supplier<Parser>> parsers;

	@SafeVarargs
	Sequence(String name, Supplier<Parser>... parsers) {
		require(!Arrays.isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = Arrays.asList(parsers);
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		// Starts with an emty root tree and successively attaches parsed children.
		// The whole result is null, if one of the children couldn't been parsed.
		// TODO: Does parallelStream() instead of stream() sense (using StreamSupport and Ordered Splitterator)!?
		return parsers.stream().reduce(new Tree<>(name, new Token(text, index, index)), (tree, parser) -> {
			try {
			if (tree == null) {
				// if one parser returned null the sequence does not match and the whole result is null.
				return null;
			} else {
				// next parser parses at current index
				final int lastIndex = tree.getValue().end;
				final Matcher matcher = WHITESPACE.matcher(text);
				final int currentIndex = matcher.find(lastIndex) ? matcher.end() : lastIndex;
				final Tree<Token> parsed = parser.get().parse(text, currentIndex);
				// on success, attach token to tree, else the sequence does not match and the whole result is null
				if (parsed != null) {
					// attach child
					tree.attach(parsed);
					// update current index
					tree.getValue().end = parsed.getValue().end;
					return tree;
				} else {
					// parser failed => the whole sequence could not be parsed
					return null;
				}
			}
			} catch(Exception x) {
				// TODO
				x.printStackTrace();
				throw x;
			}
		}, (t1, t2) -> null); // the combiner is not used here because this is no parallel stream
	}
	
}
