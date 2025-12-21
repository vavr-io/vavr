/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.*;
import io.vavr.collection.IteratorModule.ConcatIterator;
import io.vavr.collection.IteratorModule.DistinctIterator;
import io.vavr.collection.IteratorModule.GroupedIterator;
import io.vavr.control.Option;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

import static io.vavr.collection.IteratorModule.BigDecimalHelper.areEqual;
import static io.vavr.collection.IteratorModule.BigDecimalHelper.asDecimal;
import static io.vavr.collection.IteratorModule.CachedIterator;
import static io.vavr.collection.IteratorModule.EmptyIterator;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.math.RoundingMode.HALF_UP;

/**
 * A compositional alternative to {@code java.util.Iterator} designed for single-pass
 * traversal of a sequence.
 *
 * <p><strong>Note:</strong> Iterators maintain an internal mutable state.
 * They are not thread-safe and must not be reused or shared across operations
 * (for example, after passing them to
 * {@linkplain io.vavr.collection.List#ofAll(Iterable)}).
 *
 * <p>The abstraction defines two fundamental operations:
 * {@code hasNext()}, which checks whether another element is available,
 * and {@code next()}, which consumes and returns that element. If
 * {@code hasNext()} returns {@code false}, {@code next()} will throw
 * {@code NoSuchElementException}.
 *
 * <p><strong>Caution:</strong> Methods other than {@code hasNext()} and
 * {@code next()} are single-use. Once such a method has been invoked, further
 * method calls on the same iterator are not guaranteed to succeed.
 *
 * <p>In essence, an Iterator represents a traversal cursor over a collection
 * rather than the collection itself, and can therefore be consumed only once.
 *
 * @param <T> the element type
 * @author Daniel Dietrich
 */
// DEV-NOTE: we prefer returning empty() over this if !hasNext() == true in order to free memory.
public interface Iterator<T> extends java.util.Iterator<T>, Traversable<T> {

    /**
     * Creates an {@code Iterator} that traverses the elements of the provided
     * iterables in sequence, as if they were concatenated.
     *
     * @param iterables the source iterables
     * @param <T>       the element type
     * @return an iterator yielding the elements of each iterable in order
     * @throws NullPointerException if {@code iterables} is {@code null}
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> Iterator<T> concat(Iterable<? extends T>... iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        if (iterables.length == 0) {
            return empty();
        } else {
            ConcatIterator<T> res = new ConcatIterator<>();
            for (Iterable<? extends T> iterable : iterables) {
                res.append(iterable.iterator());
            }
            return res;
        }
    }

    /**
     * Creates an {@code Iterator} that iterates over all elements of the supplied
     * sequence of iterables, in order.
     *
     * @param iterables an iterable whose elements provide the individual iterables to traverse
     * @param <T>       the element type
     * @return an iterator yielding the concatenated contents of the nested iterables
     * @throws NullPointerException if {@code iterables} is {@code null}
     */
    static <T> Iterator<T> concat(Iterable<? extends Iterable<? extends T>> iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        if (!iterables.iterator().hasNext()) {
            return empty();
        } else {
            ConcatIterator<T> res = new ConcatIterator<>();
            for (Iterable<? extends T> iterable : iterables) {
                res.append(iterable.iterator());
            }
            return res;
        }
    }

    /**
     * Returns an empty {@code Iterator}.
     *
     * @param <T> the element type
     * @return an iterator with no elements
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> empty() {
        return (Iterator<T>) EmptyIterator.INSTANCE;
    }

    /**
     * Narrows an {@code Iterator<? extends T>} to {@code Iterator<T>} using a
     * type-safe cast. This is valid because the iterator is read-only with
     * respect to element types.
     *
     * @param iterator the iterator to narrow
     * @param <T>      the element type
     * @return the same iterator, viewed as {@code Iterator<T>}
     * @throws NullPointerException if {@code iterator} is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> narrow(Iterator<? extends T> iterator) {
        return (Iterator<T>) iterator;
    }

    /**
     * Creates an {@code Iterator} that yields exactly one element.
     *
     * @param element the single element
     * @param <T>     the element type
     * @return an iterator containing only {@code element}
     */
    static <T> Iterator<T> of(T element) {
        return new AbstractIterator<T>() {

            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T getNext() {
                hasNext = false;
                return element;
            }
        };
    }

