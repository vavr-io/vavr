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
import static javaslang.MatchTest.Patterns.*;
import static javaslang.MatchTest.Patterns.Tuple3;

public class MatchTest {

    static final List<Tuple3<String, Integer, Double>> TUPLE3_LIST = List.of(
            Tuple.of("begin", 10, 4.5),
            Tuple.of("middle", 10, 0.0),
            Tuple.of("end", 10, 1.2));

    static final List<Option<Integer>> INT_OPTION_LIST = List.of(Option.some(1));

    static final Option<Tuple2<String, Integer>> TUPLE2_OPTION = Option.of(Tuple.of("Test", 123));

    static final Option<Option<Tuple2<String, Integer>>> TUPLE2_OPTION_OPTION = Option.of(Option.of(Tuple.of("Test", 123)));

    @Test
    public void shouldMatch() {

        Match.match(TUPLE2_OPTION)
                ._case($_)
                .then(() -> "good!");

        Match.match(TUPLE2_OPTION)
                ._case(Option($()))
                .then(value -> {
                    Tuple2<String, Integer> tuple2 = value;
                    System.out.printf("Option($()) = Option(%s)\n", value);
                    return null;
                });

        Match.match(TUPLE2_OPTION_OPTION)
                ._case(Option(Option($(Tuple.of("Test", 123)))))
                .then(value -> {
                    Tuple2<String, Integer> i = value;
                    System.out.printf("Option(Option($(Tuple.of(\"Test\", 123)))) = Option(Option(%s))\n", value);
                    return null;
                });

        Match.match(INT_OPTION_LIST)
                ._case(List(Option($(1)), $_))
                .then(value -> {
                    int i = value;
                    System.out.printf("List(Option($(1)), _) = List(Option(%d), _)\n", i);
                    return null;
                });

        Match.match(TUPLE3_LIST)
                ._case(List($(), $()))
                .then((x, xs) -> {
                    Tuple3<String, Integer, Double> head = x;
                    List<Tuple3<String, Integer, Double>> tail = xs;
                    System.out.printf("List($(), $()) = List(%s, %s)\n", head, tail);
                    return null;
                });

        Match.match(TUPLE3_LIST)
                ._case(List($(), $_))
                .then(x -> {
                    Tuple3<String, Integer, Double> head = x;
                    System.out.printf("List($(), _) = List(%s, ?)\n", head);
                    return null;
                });

//        // CORRECT: Does not compile because List(Tuple3, ...) is not of List(Option, ...)
//        Match.match(TUPLE3_LIST)
//                ._case(List(Option($(1)), List($(2), $_)))
//                .then((i1, i2) -> {
//                    int j1 = i1;
//                    int j2 = i2;
//                    System.out.printf("List(Option($(1)), List($(2), _)) = List(%s, %s)\n", j1, j2);
//                    return null;
//                });

        Match.match(TUPLE3_LIST)
                ._case(List(Tuple3($("begin"), $_, $_), $_))
                .then(s -> {
                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
                    return null;
                });

        Match.match(TUPLE3_LIST)
                ._case(List(Tuple3($_, $_, $_), $_))
                .then(() -> {
                    System.out.printf("List(Tuple3($_, _, _), _) = List(Tuple3(_, _, _), _)\n");
                    return null;
                });

        // CORRECT: Does not compile because $(1) is not of type String
//        // changed String to int
//        Match.match(TUPLE3_LIST)
//                ._case(List(Tuple3($(1), $_, $_), $_))
//                .then(s -> {
//                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
//                    return null;
//                });

//        // SHOULD NOT COMPILE!
//        Match.match(TUPLE3_LIST)
//                ._case(Tuple3($(1), $_, $_))
//                .then(s -> {
//                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
//                    return null;
//                });

    }

    interface Patterns {

        static <T extends Option<U>, U, T1> Pattern1<Option<U>, T1> Option(Pattern1<U, T1> p1) {
            return new Pattern1<Option<U>, T1>() {
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

        static <T extends Option<U>, U> Pattern1<Option<U>, U> Option(InversePattern<U> p1) {
            return new Pattern1<Option<U>, U>() {
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

        static <T extends List<U>, U, T1> Pattern1<List<U>, T1> List(Pattern1<U, T1> p1, Pattern0 p2) {
            return new Pattern1<List<U>, T1>() {
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

        static <T extends List<U>, U, T1, T2> Pattern2<List<U>, T1, T2> List(Pattern1<U, T1> p1, Pattern1<List<U>, T2> p2) {
            return new Pattern2<List<U>, T1, T2>() {
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
        static <T extends List<U>, U> Pattern1<T, U> List(InversePattern<U> head, Pattern0 tail) {
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
        static <T extends List<U>, U> Pattern2<T, U, List<U>> List(InversePattern<U> head, InversePattern<List<U>> tail) {
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

        // !IMPORTANT: U2, U3 needed to propagate correct types
        static <T extends Tuple3<U1, U2, U3>, U1, U2, U3, T1> Pattern1<Tuple3<U1, U2, U3>, T1> Tuple3(Pattern1<U1, T1> p1, Pattern0 p2, Pattern0 p3) {
            return new Pattern1<Tuple3<U1, U2, U3>, T1>() {
                @Override
                public Option<T1> apply(Object o) {
                    return (o instanceof Tuple3) ? p1.apply(((Tuple3) o)._1) : Option.none();
                }
            };
        }
    }
}
