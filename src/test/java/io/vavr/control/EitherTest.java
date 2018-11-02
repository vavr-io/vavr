/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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

@SuppressWarnings("deprecation")
class EitherTest {

    // -- Testees

    private static final String RIGHT_VALUE = "right";
    private static final Either<Integer, String> RIGHT = Either.right(RIGHT_VALUE);

    private static final int LEFT_VALUE = 0;
    private static final Either<Integer, String> LEFT = Either.left(LEFT_VALUE);

    private static final Error ERROR = new Error();
    private static final AssertionError ASSERTION_ERROR = new AssertionError("unexpected");

    // -- static .left(Object)

    @Test
    void shouldCreateLeftWithNullValue() {
        assertNotNull(Either.left(null));
    }

    @Test
    void shouldCreateLeft() {
        assertNotNull(Either.left(LEFT_VALUE));
    }

    @Test
    void shouldVerifyBasicLeftProperties() {
        assertEquals(
                "get() on Left",
                assertThrows(NoSuchElementException.class, LEFT::get).getMessage()
        );
        assertSame(LEFT_VALUE, LEFT.getLeft());
        assertFalse(LEFT.isRight());
        assertTrue(LEFT.isLeft());
        assertEquals(Either.left(LEFT_VALUE), LEFT);
        assertEquals(31 + Objects.hashCode(LEFT_VALUE), LEFT.hashCode());
        assertEquals("Left(" + LEFT_VALUE + ")", LEFT.toString());
    }

    // -- static .right(Object)

    @Test
    void shouldCreateRightWithNullValue() {
        assertNotNull(Either.right(null));
    }

    @Test
    void shouldCreateRight() {
        assertNotNull(Either.right(RIGHT_VALUE));
    }

    @Test
    void shouldVerifyBasicRightProperties() {
        assertSame(
                "getLeft() on Right",
                assertThrows(NoSuchElementException.class, RIGHT::getLeft).getMessage()
        );
        assertFalse(RIGHT.isLeft());
        assertTrue(RIGHT.isRight());
        assertEquals(Either.right(RIGHT_VALUE), RIGHT);
        assertEquals(31 + Objects.hashCode(RIGHT_VALUE), RIGHT.hashCode());
        assertEquals("Right(" + RIGHT_VALUE + ")", RIGHT.toString());
    }

    // -- static .cond(boolean, Supplier, Supplier)

    @Test
    void shouldCreateConditionalEitherWhenConditionIsTrue() {
        assertEquals(RIGHT, Either.cond(true, () -> LEFT_VALUE, () -> RIGHT_VALUE));
    }

    @Test
    void shouldCreateConditionalEitherWhenConditionIsFalse() {
        assertEquals(LEFT, Either.cond(false, () -> LEFT_VALUE, () -> RIGHT_VALUE));
    }

