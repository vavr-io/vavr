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
import io.vavr.control.Option;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import org.jspecify.annotations.NonNull;

import static io.vavr.collection.JavaConverters.ChangePolicy.IMMUTABLE;
import static io.vavr.collection.JavaConverters.ChangePolicy.MUTABLE;
import static io.vavr.collection.JavaConverters.ListView;

/**
 * An immutable {@code Queue} stores elements allowing a first-in-first-out (FIFO) retrieval.
 *
 * <p>Queue API:
 *
 * <ul>
 *   <li>{@link #dequeue()}
 *   <li>{@link #dequeueOption()}
 *   <li>{@link #enqueue(Object)}
 *   <li>{@link #enqueue(Object[])}
 *   <li>{@link #enqueueAll(Iterable)}
 *   <li>{@link #peek()}
 *   <li>{@link #peekOption()}
 * </ul>
 *
 * A Queue internally consists of a front List containing the front elements of the Queue in the
 * correct order and a rear List containing the rear elements of the Queue in reverse order.
 *
 * <p>When the front list is empty, front and rear are swapped and rear is reversed. This implies
 * the following queue invariant: {@code front.isEmpty() => rear.isEmpty()}.
 *
 * <p>See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 42 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the Queue
 * @author Daniel Dietrich
 */
public final class Queue<T> extends AbstractQueue<T, Queue<T>> implements LinearSeq<T> {

  private static final long serialVersionUID = 1L;

  private static final Queue<?> EMPTY =
      new Queue<>(io.vavr.collection.List.empty(), io.vavr.collection.List.empty());

  private final io.vavr.collection.List<T> front;
  private final io.vavr.collection.List<T> rear;

  /**
   * Creates a Queue consisting of a front List and a rear List.
   *
   * <p>For a {@code Queue(front, rear)} the following invariant holds: {@code Queue is empty <=>
   * front is empty}. In other words: If the Queue is not empty, the front List contains at least
   * one element.
   *
   * @param front A List of front elements, in correct order.
   * @param rear A List of rear elements, in reverse order.
   */
  private Queue(io.vavr.collection.List<T> front, io.vavr.collection.List<T> rear) {
    final boolean frontIsEmpty = front.isEmpty();
    this.front = frontIsEmpty ? rear.reverse() : front;
    this.rear = frontIsEmpty ? front : rear;
  }

  /**
   * Returns a {@link java.util.stream.Collector} which may be used in conjunction with {@link
   * java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link Queue} .
   *
   * @param <T> Component type of the Queue.
   * @return A io.vavr.collection.Queue Collector.
   */
  public static <T> Collector<T, ArrayList<T>, Queue<T>> collector() {
    final Supplier<ArrayList<T>> supplier = ArrayList::new;
    final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
    final BinaryOperator<ArrayList<T>> combiner =
        (left, right) -> {
          left.addAll(right);
          return left;
        };
    final Function<ArrayList<T>, Queue<T>> finisher = Queue::ofAll;
    return Collector.of(supplier, accumulator, combiner, finisher);
  }

  /**
   * Returns the empty Queue.
   *
   * @param <T> Component type
   * @return The empty Queue.
   */
  @SuppressWarnings("unchecked")
  public static <T> Queue<T> empty() {
    return (Queue<T>) EMPTY;
  }

  /**
   * Narrows a {@code Queue<? extends T>} to {@code Queue<T>} via a type-safe cast. Safe here
   * because the queue is immutable and no elements can be added that would violate the type
   * (covariance)
   *
   * @param queue the queue to narrow
   * @param <T> the target element type
   * @return the same queue viewed as {@code Queue<T>}
   */
  @SuppressWarnings("unchecked")
  public static <T> Queue<T> narrow(Queue<? extends T> queue) {
    return (Queue<T>) queue;
  }

  /**
   * Returns a singleton {@code Queue}, i.e. a {@code Queue} of one element.
   *
   * @param element An element.
   * @param <T> The component type
   * @return A new Queue instance containing the given element
   */
  public static <T> Queue<T> of(T element) {
    return ofAll(io.vavr.collection.List.of(element));
  }

