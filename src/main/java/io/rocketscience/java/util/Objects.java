package io.rocketscience.java.util;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.joining;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// TODO: refactor this class and move methods to Strings, Arrays, Collections etc.
public final class Objects {
	
	private Objects() {
        throw new AssertionError(Objects.class.getName() + " cannot be instantiated.");
    }

	/**
	 * Converts an Object o to a Boolean according to these rules:
	 * 
	 * <ul>
	 * <li>null -&gt; null</li>
	 * <li>Boolean b -&gt; b</li>
	 * <li>Number n -&gt; n.intValue() != 0</li>
	 * <li>String s -&gt; Boolean.valueOf((String) o) ? true : toBigDecimalOption((String) o).map(n -> !ZERO.equals(n)).orElse(null)</li>
	 * <li>Object[] arr -&gt; toBoolean(unbox(arr))</li>
	 * <li>Collection c -&gt; toBoolean(unbox(c))</li>
	 * <li>Optional o -&gt; toBoolean(unbox(o))</li>
	 * <li>Object o -&gt; toBoolean(o.toString())</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return A Boolean representation of o or null.
	 */
	public static Boolean toBoolean(Object o) {
		return
				(o == null) ? null :
				(o instanceof Boolean) ? (Boolean) o :
				(o instanceof Number) ? ((Number) o).intValue() != 0 :
				(o instanceof String) ? Boolean.valueOf((String) o) ? true : toBigDecimalOption((String) o).map(n -> !ZERO.equals(n)).orElse(null) :
				(o.getClass().isArray()) ? toBoolean(unbox(arrayToList(o))) :
				(o instanceof Collection) ? toBoolean(unbox((Collection<?>) o)) :
				(o instanceof Optional) ? toBoolean(unbox((Optional<?>) o)) :
				toBoolean(o.toString());
	}

	/**
	 * Converts an Object o to an Optional&lt;Boolean&gt; by calling <code>Optional.ofNullable(toBoolean(o))</code>.
	 * 
	 * @param o An Object
	 * @return An Optional&lt;Boolean&gt; representation of o.
	 */
	public static Optional<Boolean> toBooleanOption(Object o) {
		return Optional.ofNullable(toBoolean(o));
	}
	
	/**
	 * Converts an Object o to a Number according to these rules:
	 * 
	 * <ul>
	 * <li>null -&gt; null</li>
	 * <li>Boolean b -&gt; b ? 1 : 0</li>
	 * <li>Number n -&gt; n</li>
	 * <li>String s -&gt; toNumber(s) <em>(calling {@link #toNumber(String)})</em></li>
	 * <li>Object[] arr -&gt; toNumber(unbox(arr)) <em>(calling {@link #toNumber(Object)})</em></li>
	 * <li>Collection c -&gt; toNumber(unbox(c)) <em>(calling {@link #toNumber(Object)})</em></li>
	 * <li>Optional o -&gt; toNumber(unbox(o)) <em>(calling {@link #toNumber(Object)})</em></li>
	 * <li>Object o -&gt; toNumber(o.toString()) <em>(calling {@link #toNumber(String)})</em></li>
	 * </ul>
	 * 
	 * The returned {@link java.lang.Number} may be of type {@link java.lang.Integer}, {@link java.lang.Double} or
	 * {@link java.math.BigDecimal}.
	 * 
	 * @param o An Object
	 * @return A Number representation of o or null.
	 */
	public static Number toNumber(Object o) {
		return
				(o == null) ? null :
				(o instanceof Boolean) ? ((Boolean) o) ? 1 : 0 :
				(o instanceof Number) ? (Number) o :
				(o instanceof String) ? toNumber((String) o) :
				(o.getClass().isArray()) ? toNumber(unbox(arrayToList(o))) :
				(o instanceof Collection) ? toNumber(unbox((Collection<?>) o)) :
				(o instanceof Optional) ? toNumber(unbox((Optional<?>) o)) :
				toNumber(o.toString());
	}
	
	/**
	 * Converts an Object o to an Optional&lt;Number&gt; by calling <code>Optional.ofNullable(toNumber(o))</code>.
	 * 
	 * @param o An Object
	 * @return An Optional&lt;Number&gt; representation of o.
	 */
	public static Optional<Number> toNumberOption(Object o) {
		return Optional.ofNullable(toNumber(o));
	}
	
	/**
	 * Converts a String to a Number according to these rules:
	 * 
	 * <ul>
	 * <li>null -&gt; null</li>
	 * <li>s is Integer -&gt; toInt(s)</li>
	 * <li>s is Double -&gt; toDouble(s)</li>
	 * <li>s is BigDecimal -&gt; toBigDecimal(s)</li>
	 * <li>otherwise null</em></li>
	 * </ul>
	 * 
	 * The returned {@link java.lang.Number} may be of type {@link java.lang.Integer}, {@link java.lang.Double} or
	 * {@link java.math.BigDecimal}.
	 * 
	 * @param o An Object
	 * @return A Number representation of o or null.
	 */
	public static Number toNumber(String s) {
		if (s == null) {
			return null;
		} else {
			final Integer i = toInt(s);
			if (i != null) {
				return i;
			} else {
				final Double d = toDouble(s);
				return (d != null) ? d : toBigDecimal(s);
			}
		}
	}
	
	/**
	 * Converts a String to an Optional&lt;Number&gt; by calling <code>Optional.ofNullable(toNumber(s))</code>.
	 * 
	 * @param s A String
	 * @return An Optional&lt;Number&gt; representation of s.
	 */
	public static Optional<Number> toNumberOption(String s) {
		return Optional.ofNullable(toNumber(s));
	}

