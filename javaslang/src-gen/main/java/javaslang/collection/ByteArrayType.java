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
 * byte[] helper to replace reflective array access.
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class ByteArrayType extends ArrayType<Byte> {
    private static final long serialVersionUID = 1L;
    static final ByteArrayType INSTANCE = new ByteArrayType();
    static final byte[] EMPTY = new byte[0];

    private static byte[] cast(Object array) { return (byte[]) array; }

    @Override
    Class<Byte> type() { return byte.class; }

    @Override
    byte[] empty() { return EMPTY; }

    @Override
    int lengthOf(Object array) { return (array == null) ? 0 : cast(array).length; }

    @Override
    Byte getAt(Object array, int index) { return cast(array)[index]; }

    @Override
    void setAt(Object array, int index, Object value) { cast(array)[index] = (Byte) value; }

    @Override
    Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
        if (size == 0) {
            return new byte[arraySize];
        } else {
            final byte[] result = new byte[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }
}