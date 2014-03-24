/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
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
