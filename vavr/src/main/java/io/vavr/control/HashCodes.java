package io.vavr.control;

import java.util.Objects;

/**
 * Utility that allows to compute hashcodes without var-arg conversion into arrays for common
 * types of vavr.
 */
public interface HashCodes {

    static int hash(int value) {
        return Integer.hashCode(value);
    }

    static int hash(int v1, int v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(long value) {
        return Long.hashCode(value);
    }

    static int hash(byte value) {
        return Byte.hashCode(value);
    }

    static int hash(short value) {
        return Short.hashCode(value);
    }

    static int hash(char value) {
        return Character.hashCode(value);
    }

    static int hash(boolean value) {
        return Boolean.hashCode(value);
    }

    static int hash(float value) {
        return Float.hashCode(value);
    }

    static int hash(double value) {
        return Double.hashCode(value);
    }

    static int hash(Object value) {
        return Objects.hashCode(value);
    }

    static int hash(int v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(long v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(byte v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(short v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(char v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(boolean v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(float v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(double v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(Object v1, Object v2) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        return result;
    }

    static int hash(Object v1, Object v2, Object v3) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        return result;
    }

    static int hash(Object v1, Object v2, Object v3, Object v4) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        return result;
    }

    static int hash(Object v1, Object v2, Object v3, Object v4, Object v5) {
        int result = 1;
        result = 31 * result + hash(v1);
        result = 31 * result + hash(v2);
        result = 31 * result + hash(v3);
        result = 31 * result + hash(v4);
        result = 31 * result + hash(v5);
        return result;
    }

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
