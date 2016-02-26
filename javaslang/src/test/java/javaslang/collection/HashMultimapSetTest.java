package javaslang.collection;

import org.junit.Test;

public class HashMultimapSetTest {

    @Test
    public void test() {
        HashMultimapSet<Integer, String> hms = HashMultimapSet.empty();
        hms = hms.put(1, "a").put(1, "b");
        hms = hms.put(2, "c").put(2, "d");
        hms = hms.put(3, "e");
        hms = hms.remove(2, "c");
        System.out.println(hms);
        System.out.println(hms.toJavaMap());
    }
}
