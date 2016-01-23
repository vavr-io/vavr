/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.Either;
import javaslang.control.Match;
import javaslang.control.Option;
import javaslang.control.Try;
import org.assertj.core.api.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractValueTest {

    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
        };
    }

    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {
        };
    }

    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {
        };
    }

    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {
        };
    }

    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {
        };
    }

    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {
        };
    }

    protected StringAssert assertThat(String actual) {
        return new StringAssert(actual) {
        };
    }

    abstract protected <T> Value<T> empty();

    abstract protected <T> Value<T> of(T element);

    @SuppressWarnings("unchecked")
    abstract protected <T> Value<T> of(T... elements);

    abstract protected boolean useIsEqualToInsteadOfIsSameAs();

    // returns the peek result of the specific Traversable implementation
    abstract protected int getPeekNonNilPerformingAnAction();

// TODO: filter went from Value into the subclasses
//    // -- filter
//
//    @Test
//    public void shouldFilterEmptyTraversable() {
//        final Value<Integer> value = empty();
//        if (value.isSingleValued()) {
//            // TODO
//        } else {
//            assertThat(value.filter(ignored -> true)).isEqualTo(empty());
//        }
//    }
//
//    @Test
//    public void shouldFilterNonEmptyTraversable() {
//        final Value<Integer> value = of(1, 2, 3, 4);
//        if (value.isSingleValued()) {
//            // TODO
//        } else {
//            assertThat(value.filter(i -> i % 2 == 0)).isEqualTo(of(2, 4));
//        }
//    }
//
//    @Test
//    public void shouldFilterNonEmptyTraversableAllMatch() {
//        final Value<Integer> v = of(1, 2, 3, 4);
//        if (v.isSingleValued()) {
//            // TODO
//        } else {
//            if (useIsEqualToInsteadOfIsSameAs()) {
//                final Value<Integer> v2 = of(1, 2, 3, 4);
//                assertThat(v.filter(i -> true)).isEqualTo(v2);
//            } else {
//                assertThat(v.filter(i -> true)).isSameAs(v);
//            }
//        }
//    }
//
    // -- get()

    @Test(expected = NoSuchElementException.class)
    public void shouldGetEmpty() {
        assertThat(empty().get()).isEqualTo(1);
    }

    @Test
    public void shouldGetNonEmpty() {
        assertThat(of(1).get()).isEqualTo(1);
    }

    // -- getOption()

    @Test
    public void shouldGetOptionEmpty() {
        assertThat(empty().getOption()).isEqualTo(Option.none());
    }

    @Test
    public void shouldGetOptionNonEmpty() {
        assertThat(of(1).getOption()).isEqualTo(Option.of(1));
    }

    // -- getOrElse(T)

    @Test
    public void shouldCalculateGetOrElse() {
        assertThat(empty().getOrElse(1)).isEqualTo(1);
        assertThat(of(1).getOrElse(2)).isEqualTo(1);
    }

    // -- getOrElse(Supplier)

    @Test
    public void shouldCalculateGetOrElseSupplier() {
        assertThat(empty().getOrElse(() -> 1)).isEqualTo(1);
        assertThat(of(1).getOrElse(() -> 2)).isEqualTo(1);
    }

    // -- getOrElseThrow

    @Test(expected = ArithmeticException.class)
    public void shouldThrowOnGetOrElseThrowIfEmpty() {
        empty().getOrElseThrow(ArithmeticException::new);
    }

    @Test
    public void shouldNotThrowOnGetOrElseThrowIfNonEmpty() {
        assertThat(of(1).getOrElseThrow(ArithmeticException::new)).isEqualTo(1);
    }

    // -- getOrElseTry

    @Test
    public void shouldReturnUnderlyingValueWhenCallingGetOrElseTryOnNonEmptyValue() {
        assertThat(of(1).getOrElseTry(() -> 2)).isEqualTo(1);
    }

    @Test
    public void shouldReturnAlternateValueWhenCallingGetOrElseTryOnEmptyValue() {
        assertThat(empty().getOrElseTry(() -> 2)).isEqualTo(2);
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldThrowWhenCallingGetOrElseTryOnEmptyValueAndTryIsAFailure() {
        empty().getOrElseTry(() -> {
            throw new Error();
        });
    }

    // -- isEmpty

    @Test
    public void shouldCalculateIsEmpty() {
        assertThat(empty().isEmpty()).isTrue();
        assertThat(of(1).isEmpty()).isFalse();
    }

    // -- isDefined

    @Test
    public void shouldCalculateIsDefined() {
        assertThat(empty().isDefined()).isFalse();
        assertThat(of(1).isDefined()).isTrue();
    }

    // -- peek

    @Test
    public void shouldPeekNil() {
        assertThat(empty().peek(t -> {})).isEqualTo(empty());
    }

    @Test
    public void shouldPeekNonNilPerformingNoAction() {
        assertThat(of(1).peek(t -> {})).isEqualTo(of(1));
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final int[] effect = { 0 };
        final Value<Integer> actual = of(1).peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of(1));
        assertThat(effect[0]).isEqualTo(1);
    }

    @Test
    public void shouldPeekNonNilPerformingAnAction() {
        final int[] effect = { 0 };
        final Value<Integer> actual = of(1, 2, 3).peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of(1, 2, 3)); // traverses all elements in the lazy case
        assertThat(effect[0]).isEqualTo(getPeekNonNilPerformingAnAction());
    }

    // -- Conversions match(), toXxx()

    @Test
    public void shouldConvertNonEmptyValueToMatchValue() {
        final Value<Integer> value = of(1);
        final String actual = value.match()
                .when((Value<Integer> v) -> v.getOrElse(-1) == 1).then("ok")
                .getOrElse("nok");
        assertThat(actual).isEqualTo("ok");
    }

    @Test
    public void shouldConvertEmptyValueToMatchValue() {
        final Value<Integer> value = empty();
        final String actual = value.match()
                .when(Value<Integer>::isEmpty).then("ok")
                .getOrElse("nok");
        assertThat(actual).isEqualTo("ok");
    }

    @Test
    public void shouldConvertToCharSeq() {
        Value<Character> v = of('a', 'b', 'c');
        assertThat(Match.of(v)
                .whenTypeIn(Iterator.class).then(Iterator.of("ignore").toString())
                .getOrElse(v.toString())
        ).isEqualTo(v.toCharSeq().toString());
    }

    @Test
    public void shouldConvertToArray() {
        final Value<Integer> value = of(1, 2, 3);
        final Array<Integer> array = value.toArray();
        if (value.isSingleValued()) {
            assertThat(array).isEqualTo(Array.of(1));
        } else {
            assertThat(array).isEqualTo(Array.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToList() {
        final Value<Integer> value = of(1, 2, 3);
        final List<Integer> list = value.toList();
        if (value.isSingleValued()) {
            assertThat(list).isEqualTo(List.of(1));
        } else {
            assertThat(list).isEqualTo(List.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToMap() {
        final Value<Integer> value = of(1, 2, 3);
        final Map<Integer, Integer> map = value.toMap(v -> Tuple.of(v, v));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(HashMap.of(1, 1));
        } else {
            assertThat(map).isEqualTo(HashMap.empty().put(1, 1).put(2, 2).put(3, 3));
        }
    }

    @Test
    public void shouldConvertToOption() {
        assertThat(empty().toOption()).isSameAs(Option.none());
        assertThat(of(1).toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertToQueue() {
        final Value<Integer> value = of(1, 2, 3);
        final Queue<Integer> queue = value.toQueue();
        if (value.isSingleValued()) {
            assertThat(queue).isEqualTo(Queue.of(1));
        } else {
            assertThat(queue).isEqualTo(Queue.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToSet() {
        final Value<Integer> value = of(1, 2, 3);
        final Set<Integer> set = value.toSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(HashSet.of(1));
        } else {
            assertThat(set).isEqualTo(HashSet.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToStack() {
        final Value<Integer> value = of(1, 2, 3);
        final Stack<Integer> stack = value.toStack();
        if (value.isSingleValued()) {
            assertThat(stack).isEqualTo(Stack.of(1));
        } else {
            assertThat(stack).isEqualTo(Stack.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToStream() {
        final Value<Integer> value = of(1, 2, 3);
        final Stream<Integer> stream = value.toStream();
        if (value.isSingleValued()) {
            assertThat(stream).isEqualTo(Stream.of(1));
        } else {
            assertThat(stream).isEqualTo(Stream.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertNonEmptyToTry() {
        assertThat(of(1, 2, 3).toTry()).isEqualTo(Try.of(() -> 1));
    }

    @Test
    public void shouldConvertEmptyToTry() {
        final Try<?> actual = empty().toTry();
        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getCause().getClass()).isEqualTo(NoSuchElementException.class);
    }

    @Test
    public void shouldConvertNonEmptyToTryUsingExceptionSupplier() {
        final Exception x = new Exception("test");
        assertThat(of(1, 2, 3).toTry(() -> x)).isEqualTo(Try.of(() -> 1));
    }

    @Test
    public void shouldConvertEmptyToTryUsingExceptionSupplier() {
        final Exception x = new Exception("test");
        assertThat(empty().toTry(() -> x)).isEqualTo(Try.failure(x));
    }

    @Test
    public void shouldConvertToVector() {
        final Value<Integer> value = of(1, 2, 3);
        final Vector<Integer> vector = value.toVector();
        if (value.isSingleValued()) {
            assertThat(vector).isEqualTo(Vector.of(1));
        } else {
            assertThat(vector).isEqualTo(Vector.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaArray() {
        final Value<Integer> value = of(1, 2, 3);
        final Integer[] ints = value.toJavaArray(Integer.class);
        if (value.isSingleValued()) {
            assertThat(ints).isEqualTo(new int[] { 1 });
        } else {
            assertThat(ints).isEqualTo(new int[] { 1, 2, 3 });
        }
    }

    @Test
    public void shouldConvertToJavaList() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.List<Integer> list = value.toJavaList();
        if (value.isSingleValued()) {
            assertThat(list).isEqualTo(Arrays.asList(1));
        } else {
            assertThat(list).isEqualTo(Arrays.asList(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaMap() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Map<Integer, Integer> map = value.toJavaMap(v -> Tuple.of(v, v));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1));
        } else {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1, 2, 2, 3, 3));
        }
    }

    @Test
    public void shouldConvertToJavaOptional() {
        assertThat(of(1, 2, 3).toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertToJavaSet() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Set<Integer> set = value.toJavaSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1));
        } else {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaStream() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.stream.Stream<Integer> s1 = value.toJavaStream();
        if (value.isSingleValued()) {
            final java.util.stream.Stream<Integer> s2 = java.util.stream.Stream.of(1);
            assertThat(List.ofAll(s1::iterator)).isEqualTo(List.ofAll(s2::iterator));
        } else {
            final java.util.stream.Stream<Integer> s2 = java.util.stream.Stream.of(1, 2, 3);
            assertThat(List.ofAll(s1::iterator)).isEqualTo(List.ofAll(s2::iterator));
        }
    }

    @Test
    public void shouldConvertToEitherLeftFromValueSupplier() {
        Either<Integer, String> either = of(0).toLeft(() -> "fallback");
        assertThat(either.isLeft()).isTrue();
        assertThat(either.getLeft()).isEqualTo(0);

        Either<Object, String> either2 = empty().toLeft(() -> "fallback");
        assertThat(either2.isRight()).isTrue();
        assertThat(either2.get()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherLeftFromValue() {
        Either<Integer, String> either = of(0).toLeft("fallback");
        assertThat(either.isLeft()).isTrue();
        assertThat(either.getLeft()).isEqualTo(0);

        Either<Object, String> either2 = empty().toLeft("fallback");
        assertThat(either2.isRight()).isTrue();
        assertThat(either2.get()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherRightFromValueSupplier() {
        Either<String, Integer> either = of(0).toRight(() -> "fallback");
        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(0);

        Either<String, Object> either2 = empty().toRight(() -> "fallback");
        assertThat(either2.isLeft()).isTrue();
        assertThat(either2.getLeft()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherRightFromValue() {
        Either<String, Integer> either = of(0).toRight("fallback");
        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(0);

        Either<String, Object> either2 = empty().toRight("fallback");
        assertThat(either2.isLeft()).isTrue();
        assertThat(either2.getLeft()).isEqualTo("fallback");
    }

    // -- exists

    @Test
    public void shouldBeAwareOfExistingElement() {
        final Value<Integer> value = of(1, 2);
        if (value.isSingleValued()) {
            assertThat(value.exists(i -> i == 1)).isTrue();
        } else {
            assertThat(value.exists(i -> i == 2)).isTrue();
        }

    }

    @Test
    public void shouldBeAwareOfNonExistingElement() {
        assertThat(this.<Integer> empty().exists(i -> i == 1)).isFalse();
    }

    // -- forAll

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAll() {
        assertThat(of(2, 4).forAll(i -> i % 2 == 0)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        assertThat(of(1, 2).forAll(i -> i % 2 == 0)).isFalse();
    }

    // ### ValueModule.Iterable ###

    // -- corresponds

    @Test
    public void shouldntCorrespondsNilNil() {
        assertThat(empty().corresponds(empty(), (o1, o2) -> true)).isTrue();
    }

    @Test
    public void shouldntCorrespondsNilNonNil() {
        assertThat(empty().corresponds(of(1), (o1, i2) -> true)).isFalse();
    }

    @Test
    public void shouldntCorrespondsNonNilNil() {
        assertThat(of(1).corresponds(empty(), (i1, o2) -> true)).isFalse();
    }

    @Test
    public void shouldntCorrespondsDifferentLengths() {
        if (!empty().isSingleValued()) {
            assertThat(of(1, 2, 3).corresponds(of(1, 2), (i1, i2) -> true)).isFalse();
            assertThat(of(1, 2).corresponds(of(1, 2, 3), (i1, i2) -> true)).isFalse();
        }
    }

    @Test
    public void shouldCorresponds() {
        assertThat(of(1, 2, 3).corresponds(of(3, 4, 5), (i1, i2) -> i1 == i2 - 2)).isTrue();
        assertThat(of(1, 2, 3).corresponds(of(1, 2, 3), (i1, i2) -> i1 == i2 + 1)).isFalse();
    }

}
