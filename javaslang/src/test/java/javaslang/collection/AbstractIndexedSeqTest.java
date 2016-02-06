/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;

public abstract class AbstractIndexedSeqTest extends AbstractSeqTest {

    @Override
    abstract protected <T> IndexedSeq<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> IndexedSeq<T> of(T... elements);

    // -- static narrow

    @Test
    public void shouldNarrowIndexedSeq() {
        final IndexedSeq<Double> doubles = of(1.0d);
        final IndexedSeq<Number> numbers = IndexedSeq.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- search

    @Test
    public void shouldSearchIndexForPresentElements() {
        assertThat(of(1, 2, 3, 4, 5, 6).search(3)).isEqualTo(2);
    }

    @Test
    public void shouldSearchNegatedInsertionPointMinusOneForAbsentElements() {
        assertThat(of(10, 20, 30).search(25)).isEqualTo(-3);
    }

    @Test
    public void shouldSearchIndexForPresentElementsUsingComparator() {
        assertThat(of(1, 2, 3, 4, 5, 6).search(3, Integer::compareTo)).isEqualTo(2);
    }

    @Test
    public void shouldSearchNegatedInsertionPointMinusOneForAbsentElementsUsingComparator() {
        assertThat(of(10, 20, 30).search(25, Integer::compareTo)).isEqualTo(-3);
    }

}
