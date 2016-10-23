/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

 /**
 * short[] helper to replace reflective array access.
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class ShortArrayType extends ArrayType<Short> {
    private static final long serialVersionUID = 1L;
    static final ShortArrayType INSTANCE = new ShortArrayType();
    static final short[] EMPTY = new short[0];

    private static short[] cast(Object array) { return (short[]) array; }

    @Override
    Class<Short> type() { return short.class; }

    @Override
    short[] empty() { return EMPTY; }

    @Override
    int lengthOf(Object array) { return (array == null) ? 0 : cast(array).length; }

    @Override
    Short getAt(Object array, int index) { return cast(array)[index]; }

    @Override
    void setAt(Object array, int index, Object value) { cast(array)[index] = (Short) value; }

    @Override
    Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
        if (size == 0) {
            return new short[arraySize];
        } else {
            final short[] result = new short[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }
}