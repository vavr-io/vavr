/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
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
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import static io.vavr.collection.Iterator.range;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LazyTest extends AbstractValueTest {

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Undefined<T> empty() {
        return (Undefined<T>) Undefined.INSTANCE;
    }
    
    @Override
    protected <T> Lazy<T> of(T element) {
        return Lazy.of(() -> element);
    }

    @SafeVarargs
    @Override
    protected final <T> Lazy<T> of(T... elements) {
        return of(elements[0]);
    }
    
    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }
    
    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- static narrow

    @Test
    public void shouldNarrow() {
        final String expected = "Zero args";
        final Lazy<String> wideFunction = Lazy.of(() -> expected);
        final Lazy<CharSequence> actual = Lazy.narrow(wideFunction);
        assertThat(actual.get()).isEqualTo(expected);
    }

    // -- of(Supplier)

    @Test
    public void shouldNotChangeLazy() {
        final Lazy<Integer> expected = Lazy.of(() -> 1);
        assertThat(Lazy.of(expected)).isSameAs(expected);
    }

    @Test
    public void shouldThrowOnNullSupplier() {
        assertThrows(NullPointerException.class, () -> Lazy.of((Supplier<?>) null));
    }

    @Test
    public void shouldMemoizeValues() {
        final Lazy<Double> testee = Lazy.of(Math::random);
        final double expected = testee.get();
        for (int i = 0; i < 10; i++) {
            final double actual = testee.get();
            assertThat(actual).isEqualTo(expected);
        }
    }

    // -- iterate

    @Test
    public void shouldIterate() {
        final Iterator<Integer> iterator = Lazy.of(() -> 1).iterator();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- peek

    @Test
    public void shouldPeek() {
        final Lazy<Integer> lazy = Lazy.of(() -> 1);
        final Lazy<Integer> peek = lazy.peek(v -> assertThat(v).isEqualTo(1));
        assertThat(peek).isSameAs(lazy);
    }

    // -- sequence(Iterable)

    @Test
    public void shouldSequenceEmpty() {
        final List<Lazy<Integer>> testee = List.empty();
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.get()).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldSequenceNonEmptyLazy() {
        final List<Lazy<Integer>> testee = List.of(1, 2, 3).map(i -> Lazy.of(() -> i));
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.get()).isEqualTo(Vector.of(1, 2, 3));
    }

    @Test
    public void shouldNotEvaluateEmptySequence() {
        final List<Lazy<Integer>> testee = List.empty();
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.isEvaluated()).isFalse();
    }

    @Test
    public void shouldNotEvaluateNonEmptySequence() {
        final List<Lazy<Integer>> testee = List.of(1, 2, 3).map(i -> Lazy.of(() -> i));
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.isEvaluated()).isFalse();
    }

    @Test
    public void shouldMapOverLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Lazy<Integer> expected = Lazy.of(() -> 21);

        assertThat(testee.map(i -> i / 2)).isEqualTo(expected);
    }

    @Test
    public void shouldFilterOverLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Option<Integer> expectedPositive = Option.some(42);
        final Option<Integer> expectedNegative = Option.none();

        assertThat(testee.filter(i -> i % 2 == 0)).isEqualTo(expectedPositive);
        assertThat(testee.filter(i -> i % 2 != 0)).isEqualTo(expectedNegative);
    }

    @Test
    public void shouldTransformLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Integer expected = 21;

        final Integer actual = testee.transform(lazy -> lazy.get() / 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNotBeEmpty() {
        assertThat(Lazy.of(Option::none).isEmpty()).isFalse();
    }

    @Test
    public void shouldContainASingleValue() {
        assertThat(Lazy.of(Option::none).isSingleValued()).isTrue();
    }

    // -- val(Supplier, Class) -- Proxy

    @Test
    public void shouldCreateLazyProxy() {

        final String[] evaluated = new String[] { null };

        final CharSequence chars = Lazy.val(() -> {
            final String value = "Yay!";
            evaluated[0] = value;
            return value;
        }, CharSequence.class);

        assertThat(evaluated[0]).isEqualTo(null);
        assertThat(chars).isEqualTo("Yay!");
        assertThat(evaluated[0]).isEqualTo("Yay!");
    }

    @Test
    public void shouldThrowWhenCreatingLazyProxyAndSupplierIsNull() {
        assertThrows(NullPointerException.class, () -> Lazy.val(null, CharSequence.class));
    }

    @Test
    public void shouldThrowWhenCreatingLazyProxyAndTypeIsNull() {
        assertThrows(NullPointerException.class, () -> Lazy.val(() -> "", null));
    }

    @Test
    public void shouldThrowWhenCreatingLazyProxyOfObjectType() {
        assertThrows(IllegalArgumentException.class, () -> Lazy.val(() -> "", String.class));
    }

    @Test
    public void shouldBehaveLikeValueWhenCreatingProxy() {
        final CharSequence chars = Lazy.val(() -> "Yay!", CharSequence.class);
        assertThat(chars.toString()).isEqualTo("Yay!");
    }

    // -- isEvaluated()

    @Test
    public void shouldBeAwareOfEvaluated() {
        final Lazy<Void> lazy = Lazy.of(() -> null);
        assertThat(lazy.isEvaluated()).isFalse();
        assertThat(lazy.isEvaluated()).isFalse(); // remains not evaluated
        lazy.get();
        assertThat(lazy.isEvaluated()).isTrue();
    }

    // -- Serialization

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = Serializables.deserialize(Serializables.serialize(Lazy.of(() -> 1)));
        final Object expected = Lazy.of(() -> 1);
        assertThat(actual).isEqualTo(expected);
    }

    // -- concurrency

    @Test
    public void shouldSupportMultithreading() {
        final AtomicBoolean isEvaluated = new AtomicBoolean();
        final AtomicBoolean lock = new AtomicBoolean();
        final Lazy<Integer> lazy = Lazy.of(() -> {
            while (lock.get()) {
                Try.run(() -> Thread.sleep(300));
            }
            return 1;
        });
        new Thread(() -> {
            Try.run(() -> Thread.sleep(100));
            new Thread(() -> {
                Try.run(() -> Thread.sleep(100));
                lock.set(false);
            }).start();
            isEvaluated.compareAndSet(false, lazy.isEvaluated());
            lazy.get();
        }).start();
        assertThat(isEvaluated.get()).isFalse();
        assertThat(lazy.get()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "StatementWithEmptyBody", "rawtypes" })
    public void shouldBeConsistentFromMultipleThreads() throws Exception {
        for (int i = 0; i < 100; i++) {
            final AtomicBoolean canProceed = new AtomicBoolean(false);
            final Vector<CompletableFuture<Void>> futures = Vector.range(0, 10).map(j -> {
                final AtomicBoolean isEvaluated = new AtomicBoolean(false);
                final Integer expected = ((j % 2) == 1) ? null : j;
                Lazy<Integer> lazy = Lazy.of(() -> {
                    assertThat(isEvaluated.getAndSet(true)).isFalse();
                    return expected;
                });
                return Tuple.of(lazy, expected);
            }).flatMap(t -> range(0, 5).map(j -> runAsync(() -> {
                        while (!canProceed.get()) { /* busy wait */ }
                        assertThat(t._1.get()).isEqualTo(t._2);
                    }))
            );

            final CompletableFuture all = CompletableFuture.allOf(futures.toJavaList().toArray(new CompletableFuture<?>[0]));
            canProceed.set(true);
            all.join();
        }
    }

    // -- equals

    @SuppressWarnings({ "EqualsBetweenInconvertibleTypes", "EqualsWithItself" })
    @Test
    public void shouldDetectEqualObject() {
        assertThat(Lazy.of(() -> 1).equals("")).isFalse();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 1))).isTrue();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 2))).isFalse();
        final Lazy<Integer> same = Lazy.of(() -> 1);
        assertThat(same.equals(same)).isTrue();
    }

    @SuppressWarnings({ "EqualsBetweenInconvertibleTypes", "EqualsWithItself" })
    @Test
    public void shouldUseDefaultEqualsSemanticsForArrays() {
        assertThat(Lazy.of(() -> new Integer[] {1}).equals("")).isFalse();
        assertThat(Lazy.of(() -> new Integer[] {1}).equals(Lazy.of(() -> new Integer[] {1}))).isFalse();
        final Lazy<Integer[]> same = Lazy.of(() -> new Integer[] {1});
        assertThat(same.equals(same)).isTrue();
    }

    @Test
    public void shouldDetectUnequalObject() {
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldComputeHashCode() {
        assertThat(Lazy.of(() -> 1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    @Test
    public void shouldComputeHashCodeForArrays() {
        Integer[] value = new Integer[] {1};
        //noinspection ArrayHashCode
        assertThat(Lazy.of(() -> value).hashCode()).isEqualTo(value.hashCode());
    }

    // -- toString

    @Test
    public void shouldConvertNonEvaluatedValueToString() {
        final Lazy<Integer> lazy = Lazy.of(() -> 1);
        assertThat(lazy.toString()).isEqualTo("Lazy(?)");
    }

    @Test
    public void shouldConvertEvaluatedValueToString() {
        final Lazy<Integer> lazy = Lazy.of(() -> 1);
        lazy.get();
        assertThat(lazy.toString()).isEqualTo("Lazy(1)");
    }

    // -- spliterator

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(Lazy.of(() -> 1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)).isTrue();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(Lazy.of(() -> 1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(Lazy.of(() -> 1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
    }

    // === OVERRIDDEN

    // -- isLazy

    @Override
    @Test
    public void shouldVerifyLazyProperty() {
        assertThat(empty().isLazy()).isTrue();
        assertThat(of(1).isLazy()).isTrue();
    }

}

/**
 * Lazy can't be empty. It is a placeholder for an existing value, but only evaluated when needed.
 * In order to reuse existing unit tests, that are valid for all Value implementations,
 * we provide here an _imaginary_ empty Lazy implementation, called 'undefined'.
 * <p>
 * Note: It is no good idea to leak it outside of the test scope into the core library (otherwise Undefined will be the new null).
 */
final class Undefined<T> implements Value<T>, Serializable {

    private static final long serialVersionUID = 1L;

    static final Undefined<?> INSTANCE = new Undefined<>();

    private Lazy<T> prototype = Lazy.of(() -> null);

    private Undefined() {
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isAsync() {
        return prototype.isAsync();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isLazy() {
        return prototype.isLazy();
    }

    @Override
    public boolean isSingleValued() {
        return prototype.isSingleValued();
    }

    @Override
    public Value<T> peek(@NonNull Consumer<? super T> action) {
        return this;
    }

    @Override
    public String stringPrefix() {
        return null;
    }

    @Override
    public @NonNull Iterator<T> iterator() {
        return Iterator.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Value<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        return (Value<U>) this;
    }

    @Override
    public boolean equals(Object o) {
        return o == INSTANCE;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Lazy()";
    }

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Undefined.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
