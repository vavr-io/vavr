package javaslang.text;

import static javaslang.lang.Lang.require;
import static javaslang.util.Arrays.isNullOrEmpty;

import java.util.Arrays;
import java.util.List;

import javaslang.util.Objects;
import javaslang.util.Strings;
import javaslang.util.tree.Tree;

class Branch implements Parser {

	final List<Parser> parsers;

	Branch(Parser... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.parsers = Arrays.asList(parsers);
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		return parsers
				.parallelStream()
				.map(parser -> {
					System.out.println("Parser " + parser);
					return parser.parse(text, index);
				})
				.reduce(null, (tree1, tree2) -> {
					require(tree1 == null || tree2 == null, "ambiguity found at " + Objects.toString(Strings.lineAndColumn(text, index)) + ":\n" + text);
					return (tree1 != null) ? tree1 : tree2; // may be null
				}, (t1,t2) -> (t1 != null) ? t1 : t2);
	}

}
