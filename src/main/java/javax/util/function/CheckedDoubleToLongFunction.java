/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

/**
 * Checked version of java.util.function.DoubleToLongFunction.
 */
@FunctionalInterface
public interface CheckedDoubleToLongFunction extends CheckedFunction1<Double, Long> {

    static final long serialVersionUID = 1L;

    long applyAsLong(double value) throws Throwable;

    @Override
    default Long apply(Double value) throws Throwable {
        return applyAsLong(value);
    }
}
