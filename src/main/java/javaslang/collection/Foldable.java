package javaslang.collection;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.collection.Tuple.Tuple2;

/**
 * An interface for inherently recursive data structures.
 *
 * @param <A> A type.
 */
public interface Foldable<A> extends Iterable<A> {

	// -- primary operations

	default <B> B foldMap(Monoid<B> m, Function<A, B> f) {
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
		return foldMap(Monoid.<B> endoMonoid(), curried).apply(zero);
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

	// -- interfaces

	/**
	 * A Semigroup is an algebraic structure consisting of
	 * 
	 * <ul>
	 * <li>Some type A</li>
	 * <li>An associative binary operation {@code op}, such that {@code op(op(x,y),z) == op(x,op(y,z))} for any x,y,z of
	 * type A.</li>
	 * </ul>
	 * 
	 * Technically a Semigroup is the same as a {@code java.util.function.BiFunction<A,A,A>}. Introducing this new type
	 * clarifies that the operation {@code op} is associative.
	 *
	 * @param <A> A type.
	 */
	@FunctionalInterface
	static interface Semigroup<A> {

		A op(A a1, A a2);
	}

	/**
	 * A Monoid is a Semigroup with an identity element {@code zero}, i.e. it consits of
	 * 
	 * <ul>
	 * <li>Some type A</li>
	 * <li>An associative binary operation {@code op}, such that {@code op(op(x,y),z) == op(x,op(y,z))} for any x,y,z of
	 * type A.</li>
	 * <li>An identity element {@code zero}, such that {@code op(zero(), x) == x == op(x, zero())} for any x of type A.</li>
	 * </ul>
	 * 
	 * @param <A> A type.
	 */
	static interface Monoid<A> extends Semigroup<A> {

		A zero();

		/**
		 * Function composition of one type is an Endo monoid.
		 * 
		 * @return The Endo monoid of type A.
		 */
		static <A> Monoid<Function<A, A>> endoMonoid() {
			return Monoid.of(Function.<A> identity(), (f, g) -> f.compose(g));
		}

		/**
		 * Factory method for monoidsm taking a zero and a Semigroup.
		 * 
		 * @param zero The zero of the Monoid.
		 * @param semigroup Has the associative operation of the Monoid.
		 * @return a new Monoid
		 */
		static <A> Monoid<A> of(A zero, Semigroup<A> semigroup) {
			return new Monoid<A>() {

				@Override
				public A op(A a1, A a2) {
					return semigroup.op(a1, a2);
				}

				@Override
				public A zero() {
					return zero;
				}
			};
		}
	}
}
