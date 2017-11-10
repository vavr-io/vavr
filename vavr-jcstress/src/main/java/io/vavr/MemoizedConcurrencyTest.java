package io.vavr;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.LongResult1;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@Outcome(id = "1", expect = ACCEPTABLE, desc = "Source touched only once.")
@Outcome(id = "2", expect = FORBIDDEN, desc = "Source touched twice.")
public class MemoizedConcurrencyTest {

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function1MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function1<Object, Long> lambda = (o1) -> source.incrementAndGet();
        Function1<Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function2MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function2<Object, Object, Long> lambda = (o1, o2) -> source.incrementAndGet();
        Function2<Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function3MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function3<Object, Object, Object, Long> lambda = (o1, o2, o3) -> source.incrementAndGet();
        Function3<Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function4MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function4<Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4) -> source.incrementAndGet();
        Function4<Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3, 4);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function5MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function5<Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5) -> source.incrementAndGet();
        Function5<Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3, 4, 5);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function6MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function6<Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6) -> source.incrementAndGet();
        Function6<Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3, 4, 5, 6);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function7MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function7<Object, Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6, o7) -> source.incrementAndGet();
        Function7<Object, Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3, 4, 5, 6, 7);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class Function8MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        Function8<Object, Object, Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> source.incrementAndGet();
        Function8<Object, Object, Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.apply(1, 2, 3, 4, 5, 6, 7, 8);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction1MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction1<Object, Long> lambda = (o1) -> source.incrementAndGet();
        CheckedFunction1<Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction2MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction2<Object, Object, Long> lambda = (o1, o2) -> source.incrementAndGet();
        CheckedFunction2<Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction3MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction3<Object, Object, Object, Long> lambda = (o1, o2, o3) -> source.incrementAndGet();
        CheckedFunction3<Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction4MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction4<Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4) -> source.incrementAndGet();
        CheckedFunction4<Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3, 4);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction5MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction5<Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5) -> source.incrementAndGet();
        CheckedFunction5<Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3, 4, 5);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction6MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction6<Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6) -> source.incrementAndGet();
        CheckedFunction6<Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3, 4, 5, 6);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction7MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6, o7) -> source.incrementAndGet();
        CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3, 4, 5, 6, 7);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }

    @JCStressTest
    @JCStressMeta(MemoizedConcurrencyTest.class)
    @State
    public static class CheckedFunction8MemoizedConcurrencyTest {
        AtomicLong source = new AtomicLong(0);
        CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Long> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> source.incrementAndGet();
        CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Long> memoized = lambda.memoized();
        Supplier<Long> target = () -> memoized.unchecked().apply(1, 2, 3, 4, 5, 6, 7, 8);

        @Actor
        public void actor1(LongResult1 r) {
            r.r1 = target.get();
        }

        @Actor
        public void actor2(LongResult1 r) {
            r.r1 = target.get();
        }

        @Arbiter
        public void arbiter(LongResult1 r) {
            r.r1 = source.get();
        }
    }
}
