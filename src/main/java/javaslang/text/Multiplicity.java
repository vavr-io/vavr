package javaslang.text;

import static javaslang.lang.Lang.require;
import static javaslang.text.Multiplicity.Bounds.ONE_TO_N;
import static javaslang.text.Multiplicity.Bounds.ZERO_TO_ONE;

import java.util.function.Supplier;

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
class Multiplicity implements Parser {

	static enum Bounds {
		ZERO_TO_ONE, ZERO_TO_N, ONE_TO_N;
	}

	final Supplier<Parser> parser;
	final Bounds bounds;

	Multiplicity(Supplier<Parser> parser, Bounds bounds) {
		require(parser != null, "parser is null");
		require(bounds != null, "bounds is null");
		this.parser = parser;
		this.bounds = bounds;
	}

	@Override
	public Tree<Token> parse(String text, int index) {
		return extracted(text, index);
	}

	private Tree<Token> extracted(String text, int index) {
		if (ZERO_TO_ONE.equals(bounds)) {
			final Tree<Token> result = new Tree<>(bounds.name(), new Token(text, index, index));
			final Tree<Token> child = parser.get().parse(text, index);
			if (child != null) {
				result.attach(child);
				result.getValue().end = child.getValue().end;
			}				
			return result;
		} else {
			// upper bound is N
			final Tree<Token> result = new Tree<>(bounds.name(), new Token(text, index, index));
			int currentIndex = index;
			Tree<Token> child = null;
			while((child = parser.get().parse(text, currentIndex)) != null) {
				result.attach(child);
				currentIndex = child.getValue().end;
				result.getValue().end = currentIndex;
			}
			final boolean notMatched = currentIndex == index;
			final boolean shouldHaveMatched = ONE_TO_N.equals(bounds); 
			if (notMatched && shouldHaveMatched) {
				return null;
			} else {
				return result;
			}
		}
	}
	
}
