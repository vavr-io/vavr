/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javaslang.AssertionsExtensions;
import javaslang.AssertionsExtensions.CheckedRunnable;
import javaslang.monad.Either.Left;
import javaslang.monad.Either.LeftProjection;
import javaslang.monad.Either.Right;
import javaslang.monad.Either.RightProjection;

import org.junit.Test;

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

	@Test
	public void shouldThrowOnGetOnLeftProjectionOfRight() {
		AssertionsExtensions.assertThat((() -> new Right<>(1).left().get())).isThrowing(NoSuchElementException.class,
				"Either.left().get() on Right");
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

	// orElseGet

	@Test
	public void shouldReturnLeftWhenOrElseGetOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElseGet(() -> 2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseGetOnLeftProjectionOfRight() {
		final Integer actual = new Right<Integer, String>("1").left().orElseGet(() -> 2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseThrow(Supplier)

	@Test
	public void shouldReturnLeftWhenOrElseThrowWithSupplierOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElseThrow(() -> new RuntimeException("x"));
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldThrowWhenOrElseThrowWithSupplierOnLeftProjectionOfRight() {
		final CheckedRunnable actual = () -> new Right<Integer, String>("1").left().orElseThrow(
				() -> new RuntimeException("x"));
		AssertionsExtensions.assertThat(actual).isThrowing(RuntimeException.class, "x");
	}

	// orElseThrow(Function)

	@Test
	public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
		final Integer actual = new Left<Integer, String>(1).left().orElseThrow(str -> new RuntimeException(str));
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
		final CheckedRunnable actual = () -> new Right<Integer, String>("1").left().orElseThrow(
				str -> new RuntimeException(str));
		AssertionsExtensions.assertThat(actual).isThrowing(RuntimeException.class, "1");
	}

	// filter

	@Test
	public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
		final boolean actual = new Left<Integer, String>(1).left().filter(i -> true).isPresent();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
		final boolean actual = new Left<Integer, String>(1).left().filter(i -> false).isPresent();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
		final boolean actual = new Right<Integer, String>("1").left().filter(i -> true).isPresent();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
		final boolean actual = new Right<Integer, String>("1").left().filter(i -> false).isPresent();
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

	@Test
	public void shouldThrowOnGetOnRightProjectionOfLeft() {
		AssertionsExtensions.assertThat((() -> new Left<>(1).right().get())).isThrowing(NoSuchElementException.class,
				"Either.right().get() on Left");
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

	// orElseGet

	@Test
	public void shouldReturnRightWhenOrElseGetOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElseGet(() -> 2);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldReturnOtherWhenOrElseGetOnRightProjectionOfLeft() {
		final Integer actual = new Left<String, Integer>("1").right().orElseGet(() -> 2);
		assertThat(actual).isEqualTo(2);
	}

	// orElseThrow(Supplier)

	@Test
	public void shouldReturnRightWhenOrElseThrowWithSupplierOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElseThrow(() -> new RuntimeException("x"));
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldThrowWhenOrElseThrowWithSupplierOnRightProjectionOfLeft() {
		final CheckedRunnable actual = () -> new Left<String, Integer>("1").right().orElseThrow(
				() -> new RuntimeException("x"));
		AssertionsExtensions.assertThat(actual).isThrowing(RuntimeException.class, "x");
	}

	// orElseThrow(Function)

	@Test
	public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfRight() {
		final Integer actual = new Right<String, Integer>(1).right().orElseThrow(str -> new RuntimeException(str));
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOfLeft() {
		final CheckedRunnable actual = () -> new Left<String, Integer>("1").right().orElseThrow(
				str -> new RuntimeException(str));
		AssertionsExtensions.assertThat(actual).isThrowing(RuntimeException.class, "1");
	}

	// filter

	@Test
	public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> true).isPresent();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
		final boolean actual = new Right<String, Integer>(1).right().filter(i -> false).isPresent();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
		final boolean actual = new Left<String, Integer>("1").right().filter(i -> true).isPresent();
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
		final boolean actual = new Left<String, Integer>("1").right().filter(i -> false).isPresent();
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