    /**
     * Creates an {@code Iterator} that iterates over the provided elements.
     *
     * @param elements zero or more elements
     * @param <T>      the element type
     * @return an iterator over the supplied elements
     */
    @SafeVarargs
    static <T> Iterator<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements.length == 0) {
            return empty();
        } else {
            return new AbstractIterator<T>() {

                int index = 0;

                @Override
                public boolean hasNext() {
                    return index < elements.length;
                }

                @Override
                public T getNext() {
                    return elements[index++];
                }
            };
        }
    }

    /**
     * Creates an {@code Iterator} from the provided {@link Iterable}.
     * This is a convenience method equivalent to calling
     * {@code Iterator.ofAll(iterable.iterator())}.
     *
     * @param iterable the source iterable
     * @param <T>      the element type
     * @return an iterator over the iterable's elements
     * @throws NullPointerException if {@code iterable} is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> ofAll(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Iterator) {
            return (Iterator<T>) iterable;
        } else {
            return ofAll(iterable.iterator());
        }
    }

    /**
     * Creates an {@code Iterator} that delegates {@code hasNext()} and {@code next()}
     * to the given {@link java.util.Iterator}.
     *
     * @param iterator the underlying iterator
     * @param <T>      the element type
     * @return an iterator that forwards calls to {@code iterator}
     * @throws NullPointerException if {@code iterator} is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> ofAll(java.util.Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        if (iterator instanceof Iterator) {
            return (Iterator<T>) iterator;
        } else {
            return new AbstractIterator<T>() {

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public T getNext() {
                    return iterator.next();
                }
            };
        }
    }

    /**
     * Creates an {@code Iterator} over the given boolean values.
     *
     * @param elements the boolean values
     * @return an iterator yielding the boxed values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Boolean>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Boolean getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given byte values.
     *
     * @param elements the byte values
     * @return an iterator yielding the boxed {@code Byte} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Byte>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Byte getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given char values.
     *
     * @param elements the char values
     * @return an iterator yielding the boxed {@code Character} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Character>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Character getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given double values.
     *
     * @param elements the double values
     * @return an iterator yielding the boxed {@code Double} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Double> ofAll(double... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Double>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Double getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given float values.
     *
     * @param elements the float values
     * @return an iterator yielding the boxed {@code Float} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Float> ofAll(float... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Float>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Float getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given int values.
     *
     * @param elements the int values
     * @return an iterator yielding the boxed {@code Integer} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Integer>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Integer getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given long values.
     *
     * @param elements the long values
     * @return an iterator yielding the boxed {@code Long} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Long>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Long getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Creates an {@code Iterator} over the given short values.
     *
     * @param elements the short values
     * @return an iterator yielding the boxed {@code Short} values
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Iterator<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return new AbstractIterator<Short>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public Short getNext() {
                return elements[i++];
            }
        };
    }

    /**
     * Returns an {@code Iterator} over a sequence of {@code n} elements, where each element
     * is computed by the given function {@code f} applied to its index.
     *
     * <p>The resulting sequence is {@code f(0), f(1), ..., f(n - 1)}.
     *
     * @param <T> the element type
     * @param n   the number of elements
     * @param f   the function computing element values
     * @return an iterator over the computed elements
     * @throws NullPointerException if {@code f} is {@code null}
     */
    static <T> Iterator<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return io.vavr.collection.Collections.tabulate(n, f);
    }

    /**
     * Returns an {@code Iterator} over a sequence of {@code n} elements supplied
     * by the given {@code Supplier}.
     *
     * <p>Each element is obtained by invoking {@code s.get()}.
     *
     * @param <T> the element type
     * @param n   the number of elements
     * @param s   the supplier providing element values
     * @return an iterator over the supplied elements
     * @throws NullPointerException if {@code s} is {@code null}
     */
    static <T> Iterator<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return io.vavr.collection.Collections.fill(n, s);
    }

    /**
     * Returns an {@code Iterator} containing the given {@code element} repeated {@code n} times.
     *
     * @param <T>     the element type
     * @param n       the number of repetitions
     * @param element the element to repeat
     * @return an iterator over {@code n} occurrences of {@code element}
     */
    static <T> Iterator<T> fill(int n, T element) {
        return io.vavr.collection.Collections.fillObject(n, element);
    }

    /**
     * Creates an {@code Iterator} of characters starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.range('a', 'c')  // yields 'a', 'b'
     * Iterator.range('c', 'a')  // yields no elements
     * </pre>
     *
     * @param from        the first character (inclusive)
     * @param toExclusive the end character (exclusive)
     * @return an iterator over the specified character range, or empty if {@code from >= toExclusive}
     */
    static Iterator<Character> range(char from, char toExclusive) {
        return rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates an {@code Iterator} of characters starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeBy('a', 'c', 1)  // yields 'a', 'b'
     * Iterator.rangeBy('a', 'd', 2)  // yields 'a', 'c'
     * Iterator.rangeBy('d', 'a', -2) // yields 'd', 'b'
     * Iterator.rangeBy('d', 'a', 2)  // yields no elements
     * </pre>
     *
     * @param from        the first character (inclusive)
     * @param toExclusive the end character (exclusive) - successor of the last character if {@code step > 0}, or predecessor if {@code step < 0}
     * @param step        the increment between characters; must not be zero
     * @return an iterator over the specified character range, or empty if the step direction
     *         does not match the direction from {@code from} to {@code toExclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Character> rangeBy(char from, char toExclusive, int step) {
        return rangeBy((int) from, (int) toExclusive, step).map(i -> (char) i.shortValue());
    }

    /**
     * Creates an {@code Iterator} of double values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeBy(1.0, 3.0, 1.0)   // yields 1.0, 2.0
     * Iterator.rangeBy(1.0, 4.0, 2.0)   // yields 1.0, 3.0
     * Iterator.rangeBy(4.0, 1.0, -2.0)  // yields 4.0, 2.0
     * Iterator.rangeBy(4.0, 1.0, 2.0)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step direction does not match the
     *         direction from {@code from} to {@code toExclusive}, or if {@code from == toExclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Double> rangeBy(double from, double toExclusive, double step) {
        final BigDecimal fromDecimal = asDecimal(from), toDecimal = asDecimal(toExclusive), stepDecimal = asDecimal(step);
        return rangeBy(fromDecimal, toDecimal, stepDecimal).map(BigDecimal::doubleValue);
    }

    /**
     * Creates an {@code Iterator} of {@code BigDecimal} values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive), advancing by the specified {@code step}.
     *
     * <p>This method provides precise decimal arithmetic suitable for financial calculations
     * and other scenarios where exact decimal representation is required.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeBy(new BigDecimal("1.0"), new BigDecimal("3.0"), new BigDecimal("1.0"))   // yields 1.0, 2.0
     * Iterator.rangeBy(new BigDecimal("1.0"), new BigDecimal("4.0"), new BigDecimal("2.0"))   // yields 1.0, 3.0
     * Iterator.rangeBy(new BigDecimal("4.0"), new BigDecimal("1.0"), new BigDecimal("-2.0"))  // yields 4.0, 2.0
     * Iterator.rangeBy(new BigDecimal("4.0"), new BigDecimal("1.0"), new BigDecimal("2.0"))   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step direction does not match the
     *         direction from {@code from} to {@code toExclusive}, or if {@code from == toExclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<BigDecimal> rangeBy(BigDecimal from, BigDecimal toExclusive, BigDecimal step) {
        if (step.signum() == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (areEqual(from, toExclusive) || step.signum() == from.subtract(toExclusive).signum()) {
            return empty();
        } else {
            if (step.signum() > 0) {
                return new AbstractIterator<BigDecimal>() {
                    BigDecimal i = from;

                    @Override
                    public boolean hasNext() {
                        return i.compareTo(toExclusive) < 0;
                    }

                    @Override
                    public BigDecimal getNext() {
                        final BigDecimal next = this.i;
                        this.i = next.add(step);
                        return next;
                    }
                };
            } else {
                return new AbstractIterator<BigDecimal>() {
                    BigDecimal i = from;

                    @Override
                    public boolean hasNext() {
                        return i.compareTo(toExclusive) > 0;
                    }

                    @Override
                    public BigDecimal getNext() {
                        final BigDecimal next = this.i;
                        this.i = next.add(step);
                        return next;
                    }
                };
            }
        }
    }

    /**
     * Creates an {@code Iterator} of int values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.range(0, 0)   // yields no elements
     * Iterator.range(2, 0)   // yields no elements
     * Iterator.range(-2, 2)  // yields -2, -1, 0, 1
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     * @return an iterator over the specified range, or empty if {@code from >= toExclusive}
     */
    static Iterator<Integer> range(int from, int toExclusive) {
        return rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates an {@code Iterator} of int values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeBy(1, 3, 1)   // yields 1, 2
     * Iterator.rangeBy(1, 4, 2)   // yields 1, 3
     * Iterator.rangeBy(4, 1, -2)  // yields 4, 2
     * Iterator.rangeBy(4, 1, 2)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     *                     — last number + 1 if {@code step > 0}, or last number - 1 if {@code step < 0}
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step direction does not match the
     *         direction from {@code from} to {@code toExclusive}, or if {@code from == toExclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Integer> rangeBy(int from, int toExclusive, int step) {
        final int toInclusive = toExclusive - (step > 0 ? 1 : -1);
        return rangeClosedBy(from, toInclusive, step);
    }

    /**
     * Creates an {@code Iterator} of long values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.range(0L, 0L)   // yields no elements
     * Iterator.range(2L, 0L)   // yields no elements
     * Iterator.range(-2L, 2L)  // yields -2L, -1L, 0L, 1L
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     * @return an iterator over the specified range, or empty if {@code from >= toExclusive}
     */
    static Iterator<Long> range(long from, long toExclusive) {
        return rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates an {@code Iterator} of long values starting from {@code from} (inclusive)
     * up to {@code toExclusive} (exclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeBy(1L, 3L, 1L)   // yields 1L, 2L
     * Iterator.rangeBy(1L, 4L, 2L)   // yields 1L, 3L
     * Iterator.rangeBy(4L, 1L, -2L)  // yields 4L, 2L
     * Iterator.rangeBy(4L, 1L, 2L)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toExclusive the end number (exclusive)
     *                     — last number + 1 if {@code step > 0}, or last number - 1 if {@code step < 0}
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step direction does not match
     *         the direction from {@code from} to {@code toExclusive}, or if {@code from == toExclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Long> rangeBy(long from, long toExclusive, long step) {
        final long toInclusive = toExclusive - (step > 0 ? 1 : -1);
        return rangeClosedBy(from, toInclusive, step);
    }

    /**
     * Creates an {@code Iterator} of characters starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosed('a', 'c')  // yields 'a', 'b', 'c'
     * Iterator.rangeClosed('c', 'a')  // yields no elements
     * </pre>
     *
     * @param from        the first character (inclusive)
     * @param toInclusive the last character (inclusive)
     * @return an iterator over the specified character range, or empty if {@code from > toInclusive}
     */

    static Iterator<Character> rangeClosed(char from, char toInclusive) {
        return rangeClosedBy(from, toInclusive, 1);
    }

    /**
     * Creates an {@code Iterator} of characters starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosedBy('a', 'c', 1)   // yields 'a', 'b', 'c'
     * Iterator.rangeClosedBy('a', 'd', 2)   // yields 'a', 'c'
     * Iterator.rangeClosedBy('d', 'a', -2)  // yields 'd', 'b'
     * Iterator.rangeClosedBy('d', 'a', 2)   // yields no elements
     * </pre>
     *
     * @param from        the first character (inclusive)
     * @param toInclusive the last character (inclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified character range, or empty if the step
     *         direction does not match the direction from {@code from} to {@code toInclusive}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return rangeClosedBy((int) from, (int) toInclusive, step).map(i -> (char) i.shortValue());
    }

    /**
     * Creates an {@code Iterator} of double values starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosedBy(1.0, 3.0, 1.0)   // yields 1.0, 2.0, 3.0
     * Iterator.rangeClosedBy(1.0, 4.0, 2.0)   // yields 1.0, 3.0
     * Iterator.rangeClosedBy(4.0, 1.0, -2.0)  // yields 4.0, 2.0
     * Iterator.rangeClosedBy(4.0, 1.0, 2.0)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toInclusive the last number (inclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step
     *         direction does not match the direction from {@code from} to {@code toInclusive},
     *         or if {@code from == toInclusive} it returns a singleton iterator
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Double> rangeClosedBy(double from, double toInclusive, double step) {
        if (from == toInclusive) {
            return of(from);
        }

        final double toExclusive = (step > 0) ? Math.nextUp(toInclusive) : Math.nextDown(toInclusive);
        return rangeBy(from, toExclusive, step);
    }

    /**
     * Creates an {@code Iterator} of int values starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosed(0, 0)   // yields 0
     * Iterator.rangeClosed(2, 0)   // yields no elements
     * Iterator.rangeClosed(-2, 2)  // yields -2, -1, 0, 1, 2
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toInclusive the last number (inclusive)
     * @return an iterator over the specified range, or empty if {@code from > toInclusive}
     */
    static Iterator<Integer> rangeClosed(int from, int toInclusive) {
        return rangeClosedBy(from, toInclusive, 1);
    }

    /**
     * Creates an {@code Iterator} of int values starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosedBy(1, 3, 1)   // yields 1, 2, 3
     * Iterator.rangeClosedBy(1, 4, 2)   // yields 1, 3
     * Iterator.rangeClosedBy(4, 1, -2)  // yields 4, 2
     * Iterator.rangeClosedBy(4, 1, 2)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toInclusive the last number (inclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step
     *         direction does not match the direction from {@code from} to {@code toInclusive},
     *         or if {@code from == toInclusive} it returns a singleton iterator
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return of(from);
        } else if (Integer.signum(step) == Integer.signum(from - toInclusive)) {
            return empty();
        } else {
            final int end = toInclusive - step;
            if (step > 0) {
                return new AbstractIterator<Integer>() {
                    int i = from - step;

                    @Override
                    public boolean hasNext() {
                        return i <= end;
                    }

                    @Override
                    public Integer getNext() {
                        return i += step;
                    }
                };
            } else {
                return new AbstractIterator<Integer>() {
                    int i = from - step;

                    @Override
                    public boolean hasNext() {
                        return i >= end;
                    }

                    @Override
                    public Integer getNext() {
                        return i += step;
                    }
                };
            }
        }
    }

    /**
     * Creates an {@code Iterator} of long values starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive).
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosed(0L, 0L)   // yields 0L
     * Iterator.rangeClosed(2L, 0L)   // yields no elements
     * Iterator.rangeClosed(-2L, 2L)  // yields -2L, -1L, 0L, 1L, 2L
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toInclusive the last number (inclusive)
     * @return an iterator over the specified range, or empty if {@code from > toInclusive}
     */
    static Iterator<Long> rangeClosed(long from, long toInclusive) {
        return rangeClosedBy(from, toInclusive, 1L);
    }

    /**
     * Creates an {@code Iterator} of long values starting from {@code from} (inclusive)
     * up to {@code toInclusive} (inclusive), advancing by the specified {@code step}.
     *
     * <p>Examples:
     * <pre>
     * Iterator.rangeClosedBy(1L, 3L, 1L)   // yields 1L, 2L, 3L
     * Iterator.rangeClosedBy(1L, 4L, 2L)   // yields 1L, 3L
     * Iterator.rangeClosedBy(4L, 1L, -2L)  // yields 4L, 2L
     * Iterator.rangeClosedBy(4L, 1L, 2L)   // yields no elements
     * </pre>
     *
     * @param from        the first number (inclusive)
     * @param toInclusive the last number (inclusive)
     * @param step        the increment; must not be zero
     * @return an iterator over the specified range, or empty if the step
     *         direction does not match the direction from {@code from} to {@code toInclusive},
     *         or if {@code from == toInclusive} it returns a singleton iterator
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return of(from);
        } else if (Long.signum(step) == Long.signum(from - toInclusive)) {
            return empty();
        } else {
            final long end = toInclusive - step;
            if (step > 0) {
                return new AbstractIterator<Long>() {
                    long i = from - step;

                    @Override
                    public boolean hasNext() {
                        return i <= end;
                    }

                    @Override
                    public Long getNext() {
                        return i += step;
                    }
                };
            } else {
                return new AbstractIterator<Long>() {
                    long i = from - step;

                    @Override
                    public boolean hasNext() {
                        return i >= end;
                    }

                    @Override
                    public Long getNext() {
                        return i += step;
                    }
                };

            }
        }
    }

    /**
     * Returns an infinite {@code Iterator} of int values starting from {@code value}.
     *
     * <p>The iterator wraps from {@code Integer.MAX_VALUE} to {@code Integer.MIN_VALUE}.
     *
     * @param value the starting int value
     * @return an iterator that endlessly yields consecutive int values starting from {@code value}
     */
    static Iterator<Integer> from(int value) {
        return new AbstractIterator<Integer>() {
            private int next = value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer getNext() {
                return next++;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} of int values starting from {@code value}
     * and advancing by the specified {@code step}.
     *
     * <p>The iterator wraps from {@code Integer.MAX_VALUE} to {@code Integer.MIN_VALUE} if overflow occurs.
     *
     * @param value the starting int value
     * @param step  the increment for each iteration
     * @return an iterator that endlessly yields consecutive int values starting from {@code value}, spaced by {@code step}
     */
    static Iterator<Integer> from(int value, int step) {
        return new AbstractIterator<Integer>() {
            private int next = value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer getNext() {
                final int result = next;
                next += step;
                return result;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} of long values starting from {@code value}.
     *
     * <p>The iterator wraps from {@code Long.MAX_VALUE} to {@code Long.MIN_VALUE} if overflow occurs.
     *
     * @param value the starting long value
     * @return an iterator that endlessly yields consecutive long values starting from {@code value}
     */
    static Iterator<Long> from(long value) {
        return new AbstractIterator<Long>() {
            private long next = value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long getNext() {
                return next++;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} of long values starting from {@code value}
     * and advancing by the specified {@code step}.
     *
     * <p>The iterator wraps from {@code Long.MAX_VALUE} to {@code Long.MIN_VALUE} if overflow occurs.
     *
     * @param value the starting long value
     * @param step  the increment for each iteration
     * @return an iterator that endlessly yields consecutive long values starting from {@code value}, spaced by {@code step}
     */
    static Iterator<Long> from(long value, long step) {
        return new AbstractIterator<Long>() {
            private long next = value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long getNext() {
                final long result = next;
                next += step;
                return result;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} that repeatedly generates values
     * using the provided {@code Supplier}.
     *
     * @param supplier the supplier providing iterator values; must not be {@code null}
     * @param <T>      the type of values produced
     * @return an iterator that endlessly yields values from the supplier
     * @throws NullPointerException if {@code supplier} is {@code null}
     */
    static <T> Iterator<T> continually(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new AbstractIterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T getNext() {
                return supplier.get();
            }
        };
    }

    /**
     * Creates an {@code Iterator} that repeatedly invokes the given {@code Supplier}
     * as long as it returns a {@code Some} value, terminating when it returns {@code None}.
     *
     * @param supplier the supplier providing {@code Option} values; must not be {@code null}
     * @param <T>      the type of values produced
     * @return an iterator yielding the values wrapped in {@code Some}, stopping at the first {@code None}
     * @throws NullPointerException if the supplier produces a {@code null} value
     */
    static <T> Iterator<T> iterate(Supplier<? extends Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new AbstractIterator<T>() {
            Option<? extends T> nextOption;

            @Override
            public boolean hasNext() {
                if (nextOption == null) {
                    nextOption = supplier.get();
                }
                return nextOption.isDefined();
            }

            @Override
            public T getNext() {
                final T next =  nextOption.get();
                nextOption = null;
                return next;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} that generates values by repeatedly
     * applying the given function to the previous value, starting with {@code seed}.
     *
     * <p>Each call to {@code getNext()} produces the next element by applying {@code f}
     * to the previous element.
     *
     * @param seed the initial value
     * @param f    the function to compute the next value from the previous; must not be {@code null}
     * @param <T>  the type of values produced
     * @return an iterator that endlessly yields values generated from {@code seed} using {@code f}
     * @throws NullPointerException if {@code f} is {@code null}
     */
    static <T> Iterator<T> iterate(T seed, Function<? super T, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return new AbstractIterator<T>() {
            Function<? super T, ? extends T> nextFunc = s -> {
                nextFunc = f;
                return seed;
            };
            T current = null;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T getNext() {
                current = nextFunc.apply(current);
                return current;
            }
        };
    }

    /**
     * Returns an infinite {@code Iterator} that endlessly yields the given element.
     *
     * @param t   the element to repeat
     * @param <T> the type of the element
     * @return an iterator that repeatedly returns {@code t}
     */
    static <T> Iterator<T> continually(T t) {
        return new AbstractIterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T getNext() {
                return t;
            }
        };
    }

    // -- Additional methods of Iterator

    @Override
    default <R> Iterator<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction) {
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return filter(partialFunction::isDefinedAt).map(partialFunction::apply);
    }

    /**
     * Returns a new {@code Iterator} that yields the elements of this iterator
     * followed by all elements of the specified iterator.
     *
     * <p>This method appends the elements from {@code that} to the end of this
     * iterator, creating a concatenated sequence.
     *
     * <p>Examples:
     * <pre>
     * Iterator.of(1, 2).concat(Iterator.of(3, 4))  // yields 1, 2, 3, 4
     * Iterator.empty().concat(Iterator.of(1, 2))   // yields 1, 2
     * Iterator.of(1, 2).concat(Iterator.empty())   // yields 1, 2
     * </pre>
     *
     * @param that the iterator whose elements should be appended
     * @return a new iterator containing elements from both iterators
     * @throws NullPointerException if {@code that} is {@code null}
     */
    // DEV-NOTE: cannot use arg Iterable, it would be ambiguous
    default Iterator<T> concat(java.util.Iterator<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        if (!that.hasNext()) {
            return this;
        } else if (!hasNext()) {
            return ofAll(that);
        } else {
            return concat(this, ofAll(that));
        }
    }

    /**
     * Returns a new {@code Iterator} where the specified {@code element} is inserted
     * between each element of this iterator.
     *
     * @param element the element to intersperse
     * @return an iterator with {@code element} interleaved between the original elements
     */
    default Iterator<T> intersperse(T element) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                boolean insertElement = false;

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    if (insertElement) {
                        insertElement = false;
                        return element;
                    } else {
                        insertElement = true;
                        return that.next();
                    }
                }
            };
        }
    }

    /**
     * Applies a transformation function to this {@code Iterator} and returns the result.
     *
     * @param f   the function to transform this iterator; must not be {@code null}
     * @param <U> the type of the result
     * @return the result of applying {@code f} to this iterator
     * @throws NullPointerException if {@code f} is {@code null}
     */
    default <U> U transform(Function<? super Iterator<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    default <U> Iterator<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    default <U, R> Iterator<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            final java.util.Iterator<? extends U> it2 = that.iterator();
            return new AbstractIterator<R>() {
                @Override
                public boolean hasNext() {
                    return it1.hasNext() && it2.hasNext();
                }

                @Override
                public R getNext() {
                    return mapper.apply(it1.next(), it2.next());
                }
            };
        }
    }

    @Override
    default <U> Iterator<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        final java.util.Iterator<? extends U> thatIt = that.iterator();
        if (isEmpty() && !thatIt.hasNext()) {
            return empty();
        } else {
            final Iterator<T> thisIt = this;
            return new AbstractIterator<Tuple2<T, U>>() {
                @Override
                public boolean hasNext() {
                    return thisIt.hasNext() || thatIt.hasNext();
                }

                @Override
                public Tuple2<T, U> getNext() {
                    final T v1 = thisIt.hasNext() ? thisIt.next() : thisElem;
                    final U v2 = thatIt.hasNext() ? thatIt.next() : thatElem;
                    return Tuple.of(v1, v2);
                }
            };
        }
    }

    @Override
    default Iterator<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    default <U> Iterator<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            return new AbstractIterator<U>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return it1.hasNext();
                }

                @Override
                public U getNext() {
                    return mapper.apply(it1.next(), index++);
                }
            };
        }
    }

    @Override
    default <T1, T2> Tuple2<Iterator<T1>, Iterator<T2>> unzip(
      @NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        } else {
            final Stream<Tuple2<? extends T1, ? extends T2>> source = Stream.ofAll(this.map(unzipper));
            return Tuple.of(source.map(t -> (T1) t._1()).iterator(), source.map(t -> (T2) t._2()).iterator());
        }
    }

    @Override
    default <T1, T2, T3> Tuple3<Iterator<T1>, Iterator<T2>, Iterator<T3>> unzip3(
      @NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (!hasNext()) {
            return Tuple.of(empty(), empty(), empty());
        } else {
            final Stream<Tuple3<? extends T1, ? extends T2, ? extends T3>> source = Stream.ofAll(this.map(unzipper));
            return Tuple.of(source.map(t -> (T1) t._1()).iterator(), source.map(t -> (T2) t._2()).iterator(), source.map(t -> (T3) t._3()).iterator());
        }
    }

    /**
     * Creates an {@code Iterator} by repeatedly applying a function to a seed value.
     * <p>
     * The function takes the current seed and returns {@code None} to signal the end of iteration,
     * or {@code Some<Tuple2>} containing the next element to yield and the seed for the next step.
     *
     * <p>Example:
     * <pre>{@code
     * Iterator.unfold(10, x -> x == 0
     *   ? Option.none()
     *   : Option.of(new Tuple2<>(x-1, x)));
     * // yields 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
     * }
     * </pre>
     *
     * @param <T>  the type of the seed and produced elements
     * @param seed the initial seed value
     * @param f    the function to produce the next element and seed; must not be {@code null}
     * @return an iterator producing the elements generated by repeatedly applying {@code f}
     * @throws NullPointerException if {@code f} is {@code null}
     */
    static <T> Iterator<T> unfold(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
        return unfoldLeft(seed, f);
    }

    /**
     * Creates an {@code Iterator} by repeatedly applying a function to a seed value,
     * generating elements in a left-to-right order.
     *
     * <p>The function receives the current seed and returns {@code None} to signal
     * the end of iteration, or {@code Some<Tuple2>} containing the next seed and
     * the element to include in the iterator.
     *
     * <p>Example:
     * <pre>{@code
     * Iterator.unfoldLeft(10, x -> x == 0
     *   ? Option.none()
     *   : Option.of(new Tuple2<>(x-1, x)));
     * // yields 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
     * }
     * </pre>
     *
     * @param <T>  the type of the seed
     * @param <U>  the type of the produced elements
     * @param seed the initial seed value
     * @param f    the function to produce the next element and seed; must not be {@code null}
     * @return an iterator producing elements generated from the seed using {@code f}
     * @throws NullPointerException if {@code f} is {@code null}
     */
    static <T, U> Iterator<U> unfoldLeft(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends U>>> f) {
        Objects.requireNonNull(f, "f is null");
        return Stream.<U> ofAll(
                unfoldRight(seed, f.andThen(tupleOpt -> tupleOpt.map(t -> Tuple.of(t._2(), t._1())))))
                .reverse().iterator();
    }

    /**
     * Creates an {@code Iterator} by repeatedly applying a function to a seed value,
     * generating elements in a right-to-left order.
     *
     * <p>The function receives the current seed and returns {@code None} to signal
     * the end of iteration, or {@code Some<Tuple2>} containing the element to yield
     * and the next seed for subsequent calls.
     *
     * <p>Example:
     * <pre>{@code
     * Iterator.unfoldRight(10, x -> x == 0
     *   ? Option.none()
     *   : Option.of(new Tuple2<>(x, x-1)));
     * // yields 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
     * }
     * </pre>
     *
     * @param <T>  the type of the seed
     * @param <U>  the type of the produced elements
     * @param seed the initial seed value
     * @param f    the function to produce the next element and seed; must not be {@code null}
     * @return an iterator producing elements generated from the seed using {@code f}
     * @throws NullPointerException if {@code f} is {@code null}
     */
    static <T, U> Iterator<U> unfoldRight(T seed, Function<? super T, Option<Tuple2<? extends U, ? extends T>>> f) {
        Objects.requireNonNull(f, "the unfold iterating function is null");
        return new AbstractIterator<U>() {
            private Lazy<Option<Tuple2<? extends U, ? extends T>>> nextVal = Lazy.of(() -> f.apply(seed));

            @Override
            public boolean hasNext() {
                return nextVal.get().isDefined();
            }

            @Override
            public U getNext() {
                Tuple2<? extends U, ? extends T> tuple = nextVal.get().get();
                final U result = tuple._1();
                nextVal = Lazy.of(() -> f.apply(tuple._2()));
                return result;
            }
        };
    }

    // -- Overridden methods of Traversable

    @Override
    default Iterator<T> distinct() {
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, io.vavr.collection.HashSet.empty(), Function.identity());
        }
    }

    @Override
    default Iterator<T> distinctBy(@NonNull Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, TreeSet.empty(comparator), Function.identity());
        }
    }

    @Override
    default <U> Iterator<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, io.vavr.collection.HashSet.empty(), keyExtractor);
        }
    }

    /**
     * Returns a new {@code Iterator} containing the elements of this instance without duplicates,
     * keeping the last occurrence of each duplicate element, as determined by the given {@code comparator}.
     *
     * <p>When multiple elements are considered equal according to the comparator, only the
     * last occurrence in the sequence is retained.
     *
     * <p>Examples:
     * <pre>
     * Iterator.of(1, 2, 2, 3, 1).distinctByKeepLast(Comparator.naturalOrder())  // yields 2, 3, 1
     * </pre>
     *
     * @param comparator the comparator used to determine equality
     * @return a new iterator containing distinct elements, keeping the last occurrence of duplicates
     * @throws NullPointerException if {@code comparator} is {@code null}
     */
    default Iterator<T> distinctByKeepLast(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (!hasNext()) {
            return empty();
        } else {
            return Collections.reverseIterator(new DistinctIterator<>(
                    Collections.reverseIterator(this),
                    TreeSet.empty(comparator),
                    Function.identity()));
        }
    }

    /**
     * Returns a new {@code Iterator} containing the elements of this instance without duplicates,
     * keeping the last occurrence of each duplicate element, based on keys extracted from elements
     * using {@code keyExtractor}.
     *
     * <p>When multiple elements have the same extracted key, only the last occurrence in the
     * sequence is retained.
     *
     * <p>Examples:
     * <pre>
     * Iterator.of("a", "ab", "abc", "b").distinctByKeepLast(String::length)  // yields "abc", "b"
     * </pre>
     *
     * @param keyExtractor function used to extract the key from elements
     * @param <U>          the type of the extracted key
     * @return a new iterator containing distinct elements, keeping the last occurrence of duplicates
     * @throws NullPointerException if {@code keyExtractor} is {@code null}
     */
    default <U> Iterator<T> distinctByKeepLast(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        if (!hasNext()) {
            return empty();
        } else {
            return Collections.reverseIterator(new DistinctIterator<>(
                    Collections.reverseIterator(this),
                    io.vavr.collection.HashSet.empty(),
                    keyExtractor));
        }
    }

    /**
     * Removes up to n elements from this iterator.
     *
     * @param n A number
     * @return The empty iterator, if {@code n <= 0} or this is empty, otherwise a new iterator without the first n elements.
     */
    @Override
    default Iterator<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                long count = n;

                @Override
                public boolean hasNext() {
                    while (count > 0 && that.hasNext()) {
                        that.next(); // discarded
                        count--;
                    }
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    return that.next();
                }
            };
        }
    }

    @Override
    default Iterator<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {
                private io.vavr.collection.Queue<T> queue = io.vavr.collection.Queue.empty();

                @Override
                public boolean hasNext() {
                    while (queue.length() < n && that.hasNext()) {
                        queue = queue.append(that.next());
                    }
                    return queue.length() == n && that.hasNext();
                }

                @Override
                public T getNext() {
                    final Tuple2<T, io.vavr.collection.Queue<T>> t = queue.append(that.next()).dequeue();
                    queue = t._2();
                    return t._1();
                }
            };
        }
    }

    @Override
    default Iterator<T> dropUntil(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    default Iterator<T> dropWhile(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final CachedIterator<T> that = new CachedIterator<>(this);
            while (that.hasNext() && predicate.test(that.touch())) {
                that.next();
            }
            return that;
        }
    }

    /**
     * Returns an Iterator that contains elements that satisfy the given {@code predicate}.
     *
     * @param predicate A predicate
     * @return A new Iterator
     */
    @Override
    default Iterator<T> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                Option<T> next = Option.none();

                @Override
                public boolean hasNext() {
                    while (next.isEmpty() && that.hasNext()) {
                        final T candidate = that.next();
                        if (predicate.test(candidate)) {
                            next = Option.some(candidate);
                        }
                    }
                    return next.isDefined();
                }

                @Override
                public T getNext() {
                    final T result = next.get();
                    next = Option.none();
                    return result;
                }
            };
        }
    }

    @Override
    default Iterator<T> reject(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    default Option<T> findLast(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        T last = null;
        while (hasNext()) {
            final T elem = next();
            if (predicate.test(elem)) {
                last = elem;
            }
        }
        return Option.of(last);
    }

    /**
     * FlatMaps the elements of this Iterator to Iterables, which are iterated in the order of occurrence.
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A new Iterable
     */
    @Override
    default <U> Iterator<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<U>() {

                final Iterator<? extends T> inputs = that;
                java.util.Iterator<? extends U> current = java.util.Collections.emptyIterator();

                @Override
                public boolean hasNext() {
                    boolean currentHasNext;
                    while (!(currentHasNext = current.hasNext()) && inputs.hasNext()) {
                        current = mapper.apply(inputs.next()).iterator();
                    }
                    return currentHasNext;
                }

                @Override
                public U getNext() {
                    return current.next();
                }
            };
        }
    }

    @Override
    default <U> U foldRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return Stream.ofAll(this).foldRight(zero, f);
    }

    @Override
    default T get() {
        return head();
    }

    @Override
    default <C> Map<C, Iterator<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier) {
        return io.vavr.collection.Collections.groupBy(this, classifier, Iterator::ofAll);
    }

    @Override
    default Iterator<Seq<T>> grouped(int size) {
        return new GroupedIterator<>(this, size, size);
    }
    
    @Override
    default boolean hasDefiniteSize() {
        return false;
    }

    @Override
    default T head() {
        if (!hasNext()) {
            throw new NoSuchElementException("head() on empty iterator");
        }
        return next();
    }

    @Override
    default Iterator<T> init() {
        if (!hasNext()) {
            throw new UnsupportedOperationException();
        } else {
            return dropRight(1);
        }
    }

    @Override
    default Option<Iterator<T>> initOption() {
        return hasNext() ? Option.some(init()) : Option.none();
    }

    /**
     * An {@code Iterator} is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    @Override
    default boolean isEmpty() {
        return !hasNext();
    }

    /**
     * An {@code Iterator} is computed lazily.
     *
     * @return true
     */
    @Override
    default boolean isLazy() {
        return true;
    }

    @Override
    default boolean isTraversableAgain() {
        return false;
    }

    @Override
    default boolean isSequential() {
        return true;
    }

    @Override
    default @NonNull Iterator<T> iterator() {
        return this;
    }

    @Override
    default T last() {
        return Collections.last(this);
    }

    @Override
    default int length() {
        return foldLeft(0, (n, ignored) -> n + 1);
    }

    /**
     * Maps the elements of this Iterator lazily using the given {@code mapper}.
     *
     * @param mapper A mapper.
     * @param <U>    Component type
     * @return A new Iterator
     */
    @Override
    default <U> Iterator<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<U>() {

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public U getNext() {
                    return mapper.apply(that.next());
                }
            };
        }
    }

    @Override
    default <U> Iterator<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Iterator<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    default Iterator<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? ofAll(other) : this;
    }

    @Override
    default Iterator<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? ofAll(supplier.get()) : this;
    }

    @Override
    default Tuple2<Iterator<T>, Iterator<T>> partition(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        } else {
            final Tuple2<Iterator<T>, Iterator<T>> dup = IteratorModule.duplicate(this);
            return Tuple.of(dup._1().filter(predicate), dup._2().filter(predicate.negate()));
        }
    }

    @Override
    default Iterator<T> peek(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {
                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    final T next = that.next();
                    action.accept(next);
                    return next;
                }
            };
        }
    }

    @Override
    default T reduceLeft(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceLeft on Nil");
        } else {
            T xs = next();
            while (hasNext()) {
                xs = op.apply(xs, next());
            }
            return xs;
        }
    }

    @Override
    default T reduceRight(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceRight on Nil");
        } else {
            final Stream<T> reversed = Stream.ofAll(this).reverse();
            return reversed.reduceLeft((xs, x) -> op.apply(x, xs));
        }
    }

    @Override
    default Iterator<T> replace(T currentElement, T newElement) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {
                boolean isFirst = true;

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    final T elem = that.next();
                    if (isFirst && Objects.equals(currentElement, elem)) {
                        isFirst = false;
                        return newElement;
                    } else {
                        return elem;
                    }
                }
            };
        }
    }

    @Override
    default Iterator<T> replaceAll(T currentElement, T newElement) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    final T elem = that.next();
                    if (Objects.equals(currentElement, elem)) {
                        return newElement;
                    } else {
                        return elem;
                    }
                }
            };
        }
    }

    @Override
    default Iterator<T> retainAll(@NonNull Iterable<? extends T> elements) {
        return io.vavr.collection.Collections.retainAll(this, elements);
    }

    @Override
    default Traversable<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    default <U> Iterator<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        if (isEmpty()) {
            return of(zero);
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<U>() {

                boolean isFirst = true;
                U acc = zero;

                @Override
                public boolean hasNext() {
                    return isFirst || that.hasNext();
                }

                @Override
                public U getNext() {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        acc = operation.apply(acc, that.next());
                    }
                    return acc;
                }
            };
        }
    }

    // not lazy!
    @Override
    default <U> Iterator<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        if (isEmpty()) {
            return of(zero);
        } else {
            return io.vavr.collection.Collections.scanRight(this, zero, operation, Function.identity());
        }
    }

    @Override
    default Iterator<Seq<T>> slideBy(@NonNull Function<? super T, ?> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        if (!hasNext()) {
            return empty();
        } else {
            final CachedIterator<T> source = new CachedIterator<>(this);
            return new AbstractIterator<Seq<T>>() {
                private Stream<T> next = null;

                @Override
                public boolean hasNext() {
                    if (next == null && source.hasNext()) {
                        final Object key = classifier.apply(source.touch());
                        final java.util.List<T> acc = new ArrayList<>();
                        acc.add(source.next());
                        while (source.hasNext() && key.equals(classifier.apply(source.touch()))) {
                            acc.add(source.getNext());
                        }
                        next = Stream.ofAll(acc);
                    }
                    return next != null;
                }

                @Override
                public Stream<T> getNext() {
                    final Stream<T> result = next;
                    next = null;
                    return result;
                }
            };
        }
    }

    @Override
    default Iterator<Seq<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Iterator<Seq<T>> sliding(int size, int step) {
        return new GroupedIterator<>(this, size, step);
    }
    
    @Override
    default Tuple2<Iterator<T>, Iterator<T>> span(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        } else {
            final Stream<T> that = Stream.ofAll(this);
            return Tuple.of(that.iterator().takeWhile(predicate), that.iterator().dropWhile(predicate));
        }
    }


    @Override
    default String stringPrefix() {
        return "Iterator";
    }

    @Override
    default Iterator<T> tail() {
        if (!hasNext()) {
            throw new UnsupportedOperationException();
        } else {
            next(); // remove first element
            return this;
        }
    }

    @Override
    default Option<Iterator<T>> tailOption() {
        if (hasNext()) {
            next();
            return Option.some(this);
        } else {
            return Option.none();
        }
    }

    /**
     * Take the first n elements from this iterator.
     *
     * @param n A number
     * @return The empty iterator, if {@code n <= 0} or this is empty, otherwise a new iterator without the first n elements.
     */
    @Override
    default Iterator<T> take(int n) {
        if (n <= 0 || !hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                long count = n;

                @Override
                public boolean hasNext() {
                    return count > 0 && that.hasNext();
                }

                @Override
                public T getNext() {
                    count--;
                    return that.next();
                }
            };
        }
    }

    @Override
    default Iterator<T> takeRight(int n) {
        if (n <= 0) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {
                private io.vavr.collection.Queue<T> queue = io.vavr.collection.Queue.empty();

                @Override
                public boolean hasNext() {
                    while (that.hasNext()) {
                        queue = queue.enqueue(that.next());
                        if (queue.length() > n) {
                            queue = queue.dequeue()._2();
                        }
                    }
                    return !queue.isEmpty();
                }

                @Override
                public T getNext() {
                    final Tuple2<T, io.vavr.collection.Queue<T>> t = queue.dequeue();
                    queue = t._2();
                    return t._1();
                }
            };
        }
    }

    @Override
    default Iterator<T> takeUntil(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    default Iterator<T> takeWhile(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                private T next;
                private boolean cached = false;
                private boolean finished = false;

                @Override
                public boolean hasNext() {
                    if (cached) {
                        return true;
                    } else if (finished) {
                        return false;
                    } else if (that.hasNext()) {
                        next = that.next();
                        if (predicate.test(next)) {
                            cached = true;
                            return true;
                        }
                    }
                    finished = true;
                    return false;
                }

                @Override
                public T getNext() {
                    cached = false;
                    return next;
                }
            };
        }
    }
}

