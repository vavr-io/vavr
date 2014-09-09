/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import java.util.Objects;

public final class Token {

	final String id;
	final int index;
	final int length;

	Token(String id, int index, int length) {
		this.id = id;
		this.index = index;
		this.length = length;
	}

	int endIndex() {
		return index + length;
	}

	String asSubstringOf(String text) {
		return text.substring(index, index + length);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof Token)) {
			return false;
		} else {
			final Token other = (Token) obj;
			return Objects.equals(id, other.id) && index == other.index && length == other.length;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, index, length);
	}

	@Override
	public String toString() {
		return String.format("%s[%s,%s]", (id == null) ? "" : id, index, index + length);
	}
}
