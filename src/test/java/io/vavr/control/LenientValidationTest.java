/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.control;

import io.vavr.AbstractValueTest;
import io.vavr.Function1;
import io.vavr.Value;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import org.junit.Test;

public class LenientValidationTest extends AbstractValueTest {

    // -- AbstractValueTest

    @Override
    protected <T> LenientValidation<String, T> empty() {
        return LenientValidation.empty();
    }

    @Override
    protected <T> LenientValidation<String, T> of(T element) {
        return LenientValidation.valid(element);
    }

    @SafeVarargs
    @Override
    protected final <T> Value<T> of(T... elements) {
        return LenientValidation.valid(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- LenientValidation.of

    @Test
    public void shouldCreateValidLenientValidation() {
        LenientValidation<Integer, String> validation = LenientValidation.of(Arrays.asList(1, 2), Option.some("ok"));
        assertThat(validation.getErrors()).isEqualTo(List.of(1, 2));
        assertThat(validation.getValue()).isEqualTo(Option.some("ok"));
    }

    @Test
    public void shouldCreateInvalidLenientValidation() {
        LenientValidation<Integer, String> validation = LenientValidation.of(Arrays.asList(1, 2), Option.none());
        assertThat(validation.getErrors()).isEqualTo(List.of(1, 2));
        assertThat(validation.getValue()).isEqualTo(Option.none());
    }

    // -- LenientValidation.fromNullable

    @Test
    public void shouldCreateValidLenientValidationFromNullable() {
        LenientValidation<Integer, String> validation = LenientValidation.fromNullable(Arrays.asList(1, 2), "ok");
        assertThat(validation.getErrors()).isEqualTo(List.of(1, 2));
        assertThat(validation.getValue()).isEqualTo(Option.some("ok"));
    }

    @Test
    public void shouldCreateInvalidLenientValidationFromNullable() {
        LenientValidation<Integer, String> validation = LenientValidation.fromNullable(Arrays.asList(1, 2), null);
        assertThat(validation.getErrors()).isEqualTo(List.of(1, 2));
        assertThat(validation.getValue()).isEqualTo(Option.none());
    }

    // -- LenientValidation.valid

    @Test
    public void shouldCreateValid() {
        assertThat(LenientValidation.valid(1).isValid()).isTrue();
    }

    // -- LenientValidation.invalid

    @Test
    public void shouldCreateInstanceWithErrors() {
        assertThat(LenientValidation.invalid(List.of("error")).hasErrors()).isTrue();
    }

    // -- LenientValidation.empty

    @Test
    public void shouldCreateEmptyLenientValidation() {
        assertThat(LenientValidation.empty()).isEqualTo(LenientValidation.of(List.empty(), Option.none()));
    }

    // -- LenientValidation.fromValidation

    @Test
    public void shouldCreateFromValidValidation() {
        assertThat(LenientValidation.fromValidation(Validation.valid("ok")))
            .isEqualTo(LenientValidation.valid("ok"));
    }

    @Test
    public void shouldCreateFromInvalidValidation() {
        assertThat(LenientValidation.fromValidation(Validation.invalid("error")))
            .isEqualTo(LenientValidation.invalid(List.of("error")));
    }

    // -- LenientValidation.strict

    @Test
    public void shouldConvertValidToValidation() {
        assertThat(LenientValidation.valid("ok").strict())
            .isEqualTo(Validation.valid("ok"));
    }

    @Test
    public void shouldConvertInvalidToValidation() {
        assertThat(LenientValidation.invalid(List.of("error")).strict())
            .isEqualTo(Validation.invalid(List.of("error")));
    }

    @Test
    public void shouldConvertToValidation() {
        assertThat(LenientValidation.of(List.of("error-1", "error-2"), Option.some("ok")).strict())
            .isEqualTo(Validation.invalid(List.of("error-1", "error-2")));
    }

    // -- LenientValidation.narrow

    @Test
    public void shouldNarrow() {
        LenientValidation<String, Integer> validation = LenientValidation.valid(42);
        LenientValidation<CharSequence, Number> narrow = LenientValidation.narrow(validation);
        assertThat(narrow.get()).isEqualTo(42);
    }

    // -- LenientValidation.combine

    @Test
    public void shouldCombine2Valids() {
        assertThat(LenientValidation.combine(
            LenientValidation.valid(1),
            LenientValidation.valid(2)
        ).ap(Integer::sum))
            .isEqualTo(LenientValidation.valid(3));
    }

    @Test
    public void shouldFailCombine2WithInvalid() {
        assertThat(LenientValidation.combine(
            LenientValidation.valid(1),
            LenientValidation.<String, Integer>invalid(List.of("error"))
        ).ap(Integer::sum))
            .isEqualTo(LenientValidation.invalid(List.of("error")));
    }

    @Test
    public void shouldFailCombine2Invalids() {
        assertThat(LenientValidation.combine(
            LenientValidation.<String, Integer>invalid(List.of("error-1")),
            LenientValidation.<String, Integer>invalid(List.of("error-2"))
        ).ap(Integer::sum))
            .isEqualTo(LenientValidation.invalid(List.of("error-1", "error-2")));
    }

    // -- LenientValidation.hasErrors

    @Test
    public void shouldValidHaveNoErrors() {
        assertThat(LenientValidation.valid("ok").hasErrors()).isFalse();
    }

    @Test
    public void shouldInvalidHaveErrors() {
        assertThat(LenientValidation.invalid(List.of("error")).hasErrors()).isTrue();
    }

    @Test
    public void shouldEmptyHaveNoErrors() {
        assertThat(LenientValidation.empty().hasErrors()).isFalse();
    }

    @Test
    public void shouldPartiallyValidHaveErrors() {
        assertThat(LenientValidation.of(List.of("error"), Option.some("ok")).hasErrors()).isTrue();
    }

    // -- LenientValidation.isValid

    @Test
    public void shouldValidBeValid() {
        assertThat(LenientValidation.valid("ok").isValid()).isTrue();
    }

    @Test
    public void shouldInvalidBeNotValid() {
        assertThat(LenientValidation.invalid(List.of("error")).isValid()).isFalse();
    }

    @Test
    public void shouldPartiallyValidBeValid() {
        assertThat(LenientValidation.of(List.of("error"), Option.some("ok")).isValid()).isTrue();
    }

    // -- LenientValidation.ap

    @Test
    public void shouldApValids() {
        LenientValidation<String, Integer> valid = LenientValidation.valid(42);
        LenientValidation<String, Function1<Integer, String>> function = LenientValidation.valid(Object::toString);
        assertThat(valid.ap(function)).isEqualTo(LenientValidation.valid("42"));
    }

    @Test
    public void shouldApContainAllErrors() {
        LenientValidation<String, Integer> valid = LenientValidation.of(List.of("error-value"), Option.some(42));
        LenientValidation<String, Function1<Integer, String>> function =
            LenientValidation.of(List.of("error-function"), Option.some(Object::toString));
        assertThat(valid.ap(function))
            .isEqualTo(LenientValidation.of(List.of("error-function", "error-value"), Option.some("42")));
    }

    // -- LenientValidation.sequence

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenSequencingNull() {
        LenientValidation.sequence(null);
    }

    @Test
    public void shouldCreateValidWhenSequencingValids() {
        final LenientValidation<String, Seq<Integer>> actual = LenientValidation.sequence(List.of(
            LenientValidation.valid(1),
            LenientValidation.valid(2)
        ));
        assertThat(actual).isEqualTo(LenientValidation.valid(List.of(1, 2)));
    }

    @Test
    public void shouldCreateInstanceWithAllErrorsAndAllValidsWhenSequencing() {
        final LenientValidation<String, Seq<Integer>> actual = LenientValidation.sequence(List.of(
            LenientValidation.valid(1),
            LenientValidation.invalid(List.of("error1", "error2")),
            LenientValidation.valid(2),
            LenientValidation.invalid(List.of("error3", "error4"))
        ));
        assertThat(actual)
            .isEqualTo(LenientValidation.fromNullable(List.of("error1", "error2", "error3", "error4"), List.of(1, 2)));
    }

    // -- LenientValidation.traverse

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenTraversingNull() {
        LenientValidation.traverse(null, null);
    }

    @Test
    public void shouldCreateValidWhenTraversingValids() {
        final LenientValidation<String, Seq<Integer>> actual =
            LenientValidation.traverse(List.of(1, 2), LenientValidation::valid);
        assertThat(actual).isEqualTo(LenientValidation.valid(List.of(1, 2)));
    }

    @Test
    public void shouldCreateInstanceWithAllErrorsAndAllValidsWhenTraversing() {
        final LenientValidation<String, Seq<Integer>> actual =
            LenientValidation.traverse(
                List.of(1, -1, 2, -2),
                x -> x >= 0
                    ? LenientValidation.valid(x)
                    : LenientValidation.invalid(List.of("error" + x, "error" + (x + 1))));
        assertThat(actual)
            .isEqualTo(LenientValidation.fromNullable(List.of("error-1", "error0", "error-2", "error-1"), List.of(1, 2)));
    }

    // -- LenientValidation.filter

    @Test
    public void shouldFilterValid() {
        LenientValidation<String, Integer> valid = LenientValidation.valid(42);
        assertThat(valid.filter(i -> true)).isEqualTo(valid);
        assertThat(valid.filter(i -> false).isEmpty()).isTrue();
    }

    @Test
    public void shouldFilterInstanceWithErrors() {
        LenientValidation<String, Integer> invalid = LenientValidation.invalid(List.of("error"));
        assertThat(invalid.filter(i -> true)).isEqualTo(invalid);
        assertThat(invalid.filter(i -> false)).isEqualTo(invalid);
    }

    // -- LenientValidation.flatMap

    @Test
    public void shouldFlatMapValidsCorrectly() {
        LenientValidation<String, Integer> valid = LenientValidation.valid(42);
        assertThat(valid.flatMap(v -> LenientValidation.valid("ok")))
            .isEqualTo(LenientValidation.valid("ok"));
    }

    @Test
    public void shouldHaveOutsideErrorsWhenFlatMapInvalids() {
        LenientValidation<String, Integer> invalid = LenientValidation.invalid(List.of("error-1"));
        assertThat(invalid.flatMap(v -> LenientValidation.invalid(List.of("error-2"))).getErrors())
            .isEqualTo(List.of("error-1"));
    }

    @Test
    public void shouldHaveAllErrorsWhenFlatMapValidAndInvalid() {
        LenientValidation<String, String> invalid = LenientValidation.fromNullable(List.of("error-1"), "ok");
        assertThat(invalid.flatMap(v -> LenientValidation.invalid(List.of("error-2"))).getErrors())
            .isEqualTo(List.of("error-1", "error-2"));
    }

    // - flatMapTry

    @Test
    public void shouldHaveExceptionAsErrorWhenFlatMapTryWithException() {
        LenientValidation<String, Integer> valid = LenientValidation.valid(42);
        assertThat(valid.flatMapTry(v -> {
            throw new RuntimeException("error");
        }, Throwable::getMessage))
            .isEqualTo(LenientValidation.invalid(List.of("error")));
    }

    @Test
    public void shouldBehaveLikeFlatMapWhenFlatMapTryWithoutException() {
        LenientValidation<String, Integer> invalid = LenientValidation.invalid(List.of("error-1"));
        assertThat(invalid.flatMapTry(v -> LenientValidation.invalid(List.of("error-2")), Throwable::getMessage).getErrors())
            .isEqualTo(List.of("error-1"));
    }

    // -- orElse

    @Test
    public void shouldReturnSelfOnOrElseIfValid() {
        LenientValidation<String, String> valid = LenientValidation.valid("ok");
        assertThat(valid.orElse("ok-2")).isEqualTo(valid);
    }

    @Test
    public void shouldReturnDefaultOnOrElseIfInvalidValid() {
        LenientValidation<String, String> invalid = LenientValidation.invalid(List.of("error"));
        assertThat(invalid.orElse("ok").getValue()).isEqualTo(Option.of("ok"));
    }

    // -- LenientValidation.toEither

    @Test
    public void shouldConvertValidToRight() {
        assertThat(LenientValidation.valid("ok").toEither()).isEqualTo(Either.right("ok"));
    }

    @Test
    public void shouldConvertInvalidToLeft() {
        assertThat(LenientValidation.invalid(List.of("error-1")).toEither()).isEqualTo(Either.left(List.of("error-1")));
    }

    @Test
    public void shouldConvertPartiallyValidToRight() {
        assertThat(LenientValidation.of(List.of("error-1"), Option.some("ok")).toEither()).isEqualTo(Either.right("ok"));
    }

    // -- fold

    @Test
    public void shouldFoldToString() {
        LenientValidation<Integer, String> validation = LenientValidation.of(List.of(1, 2), Option.some("ok"));
        String result = validation.fold(
            errors -> errors.foldLeft("", (acc, value) -> acc + "error-" + value + " "),
            maybeValue -> maybeValue.fold(() -> "No value", value -> value),
            (errorStr, valueStr) -> "Errors: " + errorStr + ", Value: " + valueStr
        );
        assertThat(result).isEqualTo("Errors: error-1 error-2 , Value: ok");
    }

    @Test
    public void shouldFoldEmptyToString() {
        LenientValidation<Integer, String> validation = LenientValidation.of(List.of(), Option.none());
        String result = validation.fold(
            errors -> errors.foldLeft("", (acc, value) -> acc + "error-" + value + " "),
            maybeValue -> maybeValue.fold(() -> "No value", value -> value),
            (errorStr, valueStr) -> "Errors: " + errorStr + ", Value: " + valueStr
        );
        assertThat(result).isEqualTo("Errors: , Value: No value");
    }

    // -- toString

    @Test
    public void shouldReturnCorrectStringForToString() {
        assertThat(LenientValidation.fromNullable(List.of("error"), "test").toString())
            .isEqualTo("LenientValidation(List(error), Some(test))");
    }

    // -- map

    @Test
    public void shouldMapValid() {
        assertThat(LenientValidation.fromNullable(List.of("error"), 4).map(i -> i + 3))
            .isEqualTo(LenientValidation.fromNullable(List.of("error"), 7));
    }

    @Test
    public void shouldMapInvalid() {
        LenientValidation<String, Integer> invalid = LenientValidation.invalid(List.of("error"));
        assertThat(invalid.map(i -> i + 3))
            .isEqualTo(invalid);
    }

    // -- mapError

    @Test
    public void shouldMapErrorOnValid() {
        assertThat(LenientValidation.valid(5).mapError(x -> 2))
            .isEqualTo(LenientValidation.valid(5));
    }

    @Test
    public void shouldMapErrorOnInvalid() {
        assertThat(LenientValidation.invalid(List.of(2, 4)).mapError(x -> x + 1))
            .isEqualTo(LenientValidation.invalid(List.of(3, 5)));
    }

    // -- LenientValidation.flatMapError

    @Test
    public void shouldFlatMapErrorOnValid() {
        assertThat(LenientValidation.valid(5).flatMapError(List::of))
            .isEqualTo(LenientValidation.valid(5));
    }

    @Test
    public void shouldFlatMapErrorOnInvalid() {
        assertThat(LenientValidation.invalid(List.of(1, 3)).flatMapError(x -> List.of(x, x * 2)))
            .isEqualTo(LenientValidation.invalid(List.of(1, 2, 3, 6)));
    }

}
