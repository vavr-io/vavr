/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;

import static javaslang.TestComparators.toStringComparator;

public abstract class AbstractSortedSetTest extends AbstractSetTest {

    @Override
    abstract protected <T> SortedSet<T> of(T element);

    abstract protected <T> SortedSet<T> of(Comparator<? super T> comparator, T element);

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

    // -- toSortedSet

    @Override
    @Test(expected = ClassCastException.class)
    @Ignore("SortedSet in test always created with working comparator, and because method toSortedSet() return same object will never throw ClassCastException")
    public void shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable() {
        super.shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable();
    }
}
