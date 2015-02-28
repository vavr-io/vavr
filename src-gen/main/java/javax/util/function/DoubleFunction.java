/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function1;

@FunctionalInterface
public interface DoubleFunction<R> extends Function1<Double, R>, java.util.function.DoubleFunction<R> {

    static final long serialVersionUID = 1L;

    R apply(double value);

    @Override
    default R apply(Double value) {
        return apply((double) value);
    }

    static DoubleFunction<Double> identity() {
        return v -> v;
    }
}