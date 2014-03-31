/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import java.util.Set;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;

/**
 * TODO
 */
public final class EOF extends Parser {
	
	private static final String NAME = "<EOF>";
	
	static final EOF INSTANCE = new EOF();

	private EOF() {
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
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		rule.append(NAME);
	}

	@Override
	public String toString() {
		return NAME;
	}

}
