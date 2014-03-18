package javaslang.util;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.collection.Arrays;
import javaslang.match.Matcher;

public final class Strings {

	/**
	 * An end of line pattern (mac/unix/win)
	 */
	public static final Pattern EOL = compile("\\r\\n|\\n|\\r");

	private Strings() {
		throw new AssertionError(Strings.class.getName() + " cannot be instantiated.");
	}

	public static String toString(Object o) {
		return toString(o, new HashSet<>());
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
	 * Computes line and column of index within s.
	 * 
	 * @param s input
	 * @param index {@code <= s.length}
	 * @return {@code new int[] { line, column }}
	 */
	public static int[] lineAndColumn(String s, int index) {
		final String documentToCursor = s.substring(0, index);
		final java.util.regex.Matcher matcher = EOL.matcher(documentToCursor);
		int line = 1;
		for (; matcher.find(); line++)
			;
		final int eol = max(documentToCursor.lastIndexOf("\r"), documentToCursor.lastIndexOf("\n"));
		final int len = documentToCursor.length();
		final int column = (len == 0) ? 1 : len - ((eol == -1) ? 0 : eol);
		return new int[] { line, column };
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
	 */
	public static <T> String mkString(T[] array, CharSequence delimiter) {
		return mkString(asList(array), delimiter);
	}

	/**
	 * Combines the elements of an array to a String using a specific delimiter, prefix and suffix.
	 * Shortcut for
	 * <code>Arrays.asList(array).stream().map(Types::toString).collect(Collectors.joining(delimiter, prefix, suffix))</code>
	 * .
	 *
	 * @param <T> Type of elements in the given array.
	 * @param array An array.
	 * @param delimiter Element delimiter.
	 * @param prefix Prefix of the result String.
	 * @param suffix Suffix of the result String.
	 * @return prefix + array[0] + delimiter + ... + array[n-1] + suffix, where n = array.length
	 */
	public static <T> String mkString(T[] array, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix) {
		return mkString(asList(array), delimiter, prefix, suffix);
	}

	/**
	 * Combines the elements of a collection to a String using a specific delimiter. Shortcut for
	 * <code>collection.stream().map(Types::toString).collect(joining(delimiter))</code>.
	 *
	 * @param <T> Type of elements in the given collection.
	 * @param collection A Collection.
	 * @param delimiter Element delimiter.
	 * @return array[0] + delimiter + ... + array[n-1], where n = array.length
	 */
	public static <T> String mkString(Collection<T> collection, CharSequence delimiter) {
		return collection.stream().map(o -> toString(o)).collect(joining(delimiter));
	}

	/**
	 * Combines the elements of an array to a String using a specific delimiter, prefix and suffix.
	 * Shortcut for
	 * <code>collection.stream().map(Types::toString).collect(joining(delimiter, prefix, suffix))</code>
	 * .
	 *
	 * @param <T> Type of elements in the given collection.
	 * @param collection A Collection.
	 * @param delimiter Element delimiter.
	 * @param prefix Prefix of the result String.
	 * @param suffix Suffix of the result String.
	 * @return prefix + array[0] + delimiter + ... + array[n-1] + suffix, where n = array.length
	 */
	public static <T> String mkString(Collection<T> collection, CharSequence delimiter,
			CharSequence prefix, CharSequence suffix) {
		return collection
				.stream()
				.map(o -> toString(o))
				.collect(joining(delimiter, prefix, suffix));
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
		require(string != null, "string is null");
		require(separator != null, "separator is null");
		require(separator.length() > 0, "separator is empty");
		final List<String> tokens = new ArrayList<String>();
		int current = 0;
		int next;
		while ((next = string.indexOf(separator, current)) != -1) {
			final String token = string.substring(current, next);
			tokens.add(token);
			current = next + separator.length();
		}
		final String token = string.substring(current);
		tokens.add(token);
		return tokens.toArray(new String[tokens.size()]);
	}

	private static String toString(Object o, Set<Object> visited) {
		if (visited.contains(o)) {
			return "...";
		} else if (o == null) {
			return "null";
		} else if (o.getClass().isArray()) {
			visited.add(o);
			final String result = getStream(o).map(x -> toString(x, visited)).collect(
					Collectors.joining(", ", "[", "]"));
			visited.remove(o);
			return result;
		} else if (o instanceof Collection) {
			visited.add(o);
			final String result = ((Collection<?>) o)
					.stream()
					.map(x -> toString(x, visited))
					.collect(Collectors.joining(", ", "[", "]"));
			visited.remove(o);
			return result;
		} else {
			return o.toString();
		}
	}

	private static final Matcher<Stream<?>> ARRAY_TO_STREAM_MATCHER = Matcher
			.<Stream<?>> create()
			.caze((boolean[] a) -> Arrays.stream(a))
			.caze((byte[] a) -> Arrays.stream(a))
			.caze((char[] a) -> Arrays.stream(a))
			.caze((double[] a) -> Arrays.stream(a))
			.caze((float[] a) -> Arrays.stream(a))
			.caze((int[] a) -> Arrays.stream(a))
			.caze((long[] a) -> Arrays.stream(a))
			.caze((short[] a) -> Arrays.stream(a))
			.caze((Object[] a) -> Arrays.stream(a));

	/**
	 * Converts a given array to a List, depending on its component type (primitive or Object).
	 * 
	 * @param o An array, not null.
	 * @return A List representation of the given array.
	 */
	private static Stream<?> getStream(Object object) {
		return ARRAY_TO_STREAM_MATCHER.apply(object);
	}

}
