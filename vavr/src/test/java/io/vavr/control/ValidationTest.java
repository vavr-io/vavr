/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ValidationTest extends AbstractValueTest {

    private static final String OK = "ok";
    private static final List<String> ERRORS = List.of("error1", "error2", "error3");

    // -- AbstractValueTest

    @Override
    protected <T> Validation<String, T> empty() {
        return Validation.invalid("empty");
    }

    @Override
    protected <T> Validation<String, T> of(T element) {
        return Validation.valid(element);
    }

    @SafeVarargs
    @Override
    protected final <T> Value<T> of(T... elements) {
        return Validation.valid(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- Validation.lift

    @Test
    public void shouldLiftLambdaToValid() {
        assertThat(Validation.lift(Function.identity())).isNotNull();
    }

    // -- Validation.valid

    @Test
    public void shouldCreateSuccessWhenCallingValidationSuccess() {
        assertThat(Validation.valid(1) instanceof Validation.Valid).isTrue();
    }

    // -- Validation.invalid

    @Test
    public void shouldCreateFailureWhenCreatingInvalidOfSingleError() {
        final Validation<String, ?> invalid = Validation.invalid("error");
        assertThat(invalid instanceof Validation.Invalid).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCreatingInvalidOfSingleErrorThatIsIterable() {
        final Validation<Seq<String>, ?> invalid = Validation.invalid(List.of("error"));
        assertThat(invalid instanceof Validation.Invalid).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCreatingnInvalidOfVarargErrors() {
        final Validation<String, ?> invalid = Validation.invalid("err1", "err2");
        assertThat(invalid instanceof Validation.Invalid).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCreatingInvalidOfIterableErrors() {
        final Validation<String, ?> invalid = Validation.invalidAll(Arrays.asList("err1", "err2"));
        assertThat(invalid instanceof Validation.Invalid).isTrue();
    }

    // -- Validation.fromEither

    @Test
    public void shouldCreateFromRightEither() {
        final Validation<String, Integer> validation = Validation.fromEither(Either.right(42));
        assertThat(validation.isValid()).isTrue();
        assertThat(validation.get()).isEqualTo(42);
    }

    @Test
    public void shouldCreateFromLeftEither() {
        final Validation<String, Integer> validation = Validation.fromEither(Either.left("vavr"));
        assertThat(validation.isValid()).isFalse();
        assertThat(validation.getErrors()).isEqualTo(List.of("vavr"));
    }

    // -- Validation.fromTry

    @Test
    public void shouldCreateFromSuccessTry() {
        final Validation<Throwable, Integer> validation = Validation.fromTry(Try.success(42));
        assertThat(validation.isValid()).isTrue();
        assertThat(validation.get()).isEqualTo(42);
    }

    @Test
    public void shouldCreateFromFailureTry() {
        final Throwable throwable = new Throwable("vavr");
        final Validation<Throwable, Integer> validation = Validation.fromTry(Try.failure(throwable));
        assertThat(validation.isValid()).isFalse();
        assertThat(validation.getErrors()).isEqualTo(List.of(throwable));
    }

    // -- Validation.narrow

    @Test
    public void shouldNarrowValid() {
        final Validation<String, Integer> validation = Validation.valid(42);
        final Validation<CharSequence, Number> narrow = Validation.narrow(validation);
        assertThat(narrow.get()).isEqualTo(42);
    }

    @Test
    public void shouldNarrowInvalid() {
        final Validation<String, Integer> validation = Validation.invalid("vavr");
        final Validation<CharSequence, Number> narrow = Validation.narrow(validation);
        assertThat(narrow.getErrors()).isEqualTo(List.of("vavr"));
    }

    // -- Validation.sequence

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenSequencingNull() {
        Validation.sequence(null);
    }

    @Test
    public void shouldCreateValidWhenSequencingValids() {
        final Validation<Seq<String>, Seq<Integer>> actual = Validation.sequence(List.of(
                Validation.valid(1),
                Validation.valid(2)
        ));
        assertThat(actual).isEqualTo(Validation.valid(List.of(1, 2)));
    }

    @Test
    public void shouldCreateInvalidWhenSequencingAnInvalid() {
        final Validation<String, Seq<Integer>> actual = Validation.sequence(List.of(
                Validation.valid(1),
                Validation.invalid("error1", "error2"),
                Validation.valid(2),
                Validation.invalid("error3", "error4")
        ));
        assertThat(actual).isEqualTo(Validation.invalid("error1", "error2", "error3", "error4"));
    }

    // -- Validation.traverse

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenTraversingNull() {
        Validation.traverse(null, null);
    }

    @Test
    public void shouldCreateValidWhenTraversingValids() {
        final Validation<Seq<String>, Seq<Integer>> actual =
            Validation.traverse(List.of(1, 2), Validation::valid);
        assertThat(actual).isEqualTo(Validation.valid(List.of(1, 2)));
    }

    @Test
    public void shouldCreateInvalidWhenTraversingAnInvalid() {
        final Validation<String, Seq<Integer>> actual =
            Validation.traverse(
                List.of(1, -1, 2, -2),
                x -> x >= 0
                    ? Validation.valid(x)
                    : Validation.invalid("error" + Integer.toString(x), "error" + Integer.toString(x+1)));
        assertThat(actual).isEqualTo(Validation.invalid("error-1", "error0", "error-2", "error-1"));
    }

    // -- ap

    @Test
    public void shouldApLiftedFunctionToValid() {
        assertThat(Validation.valid(1).ap(Validation.lift(i -> i)).get()).isEqualTo(1);
    }

    @Test
    public void shouldApLiftedFunctionToInvalid() {
        assertThat(Validation.invalid("err1", "err2").ap(Validation.lift(i -> i)).getErrors()).isEqualTo(List.of("err1", "err2"));
    }

    // -- toEither

    @Test
    public void shouldConvertToRightEither() {
        final Either<?, Integer> either = Validation.valid(42).toEither();
        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(42);
    }

    @Test
    public void shouldConvertToLeftEither() {
        final Either<Seq<String>, ?> either = Validation.invalid("vavr").toEither();
        assertThat(either.isLeft()).isTrue();
        assertThat(either.getLeft()).isEqualTo(List.of("vavr"));
    }

    // -- toTry

    @Test
    public void shouldConvertToSuccessfulTry() {
        final Try<Integer> tryRight = Validation.valid(42).toTry(errors -> new RuntimeException("vavr"));
        assertThat(tryRight.isSuccess()).isTrue();
        assertThat(tryRight.get()).isEqualTo(42);
    }

    @Test
    public void shouldConvertToFailedTry() {
        final Try<?> tryLeft = Validation.invalid("vavr", "rvav")
                .toTry(errors -> new RuntimeException(errors.mkString(",")));
        assertThat(tryLeft.isFailure()).isTrue();
        assertThat(tryLeft.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(tryLeft.getCause().getMessage()).isEqualTo("vavr,rvav");
    }

    // -- filter

    @Test
    public void shouldFilterValid() {
        final Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.filter(i -> true)).isSameAs(valid);
        assertThat(valid.filter(i -> false).getErrors()).isSameAs(List.empty());
    }

    @Test
    public void shouldFilterInvalid() {
        final Validation<String, Integer> invalid = Validation.invalid("vavr");
        assertThat(invalid.filter(i -> true)).isSameAs(invalid);
        assertThat(invalid.filter(i -> false)).isSameAs(invalid);
    }

    // -- filterErrors

    @Test
    public void shouldFilterErrorsOfValid() {
        final Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.filterErrors(s -> true)).isSameAs(valid);
        assertThat(valid.filterErrors(s -> false)).isSameAs(valid);
    }

    @Test
    public void shouldFilterErrorsOfInvalid() {
        final Validation<String, Integer> invalid = Validation.invalid("vavr");
        assertThat(invalid.filterErrors(s -> true)).isEqualTo(invalid);
        assertThat(invalid.filterErrors(s -> false).getErrors()).isSameAs(List.empty());
    }

    // -- flatMap

    @Test
    public void shouldFlatMapValid() {
        final Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.flatMap(v -> Validation.valid("ok")).get()).isEqualTo("ok");
    }

    @Test
    public void shouldFlatMapInvalid() {
        final Validation<String, Integer> invalid = Validation.invalid("vavr");
        assertThat(invalid.flatMap(v -> Validation.valid("ok"))).isSameAs(invalid);
    }

    // -- flatMapErrors

    @Test
    public void shouldFlatMapErrorsOfValid() {
        final Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.flatMapErrors(s -> List.of("error"))).isSameAs(valid);
        assertThat(valid.flatMapErrors(s -> List.empty())).isSameAs(valid);
    }

    @Test
    public void shouldFlatMapErrorsOfInvalid() {
        final Validation<String, Integer> invalid = Validation.invalid("vavr");
        assertThat(invalid.flatMapErrors(s -> List.of("error")).getErrors()).isEqualTo(List.of("error"));
        assertThat(invalid.flatMapErrors(s -> List.empty()).getErrors()).isSameAs(List.empty());
    }

    // -- orElse

    @Test
    public void shouldReturnSelfOnOrElseIfValid() {
        final Validation<String, String> validValidation = valid();
        assertThat(validValidation.orElse(invalid())).isSameAs(validValidation);
    }

    @Test
    public void shouldReturnSelfOnOrElseSupplierIfValid() {
        final Validation<String, String> validValidation = valid();
        assertThat(validValidation.orElse(this::invalid)).isSameAs(validValidation);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseIfValid() {
        final Validation<String, String> validValidation = valid();
        assertThat(invalid().orElse(validValidation)).isSameAs(validValidation);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseSupplierIfValid() {
        final Validation<String, String> validValidation = valid();
        assertThat(invalid().orElse(() -> validValidation)).isSameAs(validValidation);
    }

    // -- getErrors

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowErrorOnGetErrorValid() {
        final Validation<String, String> v1 = valid();
        v1.getErrors();
    }

    // -- getOrElseGet

    @Test
    public void shouldReturnValueOnGetOrElseGetIfValid() {
        final Validation<Integer, String> validValidation = valid();
        assertThat(validValidation.getOrElseGet(e -> "error" + e)).isEqualTo(OK);
    }

    @Test
    public void shouldReturnCalculationOnGetOrElseGetIfInvalid() {
        final Validation<Integer, String> invalidValidation = Validation.invalid(42);
        assertThat(invalidValidation.getOrElseGet(errors -> "error" + errors.get(0))).isEqualTo("error42");
    }

    // -- fold

    @Test
    public void shouldConvertSuccessToU() {
        final Validation<Seq<String>, String> validValidation = valid();
        final Integer result = validValidation.fold(Seq::length, String::length);
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void shouldConvertFailureToU() {
        final Validation<String, String> invalidValidation = invalid();
        final Integer result = invalidValidation.fold(Seq::length, String::length);
        assertThat(result).isEqualTo(3);
    }

    // -- swap

    @Test
    public void shouldSwapSuccessToFailure() {
        assertThat(valid().swap() instanceof Validation.Invalid).isTrue();
        assertThat(valid().swap().getErrors()).isEqualTo(List.of(OK));
    }

    @Test
    public void shouldSwapFailureToSuccess() {
        assertThat(invalid().swap() instanceof Validation.Valid).isTrue();
        assertThat(invalid().swap().get()).isEqualTo(ERRORS);
    }

    // -- map

    @Test
    public void shouldMapSuccessValue() {
        assertThat(valid().map(s -> s + "!").get()).isEqualTo(OK + "!");
    }

    @Test
    public void shouldMapFailureError() {
        assertThat(invalid().map(s -> 2).getErrors()).isEqualTo(ERRORS);
    }

    @Test(expected = RuntimeException.class)
    public void shouldMapFailureErrorOnGet() {
        assertThat(invalid().map(s -> 2).get()).isEqualTo(ERRORS);
    }

    // -- mapErrors

    @Test
    public void shouldNotMapSuccess() {
        assertThat(valid().mapErrors(x -> 2).get()).isEqualTo(OK);
    }

    @Test
    public void shouldMapFailure() {
        assertThat(invalid().mapErrors(x -> 5).getErrors()).isEqualTo(List.of(5, 5, 5));
    }

    // -- bimap

    @Test
    public void shouldMapOnlySuccessValue() {
        final Validation<String, String> validValidation = valid();
        final Validation<Integer, Integer> validMapping = validValidation.bimap(String::length, String::length);
        assertThat(validMapping instanceof Validation.Valid).isTrue();
        assertThat(validMapping.get()).isEqualTo(2);
    }

    @Test
    public void shouldMapOnlyFailureValue() {
        final Validation<String, String> invalidValidation = invalid();
        final Validation<Integer, Integer> invalidMapping = invalidValidation.bimap(String::length, String::length);
        assertThat(invalidMapping instanceof Validation.Invalid).isTrue();
        assertThat(invalidMapping.getErrors()).isEqualTo(List.of(6, 6, 6));
    }

    // -- forEach

    @Test
    public void shouldProcessFunctionInForEach() {

        { // Valid.forEach
            final java.util.List<String> accumulator = new ArrayList<>();
            final Validation<String, String> v = Validation.valid("valid");
            v.forEach(accumulator::add);
            assertThat(accumulator.size()).isEqualTo(1);
            assertThat(accumulator.get(0)).isEqualTo("valid");
        }

        { // Invalid.forEach
            final java.util.List<String> accumulator = new ArrayList<>();
            final Validation<String, String> v = Validation.invalid("error");
            v.forEach(accumulator::add);
            assertThat(accumulator.size()).isEqualTo(0);
        }
    }

    // -- combine and apply

    @Test
    public void shouldBuildUpForSuccessCombine() {
        final Validation<String, String> v1 = Validation.valid("John Doe");
        final Validation<String, Integer> v2 = Validation.valid(39);
        final Validation<String, Option<String>> v3 = Validation.valid(Option.of("address"));
        final Validation<String, Option<String>> v4 = Validation.valid(Option.none());
        final Validation<String, String> v5 = Validation.valid("111-111-1111");
        final Validation<String, String> v6 = Validation.valid("alt1");
        final Validation<String, String> v7 = Validation.valid("alt2");
        final Validation<String, String> v8 = Validation.valid("alt3");
        final Validation<String, String> v9 = Validation.valid("alt4");

        final Validation<String, TestPojo> result = v1.combine(v2).ap(TestPojo::new);

        final Validation<String, TestPojo> result2 = v1.combine(v2).combine(v3).ap(TestPojo::new);
        final Validation<String, TestPojo> result3 = v1.combine(v2).combine(v4).ap(TestPojo::new);

        final Validation<String, TestPojo> result4 = v1.combine(v2).combine(v3).combine(v5).ap(TestPojo::new);
        final Validation<String, TestPojo> result5 = v1.combine(v2).combine(v3).combine(v5).combine(v6).ap(TestPojo::new);
        final Validation<String, TestPojo> result6 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).ap(TestPojo::new);
        final Validation<String, TestPojo> result7 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).ap(TestPojo::new);
        final Validation<String, TestPojo> result8 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).combine(v9).ap(TestPojo::new);

        final Validation<String, String> result9 = v1.combine(v2).combine(v3).ap((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.getOrElse("none"));

        assertThat(result.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result4.isValid()).isTrue();
        assertThat(result5.isValid()).isTrue();
        assertThat(result6.isValid()).isTrue();
        assertThat(result7.isValid()).isTrue();
        assertThat(result8.isValid()).isTrue();
        assertThat(result9.isValid()).isTrue();

        assertThat(result.get() != null).isTrue();
        assertThat(result9.get() != null).isTrue();
    }

    @Test
    public void shouldCreateSuccessNestedCombine() {
        final Validation<String, String> v1 = Validation.valid("John Doe");
        final Validation<String, Integer> v2 = Validation.valid(39);
        final Validation<String, Option<String>> v3 = Validation.valid(Option.of("address"));

        final Validation<String, TestNestedPojo> nestedResult = v3.combine(
                v1.combine(v2).ap(TestPojo::new)
        ).ap(TestNestedPojo::new);

        assertThat(nestedResult.isValid()).isTrue();
        assertThat(nestedResult.get() != null).isTrue();
    }

    @Test
    public void shouldBuildUpForSuccessMapN() {
        final Validation<String, String> v1 = Validation.valid("John Doe");
        final Validation<String, Integer> v2 = Validation.valid(39);
        final Validation<String, Option<String>> v3 = Validation.valid(Option.of("address"));
        final Validation<String, Option<String>> v4 = Validation.valid(Option.none());
        final Validation<String, String> v5 = Validation.valid("111-111-1111");
        final Validation<String, String> v6 = Validation.valid("alt1");
        final Validation<String, String> v7 = Validation.valid("alt2");
        final Validation<String, String> v8 = Validation.valid("alt3");
        final Validation<String, String> v9 = Validation.valid("alt4");

        // Alternative map(n) functions to the 'combine' function
        final Validation<String, TestPojo> result = Validation.combine(v1, v2).ap(TestPojo::new);
        final Validation<String, TestPojo> result2 = Validation.combine(v1, v2, v3).ap(TestPojo::new);
        final Validation<String, TestPojo> result3 = Validation.combine(v1, v2, v4).ap(TestPojo::new);
        final Validation<String, TestPojo> result4 = Validation.combine(v1, v2, v3, v5).ap(TestPojo::new);
        final Validation<String, TestPojo> result5 = Validation.combine(v1, v2, v3, v5, v6).ap(TestPojo::new);
        final Validation<String, TestPojo> result6 = Validation.combine(v1, v2, v3, v5, v6, v7).ap(TestPojo::new);
        final Validation<String, TestPojo> result7 = Validation.combine(v1, v2, v3, v5, v6, v7, v8).ap(TestPojo::new);
        final Validation<String, TestPojo> result8 = Validation.combine(v1, v2, v3, v5, v6, v7, v8, v9).ap(TestPojo::new);

        final Validation<String, String> result9 = Validation.combine(v1, v2, v3).ap((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.getOrElse("none"));

        assertThat(result.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result4.isValid()).isTrue();
        assertThat(result5.isValid()).isTrue();
        assertThat(result6.isValid()).isTrue();
        assertThat(result7.isValid()).isTrue();
        assertThat(result8.isValid()).isTrue();
        assertThat(result9.isValid()).isTrue();

        assertThat(result.get() != null).isTrue();
        assertThat(result9.get() != null).isTrue();
    }

    @Test
    public void shouldBuildUpForFailure() {
        final Validation<String, String> v1 = Validation.valid("John Doe");
        final Validation<String, Integer> v2 = Validation.valid(39);
        final Validation<String, Option<String>> v3 = Validation.valid(Option.of("address"));

        final Validation<String, String> e1 = Validation.invalid("error2");
        final Validation<String, Integer> e2 = Validation.invalid("error1");
        final Validation<String, Option<String>> e3 = Validation.invalid("error3");

        final Validation<String, TestPojo> result = v1.combine(e2).combine(v3).ap(TestPojo::new);
        final Validation<String, TestPojo> result2 = e1.combine(v2).combine(e3).ap(TestPojo::new);

        assertThat(result.isInvalid()).isTrue();
        assertThat(result2.isInvalid()).isTrue();
    }

    // -- equals

    @Test
    public void shouldMatchLikeObjects() {
        final Validation<String, String> v1 = Validation.valid("test");
        final Validation<String, String> v2 = Validation.valid("test");
        final Validation<String, String> v3 = Validation.valid("test diff");

        final Validation<String, String> e1 = Validation.invalid("error1");
        final Validation<String, String> e2 = Validation.invalid("error1");
        final Validation<String, String> e3 = Validation.invalid("error diff");

        assertThat(v1.equals(v1)).isTrue();
        assertThat(v1.equals(v2)).isTrue();
        assertThat(v1.equals(v3)).isFalse();

        assertThat(e1.equals(e1)).isTrue();
        assertThat(e1.equals(e2)).isTrue();
        assertThat(e1.equals(e3)).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldReturnHashCode() {
        final Validation<String, String> v1 = Validation.valid("test");
        final Validation<String, String> e1 = Validation.invalid("error");

        assertThat(v1.hashCode()).isEqualTo(Objects.hashCode(v1));
        assertThat(e1.hashCode()).isEqualTo(Objects.hashCode(e1));
    }

    // -- toString

    @Test
    public void shouldReturnCorrectStringForToString() {
        final Validation<String, String> v1 = Validation.valid("test");
        final Validation<String, String> v2 = Validation.invalid("error");

        assertThat(v1.toString()).isEqualTo("Valid(test)");
        assertThat(v2.toString()).isEqualTo("Invalid(List(error))");
    }

    // ------------------------------------------------------------------------------------------ //

    private <E> Validation<E, String> valid() {
        return Validation.valid(OK);
    }

    private <T> Validation<String, T> invalid() {
        return Validation.invalidAll(ERRORS);
    }

    static class TestPojo {
        String name;
        Integer age;
        Option<String> address;
        String phone;
        String alt1;
        String alt2;
        String alt3;
        String alt4;

        TestPojo(String name, Integer age) {
            this.name = name;
            this.age = age;
            address = Option.none();
        }

        TestPojo(String name, Integer age, Option<String> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        TestPojo(String name, Integer age, Option<String> address, String phone) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
        }

        TestPojo(String name, Integer age, Option<String> address, String phone, String alt1) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
        }

        TestPojo(String name, Integer age, Option<String> address, String phone, String alt1, String alt2) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
        }

        TestPojo(String name, Integer age, Option<String> address, String phone, String alt1, String alt2, String alt3) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
            this.alt3 = alt3;
        }

        TestPojo(String name, Integer age, Option<String> address, String phone, String alt1, String alt2, String alt3, String alt4) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
            this.alt3 = alt3;
            this.alt4 = alt4;
        }

        @Override
        public String toString() {
            return "TestPojo(" + name + "," + age + "," + address.getOrElse("none") + phone + "," + ")";
        }
    }

    static class TestNestedPojo {
        Option<String> pojoName;
        TestPojo testPojo;

        TestNestedPojo(Option<String> pojoName, TestPojo testPojo) {
            this.pojoName = pojoName;
            this.testPojo = testPojo;
        }

        @Override
        public String toString() {
            return "TestNestedPojo(" + pojoName + "," + testPojo + ")";
        }
    }

    // -- Complete Validation example, may be moved to Vavr documentation later

    @Test
    public void shouldValidateValidPerson() {
        final String name = "John Doe";
        final int age = 30;
        final Validation<String, Person> actual = new PersonValidator().validatePerson(name, age);
        final Validation<String, Person> expected = Validation.valid(new Person(name, age));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldValidateInvalidPerson() {
        final String name = "John? Doe!4";
        final int age = -1;
        final Validation<String, Person> actual = new PersonValidator().validatePerson(name, age);
        final Validation<String, Person> expected = Validation.invalid(
                "Name contains invalid characters: '!4?'",
                "Age must be greater than 0"
        );
        assertThat(actual).isEqualTo(expected);
    }

    static class PersonValidator {

        private final String validNameChars = "[a-zA-Z ]";
        private final int minAge = 0;

        Validation<String, Person> validatePerson(String name, int age) {
            return Validation.combine(validateName(name), validateAge(age)).ap(Person::new);
        }

        private Validation<String, String> validateName(String name) {
            return CharSeq.of(name).replaceAll(validNameChars, "").transform(seq ->
                    seq.isEmpty() ? Validation.<String, String> valid(name)
                                  : Validation.<String, String> invalid("Name contains invalid characters: '" + seq.distinct().sorted() + "'"));
        }

        private Validation<String, Integer> validateAge(int age) {
            return (age < minAge) ? Validation.invalid("Age must be greater than 0")
                                  : Validation.valid(age);
        }
    }

    static class Person {

        final String name;
        final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Person) {
                final Person person = (Person) o;
                return Objects.equals(name, person.name) && age == person.age;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public String toString() {
            return "Person(" + name + ", " + age + ")";
        }
    }

    // -- spliterator

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

    // -- onInvalid

    private <T> Consumer<Seq<T>> withSideEffectOn(ArrayList<T> effect) {
        return e -> e.forEach(effect::add);
    }
    
    @Test
    public void shouldOnInvalidNoEffectOnValid() {
        final ArrayList<Integer> mutableList = new ArrayList<>();
        final Validation<Integer, String> expected = Validation.valid("");
        final Validation<Integer, String> actual = expected.onInvalid(withSideEffectOn(mutableList));
        assertThat(actual).isEqualTo(expected);
        assertThat(mutableList).isEmpty();
    }

    @Test
    public void shouldOnInvalidEffectOnSingleInvalid() {
        final ArrayList<Integer> mutableList = new ArrayList<>();
        final Validation<Integer, String> expected = Validation.invalid(1);
        final Validation<Integer, String> actual = expected.onInvalid(withSideEffectOn(mutableList));
        assertThat(actual).isEqualTo(expected);
        assertThat(mutableList).containsExactly(1);
    }

    @Test
    public void shouldOnInvalidEffectOnMultipleInvalid() {
        final ArrayList<Integer> mutableList = new ArrayList<>();
        final Validation<Integer, String> expected = Validation.invalid(1, 2, 3);
        final Validation<Integer, String> actual = expected.onInvalid(withSideEffectOn(mutableList));
        assertThat(actual).isEqualTo(expected);
        assertThat(mutableList).containsExactly(1, 2, 3);
    }

    // -- onValid

    @Test
    public void shouldOnValidEffectOnSingleInvalid() {
        final ArrayList<String> mutableList = new ArrayList<>();
        final Validation<Integer, String> expected = Validation.valid("");
        final Validation<Integer, String> actual = expected.onValid(mutableList::add);
        assertThat(actual).isEqualTo(expected);
        assertThat(mutableList).containsExactly("");
    }

    @Test
    public void shouldOnValidNoEffectOnValid() {
        final ArrayList<String> mutableList = new ArrayList<>();
        final Validation<Integer, String> expected = Validation.invalid(1);
        final Validation<Integer, String> actual = expected.onValid(mutableList::add);
        assertThat(actual).isEqualTo(expected);
        assertThat(mutableList).isEmpty();
    }
}
