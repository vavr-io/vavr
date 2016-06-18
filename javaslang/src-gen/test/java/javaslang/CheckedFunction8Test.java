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

public class CheckedFunction8Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction8.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction8.lift((o1, o2, o3, o4, o5, o6, o7, o8) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        assertThat(f.arity()).isEqualTo(8);
    }

    @Test
    public void shouldConstant() throws Throwable {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = CheckedFunction8.constant(6);
        assertThat(f.apply(1, 2, 3, 4, 5, 6, 7, 8)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        final CheckedFunction1<Tuple8<Object, Object, Object, Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + integer.getAndIncrement();
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4, 5, 6, 7, 8);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4, 5, 6, 7, 8)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 7  + 8  + 9  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 7  + 8  + 9  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6, i7, i8) -> null;
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6, i7, i8) -> null;
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null, null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5, i6, i7, i8) -> null;
        final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final CheckedFunction8<String, String, String, String, String, String, String, String, MessageDigest> digest = (s1, s2, s3, s4, s5, s6, s7, s8) -> MessageDigest.getInstance(s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8);

    @Test
    public void shouldRecover() {
        Function8<String, String, String, String, String, String, String, String, MessageDigest> recover = digest.recover(throwable -> (s1, s2, s3, s4, s5, s6, s7, s8) -> null);
        MessageDigest md5 = recover.apply("M", "D", "5", "", "", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("U", "n", "k", "n", "o", "w", "n", "")).isNull();
    }

    @Test
    public void shouldRecoverNonNull() {
        Function8<String, String, String, String, String, String, String, String, MessageDigest> recover = digest.recover(throwable -> null);
        MessageDigest md5 = recover.apply("M", "D", "5", "", "", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = Function8.liftTry(recover).apply("U", "n", "k", "n", "o", "w", "n", "");
        assertThat(unknown).isNotNull();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
    }

    @Test
    public void shouldUncheckedWork() {
        Function8<String, String, String, String, String, String, String, String, MessageDigest> unchecked = digest.unchecked();
        MessageDigest md5 = unchecked.apply("M", "D", "5", "", "", "", "", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldUncheckedThrowIllegalState() {
        Function8<String, String, String, String, String, String, String, String, MessageDigest> unchecked = digest.unchecked();
        unchecked.apply("U", "n", "k", "n", "o", "w", "n", "");
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        Function8<String, String, String, String, String, String, String, String, Try<MessageDigest>> liftTry = CheckedFunction8.liftTry(digest);
        Try<MessageDigest> md5 = liftTry.apply("M", "D", "5", "", "", "", "", "");
        assertThat(md5.isSuccess()).isTrue();
        assertThat(md5.get()).isNotNull();
        assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.get().getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = liftTry.apply("U", "n", "k", "n", "o", "w", "n", "");
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull();
        assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
    }

    private static final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4, i5, i6, i7, i8) -> i1 <= 0 ? i1 : CheckedFunction8Test.recurrent2.apply(i1 - 1, i2, i3, i4, i5, i6, i7, i8) + 1;
    private static final CheckedFunction8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> recurrent2 = CheckedFunction8Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11, 11, 11, 11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22, 22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}