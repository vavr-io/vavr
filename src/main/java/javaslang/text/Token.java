/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

public class Token {

	final String text;
	int start;
	int end;
	
	Token(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}
	
	public String get() {
		return text.substring(start, end);
	}
	
	@Override
	public String toString() {
		return "Token('" + get() + "')";
	}	

}
