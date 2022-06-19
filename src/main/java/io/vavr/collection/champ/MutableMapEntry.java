package io.vavr.collection.champ;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class MutableMapEntry<K, V> extends AbstractMap.SimpleEntry<K, V> {
    private final static long serialVersionUID = 0L;
    private final BiConsumer<K, V> putFunction;

    public MutableMapEntry(BiConsumer<K, V> putFunction, K key, V value) {
        super(key, value);
        this.putFunction = putFunction;
    }

    @Override
    public V setValue(V value) {
        V oldValue = super.setValue(value);
        putFunction.accept(getKey(), value);
        return oldValue;
    }
}
