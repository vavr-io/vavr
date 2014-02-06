package io.rocketscience.java.util;


public interface Strings {

	static String repeat(String s, int times) {
		if (s == null) {
			return null;
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			builder.append(s);
		}
		return builder.toString();
	}
	
	static String escape(String s) {
		return (s == null) ? null : s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"");
	}
	
}
