package javaslang.option;

import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

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
	
}
