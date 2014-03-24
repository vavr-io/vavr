/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.function.Supplier;

import javaslang.lang.Strings;

class Literal implements Parser, Supplier<Literal> {

	final String literal;

	Literal(String literal) {
		require(!Strings.isNullOrEmpty(literal), "literal is null or empty");
		this.literal = literal;
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		return text.startsWith(literal, index)
				? new Tree<Token>("Literal", new Token(text, index, index + literal.length()))
				: null;
	}

	@Override
	public Literal get() {
		return this;
	}
	
}
