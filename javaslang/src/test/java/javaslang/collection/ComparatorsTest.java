package javaslang.collection;

import javaslang.*;
import javaslang.collection.Comparators.SerializableComparator;
import org.junit.Test;

import java.util.Comparator;

import static java.util.Comparator.naturalOrder;
import static javaslang.collection.Comparators.naturalComparator;
import static org.assertj.core.api.Assertions.assertThat;

public class ComparatorsTest {
    private static final List<Integer> VALUES = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6);

    @Test
    public void testNaturalComparator() throws Exception {
        final List<Integer> actual = VALUES.sorted(naturalComparator());

        final java.util.List<Integer> expected = VALUES.toJavaList();
        expected.sort(naturalOrder());

        assertThat(actual.toJavaList()).isEqualTo(expected);
    }

    @Test
    @SuppressWarnings({ "NumberEquality", "RedundantTypeArguments" })
    public void testDefaultComparisons() throws Exception {
        final List<Tuple2<Integer, Integer>> pairs = VALUES.combinations(2).map(list -> Tuple.of(list.head(), list.last()));
        final SerializableComparator<Integer> comparator = SerializableComparator.of(Comparator.<Integer> naturalOrder());
        for (Tuple2<Integer, Integer> pair : pairs) {
            assertThat(comparator.isLess(pair._1, pair._2)).isEqualTo(pair._1 < pair._2);
            assertThat(comparator.isLessOrEqual(pair._1, pair._2)).isEqualTo(pair._1 <= pair._2);
            assertThat(comparator.isEqual(pair._1, pair._2)).isEqualTo(pair._1 == pair._2);
            assertThat(comparator.isNotEqual(pair._1, pair._2)).isEqualTo(pair._1 != pair._2);
            assertThat(comparator.isGreaterOrEqual(pair._1, pair._2)).isEqualTo(pair._1 >= pair._2);
            assertThat(comparator.isGreater(pair._1, pair._2)).isEqualTo(pair._1 > pair._2);
        }
    }
}