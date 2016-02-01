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

    // -- static narrow

    @Test
    public void shouldNarrowIndexedSeq() {
        final IndexedSeq<Double> doubles = of(1.0d);
        final IndexedSeq<Number> numbers = IndexedSeq.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }
}
