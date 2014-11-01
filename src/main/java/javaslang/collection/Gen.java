/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * Is Gen a Monad or a Collection or both or nothing?
 */
public class Gen<T> {

	// Arbitrary
	//	Gen<T> arbitrary();

	// Choose
	//	static <U extends Ordered, T extends Ordered<U>> Gen<T> choose(T min, T max);

	//	val vectors: Gen[Vector] =
	//	for {
	//	  x <- Gen.choose(-100, 100)
	//	  y <- Gen.choose(-100, 100)
	//	} yield Vector(x, y)
	// TODO: <U> Gen<U> combine(TupleX generators);

}
