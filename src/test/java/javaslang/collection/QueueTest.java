/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

public class QueueTest extends AbstractSeqTest {

    @Override
    protected <T> Queue<T> nil() {
        return Queue.empty();
    }

    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected final <T> Queue<T> of(T... elements) {
        return Queue.of(elements);
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }
}
