package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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

	public <S> Matcher<T> caze(S prototype, Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(new Some<>(prototype), function));
		return this;
	}

	// TODO: does this method make sense?
	public <S> Matcher<T> caze(S prototype, Consumer<S> consumer) {
		require(consumer != null, "supplier is null");
		cases.add(new Case<>(new Some<>(prototype), (S o) -> { consumer.accept(o); return null; }));
		return this;
	}
	
	public <S> Matcher<T> caze(S prototype, Supplier<T> supplier) {
		require(supplier != null, "supplier is null");
		cases.add(new Case<>(new Some<>(prototype), o -> supplier.get()));
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
		final Option<?> prototype;
		final Function<?, T> function;

		Case(Option<?> prototype, Function<?, T> function) {
			this.prototype = prototype;
			this.function = function;
		}

		/** prototype := Some(value). isApplicable == true <=> value equals obj or prototype == None */
		boolean isApplicable(Object obj) {
			return prototype.map(v -> v == obj || (v != null && v.equals(obj))).orElse(true);
		}
		
		@SuppressWarnings("unchecked")
		T apply(Object obj) {
			return ((Function<Object, T>) function).apply(obj);
		}
	}
}
