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
import io.vavr.Value;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.util.ArrayList;
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


    // -- LenientValidation.narrow

    @Test
    public void shouldNarrow() {
        LenientValidation<String, Integer> validation = LenientValidation.valid(42);
        LenientValidation<CharSequence, Number> narrow = LenientValidation.narrow(validation);
        assertThat(narrow.get()).isEqualTo(42);
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
        assertThat(actual).isEqualTo(LenientValidation.fromNullable(List.of("error1", "error2", "error3", "error4"), List.of(1, 2)));
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
                    : LenientValidation.invalid(List.of("error" + x, "error" + (x+1))));
        assertThat(actual).isEqualTo(LenientValidation.fromNullable(List.of("error-1", "error0", "error-2", "error-1"), List.of(1, 2)));
    }

    // -- filter

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

    // -- flatMap

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

}
