package javaslang.text;

import static javaslang.lang.Lang.require;

import java.util.function.Function;
import java.util.function.Supplier;

public class Grammar implements Parser, Supplier<Grammar>, Function<String, Tree<Token>> {
	
	final Parser parser;
	
	public Grammar(Supplier<? extends Parser> parser) {
		require(parser != null, "parser is null");
		this.parser = parser.get();
	}
	
	public Tree<Token> parse(String text) {
		return parse(text, 0);
	}
	
	@Override
	public Tree<Token> apply(String text) {
		return parse(text);
	}
	
	@Override
	public Tree<Token> parse(String text, int index) {
		return parser.parse(text, index);
	}
	
	@Override
	public Grammar get() {
		return this;
	}

}
