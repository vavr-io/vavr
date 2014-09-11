/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.Collections.emptyList;
import static javaslang.collection.Node.node;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javaslang.AssertionsExtensions;
import javaslang.AssertionsExtensions.CheckedRunnable;
import javaslang.collection.Node;
import javaslang.monad.Either;
import javaslang.monad.Left;
import javaslang.monad.Right;
import javaslang.parser.Parser.Quantifier;
import javaslang.parser.Parser.Rule;

import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

	// -- Any parser

	@Test
	public void shouldConvertAnyToString() {
		assertThat(Parser.Any.INSTANCE.toString()).isEqualTo(".");
	}

	@Test
	public void shouldParseCharUsingAny() {
		final String actual = parse(Parser.Any.INSTANCE, "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	// -- Charset parser

	@Test
	public void shouldConvertCharsetToString() {
		assertThat(new Parser.Charset("a-z$_A-Z").toString()).isEqualTo("[a-z$_A-Z]");
	}

	@Test
	public void shouldParseCharWithinCharsetWithRange() {
		final String actual = parse(new Parser.Charset("a-z"), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinCharsetWithRange() {
		final CheckedRunnable actual = () -> parse(new Parser.Charset("a-z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTestingSingleChar() {
		final String actual = parse(new Parser.Charset("a-z$_A-Z"), "$", false);
		assertThat(actual).isEqualTo("$");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTesting2ndRange() {
		final String actual = parse(new Parser.Charset("a-z$_A-Z"), "D", false);
		assertThat(actual).isEqualTo("D");
	}

	@Test
	public void shouldNotParseChartNotWithinFullFledgedCharset() {
		final CheckedRunnable actual = () -> parse(new Parser.Charset("a-z$_A-Z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- EOF parser

	@Test
	public void shouldConvertEOFToString() {
		assertThat(Parser.EOF.INSTANCE.toString()).isEqualTo("EOF");
	}

	@Test
	public void shouldRecognizeEOF() {
		final String actual = parse(Parser.EOF.INSTANCE, "", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldRecognizeNotEOF() {
		final CheckedRunnable actual = () -> parse(Parser.EOF.INSTANCE, "abc", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Literal parser

	@Test
	public void shouldConvertLiteralToString() {
		assertThat(new Parser.Literal("v'ger\\").toString()).isEqualTo("'v\\'ger\\\\'");
	}

	@Test
	public void shouldParseLiteral() {
		final String actual = parse(new Parser.Literal("literal"), "literal!", false);
		assertThat(actual).isEqualTo("literal");
	}

	@Test
	public void shouldNotParseLiteralIfNotMatching() {
		final CheckedRunnable actual = () -> parse(new Parser.Literal("no match"), "literal!", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Quantifier parser

	@Test
	public void shouldConvertQuantifierWithRuleToString() {
		final String actual = new Parser.Quantifier(new Rule("rule", Parser.Any.INSTANCE), Quantifier.Bounds.ZERO_TO_N)
				.toString();
		assertThat(actual).isEqualTo("rule*");
	}

	@Test
	public void shouldConvertQuantifierWithSequenceToString() {
		final String actual = new Parser.Quantifier(new Parser.Sequence(Parser.Any.INSTANCE, Parser.Any.INSTANCE),
				Quantifier.Bounds.ZERO_TO_N).toString();
		assertThat(actual).isEqualTo("( . . )*");
	}

	// 0..1

	@Test
	public void shouldConvertZeroToOneQuantifierToString() {
		final String actual = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_ONE).toString();
		assertThat(actual).isEqualTo(".?");
	}

	@Test
	public void shouldParseNoneOccurrenceWithZeroToOneQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_ONE);
		final Either<Integer, List<Node<Token>>> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(new Right<>(emptyList()));
	}

	@Test
	public void shouldParseOneOccurrenceWithZeroToOneQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_ONE);
		final String text = " ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(new Right<>(Arrays.asList(node(new Token(null, text, 0, 1)))));
	}

	// 0..n

	@Test
	public void shouldConvertZeroToNQuantifierToString() {
		final String actual = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_N).toString();
		assertThat(actual).isEqualTo(".*");
	}

	@Test
	public void shouldParseNoneOccurrenceWithZeroToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_N);
		final Either<Integer, List<Node<Token>>> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(new Right<>(emptyList()));
	}

	@Test
	public void shouldParseOneOccurrenceWithZeroToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_N);
		final String text = " ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(new Right<>(Arrays.asList(node(new Token(null, text, 0, 1)))));
	}

	@Test
	public void shouldParseTwoOccurrencesAsCombinedWithZeroToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_N);
		final String text = "  ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(new Right<>(Arrays.asList(node(new Token(null, text, 0, 2)))));
	}

	@Test
	public void shouldParseTwoOccurrencesAsAggregatedWithZeroToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ZERO_TO_N);
		final String text = "  ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, false);
		assertThat(actual).isEqualTo(
				new Right<>(Arrays.asList(node(new Token(null, text, 0, 1)), node(new Token(null, text, 1, 1)))));
	}

	// 1..n

	@Test
	public void shouldConvertOneToNQuantifierToString() {
		final String actual = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ONE_TO_N).toString();
		assertThat(actual).isEqualTo(".+");
	}

	@Test
	public void shouldParseNoneOccurrenceWithOneToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ONE_TO_N);
		final Either<Integer, List<Node<Token>>> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(new Left<>(0));
	}

	@Test
	public void shouldParseOneOccurrenceWithOneToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ONE_TO_N);
		final String text = " ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(new Right<>(Arrays.asList(node(new Token(null, text, 0, 1)))));
	}

	@Test
	public void shouldParseTwoOccurrencesAsCombinedWithOneToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ONE_TO_N);
		final String text = "  ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(new Right<>(Arrays.asList(node(new Token(null, text, 0, 2)))));
	}

	@Test
	public void shouldParseTwoOccurrencesAsAggregatedWithOneToNQuantifier() {
		final Parser parser = new Parser.Quantifier(Parser.Any.INSTANCE, Quantifier.Bounds.ONE_TO_N);
		final String text = "  ";
		final Either<Integer, List<Node<Token>>> actual = parser.parse(text, 0, false);
		assertThat(actual).isEqualTo(
				new Right<>(Arrays.asList(node(new Token(null, text, 0, 1)), node(new Token(null, text, 1, 1)))));
	}

	// -- Range parser

	@Test
	public void shouldConvertRangeToString() {
		assertThat(new Parser.Range('a', 'z').toString()).isEqualTo("'a'..'z'");
	}

	@Test
	public void shouldParseCharWithinRange() {
		final String actual = parse(new Parser.Range('a', 'z'), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinRange() {
		final CheckedRunnable actual = () -> parse(new Parser.Range('a', 'z'), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	// -- Rule parser

	@Test
	public void shouldConvertRuleToString() {
		assertThat(expr().toString()).isEqualTo("expr : expr '+' expr\n     | expr '*' expr\n     | INT\n     ;");
	}

	// DEV-NOTE: recursive self-reference of the rule
	static Parser.Rule expr() {
		return new Parser.Rule("expr",
				new Parser.Sequence(ParserTest::expr, new Parser.Literal("+"), ParserTest::expr), new Parser.Sequence(
						ParserTest::expr, new Parser.Literal("*"), ParserTest::expr),
				new Parser.Rule("INT", () -> null));
	}

	@Test
	@Ignore
	public void shouldParseTextUsingRule() {
		// TODO
		fail("not implemented");
	}

	@Test
	@Ignore
	public void shouldParseTextUsingFirstMatchingRule() {
		// TODO
		fail("not implemented");
	}

	// -- Sequence parser

	@Test
	public void shouldParseTextUsingSequence() {
		final Parser[] parsers = new Parser[] {
				new Parser.Literal("one"),
				new Parser.Literal("two"),
				new Parser.Literal("three") };
		final String actual = parse(new Parser.Sequence(parsers), "one two three...", false);
		assertThat(actual).isEqualTo("one two three");
	}

	// -- Subrule parser

	// TODO

	// -- Token

	@Test
	public void shouldConvertTokenToString() {
		assertThat(new Token("id", "", 0, 0).toString()).isEqualTo("id");
	}

	// -- parse helpers

	private String parse(Parser parser, String text, boolean lexer) {
		return parser
				.parse(text, 0, lexer)
				.orElseThrow(i -> new AssertionError("no match at index " + i))
				.stream()
				.map(node -> node.getValue().value())
				.collect(Collectors.joining(" "));
	}
}
