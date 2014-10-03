/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import javaslang.parser.Parser.Rule;

public class BootstrapTest {

	static class JavaslangGrammar extends Grammar {

		JavaslangGrammar() {
			super("Javaslang");
		}

		@Override
		protected Rule getStartRule() {
			return grammar();
		}

		// grammar : 'grammar' ID ';'? rule+
		Rule grammar() {
			return rule("grammar", str("grammar"), ref(this::ID), _0_1(str(";")), _1_n(ref(this::rule)));
		}

		// rule : ID ':' rulePart ( '|' rulePart )* ';'?
		Rule rule() {
			return rule("rule", ref(this::ID), str(":"), ref(this::rulePart), _0_n(str("|"), ref(this::rulePart)),
					_0_1(str(";")));
		}

		// rulePart
		//   : negatable
		//   | Literal
		//   | Reference
		//   | quantifier
		//   | subrule
		//   | sequence
		Rule rulePart() {
			return rule("rulePart", ref(this::negatable), ref(this::Reference), ref(this::quantifier),
					ref(this::subrule), ref(this::sequence));
		}

		// negatable
		//   : ANY
		//   | EOF
		//   | Charset
		//   | Range
		//   | negation
		Rule negatable() {
			return rule("negatable", ref(this::ANY), ref(this::EOF), ref(this::Charset), ref(this::Range),
					ref(this::negation));
		}

		// ANY : .
		Rule ANY() {
			return rule("ANY", ANY);
		}

		// EOF : 'EOF'
		Rule EOF() {
			return rule("EOF", str("EOF"));
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
			return rule("negation", str("!"), ref(this::negatable));
		}

		// Literal : '\'' CHAR* '\''
		Rule Literal() {
			return rule("Literal", seq(str("'"), _0_n(ref(this::CHAR)), str("'")));
		}

		// Reference : ID
		Rule Reference() {
			return rule("Reference", ref(this::ID));
		}

		// quantifier : rulePart ( '?' | '*' | '+' | '{' INT ',' INT '}' | '{' INT '}' )
		Rule quantifier() {
			return rule(
					"quantifier",
					ref(this::rulePart),
					subrule(str("?"), str("*"), str("+"),
							seq(str("{"), ref(this::INT), str(","), ref(this::INT), str("}")),
							seq(str("{"), ref(this::INT), str("}"))));
		}

		// subrule : '(' ( ID ':' )? rulePart ( '|' rulePart )* ')'
		Rule subrule() {
			return rule(
					"subrule",
					seq(str("("), _0_1(ref(this::ID), str(":")), ref(this::rulePart),
							_0_n(str("|"), ref(this::rulePart)), str(")")));
		}

		// sequence : rulePart*
		Rule sequence() {
			return rule("sequence", _0_n(ref(this::rulePart)));
		}

		// CHAR : .
		Rule CHAR() {
			return rule("CHAR", ANY);
		}

		// ID : [a-zA-Z]+
		Rule ID() {
			return rule("ID", _1_n(charset("a-zA-Z")));
		}

		// INT : '0'..'9'+
		Rule INT() {
			return rule("INT", _1_n(range('0', '9')));
		}
	}
}
