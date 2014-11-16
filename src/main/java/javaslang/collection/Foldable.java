package javaslang.collection;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.Algebra;
import javaslang.Tuple.Tuple2;

/**
 * An interface for inherently recursive data structures.
 *
 * @param <A> A type.
 */
public interface Foldable<A> extends Iterable<A> {

	// -- primary operations

	default <B> B foldMap(Algebra.Monoid<B> m, Function<A, B> f) {
		return foldLeft(m.zero(), (b, a) -> m.op(b, f.apply(a)));
	}

	default <B> B foldLeft(B zero, BiFunction<B, ? super A, B> f) {
		B result = zero;
		for (Iterator<A> iter = iterator(); iter.hasNext(); result = f.apply(result, iter.next()))
			;
		return result;
	}

	default <B> B foldRight(B zero, BiFunction<? super A, B, B> f) {
		final Function<A, Function<B, B>> curried = a -> b -> f.apply(a, b);
		return foldMap(Algebra.Monoid.<B> endoMonoid(), curried).apply(zero);
	}

	// -- secondary operations, derived from the primary operations

	default int length() {
		// TODO: jdk compiler bug
		// return foldLeft(0, (n, __) -> n + 1);
		int length = 0;
		for (Iterator<?> iter = iterator(); iter.hasNext(); iter.next(), length++)
			;
		return length;
	}

	Foldable<A> drop(int n);

	Foldable<A> dropWhile(Predicate<A> predicate);

	Foldable<A> filter(Predicate<A> predicate);

	<B> Foldable<B> map(Function<A, B> f);

	<B> Foldable<B> flatMap(Function<A, ? extends Foldable<B>> f);

	default boolean isEmpty() {
		return length() == 0;
	}

	A reduceLeft(BinaryOperator<A> op);

	A reduceRight(BinaryOperator<A> op);

	Foldable<A> reverse();

	default int size() {
		return length();
	}

	Foldable<A> take(int n);

	Foldable<A> takeWhile(Predicate<A> predicate);

	// TODO: unfold

	<B> List<Tuple2<A, B>> zip(Iterable<B> that);

	<B> List<Tuple2<A, B>> zipAll(Iterable<B> that, A thisElem, B thatElem);

	List<Tuple2<A, Integer>> zipWithIndex();
}
