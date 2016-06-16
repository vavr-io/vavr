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
import org.junit.Test;

public class CheckedFunction4Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction4.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction4.lift((o1, o2, o3, o4) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.arity()).isEqualTo(4);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Tuple4<Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> i1 + i2 + i3 + i4 + integer.getAndIncrement();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 )).isEqualTo(2  + 3  + 4  + 5  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 )).isEqualTo(2  + 3  + 4  + 5  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldRecover() {
        CheckedFunction4<String, String, String, String, MessageDigest> digest = (s1, s2, s3, s4) -> MessageDigest.getInstance(s1 + s2 + s3 + s4);
        Function4<String, String, String, String, MessageDigest> recover = digest.recover((tuple, throwable) -> null);
        MessageDigest md5 = recover.apply("M", "D", "5", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("U", "n", "k", "nown")).isNull();
    }

    private static final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4) -> i1 <= 0 ? i1 : CheckedFunction4Test.recurrent2.apply(i1 - 1, i2, i3, i4) + 1;
    private static final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> recurrent2 = CheckedFunction4Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction4<Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}