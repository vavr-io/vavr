/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.stream.Collectors.joining;
import static javaslang.Requirements.requireNotInstantiable;

import java.util.stream.Stream;

import javaslang.match.Match;
import javaslang.match.Matchs;
import javaslang.parser.Parsers.Any;
import javaslang.parser.Parsers.CharRange;
import javaslang.parser.Parsers.CharSet;
import javaslang.parser.Parsers.EOF;
import javaslang.parser.Parsers.Literal;
import javaslang.parser.Parsers.Quantifier;
import javaslang.parser.Parsers.Rule;
import javaslang.parser.Parsers.Sequence;

public final class Grammars {

	private static final Match<String> TO_STRING = Matchs
			.caze((Any any) -> ".")
			.caze((CharRange r) -> r.from + ".." + r.to)
			.caze((CharSet s) -> s.charSetString)
			.caze((EOF eof) -> "EOF")
			.caze((Literal l) -> "'" + l.literal + "'")
			.caze((Quantifier q) -> "(" + stringify(q.parser.get()) + ")" + q.bounds.symbol)
			.caze((Rule r) -> Stream
					.of(r.alternatives)
					.map(p -> stringify(p.get()))
					.collect(joining("\n  | ", r.name + "\n  : ", "\n  ;")))
			.caze((Sequence s) -> Stream
					.of(s.parsers)
					.map(p -> stringify(p.get()))
					.collect(joining(" ")))
			.build();

	/**
	 * This class is not intended to be instantiated.
	 */
	private Grammars() {
		requireNotInstantiable();
	}

	public static String stringify(Grammar grammar) {
		// TODO: return TO_STRING.apply(grammar.startRule);
		throw new UnsupportedOperationException();
	}

	private static String stringify(Parser parser) {
		return TO_STRING.apply(parser);
	}

}
