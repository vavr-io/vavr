/*     / \$_$_  _    _  $_$_   $_$_$_  / \ $_$_  $_    _ $_$__
 *    /  /    \/ \  / \/    \ /  /\$_\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\$_\\  \  //  /\  \ /\\/  \$_/  /   Copyright 2014-now Daniel Dietrich
 * /$__/\_/  \_/\$_$_/\_/  \_/\$_\/$_/$__\_/  \_//  \$_/$_$__/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

import static javaslang.Match.*;

public class MatchTest {

    @Test
    public void shouldMatch() {

        List<Tuple3<String, Integer, Double>> data =
                List.of(
                        Tuple.of("begin", 10, 4.5),
                        Tuple.of("middle", 10, 0.0),
                        Tuple.of("end", 10, 1.2));

        List<Option<Integer>> data2 = List.of(Option.some(1));

        Option<Tuple2<String, Integer>> opt = Option.of(Tuple.of("Test", 123));

        Option<Option<Tuple2<String, Integer>>> opt2 = Option.of(Option.of(Tuple.of("Test", 123)));

        // ALL TYPES ARE INFERRED RECURSIVELY!

        Match.of(opt)
                .when(Option($()))
                .then(value -> {
                    Tuple2<String, Integer> tuple2 = value;
                    System.out.printf("Option($()) = Option(%s)\n", value);
                    return null;
                });

        Match.of(opt2)
                .when(Option(Option($(Tuple.of("Test", 123)))))
                .then(value -> {
                    Tuple2<String, Integer> i = value;
                    System.out.printf("Option(Option($(Tuple.of(\"Test\", 123)))) = Option(Option(%s))\n", value);
                    return null;
                });

        Match.of(data2)
                .when(List(Option($(1)), $_))
                .then(value -> {
                    int i = value;
                    System.out.printf("List(Option($(1)), _) = List(Option(%d), _)\n", i);
                    return null;
                });

        Match.of(data)
                .when(List($(), $()))
                .then((x, xs) -> {
                    Tuple3<String, Integer, Double> head = x;
                    List<Tuple3<String, Integer, Double>> tail = xs;
                    System.out.printf("List($(), $()) = List(%s, %s)\n", head, tail);
                    return null;
                });

        Match.of(data)
                .when(List($(), $_))
                .then(x -> {
                    Tuple3<String, Integer, Double> head = x;
                    System.out.printf("List($(), _) = List(%s, ?)\n", head);
                    return null;
                });

        Match.of(data)
                .when(List(Option($(1)), List($(2), $_)))
                .then((i1, i2) -> {
                    int j1 = i1;
                    int j2 = i2;
                    System.out.printf("List(Option($(1)), List($(2), _)) = List(%s, %s)\n", j1, j2);
                    return null;
                });

        Match.of(data)
                .when(List(Tuple3($("begin"), $_, $_), $_))
                .then(s -> {
                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
                    return null;
                });
    }

    static <T, T1> Pattern1<T, T1> Option(Pattern1<?, T1> p1) {
        return new Pattern1<T, T1>() {
            @Override
            public Option<T1> apply(Object o) {
                if (o instanceof Option) {
                    final Option<?> option = (Option<?>) o;
                    return option.flatMap(p1::apply);
                } else {
                    return Option.none();
                }
            }
        };
    }

    static <T extends Option<U>, U> Pattern1<T, U> Option(InversePattern1<U> p1) {
        return new Pattern1<T, U>() {
            @SuppressWarnings("unchecked")
            @Override
            public Option<U> apply(Object o) {
                if (o instanceof Option) {
                    final Option<?> option = (Option<?>) o;
                    return option.flatMap(u -> p1.apply((U) u));
                } else {
                    return Option.none();
                }
            }
        };
    }

    static Pattern0 List(Pattern0 p1) {
        return null;
    }

    static Pattern0 List(Pattern0 p1, Pattern0 p2) {
        return new Pattern0() {
            @Override
            public Option<Void> apply(Object o) {
                if (o instanceof List) {
                    final List<?> list = (List<?>) o;
                    if (list.isEmpty()) {
                        return Option.none();
                    } else {
                        return p1.apply(list.head()).flatMap(nothing1 ->
                                p2.apply(list.tail()).map(nothing2 -> null));
                    }
                } else {
                    return Option.none();
                }
            }
        };
    }

    static <T, T1> Pattern1<T, T1> List(Pattern1<?, T1> p1, Pattern0 p2) {
        return new Pattern1<T, T1>() {
            @Override
            public Option<T1> apply(Object o) {
                if (o instanceof List) {
                    final List<?> list = (List<?>) o;
                    if (list.isEmpty()) {
                        return Option.none();
                    } else {
                        return p1.apply(list.head()).flatMap(v1 ->
                                p2.apply(list.tail()).map(any -> v1));
                    }
                } else {
                    return Option.none();
                }
            }
        };
    }

    static <T extends List<U>, U, T1, T2> Pattern2<T, T1, T2> List(Pattern1<U, T1> p1, Pattern1<U, T2> p2) {
        return new Pattern2<T, T1, T2>() {
            @Override
            public Option<Tuple2<T1, T2>> apply(Object o) {
                if (o instanceof List) {
                    final List<?> list = (List<?>) o;
                    if (list.isEmpty()) {
                        return Option.none();
                    } else {
                        return p1.apply(list.head()).flatMap(v1 ->
                                p2.apply(list.tail()).map(v2 -> Tuple.of(v1, v2)));
                    }
                } else {
                    return Option.none();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <T extends List<U>, U> Pattern1<T, U> List(InversePattern1<U> head, Pattern0 tail) {
        return new Pattern1<T, U>() {
            @Override
            public Option<U> apply(Object o) {
                if (o instanceof List) {
                    final List<?> list = (List<?>) o;
                    if (list.isEmpty()) {
                        return Option.none();
                    } else {
                        return head.apply((U) list.head()).flatMap(v1 ->
                                tail.apply(list.tail()).map(any -> v1));
                    }
                } else {
                    return Option.none();
                }
            }
        };
    }

    // TODO: check if cast to (U) and (List<U>) always succeeds
    @SuppressWarnings("unchecked")
    static <T extends List<U>, U> Pattern2<T, U, List<U>> List(InversePattern1<U> head, InversePattern1<List<U>> tail) {
        return new Pattern2<T, U, List<U>>() {
            @Override
            public Option<Tuple2<U, List<U>>> apply(Object o) {
                if (o instanceof List) {
                    final List<?> list = (List<?>) o;
                    if (list.isEmpty()) {
                        return Option.none();
                    } else {
                        return head.apply((U) list.head()).flatMap(v1 ->
                                tail.apply((List<U>) list.tail()).map(v2 -> Tuple.of(v1, v2)));
                    }
                } else {
                    return Option.none();
                }
            }
        };
    }

    static Pattern0 Tuple3(Pattern0 p1, Pattern0 p2, Pattern0 p3) {
        return new Pattern0() {
            @Override
            public Option<Void> apply(Object o) {
                return (o instanceof Tuple3) ? Option.nothing() : Option.none();
            }
        };
    }

    static <T, T1> Pattern1<T, T1> Tuple3(Pattern1<?, T1> p1, Pattern0 p2, Pattern0 p3) {
        return new Pattern1<T, T1>() {
            @Override
            public Option<T1> apply(Object o) {
                return (o instanceof Tuple3) ? p1.apply(((Tuple3) o)._1) : Option.none();
            }
        };
    }
}
