/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionsTest {

    // -- lambda reflection tests

    @Test
    public void shouldRecognizeLambdaSignature() {
        final Function1<Integer, Integer> f = i -> i + 1;
        assertThat(f.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeLiftedLambdaSignature() {
        final Function1<Integer, Integer> f = Function1.lift(i -> i + 1);
        assertThat(f.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeSimpleMethodReferenceSignature() {
        final class Test {
            Integer method(Integer i) {
                return i + 1;
            }
        }
        final Test test = new Test();
        final Function1<Integer, Integer> f = test::method;
        assertThat(f.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeLambdaCallSignature() {
        final Function<Integer, Integer> f1 = i -> i + 1;
        final Function1<Integer, Integer> f2 = i -> f1.apply(i);
        assertThat(f2.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeLiftedLambdaCallSignature() {
        final Function<Integer, Integer> f1 = i -> i + 1;
        final Function1<Integer, Integer> f2 = Function1.lift(i -> f1.apply(i));
        assertThat(f2.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeMethodReferenceSignature() {
        final Function<Integer, Integer> f1 = i -> i + 1;
        final Function1<Integer, Integer> f2 = f1::apply;
        assertThat(f2.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldRecognizeLiftedMethodReferenceSignature() {
        final Function<Integer, Integer> f1 = i -> i + 1;
        final Function1<Integer, Integer> f2 = Function1.lift(f1::apply);
        assertThat(f2.getType().toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldSerializeDeserializeFunction1Type() {
        final Function1<Integer, String> f = String::valueOf;
        final byte[] typeBytes = Serializables.serialize(f.getType());
        final Function1.Type<Integer, String> type = Serializables.deserialize(typeBytes);
        assertThat(type.toString()).isEqualTo("(java.lang.Integer) -> java.lang.String");
    }
}
