/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import java.util.Set;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;

public class EOF extends Parser {
	
	private static final String NAME = "EOF";
	
	/**
	 * The singleton instance of EOF.
	 */
	private static final EOF INSTANCE = new EOF();

	/**
	 * Hidden constructor.
	 */
	private EOF() {
	}

	/**
	 * Returns the singleton instance of EOF.
	 * 
	 * @return The EOF instance.
	 */
	public static EOF instance() {
		return INSTANCE;
	}
	
	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		if (index == text.length()) {
			return new Right<>(new Tree<>(NAME, new Token(text, index, index)));
		} else {
			return new Left<>(index);
		}
	}
	
	@Override
	void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		rule.append(NAME);
	}

	@Override
	public String toString() {
		return NAME;
	}

}
