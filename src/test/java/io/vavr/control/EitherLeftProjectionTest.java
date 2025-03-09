/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.control;

import io.vavr.API;
import io.vavr.AbstractValueTest;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("deprecation")
public class EitherLeftProjectionTest extends AbstractValueTest {

    @Override
    protected <T> Either.LeftProjection<T, ?> empty() {
        return Either.<T, T>right(null).left();
    }

    @Override
    protected <T> Either.LeftProjection<T, ?> of(T element) {
        return Either.<T, T>left(element).left();
    }

    @SafeVarargs
    @Override
    protected final <T> Either.LeftProjection<T, ?> of(T... elements) {
        return of(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Nested
    @DisplayName("Get")
    class GetTest {

        @Test
        public void shouldThrowOnGetOnLeftProjectionOfRight() {
            assertThrows(NoSuchElementException.class, () -> Either.right(1).left().get());
        }

        @Test
        public void shouldGetOnLeftProjectionOfLeft() {
            assertThat(Either.left(1).left().get()).isEqualTo(1);
        }

    }

    @Nested
    @DisplayName("orElse")
    class OrElseTest {

        @Test
        public void shouldLeftProjectionOrElseLeftProjection() {
            final Either.LeftProjection<Integer, Integer> elseProjection = API.<Integer, Integer>Left(2).left();
            assertThat(API.Left(1).left().orElse(elseProjection).get()).isEqualTo(1);
            assertThat(API.Right(1).left().orElse(elseProjection).get()).isEqualTo(2);
        }

        @Test
        public void shouldLeftProjectionOrElseLeftProjectionFromSupplier() {
            final Either.LeftProjection<Integer, Integer> elseProjection = API.<Integer, Integer>Left(2).left();
            assertThat(API.Left(1).left().orElse(() -> elseProjection).get()).isEqualTo(1);
            assertThat(API.Right(1).left().orElse(() -> elseProjection).get()).isEqualTo(2);
        }

    }

    @Nested
    @DisplayName("getOrElse")
    class GetOrElseTest {

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

    }

    @Nested
    @DisplayName("getOrElse(Function)")
    class GetOrElseFunctionTest {

        @Test
        public void shouldReturnLeftWhenOrElseGetGivenFunctionOnLeftProjectionOfLeft() {
            final Integer actual = Either.left(1).left().getOrElseGet(r -> 2);
            assertThat(actual).isEqualTo(1);
        }

        @Test
        public void shouldReturnOtherWhenOrElseGetGivenFunctionOnLeftProjectionOfRight() {
            final Integer actual = Either.<Integer, String>right("1").left().getOrElseGet(r -> 2);
            assertThat(actual).isEqualTo(2);
        }

    }

    @Nested
    @DisplayName("orElseRun(Function)")
    class OrElseRunTest {

        @Test
        public void shouldReturnLeftWhenOrElseRunOnLeftProjectionOfLeft() {
            final boolean[] actual = new boolean[]{true};
            Either.left(1).left().orElseRun(s -> actual[0] = false);
            assertThat(actual[0]).isTrue();
        }

        @Test
        public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfRight() {
            final boolean[] actual = new boolean[]{false};
            Either.right("1").left().orElseRun(s -> {
                actual[0] = true;
            });
            assertThat(actual[0]).isTrue();
        }

    }

    @Nested
    @DisplayName("getOrElseThrow(Function)")
    class GetOrElseThrowFunctionTest {

        @Test
        public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOfLeft() {
            final Integer actual = Either.<Integer, String>left(1).left().getOrElseThrow(s -> new RuntimeException(s));
            assertThat(actual).isEqualTo(1);
        }

        @Test
        public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfRight() {
            assertThrows(RuntimeException.class, () -> Either.right("1").left().getOrElseThrow(s -> new RuntimeException(s)));
        }

    }

    @Nested
    @DisplayName("toOption")
    class ToOptionTest {

        @Test
        public void shouldConvertLeftProjectionOfLeftToSome() {
            assertThat(Either.left(1).left().toOption()).isEqualTo(Option.of(1));
        }

        @Test
        public void shouldConvertLeftProjectionOfRightToNone() {
            assertThat(Either.right("x").left().toOption()).isEqualTo(Option.none());
        }

    }

    @Nested
    @DisplayName("toEither")
    class ToEitherTest {

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

    }

    @Nested
    @DisplayName("toJavaOptional")
    class ToJavaOptionalTest {

        @Test
        public void shouldConvertLeftProjectionOfLeftToJavaOptional() {
            assertThat(Either.left(1).left().toJavaOptional()).isEqualTo(Optional.of(1));
        }

        @Test
        public void shouldConvertLeftProjectionOfRightToJavaOptional() {
            assertThat(Either.<Integer, String>right("x").left().toJavaOptional()).isEqualTo(Optional.empty());
        }

    }

    @Nested
    @DisplayName("filter")
    class FilterTest {

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
            final boolean actual = Either.right("1").left().filter(i -> true).isDefined();
            assertThat(actual).isTrue();
        }

        @Test
        public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
            final boolean actual = Either.right("1").left().filter(i -> false).isDefined();
            assertThat(actual).isTrue();
        }

    }

