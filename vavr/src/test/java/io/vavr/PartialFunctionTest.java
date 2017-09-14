/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartialFunctionTest {

    @Test
    public void shouldReturnSome() {
        Option<String> oneToOne = HashMap.of(1, "One").lift().apply(1);
        assertThat(oneToOne).isEqualTo(Option.some("One"));
    }

    @Test
    public void shouldReturnNone() {
        Option<String> oneToOne = HashMap.<Integer, String>empty().lift().apply(1);
        assertThat(oneToOne).isEqualTo(Option.none());
    }

}