/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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
package io.vavr;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Serializables {

    private Serializables() {
    }

    /**
     * Serializes a given object.
     *
     * @param obj An object.
     * @return IllegalStateException if an IOException occurs when writing the obj to the ObjectOutputStream.
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
     * <p>
     * Examples:
     *
     * <pre>
     * <code>
     * // variable assignment
     * final CharSequence s = deserialize(serialize("test"));
     *
     * // type casting
     * final int i = ((String) deserialize(serialize("test"))).length();
     * </code>
     * </pre>
     *
     * @param <T>        Target type the deserialized object will be casted to.
     * @param objectData A serialized object.
     * @return The deserialized Object.
     * @throws IllegalStateException if an IOException occurs when reading from the ObjectInputStream or the serialized
     *                               class cannot be found.
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
