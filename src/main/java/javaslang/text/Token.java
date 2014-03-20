package javaslang.text;

public class Token {

	final String text;
	int start;
	int end;
	
	Token(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}
	
	public String get() {
		return text.substring(start, end);
	}
	
	@Override
	public String toString() {
		return "Token('" + get() + "')";
	}	

}
