/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.Lang;
import javaslang.Lang.UnsatisfiedRequirementException;

import org.junit.Test;

public class LangTest {

	@Test
	public void shouldPassOnTrueCondition() {
		Lang.require(true, "you will never see this");
	}

	@Test(expected = UnsatisfiedRequirementException.class)
	public void shouldThrowOnFalseCondition() {
		Lang.require(false, "expected");
	}

	@Test(expected = Error.class)
	public void shouldEvaluateNonLambdas() {
		Lang.require(true, getMessage());
	}

	@Test
	public void shouldNotEvaluateLambdas() {
		Lang.require(true, () -> getMessage());
	}

	private String getMessage() {
		throw new Error();
	}

}
