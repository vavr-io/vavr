/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import java.util.Set;
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
	 * TODO
	 * 
	 * @param name
	 * @param parsers
	 * @param separator
	 * @param separatorInline
	 * @param withBraces
	 * @param rule
	 * @param definitions
	 * @param visited
	 */
	static void stringify(String name, Supplier<Parser>[] parsers, String separator,
			String separatorInline, boolean withBraces, StringBuilder rule,
			StringBuilder definitions, Set<String> visited) {
		if (name != null) {
			rule.append(name);
			if (!visited.contains(name)) {
				visited.add(name);
				definitions.append(name + "\n  : ");
				final StringBuilder definitionsBuffer = new StringBuilder();
				stringify(parsers, separator, false, definitions, definitionsBuffer, visited);
				definitions.append("\n  ;\n\n").append(definitionsBuffer);
			}
		} else {
			stringify(parsers, separatorInline, withBraces, rule, definitions, visited);
		}
	}

	/**
	 * TODO
	 * 
	 * @param parsers
	 * @param separator
	 * @param withBraces
	 * @param rule
	 * @param definitions
	 * @param visited
	 */
	static void stringify(Supplier<Parser>[] parsers, String separator, boolean withBraces,
			StringBuilder rule, StringBuilder definitions, Set<String> visited) {
		final boolean writeBraces = withBraces && parsers.length > 1;
		if (writeBraces) {
			rule.append("(");
		}
		boolean writeSeparator = false;
		for (Supplier<? extends Parser> parserSupplier : parsers) {
			final Parser parser = parserSupplier.get();
			if (writeSeparator) {
				rule.append(separator);
			} else {
				writeSeparator = true;
			}
			parser.stringify(rule, definitions, visited);
		}
		if (writeBraces) {
			rule.append(")");
		}
	}

	/**
	 * TODO
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
