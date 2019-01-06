/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
package io.vavr.test;

import io.vavr.test.Property.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionTest {

    /**
     * Def: A 'Condition' is the result of {@code p => q} where {@code p} is a pre-condition and {@code q} is a post-condition.
     * <p>
     * The following holds: {@code p => q ≡ ¬p ∨ q}
     */
    @Test
    public void should() {
        assertThat(cond(false, false)).isTrue();
        assertThat(cond(false, true)).isTrue();
        assertThat(cond(true, false)).isFalse();
        assertThat(cond(true, true)).isTrue();
    }

    private boolean cond(boolean p, boolean q) {
        return !new Condition(p, q).isFalse();
    }
}
