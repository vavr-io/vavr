/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static java.util.Collections.emptyList;
import static javaslang.collection.Node.node;
import static javaslang.parser.Parser.Quantifier.UNBOUNDED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javaslang.AssertionsExtensions;
import javaslang.AssertionsExtensions.CheckedRunnable;
import javaslang.Serializables;
import javaslang.collection.Node;
import javaslang.monad.Either;
import javaslang.monad.Left;
import javaslang.monad.Right;
import javaslang.parser.Parser.Any;
import javaslang.parser.Parser.Charset;
import javaslang.parser.Parser.EOF;
import javaslang.parser.Parser.Empty;
import javaslang.parser.Parser.Literal;
import javaslang.parser.Parser.Negation;
import javaslang.parser.Parser.ParseResult;
import javaslang.parser.Parser.Quantifier;
import javaslang.parser.Parser.Range;
import javaslang.parser.Parser.Reference;
import javaslang.parser.Parser.Rule;
import javaslang.parser.Parser.RulePart;
import javaslang.parser.Parser.Sequence;
import javaslang.parser.Parser.Subrule;

import org.junit.Test;

public class ParserTest {

	// -- Any parser

	@Test
	public void shouldConvertAnyToString() {
		assertThat(Any.INSTANCE.toString()).isEqualTo(".");
	}

