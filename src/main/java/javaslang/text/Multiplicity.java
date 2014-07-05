/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.Lang.requireNonNull;
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
public class Multiplicity extends Parser {

	final Supplier<Parser> parser;
	final Bounds bounds;

	public Multiplicity(Supplier<Parser> parser, Bounds bounds) {
		requireNonNull(parser, "parser is null");
		requireNonNull(bounds, "bounds is null");
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
			final Either<Integer, Tree<Token>> child = parser.get()
					.parse(text, tree.getValue().end);
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
	int getChildCount() {
		return 1;
	}

	@Override
	void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		final Parser p = parser.get();
		final boolean writeBraces = p.getChildCount() > 1 || p instanceof Multiplicity;
		if (writeBraces) {
			rule.append("(");
		}
		p.stringify(rule, definitions, visited);
		if (writeBraces) {
			rule.append(")");
		}
		rule.append(bounds.symbol());
	}

	@Override
	public String toString() {
		return "[ " + parser.get().toString() + " ]" + bounds.symbol();
	}

	public static enum Bounds {
		ZERO_TO_ONE, ZERO_TO_N, ONE_TO_N;

		String symbol() {
			switch (this) {
				case ZERO_TO_ONE:
					return "?";
				case ZERO_TO_N:
					return "*";
				case ONE_TO_N:
					return "+";
				default:
					throw new IllegalStateException("Unknown Bounds: " + name());
			}
		}
	}

}
