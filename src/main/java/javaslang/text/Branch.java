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

public class Branch extends Parser {

	final String name;
	final Supplier<Parser>[] parsers;

	@SafeVarargs
	Branch(Supplier<Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.name = null;
		this.parsers = parsers;
	}

	@SafeVarargs
	Branch(String name, Supplier<Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.name = name;
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
	protected void stringify(StringBuilder rule, StringBuilder definitions, Set<String> visited) {
//		boolean separator = false;
//		for (Supplier<Parser> parser : parsers) {
//			if (separator) {
//				rule.append("\n  | ");	
//			} else {
//				separator = true;
//			}
//			parser.get().stringify(rule, definitions, visited);
//		}
		if (name != null) {
			rule.append(name);
			if (!visited.contains(name)) {
				visited.add(name);
				definitions.append(name + "\n  : ");
				final StringBuilder definitionsBuffer = new StringBuilder();
				internalStringify(definitions, definitionsBuffer, visited, "", "", "\n  | ");
				definitions.append("\n  ;\n\n").append(definitionsBuffer);
			}
		} else {
			internalStringify(rule, definitions, visited, "(", ")", " | ");
		}
	}

	protected void internalStringify(StringBuilder rule, StringBuilder definitions, Set<String> visited, String prefix, String suffix, String separator) {
		boolean writeBraces = parsers.length > 1;
		boolean writeSeparator = false;
		if (writeBraces) {
			rule.append(prefix);
		}
		for (Supplier<Parser> parser : parsers) {
			if (writeSeparator) {
				rule.append(separator);	
			} else {
				writeSeparator = true;
			}
			parser.get().stringify(rule, definitions, visited);
		}
		if (writeBraces) {
			rule.append(suffix);
		}
	}

}
