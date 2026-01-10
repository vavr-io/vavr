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
package io.vavr.control;

import java.util.List;
import java.util.Objects;

/**
 * Utility that allows to compute hashcodes without var-arg conversion into arrays for common
 * types of vavr.
 *
 * @author Sebastian Zarnekow
 */
class HashCodes {

    private HashCodes() {
    }

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
