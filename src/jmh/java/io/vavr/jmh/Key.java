package io.vavr.jmh;

/** A key with an integer value and a masked hash code.
 * The mask allows to provoke collisions in hash maps.
 */
public class Key {
    public final int value;
    public final int hashCode;

    public Key(int value, int mask) {
        this.value = value;
        this.hashCode = value&mask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Key that = (Key) o;
        return value == that.value ;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
