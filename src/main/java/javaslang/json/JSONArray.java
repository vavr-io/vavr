package javaslang.json;

import static javaslang.util.Strings.repeat;
import static java.util.stream.Collectors.joining;

import java.util.List;

public final class JSONArray extends JSON {

	private final List<JSON> list;

	JSONArray(List<JSON> list) {
		this.list = list;
	}

	@Override
	protected String stringify(int depth, boolean isListElem) {
		final boolean isComplex = list.stream().reduce(false,
				(b, elem) -> b || elem instanceof JSONArray || elem instanceof JSONObject, (b1, b2) -> b1 || b2);
		final String indent0 = repeat(INDENT, depth);
		final String indent1 = indent0 + INDENT;
		final String prefix = isComplex ? "[\n" + indent1 : "[ ";
		final String delimiter = isComplex ? ",\n" + indent1 : ", ";
		final String suffix = isComplex ? "\n" + indent0 + "]" : " ]";
		final String elements = list.stream().map(elem -> elem.stringify(depth + 1, true)).collect(joining(delimiter));
		return prefix + elements + suffix;
	}
	
	@Override
	public boolean isArray() { return true; }
	
	@Override
	public JSONArray asJSONArray() { return this; }

}
