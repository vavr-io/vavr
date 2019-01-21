/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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
package io.vavr.collection;

import java.util.Objects;
import java.util.function.Function;

public interface Traversable<T> extends io.vavr.Iterable<T> {

    <U> Traversable<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    // `Iterable<T>` must not have a generic type bound, see TraversableTest ShouldJustCompile
    default <C> C to(Function<? super Iterable<T>, C> fromIterable) {
        Objects.requireNonNull(fromIterable, "fromIterable is null");
        return fromIterable.apply(this);
    }

}
