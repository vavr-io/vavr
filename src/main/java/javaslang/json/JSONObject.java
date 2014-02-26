package javaslang.json;

import static javaslang.lang.Lang.require;
import static javaslang.util.Strings.escape;
import static javaslang.util.Strings.repeat;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public final class JSONObject extends JSON {

	// preserves insertion order
	private final LinkedHashMap<String, JSON> bindings;

	JSONObject() {
		this.bindings = new LinkedHashMap<String, JSON>();
	}
	
	public JSONObject bind(String key, Object value) {
		require(key != null, "key is null");
		bindings.put(key, toJSON(value));
		return this;
	}
	
	@Override
	protected String stringify(int depth, boolean isListElem) {
		final String indent1 = repeat(INDENT, depth + 1);
		final String assocs = bindings.entrySet().stream().map(entry -> {
			final String key = escape(entry.getKey());
			final String value = entry.getValue().stringify(depth + 1, false);
			return "\"" + key + "\": " + value;
		}).collect(Collectors.joining(",\n" + indent1));
		return "{" + (isListElem ? " " : "\n" + indent1) + assocs + ((depth == 0) ? "\n" : " ") + "}";
	}
	
	@Override
	public boolean isObject() { return true; }
	
	@Override
	public JSONObject asJSONObject() { return this; }

}
