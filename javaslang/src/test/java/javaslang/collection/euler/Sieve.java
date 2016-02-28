/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function2;
import javaslang.Function3;
import javaslang.collection.*;
import javaslang.control.Option;

public class Sieve {

    final static List<Function2<Integer, Integer, Option<Integer>>> rules = List.of(
            (x, y) -> Option.of((4 * x * x) + (y * y)).filter(n -> n % 12 == 1 || n % 12 == 5),
            (x, y) -> Option.of((3 * x * x) + (y * y)).filter(n -> n % 12 == 7),
            (x, y) -> Option.of((3 * x * x) - (y * y)).filter(n -> x > y && n % 12 == 11)
    );

    final static List<Function3<Set<Integer>, Integer, Integer, Set<Integer>>> steps = List.of(
            (sieve, limit, root) -> Stream.rangeClosed(1, root).crossProduct()
                    .foldLeft(sieve, (xs, xy) ->
                            rules.foldLeft(xs, (ss, r) -> r.apply(xy._1, xy._2)
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

    final static Function2<Integer, Set<Integer>, Set<Integer>> fillSieve = (limit, empty) ->
            steps.foldLeft(empty.add(2).add(3), (s, step) ->
                    step.apply(s, limit, (int) Math.ceil(Math.sqrt(limit)))
            );

    final static int BENCH_LIMIT = 2_000_000;

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        fillSieve.apply(BENCH_LIMIT, HashSet.empty());
        System.out.println("HashSet: " + (System.currentTimeMillis() - t) + "ms");

        t = System.currentTimeMillis();
        fillSieve.apply(BENCH_LIMIT, TreeSet.empty());
        System.out.println("TreeSet: " + (System.currentTimeMillis() - t) + "ms");
    }

}
