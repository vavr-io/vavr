package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMultimapTest {

    @Test
    public void test1() {
        HashMultimap<Integer, String, Set<String>> hms = HashMultimap.emptyWithSet();
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("HashMultimap[Set]((1, a), (1, b))");
    }

    @Test
    public void test2() {
        HashMultimap<Integer, String, Seq<String>> hms = HashMultimap.emptyWithSeq();
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("HashMultimap[Seq]((1, a), (1, b), (1, b))");
    }
}
