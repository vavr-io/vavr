/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

public abstract class AbstractSetTest extends AbstractTraversableRangeTest {

    // TODO: test difference(), intersection(), union()

    @Override
    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        // sets have only distinct elements
    }

    @Override
    @Test
    public void shouldReplaceFirstOccurrenceOfNonNilUsingCurrNewWhenMultipleOccurrencesExist() {
        // sets have only distinct elements
    }
}
