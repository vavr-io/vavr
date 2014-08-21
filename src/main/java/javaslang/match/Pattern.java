/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javaslang.Tuples;
import javaslang.Tuples.Tuple;
import javaslang.Tuples.Tuple1;
import javaslang.Tuples.Tuple10;
import javaslang.Tuples.Tuple11;
import javaslang.Tuples.Tuple12;
import javaslang.Tuples.Tuple13;
import javaslang.Tuples.Tuple2;
import javaslang.Tuples.Tuple3;
import javaslang.Tuples.Tuple4;
import javaslang.Tuples.Tuple5;
import javaslang.Tuples.Tuple6;
import javaslang.Tuples.Tuple7;
import javaslang.Tuples.Tuple8;
import javaslang.Tuples.Tuple9;

// TODO: Matchs.caze(Pattern.of(decompositionOfA, any(), any()), (a, dA) -> ???)
public interface Pattern<R extends Tuple> extends Applicative<R> {

	/**
	 * 
	 */
	@Override
	boolean isApplicable(Object o);

	/**
	 * 
	 * @param o
	 * @return
	 */
	@Override
	R apply(Object o);

	static <T, R extends Tuple> Pattern<R> of(Decomposition<T, R> decomposition, R prototype) {
		return null;
	}

	static <T, E1> Pattern<Tuple1<E1>> of(Decomposition<T, Tuple1<E1>> decomposition, E1 e1) {
		return Pattern.of(decomposition, Tuples.of(e1));
	}

	static <T, E1, E2> Pattern<Tuple2<E1, E2>> of(Decomposition<T, Tuple2<E1, E2>> decomposition, E1 e1, E2 e2) {
		return Pattern.of(decomposition, Tuples.of(e1, e2));
	}

	/*
	static <T, E1, E2, E3> Pattern<Tuple3<E1, E2, E3>> of(Decomposition<T, Tuple3<E1, E2, E3>> decomposition, E1 e1,
			E2 e2, E3 e3) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3));
	}

	static <T, E1, E2, E3, E4> Pattern<Tuple4<E1, E2, E3, E4>> of(
			Decomposition<T, Tuple4<E1, E2, E3, E4>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4));
	}

	static <T, E1, E2, E3, E4, E5> Pattern<Tuple5<E1, E2, E3, E4, E5>> of(
			Decomposition<T, Tuple5<E1, E2, E3, E4, E5>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5));
	}

	static <T, E1, E2, E3, E4, E5, E6> Pattern<Tuple6<E1, E2, E3, E4, E5, E6>> of(
			Decomposition<T, Tuple6<E1, E2, E3, E4, E5, E6>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7> Pattern<Tuple7<E1, E2, E3, E4, E5, E6, E7>> of(
			Decomposition<T, Tuple7<E1, E2, E3, E4, E5, E6, E7>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5,
			E6 e6, E7 e7) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8> Pattern<Tuple8<E1, E2, E3, E4, E5, E6, E7, E8>> of(
			Decomposition<T, Tuple8<E1, E2, E3, E4, E5, E6, E7, E8>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5,
			E6 e6, E7 e7, E8 e8) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9> Pattern<Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9>> of(
			Decomposition<T, Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9>> decomposition, E1 e1, E2 e2, E3 e3, E4 e4,
			E5 e5, E6 e6, E7 e7, E8 e8, E9 e9) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10> Pattern<Tuple10<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10>> of(
			Decomposition<T, Tuple10<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10>> decomposition, E1 e1, E2 e2, E3 e3,
			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11> Pattern<Tuple11<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11>> of(
			Decomposition<T, Tuple11<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11>> decomposition, E1 e1, E2 e2, E3 e3,
			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12> Pattern<Tuple12<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12>> of(
			Decomposition<T, Tuple12<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12>> decomposition, E1 e1, E2 e2, E3 e3,
			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11, E12 e12) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12));
	}

	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13> Pattern<Tuple13<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13>> of(
			Decomposition<T, Tuple13<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13>> decomposition, E1 e1, E2 e2, E3 e3,
			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11, E12 e12, E13 e13) {
		return Pattern.of(decomposition, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13));
	}
	*/
}
