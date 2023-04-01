/*
 * @(#)Enumerator.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */
package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Interface for enumerating elements of a collection.
 * <p>
 * The protocol for accessing elements via a {@code Enumerator} imposes smaller per-element overhead than
 * {@link Iterator}, and avoids the inherent race involved in having separate methods for
 * {@code hasNext()} and {@code next()}.
 *
 * @param <E> the element type
 * @author Werner Randelshofer
 */
interface EnumeratorSpliterator<E> extends Enumerator<E>, Spliterator<E> {
    @Override
    default boolean tryAdvance(@NonNull Consumer<? super E> action) {
        if (moveNext()) {
            action.accept(current());
            return true;
        }
        return false;
    }


}
