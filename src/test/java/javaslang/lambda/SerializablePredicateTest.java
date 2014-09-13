/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.invoke.MethodType;

import org.junit.Test;

public class SerializablePredicateTest {

	@Test
	public void shouldGetMethodTypeOfSerializablePredicate() {
		final SerializablePredicate<?> lambda = o -> true;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}
}
