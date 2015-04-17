/*     / \____  _    ______   _____ / \____   ____  _____
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
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

    // -- Either

    @Test
    public void shouldBimapLeft() {
        final Either<Integer, String> actual = Left.<Integer, String> of(1).bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Left.of(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBimapRight() {
        final Either<Integer, String> actual = Right.<Integer, String> of("1").bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Right.of("11");
        assertThat(actual).isEqualTo(expected);
    }

    // -- Left

    @Test
    public void shouldReturnTrueWhenCallingIsLeftOnLeft() {
        assertThat(Left.of(1).isLeft()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsRightOnLeft() {
        assertThat(Left.of(1).isRight()).isFalse();
    }

    // unapply

    @Test
    public void shouldUnapplyLeft() {
        assertThat(Left.of(1).unapply()).isEqualTo(Tuple.of(1));
    }

    // equals

    @Test
    public void shouldEqualLeftIfObjectIsSame() {
        final Left<?, ?> left = Left.of(1);
        assertThat(left.equals(left)).isTrue();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsNull() {
        assertThat(Left.of(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsOfDifferentType() {
        assertThat(Left.of(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualLeft() {
        assertThat(Left.of(1)).isEqualTo(Left.of(1));
    }

    // hashCode

    @Test
    public void shouldHashLeft() {
        assertThat(Left.of(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertLeftToString() {
        assertThat(Left.of(1).toString()).isEqualTo("Left(1)");
    }

    // -- LeftProjection

    // get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnGetOnLeftProjectionOfRight() {
        Right.of(1).left().get();
    }

    @Test
    public void shouldGetOnLeftProjectionOfLeft() {
        assertThat(Left.of(1).left().get()).isEqualTo(1);
    }

    // orElse

    @Test
    public void shouldReturnLeftWhenOrElseOnLeftProjectionOfLeft() {
        final Integer actual = Left.<Integer, String> of(1).left().orElse(2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseOnLeftProjectionOfRight() {
        final Integer actual = Right.<Integer, String> of("1").left().orElse(2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseGet(Function)

    @Test
    public void shouldReturnLeftWhenOrElseGetGivenFunctionOnLeftProjectionOfLeft() {
        final Integer actual = Left.<Integer, String> of(1).left().orElseGet(r -> 2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseGetGivenFunctionOnLeftProjectionOfRight() {
        final Integer actual = Right.<Integer, String> of("1").left().orElseGet(r -> 2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseRun

    @Test
    public void shouldReturnLeftWhenOrElseRunOnLeftProjectionOfLeft() {
        final boolean[] actual = new boolean[]{true};
        Left.<Integer, String> of(1).left().orElseRun(s -> actual[0] = false);
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfRight() {
        final boolean[] actual = new boolean[]{false};
        Right.<Integer, String> of("1").left().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
        final Integer actual = Left.<Integer, String> of(1).left().orElseThrow(RuntimeException::new);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
        Right.<Integer, String> of("1").left().orElseThrow(RuntimeException::new);
    }

    // toOption

    @Test
    public void shouldConvertLeftProjectionOfLeftToSome() {
        assertThat(Left.<Integer, String> of(1).left().toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToNone() {
        assertThat(Right.<Integer, String> of("x").left().toOption()).isEqualTo(Option.none());
    }

    // toEither

    @Test
    public void shouldConvertLeftProjectionOfLeftToEither() {
        final Either<Integer, String> self = Left.of(1);
        assertThat(self.left().toEither()).isEqualTo(self);
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToEither() {
        final Either<Integer, String> self = Right.of("1");
        assertThat(self.left().toEither()).isEqualTo(self);
    }

    // toJavaOptional

    @Test
    public void shouldConvertLeftProjectionOfLeftToJavaOptional() {
        assertThat(Left.<Integer, String> of(1).left().toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToJavaOptional() {
        assertThat(Right.<Integer, String> of("x").left().toJavaOptional()).isEqualTo(Optional.empty());
    }

    // filter

    @Test
    public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
        final boolean actual = Left.<Integer, String> of(1).left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
        Left.<Integer, String> of(1).left().filter(i -> false).toOption();
    }

    @Test
    public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
        final boolean actual = Right.<Integer, String> of("1").left().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
        final boolean actual = Right.<Integer, String> of("1").left().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatMap

    @Test
    public void shouldFlatMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = Left.<Integer, String> of(1).left().flatMap(i -> Left.<Integer, String> of(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(Left.of(2));
    }

    @Test
    public void shouldFlatMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = Right.<Integer, String> of("1").left().flatMap(i -> Left.<Integer, String> of(i + 1).left()).toEither();
        assertThat(actual).isEqualTo(Right.of("1"));
    }

    // forEach

    @Test
    public void shouldForEachOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        Left.<Integer, String> of(1).left().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        Right.<Integer, String> of("1").left().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnLeftProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = Left.<Integer, String> of(1).left().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(Left.<Integer, String> of(1));
    }

    @Test
    public void shouldPeekOnLeftProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<Integer, String> testee = Right.<Integer, String> of("1").left().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(Right.<Integer, String> of("1"));
    }

    // map

    @Test
    public void shouldMapOnLeftProjectionOfLeft() {
        final Either<Integer, String> actual = Left.<Integer, String> of(1).left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Left.of(2));
    }

    @Test
    public void shouldMapOnLeftProjectionOfRight() {
        final Either<Integer, String> actual = Right.<Integer, String> of("1").left().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Right.of("1"));
    }

    // equals

    @Test
    public void shouldEqualLeftProjectionOfLeftIfObjectIsSame() {
        final LeftProjection<?, ?> l = Left.of(1).left();
        assertThat(l.equals(l)).isTrue();
    }

    @Test
    public void shouldEqualLeftProjectionOfRightIfObjectIsSame() {
        final LeftProjection<?, ?> l = Right.of(1).left();
        assertThat(l.equals(l)).isTrue();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfLeftIfObjectIsNull() {
        assertThat(Left.of(1).left().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfRightIfObjectIsNull() {
        assertThat(Right.of(1).left().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfLeftIfObjectIsOfDifferentType() {
        assertThat(Left.of(1).left().equals(new Object())).isFalse();
    }

    @Test
    public void shouldNotEqualLeftProjectionOfRightIfObjectIsOfDifferentType() {
        assertThat(Right.of(1).left().equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualLeftProjectionOfLeft() {
        assertThat(Left.of(1).left()).isEqualTo(Left.of(1).left());
    }

    @Test
    public void shouldEqualLeftProjectionOfRight() {
        assertThat(Right.of(1).left()).isEqualTo(Right.of(1).left());
    }

    // hashCode

    @Test
    public void shouldHashLeftProjectionOfLeft() {
        assertThat(Left.of(1).left().hashCode()).isEqualTo(Objects.hashCode(Right.of(1)));
    }

    @Test
    public void shouldHashLeftProjectionOfRight() {
        assertThat(Right.of(1).left().hashCode()).isEqualTo(Objects.hashCode(Left.of(1)));
    }

    // toString

    @Test
    public void shouldConvertLeftProjectionOfLeftToString() {
        assertThat(Left.of(1).left().toString()).isEqualTo("LeftProjection(Left(1))");
    }

    @Test
    public void shouldConvertLeftProjectionOfRightToString() {
        assertThat(Right.of(1).left().toString()).isEqualTo("LeftProjection(Right(1))");
    }

    // -- Right

    @Test
    public void shouldReturnTrueWhenCallingIsRightOnRight() {
        assertThat(Right.of(1).isRight()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsLeftOnRight() {
        assertThat(Right.of(1).isLeft()).isFalse();
    }

    // unapply

    @Test
    public void shouldUnapplyRight() {
        assertThat(Right.of(1).unapply()).isEqualTo(Tuple.of(1));
    }

    // equals

    @Test
    public void shouldEqualRightIfObjectIsSame() {
        final Right<?, ?> right = Right.of(1);
        assertThat(right.equals(right)).isTrue();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsNull() {
        assertThat(Right.of(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsOfDifferentType() {
        assertThat(Right.of(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualRight() {
        assertThat(Right.of(1)).isEqualTo(Right.of(1));
    }

    // hashCode

    @Test
    public void shouldHashRight() {
        assertThat(Right.of(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertRightToString() {
        assertThat(Right.of(1).toString()).isEqualTo("Right(1)");
    }

    // -- RightProjection

    // get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnGetOnRightProjectionOfLeft() {
        Left.of(1).right().get();
    }

    @Test
    public void shouldGetOnRightProjectionOfRight() {
        assertThat(Right.of(1).right().get()).isEqualTo(1);
    }

    // orElse

    @Test
    public void shouldReturnRightWhenOrElseOnRightProjectionOfRight() {
        final Integer actual = Right.<String, Integer> of(1).right().orElse(2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseOnRightProjectionOfLeft() {
        final Integer actual = Left.<String, Integer> of("1").right().orElse(2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseGet(Function)

    @Test
    public void shouldReturnRightWhenOrElseGetGivenFunctionOnRightProjectionOfRight() {
        final Integer actual = Right.<String, Integer> of(1).right().orElseGet(l -> 2);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldReturnOtherWhenOrElseGetGivenFunctionOnRightProjectionOfLeft() {
        final Integer actual = Left.<String, Integer> of("1").right().orElseGet(l -> 2);
        assertThat(actual).isEqualTo(2);
    }

    // orElseRun

    @Test
    public void shouldReturnRightWhenOrElseRunOnRightProjectionOfRight() {
        final boolean[] actual = new boolean[]{true};
        Right.<String, Integer> of(1).right().orElseRun(s -> {
            actual[0] = false;
        });
        assertThat(actual[0]).isTrue();
    }

    @Test
    public void shouldReturnOtherWhenOrElseRunOnRightProjectionOfLeft() {
        final boolean[] actual = new boolean[]{false};
        Left.<String, Integer> of("1").right().orElseRun(s -> {
            actual[0] = true;
        });
        assertThat(actual[0]).isTrue();
    }

    // orElseThrow(Function)

    @Test
    public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfRight() {
        final Integer actual = Right.<String, Integer> of(1).right().orElseThrow(RuntimeException::new);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOfLeft() {
        Left.<String, Integer> of("1").right().orElseThrow(RuntimeException::new);
    }

    // toOption

    @Test
    public void shouldConvertRightProjectionOfLeftToNone() {
        assertThat(Left.<Integer, String> of(0).right().toOption()).isEqualTo(Option.none());
    }

    @Test
    public void shouldConvertRightProjectionOfRightToSome() {
        assertThat(Right.<Integer, String> of("1").right().toOption()).isEqualTo(Option.of("1"));
    }

    // toEither

    @Test
    public void shouldConvertRightProjectionOfLeftToEither() {
        final Either<Integer, String> self = Left.of(1);
        assertThat(self.right().toEither()).isEqualTo(self);
    }

    @Test
    public void shouldConvertRightProjectionOfRightToEither() {
        final Either<Integer, String> self = Right.of("1");
        assertThat(self.right().toEither()).isEqualTo(self);
    }

    // toJavaOptional

    @Test
    public void shouldConvertRightProjectionOfLeftToJavaOptional() {
        assertThat(Left.<Integer, String> of(0).right().toJavaOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldConvertRightProjectionOfRightToJavaOptional() {
        assertThat(Right.<Integer, String> of("1").right().toJavaOptional()).isEqualTo(Optional.of("1"));
    }

    // filter

    @Test
    public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
        final boolean actual = Right.<String, Integer> of(1).right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
        Right.<String, Integer> of(1).right().filter(i -> false).toOption();
    }

    @Test
    public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
        final boolean actual = Left.<String, Integer> of("1").right().filter(i -> true).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
        final boolean actual = Left.<String, Integer> of("1").right().filter(i -> false).toOption().isDefined();
        assertThat(actual).isFalse();
    }

    // flatMap

    @Test
    public void shouldFlatMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = Right.<String, Integer> of(1).right().flatMap(i -> Right.<String, Integer> of(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(Right.of(2));
    }

    @Test
    public void shouldFlatMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = Left.<String, Integer> of("1").right().flatMap(i -> Right.<String, Integer>of(i + 1).right()).toEither();
        assertThat(actual).isEqualTo(Left.of("1"));
    }

    // forEach

    @Test
    public void shouldForEachOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        Right.<String, Integer> of(1).right().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(1));
    }

    @Test
    public void shouldForEachOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        Left.<String, Integer> of("1").right().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // peek

    @Test
    public void shouldPeekOnRightProjectionOfRight() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = Right.<String, Integer> of(1).right().peek(actual::add).toEither();
        assertThat(actual).isEqualTo(Collections.singletonList(1));
        assertThat(testee).isEqualTo(Right.<String, Integer> of(1));
    }

    @Test
    public void shouldPeekOnRightProjectionOfLeft() {
        final List<Integer> actual = new ArrayList<>();
        final Either<String, Integer> testee = Left.<String, Integer> of("1").right().peek(actual::add).toEither();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(testee).isEqualTo(Left.<String, Integer> of("1"));
    }

    // map

    @Test
    public void shouldMapOnRightProjectionOfRight() {
        final Either<String, Integer> actual = Right.<String, Integer> of(1).right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Right.of(2));
    }

    @Test
    public void shouldMapOnRightProjectionOfLeft() {
        final Either<String, Integer> actual = Left.<String, Integer> of("1").right().map(i -> i + 1).toEither();
        assertThat(actual).isEqualTo(Left.of("1"));
    }

    // equals

    @Test
    public void shouldEqualRightProjectionOfRightIfObjectIsSame() {
        final RightProjection<?, ?> r = Right.of(1).right();
        assertThat(r.equals(r)).isTrue();
    }

    @Test
    public void shouldEqualRightProjectionOfLeftIfObjectIsSame() {
        final RightProjection<?, ?> r = Left.of(1).right();
        assertThat(r.equals(r)).isTrue();
    }

    @Test
    public void shouldNotEqualRightProjectionOfRightIfObjectIsNull() {
        assertThat(Right.of(1).right().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfLeftIfObjectIsNull() {
        assertThat(Left.of(1).right().equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfRightIfObjectIsOfDifferentType() {
        assertThat(Right.of(1).right().equals(new Object())).isFalse();
    }

    @Test
    public void shouldNotEqualRightProjectionOfLeftIfObjectIsOfDifferentType() {
        assertThat(Left.of(1).right().equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualRightProjectionOfRight() {
        assertThat(Right.of(1).right()).isEqualTo(Right.of(1).right());
    }

    @Test
    public void shouldEqualRightProjectionOfLeft() {
        assertThat(Left.of(1).right()).isEqualTo(Left.of(1).right());
    }

    // hashCode

    @Test
    public void shouldHashRightProjectionOfRight() {
        assertThat(Right.of(1).right().hashCode()).isEqualTo(Objects.hashCode(Right.of(1)));
    }

    @Test
    public void shouldHashRightProjectionOfLeft() {
        assertThat(Left.of(1).right().hashCode()).isEqualTo(Objects.hashCode(Left.of(1)));
    }

    // toString

    @Test
    public void shouldConvertRightProjectionOfLeftToString() {
        assertThat(Left.of(1).right().toString()).isEqualTo("RightProjection(Left(1))");
    }

    @Test
    public void shouldConvertRightProjectionOfRightToString() {
        assertThat(Right.of(1).right().toString()).isEqualTo("RightProjection(Right(1))");
    }
}
