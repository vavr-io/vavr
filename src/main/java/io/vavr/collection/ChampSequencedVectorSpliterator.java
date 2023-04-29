/*
 * @(#)VectorSpliterator.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;



import java.util.Spliterators;
import java.util.function.Function;

 abstract class ChampSequencedVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
   // private final  BitMappedTrie.MySpliterator<Object> vector;
    private final  Function<Object, K> mapper;
    private int index;

    public ChampSequencedVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, long est, int additionalCharacteristics) {
        super(est, additionalCharacteristics);
  //      this.vector = new BitMappedTrie.MySpliterator<>(vector, 0, 0);
        this.mapper = mapper;
    }
/*
    @Override
    public boolean moveNext() {
        boolean success = vector.moveNext();
        if (!success) return false;
        if (vector.current() instanceof ChampTombstone t) {
            vector.skip(t.after());
            vector.moveNext();
        }
        current = mapper.apply(vector.current());
        return true;
    }
    */
}
