/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Arrays.isNullOrEmpty;
import static javaslang.lang.Lang.require;

import java.util.function.Supplier;

import javaslang.lang.Arrays;
import javaslang.lang.Strings;

class Branch implements Parser, Supplier<Branch> {

	final Supplier<? extends Parser>[] parsers;

	@SafeVarargs
	Branch(Supplier<? extends Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.parsers = parsers;
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		return Arrays.parallelStream(parsers)
				.map(parser -> {
					return parser.get().parse(text, index);
				})
				.reduce(null, (tree1, tree2) -> {
					checkAmbiguity(tree1, tree2, text, index);
					return (tree1 != null) ? tree1 : tree2; // may be null
				}, (t1,t2) -> (t1 != null) ? t1 : t2);
	}
	
	private void checkAmbiguity(Tree<Token> tree1, Tree<Token> tree2, String text, int index) {
		// TODO: better message
		require(tree1 == null || tree2 == null, () -> "ambiguity found at " + Strings.toString(Strings.lineAndColumn(text, index)) + ":\n" + text);
	}

	@Override
	public Branch get() {
		return this;
	}

}
