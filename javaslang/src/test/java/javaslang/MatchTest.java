/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Option.Some;
import javaslang.match.annotation.Unapply;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Year;

import static javaslang.API.$;
import static javaslang.API.*;
import static javaslang.MatchTest_DeveloperPatterns.Developer;
import static javaslang.Patterns.*;
import static javaslang.Predicates.instanceOf;
import static javaslang.Predicates.is;
import static org.assertj.core.api.Assertions.assertThat;

public class MatchTest {

    // -- MatchError

    @Test(expected = MatchError.class)
    public void shouldThrowIfNotMatching() {
        Match(new Object()).of(
                Case(ignored -> false, o -> null)
        );
    }

    // -- $()

    @Test
    public void shouldMatchNullWithAnyReturningValue() {
        assertThat(Case($(), 1).apply(null)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchAnyReturningValue() {
        assertThat(Case($(), 1).apply(new Object())).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchNullWithAnyReturningAppliedFunction() {
        assertThat(Case($(), o -> 1).apply(null)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchAnyReturningAppliedFunction() {
        assertThat(Case($(), o -> 1).apply(new Object())).isEqualTo(Option.of(1));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnMatchAnyIfFunctionIsNull() {
        Match(new Object()).of(
                Case($(), null)
        );
    }

    @Test
    public void shouldTakeFirstMatch() {
        final String actual = Match(new Object()).of(
                Case($(), "first"),
                Case($(), "second")
        );
        assertThat(actual).isEqualTo("first");
    }

    // -- $(value)

    @Test
    public void shouldMatchValueReturningValue() {
        final Object value = new Object();
        assertThat(Case($(value), 1).apply(value)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchValueReturningValue_NegativeCase() {
        final Object value = new Object();
        assertThat(Case($(value), 1).apply(new Object())).isEqualTo(Option.none());
    }

    @Test
    public void shouldMatchValueReturningAppliedFunction() {
        final Object value = new Object();
        assertThat(Case($(value), o -> 1).apply(value)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchValueReturningAppliedFunction_NegativeCase() {
        final Object value = new Object();
        assertThat(Case($(value), o -> 1).apply(new Object())).isEqualTo(Option.none());
    }

    // -- $(predicate)

    @Test
    public void shouldMatchPredicateReturningValue() {
        final Object value = new Object();
        assertThat(Case($(is(value)), 1).apply(value)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchPredicateReturningValue_NegativeCase() {
        final Object value = new Object();
        assertThat(Case($(is(value)), 1).apply(new Object())).isEqualTo(Option.none());
    }

    @Test
    public void shouldMatchPredicateReturningAppliedFunction() {
        final Object value = new Object();
        assertThat(Case($(is(value)), o -> 1).apply(value)).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldMatchPredicateReturningAppliedFunction_NegativeCase() {
        final Object value = new Object();
        assertThat(Case($(is(value)), o -> 1).apply(new Object())).isEqualTo(Option.none());
    }

    // -- multiple cases

    // i match {
    //     case 1 => "one"
    //     case 2 => "two"
    //     case _ => "many"
    // }

    @Test
    public void shouldMatchIntUsingPatterns() {
        final String actual = Match(3).of(
                Case($(1), "one"),
                Case($(2), "two"),
                Case($(), "many")
        );
        assertThat(actual).isEqualTo("many");
    }

    @Test
    public void shouldMatchIntUsingPredicates() {
        final String actual = Match(3).of(
                Case(is(1), "one"),
                Case(is(2), "two"),
                Case($(), "many")
        );
        assertThat(actual).isEqualTo("many");
    }

    @Test
    public void shouldComputeUpperBoundOfReturnValue() {
        final Number num = Match(3).of(
                Case(is(1), 1),
                Case(is(2), 2.0),
                Case($(), i -> new BigDecimal("" + i))
        );
        assertThat(num).isEqualTo(new BigDecimal("3"));
    }

    // -- instanceOf

    @Test
    public void shouldMatchX() {
        final Object obj = 1;
        final int actual = Match(obj).of(
                Case(instanceOf(Year.class), y -> 0),
                Case(instanceOf(Integer.class), i -> 1)
        );
        assertThat(actual).isEqualTo(1);
    }

    // -- Option

    @Test
    public void shouldMatchSome() {
        final Option<Integer> opt = Option.some(1);
        final String actual = Match(opt).of(
                Case(Some($()), String::valueOf),
                Case(None(), "no value")
        );
        assertThat(actual).isEqualTo("1");
    }

    @Test
    public void shouldMatchNone() {
        final Option<Integer> opt = Option.none();
        final String actual = Match(opt).of(
                Case(Some($()), String::valueOf),
                Case(None(), "no value")
        );
        assertThat(actual).isEqualTo("no value");
    }

    @Test
    public void shouldDecomposeSomeTuple() {
        final Option<Tuple2<String, Integer>> tuple2Option = Option.of(Tuple.of("Test", 123));
        final Tuple2<String, Integer> actual = Match(tuple2Option).of(
                Case(Some($()), value -> {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    Tuple2<String, Integer> tuple2 = value; // types are inferred correctly!
                    return tuple2;
                })
        );
        assertThat(actual).isEqualTo(Tuple.of("Test", 123));
    }

    @Test
    public void shouldDecomposeSomeSomeTuple() {
        final Option<Option<Tuple2<String, Integer>>> tuple2OptionOption = Option.of(Option.of(Tuple.of("Test", 123)));
        final Some<Tuple2<String, Integer>> actual = Match(tuple2OptionOption).of(
                Case(Some(Some($(Tuple.of("Test", 123)))), value -> {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    final Some<Tuple2<String, Integer>> some = value; // types are inferred correctly!
                    return some;
                })
        );
        assertThat(actual).isEqualTo(Option.of(Tuple.of("Test", 123)));
    }

    // -- List

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void shouldDecomposeListOfTuple3() {
        final List<Tuple3<String, Integer, Double>> tuple3List = List.of(
                Tuple.of("begin", 10, 4.5),
                Tuple.of("middle", 11, 0.0),
                Tuple.of("end", 12, 1.2));
        final String actual = Match(tuple3List).of(
                Case(List($(), $()), (x, xs) -> {
                    // types are inferred correctly!
                    final Tuple3<String, Integer, Double> head = x;
                    final List<Tuple3<String, Integer, Double>> tail = xs;
                    return head + "::" + tail;
                })
        );
        assertThat(actual).isEqualTo("(begin, 10, 4.5)::List((middle, 11, 0.0), (end, 12, 1.2))");
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void shouldDecomposeListWithNonEmptyTail() {
        final List<Option<Number>> intOptionList = List.of(Option.some(1), Option.some(2.0));
        final String actual = Match(intOptionList).of(
                Case(List(Some($(1)), List(Some($(2.0)), $())), (x, xs) -> {
                    // types are inferred correctly!
                    final Some<Number> head = x;
                    final List<Option<Number>> tail = xs;
                    return head + "::" + tail;
                })
        );
        assertThat(actual).isEqualTo("Some(1)::List(Some(2.0))");
    }

    // -- Developer

    @Test
    public void shouldMatchCustomTypeWithUnapplyMethod() {
        final Person person = new Developer("Daniel", true, Option.some(13));
        final String actual = Match(person).of(
                Case(Developer($("Daniel"), $(true), $()), Person.Util::devInfo),
                Case($(), p -> "Unknown person: " + p.getName())
        );
        assertThat(actual).isEqualTo("Daniel is caffeinated.");
    }

    interface Person {
        String getName();

        class Util {
            static String devInfo(String name, boolean isCaffeinated, Option<Number> number) {
                return name + " is " + (isCaffeinated ? "" : "not ") + "caffeinated.";
            }
        }
    }

    static final class Developer implements Person {
        private final String name;
        private final boolean isCaffeinated;
        private final Option<Number> number;

        Developer(String name, boolean isCaffeinated, Option<Number> number) {
            this.name = name;
            this.isCaffeinated = isCaffeinated;
            this.number = number;
        }

        public String getName() { return name; }

        public boolean isCaffeinated() { return isCaffeinated; }

        public Option<Number> number() { return number; }

        @javaslang.match.annotation.Patterns
        static class $ {
            @Unapply
            static Tuple3<String, Boolean, Option<Number>> Developer(Developer dev) {
                return Tuple.of(dev.getName(), dev.isCaffeinated(), dev.number());
            }
        }
    }
}
