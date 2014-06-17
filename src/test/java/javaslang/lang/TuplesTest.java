/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.lang;

import static org.fest.assertions.api.Assertions.assertThat;
import static javaslang.lang.Tuples.*;

import org.junit.Test;

public class TuplesTest {

	@Test
	public void shouldCreateEmptyTuple() {
		assertThat(t().toString()).isEqualTo("()");
	}

	@Test
	public void shouldCreateSingle() {
		assertThat(t(1).toString()).isEqualTo("(1)");
	}

	@Test
	public void shouldCreatePair() {
		assertThat(t(1, 2).toString()).isEqualTo("(1, 2)");
	}

	@Test
	public void shouldCreateTriple() {
		assertThat(t(1, 2, 3).toString()).isEqualTo("(1, 2, 3)");
	}

	@Test
	public void shouldCreateQuadruple() {
		assertThat(t(1, 2, 3, 4).toString()).isEqualTo("(1, 2, 3, 4)");
	}

	@Test
	public void shouldCreateQuintuple() {
		assertThat(t(1, 2, 3, 4, 5).toString()).isEqualTo("(1, 2, 3, 4, 5)");
	}

	@Test
	public void shouldCreateSextuple() {
		assertThat(t(1, 2, 3, 4, 5, 6).toString()).isEqualTo("(1, 2, 3, 4, 5, 6)");
	}

	@Test
	public void shouldCreateSeptuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7)");
	}

	@Test
	public void shouldCreateOctuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8)");
	}

	@Test
	public void shouldCreateNonuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8, 9).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9)");
	}

	@Test
	public void shouldCreateDecuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)");
	}

	@Test
	public void shouldCreateUndecuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)");
	}

	@Test
	public void shouldCreateDuodecuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)");
	}

	@Test
	public void shouldCreateTredecuple() {
		assertThat(t(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)");
	}

}
