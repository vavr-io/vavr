/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;
import java.util.Objects;

public interface Tuple extends Serializable {

	/**
	 * Returns the number of elements of this tuple.
	 * 
	 * @return The number of elements.
	 */
	int arity();

	/**
	 * Tests if an object is equal to this Tuple.
	 * <p>
	 * An object equals this Tuple, if it is not null, of the same type and all elements are pair-wise equal calling
	 * {@link java.util.Objects#equals(Object, Object)}.
	 * 
	 * @param obj An object.
	 * @return true, if obj is equal to this in the sense described above, false otherwise.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Computes the hashCode of this Tuple by calling {@link java.util.Objects#hash(Object...)}, passing all elements of
	 * this Tuple in the order of occurrence.
	 * 
	 * @return The hashCode of this Tuple in the sense described above.
	 */
	@Override
	int hashCode();

	/**
	 * Returns the String representation of this Tuple. The result starts with '(', ends with ')' and contains the
	 * String representations of the Tuple elements in their order of occurrence, separated by ', '.
	 * 
	 * @return This Tuple as String.
	 */
	@Override
	String toString();

	// -- factory methods

	static Tuple0 empty() {
		return Tuple0.instance();
	}

	static <T> Tuple1<T> of(T t) {
		return new Tuple1<>(t);
	}

