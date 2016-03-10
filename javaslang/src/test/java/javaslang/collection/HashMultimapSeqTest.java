package javaslang.collection;

import org.junit.Test;

public class HashMultimapSeqTest extends AbstractMultimapTest {

    @Override
    protected Multimap.ContainerType containerType() {
        return Multimap.ContainerType.SEQ;
    }

    @Override
    protected Multimap.MapType mapType() {
        return Multimap.MapType.HASH_MAP;
    }

    @Override
    protected String className() {
        return "Multimap[HashMap,List]";
    }

    @Test
    public void test1() {
        Multimap<Integer, String> multimap = emptyMap();
        multimap = multimap.put(1, "a").put(1, "b").put(1, "b");
        assertThat(multimap.toString()).isEqualTo(className() + "((1, a), (1, b), (1, b))");
    }
}
