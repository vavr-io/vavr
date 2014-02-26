/**                       ___.__          .                   ___
 *  ______._____  _______/  /  / ______  / \_   ______.______/__/_____  ______  _______._____
 * /   __/   _  \/   ___/      \/   ___\/  _/__/_____/   ___/  /   ___\/   _  \/   ___/   ___\
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.IO
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;
import static javaslang.util.Arrays.isNullOrEmpty;
import javaslang.util.Collections;
import javaslang.util.tree.Tree;

import java.util.Arrays;
import java.util.List;

class Rule implements Parser {

	final String name;
	final List<Parser> parsers;

	Rule(String name, Parser... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.name = name;
		this.parsers = Arrays.asList(parsers);
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		// Starts with an emty root tree and successively attaches parsed children.
		// The whole result is null, if one of the children couldn't been parsed.
		return parsers.stream().reduce(new Tree<>(name), (tree, parser) -> {
			if (tree == null) {
				// if one parser returned null the sequence does not match and the whole result is null.
				return null;
			} else {
				// start to parse at the end-index of the previous parser
				final List<Tree<Token>> children = tree.getChildren();
				// TODO: indices have to be propagated from child nodes to parent nodes (change all grammar classes!)
				// TODO: simple reduce methods do not support null values (change all grammar classes!)  
				final int currentIndex = Collections.lastElement(children).map(child -> child.getValue().end)
						.orElse(index);
				final Tree<Token> parsed = parser.parse(text, currentIndex);
				// on success, attach token to tree, else the sequence does not match and the whole result is null
				if (parsed != null) {
					tree.attach(parsed);
					return tree;
				} else {
					return null;
				}
			}
		}, (t1, t2) -> null); // the combiner is not used here because this is no parallel stream
	}

}