interface IteratorModule {

    /**
     * Creates two new iterators that both iterates over the same elements as
     * this iterator and in the same order. The duplicate iterators are
     * considered equal if they are positioned at the same element.
     * <p>
     * Given that most methods on iterators will make the original iterator
     * unfit for further use, this method provides a reliable way of calling
     * multiple such methods on an iterator.
     *
     * @return a pair of iterators
     */
    static <T> Tuple2<Iterator<T>, Iterator<T>> duplicate(Iterator<T> iterator) {
        final java.util.Queue<T> gap = new java.util.LinkedList<>();
        final AtomicReference<Iterator<T>> ahead = new AtomicReference<>();
        class Partner implements Iterator<T> {

            @Override
            public boolean hasNext() {
                return (this != ahead.get() && !gap.isEmpty()) || iterator.hasNext();
            }

            @Override
            public T next() {
                if (gap.isEmpty()) {
                    ahead.set(this);
                }
                if (this == ahead.get()) {
                    final T element = iterator.next();
                    gap.add(element);
                    return element;
                } else {
                    return gap.poll();
                }
            }
        }
        return Tuple.of(new Partner(), new Partner());
    }

    // inspired by Scala's ConcatIterator
    final class ConcatIterator<T> extends AbstractIterator<T> {

        private static class Cell<T> {