	@Test
	public void shouldParseCharUsingAny() {
		final String actual = parse(Any.INSTANCE, "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseNonEmptyStringWithNegatedAny() {
		final CheckedRunnable actual = () -> parse(new Negation(Any.INSTANCE), "abc", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseEmptyStringWithNegatedAny() {
		final String actual = parse(new Negation(Any.INSTANCE), "", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldBePureAny() {
		assertThat(Any.INSTANCE.isPure()).isTrue();
	}

	// serialization

	@Test
	public void shouldPreserveSingletonWhenDeserializingAny() {
		final Object any = Serializables.deserialize(Serializables.serialize(Any.INSTANCE));
		assertThat(any == Any.INSTANCE).isTrue();
	}

	// -- Charset parser

	@Test
	public void shouldConvertCharsetToString() {
		assertThat(new Charset("a-z$_A-Z").toString()).isEqualTo("[a-z$_A-Z]");
	}

	@Test
	public void shouldConvertCharsetWithSpecialCharsToString() {
		assertThat(new Charset("\n[']\t\\").toString()).isEqualTo("[\\n\\['\\]\\t\\\\]");
	}

	@Test
	public void shouldConvertCharsetWithUnicodeToString() {
		assertThat(new Charset("©opyright").toString()).isEqualTo("[\\u00a9opyright]");
	}

	@Test
	public void shouldParseCharWithinCharsetWithRange() {
		final String actual = parse(new Charset("a-z"), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinCharsetWithRange() {
		final CheckedRunnable actual = () -> parse(new Charset("a-z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTestingSingleChar() {
		final String actual = parse(new Charset("a-z$_A-Z"), "$", false);
		assertThat(actual).isEqualTo("$");
	}

	@Test
	public void shouldParseCharWithinFullFledgedCharsetTesting2ndRange() {
		final String actual = parse(new Charset("a-z$_A-Z"), "D", false);
		assertThat(actual).isEqualTo("D");
	}

	@Test
	public void shouldNotParseChartNotWithinFullFledgedCharset() {
		final CheckedRunnable actual = () -> parse(new Charset("a-z$_A-Z"), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharInNegationOfCharset() {
		final String actual = parse(new Negation(new Charset("a-y")), "z", false);
		assertThat(actual).isEqualTo("z");
	}

	@Test
	public void shouldNotParseCharNotInNegationOfCharset() {
		final CheckedRunnable actual = () -> parse(new Negation(new Charset("a-y")), "a", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldBePureCharset() {
		assertThat(new Charset("a").isPure()).isTrue();
	}

	// -- Empty parser

	@Test
	public void shouldConvertEmptyToString() {
		assertThat(Empty.INSTANCE.toString()).isEmpty();
	}

	@Test
	public void shouldRecognizeEmpty() {
		final String actual = parse(Empty.INSTANCE, "", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldRecognizeNotEmpty() {
		final String actual = parse(Empty.INSTANCE, "abc", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldNotBePureEmpty() {
		assertThat(Empty.INSTANCE.isPure()).isFalse();
	}

	// serialization

	@Test
	public void shouldPreserveSingletonWhenDeserializingEmpty() {
		final Object empty = Serializables.deserialize(Serializables.serialize(Empty.INSTANCE));
		assertThat(empty == Empty.INSTANCE).isTrue();
	}

	// -- EOF parser

	@Test
	public void shouldConvertEOFToString() {
		assertThat(EOF.INSTANCE.toString()).isEqualTo("EOF");
	}

	@Test
	public void shouldRecognizeEOF() {
		final String actual = parse(EOF.INSTANCE, "", false);
		assertThat(actual).isEqualTo("");
	}

	@Test
	public void shouldRecognizeNotEOF() {
		final CheckedRunnable actual = () -> parse(EOF.INSTANCE, "abc", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseNonEmptyStringWithNegatedEOF() {
		final String actual = parse(new Negation(EOF.INSTANCE), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseEmptyStringWithNegatedAny() {
		final CheckedRunnable actual = () -> parse(new Negation(EOF.INSTANCE), "", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldBePureEOF() {
		assertThat(EOF.INSTANCE.isPure()).isTrue();
	}

	// serialization

	@Test
	public void shouldPreserveSingletonWhenDeserializingEOF() {
		final Object eof = Serializables.deserialize(Serializables.serialize(EOF.INSTANCE));
		assertThat(eof == EOF.INSTANCE).isTrue();
	}

	// -- Literal parser

	@Test
	public void shouldConvertLiteralToString() {
		assertThat(new Literal("v'ger\\").toString()).isEqualTo("'v\\'ger\\\\'");
	}

	@Test
	public void shouldConvertLiteralWithSpecialCharsToString() {
		assertThat(new Literal("\b\t\n\f\r\\[']").toString()).isEqualTo("'\\b\\t\\n\\f\\r\\\\[\\']'");
	}

	@Test
	public void shouldConvertLiteralWithUnicodeToString() {
		assertThat(new Literal("©opyright").toString()).isEqualTo("'\\u00a9opyright'");
	}

	@Test
	public void shouldParseLiteral() {
		final String actual = parse(new Literal("literal"), "literal!", false);
		assertThat(actual).isEqualTo("literal");
	}

	@Test
	public void shouldNotParseLiteralIfNotMatching() {
		final CheckedRunnable actual = () -> parse(new Literal("no match"), "literal!", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldNotBePureLiteral() {
		assertThat(new Literal("test").isPure()).isFalse();
	}

	// -- Negation parser

	@Test
	public void shouldConvertNegationToString() {
		assertThat(new Negation(Any.INSTANCE).toString()).isEqualTo("!.");
	}

	@Test
	public void shouldBeIdentityWhenNegatingTheNegation() {
		final String actual = parse(new Negation(new Negation(Any.INSTANCE)), "a", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldGetChildrenOfNegation() {
		final Negation negation = new Negation(Any.INSTANCE);
		final RulePart[] expected = new RulePart[] { Any.INSTANCE };
		assertThat(negation.getChildren()).isEqualTo(expected);
	}

	@Test
	public void shouldBePureNegationOfPure() {
		assertThat(new Negation(Any.INSTANCE).isPure()).isTrue();
	}

	// -- Quantifier parser

	@Test
	public void shouldConvertQuantifierWithRuleToString() {
		final String actual = new Quantifier(new Reference(() -> new Rule("rule", Any.INSTANCE)), 0, UNBOUNDED)
				.toString();
		assertThat(actual).isEqualTo("rule*");
	}

	@Test
	public void shouldConvertQuantifierWithSequenceToString() {
		final String actual = new Quantifier(new Sequence(Any.INSTANCE, Any.INSTANCE), 0, UNBOUNDED).toString();
		assertThat(actual).isEqualTo("( . . )*");
	}

	@Test
	public void shouldConvertQuantifierWithCustomBoundsToString() {
		final String actual = new Quantifier(new Sequence(Any.INSTANCE, Any.INSTANCE), 1, 13).toString();
		assertThat(actual).isEqualTo("( . . ){1,13}");
	}

	@Test
	public void shouldConvertQuantifierWithSameBoundsToString() {
		final String actual = new Quantifier(new Sequence(Any.INSTANCE, Any.INSTANCE), 13, 13).toString();
		assertThat(actual).isEqualTo("( . . ){13}");
	}

	@Test
	public void shouldBePureQuantifierOfPure() {
		assertThat(new Quantifier(Any.INSTANCE, 0, 1).isPure()).isTrue();
	}

	@Test
	public void shouldNotBePureQuantifierOfNonPure() {
		assertThat(new Quantifier(new Literal("test"), 0, 1).isPure()).isFalse();
	}

	// 0..1

	@Test
	public void shouldConvertZeroToOneQuantifierToString() {
		final String actual = new Quantifier(Any.INSTANCE, 0, 1).toString();
		assertThat(actual).isEqualTo(".?");
	}

	@Test
	public void shouldParseNoneOccurrenceWithZeroToOneQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, 1);
		final Either<Integer, ParseResult> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(parseResult(emptyList(), 0, 0));
	}

	@Test
	public void shouldParseOneOccurrenceWithZeroToOneQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, 1);
		final String text = " ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(parseResult(text, 0, 1));
	}

	// 0..n

	@Test
	public void shouldConvertZeroToNQuantifierToString() {
		final String actual = new Quantifier(Any.INSTANCE, 0, UNBOUNDED).toString();
		assertThat(actual).isEqualTo(".*");
	}

	@Test
	public void shouldParseNoneOccurrenceWithZeroToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, UNBOUNDED);
		final Either<Integer, ParseResult> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(parseResult(emptyList(), 0, 0));
	}

	@Test
	public void shouldParseOneOccurrenceWithZeroToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, UNBOUNDED);
		final String text = " ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(parseResult(text, 0, 1));
	}

	@Test
	public void shouldParseTwoOccurrencesOfSpaceWhenCombiningSpacesWithZeroToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, UNBOUNDED);
		final String text = "  ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(parseResult(text, 0, 2));
	}

	@Test
	public void shouldParseEmptyTokenWhenAggregatingSpacesWithZeroToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 0, UNBOUNDED);
		final String text = "  ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, false);
		assertThat(actual).isEqualTo(parseResult(text, 0, 2));
	}

	// 1..n

	@Test
	public void shouldConvertOneToNQuantifierToString() {
		final String actual = new Quantifier(Any.INSTANCE, 1, UNBOUNDED).toString();
		assertThat(actual).isEqualTo(".+");
	}

	@Test
	public void shouldParseNoneOccurrenceWithOneToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 1, UNBOUNDED);
		final Either<Integer, ParseResult> actual = parser.parse("", 0, true);
		assertThat(actual).isEqualTo(new Left<>(0));
	}

	@Test
	public void shouldParseOneOccurrenceWithOneToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 1, UNBOUNDED);
		final String text = " ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(parseResult(text, 0, 1));
	}

	@Test
	public void shouldParseTwoOccurrencesOfSpaceWhenCombiningSpacesWithOneToNQuantifier() {
		final Parser parser = new Quantifier(Any.INSTANCE, 1, UNBOUNDED);
		final String text = "  ";
		final Either<Integer, ParseResult> actual = parser.parse(text, 0, true);
		assertThat(actual).isEqualTo(parseResult(text, 0, 2));
	}

	// -- Range parser

	@Test
	public void shouldConvertRangeToString() {
		assertThat(new Range('a', 'z').toString()).isEqualTo("'a'..'z'");
	}

	@Test
	public void shouldConvertRangeWithSpecialCharsToString() {
		assertThat(new Range('\b', '\'').toString()).isEqualTo("'\\b'..'\\''");
	}

	@Test
	public void shouldParseCharWithinRange() {
		final String actual = parse(new Range('a', 'z'), "abc", false);
		assertThat(actual).isEqualTo("a");
	}

	@Test
	public void shouldNotParseCharNotWithinRange() {
		final CheckedRunnable actual = () -> parse(new Range('a', 'z'), "@@@", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldParseCharInNegationOfRange() {
		final String actual = parse(new Negation(new Range('a', 'y')), "z", false);
		assertThat(actual).isEqualTo("z");
	}

	@Test
	public void shouldNotParseCharNotInNegationOfRange() {
		final CheckedRunnable actual = () -> parse(new Negation(new Range('a', 'y')), "a", false);
		AssertionsExtensions.assertThat(actual).isThrowing(AssertionError.class, "no match at index 0");
	}

	@Test
	public void shouldBePureRange() {
		assertThat(new Range('a', 'z').isPure()).isTrue();
	}

	// -- Reference parser

	@Test
	public void shouldConvertRuleRefToString() {
		assertThat(new Reference(() -> expr()).toString()).isEqualTo("expr");
	}

	@Test
	public void shouldNotBePureReference() {
		assertThat(new Reference(() -> expr()).isPure()).isFalse();
	}

	// -- Rule parser

	@Test
	public void shouldConvertRuleToString() {
		assertThat(expr().toString()).isEqualTo("expr : expr '+' expr\n     | expr '*' expr\n     | INT\n     ;");
	}

	// DEV-NOTE: recursive self-reference of the rule
	static Rule expr() {
		return new Rule("expr", new Sequence(new Reference(ParserTest::expr), new Literal("+"), new Reference(
				ParserTest::expr)), new Sequence(new Reference(ParserTest::expr), new Literal("*"), new Reference(
				ParserTest::expr)), new Reference(() -> new Rule("INT", new Quantifier(new Range('0', '9'), 1,
				UNBOUNDED))));
	}

	@Test
	public void shouldParsePureRuleParts() {
		// rule : 'Hello' [a-zA-Z]+ '!'
		final Rule rule = new Rule("rule", new Sequence(new Literal("Hello"), new Quantifier(new Charset("a-zA-Z"), 1,
				UNBOUNDED), new Literal("!")));
		final String actual = rule.parse("Hello Daniel!", 0, false).toString();
		assertThat(actual).isEqualTo("Right([Node(rule 'Hello' 'Daniel' '!')])");
	}

	@Test
	public void shouldParsePureRulePartsWithoutSkippingWhitespace() {
		// rule : 'Hello' [a-zA-Z]+ '!'
		final Rule rule = new Rule("rule", new Sequence(new Literal("Hello"), new Quantifier(new Charset("a-zA-Z"), 1,
				UNBOUNDED), new Literal("!")));
		final String actual = rule.parse("Hello D a n i e l!", 0, false).toString();
		assertThat(actual).isEqualTo("Left(8)"); // no viable alternatve at space ' ' behind 'D'
	}

	@Test
	public void shouldParseNonPureLiteral() {
		// rule : 'test'+
		final Rule rule = new Rule("rule", new Sequence(new Quantifier(new Literal("test"), 1, UNBOUNDED)));
		final String actual = rule.parse("test test test", 0, false).toString();
		assertThat(actual).isEqualTo("Right([Node(rule 'test' 'test' 'test')])");
	}

	@Test
	public void shouldNotBePureRule() {
		assertThat(expr().isPure()).isFalse();
	}

	// Rule.equals

	@Test
	public void shouldRuleEqualSameObject() {
		final Rule rule = expr();
		assertThat(rule.equals(rule)).isTrue();
	}

	@Test
	public void shouldRuleNotEqualNull() {
		final Rule rule = expr();
		assertThat(rule.equals(null)).isFalse();
	}

	@Test
	public void shouldRuleNotEqualObjectOfDifferentType() {
		final Rule rule = expr();
		assertThat(rule.equals(new Object())).isFalse();
	}

	@Test
	public void shouldRuleEqualDiffernetObject() {
		final Rule rule1 = expr();
		final Rule rule2 = expr();
		assertThat(rule1.equals(rule2)).isTrue();
	}

	// Rule.hashCode

	@Test
	public void shouldRuleHashAsExpected() {
		final Rule rule = expr();
		assertThat(rule.hashCode()).isEqualTo(rule.name.hashCode());
	}

	// -- Sequence parser

	@Test
	public void shouldParseTextUsingSequence() {
		final RulePart[] parsers = new RulePart[] { new Literal("one"), new Literal("two"), new Literal("three") };
		final String actual = parse(new Sequence(parsers), "onetwothree...", false);
		assertThat(actual).isEqualTo("one two three");
	}

	@Test
	public void shouldParseSequenceOfTwoParsersIfFirstParserReturnsEmptyResult() {
		final RulePart[] parsers = new RulePart[] { new Quantifier(new Literal("a"), 0, 1), new Literal("b") };
		final String actual = parse(new Sequence(parsers), "b", false);
		assertThat(actual).isEqualTo("b");
	}

	@Test
	public void shouldParseSequenceOfThreeParsersIfSecondParserReturnsEmptyResult() {
		final RulePart[] parsers = new RulePart[] {
				new Literal("a"),
				new Quantifier(new Literal("b"), 0, 1),
				new Literal("c") };
		final String actual = parse(new Sequence(parsers), "ac", false);
		assertThat(actual).isEqualTo("a c"); // parsed two separate tokens
	}

	@Test
	public void shouldLexSequenceOfThreeParsersIfSecondParserReturnsEmptyResult() {
		final RulePart[] parsers = new RulePart[] {
				new Literal("a"),
				new Quantifier(new Literal("b"), 0, 1),
				new Literal("c") };
		final String actual = parse(new Sequence(parsers), "ac", true);
		assertThat(actual).isEqualTo("ac"); // lexer combined tokens
	}

	@Test
	public void shouldNotBePureSequence() {
		assertThat(new Sequence(Any.INSTANCE).isPure()).isFalse();
	}

	// -- Subrule parser

	@Test
	public void shouldConvertSubruleToString() {
		final Subrule subrule = new Subrule(Any.INSTANCE, EOF.INSTANCE);
		assertThat(subrule.toString()).isEqualTo("( . | EOF )");
	}

	@Test
	public void shouldGetChildrenOfSubrule() {
		final Subrule subrule = new Subrule(Any.INSTANCE, EOF.INSTANCE);
		final RulePart[] expected = new RulePart[] { Any.INSTANCE, EOF.INSTANCE };
		assertThat(subrule.getChildren()).isEqualTo(expected);
	}

	@Test
	public void shouldParseFirstAlternativeUsingSubrule() {
		final Subrule subrule = new Subrule(Any.INSTANCE, EOF.INSTANCE);
		assertThat(subrule.parse("a", 0, false).toString()).isEqualTo("Right([Node('a')])");
	}

	@Test
	public void shouldParseSecondAlternativeUsingSubrule() {
		final Subrule subrule = new Subrule(Any.INSTANCE, EOF.INSTANCE);
		assertThat(subrule.parse("", 0, false).toString()).isEqualTo("Right([Node(<EOF>)])");
	}

	@Test
	public void shouldParseNoMatchUsingSubrule() {
		final Subrule subrule = new Subrule(new Literal("a"), new Literal("b"));
		assertThat(subrule.parse("c", 0, false).toString()).isEqualTo("Left(0)");
	}

	@Test
	public void shouldNotBePureSubrule() {
		assertThat(new Subrule(Any.INSTANCE, EOF.INSTANCE).isPure()).isFalse();
	}

	// -- ParseResult

	@Test
	public void shouldConvertParseResultToString() {
		assertThat(new ParseResult(Collections.emptyList(), 0, 0, false).toString()).isEqualTo("[]");
	}

	// ParseResult.equals

	@Test
	public void shouldParseResultEqualSameObject() {
		final ParseResult parseResult = new ParseResult(Collections.emptyList(), 0, 0, false);
		assertThat(parseResult.equals(parseResult)).isTrue();
	}

	@Test
	public void shouldParseResultNotEqualNull() {
		final ParseResult parseResult = new ParseResult(Collections.emptyList(), 0, 0, false);
		assertThat(parseResult.equals(null)).isFalse();
	}

	@Test
	public void shouldParseResultNotEqualObjectOfDifferentType() {
		final ParseResult parseResult = new ParseResult(Collections.emptyList(), 0, 0, false);
		assertThat(parseResult.equals(new Object())).isFalse();
	}

	@Test
	public void shouldParseResultEqualDiffernetObject() {
		final ParseResult parseResult1 = new ParseResult(Collections.emptyList(), 0, 0, false);
		final ParseResult parseResult2 = new ParseResult(Collections.emptyList(), 0, 0, false);
		assertThat(parseResult1.equals(parseResult2)).isTrue();
	}

	// ParseResult.hashCode

	@Test
	public void shouldParseResultHashAsExpected() {
		final ParseResult parseResult = new ParseResult(Collections.emptyList(), 0, 0, false);
		assertThat(parseResult.hashCode()).isEqualTo(
				Objects.hash(parseResult.startIndex, parseResult.endIndex, parseResult.tokens));
	}

	// -- parse helpers

	private static String parse(Parser parser, String text, boolean lexer) {
		return parser.parse(text, 0, lexer).orElseThrow(i -> new AssertionError("no match at index " + i)).tokens
				.stream()
				.map(node -> node.getValue().getValue())
				.collect(Collectors.joining(" "));
	}

	private static Either<Integer, ParseResult> parseResult(List<Node<Token>> tokens, int startIndex, int endIndex) {
		return new Right<>(new ParseResult(tokens, startIndex, endIndex, false));
	}

	private static Either<Integer, ParseResult> parseResult(String text, int startIndex, int endIndex) {
		return parseResult(Arrays.asList(node(new Token(null, text, startIndex, endIndex))), startIndex, endIndex);
	}
}
