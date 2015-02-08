/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction0;

/**
 * Checked version of java.util.function.BooleanSupplier.
 */
@FunctionalInterface
public interface CheckedBooleanSupplier extends CheckedFunction0<Boolean> {

    static final long serialVersionUID = 1L;

    boolean getAsBoolean() throws Throwable;

    @Override
    default Boolean apply() throws Throwable {
        return getAsBoolean();
    }
}
