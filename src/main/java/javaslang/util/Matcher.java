package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Matcher<T> implements Function<Object, T> {

	private List<Case<T>> cases = new ArrayList<>();

	private Matcher() {
	}

	public <S> Matcher<T> caze(Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(None.instance(), function));
		return this;
	}

	public <S> Matcher<T> caze(S value, Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(new Some<>(value), function));
		return this;
	}

	public <S> Matcher<T> caze(S value, Supplier<T> supplier) {
		require(supplier != null, "supplier is null");
		cases.add(new Case<>(new Some<>(value), o -> supplier.get()));
		return this;
	}

	/**
	 * Creates a Matcher
	 * @param type Makes the compiler aware of the generic Matcher type T.
	 * @return A Matcher instance of type <code>Matcher&lt;T&gt;</code>
	 */
	public static <T> Matcher<T> of(Class<T> type) {
		return new Matcher<>();
	}
	
	/**
	 * Creates a Matcher which returns nothing, i.e. <code>Void</code>.
	 * @return A Matcher instance of type <code>Matcher&lt;Void&gt;</code>
	 */
	public static Matcher<Void> create() {
		return new Matcher<>();
	}

	@Override
	public T apply(Object obj) {
		for (Case<T> caze : cases) {
			try {
				if (caze.isApplicable(obj)) {
					return caze.apply(obj);
				}
			} catch (ClassCastException x) {
				// TODO: may also occur within f.apply if o has correct type
			} catch (NullPointerException x) {
				// TODO: may also occur within f.apply if o has correct type
			}
		}
		throw new MatchError(obj);
	}

	static class Case<T> {
		final Option<?> value;
		final Function<?, T> function;

		Case(Option<?> value, Function<?, T> function) {
			this.value = value;
			this.function = function;
		}

		/** true, if value == Some(v) && v equals obj or value == None */
		boolean isApplicable(Object obj) {
			return value.map(v -> v == obj || (v != null && v.equals(obj))).orElse(true);
		}
		
		@SuppressWarnings("unchecked")
		T apply(Object obj) {
			return ((Function<Object, T>) function).apply(obj);
		}
	}
}
