/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * Internal class, containing helpers.
 *
 * @since 3.0.0
 */
final class Sets {

    private Sets() {}

    /**
     * Checks equality of a Set agains any object.
     *
     * @param source Set
     * @param obj    unknown object
     * @return true iff: <br>
     * <ul>
     * <li>both parameters point to the same object</li>
     * <li>if obj is a set, moth bust have the same size and all element from source need to be found in the obj</li>
     * </ul> <br/>
     * and false otherwise.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static boolean equals(Set source, Object obj) {
        if (source == obj) {
            return true;
        } else if (obj instanceof Set) {
            final Set other = (Set) obj;
            return source.size() == other.size() && source.forAll(other::contains);
        } else {
            return false;
        }
    }
}
