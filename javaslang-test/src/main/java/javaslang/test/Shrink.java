package javaslang.test;

import javaslang.Value;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.collection.Traversable;

import java.util.function.Function;
import java.util.function.Predicate;

import static javaslang.API.Stream;
import static javaslang.collection.Stream.empty;
import static javaslang.test.ShrinkModule.*;

/**
 * Represents a shrink for object of type T.
 *
 * @param <T> The type of the shrink object.
 */
@FunctionalInterface
public interface Shrink<T> {

    /**
     * Generates shrink for char values
     *
     * @return A new Shrink of Character
     */
    static Shrink<Character> chars() {
        return longs().filter(l -> l >= 0).map(Long::new, l -> (char) l.longValue());
    }

    /**
     * Generates an empty shrink
     *
     * @return a new empty Shrink
     */
    static <T> Shrink<T> empty() {
        return value -> Stream();
    }

    /**
     * Generates shrink for integer values
     *
     * @return A new Shrink of Integer
     */
    static Shrink<Integer> integer() {
        return longs().map(Integer::longValue, Long::intValue);
    }

    /**
     * Generates shrink for list of values specified by a given shrink.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Shrink.list(Shrink.integer());
     * </code>
     * </pre>
     *
     * @param shrink Shrink for elements of type T
     * @param <T>    Component type of the List
     * @return a new Shrink of List&lt;T&gt;
     */
    static <T> Shrink<List<T>> list(Shrink<T> shrink) {
        return stream(shrink).map(Value::toStream, Value::toList);
    }

    /**
     * Generates a shrink for long values
     *
     * @return A new Shrink of Long
     */
    static Shrink<Long> longs() {
        return value -> value == 0 ? Stream() : halves(value);
    }

    /**
     * Generates shrink for stream of values specified by a given shrink.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Shrink.stream(Shrink.integer());
     * </code>
     * </pre>
     *
     * @param shrink Shrink for elements of type T
     * @param <T>    Component type of the Stream
     * @return a new Shrink of Stream&lt;T&gt;
     */
    static <T> Shrink<Stream<T>> stream(Shrink<T> shrink) {
        return stream -> shrinkSize(stream.size(), stream).appendAll(shrinkValue(stream, shrink));
    }

    /**
     * Generates shrink for string
     *
     * @return a new Shrink of String
     */
    static Shrink<String> string() {
        return stream(chars()).map(s -> CharSeq.of(s).toStream(), chars -> CharSeq.ofAll(chars).toString());
    }

    /**
     * Functional interface of this shrink.
     *
     * @param value a object to shrink
     * @return A stream of shrunk values.
     */
    Stream<T> apply(T value);

    /**
     * Returns an Shrink based on this Shrink which produces values that fulfill the given predicate.
     *
     * @param predicate A predicate
     * @return A new Shrink
     */
    default Shrink<T> filter(Predicate<T> predicate) {
        return (T value) -> apply(value).filter(predicate);
    }

    /**
     * Maps shrink object T to shrink object U.
     *
     * @param comapping A function that maps a U to an object of type T.
     * @param mapping   A function that maps a T to an object of type U.
     * @param <U>       Type of the mapped object
     * @return A new shrink
     */
    default <U> Shrink<U> map(Function<U, T> comapping, Function<T, U> mapping) {
        return (U value) -> apply(comapping.apply(value)).map(mapping);
    }
}

interface ShrinkModule {

    /**
     * Interleaves two streams
     */
    static <T> Stream<T> interleave(Stream<T> a, Stream<T> b) {
        if (a.isEmpty()) {
            return b;
        } else if (b.isEmpty()) {
            return a;
        } else {
            return Stream.of(a.head(), b.head()).appendAll(interleave(a.tail(), b.tail()));
        }
    }

    /**
     * Shrinks value by half, with oscillation of sign
     *
     * @param value value to shrink
     * @return stream of shrinks by half
     */
    static Stream<Long> halves(Long value) {
        final long half = value / 2;
        return half == 0 ? Stream.of(half) : Stream.of(half, -half).appendAll(halves(half));
    }

    /**
     * Shrinks length of a source stream
     *
     * @param length the length of stream
     * @param values a source stream
     * @param <T>    type of stream values
     * @return stream of shrinks for stream by length
     */
    static <T> Stream<Stream<T>> shrinkSize(int length, Stream<T> values) {
        if (values.isEmpty()) {
            return Stream(empty());
        } else if (values.tail().isEmpty()) {
            return Stream(empty());
        } else {
            final int len1 = length / 2;
            final int len2 = length - len1;
            final Stream<T> chunk1 = values.take(len1);
            final Stream<T> chunk2 = values.drop(len1);
            final Stream<Stream<T>> reducedChunk1 = shrinkSize(len1, chunk1).filter(Traversable::nonEmpty).map(s -> s.appendAll(chunk2));
            final Stream<Stream<T>> reducedChunk2 = shrinkSize(len2, chunk2).filter(Traversable::nonEmpty).map(s -> chunk1.appendAll(s));
            return Stream.of(chunk1).append(chunk2).appendAll(interleave(reducedChunk1, reducedChunk2));
        }
    }

    /**
     * Shrinks values of the source stream
     *
     * @param items  a source stream
     * @param shrink a shrink for stream values
     * @param <T>    type of stream values
     * @return a stream of shrinks of stream by value
     */
    static <T> Stream<Stream<T>> shrinkValue(Stream<T> items, Shrink<T> shrink) {
        if (items.isEmpty()) {
            return empty();
        } else {
            final T head = items.head();
            final Stream<T> tail = items.tail();
            final Stream<Stream<T>> shrinkOfHead = shrink.apply(head).map(tail::prepend);
            final Stream<Stream<T>> shrinkOfTail = shrinkValue(tail, shrink).map(t -> t.prepend(head));
            return shrinkOfHead.appendAll(shrinkOfTail);
        }
    }
}
