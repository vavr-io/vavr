/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Random;

/**
 * Internal class, containing Java array manipulation helpers.
 * Many arrays are represented as simple Objects, to avoid casts at the client site, and to work with all types of arrays, including primitive ones.
 *
 * @author Pap Lőrinc
 * @since 3.0.0
 */
@SuppressWarnings({"unchecked", "SuspiciousArrayCast"})
final class Arrays2 { // TODO reuse these in `Array` also
    private static final Object[] EMPTY = {};

    /** Randomly mutate array positions */
    static <T> int[] shuffle(int[] array, Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    static <T> void swap(int[] array, int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
