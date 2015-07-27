/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

public class StackTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Stack<T> empty() {
        return Stack.empty();
    }

    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected final <T> Stack<T> of(T... elements) {
        return Stack.of(elements);
    }

    // -- range

    @Override
    protected Stack<Integer> range(int from, int toExclusive) {
        return Stack.range(from, toExclusive);
    }

    @Override
    protected Stack<Integer> rangeBy(int from, int toExclusive, int step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Long> range(long from, long toExclusive) {
        return Stack.range(from, toExclusive);
    }

    @Override
    protected Stack<Long> rangeBy(long from, long toExclusive, long step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Integer> rangeClosed(int from, int toInclusive) {
        return Stack.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stack<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stack<Long> rangeClosed(long from, long toInclusive) {
        return Stack.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stack<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    // -- other

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }
}
