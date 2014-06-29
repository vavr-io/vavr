/**    / \ ___  _    _  ___   _____ / \ ___   ____  _____
 *    /  //   \/ \  / \/   \ /   _//  //   \ /    \/  _  \   Javaslang
 *  _/  //  -  \  \/  /  -  \\_  \/  //  -  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_/ \_/\____/\_/ \_/____/\___\_/ \_/_/  \_/\___/    Licensed under the Apache License, Version 2.0
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
