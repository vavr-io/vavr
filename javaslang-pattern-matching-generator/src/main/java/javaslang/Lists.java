/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

class Lists {

    @SuppressWarnings("unchecked")
    static <T> List<List<T>> crossProduct(List<? extends T> list, int power) {
        Objects.requireNonNull(list, "list is null");
        if (power < 0) {
            throw new IllegalArgumentException("negative power");
        }
        if (power == 0) {
            return singletonList(emptyList());
        } else {
            return list.isEmpty() ? emptyList() : IntStream
                    .range(1, power)
                    .boxed()
                    .reduce(list.stream().map(Lists::unit).collect(Collectors.toList()),
                            (acc, ignored) -> acc.stream()
                                    .flatMap(seq -> list.stream().map(t -> append(seq, t)))
                                    .collect(Collectors.toList()),
                            Lists::combine);
        }
    }

    static <U> List<U> unit(U element) {
        List<U> result = new ArrayList<>();
        result.add(element);
        return result;
    }

    static <U> List<U> append(List<U> list, U element) {
        List<U> result = new ArrayList<>(list);
        result.add(element);
        return result;
    }

    static <U> List<U> combine(List<U> list1, List<U> list2) {
        List<U> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }
}
