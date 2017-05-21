/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.Tuple2;

import java.util.Objects;
import java.util.function.Function;

/**
 * INTERNAL: Common {@code Multimap} functions (not intended to be public).
 *
 * @author Ruslan Sennov, Daniel Dietrich
 */
class Multimaps {

    @SuppressWarnings("unchecked")
    static <K, V, M extends Multimap<K, V>> M ofJavaMap(M source, java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        return Stream.ofAll(map.entrySet()).foldLeft(source, (m, el) -> (M) m.put(el.getKey(), el.getValue()));
    }

    @SuppressWarnings("unchecked")
    static <T, K, V, M extends Multimap<K, V>> M ofStream(M source, java.util.stream.Stream<? extends T> stream,
                                                     Function<? super T, ? extends K> keyMapper,
                                                     Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Stream.ofAll(stream).foldLeft(source, (m, el) -> (M) m.put(keyMapper.apply(el), valueMapper.apply(el)));
    }

    @SuppressWarnings("unchecked")
    static <T, K, V, M extends Multimap<K, V>> M ofStream(M source, java.util.stream.Stream<? extends T> stream,
                                                     Function<? super T, Tuple2<? extends K, ? extends V>> entryMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(entryMapper, "entryMapper is null");
        return Stream.ofAll(stream).foldLeft(source, (m, el) -> (M) m.put(entryMapper.apply(el)));
    }
}
