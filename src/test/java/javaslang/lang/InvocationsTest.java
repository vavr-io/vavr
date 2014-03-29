package javaslang.lang;

import javaslang.match.Match.SerializableFunction;

import org.junit.Test;

public class InvocationsTest {
	
	@Test
	public void shouldXxx() {
		final SerializableFunction<?, ?> lambda = getLambda((Integer i) -> i.toString());
		Invocations.getLambdaSignature(lambda);
		// TODO
	}
	
	<T,R> SerializableFunction<T,R> getLambda(SerializableFunction<T,R> lambda) {
		return lambda;
	}

}
