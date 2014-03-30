/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Arrays.isNullOrEmpty;
import static javaslang.lang.Lang.require;

import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.lang.Arrays;
import javaslang.lang.Strings;

public class Branch extends Parser implements Supplier<Branch> {

	final Supplier<? extends Parser>[] parsers;

	@SafeVarargs
	Branch(Supplier<? extends Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.parsers = parsers;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		final Either<Integer, Tree<Token>> initial = new Left<>(index);
		return Arrays.parallelStream(parsers)
				.map(parser -> {
					return parser.get().parse(text, index);
				})
				.reduce(initial, (tree1, tree2) -> {
					checkAmbiguity(tree1, tree2, text, index);
					return tree1.isRight() ? tree1 : tree2; // tree2 may be an instance of Left<Integer, Tree<Token>>
				}, (t1,t2) -> t1.isRight() ? t1 : t2);
	}
	
	private void checkAmbiguity(Either<Integer, Tree<Token>> tree1, Either<Integer, Tree<Token>> tree2, String text, int index) {
		// TODO: better message / exception does currently not exit the parser
		require(tree1.isLeft() || tree2.isLeft(), () -> "ambiguity found at " + Strings.toString(Strings.lineAndColumn(text, index)) + ":\n" + text);
	}

	@Override
	public Branch get() {
		return this;
	}

	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		boolean separator = false;
		for (Supplier<? extends Parser> parser : parsers) {
			if (separator) {
				rule.append("\n  | ");	
			} else {
				separator = true;
			}
			parser.get().stringify(rule, definitions, visited);
		}
	}
	
}
