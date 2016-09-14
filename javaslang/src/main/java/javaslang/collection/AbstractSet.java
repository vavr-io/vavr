package javaslang.collection;

import javaslang.Value;

import java.util.function.Predicate;

abstract class AbstractSet<T> implements Set<T> {

    @Override
    public boolean equals(Object obj) {
        return trueIfSameOrWhenSet(obj, this::euqalsTypeUnsafe);
    }

    @Override
    public boolean eq(Object obj) {
        return trueIfSameOrWhenSet(obj, this::eqTypeUnsafe);
    }

    private boolean trueIfSameOrWhenSet(Object obj, Predicate<Set> predicate) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Set) {
            final Set setObj = (Set) obj;
            return this.size() == setObj.size() && predicate.test(setObj);
        } else {
            return false;
        }
    }
    @SuppressWarnings({ "unchecked" })
    private boolean euqalsTypeUnsafe(Set other) {
        return this.forAll(other::contains);
    }

    @SuppressWarnings({ "unchecked" })
    private boolean eqTypeUnsafe(Set other) {
        return this.forAll(thisElem -> other.existsUnique(otherElem -> Value.eq(thisElem, otherElem)));
    }

}
