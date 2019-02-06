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
package io.vavr;

import io.vavr.collection.Iterator;
import io.vavr.collection.Traversable;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class IterableTest {

    // -- .exists(Predicate)

    @Test
    void shouldThrowOnExistsWithNullPredicate() {
        final Iterable<Integer> testee = Iterator::empty;
        assertEquals("predicate is null",
                assertThrows(NullPointerException.class, () -> testee.forAll(null)).getMessage()
        );
    }

    @Test
    void shouldBeAwareOfExistingElementWhenSingleton() {
        final Iterable<Integer> testee = () -> Iterator.of(1);
        assertTrue(testee.exists(i -> i == 1));
    }

    @Test
    void shouldBeAwareOfExistingElementWhenContainingMultipleElements() {
        final Iterable<Integer> testee = () -> Iterator.of(1, 2);
        assertTrue(testee.exists(i -> i == 2));
    }

    @Test
    void shouldBeAwareOfNonExistingElementWhenEmpty() {
        final Iterable<Integer> testee = Iterator::empty;
        assertFalse(testee.exists(i -> i == -1));
    }

    @Test
    void shouldBeAwareOfNonExistingElementWhenSingleton() {
        final Iterable<Integer> testee = () -> Iterator.of(1);
        assertFalse(testee.exists(i -> i == -1));
    }

    @Test
    void shouldBeAwareOfNonExistingElementWhenContainingMultipleElememnts() {
        final Iterable<Integer> testee = () -> Iterator.of(1, 2);
        assertFalse(testee.exists(i -> i == -1));
    }

    // -- .forAll(Predicate)

    @Test
    void shouldThrowOnForAllWithNullPredicate() {
        final Iterable<Integer> testee = Iterator::empty;
        assertEquals("predicate is null",
            assertThrows(NullPointerException.class, () -> testee.forAll(null)).getMessage()
        );
    }

    @Test
    void shouldBeAwareOfPropertyThatHoldsForAll() {
        final Iterable<Integer> testee = () -> Iterator.of(0, 2);
        assertTrue(testee.forAll(i -> i % 2 == 0));
    }

    @Test
    void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        final Iterable<Integer> testee = () -> Iterator.of(1, 2);
        assertFalse(testee.forAll(i -> i % 2 == 0));
    }

    // -- .to(Function)

    @Test
    void shouldThrowOnToWhenFromIterableIsNull() {
        assertEquals(
                "fromIterable is null",
                assertThrows(NullPointerException.class, () -> new ToDummy<>().to(null)).getMessage()
        );
    }

    @Test
    void shouldConvertTo() {
        assertSame(1, new ToDummy<>().to(ignored -> 1));
    }

}

// dummy impl, default method Traversable#to(Function) needed only
final class ToDummy<T> implements Traversable<T> {

    @Override
    public <U> Traversable<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> Traversable<U> map(Function<? super T, ? extends U> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

}
