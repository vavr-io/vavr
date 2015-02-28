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
public interface ToByteFunction<T> extends Function1<T, Byte> {

    static final long serialVersionUID = 1L;

    byte applyAsByte(T t);

    @Override
    default Byte apply(T t) {
        return applyAsByte(t);
    }

    static ToByteFunction<Byte> identity() {
        return v -> v;
    }
}