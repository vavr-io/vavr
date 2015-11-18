package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

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

    @Test
    public void shouldKeepOrder() {
        CharSeq actual = LinkedHashMap.<Integer, Character>empty().put(3, 'a').put(2, 'b').put(1, 'c').foldLeft(CharSeq.empty(), (s, t) -> s.append(t._2));
        assertThat(actual).isEqualTo(CharSeq.of("abc"));
    }
}
