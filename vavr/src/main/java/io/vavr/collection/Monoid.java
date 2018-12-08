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
package io.vavr.collection;

import io.vavr.Function2;

import java.util.function.BiFunction;

/**
 * <p>A description of how to "add" arbitrary objects, such as value objects
 * that represent numbers or text.</p>
 *
 * <p>You can define a Monoid for any value object class in order to
 * more-easily compute the "sum" of a {@link Traversable} of those objects.
 * For example, you could define a {@code MonetaryAmount} class, then define
 * a {@code MonetaryAmountMonoid} that supports computing the total amount
 * of money in a person's account or the total value of the items in a
 * shopper's shopping cart. You could also define a Monoid over strings to
 * turn lines into multiline text. Formally, this interface provides the
 * appropriate parameters to {@link Traversable#foldLeft(Object, BiFunction)}
 * in order to compute the "sum" of a {@link Traversable} of values.</p>
 *
 * <p>For numeric value objects, you probably want the identity element
 * to correspond to the value 0 and the "add" function to add the underlying
 * values. For text objects, you probably want the identity element to
 * correspond to the empty string and the "add" function to correspond to
 * concatenating strings.</p>
 *
 * <p>Although this interface defines {@link Monoid#addFunction()}, you
 * could also define this function similar to multiplication, in which case
 * the identity element should probably correspond to the value 1, and the
 * resulting "sum" would actually compute a "product".</p>
 *
 * <p>Formally, a Monoid is a set of objects and a binary operation. The
 * Monoid is closed under this binary operation and includes an identity
 * element relative to that operation. "Add"ing the identity element to
 * any other object {@code a} results in the "sum" of {@code a}.</p>
 *
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #identityElement()}</li>
 * <li>{@link #addFunction()}</li>
 * </ul>
 *
 * @param <T> Component type
 * @author J. B. Rainsberger
 */
public interface Monoid<T> {
    /**
     * <p>Identifies the unique <a href="https://en.wikipedia.org/wiki/Identity_element">identity element</a>
     * of a Monoid of objects
     * of type {@code T}. This is usually an element that corresponds
     * to the number 0 for addition or the empty string for concatenation.
     * The choice of identity element depends on the choice of "add"
     * operation in {@link Monoid#addFunction()}.</p>
     *
     * <p>Formally, the identity element {@code e} is the unique element
     * in this Monoid with the property that, if {@code a} is any element
     * of this Monoid, then {@code addFunction().apply(a, e).equals(a)} is
     * {@code true} and {@code addFunction().apply(e, a).equals(a)} is also
     * {@code true}.</p>
     *
     * @return a {@code T} with the properties of an <a href="https://en.wikipedia.org/wiki/Identity_element">identity element</a>
     */
    T identityElement();

    /**
     * <p>Returns a function that "add"s two {@code T} values together,
     * with the property that it is
     * <a href="https://en.wikipedia.org/wiki/Associative_property">associative</a>associative.
     * An associative operation has the property that one can compute a "sum" of a
     * list of values by "add"ing pairs of elements in any sequence. This allows the use of
     * strategies like partitioning to compute the "sum" of a {@link Traversable} using
     * parallelization. Note that associativity is <strong>not the same</strong> as commutativity:
     * {@code a+b} might not equal {@code b+a}, however, when adding three elements,
     * {@code (a+b)+c} equals {@code a+(b+c)}. Most numbers that you encounter will
     * have "add" operations that are both associative and commutative, but "add"ing
     * strings is associative but decidedly not commutative.</p>
     *
     * @return a {@link Function2} that maps "add"s {@code T} values, returning their "sum"
     */
    Function2<T, T, T> addFunction();
}
