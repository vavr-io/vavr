/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import javaslang.AssertionsExtensions;
import javaslang.Requirements.UnsatisfiedRequirementException;

import org.junit.Test;

// tests more or less for code coverage only 
public class TokenTest {

	// -- requirements

	@Test
	public void shouldThrowWhenTextIsNull() {
		AssertionsExtensions.assertThat(() -> new Token("id", null, 0, 0)).isThrowing(
				UnsatisfiedRequirementException.class, "text is null");
	}

	@Test
	public void shouldThrowWhenIndexIsNegative() {
		AssertionsExtensions.assertThat(() -> new Token("id", "", -1, 0)).isThrowing(
				UnsatisfiedRequirementException.class, "index out of bounds: -1");
	}

	@Test
	public void shouldThrowWhenIndexExceedsTextLength() {
		AssertionsExtensions.assertThat(() -> new Token("id", "", 1, 0)).isThrowing(
				UnsatisfiedRequirementException.class, "index out of bounds: 1");
	}

	@Test
	public void shouldThrowWhenLengthIsNegative() {
		AssertionsExtensions.assertThat(() -> new Token("id", "", 0, -1)).isThrowing(
				UnsatisfiedRequirementException.class, "negative length: -1");
	}

	@Test
	public void shouldThrowWhenEndIndexExceedsTextLength() {
		AssertionsExtensions.assertThat(() -> new Token("id", "", 0, 1)).isThrowing(
				UnsatisfiedRequirementException.class, "(index + length) exceeds text: (0 + 1)");
	}

	// -- getters (oh noes! - thank you code coverage!)

	@Test
	public void shouldGetId() {
		final Token token = new Token("id", "", 0, 0);
		assertThat(token.getId()).isEqualTo("id");
	}

	@Test
	public void shouldGetText() {
		final Token token = new Token(null, "text", 0, 0);
		assertThat(token.getText()).isEqualTo("text");
	}

	@Test
	public void shouldGetStartIndex() {
		final Token token = new Token(null, "abc", 1, 2);
		assertThat(token.getStartIndex()).isEqualTo(1);
	}

	@Test
	public void shouldGetEndIndex() {
		final Token token = new Token(null, "abc", 1, 2);
		assertThat(token.getEndIndex()).isEqualTo(3);
	}

	@Test
	public void shouldGetLength() {
		final Token token = new Token(null, "abc", 0, 3);
		assertThat(token.getEndIndex()).isEqualTo(3);
	}

	@Test
	public void shouldGetNonEmptyValue() {
		final Token token = new Token(null, "javaslang", 5, 4);
		assertThat(token.getValue()).isEqualTo("lang");
	}

	@Test
	public void shouldGetEmptyValue() {
		final Token token = new Token(null, "abc", 3, 0);
		assertThat(token.getValue()).isEqualTo("");
	}

	// -- Object.*

	// equals

	@Test
	public void shouldEqualSameObject() {
		final Token token = new Token("id", "", 0, 0);
		assertThat(token.equals(token)).isTrue();
	}

	@Test
	public void shouldNotEqualNull() {
		final Token token = new Token("id", "", 0, 0);
		assertThat(token.equals(null)).isFalse();
	}

	@Test
	public void shouldNotEqualObjectOfDifferentType() {
		final Token token = new Token("id", "", 0, 0);
		assertThat(token.equals(new Object())).isFalse();
	}

	@Test
	public void shouldEqualDiffernetObject() {
		final Token token1 = new Token("id", "", 0, 0);
		final Token token2 = new Token("id", "", 0, 0);
		assertThat(token1.equals(token2)).isTrue();
	}

	// hashCode

	@Test
	public void shouldHashTokenAsExpected() {
		final Token token = new Token("id", "", 0, 0);
		assertThat(token.hashCode()).isEqualTo(Objects.hash(token.getId(), token.getStartIndex(), token.getLength()));
	}

	// toString

	@Test
	public void shouldConvertIdentifiedTokenToString() {
		final Token token = new Token("id", "text", 0, 4);
		assertThat(token.toString()).isEqualTo("id");
	}

	@Test
	public void shouldConvertNonIdentifiedTokenToString() {
		final Token token = new Token(null, "text", 0, 4);
		assertThat(token.toString()).isEqualTo("'text'");
	}
}
