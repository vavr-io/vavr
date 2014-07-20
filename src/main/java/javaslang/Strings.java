/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static javaslang.Lang.require;
import static javaslang.Lang.requireNonNull;
import static javaslang.Lang.requireNotInstantiable;
import static javaslang.Lang.requireNotNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.Tuples.Tuple2;

/**
 * Extension methods for {@link java.lang.String}.
 */
public final class Strings {

	/**
	 * An end of line pattern (mac/unix/win)
	 */
	public static final Pattern EOL = compile("\\r\\n|\\n|\\r");

	/**
	 * Needed to detect indirect loops on recursive calls of {@link #toString(Object)}.
	 */
	private static final ThreadLocal<Set<Object>> toStringVisited = ThreadLocal
			.withInitial(HashSet::new);

	/**
	 * This class is not intended to be instantiated.
	 */
	private Strings() {
		requireNotInstantiable();
	}

	/**
	 * Returns a deep string representation of an Object.
	 * 
	 * @param o An Object.
	 * @return A deep string representation of o.
	 */
	public static String toString(Object o) {
		final Set<Object> visited = toStringVisited.get();
		final boolean isEntryPoint = visited.isEmpty();
		try {
			return toString(o, visited);
		} finally {
			if (isEntryPoint) {
				visited.clear();
			}
		}
	}

	/**
	 * Duplicate a string n times.
	 * 
	 * @param s A String
	 * @param n Duplication count, may be negative or zero.
	 * @return A string, s duplicated n times or null, if s is null. If n is negative or zero, the
	 *         empty string is returned.
	 */
	public static String repeat(String s, int n) {
		if (s == null) {
			return null;
		} else if (n <= 0) {
			return "";
		} else {
			final StringBuilder builder = new StringBuilder();
			for (int i = 0; i < n; i++) {
				builder.append(s);
			}
			return builder.toString();
		}
	}

	/**
	 * Duplicates a char n times.
	 * 
	 * @param c A char.
	 * @param n A count of spaces, may be negative or zero.
	 * @return A string, c duplicated n times. If n is negative or zero, the empty string is
	 *         returned.
	 */
	public static String repeat(char c, int n) {
		if (n <= 0) {
			return "";
		} else {
			final char[] buf = new char[n];
			fill(buf, ' ');
			return String.valueOf(buf);
		}
	}

