/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueTest {

    @Test
    public void shouldNarrowValue() {
        final Value<Double> doubles = List.of(1.0d);
        final Value<Number> numbers = Value.narrow(doubles);
        assertThat(numbers.get()).isEqualTo(1.0d);
    }
}
