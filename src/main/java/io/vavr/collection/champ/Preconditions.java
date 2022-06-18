/*
 * @(#)Preconditions.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */
package io.vavr.collection.champ;


/**
 * Preconditions.
 *
 * @author Werner Randelshofer
 */
public class Preconditions {
    private Preconditions() {

    }

    /**
     * Throws an illegal argument exception with a formatted message
     * if the expression is not true.
     *
     * @param expression           an expression
     * @param errorMessageTemplate the template for the error message
     * @param arguments            arguments for the error message
     * @throws IllegalArgumentException if expression is not true
     */
    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... arguments) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, arguments));
        }
    }

    /**
     * Checks if the provided value is in the range {@code [min, max]}.
     *
     * @param value a value
     * @param min   the lower bound of the range (inclusive)
     * @param max   the upper bound of the range (inclusive)
     * @param name  the name of the value
     * @return the value
     * @throws IllegalArgumentException if value is not in [min, max].
     */
    public static int checkValueInRange(int value, int min, int max, String name) {
        if (value < min || value >= max) {
            throw new IllegalArgumentException(name + ": " + value + " not in range: [" + min + ", " + max + "].");
        }
        return value;
    }

    /**
     * Checks if the provided index is in the range {@code [0, length)}.
     *
     * @param index  an index value
     * @param length the size value (exclusive)
     * @return the index value
     * @throws IndexOutOfBoundsException if index is not in {@code [0, length)}.
     */
    public static int checkIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index: " + index + " not in range: [0, " + length + ").");
        }
        return index;
    }

    /**
     * Checks if the provided sub-range {@code [from, to)} is inside the
     * range {@code [0, length)}, and whether {@code from <= to}.
     *
     * @param from   the lower bound of the sub-range (inclusive)
     * @param to     the upper bound of the sub-range (exclusive)
     * @param length the upper bound of the range (exclusive)
     * @return the from value
     * @throws IndexOutOfBoundsException if the sub-range is not in {@code [0, length)}.
     */
    public static int checkFromToIndex(int from, int to, int length) {
        if (from < 0 || from > to || to > length) {
            throw new IndexOutOfBoundsException("sub-range: [" + from + ", " + to + ") not in range: [0, " + length + ").");
        }
        return from;
    }

    /**
     * Checks if the provided sub-range {@code [from, from+size)} is inside the
     * range {@code [0, length)} and whether {@code 0 <= size}.
     *
     * @param from   the lower bound of the sub-range (inclusive)
     * @param size   the size of the sub-range
     * @param length the upper bound of the range (exclusive)
     * @return the from value
     * @throws IndexOutOfBoundsException if the sub-range is not in {@code [0, length)}.
     */
    public static int checkFromIndexSize(int from, int size, int length) {
        if (from < 0 || size < 0 || from + size > length) {
            throw new IndexOutOfBoundsException("sub-range: [" + from + ", " + (from + size) + ") not in range: [0, " + length + ").");
        }
        return from;
    }
}
