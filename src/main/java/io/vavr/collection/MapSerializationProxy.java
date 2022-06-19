/*
 * @(#)MapSerializationProxy.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A serialization proxy that serializes a map independently of its internal
 * structure.
 * <p>
 * Usage:
 * <pre>
 * class MyMap&lt;K, V&gt; implements Map&lt;K, V&gt;, Serializable {
 *   private final static long serialVersionUID = 0L;
 *
 *   private Object writeReplace() throws ObjectStreamException {
 *      return new SerializationProxy&lt;&gt;(this);
 *   }
 *
 *   static class SerializationProxy&lt;K, V&gt;
 *                  extends MapSerializationProxy&lt;K, V&gt; {
 *      private final static long serialVersionUID = 0L;
 *      SerializationProxy(Map&lt;K, V&gt; target) {
 *          super(target);
 *      }
 *     {@literal @Override}
 *      protected Object readResolve() {
 *          return new MyMap&lt;&gt;(deserialized);
 *      }
 *   }
 * }
 * </pre>
 * <p>
 * References:
 * <dl>
 *     <dt>Java Object Serialization Specification: 2 - Object Output Classes,
 *     2.5 The writeReplace Method</dt>
 *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/output.html#the-writereplace-method"></a>oracle.com</dd>
 *
 *     <dt>Java Object Serialization Specification: 3 - Object Input Classes,
 *     3.7 The readResolve Method</dt>
 *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/input.html#the-readresolve-method"></a>oracle.com</dd>
 * </dl>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
abstract class MapSerializationProxy<K, V> implements Serializable {
    private final transient Map<K, V> serialized;
    protected transient List<Map.Entry<K, V>> deserialized;
    private final static long serialVersionUID = 0L;

    protected MapSerializationProxy(Map<K, V> serialized) {
        this.serialized = serialized;
    }

    private void writeObject(ObjectOutputStream s)
            throws IOException {
        s.writeInt(serialized.size());
        for (Map.Entry<K, V> entry : serialized.entrySet()) {
            s.writeObject(entry.getKey());
            s.writeObject(entry.getValue());
        }
    }

    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        int n = s.readInt();
        deserialized = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            @SuppressWarnings("unchecked")
            K key = (K) s.readObject();
            @SuppressWarnings("unchecked")
            V value = (V) s.readObject();
            deserialized.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        }
    }

    protected abstract Object readResolve();
}
