package javaslang.json;

public final class JSONUndefined extends JSON {

	JSONUndefined() {
	}

	@Override
	protected String stringify(int depth, boolean isListElem) {
		return "undefined";
	}
	
	@Override
	public boolean isUndefined() { return true; }
	
}
