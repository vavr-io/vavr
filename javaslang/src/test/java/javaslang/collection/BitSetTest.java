package javaslang.collection;

import org.junit.Test;

public class BitSetTest {

    private enum E {
        V1, V2, V3
    }

    @Test
    public void test1() {
        BitSet<Integer> bs = BitSet.empty();
        bs = bs.add(2);
        bs = bs.add(4);
        bs = bs.remove(2);
        assert !bs.contains(2);
        assert bs.contains(4);
    }

    @Test
    public void test2() {
        BitSet<E> bs = BitSet.withEnum(E.class).empty();
        bs = bs.add(E.V2);
        bs = bs.add(E.V3);
        bs = bs.remove(E.V2);
        assert !bs.contains(E.V2);
        assert bs.contains(E.V3);
    }
}
