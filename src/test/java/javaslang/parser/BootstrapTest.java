/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.IO.UTF8;
import javaslang.IO;
import javaslang.collection.Tree;
import javaslang.monad.Try;
import javaslang.parser.Parser.Rule;

import org.junit.Ignore;
import org.junit.Test;

public class BootstrapTest {

	@Test
	@Ignore
	// TODO(#33): FIXME
	public void shouldBootstrapJavaslangGrammar() {
		final Grammar grammar = new JavaslangGrammar();
		/* TODO:DEBUG */System.out.println(grammar);
		IO.loadResource("javaslang/parser/Javaslang.grammar", UTF8).forEach(input -> {
			///* TODO:DEBUG */System.out.println(input);
				final Try<Tree<Token>> parseTree = grammar.parse(input);
				/* TODO:DEBUG */System.out.println(parseTree.get().toCoffeeScriptString());
			});
	}

	static class JavaslangGrammar extends Grammar {

		JavaslangGrammar() {
			super("Javaslang");
		}

		@Override
		protected Rule getStartRule() {
			return grammar();
		}

		// grammar : 'grammar' ID ';'? rule+ EOF
		Rule grammar() {
			return rule("grammar", seq(str("grammar"), ref(this::ID), _0_1(str(";")), _1_n(ref(this::rule)), EOF));
		}

		// rule : ID ':' sequence? ( '|' sequence? )* ';'?
		Rule rule() {
			return rule(
					"rule",
					seq(ref(this::ID), str(":"), _0_1(ref(this::sequence)), _0_n(str("|"), _0_1(ref(this::sequence))),
							_0_1(str(";"))));
		}

		// sequence : rulePart+
		Rule sequence() {
			return rule("sequence", _1_n(ref(this::rulePart)));
		}

		// rulePart
		//   : negatable
		//   | Literal
		//   | Reference
		//   | quantifier
		//   | subrule
		Rule rulePart() {
			return rule("rulePart", ref(this::negatable), ref(this::Literal), ref(this::Reference),
					ref(this::quantifier), ref(this::subrule));
		}

		// negatable
		//   : Any
		//   | Eof
		//   | Charset
		//   | Range
		//   | negation
		Rule negatable() {
			return rule("negatable", ref(this::Any), ref(this::Eof), ref(this::Charset), ref(this::Range),
					ref(this::negation));
		}

		// Any : .
		Rule Any() {
			return rule("Any", str("."));
		}

		// Eof : 'EOF'
		Rule Eof() {
			return rule("Eof", str("EOF"));
		}

		// Charset : '[' ( CHAR | CHAR '-' CHAR )+ ']'
		Rule Charset() {
			return rule("Charset",
					seq(str("["), subrule(ref(this::CHAR), seq(ref(this::CHAR), str("-"), ref(this::CHAR))), str("]")));
		}

		// Range : '\'' CHAR '\'' '-' '\'' CHAR '\''
		Rule Range() {
			return rule("Range",
					seq(str("'"), ref(this::CHAR), str("'"), str("-"), str("'"), ref(this::CHAR), str("'")));
		}

		// negation : '!' negatable
		Rule negation() {
			return rule("negation", seq(str("!"), ref(this::negatable)));
		}

		// Literal : '\'' CHAR+ '\''
		Rule Literal() {
			return rule("Literal", seq(str("'"), _1_n(ref(this::CHAR)), str("'")));
		}

		// Reference : ID
		Rule Reference() {
			return rule("Reference", ref(this::ID));
		}

		// quantifier : rulePart ( '?' | '*' | '+' | '{' INT ',' INT '}' | '{' INT '}' )
		Rule quantifier() {
			return rule(
					"quantifier",
					seq(ref(this::rulePart),
							subrule(str("?"), str("*"), str("+"),
									seq(str("{"), ref(this::INT), str(","), ref(this::INT), str("}")),
									seq(str("{"), ref(this::INT), str("}")))));
		}

		// subrule : '(' ( ID ':' )? sequence? ( '|' sequence? )+ ')'
		Rule subrule() {
			return rule(
					"subrule",
					seq(str("("), _0_1(ref(this::ID), str(":")), _0_1(ref(this::sequence)),
							_1_n(str("|"), _0_1(ref(this::sequence))), str(")")));
		}

		// CHAR : .
		Rule CHAR() {
			return rule("CHAR", ANY);
		}

		// ID : [a-zA-Z]+
		Rule ID() {
			//			return rule("ID", _1_n(charset("a-zA-Z")));
			return rule("ID", _1_n(subrule(range('a', 'z'), range('A', 'Z'))));
		}

		// INT : '0'..'9'+
		Rule INT() {
			return rule("INT", _1_n(range('0', '9')));
		}
	}
}
