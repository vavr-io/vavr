/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.text;

import static javaslang.lang.Lang.require;
import static javaslang.text.Multiplicity.Bounds.ONE_TO_N;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;

import java.util.Set;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.either.Left;
import javaslang.either.Right;

/**
 * Repeats a parser lowerBound (min) to upperBound (max) times.<br>
 * <br>
 * Examples:
 * <ul>
 * <li>X = 1 occurrence (lower bound = upper bound = 1)</li>
 * <li>X? = 0..1 occurrences</li>
 * <li>X* = 0..n occurrences</li>
 * <li>X+ = 1..n occurrences</li>
 * </ul>
 */
public class Multiplicity extends Parser implements Supplier<Multiplicity> {

	final Supplier<? extends Parser> parser;
	final Bounds bounds;

	Multiplicity(Supplier<? extends Parser> parser, Bounds bounds) {
		require(parser != null, "parser is null");
		require(bounds != null, "bounds is null");
		this.parser = parser;
		this.bounds = bounds;
	}

	@Override
	public Either<Integer, Tree<Token>> parse(String text, int index) {
		final Tree<Token> result = new Tree<>(bounds.name(), new Token(text, index, index));
		parseChildren(result, text, index);
		final boolean notMatched = result.getChildren().isEmpty();
		final boolean shouldHaveMatched = ONE_TO_N.equals(bounds);
		if (notMatched && shouldHaveMatched) {
			return new Left<>(index);
		} else {
			return new Right<>(result);
		}
	}
	
	private void parseChildren(Tree<Token> tree, String text, int index) {
		final boolean unbound = !ZERO_TO_ONE.equals(bounds);
		boolean found = true;
		do {
			final Either<Integer, Tree<Token>> child = parser.get().parse(text, tree.getValue().end);
			if (child.isRight()) {
				final Tree<Token> node = child.right().get();
				tree.attach(node);
				tree.getValue().end = node.getValue().end;
			} else {
				found = false;
			}
		} while (unbound && found);
	}

	@Override
	public Multiplicity get() {
		return this;
	}
	
	@Override
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		rule.append("[ ");
		parser.get().stringify(rule, definitions, visited);
		rule.append(" ]").append(bounds.symbol());
	}
	
	static enum Bounds {
		ZERO_TO_ONE, ZERO_TO_N, ONE_TO_N;
		
		String symbol() {
			switch(this) {
				case ZERO_TO_ONE : return "?";
				case ZERO_TO_N : return "*";
				case ONE_TO_N : return "+";
				default : throw new IllegalStateException("Unknown Bounds: " + name());
			}
		}
	}
	
}
