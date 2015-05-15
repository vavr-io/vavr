/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.algebra.Functor;
import javaslang.algebra.Monad;
import javaslang.algebra.MonadLaws;
import javaslang.collection.List.Cons;
import javaslang.collection.List.Nil;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest extends AbstractSeqTest implements MonadLaws<Traversable<?>> {

    @Override
    protected <T> List<T> nil() {
        return List.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> List<T> of(T... elements) {
        return List.of(elements);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final List<?> actual = List.nil().toJavaStream().collect(List.collector());
        assertThat(actual).isEqualTo(List.nil());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final List<?> actual = List.of(1, 2, 3).toJavaStream().collect(List.collector());
        assertThat(actual).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final List<?> actual = List.nil().toJavaStream().parallel().collect(List.collector());
        assertThat(actual).isEqualTo(List.nil());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final List<?> actual = List.of(1, 2, 3).toJavaStream().parallel().collect(List.collector());
        assertThat(actual).isEqualTo(List.of(1, 2, 3));
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(List.nil()).isEqualTo(Nil.instance());
    }

    // -- static of()

    @Test
    public void shouldCreateListOfListUsingCons() {
        assertThat(List.of(List.nil()).toString()).isEqualTo("List(List())");
    }

    // -- static of(T...)

    @Test
    public void shouldCreateListOfElements() {
        final List<Integer> actual = List.of(1, 2);
        final List<Integer> expected = new Cons<>(1, new Cons<>(2, Nil.instance()));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static of(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
        assertThat(List.ofAll(arrayList)).isEqualTo(List.of(1, 2, 3));
    }

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateListOfRangeWhereFromIsGreaterThanTo() {
        assertThat(List.rangeClosed(1, 0)).isEqualTo(List.nil());
    }

    @Test
    public void shouldCreateListOfRangeWhereFromEqualsTo() {
        assertThat(List.rangeClosed(0, 0)).isEqualTo(List.of(0));
    }

    @Test
    public void shouldCreateListOfRangeWhereFromIsLessThanTo() {
        assertThat(List.rangeClosed(1, 3)).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void shouldCreateListOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(List.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.of(Integer.MIN_VALUE));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateListOfUntilWhereFromIsGreaterThanTo() {
        assertThat(List.range(1, 0)).isEqualTo(List.nil());
    }

    @Test
    public void shouldCreateListOfUntilWhereFromEqualsTo() {
        assertThat(List.range(0, 0)).isEqualTo(List.nil());
    }

    @Test
    public void shouldCreateListOfUntilWhereFromIsLessThanTo() {
        assertThat(List.range(1, 3)).isEqualTo(List.of(1, 2));
    }

    @Test
    public void shouldCreateListOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(List.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.nil());
    }

    // -- combinations

        @Test
    public void shouldComputeCombinationsOfEmptyList() {
        assertThat(List.nil().combinations()).isEqualTo(List.of(List.nil()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyList() {
        assertThat(List.of(1, 2, 3).combinations()).isEqualTo(List.of(List.nil(), List.of(1), List.of(2), List.of(3), List.of(1, 2), List.of(1, 3), List.of(2, 3), List.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyList() {
        assertThat(List.nil().combinations(1)).isEqualTo(List.nil());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyList() {
        assertThat(List.of(1, 2, 3).combinations(2)).isEqualTo(List.of(List.of(1, 2), List.of(1, 3), List.of(2, 3)));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(List.of(1).combinations(-1)).isEqualTo(List.of(List.nil()));
    }

    // -- peek

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyList() {
        assertThat(List.nil().permutations()).isEqualTo(List.nil());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyList() {
        assertThat(List.of(1, 2, 3).permutations()).isEqualTo(List.ofAll(List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1))));
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(this.nil().toString()).isEqualTo("List()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(this.of(1, 2, 3).toString()).isEqualTo("List(1, 2, 3)");
    }

    // -- Cons test

    @Test(expected = InvalidObjectException.class)
    public void shouldNotSerializeEnclosingClass() throws Throwable {
        Serializables.callReadObject(List.of(1));
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotDeserializeListWithSizeLessThanOne() throws Throwable {
        try {
            /*
             * This implementation is stable regarding jvm impl changes of object serialization. The index of the
             * number of List elements is gathered dynamically.
             */
            final byte[] listWithOneElement = Serializables.serialize(List.of(0));
            final byte[] listWithTwoElements = Serializables.serialize(List.of(0, 0));
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
        final Arbitrary<? extends Functor<Integer>> lists = Arbitrary.list(Arbitrary.integer());
        final CheckResult result = checkFunctorIdentity(lists);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyFunctorComposition() {
        final Arbitrary<? extends Functor<Integer>> lists = Arbitrary.list(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Double>> before =
                size -> random -> Double::valueOf;
        final Arbitrary<Function<? super Double, ? extends String>> after =
                size -> random -> String::valueOf;
        final CheckResult result = checkFunctorComposition(lists, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    // -- MonadLaws

    @Test
    @Override
    public void shouldSatisfyMonadLeftIdentity() {
        final Arbitrary<Function<? super Integer, ? extends Monad<String, Traversable<?>>>> mappers =
                size -> random -> i -> List.of(i).map(String::valueOf);
        final CheckResult result = checkMonadLeftIdentity(List::of, Arbitrary.integer(), mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadRightIdentity() {
        final Arbitrary<? extends Monad<Integer, Traversable<?>>> lists = Arbitrary.list(Arbitrary.integer());
        final CheckResult result = checkMonadRightIdentity(List::of, lists);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadAssociativity() {
        final Arbitrary<? extends Monad<Integer, Traversable<?>>> lists = Arbitrary.list(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Monad<Double, Traversable<?>>>> before =
                size -> random -> i -> List.of(i).map(Double::valueOf);
        final Arbitrary<Function<? super Double, ? extends Monad<String, Traversable<?>>>> after =
                size -> random -> d -> List.of(d).map(String::valueOf);
        final CheckResult result = checkMonadAssociativity(lists, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }
}
