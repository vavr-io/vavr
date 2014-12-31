/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.function.Function;

public interface Algebra {

	/**
	 * A Semigroup is an algebraic structure consisting of
	 * 
	 * <ul>
	 *     <li>Some type A</li>
	 *     <li>An associative binary operation {@code combine}, such that {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any x,y,z of type A.</li>
	 * </ul>
	 * 
	 * Technically a Semigroup is the same as a {@code java.util.function.BiFunction<A,A,A>}. Introducing this new type
	 * clarifies that the operation {@code combine} is associative.
	 *
	 * @param <A> A type.
	 */
	@FunctionalInterface
	static interface Semigroup<A> {
		A combine(A a1, A a2);
	}

	/**
	 * A Monoid is a Semigroup with an identity element {@code zero}, i.e. it consits of
	 * 
	 * <ul>
	 *     <li>Some type A</li>
	 *     <li>An associative binary operation {@code combine}, such that {@code combine(combine(x,y),z) == combine(x,combine(y,z))} for any x,y,z of type A.</li>
	 *     <li>An identity element {@code zero}, such that {@code combine(zero(), x) == x == combine(x, zero())} for any x of type A.</li>
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
			return Monoid.of(Function.identity(), Function::compose);
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
				public A combine(A a1, A a2) {
					return semigroup.combine(a1, a2);
				}

				@Override
				public A zero() {
					return zero;
				}
			};
		}
	}

	/**
	 * Defines a Functor by generalizing the map function.
	 * <p>
	 * All instances of the Functor interface should obey the two functor laws:
	 * <ul>
	 *     <li>{@code m.map(a -> a) ≡ m}</li>
	 *     <li>{@code m.map(f.compse(g)) ≡ m.map(g).map(f)}</li>
	 * </ul>
	 *
	 * @param <A> Component type of this Functor.
	 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
	 */
	static interface Functor<A> {
		<B> Functor<B> map(Function<? super A, ? extends B> f);
	}

	/**
	 * Defines a Monad by generalizing the flatMap and unit functions.
	 * <p>
	 * All instances of the Monad interface should obey the three monad laws:
	 * <ul>
	 *     <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
	 *     <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
	 *     <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
	 * </ul>
	 *
	 * @param <A> Component type of this monad.
	 * @param <M> Type of Monad implementation.
	 */
	static interface Monad<A, M extends Monad<?, M>> extends Functor<A>, Manifest<A, M> {

		// -- first-class monad operations: unit() and flatMap()

		<B> Monad<B, M> unit(B b);

		// flatten may be expressed
		<B, MONAD extends Manifest<B, M>> Monad<B, M> flatMap(Function<? super A, MONAD> f);

		// -- additional operations which fit into the monad interface: map, flatten, filter

		/**
		 *
		 *
		 * @param f
		 * @param <B>
		 * @return
		 */
		@Override
		default <B> Monad<B, M> map(Function<? super A, ? extends B> f) {
			//noinspection unchecked
			return flatMap(a -> unit(f.apply(a)));
		}

// TODO: flatten
//		/**
//		 * Implementation should satisfy the following equation:
//		 *
//		 * <pre>
//		 *     <code>flatMap(f) = flatten(map(f))</code>
//		 * </pre>
//		 */
//		default <B> Monad<B, M> flatten() {
//			final Function f = Function.identity();
//			return flatMap(f);
//		}

		/**
		 * Monad composition, also known as Kleisli composition.
		 *
		 * @see <a href="http://scabl.blogspot.de/2013/03/monads-in-scala-2.html">Monads in Scala Part Two</a>
		 */
		default <B, C> Function<A, Monad<C, M>> compose(Function<B, Monad<C,M>> f, Function<A, Monad<B, M>> g) {
			return a -> g.apply(a).flatMap(f::apply);
		}
	}
}
