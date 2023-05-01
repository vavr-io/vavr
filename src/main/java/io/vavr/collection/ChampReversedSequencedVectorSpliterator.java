/*
 * @(#)VectorSpliterator.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;


import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <K>
 */
class ChampReversedSequencedVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
    private final Vector<Object> vector;
    private final Function<Object, K> mapper;
    private int index;
    private K current;

    public ChampReversedSequencedVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, int additionalCharacteristics, long est) {
        super(est, additionalCharacteristics);
        this.vector = vector;
        this.mapper = mapper;
        index = vector.size() - 1;
    }

    @Override
    public boolean tryAdvance(Consumer<? super K> action) {
        if (moveNext()) {
            action.accept(current);
            return true;
        }
        return false;
    }

    public boolean moveNext() {
        if (index < 0) {
            return false;
        }
        Object o = vector.get(index--);
        if (o instanceof ChampTombstone t) {
            index -= t.before();
            o = vector.get(index--);
        }
        current = mapper.apply(o);
        return true;
    }

    public K current() {
        return current;
    }
}
