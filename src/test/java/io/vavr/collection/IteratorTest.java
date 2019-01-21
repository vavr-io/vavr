/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class IteratorTest {

    // -- static .empty()

    @Test
    void shouldCreateEmptyIterator() {
        assertTrue(Iterator.empty() instanceof Iterator);
    }

    // -- static .of()

    @Test
    void shouldCreateIteratorOfOneElement() {
        final Iterator<Object> iterator = Iterator.of(1);
        assertNotNull(iterator);
    }

    // -- .hasNext()

    @Test
    void shouldNotHaveNextWhenEmpty() {
        assertFalse(Iterator.empty().hasNext());
    }

    @Test
    void shouldHaveNextWhenIteratorOfOneElementAndNextWasNotCalled() {
        assertTrue(Iterator.of(1).hasNext());
    }

    @Test
    void shouldNotHaveNextWhenIteratorOfOneElementAndNextWasCalled() {
        final Iterator<Object> iterator = Iterator.of(1);
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    // -- .next()

    @Test
    void shouldThrowOnNextWhenEmpty() {
        assertThrows(NoSuchElementException.class, Iterator.empty()::next);
    }

    @Test
    void shouldReturnValueOnNextWhenIteratorOfOneElementAndNextWasNotCalled() {
        final Iterator<Object> iterator = Iterator.of(1);
        assertSame(1, iterator.next());
    }

    @Test
    void shouldThrowOnNextWhenIteratorOfOneElementAndNextWasCalled() {
        final Iterator<Object> iterator = Iterator.of(1);
        iterator.next();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    // -- .nextOption()

    @Test
    void shouldReturnNoneOnNextOptionWhenEmpty() {
        assertSame(Option.none(), Iterator.empty().nextOption());
    }

    @Test
    void shouldReturnSomeOnNextOptionWhenIteratorOfOneElement() {
        assertEquals(Option.some(1), Iterator.of(1).nextOption());
    }

    // -- .toString()

    @Test
    void shouldBeReliableToStringWhenEmpty() {
        assertEquals("EmptyIterator", Iterator.empty().toString());
    }

    @Test
    void shouldBeReliableToStringWhenIteratorOfOneElement() {
        assertEquals("SingletonIterator", Iterator.of(1).toString());
    }

}
