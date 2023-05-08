/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.vavr.collection;


import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A spliterator for a {@code VectorMap} or {@code VectorSet}.
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
 *
 * @param <K> the key type
 */
class ChampVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
    private final BitMappedTrie.BitMappedTrieSpliterator<Object> vector;
    private final Function<Object, K> mapper;
    private K current;

     ChampVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, int fromIndex, long est, int additionalCharacteristics) {
        super(est, additionalCharacteristics);
        this.vector = new BitMappedTrie.BitMappedTrieSpliterator<>(vector.trie, fromIndex, 0);
        this.mapper = mapper;
    }

    @Override
    public boolean tryAdvance(Consumer<? super K> action) {
        if (moveNext()) {
            action.accept(current);
            return true;
        }
        return false;
    }

     K current() {
        return current;
    }

     boolean moveNext() {
        boolean success = vector.moveNext();
        if (!success) return false;
        if (vector.current() instanceof ChampTombstone t) {
            vector.skip(t.after());
            vector.moveNext();
        }
        current = mapper.apply(vector.current());
        return true;
    }
}
