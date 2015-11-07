/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

public abstract class AbstractSetTest extends AbstractTraversableRangeTest {

    @Override
    abstract protected <T> Set<T> empty();

    @Override
    abstract protected <T> Set<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Set<T> ofAll(T... elements);

    @Test
    public void shouldAddAllOfIterable() {
        assertThat(ofAll(1, 2, 3).addAll(empty())).isEqualTo(ofAll(1, 2, 3));
        assertThat(empty().addAll(ofAll(2, 3, 4))).isEqualTo(ofAll(2, 3, 4));
        assertThat(ofAll(1, 2, 3).addAll(ofAll(2, 3, 4))).isEqualTo(ofAll(1, 2, 3, 4));
    }

    @Test
    public void shouldCalculateDifference() {
        assertThat(ofAll(1, 2, 3).diff(of(2))).isEqualTo(ofAll(1, 3));
        assertThat(ofAll(1, 2, 3).diff(of(5))).isEqualTo(ofAll(1, 2, 3));
        assertThat(ofAll(1, 2, 3).diff(ofAll(1, 2, 3))).isEqualTo(empty());
        assertThat(empty().diff(ofAll(1, 2))).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateIntersect() {
        assertThat(ofAll(1, 2, 3).intersect(of(2))).isEqualTo(of(2));
        assertThat(ofAll(1, 2, 3).intersect(of(5))).isEqualTo(empty());
        assertThat(ofAll(1, 2, 3).intersect(ofAll(1, 2, 3))).isEqualTo(ofAll(1, 2, 3));
        assertThat(empty().intersect(ofAll(1, 2))).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateUnion() {
        assertThat(ofAll(1, 2, 3).union(of(2))).isEqualTo(ofAll(1, 2, 3));
        assertThat(ofAll(1, 2, 3).union(of(5))).isEqualTo(ofAll(1, 2, 3, 5));
        assertThat(ofAll(1, 2, 3).union(ofAll(1, 2, 3))).isEqualTo(ofAll(1, 2, 3));
        assertThat(empty().union(ofAll(1, 2))).isEqualTo(ofAll(1, 2));
    }

    @Test
    public void shouldRemoveElement() {
        assertThat(ofAll(1, 2, 3).remove(2)).isEqualTo(ofAll(1, 3));
        assertThat(ofAll(1, 2, 3).remove(5)).isEqualTo(ofAll(1, 2, 3));
        assertThat(empty().remove(5)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllElements() {
        assertThat(ofAll(1, 2, 3).removeAll(empty())).isEqualTo(ofAll(1, 2, 3));
        assertThat(ofAll(1, 2, 3).removeAll(of(2))).isEqualTo(ofAll(1, 3));
        assertThat(ofAll(1, 2, 3).removeAll(of(5))).isEqualTo(ofAll(1, 2, 3));
        assertThat(empty().removeAll(of(5))).isEqualTo(empty());
    }

    @Test
    public void shouldMapDistinctElementsToOneElement() {
        assertThat(ofAll(1, 2, 3).map(i -> 0)).isEqualTo(of(0));
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
