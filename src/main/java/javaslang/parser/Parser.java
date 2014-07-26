/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import java.util.function.Supplier;

import javaslang.Tuples.Tuple2;
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
	 * @return Either a Left, containing the index of failure or a Right, containing the range
	 *         (index, length) parsed.
	 */
	Either<Integer, Tree<Tuple2<Integer, Integer>>> parse(String text, int index);

	/**
	 * Being a self-supplier is the key for constructing grammars programatically using methods.
	 * Parser implementations which have child parsers, should provide a constructor having
	 * Supplier&lt;Parser&gt; as argument. This allows to build grammars containing cyclic
	 * references:
	 * 
	 * <pre>
	 * <code>class JSONGrammar extends Grammar {
	 * 
	 *     // define start rule
	 *     JSONGrammar() {
	 *         super(JSONGrammar::json);
	 *     }
	 *     
	 *     // json : jsonObject | jsonArray | jsonString | jsonNumber | 'true' | 'false' | 'null' ;
	 *     static Rule json() {
	 *         return new Rule("json",
	 *                 JSONGrammar::jsonObject, // rule reference
	 *                 JSONGrammar::jsonArray,
	 *                 JSONGrammar::jsonString,
	 *                 JSONGrammar::jsonNumber,
	 *                 new Literal("true"),
	 *                 new Literal("false"),
	 *                 new Literal("null"));
	 *     }
	 *     
	 *     // jsonObject : '{' ( pair ( ',' pair )* )? '}' ;
	 *     static Rule jsonObject() {
	 *         return new Rule("jsonObject", new Sequence(
	 *                 new Literal("{"),
	 *                 zeroOrMore(JSONGrammar::pair, ","), // comma separated pairs
	 *                 new Literal("}"));
	 *     }
	 *     
	 *     // pair : jsonString ':' json ;
	 *     static Parser pair() {
	 *         return new Sequence(
	 *                 JSONGrammar::jsonString,
	 *                 new Literal(":"),
	 *                 JSONGrammar::json); // LOOK MA! CYCLIC REFERENCE!
	 *     }
	 *     
	 *     // etc.
	 *     
	 * }</code>
	 * </pre>
	 * 
	 * @return This Parser instance.
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	default Parser get() {
		return this;
	}

}
