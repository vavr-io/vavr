/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @since 2.0.0
 */
public interface FilterMonadic<M extends Kind<M, ?>, T> extends Kind<M, T>{

    FilterMonadic<M, T> filter(Predicate<? super T> predicate);

    <U> FilterMonadic<M, U> flatMapM(Function<? super T, ? extends Kind<? extends M, ? extends U>> mapper);

    <U> FilterMonadic<M, U> flattenM(Function<? super T, ? extends Kind<? extends M, ? extends U>> f);

    <U> FilterMonadic<M, U> map(Function<? super T, ? extends U> mapper);
}
