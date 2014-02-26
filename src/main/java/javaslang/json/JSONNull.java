package javaslang.json;

public final class JSONNull extends JSON {

	JSONNull() {
	}

	@Override
	protected String stringify(int depth, boolean isListElem) {
		return "null";
	}

	@Override
	public boolean isNull() { return true; }
	
}
