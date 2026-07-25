/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that objects serialized by an earlier Vavr release can still be deserialized.
 * <p>
 * The fixtures under {@code /io/vavr/collection/serialization/v1_0_1} were produced by the released
 * {@code io.vavr:vavr:1.0.1} artifact and must never be regenerated: their whole purpose is to pin
 * the on-the-wire format that {@code 1.x} users already have in caches, sessions and message queues.
 */
class LegacySerializationTest {

    private static final String FIXTURES = "/io/vavr/collection/serialization/v1_0_1/";

    @SuppressWarnings("unchecked")
    private static <T> T readFixture(String name) {
        final String resource = FIXTURES + name + ".ser";
        try (InputStream in = LegacySerializationTest.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("missing fixture " + resource);
            }
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(in.readAllBytes()))) {
                return (T) stream.readObject();
            }
        } catch (IOException x) {
            throw new UncheckedIOException(x);
        } catch (ClassNotFoundException x) {
            throw new IllegalStateException(x);
        }
    }

    @Test
    void shouldDeserializeLinkedHashMapWrittenBy1_0_1() {
        final LinkedHashMap<String, Integer> actual = readFixture("LinkedHashMap");
        assertThat(actual).isEqualTo(LinkedHashMap.of("a", 1).put("b", 2).put("c", 3));
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.keySet().toJavaList()).containsExactly("a", "b", "c");
    }

    /**
     * {@code LinkedHashMap} holds its key order in a {@code Vector<K>} since 1.1.0, but must keep writing the
     * {@code Queue<Tuple2<K, V>>} shape that 1.0.1 reads, so streams stay compatible in both directions.
     */
    @Test
    void shouldPinLinkedHashMapSerializedFormToTheLegacyFieldShape() {
        final ObjectStreamClass descriptor = ObjectStreamClass.lookup(LinkedHashMap.class);
        assertThat(descriptor.getField("list")).isNotNull();
        assertThat(descriptor.getField("list").getType()).isEqualTo(Queue.class);
        assertThat(descriptor.getField("map")).isNotNull();
        assertThat(descriptor.getField("map").getType()).isEqualTo(HashMap.class);
    }

    @Test
    void shouldDeserializeLinkedHashSetWrittenBy1_0_1() {
        final LinkedHashSet<String> actual = readFixture("LinkedHashSet");
        assertThat(actual).isEqualTo(LinkedHashSet.of("a", "b", "c"));
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.toJavaList()).containsExactly("a", "b", "c");
    }

    @Test
    void shouldDeserializeLinkedHashMultimapWrittenBy1_0_1() {
        final LinkedHashMultimap<String, Integer> actual = readFixture("LinkedHashMultimap");
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.toJavaList())
                .containsExactly(Tuple.of("a", 1), Tuple.of("a", 2), Tuple.of("b", 3));
    }

    @Test
    void shouldDeserializeHashMultimapWrittenBy1_0_1() {
        final HashMultimap<String, Integer> actual = readFixture("HashMultimap");
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.get("a").get().toJavaList()).containsExactly(1, 2);
    }

    @Test
    void shouldDeserializeTreeMultimapWrittenBy1_0_1() {
        final TreeMultimap<String, Integer> actual = readFixture("TreeMultimap");
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.get("a").get().toJavaList()).containsExactly(1, 2);
    }
}
