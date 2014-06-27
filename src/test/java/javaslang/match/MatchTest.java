package javaslang.match;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javaslang.option.Some;

import org.junit.Test;

public class MatchTest {

	@Test
	public void shouldMatchNullAsPrototype() {
		final int actual = Matchs
				.caze((String s) -> s.length())
				.caze(null, o -> 1)
				.apply(null);
		assertThat(actual).isEqualTo(1);
	}
	
	@Test(expected = MatchError.class)
	public void shouldNotMatchNullAsType() {
		new Match<Boolean>()
				.caze((int i) -> false)
				.caze((Integer i) -> true)
				.apply((Integer) null);
	}
	
	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Matchs
				.caze("1", (String s) -> 1)
				.apply("1");
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldThrowOnNoMatchByValue() {
		Matchs.caze("1", o -> 1).apply("2");
	}
	
	@Test
	public void shouldMatchByValueOnMultipleCases() {
		final int actual = Matchs
				.caze("1", o -> 1)
				.caze("2", o -> 2)
				.caze("3", o -> 3)
				.apply("2");
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldMatchByDoubleOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchs
				.caze((Byte b) -> 1)
				.caze((Double d) -> 2)
				.caze((Integer i) -> 3)
				.apply(1.0d);
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchs
				.caze((Byte b) -> (int) b)
				.caze((Double d) -> d.intValue())
				.caze((Integer i) -> i)
				.apply(Integer.MAX_VALUE);
		assertThat(actual).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	public void shouldMatchByAssignableTypeOnMultipleCases() {
		final int actual = Matchs
				.caze(1, o -> 'a')
				.caze((Number n) -> 'b')
				.caze((Object o) -> 'c')
				.apply(2.0d);
		assertThat(actual).isEqualTo('b');
	}
	
	@Test
	public void shouldMatchDefaultCase() {
		final int actual = Matchs
				.caze(null, o -> 1)
				.caze((Object o) -> 2)
				.apply("default");
		assertThat(actual).isEqualTo(2);
	}
	
	@Test
	public void shouldClarifyHereThatTypeErasureIsPresent() {
		final int actual = new Match<Integer>()
				.caze((Some<Integer> some) -> 1)
				.caze((Some<String> some) -> Integer.parseInt(some.get()))
				.apply(new Some<>("123"));
		assertThat(actual).isEqualTo(1);
	}
	
	@Test
	public void shouldCompileAssignmentWithGenericWildcardType() {
		@SuppressWarnings("unused")
		final Match<List<?>> list = new Match<>();
	}
	
	@Test
	public void shouldMatchPrimitiveInt() {
		final boolean actual = new Match<Boolean>()
				.caze((int i) -> true)
				.caze((Integer i) -> false)
				.apply(1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveIntAsInteger() {
		final boolean actual = new Match<Boolean>()
				.caze((Integer i) -> true)
				.caze((int i) -> false)
				.apply(1);
		assertThat(actual).isTrue();
	}
	
	@Test
	public void shouldMatchIntegerAsPrimitiveInt() {
		final boolean actual = new Match<Boolean>()
				.caze((int i) -> true)
				.caze((Integer i) -> false)
				.apply(new Integer(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchInteger() {
		final boolean actual = new Match<Boolean>()
				.caze((Integer i) -> true)
				.caze((int i) -> false)
				.apply(new Integer(1));
		assertThat(actual).isTrue();
	}
	
	@Test
	public void shouldMatchPrimitiveBooleanValueAndApplyBooleanFunction() {
		final int actual = new Match<Integer>()
				.caze(true, b -> 1)
				.caze(Boolean.TRUE, b -> 2)
				.apply(true);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchPrimitiveBooleanValueAsBooleanAndApplyBooleanFunction() {
		final int actual = new Match<Integer>()
				.caze(Boolean.TRUE, b -> 1)
				.caze(true, b -> 2)
				.apply(true);
		assertThat(actual).isEqualTo(1);
	}

    // TODO: add tests for all primitive types + new Object() 

    @Test 
    public void shouldCompileObjectIntegerPrototypeCase() { 
            // Does *not* compile: new Match<>().caze(1, (int i) -> i); 
            new Match<>().caze(1, (Integer i) -> i); 
    }

    @Test 
    public void shouldCompileUnqualifiedIntegerPrototypeCase() { 
            new Match<>().caze(1, i -> i); 
    }
    
    @Test
    public void shouldMatchBooleanArray() {
    	final int actual = new Match<Integer>()
				.caze((boolean[] b) -> 1)
				.apply(new boolean[] { true });
		assertThat(actual).isEqualTo(1);
    }
	
}
