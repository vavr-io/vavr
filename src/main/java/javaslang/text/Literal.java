/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.Set;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;
import javaslang.lang.Strings;

public class Literal extends Parser {

	final String literal;

	Literal(String literal) {
		require(!Strings.isNullOrEmpty(literal), "literal is null or empty");
		this.literal = literal;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		if (text.startsWith(literal, index)) {
			final Token token = new Token(text, index, index + literal.length());
			return new Right<>(new Tree<Token>("Literal", token));
		} else {
			return new Left<>(index);
		}
	}

	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		rule.append(toString());
	}

	@Override
	public String toString() {
		return "'" + literal + "'";
	}

}
