/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Requirements.require;
import static javaslang.Requirements.requireNonNull;

import java.util.Objects;

public final class Token {

	private final String id;
	private final String text;
	private final int index;
	private final int length;

	// DEV-NOTE: defer substring calculation to getValue() for better performance and memory footprint
	Token(String id, String text, int index, int length) {
		requireNonNull(text, "text is null");
		require(0 <= index && index <= text.length(), () -> "index out of bounds: " + index);
		require(0 <= length, () -> "negative length: " + length);
		require(length + index <= text.length(),
				() -> String.format("(index + length) exceeds text: (%s + %s)", index, length));
		this.id = id;
		this.text = text;
		this.index = index;
		this.length = length;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public int getStartIndex() {
		return index;
	}

	public int getEndIndex() {
		return index + length;
	}

	public int getLength() {
		return length;
	}

	public String getValue() {
		return text.substring(index, index + length);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Token)) {
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
		return (id != null) ? id : (length > 0) ? "'" + getValue() + "'" : (index == text.length()) ? "<EOF>" : "ε";
	}
}
