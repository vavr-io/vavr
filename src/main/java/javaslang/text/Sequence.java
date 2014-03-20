/**                       ___.__          .                   ___
 *  ______._____  _______/  /  / ______  / \_   ______.______/__/_____  ______  _______._____
 * /   __/   _  \/   ___/      \/   ___\/  _/__/_____/   ___/  /   ___\/   _  \/   ___/   ___\
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.IO
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.List;

import javaslang.lang.Arrays;

class Sequence implements Parser {

	final String name;
	final List<Parser> parsers;

	Sequence(String name, Parser... parsers) {
		require(!Arrays.isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = Arrays.asList(parsers);
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		// Starts with an emty root tree and successively attaches parsed children.
		// The whole result is null, if one of the children couldn't been parsed.
		return parsers.stream().reduce(new Tree<>(name, new Token(text, index, index)), (tree, parser) -> {
			if (tree == null) {
				// if one parser returned null the sequence does not match and the whole result is null.
				return null;
			} else {
				// next parser parses at current index
				final int currentIndex = tree.getValue().end;
				final Tree<Token> parsed = parser.parse(text, currentIndex);
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
		}, (t1, t2) -> null); // the combiner is not used here because this is no parallel stream
	}

}
