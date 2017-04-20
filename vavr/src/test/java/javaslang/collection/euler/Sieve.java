/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function2;
import javaslang.Function3;
import javaslang.collection.List;
import javaslang.collection.Set;
import javaslang.collection.Stream;
import javaslang.control.Option;

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
