package javaslang.text;

import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_N;

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
		System.out.println("JSON: " + json.get());
		final Try<Tree<Token>> ast = json.map(s -> JSONGrammar.JSON.parse(s, 0));
		final String result = ast.map(tree -> tree.toString()).orElse("<no result>");
		System.out.println("AST: " + result);
	}

	static class JSONGrammar {

		// TODO: Whitespace handling!
		static final Parser JSON = new Branch(
				JSONGrammar::JSON_OBJECT,
				JSONGrammar::JSON_ARRAY,
				JSONGrammar::JSON_NUMBER,
				JSONGrammar::JSON_BOOLEAN);

		static final Parser JSON_OBJECT() {
			return new Sequence("JSON_OBJECT",
					() -> new Literal("{"),
					() -> n(PAIR(), ","),
					() -> new Literal("}"));
		}

		static final Parser PAIR() {
			return new Sequence("PAIR",
					JSONGrammar::KEY,
					() -> new Literal(":"),
					() -> JSON);
		}

		static final Parser KEY() {
			return new Branch(
					() -> new Literal("\"a\""),
					() -> new Literal("\"b\""));
		}

		static final Parser JSON_ARRAY() {
			return new Sequence("JSON_ARRAY",
					() -> new Literal("["),
					() -> n(JSON_NUMBER(), ","),
					() -> new Literal("]"));
		}

		static final Parser JSON_NUMBER() {
			return new Branch(
					() -> new Literal("1"),
					() -> new Literal("2"),
					() -> new Literal("3"));
		}

		static final Parser JSON_BOOLEAN() {
			return new Branch(
					() -> new Literal("true"),
					() -> new Literal("false"));
		}

		/**
		 * n(P, ',') = [ P [ ',' P]* ]?
		 * 
		 * @param parser
		 * @param separator
		 * @return
		 */
		static Parser n(Parser parser, String separator) {

			// [ ',' P]*
			final Parser more = new Multiplicity(
					() -> new Sequence("Sequence",
							() -> new Literal(separator),
							() -> parser), ZERO_TO_N);

			// [ P <more> ]?
			return new Multiplicity(() -> new Sequence("Sequence", () -> parser, () -> more), ZERO_TO_ONE);
		}
	}

}
