package javaslang.json;

import static javaslang.util.Strings.escape;

public final class JSONString extends JSON {

	private final String string;

	JSONString(String string) {
		this.string = string;
	}

	@Override
	protected String stringify(int depth, boolean isListElem) {
		return '"' + escape(string) + '"';
	}
	
	@Override
	public boolean isString() { return true; }
	
	@Override
	public JSONString asJSONString() { return this; }

}
