package javaslang.text;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class ParserTest {

	@Test
	public void shouldParseLiteralMatch() {
		
		final Literal literal = new Literal("Hi");
		final Tree<Token> ast = literal.parse("Hi there!", 0);
		
		assertThat(ast.toString()).isEqualTo("Literal(Token('Hi'))");
	}
	
	@Test
	public void shouldParseLiteralMatchAtIndex() {
		
		final Literal literal = new Literal("there");
		final Tree<Token> ast = literal.parse("Hi there!", 3);
		
		assertThat(ast.toString()).isEqualTo("Literal(Token('there'))");
	}
	
	@Test
	public void shouldParseLiteralNoMatch() {
		
		final Literal literal = new Literal("hi");
		final Tree<Token> ast = literal.parse("Hi there!", 0);
		
		assertThat(ast).isNull();
	}
	
	@Test
	public void shouldParseSequenceOfLiterals() {
		
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Tree<Token> ast = sequence.parse("ab", 0);
		
		assertThat(ast.toString()).isEqualTo("LETTERS(Token('ab')\n  Literal(Token('a')),\n  Literal(Token('b'))\n)");
	}
	
	@Test
	public void shouldParseSequenceOfLiteralsAtIndex() {
		
		final Sequence sequence = new Sequence("LETTERS", new Literal("a"), new Literal("b"));
		final Tree<Token> ast = sequence.parse(">>ab<<", 2);
		
		assertThat(ast.toString()).isEqualTo("LETTERS(Token('ab')\n  Literal(Token('a')),\n  Literal(Token('b'))\n)");
	}
	
}
