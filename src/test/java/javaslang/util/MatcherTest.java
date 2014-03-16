package javaslang.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class MatcherTest {

	@Test
	public void shouldMatchNull() {
		final int actual = Matcher.of(Integer.class)
				.caze((String s) -> s.length())
				.caze(null, o -> 1)
				.apply(null);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Matcher.of(Integer.class)
				.caze("1", (String s) -> 1)
				.apply("1");
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
				.<Short>	caze(s -> s + 1)
				.<Integer>	caze(i -> i + 2)
				.apply((short) 1);
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldMatchByIntOnMultipleCasesUsingGenericType() {
		final int actual = Matcher.of(Integer.class)
				.<Byte>		caze(b -> 1)
				.<Short>	caze(s -> 2)
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
	public void shouldMatchByDoubleOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchers
				.caze((Byte b) -> 1)
				.caze((Double d) -> 2)
				.caze((Integer i) -> 3)
				.apply(1.0d);
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchers
				.caze((Byte b) -> (int) b)
				.caze((Double d) -> d.intValue())
				.caze((Integer i) -> i)
				.apply(Integer.MAX_VALUE);
		assertThat(actual).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	public void shouldMatchByAssignableTypeOnMultipleCases() {
		final int actual = Matchers
				.caze(1, o -> 'a')
				.caze((Number n) -> 'b')
				.caze(o -> 'c')
				.apply(2.0d);
		assertThat(actual).isEqualTo('b');
	}
	
	@Test
	public void shouldMatchDefaultCase() {
		final int actual = Matcher.of(Integer.class)
				.caze(null, o -> 1)
				.caze(o -> 2)
				.apply("default");
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldClarifyHereThatTypeErasureIsPresent() {
		final int actual = Matcher.of(Integer.class)
				.<Some<Integer>>	caze(some -> 1)
				.<Some<String>>		caze(some -> Integer.parseInt(some.get()))
				.apply(new Some<>("123"));
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldClarifyHereThatClassCastExceptionsAreInterpretedAsNotMatching() {
		final Integer actual = Matchers
				.caze(o -> (int) o)
				.caze(o -> -1)
				.apply("test");
		assertThat(actual).isEqualTo(-1);
	}

	@Test
	public void shouldClarifyHereThatTryWrapsExceptions() {
		final ClassCastException x = new ClassCastException();
		final Try<Integer> actual = Matchers
				.caze(o -> Try.<Integer>of(() -> { throw x; }))
				.apply("test");
		assertThat(actual.failed().get()).isEqualTo(x);
	}

	@Test
	public void shouldClarifyHereThatTrySeparatesClassCastExceptions() {
		final Try<Integer> actual = Matchers
				.caze((Integer i) -> Try.<Integer>of(() -> { throw new ClassCastException(); }))
				.caze((String s) -> new Success<>(1))
				.apply("test");
		assertThat(actual).isEqualTo(new Success<>(1));
	}

}
