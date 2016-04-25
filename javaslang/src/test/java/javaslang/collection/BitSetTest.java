package javaslang.collection;

import org.junit.Test;

public class BitSetTest {

    @Test
    public void test1() {
        BitSet bs = BitSet.empty();
        bs = bs.add(2);
        bs = bs.add(4);
        bs = bs.remove(2);
        assert !bs.contains(2);
        assert bs.contains(4);
    }
}
