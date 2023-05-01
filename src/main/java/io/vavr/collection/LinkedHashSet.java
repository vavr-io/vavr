/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;


/**
 * Implements a mutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP) and a bit-mapped trie (Vector).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which elements were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>add: O(log N) in an amortized sense, because we sometimes have to
 *     renumber the elements.</li>
 *     <li>remove: O(log N) in an amortized sense, because we sometimes have to
 *     renumber the elements.</li>
 *     <li>contains: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in
 *     the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(log N)</li>
 *     <li>getFirst(), getLast(): O(log N)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(log N) time,
 * and in O(log N) space, where N is the number of elements in the set.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other sets.
 * <p>
 * If a write operation is performed on a node, then this set creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1).
 * <p>
 * Insertion Order:
 * <p>
 * This set uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code add} and {@code remove} methods are O(1)
 * only in an amortized sense.
 * <p>
 * To support iteration, we use a Vector. The Vector has the same contents
 * as the CHAMP trie. However, its elements are stored in insertion order.
 * <p>
 * If an element is removed from the CHAMP trie that is not the first or the
 * last element of the Vector, we replace its corresponding element in
 * the Vector by a tombstone. If the element is at the start or end of the Vector,
 * we remove the element and all its neighboring tombstones from the Vector.
 * <p>
 * A tombstone can store the number of neighboring tombstones in ascending and in descending
 * direction. We use these numbers to skip tombstones when we iterate over the vector.
 * Since we only allow iteration in ascending or descending order from one of the ends of
 * the vector, we do not need to keep the number of neighbors in all tombstones up to date.
 * It is sufficient, if we update the neighbor with the lowest index and the one with the
 * highest index.
 * <p>
 * If the number of tombstones exceeds half of the size of the collection, we renumber all
 * sequence numbers, and we create a new Vector.
 * <p>
 * The immutable version of this set extends from the non-public class
 * {@code ChampBitmapIndexNode}. This design safes 16 bytes for every instance,
 * and reduces the number of redirections for finding an element in the
 * collection by 1.
 * <p>
 * References:
 * <p>
 * Portions of the code in this class has been derived from 'vavr' Vector.java.
 * <p>
 * The design of this class is inspired by 'VectorMap.scala'.
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *      </dd>
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 *      </dd>
 *      <dt>VectorMap.scala
 *      <br>The Scala library. Copyright EPFL and Lightbend, Inc. Apache License 2.0.</dt>
 *      <dd><a href="https://github.com/scala/scala/blob/28eef15f3cc46f6d3dd1884e94329d7601dc20ee/src/library/scala/collection/immutable/VectorMap.scala">github.com</a>
 *      </dd>
 * </dl>
 *
 * @param <T> the element type
 */
