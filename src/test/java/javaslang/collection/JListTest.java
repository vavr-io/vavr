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
import javaslang.collection.JList.Cons;
import javaslang.collection.JList.Nil;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class JListTest extends AbstractJSeqTest implements MonadLaws<JTraversable<?>> {

    @Override
    protected <T> JList<T> nil() {
        return JList.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> JList<T> of(T... elements) {
        return JList.of(elements);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final JList<?> actual = JList.nil().toJavaStream().collect(JList.collector());
        assertThat(actual).isEqualTo(JList.nil());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final JList<?> actual = JList.of(1, 2, 3).toJavaStream().collect(JList.collector());
        assertThat(actual).isEqualTo(JList.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final JList<?> actual = JList.nil().toJavaStream().parallel().collect(JList.collector());
        assertThat(actual).isEqualTo(JList.nil());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final JList<?> actual = JList.of(1, 2, 3).toJavaStream().parallel().collect(JList.collector());
        assertThat(actual).isEqualTo(JList.of(1, 2, 3));
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(JList.nil()).isEqualTo(Nil.instance());
    }

    // -- static of()

    @Test
    public void shouldCreateListOfListUsingCons() {
        assertThat(JList.of(JList.nil()).toString()).isEqualTo("List(List())");
    }

    // -- static of(T...)

    @Test
    public void shouldCreateListOfElements() {
        final JList<Integer> actual = JList.of(1, 2);
        final JList<Integer> expected = new Cons<>(1, new Cons<>(2, Nil.instance()));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static of(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
        assertThat(JList.ofAll(arrayList)).isEqualTo(JList.of(1, 2, 3));
    }

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateListOfRangeWhereFromIsGreaterThanTo() {
        assertThat(JList.rangeClosed(1, 0)).isEqualTo(JList.nil());
    }

    @Test
    public void shouldCreateListOfRangeWhereFromEqualsTo() {
        assertThat(JList.rangeClosed(0, 0)).isEqualTo(JList.of(0));
    }

    @Test
    public void shouldCreateListOfRangeWhereFromIsLessThanTo() {
        assertThat(JList.rangeClosed(1, 3)).isEqualTo(JList.of(1, 2, 3));
    }

    @Test
    public void shouldCreateListOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(JList.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(JList.of(Integer.MIN_VALUE));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateListOfUntilWhereFromIsGreaterThanTo() {
        assertThat(JList.range(1, 0)).isEqualTo(JList.nil());
    }

    @Test
    public void shouldCreateListOfUntilWhereFromEqualsTo() {
        assertThat(JList.range(0, 0)).isEqualTo(JList.nil());
    }

    @Test
    public void shouldCreateListOfUntilWhereFromIsLessThanTo() {
        assertThat(JList.range(1, 3)).isEqualTo(JList.of(1, 2));
    }

    @Test
    public void shouldCreateListOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(JList.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(JList.nil());
    }

    // -- combinations

        @Test
    public void shouldComputeCombinationsOfEmptyList() {
        assertThat(JList.nil().combinations()).isEqualTo(JList.of(JList.nil()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyList() {
        assertThat(JList.of(1, 2, 3).combinations()).isEqualTo(JList.of(JList.nil(), JList.of(1), JList.of(2), JList.of(3), JList.of(1, 2), JList.of(1, 3), JList.of(2, 3), JList.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyList() {
        assertThat(JList.nil().combinations(1)).isEqualTo(JList.nil());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyList() {
        assertThat(JList.of(1, 2, 3).combinations(2)).isEqualTo(JList.of(JList.of(1, 2), JList.of(1, 3), JList.of(2, 3)));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(JList.of(1).combinations(-1)).isEqualTo(JList.of(JList.nil()));
    }

    // -- peek

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyList() {
        assertThat(JList.nil().permutations()).isEqualTo(JList.nil());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyList() {
        assertThat(JList.of(1, 2, 3).permutations()).isEqualTo(JList.ofAll(JList.of(JList.of(1, 2, 3), JList.of(1, 3, 2), JList.of(2, 1, 3), JList.of(2, 3, 1), JList.of(3, 1, 2), JList.of(3, 2, 1))));
    }

    // -- unapply

    @Test
    public void shouldUnapplyNil() {
        assertThat(Nil.instance().unapply()).isEqualTo(Tuple.empty());
    }

    @Test
    public void shouldUnapplyCons() {
        assertThat(JList.of(1, 2, 3).unapply()).isEqualTo(Tuple.of(1, JList.of(2, 3)));
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
        Serializables.callReadObject(JList.of(1));
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotDeserializeListWithSizeLessThanOne() throws Throwable {
        try {
            /*
             * This implementation is stable regarding jvm impl changes of object serialization. The index of the
             * number of List elements is gathered dynamically.
             */
            final byte[] listWithOneElement = Serializables.serialize(JList.of(0));
            final byte[] listWithTwoElements = Serializables.serialize(JList.of(0, 0));
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
        final Arbitrary<Function<? super Integer, ? extends Monad<String, JTraversable<?>>>> mappers =
                size -> random -> i -> JList.of(i).map(String::valueOf);
        final CheckResult result = checkMonadLeftIdentity(JList::of, Arbitrary.integer(), mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadRightIdentity() {
        final Arbitrary<? extends Monad<Integer, JTraversable<?>>> lists = Arbitrary.list(Arbitrary.integer());
        final CheckResult result = checkMonadRightIdentity(JList::of, lists);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyMonadAssociativity() {
        final Arbitrary<? extends Monad<Integer, JTraversable<?>>> lists = Arbitrary.list(Arbitrary.integer());
        final Arbitrary<Function<? super Integer, ? extends Monad<Double, JTraversable<?>>>> before =
                size -> random -> i -> JList.of(i).map(Double::valueOf);
        final Arbitrary<Function<? super Double, ? extends Monad<String, JTraversable<?>>>> after =
                size -> random -> d -> JList.of(d).map(String::valueOf);
        final CheckResult result = checkMonadAssociativity(lists, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }
}
