/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.text;

import static javaslang.text.Multiplicity.Bounds.ZERO_TO_N;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;
import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.Lang.UnsatisfiedRequirementException;
import javaslang.either.Either;

import org.junit.Test;

public class ParserTest {

	@Test(expected = UnsatisfiedRequirementException.class)
	public void shouldThrowOnEmptyLiteral() {
		new Literal("");
	}

	@Test
	public void shouldParseLiteralMatch() {
		final Literal literal = new Literal("Hi");
		final Either<Integer, Tree<Token>> ast = literal.parse("Hi there!", 0);
		assertThat(ast.right().get().toString()).isEqualTo("Literal(Token('Hi'))");
	}

	@Test
	public void shouldParseLiteralMatchAtIndex() {
		final Literal literal = new Literal("there");
		final Either<Integer, Tree<Token>> ast = literal.parse("Hi there!", 3);
		assertThat(ast.right().get().toString()).isEqualTo("Literal(Token('there'))");
	}

	@Test
	public void shouldParseLiteralNoMatch() {
		final Literal literal = new Literal("hi");
		final Either<Integer, Tree<Token>> ast = literal.parse("xxx", 0);
		assertThat(ast.left().get()).isEqualTo(0);
	}

	@Test
	public void shouldNotParseLiteralWhenFacingWhitespace() {
		final Literal literal = new Literal("hi");
		final Either<Integer, Tree<Token>> ast = literal.parse("  hi!", 0);
		assertThat(ast.left().get()).isEqualTo(0);
	}

	@Test(expected = UnsatisfiedRequirementException.class)
	public void shouldThrowOnEmptySequenceOfLiterals() {
		new Sequence("EMPTY");
	}

	@Test
	public void shouldParseSequenceOfLiterals() {
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = sequence.parse("ab", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"LETTERS(Token('ab')\n  Literal(Token('a')),\n  Literal(Token('b'))\n)");
	}

	@Test
	public void shouldParseSequenceOfLiteralsAtIndex() {
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = sequence.parse(">>ab<<", 2);
		assertThat(ast.right().get().toString()).isEqualTo(
				"LETTERS(Token('ab')\n  Literal(Token('a')),\n  Literal(Token('b'))\n)");
	}

	@Test
	public void shouldReturnNullOnNoSequenceMatch() {
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = sequence.parse("xxx", 0);
		assertThat(ast.left().get()).isEqualTo(0);
	}

	@Test
	public void shouldParseSequenceIgnoringWhitespace() {
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = sequence.parse("  a\nb.", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"LETTERS(Token(' a b')\n  Literal(Token('a')),\n  Literal(Token('b'))\n)");
	}

	@Test
	public void shouldParseFirstBranch() {
		final Branch branch = new Branch(new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = branch.parse("a", 0);
		assertThat(ast.right().get().toString()).isEqualTo("Literal(Token('a'))");
	}

	@Test
	public void shouldParseSecondBranch() {
		final Branch branch = new Branch(new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = branch.parse("b", 0);
		assertThat(ast.right().get().toString()).isEqualTo("Literal(Token('b'))");
	}

	@Test
	public void shouldReturnNullOnNoBranchMatch() {
		final Branch branch = new Branch(new Literal("a"), new Literal("b"));
		final Either<Integer, Tree<Token>> ast = branch.parse("xxx", 0);
		assertThat(ast.left().get()).isEqualTo(0);
	}

	@Test
	public void shouldParseZeroToOneWhenMatchingNone() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ZERO_TO_ONE);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("xxx", 0);
		assertThat(ast.right().get().toString()).isEqualTo("ZERO_TO_ONE(Token(''))");
	}

	@Test
	public void shouldParseZeroToOneWhenMatchingOne() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ZERO_TO_ONE);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("aaa", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"ZERO_TO_ONE(Token('a')\n  Literal(Token('a'))\n)");
	}

	@Test
	public void shouldParseZeroToNWhenMatchingNone() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ZERO_TO_N);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("xxx", 0);
		assertThat(ast.right().get().toString()).isEqualTo("ZERO_TO_N(Token(''))");
	}

	@Test
	public void shouldParseZeroToNWhenMatchingMoreThanOne() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ZERO_TO_N);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("aaa", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"ZERO_TO_N(Token('aaa')\n  Literal(Token('a')),\n  Literal(Token('a')),\n  Literal(Token('a'))\n)");
	}

	@Test
	public void shouldNotParseOneToNWhenMatchingNone() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ONE_TO_N);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("xxx", 0);
		assertThat(ast.left().get()).isEqualTo(0);
	}

	@Test
	public void shouldParseOneToNWhenMatchingOne() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ONE_TO_N);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("abc", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"ONE_TO_N(Token('a')\n  Literal(Token('a'))\n)");
	}

	@Test
	public void shouldParseOneToNWhenMatchingMoreThanOne() {
		final Multiplicity multiplicity = new Multiplicity(new Literal("a"),
				Multiplicity.Bounds.ONE_TO_N);
		final Either<Integer, Tree<Token>> ast = multiplicity.parse("aaa", 0);
		assertThat(ast.right().get().toString()).isEqualTo(
				"ONE_TO_N(Token('aaa')\n  Literal(Token('a')),\n  Literal(Token('a')),\n  Literal(Token('a'))\n)");
	}

	@Test
	public void shouldFindCommaSeparatedLiterals() {
		final Parser parser = n(new Literal("a"), ",");
		final Either<Integer, Tree<Token>> ast = parser.parse("a, a, a", 0);
		assertThat(ast.right().get().getValue().get()).isEqualTo("a, a, a");
	}

	/**
	 * n(P, ',') = [ P [ ',' P]* ]?
	 * 
	 * @param parser
	 * @param separator
	 * @return
	 */
	private Parser n(Parser parser, String separator) {

		// [ ',' P]*
		final Parser more = new Multiplicity(new Sequence("Sequence", new Literal(separator),
				parser), ZERO_TO_N);

		// [ P <more> ]?
		return new Multiplicity(new Sequence("Sequence", parser, more), ZERO_TO_ONE);
	}

}
