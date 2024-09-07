/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Spliterator;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static io.vavr.TestComparators.toStringComparator;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    @Disabled("SortedSet in test always created with working comparator, and because method toSortedSet() return same object will never throw ClassCastException")
    public void shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable() {
        assertThrows(ClassCastException.class, () -> super.shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable());
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

}
