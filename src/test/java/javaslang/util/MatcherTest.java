package javaslang.util;

import static javaslang.util.Matchers.caze;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;


public class MatcherTest {
	
	@Test
	public void shouldMatchByValuesUsingSupplier() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", o -> 1)
			.apply("1");
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", (String s) -> 1).apply("1");
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldThrowOnNoMatchByValue() {
		Matcher.of(Integer.class)
			.caze("1", o -> 1)
			.apply("2");
	}
	
	@Test
	public void shouldMatchByValueOnMultipleCases() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", o -> 1)
			.caze("2", o -> 2)
			.caze("3", o -> 3)
			.apply("2");
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByTypeOnMultipleCasesUsingGenericType() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> (int) b)
				.<Short>	caze(s -> (int) s)
				.<Integer>	caze(i -> i)
				.apply(1);
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldMatchByIntOnMultipleCasesUsingGenericType() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Double>	caze(d -> 2)
				.<Integer>	caze(i -> 3)
				.apply(1);
		assertThat(actual).isEqualTo(3);
	}

	@Test
	public void shouldMatchByDoubleOnMultipleCasesUsingGenericType() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Double>	caze(d -> 2)
				.<Integer>	caze(i -> 3)
				.apply(1.0d);
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldMatchDoubleOnMultipleCasesUsingTypedParam() {
		final int actual =
				caze( (Byte b) -> 1 ).
				caze( (Double d) -> 2 ).
				caze( (Integer i) -> 3 ).
				apply( 1.0d );
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchMaxIntOnMultipleCasesUsingTypedParam() {
		final int actual = Matcher.of(Integer.class)
				.caze( (Byte b)		-> (int) b )
				.caze( (Short s)	-> (int) s )
				.caze( (Integer i)	-> i )
				.apply(Integer.MAX_VALUE);
		assertThat(actual).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	public void shouldMatchByTypeOnMultipleCases6() {
		final int actual = Matcher.of(Character.class)
				.caze(1, o -> 'a')
				.caze((Number n) -> 'b')
				.caze(x -> 'c')
				.apply(2.0d);
		assertThat(actual).isEqualTo('b');
	}
	
	@Test
	public void shouldMatchContainerTypeByContainedTypeOnMultipleCases() {
		final int actual = Matcher.of(Integer.class)
			.<Some<Integer>>	caze(some -> some.get())
			.<Some<String>>		caze(some -> Integer.parseInt(some.get()))
			.<None<?>>			caze(none -> -1)
			.caze(o -> -13)
			.apply(new Some<>("123"));
		assertThat(actual).isEqualTo(123);
	}
}
