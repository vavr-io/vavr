/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;

/**
 * @see <a href="http://en.wikipedia.org/wiki/Parser_combinator">Parser combinator</a>
 * @see <a
 *      href="http://stackoverflow.com/questions/1888854/what-is-the-difference-between-an-abstract-syntax-tree-and-a-concrete-syntax-tre">ast
 *      vs. cst</a>
 */
public abstract class Parser implements Supplier<Parser> {

	public abstract Either<Integer, Tree<Token>> parse(String text, int index);
	
	int getChildCount() {
		return 0;
	}
	protected abstract void stringify(StringBuilder rule, StringBuilder definitions,
			Set<String> visited);

	@Override
	public Parser get() {
		return this;
	}

}
