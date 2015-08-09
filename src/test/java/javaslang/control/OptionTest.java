/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Serializables;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest {

    // -- construction

    @Test
    public void shouldMapNullToNone() {
        assertThat(Option.of(null)).isEqualTo(None.instance());
    }

    @Test
    public void shouldMapNonNullToSome() {
        final Option<?> option = Option.of(new Object());
        assertThat(option.isDefined()).isTrue();
    }

    @Test
    public void shouldWrapNullInSome() {
        final Some<?> some = new Some<>(null);
        assertThat(some.get()).isEqualTo(null);
    }

    @Test
    public void shouldCreateNothing() {
        assertThat(Some.nothing()).isEqualTo(new Some<Void>(null));
    }

    // -- get

    @Test
    public void shouldSucceedOnGetWhenValueIsPresent() {
        assertThat(Option.of(1).get()).isEqualTo(1);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnGetWhenValueIsNotPresent() {
        Option.none().get();
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

    // -- orElseGet

    @Test
    public void shouldGetValueOnOrElseGetWhenValueIsPresent() {
        assertThat(Option.of(1).orElseGet(() -> 2)).isEqualTo(1);
    }

    @Test
    public void shouldGetAlternativeOnOrElseGetWhenValueIsNotPresent() {
        assertThat(Option.none().orElseGet(() -> 2)).isEqualTo(2);
    }

    // -- orElseThrow

    @Test
    public void shouldGetValueOnOrElseThrowWhenValueIsPresent() {
        assertThat(Option.of(1).orElseThrow(() -> new RuntimeException("none"))).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnOrElseThrowWhenValueIsNotPresent() {
        Option.none().orElseThrow(() -> new RuntimeException("none"));
    }

    // -- toJavaOptional

    @Test
    public void shouldConvertNoneToJavaOptional() {
        final None<Object> none = None.instance();
        assertThat(none.toJavaOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldConvertSomeToJavaOptional() {
        final Some<Integer> some = new Some<>(1);
        assertThat(some.toJavaOptional()).isEqualTo(Optional.of(1));
    }

    // -- isPresent

    @Test
    public void shouldBePresentOnIsPresentWhenValueIsPresent() {
        assertThat(Option.of(1).isDefined()).isTrue();
    }

    @Test
    public void shouldNotBePresentOnIsPresentWhenValueIsNotPresent() {
        assertThat(Option.none().isDefined()).isFalse();
    }

    // -- isEmpty

    @Test
    public void shouldBeEmptyOnIsEmptyWhenValueIsEmpty() {
        assertThat(Option.none().isEmpty()).isTrue();
    }

    @Test
    public void shouldBePresentOnIsEmptyWhenValueIsPresent() {
        assertThat(Option.of(1).isEmpty()).isFalse();
    }

    // -- ifPresent

    @Test
    public void shouldConsumePresentValueOnIsPresentWhenValueIsPresent() {
        final int[] actual = new int[] { -1 };
        Option.of(1).forEach(i -> actual[0] = i);
        assertThat(actual[0]).isEqualTo(1);
    }

    @Test
    public void shouldNotConsumeAnythingOnIsPresentWhenValueIsNotPresent() {
        final int[] actual = new int[] { -1 };
        Option.<Integer> none().forEach(i -> actual[0] = i);
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

    // -- flatten()

    @Test
    public void shouldFlattenUnnestedSome() {
        assertThat(new Some<>(1).flatten()).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldFlattenSomeOfSome() {
        assertThat(new Some<>(new Some<>(1)).flatten()).isEqualTo(new Some<>(1));
    }

    @Test
    public void shouldFlattenSomeOfNone() {
        assertThat(new Some<>(None.instance()).flatten()).isEqualTo(None.instance());
    }

    @Test
    public void shouldFlattenNone() {
        assertThat(None.instance().flatten()).isEqualTo(None.instance());
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

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfSome() {
        assertThat(new Some<>(1).exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfSome() {
        assertThat(new Some<>(1).exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfNone() {
        assertThat(None.instance().exists(e -> true)).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfSome() {
        assertThat(new Some<>(1).forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfSome() {
        assertThat(new Some<>(1).forAll(i -> i == 2)).isFalse();
    }

    @Test // a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfNone() {
        assertThat(None.instance().forAll(e -> true)).isTrue();
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

    // -- peek

    @Test
    public void shouldConsumePresentValueOnPeekWhenValueIsPresent() {
        final int[] actual = new int[] { -1 };
        final Option<Integer> testee = Option.of(1).peek(i -> actual[0] = i);
        assertThat(actual[0]).isEqualTo(1);
        assertThat(testee).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldNotConsumeAnythingOnPeekWhenValueIsNotPresent() {
        final int[] actual = new int[] { -1 };
        final Option<Integer> testee = Option.<Integer> none().peek(i -> actual[0] = i);
        assertThat(actual[0]).isEqualTo(-1);
        assertThat(testee).isEqualTo(Option.none());
    }

    // -- iterator

    @Test
    public void shouldReturnIteratorOfSome() {
        assertThat((Iterator<Integer>) new Some<>(1).iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfNone() {
        assertThat((Iterator<Object>) None.instance().iterator()).isNotNull();
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
        final Object none = None.instance();
        assertThat(none.equals(new Object())).isFalse();
    }

    @Test
    public void shouldNotEqualSomeIfObjectIsOfDifferentType() {
        final Object some = new Some<>(1);
        assertThat(some.equals(new Object())).isFalse();
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