    @Nested
    @DisplayName("flatMap")
    class FlatMapTest {

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
            final Either.LeftProjection<Tuple2<String, String>, String> actual = good.left().flatMap(g -> bad.left().map(b -> Tuple.of(g, b)));
            assertThat(actual.toEither()).isEqualTo(Either.right("bad"));

        }

    }

    @Nested
    @DisplayName("exists")
    class ExistsTest {

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

    }

    @Nested
    @DisplayName("forall")
    class ForallTest {

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

    }

    @Nested
    @DisplayName("forEach")
    class ForEachTest {

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

    }

    @Nested
    @DisplayName("peek")
    class PeekTest {

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

    }

    @Nested
    @DisplayName("map")
    class MapTest {

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
    }

    @Nested
    @DisplayName("iterator")
    class TteratorTest {

        @Test
        public void shouldReturnIteratorOfLeftOfLeftProjection() {
            assertThat((Iterator<Integer>) Either.left(1).left().iterator()).isNotNull();
        }

        @Test
        public void shouldReturnIteratorOfRightOfLeftProjection() {
            assertThat((Iterator<Object>) Either.right(1).left().iterator()).isNotNull();
        }

    }

    @Nested
    @DisplayName("equals")
    class EqualsTest {

        @Test
        public void shouldEqualLeftProjectionOfLeftIfObjectIsSame() {
            final Either.LeftProjection<?, ?> l = Either.left(1).left();
            assertThat(l.equals(l)).isTrue();
        }

        @Test
        public void shouldEqualLeftProjectionOfRightIfObjectIsSame() {
            final Either.LeftProjection<?, ?> l = Either.right(1).left();
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

    }

    @Nested
    @DisplayName("hashCode")
    class HashCodeTest {

        @Test
        public void shouldHashLeftProjectionOfLeft() {
            assertThat(Either.left(1).left().hashCode()).isEqualTo(Objects.hashCode(Either.right(1)));
        }

        @Test
        public void shouldHashLeftProjectionOfRight() {
            assertThat(Either.right(1).left().hashCode()).isEqualTo(Objects.hashCode(Either.left(1)));
        }

    }

    @Nested
    @DisplayName("toString")
    class ToStringTest {

        @Test
        public void shouldConvertLeftProjectionOfLeftToString() {
            assertThat(Either.left(1).left().toString()).isEqualTo("LeftProjection(Left(1))");
        }

        @Test
        public void shouldConvertLeftProjectionOfRightToString() {
            assertThat(Either.right(1).left().toString()).isEqualTo("LeftProjection(Right(1))");
        }

    }

    @Nested
    @DisplayName("transform")
    class TransformTest {

        @Test
        public void shouldTransform() {
            final String transformed = of(1).transform(v -> String.valueOf(v.get()));
            assertThat(transformed).isEqualTo("1");
        }

    }

    @Nested
    @DisplayName("spliterator")
    class SpliteratorTest {

        @Test
        public void shouldHaveSizedSpliterator() {
            assertThat(of(1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)).isTrue();
        }

        @Test
        public void shouldHaveOrderedSpliterator() {
            assertThat(of(1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
        }

        @Test
        public void shouldReturnSizeWhenSpliterator() {
            assertThat(of(1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
        }
    }
}
