/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import java.util.Objects;

import javaslang.AssertionsExtensions;
import javaslang.Serializables;
import javaslang.monad.Option.None;
import javaslang.monad.Option.Some;

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
		AssertionsExtensions.assertThat(() -> Option.none().get()).isThrowing(NoSuchElementException.class,
				"No value present");
	}

	// -- orElse

	@Test
	public void shouldGetValueOnOrElseWhenValueIsPresent() {
		assertThat(Option.of(1).orElse(2)).isEqualTo(1);
	}

	@Test
	public void shouldGetAlternativeOnOrElseWhenValueIsNotPresent() {
		assertThat(Option.none().orElse(2)).isEqualTo(2);
	}

	// orElseGet

	@Test
	public void shouldGetValueOnOrElseGetWhenValueIsPresent() {
		assertThat(Option.of(1).orElseGet(() -> 2)).isEqualTo(1);
	}

	@Test
	public void shouldGetAlternativeOnOrElseGetWhenValueIsNotPresent() {
		assertThat(Option.none().orElseGet(() -> 2)).isEqualTo(2);
	}

	// orElseThrow

	@Test
	public void shouldGetValueOnOrElseThrowWhenValueIsPresent() {
		assertThat(Option.of(1).orElseThrow(() -> new RuntimeException("none"))).isEqualTo(1);
	}

	@Test
	public void shouldThrowOnOrElseThrowWhenValueIsNotPresent() {
		AssertionsExtensions
				.assertThat(() -> Option.none().orElseThrow(() -> new RuntimeException("none")))
				.isThrowing(RuntimeException.class, "none");
	}

	// -- isPresent

	@Test
	public void shouldBePresentOnIsPresentWhenValueIsPresent() {
		assertThat(Option.of(1).isPresent()).isTrue();
	}

	@Test
	public void shouldNotBePresentOnIsPresentWhenValueIsNotPresent() {
		assertThat(Option.none().isPresent()).isFalse();
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
		Option.<Integer> none().ifPresent(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(-1);
	}

	// -- filter

	@Test
	public void shouldReturnSomeOnFilterWhenValueIsPresentAndPredicateMatches() {
		assertThat(Option.of(1).filter(i -> i == 1)).isEqualTo(Option.of(1));
	}

	@Test
	public void shouldReturnNoneOnFilterWhenValueIsPresentAndPredicateNotMatches() {
		assertThat(Option.of(1).filter(i -> i == 2)).isEqualTo(Option.none());
	}

	@Test
	public void shouldReturnNoneOnFilterWhenValueIsNotPresentAndPredicateNotMatches() {
		assertThat(Option.<Integer> none().filter(i -> i == 1)).isEqualTo(Option.none());
	}

	// -- map

	@Test
	public void shouldMapSome() {
		assertThat(Option.of(1).map(String::valueOf)).isEqualTo(Option.of("1"));
	}

	@Test
	public void shouldMapNone() {
		assertThat(Option.<Integer> none().map(String::valueOf)).isEqualTo(Option.none());
	}

	// -- flatMap

	@Test
	public void shouldFlatMapSome() {
		assertThat(Option.of(1).flatMap(i -> Option.of(String.valueOf(i)))).isEqualTo(Option.of("1"));
	}

	@Test
	public void shouldFlatMapNone() {
		assertThat(Option.<Integer> none().flatMap(i -> Option.of(String.valueOf(i)))).isEqualTo(Option.none());
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
		Option.<Integer> none().forEach(i -> actual[0] = i);
		assertThat(actual[0]).isEqualTo(-1);
	}

	// -- unit

	@Test
	public void shouldReturnNoneWhenCallingUnitOnNone() {
		assertThat(None.instance().unit(1)).isEqualTo(None.instance());
	}

	@Test
	public void shouldReturnNewSomeWhenCallingUnitOnSome() {
		assertThat(new Some<>(1).unit("a")).isEqualTo(new Some<>("a"));
	}

	// -- equals

	@Test
	public void shouldEqualNoneIfObjectIsSame() {
		final None<?> none = None.instance();
		assertThat(none).isEqualTo(none);
	}

	@Test
	public void shouldEqualSomeIfObjectIsSame() {
		final Some<?> some = new Some<>(1);
		assertThat(some).isEqualTo(some);
	}

	@Test
	public void shouldNotEqualNoneIfObjectIsNull() {
		assertThat(None.instance()).isNotNull();
	}

	@Test
	public void shouldNotEqualSomeIfObjectIsNull() {
		assertThat(new Some<>(1)).isNotNull();
	}

	@Test
	public void shouldNotEqualNoneIfObjectIsOfDifferentType() {
		assertThat(None.instance().equals(new Object())).isFalse();
	}

	@Test
	public void shouldNotEqualSomeIfObjectIsOfDifferentType() {
		assertThat(new Some<>(1).equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualSome() {
		assertThat(new Some<>(1)).isEqualTo(new Some<>(1));
	}

	// -- hashCode

	@Test
	public void shouldHashNone() {
		assertThat(None.instance().hashCode()).isEqualTo(Objects.hash());
	}

	@Test
	public void shouldHashSome() {
		assertThat(new Some<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
	}

	// -- toString

	@Test
	public void shouldConvertSomeToString() {
		assertThat(new Some<>(1).toString()).isEqualTo("Some(1)");
	}

	@Test
	public void shouldConvertNoneToString() {
		assertThat(None.instance().toString()).isEqualTo("None");
	}

	// -- serialization

	@Test
	public void shouldPreserveSingletonWhenDeserializingNone() {
		final Object none = Serializables.deserialize(Serializables.serialize(None.instance()));
		assertThat(none == None.instance()).isTrue();
	}
}
