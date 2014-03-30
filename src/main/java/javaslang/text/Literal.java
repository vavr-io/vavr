/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
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
		return text.startsWith(literal, index)
				? new Right<>(new Tree<Token>("Literal", new Token(text, index, index + literal.length())))
				: new Left<>(index);
	}
	
	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		rule.append("'" + literal + "'");
	}
	
}
