/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.function.Function;

/**
 * A BiFunctor combines two unrelated {@link Functor}s.
 *
 * @param <T1> component type of the 1st {@code Functor}
 * @param <T2> component type of the 2nd {@code Functor}
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface BiFunctor<T1, T2> {
	
    /**
     * Applies two functions {@code f1}, {@code f2} to the components of this BiFunctor.
     *
     * @param <U1> 1st component type of the resulting BiFunctor
     * @param <U2> 2nd component type of the resulting BiFunctor
     * @param f1   mapper of 1st component
     * @param f2   mapper of 2nd component
     * @return a new BiFunctor
     * @throws NullPointerException if {@code f1} or {@code f2} is null
     */
    <U1, U2> BiFunctor<U1, U2> bimap(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2);

}
