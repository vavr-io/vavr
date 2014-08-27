/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

public class Token {

	final String id;
	final int index;
	int length; // TODO: make this final

	Token(String id, int index, int length) {
		this.id = id;
		this.index = index;
		this.length = length;
	}

	String asSubstringOf(String text) {
		return text.substring(index, index + length);
	}

	@Override
	public String toString() {
		return String.format("%s[%s,%s]", id, index, index + length);
	}
}