	/**
	 * Given a String s, the result of {@link Integer#parseInteger(String)} is returned.
	 * If s is null or a NumberFormatException occurs, null is returned.
	 * 
	 * @param s A String
	 * @return An Integer representation of s.
	 */
	public static Integer toInt(String s) {
		try {
			return (s == null) ? null : Integer.parseInt(s);
		} catch(Exception x) {
			return null;
		}
	}
	
	/**
	 * Converts a String to an Optional&lt;Integer&gt; by calling <code>Optional.ofNullable(toInteger(s))</code>.
	 * 
	 * @param s A String
	 * @return An Optional&lt;Integer&gt; representation of s.
	 */
	public static Optional<Integer> toIntOption(String s) {
		return Optional.ofNullable(toInt(s));
	}
	
	/**
	 * Given a String s, the result of {@link Double#parseDouble(String)} is returned.
	 * If s is null or a NumberFormatException occurs, null is returned.
	 * 
	 * @param s A String
	 * @return A Double representation of s.
	 */
	public static Double toDouble(String s) {
		try {
			return (s == null) ? null : Double.parseDouble(s);
		} catch(Exception x) {
			return null;
		}
	}
	
	/**
	 * Converts a String to an Optional&lt;Double&gt; by calling <code>Optional.ofNullable(toDouble(s))</code>.
	 * 
	 * @param s A String
	 * @return An Optional&lt;Double&gt; representation of s.
	 */
	public static Optional<Double> toDoubleOption(String s) {
		return Optional.ofNullable(toDouble(s));
	}
	
	/**
	 * Given a String s, the result of new BigDecimal(s) is returned.
	 * If s is null or a NumberFormatException occurs, null is returned.
	 * 
	 * @param s A String
	 * @return A BigDecimal representation of s.
	 */
	public static BigDecimal toBigDecimal(String s) {
		try {
			return (s == null) ? null : new BigDecimal(s);
		} catch(Exception x) {
			return null;
		}
	}
	
	/**
	 * Converts a String to an Optional&lt;BigDecimal&gt; by calling <code>Optional.ofNullable(toBigDecimal(s))</code>.
	 * 
	 * @param s A String
	 * @return An Optional&lt;BigDecimal&gt; representation of s.
	 */
	public static Optional<BigDecimal> toBigDecimalOption(String s) {
		return Optional.ofNullable(toBigDecimal(s));
	}
	
	/**
	 * Returns the single element of the Collection c or null, if it is null, empty or has more than one element.
	 * 
	 * @param c A Collection
	 * @return The single element contained in c or null.
	 */
	public static Object unbox(Collection<?> c) {
		return (c != null && c.size() == 1) ? c.iterator().next() : null;
	}
	
	/**
	 * Returns the single element of the Optional o or null, if it is empty or null.
	 * 
	 * @param o An Optional
	 * @return The single element contained in o or null.
	 */
	public static Object unbox(Optional<?> o) {
		return (o == null) ? null : o.orElse(null);
	}
	
	/**
	 * Returns the single element of the Array arr or null, if it is null, empty or has more than one element.
	 * 
	 * @param arr An Array
	 * @return The single element contained in arr or null.
	 */
	public static Object unbox(Object[] arr) {
		return (arr != null && arr.length == 1) ? arr[0] : null;
	}
	
	// TODO: DELME - just a test - use toString(Object) instead
	public static String toString(int[] ints) {
		return Arrays.asList(ints).stream().map(String::valueOf).collect(joining(", ", "[", "]"));
	}
	
	// TODO: javadoc
	// TODO: detect cycles => private String toString(Object o, Set<Object> visited)
	public static String toString(Object o) {
		System.out.println("## " + o);
		return
				(o == null) ? null :
				(o instanceof Boolean || o instanceof Number) ? o.toString() :
				(o instanceof String) ? (String) o :
				(o.getClass().isArray()) ? arrayToList(o).stream().map(Objects::toString).collect(joining(", ", "[", "]")) : 
				(o instanceof Collection) ? ((Collection<?>) o).stream().map(Objects::toString).collect(joining(", ", "[", "]")) :
				(o instanceof Optional) ? toString(((Optional<?>) o).orElse(null)) :
				o.toString();
	}
	
	// TODO: javadoc
	public static List<?> toList(Object o) {
		return
				(o == null) ? null :
				(o.getClass().isArray()) ? arrayToList(o) : 
				(o instanceof Collection) ? new ArrayList<Object>((Collection<?>) o) :
				/** TODO: remove cast to (Object) (see {@link https://bugs.eclipse.org/bugs/show_bug.cgi?id=427223}) */
				(o instanceof Optional) ? Arrays.asList((Object) ((Optional<?>) o).orElse(null)) :
				Arrays.asList(o);
	}
	
	// TODO: javadoc
	private static List<?> arrayToList(Object o) {
		    final Class<?> type = o.getClass().getComponentType();
		    return Arrays.asList(
		    		type.isPrimitive() ?
		    				boolean.class.isAssignableFrom(type) ? (boolean[]) o :
		    				byte.class.isAssignableFrom(type)    ? (byte[])    o :
		    				char.class.isAssignableFrom(type)    ? (char[])    o :
		    				double.class.isAssignableFrom(type)  ? (double[])  o :
		    				float.class.isAssignableFrom(type)   ? (float[])   o :
		    				int.class.isAssignableFrom(type)     ? (int[])     o :
		    				long.class.isAssignableFrom(type)    ? (long[])    o :
		    				short.class.isAssignableFrom(type)   ? (short[])   o :
		    				new Object[] {} : (Object[]) o);
	}
}
