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
    static <T> Case<T> match(T value) {
        return new Case<>(value); /*TODO: CaseUntyped*/
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

    final class Case<T> /*TODO: implements Match<R>*/ {

        private T value;

        private Case(T value) {
            this.value = value;
        }

        /*TODO: <T1> Then1<T, T1> _case(T value) { ... }*/

        public Then0<T> _case(Pattern0 pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then0<>(this, pattern.apply(value));
        }

        public <T1> Then1<T, T1> _case(Pattern1<T, T1> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then1<>(this, pattern.apply(value));
        }

        public <T1, T2> Then2<T, T1, T2> _case(Pattern2<T, T1, T2> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then2<>(this, pattern.apply(value));
        }

        public <T1, T2, T3> Then3<T, T1, T2, T3> _case(Pattern3<T, T1, T2, T3> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then3<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4> Then4<T, T1, T2, T3, T4> _case(Pattern4<T, T1, T2, T3, T4> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then4<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5> Then5<T, T1, T2, T3, T4, T5> _case(Pattern5<T, T1, T2, T3, T4, T5> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then5<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6> Then6<T, T1, T2, T3, T4, T5, T6> _case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then6<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6, T7> Then7<T, T1, T2, T3, T4, T5, T6, T7> _case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then7<>(this, pattern.apply(value));
        }

        public <T1, T2, T3, T4, T5, T6, T7, T8> Then8<T, T1, T2, T3, T4, T5, T6, T7, T8> _case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern) {
            Objects.requireNonNull(pattern, "pattern is null");
            return new Then8<>(this, pattern.apply(value));
        }
    }

    final class Then0<T> {

        private final Case<T> _case;
        private final Option<Void> option;

        private Then0(Case<T> _case, Option<Void> option) {
            this._case = _case;
            this.option = option;
        }

        public <R> Case<T> then(Supplier<? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            option.map(ingnored -> f.get());
            return _case;
        }
    }

    final class Then1<T, T1> {

        private final Case<T> _case;
        private final Option<T1> option;

        private Then1(Case<T> _case, Option<T1> option) {
            this._case = _case;
            this.option = option;
        }

        public <R> Case<T> then(Function<? super T1, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            option.map(f::apply);
            return _case;
        }
    }

    final class Then2<T, T1, T2> {

        final Case<T> _case;
        final Option<Tuple2<T1, T2>> option;

        Then2(Case<T> _case, Option<Tuple2<T1, T2>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(BiFunction<? super T1, ? super T2, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then3<T, T1, T2, T3> {

        final Case<T> _case;
        final Option<Tuple3<T1, T2, T3>> option;

        Then3(Case<T> _case, Option<Tuple3<T1, T2, T3>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then4<T, T1, T2, T3, T4> {

        final Case<T> _case;
        final Option<Tuple4<T1, T2, T3, T4>> option;

        Then4(Case<T> _case, Option<Tuple4<T1, T2, T3, T4>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then5<T, T1, T2, T3, T4, T5> {

        final Case<T> _case;
        final Option<Tuple5<T1, T2, T3, T4, T5>> option;

        Then5(Case<T> _case, Option<Tuple5<T1, T2, T3, T4, T5>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then6<T, T1, T2, T3, T4, T5, T6> {

        final Case<T> _case;
        final Option<Tuple6<T1, T2, T3, T4, T5, T6>> option;

        Then6(Case<T> _case, Option<Tuple6<T1, T2, T3, T4, T5, T6>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then7<T, T1, T2, T3, T4, T5, T6, T7> {

        final Case<T> _case;
        final Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> option;

        Then7(Case<T> _case, Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
        }
    }

    final class Then8<T, T1, T2, T3, T4, T5, T6, T7, T8> {

        final Case<T> _case;
        final Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> option;

        Then8(Case<T> _case, Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> option) {
            this._case = _case;
            this.option = option;
        }

        <R> Case<T> then(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            option.map(tuple -> f.apply(tuple._1, tuple._2));
            return _case;
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