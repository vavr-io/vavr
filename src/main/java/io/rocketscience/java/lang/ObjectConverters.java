package io.rocketscience.java.lang;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: handle dates (o instanceof Date and asISODate(o) instanceof Date)
public interface ObjectConverters {

	/**
	 * Converts an Object o to a Boolean according to these rules:
	 * <ul>
	 * <li>Null and Boolean values are passed back.</li>
	 * <li>Number != 0 maps to true, false otherwise.</li>
	 * <li>String that equals "true" (ignoring case) or String number != 0 map to true, false otherwise.</li>
	 * <li>Collection maps to the first value (if exists) according to these rules, false otherwise.</li>
	 * <li>Optional maps to the Boolean representation of the underlying value or null.</li>
	 * <li>Other Objects map to true, if toString() is a Boolean according to these rules.</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return A Boolean representation of o or null.
	 */
	static Boolean asBoolean(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Boolean) {
			return (Boolean) o;
		} else if (o instanceof Number) {
			return ((Number) o).intValue() != 0;
		} else if (o instanceof String) {
			final String s = (String) o;
			if (Boolean.valueOf(s)) {
				return TRUE;
			} else {
				try {
					return new BigDecimal(s).intValue() != 0;
				} catch (NumberFormatException x) {
					return FALSE;
				}
			}
		} else if (o instanceof Collection) {
			final Iterator<?> iterator = ((Collection<?>) o).iterator();
			return iterator.hasNext() ? asBoolean(iterator.next()) : FALSE;
		} else if (o instanceof Optional) {
			final Optional<?> optional = (Optional<?>) o;
			return optional.isPresent() ? asBoolean(optional.get()) : null;
		} else {
			return asBoolean(o.toString());
		}
	}

	/**
	 * Converts an Object o to an Integer according to these rules:
	 * <ul>
	 * <li>Null values are passed back.</li>
	 * <li>Boolean maps to 1 (true) and 0 (false).</li>
	 * <li>Number maps to intValue().</li>
	 * <li>String is converted to BigDecimal and mapped to intValue(). If the given String cannot be converted to a
	 * BigDecimal, null is returned.</li>
	 * <li>Collection maps to the Integer representation of the first value (if exists). Empty Collection maps to null.</li>
	 * <li>Optional maps to the Integer representation of the underlying value or null.</li>
	 * <li>Other Objects map to the Integer representation of toString(), according to these rules.</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return An Integer representation of o or null.
	 */
	static Integer asInt(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Boolean) {
			return ((boolean) o) ? 1 : 0;
		} else if (o instanceof Number) {
			return ((Number) o).intValue();
		} else if (o instanceof String) {
			final String s = (String) o;
			try {
				return new BigDecimal(s).intValue();
			} catch (NumberFormatException x) {
				return null;
			}
		} else if (o instanceof Collection) {
			final Iterator<?> iterator = ((Collection<?>) o).iterator();
			return iterator.hasNext() ? asInt(iterator.next()) : null;
		} else if (o instanceof Optional) {
			final Optional<?> optional = (Optional<?>) o;
			return optional.isPresent() ? asInt(optional.get()) : null;
		} else {
			return asInt(o.toString());
		}
	}

	/**
	 * Converts an Object o to a Double according to these rules:
	 * <ul>
	 * <li>Null values are passed back.</li>
	 * <li>Boolean maps to 1.0d (true) and 0.0d (false).</li>
	 * <li>Number maps to doubleValue().</li>
	 * <li>String is converted to BigDecimal and mapped to doubleValue(). If the given String cannot be converted to a
	 * BigDecimal, null is returned.</li>
	 * <li>Collection maps to the Double representation of the first value (if exists). Empty Collection maps to null.</li>
	 * <li>Optional maps to the Double representation of the underlying value or null.</li>
	 * <li>Other Objects map to the Double representation of toString(), according to these rules.</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return A Double representation of o or null.
	 */
	static Double asDouble(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Boolean) {
			return ((boolean) o) ? 1.0d : 0.0d;
		} else if (o instanceof Number) {
			return ((Number) o).doubleValue();
		} else if (o instanceof String) {
			final String s = (String) o;
			try {
				return new BigDecimal(s).doubleValue();
			} catch (NumberFormatException x) {
				return null;
			}
		} else if (o instanceof Collection) {
			final Iterator<?> iterator = ((Collection<?>) o).iterator();
			return iterator.hasNext() ? asDouble(iterator.next()) : null;
		} else if (o instanceof Optional) {
			final Optional<?> optional = (Optional<?>) o;
			return optional.isPresent() ? asDouble(optional.get()) : null;
		} else {
			return asDouble(o.toString());
		}
	}

	/**
	 * Converts an Object o to a String according to these rules:
	 * <ul>
	 * <li>Null and String values are passed back.</li>
	 * <li>Boolean and Number are mapped to their toString() value.</li>
	 * <li>Collection o1, o2, ... maps to "[asString(o1), asString(o2), ...]", empty Collction maps to "[]".</li>
	 * <li>Optional maps to the String representation of the underlying value or null.</li>
	 * <li>Other Objects are mapped to toString().</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return A String representation of o or null.
	 */
	static String asString(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Boolean || o instanceof Number) {
			return o.toString();
		} else if (o instanceof Collection) {
			return ((Collection<?>) o).stream().map(item -> asString(item)).collect(Collectors.joining(", ", "[", "]"));
		} else if (o instanceof Optional) {
			final Optional<?> optional = (Optional<?>) o;
			return optional.isPresent() ? asString(optional.get()) : null;
		} else {
			return o.toString();
		}
	}

	/**
	 * Converts an Object o to a List according to these rules:
	 * <ul>
	 * <li>Null and List are passed back.</li>
	 * <li>Boolean and Number are mapped to their toString() value.</li>
	 * <li>Collection maps to a List of same elements.</li>
	 * <li>Optional value is wrapped in a List if present or maps to null.</li>
	 * <li>Other Objects are wrapped into single-value Lists.</li>
	 * </ul>
	 * 
	 * @param o An Object
	 * @return A List representation of o or null.
	 */
	// TODO: List of Lists
	static List<?> asList(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof List) {
			return (List<?>) o;
		} else if (o instanceof Collection) {
			return new ArrayList<>((Collection<?>) o);
		} else if (o instanceof Optional) {
			final Optional<?> optional = (Optional<?>) o;
			return optional.isPresent() ? asList(optional.get()) : null;
		} else {
			return asList(o);
		}
	}

	/**
	 * Applies the given mapper to all items of the result of asList(o).
	 * 
	 * @param o An object.
	 * @param mapper A type converter.
	 * @return A List containing items of type T.
	 */
	static <T> List<T> asList(Object o, Function<Object, T> mapper) {
		return asList(o).stream().map(mapper).collect(toList());
	}

}