            Iterator<T> it;
            Cell<T> next;

            static <T> Cell<T> of(Iterator<T> it) {
                Cell<T> cell = new Cell<>();
                cell.it = it;
                return cell;
            }

            Cell<T> append(Iterator<T> it) {
                Cell<T> cell = of(it);
                next = cell;
                return cell;
            }
        }

        private Iterator<T> curr;
        private Cell<T> tail;
        private Cell<T> last;
        private boolean hasNextCalculated;

        void append(java.util.Iterator<? extends T> that) {
            final Iterator<T> it = Iterator.ofAll(that);
            if (tail == null) {
                tail = last = Cell.of(it);
            } else {
                last = last.append(it);
            }
        }

        @Override
        public Iterator<T> concat(java.util.Iterator<? extends T> that) {
            append(that);
            return this;
        }

        @Override
        public boolean hasNext() {
            if (hasNextCalculated) {
                return curr != null;
            }
            hasNextCalculated = true;
            while(true) {
                if (curr != null) {
                    if (curr.hasNext()) {
                        return true;
                    } else {
                        curr = null;
                    }
                }
                if (tail == null) {
                    return false;
                }
                curr = tail.it;
                tail = tail.next;
                while (curr instanceof ConcatIterator) {
                    ConcatIterator<T> it = (ConcatIterator<T>) curr;
                    curr = it.curr;
                    it.last.next = tail;
                    tail = it.tail;
                }
            }
        }

