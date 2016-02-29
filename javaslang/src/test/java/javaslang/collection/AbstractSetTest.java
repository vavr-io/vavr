/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;

public abstract class AbstractSetTest extends AbstractTraversableRangeTest {

    @Override
    abstract protected <T> Set<T> empty();

    @Override
    abstract protected <T> Set<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Set<T> of(T... elements);

    // -- static narrow

    @Test
    public void shouldNarrowSet() {
        final Set<Double> doubles = of(1.0d);
        final Set<Number> numbers = Set.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    @Test
    public void shouldAddAllOfIterable() {
        assertThat(of(1, 2, 3).addAll(empty())).isEqualTo(of(1, 2, 3));
        assertThat(empty().addAll(of(2, 3, 4))).isEqualTo(of(2, 3, 4));
        assertThat(of(1, 2, 3).addAll(of(2, 3, 4))).isEqualTo(of(1, 2, 3, 4));
    }

    @Test
    public void shouldCalculateDifference() {
        assertThat(of(1, 2, 3).diff(of(2))).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).diff(of(5))).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).diff(of(1, 2, 3))).isEqualTo(empty());
        assertThat(empty().diff(of(1, 2))).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateIntersect() {
        assertThat(of(1, 2, 3).intersect(of(2))).isEqualTo(of(2));
        assertThat(of(1, 2, 3).intersect(of(5))).isEqualTo(empty());
        assertThat(of(1, 2, 3).intersect(of(1, 2, 3))).isEqualTo(of(1, 2, 3));
        assertThat(empty().intersect(of(1, 2))).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateUnion() {
        assertThat(of(1, 2, 3).union(of(2))).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).union(of(5))).isEqualTo(of(1, 2, 3, 5));
        assertThat(of(1, 2, 3).union(of(1, 2, 3))).isEqualTo(of(1, 2, 3));
        assertThat(empty().union(of(1, 2))).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldRemoveElement() {
        assertThat(of(1, 2, 3).remove(2)).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).remove(5)).isEqualTo(of(1, 2, 3));
        assertThat(empty().remove(5)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllElements() {
        assertThat(of(1, 2, 3).removeAll(empty())).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).removeAll(of(2))).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).removeAll(of(5))).isEqualTo(of(1, 2, 3));
        assertThat(empty().removeAll(of(5))).isEqualTo(empty());
    }

    @Test
    public void shouldMapDistinctElementsToOneElement() {
        assertThat(of(1, 2, 3).map(i -> 0)).isEqualTo(of(0));
    }

    @Override
    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        // sets have only distinct elements
    }

    @Override
    @Test
    public void shouldReplaceFirstOccurrenceOfNonNilUsingCurrNewWhenMultipleOccurrencesExist() {
        // sets have only distinct elements
    }
}
