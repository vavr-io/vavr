/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
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
package io.vavr;

import org.junit.Test;

import static io.vavr.API.List;
import static org.assertj.core.api.Assertions.assertThat;

public class FoldOperatorTest {

    @Test
    public void given_foldOperator_when_simpleCombination_then_expectedBehaviourTest() {

        //a ^ (b ^ (c ^ 0))
        assertThat(List(3, 2, 1).foldRight(0d, Math::pow)).isEqualTo(9.0);

        //a ^ (b ^ (c ^ 1))
        assertThat(List(1, 2, 3).foldRight(0d, Math::pow)).isEqualTo(1.0);

        //a ^ (b ^ (c ^ 1))
        assertThat(List(1, 2, 3).foldRight(1d, Math::pow)).isEqualTo(1.0);

        //((1 ^ a) ^ b) ^ c
        assertThat(List(1, 2, 3).foldLeft(1d, Math::pow)).isEqualTo(1.0);

        //((1 ^ a) ^ b) ^ c
        assertThat(List(1, 2, 3).foldLeft(0d, Math::pow)).isEqualTo(0.0);

        //a ^ (b ^ c))
        assertThat(List(1, 2, 3).map(Double::valueOf)
                .reduce(Math::pow)).isEqualTo(1.0);

        //a ^ (b ^ c))
        assertThat(List(2, 2, 3).map(Double::valueOf).reduce(Math::pow)).isEqualTo(64.0);

        //a ^ (b ^ c))
        assertThat(List(3, 2, 1).map(Double::valueOf).reduce(Math::pow)).isEqualTo(9.0);
    }

}
