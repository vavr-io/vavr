package io.vavr;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.LongResult1;

import java.util.concurrent.atomic.AtomicLong;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Outcome(id = "1", expect = ACCEPTABLE, desc = "Source touched only once.")
@Outcome(id = "2", expect = FORBIDDEN, desc = "Source touched twice.")
@State
public class LazyConcurrencyTest {
    private AtomicLong source = new AtomicLong();
    private Lazy<Long> lazy = Lazy.of(source::incrementAndGet);

    @Actor
    public void actor1(LongResult1 r) {
        r.r1 = lazy.get();
    }

    @Actor
    public void actor2(LongResult1 r) {
        r.r1 = lazy.get();
    }

    @Arbiter
    public void arbiter(LongResult1 r) {
        r.r1 = source.get();
    }
}
