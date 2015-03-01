/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.control.Either.LeftProjection;
import javaslang.control.Either.RightProjection;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

	// -- Left

	@Test
	public void shouldReturnTrueWhenCallingIsLeftOnLeft() {
		assertThat(new Left<>(1).isLeft()).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenCallingIsRightOnLeft() {
		assertThat(new Left<>(1).isRight()).isFalse();
	}

	// unapply

	@Test
	public void shouldUnapplyLeft() {
		assertThat(new Left<>(1).unapply()).isEqualTo(Tuple.of(1));
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

	// -- LeftProjection

	// get

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowOnGetOnLeftProjectionOfRight() {
		new Right<>(1).left().get();
	}

	@Test
	public void shouldGetOnLeftProjectionOfLeft() {
		assertThat(new Left<>(1).left().get()).isEqualTo(1);
	}

	// orElse

	@Test
	public void shouldReturnLeftWhenOrElseOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElse(2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseOnLeftProjectionOfRight() {
		final Integer actual = new Right<Integer, String>("1").left().orElse(2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseGet(Function)

	@Test
	public void shouldReturnLeftWhenOrElseGetGivenFunctionOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElseGet(r -> 2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseGetGivenFunctionOnLeftProjectionOfRight() {
		final Integer actual = new Right<Integer, String>("1").left().orElseGet(r -> 2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseRun

	@Test
	public void shouldReturnLeftWhenOrElseRunOnLeftProjectionOfLeft() {
		final boolean[] actual = new boolean[] { true };
		new Left<Integer, String>(1).left().orElseRun(s -> {
			actual[0] = false;
		});
		assertThat(actual[0]).isTrue();
	}

	@Test
	public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfRight() {
		final boolean[] actual = new boolean[] { false };
		new Right<Integer, String>("1").left().orElseRun(s -> {
			actual[0] = true;
		});
		assertThat(actual[0]).isTrue();
	}

	// orElseThrow(Function)

	@Test
	public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElseThrow(str -> new RuntimeException(str));
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
		new Right<Integer, String>("1").left().orElseThrow(str -> new RuntimeException(str));
	}

	// toOption

	@Test
	public void shouldConvertLeftProjectionOfLeftToSome() {
		assertThat(new Left<Integer, String>(1).left().toOption()).isEqualTo(Option.of(1));
	}

	@Test
	public void shouldConvertLeftProjectionOfRightToNone() {
		assertThat(new Right<Integer, String>("x").left().toOption()).isEqualTo(Option.none());
	}

	// toEither

	@Test
	public void shouldConvertLeftProjectionOfLeftToEither() {
		final Either<Integer, String> self = new Left<>(1);
		assertThat(self.left().toEither()).isEqualTo(self);
	}

	@Test
	public void shouldConvertLeftProjectionOfRightToEither() {
		final Either<Integer, String> self = new Right<>("1");
		assertThat(self.left().toEither()).isEqualTo(self);
	}

	// filter

	@Test
	public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
		final boolean actual = new Left<Integer, String>(1).left().filter(i -> true).isDefined();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
		final boolean actual = new Left<Integer, String>(1).left().filter(i -> false).isDefined();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
		final boolean actual = new Right<Integer, String>("1").left().filter(i -> true).isDefined();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
		final boolean actual = new Right<Integer, String>("1").left().filter(i -> false).isDefined();
		assertThat(actual).isFalse();
	}

	// flatMap

	@Test
	public void shouldFlatMapOnLeftProjectionOfLeft() {
		final Either<Integer, String> actual = new Left<Integer, String>(1).left().flatMap(i -> new Left<>(i + 1));
		assertThat(actual).isEqualTo(new Left<>(2));
	}

	@Test
	public void shouldFlatMapOnLeftProjectionOfRight() {
		final Either<Integer, String> actual = new Right<Integer, String>("1").left().flatMap(i -> new Left<>(i + 1));
		assertThat(actual).isEqualTo(new Right<>("1"));
	}

	// forEach

	@Test
	public void shouldForEachOnLeftProjectionOfLeft() {
		final List<Integer> actual = new ArrayList<>();
		new Left<Integer, String>(1).left().forEach(i -> actual.add(i));
		assertThat(actual).isEqualTo(Arrays.asList(1));
	}

	@Test
	public void shouldForEachOnLeftProjectionOfRight() {
		final List<Integer> actual = new ArrayList<>();
		new Right<Integer, String>("1").left().forEach(i -> actual.add(i));
		assertThat(actual.isEmpty()).isTrue();
	}

	// map

	@Test
	public void shouldMapOnLeftProjectionOfLeft() {
		final Either<Integer, String> actual = new Left<Integer, String>(1).left().map(i -> i + 1);
		assertThat(actual).isEqualTo(new Left<>(2));
	}

	@Test
	public void shouldMapOnLeftProjectionOfRight() {
		final Either<Integer, String> actual = new Right<Integer, String>("1").left().map(i -> i + 1);
		assertThat(actual).isEqualTo(new Right<>("1"));
	}

	// equals

	@Test
	public void shouldEqualLeftProjectionOfLeftIfObjectIsSame() {
		final LeftProjection<?, ?> l = new Left<>(1).left();
		assertThat(l.equals(l)).isTrue();
	}

	@Test
	public void shouldEqualLeftProjectionOfRightIfObjectIsSame() {
		final LeftProjection<?, ?> l = new Right<>(1).left();
		assertThat(l.equals(l)).isTrue();
	}

	@Test
	public void shouldNotEqualLeftProjectionOfLeftIfObjectIsNull() {
		assertThat(new Left<>(1).left().equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualLeftProjectionOfRightIfObjectIsNull() {
		assertThat(new Right<>(1).left().equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualLeftProjectionOfLeftIfObjectIsOfDifferentType() {
		assertThat(new Left<>(1).left().equals(new Object())).isFalse();
	}

	@Test
	public void shouldNotEqualLeftProjectionOfRightIfObjectIsOfDifferentType() {
		assertThat(new Right<>(1).left().equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualLeftProjectionOfLeft() {
		assertThat(new Left<>(1).left()).isEqualTo(new Left<>(1).left());
	}

	@Test
	public void shouldEqualLeftProjectionOfRight() {
		assertThat(new Right<>(1).left()).isEqualTo(new Right<>(1).left());
	}

	// hashCode

	@Test
	public void shouldHashLeftProjectionOfLeft() {
		assertThat(new Left<>(1).left().hashCode()).isEqualTo(Objects.hashCode(new Right<>(1)));
	}

	@Test
	public void shouldHashLeftProjectionOfRight() {
		assertThat(new Right<>(1).left().hashCode()).isEqualTo(Objects.hashCode(new Left<>(1)));
	}

	// toString

	@Test
	public void shouldConvertLeftProjectionOfLeftToString() {
		assertThat(new Left<>(1).left().toString()).isEqualTo("LeftProjection(Left(1))");
	}

	@Test
	public void shouldConvertLeftProjectionOfRightToString() {
		assertThat(new Right<>(1).left().toString()).isEqualTo("LeftProjection(Right(1))");
	}

	// -- Right

	@Test
	public void shouldReturnTrueWhenCallingIsRightOnRight() {
		assertThat(new Right<>(1).isRight()).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenCallingIsLeftOnRight() {
		assertThat(new Right<>(1).isLeft()).isFalse();
	}

	// unapply

	@Test
	public void shouldUnapplyRight() {
		assertThat(new Right<>(1).unapply()).isEqualTo(Tuple.of(1));
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

	// -- RightProjection

	// get

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowOnGetOnRightProjectionOfLeft() {
		new Left<>(1).right().get();
	}

	@Test
	public void shouldGetOnRightProjectionOfRight() {
		assertThat(new Right<>(1).right().get()).isEqualTo(1);
	}

	// orElse

	@Test
	public void shouldReturnRightWhenOrElseOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElse(2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseOnRightProjectionOfLeft() {
		final Integer actual = new Left<String, Integer>("1").right().orElse(2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseGet(Function)

	@Test
	public void shouldReturnRightWhenOrElseGetGivenFunctionOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElseGet(l -> 2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseGetGivenFunctionOnRightProjectionOfLeft() {
		final Integer actual = new Left<String, Integer>("1").right().orElseGet(l -> 2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseRun

	@Test
	public void shouldReturnRightWhenOrElseRunOnRightProjectionOfRight() {
		final boolean[] actual = new boolean[] { true };
		new Right<String, Integer>(1).right().orElseRun(s -> { actual[0] = false; });
		assertThat(actual[0]).isTrue();
	}

	@Test
	public void shouldReturnOtherWhenOrElseRunOnRightProjectionOfLeft() {
		final boolean[] actual = new boolean[] { false };
		new Left<String, Integer>("1").right().orElseRun(s -> { actual[0] = true; });
		assertThat(actual[0]).isTrue();
	}

	// orElseThrow(Function)

	@Test
	public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElseThrow(str -> new RuntimeException(str));
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOfLeft() {
		new Left<String, Integer>("1").right().orElseThrow(str -> new RuntimeException(str));
	}

	// toOption

	@Test
	public void shouldConvertRightProjectionOfLeftToNone() {
		assertThat(new Left<Integer, String>(0).right().toOption()).isEqualTo(Option.none());
	}

	@Test
	public void shouldConvertRightProjectionOfRightToSome() {
		assertThat(new Right<Integer, String>("1").right().toOption()).isEqualTo(Option.of("1"));
	}

	// toEither

	@Test
	public void shouldConvertRightProjectionOfLeftToEither() {
		final Either<Integer, String> self = new Left<>(1);
		assertThat(self.right().toEither()).isEqualTo(self);
	}

	@Test
	public void shouldConvertRightProjectionOfRightToEither() {
		final Either<Integer, String> self = new Right<>("1");
		assertThat(self.right().toEither()).isEqualTo(self);
	}

	// filter

	@Test
	public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> true).isDefined();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> false).isDefined();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
		final boolean actual = new Left<String, Integer>("1").right().filter(i -> true).isDefined();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
		final boolean actual = new Left<String, Integer>("1").right().filter(i -> false).isDefined();
		assertThat(actual).isFalse();
	}

	// flatMap

	@Test
	public void shouldFlatMapOnRightProjectionOfRight() {
		final Either<String, Integer> actual = new Right<String, Integer>(1).right().flatMap(i -> new Right<>(i + 1));
		assertThat(actual).isEqualTo(new Right<>(2));
	}

	@Test
	public void shouldFlatMapOnRightProjectionOfLeft() {
		final Either<String, Integer> actual = new Left<String, Integer>("1").right().flatMap(i -> new Right<>(i + 1));
		assertThat(actual).isEqualTo(new Left<>("1"));
	}

	// forEach

	@Test
	public void shouldForEachOnRightProjectionOfRight() {
		final List<Integer> actual = new ArrayList<>();
		new Right<String, Integer>(1).right().forEach(i -> actual.add(i));
		assertThat(actual).isEqualTo(Arrays.asList(1));
	}

	@Test
	public void shouldForEachOnRightProjectionOfLeft() {
		final List<Integer> actual = new ArrayList<>();
		new Left<String, Integer>("1").right().forEach(i -> actual.add(i));
		assertThat(actual.isEmpty()).isTrue();
	}

	// map

	@Test
	public void shouldMapOnRightProjectionOfRight() {
		final Either<String, Integer> actual = new Right<String, Integer>(1).right().map(i -> i + 1);
		assertThat(actual).isEqualTo(new Right<>(2));
	}

	@Test
	public void shouldMapOnRightProjectionOfLeft() {
		final Either<String, Integer> actual = new Left<String, Integer>("1").right().map(i -> i + 1);
		assertThat(actual).isEqualTo(new Left<>("1"));
	}

	// equals

	@Test
	public void shouldEqualRightProjectionOfRightIfObjectIsSame() {
		final RightProjection<?, ?> r = new Right<>(1).right();
		assertThat(r.equals(r)).isTrue();
	}

	@Test
	public void shouldEqualRightProjectionOfLeftIfObjectIsSame() {
		final RightProjection<?, ?> r = new Left<>(1).right();
		assertThat(r.equals(r)).isTrue();
	}

	@Test
	public void shouldNotEqualRightProjectionOfRightIfObjectIsNull() {
		assertThat(new Right<>(1).right().equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualRightProjectionOfLeftIfObjectIsNull() {
		assertThat(new Left<>(1).right().equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualRightProjectionOfRightIfObjectIsOfDifferentType() {
		assertThat(new Right<>(1).right().equals(new Object())).isFalse();
	}

	@Test
	public void shouldNotEqualRightProjectionOfLeftIfObjectIsOfDifferentType() {
		assertThat(new Left<>(1).right().equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualRightProjectionOfRight() {
		assertThat(new Right<>(1).right()).isEqualTo(new Right<>(1).right());
	}

	@Test
	public void shouldEqualRightProjectionOfLeft() {
		assertThat(new Left<>(1).right()).isEqualTo(new Left<>(1).right());
	}

	// hashCode

	@Test
	public void shouldHashRightProjectionOfRight() {
		assertThat(new Right<>(1).right().hashCode()).isEqualTo(Objects.hashCode(new Right<>(1)));
	}

	@Test
	public void shouldHashRightProjectionOfLeft() {
		assertThat(new Left<>(1).right().hashCode()).isEqualTo(Objects.hashCode(new Left<>(1)));
	}

	// toString

	@Test
	public void shouldConvertRightProjectionOfLeftToString() {
		assertThat(new Left<>(1).right().toString()).isEqualTo("RightProjection(Left(1))");
	}

	@Test
	public void shouldConvertRightProjectionOfRightToString() {
		assertThat(new Right<>(1).right().toString()).isEqualTo("RightProjection(Right(1))");
	}
}
