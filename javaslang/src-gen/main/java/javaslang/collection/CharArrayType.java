/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;

/**
 * char[] helper to replace reflective array access.
 *
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class CharArrayType implements ArrayType<Character>, Serializable {
    private static final long serialVersionUID = 1L;
    static final CharArrayType INSTANCE = new CharArrayType();
    static final char[] EMPTY = new char[0];

    private static char[] cast(Object array) { return (char[]) array; }

    @Override
    public Class<Character> type() { return char.class; }

    @Override
    public char[] empty() { return EMPTY; }

    @Override
    public int lengthOf(Object array) { return (array == null) ? 0 : cast(array).length; }

    @Override
    public Character getAt(Object array, int index) { return cast(array)[index]; }

    @Override
    public void setAt(Object array, int index, Object value) { cast(array)[index] = (Character) value; }

    @Override
    public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
        if (size == 0) {
            return new char[arraySize];
        } else {
            final char[] result = new char[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }
}