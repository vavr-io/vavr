/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2021 Vavr, http://vavr.io
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
package io.vavr.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntMod2Test {

    private static final IntMod2 _1 = new IntMod2(1);
    private static final IntMod2 _2 = new IntMod2(2);
    private static final IntMod2 _3 = new IntMod2(3);
    private static final IntMod2 _4 = new IntMod2(4);

    @Test
    public void shouldBeEqualIfEven() {
        assertThat(_2.equals(_4)).isTrue();
        assertThat(_2.compareTo(_4)).isEqualTo(0);
    }

    @Test
    public void shouldBeEqualIfOdd() {
        assertThat(_1.equals(_3)).isTrue();
        assertThat(_1.compareTo(_3)).isEqualTo(0);
    }

    @Test
    public void shouldNotBeEqualIfEvenAndOdd() {
        assertThat(_1.equals(_2)).isFalse();
        assertThat(_1.compareTo(_2)).isEqualTo(1);
        assertThat(_2.compareTo(_3)).isEqualTo(-1);
    }

}
