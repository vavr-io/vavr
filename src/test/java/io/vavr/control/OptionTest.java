/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("deprecation")
class OptionTest {

    // -- Testees

    private static final String SOME_VALUE = "success";
    private static final Option<String> SOME = Option.some(SOME_VALUE);
    private static final Option<String> NONE = Option.none();

    private static final Error ERROR = new Error();
    private static final AssertionError ASSERTION_ERROR = new AssertionError("unexpected");

    // -- static .of(Callable)

    @Test
    void shouldCreateNoneWhenCallingOptionOfNullValue() {
        assertTrue(Option.of(null).isEmpty());
    }

    @Test
    void shouldCreateSomeWhenCallingOptionOfNonNullValue() {
        assertTrue(Option.of(SOME_VALUE).isDefined());
    }

    // -- static .some(Object)

    @Test
    void shouldCreateSomeWithNullValue() {
        assertTrue(Option.some(null).isDefined());
    }

    @Test
    void shouldCreateSomeWithNonNullValue() {
        assertTrue(Option.some(SOME_VALUE).isDefined());
    }

    @Test
    void shouldVerifyBasicSomeProperties() {
        assertSame(SOME_VALUE, SOME.get());
        assertFalse(SOME.isEmpty());
        assertTrue(SOME.isDefined());
        assertEquals(Option.some(SOME_VALUE), SOME);
        assertEquals(31 + Objects.hashCode(SOME_VALUE), SOME.hashCode());
        assertEquals("Some(" + SOME_VALUE + ")", SOME.toString());
    }

    // -- static .none()

    @Test
    void shouldCreateSingletonNone() {
        assertSame(NONE, Option.none());
    }

    @Test
    void shouldVerifyBasicNoneProperties() {
        assertTrue(NONE.isEmpty());
        assertFalse(NONE.isDefined());
        assertSame(Option.none(), NONE);
        assertEquals(1, NONE.hashCode());
        assertEquals("None", NONE.toString());
    }

    // -- static .when(boolean, Supplier)