	/**
	 * Escape backslash '\' and double quote '"'.
	 * 
	 * @param s A string.
	 * @return Escaped string or null if s is null.
	 */
	public static String escape(String s) {
		return (s == null) ? null : s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"");
	}

	/**
	 * Escapes occurrences of character within s with the given escape character. The escape
	 * character is also escaped.
	 * 
	 * @param s The String to be escaped.
	 * @param character The character to be escaped.
	 * @param escape The escape character.
	 * @return The escaped String.
	 */
	public static String escape(String s, char character, char escape) {
		return s.chars().mapToObj(i -> {
			final char c = (char) i;
			return (c == character || c == escape) ? ("" + escape + c) : ("" + c);
		}).collect(Collectors.joining());
	}

	/**
	 * Computes line and column of index within s.
	 * 
	 * @param s input
	 * @param index {@code <= s.length}
	 * @return {@code new int[] line, column }
	 */
	public static Tuple2<Integer, Integer> lineAndColumn(String s, int index) {
		final String documentToCursor = s.substring(0, index);
		final java.util.regex.Matcher matcher = EOL.matcher(documentToCursor);
		int line = 1;
		for (; matcher.find(); line++)
			;
		final int eol = max(documentToCursor.lastIndexOf("\r"), documentToCursor.lastIndexOf("\n"));
		final int len = documentToCursor.length();
		final int column = (len == 0) ? 1 : len - ((eol == -1) ? 0 : eol);
		return Tuples.of(line, column);
	}

	/**
	 * Tests if given String is null or empty.
	 * 
	 * @param s A String
	 * @return true, if s is null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || "".equals(s);
	}

	/**
	 * Combines the elements of an array to a String using a specific delimiter. Shortcut for
	 * <code>Arrays.asList(array).stream().map(Types::toString).collect(Collectors.joining(delimiter))</code>
	 * .
	 *
	 * @param <T> Type of elements in the given array.
	 * @param array An array.
	 * @param delimiter Element delimiter.
	 * @return array[0] + delimiter + ... + array[n-1], where n = array.length
	 * 
	 * @throws NullPointerException If array is null.
	 */
	public static <T> String join(T[] array, CharSequence delimiter) {
		requireNonNull(array, "array is null");
		return join(asList(array), delimiter);
	}

	/**
	 * Combines the elements of an array to a String using a specific delimiter, prefix and suffix.
	 *
	 * @param <T> Type of elements in the given array.
	 * @param array An array.
	 * @param delimiter Element delimiter.
	 * @param prefix Prefix of the result String.
	 * @param suffix Suffix of the result String.
	 * @return prefix + array[0] + delimiter + ... + array[n-1] + suffix, where n = array.length
	 * 
	 * @throws NullPointerException If array is null.
	 */
	public static <T> String join(T[] array, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix) {
		requireNonNull(array, "array is null");
		return join(asList(array), delimiter, prefix, suffix);
	}

	/**
	 * Combines the elements of a collection to a String using a specific delimiter.
	 *
	 * @param <T> Type of elements in the given collection.
	 * @param collection A Collection.
	 * @param delimiter Element delimiter.
	 * @return array[0] + delimiter + ... + array[n-1], where n = array.length
	 * 
	 * @throws NullPointerException If collection is null.
	 */
	public static <T> String join(Collection<T> collection, CharSequence delimiter) {
		requireNonNull(collection, "collection is null");
		return collection.stream().map(o -> toString(o)).collect(joining(delimiter));
	}

	/**
	 * Combines the elements of a Collection to a String using a specific delimiter, prefix and
	 * suffix. Shortcut for
	 * <code>collection.stream().map(Types::toString).collect(joining(delimiter, prefix, suffix))</code>
	 * .
	 *
	 * @param <T> Type of elements in the given collection.
	 * @param collection A Collection.
	 * @param delimiter Element delimiter.
	 * @param prefix Prefix of the result String.
	 * @param suffix Suffix of the result String.
	 * @return prefix + array[0] + delimiter + ... + array[n-1] + suffix, where n = array.length
	 * 
	 * @throws NullPointerException If collection is null.
	 */
	public static <T> String join(Collection<T> collection, CharSequence delimiter,
			CharSequence prefix, CharSequence suffix) {
		requireNonNull(collection, "collection is null");
		return collection
				.stream()
				.map(o -> toString(o))
				.collect(joining(delimiter, prefix, suffix));
	}

	/**
	 * Concatenates the given strings using the given separator character. Occurrences of escape or
	 * separator within a string are escaped with the given escape character.
	 * 
	 * @param strings An array of Strings, not null.
	 * @param separator A separator character.
	 * @param escape An escape character.
	 * @return The concatenation of the escaped strings using the given separator and escape
	 *         character.
	 * 
	 * @throws IllegalArgumentException If separator equals escape.
	 * @throws NullPointerException If strings is null.
	 */
	public static String join(String[] strings, char separator, char escape) {
		requireNonNull(strings, "strings is null");
		require(separator != escape, "separator equals escape charater");
		return Stream
				.of(strings)
				.map(s -> escape(s, separator, escape))
				.collect(Collectors.joining(String.valueOf(separator)));
	}

	/**
	 * Convenience method that calls {@link #join(String[], char, char)} by converting the given
	 * Collection strings to a String array.
	 * 
	 * @param strings A Collection of Strings, not null.
	 * @param separator A separator character.
	 * @param escape An escape character.
	 * @return The concatenation of the escaped strings using the given separator and escape
	 *         character.
	 * 
	 * @throws IllegalArgumentException If separator equals escape.
	 * @throws NullPointerException If strings is null.
	 */
	public static String join(Collection<String> strings, char separator, char escape) {
		requireNonNull(strings, "strings is null");
		require(separator != escape, "separator equals escape charater");
		return strings
				.stream()
				.map(s -> escape(s, separator, escape))
				.collect(Collectors.joining(String.valueOf(separator)));
	}

	/**
	 * Splits a string using a specific separator. By definition, a separator separates two string.
	 * This leads to the following:
	 * 
	 * <ul>
	 * <li>split("", "#") = [""]</li>
	 * <li>split("#", "#") = ["", ""]</li>
	 * <li>split("##", "#") = ["", "" ,""]</li>
	 * <li>split("123##456", "#") = ["123", "" ,"456"]</li>
	 * <li>split("123##456", "##") = ["123", "456"]</li>
	 * <li>split("#123#", "#") = ["", "123", ""]</li>
	 * </ul>
	 * 
	 * @param string A string to be splitted, not null.
	 * @param separator A token separator, not null and not empty.
	 * @return The tokens contained in string, without separators.
	 */
	public static String[] split(String string, String separator) {
		requireNonNull(string, "string is null");
		requireNotNullOrEmpty(separator, "separator is empty");
		final List<String> tokens = new ArrayList<String>();
		int fromIndex = 0;
		int index;
		while ((index = string.indexOf(separator, fromIndex)) != -1) {
			final String finding = string.substring(fromIndex, index);
			tokens.add(finding);
			fromIndex = index + separator.length();
		}
		final String rest = string.substring(fromIndex);
		tokens.add(rest);
		return tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * Splits a string which consists of zero or more parts concatenated with the given separator.
	 * Occurrences of the separator in the given string are assumed to be escaped with the given
	 * escape character. The escape character itself is also assumed to be escaped.
	 * 
	 * @param string A string consisting of zero or more parts concatenated with the given
	 *            separator.
	 * @param separator A separator character.
	 * @param escape An escape character.
	 * @return An array of unescaped parts of the given string.
	 * 
	 * @throws IllegalArgumentException If separator equals escape.
	 * @throws NullPointerException If strings is null.
	 */
	public static String[] split(String string, char separator, char escape) {
		requireNonNull(string, "string is null");
		require(separator != escape, "separator equals escape charater");
		final List<String> tokens = new ArrayList<String>();
		final StringBuilder buf = new StringBuilder();
		int fromIndex = 0;
		int index = nextIndex(string, separator, escape, fromIndex);
		while (index != -1) {
			final String original = string.substring(fromIndex, index);
			buf.append(original);
			final char finding = string.charAt(index);
			if (finding == escape) {
				final char unescaped = string.charAt(index + 1);
				buf.append(unescaped);
				fromIndex = index + 2;
			} else { // finding == separator
				tokens.add(buf.toString());
				buf.setLength(0);
				fromIndex = index + 1;
			}
			index = nextIndex(string, separator, escape, fromIndex);
		}
		final String rest = string.substring(fromIndex);
		buf.append(rest);
		tokens.add(buf.toString());
		return tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * Computes the index of the first occurrence of separator or escape within s starting at
	 * position fromIndex.
	 * <p>
	 * Returns -1 only, and only if both of separator and escape are not found.
	 *
	 * @param s A String.
	 * @param separator A char.
	 * @param escape A char.
	 * @param fromIndex A start index.
	 * @return The index of the first occurrence of a separator or a escape charater within s.
	 */
	private static int nextIndex(String s, char separator, char escape, int fromIndex) {
		final int index1 = s.indexOf(separator, fromIndex);
		final int index2 = s.indexOf(escape, fromIndex);
		return (index1 == -1) ? index2 : (index2 == -1) ? index1 : Math.min(index1, index2);
	}

	private static String toString(Object o, Set<Object> visited) {
		if (o == null) {
			return "null";
		} else if (visited.contains(o)) {
			return "...";
		} else if (o instanceof CharSequence) {
			return '"' + o.toString() + '"';
		} else if (o instanceof Class) {
			return toString((Class<?>) o, 0);
		} else if (o.getClass().isArray()) {
			return toString(Arrayz.toStream(o), ", ", "[", "]", visited, o);
		} else if (o instanceof Set) {
			return toString(((Set<?>) o).stream(), ", ", "{", "}", visited, o);
		} else if (o instanceof Collection) {
			return toString(((Collection<?>) o).stream(), ", ", "(", ")", visited, o);
		} else {
			return o.toString();
		}
	}

	private static String toString(Class<?> clazz, int dimension) {
		if (clazz.isArray()) {
			return toString(clazz.getComponentType(), dimension + 1);
		} else {
			return clazz.getName() + repeat("[]", dimension);
		}
	}

	private static String toString(Stream<?> stream, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix, Set<Object> visited, Object o) {
		visited.add(o);
		final String result = stream.map(x -> toString(x, visited)).collect(
				Collectors.joining(delimiter, prefix, suffix));
		visited.remove(o);
		return result;
	}

}
