package javaslang.json;

import static javaslang.lang.Lang.require;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class JSON {

	protected static final String INDENT = "  ";
	
	// -- JSON to String vice versa
	
	public String stringify() {
		return stringify(0, false);
	}

	protected abstract String stringify(int depth, boolean isListElem);
	
	public static JSON parse(String s) {
		// TODO
		return null;
	}
	
	// -- convenient methods

	@Override
	public int hashCode() {
		return stringify().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && getClass() == o.getClass() && stringify().equals(((JSON) o).stringify());
	}
	
	// -- reflection
	
	public boolean isNull() { return false; }
	public boolean isUndefined() { return false; }
	public boolean isBoolean() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isArray() { return false; }
	public boolean isObject() { return false; }

	// -- conversion
	
	public JSONBoolean asJSONBoolean() { throw cannotConvertTo(JSONBoolean.class); }
	public JSONNumber asJSONNumber() { throw cannotConvertTo(JSONNumber.class); }
	public JSONString asJSONString() { throw cannotConvertTo(JSONString.class); }
	public JSONArray asJSONArray() { throw cannotConvertTo(JSONArray.class); }
	public JSONObject asJSONObject() { throw cannotConvertTo(JSONObject.class); }

	private RuntimeException cannotConvertTo(Class<?> type) {
		final String message = format("Cannot convert %s to %s.", getClass().getSimpleName(), type.getSimpleName());
		return new UnsupportedOperationException(message);
	}
	
	// -- object creation
	
	public static JSON toJSON(Object o) {
		return
			(o == null) ? JSONNull :
			(o instanceof JSON) ? (JSON) o :
			(o instanceof Boolean) ? JSONBoolean((Boolean) o) :
			(o instanceof Number) ? JSONNumber((Number) o) :
			(o instanceof String) ? JSONString((String) o) :
			(o.getClass().isArray()) ? JSONArray((Object[]) o) :
			(o instanceof Collection) ? JSONArray(((Collection<?>) o).toArray()) :
			(o instanceof Map) ? JSONObject((Map<?,?>) o) :
			JSONUndefined;
	}

	public static final JSONBoolean JSONBoolean(boolean b) {
		return new JSONBoolean(b);
	}

	public static final JSONNumber JSONNumber(Number number) {
		require(number != null, "number is null");
		return new JSONNumber(number);
	}

	public static JSONString JSONString(String string) {
		require(string != null, "string is null");
		return new JSONString(string);
	}

	public static final JSONArray JSONArray(Object... elems) {
		final List<JSON> list = asList(elems).stream().map(elem -> toJSON(elem)).collect(toList());
		return new JSONArray(list);
	}
	
	public static final JSONObject JSONObject() {
		return new JSONObject();
	}

	public static final JSONObject JSONObject(Map<?,?> map) {
		return map.entrySet().stream().reduce(JSONObject(), (obj,elem) -> {
			final String key = elem.getKey().toString(); // key cannot be null
			final Object value = elem.getValue();
			return obj.bind(key, value);
		}, (o1,o2) -> o1); // o1 and o2 are identical
	}
	
	public static final JSONNull JSONNull = new JSONNull();

	public static final JSONUndefined JSONUndefined = new JSONUndefined();

}