  /**
   * Creates a Queue of the given elements.
   *
   * @param <T> Component type of the Queue.
   * @param elements Zero or more elements.
   * @return A queue containing the given elements in the same order.
   * @throws NullPointerException if {@code elements} is null
   */
  @SuppressWarnings("varargs")
  @SafeVarargs
  public static <T> Queue<T> of(T @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.of(elements));
  }

  /**
   * Creates a Queue of the given elements.
   *
   * @param <T> Component type of the Queue.
   * @param elements An Iterable of elements.
   * @return A queue containing the given elements in the same order.
   * @throws NullPointerException if {@code elements} is null
   */
  @SuppressWarnings("unchecked")
  public static <T> Queue<T> ofAll(@NonNull Iterable<? extends T> elements) {
    Objects.requireNonNull(elements, "elements is null");
    if (elements instanceof Queue) {
      return (Queue<T>) elements;
    } else if (elements instanceof ListView
        && ((ListView<T, ?>) elements).getDelegate() instanceof Queue) {
      return (Queue<T>) ((ListView<T, ?>) elements).getDelegate();
    } else if (!elements.iterator().hasNext()) {
      return empty();
    } else if (elements instanceof io.vavr.collection.List) {
      return new Queue<>((io.vavr.collection.List<T>) elements, io.vavr.collection.List.empty());
    } else {
      return new Queue<>(io.vavr.collection.List.ofAll(elements), io.vavr.collection.List.empty());
    }
  }

  /**
   * Creates a Queue that contains the elements of the given {@link java.util.stream.Stream}.
   *
   * @param javaStream A {@link java.util.stream.Stream}
   * @param <T> Component type of the Stream.
   * @return A Queue containing the given elements in the same order.
   */
  public static <T> Queue<T> ofAll(java.util.stream.@NonNull Stream<? extends T> javaStream) {
    Objects.requireNonNull(javaStream, "javaStream is null");
    return new Queue<>(io.vavr.collection.List.ofAll(javaStream), io.vavr.collection.List.empty());
  }

  /**
   * Creates a Queue from boolean values.
   *
   * @param elements boolean values
   * @return A new Queue of Boolean values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Boolean> ofAll(boolean @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from byte values.
   *
   * @param elements byte values
   * @return A new Queue of Byte values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Byte> ofAll(byte @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from char values.
   *
   * @param elements char values
   * @return A new Queue of Character values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Character> ofAll(char @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from double values.
   *
   * @param elements double values
   * @return A new Queue of Double values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Double> ofAll(double @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from float values.
   *
   * @param elements float values
   * @return A new Queue of Float values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Float> ofAll(float @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from int values.
   *
   * @param elements int values
   * @return A new Queue of Integer values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Integer> ofAll(int @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from long values.
   *
   * @param elements long values
   * @return A new Queue of Long values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Long> ofAll(long @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Creates a Queue from short values.
   *
   * @param elements short values
   * @return A new Queue of Short values
   * @throws NullPointerException if elements is null
   */
  public static Queue<Short> ofAll(short @NonNull ... elements) {
    Objects.requireNonNull(elements, "elements is null");
    return ofAll(io.vavr.collection.List.ofAll(elements));
  }

  /**
   * Returns a Queue containing {@code n} values of a given Function {@code f} over a range of
   * integer values from 0 to {@code n - 1}.
   *
   * @param <T> Component type of the Queue
   * @param n The number of elements in the Queue
   * @param f The Function computing element values
   * @return A Queue consisting of elements {@code f(0),f(1), ..., f(n - 1)}
   * @throws NullPointerException if {@code f} is null
   */
  public static <T> Queue<T> tabulate(int n, @NonNull Function<? super Integer, ? extends T> f) {
    Objects.requireNonNull(f, "f is null");
    return io.vavr.collection.Collections.tabulate(n, f, empty(), Queue::of);
  }

  /**
   * Returns a Queue containing {@code n} values supplied by a given Supplier {@code s}.
   *
   * @param <T> Component type of the Queue
   * @param n The number of elements in the Queue
   * @param s The Supplier computing element values
   * @return An Queue of size {@code n}, where each element contains the result supplied by {@code
   *     s}.
   * @throws NullPointerException if {@code s} is null
   */
  public static <T> Queue<T> fill(int n, @NonNull Supplier<? extends T> s) {
    Objects.requireNonNull(s, "s is null");
    return io.vavr.collection.Collections.fill(n, s, empty(), Queue::of);
  }

  /**
   * Returns a Queue containing {@code n} times the given {@code element}
   *
   * @param <T> Component type of the Queue
   * @param n The number of elements in the Queue
   * @param element The element
   * @return An Queue of size {@code n}, where each element is the given {@code element}.
   */
  public static <T> Queue<T> fill(int n, T element) {
    return io.vavr.collection.Collections.fillObject(n, element, empty(), Queue::of);
  }

  /**
   * Creates a Queue of characters starting from {@code from} (inclusive) up to {@code toExclusive}
   * (exclusive).
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.range('a', 'c')  // = Queue('a', 'b')
   * Queue.range('c', 'a')  // = Queue()
   * </pre>
   *
   * @param from the first character (inclusive)
   * @param toExclusive the end character (exclusive)
   * @return a Queue over the specified character range, or empty if {@code from >= toExclusive}
   */
  public static Queue<Character> range(char from, char toExclusive) {
    return ofAll(Iterator.range(from, toExclusive));
  }

  /**
   * Creates a Queue of characters starting from {@code from} (inclusive) up to {@code toExclusive}
   * (exclusive), advancing by the specified {@code step}.
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.rangeBy('a', 'c', 1)  // = Queue('a', 'b')
   * Queue.rangeBy('a', 'd', 2)  // = Queue('a', 'c')
   * Queue.rangeBy('d', 'a', -2) // = Queue('d', 'b')
   * Queue.rangeBy('d', 'a', 2)  // = Queue()
   * </pre>
   *
   * @param from the first character (inclusive)
   * @param toExclusive the end character (exclusive)
   * @param step the increment; must not be zero
   * @return a Queue over the specified character range, or empty if the step direction does not
   *     match the direction from {@code from} to {@code toExclusive}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Character> rangeBy(char from, char toExclusive, int step) {
    return ofAll(Iterator.rangeBy(from, toExclusive, step));
  }

  /**
   * Creates a Queue of double values starting from {@code from} (inclusive) up to {@code
   * toExclusive} (exclusive), advancing by the specified {@code step}.
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.rangeBy(0.0, 1.0, 0.25)   // = Queue(0.0, 0.25, 0.5, 0.75)
   * Queue.rangeBy(1.0, 0.0, -0.25)  // = Queue(1.0, 0.75, 0.5, 0.25)
   * Queue.rangeBy(0.0, 1.0, -0.25)  // = Queue()
   * </pre>
   *
   * @param from the first double value (inclusive)
   * @param toExclusive the end value (exclusive)
   * @param step the increment; must not be zero
   * @return a Queue over the specified double range, or empty if the step direction does not match
   *     the direction from {@code from} to {@code toExclusive}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Double> rangeBy(double from, double toExclusive, double step) {
    return ofAll(Iterator.rangeBy(from, toExclusive, step));
  }

  /**
   * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive -
   * 1}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.range(0, 0)  // = Queue()
   * Queue.range(2, 0)  // = Queue()
   * Queue.range(-2, 2) // = Queue(-2, -1, 0, 1)
   * }</pre>
   *
   * @param from the first number
   * @param toExclusive the last number + 1
   * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
   */
  public static Queue<Integer> range(int from, int toExclusive) {
    return ofAll(Iterator.range(from, toExclusive));
  }

  /**
   * Creates a Queue of int numbers starting from {@code from}, extending to {@code toExclusive -
   * 1}, with {@code step}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeBy(1, 3, 1)  // = Queue(1, 2)
   * Queue.rangeBy(1, 4, 2)  // = Queue(1, 3)
   * Queue.rangeBy(4, 1, -2) // = Queue(4, 2)
   * Queue.rangeBy(4, 1, 2)  // = Queue()
   * }</pre>
   *
   * @param from the first number
   * @param toExclusive the last number + 1
   * @param step the step
   * @return a range of long values as specified or {@code Nil} if<br>
   *     {@code from >= toInclusive} and {@code step > 0} or<br>
   *     {@code from <= toInclusive} and {@code step < 0}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Integer> rangeBy(int from, int toExclusive, int step) {
    return ofAll(Iterator.rangeBy(from, toExclusive, step));
  }

  /**
   * Creates a Queue of long numbers starting from {@code from}, extending to {@code toExclusive -
   * 1}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.range(0L, 0L)  // = Queue()
   * Queue.range(2L, 0L)  // = Queue()
   * Queue.range(-2L, 2L) // = Queue(-2L, -1L, 0L, 1L)
   * }</pre>
   *
   * @param from the first number
   * @param toExclusive the last number + 1
   * @return a range of long values as specified or {@code Nil} if {@code from >= toExclusive}
   */
  public static Queue<Long> range(long from, long toExclusive) {
    return ofAll(Iterator.range(from, toExclusive));
  }

  /**
   * Creates a Queue of long numbers starting from {@code from}, extending to {@code toExclusive -
   * 1}, with {@code step}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeBy(1L, 3L, 1L)  // = Queue(1L, 2L)
   * Queue.rangeBy(1L, 4L, 2L)  // = Queue(1L, 3L)
   * Queue.rangeBy(4L, 1L, -2L) // = Queue(4L, 2L)
   * Queue.rangeBy(4L, 1L, 2L)  // = Queue()
   * }</pre>
   *
   * @param from the first number
   * @param toExclusive the last number + 1
   * @param step the step
   * @return a range of long values as specified or {@code Nil} if<br>
   *     {@code from >= toInclusive} and {@code step > 0} or<br>
   *     {@code from <= toInclusive} and {@code step < 0}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Long> rangeBy(long from, long toExclusive, long step) {
    return ofAll(Iterator.rangeBy(from, toExclusive, step));
  }

  /**
   * Creates a Queue of characters starting from {@code from} (inclusive) up to {@code toInclusive}
   * (inclusive).
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.rangeClosed('a', 'c')  // = Queue('a', 'b', 'c')
   * Queue.rangeClosed('c', 'a')  // = Queue()
   * </pre>
   *
   * @param from the first character (inclusive)
   * @param toInclusive the last character (inclusive)
   * @return a Queue over the specified character range, or empty if {@code from > toInclusive}
   */
  public static Queue<Character> rangeClosed(char from, char toInclusive) {
    return ofAll(Iterator.rangeClosed(from, toInclusive));
  }

  /**
   * Creates a Queue of characters starting from {@code from} (inclusive) up to {@code toInclusive}
   * (inclusive), advancing by the specified {@code step}.
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.rangeClosedBy('a', 'c', 1)   // = Queue('a', 'b', 'c')
   * Queue.rangeClosedBy('a', 'd', 2)   // = Queue('a', 'c')
   * Queue.rangeClosedBy('d', 'a', -2)  // = Queue('d', 'b')
   * Queue.rangeClosedBy('d', 'a', 2)   // = Queue()
   * </pre>
   *
   * @param from the first character (inclusive)
   * @param toInclusive the last character (inclusive)
   * @param step the increment; must not be zero
   * @return a Queue over the specified character range, or empty if the step direction does not
   *     match the direction from {@code from} to {@code toInclusive}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Character> rangeClosedBy(char from, char toInclusive, int step) {
    return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
  }

  /**
   * Creates a Queue of double values starting from {@code from} (inclusive) up to {@code
   * toInclusive} (inclusive), advancing by the specified {@code step}.
   *
   * <p>Examples:
   *
   * <pre>
   * Queue.rangeClosedBy(0.0, 1.0, 0.25)   // = Queue(0.0, 0.25, 0.5, 0.75, 1.0)
   * Queue.rangeClosedBy(1.0, 0.0, -0.25)  // = Queue(1.0, 0.75, 0.5, 0.25, 0.0)
   * Queue.rangeClosedBy(0.0, 1.0, -0.25)  // = Queue()
   * </pre>
   *
   * @param from the first double value (inclusive)
   * @param toInclusive the last value (inclusive)
   * @param step the increment; must not be zero
   * @return a Queue over the specified double range, or empty if the step direction does not match
   *     the direction from {@code from} to {@code toInclusive}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Double> rangeClosedBy(double from, double toInclusive, double step) {
    return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
  }

  /**
   * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeClosed(0, 0)  // = Queue(0)
   * Queue.rangeClosed(2, 0)  // = Queue()
   * Queue.rangeClosed(-2, 2) // = Queue(-2, -1, 0, 1, 2)
   * }</pre>
   *
   * @param from the first number
   * @param toInclusive the last number
   * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
   */
  public static Queue<Integer> rangeClosed(int from, int toInclusive) {
    return ofAll(Iterator.rangeClosed(from, toInclusive));
  }

  /**
   * Creates a Queue of int numbers starting from {@code from}, extending to {@code toInclusive},
   * with {@code step}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeClosedBy(1, 3, 1)  // = Queue(1, 2, 3)
   * Queue.rangeClosedBy(1, 4, 2)  // = Queue(1, 3)
   * Queue.rangeClosedBy(4, 1, -2) // = Queue(4, 2)
   * Queue.rangeClosedBy(4, 1, 2)  // = Queue()
   * }</pre>
   *
   * @param from the first number
   * @param toInclusive the last number
   * @param step the step
   * @return a range of int values as specified or {@code Nil} if<br>
   *     {@code from > toInclusive} and {@code step > 0} or<br>
   *     {@code from < toInclusive} and {@code step < 0}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Integer> rangeClosedBy(int from, int toInclusive, int step) {
    return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
  }

  /**
   * Creates a Queue of long numbers starting from {@code from}, extending to {@code toInclusive}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeClosed(0L, 0L)  // = Queue(0L)
   * Queue.rangeClosed(2L, 0L)  // = Queue()
   * Queue.rangeClosed(-2L, 2L) // = Queue(-2L, -1L, 0L, 1L, 2L)
   * }</pre>
   *
   * @param from the first number
   * @param toInclusive the last number
   * @return a range of long values as specified or {@code Nil} if {@code from > toInclusive}
   */
  public static Queue<Long> rangeClosed(long from, long toInclusive) {
    return ofAll(Iterator.rangeClosed(from, toInclusive));
  }

  /**
   * Transposes the rows and columns of a {@link Queue} matrix.
   *
   * @param <T> matrix element type
   * @param matrix to be transposed.
   * @return a transposed {@link Queue} matrix.
   * @throws IllegalArgumentException if the row lengths of {@code matrix} differ.
   *     <p>ex: {@code Queue.transpose(Queue(Queue(1,2,3), Queue(4,5,6))) → Queue(Queue(1,4),
   *     Queue(2,5), Queue(3,6)) }
   */
  public static <T> Queue<Queue<T>> transpose(@NonNull Queue<Queue<T>> matrix) {
    return io.vavr.collection.Collections.transpose(matrix, Queue::ofAll, Queue::of);
  }

  /**
   * Creates a Queue of long numbers starting from {@code from}, extending to {@code toInclusive},
   * with {@code step}.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * Queue.rangeClosedBy(1L, 3L, 1L)  // = Queue(1L, 2L, 3L)
   * Queue.rangeClosedBy(1L, 4L, 2L)  // = Queue(1L, 3L)
   * Queue.rangeClosedBy(4L, 1L, -2L) // = Queue(4L, 2L)
   * Queue.rangeClosedBy(4L, 1L, 2L)  // = Queue()
   * }</pre>
   *
   * @param from the first number
   * @param toInclusive the last number
   * @param step the step
   * @return a range of int values as specified or {@code Nil} if<br>
   *     {@code from > toInclusive} and {@code step > 0} or<br>
   *     {@code from < toInclusive} and {@code step < 0}
   * @throws IllegalArgumentException if {@code step} is zero
   */
  public static Queue<Long> rangeClosedBy(long from, long toInclusive, long step) {
    return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
  }

  /**
   * Creates a Queue from a seed value and a function. The function takes the seed at first. The
   * function should return {@code None} when it's done generating the Queue, otherwise {@code Some}
   * {@code Tuple} of the element for the next call and the value to add to the resulting Queue.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Queue.unfoldRight(10, x -&gt; x == 0
   *             ? Option.none()
   *             : Option.of(new Tuple2&lt;&gt;(x, x-1)));
   * // Queue(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
   * }</pre>
   *
   * @param <T> type of seeds
   * @param <U> type of unfolded values
   * @param seed the start value for the iteration
   * @param f the function to get the next step of the iteration
   * @return a Queue with the values built up by the iteration
   * @throws NullPointerException if {@code f} is null
   */
  public static <T, U> Queue<U> unfoldRight(
      T seed, @NonNull Function<? super T, Option<Tuple2<? extends U, ? extends T>>> f) {
    return Iterator.unfoldRight(seed, f).toQueue();
  }

  /**
   * Creates a Queue from a seed value and a function. The function takes the seed at first. The
   * function should return {@code None} when it's done generating the Queue, otherwise {@code Some}
   * {@code Tuple} of the value to add to the resulting Queue and the element for the next call.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Queue.unfoldLeft(10, x -&gt; x == 0
   *             ? Option.none()
   *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
   * // Queue(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
   * }</pre>
   *
   * @param <T> type of seeds
   * @param <U> type of unfolded values
   * @param seed the start value for the iteration
   * @param f the function to get the next step of the iteration
   * @return a Queue with the values built up by the iteration
   * @throws NullPointerException if {@code f} is null
   */
  public static <T, U> Queue<U> unfoldLeft(
      T seed, @NonNull Function<? super T, Option<Tuple2<? extends T, ? extends U>>> f) {
    return Iterator.unfoldLeft(seed, f).toQueue();
  }

  /**
   * Creates a Queue from a seed value and a function. The function takes the seed at first. The
   * function should return {@code None} when it's done generating the Queue, otherwise {@code Some}
   * {@code Tuple} of the value to add to the resulting Queue and the element for the next call.
   *
   * <p>Example:
   *
   * <pre>{@code
   * Queue.unfold(10, x -&gt; x == 0
   *             ? Option.none()
   *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
   * // Queue(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
   * }</pre>
   *
   * @param <T> type of seeds and unfolded values
   * @param seed the start value for the iteration
   * @param f the function to get the next step of the iteration
   * @return a Queue with the values built up by the iteration
   * @throws NullPointerException if {@code f} is null
   */
  public static <T> Queue<T> unfold(
      T seed, @NonNull Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
    return Iterator.unfold(seed, f).toQueue();
  }

  /**
   * Enqueues a new element.
   *
   * @param element The new element
   * @return a new {@code Queue} instance, containing the new element
   */
  @Override
  public Queue<T> enqueue(T element) {
    return new Queue<>(front, rear.prepend(element));
  }

  /**
   * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
   * the first which will be retrieved.
   *
   * @param elements An Iterable of elements, may be empty
   * @return a new {@code Queue} instance, containing the new elements
   * @throws NullPointerException if elements is null
   */
  @SuppressWarnings("unchecked")
  public Queue<T> enqueueAll(@NonNull Iterable<? extends T> elements) {
    Objects.requireNonNull(elements, "elements is null");
    if (isEmpty() && elements instanceof Queue) {
      return (Queue<T>) elements;
    } else {
      return io.vavr.collection.List.ofAll(elements).foldLeft(this, Queue::enqueue);
    }
  }

  // -- Adjusted return types of Seq methods

  @Override
  public Queue<T> append(T element) {
    return enqueue(element);
  }

  @Override
  public Queue<T> appendAll(@NonNull Iterable<? extends T> elements) {
    return enqueueAll(elements);
  }

  @Override
  public java.util.List<T> asJava() {
    return JavaConverters.asJava(this, IMMUTABLE);
  }

  @Override
  public Queue<T> asJava(@NonNull Consumer<? super java.util.List<T>> action) {
    return Collections.asJava(this, action, IMMUTABLE);
  }

  @Override
  public java.util.List<T> asJavaMutable() {
    return JavaConverters.asJava(this, MUTABLE);
  }

  @Override
  public Queue<T> asJavaMutable(@NonNull Consumer<? super java.util.List<T>> action) {
    return Collections.asJava(this, action, MUTABLE);
  }

  @Override
  public <R> Queue<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction) {
    return ofAll(iterator().<R>collect(partialFunction));
  }

  @Override
  public Queue<Queue<T>> combinations() {
    return ofAll(toList().combinations().map(Queue::ofAll));
  }

  @Override
  public Queue<Queue<T>> combinations(int k) {
    return ofAll(toList().combinations(k).map(Queue::ofAll));
  }

  @Override
  public Iterator<Queue<T>> crossProduct(int power) {
    return io.vavr.collection.Collections.crossProduct(empty(), this, power);
  }

  @Override
  public Queue<T> distinct() {
    return ofAll(toList().distinct());
  }

  @Override
  public Queue<T> distinctBy(@NonNull Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "comparator is null");
    return ofAll(toList().distinctBy(comparator));
  }

  @Override
  public <U> Queue<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor) {
    Objects.requireNonNull(keyExtractor, "keyExtractor is null");
    return ofAll(toList().distinctBy(keyExtractor));
  }

  @Override
  public Queue<T> distinctByKeepLast(@NonNull Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "comparator is null");
    return ofAll(toList().distinctByKeepLast(comparator));
  }

  @Override
  public <U> Queue<T> distinctByKeepLast(@NonNull Function<? super T, ? extends U> keyExtractor) {
    Objects.requireNonNull(keyExtractor, "keyExtractor is null");
    return ofAll(toList().distinctByKeepLast(keyExtractor));
  }

  @Override
  public Queue<T> drop(int n) {
    if (n <= 0) {
      return this;
    }
    if (n >= length()) {
      return empty();
    }
    return new Queue<>(front.drop(n), rear.dropRight(n - front.length()));
  }

  @Override
  public Queue<T> dropWhile(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    final List<T> dropped = toList().dropWhile(predicate);
    return ofAll(dropped.length() == length() ? this : dropped);
  }

  @Override
  public Queue<T> dropRight(int n) {
    if (n <= 0) {
      return this;
    }
    if (n >= length()) {
      return empty();
    }
    return new Queue<>(front.dropRight(n - rear.length()), rear.drop(n));
  }

  @Override
  public Queue<T> dropRightUntil(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return reverse().dropUntil(predicate).reverse();
  }

  @Override
  public Queue<T> dropRightWhile(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return dropRightUntil(predicate.negate());
  }

  @Override
  public Queue<T> filter(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    final io.vavr.collection.List<T> filtered = toList().filter(predicate);

    if (filtered.isEmpty()) {
      return empty();
    } else if (filtered.length() == length()) {
      return this;
    } else {
      return ofAll(filtered);
    }
  }

  @Override
  public <U> Queue<U> flatMap(
      @NonNull Function<? super T, ? extends Iterable<? extends U>> mapper) {
    Objects.requireNonNull(mapper, "mapper is null");
    if (isEmpty()) {
      return empty();
    } else {
      return new Queue<>(front.flatMap(mapper), rear.flatMap(mapper));
    }
  }

  @Override
  public T get(int index) {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException("get(" + index + ") on empty Queue");
    }
    if (index < 0) {
      throw new IndexOutOfBoundsException("get(" + index + ")");
    }
    final int length = front.length();
    if (index < length) {
      return front.get(index);
    } else {
      final int rearIndex = index - length;
      final int rearLength = rear.length();
      if (rearIndex < rearLength) {
        final int reverseRearIndex = rearLength - rearIndex - 1;
        return rear.get(reverseRearIndex);
      } else {
        throw new IndexOutOfBoundsException("get(" + index + ") on Queue of length " + length());
      }
    }
  }

  @Override
  public <C> Map<C, Queue<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier) {
    return io.vavr.collection.Collections.groupBy(this, classifier, Queue::ofAll);
  }

  @Override
  public Iterator<Queue<T>> grouped(int size) {
    return sliding(size, size);
  }

  @Override
  public boolean hasDefiniteSize() {
    return true;
  }

  @Override
  public T head() {
    if (isEmpty()) {
      throw new NoSuchElementException("head of empty " + stringPrefix());
    } else {
      return front.head();
    }
  }

  @Override
  public int indexOf(T element, int from) {
    final int frontIndex = front.indexOf(element, from);
    if (frontIndex != -1) {
      return frontIndex;
    } else {
      // we need to reverse because we search the first occurrence
      final int rearIndex = rear.reverse().indexOf(element, from - front.length());
      return (rearIndex == -1) ? -1 : rearIndex + front.length();
    }
  }

  @Override
  public Queue<T> init() {
    if (isEmpty()) {
      throw new UnsupportedOperationException("init of empty " + stringPrefix());
    } else if (rear.isEmpty()) {
      return new Queue<>(front.init(), rear);
    } else {
      return new Queue<>(front, rear.tail());
    }
  }

  @Override
  public Queue<T> insert(int index, T element) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("insert(" + index + ", element)");
    }
    final int length = front.length();
    if (index <= length) {
      return new Queue<>(front.insert(index, element), rear);
    } else {
      final int rearIndex = index - length;
      final int rearLength = rear.length();
      if (rearIndex <= rearLength) {
        final int reverseRearIndex = rearLength - rearIndex;
        return new Queue<>(front, rear.insert(reverseRearIndex, element));
      } else {
        throw new IndexOutOfBoundsException(
            "insert(" + index + ", element) on Queue of length " + length());
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Queue<T> insertAll(int index, @NonNull Iterable<? extends T> elements) {
    Objects.requireNonNull(elements, "elements is null");
    if (index < 0) {
      throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
    }
    final int length = front.length();
    if (index <= length) {
      if (isEmpty() && elements instanceof Queue) {
        return (Queue<T>) elements;
      } else {
        final io.vavr.collection.List<T> newFront = front.insertAll(index, elements);
        return (newFront == front) ? this : new Queue<>(newFront, rear);
      }
    } else {
      final int rearIndex = index - length;
      final int rearLength = rear.length();
      if (rearIndex <= rearLength) {
        final int reverseRearIndex = rearLength - rearIndex;
        final io.vavr.collection.List<T> newRear =
            rear.insertAll(reverseRearIndex, io.vavr.collection.List.ofAll(elements).reverse());
        return (newRear == rear) ? this : new Queue<>(front, newRear);
      } else {
        throw new IndexOutOfBoundsException(
            "insertAll(" + index + ", elements) on Queue of length " + length());
      }
    }
  }

  @Override
  public Queue<T> intersperse(T element) {
    if (isEmpty()) {
      return this;
    } else if (rear.isEmpty()) {
      return new Queue<>(front.intersperse(element), rear);
    } else {
      return new Queue<>(front.intersperse(element), rear.intersperse(element).append(element));
    }
  }

  /**
   * A {@code Queue} is computed synchronously.
   *
   * @return false
   */
  @Override
  public boolean isAsync() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return front.isEmpty();
  }

  /**
   * A {@code Queue} is computed eagerly.
   *
   * @return false
   */
  @Override
  public boolean isLazy() {
    return false;
  }

  @Override
  public boolean isTraversableAgain() {
    return true;
  }

  @Override
  public T last() {
    return rear.isEmpty() ? front.last() : rear.head();
  }

  @Override
  public int lastIndexOf(T element, int end) {
    return toList().lastIndexOf(element, end);
  }

  @Override
  public int length() {
    return front.length() + rear.length();
  }

  @Override
  public <U> Queue<U> map(@NonNull Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper, "mapper is null");
    return new Queue<>(front.map(mapper), rear.map(mapper));
  }

  @Override
  public <U> Queue<U> mapTo(U value) {
    return map(ignored -> value);
  }

  @Override
  public Queue<Void> mapToVoid() {
    return map(ignored -> null);
  }

  @Override
  public Queue<T> orElse(@NonNull Iterable<? extends T> other) {
    return isEmpty() ? ofAll(other) : this;
  }

  @Override
  public Queue<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier) {
    return isEmpty() ? ofAll(supplier.get()) : this;
  }

  @Override
  public Queue<T> padTo(int length, T element) {
    final int actualLength = length();
    if (length <= actualLength) {
      return this;
    } else {
      return ofAll(toList().padTo(length, element));
    }
  }

  @Override
  public Queue<T> leftPadTo(int length, T element) {
    final int actualLength = length();
    if (length <= actualLength) {
      return this;
    } else {
      return ofAll(toList().leftPadTo(length, element));
    }
  }

  @Override
  public Queue<T> patch(int from, @NonNull Iterable<? extends T> that, int replaced) {
    from = Math.max(from, 0);
    replaced = Math.max(replaced, 0);
    Queue<T> result = take(from).appendAll(that);
    from += replaced;
    result = result.appendAll(drop(from));
    return result;
  }

  @Override
  public Tuple2<Queue<T>, Queue<T>> partition(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return toList()
        .partition(predicate)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public Queue<Queue<T>> permutations() {
    return ofAll(toList().permutations().map(io.vavr.collection.List::toQueue));
  }

  @Override
  public Queue<T> prepend(T element) {
    return new Queue<>(front.prepend(element), rear);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Queue<T> prependAll(@NonNull Iterable<? extends T> elements) {
    Objects.requireNonNull(elements, "elements is null");
    if (isEmpty() && elements instanceof Queue) {
      return (Queue<T>) elements;
    } else {
      final io.vavr.collection.List<T> newFront = front.prependAll(elements);
      return (newFront == front) ? this : new Queue<>(newFront, rear);
    }
  }

  @Override
  public Queue<T> remove(T element) {
    final io.vavr.collection.List<T> removed = toList().remove(element);
    return ofAll(removed.length() == length() ? this : removed);
  }

  @Override
  public Queue<T> removeFirst(@NonNull Predicate<T> predicate) {
    final io.vavr.collection.List<T> removed = toList().removeFirst(predicate);
    return ofAll(removed.length() == length() ? this : removed);
  }

  @Override
  public Queue<T> removeLast(@NonNull Predicate<T> predicate) {
    final io.vavr.collection.List<T> removed = toList().removeLast(predicate);
    return ofAll(removed.length() == length() ? this : removed);
  }

  @Override
  public Queue<T> removeAt(int index) {
    return ofAll(toList().removeAt(index));
  }

  @Override
  public Queue<T> removeAll(T element) {
    return io.vavr.collection.Collections.removeAll(this, element);
  }

  @Override
  public Queue<T> replace(T currentElement, T newElement) {
    final io.vavr.collection.List<T> newFront = front.replace(currentElement, newElement);
    final io.vavr.collection.List<T> newRear = rear.replace(currentElement, newElement);
    return newFront.size() + newRear.size() == 0
        ? empty()
        : newFront == front && newRear == rear ? this : new Queue<>(newFront, newRear);
  }

  @Override
  public Queue<T> replaceAll(T currentElement, T newElement) {
    final io.vavr.collection.List<T> newFront = front.replaceAll(currentElement, newElement);
    final io.vavr.collection.List<T> newRear = rear.replaceAll(currentElement, newElement);
    return newFront.size() + newRear.size() == 0
        ? empty()
        : newFront == front && newRear == rear ? this : new Queue<>(newFront, newRear);
  }

  @Override
  public Queue<T> reverse() {
    return isEmpty() ? this : ofAll(toList().reverse());
  }

  @Override
  public Queue<T> rotateLeft(int n) {
    return Collections.rotateLeft(this, n);
  }

  @Override
  public Queue<T> rotateRight(int n) {
    return Collections.rotateRight(this, n);
  }

  @Override
  public Queue<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation) {
    return scanLeft(zero, operation);
  }

  @Override
  public <U> Queue<U> scanLeft(
      U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation) {
    return io.vavr.collection.Collections.scanLeft(this, zero, operation, Iterator::toQueue);
  }

  @Override
  public <U> Queue<U> scanRight(
      U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation) {
    return io.vavr.collection.Collections.scanRight(this, zero, operation, Iterator::toQueue);
  }

  @Override
  public Queue<T> shuffle() {
    return io.vavr.collection.Collections.shuffle(this, Queue::ofAll);
  }

  @Override
  public Queue<T> slice(int beginIndex, int endIndex) {
    return ofAll(toList().slice(beginIndex, endIndex));
  }

  @Override
  public Iterator<Queue<T>> slideBy(@NonNull Function<? super T, ?> classifier) {
    return iterator().slideBy(classifier).map(Queue::ofAll);
  }

  @Override
  public Iterator<Queue<T>> sliding(int size) {
    return sliding(size, 1);
  }

  @Override
  public Iterator<Queue<T>> sliding(int size, int step) {
    return iterator().sliding(size, step).map(Queue::ofAll);
  }

  @Override
  public Queue<T> sorted() {
    return ofAll(toList().sorted());
  }

  @Override
  public Queue<T> sorted(@NonNull Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "comparator is null");
    return ofAll(toList().sorted(comparator));
  }

  @Override
  public <U extends Comparable<? super U>> Queue<T> sortBy(
      @NonNull Function<? super T, ? extends U> mapper) {
    return sortBy(U::compareTo, mapper);
  }

  @Override
  public <U> Queue<T> sortBy(
      @NonNull Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
    return Collections.sortBy(this, comparator, mapper, collector());
  }

  @Override
  public Tuple2<Queue<T>, Queue<T>> span(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return toList()
        .span(predicate)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public Tuple2<Queue<T>, Queue<T>> splitAt(int n) {
    return toList()
        .splitAt(n)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public Tuple2<Queue<T>, Queue<T>> splitAt(@NonNull Predicate<? super T> predicate) {
    return toList()
        .splitAt(predicate)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public Tuple2<Queue<T>, Queue<T>> splitAtInclusive(@NonNull Predicate<? super T> predicate) {
    return toList()
        .splitAtInclusive(predicate)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public boolean startsWith(@NonNull Iterable<? extends T> that, int offset) {
    return toList().startsWith(that, offset);
  }

  @Override
  public Queue<T> subSequence(int beginIndex) {
    if (beginIndex < 0 || beginIndex > length()) {
      throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
    } else {
      return drop(beginIndex);
    }
  }

  @Override
  public Queue<T> subSequence(int beginIndex, int endIndex) {
    Collections.subSequenceRangeCheck(beginIndex, endIndex, length());
    if (beginIndex == endIndex) {
      return empty();
    } else if (beginIndex == 0 && endIndex == length()) {
      return this;
    } else {
      return ofAll(toList().subSequence(beginIndex, endIndex));
    }
  }

  @Override
  public Queue<T> tail() {
    if (isEmpty()) {
      throw new UnsupportedOperationException("tail of empty " + stringPrefix());
    } else {
      return new Queue<>(front.tail(), rear);
    }
  }

  @Override
  public Queue<T> take(int n) {
    if (n <= 0) {
      return empty();
    }
    if (n >= length()) {
      return this;
    }
    final int frontLength = front.length();
    if (n < frontLength) {
      return new Queue<>(front.take(n), io.vavr.collection.List.empty());
    } else if (n == frontLength) {
      return new Queue<>(front, io.vavr.collection.List.empty());
    } else {
      return new Queue<>(front, rear.takeRight(n - frontLength));
    }
  }

  @Override
  public Queue<T> takeUntil(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    final io.vavr.collection.List<T> taken = toList().takeUntil(predicate);
    return taken.length() == length() ? this : ofAll(taken);
  }

  @Override
  public Queue<T> takeRight(int n) {
    if (n <= 0) {
      return empty();
    }
    if (n >= length()) {
      return this;
    }
    final int rearLength = rear.length();
    if (n < rearLength) {
      return new Queue<>(rear.take(n).reverse(), io.vavr.collection.List.empty());
    } else if (n == rearLength) {
      return new Queue<>(rear.reverse(), io.vavr.collection.List.empty());
    } else {
      return new Queue<>(front.takeRight(n - rearLength), rear);
    }
  }

  @Override
  public Queue<T> takeRightUntil(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    final io.vavr.collection.List<T> taken = toList().takeRightUntil(predicate);
    return taken.length() == length() ? this : ofAll(taken);
  }

  @Override
  public Queue<T> takeRightWhile(@NonNull Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return takeRightUntil(predicate.negate());
  }

  /**
   * Transforms this {@code Queue}.
   *
   * @param f A transformation
   * @param <U> Type of transformation result
   * @return An instance of type {@code U}
   * @throws NullPointerException if {@code f} is null
   */
  public <U> U transform(Function<? super Queue<T>, ? extends U> f) {
    Objects.requireNonNull(f, "f is null");
    return f.apply(this);
  }

  @Override
  public <T1, T2> Tuple2<Queue<T1>, Queue<T2>> unzip(
      @NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
    Objects.requireNonNull(unzipper, "unzipper is null");
    return toList()
        .unzip(unzipper)
        .map(io.vavr.collection.List::toQueue, io.vavr.collection.List::toQueue);
  }

  @Override
  public <T1, T2, T3> Tuple3<Queue<T1>, Queue<T2>, Queue<T3>> unzip3(
      @NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
    Objects.requireNonNull(unzipper, "unzipper is null");
    return toList()
        .unzip3(unzipper)
        .map(
            io.vavr.collection.List::toQueue,
            io.vavr.collection.List::toQueue,
            io.vavr.collection.List::toQueue);
  }

  @Override
  public Queue<T> update(int index, T element) {
    return ofAll(toList().update(index, element));
  }

  @Override
  public Queue<T> update(int index, @NonNull Function<? super T, ? extends T> updater) {
    Objects.requireNonNull(updater, "updater is null");
    return update(index, updater.apply(get(index)));
  }

  @Override
  public <U> Queue<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that) {
    return zipWith(that, Tuple::of);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <U, R> Queue<R> zipWith(
      @NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
    Objects.requireNonNull(that, "that is null");
    Objects.requireNonNull(mapper, "mapper is null");
    return ofAll(toList().zipWith(that, mapper));
  }

  @Override
  public <U> Queue<Tuple2<T, U>> zipAll(
      @NonNull Iterable<? extends U> that, T thisElem, U thatElem) {
    Objects.requireNonNull(that, "that is null");
    return ofAll(toList().zipAll(that, thisElem, thatElem));
  }

  @Override
  public Queue<Tuple2<T, Integer>> zipWithIndex() {
    return zipWithIndex(Tuple::of);
  }

  @Override
  public <U> Queue<U> zipWithIndex(
      @NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper) {
    Objects.requireNonNull(mapper, "mapper is null");
    return ofAll(toList().zipWithIndex(mapper));
  }

  private Object readResolve() {
    return isEmpty() ? EMPTY : this;
  }

  @Override
  public String stringPrefix() {
    return "Queue";
  }

  @Override
  public boolean equals(Object o) {
    return io.vavr.collection.Collections.equals(this, o);
  }

  @Override
  public int hashCode() {
    return io.vavr.collection.Collections.hashOrdered(this);
  }
}
