package javaslang.lang;

import javaslang.lang.Lang;

import org.junit.Test;

public class LangTest {

	@Test
	public void shouldPassOnTrueCondition() {
		Lang.require(true, "you will never see this");
	}

	@Test(expected = IllegalStateException.class)
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
