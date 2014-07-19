/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.either;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.Test;

public class EitherTest {
	
	// right

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowOnLeftGetOnRight() {
		new Right<String, Integer>(1).left().get();
	}

	@Test
	public void shouldReturnOtherWhenLeftOrElseOnRight() {
		final String actual = new Right<String, Integer>(1).left().orElse("1");
		assertThat(actual).isEqualTo("1");
	}
	
	@Test
	public void shouldMapOnRight() {
		final int actual = new Right<String, Integer>(1).right().map(i -> i + 1).right().get();
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldFlatMapOnRight() {
		final int actual = new Right<String, Integer>(1).right().flatMap(i -> new Right<>(i + 1)).right().get();
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldFilterSomeOnRight() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> i == 1).isPresent();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnRight() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> i != 1).isPresent();
		assertThat(actual).isFalse();
	}

	// left
	
	@Test(expected = NoSuchElementException.class)
	public void shouldThrowOnRightGetOnLeft() {
		new Left<String, Integer>("1").right().get();
	}
	
	@Test
	public void shouldReturnOtherWhenRightOrElseOnLeft() {
		final int actual = new Left<String, Integer>("1").right().orElse(1);
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldMapOnLeft() {
		final int actual = new Left<String, Integer>("1").left().map(s -> s.length()).left().get();
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldFlatMapOnLeft() {
		final int actual = new Left<String, Integer>("1").left().flatMap(s -> new Left<>(s.length())).left().get();
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldFilterSomeOnLeft() {
		final boolean actual = new Left<String, Integer>("1").left().filter(s -> "1".equals(s)).isPresent();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnLeft() {
		final boolean actual = new Left<String, Integer>("1").left().filter(s -> "2".equals(s)).isPresent();
		assertThat(actual).isFalse();
	}
	
}
