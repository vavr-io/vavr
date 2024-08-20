/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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
package io.vavr.collection.euler;

import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

final class Sieve {

    private Sieve() {
    }

    private final static List<Function2<Integer, Integer, Option<Integer>>> RULES = List.of(
            (x, y) -> Option.of((4 * x * x) + (y * y)).filter(n -> n % 12 == 1 || n % 12 == 5),
            (x, y) -> Option.of((3 * x * x) + (y * y)).filter(n -> n % 12 == 7),
            (x, y) -> Option.of((3 * x * x) - (y * y)).filter(n -> x > y && n % 12 == 11)
    );

    private final static List<Function3<Set<Integer>, Integer, Integer, Set<Integer>>> STEPS = List.of(
            (sieve, limit, root) -> Stream.rangeClosed(1, root).crossProduct()
                    .foldLeft(sieve, (xs, xy) ->
                            RULES.foldLeft(xs, (ss, r) -> r.apply(xy._1, xy._2)
                                    .filter(p -> p < limit)
                                    .map(p -> ss.contains(p) ? ss.remove(p) : ss.add(p))
                                    .getOrElse(ss)
                            )
                    ),
            (sieve, limit, root) -> Stream.rangeClosed(5, root)
                    .foldLeft(sieve, (xs, r) -> xs.contains(r)
                                                ? Stream.rangeBy(r * r, limit, r * r).foldLeft(xs, Set::remove)
                                                : xs
                    )
    );

    static Set<Integer> fillSieve(int limit, Set<Integer> empty) {
        return STEPS.foldLeft(empty.add(2).add(3), (s, step) ->
                step.apply(s, limit, (int) Math.ceil(Math.sqrt(limit)))
        );
    }

}
