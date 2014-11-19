/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;
import javaslang.Require.UnsatisfiedRequirementException;

import org.junit.Test;

public class RequirementsTest {

	@Test
	public void shouldNotBeInstantiable() {
		AssertionsExtensions.assertThat(Require.class).isNotInstantiable();
	}

	// -- require(condition, message)

	@Test
	public void shouldThrowOnRequireWithMessageWhenConditionIsFalse() {
		AssertionsExtensions.assertThat(() -> Require.isTrue(false, "false")).isThrowing(
				UnsatisfiedRequirementException.class, "false");
	}

	@Test
	public void shouldPassOnRequireWithMessageWhenConditionIsTrue() {
		Require.isTrue(true, "");
	}

	// -- require(condition, () -> message)

	@Test
	public void shouldThrowOnRequireWithMessageSupplierWhenConditionIsFalse() {
		AssertionsExtensions.assertThat(() -> Require.isTrue(false, () -> "false")).isThrowing(
				UnsatisfiedRequirementException.class, "false");
	}

	@Test
	public void shouldPassOnRequireWithMessageSupplierWhenConditionIsTrue() {
		Require.isTrue(true, () -> "");
	}

	// -- requireNonNull(obj)

	@Test
	public void shouldRequireNonNullOnNonNull() {
		final Object o = new Object();
		assertThat(Require.nonNull(o) == o).isTrue();
	}

	@Test
	public void shouldRequireNonNullOnNull() {
		AssertionsExtensions.assertThat(() -> Require.nonNull(null)).isThrowing(
				UnsatisfiedRequirementException.class, "Object is null");
	}

	// -- requireNonNull(obj, message)

	@Test
	public void shouldRequireNonNullOnNonNullWithMessage() {
		final Object o = new Object();
		assertThat(Require.nonNull(o, "") == o).isTrue();
	}

	@Test
	public void shouldRequireNonNullOnNulWithMessage() {
		AssertionsExtensions.assertThat(() -> Require.nonNull(null, "null")).isThrowing(
				UnsatisfiedRequirementException.class, "null");
	}

	// -- requireNonNull(obj, () -> message)

	@Test
	public void shouldRequireNonNullOnNonNullWithMessageSupplier() {
		final Object o = new Object();
		assertThat(Require.nonNull(o, () -> "") == o).isTrue();
	}

	@Test
	public void shouldRequireNonNullOnNulWithMessageSupplier() {
		AssertionsExtensions.assertThat(() -> Require.nonNull(null, () -> "null")).isThrowing(
				UnsatisfiedRequirementException.class, "null");
	}

	// -- requireNotNullOrEmpty(array)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWhenNull() {
		final Object[] array = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array)).isThrowing(
				UnsatisfiedRequirementException.class, "Object is null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWhenEmpty() {
		final Object[] array = {};
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array)).isThrowing(
				UnsatisfiedRequirementException.class, "Array is empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForArrayWhenNotNullOrEmpty() {
		final Object[] array = { null };
		assertThat(Require.notNullOrEmpty(array) == array).isTrue();
	}

	// -- requireNotNullOrEmpty(array, message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWithMessageWhenNull() {
		final Object[] array = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array, "null")).isThrowing(
				UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWithMessageWhenEmpty() {
		final Object[] array = {};
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array, "empty")).isThrowing(
				UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForArrayWithMessageWhenNotNullOrEmpty() {
		final Object[] array = { null };
		assertThat(Require.notNullOrEmpty(array, "") == array).isTrue();
	}

	// -- requireNotNullOrEmpty(array, () -> message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWithMessageSupplierWhenNull() {
		final Object[] array = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array, () -> "null")).isThrowing(
				UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForArrayWithMessageSupplierWhenEmpty() {
		final Object[] array = {};
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(array, () -> "empty")).isThrowing(
				UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForArrayWithMessageSupplierWhenNotNullOrEmpty() {
		final Object[] array = { null };
		assertThat(Require.notNullOrEmpty(array, () -> "") == array).isTrue();
	}

	// -- requireNotNullOrEmpty(charSequence)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(charSequence)).isThrowing(
				UnsatisfiedRequirementException.class, "Object is null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(charSequence)).isThrowing(
				UnsatisfiedRequirementException.class, "CharSequence is empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForCharSequenceWhenNotNullOrEmpty() {
		final CharSequence charSequence = " ";
		assertThat(Require.notNullOrEmpty(charSequence) == charSequence).isTrue();
	}

	// -- requireNotNullOrEmpty(array, message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWithMessageWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(charSequence, "null")).isThrowing(
				UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWithMessageWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmpty(charSequence, "empty")).isThrowing(
				UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForCharSequenceWithMessageWhenNotNullOrEmpty() {
		final CharSequence charSequence = " ";
		assertThat(Require.notNullOrEmpty(charSequence, "") == charSequence).isTrue();
	}

	// -- requireNotNullOrEmpty(charSequence, () -> message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWithMessageSupplierWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmpty(charSequence, () -> "null"))
				.isThrowing(UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyForCharSequenceWithMessageSupplierWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmpty(charSequence, () -> "empty"))
				.isThrowing(UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyForCharSequenceWithMessageSupplierWhenNotNullOrEmpty() {
		final CharSequence charSequence = " ";
		assertThat(Require.notNullOrEmpty(charSequence, () -> "") == charSequence).isTrue();
	}

	// -- requireNotNullOrEmptyTrimmed(charSequence)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence)).isThrowing(
				UnsatisfiedRequirementException.class, "Object is null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence)).isThrowing(
				UnsatisfiedRequirementException.class, "CharSequence is empty");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWhenWhitespaceTrimmed() {
		final CharSequence charSequence = " ";
		AssertionsExtensions.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence)).isThrowing(
				UnsatisfiedRequirementException.class, "CharSequence is empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyTrimmedForCharSequenceWhenNotNullOrEmptyTrimmed() {
		final CharSequence charSequence = ".";
		assertThat(Require.notNullOrEmptyTrimmed(charSequence) == charSequence).isTrue();
	}

	// -- requireNotNullOrEmptyTrimmed(array, message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, "null"))
				.isThrowing(UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, "empty"))
				.isThrowing(UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageWhenWhitespaceTrimmed() {
		final CharSequence charSequence = " ";
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, "empty"))
				.isThrowing(UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageWhenNotNullOrEmptyTrimmed() {
		final CharSequence charSequence = ".";
		assertThat(Require.notNullOrEmptyTrimmed(charSequence, "") == charSequence).isTrue();
	}

	// -- requireNotNullOrEmptyTrimmed(charSequence, () -> message)

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageSupplierWhenNull() {
		final CharSequence charSequence = null;
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, () -> "null"))
				.isThrowing(UnsatisfiedRequirementException.class, "null");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageSupplierWhenEmpty() {
		final CharSequence charSequence = "";
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, () -> "empty"))
				.isThrowing(UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldThrowOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageSupplierWhenWhitespaceTrimmed() {
		final CharSequence charSequence = " ";
		AssertionsExtensions
				.assertThat(() -> Require.notNullOrEmptyTrimmed(charSequence, () -> "empty"))
				.isThrowing(UnsatisfiedRequirementException.class, "empty");
	}

	@Test
	public void shouldPassOnRequireNotNullOrEmptyTrimmedForCharSequenceWithMessageSupplierWhenNotNullOrEmptyTrimmed() {
		final CharSequence charSequence = ".";
		assertThat(Require.notNullOrEmptyTrimmed(charSequence, () -> "") == charSequence).isTrue();
	}
}
