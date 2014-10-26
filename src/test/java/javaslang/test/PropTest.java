/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import static org.assertj.core.api.Assertions.assertThat;
import javaslang.collection.List;

import org.junit.Test;

public class PropTest {

	@Test
	public void shouldDoSth1() {
		final List<Integer> xs = List.of(1, 2, 3, 4);
		final Prop property = Props.forAll(xs).suchThat(x -> x < 5);
		assertThat(property.test()).isTrue();
	}

	@Test
	public void shouldDoSth2() {
		final List<Integer> xs = List.of(1, 2, 3, 4);
		final Prop property = Props.forAll(xs).forAll(xs).suchThat((x, y) -> x <= y || x > y);
		assertThat(property.test()).isTrue();
	}
}
