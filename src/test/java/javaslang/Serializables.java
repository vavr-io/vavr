/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Serializables {

    /**
     * This class is not intended to be instantiated.
     */
    private Serializables() {
        throw new AssertionError(Serializables.class.getName() + " is not intended to be instantiated.");
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
