/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.option;

import static javaslang.match.Matchs.caze;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class OptionTest {
	
	@Test
	public void shouldMapNullToNone() throws Exception {
		assertThat(Option.of(null)).isEqualTo(None.instance());
	}
	
	@Test
	public void shouldMapNonNullToSome() throws Exception {
		final Option<?> option = Option.of(new Object());
		assertThat(option.isPresent()).isTrue();
	}

	@Test
	public void shouldWrapNullInSome() throws Exception {
		final Some<?> some = new Some<>(null);
		assertThat(some.get()).isEqualTo(null);
	}
	
	@Test
	public void shouldMatchSome() {
		final int actual = Option.of("ok").match(
				caze((Some<?> some) -> 1).
				caze((None<?> none) -> 0)
		);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchNone() {
		final int actual = Option.of(null).match(
				caze((Some<?> some) -> 1).
				caze((None<?> none) -> 0)
		);
		assertThat(actual).isEqualTo(0);
	}
	
}