    @Test
    void shouldThrowIfWhenConditionIsFalseAndSupplierIsNull() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> Option.when(false, null)).getMessage()
        );
    }

    @Test
    void shouldThrowIfWhenConditionIsTrueAndSupplierIsNull() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> Option.when(true, null)).getMessage()
        );
    }

    @Test
    void shouldCreateNoneIfWhenConditionIsFalseAndSupplierReturnsNull() {
        assertSame(NONE, Option.when(false, () -> null));
    }

    @Test
    void shouldCreateNoneIfWhenConditionIsFalseAndSupplierReturnsNonNull() {
        assertSame(NONE, Option.when(false, () -> SOME_VALUE));
    }

    @Test
    void shouldCreateSomeIfWhenConditionIsTrueAndSupplierReturnsNull() {
        assertEquals(Option.some(null), Option.when(true, () -> null));
    }

    @Test
    void shouldCreateSomeIfWhenConditionIsTrueAndSupplierReturnsNonNull() {
        assertEquals(SOME, Option.when(true, () -> SOME_VALUE));
    }

    // -- static .unless(boolean, Supplier)

    @Test
    void shouldThrowIfUnlessConditionIsFalseAndSupplierIsNull() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> Option.unless(false, null)).getMessage()
        );
    }

    @Test
    void shouldThrowIfUnlessConditionIsTrueAndSupplierIsNull() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> Option.unless(true, null)).getMessage()
        );
    }

    @Test
    void shouldCreateNoneIfUnlessConditionIsFalseAndSupplierReturnsNull() {
        assertEquals(Option.some(null), Option.unless(false, () -> null));
    }

    @Test
    void shouldCreateNoneIfUnlessConditionIsFalseAndSupplierReturnsNonNull() {
        assertEquals(SOME, Option.unless(false, () -> SOME_VALUE));
    }

    @Test
    void shouldCreateSomeIfUnlessConditionIsTrueAndSupplierReturnsNull() {
        assertSame(NONE, Option.unless(true, () -> null));
    }

    @Test
    void shouldCreateNoneIfUnlessConditionIsTrueAndSupplierReturnsNonNull() {
        assertSame(NONE, Option.unless(true, () -> SOME_VALUE));
    }

    // -- static .ofOptional(Optional)

    @Test
    void shouldCreateNoneOfEmptyOptional() {
        assertEquals(NONE, Option.ofOptional(Optional.empty()));
    }

    @Test
    void shouldCreateSomeOfDefinedOptional() {
        assertEquals(SOME, Option.ofOptional(Optional.of(SOME_VALUE)));
    }

    // -- .filter(Predicate)

    @Test
    void shouldFilterMatchingPredicateIfEmpty() {
        assertSame(NONE, NONE.filter(s -> true));
    }

    @Test
    void shouldFilterNonMatchingPredicateIfEmpty() {
        assertSame(NONE, NONE.filter(s -> false));
    }

    @Test
    void shouldFilterMatchingPredicateIfDefined() {
        assertSame(SOME, SOME.filter(s -> true));
    }

    @Test
    void shouldFilterNonMatchingPredicateIfDefined() {
        assertSame(NONE, SOME.filter(s -> false));
    }

    @Test
    void shouldThrowNPEWhenFilteringNoneWithNullPredicate() {
        assertEquals(
                "predicate is null",
                assertThrows(NullPointerException.class, () -> NONE.filter(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFilteringSomeWithNullPredicate() {
        assertEquals(
                "predicate is null",
                assertThrows(NullPointerException.class, () -> SOME.filter(null)).getMessage()
        );
    }

    // -- .collect(Collector)

    @Test
    void shouldCollectNone() {
        assertEquals("", NONE.collect(Collectors.joining()));
    }

    @Test
    void shouldCollectSome() {
        assertEquals(SOME_VALUE, SOME.collect(Collectors.joining()));
    }

    // -- .flatMap(Function)

    @Test
    void shouldFlatMapSomeToNull() {
        assertNull(SOME.flatMap(ignored -> null));
    }

    @Test
    void shouldFlatMapToSomeIfSome() {
        assertSame(SOME, SOME.flatMap(ignored -> SOME));
    }

    @Test
    void shouldFlatMapToNoneIfSome() {
        assertSame(NONE, SOME.flatMap(ignored -> NONE));
    }

    @Test
    void shouldFlatMapIfNone() {
        assertSame(NONE, NONE.flatMap(ignored -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenFlatMappingNoneWithNullParam() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> NONE.flatMap(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFlatMappingSomeWithNullParam() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> SOME.flatMap(null)).getMessage()
        );
    }

    // -- .fold(Supplier, Function)

    @Test
    void shouldFoldNone() {
        assertEquals(0, Option.none().<Integer> fold(() -> 0, s -> 1).intValue());
    }

    @Test
    void shouldFoldSomeWhenValueIsNull() {
        assertEquals(1, Option.some(null).<Integer> fold(() -> 0, s -> 1).intValue());
    }

    @Test
    void shouldFoldNoneToNull() {
        assertNull(NONE.fold(() -> null, s -> ""));
    }

    @Test
    void shouldFoldSomeToNull() {
        assertNull(SOME.fold(() -> "", s -> null));
    }

    @Test
    void shouldFoldAndReturnValueIfSome() {
        final int folded = SOME.fold(() -> { throw ASSERTION_ERROR; }, String::length);
        assertEquals(SOME_VALUE.length(), folded);
    }

    @Test
    void shouldFoldAndReturnAlternateValueIfNone() {
        final String folded = NONE.fold(() -> SOME_VALUE, a -> { throw ASSERTION_ERROR; });
        assertEquals(SOME_VALUE, folded);
    }

    @Test
    void shouldFoldSomeAndThrowNPEWhenIfNoneFunctionIsNull() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> SOME.fold(null, Function.identity())).getMessage()
        );
    }

    @Test
    void shouldFoldNoneAndThrowNPEWhenIfNoneFunctionIsNull() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> NONE.fold(null, Function.identity())).getMessage()
        );
    }

    @Test
    void shouldFoldSomeAndThrowNPEWhenIfSomeFunctionIsNull() {
        assertEquals(
                "ifDefined is null",
                assertThrows(NullPointerException.class, () -> SOME.fold(() -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    @Test
    void shouldFoldNoneAndThrowNPEOnWhenIfSomeFunctionIsNull() {
        assertEquals(
                "ifDefined is null",
                assertThrows(NullPointerException.class, () -> NONE.fold(() -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    // -- .forEach(Consumer)

    @Test
    void shouldConsumeNoneWithForEach() {
        final List<String> list = new ArrayList<>();
        NONE.forEach(list::add);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    void shouldConsumeSomeWithForEach() {
        final List<String> list = new ArrayList<>();
        SOME.forEach(list::add);
        assertEquals(Collections.singletonList(SOME_VALUE), list);
    }

    @Test
    void shouldThrowNPEWhenConsumingFailureWithForEachAndActionIsNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> NONE.forEach(null));
    }

    @Test
    void shouldThrowNPEWhenConsumingSuccessWithForEachAndActionIsNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> SOME.forEach(null));
    }

    // -- .get()

    @Test
    void shouldGetOnSomeWhenValueIsNull() {
        assertNull(Option.some(null).get());
    }

    @Test
    void shouldGetOnSomeWhenValueIsNonNull() {
        assertEquals(SOME_VALUE, SOME.get());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenGetOnNone() {
        assertEquals(
                "get() on None",
                assertThrows(NoSuchElementException.class, NONE::get).getMessage()
        );
    }

    // -- .getOrElse(Object)

    @Test
    void shouldReturnElseWhenGetOrElseOnNone() {
        assertSame(SOME_VALUE, NONE.getOrElse(SOME_VALUE));
    }

    @Test
    void shouldGetOrElseOnSome() {
        assertSame(SOME_VALUE, SOME.getOrElse(null));
    }

    // -- .getOrElseGet(Supplier)

    @Test
    void shouldReturnElseWhenGetOrElseGetOnNone() {
        assertSame(SOME_VALUE, NONE.getOrElseGet(() -> SOME_VALUE));
    }

    @Test
    void shouldGetOrElseGetOnSome() {
        assertSame(SOME_VALUE, SOME.getOrElseGet(() -> { throw ASSERTION_ERROR; }));
    }

    // -- .getOrElseThrow(Supplier)

    @Test
    void shouldThrowOtherWhenGetOrElseThrowOnNone() {
        assertSame(
                ERROR,
                assertThrows(ERROR.getClass(), () -> NONE.getOrElseThrow(() -> ERROR))
        );
    }

    @Test
    void shouldOrElseThrowOnSome() {
        assertSame(SOME_VALUE, SOME.getOrElseThrow(() -> null));
    }

    @Test
    void shouldThrowNPEWhenWhenGetOrElseThrowOnNoneAndExceptionProviderIsNull() {
        assertEquals(
                "exceptionProvider is null",
                assertThrows(NullPointerException.class, () -> NONE.getOrElseThrow(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenWhenGetOrElseThrowOnSomeAndExceptionProviderIsNull() {
        assertEquals(
                "exceptionProvider is null",
                assertThrows(NullPointerException.class, () -> SOME.getOrElseThrow(null)).getMessage()
        );
    }

    // -- .isEmpty()

    @Test
    void shouldDetectFailureIfNone() {
        assertTrue(NONE.isEmpty());
    }

    @Test
    void shouldDetectNonFailureIfSome() {
        assertFalse(SOME.isEmpty());
    }

    // -- .isDefined()

    @Test
    void shouldDetectSuccessIfSome() {
        assertTrue(SOME.isDefined());
    }

    @Test
    void shouldDetectNonSuccessIfSome() {
        assertFalse(NONE.isDefined());
    }

    // -- .iterator()

    @Test
    void shouldIterateSome() {
        final Iterator<String> testee = SOME.iterator();
        assertTrue(testee.hasNext());
        assertSame(SOME_VALUE, testee.next());
        assertFalse(testee.hasNext());
        assertThrows(NoSuchElementException.class, testee::next);
    }

    @Test
    void shouldIterateNone() {
        final Iterator<String> testee = NONE.iterator();
        assertFalse(testee.hasNext());
        assertThrows(NoSuchElementException.class, testee::next);
    }

    // -- .map(Function)

    @Test
    void shouldMapNone() {
        assertSame(NONE, NONE.map(s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldMapSomeToNull() {
        assertEquals(Option.some(null), SOME.map(s -> null));
    }


    @Test
    void shouldMapSomeToNonNull() {
        assertEquals(Option.some(SOME_VALUE + "!"), SOME.map(s -> s + "!"));
    }

    @Test
    void shouldThrowNPEWhenMappingNoneAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> NONE.map(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenMappingSomeAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> SOME.map(null)).getMessage()
        );
    }

    // -- .onEmpty(Runnable)

    @Test
    void shouldConsumeThrowableWhenCallingOnEmptyGivenNone() {
        final List<String> sideEffect = new ArrayList<>();
        NONE.onEmpty(() -> sideEffect.add(null));
        assertEquals(Collections.singletonList(null), sideEffect);
    }

    @Test
    void shouldDoNothingWhenCallingOnEmptyGivenSome() {
        assertSame(SOME, SOME.onEmpty(() -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenCallingOnEmptyWithNullParamGivenNone() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> NONE.onEmpty(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCallingOnEmptyWithNullParamGivenSome() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> SOME.onEmpty(null)).getMessage()
        );
    }

    // -- .onSuccess(Consumer)

    @Test
    void shouldConsumeValueWhenCallingOnSomeGivenSuccess() {
        final List<String> sideEffect = new ArrayList<>();
        SOME.onDefined(sideEffect::add);
        assertEquals(Collections.singletonList(SOME_VALUE), sideEffect);
    }

    @Test
    void shouldDoNothingWhenCallingOnSomeGivenNone() {
        assertSame(NONE, NONE.onDefined(x -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenCallingOnSuccessWithNullParamOnFailure() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> NONE.onDefined(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCallingOnSuccessWithNullParamOnSuccess() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> SOME.onDefined(null)).getMessage()
        );
    }

    // -- .orElse(Supplier)

    @Test
    void shouldReturnSelfOnOrElseIfSome() {
        assertSame(SOME, SOME.orElse(() -> null));
    }

    @Test
    void shouldReturnAlternativeOnOrElseIfNone() {
        assertSame(SOME, NONE.orElse(() -> SOME));
    }

    @Test
    void shouldThrowNPEOnOrElseWithNullParameterIfSome() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> SOME.orElse(null)).getMessage()

        );
    }

    @Test
    void shouldThrowNPEOnOrElseWithNullParameterIfNone() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> NONE.orElse(null)).getMessage()
        );
    }

    // -- .stream()

    @Test
    void shouldStreamNone() {
        assertEquals(Collections.emptyList(), NONE.stream().collect(Collectors.toList()));
    }

    @Test
    void shouldStreamSome() {
        assertEquals(Collections.singletonList(SOME_VALUE), SOME.stream().collect(Collectors.toList()));
    }

    // -- .toEither(Function)

    @Test
    void shouldConvertNoneToEitherUsingSupplier() {
        assertEquals(Either.left(null), NONE.toEither(() -> null));
    }

    @Test
    void shouldConvertSomeToEither() {
        assertEquals(Either.right(SOME_VALUE), SOME.toEither(() -> null));
    }

    @Test
    void shouldThrowNPEWhenConvertingNoneToEitherUsingNullThrowableMapper() {
        assertEquals(
                "leftSupplier is null",
                assertThrows(NullPointerException.class, () -> NONE.toEither(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenConvertingSomeToEitherUsingNullThrowableMapper() {
        assertEquals(
                "leftSupplier is null",
                assertThrows(NullPointerException.class, () -> SOME.toEither(null)).getMessage()
        );
    }

    // -- .toOptional()

    @Test
    void shouldConvertNoneToOptional() {
        assertEquals(Optional.empty(), NONE.toOptional());
    }


    @Test
    void shouldConvertSomeOfNonNullToOptional() {
        assertEquals(Optional.of(SOME_VALUE), SOME.toOptional());
    }

    @Test
    void shouldConvertSomeOfNullToOptional() {
        assertEquals(Optional.empty(), Option.some(null).toOptional());
    }

    // -- .toTry(Supplier)

    @Test
    void shouldConvertNoneToTryUsingSupplier() {
        assertEquals(Try.failure(ERROR), NONE.toTry(() -> ERROR));
    }


    @Test
    void shouldConvertSomeOfNonNullToTryUsingSupplier() {
        assertEquals(Try.success(SOME_VALUE), SOME.toTry(() -> ERROR));
    }

    @Test
    void shouldConvertSomeOfNullToTryUsingSupplier() {
        assertEquals(Try.success(null), Option.some(null).toTry(() -> ERROR));
    }

    @Test
    void shouldThrowNPEWhenConvertingNoneToTryUsingNullIfEmptySupplier() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> NONE.toTry(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenConvertingSomeToTryUsingNullIfEmptySupplier() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> SOME.toTry(null)).getMessage()
        );
    }

    // -- .transform(Function, Function)

    @Test
    void shouldTransformNone() {
        assertSame(SOME, Option.none().transform(() -> SOME, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldTransformSomeWhenValueIsNull() {
        assertSame(SOME, Option.some(null).transform(() -> { throw ASSERTION_ERROR; }, s -> SOME));
    }

    @Test
    void shouldTransformNoneToNull() {
        assertNull(NONE.transform(() -> null, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldTransformSomeValueToNull() {
        assertNull(SOME.transform(() -> { throw ASSERTION_ERROR; }, s -> null));
    }

    @Test
    void shouldTransformSomeValueToNonNull() {
        final Option<Integer> transformed = SOME.transform(() -> { throw ASSERTION_ERROR; }, s -> Option.some(s.length()));
        assertEquals(Option.some(SOME_VALUE.length()), transformed);
    }

    @Test
    void shouldTransformNoneAndReturnAlternateValue() {
        final Option<String> transformed = NONE.transform(() -> SOME, a -> { throw ASSERTION_ERROR; });
        assertSame(SOME, transformed);
    }

    @Test
    void shouldTransformSomeAndThrowNPEWhenIfEmptySupplierIsNull() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> SOME.transform(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldTransformNoneAndThrowNPEOnWhenIfEmptySupplierIsNull() {
        assertEquals(
                "ifEmpty is null",
                assertThrows(NullPointerException.class, () -> NONE.transform(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldTransformSomeAndThrowNPEOnWhenIfDefinedFunctionIsNull() {
        assertEquals(
                "ifDefined is null",
                assertThrows(NullPointerException.class, () -> SOME.transform(() -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    @Test
    void shouldTransformNoneAndThrowNPEOnWhenIfDefinedFunctionIsNull() {
        assertEquals(
                "ifDefined is null",
                assertThrows(NullPointerException.class, () -> NONE.transform(() -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    // -- Object.equals(Object)

    @Test
    void shouldEqualNone() {
        assertEquals(Option.none(), Option.none());
    }

    @Test
    void shouldEqualSomeIfObjectIsSame() {
        assertEquals(SOME, SOME);
    }

    @Test
    void shouldNotEqualNoneAndSome() {
        assertNotEquals(NONE, SOME);
    }

    @Test
    void shouldNotEqualSomeAndNone() {
        assertNotEquals(SOME, NONE);
    }

    @Test
    void shouldNotEqualSomeIfValuesDiffer() {
        assertNotEquals(Option.some("1"), Option.some(1));
    }

    // -- Object.hashCode()

    @Test
    void shouldHashNone() {
        assertEquals(1, NONE.hashCode());
    }

    @Test
    void shouldHashSome() {
        assertEquals(31 + Objects.hashCode(SOME_VALUE), SOME.hashCode());
    }

    @Test
    void shouldHashSomeWithNullValue() {
        assertEquals(31 + Objects.hashCode(null), Option.some(null).hashCode());
    }

    // -- Object.toString()

    @Test
    void shouldConvertNoneToString() {
        assertEquals("None", NONE.toString());
    }

    @Test
    void shouldConvertSomeToString() {
        assertEquals("Some(" + SOME_VALUE + ")", SOME.toString());
    }

    @Test
    void shouldConvertSomeWithNullValueToString() {
        assertEquals("Some(null)", Option.some(null).toString());
    }

    // Serialization

    @Test
    void shouldSerializeNone() throws IOException, ClassNotFoundException {
        final Option<String> testee = deserialize(serialize(NONE));
        assertSame(NONE, testee);
    }

    @Test
    void shouldSerializeSome() throws IOException, ClassNotFoundException {
        assertEquals(SOME, deserialize(serialize(SOME)));
    }

    private static byte[] serialize(Object obj) throws IOException {
        try (final ByteArrayOutputStream buf = new ByteArrayOutputStream(); final ObjectOutputStream stream = new ObjectOutputStream(buf)) {
            stream.writeObject(obj);
            return buf.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (final ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (T) stream.readObject();
        }
    }

}