        @Override
        public T getNext() {
            hasNextCalculated = false;
            return curr.next();
        }
    }

    final class DistinctIterator<T, U> extends AbstractIterator<T> {

        private final Iterator<? extends T> that;
        private io.vavr.collection.Set<U> known;
        private final Function<? super T, ? extends U> keyExtractor;
        private boolean nextDefined = false;
        private T next;

        DistinctIterator(Iterator<? extends T> that, Set<U> set, Function<? super T, ? extends U> keyExtractor) {
            this.that = that;
            this.known = set;
            this.keyExtractor = keyExtractor;
        }

        @Override
        public boolean hasNext() {
            return nextDefined || searchNext();
        }

        private boolean searchNext() {
            while (that.hasNext()) {
                final T elem = that.next();
                final U key = keyExtractor.apply(elem);
                if (!known.contains(key)) {
                    known = known.add(key);
                    nextDefined = true;
                    next = elem;
                    return true;
                }
            }
            return false;
        }

        @Override
        public T getNext() {
            final T result = next;
            nextDefined = false;
            next = null;
            return result;
        }
    }

    final class EmptyIterator implements Iterator<Object> {

        static final EmptyIterator INSTANCE = new EmptyIterator();

        @Override
        public boolean hasNext() { return false; }

        @Override
        public Object next() { throw new NoSuchElementException(stringPrefix() + ".next()"); }

