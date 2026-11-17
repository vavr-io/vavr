/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Value;
import io.vavr.control.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LinkedHashSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, LinkedHashSet<T>> collector() {
        return LinkedHashSet.collector();
    }

    @Override
    protected <T> LinkedHashSet<T> empty() {
        return LinkedHashSet.empty();
    }

    @Override
    protected <T> LinkedHashSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> LinkedHashSet<T> of(T element) {
        return LinkedHashSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> LinkedHashSet<T> of(T... elements) {
        return LinkedHashSet.of(elements);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> LinkedHashSet<T> ofAll(Iterable<? extends T> elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> LinkedHashSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return LinkedHashSet.ofAll(javaStream);
    }

    @Override
    protected LinkedHashSet<Boolean> ofAll(boolean... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Byte> ofAll(byte... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Character> ofAll(char... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Double> ofAll(double... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Float> ofAll(float... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Integer> ofAll(int... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Long> ofAll(long... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Short> ofAll(short... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected <T> LinkedHashSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return LinkedHashSet.tabulate(n, f);
    }

    @Override
    protected <T> LinkedHashSet<T> fill(int n, Supplier<? extends T> s) {
        return LinkedHashSet.fill(n, s);
    }

    @Override
    protected LinkedHashSet<Character> range(char from, char toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Integer> range(int from, int toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Long> range(long from, long toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Test
    public void shouldKeepOrder() {
        final List<Integer> actual = LinkedHashSet.<Integer> empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.of(3, 2, 1));
    }

    @Nested
    class StaticNarrowTests {
        @Test
        public void shouldNarrowLinkedHashSet() {
            final LinkedHashSet<Double> doubles = of(1.0d);
            final LinkedHashSet<Number> numbers = LinkedHashSet.narrow(doubles);
            final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
            assertThat(actual).isEqualTo(3);
        }
    }

    @Nested
    class ReplaceTests {
        @Test
        public void shouldReturnSameInstanceIfReplacingNonExistingElement() {
            final Set<Integer> set = LinkedHashSet.of(1, 2, 3);
            final Set<Integer> actual = set.replace(4, 0);
            assertThat(actual).isSameAs(set);
        }

        @Test
        public void shouldPreserveOrderWhenReplacingExistingElement() {
            final Set<Integer> set = LinkedHashSet.of(1, 2, 3);
            final Set<Integer> actual = set.replace(2, 0);
            final Set<Integer> expected = LinkedHashSet.of(1, 0, 3);
            assertThat(actual).isEqualTo(expected);
            Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
        }

        @Test
        public void shouldPreserveOrderWhenReplacingExistingElementAndRemoveOtherIfElementAlreadyExists() {
            final Set<Integer> set = LinkedHashSet.of(1, 2, 3, 4, 5);
            final Set<Integer> actual = set.replace(2, 4);
            final Set<Integer> expected = LinkedHashSet.of(1, 4, 3, 5);
            assertThat(actual).isEqualTo(expected);
            Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
        }

        @Test
        public void shouldReturnSameInstanceWhenReplacingExistingElementWithIdentity() {
            final Set<Integer> set = LinkedHashSet.of(1, 2, 3);
            final Set<Integer> actual = set.replace(2, 2);
            assertThat(actual).isSameAs(set);
        }
    }

    @Nested
    class TransformTests {
        @Test
        public void shouldTransform() {
            final String transformed = of(42).transform(v -> String.valueOf(v.get()));
            assertThat(transformed).isEqualTo("42");
        }
    }

    @Nested
    class TolinkedsetTests {
        @Test
        public void shouldReturnSelfOnConvertToLinkedSet() {
            final Value<Integer> value = of(1, 2, 3);
            assertThat(value.toLinkedSet()).isSameAs(value);
        }
    }

    @Nested
    class LinkedHashSetSpliteratorTests {
        @Test
        public void shouldNotHaveSortedSpliterator() {
            assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SORTED)).isFalse();
        }

        @Test
        public void shouldHaveOrderedSpliterator() {
            assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
        }
    }

    @Nested
    class IssequentialTests {
        @Test
        public void shouldReturnTrueWhenIsSequentialCalled() {
            assertThat(of(1, 2, 3).isSequential()).isTrue();
        }
    }

    @Nested
    class NeighborNavigationTests {
        @Test
        public void shouldSkipConsecutiveRemovedElementsWhenNavigating() {
            final LinkedHashSet<Integer> set =
                LinkedHashSet.of(1, 2, 3, 4, 5)
                    .remove(2)
                    .remove(3);

            assertThat(set.next(1)).isEqualTo(Option.some(4));
            assertThat(set.previous(4)).isEqualTo(Option.some(1));
        }

        @Test
        public void shouldReturnNextAndPreviousElementsInInsertionOrder() {
            final LinkedHashSet<Integer> set = LinkedHashSet.of(3, 1, 2);

            assertThat(set.next(3)).isEqualTo(Option.some(1));
            assertThat(set.next(1)).isEqualTo(Option.some(2));
            assertThat(set.previous(2)).isEqualTo(Option.some(1));
            assertThat(set.previous(1)).isEqualTo(Option.some(3));
        }

        @Test
        public void shouldReturnNoneForMissingElementsAndBoundaryElements() {
            final LinkedHashSet<Integer> set = LinkedHashSet.of(1, 2, 3);

            assertThat(set.next(3)).isEqualTo(Option.none());
            assertThat(set.previous(1)).isEqualTo(Option.none());
            assertThat(set.next(99)).isEqualTo(Option.none());
            assertThat(set.previous(99)).isEqualTo(Option.none());
        }

        @Test
        public void shouldSkipRemovedElementsWhenNavigating() {
            final LinkedHashSet<Integer> original = LinkedHashSet.of(1, 2, 3, 4);
            final LinkedHashSet<Integer> modified = original.remove(2);

            assertThat(modified.next(1)).isEqualTo(Option.some(3));
            assertThat(modified.previous(3)).isEqualTo(Option.some(1));
            assertThat(original.next(1)).isEqualTo(Option.some(2));
        }

        @Test
        public void shouldNavigateCorrectlyAfterRemovingFirstElement() {
            final LinkedHashSet<Integer> set = LinkedHashSet.of(1, 2, 3).remove(1);

            assertThat(set.next(2)).isEqualTo(Option.some(3));
            assertThat(set.previous(3)).isEqualTo(Option.some(2));
        }

        @Test
        public void shouldNavigateCorrectlyAfterRemovingLastElement() {
            final LinkedHashSet<Integer> set = LinkedHashSet.of(1, 2, 3).remove(3);

            assertThat(set.next(1)).isEqualTo(Option.some(2));
            assertThat(set.previous(2)).isEqualTo(Option.some(1));
            assertThat(set.next(2)).isEqualTo(Option.none());
        }

    @Test
    public void shouldNavigateCorrectlyAfterReindexing() {
        final LinkedHashSet<Integer> set =
            LinkedHashSet.of(1, 2, 3, 4, 5, 6, 7)
                .remove(2)
                .remove(4)
                .remove(5)
                .remove(6);

        assertThat(set.next(1)).isEqualTo(Option.some(3));
        assertThat(set.next(3)).isEqualTo(Option.some(7));
        assertThat(set.previous(7)).isEqualTo(Option.some(3));
        assertThat(set.previous(3)).isEqualTo(Option.some(1));
    }
    }

}
