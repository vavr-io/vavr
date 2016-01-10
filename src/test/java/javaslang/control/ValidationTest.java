/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.collection.List;
import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ValidationTest {

    public static final String OK = "ok";
    public static final List<String> ERRORS = List.of("error1", "error2", "error3");

    // -- Validation.valid

    @Test
    public void shouldCreateSuccessWhenCallingValidationSuccess() {
        assertThat(Validation.valid(1) instanceof Validation.Valid).isTrue();
    }

    @Test
    public void shouldCreateSuccessWhenCallingValidationSuccessSupplier() {
        assertThat(Validation.valid(() -> 1) instanceof Validation.Valid).isTrue();
    }

    // -- Validation.invalid

    @Test
    public void shouldCreateFailureWhenCallingValidationFailure() {
        assertThat(Validation.invalid("error") instanceof Validation.Invalid).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingValidationFailureSupplier() {
        assertThat(Validation.invalid(() -> "error") instanceof Validation.Invalid).isTrue();
    }

    // -- fold

    @Test
    public void shouldConvertSuccessToU() {
        Validation<List<String>,String> successValidation = success();
        Integer result = successValidation.fold(List::length, String::length);
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void shouldConvertFailureToU() {
        Validation<List<String>,String> failureValidation = failure();
        Integer result = failureValidation.fold(List::length, String::length);
        assertThat(result).isEqualTo(3);
    }

    // -- swap

    @Test
    public void shouldSwapSuccessToFailure() {
        assertThat(success().swap() instanceof Validation.Invalid).isTrue();
        assertThat(success().swap().error()).isEqualTo(OK);
    }

    @Test
    public void shouldSwapFailureToSuccess() {
        assertThat(failure().swap() instanceof Validation.Valid).isTrue();
        assertThat(failure().swap().get()).isEqualTo(ERRORS);
    }

    // -- map

    @Test
    public void shouldMapSuccessValue() {
        assertThat(success().map(s -> s + "!").get()).isEqualTo(OK + "!");
    }

    @Test
    public void shouldMapFailureError() {
        assertThat(failure().map(s -> 2).error()).isEqualTo(ERRORS);
    }

    @Test(expected = RuntimeException.class)
    public void shouldMapFailureErrorOnGet() {
        assertThat(failure().map(s -> 2).get()).isEqualTo(ERRORS);
    }

    // -- bimap

    @Test
    public void shouldMapOnlySuccessValue() {
        Validation<List<String>,String> successValidation = success();
        Validation<Integer,Integer> successMapping = successValidation.bimap(List::length, String::length);
        assertThat(successMapping instanceof Validation.Valid).isTrue();
        assertThat(successMapping.get()).isEqualTo(2);
    }

    @Test
    public void shouldMapOnlyFailureValue() {
        Validation<List<String>,String> failureValidation = failure();
        Validation<Integer,Integer> failureMapping = failureValidation.bimap(List::length, String::length);
        assertThat(failureMapping instanceof Validation.Invalid).isTrue();
        assertThat(failureMapping.error()).isEqualTo(3);
    }

    // -- leftMap

    @Test
    public void shouldNotMapSuccess() {
        assertThat(success().leftMap(x -> 2).get()).isEqualTo(OK);
    }

    @Test
    public void shouldMapFailure() {
        assertThat(failure().leftMap(x -> 5).error()).isEqualTo(5);
    }

    // -- forEach

    @Test
    public void shouldProcessFunctionInForEach() {
        Validation<String,String>  v1 = Validation.valid("Eric Nelson");
        Validation<String,String>  v2 = Validation.invalid("error");

        // Not sure best way to test a side-effect only function?
        v1.forEach(System.out::println);
        assertThat(true).isTrue();
    }

    // -- combine and apply

    @Test
    public void shouldBuildUpForSuccess() {
        Validation<String,String>  v1 = Validation.valid("Eric Nelson");
        Validation<String,Integer> v2 = Validation.valid(39);
        Validation<String,Option<String>> v3 = Validation.valid(Option.of("address"));
        Validation<String,Option<String>> v4 = Validation.valid(Option.none());

        Validation<List<String>,TestValidation> result  = v1.combine(v2).ap(TestValidation::new);
        Validation<List<String>,TestValidation> result2 = v1.combine(v2).combine(v3).ap(TestValidation::new);
        Validation<List<String>,TestValidation> result3 = v1.combine(v2).combine(v4).ap(TestValidation::new);
        Validation<List<String>,String> result4 = v1.combine(v2).combine(v3).ap((p1, p2, p3) -> p1+":"+p2+":"+p3.orElse("none"));

        // Alternative map(n) functions to the 'combine' function
        Validation<List<String>,TestValidation> result5 = Validation.map2(v1, v2).ap(TestValidation::new);
        Validation<List<String>,TestValidation> result6 = Validation.map3(v1, v2, v3).ap(TestValidation::new);
        Validation<List<String>,String> result7 = Validation.map3(v1, v2, v3).ap((p1, p2, p3) -> p1+":"+p2+":"+p3.orElse("none"));

        assertThat(result.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result4.isValid()).isTrue();

        assertThat(result.get() instanceof TestValidation).isTrue();
        assertThat(result4.get() instanceof String).isTrue();
    }

    @Test
    public void shouldBuildUpForFailure() {
        Validation<String,String>  v1 = Validation.valid("Eric Nelson");
        Validation<String,Integer> v2 = Validation.valid(39);
        Validation<String,Option<String>> v3 = Validation.valid(Option.of("address"));

        Validation<String,String>  e1 = Validation.invalid("error2");
        Validation<String,Integer> e2 = Validation.invalid("error1");
        Validation<String,Option<String>>  e3 = Validation.invalid("error3");

        Validation<List<String>,TestValidation> result  = v1.combine(e2).combine(v3).ap(TestValidation::new);
        Validation<List<String>,TestValidation> result2 = e1.combine(v2).combine(e3).ap(TestValidation::new);

        assertThat(result.isInvalid()).isTrue();
        assertThat(result2.isInvalid()).isTrue();
    }

    // -- miscellaneous

    @Test
    public void shouldMatchLikeObjects() {
        Validation<String,String> v1 = Validation.valid("test");
        Validation<String,String> v2 = Validation.valid("test");
        Validation<String,String> v3 = Validation.valid("test diff");

        Validation<String,String> e1 = Validation.invalid("error1");
        Validation<String,String> e2 = Validation.invalid("error1");
        Validation<String,String> e3 = Validation.invalid("error diff");

        assertThat(v1.equals(v1)).isTrue();
        assertThat(v1.equals(v2)).isTrue();
        assertThat(v1.equals(v3)).isFalse();

        assertThat(e1.equals(e1)).isTrue();
        assertThat(e1.equals(e2)).isTrue();
        assertThat(e1.equals(e3)).isFalse();
    }

    @Test
    public void shouldReturnCorrectStringForToString() {
        Validation<String,String> v1 = Validation.valid("test");
        Validation<String,String> v2 = Validation.invalid("error");

        assertThat(v1.toString()).isEqualTo("Valid(test)");
        assertThat(v2.toString()).isEqualTo("Invalid(error)");
    }

    @Test
    public void shouldReturnHashCode() {
        Validation<String,String> v1 = Validation.valid("test");
        Validation<String,String> e1 = Validation.invalid("error");

        assertThat(v1.hashCode()).isEqualTo(Objects.hashCode(v1));
        assertThat(e1.hashCode()).isEqualTo(Objects.hashCode(e1));
    }

    // ------------------------------------------------------------------------------------------ //

    private <E> Validation<E,String> success() {
        return Validation.valid(OK);
    }

    private <T> Validation<List<String>,T> failure() {
        return Validation.invalid(ERRORS);
    }

    public static class TestValidation {
        public String name;
        public Integer age;
        public Option<String> address;

        public TestValidation(String name, Integer age) {
            this.name = name;
            this.age = age;
            address = Option.none();
        }

        public TestValidation(String name, Integer age, Option<String> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        @Override
        public String toString() {
            return "TestValidation("+name+","+age+","+address.orElse("none")+")";
        }
    }

}
