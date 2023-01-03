/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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