        @Override
        public String stringPrefix() {
            return "EmptyIterator";
        }

        @Override
        public String toString() {
            return stringPrefix() + "()";
        }
    }

    final class GroupedIterator<T> implements Iterator<Seq<T>> {

        private final Iterator<T> that;
        private final int size;
        private final int step;
        private final int gap;
        private final int preserve;

        private Object[] buffer;

        GroupedIterator(Iterator<T> that, int size, int step) {
            if (size < 1 || step < 1) {
                throw new IllegalArgumentException("size (" + size + ") and step (" + step + ") must both be positive");
            }
            this.that = that;
            this.size = size;
            this.step = step;
            this.gap = Math.max(step - size, 0);
            this.preserve = Math.max(size - step, 0);
            this.buffer = take(that, new Object[size], 0, size);
        }

        @Override
        public boolean hasNext() {
            return buffer.length > 0;
        }

        @Override
        public Seq<T> next() {
            if (buffer.length == 0) {
                throw new NoSuchElementException();
            }
            final Object[] result = buffer;
            if (that.hasNext()) {
                buffer = new Object[size];
                if (preserve > 0) {
                    System.arraycopy(result, step, buffer, 0, preserve);
                }
                if (gap > 0) {
                    drop(that, gap);
                    buffer = take(that, buffer, preserve, size);
                } else {
                    buffer = take(that, buffer, preserve, step);
                }
            } else {
                buffer = new Object[0];
            }
            return Array.wrap(result);
        }

