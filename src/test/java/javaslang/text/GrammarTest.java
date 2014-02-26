package javaslang.text;

import static javaslang.text.Sequence.Bound.N;
import static javaslang.text.Sequence.Bound.ONE;
import static javaslang.text.Sequence.Bound.ZERO;

import java.io.InputStream;
import java.nio.charset.Charset;

import javaslang.io.IO;
import javaslang.util.Try;
import javaslang.util.tree.Tree;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void bootstrap() {
		// TODO: Grammar.toString
		System.out.println(JSONGrammar.JSON);
		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		final Try<Tree<Token>> ast = json.map(s -> JSONGrammar.JSON.parse(s,  0));
		final String result = ast.map(tree -> tree.toString()).orElse("<no result>");
		System.out.println(result);
	}
	
	static class JSONGrammar {
		
		static final Parser JSON = new Rule("JSON", JSON_OBJECT(), JSON_ARRAY(), JSON_NUMBER(), JSON_BOOLEAN());
		static final Parser JSON_OBJECT() { return new Rule("JSON_OBJECT", new Literal("{"), n(PAIR(), ","), new Literal("}")); }
		static final Parser PAIR() { return new Rule("PAIR", KEY(), new Literal(":"), JSON); }
		static final Parser KEY() { return new Rule("KEY", new Branch(new Literal("a"), new Literal("b"))); }
		static final Parser JSON_ARRAY() { return new Rule("JSON_ARRAY", n(JSON_NUMBER(), ",")); }
		static final Parser JSON_NUMBER() { return new Rule("JSON_NUMBER", new Literal("1"), new Literal("2"), new Literal("3")); }
		static final Parser JSON_BOOLEAN() { return new Rule("JSON_BOOLEAN", new Literal("true"), new Literal("false")); }
		
		static Parser n(Parser parser, String separator) {
			return new Sequence(new Rule("Sequence", parser, new Sequence(new Rule("Sequence", new Literal(separator), parser), ZERO, N)), ZERO, ONE);
		}
	}
	
}
