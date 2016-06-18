/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;
import javaslang.control.Try;
import org.junit.Test;

public class CheckedFunction6Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction6.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction6.lift((o1, o2, o3, o4, o5, o6) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.arity()).isEqualTo(6);
    }

    @Test
    public void shouldConstant() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = CheckedFunction6.constant(6);
        assertThat(f.apply(1, 2, 3, 4, 5, 6)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Tuple6<Object, Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6 + integer.getAndIncrement();
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4, 5, 6);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4, 5, 6)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 , 7 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 7  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 , 7 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 7  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6) -> null;
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6) -> null;
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6) -> null;
        final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final CheckedFunction6<String, String, String, String, String, String, MessageDigest> digest = (s1, s2, s3, s4, s5, s6) -> MessageDigest.getInstance(s1 + s2 + s3 + s4 + s5 + s6);

    @Test
    public void shouldRecover() {
        Function6<String, String, String, String, String, String, MessageDigest> recover = digest.recover(throwable -> (s1, s2, s3, s4, s5, s6) -> null);
        MessageDigest md5 = recover.apply("M", "D", "5", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("U", "n", "k", "n", "o", "wn")).isNull();
    }

    @Test
    public void shouldRecoverNonNull() {
        Function6<String, String, String, String, String, String, MessageDigest> recover = digest.recover(throwable -> null);
        MessageDigest md5 = recover.apply("M", "D", "5", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = Function6.liftTry(recover).apply("U", "n", "k", "n", "o", "wn");
        assertThat(unknown).isNotNull();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
    }

    @Test
    public void shouldUncheckedWork() {
        Function6<String, String, String, String, String, String, MessageDigest> unchecked = digest.unchecked();
        MessageDigest md5 = unchecked.apply("M", "D", "5", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldUncheckedThrowIllegalState() {
        Function6<String, String, String, String, String, String, MessageDigest> unchecked = digest.unchecked();
        unchecked.apply("U", "n", "k", "n", "o", "wn");
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        Function6<String, String, String, String, String, String, Try<MessageDigest>> liftTry = CheckedFunction6.liftTry(digest);
        Try<MessageDigest> md5 = liftTry.apply("M", "D", "5", "", "", "");
        assertThat(md5.isSuccess()).isTrue();
        assertThat(md5.get()).isNotNull();
        assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.get().getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = liftTry.apply("U", "n", "k", "n", "o", "wn");
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull();
        assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
    }

    private static final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4, i5, i6) -> i1 <= 0 ? i1 : CheckedFunction6Test.recurrent2.apply(i1 - 1, i2, i3, i4, i5, i6) + 1;
    private static final CheckedFunction6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> recurrent2 = CheckedFunction6Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11, 11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}