/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package java.util.concurrent.atomic;

import java.util.function.IntUnaryOperator;
import java.util.function.IntBinaryOperator;
import sun.misc.Unsafe;

/**
 * GWT emulated version of {@link AtomicInteger} wrapping simple int value.
 */
public class AtomicInteger extends Number implements java.io.Serializable {
    private static final long serialVersionUID = 6214790243416807050L;

    private int value;

    public AtomicInteger(int initialValue) {
        value = initialValue;
    }

    public AtomicInteger() {}

    public final int get() { return value; }

    public final void set(int newValue) { value = newValue; }

    public final void lazySet(int newValue) { set(newValue); }

    public final int getAndSet(int newValue) {
        int old = value;
        value = newValue;

        return old;
    }

    public final boolean compareAndSet(int expect, int update) {
        if (value == expect) {
            value = update;

            return true;
        }

        return false;
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        return compareAndSet(expect, update);
    }

    public final int getAndIncrement() {
        return value++;
    }

    public final int getAndDecrement() {
        return value--;
    }

    public final int getAndAdd(int delta) {
        int old = value;
        value += delta;

        return old;
    }

    public final int incrementAndGet() {
        return ++value;
    }

    public final int decrementAndGet() {
        return --value;
    }

    public final int addAndGet(int delta) {
        value += delta;

        return value;
    }

    public final int getAndUpdate(IntUnaryOperator updateFunction) {
        int old = value;
        value = updateFunction.applyAsInt(value);

        return old;
    }

    public final int updateAndGet(IntUnaryOperator updateFunction) {
        value = updateFunction.applyAsInt(value);

        return value;
    }

    public final int getAndAccumulate(int x,
            IntBinaryOperator accumulatorFunction) {
        int old = value;
        value = accumulatorFunction.applyAsInt(value, x);

        return old;
    }

    public final int accumulateAndGet(int x,
            IntBinaryOperator accumulatorFunction) {
        value = accumulatorFunction.applyAsInt(value, x);

        return value;
    }

    public String toString() {
        return Integer.toString(get());
    }

    public int intValue() {
        return get();
    }

    public long longValue() {
        return (long) get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return (double) get();
    }

}
