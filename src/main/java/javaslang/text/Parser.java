package javaslang.text;

import javaslang.tree.Tree;

@FunctionalInterface
public interface Parser {

	Tree<Token> parse(String text, int index);
	
}
