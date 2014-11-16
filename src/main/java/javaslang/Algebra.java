package javaslang;

import java.util.function.Function;

public interface Algebra {

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
			return Monoid.of(a -> a, (f, g) -> f.compose(g));
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

	// TODO
	static interface Functor<A> {
		<B> Functor<B> map(Function<A, B> f);
	}

	// TODO
	static interface Applicative<A> extends Functor<A> {
		// TODO: ??? map2(???);
	}

	// TODO
	static interface Monad<A> extends Applicative<A> {
		// TODO: unit
		// TODO: flatMap
		// TODO: compose
		// TODO: join
		// TODO: map
		// TODO: filter?
	}
}
