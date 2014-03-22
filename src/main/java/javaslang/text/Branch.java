package javaslang.text;

import static javaslang.lang.Arrays.isNullOrEmpty;
import static javaslang.lang.Lang.require;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javaslang.lang.Strings;

class Branch implements Parser {

	final List<Supplier<Parser>> parsers;

	@SafeVarargs
	Branch(Supplier<Parser>... parsers) {
		require(!isNullOrEmpty(parsers), "no parsers");
		this.parsers = Arrays.asList(parsers);
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		return parsers
				.parallelStream()
				.map(parser -> {
					return parser.get().parse(text, index);
				})
				.reduce(null, (tree1, tree2) -> {
					require(tree1 == null || tree2 == null, () -> "ambiguity found at " + Strings.toString(Strings.lineAndColumn(text, index)) + ":\n" + text);
					return (tree1 != null) ? tree1 : tree2; // may be null
				}, (t1,t2) -> (t1 != null) ? t1 : t2);
	}

}
