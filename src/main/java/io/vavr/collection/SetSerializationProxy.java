package io.vavr.collection;

import java.io.IOException;
import java.io.Serializable;

/**
 * A serialization proxy that serializes a set independently of its internal
 * structure.
 * <p>
 * Usage:
 * <pre>
 * class MySet&lt;E&gt; implements Set&lt;E&gt;, Serializable {
 *   private final static long serialVersionUID = 0L;
 *
 *   private Object writeReplace() throws ObjectStreamException {
 *      return new SerializationProxy&lt;&gt;(this);
 *   }
 *
 *   static class SerializationProxy&lt;E&gt;
 *                  extends SetSerializationProxy&lt;E&gt; {
 *      private final static long serialVersionUID = 0L;
 *      SerializationProxy(Set&lt;E&gt; target) {
 *          super(target);
 *      }
 *     {@literal @Override}
 *      protected Object readResolve() {
 *          return new MySet&lt;&gt;(deserialized);
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
 * @param <E> the element type
 */
abstract class SetSerializationProxy<E> implements Serializable {
    private final static long serialVersionUID = 0L;
    private final transient java.util.Set<E> serialized;
    protected transient java.util.List<E> deserialized;

    protected SetSerializationProxy(java.util.Set<E> serialized) {
        this.serialized = serialized;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws IOException {
        s.writeInt(serialized.size());
        for (E e : serialized) {
            s.writeObject(e);
        }
    }

    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        int n = s.readInt();
        deserialized = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            @SuppressWarnings("unchecked")
            E e = (E) s.readObject();
            deserialized.add(e);
        }
    }

    protected abstract Object readResolve();
}
