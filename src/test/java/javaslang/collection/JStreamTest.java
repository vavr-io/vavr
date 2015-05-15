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
import javaslang.collection.JStream.Cons;
import javaslang.collection.JStream.Nil;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class JStreamTest extends AbstractJSeqTest implements MonadLaws<JTraversable<?>> {

    @Override
    protected <T> JStream<T> nil() {
        return JStream.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> JStream<T> of(T... elements) {
        return JStream.of(elements);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final JStream<?> actual = JStream.nil().toJavaStream().collect(JStream.collector());
        assertThat(actual).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final JStream<?> actual = JStream.of(1, 2, 3).toJavaStream().collect(JStream.collector());
        assertThat(actual).isEqualTo(JStream.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final JStream<?> actual = JStream.nil().toJavaStream().parallel().collect(JStream.collector());
        assertThat(actual).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final JStream<?> actual = JStream.of(1, 2, 3).toJavaStream().parallel().collect(JStream.collector());
        assertThat(actual).isEqualTo(JStream.of(1, 2, 3));
    }

    // -- static from(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(JStream.from(-1).take(3)).isEqualTo(JStream.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateTerminatingIntStream() {
        assertThat(JStream.from(Integer.MAX_VALUE).take(2)).isEqualTo(JStream.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
    }

    // -- static gen(Supplier)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(JStream.gen(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(JStream.nil()).isEqualTo(Nil.instance());
    }

    // -- static of()

    @Test
    public void shouldCreateStreamOfStreamUsingCons() {
        assertThat(JStream.of(JStream.nil()).toString()).isEqualTo("Stream(Stream(), ?)");
    }

    // -- static of(T...)

    @Test
    public void shouldCreateStreamOfElements() {
        final JStream<Integer> actual = JStream.of(1, 2);
        final JStream<Integer> expected = new Cons<>(1, () -> new Cons<>(2, Nil::instance));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static of(Iterable)

    @Test
    public void shouldCreateStreamOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
        assertThat(JStream.ofAll(arrayList)).isEqualTo(JStream.of(1, 2, 3));
    }

    // -- static of(Iterator)

    @Test
    public void shouldCreateStreamOfIterator() {
        final Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        assertThat(JStream.ofAll(iterator)).isEqualTo(JStream.of(1, 2, 3));
    }

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsGreaterThanTo() {
        assertThat(JStream.rangeClosed(1, 0)).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsTo() {
        assertThat(JStream.rangeClosed(0, 0)).isEqualTo(JStream.of(0));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsLessThanTo() {
        assertThat(JStream.rangeClosed(1, 3)).isEqualTo(JStream.of(1, 2, 3));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(JStream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(JStream.of(Integer.MIN_VALUE));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MAX_VALUE() {
        assertThat(JStream.rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(JStream.of(Integer.MAX_VALUE));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(JStream.range(1, 0)).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(JStream.range(0, 0)).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(JStream.range(1, 3)).isEqualTo(JStream.of(1, 2));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(JStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MAX_VALUE() {
        assertThat(JStream.range(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(JStream.nil());
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyStream() {
        assertThat(JStream.nil().combinations()).isEqualTo(JStream.of(JStream.nil()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyStream() {
        assertThat(JStream.of(1, 2, 3).combinations()).isEqualTo(JStream.of(JStream.nil(), JStream.of(1), JStream.of(2), JStream.of(3), JStream.of(1, 2), JStream.of(1, 3), JStream.of(2, 3), JStream.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyStream() {
        assertThat(JStream.nil().combinations(1)).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyStream() {
        assertThat(JStream.of(1, 2, 3).combinations(2)).isEqualTo(JStream.of(JStream.of(1, 2), JStream.of(1, 3), JStream.of(2, 3)));
    }

    // -- peek

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 3;
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(JStream.of(1).combinations(-1)).isEqualTo(JStream.of(JStream.nil()));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyStream() {
        assertThat(JStream.nil().permutations()).isEqualTo(JStream.nil());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyStream() {
        assertThat(JStream.of(1, 2, 3).permutations()).isEqualTo(JStream.ofAll(JStream.of(JStream.of(1, 2, 3), JStream.of(1, 3, 2), JStream.of(2, 1, 3), JStream.of(2, 3, 1), JStream.of(3, 1, 2), JStream.of(3, 2, 1))));
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
        assertThat(JStream.of(1, 2, 3).unapply()).isEqualTo(Tuple.of(1, JStream.of(2, 3)));
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(this.nil().toString()).isEqualTo("Stream()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(this.of(1, 2, 3).toString()).isEqualTo("Stream(1, ?)");
    }

    @Test
    public void shouldStringifyNonNilEvaluatingFirstTail() {
        final JStream<Integer> stream = this.of(1, 2, 3);
        stream.tail(); // evaluates second head element
        assertThat(stream.toString()).isEqualTo("Stream(1, 2, ?)");
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
            final byte[] listWithOneElement = Serializables.serialize(JStream.of(0));
            final byte[] listWithTwoElements = Serializables.serialize(JStream.of(0, 0));
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
        final Arbitrary<Function<? super Integer, ? extends Monad<String, JTraversable<?>>>> mappers =
                size -> random -> i -> JStream.of(i).map(String::valueOf);
        final CheckResult result = checkMonadLeftIdentity(JStream::of, Arbitrary.integer(), mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadRightIdentity() {
        final Arbitrary<? extends Monad<Integer, JTraversable<?>>> streams = Arbitrary.stream(Arbitrary.integer());
        final CheckResult result = checkMonadRightIdentity(JStream::of, streams);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadAssociativity() {
        final Arbitrary<? extends Monad<Integer, JTraversable<?>>> streams = Arbitrary.stream(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Monad<Double, JTraversable<?>>>> before =
                size -> random -> i -> JStream.of(i).map(Double::valueOf);
        final Arbitrary<Function<? super Double, ? extends Monad<String, JTraversable<?>>>> after =
                size -> random -> d -> JStream.of(d).map(String::valueOf);
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
