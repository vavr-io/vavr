/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class StackTest {

    // -- static narrow

    @Test
    public void shouldNarrowStack() {
        final Stack<Double> doubles = List.of(1.0d);
        Stack<Number> numbers = Stack.narrow(doubles);
        numbers = numbers.push(new BigDecimal("2.0"));
        int sum = 0;
        while (!numbers.isEmpty()) {
            final Tuple2<Number, ? extends Stack<Number>> t = numbers.pop2();
            sum += t._1.intValue();
            numbers = t._2;
        }
        assertThat(sum).isEqualTo(3);
    }

}
