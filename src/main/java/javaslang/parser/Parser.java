/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import java.util.function.Supplier;

import javaslang.collection.Node;
import javaslang.either.Either;

/**
 * The parser interface.
 */
interface Parser extends Supplier<Parser> {

	/**
	 * Trying to parse a text using the current parser, starting at the given index.
	 * 
	 * @param text The whole text to parse.
	 * @param index The current index of the parser.
	 * @param combineResults Indicates, if the scope is lexing (true) or parsing (false). When in lexing mode, cascaded
	 *            rules are combined to one and whitespace is not ignored. When in parsing mode, parse-results of
	 *            cascaded rules are added as children the the actual tree node and whitespace may be ignored.
	 * @return Either a Left, containing the index of failure or a Right, containing the range (index, length) parsed.
	 */
	// TODO: combineResults vs. lexer/parser rules vs. fragments
	Either<Integer, Node<Token>> parse(String text, int index, boolean combineResults);

	/**
	 * Returns a String representation in grammar notation of this parser.
	 * 
	 * @return A String in grammar notation.
	 */
	@Override
	String toString();

	/**
	 * Being a self-supplier is the key for constructing grammars programatically using methods, which are evaluated
	 * lazily. This allows to build grammars containing cyclic references.
	 * <p>
	 * Parser implementations which have child parsers, should provide a constructor having Supplier&lt;Parser&gt; as
	 * argument.
	 * 
	 * @return This Parser instance.
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	default Parser get() {
		return this;
	}
}
