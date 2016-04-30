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
        assert bs.head() == 2;

        bs = bs.add(4);
        assert bs.head() == 2;

        bs = bs.add(70);
        assert bs.head() == 2;

        bs = bs.add(300);
        assert bs.head() == 2;

        bs = bs.remove(2);
        assert bs.head() == 4;
        assert !bs.contains(2);

        bs = bs.remove(4);
        assert bs.head() == 70;
        assert !bs.contains(4);

        bs = bs.remove(70);
        assert bs.head() == 300;
        assert !bs.contains(70);

        assert bs.contains(300);
    }

    @Test
    public void test2() {
        BitSet<E> bs = BitSet.withEnum(E.class).empty();
        bs = bs.add(E.V2);
        assert bs.head() == E.V2;
        bs = bs.add(E.V3);
        assert bs.head() == E.V2;
        bs = bs.remove(E.V2);
        assert bs.head() == E.V3;
        assert !bs.contains(E.V2);
        assert bs.contains(E.V3);
    }

    @Test
    public void test3() {
        BitSet<Integer> bs = BitSet.empty();
        assert bs.add(1).add(2).init().toList().equals(List.of(1));
        assert bs.add(1).add(70).init().toList().equals(List.of(1));
        assert bs.add(1).add(700).init().toList().equals(List.of(1));
    }
}
