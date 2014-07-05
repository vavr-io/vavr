/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.either.Either;

/**
 * @see <a href="http://en.wikipedia.org/wiki/Parser_combinator">Parser combinator</a>.<br>
 * @see <a href=
 *      "http://stackoverflow.com/questions/1888854/what-is-the-difference-between-an-abstract-syntax-tree-and-a-concrete-syntax-tre"
 *      >ast vs. cst</a>
 */
public abstract class Parser implements Supplier<Parser> {

	public abstract Either<Integer, Tree<Token>> parse(String text, int index);

	// currently needed for stringify only to decide to write braces
	int getChildCount() {
		return 0;
	}

	abstract void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited);

	@Override
	public final Parser get() {
		return this;
	}

	static void stringify(String name, Supplier<Parser>[] parsers, String separator,
			String separatorInline, Predicate<Parser> needBraces, StringBuilder rule,
			StringBuilder definitions, Set<String> visited) {
		if (name != null) {
			rule.append(name);
			if (!visited.contains(name)) {
				visited.add(name);
				definitions.append(name + "\n  : ");
				final StringBuilder definitionsBuffer = new StringBuilder();
				stringify(parsers, separator, needBraces, definitions, definitionsBuffer, visited);
				definitions.append("\n  ;\n\n").append(definitionsBuffer);
			}
		} else {
			stringify(parsers, separatorInline, needBraces, rule, definitions, visited);
		}
	}

	private static void stringify(Supplier<Parser>[] parsers, String separator,
			Predicate<Parser> needBraces, StringBuilder rule, StringBuilder definitions,
			Set<String> visited) {
		boolean writeSeparator = false;
		for (Supplier<? extends Parser> parserSupplier : parsers) {
			final Parser parser = parserSupplier.get();
			if (writeSeparator) {
				rule.append(separator);
			} else {
				writeSeparator = true;
			}
			final boolean writeBraces = needBraces.test(parser);
			if (writeBraces) {
				rule.append("(");
			}
			parser.stringify(rule, definitions, visited);
			if (writeBraces) {
				rule.append(")");
			}
		}
	}

}
