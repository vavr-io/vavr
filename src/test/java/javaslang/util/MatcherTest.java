package javaslang.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;


public class MatcherTest {
	
	@Test(expected = MatchError.class)
	public void shouldThrowOnEmptyMatcher() {
		Matcher.create().apply(1);
	}
	
	@Test
	public void shouldMatchByValuesUsingSupplier() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", () -> 1)
			.apply("1");
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", s -> 1)
			.apply("1");
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldThrowOnNoMatchByValue() {
		Matcher.of(Integer.class)
			.caze("1", () -> 1)
			.apply("2");
	}
	
	@Test
	public void shouldMatchByValueOnMultipleCases() {
		final int actual = Matcher.of(Integer.class)
			.caze("1", () -> 1)
			.caze("2", () -> 2)
			.caze("3", () -> 3)
			.apply("2");
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByTypeOnMultipleCases1() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> (int) b)
				.<Short>	caze(s -> (int) s)
				.<Integer>	caze(i -> i)
				.apply(1);
			assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldMatchByTypeOnMultipleCases2() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Short>	caze(s -> 2)
				.<Double>	caze(d -> 3)
				.<Integer>	caze(i -> 4)
				.apply(1);
			assertThat(actual).isEqualTo(4);
	}

	@Test
	public void shouldMatchByTypeOnMultipleCases3() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Short>	caze(s -> 2)
				.<Float>	caze(f -> 3)
				.<Double>	caze(d -> 4)
				.<Integer>	caze(i -> 5)
				.apply(1.0d);
			assertThat(actual).isEqualTo(4);
	}
	
	@Test
	public void shouldMatchByTypeOnMultipleCases4() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Short>	caze(s -> 2)
				.<Float>	caze(f -> 3)
				.<Double>	caze(d -> 4)
				.<Integer>	caze(i -> 5)
				.apply(1.0f);
			assertThat(actual).isEqualTo(3);
	}

//	@Test
//	public void shouldMatchByTypeOnMultipleCases2() throws Exception {
//		final short x = 1; // TODO: check values bigger than Byte and/or Short
//		final int actual = Matcher.of(Integer.class)
//				.caze((Byte b) -> (int) b)
//				.caze((Short s) -> (int) s)
//				.caze((Integer i) -> i)
//				.apply(x);
//			assertThat(actual).isEqualTo(x);
//	}

//	@Test
//	public void shouldMatchWithGuardsMultipleCases() throws Exception {
//		// TODO: to be implemented
//		throw new UnsupportedOperationException();
//	}

	
//	final Matcher<Integer> matcher = Matcher.of(Integer.class)
//			// .caze(o -> { throw new RuntimeException("oh"); })
//			.caze((Some<Integer> some) -> some.get())
//			.caze(new Some<>(1.1d), () -> 22)
//			.<Some<Integer>>	caze(some -> some.get())
//			.<Some<String>>		caze(some -> Integer.parseInt(some.get()))
//			.<None<?>>			caze(none -> -1)
//			.caze(o -> -13);
//	
//	@Test
//	public void shouldDoTheJob() {
//
//		println("%s: %s", Option.of(1), matcher.apply(Option.of(1)));
//		println("%s: %s", Option.of("13"), matcher.apply(Option.of("13")));
//		println("%s: %s", Option.of(null), matcher.apply(Option.of(null)));
//		println("%s: %s", Option.of(1.1d), matcher.apply(Option.of(1.1d)));
//		println("%s: %s", null, matcher.apply(null));
//		
//	}

}
