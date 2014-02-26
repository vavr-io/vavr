package javaslang.text;

import static javaslang.lang.Lang.require;
import javaslang.util.tree.Tree;

/**
 * Repeats a parser lowerBound (min) to upperBound (max) times.<br>
 * <br>
 * Examples:
 * <ul>
 *   <li>X = 1 occurrence (lower bound = upper bound = 1)</li>
 *   <li>X? = 0..1 occurrences</li>
 *   <li>X* = 0..n occurrences</li>
 *   <li>X+ = 1..n occurrences</li>
 * </ul>
 */
class Sequence implements Parser {

	final Parser parser;
	final Bound lowerBound;
	final Bound upperBound;

	Sequence(Parser parser, Bound lowerBound, Bound upperBound) {
		require(parser != null, "parser is null");
		require(lowerBound.isLessOrEqual(upperBound), "min > max");
		this.parser = parser;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		final Tree<Token> result = new Tree<>("Sequence");
		// TODO: double-check if index grows on repeated calls of parser
		int currentIndex = index;
		Tree<Token> parsed = null;
		while((parsed = parser.parse(text, currentIndex)) != null) {
			result.attach(parsed);
			currentIndex = parsed.getValue().end;
		}
		// TODO Auto-generated method stub
		return result;
	}

	static enum Bound {
		ZERO, ONE, N;

		boolean isLessOrEqual(Bound that) {
			switch (this) {
				case ZERO:
					return true;
				case ONE:
					return !ZERO.equals(that);
				case N:
					return N.equals(that);
				default:
					return false;
			}
		}
	}
}
