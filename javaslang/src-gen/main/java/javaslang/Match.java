/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javaslang.control.Option;

public interface Match<R> {

    /**
     * Entry point of the match API.
     */
    static <T> MatchBuilder<T> match(T value) {
        return new MatchBuilder<>(value); /*TODO: Untyped*/
    }

    // -- Atomic matchers $_, $(), $(val)

    Pattern0 $_ = new Pattern0() {
        @Override
        public Option<Void> apply(Object any) {
            return Option.nothing();
        }
    };

    static <T1> Pattern1<T1, T1> $(T1 prototype) {
      return new Pattern1<T1, T1>() {
                  @SuppressWarnings("unchecked")
                  @Override
                  public Option<T1> apply(Object that) {
                      // 'that' is of type T1 because T1 is injected from the outside
                      return Objects.equals(that, prototype) ? Option.some((T1) that) : Option.none();
                  }
              };
    }

    static <T> InversePattern<T> $() {
        return new InversePattern<T>() {
            @Override
            public Option<T> apply(T t) {
                return Option.some(t);
            }
        };
    }

    // -- Match DSL

    final class MatchBuilder<T> {

        private T value;

        private MatchBuilder(T value) {
            this.value = value;
        }

        /*TODO: <T1> Case1<T, T1> _case(T value) { ... }*/

        public Case0<T> _case(Pattern0 pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case0<>(this, pattern.apply(value));
        }

        public <T1> Case1<T, T1> _case(Pattern1<T, T1> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case1<>(this, pattern.apply(value));
        }

        public <T1, T2> Case2<T, T1, T2> _case(Pattern2<T, T1, T2> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case2<>(this, pattern.apply(value));
        }

        public <T1, T2, T3> Case3<T, T1, T2, T3> _case(Pattern3<T, T1, T2, T3> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case3<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4> Case4<T, T1, T2, T3, T4> _case(Pattern4<T, T1, T2, T3, T4> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case4<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5> Case5<T, T1, T2, T3, T4, T5> _case(Pattern5<T, T1, T2, T3, T4, T5> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case5<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6> Case6<T, T1, T2, T3, T4, T5, T6> _case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case6<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6, T7> Case7<T, T1, T2, T3, T4, T5, T6, T7> _case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case7<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6, T7, T8> Case8<T, T1, T2, T3, T4, T5, T6, T7, T8> _case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Case8<>(this, pattern.apply(value));
        }
    }

    final class Case0<T> {

        private final MatchBuilder<T> builder;
        private final Option<Void> option;

        private Case0(MatchBuilder<T> builder, Option<Void> option) {
            this.builder = builder;
            this.option = option;
        }

        public <R> MatchBuilder<T> then(Supplier<? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            option.map(ingnored -> f.get());
            return builder;
        }
    }

    final class Case1<T, T1> {

        private final MatchBuilder<T> builder;
        private final Option<T1> option;

        private Case1(MatchBuilder<T> builder, Option<T1> option) {
            this.builder = builder;
            this.option = option;
        }

        public <R> MatchBuilder<T> then(Function<? super T1, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            option.map(f::apply);
            return builder;
        }
    }

    final class Case2<T, T1, T2> {

        final MatchBuilder<T> builder;
        final Option<Tuple2<T1, T2>> option;

        Case2(MatchBuilder<T> builder, Option<Tuple2<T1, T2>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(BiFunction<? super T1, ? super T2, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case3<T, T1, T2, T3> {

        final MatchBuilder<T> builder;
        final Option<Tuple3<T1, T2, T3>> option;

        Case3(MatchBuilder<T> builder, Option<Tuple3<T1, T2, T3>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case4<T, T1, T2, T3, T4> {

        final MatchBuilder<T> builder;
        final Option<Tuple4<T1, T2, T3, T4>> option;

        Case4(MatchBuilder<T> builder, Option<Tuple4<T1, T2, T3, T4>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case5<T, T1, T2, T3, T4, T5> {

        final MatchBuilder<T> builder;
        final Option<Tuple5<T1, T2, T3, T4, T5>> option;

        Case5(MatchBuilder<T> builder, Option<Tuple5<T1, T2, T3, T4, T5>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case6<T, T1, T2, T3, T4, T5, T6> {

        final MatchBuilder<T> builder;
        final Option<Tuple6<T1, T2, T3, T4, T5, T6>> option;

        Case6(MatchBuilder<T> builder, Option<Tuple6<T1, T2, T3, T4, T5, T6>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case7<T, T1, T2, T3, T4, T5, T6, T7> {

        final MatchBuilder<T> builder;
        final Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> option;

        Case7(MatchBuilder<T> builder, Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    final class Case8<T, T1, T2, T3, T4, T5, T6, T7, T8> {

        final MatchBuilder<T> builder;
        final Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> option;

        Case8(MatchBuilder<T> builder, Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> option) {
            this.builder = builder;
            this.option = option;
        }

        <R> MatchBuilder<T> then(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return builder;
        }
    }

    // -- Match Patterns
    //    These can't be @FunctionalInterfaces because of ambiguities.
    //    For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

    // Used by any-match $() to inject a type into the pattern.
    abstract class InversePattern<T> {
        public abstract Option<T> apply(T t);
    }

    abstract class Pattern0 {
        public abstract Option<Void> apply(Object o);
    }

    abstract class Pattern1<T, T1> {
        public abstract Option<T1> apply(Object o);
    }

    abstract class Pattern2<T, T1, T2> {
        public abstract Option<Tuple2<T1, T2>> apply(Object o);
    }

    abstract class Pattern3<T, T1, T2, T3> {
        public abstract Option<Tuple3<T1, T2, T3>> apply(Object o);
    }

    abstract class Pattern4<T, T1, T2, T3, T4> {
        public abstract Option<Tuple4<T1, T2, T3, T4>> apply(Object o);
    }

    abstract class Pattern5<T, T1, T2, T3, T4, T5> {
        public abstract Option<Tuple5<T1, T2, T3, T4, T5>> apply(Object o);
    }

    abstract class Pattern6<T, T1, T2, T3, T4, T5, T6> {
        public abstract Option<Tuple6<T1, T2, T3, T4, T5, T6>> apply(Object o);
    }

    abstract class Pattern7<T, T1, T2, T3, T4, T5, T6, T7> {
        public abstract Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> apply(Object o);
    }

    abstract class Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> {
        public abstract Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> apply(Object o);
    }
}