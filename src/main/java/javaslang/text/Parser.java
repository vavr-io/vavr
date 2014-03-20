package javaslang.text;

@FunctionalInterface
public interface Parser {

	Tree<Token> parse(String text, int index);
	
}
