/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.PartialFunction;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Value;
import io.vavr.control.Option;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * An interface for inherently recursive, multi-valued data structures. The order of elements is determined by
 * {@link Iterable#iterator()}, which may vary each time it is called.
 *
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #collect(PartialFunction)}</li>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #containsAll(Iterable)}</li>
 * <li>{@link #head()}</li>
 * <li>{@link #headOption()}</li>
 * <li>{@link #init()}</li>
 * <li>{@link #initOption()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #last()}</li>
 * <li>{@link #lastOption()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #size()}</li>
 * <li>{@link #tail()}</li>
 * <li>{@link #tailOption()}</li>
 * </ul>
 *
 * Iteration:
 *
 * <ul>
 * <li>{@link #grouped(int)}</li>
 * <li>{@link #iterator()}</li>
 * <li>{@link #slideBy(Function)}</li>
 * <li>{@link #sliding(int)}</li>
 * <li>{@link #sliding(int, int)}</li>
 * </ul>
 *
 * Numeric operations:
 *
 * <ul>
 * <li>{@link #average()}</li>
 * <li>{@link #max()}</li>
 * <li>{@link #maxBy(Comparator)}</li>
 * <li>{@link #maxBy(Function)}</li>
 * <li>{@link #min()}</li>
 * <li>{@link #minBy(Comparator)}</li>
 * <li>{@link #minBy(Function)}</li>
 * <li>{@link #product()}</li>
 * <li>{@link #sum()}</li>
 * </ul>
 *
 * Reduction/Folding:
 *
 * <ul>
 * <li>{@link #count(Predicate)}</li>
 * <li>{@link #fold(Object, BiFunction)}</li>
 * <li>{@link #foldLeft(Object, BiFunction)}</li>
 * <li>{@link #foldRight(Object, BiFunction)}</li>
 * <li>{@link #mkString()}</li>
 * <li>{@link #mkString(CharSequence)}</li>
 * <li>{@link #mkString(CharSequence, CharSequence, CharSequence)}</li>
 * <li>{@link #reduce(BiFunction)}</li>
 * <li>{@link #reduceOption(BiFunction)}</li>
 * <li>{@link #reduceLeft(BiFunction)}</li>
 * <li>{@link #reduceLeftOption(BiFunction)}</li>
 * <li>{@link #reduceRight(BiFunction)}</li>
 * <li>{@link #reduceRightOption(BiFunction)}</li>
 * </ul>
 *
 * Selection:
 *
 * <ul>
 * <li>{@link #drop(int)}</li>
 * <li>{@link #dropRight(int)}</li>
 * <li>{@link #dropUntil(Predicate)}</li>
 * <li>{@link #dropWhile(Predicate)}</li>
 * <li>{@link #filter(Predicate)}</li>
 * <li>{@link #find(Predicate)}</li>
 * <li>{@link #findLast(Predicate)}</li>
 * <li>{@link #groupBy(Function)}</li>
 * <li>{@link #partition(Predicate)}</li>
 * <li>{@link #retainAll(Iterable)}</li>
 * <li>{@link #take(int)}</li>
 * <li>{@link #takeRight(int)}</li>
 * <li>{@link #takeUntil(Predicate)}</li>
 * <li>{@link #takeWhile(Predicate)}</li>
 * </ul>
 *
 * Tests:
 *
 * <ul>
 * <li>{@link #existsUnique(Predicate)}</li>
 * <li>{@link #hasDefiniteSize()}</li>
 * <li>{@link #isDistinct()}</li>
 * <li>{@link #isOrdered()}</li>
 * <li>{@link #isSequential()}</li>
 * <li>{@link #isTraversableAgain()}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #distinct()}</li>
 * <li>{@link #distinctBy(Comparator)}</li>
 * <li>{@link #distinctBy(Function)}</li>
 * <li>{@link #flatMap(Function)}</li>
 * <li>{@link #map(Function)}</li>
 * <li>{@link #replace(Object, Object)}</li>
 * <li>{@link #replaceAll(Object, Object)}</li>
 * <li>{@link #scan(Object, BiFunction)}</li>
 * <li>{@link #scanLeft(Object, BiFunction)}</li>
 * <li>{@link #scanRight(Object, BiFunction)}</li>
 * <li>{@link #span(Predicate)}</li>
 * <li>{@link #unzip(Function)}</li>
 * <li>{@link #unzip3(Function)}</li>
 * <li>{@link #zip(Iterable)}</li>
 * <li>{@link #zipAll(Iterable, Object, Object)}</li>
 * <li>{@link #zipWithIndex()}</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich and others
 */
public interface Traversable<T> extends Foldable<T>, Value<T> {

    /**
     * Narrows a widened {@code Traversable<? extends T>} to {@code Traversable<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param traversable An {@code Traversable}.
     * @param <T>         Component type of the {@code Traversable}.
     * @return the given {@code traversable} instance as narrowed type {@code Traversable<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Traversable<T> narrow(Traversable<? extends T> traversable) {
        return (Traversable<T>) traversable;
    }
    
    /**
     * Matches each element with a unique key that you extract from it.
     * If the same key is present twice, the function will return {@code None}.
     *
     * @param getKey A function which extracts a key from elements
     * @param <K>    key class type
     * @return A Map containing the elements arranged by their keys.
     * @throws NullPointerException if {@code getKey} is null.
     * @see #groupBy(Function)
     */
    default <K> Option<Map<K, T>> arrangeBy(Function<? super T, ? extends K> getKey) {
        return Option.of(groupBy(getKey).mapValues(Traversable<T>::singleOption))
                .filter(map -> !map.exists(kv -> kv._2.isEmpty()))
                .map(map -> Map.narrow(map.mapValues(Option::get)));
    }

    /**
     * Calculates the average of this elements. Returns {@code None} if this is empty, otherwise {@code Some(average)}.
     * Supported component types are {@code Byte}, {@code Double}, {@code Float}, {@code Integer}, {@code Long},
     * {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().average()              // = None
     * List.of(1, 2, 3).average()          // = Some(2.0)
     * List.of(0.1, 0.2, 0.3).average()    // = Some(0.2)
     * List.of("apple", "pear").average()  // throws
     * </code>
     * </pre>
     *
     * @return {@code Some(average)} or {@code None}, if there are no elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings({ "unchecked", "OptionalGetWithoutIsPresent" })
    default Option<Double> average() {
        if (isEmpty()) {
            return Option.none();
        } else {
            final Traversable<?> objects = isTraversableAgain() ? this : toStream();
            final Object o = objects.head();
            if (o instanceof Number) {
                final Traversable<Number> numbers = (Traversable<Number>) objects;
                final double d;
                if (o instanceof Integer || o instanceof Long || o instanceof Byte || o instanceof BigInteger || o instanceof Short) {
                    d = numbers.toJavaStream()
                            .mapToLong(Number::longValue)
                            .average()
                            .getAsDouble();
                } else {
                    d = numbers.toJavaStream()
                            .mapToDouble(Number::doubleValue)
                            .average()
                            .getAsDouble();
                }
                return Option.some(d);
            } else {
                throw new UnsupportedOperationException("not numeric");
            }
        }
    }

    /**
     * Collects all elements that are in the domain of the given {@code partialFunction} by mapping the elements to type {@code R}.
     * <p>
     * More specifically, for each of this elements in iteration order first it is checked
     *
     * <pre>{@code
     * partialFunction.isDefinedAt(element)
     * }</pre>
     *
     * If the elements makes it through that filter, the mapped instance is added to the result collection
     *
     * <pre>{@code
     * R newElement = partialFunction.apply(element)
     * }</pre>
     *
     * <strong>Note:</strong>If this {@code Traversable} is ordered (i.e. extends {@link Ordered},
     * the caller of {@code collect} has to ensure that the elements are comparable (i.e. extend {@link Comparable}).
     *
     * @param partialFunction A function that is not necessarily defined of all elements of this traversable.
     * @param <R> The new element type
     * @return A new {@code Traversable} instance containing elements of type {@code R}
     * @throws NullPointerException if {@code partialFunction} is null
     */
    <R> Traversable<R> collect(PartialFunction<? super T, ? extends R> partialFunction);

    /**
     * Tests if this Traversable contains all given elements.
     * <p>
     * The result is equivalent to
     * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())} but implemented
     * without recursion.
     *
     * @param elements A List of values of type T.
     * @return true, if this List contains all given elements, false otherwise.
     * @throws NullPointerException if {@code elements} is null
     */
    default boolean containsAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        for (T element : elements) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts the elements which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return A number {@code >= 0}
     * @throws NullPointerException if {@code predicate} is null.
     */
    default int count(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return foldLeft(0, (i, t) -> predicate.test(t) ? i + 1 : i);
    }

    /**
     * Returns a new version of this which contains no duplicates. Elements are compared using {@code equals}.
     *
     * @return a new {@code Traversable} containing this elements without duplicates
     */
    Traversable<T> distinct();

    /**
     * Returns a new version of this which contains no duplicates. Elements are compared using the given
     * {@code comparator}.
     *
     * @param comparator A comparator
     * @return a new {@code Traversable} containing this elements without duplicates
     * @throws NullPointerException if {@code comparator} is null.
     */
    Traversable<T> distinctBy(Comparator<? super T> comparator);

    /**
     * Returns a new version of this which contains no duplicates. Elements mapped to keys which are compared using
     * {@code equals}.
     * <p>
     * The elements of the result are determined in the order of their occurrence - first match wins.
     *
     * @param keyExtractor A key extractor
     * @param <U>          key type
     * @return a new {@code Traversable} containing this elements without duplicates
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    <U> Traversable<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    /**
     * Drops the first n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the first n ones, or else the empty instance,
     * if this has less than n elements.
     */
    Traversable<T> drop(int n);

    /**
     * Drops the last n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the last n ones, or else the empty instance,
     * if this has less than n elements.
     */
    Traversable<T> dropRight(int n);

    /**
     * Drops elements until the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for this elements.
     * @return a new instance consisting of all elements starting from the first one which does satisfy the given
     * predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> dropUntil(Predicate<? super T> predicate);

    /**
     * Drops elements while the predicate holds for the current element.                                                                            
     * <p>
     * Note: This is essentially the same as {@code dropUntil(predicate.negate())}.
     * It is intended to be used with method references, which cannot be negated directly.
     *
     * @param predicate A condition tested subsequently for this elements.
     * @return a new instance consisting of all elements starting from the first one which does not satisfy the
     * given predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> dropWhile(Predicate<? super T> predicate);

    /**
     * In Vavr there are four basic classes of collections:
     *
     * <ul>
     * <li>Seq (sequential elements)</li>
     * <li>Set (distinct elements)</li>
     * <li>Map (indexed elements)</li>
     * <li>Multimap (indexed collections)</li>
     * </ul>
     *
     * Two collection instances of these classes are equal if and only if both collections
     *
     * <ul>
     * <li>belong to the same basic collection class (Seq, Set, Map or Multimap)</li>
     * <li>contain the same elements</li>
     * <li>have the same element order, if the collections are of type Seq</li>
     * </ul>
     * 
     * Two Map/Multimap elements, resp. entries, (key1, value1) and (key2, value2) are equal,
     * if the keys are equal and the values are equal.
     * <p>
     * <strong>Notes:</strong>
     *
     * <ul>
     * <li>No collection instance equals null, e.g. Queue(1) not equals null.</li>
     * <li>Nulls are allowed and handled as expected, e.g. List(null, 1) equals Stream(null, 1)
     * and HashMap((null, 1)) equals LinkedHashMap((null, 1)).
     * </li>
     * <li>The element order is taken into account for Seq only.
     * E.g. List(null, 1) not equals Stream(1, null)
     * and HashMap((null, 1), ("a", null)) equals LinkedHashMap(("a", null), (null, 1)).
     * The reason is, that we do not know which implementations we compare when having
     * two instances of type Map, Multimap or Set (see <a href="https://en.wikipedia.org/wiki/Liskov_substitution_principle">Liskov Substitution Principle</a>).</li>
     * <li>Other collection classes are equal if their types are equal and their elements are equal (in iteration order).</li>
     * <li>Iterator equality is defined to be object reference equality.</li>
     * </ul>
     *
     * @param obj an object, may be null
     * @return true, if this collection equals the given object according to the rules described above, false otherwise.
     */
    boolean equals(Object obj);

    /**
     * Checks, if a unique elements exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for a unique element, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean existsUnique(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        boolean exists = false;
        for (T t : this) {
            if (predicate.test(t)) {
                if (exists) {
                    return false;
                } else {
                    exists = true;
                }
            }
        }
        return exists;
    }

    /**
     * Returns a new traversable consisting of all elements which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new traversable
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> filter(Predicate<? super T> predicate);

    /**
     * Returns the first element of this which satisfies the given predicate.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).find(e -> e == null)}).
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> find(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T a : this) {
            if (predicate.test(a)) {
                return Option.some(a); // may be Some(null)
            }
        }
        return Option.none();
    }

    /**
     * Returns the last element of this which satisfies the given predicate.
     * <p>
     * Same as {@code reverse().find(predicate)}.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).find(e -> e == null)}).
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> findLast(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterator().findLast(predicate);
    }

    /**
     * FlatMaps this Traversable.
     *
     * @param mapper A mapper
     * @param <U>    The resulting component type.
     * @return A new Traversable instance.
     */
    <U> Traversable<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        U xs = zero;
        for (T x : this) {
            xs = f.apply(xs, x);
        }
        return xs;
    }

    @Override
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f);

    /**
     * Gets the first value in iteration order if this {@code Traversable} is not empty, otherwise throws.
     *
     * @return the first value
     * @throws NoSuchElementException if this {@code Traversable} is empty.
     */
    @Override
    default T get() {
        return head();
    }

    /**
     * Groups this elements by classifying the elements.
     *
     * @param classifier A function which classifies elements into classes
     * @param <C>        classified class type
     * @return A Map containing the grouped elements
     * @throws NullPointerException if {@code classifier} is null.
     * @see #arrangeBy(Function)
     */
    <C> Map<C, ? extends Traversable<T>> groupBy(Function<? super T, ? extends C> classifier);

    /**
     * Groups this {@code Traversable} into fixed size blocks.
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
    Iterator<? extends Traversable<T>> grouped(int size);

    /**
     * Checks if this Traversable is known to have a finite size.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to have a finite size, false otherwise.
     */
    boolean hasDefiniteSize();

    /**
     * Returns the first element of a non-empty Traversable.
     *
     * @return The first element of this Traversable.
     * @throws NoSuchElementException if this is empty
     */
    T head();

    /**
     * Returns the first element of a non-empty Traversable as {@code Option}.
     *
     * @return {@code Some(element)} or {@code None} if this is empty.
     */
    default Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }
    
    /**
     * Returns the hash code of this collection.
     * <br>
     * We distinguish between two types of hashes, those for collections with predictable iteration order (like Seq) and those with arbitrary iteration order (like Set, Map and Multimap).
     * <br>
     * In all cases the hash of an empty collection is defined to be 1.
     * <br>
     * Collections with predictable iteration order are hashed as follows:
     *
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) { hash = hash * 31 + Objects.hashCode(t); }
     * }</pre>
     *
     * Collections with arbitrary iteration order are hashed in a way such that the hash of a fixed number of elements is independent of their iteration order.
     *
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) { hash += Objects.hashCode(t); }
     * }</pre>
     *
     * Please note that the particular hashing algorithms may change in a future version of Vavr.
     * <br>
     * Generally, hash codes of collections aren't cached in Vavr (opposed to the size/length).
     * Storing hash codes in order to reduce the time complexity would increase the memory footprint.
     * Persistent collections are built upon tree structures, it allows us to implement efficient memory sharing.
     * A drawback of tree structures is that they make it necessary to store collection attributes at each tree node (read: element).
     * <br>
     * The computation of the hash code is linear in time, i.e. O(n). If the hash code of a collection is re-calculated often,
     * e.g. when using a List as HashMap key, we might want to cache the hash code.
     * This can be achieved by simply using a wrapper class, which is not included in Vavr but could be implemented like this:
     *
     * <pre>{@code public final class Hashed<K> {
     *
     *     private final K key;
     *     private final Lazy<Integer> hashCode;
     *
     *     public Hashed(K key) {
     *         this.key = key;
     *         this.hashCode = Lazy.of(() -> Objects.hashCode(key));
     *     }
     *
     *     public K key() {
     *         return key;
     *     }
     *
     *     &#64;Override
     *     public boolean equals(Object o) {
     *         if (o == key) {
     *             return true;
     *         } else if (key != null && o instanceof Hashed) {
     *             final Hashed that = (Hashed) o;
     *             return key.equals(that.key);
     *         } else {
     *             return false;
     *         }
     *     }
     *
     *     &#64;Override
     *     public int hashCode() {
     *         return hashCode.get();
     *     }
     *
     *     &#64;Override
     *     public String toString() {
     *         return "Hashed(" + (key == null ? "null" : key.toString()) + ")";
     *     }
     * }}</pre>
     *
     * @return The hash code of this collection
     */
    int hashCode();

    /**
     * Dual of {@linkplain #tail()}, returning all elements except the last.
     *
     * @return a new instance containing all elements except the last.
     * @throws UnsupportedOperationException if this is empty
     */
    Traversable<T> init();

    /**
     * Dual of {@linkplain #tailOption()}, returning all elements except the last as {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    default Option<? extends Traversable<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    /**
     * Checks if this Traversable may consist of distinct elements only.
     *
     * @return true if this Traversable may consist of distinct elements only, false otherwise.
     */
    default boolean isDistinct() {
        return false;
    }

    /**
     * Checks if this Traversable is empty.
     *
     * @return true, if this Traversable contains no elements, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Checks if this Traversable is ordered
     *
     * @return true, if this Traversable is ordered, false otherwise.
     */
    default boolean isOrdered() {
        return false;
    }

    /**
     * Checks if the elements of this Traversable appear in encounter order.
     *
     * @return true, if the insertion order of elements is preserved, false otherwise.
     */
    default boolean isSequential() {
        return false;
    }

    /**
     * Each of Vavr's collections may contain more than one element.
     *
     * @return {@code false}
     */
    @Override
    default boolean isSingleValued() {
        return false;
    }

    /**
     * Checks if this Traversable can be repeatedly traversed.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to be traversable repeatedly, false otherwise.
     */
    boolean isTraversableAgain();

    /**
     * An iterator by means of head() and tail(). Subclasses may want to override this method.
     *
     * @return A new Iterator of this Traversable elements.
     */
    @Override
    default Iterator<T> iterator() {
        final Traversable<T> that = this;
        return new AbstractIterator<T>() {

            Traversable<T> traversable = that;

            @Override
            public boolean hasNext() {
                return !traversable.isEmpty();
            }

            @Override
            public T getNext() {
                final T result = traversable.head();
                traversable = traversable.tail();
                return result;
            }
        };
    }

    /**
     * Dual of {@linkplain #head()}, returning the last element.
     *
     * @return the last element.
     * @throws NoSuchElementException is this is empty
     */
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty Traversable");
        } else {
            final Iterator<T> it = iterator();
            T result = null;
            while (it.hasNext()) {
                result = it.next();
            }
            return result;
        }
    }

    /**
     * Dual of {@linkplain #headOption()}, returning the last element as {@code Option}.
     *
     * @return {@code Some(element)} or {@code None} if this is empty.
     */
    default Option<T> lastOption() {
        return isEmpty() ? Option.none() : Option.some(last());
    }

    /**
     * Computes the number of elements of this Traversable.
     * <p>
     * Same as {@link #size()}.
     *
     * @return the number of elements
     */
    int length();

    /**
     * Maps the elements of this {@code Traversable} to elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the target Traversable
     * @return a mapped Traversable
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Calculates the maximum of this elements according to their natural order.
     *
     * @return {@code Some(maximum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if an element is null
     * @throws ClassCastException   if the elements do not have a natural order, i.e. they do not implement Comparable
     */
    @SuppressWarnings("unchecked")
    default Option<T> max() {
        if (isEmpty()) {
            return Option.none();
        } else {
            final Traversable<T> ts = isTraversableAgain() ? this : toStream();
            return ts.maxBy(Comparators.naturalComparator());
        }
    }

    /**
     * Calculates the maximum of this elements using a specific comparator.
     *
     * @param comparator A non-null element comparator
     * @return {@code Some(maximum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) >= 0 ? t1 : t2);
            return Option.some(value);
        }
    }

    /**
     * Calculates the maximum of this elements within the co-domain of a specific function.
     *
     * @param f   A function that maps this elements to comparable elements
     * @param <U> The type where elements are compared
     * @return The element of type T which is the maximum within U
     * @throws NullPointerException if {@code f} is null.
     */
    default <U extends Comparable<? super U>> Option<T> maxBy(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final Iterator<T> iter = iterator();
            T tm = iter.next();
            U um = f.apply(tm);
            while (iter.hasNext()) {
                final T t = iter.next();
                final U u = f.apply(t);
                if (u.compareTo(um) > 0) {
                    um = u;
                    tm = t;
                }
            }
            return Option.some(tm);
        }
    }

    /**
     * Calculates the minimum of this elements according to their natural order.
     *
     * @return {@code Some(minimum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if an element is null
     * @throws ClassCastException   if the elements do not have a natural order, i.e. they do not implement Comparable
     */
    @SuppressWarnings("unchecked")
    default Option<T> min() {
        if (isEmpty()) {
            return Option.none();
        } else {
            final Traversable<T> ts = isTraversableAgain() ? this : toStream();
            return ts.minBy(Comparators.naturalComparator());
        }
    }

    /**
     * Calculates the minimum of this elements using a specific comparator.
     *
     * @param comparator A non-null element comparator
     * @return {@code Some(minimum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) <= 0 ? t1 : t2);
            return Option.some(value);
        }
    }

    /**
     * Calculates the minimum of this elements within the co-domain of a specific function.
     *
     * @param f   A function that maps this elements to comparable elements
     * @param <U> The type where elements are compared
     * @return The element of type T which is the minimum within U
     * @throws NullPointerException if {@code f} is null.
     */
    default <U extends Comparable<? super U>> Option<T> minBy(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final Iterator<T> iter = iterator();
            T tm = iter.next();
            U um = f.apply(tm);
            while (iter.hasNext()) {
                final T t = iter.next();
                final U u = f.apply(t);
                if (u.compareTo(um) < 0) {
                    um = u;
                    tm = t;
                }
            }
            return Option.some(tm);
        }
    }
    
    /**
     * Joins the elements of this by concatenating their string representations.
     * <p>
     * This has the same effect as calling {@code mkCharSeq("", "", "")}.
     *
     * @return a new {@link CharSeq}
     */
    default CharSeq mkCharSeq() {
        return mkCharSeq("", "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter.
     * <p>
     * This has the same effect as calling {@code mkCharSeq("", delimiter, "")}.
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @return A new {@link CharSeq}
     */
    default CharSeq mkCharSeq(CharSequence delimiter) {
        return mkCharSeq("", delimiter, "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter, prefix and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkCharSeq("Chars(", ", ", ")") = CharSeq.of("Chars(a, b, c))"}
     *
     * @param prefix    prefix of the resulting {@link CharSeq}
     * @param delimiter A delimiter string put between string representations of elements of this
     * @param suffix    suffix of the resulting {@link CharSeq}
     * @return a new {@link CharSeq}
     */
    default CharSeq mkCharSeq(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        return CharSeq.of(mkString(prefix, delimiter, suffix));
    }

    /**
     * Joins the elements of this by concatenating their string representations.
     * <p>
     * This has the same effect as calling {@code mkString("", "", "")}.
     *
     * @return a new String
     */
    default String mkString() {
        return mkString("", "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter.
     * <p>
     * This has the same effect as calling {@code mkString("", delimiter, "")}.
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @return A new String
     */
    default String mkString(CharSequence delimiter) {
        return mkString("", delimiter, "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter, prefix and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkString("Chars(", ", ", ")") = "Chars(a, b, c)"}
     *
     * @param prefix    prefix of the resulting string
     * @param delimiter A delimiter string put between string representations of elements of this
     * @param suffix    suffix of the resulting string
     * @return a new String
     */
    default String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        iterator().map(String::valueOf).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }

    /**
     * Checks, this {@code Traversable} is not empty.
     * <p>
     * The call is equivalent to {@code !isEmpty()}.
     *
     * @return true, if an underlying value is present, false otherwise.
     */
    default boolean nonEmpty() {
        return !isEmpty();
    }

    /**
     * Returns this {@code Traversable} if it is nonempty, otherwise return the alternative.
     *
     * @param other An alternative {@code Traversable}
     * @return this {@code Traversable} if it is nonempty, otherwise return the alternative.
     */
    Traversable<T> orElse(Iterable<? extends T> other);

    /**
     * Returns this {@code Traversable} if it is nonempty, otherwise return the result of evaluating supplier.
     *
     * @param supplier An alternative {@code Traversable} supplier
     * @return this {@code Traversable} if it is nonempty, otherwise return the result of evaluating supplier.
     */
    Traversable<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    /**
     * Creates a partition of this {@code Traversable} by splitting this elements in two in distinct tarversables
     * according to a predicate.
     *
     * @param predicate A predicate which classifies an element if it is in the first or the second traversable.
     * @return A disjoint union of two traversables. The first {@code Traversable} contains all elements that satisfy the given {@code predicate}, the second {@code Traversable} contains all elements that don't. The original order of elements is preserved.
     * @throws NullPointerException if predicate is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> partition(Predicate<? super T> predicate);

    @Override
    Traversable<T> peek(Consumer<? super T> action);

    /**
     * Calculates the product of this elements. Supported component types are {@code Byte}, {@code Double}, {@code Float},
     * {@code Integer}, {@code Long}, {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().product()              // = 1
     * List.of(1, 2, 3).product()          // = 6L
     * List.of(0.1, 0.2, 0.3).product()    // = 0.006
     * List.of("apple", "pear").product()  // throws
     * </code>
     * </pre>
     *
     * @return a {@code Number} representing the sum of this elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number product() {
        if (isEmpty()) {
            return 1;
        } else {
            final Iterator<?> iter = iterator();
            final Object o = iter.next();
            if (o instanceof Number) {
                final Number head = (Number) o;
                final Iterator<Number> numbers = (Iterator<Number>) iter;
                if (head instanceof Integer || head instanceof Long || head instanceof Byte || head instanceof BigInteger || head instanceof Short) {
                    return numbers.toJavaStream().mapToLong(Number::longValue).reduce(head.longValue(), (l1, l2) -> l1 * l2);
                } else {
                    return numbers.toJavaStream().mapToDouble(Number::doubleValue).reduce(head.doubleValue(), (d1, d2) -> d1 * d2);
                }
            } else {
                throw new UnsupportedOperationException("not numeric");
            }
        }
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return iterator().reduceLeft(op);
    }

    /**
     * Shortcut for {@code isEmpty() ? Option.none() : Option.some(reduceLeft(op))}.
     *
     * @param op A BiFunction of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceLeftOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceLeft(op));
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    @Override
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceRight on empty");
        } else {
            return iterator().reduceRight(op);
        }
    }

    /**
     * Shortcut for {@code isEmpty() ? Option.none() : Option.some(reduceRight(op))}.
     *
     * @param op An operation of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceRightOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceRight(op));
    }

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a Traversable containing all elements of this where the first occurrence of currentElement is replaced with newElement.
     */
    Traversable<T> replace(T currentElement, T newElement);

    /**
     * Replaces all occurrences of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a Traversable containing all elements of this where all occurrences of currentElement are replaced with newElement.
     */
    Traversable<T> replaceAll(T currentElement, T newElement);

    /**
     * Keeps all occurrences of the given elements from this.
     *
     * @param elements Elements to be kept.
     * @return a Traversable containing all occurrences of the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    Traversable<T> retainAll(Iterable<? extends T> elements);

    /**
     * Computes a prefix scan of the elements of the collection.
     *
     * Note: The neutral element z may be applied more than once.
     *
     * @param zero      neutral element for the operator op
     * @param operation the associative operator for the scan
     * @return a new traversable collection containing the prefix scan of the elements in this traversable collection
     * @throws NullPointerException if {@code operation} is null.
     */
    Traversable<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    /**
     * Produces a collection containing cumulative results of applying the
     * operator going left to right.
     *
     * Note: will not terminate for infinite-sized collections.
     *
     * Note: might return different results for different runs, unless the
     * underlying collection type is ordered.
     *
     * @param <U>       the type of the elements in the resulting collection
     * @param zero      the initial value
     * @param operation the binary operator applied to the intermediate result and the element
     * @return collection with intermediate results
     * @throws NullPointerException if {@code operation} is null.
     */
    <U> Traversable<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    /**
     * Produces a collection containing cumulative results of applying the
     * operator going right to left. The head of the collection is the last
     * cumulative result.
     *
     * Note: will not terminate for infinite-sized collections.
     *
     * Note: might return different results for different runs, unless the
     * underlying collection type is ordered.
     *
     * @param <U>       the type of the elements in the resulting collection
     * @param zero      the initial value
     * @param operation the binary operator applied to the intermediate result and the element
     * @return collection with intermediate results
     * @throws NullPointerException if {@code operation} is null.
     */
    <U> Traversable<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);
    
    /**
     * Returns the single element of this Traversable or throws, if this is empty or contains more than one element.
     *
     * @return the single element from the Traversable
     * @throws NoSuchElementException if the Traversable does not contain a single element.
     */
    default T single() {
        return singleOption().getOrElseThrow(() -> new NoSuchElementException("Does not contain a single value"));
    }

    /**
     * Returns the only element of a Traversable as {@code Option}.
     *
     * @return {@code Some(element)} or {@code None} if the Traversable does not contain a single element.
     */
    default Option<T> singleOption() {
        final Iterator<T> it = iterator();
        if (!it.hasNext()) {
            return Option.none();
        }
        final T first = it.next();
        if (it.hasNext()) {
            return Option.none();
        } else {
            return Option.some(first);
        }
    }

    /**
     * Computes the number of elements of this Traversable.
     * <p>
     * Same as {@link #length()}.
     *
     * @return the number of elements
     */
    default int size() {
        return length();
    }

    /**
     * Slides a non-overlapping window of a variable size over this {@code Traversable}.
     * <p>
     * Each window contains elements with the same class, as determined by {@code classifier}. Two consecutive
     * values in this {@code Traversable} will be in the same window only if {@code classifier} returns equal
     * values for them. Otherwise, the values will constitute the last element of the previous window and the
     * first element of the next window.
     * <p>
     * Examples:
     * <pre>{@code
     * [].slideBy(Function.identity()) = []
     * [1,2,3,4,4,5].slideBy(Function.identity()) = [[1],[2],[3],[4,4],[5]]
     * [1,2,3,10,12,5,7,20,29].slideBy(x -> x/10) = [[1,2,3],[10,12],[5,7],[20,29]]
     * }</pre>
     *
     * @param classifier A function which classifies elements into classes
     * @return A new Iterator of windows of the grouped elements
     * @throws NullPointerException if {@code classifier} is null.
     */
    Iterator<? extends Traversable<T>> slideBy(Function<? super T, ?> classifier);

    /**
     * Slides a window of a specific {@code size} and step size 1 over this {@code Traversable} by calling
     * {@link #sliding(int, int)}.
     *
     * @param size a positive window size
     * @return a new Iterator of windows of a specific size using step size 1
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    Iterator<? extends Traversable<T>> sliding(int size);

    /**
     * Slides a window of a specific {@code size} and {@code step} size over this {@code Traversable}.
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
    Iterator<? extends Traversable<T>> sliding(int size, int step);

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy the given
     * {@code predicate} and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return a {@code Tuple} containing the longest prefix of elements that satisfy p and the remainder.
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    @Override
    default Spliterator<T> spliterator() {
        int characteristics = Spliterator.IMMUTABLE;
        if (isDistinct()) {
            characteristics |= Spliterator.DISTINCT;
        }
        if (isOrdered()) {
            characteristics |= (Spliterator.SORTED | Spliterator.ORDERED);
        }
        if (isSequential()) {
            characteristics |= Spliterator.ORDERED;
        }
        if (hasDefiniteSize()) {
            characteristics |= (Spliterator.SIZED | Spliterator.SUBSIZED);
            return Spliterators.spliterator(iterator(), length(), characteristics);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), characteristics);
        }
    }
    
    /**
     * Calculates the sum of this elements. Supported component types are {@code Byte}, {@code Double}, {@code Float},
     * {@code Integer}, {@code Long}, {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().sum()              // = 0
     * List.of(1, 2, 3).sum()          // = 6L
     * List.of(0.1, 0.2, 0.3).sum()    // = 0.6
     * List.of("apple", "pear").sum()  // throws
     * </code>
     * </pre>
     *
     * @return a {@code Number} representing the sum of this elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number sum() {
        if (isEmpty()) {
            return 0;
        } else {
            final Iterator<?> iter = iterator();
            final Object o = iter.next();
            if (o instanceof Number) {
                final Number head = (Number) o;
                final Iterator<Number> numbers = (Iterator<Number>) iter;
                if (head instanceof Integer || head instanceof Long || head instanceof Byte || head instanceof BigInteger || head instanceof Short) {
                    return numbers.foldLeft(head.longValue(), (n1, n2) -> n1 + n2.longValue());
                } else {
                    return numbers.foldLeft(head.doubleValue(), (n1, n2) -> n1 + n2.doubleValue());
                }
            } else {
                throw new UnsupportedOperationException("not numeric");
            }
        }
    }

    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    Traversable<T> tail();

    /**
     * Drops the first element of a non-empty Traversable and returns an {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends Traversable<T>> tailOption();

    /**
     * Takes the first n elements of this or all elements, if this length &lt; n.
     * <p>
     * The result is equivalent to {@code sublist(0, max(0, min(length(), n)))} but does not throw if {@code n < 0} or
     * {@code n > length()}.
     * <p>
     * In the case of {@code n < 0} the empty instance is returned, in the case of {@code n > length()} this is returned.
     *
     * @param n The number of elements to take.
     * @return A new instance consisting of the first n elements of this or all elements, if this has less than n elements.
     */
    Traversable<T> take(int n);

    /**
     * Takes the last n elements of this or all elements, if this length &lt; n.
     * <p>
     * The result is equivalent to {@code sublist(max(0, min(length(), length() - n)), n)}, i.e. takeRight will not
     * throw if {@code n < 0} or {@code n > length()}.
     * <p>
     * In the case of {@code n < 0} the empty instance is returned, in the case of {@code n > length()} this is returned.
     *
     * @param n The number of elements to take.
     * @return A new instance consisting of the last n elements of this or all elements, if this has less than n elements.
     */
    Traversable<T> takeRight(int n);

    /**
     * Takes elements until the predicate holds for the current element.
     * <p>
     * Note: This is essentially the same as {@code takeWhile(predicate.negate())}. It is intended to be used with
     * method references, which cannot be negated directly.
     *
     * @param predicate A condition tested subsequently for this elements.
     * @return a new instance consisting of all elements before the first one which does satisfy the given
     * predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> takeUntil(Predicate<? super T> predicate);

    /**
     * Takes elements while the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for the contained elements.
     * @return a new instance consisting of all elements before the first one which does not satisfy the
     * given predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Unzips this elements by mapping this elements to pairs which are subsequently split into two distinct
     * sets.
     *
     * @param unzipper a function which converts elements of this to pairs
     * @param <T1>     1st element type of a pair returned by unzipper
     * @param <T2>     2nd element type of a pair returned by unzipper
     * @return A pair of set containing elements split by unzipper
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2> Tuple2<? extends Traversable<T1>, ? extends Traversable<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    /**
     * Unzips this elements by mapping this elements to triples which are subsequently split into three distinct
     * sets.
     *
     * @param unzipper a function which converts elements of this to pairs
     * @param <T1>     1st element type of a triplet returned by unzipper
     * @param <T2>     2nd element type of a triplet returned by unzipper
     * @param <T3>     3rd element type of a triplet returned by unzipper
     * @return A triplet of set containing elements split by unzipper
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2, T3> Tuple3<? extends Traversable<T1>, ? extends Traversable<T2>, ? extends Traversable<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    /**
     * Returns a traversable formed from this traversable and another Iterable collection by combining
     * corresponding elements in pairs. If one of the two iterables is longer than the other, its remaining elements
     * are ignored.
     * <p>
     * The length of the returned traversable is the minimum of the lengths of this traversable and {@code that}
     * iterable.
     *
     * @param <U>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new traversable containing pairs consisting of corresponding elements of this traversable and {@code that} iterable.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zip(Iterable<? extends U> that);

    /**
     * Returns a traversable formed from this traversable and another Iterable by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     * <p>
     * The length of the returned traversable is the maximum of the lengths of this traversable and {@code that}
     * iterable.
     * <p>
     * Special case: if this traversable is shorter than that elements, and that elements contains duplicates, the
     * resulting traversable may be shorter than the maximum of the lengths of this and that because a traversable
     * contains an element at most once.
     * <p>
     * If this Traversable is shorter than that, thisElem values are used to fill the result.
     * If that is shorter than this Traversable, thatElem values are used to fill the result.
     *
     * @param <U>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this traversable is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this traversable.
     * @return A new traversable containing pairs consisting of corresponding elements of this traversable and that.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);
    
    /**
     * Returns a traversable formed from this traversable and another Iterable collection by mapping elements.
     * If one of the two iterables is longer than the other, its remaining elements are ignored.
     * <p>
     * The length of the returned traversable is the minimum of the lengths of this traversable and {@code that}
     * iterable.
     *
     * @param <U>    The type of the second parameter of the mapper.
     * @param <R>    The type of the mapped elements.
     * @param that   The Iterable providing the second parameter of the mapper.
     * @param mapper a mapper.
     * @return a new traversable containing mapped elements of this traversable and {@code that} iterable.
     * @throws NullPointerException if {@code that} or {@code mapper} is null
     */
    <U, R> Traversable<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    /**
     * Zips this traversable with its indices.
     *
     * @return A new traversable containing all elements of this traversable paired with their index, starting with 0.
     */
    Traversable<Tuple2<T, Integer>> zipWithIndex();

    /**
     * Returns a traversable formed from this traversable and another Iterable collection by mapping elements.
     * If one of the two iterables is longer than the other, its remaining elements are ignored.
     * <p>
     * The length of the returned traversable is the minimum of the lengths of this traversable and {@code that}
     * iterable.
     *
     * @param <U>    The type of the mapped elements.
     * @param mapper a mapper.
     * @return a new traversable containing mapped elements of this traversable and {@code that} iterable.
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Traversable<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);

}
