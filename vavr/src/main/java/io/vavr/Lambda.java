/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import java.io.Serializable;
import java.util.Map;

/**
 * <strong>INTERNAL API - This class is subject to change.</strong>
 * <p>
 * This is a general definition of a (checked/unchecked) function of unknown parameters and a return type R.
 * <p>
 * A checked function may throw an exception. The exception type cannot be expressed as a generic type parameter
 * because Java cannot calculate type bounds on function composition.
 *
 * @param <R> Return type of the function.
 * @author Daniel Dietrich
 */
interface Lambda<R> extends Serializable {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * @return the number of function arguments.
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    int arity();

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    Lambda<?> curried();

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    Lambda<R> tupled();

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    Lambda<R> reversed();

    /**
     * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
     * On subsequent calls given the same arguments the memoized value is returned.
     * <p>
     * Please note that memoizing functions do not permit {@code null} as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    Lambda<R> memoized();

    /**
     * Checks if this function is memoizing (= caching) computed values.
     *
     * @return true, if this function is memoizing, false otherwise
     */
    default boolean isMemoized() {
        return this instanceof Memoized;
    }

    /**
     * Zero Abstract Method (ZAM) interface for marking functions as memoized using intersection types.
     */
    interface Memoized {
        static <T extends Tuple, R> R of(Map<T, R> cache, T key, Function1<T, R> tupled) {
            synchronized (cache) {
                if (cache.containsKey(key)) {
                    return cache.get(key);
                } else {
                    final R value = tupled.apply(key);
                    cache.put(key, value);
                    return value;
                }
            }
        }
    }
}