public final class LinkedHashSet<T>
        extends ChampBitmapIndexedNode<ChampSequencedElement<T>>
        implements Set<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final LinkedHashSet<?> EMPTY = new LinkedHashSet<>(
            ChampBitmapIndexedNode.emptyNode(), Vector.of(), 0, 0);

    /**
     * Offset of sequence numbers to vector indices.
     *
     * <pre>vector index = sequence number + offset</pre>
     */
    final int offset;
    /**
     * The size of the set.
     */
    final int size;
    /**
     * In this vector we store the elements in the order in which they were inserted.
     */
    final Vector<Object> vector;

    private LinkedHashSet(
            ChampBitmapIndexedNode<ChampSequencedElement<T>> root,
            Vector<Object> vector,
            int size, int offset) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
        this.offset = offset;
        this.vector = Objects.requireNonNull(vector);
    }

    @SuppressWarnings("unchecked")
    public static <T> LinkedHashSet<T> empty() {
        return (LinkedHashSet<T>) EMPTY;
    }


    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link LinkedHashSet}.
     *
     * @param <T> Component type of the LinkedHashSet.
     * @return A io.vavr.collection.LinkedHashSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, LinkedHashSet<T>> collector() {
        return Collections.toListAndThen(LinkedHashSet::ofAll);
    }

    /**
     * Narrows a widened {@code LinkedHashSet<? extends T>} to {@code LinkedHashSet<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param linkedHashSet A {@code LinkedHashSet}.
     * @param <T>           Component type of the {@code linkedHashSet}.
     * @return the given {@code linkedHashSet} instance as narrowed type {@code LinkedHashSet<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedHashSet<T> narrow(LinkedHashSet<? extends T> linkedHashSet) {
        return (LinkedHashSet<T>) linkedHashSet;
    }

    /**
     * Returns a singleton {@code LinkedHashSet}, i.e. a {@code LinkedHashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new LinkedHashSet instance containing the given element
     */
    public static <T> LinkedHashSet<T> of(T element) {
        return LinkedHashSet.<T>empty().add(element);
    }

    /**
     * Creates a LinkedHashSet of the given elements.
     *
     * <pre><code>LinkedHashSet.of(1, 2, 3, 4)</code></pre>
     *
     * @param <T>      Component type of the LinkedHashSet.
     * @param elements Zero or more elements.
     * @return A set containing the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> LinkedHashSet<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.<T>empty().addAll(Arrays.asList(elements));
    }

    /**
     * Returns a LinkedHashSet containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the LinkedHashSet
     * @param n   The number of elements in the LinkedHashSet
     * @param f   The Function computing element values
     * @return A LinkedHashSet consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> LinkedHashSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, LinkedHashSet.empty(), LinkedHashSet::of);
    }

    /**
     * Returns a LinkedHashSet containing tuples returned by {@code n} calls to a given Supplier {@code s}.
     *
     * @param <T> Component type of the LinkedHashSet
     * @param n   The number of elements in the LinkedHashSet
     * @param s   The Supplier computing element values
     * @return A LinkedHashSet of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> LinkedHashSet<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, LinkedHashSet.empty(), LinkedHashSet::of);
    }

    /**
     * Creates a LinkedHashSet of the given elements.
     *
     * @param elements Set elements
     * @param <T>      The value type
     * @return A new LinkedHashSet containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedHashSet<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.<T>empty().addAll(elements);
    }

    /**
     * Creates a LinkedHashSet that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A LinkedHashSet containing the given elements in the same order.
     */
    public static <T> LinkedHashSet<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        return ofAll(Iterator.ofAll(javaStream.iterator()));
    }

    /**
     * Creates a LinkedHashSet from boolean values.
     *
     * @param elements boolean values
     * @return A new LinkedHashSet of Boolean values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from byte values.
     *
     * @param elements byte values
     * @return A new LinkedHashSet of Byte values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from char values.
     *
     * @param elements char values
     * @return A new LinkedHashSet of Character values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from double values.
     *
     * @param elements double values
     * @return A new LinkedHashSet of Double values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Double> ofAll(double... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from float values.
     *
     * @param elements a float values
     * @return A new LinkedHashSet of Float values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Float> ofAll(float... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from int values.
     *
     * @param elements int values
     * @return A new LinkedHashSet of Integer values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from long values.
     *
     * @param elements long values
     * @return A new LinkedHashSet of Long values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet from short values.
     *
     * @param elements short values
     * @return A new LinkedHashSet of Short values
     * @throws NullPointerException if elements is null
     */
    public static LinkedHashSet<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.range(0, 0)  // = LinkedHashSet()
     * LinkedHashSet.range(2, 0)  // = LinkedHashSet()
     * LinkedHashSet.range(-2, 2) // = LinkedHashSet(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static LinkedHashSet<Integer> range(int from, int toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    public static LinkedHashSet<Character> range(char from, char toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeBy(1, 3, 1)  // = LinkedHashSet(1, 2)
     * LinkedHashSet.rangeBy(1, 4, 2)  // = LinkedHashSet(1, 3)
     * LinkedHashSet.rangeBy(4, 1, -2) // = LinkedHashSet(4, 2)
     * LinkedHashSet.rangeBy(4, 1, 2)  // = LinkedHashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static LinkedHashSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static LinkedHashSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static LinkedHashSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.range(0L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.range(2L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.range(-2L, 2L) // = LinkedHashSet(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static LinkedHashSet<Long> range(long from, long toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeBy(1L, 3L, 1L)  // = LinkedHashSet(1L, 2L)
     * LinkedHashSet.rangeBy(1L, 4L, 2L)  // = LinkedHashSet(1L, 3L)
     * LinkedHashSet.rangeBy(4L, 1L, -2L) // = LinkedHashSet(4L, 2L)
     * LinkedHashSet.rangeBy(4L, 1L, 2L)  // = LinkedHashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static LinkedHashSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosed(0, 0)  // = LinkedHashSet(0)
     * LinkedHashSet.rangeClosed(2, 0)  // = LinkedHashSet()
     * LinkedHashSet.rangeClosed(-2, 2) // = LinkedHashSet(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static LinkedHashSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static LinkedHashSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosedBy(1, 3, 1)  // = LinkedHashSet(1, 2, 3)
     * LinkedHashSet.rangeClosedBy(1, 4, 2)  // = LinkedHashSet(1, 3)
     * LinkedHashSet.rangeClosedBy(4, 1, -2) // = LinkedHashSet(4, 2)
     * LinkedHashSet.rangeClosedBy(4, 1, 2)  // = LinkedHashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static LinkedHashSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static LinkedHashSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static LinkedHashSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosed(0L, 0L)  // = LinkedHashSet(0L)
     * LinkedHashSet.rangeClosed(2L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.rangeClosed(-2L, 2L) // = LinkedHashSet(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static LinkedHashSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosedBy(1L, 3L, 1L)  // = LinkedHashSet(1L, 2L, 3L)
     * LinkedHashSet.rangeClosedBy(1L, 4L, 2L)  // = LinkedHashSet(1L, 3L)
     * LinkedHashSet.rangeClosedBy(4L, 1L, -2L) // = LinkedHashSet(4L, 2L)
     * LinkedHashSet.rangeClosedBy(4L, 1L, 2L)  // = LinkedHashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static LinkedHashSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Add the given element to this set, doing nothing if it is already contained.
     * <p>
     * Note that this method has a worst-case linear complexity.
     *
     * @param element The element to be added.
     * @return A new set containing all elements of this set and also {@code element}.
     */
    @Override
    public LinkedHashSet<T> add(T element) {
        return addLast(element, false);
    }

    private LinkedHashSet<T> addLast(T e, boolean moveToLast) {
        var details = new ChampChangeEvent<ChampSequencedElement<T>>();
        var newElem = new ChampSequencedElement<T>(e, vector.size() - offset);
        var newRoot = update(null, newElem,
                Objects.hashCode(e), 0, details,
                moveToLast ? ChampSequencedElement::updateAndMoveToLast : ChampSequencedElement::update,
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            var newVector = vector;
            int newOffset = offset;
            int newSize = size;
            if (details.isReplaced()) {
                if (moveToLast) {
                    var oldElem = details.getOldData();
                    var result = ChampSequencedData.vecRemove(newVector, new ChampIdentityObject(), oldElem, details, newOffset);
                    newVector = result._1;
                    newOffset = result._2;
                }
            } else {
                newSize++;
            }
            newVector = newVector.append(newElem);
            return renumber(newRoot, newVector, newSize, newOffset);
        }
        return this;
    }

    /**
     * Adds all of the given elements to this set, replacing existing one if they are not already contained.
     * <p>
     * Note that this method has a worst-case quadratic complexity.
     *
     * @param elements The elements to be added.
     * @return A new set containing all elements of this set and the given {@code elements}, if not already contained.
     */
    @SuppressWarnings("unchecked")
    @Override
    public LinkedHashSet<T> addAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements == this || isEmpty() && (elements instanceof LinkedHashSet<?>)) {
            return (LinkedHashSet<T>) elements;
        }

        var details = new ChampChangeEvent<ChampSequencedElement<T>>();
        var newVector = vector;
        int newOffset = offset;
        int newSize = size;
        var mutator = new ChampIdentityObject();
        ChampBitmapIndexedNode<ChampSequencedElement<T>> newRoot = this;
        for (var e : elements) {
            var newElem = new ChampSequencedElement<T>(e, newVector.size() - newOffset);
            details.reset();
            newRoot = newRoot.update(mutator, newElem,
                    Objects.hashCode(e), 0, details,
                    ChampSequencedElement::update,
                    Objects::equals, Objects::hashCode);
            if (details.isModified()) {
                if (!details.isReplaced()) {
                    newSize++;
                }
                newVector = newVector.append(newElem);
                if (ChampSequencedData.vecMustRenumber(size, offset, this.vector.size())) {
                    LinkedHashSet<T> renumbered = renumber(newRoot, newVector, newSize, newOffset);
                    newRoot = renumbered;
                    newVector = renumbered.vector;
                    newOffset = 0;
                }
            }
        }
        return newRoot == this ? this : new LinkedHashSet<>(newRoot, newVector, newSize, newOffset);
    }

    @Override
    public <R> LinkedHashSet<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
        return ofAll(iterator().<R>collect(partialFunction));
    }

    @Override
    public boolean contains(T element) {
        return find(new ChampSequencedElement<>(element), Objects.hashCode(element), 0, Objects::equals) != ChampNode.NO_DATA;
    }

    @Override
    public LinkedHashSet<T> diff(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return this;
        } else {
            return removeAll(elements);
        }
    }

    @Override
    public LinkedHashSet<T> distinct() {
        return this;
    }

    @Override
    public LinkedHashSet<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return LinkedHashSet.ofAll(iterator().distinctBy(comparator));
    }

    @Override
    public <U> LinkedHashSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return LinkedHashSet.ofAll(iterator().distinctBy(keyExtractor));
    }

    @Override
    public LinkedHashSet<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else {
            return LinkedHashSet.ofAll(iterator().drop(n));
        }
    }

    @Override
    public LinkedHashSet<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else {
            return LinkedHashSet.ofAll(iterator().dropRight(n));
        }
    }

    @Override
    public LinkedHashSet<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public LinkedHashSet<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final LinkedHashSet<T> dropped = LinkedHashSet.ofAll(iterator().dropWhile(predicate));
        return dropped.length() == length() ? this : dropped;
    }

    @Override
    public LinkedHashSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final LinkedHashSet<T> filtered = LinkedHashSet.ofAll(iterator().filter(predicate));
        return filtered.length() == length() ? this : filtered;
    }

    @Override
    public LinkedHashSet<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    public <U> LinkedHashSet<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        LinkedHashSet<U> flatMapped = empty();
        for (T t : this) {
            for (U u : mapper.apply(t)) {
                flatMapped = flatMapped.add(u);
            }
        }
        return flatMapped;
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    public <C> Map<C, LinkedHashSet<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, LinkedHashSet::ofAll);
    }

    @Override
    public Iterator<LinkedHashSet<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public T head() {
        return iterator().next();
    }

    @Override
    public Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    @Override
    public LinkedHashSet<T> init() {
        // XXX Traversable.init() specifies that we must throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(last());
    }

    @Override
    public Option<LinkedHashSet<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public LinkedHashSet<T> intersect(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return empty();
        } else {
            return retainAll(elements);
        }
    }

    /**
     * An {@code LinkedHashSet}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * An {@code LinkedHashSet}'s value is computed eagerly.
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
    public boolean isSequential() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new ChampIteratorAdapter<>(spliterator());
    }

    @Override
    public T last() {
        return reversedIterator().next();
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public <U> LinkedHashSet<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        LinkedHashSet<U> mapped = empty();
        for (T t : this) {
            mapped = mapped.add(mapper.apply(t));
        }
        return mapped;
    }

    @Override
    public String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        return iterator().mkString(prefix, delimiter, suffix);
    }

    @Override
    public LinkedHashSet<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? ofAll(other) : this;
    }

    @Override
    public LinkedHashSet<T> orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? ofAll(supplier.get()) : this;
    }

    @Override
    public Tuple2<LinkedHashSet<T>, LinkedHashSet<T>> partition(Predicate<? super T> predicate) {
        return Collections.partition(this, LinkedHashSet::ofAll, predicate);
    }

    @Override
    public LinkedHashSet<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(iterator().head());
        }
        return this;
    }

    @Override
    public LinkedHashSet<T> remove(T element) {
        int keyHash = Objects.hashCode(element);
        var details = new ChampChangeEvent<ChampSequencedElement<T>>();
        ChampBitmapIndexedNode<ChampSequencedElement<T>> newRoot = remove(null,
                new ChampSequencedElement<>(element),
                keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            var oldElem = details.getOldDataNonNull();
            var result = ChampSequencedData.vecRemove(vector, null, oldElem, details, offset);
            return renumber(newRoot, result._1, size - 1,
                    result._2);
        }
        return this;
    }

    @Override
    public LinkedHashSet<T> removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll(this, elements);
    }

    /**
     * Renumbers the sequenced elements in the trie if necessary.
     *
     * @param root   the root of the trie
     * @param vector the root of the vector
     * @param size   the size of the trie
     * @param offset the offset that must be added to a sequence number to get the index into the vector
     * @return a new {@link LinkedHashSet} instance
     */
    private LinkedHashSet<T> renumber(
            ChampBitmapIndexedNode<ChampSequencedElement<T>> root,
            Vector<Object> vector,
            int size, int offset) {

        if (ChampSequencedData.vecMustRenumber(size, offset, this.vector.size())) {
            var mutator = new ChampIdentityObject();
            var result = ChampSequencedData.<ChampSequencedElement<T>>vecRenumber(
                    size, root, vector, mutator, Objects::hashCode, Objects::equals,
                    (e, seq) -> new ChampSequencedElement<>(e.getElement(), seq));
            return new LinkedHashSet<>(
                    result._1(), result._2(),
                    size, 0);
        }
        return new LinkedHashSet<>(root, vector, size, offset);
    }

    @Override
    public LinkedHashSet<T> replace(T currentElement, T newElement) {
        // currentElement and newElem are the same => do nothing
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }

        // try to remove currentElem from the 'root' trie
        final ChampChangeEvent<ChampSequencedElement<T>> detailsCurrent = new ChampChangeEvent<>();
        ChampIdentityObject mutator = new ChampIdentityObject();
        ChampBitmapIndexedNode<ChampSequencedElement<T>> newRoot = remove(mutator,
                new ChampSequencedElement<>(currentElement),
                Objects.hashCode(currentElement), 0, detailsCurrent, Objects::equals);
        // currentElement was not in the 'root' trie => do nothing
        if (!detailsCurrent.isModified()) {
            return this;
        }

        // currentElement was in the 'root' trie, and we have just removed it
        // => also remove its entry from the 'sequenceRoot' trie
        var newVector = vector;
        var newOffset = offset;
        ChampSequencedElement<T> currentData = detailsCurrent.getOldData();
        int seq = currentData.getSequenceNumber();
        var result = ChampSequencedData.vecRemove(newVector, mutator, currentData, detailsCurrent, newOffset);
        newVector = result._1;
        newOffset = result._2;

        // try to update the trie with the newElement
        ChampChangeEvent<ChampSequencedElement<T>> detailsNew = new ChampChangeEvent<>();
        ChampSequencedElement<T> newData = new ChampSequencedElement<>(newElement, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement), 0, detailsNew,
                ChampSequencedElement::forceUpdate,
                Objects::equals, Objects::hashCode);
        boolean isReplaced = detailsNew.isReplaced();

        // there already was an element with key newElement._1 in the trie, and we have just replaced it
        // => remove the replaced entry from the 'sequenceRoot' trie
        if (isReplaced) {
            ChampSequencedElement<T> replacedEntry = detailsNew.getOldData();
            result = ChampSequencedData.vecRemove(newVector, mutator, replacedEntry, detailsCurrent, newOffset);
            newVector = result._1;
            newOffset = result._2;
        }

        // we have just successfully added or replaced the newElement
        // => insert the new entry in the 'sequenceRoot' trie
        newVector = seq + newOffset < newVector.size() ? newVector.update(seq + newOffset, newData) : newVector.append(newData);

        if (isReplaced) {
            // we reduced the size of the map by one => renumbering may be necessary
            return renumber(newRoot, newVector, size - 1, newOffset);
        } else {
            // we did not change the size of the map => no renumbering is needed
            return new LinkedHashSet<>(newRoot, newVector, size, newOffset);
        }
    }

    @Override
    public LinkedHashSet<T> replaceAll(T currentElement, T newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public LinkedHashSet<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }


    private Iterator<T> reversedIterator() {
        return new ChampIteratorAdapter<>(reversedSpliterator());
    }

    @SuppressWarnings("unchecked")
    private Spliterator<T> reversedSpliterator() {
        return new ChampReversedSequencedVectorSpliterator<>(vector,
                e -> ((ChampSequencedElement<T>) e).getElement(),
                size(), Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public LinkedHashSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> LinkedHashSet<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, LinkedHashSet::ofAll);
    }

    @Override
    public <U> LinkedHashSet<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, LinkedHashSet::ofAll);
    }

    @Override
    public Iterator<LinkedHashSet<T>> slideBy(Function<? super T, ?> classifier) {
        return iterator().slideBy(classifier).map(LinkedHashSet::ofAll);
    }

    @Override
    public Iterator<LinkedHashSet<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<LinkedHashSet<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(LinkedHashSet::ofAll);
    }

    @Override
    public Tuple2<LinkedHashSet<T>, LinkedHashSet<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> t = iterator().span(predicate);
        return Tuple.of(LinkedHashSet.ofAll(t._1), LinkedHashSet.ofAll(t._2));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Spliterator<T> spliterator() {
        return new ChampSequencedVectorSpliterator<>(vector,
                e -> ((ChampSequencedElement<T>) e).getElement(),
                0, size(), Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public LinkedHashSet<T> tail() {
        // XXX AbstractTraversableTest.shouldThrowWhenTailOnNil requires that we throw UnsupportedOperationException instead of NoSuchElementException.
        if (isEmpty()) throw new UnsupportedOperationException();
        return remove(head());
    }

    @Override
    public Option<LinkedHashSet<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    public LinkedHashSet<T> take(int n) {
        if (size() <= n) {
            return this;
        }
        return LinkedHashSet.ofAll(() -> iterator().take(n));
    }

    @Override
    public LinkedHashSet<T> takeRight(int n) {
        if (size() <= n) {
            return this;
        }
        return LinkedHashSet.ofAll(() -> iterator().takeRight(n));
    }

    @Override
    public LinkedHashSet<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public LinkedHashSet<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final LinkedHashSet<T> taken = LinkedHashSet.ofAll(iterator().takeWhile(predicate));
        return taken.length() == length() ? this : taken;
    }

    /**
     * Transforms this {@code LinkedHashSet}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super LinkedHashSet<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public java.util.LinkedHashSet<T> toJavaSet() {
        return toJavaSet(java.util.LinkedHashSet::new);
    }

    /**
     * Adds all of the elements of {@code that} set to this set, if not already present.
     *
     * @param elements The set to form the union with.
     * @return A new set that contains all distinct elements of this and {@code elements} set.
     */
    @Override
    public LinkedHashSet<T> union(Set<? extends T> elements) {
        return addAll(elements);
    }

    @Override
    public <U> LinkedHashSet<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    public <U, R> LinkedHashSet<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return LinkedHashSet.ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    public <U> LinkedHashSet<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return LinkedHashSet.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public LinkedHashSet<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    public <U> LinkedHashSet<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return LinkedHashSet.ofAll(iterator().zipWithIndex(mapper));
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        return Collections.equals(this, o);
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(this);
    }

    @Override
    public String stringPrefix() {
        return "LinkedHashSet";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    // -- Serialization

    /**
     * {@code writeReplace} method for the serialization proxy pattern.
     * <p>
     * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
     * an instance of the enclosing class.
     *
     * @return A SerializationProxy for this enclosing class.
     */
    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    /**
     * {@code readObject} method for the serialization proxy pattern.
     * <p>
     * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
     *
     * @param stream An object serialization stream.
     * @throws InvalidObjectException This method will throw with the message "Proxy required".
     */
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    /**
     * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
     * instance fields.
     *
     * @param <T> The component type of the underlying list.
     */
    // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
    // classes. Also, it may not be compatible with circular object graphs.
    private static final class SerializationProxy<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        // the instance to be serialized/deserialized
        private transient LinkedHashSet<T> set;

        /**
         * Constructor for the case of serialization, called by {@link LinkedHashSet#writeReplace()}.
         * <p/>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param set a Cons
         */
        SerializationProxy(LinkedHashSet<T> set) {
            this.set = set;
        }

        /**
         * Write an object to a serialization stream.
         *
         * @param s An object serialization stream.
         * @throws IOException If an error occurs writing to the stream.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(set.size());
            for (T e : set) {
                s.writeObject(e);
            }
        }

        /**
         * Read an object from a deserialization stream.
         *
         * @param s An object deserialization stream.
         * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
         * @throws InvalidObjectException If the stream contains no list elements.
         * @throws IOException            If an error occurs reading from the stream.
         */
        private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
            s.defaultReadObject();
            final int size = s.readInt();
            if (size < 0) {
                throw new InvalidObjectException("No elements");
            }
            LinkedHashSet<T> temp = LinkedHashSet.empty();
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked") final T element = (T) s.readObject();
                temp = temp.add(element);
            }
            set = temp;
        }

        /**
         * {@code readResolve} method for the serialization proxy pattern.
         * <p>
         * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
         * serialization system to translate the serialization proxy back into an instance of the enclosing class
         * upon deserialization.
         *
         * @return A deserialized instance of the enclosing class.
         */
        private Object readResolve() {
            return LinkedHashSet.empty().addAll(set);
        }
    }
}
