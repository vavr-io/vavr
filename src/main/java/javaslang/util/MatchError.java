package javaslang.util;

public class MatchError extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	final Object obj;
	
	// called by Matcher only
	MatchError(Object obj) {
		super((obj == null) ? "null" : obj.toString() + " (of class " + obj.getClass().getName() + ")");
		this.obj = obj;
	}
	
	public Object getObject() {
		return obj;
	}

}
