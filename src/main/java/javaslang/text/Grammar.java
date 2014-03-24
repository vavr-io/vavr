/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.function.Function;
import java.util.function.Supplier;

public class Grammar implements Parser, Supplier<Grammar>, Function<String, Tree<Token>> {
	
	final Parser parser;
	
	public Grammar(Supplier<? extends Parser> parser) {
		require(parser != null, "parser is null");
		this.parser = parser.get();
	}
	
	public Tree<Token> parse(String text) {
		return parse(text, 0);
	}
	
	@Override
	public Tree<Token> apply(String text) {
		return parse(text);
	}
	
	@Override
	public Tree<Token> parse(String text, int index) {
		return parser.parse(text, index);
	}
	
	@Override
	public Grammar get() {
		return this;
	}

}
