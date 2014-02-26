package javaslang.json;

public final class JSONBoolean extends JSON {

	private final boolean b;

	JSONBoolean(boolean b) {
		this.b = b;
	}

	@Override
	protected String stringify(int depth, boolean isListElem) {
		return String.valueOf(b);
	}
	
	@Override
	public boolean isBoolean() { return true; }
	
	@Override
	public JSONBoolean asJSONBoolean() { return this; }

}
