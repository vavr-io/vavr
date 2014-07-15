/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

public class ListTest {

	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowOnGetOnEmptyList() {
		List.empty().get(0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowOnGetExceedingUpperBound() {
		List.of((Object) null).get(1);
	}

}
