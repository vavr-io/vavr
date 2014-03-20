package javaslang.text;

import static javaslang.text.Multiplicity.Bound.N;
import static javaslang.text.Multiplicity.Bound.ONE;
import static javaslang.text.Multiplicity.Bound.ZERO;

import java.io.InputStream;
import java.nio.charset.Charset;

import javaslang.exception.Try;
import javaslang.io.IO;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void bootstrap() {
		// TODO: Grammar.toString
		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		final Try<Tree<Token>> ast = json.map(s -> JSONGrammar.JSON.parse(s,  0));
		final String result = ast.map(tree -> tree.toString()).orElse("<no result>");
		System.out.println(result);
	}
	
	static class JSONGrammar {
		
		// TODO: Whitespace handling!
		static final Parser JSON = new Sequence("JSON", JSON_OBJECT(), JSON_ARRAY(), JSON_NUMBER(), JSON_BOOLEAN());
		static final Parser JSON_OBJECT() { return new Sequence("JSON_OBJECT", new Literal("{"), n(PAIR(), ","), new Literal("}")); }
		static final Parser PAIR() { return new Sequence("PAIR", KEY(), new Literal(":"), JSON); }
		static final Parser KEY() { return new Sequence("KEY", new Branch(new Literal("a"), new Literal("b"))); }
		static final Parser JSON_ARRAY() { return new Sequence("JSON_ARRAY", n(JSON_NUMBER(), ",")); }
		static final Parser JSON_NUMBER() { return new Sequence("JSON_NUMBER", new Literal("1"), new Literal("2"), new Literal("3")); }
		static final Parser JSON_BOOLEAN() { return new Sequence("JSON_BOOLEAN", new Literal("true"), new Literal("false")); }
		
		static Parser n(Parser parser, String separator) {
			return new Multiplicity(new Sequence("Sequence", parser, new Multiplicity(new Sequence("Sequence", new Literal(separator), parser), ZERO, N)), ZERO, ONE);
		}
	}
	
}
