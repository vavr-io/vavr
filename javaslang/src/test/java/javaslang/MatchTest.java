/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.control.Option.Some;
import javaslang.match.annotation.Unapply;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.function.Predicate;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.API.run;
import static javaslang.MatchTest_DeveloperPatterns.Developer;
import static javaslang.Patterns.*;
import static javaslang.Predicates.*;
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

    // -- Either

    @Test
    public void shouldMatchLeft() {
        final Either<Integer, String> either = Either.left(1);
        final String actual = Match(either).of(
                Case(Left($()), l -> "left: " + l),
                Case(Right($()), r -> "right: " + r)
        );
        assertThat(actual).isEqualTo("left: 1");
    }

    @Test
    public void shouldMatchRight() {
        final Either<Integer, String> either = Either.right("a");
        final String actual = Match(either).of(
                Case(Left($()), l -> "left: " + l),
                Case(Right($()), r -> "right: " + r)
        );
        assertThat(actual).isEqualTo("right: a");
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

    @Test
    public void shouldDecomposeEmptyList() {
        final List<Integer> list = List.empty();
        final boolean isEmpty = Match(list).of(
                Case(List($(), $()), (x, xs) -> false),
                Case(List(), true)
        );
        assertThat(isEmpty).isTrue();
    }

    @Test
    public void shouldDecomposeNonEmptyList() {
        final List<Integer> list = List.of(1);
        final boolean isNotEmpty = Match(list).of(
                Case(List($(), $()), (x, xs) -> true),
                Case(List(), false)
        );
        assertThat(isNotEmpty).isTrue();
    }

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
    
     // -- run

     @Test
     public void shouldRunUnitOfWork() {

         class OuterWorld {

             String effect = null;

             void displayHelp() {
                 effect = "help";
             }

             void displayVersion() {
                 effect = "version";
             }
         }

         final OuterWorld outerWorld = new OuterWorld();

         Match("-v").of(
                 Case(isIn("-h", "--help"), o -> run(outerWorld::displayHelp)),
                 Case(isIn("-v", "--version"), o -> run(outerWorld::displayVersion)),
                 Case($(), o -> { throw new IllegalArgumentException(); })
         );

         assertThat(outerWorld.effect).isEqualTo("version");
     }

     @Test
     public void shouldRunWithInferredArguments() {

         class OuterWorld {

             Number effect = null;

             void writeInt(int i) {
                 effect = i;
             }

             void writeDouble(double d) {
                 effect = d;
             }
         }

         final OuterWorld outerWorld = new OuterWorld();
         final Object obj = .1d;

         Match(obj).of(
                 Case(instanceOf(Integer.class), i -> run(() -> outerWorld.writeInt(i))),
                 Case(instanceOf(Double.class), d -> run(() -> outerWorld.writeDouble(d))),
                 Case($(), o -> { throw new NumberFormatException(); })
         );

         assertThat(outerWorld.effect).isEqualTo(.1d);
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

    // Ambiguity check

    @Test
    public void shouldNotAmbiguous() {

        final Option<String> ok = Option.of("ok");

        { // value
            // Case("1", o -> "ok"); // Not possible, would lead to ambiguities (see below)
            assertThat(Case("1", () -> "ok").apply("1")).isEqualTo(ok);
            assertThat(Case("1", "ok").apply("1")).isEqualTo(ok);
        }

        { // predicate as variable
            Predicate<String> p = s -> true;
            assertThat(Case(p, o -> "ok").apply("1")).isEqualTo(ok); // ambiguous, if Case(T, Function<T, R>) present
            assertThat(Case(p, () -> "ok").apply("1")).isEqualTo(ok);
            assertThat(Case(p, "ok").apply("1")).isEqualTo(ok);
        }

        { // predicate as lambda
            assertThat(Case(o -> true, o -> "ok").apply("1")).isEqualTo(ok); // ambiguous, if Case(T, Function<T, R>) present
            assertThat(Case(o -> true, () -> "ok").apply("1")).isEqualTo(ok);
            assertThat(Case(o -> true, "ok").apply("1")).isEqualTo(ok);
        }

        { // $(predicate)
            assertThat(Case($(o -> true), o -> "ok").apply("1")).isEqualTo(ok); // ambiguous, if Case(T, Function<T, R>) present
            assertThat(Case($(o -> true), () -> "ok").apply("1")).isEqualTo(ok);
            assertThat(Case($(o -> true), "ok").apply("1")).isEqualTo(ok);
        }

        { // $(value)
            assertThat(Case($("1"), o -> "ok").apply("1")).isEqualTo(ok); // ambiguous, if Case(T, Function<T, R>) present
            assertThat(Case($("1"), () -> "ok").apply("1")).isEqualTo(ok);
            assertThat(Case($("1"), "ok").apply("1")).isEqualTo(ok);
        }
    }
}
