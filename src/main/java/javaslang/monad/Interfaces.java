package javaslang.monad;

import java.util.function.Function;

public interface Interfaces {

	static interface Functor<A> {
		<B> Functor<B> map(Function<A, B> f);
	}

	static interface Applicative<A> extends Functor<A> {
		// TODO: ??? map2(???);
	}

	static interface Monad<A> extends Applicative<A> {
		// TODO: unit
		// TODO: flatMap
		// TODO: compose
		// TODO: join
		// TODO: map
		// TODO: filter?
	}
}
