/*     / \$_$_  _    _  $_$_   $_$_$_  / \ $_$_  $_    _ $_$__
 *    /  /    \/ \  / \/    \ /  /\$_\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\$_\\  \  //  /\  \ /\\/  \$_/  /   Copyright 2014-now Daniel Dietrich
 * /$__/\_/  \_/\$_$_/\_/  \_/\$_\/$_/$__\_/  \_//  \$_/$_$__/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Option.Some;
import org.junit.Test;

import static javaslang.Match.*;
import static javaslang.Match.Match;
import static javaslang.MatchTest.StandardPatterns.*;
import static javaslang.MatchTest.StandardPatterns.Tuple3;

public class MatchTest {

    static final List<Tuple3<String, Integer, Double>> TUPLE3_LIST = List.of(
            Tuple.of("begin", 10, 4.5),
            Tuple.of("middle", 10, 0.0),
            Tuple.of("end", 10, 1.2));

    static final List<Option<Integer>> INT_OPTION_LIST = List.of(Option.some(1));

    static final Option<Tuple2<String, Integer>> TUPLE2_OPTION = Option.of(Tuple.of("Test", 123));

    static final Option<Option<Tuple2<String, Integer>>> TUPLE2_OPTION_OPTION = Option.of(Option.of(Tuple.of("Test", 123)));

    static final Person PERSON = new Developer("Daniel", true);

    @Test
    public void shouldMatch() {

        Match(TUPLE2_OPTION).of(
                Case($_, () -> "good!")
        );

        Match(TUPLE2_OPTION).of(
                Case(Some($()), value -> {
                    Tuple2<String, Integer> tuple2 = value;
                    System.out.printf("Option($()) = Option(%s)\n", value);
                    return null;
                })
        );

        Match(TUPLE2_OPTION_OPTION).of(
                Case(Some(Some($(Tuple.of("Test", 123)))), value -> {
                    Tuple2<String, Integer> i = value;
                    System.out.printf("Option(Option($(Tuple.of(\"Test\", 123)))) = Option(Option(%s))\n", value);
                    return null;
                })
        );

        Match(INT_OPTION_LIST).of(
                Case(List(Some($(1)), $_), value -> {
                    int i = value;
                    System.out.printf("List(Option($(1)), _) = List(Option(%d), _)\n", i);
                    return null;
                })
        );

        Match(TUPLE3_LIST).of(
                Case(List($(), $()), (x, xs) -> {
                    Tuple3<String, Integer, Double> head = x;
                    List<Tuple3<String, Integer, Double>> tail = xs;
                    System.out.printf("List($(), $()) = List(%s, %s)\n", head, tail);
                    return null;
                })
        );

        Match(TUPLE3_LIST).of(
                Case(List($(), $_), x -> {
                    Tuple3<String, Integer, Double> head = x;
                    System.out.printf("List($(), _) = List(%s, ?)\n", head);
                    return null;
                })
        );

        Match(TUPLE3_LIST).of(
                Case(List(Tuple3($("begin"), $_, $_), $_), s -> {
                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
                    return null;
                })
        );

        Match(TUPLE3_LIST).of(
                Case(List(Tuple3($_, $_, $_), $_), () -> {
                    System.out.printf("List(Tuple3($_, _, _), _) = List(Tuple3(_, _, _), _)\n");
                    return null;
                })
        );

        // = Daniel is caffeinated
        final String msg1 = Match(PERSON).of(
                Case(Developer($("Daniel"), $(true)), Util::devInfo),
                Case($_, () -> "Unknown Person type")
        );

        // = Some(Daniel is caffeinated)
        final Option<String> msg2 = Match(PERSON).safe(
                Case(Developer($("Daniel"), $(true)), Util::devInfo)
        );

        // should not match wrong subtype
        final Option<Integer> opt = Option.none();
        final String val = Match(opt).of(
                Case(Some($()), String::valueOf),
                Case(None, () -> "no value")
        );
        System.out.println("opt.match = " + val);

        // --
        // -- EXAMPLES THAT CORRECTLY DO NOT COMPILE BECAUSE OF WRONG TYPES
        // --

//        // Does not compile because List(Tuple3, ...) is not of List(Option, ...)
//        Match(TUPLE3_LIST).of(
//                Case(List(Option($(1)), List($(2), $_)), (i1, i2) -> {
//                    int j1 = i1;
//                    int j2 = i2;
//                    System.out.printf("List(Option($(1)), List($(2), _)) = List(%s, %s)\n", j1, j2);
//                    return null;
//                })
//        );

//        // Does not compile because $(1) is not of type String
//        Match(TUPLE3_LIST).of(
//                Case(List(Tuple3($(1), $_, $_), $_), s -> {
//                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
//                    return null;
//                })
//        );

//        // Does not compile because Tuple3-Pattern does not match List
//        Match(TUPLE3_LIST).of(
//                Case(Tuple3($(1), $_, $_), s -> {
//                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
//                    return null;
//                })
//        );

    }

    static class Util {
        static String devInfo(String name, boolean isCaffeinated) {
            return name + " is " + (isCaffeinated ? "" : "not ") + "caffeinated.";
        }
    }

    interface Person {
        String getName();
    }

    static final class Developer implements Person {
        private final String name;
        private final boolean isCaffeinated;

        Developer(String name, boolean isCaffeinated) {
            this.name = name;
            this.isCaffeinated = isCaffeinated;
        }

        public String getName() { return name; }

        public boolean isCaffeinated() { return isCaffeinated; }
    }


    interface StandardPatterns {

        static <T1, T2> Pattern2<Developer, T1, T2> Developer(Pattern1<String, T1> p1, Pattern1<Boolean, T2> p2) {
            return new Pattern2<Developer, T1, T2>() {
                @Override
                public Option<Tuple2<T1, T2>> apply(Object o) {
                    if (o instanceof Developer) {
                        final Developer dev = (Developer) o;
                        final Tuple2<String, Boolean> t = My.dev(dev); // <-- DECOMPOSITION
                        return p1.apply(t._1).flatMap(v1 -> p2.apply(t._2).map(v2 -> Tuple.of(v1, v2)));
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        static Pattern2<Developer, String, Boolean> Developer(InversePattern<String> p1, InversePattern<Boolean> p2) {
            return new Pattern2<Developer, String, Boolean>() {
                @Override
                public Option<Tuple2<String, Boolean>> apply(Object o) {
                    if (o instanceof Developer) {
                        final Developer dev = (Developer) o;
                        final Tuple2<String, Boolean> t = My.dev(dev); // <-- DECOMPOSITION
                        return p1.apply(dev.getName()).flatMap(v1 ->
                                p2.apply(dev.isCaffeinated()).map(v2 -> Tuple.of(v1, v2)));
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        @Patterns
        class My {

            // Option
            @Unapply static <T> Tuple1<T> some(Option.Some<T> some) { return Tuple.of(some.get()); }
            @Unapply static Tuple0 none(Option.None<?> none) { return Tuple.empty(); }

            // List
            @Unapply static <T> Tuple2<T, List<T>> cons(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }
            @Unapply static Tuple0 nil(List.Nil<?> nil) { return Tuple.empty(); }

            // Developer
            @Unapply static Tuple2<String, Boolean> dev(Developer dev) { return Tuple.of(dev.getName(), dev.isCaffeinated()); }

        }

        static <T extends Some<U>, U, T1> Pattern1<Some<U>, T1> Some(Pattern1<? extends U, T1> p1) {
            return new Pattern1<Some<U>, T1>() {
                @Override
                public Option<T1> apply(Object o) {
                    if (o instanceof Some) {
                        final Some<?> some = (Some<?>) o;
                        return p1.apply(some.get()); // DECOMPOSITION!
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        static <T extends Some<U>, U> Pattern1<Some<U>, U> Some(InversePattern<? extends U> p1) {
            return new Pattern1<Some<U>, U>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<U> apply(Object o) {
                    if (o instanceof Some) {
                        final Some<U> some = (Some<U>) o; // <-- read-only co-variant casts always work!
                        return ((InversePattern<U>) p1).apply(some.get()); // <-- TODO: better alternative to cast?
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        static Pattern0 None = new Pattern0() {
            @Override
            public Option<Void> apply(Object o) {
                return (o instanceof Option.None) ? Option.nothing() : Option.none();
            }
        };

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

        static <T extends List<U>, U, T1> Pattern1<List<U>, T1> List(Pattern1<? extends U, T1> p1, Pattern0 p2) {
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
        static <T extends List<U>, U> Pattern1<List<U>, U> List(InversePattern<U> head, Pattern0 tail) {
            return new Pattern1<List<U>, U>() {
                @Override
                public Option<U> apply(Object o) {
                    if (o instanceof List) {
                        final List<U> list = (List<U>) o;
                        if (list.isEmpty()) {
                            return Option.none();
                        } else {
                            return head.apply(list.head()).flatMap(v1 ->
                                    tail.apply(list.tail()).map(any -> v1));
                        }
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        @SuppressWarnings("unchecked")
        static <T extends List<U>, U> Pattern2<List<U>, U, List<U>> List(InversePattern<U> head, InversePattern<List<U>> tail) {
            return new Pattern2<List<U>, U, List<U>>() {
                @Override
                public Option<Tuple2<U, List<U>>> apply(Object o) {
                    if (o instanceof List) {
                        final List<U> list = (List<U>) o;
                        if (list.isEmpty()) {
                            return Option.none();
                        } else {
                            return head.apply(list.head()).flatMap(v1 ->
                                    tail.apply(list.tail()).map(v2 -> Tuple.of(v1, v2)));
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
