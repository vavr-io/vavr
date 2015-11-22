package javaslang.collection;

import java.util.ArrayList;
import java.util.stream.Collector;

import javaslang.Tuple;
import javaslang.Tuple2;

import org.junit.Assert;
import org.junit.Test;

public class LinkedHashMapTest extends AbstractMapTest {
    @Override
    protected String className() {
        return "LinkedHashMap";
    }

    @Override
    protected <T1, T2> Map<T1, T2> emptyMap() {
        return LinkedHashMap.empty();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return LinkedHashMap.<Integer, T> collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOf(Tuple2<? extends K, ? extends V>... entries) {
        return LinkedHashMap.ofAll(entries);
    }

    @Override
    protected <K extends Comparable<? super K>, V> Map<K, V> of(K key, V value) {
        return LinkedHashMap.of(key, value);
    }

    @Test
    public void shouldKeepOrder() {
        CharSeq actual = LinkedHashMap.<Integer, Character>empty().put(3, 'a').put(2, 'b').put(1, 'c').foldLeft(CharSeq.empty(), (s, t) -> s.append(t._2));
        assertThat(actual).isEqualTo(CharSeq.of("abc"));
    }
    
    @Test
    public void shouldScanRight() {
        Map<Integer, String> map = this.<Integer, String>emptyMap()
                .put(Tuple.of(1, "a"))
                .put(Tuple.of(2, "b"))
                .put(Tuple.of(3, "c"))
                .put(Tuple.of(4, "d"));
        Traversable<Tuple2<Integer,String>> result = map.scanRight(Tuple.of(0, "x"), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        Assert.assertTrue(result.contains(Tuple.of(10, "abcdx")));
        Assert.assertTrue(result.contains(Tuple.of( 9, "bcdx")));
        Assert.assertTrue(result.contains(Tuple.of( 7, "cdx")));
        Assert.assertTrue(result.contains(Tuple.of( 4, "dx")));
        Assert.assertTrue(result.contains(Tuple.of( 0, "x")));
    }

}
