/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Objects;

@FunctionalInterface
public interface ObjDoubleConsumer<T> extends Serializable, java.util.function.ObjDoubleConsumer<T> {

    static final long serialVersionUID = 1L;

    @Override
    void accept(T t, double value);

    default ObjDoubleConsumer<T> andThen(java.util.function.ObjDoubleConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t, double value) -> { accept(t, value); after.accept(t, value); };
    }

}