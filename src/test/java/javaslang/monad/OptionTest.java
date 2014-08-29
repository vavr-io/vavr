/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import javaslang.monad.None;
import javaslang.monad.Option;
import javaslang.monad.Some;

import org.junit.Test;

public class OptionTest {

	// -- construction

	@Test
	public void shouldMapNullToNone() throws Exception {
		assertThat(Option.of(null)).isEqualTo(None.instance());
	}

	@Test
	public void shouldMapNonNullToSome() throws Exception {
		final Option<?> option = Option.of(new Object());
		assertThat(option.isPresent()).isTrue();
	}

	@Test
	public void shouldWrapNullInSome() throws Exception {
		final Some<?> some = new Some<>(null);
		assertThat(some.get()).isEqualTo(null);
	}

	// -- get

	@Test
	public void shouldSucceedOnGetWhenValueIsPresent() {
		assertThat(Option.of(1).get()).isEqualTo(1);
	}

	@Test
	public void shouldThrowOnGetWhenValueIsNotPresent() {
		assertThat(() -> Option.empty().get()).isThrowing(NoSuchElementException.class, "No value present");
	}

	// -- orElse

	@Test
	public void shouldGetValueOnOrElseWhenValueIsPresent() {
		assertThat(Option.of(1).orElse(2)).isEqualTo(1);
	}

	@Test
	public void shouldGetAlternativeOnOrElseWhenValueIsNotPresent() {
		assertThat(Option.empty().orElse(2)).isEqualTo(2);
	}

	// orElseGet

	@Test
	public void shouldGetValueOnOrElseGetWhenValueIsPresent() {
		assertThat(Option.of(1).orElseGet(() -> 2)).isEqualTo(1);
	}

	@Test
	public void shouldGetAlternativeOnOrElseGetWhenValueIsNotPresent() {
		assertThat(Option.empty().orElseGet(() -> 2)).isEqualTo(2);
	}

	// orElseThrow

	@Test
	public void shouldGetValueOnOrElseThrowWhenValueIsPresent() {
		assertThat(Option.of(1).orElseThrow(() -> new RuntimeException("none"))).isEqualTo(1);
	}

	@Test
	public void shouldThrowOnOrElseThrowWhenValueIsNotPresent() {
		assertThat(() -> Option.empty().orElseThrow(() -> new RuntimeException("none"))).isThrowing(
				RuntimeException.class, "none");
	}

	// -- isPresent

	@Test
	public void shouldBePresentOnIsPresentWhenValueIsPresent() {
		assertThat(Option.of(1).isPresent()).isTrue();
	}

	@Test
	public void shouldNotBePresentOnIsPresentWhenValueIsNotPresent() {
		assertThat(Option.empty().isPresent()).isFalse();
	}

	// -- ifPresent

	@Test
	public void shouldConsumePresentValueOnIsPresentWhenValueIsPresent() {
		final int[] actual = new int[] { -1 };
		Option.of(1).ifPresent(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(1);
	}

	@Test
	public void shouldNotConsumeAnythingOnIsPresentWhenValueIsNotPresent() {
		final int[] actual = new int[] { -1 };
		Option.<Integer> empty().ifPresent(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(-1);
	}

	// -- filter

	@Test
	public void shouldReturnSomeOnFilterWhenValueIsPresentAndPredicateMatches() {
		assertThat(Option.of(1).filter(i -> i == 1)).isEqualTo(Option.of(1));
	}

	@Test
	public void shouldReturnNoneOnFilterWhenValueIsPresentAndPredicateNotMatches() {
		assertThat(Option.of(1).filter(i -> i == 2)).isEqualTo(Option.empty());
	}

	@Test
	public void shouldReturnNoneOnFilterWhenValueIsNotPresentAndPredicateNotMatches() {
		assertThat(Option.<Integer> empty().filter(i -> i == 1)).isEqualTo(Option.empty());
	}

	// -- map

	@Test
	public void shouldMapSome() {
		assertThat(Option.of(1).map(i -> String.valueOf(i))).isEqualTo(Option.of("1"));
	}

	@Test
	public void shouldMapNone() {
		assertThat(Option.<Integer> empty().map(i -> String.valueOf(i))).isEqualTo(Option.empty());
	}

	// -- flatMap

	@Test
	public void shouldFlatMapSome() {
		assertThat(Option.of(1).flatMap(i -> Option.of(String.valueOf(i)))).isEqualTo(Option.of("1"));
	}

	@Test
	public void shouldFlatMapNone() {
		assertThat(Option.<Integer> empty().flatMap(i -> Option.of(String.valueOf(i)))).isEqualTo(Option.empty());
	}

	// -- forEach

	@Test
	public void shouldConsumePresentValueOnForEachWhenValueIsPresent() {
		final int[] actual = new int[] { -1 };
		Option.of(1).forEach(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(1);
	}

	@Test
	public void shouldNotConsumeAnythingOnForEachWhenValueIsNotPresent() {
		final int[] actual = new int[] { -1 };
		Option.<Integer> empty().forEach(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(-1);
	}
}
