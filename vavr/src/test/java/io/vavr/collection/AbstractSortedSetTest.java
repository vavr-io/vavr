/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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
package io.vavr.collection;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static io.vavr.TestComparators.toStringComparator;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractSortedSetTest extends AbstractSetTest {

    @Override
    abstract protected <T> SortedSet<T> empty();

    @Override
    abstract protected <T> SortedSet<T> emptyWithNull();

    @Override
    abstract protected <T> SortedSet<T> of(T element);

    abstract protected <T> SortedSet<T> of(Comparator<? super T> comparator, T element);

    @SuppressWarnings("unchecked")
    abstract protected <T> SortedSet<T> of(Comparator<? super T> comparator, T... elements);

    // -- static narrow

    @Test
    public void shouldNarrowSortedSet() {
        final SortedSet<Double> doubles = of(toStringComparator(), 1.0d);
        final SortedSet<Number> numbers = SortedSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    @Test
    public void shouldReturnComparator() {
        assertThat(of(1).comparator()).isNotNull();
    }

    @Override
    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // not possible, because the empty instance stores information about the underlying comparator
    }

    @Override
    @Test
    public void shouldScanWithNonComparable() {
        // makes no sense because sorted sets contain ordered elements
    }

    @Override
    @Test
    public void shouldNarrowSet() {
        // makes no sense because disjoint types share not the same ordering
    }

    @Override
    @Test
    public void shouldNarrowTraversable() {
        // makes no sense because disjoint types share not the same ordering
    }

    // -- head

    @Test
    public void shouldReturnHeadOfNonEmptyHavingNaturalOrder() {
        assertThat(of(naturalOrder(), 1, 2, 3, 4).head()).isEqualTo(1);
    }


    @Test
    public void shouldReturnHeadOfNonEmptyHavingReversedOrder() {
        assertThat(of(reverseOrder(), 1, 2, 3, 4).head()).isEqualTo(4);
    }

    // -- init

    @Test
    public void shouldReturnInitOfNonEmptyHavingNaturalOrder() {
        assertThat(of(naturalOrder(), 1, 2, 3, 4).init()).isEqualTo(of(naturalOrder(), 1, 2, 3));
    }
    
    @Test
    public void shouldReturnInitOfNonEmptyHavingReversedOrder() {
        assertThat(of(reverseOrder(), 1, 2, 3, 4).init()).isEqualTo(of(naturalOrder(), 2, 3, 4));
    }

    // -- last

    @Test
    public void shouldReturnLastOfNonEmptyHavingNaturalOrder() {
        assertThat(of(naturalOrder(), 1, 2, 3, 4).last()).isEqualTo(4);
    }
    
    @Test
    public void shouldReturnLastOfNonEmptyHavingReversedOrder() {
        assertThat(of(reverseOrder(), 1, 2, 3, 4).last()).isEqualTo(1);
    }

    // -- tail

    @Test
    public void shouldReturnTailOfNonEmptyHavingNaturalOrder() {
        assertThat(of(naturalOrder(), 1, 2, 3, 4).tail()).isEqualTo(of(naturalOrder(), 2, 3, 4));
    }


    @Test
    public void shouldReturnTailOfNonEmptyHavingReversedOrder() {
        assertThat(of(reverseOrder(), 1, 2, 3, 4).tail()).isEqualTo(of(naturalOrder(), 1, 2, 3));
    }

    // -- equals

    @Test
    public void shouldBeEqualWhenHavingSameElementsAndDifferentOrder() {
        final SortedSet<Integer> set1 = of(naturalOrder(), 1, 2, 3);
        final SortedSet<Integer> set2 = of(reverseOrder(), 3, 2, 1);
        assertThat(set1).isEqualTo(set2);
    }

    // -- toSortedSet

    @Override
    @Test(expected = ClassCastException.class)
    @Ignore("SortedSet in test always created with working comparator, and because method toSortedSet() return same object will never throw ClassCastException")
    public void shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable() {
        super.shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable();
    }

    // -- spliterator

    @Test
    public void shouldHaveSortedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SORTED)).isTrue();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    // -- isSequential()

    @Test
    public void shouldReturnFalseWhenIsSequentialCalled() {
        assertThat(of(1, 2, 3).isSequential()).isFalse();
    }

    // -- filter

    @Test
    @Override
    public void shouldFilterNonExistingElements() {
        assertThat(this.<Integer> empty().filter(i -> i == 0)).isEqualTo(empty());
        assertThat(of(1, 2, 3).filter(i -> i == 0)).isEqualTo(empty());
    }

    // -- flatMap

    @Test
    @Override
    public void shouldFlatMapEmpty() {
        assertThat(empty().flatMap(v -> of(v, 0))).isEqualTo(empty());
    }

    // -- reject

    @Test
    @Override
    public void shouldRejectNonExistingElements() {
        assertThat(this.<Integer> empty().reject(i -> i == 0)).isEqualTo(empty());
        assertThat(of(1, 2, 3).reject(i -> i > 0)).isEqualTo(empty());
    }

    // -- replace(curr, new)

    @Test
    @Override
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replace(1, 2)).isEqualTo(empty());
    }

    // -- replaceAll(curr, new)

    @Test
    @Override
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(this.<Integer> empty().replaceAll(1, 2)).isEqualTo(empty());
    }

    // -- retainAll

    @Test
    @Override
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        final Traversable<Integer> src = of(1, 2, 3);
        final Traversable<Object> expected = empty();
        final Traversable<Integer> actual = src.retainAll(of(4, 5));
        assertThat(actual).isEqualTo(expected);
    }

    // -- take

    @Test
    @Override
    public void shouldTakeNoneOnNil() {
        assertThat(empty().take(1)).isEqualTo(empty());
    }

    @Test
    @Override
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).take(-1)).isEqualTo(empty());
    }

    // -- takeRight

    @Test
    @Override
    public void shouldTakeRightNoneIfCountIsNegative() {
        assertThat(of(1, 2, 3).takeRight(-1)).isEqualTo(empty());
    }

    @Test
    @Override
    public void shouldTakeRightNoneOnNil() {
        assertThat(empty().takeRight(1)).isEqualTo(empty());
    }

    // -- takeUntil

    @Test
    @Override
    public void shouldTakeUntilAllOnTrueCondition() {
        assertThat(of(1, 2, 3).takeUntil(x -> true)).isEqualTo(empty());
    }

    @Test
    @Override
    public void shouldTakeUntilNoneOnNil() {
        assertThat(empty().takeUntil(x -> true)).isEqualTo(empty());
    }

    // -- takeWhile

    @Test
    @Override
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(of(1, 2, 3).takeWhile(x -> false)).isEqualTo(empty());
    }

    @Test
    @Override
    public void shouldTakeWhileNoneOnNil() {
        assertThat(empty().takeWhile(x -> true)).isEqualTo(empty());
    }

}
