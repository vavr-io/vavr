package javaslang.json;


public final class JSONNumber extends JSON {
	
	private final Number number;
	
	JSONNumber(Number number) {
		this.number = number;
	}
	
	@Override
	protected String stringify(int depth, boolean isListElem) {
		return number.toString();
	}
	
	@Override
	public boolean isNumber() { return true; }
	
	@Override
	public JSONNumber asJSONNumber() { return this; }

}
