/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utils for implementations of {@link Parser}.
 */
class Parsers {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private Parsers() {
		throw new AssertionError(Parsers.class.getName() + " cannot be instantiated.");
	}

	/**
	 * 
	 * @param name
	 * @param parsers
	 * @param separator
	 * @param separatorInline
	 * @param needBraces
	 * @param rule
	 * @param definitions
	 * @param visited
	 */
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

	/**
	 * 
	 * @param parsers
	 * @param separator
	 * @param needBraces
	 * @param rule
	 * @param definitions
	 * @param visited
	 */
	private static void stringify(Supplier<Parser>[] parsers, String separator, Predicate<Parser> needBraces,
			StringBuilder rule, StringBuilder definitions, Set<String> visited) {
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

	/**
	 * 
	 * @param name
	 * @param parsers
	 * @param separator
	 * @return
	 */
	static String toString(String name, Supplier<Parser>[] parsers, String separator) {
		if (name != null) {
			return name;
		} else {
			return Stream.of(parsers)
					.map(p -> p.get().toString())
					.collect(Collectors.joining(separator));
		}
	}

}
