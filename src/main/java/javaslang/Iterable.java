/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.IndexedSeq;
import javaslang.collection.Iterator;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A rich extension of {@code java.lang.Iterable} and basis of all {@link Value} types, e.g. controls, collections et al.
 * <p>
 *
 * Equality:
 *
 * <ul>
 * <li>{@link #corresponds(java.lang.Iterable, BiPredicate)}</li>
 * <li>{@link #eq(Object)}</li>
 * </ul>
 *
 * java.lang.Iterable extensions:
 *
 * <ul>
 * <li>{@link #exists(Predicate)}</li>
 * <li>{@link #forAll(Predicate)}</li>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #grouped(int)}</li>
 * <li>{@link #iterator()}</li>
 * <li>{@link #sliding(int)}</li>
 * <li>{@link #sliding(int, int)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * Returns a rich {@code javaslang.collection.Iterator}.
     *
     * @return A new Iterator
     */
    @Override
    Iterator<T> iterator();

    /**
     * Tests whether every element of this iterable relates to the corresponding element of another iterable by
     * satisfying a test predicate.
     *
     * @param <U>       Component type of that iterable
     * @param that      the other iterable
     * @param predicate the test predicate, which relates elements from both iterables
     * @return {@code true} if both iterables have the same length and {@code predicate(x, y)}
     * is {@code true} for all corresponding elements {@code x} of this iterable and {@code y} of {@code that},
     * otherwise {@code false}.
     */
    default <U> boolean corresponds(java.lang.Iterable<U> that, BiPredicate<? super T, ? super U> predicate) {
        final java.util.Iterator<T> it1 = iterator();
        final java.util.Iterator<U> it2 = that.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!predicate.test(it1.next(), it2.next())) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    /**
     * A <em>smoothing</em> replacement for {@code equals}. It is similar to Scala's {@code ==} but better in the way
     * that it is not limited to collection types, e.g. `Some(1) eq List(1)`, `None eq Failure(x)` etc.
     * <p>
     * In a nutshell: eq checks <strong>congruence of structures</strong> and <strong>equality of contained values</strong>.
     * <p>
     * Example:
     *
     * <pre><code>
     * // ((1, 2), ((3))) =&gt; structure: (()(())) values: 1, 2, 3
     * final Iterable&lt;?&gt; i1 = List.of(List.of(1, 2), Arrays.asList(List.of(3)));
     * final Iterable&lt;?&gt; i2 = Queue.of(Stream.of(1, 2), List.of(Lazy.of(() -&gt; 3)));
     * assertThat(i1.eq(i2)).isTrue();
     * </code></pre>
     *
     * Semantics:
     *
     * <pre><code>
     * o == this                       : true
     * o instanceof javaslang.Iterable : iterable elements are eq, non-iterable elements equals, for all (o1, o2) in (this, o)
     * o instanceof java.lang.Iterable : this eq Iterator.ofAll((java.lang.Iterable&lt;?&gt;) o);
     * otherwise                       : false
     * </code></pre>
     *
     * @param o An object
     * @return true, if this equals o according to the rules defined above, otherwise false.
     */
    default boolean eq(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Iterable) {
            final Iterable<?> that = (Iterable<?>) o;
            return this.iterator().corresponds(that.iterator(), (o1, o2) -> {
                if (o1 instanceof Iterable) {
                    return ((Iterable<?>) o1).eq(o2);
                } else if (o2 instanceof Iterable) {
                    return ((Iterable<?>) o2).eq(o1);
                } else {
                    return Objects.equals(o1, o2);
                }
            });
        } else if (o instanceof java.lang.Iterable) {
            final Iterable<?> that = Iterator.ofAll((java.lang.Iterable<?>) o);
            return this.eq(that);
        } else {
            return false;
        }
    }

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for one or more elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if the given predicate holds for all elements.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return !exists(predicate.negate());
    }

    /**
     * Performs an action on each element.
     *
     * @param action A {@code Consumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Groups this {@code Iterable} into fixed size blocks.
     * <p>
     * Let length be the length of this Iterable. Then grouped is defined as follows:
     * <ul>
     * <li>If {@code this.isEmpty()}, the resulting {@code Iterator} is empty.</li>
     * <li>If {@code size <= length}, the resulting {@code Iterator} will contain {@code length / size} blocks of size
     * {@code size} and maybe a non-empty block of size {@code length % size}, if there are remaining elements.</li>
     * <li>If {@code size > length}, the resulting {@code Iterator} will contain one block of size {@code length}.</li>
     * </ul>
     * Examples:
     * <pre>
     * <code>
     * [].grouped(1) = []
     * [].grouped(0) throws
     * [].grouped(-1) throws
     * [1,2,3,4].grouped(2) = [[1,2],[3,4]]
     * [1,2,3,4,5].grouped(2) = [[1,2],[3,4],[5]]
     * [1,2,3,4].grouped(5) = [[1,2,3,4]]
     * </code>
     * </pre>
     *
     * Please note that {@code grouped(int)} is a special case of {@linkplain #sliding(int, int)}, i.e.
     * {@code grouped(size)} is the same as {@code sliding(size, size)}.
     *
     * @param size a positive block size
     * @return A new Iterator of grouped blocks of the given size
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    default Iterator<IndexedSeq<T>> grouped(int size) {
        return sliding(size, size);
    }


    /**
     * Slides a window of a specific {@code size} and step size 1 over this {@code Iterable} by calling
     * {@link #sliding(int, int)}.
     *
     * @param size a positive window size
     * @return a new Iterator of windows of a specific size using step size 1
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    default Iterator<IndexedSeq<T>> sliding(int size) {
        return sliding(size, 1);
    }

    /**
     * Slides a window of a specific {@code size} and {@code step} size over this {@code Iterable}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * [].sliding(1,1) = []
     * [1,2,3,4,5].sliding(2,3) = [[1,2],[4,5]]
     * [1,2,3,4,5].sliding(2,4) = [[1,2],[5]]
     * [1,2,3,4,5].sliding(2,5) = [[1,2]]
     * [1,2,3,4].sliding(5,3) = [[1,2,3,4],[4]]
     * </code>
     * </pre>
     *
     * @param size a positive window size
     * @param step a positive step size
     * @return a new Iterator of windows of a specific size using a specific step size
     * @throws IllegalArgumentException if {@code size} or {@code step} are negative or zero
     */
    default Iterator<IndexedSeq<T>> sliding(int size, int step) {
        return iterator().sliding(size, step);
    }

}
