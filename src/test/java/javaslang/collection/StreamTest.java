/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

import javaslang.*;
import javaslang.Algebra.Monoid;
import javaslang.collection.Stream.Cons;
import javaslang.collection.Stream.Nil;

import org.junit.Test;

public class StreamTest extends AbstractSeqTest {

    @Override
    protected <T> Stream<T> nil() {
        return Stream.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T, U extends Traversable<T>> Monoid<U> zero() {
        return (Monoid<U>) (Monoid) Stream.nil().zero();
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Stream<?> actual = Stream.nil().toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Stream<?> actual = Stream.of(1, 2, 3).toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Stream<?> actual = Stream.nil().toJavaStream().parallel().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Stream<?> actual = Stream.of(1, 2, 3).toJavaStream().parallel().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- static gen(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(Stream.gen(-1).take(3)).isEqualTo(Stream.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateTerminatingIntStream() {
        assertThat(Stream.gen(Integer.MAX_VALUE).take(2)).isEqualTo(Stream.of(Integer.MAX_VALUE));
    }

    // -- static gen(BigInteger)

    @Test
    public void shouldGenerateBigIntegerStream() {
        assertThat(Stream.gen(BigInteger.ZERO.subtract(BigInteger.ONE)).map(BigInteger::intValue).take(3)).isEqualTo(Stream.of(-1, 0, 1));
    }

    // -- static gen(BigInteger)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(Stream.gen(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(Stream.nil()).isEqualTo(Nil.instance());
    }

    // -- static of(T...)

    @Test
    public void shouldCreateStreamOfElements() {
        final Stream<Integer> actual = Stream.of(1, 2);
        final Stream<Integer> expected = new Cons<>(1, () -> new Cons<>(2, Nil::instance));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static of(Iterable)

    @Test
    public void shouldCreateStreamOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
        assertThat(Stream.of(arrayList)).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- static of(Iterator)

    @Test
    public void shouldCreateStreamOfIterator() {
        final Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        assertThat(Stream.of(iterator)).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsGreaterThanTo() {
        assertThat(Stream.range(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsTo() {
        assertThat(Stream.range(0, 0)).isEqualTo(Stream.of(0));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsLessThanTo() {
        assertThat(Stream.range(1, 3)).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.of(Integer.MIN_VALUE));
    }

    // -- static until(int, int)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(Stream.until(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(Stream.until(0, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(Stream.until(1, 3)).isEqualTo(Stream.of(1, 2));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.until(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.nil());
    }

    // -- unapply

    @Test
    public void shouldUnapplyNil() {
        assertThat(Nil.instance().unapply()).isEqualTo(Tuple.empty());
    }

    @Test
    public void shouldUnapplyCons() {
        assertThat(Stream.of(1, 2, 3).unapply()).isEqualTo(Tuple.of(1, Stream.of(2, 3)));
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(this.nil().toString()).isEqualTo("Stream()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(this.of(1, 2, 3).toString()).isEqualTo("Stream(1, 2, 3)");
    }

    // -- Cons test

    @Test
    public void shouldNotSerializeEnclosingClass() throws Exception {
        AssertionsExtensions.assertThat(() -> callReadObject(Stream.of(1))).isThrowing(InvalidObjectException.class,
                "Proxy required");
    }

    @Test
    public void shouldNotDeserializeStreamWithSizeLessThanOne() {
        AssertionsExtensions.assertThat(() -> {
            try {
				/*
				 * This implementation is stable regarding jvm impl changes of object serialization. The index of the
				 * number of Stream elements is gathered dynamically.
				 */
                final byte[] listWithOneElement = Serializables.serialize(Stream.of(0));
                final byte[] listWithTwoElements = Serializables.serialize(Stream.of(0, 0));
                int index = -1;
                for (int i = 0; i < listWithOneElement.length && index == -1; i++) {
                    final byte b1 = listWithOneElement[i];
                    final byte b2 = listWithTwoElements[i];
                    if (b1 != b2) {
                        if (b1 != 1 || b2 != 2) {
                            throw new IllegalStateException("Difference does not indicate number of elements.");
                        } else {
                            index = i;
                        }
                    }
                }
                if (index == -1) {
                    throw new IllegalStateException("Hack incomplete - index not found");
                }
				/*
				 * Hack the serialized data and fake zero elements.
				 */
                listWithOneElement[index] = 0;
                Serializables.deserialize(listWithOneElement);
            } catch (IllegalStateException x) {
                throw (x.getCause() != null) ? x.getCause() : x;
            }
        }).isThrowing(InvalidObjectException.class, "No elements");
    }

    private void callReadObject(Object o) throws Throwable {
        final byte[] objectData = Serializables.serialize(o);
        try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
            final Method method = o.getClass().getDeclaredMethod("readObject", ObjectInputStream.class);
            method.setAccessible(true);
            try {
                method.invoke(o, stream);
            } catch (InvocationTargetException x) {
                throw (x.getCause() != null) ? x.getCause() : x;
            }
        }
    }
}
