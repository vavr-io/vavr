/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.lang.invoke.MethodType;

import javaslang.Require;
import javaslang.Tuple;
import javaslang.Lambda.λ1;
import javaslang.Tuple.Tuple1;
import javaslang.Tuple.Tuple10;
import javaslang.Tuple.Tuple11;
import javaslang.Tuple.Tuple12;
import javaslang.Tuple.Tuple13;
import javaslang.Tuple.Tuple2;
import javaslang.Tuple.Tuple3;
import javaslang.Tuple.Tuple4;
import javaslang.Tuple.Tuple5;
import javaslang.Tuple.Tuple6;
import javaslang.Tuple.Tuple7;
import javaslang.Tuple.Tuple8;
import javaslang.Tuple.Tuple9;
import javaslang.monad.Option;

/**
 * Represents a Pattern for pattern matching. In this context, Pattern matching is defined as
 * 
 * <ol>
 * <li>Perform check, if an object is applicable to the pattern.</li>
 * <li>If the pattern is applicable, decompose the object.</li>
 * <li>The pattern matches, if the prototype equals the decomposition result.</li>
 * <li>After a match, the typed object and the decomposition result of the object can be further processed.</li>
 * </ol>
 *
 * <strong>Note:</strong> Please not, that the equals check not necessary needs equal component types. I.e. the
 * prototype could contain components, which equal any object.
 * 
 * <strong>Note:</strong> The decomposition can return arbitrary, refined results depending or not depending on the
 * underlying object. With this property, code of further processing can be moved to the decomposition.
 *
 * @param <T> Object type to be decomposed.
 * @param <P> Type of prototype tuple containing components that will be compared with the decomposition result.
 * @param <R> Type of decomposition result.
 */
public class Pattern<T, P extends Tuple, R extends Tuple> {

	private final Class<T> decompositionType;
	private final λ1<T, R> decomposition;
	private final P prototype;

	/**
	 * Constructs a Pattern.
	 * 
	 * @param decompositionType
	 * @param decomposition
	 * @param prototype
	 */
	private Pattern(Class<T> decompositionType, λ1<T, R> decomposition, P prototype) {
		this.decompositionType = decompositionType;
		this.decomposition = decomposition;
		this.prototype = prototype;
	}

	/**
	 * Checks, if an object can be decomposed by this pattern.
	 * 
	 * @param obj An object to be tested.
	 * @return true, if the given object is not null and assignable to the decomposition type of this pattern.
	 */
	public boolean isApplicable(Object obj) {
		return obj != null && decompositionType.isAssignableFrom(obj.getClass());
	}

	/**
	 * Pattern matches the given object.
	 * 
	 * @param obj An object to be pattern matched.
	 * @return {@code Some(typedObject, decompositionOfTypedObject)}, if the prototype of this pattern equals the
	 *         decomposition result, otherwise {@code None}.
	 */
	@SuppressWarnings("unchecked")
	public Option<Tuple2<T, R>> apply(Object obj) {
		final T t = (T) obj;
		final R components = decomposition.apply(t);
		if (prototype.equals(components)) {
			return Option.of(Tuple.of(t, components));
		} else {
			return Option.none();
		}
	}