    @Test
    void shouldThrowNPEWhenCreatingConditionalEitherWhenConditionIsTrueAndRightSupplierIsNull() {
        assertEquals(
                "rightSupplier is null",
                assertThrows(NullPointerException.class, () -> Either.cond(true, () -> LEFT_VALUE, null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCreatingConditionalEitherWhenConditionIsFalseAndLeftSupplierIsNull() {
        assertEquals(
                "leftSupplier is null",
                assertThrows(NullPointerException.class, () -> Either.cond(false, null, () -> RIGHT_VALUE)).getMessage()
        );
    }

    // -- .collect(Collector)

    @Test
    void shouldCollectNone() {
        assertEquals("", LEFT.collect(Collectors.joining()));
    }

    @Test
    void shouldCollectSome() {
        assertEquals(RIGHT_VALUE, RIGHT.collect(Collectors.joining()));
    }

    // -- .filterOrElse(Predicate, Function)

    @Test
    void shouldFilterMatchingPredicateOnLeft() {
        assertSame(LEFT, LEFT.filterOrElse(s -> true, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldFilterNonMatchingPredicateOnLeft() {
        assertSame(LEFT, LEFT.filterOrElse(s -> false, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldFilterMatchingPredicateOnRight() {
        assertSame(RIGHT, RIGHT.filterOrElse(s -> true, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldFilterNonMatchingPredicateOnRight() {
        assertEquals(LEFT, RIGHT.filterOrElse(s -> false, s -> LEFT_VALUE));
    }

    @Test
    void shouldThrowNPEWhenFilteringLeftWithNullPredicate() {
        assertEquals(
                "predicate is null",
                assertThrows(NullPointerException.class, () -> LEFT.filterOrElse(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFilteringLeftWithNullZero() {
        assertEquals(
                "zero is null",
                assertThrows(NullPointerException.class, () -> LEFT.filterOrElse(s -> false, null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFilteringRightWithNullPredicate() {
        assertEquals(
                "predicate is null",
                assertThrows(NullPointerException.class, () -> RIGHT.filterOrElse(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFilteringRightWithNullZero() {
        assertEquals(
                "zero is null",
                assertThrows(NullPointerException.class, () -> RIGHT.filterOrElse(s -> false, null)).getMessage()
        );
    }

    @Test
    void shouldFilterLeftWithNullValue() {
        assertNotNull(Either.left(null).filterOrElse(x -> true, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldFilterRightWithNullValue() {
        assertNotNull(Either.right(null).filterOrElse(x -> true, s -> { throw ASSERTION_ERROR; }));
    }

    // -- .flatMap(Function)

    @Test
    void shouldFlatMapRightToNull() {
        assertNull(RIGHT.flatMap(ignored -> null));
    }

    @Test
    void shouldFlatMapToRightOnRight() {
        assertSame(RIGHT, RIGHT.flatMap(ignored -> RIGHT));
    }

    @Test
    void shouldFlatMapToLeftOnRight() {
        assertSame(LEFT, RIGHT.flatMap(ignored -> LEFT));
    }

    @Test
    void shouldFlatMapOnLeft() {
        assertSame(LEFT, LEFT.flatMap(ignored -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenFlatMappingLeftWithNullParam() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> LEFT.flatMap(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenFlatMappingRightWithNullParam() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> RIGHT.flatMap(null)).getMessage()
        );
    }

    @Test
    void shouldFlatMapLeftWithNullValue() {
        assertNotNull(Either.left(null).flatMap(x -> null));
    }

    @Test
    void shouldFlatMapRightWithNullValue() {
        assertSame(RIGHT, Either.<Integer, String> right(null).flatMap(s -> RIGHT));
    }

    // -- .fold(Function, Function)

    @Test
    void shouldFoldLeftWhenValueIsNull() {
        assertEquals(0, Either.left(null).fold(x -> 0, s -> 1).intValue());
    }

    @Test
    void shouldFoldRightWhenValueIsNull() {
        assertEquals(1, Either.right(null).fold(x -> 0, s -> 1).intValue());
    }

    @Test
    void shouldFoldLeftToNull() {
        assertNull(LEFT.fold(x -> null, s -> ""));
    }

    @Test
    void shouldFoldRightToNull() {
        assertNull(RIGHT.fold(x -> "", s -> null));
    }

    @Test
    void shouldFoldAndReturnValueIfRight() {
        final int folded = RIGHT.fold(x -> { throw ASSERTION_ERROR; }, String::length);
        assertEquals(RIGHT_VALUE.length(), folded);
    }

    @Test
    void shouldFoldAndReturnAlternateValueIfLeft() {
        final String folded = LEFT.fold(x -> RIGHT_VALUE, a -> { throw ASSERTION_ERROR; });
        assertEquals(RIGHT_VALUE, folded);
    }

    @Test
    void shouldFoldRightAndThrowNPEOnWhenIfLeftFunctionIsNull() {
        assertEquals(
                "ifLeft is null",
                assertThrows(NullPointerException.class, () -> RIGHT.fold(null, Function.identity())).getMessage()
        );
    }

    @Test
    void shouldFoldLeftAndThrowNPEOnWhenIfLeftFunctionIsNull() {
        assertEquals(
                "ifLeft is null",
                assertThrows(NullPointerException.class, () -> LEFT.fold(null, Function.identity())).getMessage()
        );
    }

    @Test
    void shouldFoldRightAndThrowNPEOnWhenIfRightFunctionIsNull() {
        assertEquals(
                "ifRight is null",
                assertThrows(NullPointerException.class, () -> RIGHT.fold(Function.identity(), null)).getMessage()
        );
    }

    @Test
    void shouldFoldLeftAndThrowNPEOnWhenIfRightFunctionIsNull() {
        assertEquals(
                "ifRight is null",
                assertThrows(NullPointerException.class, () -> LEFT.fold(Function.identity(), null)).getMessage()
        );
    }

    // -- .forEach(Consumer)

    @Test
    void shouldConsumeLeftWithForEach() {
        final List<String> list = new ArrayList<>();
        LEFT.forEach(list::add);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    void shouldConsumeRightWithForEach() {
        final List<String> list = new ArrayList<>();
        RIGHT.forEach(list::add);
        assertEquals(Collections.singletonList(RIGHT_VALUE), list);
    }

    @Test
    void shouldThrowNPEWhenConsumingLeftWithForEachAndActionIsNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> LEFT.forEach(null));
    }

    @Test
    void shouldThrowNPEWhenConsumingRightWithForEachAndActionIsNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> RIGHT.forEach(null));
    }

    // -- .get()

    @Test
    void shouldGetOnRightWhenValueIsNull() {
        assertNull(Either.right(null).get());
    }

    @Test
    void shouldGetOnRightWhenValueIsNonNull() {
        assertEquals(RIGHT_VALUE, RIGHT.get());
    }

    @Test
    void shouldThrowWhenGetOnLeft() {
        assertSame(
                "get() on Left",
                assertThrows(NoSuchElementException.class, LEFT::get).getMessage()
        );
    }

    // -- .getLeft()

    @Test
    void shouldGetLeftOnLeftWhenValueIsNull() {
        assertNull(Either.left(null).getLeft());
    }

    @Test
    void shouldGetLeftOnLeft() {
        assertSame(LEFT_VALUE, LEFT.getLeft());
    }

    @Test
    void shouldThrowWhenCallingGetLeftOnRight() {
        assertEquals(
                "getLeft() on Right",
                assertThrows(NoSuchElementException.class, RIGHT::getLeft).getMessage()
        );
    }

    // -- .getOrElse(Object)

    @Test
    void shouldReturnElseWhenGetOrElseOnLeft() {
        assertSame(RIGHT_VALUE, LEFT.getOrElse(RIGHT_VALUE));
    }

    @Test
    void shouldGetOrElseOnRight() {
        assertSame(RIGHT_VALUE, RIGHT.getOrElse(null));
    }

    // -- .getOrElseGet(Supplier)

    @Test
    void shouldReturnElseWhenGetOrElseGetOnLeft() {
        assertSame(RIGHT_VALUE, LEFT.getOrElseGet(ignored -> RIGHT_VALUE));
    }

    @Test
    void shouldGetOrElseGetOnRight() {
        assertSame(RIGHT_VALUE, RIGHT.getOrElseGet(ignored -> { throw ASSERTION_ERROR; }));
    }

    // -- .getOrElseThrow(Function)

    @Test
    void shouldThrowOtherWhenGetOrElseThrowOnLeft() {
        assertSame(
                ERROR,
                assertThrows(ERROR.getClass(), () -> LEFT.getOrElseThrow(x -> ERROR))
        );
    }

    @Test
    void shouldOrElseThrowOnRight() {
        assertSame(RIGHT_VALUE, RIGHT.getOrElseThrow(x -> null));
    }

    @Test
    void shouldThrowNPEWhenWhenGetOrElseThrowOnLeftAndExceptionProviderIsNull() {
        assertEquals(
                "exceptionProvider is null",
                assertThrows(NullPointerException.class, () -> LEFT.getOrElseThrow(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenWhenGetOrElseThrowOnRightAndExceptionProviderIsNull() {
        assertEquals(
                "exceptionProvider is null",
                assertThrows(NullPointerException.class, () -> RIGHT.getOrElseThrow(null)).getMessage()
        );
    }

    // -- .isLeft()

    @Test
    void shouldDetectLeftIfLeft() {
        assertTrue(LEFT.isLeft());
    }

    @Test
    void shouldDetectNonLeftIfRight() {
        assertFalse(RIGHT.isLeft());
    }

    // -- .isRight()

    @Test
    void shouldDetectRightIfRight() {
        assertTrue(RIGHT.isRight());
    }

    @Test
    void shouldDetectNonRightIfRight() {
        assertFalse(LEFT.isRight());
    }

    // -- .iterator()

    @Test
    void shouldIterateRight() {
        final Iterator<String> testee = RIGHT.iterator();
        assertTrue(testee.hasNext());
        assertSame(RIGHT_VALUE, testee.next());
        assertFalse(testee.hasNext());
        assertThrows(NoSuchElementException.class, testee::next);
    }

    @Test
    void shouldIterateLeft() {
        final Iterator<String> testee = LEFT.iterator();
        assertFalse(testee.hasNext());
        assertThrows(NoSuchElementException.class, testee::next);
    }

    // -- .map(Function)

    @Test
    void shouldMapLeft() {
        assertSame(LEFT, LEFT.map(ignored -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldMapRight() {
        assertEquals(Either.right(RIGHT_VALUE + "!"), RIGHT.map(s -> s + "!"));
    }

    @Test
    void shouldMapRightWhenValueIsNull() {
        assertEquals(Either.right("null!"), Either.right(null).map(s -> s + "!"));
    }

    @Test
    void shouldThrowNPEWhenMappingLeftAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> LEFT.map(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenMappingRightAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> RIGHT.map(null)).getMessage()
        );
    }

    // -- .mapLeft(Function)

    @Test
    void shouldMapLeftOnLeft() {
        assertEquals(Either.left(ERROR), LEFT.mapLeft(x -> ERROR));
    }

    @Test
    void shouldMapLeftOnLeftWhenValueIsNull() {
        assertEquals(Either.left(ERROR), Either.left(null).mapLeft(x -> ERROR));
    }

    @Test
    void shouldMapLeftOnRight() {
        assertSame(RIGHT, RIGHT.mapLeft(x -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenCallingMapLeftOnLeftAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> LEFT.mapLeft(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCallingMapLeftOnRightAndParamIsNull() {
        assertEquals(
                "mapper is null",
                assertThrows(NullPointerException.class, () -> RIGHT.mapLeft(null)).getMessage()
        );
    }

    // -- .onLeft(Consumer)

    @Test
    void shouldConsumeThrowableWhenCallingOnLeftGivenLeft() {
        final List<Integer> sideEffect = new ArrayList<>();
        LEFT.onLeft(sideEffect::add);
        assertEquals(Collections.singletonList(LEFT_VALUE), sideEffect);
    }

    @Test
    void shouldNotHandleUnexpectedExceptionWhenCallingOnLeftGivenLeft() {
        assertSame(
                ERROR,
                assertThrows(ERROR.getClass(), () -> LEFT.onLeft(ignored -> { throw ERROR; }))
        );
    }

    @Test
    void shouldDoNothingWhenCallingOnLeftGivenRight() {
        assertSame(RIGHT, RIGHT.onLeft(x -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenCallingOnLeftWithNullParamGivenLeft() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> LEFT.onLeft(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCallingOnLeftWithNullParamGivenRight() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> RIGHT.onLeft(null)).getMessage()
        );
    }

    // -- .onRight(Consumer)

    @Test
    void shouldConsumeValueWhenCallingOnRightGivenRight() {
        final List<String> sideEffect = new ArrayList<>();
        RIGHT.onRight(sideEffect::add);
        assertEquals(Collections.singletonList(RIGHT_VALUE), sideEffect);
    }

    @Test
    void shouldNotHandleUnexpectedExceptionWhenCallingOnRightGivenRight() {
        assertSame(
                ERROR,
                assertThrows(ERROR.getClass(), () -> RIGHT.onRight(ignored -> { throw ERROR; }))
        );
    }

    @Test
    void shouldDoNothingWhenCallingOnRightGivenLeft() {
        assertSame(LEFT, LEFT.onRight(x -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenCallingOnRightWithNullParamOnLeft() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> LEFT.onRight(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenCallingOnRightWithNullParamOnRight() {
        assertEquals(
                "action is null",
                assertThrows(NullPointerException.class, () -> RIGHT.onRight(null)).getMessage()
        );
    }

    // -- .orElse(Callable)

    @Test
    void shouldReturnSelfOnOrElseIfRight() {
        assertSame(RIGHT, RIGHT.orElse(() -> null));
    }

    @Test
    void shouldReturnAlternativeOnOrElseIfLeft() {
        assertSame(RIGHT, LEFT.orElse(() -> RIGHT));
    }

    @Test
    void shouldThrowNPEOnOrElseWithNullParameterIfRight() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> RIGHT.orElse(null)).getMessage()

        );
    }

    @Test
    void shouldThrowNPEOnOrElseWithNullParameterIfLeft() {
        assertEquals(
                "supplier is null",
                assertThrows(NullPointerException.class, () -> LEFT.orElse(null)).getMessage()
        );
    }

    // -- .stream()

    @Test
    void shouldStreamLeft() {
        assertEquals(Collections.emptyList(), LEFT.stream().collect(Collectors.toList()));
    }

    @Test
    void shouldStreamRight() {
        assertEquals(Collections.singletonList(RIGHT_VALUE), RIGHT.stream().collect(Collectors.toList()));
    }

    // -- .swap()

    @Test
    void shouldSwapLeft() {
        assertEquals(Either.right(LEFT_VALUE), LEFT.swap());
    }

    @Test
    void shouldSwapRight() {
        assertEquals(Either.left(RIGHT_VALUE), RIGHT.swap());
    }

    // -- .toOption()

    @Test
    void shouldConvertLeftToOption() {
        assertEquals(Option.none(), LEFT.toOption());
    }


    @Test
    void shouldConvertRightOfNonNullToOption() {
        assertEquals(Option.some(RIGHT_VALUE), RIGHT.toOption());
    }

    @Test
    void shouldConvertRightOfNullToOption() {
        assertEquals(Option.some(null), Either.right(null).toOption());
    }

    // -- .toOptional()

    @Test
    void shouldConvertLeftToOptional() {
        assertEquals(Optional.empty(), LEFT.toOptional());
    }


    @Test
    void shouldConvertRightOfNonNullToOptional() {
        assertEquals(Optional.of(RIGHT_VALUE), RIGHT.toOptional());
    }

    @Test
    void shouldConvertRightOfNullToOptional() {
        assertEquals(Optional.empty(), Either.right(null).toOptional());
    }

    // -- .toTry(Function)

    @Test
    void shouldConvertLeftToTry() {
        assertEquals(Try.failure(ERROR), LEFT.toTry(ignored -> ERROR));
    }

    @Test
    void shouldConvertRightToTry() {
        assertEquals(Try.success(RIGHT_VALUE), RIGHT.toTry(ignored -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldThrowNPEWhenConvertingLeftToTryUsingNullThrowableMapper() {
        assertEquals(
                "leftMapper is null",
                assertThrows(NullPointerException.class, () -> LEFT.toTry(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEWhenConvertingRightToTryUsingNullThrowableMapper() {
        assertEquals(
                "leftMapper is null",
                assertThrows(NullPointerException.class, () -> RIGHT.toTry(null)).getMessage()
        );
    }

    // -- .transform(Function, Function)

    @Test
    void shouldTransformLeftWhenValueIsNull() {
        assertSame(RIGHT, Either.<Integer, String> left(null).transform(x -> RIGHT, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldTransformRightWhenValueIsNull() {
        assertSame(RIGHT, Either.<Integer, String> right(null).transform(x -> { throw ASSERTION_ERROR; }, s -> RIGHT));
    }

    @Test
    void shouldTransformLeftToNull() {
        assertNull(LEFT.transform(x -> null, s -> { throw ASSERTION_ERROR; }));
    }

    @Test
    void shouldTransformRightToNull() {
        assertNull(RIGHT.transform(x -> { throw ASSERTION_ERROR; }, s -> null));
    }

    @Test
    void shouldTransformAndReturnValueIfRight() {
        final Either<Integer, Integer> transformed = RIGHT.transform(x -> { throw ASSERTION_ERROR; }, s -> Either.right(s.length()));
        assertEquals(Either.right(RIGHT_VALUE.length()), transformed);
    }

    @Test
    void shouldTransformAndReturnAlternateValueIfLeft() {
        final Either<Integer, String> transformed = LEFT.transform(x -> RIGHT, a -> { throw ASSERTION_ERROR; });
        assertSame(RIGHT, transformed);
    }

    @Test
    void shouldTransformAndThrowNPEOnWhenOnLeftFunctionIsNullIfRight() {
        assertEquals(
                "ifLeft is null",
                assertThrows(NullPointerException.class, () -> RIGHT.transform(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldTransformAndThrowNPEOnWhenOnLeftFunctionIsNullIfLeft() {
        assertEquals(
                "ifLeft is null",
                assertThrows(NullPointerException.class, () -> LEFT.transform(null, s -> { throw ASSERTION_ERROR; })).getMessage()
        );
    }

    @Test
    void shouldTransformAndThrowNPEOnWhenOnRightFunctionIsNullIfRight() {
        assertEquals(
                "ifRight is null",
                assertThrows(NullPointerException.class, () -> RIGHT.transform(x -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    @Test
    void shouldTransformAndThrowNPEOnWhenOnRightFunctionIsNullIfLeft() {
        assertEquals(
                "ifRight is null",
                assertThrows(NullPointerException.class, () -> LEFT.transform(x -> { throw ASSERTION_ERROR; }, null)).getMessage()
        );
    }

    // -- Object.equals(Object)

    @Test
    void shouldEqualLeftIfObjectIsSame() {
        assertEquals(LEFT, LEFT);
    }

    @Test
    void shouldNotEqualLeftIfObjectIsNotSame() {
        assertNotEquals(Either.left(new Error()), Either.left(new Error()));
    }

    @Test
    void shouldEqualRightIfObjectIsSame() {
        assertEquals(RIGHT, RIGHT);
    }

    @Test
    void shouldNotEqualLeftAndRight() {
        assertNotEquals(RIGHT, LEFT);
    }

    @Test
    void shouldNotEqualRightAndLeft() {
        assertNotEquals(LEFT, RIGHT);
    }

    @Test
    void shouldNotEqualRightIfValuesDiffer() {
        assertNotEquals(Either.right("1"), Either.right(1));
    }

    // -- Object.hashCode()

    @Test
    void shouldHashLeft() {
        assertEquals(31 + Objects.hashCode(LEFT_VALUE), LEFT.hashCode());
    }

    @Test
    void shouldHashLeftWithNullValue() {
        assertEquals(31 + Objects.hashCode(null), Either.left(null).hashCode());
    }

    @Test
    void shouldHashRight() {
        assertEquals(31 + Objects.hashCode(RIGHT_VALUE), RIGHT.hashCode());
    }

    @Test
    void shouldHashRightWithNullValue() {
        assertEquals(31 + Objects.hashCode(null), Either.right(null).hashCode());
    }

    // -- Object.toString()

    @Test
    void shouldConvertLeftToString() {
        assertEquals("Left(" + LEFT_VALUE + ")", LEFT.toString());
    }

    @Test
    void shouldConvertLeftWithNullValueToString() {
        assertEquals("Left(null)", Either.left(null).toString());
    }

    @Test
    void shouldConvertRightToString() {
        assertEquals("Right(" + RIGHT_VALUE + ")", RIGHT.toString());
    }

    @Test
    void shouldConvertRightWithNullValueToString() {
        assertEquals("Right(null)", Either.right(null).toString());
    }

    // Serialization

    @Test
    void shouldSerializeLeft() throws IOException, ClassNotFoundException {
        assertEquals(LEFT, deserialize(serialize(LEFT)));
    }

    @Test
    void shouldSerializeRight() throws IOException, ClassNotFoundException {
        assertEquals(RIGHT, deserialize(serialize(RIGHT)));
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
