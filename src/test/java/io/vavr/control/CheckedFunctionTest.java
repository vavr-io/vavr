/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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
package io.vavr.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckedFunctionTest {

    // -- static .identity()

    @Test
    void shouldCreateIdentity() throws Exception {
        CheckedFunction<Object, Object> f = CheckedFunction.identity();
        assertNull(f.apply(null));
        assertEquals(1, f.apply(1));
    }

    // -- .andThen(CheckedFunction)

    @Test
    void shouldApplyOneCheckedFunctionAndThenAnotherCheckedFunction() throws Exception {
        final CheckedFunction<Integer, Boolean> before = i -> i % 2 == 0;
        final CheckedFunction<Boolean, String> after = Object::toString;
        final CheckedFunction<Integer, String> f = before.andThen(after);
        assertEquals("true", f.apply(0));
        assertEquals("false", f.apply(1));
    }

    @Test
    void shouldNotApplyAfterWhenBeforeThrowsWhenCombiningWithAndThen() {
        final CheckedFunction<Integer, Boolean> before = ignored -> { throw new Exception("before"); };
        final CheckedFunction<Boolean, String> after = ignored -> { throw new AssertionError("after called"); };
        final CheckedFunction<Integer, String> f = before.andThen(after);
        assertEquals(
                "before",
                assertThrows(Exception.class, () -> f.apply(null)).getMessage()
        );
    }

    @Test
    void shouldApplyBeforeWhenAfterThrowsWhenCombiningWithAndThen() {
        final CheckedFunction<Integer, Boolean> before = ignored -> true;
        final CheckedFunction<Boolean, String> after = ignored -> { throw new Exception("after"); };
        final CheckedFunction<Integer, String> f = before.andThen(after);
        assertEquals(
                "after",
                assertThrows(Exception.class, () -> f.apply(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEIfAndThenReceivesNullParam() {
        final CheckedFunction<?, ?> f = o -> o;
        assertEquals(
                "after is null",
                assertThrows(NullPointerException.class, () -> f.andThen(null)).getMessage()
        );
    }

    // -- .apply(Object)

    @Test
    void shouldBeAbleToThrowCheckedException() {
        final CheckedFunction<?, ?> f = ignored -> { throw new Exception(); };
        assertThrows(Exception.class, () -> f.apply(null));
    }

    // -- .compose(CheckedFunction)

    @Test
    void shouldApplyOneCheckedFunctionComposedWithAnotherCheckedFunction() throws Exception {
        final CheckedFunction<Integer, Boolean> before = i -> i % 2 == 0;
        final CheckedFunction<Boolean, String> after = Object::toString;
        final CheckedFunction<Integer, String> f = after.compose(before);
        assertEquals("true", f.apply(0));
        assertEquals("false", f.apply(1));
    }

    @Test
    void shouldNotApplyAfterWhenBeforeThrowsWhenCombiningWithCompose() {
        final CheckedFunction<Integer, Boolean> before = ignored -> { throw new Exception("before"); };
        final CheckedFunction<Boolean, String> after = ignored -> { throw new AssertionError("before called"); };
        final CheckedFunction<Integer, String> f = after.compose(before);
        assertEquals(
                "before",
                assertThrows(Exception.class, () -> f.apply(null)).getMessage()
        );
    }

    @Test
    void shouldApplyBeforeWhenAfterThrowsWhenCombiningWithCompose() {
        final CheckedFunction<Integer, Boolean> before = ignored -> true;
        final CheckedFunction<Boolean, String> after = ignored -> { throw new Exception("after"); };
        final CheckedFunction<Integer, String> f = after.compose(before);
        assertEquals(
                "after",
                assertThrows(Exception.class, () -> f.apply(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEIfComposeReceivesNullParam() {
        final CheckedFunction<?, ?> f = o -> o;
        assertEquals(
                "before is null",
                assertThrows(NullPointerException.class, () -> f.compose(null)).getMessage()
        );
    }
    
}
