/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Value;

import java.util.function.Predicate;

/**
 * @since 2.0.5
 */
public abstract class AbstractSet<T> implements Set<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        return trueIfSameOrWhenInstanceOfASet(obj, this::euqalsTypeUnsafe);
    }

    @Override
    public boolean eq(Object obj) {
        return trueIfSameOrWhenInstanceOfASet(obj, this::eqTypeUnsafe);
    }

    private boolean trueIfSameOrWhenInstanceOfASet(Object obj, Predicate<Set<?>> predicate) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Set<?>) {
            final Set<?> setObj = (Set<?>) obj;
            return this.size() == setObj.size() && predicate.test(setObj);
        } else {
            return false;
        }
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean euqalsTypeUnsafe(Set other) {
        return this.forAll(other::contains);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean eqTypeUnsafe(Set other) {
        return this.forAll(thisElem -> other.existsUnique(otherElem -> Value.eq(thisElem, otherElem)));
    }

}
