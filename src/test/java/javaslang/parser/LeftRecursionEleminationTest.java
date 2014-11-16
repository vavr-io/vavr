/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.parser.Grammar._1_n;
import static javaslang.parser.Grammar.charset;
import static javaslang.parser.Grammar.ref;
import static javaslang.parser.Grammar.rule;
import static javaslang.parser.Grammar.seq;
import static javaslang.parser.Grammar.str;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javaslang.collection.Stream;
import javaslang.monad.None;
import javaslang.monad.Option;
import javaslang.parser.Parser.HasChildren;
import javaslang.parser.Parser.Rule;

import org.junit.Ignore;
import org.junit.Test;

public class LeftRecursionEleminationTest {

	/**
	 * Interface which solves the general problem of left-recursion of grammar rules.
	 *
	 * @param <T> Type of grammar rules.
	 */
	// TODO: Issue #54
	static interface LeftRecursionSolver<T> {

		Rule from(T input);

		T to(Rule output);

		default T solve(T rule) {
			return to(solve(from(rule)));
		}

		// TODO: Type 'Rule' is a placeholder
		//       => Need for a rule/grammar representation which can be mutated,
		//          i.e. is capable of rule substitution. 
		static Rule solve(Rule rule) {
			return null;
		}
	}

	@Test
	@Ignore
	public void shouldDoSth() {

		// TODO: Move this to interface LeftRecursionSolver

		final Rule startRule = expr();
		final Set<Rule> rules = findRules(startRule);

		for (Rule rule : rules) {

			// Search A : A α | β
			if (isDirectLeftRecursive(rule)) {

				/* TODO:DEBUG */System.out.println(rule.name + " is direct left-recursive");
				/* TODO:DEBUG */System.out.println("// Current rule:\n" + rule);

				if (rule.alternatives.length == 1) {
					// Replace A : A α
					// with    A : α A | ε

					//					final Rule rewrittenRule = rewrite(rule, xxx);

				} else {
					// Replace A  : A α1 | A α2 | ... | A αn | β1 | β2 | ... | βm
					// with    A  : β1 A' | β2 A' | ... | βm A'
					//         A' : α1 A' | α2 A' | ... | αn A' | ε

					//					Stream.of(rule.alternatives).zipWithIndex().map(t -> {
					//						final RulePart rulePart = t._1;
					//						final long index = t._2;
					//						return null;
					//					});

					//					final Rule rewrittenRule = rewrite(rule, xxx);
					//					/* TODO:DEBUG */System.out.println("// Rewritten rule:\n" + rewrittenRule);
					//					final Rule newRule = newRule(rule, xxx);
					//					/* TODO:DEBUG */System.out.println("// New rule:\n" + newRule);
				}
			}
		}
	}

	// -- find all transitively referenced rules

	Set<Rule> findRules(Rule startRule) {
		final Set<Parser> visited = new HashSet<>();
		final Set<Rule> rules = new LinkedHashSet<>();
		findRules(visited, rules, startRule);
		return rules;
	}

	private void findRules(Set<Parser> visited, Set<Rule> rules, Parser parser) {
		if (!visited.contains(parser)) {
			visited.add(parser);
			if (parser instanceof Rule) {
				rules.add((Rule) parser);
			}
			if (parser instanceof HasChildren) {
				final HasChildren hasChildren = (HasChildren) parser;
				Stream.of(hasChildren.getChildren()).forEach(child -> findRules(visited, rules, child));
			}
		}
	}

	// -- check if rule is direct left recursive

	boolean isDirectLeftRecursive(Rule rule) {
		return Stream.of(rule.alternatives).filter(alt -> isDirectLeftRecursive(rule, alt)).findAny().isPresent();
	}

	private boolean isDirectLeftRecursive(Rule rule, Parser parser) {
		return parser.equals(rule)
				|| firstChild(parser).filter(child -> isDirectLeftRecursive(rule, child)).isPresent();
	}

	private Option<Parser> firstChild(Parser parser) {
		if (parser instanceof HasChildren) {
			return Option.of(((HasChildren) parser).getChildren()[0]);
		} else {
			return None.instance();
		}
	}

	// -- rule definitions

	// expr : expr '+' expr | [0-9]+
	Rule expr() {
		return rule("expr", seq(ref(this::expr), str("+"), ref(this::expr)), ref(this::ID));
	}

	// ID : [a-zA-Z]+
	Rule ID() {
		return rule("ID", _1_n(charset("a-zA-Z")));
	}
}
