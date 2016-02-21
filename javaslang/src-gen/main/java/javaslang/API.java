/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang contributors
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import javaslang.collection.Stream;

public final class API {

    private API() {
    }

    public static <T1> For1<T1> For(Iterable<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1<>(Stream.ofAll(ts1));
    }

    public static <T1, T2> For2<T1, T2> For(Iterable<T1> ts1, Iterable<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2<>(Stream.ofAll(ts1), Stream.ofAll(ts2));
    }

    public static <T1, T2, T3> For3<T1, T2, T3> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3));
    }

    public static <T1, T2, T3, T4> For4<T1, T2, T3, T4> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3), Stream.ofAll(ts4));
    }

    public static <T1, T2, T3, T4, T5> For5<T1, T2, T3, T4, T5> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3), Stream.ofAll(ts4), Stream.ofAll(ts5));
    }

    public static <T1, T2, T3, T4, T5, T6> For6<T1, T2, T3, T4, T5, T6> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3), Stream.ofAll(ts4), Stream.ofAll(ts5), Stream.ofAll(ts6));
    }

    public static <T1, T2, T3, T4, T5, T6, T7> For7<T1, T2, T3, T4, T5, T6, T7> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3), Stream.ofAll(ts4), Stream.ofAll(ts5), Stream.ofAll(ts6), Stream.ofAll(ts7));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8<T1, T2, T3, T4, T5, T6, T7, T8> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7, Iterable<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8<>(Stream.ofAll(ts1), Stream.ofAll(ts2), Stream.ofAll(ts3), Stream.ofAll(ts4), Stream.ofAll(ts5), Stream.ofAll(ts6), Stream.ofAll(ts7), Stream.ofAll(ts8));
    }

    public static class For1<T1> {

        private Stream<T1> stream1;

        private For1(Stream<T1> stream1) {
            this.stream1 = stream1;
        }

        public <R> Stream<R> yield(Function<? super T1, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                 stream1.map(t1 -> f.apply(t1));
        }
    }

    public static class For2<T1, T2> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;

        private For2(Stream<T1> stream1, Stream<T2> stream2) {
            this.stream1 = stream1;
            this.stream2 = stream2;
        }

        public <R> Stream<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 -> stream2.map(t2 -> f.apply(t1, t2)));
        }
    }

    public static class For3<T1, T2, T3> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;

        private For3(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
        }

        public <R> Stream<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 -> stream3.map(t3 -> f.apply(t1, t2, t3))));
        }
    }

    public static class For4<T1, T2, T3, T4> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;
        private Stream<T4> stream4;

        private For4(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3, Stream<T4> stream4) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
        }

        public <R> Stream<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 -> stream4.map(t4 -> f.apply(t1, t2, t3, t4)))));
        }
    }

    public static class For5<T1, T2, T3, T4, T5> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;
        private Stream<T4> stream4;
        private Stream<T5> stream5;

        private For5(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3, Stream<T4> stream4, Stream<T5> stream5) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
        }

        public <R> Stream<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 -> stream5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
        }
    }

    public static class For6<T1, T2, T3, T4, T5, T6> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;
        private Stream<T4> stream4;
        private Stream<T5> stream5;
        private Stream<T6> stream6;

        private For6(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3, Stream<T4> stream4, Stream<T5> stream5, Stream<T6> stream6) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
        }

        public <R> Stream<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 -> stream6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
        }
    }

    public static class For7<T1, T2, T3, T4, T5, T6, T7> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;
        private Stream<T4> stream4;
        private Stream<T5> stream5;
        private Stream<T6> stream6;
        private Stream<T7> stream7;

        private For7(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3, Stream<T4> stream4, Stream<T5> stream5, Stream<T6> stream6, Stream<T7> stream7) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
            this.stream7 = stream7;
        }

        public <R> Stream<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 ->
                stream6.flatMap(t6 -> stream7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
        }
    }

    public static class For8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private Stream<T1> stream1;
        private Stream<T2> stream2;
        private Stream<T3> stream3;
        private Stream<T4> stream4;
        private Stream<T5> stream5;
        private Stream<T6> stream6;
        private Stream<T7> stream7;
        private Stream<T8> stream8;

        private For8(Stream<T1> stream1, Stream<T2> stream2, Stream<T3> stream3, Stream<T4> stream4, Stream<T5> stream5, Stream<T6> stream6, Stream<T7> stream7, Stream<T8> stream8) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
            this.stream7 = stream7;
            this.stream8 = stream8;
        }

        public <R> Stream<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 ->
                stream6.flatMap(t6 ->
                stream7.flatMap(t7 -> stream8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
        }
    }
}