/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Serializables;
import javaslang.Tuple;
import javaslang.algebra.Functor1;
import javaslang.algebra.Monad1;
import javaslang.algebra.Monad1Laws;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import javaslang.test.Gen;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest implements Monad1Laws<Option<?>> {

    // -- construction

	@Test
	public void shouldMapNullToNone() throws Exception {
		assertThat(Option.of(null)).isEqualTo(None.instance());
	}

	@Test
	public void shouldMapNonNullToSome() throws Exception {
		final Option<?> option = Option.of(new Object());
		assertThat(option.isDefined()).isTrue();
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

    // -- toOption

    @Test
    public void shouldConvertNoneToOption() {
        final None<Object> none = None.instance();
        assertThat(none.toOption()).isEqualTo(none);
    }

    @Test
    public void shouldConvertSomeToOption() {
        final Some<Integer> some = new Some<>(1);
        assertThat(some.toOption()).isEqualTo(some);
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

	// -- unapply

	@Test
	public void shouldUnapplyNone() {
		assertThat(None.instance().unapply()).isEqualTo(Tuple.empty());
	}

	@Test
	public void shouldUnapplySome() {
		assertThat(new Some<>(1).unapply()).isEqualTo(Tuple.of(1));
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

    // -- Functor1Laws

    static final Arbitrary<? extends Functor1<Integer>> FUNCTOR_OPTIONS = size -> random -> Gen.frequency(
            Tuple.of(1, Gen.of(None.<Integer> instance())),
            Tuple.of(4, Gen.choose(-size, size).map(i -> new Some<>(i)))
    ).apply(random);

    @Test
    @Override
    public void shouldSatisfyFunctorIdentity() {
        final CheckResult result = checkFunctorIdentity(FUNCTOR_OPTIONS);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyFunctorComposition() {
        final Arbitrary<Function<? super Integer, ? extends Double>> before =
                size -> random -> Double::valueOf;
        final Arbitrary<Function<? super Double, ? extends String>> after =
                size -> random -> String::valueOf;
        final CheckResult result = checkFunctorComposition(FUNCTOR_OPTIONS, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    // -- Monad1Laws

    static final Arbitrary<Integer> INTEGERS = size -> random -> Gen.frequency(
            Tuple.of(1, Gen.of(null)),
            Tuple.of(4, Gen.choose(-size, size))
    ).apply(random);

    static final Arbitrary<? extends Monad1<Integer, Option<?>>> MONAD_OPTIONS = size -> random -> Gen.frequency(
            Tuple.of(1, Gen.of(None.<Integer> instance())),
            Tuple.of(4, Gen.choose(-size, size).map(i -> new Some<>(i)))
    ).apply(random);

    @Test
    @Override
    public void shouldSatisfyMonadLeftIdentity() {
        final Arbitrary<Function<? super Integer, ? extends Monad1<String, Option<?>>>> mappers =
                size -> random -> i -> Option.of(i).map(String::valueOf);
        final CheckResult result = checkMonadLeftIdentity(Option::of, INTEGERS, mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadRightIdentity() {
        final CheckResult result = checkMonadRightIdentity(Option::of, MONAD_OPTIONS);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadAssociativity() {
        final Arbitrary<Function<? super Integer, ? extends Monad1<Double, Option<?>>>> before =
                size -> random -> i -> Option.of(i).map(Double::valueOf);
        final Arbitrary<Function<? super Double, ? extends Monad1<String, Option<?>>>> after =
                size -> random -> d -> Option.of(d).map(String::valueOf);
        final CheckResult result = checkMonadAssociativity(MONAD_OPTIONS, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }
}
