/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.AbstractIterableTest;
import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Value;
import javaslang.control.Match;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Try;
import org.assertj.core.api.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractValueTest extends AbstractIterableTest {

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

    abstract boolean isThisLazyJavaslangObject();

    // returns the peek result of the specific Traversable implementation
    abstract int getPeekNonNilPerformingAnAction();

    // -- get(Iterable)

    @Test
    public void shouldGetOfValue() {
        assertThat(Value.get(of(1))).isEqualTo(1);
    }

    @Test
    public void shouldGetOfIterable() {
        java.util.List<Integer> iterable = new ArrayList<>();
        iterable.add(11);
        assertThat(Value.get(iterable)).isEqualTo(11);
    }

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
        assertThat(empty().getOption()).isEqualTo(None.instance());
    }

    @Test
    public void shouldGetOptionNonEmpty() {
        assertThat(of(1).getOption()).isEqualTo(Option.of(1));
    }

    // -- ifDefined(trueVal, falseVal)

    @Test
    public void shouldCalculateValIfDefined() {
        assertThat(empty().ifDefined(1, 2)).isEqualTo(2);
        assertThat(of(1).ifDefined(1, 2)).isEqualTo(1);
    }

    // -- ifDefined(Supplier)

    @Test
    public void shouldCalculateSupplierIfDefined() {
        assertThat(empty().ifDefined(() -> 1, () -> 2)).isEqualTo(2);
        assertThat(of(1).ifDefined(() -> 1, () -> 2)).isEqualTo(1);
    }

    // -- ifEmpty(trueVal, falseVal)

    @Test
    public void shouldCalculateValIfEmpty() {
        assertThat(empty().ifEmpty(1, 2)).isEqualTo(1);
        assertThat(of(1).ifEmpty(1, 2)).isEqualTo(2);
    }

    // -- ifEmpty(Supplier)

    @Test
    public void shouldCalculateSupplierIfEmpty() {
        assertThat(empty().ifEmpty(() -> 1, () -> 2)).isEqualTo(1);
        assertThat(of(1).ifEmpty(() -> 1, () -> 2)).isEqualTo(2);
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

    // -- orElse

    @Test
    public void shouldCalculateOrElse() {
        assertThat(empty().orElse(1)).isEqualTo(1);
        assertThat(of(1).orElse(2)).isEqualTo(1);
    }

    // -- orElseGet

    @Test
    public void shouldCalculateOrElseGet() {
        assertThat(empty().orElseGet(() -> 1)).isEqualTo(1);
        assertThat(of(1).orElseGet(() -> 2)).isEqualTo(1);
    }

    // -- orElseThrow

    @Test(expected = ArithmeticException.class)
    public void shouldThrowIfEmpty() {
        empty().orElseThrow(ArithmeticException::new);
    }

    @Test
    public void shouldNotThrowIfNonEmpty() {
        assertThat(of(1).orElseThrow(ArithmeticException::new)).isEqualTo(1);
    }

    // -- filter

    @Test
    public void shouldFilterEmptyTraversable() {
        assertThat(empty().filter(ignored -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldFilterNonEmptyTraversable() {
        assertThat(of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(of(2, 4));
    }

    @Test
    public void shouldFilterNonEmptyTraversableAllMatch() {
        if(isThisLazyJavaslangObject()) {
            final Value<Integer> v1 = of(1, 2, 3, 4);
            final Value<Integer> v2 = of(1, 2, 3, 4);
            assertThat(v1.filter(i -> true)).isEqualTo(v2);
        } else {
            final Value<Integer> v = of(1, 2, 3, 4);
            assertThat(v.filter(i -> true)).isSameAs(v);
        }
    }

    // -- flatten()

    @Test
    public void shouldFlattenEmptyTraversable() {
        assertThat(empty().flatten()).isEqualTo(empty());
    }

    @Test
    public void shouldFlattenTraversableOfPlainElements() {
        assertThat(of(1, 2, 3).flatten()).isEqualTo(of(1, 2, 3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlattenTraversableOfTraversables() {
        assertThat(of(of(1), of(2, 3)).flatten()).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldFlattenTraversableOfTraversablesAndPlainElements() {
        assertThat(of(1, of(of(2, 3), 4), 5).flatten()).isEqualTo(of(1, 2, 3, 4, 5));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlattenDifferentElementTypes() {
        assertThat(of(1, "2", of(3.1415, 1L)).flatten()).isEqualTo(of(1, "2", 3.1415, 1L));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptyTraversable() {
        assertThat(empty().flatMap(this::of)).isEqualTo(empty());
    }

    @Test
    public void shouldFlatMapNonEmptyTraversable() {
        assertThat(of(1, 2, 3).flatMap(this::of)).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldFlatMapTraversableByExpandingElements() {
        assertThat(of(1, 2, 3).flatMap(i -> {
            if (i == 1) {
                return of(1, 2, 3);
            } else if (i == 2) {
                return of(4, 5);
            } else {
                return of(6);
            }
        })).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldFlatMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Value<Integer> actualInts = of(0, 1, 2).flatMap(ignored -> of(seq.getAndIncrement(), seq.getAndIncrement()));
        final Value<Integer> expectedInts = of(0, 1, 2, 3, 4, 5);
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(this.<Integer> empty().map(i -> i + 1)).isEqualTo(empty());
    }

    @Test
    public void shouldMapNonNil() {
        assertThat(of(1, 2, 3).map(i -> i + 1)).isEqualTo(of(2, 3, 4));
    }

    @Test
    public void shouldMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Value<Integer> expectedInts = of(0, 1, 2, 3, 4);
        final Value<Integer> actualInts = of(0, 1, 2, 3, 4).map(ignored -> seq.getAndIncrement());
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- peek

    @Test
    public void shouldPeekNil() {
        assertThat(empty().peek(t -> {
        })).isEqualTo(empty());
    }

    @Test
    public void shouldPeekNonNilPerformingNoAction() {
        assertThat(of(1).peek(t -> {
        })).isEqualTo(of(1));
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

    // -- toXXXXX

    @Test
    public void shouldConvertToCharSeq() {
        Value<Character> v = of('a', 'b', 'c');
        assertThat(Match.of(v)
                .whenTypeIn(Iterator.class).then(Iterator.of("ignore").toString())
                .orElse(v.toString())
        ).isEqualTo(v.toCharSeq().toString());
    }

    @Test
    public void shouldConvertToLazy() {
        assertThat(empty().toLazy().isEmpty()).isTrue();
        assertThat(of(1).toLazy()).isEqualTo(Lazy.of(() -> 1));
    }

    @Test
    public void shouldConvertToArray() {
        assertThat(of(1, 2, 3).toArray()).isEqualTo(Array.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToList() {
        assertThat(of(1, 2, 3).toList()).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToMap() {
        assertThat(of(1, 2, 3).toMap(v -> Tuple.of(v, v))).isEqualTo(HashMap.empty().put(1, 1).put(2, 2).put(3, 3));
    }

    @Test
    public void shouldConvertToOption() {
        assertThat(empty().toOption()).isSameAs(None.instance());
        assertThat(of(1).toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertToQueue() {
        assertThat(of(1, 2, 3).toQueue()).isEqualTo(Queue.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToSet() {
        assertThat(of(1, 2, 3).toSet()).isEqualTo(HashSet.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToStack() {
        assertThat(of(1, 2, 3).toStack()).isEqualTo(Stack.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToStream() {
        assertThat(of(1, 2, 3).toStream()).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToTry() {
        assertThat(of(1, 2, 3).toTry()).isEqualTo(Try.of(() -> 1));
    }

    @Test
    public void shouldConvertToVector() {
        assertThat(of(1, 2, 3).toVector()).isEqualTo(Vector.of(1, 2, 3));
    }

    @Test
    public void shouldConvertToJavaArray() {
        assertThat(of(1, 2, 3).toJavaArray(Integer.class)).isEqualTo(new int[] {1, 2, 3});
    }

    @Test
    public void shouldConvertToJavaList() {
        java.util.List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertThat(of(1, 2, 3).toJavaList()).isEqualTo(list);
    }

    @Test
    public void shouldConvertToJavaMap() {
        java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        assertThat(of(1, 2, 3).toJavaMap(v -> Tuple.of(v, v))).isEqualTo(map);
    }

    @Test
    public void shouldConvertToJavaOptional() {
        assertThat(of(1, 2, 3).toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertToJavaSet() {
        java.util.Set<Integer> set = new java.util.HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        assertThat(of(1, 2, 3).toJavaSet()).isEqualTo(set);
    }

    @Test
    public void shouldConvertToJavaStream() {
        java.util.stream.Stream<Integer> s1 = of(1, 2, 3).toJavaStream();
        java.util.stream.Stream<Integer> s2 = java.util.stream.Stream.of(1, 2, 3);
        assertThat(List.ofAll(s1::iterator)).isEqualTo(List.ofAll(s2::iterator));
    }

    // TODO move to TraversableTest when Iterator will be OK
    // -- exists

    @Test
    public void shouldBeAwareOfExistingElement() {
        assertThat(of(1, 2).exists(i -> i == 2)).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingElement() {
        assertThat(this.<Integer> empty().exists(i -> i == 1)).isFalse();
    }

    // TODO move to TraversableTest when Iterator will be OK
    // -- forAll

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAll() {
        assertThat(of(2, 4).forAll(i -> i % 2 == 0)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        assertThat(of(2, 3).forAll(i -> i % 2 == 0)).isFalse();
    }
}
