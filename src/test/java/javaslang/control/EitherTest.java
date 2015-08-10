/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.control.Either.LeftProjection;
import javaslang.control.Either.RightProjection;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

    // -- Either

    @Test
    public void shouldBimapLeft() {
        final Either<Integer, String> actual = new Left<Integer, String>(1).bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = new Left<>(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBimapRight() {
        final Either<Integer, String> actual = new Right<Integer, String>("1").bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = new Right<>("11");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldSwapLeft() {
        assertThat(new Left<>(1).swap()).isEqualTo(new Right<>(1));
    }

    @Test
    public void shouldSwapRight() {
        assertThat(new Right<>(1).swap()).isEqualTo(new Left<>(1));
    }

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
        final Integer actual = new Left<>(1).left().orElse(2);
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
        final Integer actual = new Left<>(1).left().orElseGet(r -> 2);
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
        new Left<>(1).left().orElseRun(s -> actual[0] = false);
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfRight() {
        final boolean[] actual = new boolean[] { false };
        new Right<>("1").left().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
        final Integer actual = new Left<Integer, String>(1).left().orElseThrow(s -> new RuntimeException(s));
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
        new Right<>("1").left().orElseThrow(s -> new RuntimeException(s));
    }

    // toOption

    @Test
    public void shouldConvertLeftProjectionOfLeftToSome() {
        assertThat(new Left<>(1).left().toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToNone() {
        assertThat(new Right<>("x").left().toOption()).isEqualTo(Option.none());
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

    // toJavaOptional

    @Test
    public void shouldConvertLeftProjectionOfLeftToJavaOptional() {
        assertThat(new Left<>(1).left().toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToJavaOptional() {
        assertThat(new Right<Integer, String>("x").left().toJavaOptional()).isEqualTo(Optional.empty());
    }

    // filter

    @Test
    public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
        final boolean actual = new Left<>(1).left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
        new Left<>(1).left().filter(i -> false);
    }

    @Test
    public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
        final boolean actual = new Right<>("1").left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
        final boolean actual = new Right<>("1").left().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatten(Function)

    @Test
    public void shouldFlattenLeftWithFunctionUsingLeftProjection() {
        assertThat(new Left<>(1).left().flatten().toEither()).isEqualTo(new Left<>(1));
    }

    @Test
    public void shouldFlattenRightWithFunctionUsingLeftProjection() {
        assertThat(new Right<Integer, String>("1").left().flatten().toEither()).isEqualTo(new Right<>("1"));
    }

    // flatMap

    @Test
    public void shouldFlatMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = new Left<Integer, String>(1).left().flatMap(i -> new Left<Integer, String>(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(new Left<>(2));
    }

    @Test
    public void shouldFlatMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = new Right<Integer, String>("1").left().flatMap(i -> new Left<Integer, String>(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(new Right<>("1"));
    }

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfLeftProjectionOfLeft() {
        assertThat(new Left<>(1).left().exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfLeftProjectionOfLeft() {
        assertThat(new Left<>(1).left().exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfLeftProjectionOfRight() {
        assertThat(new Left<>(1).right().exists(e -> true)).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfLeftProjectionOfLeft() {
        assertThat(new Left<>(1).left().forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfLeftProjectionOfLeft() {
        assertThat(new Left<>(1).left().forAll(i -> i == 2)).isFalse();
    }

    @Test// a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfLeftProjectionOfRight() {
        assertThat(new Left<>(1).right().forAll(e -> true)).isTrue();
    }

    // forEach

    @Test
    public void shouldForEachOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        new Left<>(1).left().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        new Right<Integer, String>("1").left().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = new Left<Integer, String>(1).left().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(new Left<>(1));
    }

    @Test
    public void shouldPeekOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = new Right<Integer, String>("1").left().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(new Right<>("1"));
    }

    // map

    @Test
    public void shouldMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = new Left<Integer, String>(1).left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(new Left<>(2));
    }

    @Test
    public void shouldMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = new Right<Integer, String>("1").left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(new Right<>("1"));
    }

    // iterator

    @Test
    public void shouldReturnIteratorOfLeftOfLeftProjection() {
        assertThat((Iterator<Integer>) new Left<>(1).left().iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfRightOfLeftProjection() {
        assertThat((Iterator<Object>) new Right<>(1).left().iterator()).isNotNull();
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
        new Right<String, Integer>(1).right().orElseRun(s -> {
            actual[0] = false;
        });
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnRightProjectionOfLeft() {
        final boolean[] actual = new boolean[] { false };
        new Left<String, Integer>("1").right().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfRight() {
        final Integer actual = new Right<String, Integer>(1).right().orElseThrow(s -> new RuntimeException(s));
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOfLeft() {
        new Left<String, Integer>("1").right().orElseThrow(i -> new RuntimeException(String.valueOf(i)));
    }

    // toOption

    @Test
    public void shouldConvertRightProjectionOfLeftToNone() {
        assertThat(new Left<>(0).right().toOption()).isEqualTo(Option.none());
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

    // toJavaOptional

    @Test
    public void shouldConvertRightProjectionOfLeftToJavaOptional() {
        assertThat(new Left<>(0).right().toJavaOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldConvertRightProjectionOfRightToJavaOptional() {
        assertThat(new Right<Integer, String>("1").right().toJavaOptional()).isEqualTo(Optional.of("1"));
    }

    // filter

    @Test
    public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
        final boolean actual = new Right<String, Integer>(1).right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
        new Right<String, Integer>(1).right().filter(i -> false);
    }

    @Test
    public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
        final boolean actual = new Left<String, Integer>("1").right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
        final boolean actual = new Left<String, Integer>("1").right().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatten(Function)

    @Test
    public void shouldFlattenRightWithFunctionUsingRightProjection() {
        assertThat(new Right<String, Integer>(1).right().flatten().toEither()).isEqualTo(new Right<>(1));
    }

    @Test
    public void shouldFlattenLeftWithFunctionUsingRightProjection() {
        assertThat(new Left<String, Integer>("1").right().flatten().toEither()).isEqualTo(new Left<>("1"));
    }

    // flatMap

    @Test
    public void shouldFlatMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = new Right<String, Integer>(1).right().flatMap(i -> new Right<String, Integer>(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(new Right<>(2));
    }

    @Test
    public void shouldFlatMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = new Left<String, Integer>("1").right().flatMap(i -> new Right<String, Integer>(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(new Left<>("1"));
    }

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfRightProjectionOfRight() {
        assertThat(new Right<>(1).right().exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfRightProjectionOfRight() {
        assertThat(new Right<>(1).right().exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfRightProjectionOfLeft() {
        assertThat(new Right<>(1).left().exists(e -> true)).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfRightProjectionOfRight() {
        assertThat(new Right<>(1).right().forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfRightProjectionOfRight() {
        assertThat(new Right<>(1).right().forAll(i -> i == 2)).isFalse();
    }

    @Test // a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfRightProjectionOfLeft() {
        assertThat(new Right<>(1).left().forAll(e -> true)).isTrue();
    }

    // forEach

    @Test
    public void shouldForEachOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        new Right<String, Integer>(1).right().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        new Left<String, Integer>("1").right().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = new Right<String, Integer>(1).right().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(new Right<>(1));
    }

    @Test
    public void shouldPeekOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = new Left<String, Integer>("1").right().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(new Left<String, Integer>("1"));
    }

    // map

    @Test
    public void shouldMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = new Right<String, Integer>(1).right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(new Right<>(2));
    }

    @Test
    public void shouldMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = new Left<String, Integer>("1").right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(new Left<>("1"));
    }

    // iterator

    @Test
    public void shouldReturnIteratorOfRightOfRightProjection() {
        assertThat((Iterator<Integer>) new Right<>(1).right().iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfLeftOfRightProjection() {
        assertThat((Iterator<Object>) new Left<>(1).right().iterator()).isNotNull();
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
