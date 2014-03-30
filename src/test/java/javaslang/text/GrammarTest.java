package javaslang.text;

import static javaslang.text.Multiplicity.Bounds.ZERO_TO_N;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

import javaslang.either.Either;
import javaslang.exception.Try;
import javaslang.io.IO;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void bootstrap() {

		final Grammar jsonGrammar = new JSONGrammar();
		System.out.println(jsonGrammar.stringify());

		final InputStream in = getClass().getResourceAsStream("bootstrap.json");
		final Try<String> json = IO.toString(in, Charset.forName("UTF-8"));
		System.out.println("JSON: " + json.get());

		final Try<Either<Integer, Tree<Token>>> ast = json.map(s -> jsonGrammar.parse(s));
		final String result = ast.map(tree -> tree.toString()).orElse("<no result>");
		System.out.println("AST: " + result);

	}

	// TODO: Whitespace handling!
	// TODO:
	static class JSONGrammar extends Grammar {

		JSONGrammar() {
			super(JSONGrammar::JSON);
		}

		static Parser JSON() {
			return new Sequence("JSON", new Branch(JSONGrammar::JSON_OBJECT, JSONGrammar::JSON_ARRAY,
					JSONGrammar::JSON_NUMBER, JSONGrammar::JSON_BOOLEAN));
		}

		static Parser JSON_OBJECT() {
			return new Sequence("JSON_OBJECT", new Literal("{"), n(PAIR(), ","), new Literal("}"));
		}

		static Parser PAIR() {
			return new Sequence("PAIR", JSONGrammar::KEY, new Literal(":"), JSONGrammar::JSON);
		}

		static Parser KEY() {
			return new Sequence("KEY", new Branch(new Literal("\"a\""), new Literal("\"b\"")));
		}

		static Parser JSON_ARRAY() {
			return new Sequence("JSON_ARRAY", new Literal("["), n(JSON_NUMBER(), ","), new Literal(
					"]"));
		}

		static Parser JSON_NUMBER() {
			return new Sequence("JSON_NUMBER", new Branch(new Literal("1"), new Literal("2"), new Literal("3")));
		}

		static Parser JSON_BOOLEAN() {
			return new Sequence("JSON_BOOLEAN", new Branch(new Literal("true"), new Literal("false")));
		}

		@SuppressWarnings("unchecked")
		static Supplier<? extends Parser> n(Parser parser, String separator) {
			return n((Supplier<? extends Parser>) parser, separator);
		}
		
		/**
		 * n(P, ',') = [ P [ ',' P]* ]?
		 * 
		 * @param parser
		 * @param separator
		 * @return
		 */
		static Supplier<? extends Parser> n(Supplier<? extends Parser> parser, String separator) {

			// [ ',' P]*
			final Supplier<? extends Parser> more = new Multiplicity(new Sequence(new Literal(separator), parser), ZERO_TO_N);

			// [ P <more> ]?
			return new Multiplicity(new Sequence(parser, more), ZERO_TO_ONE);
		}
	}

}
