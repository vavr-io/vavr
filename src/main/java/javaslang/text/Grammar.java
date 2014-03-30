/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;

public class Grammar {
	
	final Supplier<Parser> parser;
	
	public Grammar(Supplier<Parser> parser) {
		require(parser != null, "parser is null");
		this.parser = parser;
	}
	
	public Either<Integer, Tree<Token>> parse(String text) {
		final Either<Integer, Tree<Token>> cst = parser.get().parse(text, 0);
		/*TODO:DELME*/System.out.println("endet at index " + (cst.isRight() ? cst.right().get().getValue().end : cst.left().get()) + " of " + (text.length() - 1));
		return cst;
	}
	
	public String stringify() {
		final StringBuilder rule = new StringBuilder();
		final StringBuilder definitions = new StringBuilder();
		final Set<String> visited = new HashSet<>();
		rule.append("Grammar\n  : ");
		parser.get().stringify(rule, definitions, visited);
		rule.append("\n  ;\n\n");
		return rule.append(definitions).toString();
	}
	
}
