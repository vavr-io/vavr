/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;

/**
 * @see <a href="http://en.wikipedia.org/wiki/Parser_combinator">Parser combinator</a>
 * @see <a
 *      href="http://stackoverflow.com/questions/1888854/what-is-the-difference-between-an-abstract-syntax-tree-and-a-concrete-syntax-tre">ast
 *      vs. cst</a>
 */
public abstract class Parser implements Supplier<Parser> {

	public abstract Either<Integer, Tree<Token>> parse(String text, int index);
	
	protected abstract void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited);
	
	@Override
	public Parser get() {
		return this;
	}

}
