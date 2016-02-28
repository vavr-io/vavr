/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

public class MatchTest {

// TODO
//    static final List<Tuple3<String, Integer, Double>> TUPLE3_LIST = List.of(
//            Tuple.of("begin", 10, 4.5),
//            Tuple.of("middle", 10, 0.0),
//            Tuple.of("end", 10, 1.2));
//
//    static final List<Option<Integer>> INT_OPTION_LIST = List.of(Option.some(1));
//
//    static final Option<Tuple2<String, Integer>> TUPLE2_OPTION = Option.of(Tuple.of("Test", 123));
//
//    static final Option<Option<Tuple2<String, Integer>>> TUPLE2_OPTION_OPTION = Option.of(Option.of(Tuple.of("Test", 123)));
//
//    static final Person PERSON = new Developer("Daniel", true, Option.some(13));
//
//    @Test
//    public void shouldMatch() {
//
//        Match(TUPLE2_OPTION).of(
//                Case($_, () -> "good!")
//        );
//
//        Match(TUPLE2_OPTION).of(
//                Case(Some($()), value -> {
//                    Tuple2<String, Integer> tuple2 = value; // types are inferred correctly!
//                    System.out.printf("Option($()) = Option(%s)\n", value);
//                    return null;
//                })
//        );
//
//        Match(TUPLE2_OPTION_OPTION).of(
//                Case(Some(Some($(Tuple.of("Test", 123)))), value -> {
//                    Tuple2<String, Integer> i = value;
//                    System.out.printf("Option(Option($(Tuple.of(\"Test\", 123)))) = Option(Option(%s))\n", value);
//                    return null;
//                })
//        );
//
//        Match(INT_OPTION_LIST).of(
//                Case(Cons(Some($(1)), $_), value -> {
//                    int i = value;
//                    System.out.printf("List(Option($(1)), _) = List(Option(%d), _)\n", i);
//                    return null;
//                })
//        );
//
//// TODO
////        Match(TUPLE3_LIST).of(
////                Case(Cons($(), $()), (x, xs) -> {
////                    Tuple3<String, Integer, Double> head = x;
////                    List<Tuple3<String, Integer, Double>> tail = xs;
////                    System.out.printf("List($(), $()) = List(%s, %s)\n", head, tail);
////                    return null;
////                })
////        );
//
//        Match(TUPLE3_LIST).of(
//                Case(Cons($(), $_), x -> {
//                    Tuple3<String, Integer, Double> head = x;
//                    System.out.printf("List($(), _) = List(%s, ?)\n", head);
//                    return null;
//                })
//        );
//
//// TODO
////        Match(TUPLE3_LIST).of(
////                Case(Cons(Tuple3($("begin"), $_, $_), $_), s -> {
////                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
////                    return null;
////                })
////        );
////
////        Match(TUPLE3_LIST).of(
////                Case(Cons(Tuple3($_, $_, $_), $_), () -> {
////                    System.out.printf("List(Tuple3($_, _, _), _) = List(Tuple3(_, _, _), _)\n");
////                    return null;
////                })
////        );
//
//// TODO
////        // = Daniel is caffeinated
////        final String msg1 = Match(PERSON).of(
////                Case(Developer($("Daniel"), $(true), $_), Util::devInfo),
////                Case($_, () -> "Unknown Person type")
////        );
////
////        // = Some(Daniel is caffeinated)
////        final Option<String> msg2 = Match(PERSON).safe(
////                Case(Developer($("Daniel"), $(true), $_), Util::devInfo)
////        );
//
//        // should not match wrong subtype
//        final Option<Integer> opt = Option.none();
//        final String val = Match(opt).of(
//                Case(Some($()), String::valueOf),
//                Case(None, () -> "no value")
//        );
//        System.out.println("opt.match = " + val);
//
//        // --
//        // -- EXAMPLES THAT CORRECTLY DO NOT COMPILE BECAUSE OF WRONG TYPES
//        // --
//
////        // Does not compile because List(Tuple3, ...) is not of List(Option, ...)
////        Match(TUPLE3_LIST).of(
////                Case(List(Option($(1)), List($(2), $_)), (i1, i2) -> {
////                    int j1 = i1;
////                    int j2 = i2;
////                    System.out.printf("List(Option($(1)), List($(2), _)) = List(%s, %s)\n", j1, j2);
////                    return null;
////                })
////        );
//
////        // Does not compile because $(1) is not of type String
////        Match(TUPLE3_LIST).of(
////                Case(List(Tuple3($(1), $_, $_), $_), s -> {
////                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
////                    return null;
////                })
////        );
//
////        // Does not compile because Tuple3-Pattern does not match List
////        Match(TUPLE3_LIST).of(
////                Case(Tuple3($(1), $_, $_), s -> {
////                    System.out.printf("List(Tuple3($(\"begin\"), _, _), _) = List(Tuple3(%s, _, _), _)\n", s);
////                    return null;
////                })
////        );
//
//    }
//
//    static class Util {
//        static String devInfo(String name, boolean isCaffeinated) {
//            return name + " is " + (isCaffeinated ? "" : "not ") + "caffeinated.";
//        }
//    }
//
//    interface Person {
//        String getName();
//    }
//
//    static final class Developer implements Person {
//        private final String name;
//        private final boolean isCaffeinated;
//        private final Option<Number> number;
//
//        Developer(String name, boolean isCaffeinated, Option<Number> number) {
//            this.name = name;
//            this.isCaffeinated = isCaffeinated;
//            this.number = number;
//        }
//
//        public String getName() { return name; }
//
//        public boolean isCaffeinated() { return isCaffeinated; }
//
//        public Option<Number> number() { return number; }
//    }
}
