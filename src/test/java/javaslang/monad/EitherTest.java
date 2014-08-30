/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.Test;

public class EitherTest {

	// -- Right

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

	// equals

	@Test
	public void shouldEqualRightIfObjectIsSame() {
		final Right<?, ?> right = new Right<>(1);
		assertThat(right.equals(right)).isTrue();
	}

	@Test
	public void shouldNotEqualRightIfObjectIsNull() {
		assertThat(new Right<>(1).equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualRightIfObjectIsOfDifferentType() {
		assertThat(new Right<>(1).equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualRight() {
		assertThat(new Right<>(1)).isEqualTo(new Right<>(1));
	}

	// hashCode

	@Test
	public void shouldHashRight() {
		assertThat(new Right<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
	}

	// toString

	@Test
	public void shouldConvertRightToString() {
		assertThat(new Right<>(1).toString()).isEqualTo("Right(1)");
	}

	// -- Left

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

	// equals

	@Test
	public void shouldEqualLeftIfObjectIsSame() {
		final Left<?, ?> left = new Left<>(1);
		assertThat(left.equals(left)).isTrue();
	}

	@Test
	public void shouldNotEqualLeftIfObjectIsNull() {
		assertThat(new Left<>(1).equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualLeftIfObjectIsOfDifferentType() {
		assertThat(new Left<>(1).equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualLeft() {
		assertThat(new Left<>(1)).isEqualTo(new Left<>(1));
	}

	// hashCode

	@Test
	public void shouldHashLeft() {
		assertThat(new Left<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
	}

	// toString

	@Test
	public void shouldConvertLeftToString() {
		assertThat(new Left<>(1).toString()).isEqualTo("Left(1)");
	}
}
