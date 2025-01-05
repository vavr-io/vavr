/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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
package io.vavr.control;

import java.util.List;
import java.util.Objects;

// TODO should we generate this file and add more permutations of parameter types?

/**
 * Utility that allows to compute hashcodes without var-arg conversion into arrays for common
 * types of vavr.
 *
 * @deprecated Will be removed from public API
 */
@Deprecated
public interface HashCodes {

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Integer#hashCode()
     */
    static int hash(int value) {
        return Integer.hashCode(value);
    }

    /**
     * Return the order-dependent hash of the two given integers.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(int v1, int v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Long#hashCode()
     */
    static int hash(long value) {
        return Long.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Byte#hashCode()
     */
    static int hash(byte value) {
        return Byte.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Short#hashCode()
     */
    static int hash(short value) {
        return Short.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Character#hashCode()
     */
    static int hash(char value) {
        return Character.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Boolean#hashCode()
     */
    static int hash(boolean value) {
        return Boolean.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Float#hashCode()
     */
    static int hash(float value) {
        return Float.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Double#hashCode()
     */
    static int hash(double value) {
        return Double.hashCode(value);
    }

    /**
     * Returns the hashcode of the given value.
     *
     * @param value the value to hash
     * @return the hashcode
     * @see Objects#hashCode()
     */
    static int hash(Object value) {
        return Objects.hashCode(value);
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(int v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(long v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(byte v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(short v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(char v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(boolean v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(float v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(double v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the two given values.
     * <p>
     * More formally, it returns the value {@code 31 * (31 + hash(v1)) + hash(v2)}.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    /**
     * Return the order-dependent hash of the three given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @param v4 the fourth value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3, Object v4) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @param v4 the fourth value to hash
     * @param v5 the fifth value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3, Object v4, Object v5) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        result = 31 * result + hash(v5);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @param v4 the fourth value to hash
     * @param v5 the fifth value to hash
     * @param v6 the sixth value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3, Object v4, Object v5, Object v6) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        result = 31 * result + hash(v5);
        result = 31 * result + hash(v6);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @param v4 the fourth value to hash
     * @param v5 the fifth value to hash
     * @param v6 the sixth value to hash
     * @param v7 the seventh value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3, Object v4, Object v5, Object v6, Object v7) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        result = 31 * result + hash(v5);
        result = 31 * result + hash(v6);
        result = 31 * result + hash(v7);
        return result;
    }

    /**
     * Return the order-dependent hash of the two given values.
     * <p>
     * The hashcode is equivalent to the hashcode of a {@link java.util.List} containing the given values.
     *
     * @param v1 the first value to hash
     * @param v2 the second value to hash
     * @param v3 the third value to hash
     * @param v4 the fourth value to hash
     * @param v5 the fifth value to hash
     * @param v6 the sixth value to hash
     * @param v7 the seventh value to hash
     * @param v8 the eighth value to hash
     * @return the hashcode
     * @see List#hashCode()
     * @see java.util.Arrays#hashCode(Object[])
     */
    static int hash(Object v1, Object v2, Object v3, Object v4, Object v5, Object v6, Object v7, Object v8) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        result = 31 * result + hash(v5);
        result = 31 * result + hash(v6);
        result = 31 * result + hash(v7);
        result = 31 * result + hash(v8);
        return result;
    }

}