	static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
		return new Tuple2<>(t1, t2);
	}

	static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
		return new Tuple3<>(t1, t2, t3);
	}

	static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
		return new Tuple4<>(t1, t2, t3, t4);
	}

	static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
		return new Tuple5<>(t1, t2, t3, t4, t5);
	}

	static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
		return new Tuple6<>(t1, t2, t3, t4, t5, t6);
	}

	static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6,
			T7 t7) {
		return new Tuple7<>(t1, t2, t3, t4, t5, t6, t7);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(T1 t1, T2 t2, T3 t3, T4 t4,
			T5 t5, T6 t6, T7 t7, T8 t8) {
		return new Tuple8<>(t1, t2, t3, t4, t5, t6, t7, t8);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> of(T1 t1, T2 t2, T3 t3,
			T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
		return new Tuple9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> of(T1 t1, T2 t2,
			T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
		return new Tuple10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> of(
			T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
		return new Tuple11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> of(
			T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
		return new Tuple12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
	}

	static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> of(
			T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
		return new Tuple13<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
	}

	/**
	 * Implementation of an empty tuple, a tuple containing no elements.
	 */
	public static final class Tuple0 implements Tuple {

		private static final long serialVersionUID = -8715576573413569748L;

		/**
		 * The singleton instance of Tuple0.
		 */
		private static final Tuple0 INSTANCE = new Tuple0();

		/**
		 * Hidden constructor.
		 */
		private Tuple0() {
		}

		/**
		 * Returns the singleton instance of Tuple0.
		 * 
		 * @return The singleton instance of Tuple0.
		 */
		public static Tuple0 instance() {
			return INSTANCE;
		}

		@Override
		public int arity() {
			return 0;
		}

		@Override
		public boolean equals(Object o) {
			return o == this;
		}

		@Override
		public int hashCode() {
			return Objects.hash();
		}

		@Override
		public String toString() {
			return "()";
		}

		// -- Serializable implementation

		/**
		 * Instance control for object serialization.
		 * 
		 * @return The singleton instance of Tuple0.
		 * @see java.io.Serializable
		 */
		private Object readResolve() {
			return INSTANCE;
		}
	}

	/**
	 * Implementation of a single, a tuple containing 1 element.
	 */
	public static final class Tuple1<T> implements Tuple {

		private static final long serialVersionUID = -8005498887610699234L;

		public final T _1;

		public Tuple1(T t) {
			this._1 = t;
		}

		@Override
		public int arity() {
			return 1;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple1)) {
				return false;
			} else {
				final Tuple1<?> that = (Tuple1<?>) o;
				return Objects.equals(this._1, that._1);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1);
		}

		@Override
		public String toString() {
			return String.format("(%s)", _1);
		}
	}

	/**
	 * Implementation of a pair, a tuple containing 2 elements.
	 */
	public static final class Tuple2<T1, T2> implements Tuple {

		private static final long serialVersionUID = -1359843718617881431L;

		public final T1 _1;
		public final T2 _2;

		public Tuple2(T1 t1, T2 t2) {
			this._1 = t1;
			this._2 = t2;
		}

		@Override
		public int arity() {
			return 2;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple2)) {
				return false;
			} else {
				final Tuple2<?, ?> that = (Tuple2<?, ?>) o;
				return Objects.equals(this._1, that._1) && Objects.equals(this._2, that._2);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s)", _1, _2);
		}
	}

	/**
	 * Implementation of a triple, a tuple containing 3 elements.
	 */
	public static final class Tuple3<T1, T2, T3> implements Tuple {

		private static final long serialVersionUID = 1353320010987934190L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;

		public Tuple3(T1 t1, T2 t2, T3 t3) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
		}

		@Override
		public int arity() {
			return 3;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple3)) {
				return false;
			} else {
				final Tuple3<?, ?, ?> that = (Tuple3<?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s)", _1, _2, _3);
		}
	}

	/**
	 * Implementation of a quadruple, a tuple containing 4 elements.
	 */
	public static final class Tuple4<T1, T2, T3, T4> implements Tuple {

		private static final long serialVersionUID = -835853771811712181L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;

		public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
		}

		@Override
		public int arity() {
			return 4;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple4)) {
				return false;
			} else {
				final Tuple4<?, ?, ?, ?> that = (Tuple4<?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s)", _1, _2, _3, _4);
		}
	}

	/**
	 * Implementation of a quintuple, a tuple containing 5 elements.
	 */
	public static final class Tuple5<T1, T2, T3, T4, T5> implements Tuple {

		private static final long serialVersionUID = 8365094604388856720L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;

		public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
		}

		@Override
		public int arity() {
			return 5;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple5)) {
				return false;
			} else {
				final Tuple5<?, ?, ?, ?, ?> that = (Tuple5<?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s)", _1, _2, _3, _4, _5);
		}
	}

	/**
	 * Implementation of a sextuple, a tuple containing 6 elements.
	 */
	public static final class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple {

		private static final long serialVersionUID = -5282391675740552818L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;

		public Tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
		}

		@Override
		public int arity() {
			return 6;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple6)) {
				return false;
			} else {
				final Tuple6<?, ?, ?, ?, ?, ?> that = (Tuple6<?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6);
		}
	}

	/**
	 * Implementation of a septuple, a tuple containing 7 elements.
	 */
	public static final class Tuple7<T1, T2, T3, T4, T5, T6, T7> implements Tuple {

		private static final long serialVersionUID = 6913366542759921153L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;

		public Tuple7(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
		}

		@Override
		public int arity() {
			return 7;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple7)) {
				return false;
			} else {
				final Tuple7<?, ?, ?, ?, ?, ?, ?> that = (Tuple7<?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7);
		}
	}

	/**
	 * Implementation of a octuple, a tuple containing 8 elements.
	 */
	public static final class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> implements Tuple {

		private static final long serialVersionUID = 117641715065938183L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;

		public Tuple8(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
		}

		@Override
		public int arity() {
			return 8;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple8)) {
				return false;
			} else {
				final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple8<?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8);
		}
	}

	/**
	 * Implementation of a nonuple, a tuple containing 9 elements.
	 */
	public static final class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Tuple {

		private static final long serialVersionUID = -1578540921124551840L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;
		public final T9 _9;

		public Tuple9(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
			this._9 = t9;
		}

		@Override
		public int arity() {
			return 9;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple9)) {
				return false;
			} else {
				final Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8)
						&& Objects.equals(this._9, that._9);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9);
		}
	}

	/**
	 * Implementation of a decuple, a tuple containing 10 elements.
	 */
	public static final class Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Tuple {

		private static final long serialVersionUID = 7991284808329690986L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;
		public final T9 _9;
		public final T10 _10;

		public Tuple10(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
			this._9 = t9;
			this._10 = t10;
		}

		@Override
		public int arity() {
			return 10;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple10)) {
				return false;
			} else {
				final Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8)
						&& Objects.equals(this._9, that._9)
						&& Objects.equals(this._10, that._10);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
		}
	}

	/**
	 * Implementation of a undecuple, a tuple containing 11 elements.
	 */
	public static final class Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> implements Tuple {

		private static final long serialVersionUID = 3493688489700741360L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;
		public final T9 _9;
		public final T10 _10;
		public final T11 _11;

		public Tuple11(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
			this._9 = t9;
			this._10 = t10;
			this._11 = t11;
		}

		@Override
		public int arity() {
			return 11;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple11)) {
				return false;
			} else {
				final Tuple11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8)
						&& Objects.equals(this._9, that._9)
						&& Objects.equals(this._10, that._10)
						&& Objects.equals(this._11, that._11);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9,
					_10, _11);
		}
	}

	/**
	 * Implementation of a duodecuple, a tuple containing 12 elements.
	 */
	public static final class Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> implements Tuple {

		private static final long serialVersionUID = -175212910367376967L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;
		public final T9 _9;
		public final T10 _10;
		public final T11 _11;
		public final T12 _12;

		public Tuple12(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
			this._9 = t9;
			this._10 = t10;
			this._11 = t11;
			this._12 = t12;
		}

		@Override
		public int arity() {
			return 12;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple12)) {
				return false;
			} else {
				final Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8)
						&& Objects.equals(this._9, that._9)
						&& Objects.equals(this._10, that._10)
						&& Objects.equals(this._11, that._11)
						&& Objects.equals(this._12, that._12);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8,
					_9, _10, _11, _12);
		}
	}

	/**
	 * Implementation of a tredecuple, a tuple containing 13 elements.
	 */
	public static final class Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> implements Tuple {

		private static final long serialVersionUID = 2027952127515234777L;

		public final T1 _1;
		public final T2 _2;
		public final T3 _3;
		public final T4 _4;
		public final T5 _5;
		public final T6 _6;
		public final T7 _7;
		public final T8 _8;
		public final T9 _9;
		public final T10 _10;
		public final T11 _11;
		public final T12 _12;
		public final T13 _13;

		public Tuple13(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
			this._1 = t1;
			this._2 = t2;
			this._3 = t3;
			this._4 = t4;
			this._5 = t5;
			this._6 = t6;
			this._7 = t7;
			this._8 = t8;
			this._9 = t9;
			this._10 = t10;
			this._11 = t11;
			this._12 = t12;
			this._13 = t13;
		}

		@Override
		public int arity() {
			return 13;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof Tuple13)) {
				return false;
			} else {
				final Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
				return Objects.equals(this._1, that._1)
						&& Objects.equals(this._2, that._2)
						&& Objects.equals(this._3, that._3)
						&& Objects.equals(this._4, that._4)
						&& Objects.equals(this._5, that._5)
						&& Objects.equals(this._6, that._6)
						&& Objects.equals(this._7, that._7)
						&& Objects.equals(this._8, that._8)
						&& Objects.equals(this._9, that._9)
						&& Objects.equals(this._10, that._10)
						&& Objects.equals(this._11, that._11)
						&& Objects.equals(this._12, that._12)
						&& Objects.equals(this._13, that._13);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13);
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7,
					_8, _9, _10, _11, _12, _13);
		}
	}
}
