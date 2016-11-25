package javaslang.collection;

import org.junit.Test;

import java.util.Comparator;

import static javaslang.collection.Comparators.naturalComparator;
import static org.assertj.core.api.Assertions.assertThat;

public class ComparatorsTest {

    // -- naturalComparator()

    @Test
    public void shouldCompareTwoIntegersUsingNaturealOrder() {
        final Comparator<Integer> comparator = naturalComparator();
        assertThat(comparator.compare(0, 1)).isEqualTo(-1);
        assertThat(comparator.compare(2, -1)).isEqualTo(1);
        assertThat(comparator.compare(3, 3)).isEqualTo(0);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenComparingNullAndIntegerUsingNaturalOrder() {
        naturalComparator().compare(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenComparingIntegerAndNullUsingNaturalOrder() {
        naturalComparator().compare(1, null);
    }

}