	/**
	 * Creates a Pattern for objects of type T. The Pattern matches a given object o, if {@code isApplicable(o)} returns
	 * true and {@code prototype.equals(decomposition.apply(o))} returns true.
	 * 
	 * @param <T> Object type to be decomposed.
	 * @param <P1> Type of component 1 of prototype.
	 * @param <R1> Type of component 1 of decomposition result.
	 * @param decomposition A Decomposition for objects of type T.
	 * @param prototype The prototype for comparision with the decomposition result.
	 * @return {@code Some(typedObject, decompositionOfTypedObject)} if the Pattern matches, otherwise {@code None}.
	 */
	public static <T, P1, R1> Pattern<T, Tuple1<P1>, Tuple1<R1>> of(λ1<T, Tuple1<R1>> decomposition,
			Tuple1<P1> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, R1, R2> Pattern<T, Tuple2<P1, P2>, Tuple2<R1, R2>> of(
			λ1<T, Tuple2<R1, R2>> decomposition, Tuple2<P1, P2> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, R1, R2, R3> Pattern<T, Tuple3<P1, P2, P3>, Tuple3<R1, R2, R3>> of(
			λ1<T, Tuple3<R1, R2, R3>> decomposition, Tuple3<P1, P2, P3> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, R1, R2, R3, R4> Pattern<T, Tuple4<P1, P2, P3, P4>, Tuple4<R1, R2, R3, R4>> of(
			λ1<T, Tuple4<R1, R2, R3, R4>> decomposition, Tuple4<P1, P2, P3, P4> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, R1, R2, R3, R4, R5> Pattern<T, Tuple5<P1, P2, P3, P4, P5>, Tuple5<R1, R2, R3, R4, R5>> of(
			λ1<T, Tuple5<R1, R2, R3, R4, R5>> decomposition, Tuple5<P1, P2, P3, P4, P5> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, R1, R2, R3, R4, R5, R6> Pattern<T, Tuple6<P1, P2, P3, P4, P5, P6>, Tuple6<R1, R2, R3, R4, R5, R6>> of(
			λ1<T, Tuple6<R1, R2, R3, R4, R5, R6>> decomposition, Tuple6<P1, P2, P3, P4, P5, P6> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, R1, R2, R3, R4, R5, R6, R7> Pattern<T, Tuple7<P1, P2, P3, P4, P5, P6, P7>, Tuple7<R1, R2, R3, R4, R5, R6, R7>> of(
			λ1<T, Tuple7<R1, R2, R3, R4, R5, R6, R7>> decomposition, Tuple7<P1, P2, P3, P4, P5, P6, P7> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, R1, R2, R3, R4, R5, R6, R7, R8> Pattern<T, Tuple8<P1, P2, P3, P4, P5, P6, P7, P8>, Tuple8<R1, R2, R3, R4, R5, R6, R7, R8>> of(
			λ1<T, Tuple8<R1, R2, R3, R4, R5, R6, R7, R8>> decomposition,
			Tuple8<P1, P2, P3, P4, P5, P6, P7, P8> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, P9, R1, R2, R3, R4, R5, R6, R7, R8, R9> Pattern<T, Tuple9<P1, P2, P3, P4, P5, P6, P7, P8, P9>, Tuple9<R1, R2, R3, R4, R5, R6, R7, R8, R9>> of(
			λ1<T, Tuple9<R1, R2, R3, R4, R5, R6, R7, R8, R9>> decomposition,
			Tuple9<P1, P2, P3, P4, P5, P6, P7, P8, P9> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Pattern<T, Tuple10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>, Tuple10<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10>> of(
			λ1<T, Tuple10<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10>> decomposition,
			Tuple10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11> Pattern<T, Tuple11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11>, Tuple11<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11>> of(
			λ1<T, Tuple11<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11>> decomposition,
			Tuple11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12> Pattern<T, Tuple12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12>, Tuple12<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12>> of(
			λ1<T, Tuple12<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12>> decomposition,
			Tuple12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> prototype) {
		return create(decomposition, prototype);
	}

	public static <T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13> Pattern<T, Tuple13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13>, Tuple13<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13>> of(
			λ1<T, Tuple13<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13>> decomposition,
			Tuple13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> prototype) {
		return create(decomposition, prototype);
	}

	private static <T, P extends Tuple, R extends Tuple> Pattern<T, P, R> create(λ1<T, R> decomposition, P prototype) {
		Require.nonNull(decomposition, "decomposition is null");
		Require.nonNull(prototype, "prototype is null");

		final MethodType methodType = decomposition.getType();
		@SuppressWarnings("unchecked")
		final Class<T> type = (Class<T>) methodType.parameterType(methodType.parameterCount() - 1);

		return new Pattern<>(type, decomposition, prototype);
	}
}
