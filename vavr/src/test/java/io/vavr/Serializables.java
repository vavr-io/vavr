/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Serializables {

  private Serializables() {}

  /**
   * Serializes a given object.
   *
   * @param obj An object.
   * @return IllegalStateException if an IOException occurs when writing the obj to the
   *     ObjectOutputStream.
   */
  public static byte[] serialize(Object obj) {
    try (ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(buf)) {
      stream.writeObject(obj);
      return buf.toByteArray();
    } catch (IOException x) {
      throw new IllegalStateException("Error serializing object", x);
    }
  }

  /**
   * Deserializes a given object, returning the correct target type of assignment or cast.
   *
   * <p>Examples:
   *
   * <pre>{@code
   * // variable assignment
   * final CharSequence s = deserialize(serialize("test"));
   *
   * // type casting
   * final int i = ((String) deserialize(serialize("test"))).length();
   * }</pre>
   *
   * @param <T> Target type the deserialized object will be casted to.
   * @param objectData A serialized object.
   * @return The deserialized Object.
   * @throws IllegalStateException if an IOException occurs when reading from the ObjectInputStream
   *     or the serialized class cannot be found.
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserialize(byte[] objectData) {
    try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
      return (T) stream.readObject();
    } catch (IOException | ClassNotFoundException x) {
      throw new IllegalStateException("Error deserializing object", x);
    }
  }

  public static void callReadObject(Object o) throws Throwable {
    final byte[] objectData = Serializables.serialize(o);
    try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
      final Method method = o.getClass().getDeclaredMethod("readObject", ObjectInputStream.class);
      method.setAccessible(true);
      try {
        method.invoke(o, stream);
      } catch (InvocationTargetException x) {
        throw (x.getCause() != null) ? x.getCause() : x;
      }
    }
  }
}
