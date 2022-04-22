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

import io.vavr.control.Option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Interface of foldable data structures.
 * <p>
 * <strong>Example:</strong>
 *
 * <pre><code>
 * // = "123"
 * Stream.of("1", "2", "3").fold("", (a1, a2) -&gt; a1 + a2);
 * </code></pre>
 *
 * @param <T> Component type of this foldable
 * @deprecated Marked for removal. Methods moved to io.vavr.collection.Traverable.
 */
@Deprecated
public interface Foldable<T> {

    /**
     * Folds this elements using the given associative binary operator, starting with {@code zero} and
     * successively calling {@code combine}. The order in which the elements are combined is
     * non-deterministic.
     * <p>
     * The methods {@code fold}, {@code foldLeft} and {@code foldRight} differ in how the elements are combined:
     *
     * <ul>
     * <li>{@link #foldLeft(Object, BiFunction)} associates to the left</li>
     * <li>{@link #foldRight(Object, BiFunction)} associates to the right</li>
     * <li>
     * {@code fold} takes an associative combine operation because the traversal of elements is
     * unordered/non-deterministic. The associativity guarantees that in each case the result will
     * be the same, it does not matter in which order the elements are combined. Generally binary
     * operators aren't associative, i.e. the result may differ if elements are combined in a different
     * order.
     * <p>
     * We say that this Foldable and the associative combine operation form a
     * <a href="https://en.wikipedia.org/wiki/Monoid" target="_blank">Monoid</a>.
     * </li>
     * </ul>
     *
     * Example:
     *
     * <pre> {@code
     * // = 6
     * Set(1, 2, 3).fold(0, (a, b) -> a + b);
     * } </pre>
     *
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> combine) {
        return foldLeft(zero, combine);
    }

    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     * <p>
     * Example:
     *
     * <pre> {@code
     * // = "cba!"
     * List("a", "b", "c").foldLeft("!", (xs, x) -> x + xs)
     * } </pre>
     *
     * @param <U>     the type to fold over
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine);

    /**
     * Folds this elements from the right, starting with {@code zero} and successively calling {@code combine}.
     * <p>
     * Example:
     *
     * <pre> {@code
     * // = "!cba"
     * List("a", "b", "c").foldRight("!", (x, xs) -> xs + x)
     * } </pre>
     *
     * @param <U>     the type of the folded value
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    default T reduce(BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeft(op);
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return Some of reduced value or None if the Foldable is empty.
     * @throws NullPointerException if {@code op} is null
     */
    default Option<T> reduceOption(BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeftOption(op);
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return Some of reduced value or None if the Foldable is empty.
     * @throws NullPointerException if {@code op} is null
     */
    Option<T> reduceLeftOption(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    T reduceRight(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return Some of reduced value or None.
     * @throws NullPointerException if {@code op} is null
     */
    Option<T> reduceRightOption(BiFunction<? super T, ? super T, ? extends T> op);

}