        private static void drop(Iterator<?> source, int count) {
            for (int i = 0; i < count && source.hasNext(); i++) {
                source.next();
            }
        }

        private static Object[] take(Iterator<?> source, Object[] target, int offset, int count) {
            int i = offset;
            while (i < count + offset && source.hasNext()) {
                target[i] = source.next();
                i++;
            }
            if (i < target.length) {
                final Object[] result = new Object[i];
                System.arraycopy(target, 0, result, 0, i);
                return result;
            } else {
                return target;
            }
        }
    }

    final class CachedIterator<T> extends AbstractIterator<T> {

        private final Iterator<T> that;

        private T next;
        private boolean cached = false;

        CachedIterator(Iterator<T> that) {
            this.that = that;
        }

        @Override
        public boolean hasNext() {
            return cached || that.hasNext();
        }

        @Override
        public T getNext() {
            if (cached) {
                T result = next;
                next = null;
                cached = false;
                return result;
            } else {
                return that.next();
            }
        }

        T touch() {
            next = next();
            cached = true;
            return next;
        }
    }

    final class BigDecimalHelper {

        private static final Lazy<BigDecimal> INFINITY_DISTANCE = Lazy.of(() -> {
            final BigDecimal two = BigDecimal.valueOf(2);
            final BigDecimal supremum = BigDecimal.valueOf(Math.nextDown(Double.POSITIVE_INFINITY));
            BigDecimal lowerBound = supremum;
            BigDecimal upperBound = two.pow(Double.MAX_EXPONENT + 1);
            while (true) {
                final BigDecimal magicValue = lowerBound.add(upperBound).divide(two, HALF_UP);
                if (Double.isInfinite(magicValue.doubleValue())) {
                    if (areEqual(magicValue, upperBound)) {
                        return magicValue.subtract(supremum);
                    }
                    upperBound = magicValue;
                } else {
                    lowerBound = magicValue;
                }
            }
        });

        /* scale-independent equality */
        static boolean areEqual(BigDecimal from, BigDecimal toExclusive) {
            return from.compareTo(toExclusive) == 0;
        }

        /* parse infinite values also */
        static BigDecimal asDecimal(double number) {
            if (number == NEGATIVE_INFINITY) {
                final BigDecimal result = BigDecimal.valueOf(Math.nextUp(NEGATIVE_INFINITY));
                return result.subtract(INFINITY_DISTANCE.get());
            } else if (number == POSITIVE_INFINITY) {
                final BigDecimal result = BigDecimal.valueOf(Math.nextDown(POSITIVE_INFINITY));
                return result.add(INFINITY_DISTANCE.get());
            } else {
                return BigDecimal.valueOf(number);
            }
        }
    }
}
