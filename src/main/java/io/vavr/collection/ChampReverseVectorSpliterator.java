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
class ChampReverseVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
    private final Vector<Object> vector;
    private final Function<Object, K> mapper;
    private int index;
    private K current;

     ChampReverseVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, int fromIndex, int additionalCharacteristics, long est) {
        super(est, additionalCharacteristics);
        this.vector = vector;
        this.mapper = mapper;
        index = vector.size() - 1-fromIndex;
    }

    @Override
    public boolean tryAdvance(Consumer<? super K> action) {
        if (moveNext()) {
            action.accept(current);
            return true;
        }
        return false;
    }

     boolean moveNext() {
        if (index < 0) {
            return false;
        }
        Object o = vector.get(index--);
        if (o instanceof ChampTombstone) {
            ChampTombstone t = (ChampTombstone) o;
            index -= t.before();
            o = vector.get(index--);
        }
        current = mapper.apply(o);
        return true;
    }

     K current() {
        return current;
    }
}
