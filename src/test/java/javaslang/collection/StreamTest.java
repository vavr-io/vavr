/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.Tuple;
import javaslang.algebra.Functor;
import javaslang.algebra.Monad;
import javaslang.algebra.MonadLaws;
import javaslang.collection.Stream.Cons;
import javaslang.collection.Stream.Nil;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest extends AbstractSeqTest implements MonadLaws<Traversable<?>> {

    @Override
    protected <T> Stream<T> nil() {
        return Stream.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
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

    // -- static stdin()

    @Test
    public void shouldCreateNonBlockingStreamOfStringLinesFromStdin() {
        assertThat(Stream.stdin()).isNotNull();
    }

    // -- static stdin(Charset)

    @Test
    public void shouldCreateNonBlockingStreamOfStringLinesFromStdinUsingCharset() {
        assertThat(Stream.stdin(Charset.defaultCharset())).isNotNull();
    }

    // -- static lines(InputStream)

    @Test
    public void shouldCreateNonBlockingStreamOfStringLinesFromInputStream() {
        assertThat(Stream.lines(System.in)).isNotNull();
    }

    // -- static lines(InputStream, Charset)

    @Test
    public void shouldCreateNonBlockingStreamOfStringLinesFromInputStreamUsingCharset() {
        assertThat(Stream.lines(System.in, Charset.defaultCharset())).isNotNull();
    }

    @Test
    public void shouldReadLineFromStringLinesByReadingInputStreamUsingCharset() {
        assertThat(Stream.lines(new OneElement(), Charset.defaultCharset()).head()).isNotNull();
    }

    @Test
    public void shouldHandleEndOfStringLinesByReadingInputStreamUsingCharset() {
        assertThat(Stream.lines(new OneElement(), Charset.defaultCharset()).tail().isEmpty()).isTrue();
    }

    // -- static chars(InputStream)

    @Test
    public void shouldCreateNonBlockingStreamOfCharsFromInputStream() {
        assertThat(Stream.chars(System.in)).isNotNull();
    }

    // -- static chars(InputStream, Charset)

    @Test
    public void shouldCreateNonBlockingStreamOfCharsFromInputStreamUsingCharset() {
        assertThat(Stream.chars(System.in, Charset.defaultCharset())).isNotNull();
    }

    @Test
    public void shouldReadCharByReadingInputStreamUsingCharset() {
        assertThat(Stream.chars(new OneElement(), Charset.defaultCharset()).head()).isNotNull();
    }

    @Test
    public void shouldHandleEndOfCharsByReadingInputStreamUsingCharset() {
        assertThat(Stream.chars(new OneElement(), Charset.defaultCharset()).tail().isEmpty()).isTrue();
    }

    // -- static bytes(InputStream)

    @Test
    public void shouldCreateNonBlockingStreamOfBytesFromInputStream() {
        assertThat(Stream.bytes(System.in)).isNotNull();
    }

    @Test
    public void shouldReadByteByReadingInputStream() {
        assertThat(Stream.bytes(new OneElement()).head()).isNotNull();
    }

    @Test
    public void shouldHandleEndOfBytesByReadingInputStream() {
        assertThat(Stream.bytes(new OneElement()).tail().isEmpty()).isTrue();
    }

    // -- static ints(InputStream)

    @Test
    public void shouldCreateNonBlockingStreamOfIntsFromInputStream() {
        assertThat(Stream.ints(System.in)).isNotNull();
    }

    @Test
    public void shouldReadIntByReadingInputStream() {
        assertThat(Stream.ints(new OneElement()).head()).isNotNull();
    }

    @Test
    public void shouldHandleEndOfIntsByReadingInputStream() {
        assertThat(Stream.ints(new OneElement()).tail().isEmpty()).isTrue();
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(Stream.nil()).isEqualTo(Nil.instance());
    }

    // -- static cons()

    @Test
    public void shouldCreateStreamOfStreamUsingCons() {
        assertThat(Stream.cons(Stream.nil()).toString()).isEqualTo("Stream(Stream())");
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

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsGreaterThanTo() {
        assertThat(Stream.rangeClosed(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsTo() {
        assertThat(Stream.rangeClosed(0, 0)).isEqualTo(Stream.of(0));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsLessThanTo() {
        assertThat(Stream.rangeClosed(1, 3)).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.of(Integer.MIN_VALUE));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(Stream.range(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(Stream.range(0, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(Stream.range(1, 3)).isEqualTo(Stream.of(1, 2));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.nil());
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyStream() {
        assertThat(Stream.nil().combinations()).isEqualTo(Stream.cons(Stream.nil()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations()).isEqualTo(Stream.of(Stream.nil(), Stream.of(1), Stream.of(2), Stream.of(3), Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3), Stream.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyStream() {
        assertThat(Stream.nil().combinations(1)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations(2)).isEqualTo(Stream.of(Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3)));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(Stream.of(1).combinations(-1)).isEqualTo(Stream.cons(Stream.nil()));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyStream() {
        assertThat(Stream.nil().permutations()).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).permutations()).isEqualTo(Stream.of(Stream.of(Stream.of(1, 2, 3), Stream.of(1, 3, 2), Stream.of(2, 1, 3), Stream.of(2, 3, 1), Stream.of(3, 1, 2), Stream.of(3, 2, 1))));
    }

    // -- unapply

    @Test
    public void shouldUnapplyNil() {
        assertThat(Nil.instance().unapply()).isEqualTo(Tuple.empty());
    }

    @Test
    public void shouldUnapplyCons() {
        assertThat(new Cons<>(1, Nil::instance).unapply()).isEqualTo(Tuple.of(1, Nil.instance()));
    }

    @Test
    public void shouldUnapplyDeferred() {
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

    // -- Serializable

    @Test(expected = InvalidObjectException.class)
    public void shouldNotSerializeEnclosingClassOfCons() throws Throwable {
        Serializables.callReadObject(new Cons<>(1, Nil::instance));
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotDeserializeStreamWithSizeLessThanOne() throws Throwable {
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
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotSerializeEnclosingClassOfDeferred() throws Throwable {
        Serializables.callReadObject(new Stream.Deferred<>(Nil::instance));
    }

    // -- FunctorLaws

    @Test
    @Override
    public void shouldSatisfyFunctorIdentity() {
        final Arbitrary<? extends Functor<Integer>> streams = Arbitrary.stream(Arbitrary.integer());
        final CheckResult result = checkFunctorIdentity(streams);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyFunctorComposition() {
        final Arbitrary<? extends Functor<Integer>> streams = Arbitrary.stream(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Double>> before =
                size -> random -> Double::valueOf;
        final Arbitrary<Function<? super Double, ? extends String>> after =
                size -> random -> String::valueOf;
        final CheckResult result = checkFunctorComposition(streams, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    // -- MonadLaws

    @Test
    @Override
    public void shouldSatisfyMonadLeftIdentity() {
        final Arbitrary<Function<? super Integer, ? extends Monad<String, Traversable<?>>>> mappers =
                size -> random -> i -> Stream.of(i).map(String::valueOf);
        final CheckResult result = checkMonadLeftIdentity(Stream::of, Arbitrary.integer(), mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadRightIdentity() {
        final Arbitrary<? extends Monad<Integer, Traversable<?>>> streams = Arbitrary.stream(Arbitrary.integer());
        final CheckResult result = checkMonadRightIdentity(Stream::of, streams);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadAssociativity() {
        final Arbitrary<? extends Monad<Integer, Traversable<?>>> streams = Arbitrary.stream(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Monad<Double, Traversable<?>>>> before =
                size -> random -> i -> Stream.of(i).map(Double::valueOf);
        final Arbitrary<Function<? super Double, ? extends Monad<String, Traversable<?>>>> after =
                size -> random -> d -> Stream.of(d).map(String::valueOf);
        final CheckResult result = checkMonadAssociativity(streams, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    // helpers

    static class OneElement extends InputStream {

        int count = 0;

        @Override
        public int read() throws IOException {
            if (count-- > -1) {
                return '\n';
            } else {
                throw new IOException("end of stream");
            }
        }
    }
}
