/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Either.LeftProjection;
import javaslang.control.Either.RightProjection;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

    // -- Either

    @Test
    public void shouldBimapLeft() {
        final Either<Integer, String> actual = Either.<Integer, String>left(1).bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.left(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBimapRight() {
        final Either<Integer, String> actual = Either.<Integer, String>right("1").bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.right("11");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFoldLeft() {
        final String value = Either.left("L").fold(l -> l + "+", r -> r + "-");
        assertThat(value).isEqualTo("L+");
    }

    @Test
    public void shouldFoldRight() {
        final String value = Either.right("R").fold(l -> l + "-", r -> r + "+");
        assertThat(value).isEqualTo("R+");
    }

    @Test
    public void shouldSwapLeft() {
        assertThat(Either.left(1).swap()).isEqualTo(Either.right(1));
    }

    @Test
    public void shouldSwapRight() {
        assertThat(Either.right(1).swap()).isEqualTo(Either.left(1));
    }

    // -- Left

    @Test
    public void shouldReturnTrueWhenCallingIsLeftOnLeft() {
        assertThat(Either.left(1).isLeft()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsRightOnLeft() {
        assertThat(Either.left(1).isRight()).isFalse();
    }

    // equals

    @Test
    public void shouldEqualLeftIfObjectIsSame() {
        final Either<Integer, ?> left = Either.left(1);
        assertThat(left.equals(left)).isTrue();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsNull() {
        assertThat(Either.left(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsOfDifferentType() {
        assertThat(Either.left(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualLeft() {
        assertThat(Either.left(1)).isEqualTo(Either.left(1));
    }

    // hashCode

    @Test
    public void shouldHashLeft() {
        assertThat(Either.left(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertLeftToString() {
        assertThat(Either.left(1).toString()).isEqualTo("Left(1)");
    }

    // -- LeftProjection

    // get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnGetOnLeftProjectionOfRight() {
        Either.right(1).left().get();
    }

    @Test
    public void shouldGetOnLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().get()).isEqualTo(1);
    }

    // orElse

    @Test
    public void shouldReturnLeftWhenOrElseOnLeftProjectionOfLeft() {
        final Integer actual = Either.left(1).left().getOrElse(2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseOnLeftProjectionOfRight() {
        final Integer actual = Either.<Integer, String>right("1").left().getOrElse(2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseGet(Function)

    @Test
    public void shouldReturnLeftWhenOrElseGetGivenFunctionOnLeftProjectionOfLeft() {
        final Integer actual = Either.left(1).left().orElseGet(r -> 2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseGetGivenFunctionOnLeftProjectionOfRight() {
        final Integer actual = Either.<Integer, String>right("1").left().orElseGet(r -> 2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseRun

    @Test
    public void shouldReturnLeftWhenOrElseRunOnLeftProjectionOfLeft() {
        final boolean[] actual = new boolean[] { true };
        Either.left(1).left().orElseRun(s -> actual[0] = false);
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfRight() {
        final boolean[] actual = new boolean[] { false };
        Either.right("1").left().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
        final Integer actual = Either.<Integer, String>left(1).left().orElseThrow(s -> new RuntimeException(s));
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
        Either.right("1").left().orElseThrow(s -> new RuntimeException(s));
    }

    // toOption

    @Test
    public void shouldConvertLeftProjectionOfLeftToSome() {
        assertThat(Either.left(1).left().toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToNone() {
        assertThat(Either.right("x").left().toOption()).isEqualTo(Option.none());
    }

    // toEither

    @Test
    public void shouldConvertLeftProjectionOfLeftToEither() {
        final Either<Integer, String> self = Either.left(1);
        assertThat(self.left().toEither()).isEqualTo(self);
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToEither() {
        final Either<Integer, String> self = Either.right("1");
        assertThat(self.left().toEither()).isEqualTo(self);
    }

    // toJavaOptional

    @Test
    public void shouldConvertLeftProjectionOfLeftToJavaOptional() {
        assertThat(Either.left(1).left().toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToJavaOptional() {
        assertThat(Either.<Integer, String>right("x").left().toJavaOptional()).isEqualTo(Optional.empty());
    }

    // filter

    @Test
    public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
        final boolean actual = Either.left(1).left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
        assertThat(Either.left(1).left().filter(i -> false)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
        final boolean actual = Either.right("1").left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
        final boolean actual = Either.right("1").left().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatMap

    @Test
    public void shouldFlatMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = Either.<Integer, String>left(1).left().flatMap(i -> Either.<Integer, String>left(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(Either.left(2));
    }

    @Test
    public void shouldFlatMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = Either.<Integer, String>right("1").left().flatMap(i -> Either.<Integer, String>left(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(Either.right("1"));
    }

    @Test
    public void shouldFlatMapLeftProjectionOfRightOnLeftProjectionOfLeft() {
        final Either<String, String> good = Either.left("good");
        final Either<String, String> bad = Either.right("bad");
        final LeftProjection<Tuple2<String, String>, String> actual = good.left().flatMap(g -> bad.left().map(b -> Tuple.of(g, b)));
        assertThat(actual.toEither()).isEqualTo(Either.right("bad"));

    }

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfLeftProjectionOfRight() {
        assertThat(Either.left(1).right().exists(e -> true)).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().forAll(i -> i == 2)).isFalse();
    }

    @Test// a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfLeftProjectionOfRight() {
        assertThat(Either.left(1).right().forAll(e -> true)).isTrue();
    }

    // forEach

    @Test
    public void shouldForEachOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        Either.left(1).left().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        Either.<Integer, String>right("1").left().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = Either.<Integer, String>left(1).left().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(Either.left(1));
    }

    @Test
    public void shouldPeekOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = Either.<Integer, String>right("1").left().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(Either.right("1"));
    }

    // map

    @Test
    public void shouldMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = Either.<Integer, String>left(1).left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Either.left(2));
    }

    @Test
    public void shouldMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = Either.<Integer, String>right("1").left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Either.right("1"));
    }

    // iterator

    @Test
    public void shouldReturnIteratorOfLeftOfLeftProjection() {
        assertThat((Iterator<Integer>) Either.left(1).left().iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfRightOfLeftProjection() {
        assertThat((Iterator<Object>) Either.right(1).left().iterator()).isNotNull();
    }

    // equals

    @Test
    public void shouldEqualLeftProjectionOfLeftIfObjectIsSame() {
        final LeftProjection<?, ?> l = Either.left(1).left();
        assertThat(l.equals(l)).isTrue();
    }

    @Test
    public void shouldEqualLeftProjectionOfRightIfObjectIsSame() {
        final LeftProjection<?, ?> l = Either.right(1).left();
        assertThat(l.equals(l)).isTrue();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfLeftIfObjectIsNull() {
        assertThat(Either.left(1).left().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfRightIfObjectIsNull() {
        assertThat(Either.right(1).left().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfLeftIfObjectIsOfDifferentType() {
        assertThat(Either.left(1).left().equals(new Object())).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfRightIfObjectIsOfDifferentType() {
        assertThat(Either.right(1).left().equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualLeftProjectionOfLeft() {
        assertThat(Either.left(1).left()).isEqualTo(Either.left(1).left());
    }

    @Test
    public void shouldEqualLeftProjectionOfRight() {
        assertThat(Either.right(1).left()).isEqualTo(Either.right(1).left());
    }

    // hashCode

    @Test
    public void shouldHashLeftProjectionOfLeft() {
        assertThat(Either.left(1).left().hashCode()).isEqualTo(Objects.hashCode(Either.right(1)));
    }

    @Test
    public void shouldHashLeftProjectionOfRight() {
        assertThat(Either.right(1).left().hashCode()).isEqualTo(Objects.hashCode(Either.left(1)));
    }

    // toString

    @Test
    public void shouldConvertLeftProjectionOfLeftToString() {
        assertThat(Either.left(1).left().toString()).isEqualTo("LeftProjection(Left(1))");
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToString() {
        assertThat(Either.right(1).left().toString()).isEqualTo("LeftProjection(Right(1))");
    }

    // -- Right

    @Test
    public void shouldReturnTrueWhenCallingIsRightOnRight() {
        assertThat(Either.right(1).isRight()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsLeftOnRight() {
        assertThat(Either.right(1).isLeft()).isFalse();
    }

    // equals

    @Test
    public void shouldEqualRightIfObjectIsSame() {
        final Either<?, ?> right = Either.right(1);
        assertThat(right.equals(right)).isTrue();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsNull() {
        assertThat(Either.right(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsOfDifferentType() {
        assertThat(Either.right(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualRight() {
        assertThat(Either.right(1)).isEqualTo(Either.right(1));
    }

    // hashCode

    @Test
    public void shouldHashRight() {
        assertThat(Either.right(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertRightToString() {
        assertThat(Either.right(1).toString()).isEqualTo("Right(1)");
    }

    // -- RightProjection

    // get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnGetOnRightProjectionOfLeft() {
        Either.left(1).right().get();
    }

    @Test
    public void shouldGetOnRightProjectionOfRight() {
        assertThat(Either.right(1).right().get()).isEqualTo(1);
    }

    // orElse

    @Test
    public void shouldReturnRightWhenOrElseOnRightProjectionOfRight() {
        final Integer actual = Either.<String, Integer>right(1).right().getOrElse(2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseOnRightProjectionOfLeft() {
        final Integer actual = Either.<String, Integer>left("1").right().getOrElse(2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseGet(Function)

    @Test
    public void shouldReturnRightWhenOrElseGetGivenFunctionOnRightProjectionOfRight() {
        final Integer actual = Either.<String, Integer>right(1).right().orElseGet(l -> 2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseGetGivenFunctionOnRightProjectionOfLeft() {
        final Integer actual = Either.<String, Integer>left("1").right().orElseGet(l -> 2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseRun

    @Test
    public void shouldReturnRightWhenOrElseRunOnRightProjectionOfRight() {
        final boolean[] actual = new boolean[] { true };
        Either.<String, Integer>right(1).right().orElseRun(s -> {
            actual[0] = false;
        });
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnRightProjectionOfLeft() {
        final boolean[] actual = new boolean[] { false };
        Either.<String, Integer>left("1").right().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfRight() {
        final Integer actual = Either.<String, Integer>right(1).right().orElseThrow(s -> new RuntimeException(s));
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOfLeft() {
        Either.<String, Integer>left("1").right().orElseThrow(i -> new RuntimeException(String.valueOf(i)));
    }

    // toOption

    @Test
    public void shouldConvertRightProjectionOfLeftToNone() {
        assertThat(Either.left(0).right().toOption()).isEqualTo(Option.none());
    }

    @Test
    public void shouldConvertRightProjectionOfRightToSome() {
        assertThat(Either.<Integer, String>right("1").right().toOption()).isEqualTo(Option.of("1"));
    }

    // toEither

    @Test
    public void shouldConvertRightProjectionOfLeftToEither() {
        final Either<Integer, String> self = Either.left(1);
        assertThat(self.right().toEither()).isEqualTo(self);
    }

    @Test
    public void shouldConvertRightProjectionOfRightToEither() {
        final Either<Integer, String> self = Either.right("1");
        assertThat(self.right().toEither()).isEqualTo(self);
    }

    // toJavaOptional

    @Test
    public void shouldConvertRightProjectionOfLeftToJavaOptional() {
        assertThat(Either.left(0).right().toJavaOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldConvertRightProjectionOfRightToJavaOptional() {
        assertThat(Either.<Integer, String>right("1").right().toJavaOptional()).isEqualTo(Optional.of("1"));
    }

    // filter

    @Test
    public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
        final boolean actual = Either.<String, Integer>right(1).right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
        assertThat(Either.<String, Integer>right(1).right().filter(i -> false)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
        final boolean actual = Either.<String, Integer>left("1").right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
        final boolean actual = Either.<String, Integer>left("1").right().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatMap

    @Test
    public void shouldFlatMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = Either.<String, Integer>right(1).right().flatMap(i -> Either.<String, Integer>right(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(Either.right(2));
    }

    @Test
    public void shouldFlatMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = Either.<String, Integer>left("1").right().flatMap(i -> Either.<String, Integer>right(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(Either.left("1"));
    }

    @Test
    public void shouldFlatMapRightProjectionOfLeftOnRightProjectionOfRight() {
        final Either<String, String> good = Either.right("good");
        final Either<String, String> bad = Either.left("bad");
        final RightProjection<String, Tuple2<String, String>> actual = good.right().flatMap(g -> bad.right().map(b -> Tuple.of(g, b)));
        assertThat(actual.toEither()).isEqualTo(Either.left("bad"));
    }

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfRightProjectionOfRight() {
        assertThat(Either.right(1).right().exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfRightProjectionOfRight() {
        assertThat(Either.right(1).right().exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfRightProjectionOfLeft() {
        assertThat(Either.right(1).left().exists(e -> true)).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfRightProjectionOfRight() {
        assertThat(Either.right(1).right().forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfRightProjectionOfRight() {
        assertThat(Either.right(1).right().forAll(i -> i == 2)).isFalse();
    }

    @Test // a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfRightProjectionOfLeft() {
        assertThat(Either.right(1).left().forAll(e -> true)).isTrue();
    }

    // forEach

    @Test
    public void shouldForEachOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        Either.<String, Integer>right(1).right().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        Either.<String, Integer>left("1").right().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = Either.<String, Integer>right(1).right().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(Either.right(1));
    }

    @Test
    public void shouldPeekOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = Either.<String, Integer>left("1").right().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(Either.<String, Integer>left("1"));
    }

    // map

    @Test
    public void shouldMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = Either.<String, Integer>right(1).right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Either.right(2));
    }

    @Test
    public void shouldMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = Either.<String, Integer>left("1").right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Either.left("1"));
    }

    // iterator

    @Test
    public void shouldReturnIteratorOfRightOfRightProjection() {
        assertThat((Iterator<Integer>) Either.right(1).right().iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfLeftOfRightProjection() {
        assertThat((Iterator<Object>) Either.left(1).right().iterator()).isNotNull();
    }

    // equals

    @Test
    public void shouldEqualRightProjectionOfRightIfObjectIsSame() {
        final RightProjection<?, ?> r = Either.right(1).right();
        assertThat(r.equals(r)).isTrue();
    }

    @Test
    public void shouldEqualRightProjectionOfLeftIfObjectIsSame() {
        final RightProjection<?, ?> r = Either.left(1).right();
        assertThat(r.equals(r)).isTrue();
    }

    @Test
    public void shouldNotEqualRightProjectionOfRightIfObjectIsNull() {
        assertThat(Either.right(1).right().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfLeftIfObjectIsNull() {
        assertThat(Either.left(1).right().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfRightIfObjectIsOfDifferentType() {
        assertThat(Either.right(1).right().equals(new Object())).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfLeftIfObjectIsOfDifferentType() {
        assertThat(Either.left(1).right().equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualRightProjectionOfRight() {
        assertThat(Either.right(1).right()).isEqualTo(Either.right(1).right());
    }

    @Test
    public void shouldEqualRightProjectionOfLeft() {
        assertThat(Either.left(1).right()).isEqualTo(Either.left(1).right());
    }

    // hashCode

    @Test
    public void shouldHashRightProjectionOfRight() {
        assertThat(Either.right(1).right().hashCode()).isEqualTo(Objects.hashCode(Either.right(1)));
    }

    @Test
    public void shouldHashRightProjectionOfLeft() {
        assertThat(Either.left(1).right().hashCode()).isEqualTo(Objects.hashCode(Either.left(1)));
    }

    // toString

    @Test
    public void shouldConvertRightProjectionOfLeftToString() {
        assertThat(Either.left(1).right().toString()).isEqualTo("RightProjection(Left(1))");
    }

    @Test
    public void shouldConvertRightProjectionOfRightToString() {
        assertThat(Either.right(1).right().toString()).isEqualTo("RightProjection(Right(1))");
    }
}
