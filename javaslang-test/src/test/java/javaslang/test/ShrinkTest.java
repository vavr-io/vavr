package javaslang.test;

import javaslang.CheckedFunction1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import java.util.function.BiPredicate;

import static java.lang.Math.abs;
import static javaslang.test.Checkable.DEFAULT_TRIES;
import static javaslang.test.Property.def;
import static javaslang.test.Shrink.integer;

public class ShrinkTest {

    private static final int SIZE = 20;

    private static <L, R> CheckedFunction1<Tuple2<L, R>, Boolean> pair(BiPredicate<L, R> predicate) {
        return t -> predicate.test(t._1, t._2);
    }

    @Test public void integers() {
        final Arbitrary<Tuple2<Integer, Stream<Integer>>> integerShrinks = Arbitrary.integer().map((value) ->
                Tuple.of(value, integer().apply(value)));

        def("integer: Doesn't contain initial value")
                .forAll(integerShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.contains(value)))
                .check()
                .assertIsSatisfied();

        def("integer: Decreases absolute value")
                .forAll(integerShrinks)
                .suchThat(pair((value, shrinks) -> shrinks.forAll(shrunk -> abs(shrunk) < abs(value))))
                .check()
                .assertIsSatisfied();

        def("integer: Reduces to zero")
                .forAll(integerShrinks)
                .suchThat(pair((value, shrinks) -> shrinks.contains(0) || shrinks.isEmpty()))
                .check()
                .assertIsSatisfied();
    }

    @Test public void filter() {
        final Shrink<Integer> positiveShrink = Shrink.integer().filter(i -> i >= 0);
        def("filter: only positive")
                .forAll(Arbitrary.integer().map(positiveShrink::apply))
                .suchThat(shrink -> shrink.forAll(i -> i >= 0))
                .check()
                .assertIsSatisfied();

        final Shrink<Integer> evenShrink = Shrink.integer().filter(i -> i % 2 == 0);
        def("filter: only even")
                .forAll(Arbitrary.integer().map(evenShrink::apply))
                .suchThat(shrink -> shrink.forAll(i -> i % 2 == 0))
                .check()
                .assertIsSatisfied();

        final Shrink<List<Integer>> fixedSizeLists = Shrink.list(Shrink.integer()).filter(l -> l.length() == 12);
        def("filter: fixed size lists")
                .forAll(Arbitrary.list(Arbitrary.integer()).map(fixedSizeLists::apply))
                .suchThat(shrink -> shrink.forAll(l -> l.length() == 12))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();
    }
    @Test public void list() throws Exception {
        final Arbitrary<Tuple2<List<Integer>, Stream<List<Integer>>>> listShrinks =
                Arbitrary.list(Arbitrary.integer()).map(list -> Tuple.of(list, Shrink.list(Shrink.integer()).apply(list)));

        def("list: Not contains initial value")
                .forAll(listShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.contains(value)))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();

        def("list: Shrinks length")
                .forAll(listShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.exists(list -> list.size() > value.size())))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();

        def("list: Shrinks values")
                .forAll(listShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.exists(l -> l.map(Math::abs).max().exists(m -> value.map(Math::abs).max().exists(m2 -> m2 < m)))))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();
    }

    @Test
    public void string() throws Exception {
        final Arbitrary<String> strings = Arbitrary.string(
                Gen.frequency(
                        Tuple.of(1, Gen.choose('A', 'Z')),
                        Tuple.of(1, Gen.choose('a', 'z')),
                        Tuple.of(1, Gen.choose('0', '9')),
                        Tuple.of(1, Gen.choose('а', 'я')),
                        Tuple.of(1, Gen.choose('А', 'Я'))
                ));

        final Arbitrary<Tuple2<String, Stream<String>>> stringShrinks = strings.map(s -> Tuple.of(s, Shrink.string().apply(s)));

        def("string: Not contains initial value")
                .forAll(stringShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.contains(value)))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();

        def("string: Shrinks length")
                .forAll(stringShrinks)
                .suchThat(pair((value, shrinks) -> !shrinks.exists(str -> str.length() > value.length())))
                .check(SIZE, DEFAULT_TRIES)
                .assertIsSatisfied();
    }
}